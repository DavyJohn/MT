package com.zzh.mt.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.pgyersdk.javabean.AppBean;
import com.pgyersdk.update.PgyUpdateManager;
import com.pgyersdk.update.UpdateManagerListener;
import com.squareup.picasso.Picasso;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;
import com.zhy.http.okhttp.callback.StringCallback;
import com.zzh.mt.R;
import com.zzh.mt.base.BaseActivity;
import com.zzh.mt.base.CommonAdapter;
import com.zzh.mt.base.MyApplication;
import com.zzh.mt.base.ViewHolder;
import com.zzh.mt.http.callback.SpotsCallBack;
import com.zzh.mt.mode.BannerEntity;
import com.zzh.mt.mode.BaseData;
import com.zzh.mt.mode.UserData;
import com.zzh.mt.utils.CommonUtil;
import com.zzh.mt.utils.Contants;
import com.zzh.mt.utils.MdTools;
import com.zzh.mt.utils.SharedPreferencesUtil;
import com.zzh.mt.widget.CircleImageView;
import com.zzh.mt.widget.banner.BannerView;
import com.zzh.mt.wrapper.HeaderAndFooterWrapper;

import java.util.LinkedHashMap;
import java.util.LinkedList;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Response;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener ,View.OnClickListener{

    private static final String TAG = MainActivity.class.getSimpleName();
    private ImageView mNavImage;
    DrawerLayout drawer;
    private UserData userData;
    ActionBarDrawerToggle toggle;
    private TextView mNickName,mInfo;
    private HeaderAndFooterWrapper mHeaderAndFooterWrapper;
    private LinkedList<BannerEntity.ImageData> banners = new LinkedList<>();
    private CommonAdapter<Integer> adapter;
    private Integer[] imageData = {R.drawable.ic_menu_elective,R.drawable.ic_menu_data,R.drawable.main_item_data};
    private Integer[] data = {R.string.my_courde,R.string.class_schedule,R.string.Course_materials};
    private LinkedList<Integer> list = new LinkedList<>();
    BannerView mBanner;
//    @BindView(R.id.banner)
//    BannerView mBanner;
    @BindView(R.id.main_recyclerview)
    RecyclerView mRecycler;
    @BindView(R.id.main_swipe)
    SwipeRefreshLayout mSwipe;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (SharedPreferencesUtil.getInstance(mContext).getString("userid") == null ||TextUtils.isEmpty(SharedPreferencesUtil.getInstance(mContext).getString("userid"))){
            startActivity(new Intent(mContext,LoginActivity.class));
            finish();
        }
        MyApplication.getInstance().add(this);
        ButterKnife.bind(this);
        getInfo();
        //更新
        PgyUpdateManager.register(this, "com.zzh.mt.fileprovider", new UpdateManagerListener() {
            @Override
            public void onNoUpdateAvailable() {

            }

            @Override
            public void onUpdateAvailable(String result) {
                // 将新版本信息封装到AppBean中
                final AppBean appBean = getAppBeanFromString(result);
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("更新")
                        .setMessage("检测到新的版本")
                        .setNegativeButton(
                                "确定",
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(
                                            DialogInterface dialog,
                                            int which) {
                                        startDownloadTask(
                                                MainActivity.this,
                                                appBean.getDownloadURL());
                                    }
                                }).show();
            }
        });
        //end

        hasToolBar(false);
        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        TextView textView = (TextView) toolbar.findViewById(R.id.toolbar_text);
        textView.setText(R.string.main_page);
        getSupportActionBar().setHomeButtonEnabled(true);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this,
                drawer,
                toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.setDrawerIndicatorEnabled(true);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);//防止icon 为灰色
        navigationView.inflateMenu(R.menu.activity_main_drawer);
        View headerLayout = navigationView.inflateHeaderView(R.layout.nav_header_main);
        mNavImage = (CircleImageView) headerLayout.findViewById(R.id.nav_header_image);
        mInfo = (TextView) headerLayout.findViewById(R.id.nav_header_info);
        mNickName = (TextView) headerLayout.findViewById(R.id.nav_header_nickname);
        mNavImage.setOnClickListener(this);
        mInfo.setOnClickListener(this);
        initRecycler();
        banner();
    }


    private void initRecycler(){
        for (int i=0;i<data.length;i++ ){
            list.add(data[i]);
        }

        mSwipe.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);

        mRecycler.setHasFixedSize(true);
        mRecycler.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CommonAdapter<Integer>(this,R.layout.main_recycler_item_layout,list) {

            @Override
            protected void convert(ViewHolder holder, Integer integer, int position) {
                holder.setTextid(R.id.main_recycler_item_text,integer);
                switch (position){
                    case 1:
                        holder.setImageResource(R.id.main_recycle_item_image,imageData[0]);
                        break;
                    case 2:
                        holder.setImageResource(R.id.main_recycle_item_image,imageData[1]);
                        break;
                    case 3:
                        holder.setImageResource(R.id.main_recycle_item_image,imageData[2]);
                        break;
                }
            }
        };
        mHeaderAndFooterWrapper = new HeaderAndFooterWrapper(adapter);
        //设置 banner
        mBanner= new BannerView(mContext);
        mHeaderAndFooterWrapper.addHeaderView(mBanner);
        mRecycler.setAdapter(adapter);
        mRecycler.setAdapter(mHeaderAndFooterWrapper);
        mHeaderAndFooterWrapper.notifyDataSetChanged();
        adapter.setOnItemClickListener(new CommonAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                switch (position){
                    case 1:
                        //我要选课
                        startActivity(new Intent(MainActivity.this,CourseActivity.class));
                        break;
                    case 2:
                        //课程安排
                        startActivity(new Intent(MainActivity.this,ScheduleActivity.class));
                        break;
                    case 3:
                        //课程材料
                        startActivity(new Intent(MainActivity.this,DataActivity.class));
                        break;
                }
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        mSwipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (mSwipe.isRefreshing() == true){
                    mSwipe.setRefreshing(false);
                }
                banner();
            }
        });
    }

    private void banner(){
        LinkedHashMap<String,String> map = new LinkedHashMap<>();
        map.put("userId",SharedPreferencesUtil.getInstance(mContext).getString("userid"));
        map.put("appVersion", CommonUtil.getVersion(mContext));
        map.put("ostype","android");
        map.put("uuid",CommonUtil.android_id(mContext));
        map.put("digest", MdTools.sign_digest(map));
        mOkHttpHelper.post(mContext, Contants.BASEURL+Contants.BANNERURL, map, TAG, new
                SpotsCallBack<BannerEntity>(mContext) {
                    @Override
                    public void onSuccess(Response response, BannerEntity data) {
                        if (data.getCode().equals("200")){
                            mSwipe.setRefreshing(false);
                            banners.clear();
                            banners.addAll(data.getImageList());
                            if (banners.size() == 1){
                                mBanner.build(banners);
                            }else {
                                mBanner.delayTime(5).build(banners);
                            }

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
    //获取个人信息昵称头像
    private void getInfo(){
        LinkedHashMap<String,String> map = new LinkedHashMap<>();
        map.put("appVersion", CommonUtil.getVersion(mContext));
        map.put("ostype","android");
        map.put("uuid",CommonUtil.android_id(mContext));
        map.put("searchUserId",SharedPreferencesUtil.getInstance(mContext).getString("userid"));
        map.put("userId",SharedPreferencesUtil.getInstance(mContext).getString("userid"));
        map.put("digest", MdTools.sign_digest(map));
        mOkHttpHelper.post(mContext, Contants.BASEURL + Contants.GETUSER, map, TAG, new SpotsCallBack<UserData>(mContext) {
            @Override
            public void onSuccess(Response response, UserData data) {
                if (data.getCode().equals("200")){
                    userData = data;
                    if (userData.getUserInfo().getDepartment() != null){
                        Contants.Deparmentname = userData.getUserInfo().getDepartment().getDepartmentName();
                        Contants.Deparmentid = userData.getUserInfo().getDepartmentId();
                    }

                    if (userData.getUserInfo().getSex().equals("1")){
                        Picasso.with(mContext).load(data.getUserInfo().getHeadUrl()).placeholder(R.drawable.image_b).error(R.drawable.image_b).into(mNavImage);
                    }else {
                        Picasso.with(mContext).load(data.getUserInfo().getHeadUrl()).placeholder(R.drawable.image_g).error(R.drawable.image_g).into(mNavImage);
                    }
                    mNickName.setText(data.getUserInfo().getNickName());
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
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (toggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }

        if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
            CommonUtil.exitBy2Click(this);
        } else {
            getSupportFragmentManager().popBackStack();
        }
    }
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_mainpage) {
        //首页
        } else if (id == R.id.nav_classmate) {
            //我的同学
            startActivity(new Intent(this,ClassmateActivity.class));
        } else if (id == R.id.nav_schedule) {
            //我的日程
            startActivity(new Intent(this,ScheduleActivity.class));
        } else if (id == R.id.nav_course) {
            //我要选课
            startActivity(new Intent(this,CourseActivity.class));
        } else if (id == R.id.nav_data) {
            //课程资料
            startActivity(new Intent(this,DataActivity.class));
        } else if (id == R.id.nav_remarks) {
            //我的备注
            startActivity(new Intent(this,MyRemarketsActivity.class));
        } else if (id == R.id.nav_info) {
            Intent intent = new Intent(this, MineActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else if (id == R.id.nav_exit) {
            quite("确认退出？",mContext);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.nav_header_info:
                Intent intent = new Intent(mContext,EditInfoActivity.class);
                intent.putExtra("headurl",userData.getUserInfo().getHeadUrl());
                intent.putExtra("nickname",userData.getUserInfo().getNickName());
                intent.putExtra("brandname",userData.getUserInfo().getBrandName());
                intent.putExtra("sex",userData.getUserInfo().getSex());
                startActivity(intent);
                break;

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getInfo();
    }
}
