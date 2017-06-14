package com.zzh.mt.activity;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.soundcloud.android.crop.Crop;
import com.squareup.picasso.Picasso;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;
import com.zhy.http.okhttp.callback.StringCallback;
import com.zzh.mt.R;
import com.zzh.mt.base.BaseActivity;
import com.zzh.mt.base.MyApplication;
import com.zzh.mt.http.callback.SpotsCallBack;
import com.zzh.mt.mode.BaseData;
import com.zzh.mt.mode.UpLoadData;
import com.zzh.mt.utils.CommonUtil;
import com.zzh.mt.utils.Contants;
import com.zzh.mt.utils.SharedPreferencesUtil;
import com.zzh.mt.utils.TimeUtil;
import com.zzh.mt.widget.CircleImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by 腾翔信息 on 2017/5/25.
 */

public class EditInfoActivity extends BaseActivity implements ActivityCompat.OnRequestPermissionsResultCallback{

    private static final String TAG = EditInfoActivity.class.getSimpleName();
    private String path = null;
    Dialog mCameraDialog;
    File file = null;
    private Uri destinationUri;
    private String imageFilePath;
    private int isFirst  = 0 ;
    private static final int TAKE_PHOTO =  1;
    private static final int REQUEST_CODE_IMAGE = 2;
    @BindView(R.id.edit_nickname)
    EditText mNick;
    @BindView(R.id.edit_brand)
    EditText mBrand;
    @BindView(R.id.edit_info_header)
    CircleImageView mImage;
    @BindView(R.id.text_deparname)
    TextView mDeparname;
    @OnClick(R.id.deparname_layout) void click(){
        startActivity(new Intent(mContext,DeparNameActivity.class));
    }
    @OnClick(R.id.edit_info_image) void image(){
        //开启相机相册
        mCameraDialog = new Dialog(mContext, R.style.my_dialog);
        LinearLayout root = (LinearLayout) LayoutInflater.from(mContext).inflate(
                R.layout.layout_camera_control, null);
        root.findViewById(R.id.btn_open_camera).setOnClickListener(btnlistener);
        root.findViewById(R.id.btn_choose_img).setOnClickListener(btnlistener);
        root.findViewById(R.id.btn_cancel).setOnClickListener(btnlistener);
        mCameraDialog.setContentView(root);
        Window dialogWindow = mCameraDialog.getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM);
        dialogWindow.setWindowAnimations(R.style.dialogstyle); // 添加动画
        WindowManager.LayoutParams lp = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        lp.x = 0; // 新位置X坐标
        lp.y = -20; // 新位置Y坐标
        lp.width = (int) getResources().getDisplayMetrics().widthPixels; // 宽度
        root.measure(0, 0);
        lp.height = root.getMeasuredHeight();
        lp.alpha = 9f; // 透明度
        dialogWindow.setAttributes(lp);
        mCameraDialog.show();
    }
    @OnClick(R.id.edit_save) void save(){
        //先上传再保存
        if (TextUtils.isEmpty(mNick.getText().toString())){
            showToast("昵称不能为空！");
        }else if (TextUtils.isEmpty(mBrand.getText().toString())){
            showToast("品牌不能为空！");
        }else {
           if (file == null){
               s(getIntent().getStringExtra("headurl"));
           }else {
               upPhoto(file);
           }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getToolBar().setTitle(getString(R.string.Edit_personal_profile));
        MyApplication.getInstance().add(this);
        CommonUtil.moveCursor2End(mNick);
        CommonUtil.moveCursor2End(mBrand);
        initview();
    }

    private void initview(){
        path = getIntent().getStringExtra("headurl");
        mNick.setText(getIntent().getStringExtra("nickname"));

        mBrand.setText(getIntent().getStringExtra("brandname"));
        if (getIntent().getStringExtra("sex").equals("1")){
            Picasso.with(mContext).load(path).placeholder(R.drawable.image_b).error(R.drawable.image_b).into(mImage);
        }else {
            Picasso.with(mContext).load(path).placeholder(R.drawable.image_g).error(R.drawable.image_g).into(mImage);
        }
    }
    //保存
    private void s(String path){
        LinkedHashMap<String,String> map = new LinkedHashMap<>();
        map.put("brandName",mBrand.getText().toString());
        map.put("departmentId",Contants.Deparmentid);
        map.put("headImageUrl",path);
        map.put("nickname",mNick.getText().toString());
        map.put("userId",SharedPreferencesUtil.getInstance(mContext).getString("userid"));
        mOkHttpHelper.post(mContext, Contants.BASEURL + Contants.CHANGEINFO, map, TAG, new SpotsCallBack<BaseData>(mContext) {
            @Override
            public void onSuccess(Response response, BaseData data) {
                if (data.getCode().equals("200")){
                    Contants.Deparmentname = null;
                    Contants.Deparmentid = null;
                    finish();
                    showToast(data.getMessage());
                }else {
                    showMessageDialog(data.getMessage(),mContext);
                }

            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });
    }
    private void dispatchTakePictureIntent() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        TAKE_PHOTO);
            }else {
                isFirst = 1;
            }
        }else {//只适用24以下
            isFirst = 1;
        }
        if (isFirst == 1 ){
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            takePictureIntent.putExtra("android.intent.extras.CAMERA_FACING", 1);
            if (takePictureIntent.resolveActivity(getPackageManager())!= null){
                File photoFile = null;
                try {
                    photoFile = createImageFile();//创建临时图片文件
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                if ( photoFile!= null){
                    Uri photoURI = Uri.fromFile(photoFile);
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                            photoURI);
                    startActivityForResult(takePictureIntent, TAKE_PHOTO);
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if ( grantResults[0] == PackageManager.PERMISSION_GRANTED){
            isFirst = 1;
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getPackageManager())!= null){
                File photoFile = null;
                try {
                    photoFile = createImageFile();//创建临时图片文件
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                if ( photoFile!= null){
//                    Uri photoURI = FileProvider.getUriForFile(mContext, ProviderUtil.getFileProviderName(mContext),photoFile);
                    Uri photoURI = Uri.fromFile(photoFile);
                    takePictureIntent.putExtra(
                            "android.intent.extras.CAMERA_FACING",
                            Camera.CameraInfo.CAMERA_FACING_FRONT);
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                            photoURI);
                    startActivityForResult(takePictureIntent, TAKE_PHOTO);
                }
            }
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = mContext.getExternalFilesDir(
                Environment.DIRECTORY_PICTURES);
//        File storageDir = Environment.getExternalStoragePublicDirectory(
//                Environment.DIRECTORY_DCIM);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        imageFilePath = image.getAbsolutePath();
        return image;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        mCameraDialog.dismiss();

        if (requestCode == TAKE_PHOTO && resultCode == RESULT_OK) {
            Uri uri = Uri.fromFile(new File(imageFilePath));
            Crop.of(uri,destinationUri).asSquare().start(this);
        }else if (requestCode == Crop.REQUEST_CROP){
            if (data != null){
                path = CommonUtil.getRealFilePath(mContext,Crop.getOutput(data));
                file = new File(path);

                Uri uri = Uri.fromFile(new File(path));
                mImage.setImageURI(uri);

            }
        }else if (requestCode == Crop.RESULT_ERROR){
            showToast(Crop.getError(data).getMessage());
        }else if (requestCode == REQUEST_CODE_IMAGE&& resultCode == RESULT_OK){
            if (data != null){
                Uri uri = data.getData();
                Cursor cursor = mContext.getContentResolver().query(uri,null,null,null,null);
                if (cursor != null && cursor.moveToFirst()){
                    String path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));
                    Uri chooseuri = Uri.fromFile(new File(path));
                    //TODO 压缩就报空
                    Uri inputuri = Uri.fromFile(scal(chooseuri,path));
                    Crop.of(inputuri,destinationUri).asSquare().start(this);
                }
            }

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    //压缩图片
    public  File scal(Uri fileUri,String imagepath){
        String path = fileUri.getPath();
        if (!TextUtils.isEmpty(path)){

        }
        File outputFile = new File(path);
        long fileSize = outputFile.length();
        final long fileMaxSize = 100 * 1024;
        if (fileSize >= fileMaxSize) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(path, options);
            int height = options.outHeight;
            int width = options.outWidth;
            double scale = Math.sqrt((float) fileSize / fileMaxSize);
            options.outHeight = (int) (height / scale);
            options.outWidth = (int) (width / scale);
            options.inSampleSize = (int) (scale + 0.5);
            options.inJustDecodeBounds = false;

            Bitmap bitmap = BitmapFactory.decodeFile(path, options);
            outputFile = new File(imagepath);//问题出在这 imagepath 不能写死
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(outputFile);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, fos);
                fos.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            //TODO 某些时候会报出异常但是可能不会影响app
            if (!bitmap.isRecycled()) {
                bitmap.recycle();
            }else{
                File tempFile = outputFile;
                outputFile = new File(imagepath);//问题出在这 imagepath 不能写死
                copyFileUsingFileChannels(tempFile, outputFile);
            }

        }
        return outputFile;

    }
    public static void copyFileUsingFileChannels(File source, File dest){
        FileChannel inputChannel = null;
        FileChannel outputChannel = null;
        try {
            try {
                inputChannel = new FileInputStream(source).getChannel();
                outputChannel = new FileOutputStream(dest).getChannel();
                outputChannel.transferFrom(inputChannel, 0, inputChannel.size());
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } finally {
            try {
                inputChannel.close();
                outputChannel.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
    private View.OnClickListener btnlistener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_open_camera: // 打开照相机
                    String state = Environment.getExternalStorageState(); //拿到sdcard是否可用的状态码
                    if (state.equals(Environment.MEDIA_MOUNTED)){   //如果可用
                        dispatchTakePictureIntent();
                    }else {
                        showToast("sdcard不可用");
                    }
                    break;
                // 打开相册
                case R.id.btn_choose_img:
                    Intent chooseImg = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(chooseImg,REQUEST_CODE_IMAGE);
                    break;
                // 取消
                case R.id.btn_cancel:
                    if (mCameraDialog != null) {
                        mCameraDialog.dismiss();
                    }
                    break;
            }
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        destinationUri = Uri.fromFile(new File(getCacheDir(), TimeUtil.getData()+"cropimage"));
        if (Contants.Deparmentname != null && !TextUtils.isEmpty(Contants.Deparmentname)){
            mDeparname.setText(Contants.Deparmentname);
        }

    }

    private void upPhoto(File file){
        LinkedHashMap<String,String> map = new LinkedHashMap<>();
        map.put("appVersion", CommonUtil.getVersion(mContext));
        map.put("digest","");
        map.put("ostype","android");
        map.put("uuid",CommonUtil.android_id(mContext));
        map.put("userId", SharedPreferencesUtil.getInstance(mContext).getString("userid"));
        OkHttpUtils.post().addFile("file","android_head_image.png",file)
                .url(Contants.BASEURL+Contants.UPLOAD).params(map).build().execute(new HeadCallBack() {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(UpLoadData response, int id) {
                if (response.getCode().equals("200")){
                    if (getIntent().getStringExtra("sex").equals("1")){
                        Picasso.with(mContext).load(response.getHeadImageUrl()).placeholder(R.drawable.image_b).error(R.drawable.image_b).into(mImage);
                    }else {
                        Picasso.with(mContext).load(response.getHeadImageUrl()).placeholder(R.drawable.image_g).error(R.drawable.image_g).into(mImage);
                    }
                    s(response.getHeadImageUrl());
                }
            }
        });

    }
    private abstract  class HeadCallBack extends Callback<UpLoadData> {
        @Override
        public UpLoadData parseNetworkResponse(Response response, int id) throws Exception {
            String string = response.body().string();
            UpLoadData data = new Gson().fromJson(string,UpLoadData.class);
            return data;
        }

    }
    @Override
    public int getLayoutId() {
        return R.layout.edit_info_layout;
    }
}
