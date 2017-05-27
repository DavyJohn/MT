package com.zzh.mt.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.zzh.mt.R;
import com.zzh.mt.base.BaseActivity;
import com.zzh.mt.base.CommonAdapter;
import com.zzh.mt.base.MultiItemTypeAdapter;
import com.zzh.mt.base.MyApplication;
import com.zzh.mt.base.ViewHolder;
import com.zzh.mt.widget.DividerItemDecoration;

import java.util.LinkedList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by 腾翔信息 on 2017/5/23.
 */

public class ScheduleDateActivity extends BaseActivity {
    private static final String TAG = ScheduleDateActivity.class.getSimpleName();
    private CommonAdapter<String> adapter;
    private LinkedList<String> list = new LinkedList<>();
    @BindView(R.id.date_schedule_text)
    TextView mDate;
    @BindView(R.id.schedule_date_recycler)
    RecyclerView mRecycler;
    @OnClick(R.id.schedule_left) void sub (){
        showToast("－1");
    }
    @OnClick(R.id.schedule_right) void add(){
        showToast("＋1");
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyApplication.getInstance().add(this);
//        getToolBar().setTitle(getIntent().getStringExtra("Course"));
        getToolBar().setTitle("javascript基础");
        initview();
    }

    private void initview(){
        for (int i=0;i<5;i++){
            list.add(i+"");
        }
        mRecycler.setLayoutManager(new LinearLayoutManager(this));
        mRecycler.setHasFixedSize(true);
        mRecycler.addItemDecoration(new DividerItemDecoration(mContext,DividerItemDecoration.VERTICAL_LIST));
        adapter = new CommonAdapter<String>(mContext,R.layout.schedule_date_recycler_item_layout,list) {
            @Override
            protected void convert(ViewHolder holder, String s, final int position) {
                if (position ==0 || position == 1){
                    holder.setVisible(R.id.schedule_date_remarks_show_image,true);
                    if (position == 1){
                        holder.setVisible(R.id.schedule_date_remarks_show_image,false);
                    }
                }
                if (position == 2 || position == 3){
                    holder.setVisible(R.id.group_view,true);
                    holder.setVisible(R.id.schedule_date_remarks_image,false);
                }

                holder.setOnClickListener(R.id.schedule_date_remarks_image, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext,MyRemarksTwoActivity.class);
                        intent.putExtra("postion",position);
                        startActivity(intent);

                    }
                });
            }
        };
        mRecycler.setAdapter(adapter);
        adapter.setOnItemClickListener(new CommonAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                if (position == 0 || position ==1 ||position == 4 ){

                }else {
                    startActivity(new Intent(mContext,GroupActivity.class));
                }

            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
    }
    @Override
    public int getLayoutId() {
        return R.layout.schedule_data_main_layout;
    }
}
