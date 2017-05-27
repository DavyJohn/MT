package com.zzh.mt.activity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.widget.TextView;

import com.zzh.mt.R;
import com.zzh.mt.base.BaseActivity;
import com.zzh.mt.base.MyApplication;

import org.w3c.dom.Text;

import butterknife.BindView;

/**
 * Created by 腾翔信息 on 2017/5/22.
 */

public class CourseDetailsActivity extends BaseActivity {

    private static final String TAG = CourseDetailsActivity.class.getSimpleName();
    @BindView(R.id.course_details_date)
    TextView mCdd;
    @BindView(R.id.course_details_where)
    TextView mCdw;
    @BindView(R.id.course_details_clock)
    TextView mCdc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyApplication.getInstance().add(this);
//        getToolBar().setTitle(getIntent().getStringExtra("Course"));
        getToolBar().setTitle("javascript基础");

    }

    @Override
    public int getLayoutId() {
        return R.layout.course_details_main_layout;
    }
}
