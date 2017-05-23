package com.zzh.mt.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.zzh.mt.R;
import com.zzh.mt.base.BaseActivity;
import com.zzh.mt.base.CommonAdapter;
import com.zzh.mt.base.ViewHolder;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by 腾翔信息 on 2017/5/15.
 */

public class CourseActivity extends BaseActivity {

    private CommonAdapter<String> adapter;
    private MenuItem item = null;
    private String[] date = {"1天","2天","3天"};
    private LinkedList<String> list = new LinkedList<>();
    @OnClick(R.id.elective_layout) void elective(){
        startActivity(new Intent(mContext,ElectiveListActivity.class));
    }
    @BindView(R.id.elective_recycler)
    RecyclerView mRecycler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getToolBar().setTitle(getString(R.string.my_courde));
        initview();
    }


    private void initview(){
        //利用item.setText来跟新menu
        for (int i=0;i<date.length;i++){
            list.add(date[i]);
        }
        mRecycler.setLayoutManager(new LinearLayoutManager(this));
        mRecycler.setHasFixedSize(true);
        adapter = new CommonAdapter<String>(mContext,R.layout.elevtive_recycer_item_layout,list) {
            @Override
            protected void convert(ViewHolder holder, String s, final int position) {
                holder.setText(R.id.elevtive_date,s);
                holder.setOnClickListener(R.id.elevtive_recycler_item_detail, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext,CourseDetailsActivity.class);
                        intent.putExtra("Course",list.get(position));
                        startActivity(intent);

                    }
                });
            }
        };
        mRecycler.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.elective_menu,menu);
        item = menu.findItem(R.id.elective_menu_id);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.elective_menu_id:
                item.setTitle("已修：4/10");
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public int getLayoutId() {
        return R.layout.course_main_activity;
    }
}
