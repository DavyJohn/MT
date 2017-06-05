package com.zzh.mt.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.CheckBox;

import com.zzh.mt.R;
import com.zzh.mt.adapter.LanguageAdapter;
import com.zzh.mt.base.BaseActivity;
import com.zzh.mt.base.MyApplication;
import com.zzh.mt.utils.Contants;
import com.zzh.mt.utils.ObserverUtils;
import com.zzh.mt.widget.DividerItemDecoration;

import butterknife.BindView;

/**
 * Created by 腾翔信息 on 2017/5/24.
 */

public class LanguageSettingActivity extends BaseActivity {
    @BindView(R.id.language_setting_recycler)
    RecyclerView mRecycler;
    LanguageAdapter adapter;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getToolBar().setTitle("语言/language");
        MyApplication.getInstance().add(this);
        preferences = getSharedPreferences("lang", Context.MODE_PRIVATE);
        editor = preferences.edit();
        inintview();
    }
    private void inintview(){
        mRecycler.setLayoutManager(new LinearLayoutManager(this));
        mRecycler.setHasFixedSize(true);
        mRecycler.addItemDecoration(new DividerItemDecoration(mContext,DividerItemDecoration.VERTICAL_LIST));
        adapter = new LanguageAdapter(mContext);
        adapter.setOnClickItemListener(new LanguageAdapter.OnClickItemListener() {
            @Override
            public void onClickItem(CheckBox view, int postion) {
                adapter.addData(postion);
                if (postion == 0){
                    editor.putString("lang", "zh");
                    editor.commit();
                    ObserverUtils.getInstance().notifyObservers(Integer.parseInt("1"));
                }else if (postion ==1){
                    editor.putString("lang", "en");
                    editor.commit();
                    ObserverUtils.getInstance().notifyObservers(Integer.parseInt("1"));
                }
            }
        });
        mRecycler.setAdapter(adapter);
    }
    @Override
    public int getLayoutId() {
        return R.layout.language_setting_main_layout;
    }
}
