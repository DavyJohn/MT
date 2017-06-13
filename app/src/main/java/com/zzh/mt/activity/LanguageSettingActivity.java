package com.zzh.mt.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.widget.CheckBox;

import com.zzh.mt.R;
import com.zzh.mt.adapter.LanguageAdapter;
import com.zzh.mt.base.BaseActivity;
import com.zzh.mt.base.MyApplication;
import com.zzh.mt.utils.Contants;
import com.zzh.mt.utils.DisplayUtil;
import com.zzh.mt.utils.LocaleUtils;
import com.zzh.mt.utils.ObserverUtils;
import com.zzh.mt.widget.DividerItemDecoration;

import java.util.Locale;

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
        getToolBar().setTitle(getString(R.string.lan_set));
        MyApplication.getInstance().add(this);
        mRecycler.setLayoutManager(new LinearLayoutManager(this));
        mRecycler.setHasFixedSize(true);
        mRecycler.addItemDecoration(new DividerItemDecoration(mContext,DividerItemDecoration.VERTICAL_LIST));
        inintview();
    }
    private void inintview(){

        adapter = new LanguageAdapter(mContext);
        adapter.setOnClickItemListener(new LanguageAdapter.OnClickItemListener() {
            @Override
            public void onClickItem(CheckBox view, int postion) {
                adapter.addData(postion);
                if (postion == 0){
                    switchLanguage("zh");
                    ObserverUtils.getInstance().notifyObservers(Integer.parseInt("1"));

//                    if (LocaleUtils.needUpdateLocale(mContext, LocaleUtils.LOCALE_CHINESE)) {
//                        LocaleUtils.updateLocale(mContext, LocaleUtils.LOCALE_CHINESE);
                        ObserverUtils.getInstance().notifyObservers(Integer.parseInt("1"));
//
//                    }
                }else if (postion ==1){
//                    if (LocaleUtils.needUpdateLocale(mContext, LocaleUtils.LOCALE_ENGLISH)) {
//                        LocaleUtils.updateLocale(mContext, LocaleUtils.LOCALE_ENGLISH);
//                        ObserverUtils.getInstance().notifyObservers(Integer.parseInt("1"));
//
//                    }
                    switchLanguage("en");
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
