package com.zzh.mt.activity;

import android.content.Intent;
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
import butterknife.OnClick;

/**
 * Created by 腾翔信息 on 2017/5/25.
 */

public class GroupActivity extends BaseActivity {
    @OnClick(R.id.group_note) void note (){
        startActivity(new Intent(mContext,MyRemarksTwoActivity.class));
    }
    CommonAdapter<String> adapter ;
    private LinkedList<String> list = new LinkedList<>();
    @BindView(R.id.group_recycler)
    RecyclerView mRecycler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyApplication.getInstance().add(this);
        getToolBar().setTitle("小组活动");
        initview();
    }

    private void initview(){
        list.clear();
        for (int i=0;i<4;i++){
            list.add(i+"");
        }
        mRecycler.setHasFixedSize(true);
        mRecycler.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CommonAdapter<String>(mContext,R.layout.group_item_layout,list) {
            @Override
            protected void convert(ViewHolder holder, String s, int position) {

            }
        };
        mRecycler.setAdapter(adapter);
    }
    @Override
    public int getLayoutId() {
        return R.layout.group_main_layout;
    }
}
