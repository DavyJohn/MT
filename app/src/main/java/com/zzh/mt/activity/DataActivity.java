package com.zzh.mt.activity;

import android.content.Intent;
import android.os.Bundle;
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
 * Created by 腾翔信息 on 2017/5/15.
 */

public class DataActivity extends BaseActivity {
    @BindView(R.id.data_recycler)
    RecyclerView mRecycler;
    private LinkedList<String> list = new LinkedList<>();
    private CommonAdapter<String> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getToolBar().setTitle(R.string.Course_materials);
        MyApplication.getInstance().add(this);
        initview();
    }

    private void initview(){
        for (int i=0;i<7;i++){
            list.add(i+"");
        }
        mRecycler.setHasFixedSize(true);
        mRecycler.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CommonAdapter<String>(mContext,R.layout.data_recyclerview_item_layout,list) {
            @Override
            protected void convert(ViewHolder holder, final String s, int position) {
                holder.setOnClickListener(R.id.data_recycler_item_details, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext,CourseDetailsActivity.class);
                        intent.putExtra("Course",s);
                        startActivity(intent);
                    }
                });

                //课程资料

                holder.setOnClickListener(R.id.data_recycler_item_image_materials, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(mContext,MaterialsActivity.class) );
                    }
                });
            }
        };
        mRecycler.setAdapter(adapter);
    }

    @Override
    public int getLayoutId() {
        return R.layout.data_main_layout;
    }
}
