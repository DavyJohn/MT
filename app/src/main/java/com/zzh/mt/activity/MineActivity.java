package com.zzh.mt.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.zzh.mt.R;
import com.zzh.mt.base.BaseActivity;
import com.zzh.mt.base.MyApplication;
import com.zzh.mt.utils.ObserverUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 腾翔信息 on 2017/5/15.
 */

public class MineActivity extends BaseActivity implements View.OnClickListener{
    @BindView(R.id.language)
    Button mButton;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getToolBar().setTitle(getString(R.string.my_info));
        MyApplication.getInstance().add(this);
        ButterKnife.bind(this);
        mButton.setOnClickListener(this);
        preferences = getSharedPreferences("lang", Context.MODE_PRIVATE);
        editor = preferences.edit();
    }

    @Override
    public int getLayoutId() {
        return R.layout.mine_main_layout;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.language:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setSingleChoiceItems(R.array.lang, android.R.id.text1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        saveLangConfig(i);
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
                break;
        }
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
