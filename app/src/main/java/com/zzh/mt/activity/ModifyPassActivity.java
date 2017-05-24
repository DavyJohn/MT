package com.zzh.mt.activity;

import android.os.Bundle;

import com.zzh.mt.R;
import com.zzh.mt.base.BaseActivity;
import com.zzh.mt.base.MyApplication;

/**
 * Created by 腾翔信息 on 2017/5/24.
 */

public class ModifyPassActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyApplication.getInstance().add(this);
        getToolBar().setTitle("密码更新");

    }

    private void initview(){

    }

    @Override
    public int getLayoutId() {
        return R.layout.modify_pass_main_layout;
    }
}
