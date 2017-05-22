package com.zzh.mt.activity;

import android.content.Intent;
import android.graphics.Color;
import android.icu.text.UnicodeSetSpanner;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.zzh.mt.R;
import com.zzh.mt.base.BaseActivity;
import com.zzh.mt.base.CommonAdapter;
import com.zzh.mt.base.MultiItemTypeAdapter;
import com.zzh.mt.base.MyApplication;
import com.zzh.mt.base.ViewHolder;
import com.zzh.mt.http.SpotsCallBack;
import com.zzh.mt.mode.BannerEntity;
import com.zzh.mt.utils.CommonUtil;
import com.zzh.mt.utils.Contants;
import com.zzh.mt.widget.CircleImageView;
import com.zzh.mt.widget.banner.BannerView;
import com.zzh.mt.wrapper.HeaderAndFooterWrapper;

import java.util.LinkedHashMap;
import java.util.LinkedList;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Response;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener ,View.OnClickListener{

    private static final String TAG = MainActivity.class.getSimpleName();
    private ImageView mNavImage;
    DrawerLayout drawer;
    ActionBarDrawerToggle toggle;
    private TextView mNickName,mInfo;
    private HeaderAndFooterWrapper mHeaderAndFooterWrapper;
    private LinkedList<BannerEntity.Head> banners = new LinkedList<>();
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
        MyApplication.getInstance().add(this);
        ButterKnife.bind(this);
        hasToolBar(false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            getWindow().setStatusBarColor(Color.TRANSPARENT);
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this,
                drawer,
                toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);//防止icon 为灰色
        View headerLayout = navigationView.inflateHeaderView(R.layout.nav_header_main);
        mNavImage = (CircleImageView) headerLayout.findViewById(R.id.nav_header_image);
        Picasso.with(mContext).load(R.drawable.imag_demo).placeholder(R.drawable.image_ing).error(R.drawable.image_ing).into(mNavImage);
        mInfo = (TextView) headerLayout.findViewById(R.id.nav_header_info);
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

        mSwipe.post(new Runnable() {
            @Override
            public void run() {
                if (mSwipe.isRefreshing() == false){
                    mSwipe.setRefreshing(true);
                    banner();
                }
            }
        });
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
        mOkHttpHelper.post(mContext, Contants.BASEBANNERURL+Contants.BANNERURL, map, TAG, new
                SpotsCallBack<BannerEntity>(mContext) {
                    @Override
                    public void onSuccess(Response response, BannerEntity data) {
                        if (mSwipe.isRefreshing()){
                            mSwipe.setRefreshing(false);
                        }
                        banners.clear();
                        banners.addAll(data.getResult().getAd().getHead());
                        mBanner.delayTime(5).build(banners);
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
            //课程安排
            startActivity(new Intent(this,ScheduleActivity.class));
        } else if (id == R.id.nav_course) {
            //我要选课
            startActivity(new Intent(this,CourseActivity.class));
        } else if (id == R.id.nav_data) {
            //课程资料
            startActivity(new Intent(this,DataActivity.class));
        } else if (id == R.id.nav_remarks) {
            //我的备注
            startActivity(new Intent(this,RemarksActivity.class));
        } else if (id == R.id.nav_info) {
            Intent intent = new Intent(this, MineActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else if (id == R.id.nav_exit) {
          //退出
            startActivity(new Intent(mContext,LoginActivity.class));
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.nav_header_image:
                showToast("修改头像");
                break;
            case R.id.nav_header_info:
                showToast("编辑资料");
                break;

        }
    }
}
