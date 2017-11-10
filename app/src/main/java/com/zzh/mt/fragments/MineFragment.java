package com.zzh.mt.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.zzh.mt.R;
import com.zzh.mt.activity.EditInfoActivity;
import com.zzh.mt.activity.LanguageSettingActivity;
import com.zzh.mt.activity.MineActivity;
import com.zzh.mt.activity.ModifyPassActivity;
import com.zzh.mt.base.BaseFragment;
import com.zzh.mt.base.CommonAdapter;
import com.zzh.mt.base.ViewHolder;
import com.zzh.mt.http.callback.SpotsCallBack;
import com.zzh.mt.mode.UserData;
import com.zzh.mt.utils.CommonUtil;
import com.zzh.mt.utils.Contants;
import com.zzh.mt.utils.MdTools;
import com.zzh.mt.utils.SharedPreferencesUtil;
import com.zzh.mt.widget.CircleImageView;
import com.zzh.mt.widget.DividerItemDecoration;

import java.util.LinkedHashMap;
import java.util.LinkedList;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Response;

/**
 * Created by 腾翔信息 on 2017/10/26.
 */

public class MineFragment  extends BaseFragment{
    private String TAG = MineFragment.class.getSimpleName();
    @BindView(R.id.mine_recycler)
    RecyclerView mRecycler;
    @BindView(R.id.nav_header_image)
    CircleImageView mImage;
    @BindView(R.id.nav_header_nickname)
    TextView mName;
    @OnClick(R.id.mine_language_setting) void language(){
        startActivity(new Intent(mContext,LanguageSettingActivity.class));
    }
    @OnClick(R.id.mine_passw) void updataPass(){
        startActivity(new Intent(mContext,ModifyPassActivity.class));
    }
    @OnClick(R.id.mine_getout) void out(){
        quite("确认退出？",mContext);
    }
    @OnClick(R.id.nav_header_info) void edit(){
        Intent intent = new Intent(mContext,EditInfoActivity.class);
        intent.putExtra("headurl",userData.getUserInfo().getHeadUrl());
        intent.putExtra("nickname",userData.getUserInfo().getNickName());
        intent.putExtra("brandname",userData.getUserInfo().getBrandName());
        intent.putExtra("deparname",userData.getUserInfo().getDepartment().getDepartmentName());
        intent.putExtra("sex",userData.getUserInfo().getSex());
        startActivity(intent);
    }
    private LinkedList<Integer> list = new LinkedList<>();
    private Integer [] data = {R.string.chinese_name,R.string.englich_name,R.string.brandname,R.string.department,R.string.gender,R.string.birthday,R.string.year_of_work,R.string.company_email};
    UserData userData = null;
    CommonAdapter<Integer> adapter;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.mine_main_layout,container,false);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecycler.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setSmoothScrollbarEnabled(true);
        mRecycler.setLayoutManager(layoutManager);
        mRecycler.setNestedScrollingEnabled(false);
        mRecycler.addItemDecoration(new DividerItemDecoration(mContext,DividerItemDecoration.VERTICAL_LIST));
        preferences = mContext.getSharedPreferences("lang", Context.MODE_PRIVATE);
        editor = preferences.edit();
        getInfo();
    }
    private void initview(){
        mImage.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.imag_demo));
        list.clear();
        for (int i=0;i<data.length;i++){
            list.add(data[i]);
        }
        mName.setText(userData.getUserInfo().getNickName());
        if (userData.getUserInfo().getSex().equals("1")){
            Picasso.with(mContext).load(userData.getUserInfo().getHeadUrl()).placeholder(R.drawable.image_b).error(R.drawable.image_b).into(mImage);
        }else {
            Picasso.with(mContext).load(userData.getUserInfo().getHeadUrl()).placeholder(R.drawable.image_g).error(R.drawable.image_g).into(mImage);
        }

        adapter = new CommonAdapter<Integer>(mContext,R.layout.classmate_info_item_main_layout,list) {
            @Override
            protected void convert(ViewHolder holder, Integer s, int position) {
                holder.setText(R.id.classmate_info_item_key,getString(s));
                switch (position){
                    case 0:
                        holder.setText(R.id.classmate_info_item_view,userData.getUserInfo().getChineseName());
                        break;
                    case 1:
                        holder.setText(R.id.classmate_info_item_view,userData.getUserInfo().getEnglishName());
                        break;
                    case 2:
                        holder.setText(R.id.classmate_info_item_view,userData.getUserInfo().getBrandName());
                        break;
                    case 3:
                        if (userData.getUserInfo().getDepartment() != null){
                            holder.setText(R.id.classmate_info_item_view,userData.getUserInfo().getDepartment().getDepartmentName());
                        }
                        break;
                    case 4:
                        holder.setText(R.id.classmate_info_item_view,userData.getUserInfo().getSex().equals("1")?"男":"女");
                        break;
                    case 5:
                        holder.setText(R.id.classmate_info_item_view,userData.getUserInfo().getBirthday().substring(5,userData.getUserInfo().getBirthday().length()));
                        break;
                    case 6:
                        holder.setText(R.id.classmate_info_item_view,userData.getUserInfo().getEntryYear().substring(0,4));
                        break;
                    case 7:
                        holder.setText(R.id.classmate_info_item_view,userData.getUserInfo().getCompanyEmail());
                        break;
                }
            }
        };
        mRecycler.setAdapter(adapter);
    }
    private void getInfo(){
        //个人资料
        LinkedHashMap<String,String> map = new LinkedHashMap<>();
        map.put("appVersion", CommonUtil.getVersion(mContext));
        map.put("ostype","android");
        map.put("uuid",CommonUtil.android_id(mContext));
        map.put("searchUserId", SharedPreferencesUtil.getInstance(mContext).getString("userid"));
        map.put("userId", SharedPreferencesUtil.getInstance(mContext).getString("userid"));
        map.put("digest", MdTools.sign_digest(map));
        mOkHttpHelper.post(mContext, Contants.BASEURL + Contants.GETUSER, map, TAG, new SpotsCallBack<UserData>(mContext) {
            @Override
            public void onSuccess(Response response, UserData data) {
                if (data.getCode().equals("200")){
                    userData = data;
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
    public void onResume() {
        super.onResume();
        getInfo();
    }
}
