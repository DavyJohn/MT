package com.zzh.mt.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
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
 * Created by 腾翔信息 on 2017/5/22.
 */

public class ElectiveListActivity extends BaseActivity{

    private static final String TAG = ElectiveListActivity.class.getSimpleName();
    private LinkedList<Integer> list =  new LinkedList<>();
    private CommonAdapter<Integer> adapter;
    @BindView(R.id.selctive_list_swipe)
    SwipeRefreshLayout mSwipe;
    @BindView(R.id.elective_list_recycler)
    RecyclerView mRecycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyApplication.getInstance().add(this);
        getToolBar().setTitle("选修列表");
        initview();
    }

    private void initview(){

        for (int i=0;i<4;i++){
            list.add(i);
        }
        mSwipe.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);

        mRecycler.setLayoutManager(new LinearLayoutManager(this));
        mRecycler.setHasFixedSize(true);
        mSwipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (mSwipe.isRefreshing() == true){
                    mSwipe.setRefreshing(false);
                }
            }
        });
        adapter = new CommonAdapter<Integer>(mContext,R.layout.elective_list_recycler_item_layout,list) {
            @Override
            protected void convert(ViewHolder holder, final Integer integer, int position) {
                holder.setOnClickListener(R.id.training_num, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(mContext,ElectiveListTwoActivity.class));
                    }
                });

                holder.setOnClickListener(R.id.elective_list_details, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext,CourseDetailsActivity.class);
                        intent.putExtra("Course",integer+"");
                        startActivity(intent);
                    }
                });
                if (position == 1){
                    holder.setClick(R.id.training_num,false);
                    holder.setImageDrawable(R.id.training_num, ContextCompat.getDrawable(mContext,R.drawable.click_training_unsel));
                }
            }

        };

        mRecycler.setAdapter(adapter);

    }
    @Override
    public int getLayoutId() {
        return R.layout.elective_list_main_layout;
    }
}
