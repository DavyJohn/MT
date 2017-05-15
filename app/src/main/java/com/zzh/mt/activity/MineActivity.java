package com.zzh.mt.activity;

import android.os.Bundle;

import com.zzh.mt.R;
import com.zzh.mt.base.BaseActivity;
import com.zzh.mt.base.MyApplication;

import butterknife.ButterKnife;

/**
 * Created by 腾翔信息 on 2017/5/15.
 */

public class MineActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getToolBar().setTitle(getString(R.string.my_info));
        MyApplication.getInstance().add(this);
        ButterKnife.bind(this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.mine_main_layout;
    }
}
