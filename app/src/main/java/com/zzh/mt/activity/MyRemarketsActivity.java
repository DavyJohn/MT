package com.zzh.mt.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.zzh.mt.R;
import com.zzh.mt.base.BaseActivity;
import com.zzh.mt.base.CommonAdapter;
import com.zzh.mt.base.MultiItemTypeAdapter;
import com.zzh.mt.base.MyApplication;
import com.zzh.mt.base.ViewHolder;

import java.util.LinkedList;

import butterknife.BindView;

/**
 * Created by 腾翔信息 on 2017/5/23.
 */

public class MyRemarketsActivity extends BaseActivity {
    private static final String TAG = MyRemarketsActivity.class.getSimpleName();
    private LinkedList<String> list = new LinkedList<>();
    private CommonAdapter<String> adapter;

    @BindView(R.id.my_remarks_swipe)
    SwipeRefreshLayout mSwipe;
    @BindView(R.id.my_remarks_recycler)
    RecyclerView mRecyler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getToolBar().setTitle(R.string.my_remarks);
        MyApplication.getInstance().add(this);
        initview();
    }

    private void initview(){
        for(int i=0;i<6;i++){
            list.add(i+"");
        }
        mSwipe.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);

        mSwipe.post(new Runnable() {
            @Override
            public void run() {
                if (mSwipe.isRefreshing() == false){
                    mSwipe.setRefreshing(true);
                }
            }
        });
        mSwipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (mSwipe.isRefreshing() == true){
                    mSwipe.setRefreshing(false);
                }
            }
        });

        mRecyler.setHasFixedSize(true);
        mRecyler.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CommonAdapter<String>(mContext,R.layout.my_remarks_recycler_item_layout,list) {
            @Override
            protected void convert(ViewHolder holder, String s, int position) {

            }
        };
        mRecyler.setAdapter(adapter);
        adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                startActivity(new Intent(mContext,MyRemarksTwoActivity.class));
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
    }
    @Override
    public int getLayoutId() {
        return R.layout.my_remarks_main_layout;
    }
}
