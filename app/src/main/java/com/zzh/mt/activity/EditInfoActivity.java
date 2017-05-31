package com.zzh.mt.activity;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.squareup.picasso.Picasso;
import com.zhy.http.okhttp.callback.StringCallback;
import com.zzh.mt.R;
import com.zzh.mt.base.BaseActivity;
import com.zzh.mt.base.MyApplication;
import com.zzh.mt.utils.TimeUtil;
import com.zzh.mt.widget.CircleImageView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by 腾翔信息 on 2017/5/25.
 */

public class EditInfoActivity extends BaseActivity implements ActivityCompat.OnRequestPermissionsResultCallback{

    private static final String TAG = EditInfoActivity.class.getSimpleName();
    private String path = null;
    Dialog mCameraDialog;
    private Uri destinationUri;
    private String imageFilePath;
    private int isFirst  = 0 ;
    private static final int TAKE_PHOTO =  1;
    private static final int REQUEST_CODE_IMAGE = 2;
    @BindView(R.id.edit_nickname)
    EditText mNick;
    @BindView(R.id.edit_brand)
    EditText mBrand;
    @BindView(R.id.edit_deparname)
    EditText mDeparname;
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
    }
    @BindView(R.id.edit_info_header)
    CircleImageView mImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getToolBar().setTitle("编辑资料");
        MyApplication.getInstance().add(this);
        initview();
    }

    private void initview(){
        path = getIntent().getStringExtra("headurl");
        mNick.setText(getIntent().getStringExtra("nickname"));
        mBrand.setText(getIntent().getStringExtra("brandname"));
        mDeparname.setText(getIntent().getStringExtra("deparname"));
        Picasso.with(mContext).load(R.drawable.imag_demo).placeholder(R.drawable.imag_demo).error(R.drawable.imag_demo).into(mImage);
    }
    //保存
    private void s(){

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

    }

    @Override
    public int getLayoutId() {
        return R.layout.edit_info_layout;
    }
}
