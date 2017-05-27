package com.zzh.mt.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.zzh.mt.R;
import com.zzh.mt.adapter.ElectiveListAdapter;
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
    private ElectiveListAdapter adapter;
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
        list.clear();
        for (int i=0;i<5;i++){
            list.add(i);
        }
        mRecycler.setLayoutManager(new LinearLayoutManager(this));
        mRecycler.setHasFixedSize(true);
        mRecycler.addItemDecoration(new DividerItemDecoration(mContext,DividerItemDecoration.VERTICAL_LIST));
        //// TODO: 2017/5/25 checkbox 修改
        adapter = new ElectiveListAdapter(mContext);
        mRecycler.setAdapter(adapter);

        adapter.setOnClickItemListener(new ElectiveListAdapter.OnClickItemListener() {
            @Override
            public void onClickItem(View itemview, CheckBox view, int postion) {
                adapter.addPostion(postion);

            }
        });

    }

    @Override
    public int getLayoutId() {
        return R.layout.elective_list_two_main_layout;
    }
}
