package com.zzh.mt.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.zzh.mt.R;
import com.zzh.mt.base.BaseActivity;
import com.zzh.mt.base.CommonAdapter;
import com.zzh.mt.base.MyApplication;
import com.zzh.mt.base.ViewHolder;
import com.zzh.mt.http.callback.SpotsCallBack;
import com.zzh.mt.mode.UserData;
import com.zzh.mt.utils.Contants;
import com.zzh.mt.utils.ObserverUtils;
import com.zzh.mt.utils.SharedPreferencesUtil;
import com.zzh.mt.widget.CircleImageView;
import com.zzh.mt.widget.DividerItemDecoration;

import java.util.LinkedHashMap;
import java.util.LinkedList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Response;

/**
 * Created by 腾翔信息 on 2017/5/15.
 */

public class MineActivity extends BaseActivity implements View.OnClickListener{

    private String TAG = MineActivity.class.getSimpleName();
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
    @OnClick(R.id.nav_header_info) void edit(){
        Intent intent = new Intent(mContext,EditInfoActivity.class);
        intent.putExtra("headurl",userData.getUserInfo().getHeadUrl());
        intent.putExtra("nickname",userData.getUserInfo().getNickName());
        intent.putExtra("brandname",userData.getUserInfo().getBrandName());
        intent.putExtra("deparname",userData.getUserInfo().getDepartment().getDepartmentName());
        startActivity(intent);
    }
    private LinkedList<String> list = new LinkedList<>();
    private String [] data = {"中文名字","英文名字","品牌","部门","性别","生日","入职年份","公司邮箱"};
    UserData userData = null;
    CommonAdapter<String> adapter;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getToolBar().setTitle(getString(R.string.my_info));
        MyApplication.getInstance().add(this);
        preferences = getSharedPreferences("lang", Context.MODE_PRIVATE);
        editor = preferences.edit();
        getInfo();
    }

    private void initview(){
        mImage.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.imag_demo));
        for (int i=0;i<data.length;i++){
            list.add(data[i]);
        }
        mName.setText(userData.getUserInfo().getChineseName());
        Picasso.with(mContext).load(userData.getUserInfo().getHeadUrl()).placeholder(R.drawable.image_ing).error(R.drawable.image_ing).into(mImage);
        mRecycler.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setSmoothScrollbarEnabled(true);
        mRecycler.setLayoutManager(layoutManager);
        mRecycler.setNestedScrollingEnabled(false);
        mRecycler.addItemDecoration(new DividerItemDecoration(mContext,DividerItemDecoration.VERTICAL_LIST));
        adapter = new CommonAdapter<String>(mContext,R.layout.classmate_info_item_main_layout,list) {
            @Override
            protected void convert(ViewHolder holder, String s, int position) {
                holder.setText(R.id.classmate_info_item_key,s);
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
                        holder.setText(R.id.classmate_info_item_view,userData.getUserInfo().getDepartment().getDepartmentName());
                        break;
                    case 4:
                        holder.setText(R.id.classmate_info_item_view,userData.getUserInfo().getSex().equals("1")?"男":"女");
                        break;
                    case 5:
                        holder.setText(R.id.classmate_info_item_view,userData.getUserInfo().getDepartment().getDepartmentName());
                        break;
                    case 6:
                        holder.setText(R.id.classmate_info_item_view,userData.getUserInfo().getCreateTime());
                        break;
                    case 7:
                        holder.setText(R.id.classmate_info_item_view,userData.getUserInfo().getCompanyEmail());
                        break;
                }
            }
        };
        mRecycler.setAdapter(adapter);
    }

    @Override
    public int getLayoutId() {
        return R.layout.mine_main_layout;
    }

    private void getInfo(){
        //个人资料
        LinkedHashMap<String,String> map = new LinkedHashMap<>();
        map.put("userId", SharedPreferencesUtil.getInstance(mContext).getString("userid"));
        mOkHttpHelper.post(mContext, Contants.BASEURL + Contants.GETUSER, map, TAG, new SpotsCallBack<UserData>(mContext) {
            @Override
            public void onSuccess(Response response, UserData data) {
                if (data.getCode().equals("200")){
                    userData = data;
                    initview();
                }
            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });
    }
    @Override
    public void onClick(View v) {

    }

}
