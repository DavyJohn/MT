package com.zzh.mt.activity;

import android.os.Bundle;

import com.zzh.mt.R;
import com.zzh.mt.base.BaseActivity;

/**
 * Created by 腾翔信息 on 2017/5/15.
 */

public class RemarksActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getToolBar().setTitle(getString(R.string.my_remarks));
    }

    @Override
    public int getLayoutId() {
        return R.layout.remokes_main_layout;
    }
}
