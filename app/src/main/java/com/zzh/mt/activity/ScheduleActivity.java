package com.zzh.mt.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.zzh.mt.R;
import com.zzh.mt.base.BaseActivity;
import com.zzh.mt.base.CommonAdapter;
import com.zzh.mt.base.MyApplication;
import com.zzh.mt.base.ViewHolder;

import java.util.LinkedList;

import butterknife.BindView;

/**
 * Created by 腾翔信息 on 2017/5/15.
 */

public class ScheduleActivity extends BaseActivity {
    private static final String TAG = ScheduleActivity.class.getSimpleName();
    CommonAdapter<Integer> adapter;
    private LinkedList<Integer> list = new LinkedList<>();
    @BindView(R.id.schedule_recycler)
    RecyclerView mReycler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getToolBar().setTitle(getString(R.string.class_schedule));
        MyApplication.getInstance().add(this);
        initview();
    }

    private void initview(){
        mReycler.setHasFixedSize(true);
        mReycler.setLayoutManager(new LinearLayoutManager(this));
//        adapter = new CommonAdapter<Integer>(mContext,R.layout.) {
//            @Override
//            protected void convert(ViewHolder holder, Integer integer, int position) {
//
//            }
//        }
    }
    @Override
    public int getLayoutId() {
        return R.layout.schedule_main_layout;
    }
}
