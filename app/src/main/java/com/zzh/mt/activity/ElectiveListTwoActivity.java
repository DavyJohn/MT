package com.zzh.mt.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.zzh.mt.R;
import com.zzh.mt.base.BaseActivity;
import com.zzh.mt.base.CommonAdapter;
import com.zzh.mt.base.MyApplication;
import com.zzh.mt.base.ViewHolder;
import com.zzh.mt.widget.DividerItemDecoration;

import java.util.LinkedList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by 腾翔信息 on 2017/5/22.
 */

public class ElectiveListTwoActivity extends BaseActivity {

    private static final String TAG = ElectiveListActivity.class.getSimpleName();
    private CommonAdapter<Integer> adapter;
    private LinkedList<Integer> list = new LinkedList<>();
    @BindView(R.id.elective_list_two_recycler)
    RecyclerView mRecycler;
    @BindView(R.id.elective_list_title)
    TextView mText;
    @OnClick(R.id.elective_list_two_dedails) void details(){
        Intent intent = new Intent(mContext,CourseDetailsActivity.class);
        intent.putExtra("Course",mText.getText().toString());
        startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyApplication.getInstance().add(this);
        getToolBar().setTitle("选修列表");
        initview();
    }

    private void initview(){
        for (int i=0;i<5;i++){
            list.add(i);
        }
        mRecycler.setLayoutManager(new LinearLayoutManager(this));
        mRecycler.setHasFixedSize(true);
        mRecycler.addItemDecoration(new DividerItemDecoration(mContext,DividerItemDecoration.VERTICAL_LIST));
        //// TODO: 2017/5/25 checkbox 修改
        adapter = new CommonAdapter<Integer>(mContext,R.layout.elective_list_two_recycler_item_layout,list) {
            @Override
            protected void convert(ViewHolder holder, Integer integer, int position) {
                if (position == 1){
                    holder.setChecked(R.id.elective_checkbox,true);
                }
            }

        };

        mRecycler.setAdapter(adapter);

    }

    @Override
    public int getLayoutId() {
        return R.layout.elective_list_two_main_layout;
    }
}
