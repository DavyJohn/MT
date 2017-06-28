package com.zzh.mt.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.FileCallBack;
import com.zzh.mt.R;
import com.zzh.mt.adapter.MaterialPlAdapter;
import com.zzh.mt.adapter.MaterialsAdapter;
import com.zzh.mt.base.BaseActivity;
import com.zzh.mt.base.MyApplication;
import com.zzh.mt.download.DownloadManager;
import com.zzh.mt.http.callback.SpotsCallBack;
import com.zzh.mt.mode.CoursewareById;
import com.zzh.mt.sql.MyProvider;
import com.zzh.mt.utils.CommonUtil;
import com.zzh.mt.utils.Contants;
import com.zzh.mt.utils.MdTools;
import com.zzh.mt.utils.NetworkUtils;
import com.zzh.mt.utils.SqliteTool;
import com.zzh.mt.widget.DividerItemDecoration;
import com.zzh.mt.widget.HorizontalProgressBarWithNumber;

import org.xutils.ex.DbException;

import java.io.File;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

import static android.R.attr.data;
import static android.R.attr.id;
import static android.R.attr.inAnimation;
import static com.pgyersdk.views.b.p;

/**
 * Created by 腾翔信息 on 2017/6/12.
 */

public class MaterialsTwoActivity extends BaseActivity  {
    private static final String TAG  = MaterialsActivity.class.getSimpleName();
    private LinkedList<CoursewareById.CoursewareByIdData> list = new LinkedList<>();
    private LinkedList<CoursewareById.CoursewareByIdData> listtwo = new LinkedList<>();
    private LinkedList<Integer> postions = new LinkedList<>();
    private LinkedList<String> urllist = new LinkedList<>();
    private int sizes = 0;//用它来存放多少条数据
    private int sqlsize = 0;
    LinkedList<String> ids = new LinkedList<>();
    MenuItem item = null;
    private AlertDialog dialog;
    MaterialsAdapter adapter ;
    MaterialPlAdapter mPladapter;
    @BindView(R.id.materials_piliang_recycler)
    RecyclerView mPlRecycler;
    @BindView(R.id.materials_recycler)
    RecyclerView mRecycler;
    @BindView(R.id.all_check)
    TextView mTextCheck;
    @BindView(R.id.downing)
    TextView mTextDown;

