package com.zzh.mt.activity;

import android.os.Bundle;

import com.zzh.mt.R;
import com.zzh.mt.base.BaseActivity;

/**
 * Created by 腾翔信息 on 2017/5/15.
 */

public class CourseActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getToolBar().setTitle(getString(R.string.my_courde));
    }

    @Override
    public int getLayoutId() {
        return R.layout.course_main_activity;
    }
}
