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
import com.zzh.mt.base.MyApplication;
import com.zzh.mt.base.ViewHolder;

import java.util.LinkedList;

import butterknife.BindView;

/**
 * Created by 腾翔信息 on 2017/5/23.
 */

public class ObligatoryActivity extends BaseActivity {

    private CommonAdapter<String> adapter;
    private LinkedList<String> list = new LinkedList<>();
    @BindView(R.id.obligatory_swipe)
    SwipeRefreshLayout mSwipe;
    @BindView(R.id.obligatory_recycler)
    RecyclerView mRecycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getToolBar().setTitle(getString(R.string.my_obligatory));
        MyApplication.getInstance().add(this);
        initview();
    }

    private void initview(){
        for (int i=0;i<5;i++){
            list.add(i+"");
        }
        mSwipe.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);
        mSwipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (mSwipe.isRefreshing() == true){
                    mSwipe.setRefreshing(false);
                }

            }
        });

        mRecycler.setLayoutManager(new LinearLayoutManager(this));
        mRecycler.setHasFixedSize(true);
        adapter = new CommonAdapter<String>(mContext,R.layout.schedule_recyclerview_item_layout,list) {
            @Override
            protected void convert(ViewHolder holder, String s, final int position) {
                //详情
                holder.setOnClickListener(R.id.schedule_recycler_item_details, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext,CourseDetailsActivity.class);
                        intent.putExtra("Course",list.get(position));
                        startActivity(intent);
                    }
                });
                //时间安排
                holder.setOnClickListener(R.id.schedule_recycler_item_image_schedule, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext,ScheduleDateActivity.class);
                        intent.putExtra("Course",list.get(position));
                        startActivity(intent);
                    }
                });
            }
        };
        mRecycler.setAdapter(adapter);
    }
    @Override
    public int getLayoutId() {
        return R.layout.oblifatory_main_layout;
    }
}
