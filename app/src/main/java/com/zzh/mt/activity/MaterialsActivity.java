package com.zzh.mt.activity;


import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.FileCallBack;
import com.zhy.http.okhttp.callback.StringCallback;
import com.zzh.mt.R;
import com.zzh.mt.adapter.MaterialPlAdapter;
import com.zzh.mt.adapter.MaterialsAdapter;
import com.zzh.mt.base.BaseActivity;
import com.zzh.mt.base.CommonAdapter;
import com.zzh.mt.base.MyApplication;
import com.zzh.mt.base.ViewHolder;
import com.zzh.mt.http.callback.SpotsCallBack;
import com.zzh.mt.mode.CoursewareById;
import com.zzh.mt.sql.MyProvider;
import com.zzh.mt.utils.CommonUtil;
import com.zzh.mt.utils.Contants;
import com.zzh.mt.utils.SqliteTool;
import com.zzh.mt.widget.DividerItemDecoration;
import com.zzh.mt.widget.HorizontalProgressBarWithNumber;

import org.w3c.dom.Text;

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

/**
 * Created by 腾翔信息 on 2017/5/24.
 */

public class MaterialsActivity extends BaseActivity implements SearchView.OnQueryTextListener {
    private static final String TAG  = MaterialsActivity.class.getSimpleName();
    private LinkedList<CoursewareById.CoursewareByIdData> list = new LinkedList<>();
    private LinkedList<Integer> postions = new LinkedList<>();
    private LinkedList<String> urllist = new LinkedList<>();
    private Cursor cursor;
    private AlertDialog dialog;
    private int success = 1;
    private TextView mTitle;
    private HorizontalProgressBarWithNumber progressbar;
    MenuItem item = null;
    private int num;
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


//    @BindView(R.id.searchview_root)
//    LinearLayout root;
//    @BindView(R.id.search_materials)
//    SearchView mSearch;
    @OnClick(R.id.all_check) void check(){
        if (mTextCheck.getText().toString().equals("全选")){
            mTextCheck.setText("取消");
            postions.clear();
            urllist.clear();
            for (int i=0;i<list.size();i++){
                postions.add(i);
                urllist.add(list.get(i).getCoursewareUrl());
            }
            initpl();
        }else {
            mTextCheck.setText("全选");
            postions.clear();
            urllist.clear();
            initpl();
        }
    }
    @OnClick(R.id.downing) void down(){

        if (mTextDown.getText().toString().equals(getString(R.string.Download))){

            //将url 整合
            urllist.clear();
            if (postions.size() == 0){
                mTextDown.setText(getString(R.string.Download));
            }else {
                for (int i=0;i<postions.size();i++){
                    urllist.add(list.get(postions.get(i)).getCoursewareUrl());
                }
                mTextDown.setText("正在下载");
                //xiazai
                View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_view_main_layout,null);
                dialog = new AlertDialog.Builder(mContext)
                        .setTitle("下载")
                        .setView(view)
                        .create();
                dialog.show();
                for ( int m=0;m<urllist.size();m++){
                        num = m;
                        mTitle = (TextView) view.findViewById(R.id.dialog_title);
                        mTitle.setText(list.get(postions.get(m)).getCoursewareName());
                        progressbar = (HorizontalProgressBarWithNumber) view.findViewById(R.id.dialog_progress);
                        final String urlid = list.get(postions.get(m)).getId();
                        OkHttpUtils
                                .get()
                                .url(urllist.get(m))
                                .build()
                                .execute(new FileCallBack(Environment.getExternalStorageDirectory().getAbsolutePath(),list.get(postions.get(m)).getCoursewareName()) {
                                    @Override
                                    public void onError(Call call, Exception e, int id) {
                                        Log.e(TAG, "onError :" + e.getMessage());
                                        mTextDown.setText("下载失败");
                                    }

                                    @Override
                                    public void inProgress(float progress, long total, int id) {
                                        progressbar.setProgress((int)(100*progress));
                                        System.out.print(progress);
                                    }

                                    @Override
                                    public void onResponse(File response, int id) {
                                        showToast("文件下载至："+response.getAbsolutePath());
                                        SqliteTool.getInstance().addData(mContext,urlid);
                                        mTextDown.setText(getString(R.string.Download));
                                        initpl();
                                        if (urllist.size() == num+1){
                                            urllist.clear();
                                        }
                                        dialog.dismiss();
                                        success = 1;
                                    }

                                });
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }

        }else {
            mTextDown.setText(getString(R.string.Download));
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

//        mSearch.clearFocus();
//        mSearch.setOnQueryTextListener(this);
//        mSearch.onActionViewExpanded();
//        mSearch.setIconifiedByDefault(false);
        mRecycler.setVisibility(View.VISIBLE);
        mPlRecycler.setVisibility(View.GONE);
        adapter = new MaterialsAdapter(mContext);
        adapter.addData(list);
        mRecycler.setAdapter(adapter);
        adapter.setOnClickItemListener(new MaterialsAdapter.OnClickItemListener() {
            @Override
            public void onClickItem(HorizontalProgressBarWithNumber progress, TextView mSize,TextView down, String id, int postion) {
                if (!down.getText().toString().equals("已下载")){
                    progress.setVisibility(View.VISIBLE);
                    mSize.setVisibility(View.GONE);
                    downlist(progress,mSize,down,list.get(postion).getCoursewareUrl(),id,list.get(postion).getCoursewareName());
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
//                progressbar = view;
                HashSet<Integer> set = new LinkedHashSet<Integer>();
                set.addAll(postions);
                postions.clear();
                postions.addAll(set);
                if (postions.contains(postion)){
                    LinkedList<Integer> re = new LinkedList<Integer>();
                    re.addAll(postions);
                    postions.clear();
//                    Iterator it = postions.iterator();
                    //去掉 重复的postion再删除
                    for (int i=0;i<re.size();i++){
                        if (re.get(i) == postion){

                        }else {
                            postions.add(re.get(i));
                            System.out.print(postions);
                        }
                    }
                }else {
                    postions.add(postion);
                    System.out.print(postions);
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
    public boolean onQueryTextSubmit(String query) {
        showToast(query);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {

        return true;
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
            findViewById(R.id.materials_bottom_layout).setVisibility(View.GONE);
            initview();
        }
        return super.onOptionsItemSelected(item);
    }

    private void getInfo(){
        LinkedHashMap<String,String> map = new LinkedHashMap<>();
        map.put("courseId",getIntent().getStringExtra("courseId"));
        map.put("appVersion", CommonUtil.getVersion(mContext));
        map.put("digest","");
        map.put("ostype","android");
        map.put("uuid",CommonUtil.android_id(mContext));
        mOkHttpHelper.post(mContext, Contants.BASEURL + Contants.getCoursewareById, map, TAG, new SpotsCallBack<CoursewareById>(mContext) {
            @Override
            public void onSuccess(Response response, CoursewareById data) {
                if (data.getCode().equals("200")){
                    list.clear();
                    list.addAll(data.getFileList());
                    initview();
                }else {
                    showMessageDialog(data.getMessage(),mContext);
                }
            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });

    }

    private void downlist(final HorizontalProgressBarWithNumber progressbar, final TextView size,final TextView down,final String url, final String urlid, String name){
        OkHttpUtils
                .get()
                .url(url)
                .build()
                .execute(new FileCallBack(Environment.getExternalStorageDirectory().getAbsolutePath(),name) {
            @Override
            public void inProgress(float progress, long total, int id) {
                progressbar.setProgress((int)(100*progress));
                down.setText("正在下载");
                size.setVisibility(View.GONE);
                progressbar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onError(Call call, Exception e, int id) {
                Log.e(TAG, "onError :" + e.getMessage());
                down.setText("下载失败");
            }

            @Override
            public void onResponse(File response, int id) {
                Log.e(TAG, "onResponse :" + response.getAbsolutePath());//文件路径
                showToast("文件下载至："+response.getAbsolutePath());
                down.setText("已下载");
                SqliteTool.getInstance().addData(mContext,urlid);
                progressbar.setVisibility(View.GONE);
                size.setVisibility(View.VISIBLE);
            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
//        root.setFocusable(true);
//        root.setFocusableInTouchMode(true);
//        root.requestFocus();
    }
}