    @OnClick(R.id.all_check) void check(){
        if (mTextCheck.getText().toString().equals(getString(R.string.all_check))){
            mTextCheck.setText(R.string.cancel_all);
            postions.clear();
            urllist.clear();
            listtwo.clear();
            ids.clear();
            listtwo.addAll(list);
            //todo 去掉数据库里面存在的id
            for (int i=0;i<list.size();i++){
                postions.add(i);
            }
            Cursor cursor = getContentResolver().query(MyProvider.URI,null,null,null,null);
            while (cursor.moveToNext()){
                ids.add(cursor.getString(cursor.getColumnIndex("url")));
            }
            for (int i=0;i<postions.size();i++){
                if (ids.contains(listtwo.get(i).getId())){
                    postions.remove(i);
                    listtwo.remove(i);
                    i--;
                }

            }
            for (int m=0; m<postions.size();m++){
                urllist.add(list.get(postions.get(m)).getCoursewareUrl());
            }
            // todo全选 psotions
            initpl();
        }else {
            mTextCheck.setText(getString(R.string.all_check));
            postions.clear();
            urllist.clear();
            initpl();
        }
    }
    @OnClick(R.id.downing) void down(){
        if (NetworkUtils.isWifi(mContext)){
            //是wifi
            toDown();
        }else {
            //手机流量 提示
            dialog = new AlertDialog.Builder(mContext)
                    .setTitle(R.string.tips)
                    .setMessage(R.string.care_wifi)
                    .setPositiveButton(getString(R.string.sure), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            toDown();
                        }
                    })
                    .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create();
            dialog.show();
        }
    }

    private void toDown(){
        if (urllist.size()>0){
            for (int i=0;i<urllist.size();i++){
                String url = urllist.get(i);
                String name = CommonUtil.getData()+list.get(postions.get(i)).getCoursewareName();
                try {
                    DownloadManager.getInstance().startDownload(url,
                            name,
                            list.get(postions.get(i)).getId(),
                            list.get(postions.get(i)).getCoursewareType(),
                            String.valueOf(mContext.getExternalCacheDir()+list.get(postions.get(i)).getCoursewareName()+list.get(postions.get(i)).getCoursewareType()),
                            true,
                            false,null);
                } catch (DbException e) {
                    e.printStackTrace();
                }
            }
            startActivity(new Intent(mContext,DownloadActivity.class));
            postions.clear();
        }else {

        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getToolBar().setTitle(R.string.Course_materials);
        MyApplication.getInstance().add(this);
        mRecycler.setLayoutManager(new LinearLayoutManager(this));
        mRecycler.setHasFixedSize(true);
        mRecycler.addItemDecoration(new DividerItemDecoration(mContext,DividerItemDecoration.VERTICAL_LIST));
        mPlRecycler.setLayoutManager(new LinearLayoutManager(this));
        mPlRecycler.setHasFixedSize(true);
        mPlRecycler.addItemDecoration(new DividerItemDecoration(mContext,DividerItemDecoration.VERTICAL_LIST));
        getInfo();
    }

    private void initview(){

        mTextDown.setText(getString(R.string.Download));
        mTextCheck.setText(getString(R.string.all_check));
        mRecycler.setVisibility(View.VISIBLE);
        mPlRecycler.setVisibility(View.GONE);
        adapter = new MaterialsAdapter(mContext);
        adapter.addData(list);
        mRecycler.setAdapter(adapter);
        adapter.setOnClickItemListener(new MaterialsAdapter.OnClickItemListener() {
            @RequiresApi(api = Build.VERSION_CODES.FROYO)
            @Override
            public void onClickItem(HorizontalProgressBarWithNumber progress, TextView mSize, TextView down, String id, final int postion) {
                if (!down.getText().toString().equals(getString(R.string.Finished))){
                    if (NetworkUtils.isWifi(mContext)){
                        //是wifi
                        try {
                            DownloadManager.getInstance().startDownload(
                                    list.get(postion).getCoursewareUrl()
                                    ,CommonUtil.getData()+list.get(postion).getCoursewareName()+list.get(postion).getCoursewareType()
                                    ,list.get(postion).getId()
                                    ,list.get(postion).getCoursewareType()
                                    ,String.valueOf(mContext.getExternalCacheDir()+list.get(postion).getCoursewareName()+list.get(postion).getCoursewareType())
                                    ,true
                                    ,false
                                    ,null);
                        } catch (DbException e) {
                            e.printStackTrace();
                        }
                        startActivity(new Intent(mContext,DownloadActivity.class));
                        postions.clear();
                    }else {
                        //手机流量 提示
                        dialog = new AlertDialog.Builder(mContext)
                                .setTitle(R.string.tips)
                                .setMessage(R.string.care_wifi)
                                .setPositiveButton(getString(R.string.sure), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        try {
                                            DownloadManager.getInstance().startDownload(
                                                    list.get(postion).getCoursewareUrl()
                                                    ,CommonUtil.getData()+list.get(postion).getCoursewareName()+list.get(postion).getCoursewareType()
                                                    ,list.get(postion).getId()
                                                    ,list.get(postion).getCoursewareType()
                                                    ,String.valueOf(mContext.getExternalCacheDir()+list.get(postion).getCoursewareName()+list.get(postion).getCoursewareType())
                                                    ,true
                                                    ,false
                                                    ,null);
                                        } catch (DbException e) {
                                            e.printStackTrace();
                                        }
                                        startActivity(new Intent(mContext,DownloadActivity.class));
                                        postions.clear();
                                    }
                                })
                                .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                })
                                .create();
                        dialog.show();
                    }

                }else {
                    startActivity(new Intent(mContext,DownloadActivity.class));
                    postions.clear();
                }
            }
        });

    }

    private void initpl(){
        mRecycler.setVisibility(View.GONE);
        mPlRecycler.setVisibility(View.VISIBLE);
        mPladapter = new MaterialPlAdapter(mContext);
        mPladapter.addData(list);
        mPladapter.addpostion(postions);
        mPlRecycler.setAdapter(mPladapter);
        mPladapter.setOnClickItemListener(new MaterialPlAdapter.OnClickItemListener() {
            @Override
            public void onClickItem(HorizontalProgressBarWithNumber view, int postion) {
                HashSet<Integer> set = new LinkedHashSet<Integer>();
                set.addAll(postions);
                postions.clear();
                postions.addAll(set);
                if (postions.contains(postion)){
                    LinkedList<Integer> re = new LinkedList<Integer>();
                    re.addAll(postions);
                    postions.clear();
                    for (int i=0;i<re.size();i++){
                        if (re.get(i) == postion){
                        }else {
                            postions.add(re.get(i));
                        }
                    }
                    urllist.clear();
                    for (int i=0;i<postions.size();i++){
                        urllist.add(list.get(postions.get(i)).getCoursewareUrl());
                    }
                }else {
                    postions.add(postion);
                    urllist.clear();
                    for (int i=0;i<postions.size();i++){
                        urllist.add(list.get(postions.get(i)).getCoursewareUrl());
                    }
                }
                //todo 获得 postions
                //利用总条数来实现全选的变化
                Cursor cursor = getContentResolver().query(MyProvider.URI,null,null,null,null);
                sqlsize = 0;//初始化
                while (cursor.moveToNext()){
                    //拿到 数据库存放的数据条数
                    sqlsize = sqlsize+1;
                }
                System.out.print(sqlsize);
                System.out.print(postions);
                if (postions.size()+sqlsize == sizes){
                    mTextCheck.setText(R.string.cancel_all);
                }else {
                    mTextCheck.setText(getString(R.string.all_check));
                }
                initpl();
            }
        });

    }
    @Override
    public int getLayoutId() {
        return R.layout.materials_main_layout;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.materials_menu,menu);
        item = menu.findItem(R.id.down_list);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuitem) {
        if (item.getTitle().equals(getString(R.string.MultiSelect))){
            item.setTitle(R.string.cancel);
            //切换 显示底部
            findViewById(R.id.materials_bottom_layout).setVisibility(View.VISIBLE);
            initpl();
        }else {
            item.setTitle(R.string.MultiSelect);
            //切换
            urllist.clear();
            postions.clear();
            findViewById(R.id.materials_bottom_layout).setVisibility(View.GONE);
            initview();
        }
        return super.onOptionsItemSelected(item);
    }

    private void getInfo(){
        LinkedHashMap<String,String> map = new LinkedHashMap<>();
        map.put("courseId",getIntent().getStringExtra("courseId"));
        map.put("appVersion", CommonUtil.getVersion(mContext));
        map.put("ostype","android");
        map.put("uuid",CommonUtil.android_id(mContext));
        map.put("digest", MdTools.sign_digest(map));
        mOkHttpHelper.post(mContext, Contants.BASEURL + Contants.getCoursewareById, map, TAG, new SpotsCallBack<CoursewareById>(mContext) {
            @Override
            public void onSuccess(Response response, CoursewareById data) {
                if (data.getCode().equals("200")){
                    list.clear();
                    list.addAll(data.getFileList());
                    //获取存放条数
                    sizes = list.size();
                    initview();
                }else if (data.getCode().equals("110")){
                    goBack(data.getMessage(),mContext);
                }else {
                    showMessageDialog(data.getMessage(),mContext);
                }
            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (item.getTitle().equals(getString(R.string.MultiSelect))){
            initview();
        }else {
            initpl();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}