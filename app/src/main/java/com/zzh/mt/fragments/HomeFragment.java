package com.zzh.mt.fragments;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.zzh.mt.R;
import com.zzh.mt.activity.CourseActivity;
import com.zzh.mt.activity.DataActivity;
import com.zzh.mt.activity.MainActivity;
import com.zzh.mt.activity.MyRemarketsActivity;
import com.zzh.mt.activity.ScheduleActivity;
import com.zzh.mt.base.BaseFragment;
import com.zzh.mt.base.CommonAdapter;
import com.zzh.mt.base.MyApplication;
import com.zzh.mt.base.ViewHolder;
import com.zzh.mt.http.callback.SpotsCallBack;
import com.zzh.mt.mode.BannerEntity;
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
import butterknife.OnClick;
import okhttp3.Response;

/**
 * Created by 腾翔信息 on 2017/10/26.
 */

public class HomeFragment extends BaseFragment {

    private static final String TAG = HomeFragment.class.getSimpleName();
    private ImageView mNavImage;
    private UserData userData;
    private TextView mNickName,mInfo;
    private LinkedList<BannerEntity.ImageData> banners = new LinkedList<>();
    private CommonAdapter<Integer> adapter;
    private Integer[] imageData = {R.drawable.index_remark,R.drawable.index_selection,R.drawable.index_curriculum,R.drawable.index_material};
    private Integer[] data = {R.string.my_remarks,R.string.my_courde,R.string.class_schedule,R.string.Course_materials};
    private LinkedList<Integer> list = new LinkedList<>();
    @BindView(R.id.banner)
    BannerView mBanner;
    @BindView(R.id.main_recyclerview)
    RecyclerView mRecycler;
    @OnClick(R.id.home_rili) void ri(){
        showToast("日历",mContext);
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment_layout,container,false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        ButterKnife.bind(this,view);
        Toolbar toolbar = (Toolbar)view.findViewById(R.id.main_toolbar);
        toolbar.setTitle("");
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        TextView textView = (TextView) toolbar.findViewById(R.id.toolbar_text);
        textView.setText(R.string.main_page);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setHomeButtonEnabled(true);
        initRecycler();
        banner();
    }

    private void initRecycler(){
        list.clear();
        for (int i=0;i<data.length;i++ ){
            list.add(data[i]);
        }
        mRecycler.setHasFixedSize(true);
        mRecycler.setLayoutManager(new GridLayoutManager(mContext,4));
        adapter = new CommonAdapter<Integer>(getActivity(),R.layout.main_recycler_item_layout,list) {

            @Override
            protected void convert(ViewHolder holder, Integer integer, int position) {
                holder.setTextid(R.id.main_recycler_item_text,integer);
                switch (position){
                    case 0:
                        holder.setImageResource(R.id.main_recycle_item_image,imageData[0]);
                        break;
                    case 1:
                        holder.setImageResource(R.id.main_recycle_item_image,imageData[1]);
                        break;
                    case 2:
                        holder.setImageResource(R.id.main_recycle_item_image,imageData[2]);
                        break;
                    case 3:
                        holder.setImageResource(R.id.main_recycle_item_image,imageData[3]);
                        break;
                }
            }
        };
        mRecycler.setAdapter(adapter);
        adapter.setOnItemClickListener(new CommonAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                switch (position){
                    case 0:
                       //我的备注
                        startActivity(new Intent(mContext,MyRemarketsActivity.class));
                        break;
                    case 1:
                        //我要选课
                        startActivity(new Intent(mContext,CourseActivity.class));
                        break;
                    case 2:
                        //课程安排
                        startActivity(new Intent(mContext,ScheduleActivity.class));
                        break;
                    case 3:
                        //课程材料
                        startActivity(new Intent(mContext,DataActivity.class));
                        break;
                }
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });

    }

    private void banner(){
        LinkedHashMap<String,String> map = new LinkedHashMap<>();
        map.put("userId", SharedPreferencesUtil.getInstance(mContext).getString("userid"));
        map.put("appVersion", CommonUtil.getVersion(mContext));
        map.put("ostype","android");
        map.put("uuid",CommonUtil.android_id(mContext));
        map.put("digest", MdTools.sign_digest(map));
        mOkHttpHelper.post(mContext, Contants.BASEURL+Contants.BANNERURL, map, TAG, new
                SpotsCallBack<BannerEntity>(mContext) {
                    @Override
                    public void onSuccess(Response response, BannerEntity data) {
                        if (data.getCode().equals("200")){
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

}
