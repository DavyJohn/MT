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

import com.zzh.mt.R;
import com.zzh.mt.base.BaseActivity;
import com.zzh.mt.base.CommonAdapter;
import com.zzh.mt.base.MyApplication;
import com.zzh.mt.base.ViewHolder;
import com.zzh.mt.utils.ObserverUtils;
import com.zzh.mt.widget.CircleImageView;
import com.zzh.mt.widget.DividerItemDecoration;

import java.util.LinkedList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by 腾翔信息 on 2017/5/15.
 */

public class MineActivity extends BaseActivity implements View.OnClickListener{
//    @BindView(R.id.language)
//    Button mButton;
    @BindView(R.id.mine_recycler)
    RecyclerView mRecycler;
    @BindView(R.id.nav_header_image)
    CircleImageView mImage;
    @OnClick(R.id.mine_language_setting) void language(){
        startActivity(new Intent(mContext,LanguageSettingActivity.class));
    }
    @OnClick(R.id.mine_passw) void updataPass(){
        startActivity(new Intent(mContext,ModifyPassActivity.class));
    }
    private LinkedList<String> list = new LinkedList<>();
    private String [] data = {"中文名字","英文名字","品牌","部门","性别","生日","公司邮箱"};
    private String [] da = {"张杰","jason","牌牌","生产部","难","05月11日","1111111@163.com"};
    CommonAdapter<String> adapter;

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getToolBar().setTitle(getString(R.string.my_info));
        MyApplication.getInstance().add(this);
//        mButton.setOnClickListener(this);
        preferences = getSharedPreferences("lang", Context.MODE_PRIVATE);
        editor = preferences.edit();
        initview();
    }

    private void initview(){
        mImage.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.imag_demo));
        for (int i=0;i<data.length;i++){
            list.add(data[i]);
        }
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
                holder.setText(R.id.classmate_info_item_view,da[position]);
            }
        };
        mRecycler.setAdapter(adapter);
    }
    @Override
    public int getLayoutId() {
        return R.layout.mine_main_layout;
    }

    @Override
    public void onClick(View v) {
//        switch (v.getId()){
//            case R.id.language:
//                AlertDialog.Builder builder = new AlertDialog.Builder(this);
//                builder.setSingleChoiceItems(R.array.lang, android.R.id.text1, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        dialogInterface.dismiss();
//                        saveLangConfig(i);
//                    }
//                });
//                AlertDialog dialog = builder.create();
//                dialog.show();
//                break;
//        }
    }

    private void saveLangConfig(int i) {
        switch (i) {
            case 0:
                editor.putString("lang", "zh");
                editor.commit();
                break;
            case 1:
                editor.putString("lang", "en");
                editor.commit();
                break;
        }
        // 第一种方式，观察者模式通知刷新recreate
		ObserverUtils.getInstance().notifyObservers(Integer.parseInt("1"));
        //TODO SOMETHING TO ERROR
        // 第二种方式，跳回MainActivity
//        go2Main();
    }
    private void go2Main() {
        // 微信切换语言后是回调主页
        // 微博切换语音后是重启App
        // 设计切换语音为系统自动，和自己支持的语音可选
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
