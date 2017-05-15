package com.zzh.mt.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.zzh.mt.R;
import com.zzh.mt.base.BaseActivity;
import com.zzh.mt.base.MyApplication;
import com.zzh.mt.mode.BannerEntity;
import com.zzh.mt.widget.CircleImageView;
import com.zzh.mt.widget.banner.BannerView;

import java.util.LinkedList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener ,View.OnClickListener{
    private ImageView mNavImage;
    private TextView mNickName,mInfo;
    private LinkedList<BannerEntity> banners = new LinkedList<>();

    @BindView(R.id.banner)
    BannerView mBanner;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
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
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this,
                drawer,
                toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerLayout = navigationView.inflateHeaderView(R.layout.nav_header_main);
        mNavImage = (CircleImageView) headerLayout.findViewById(R.id.nav_header_image);
        Picasso.with(mContext).load(R.drawable.image_ing).placeholder(R.drawable.image_ing).error(R.drawable.image_ing).into(mNavImage);
        mInfo = (TextView) headerLayout.findViewById(R.id.nav_header_info);
        mNavImage.setOnClickListener(this);
        mInfo.setOnClickListener(this);
//        mBanner.delayTime(5).build(banners);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_mainpage) {

        } else if (id == R.id.nav_classmate) {
            startActivity(new Intent(this,ClassmateActivity.class));
        } else if (id == R.id.nav_schedule) {
            startActivity(new Intent(this,ScheduleActivity.class));
        } else if (id == R.id.nav_course) {
            startActivity(new Intent(this,CourseActivity.class));
        } else if (id == R.id.nav_data) {
            startActivity(new Intent(this,DataActivity.class));
        } else if (id == R.id.nav_remarks) {
            startActivity(new Intent(this,RemarksActivity.class));
        } else if (id == R.id.nav_info) {
            startActivity(new Intent(this,MineActivity.class));
        } else if (id == R.id.nav_exit) {
          //退出
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
