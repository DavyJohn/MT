package com.zzh.mt.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.zzh.mt.R;
import com.zzh.mt.base.BaseActivity;
import com.zzh.mt.base.CommonAdapter;
import com.zzh.mt.base.MultiItemTypeAdapter;
import com.zzh.mt.base.MyApplication;
import com.zzh.mt.base.ViewHolder;
import com.zzh.mt.widget.MysearchView;
import com.zzh.mt.widget.sidebar.WaveSideBar;

import java.util.LinkedList;

import butterknife.BindView;

/**
 * Created by 腾翔信息 on 2017/5/15.
 */

public class ClassmateActivity extends BaseActivity {

    @BindView(R.id.classmate_recycler)
    RecyclerView mRecycler;
    @BindView(R.id.classmate_side)
    WaveSideBar mBar;
    @BindView(R.id.classmate_search)
    MysearchView mSearch;
    CommonAdapter<String> adapter;
    LinkedList<String> list = new LinkedList<>();
    String [] data = {"A","B","C","D","E","F","G"};
    String [] name = {"张三","里斯","王马死","王麻子","历史","请示黄","你妈妈"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getToolBar().setTitle(getString(R.string.check_out_classmate));
        MyApplication.getInstance().add(this);
        initview();
    }

    private void initview(){
        list.clear();
        for (int i=0;i<data.length;i++){
            list.add(data[i]);
        }
        mRecycler.setLayoutManager(new LinearLayoutManager(this));
        mRecycler.setHasFixedSize(true);
        adapter = new CommonAdapter<String>(mContext,R.layout.classmate_item_main_layout,list) {
            @Override
            protected void convert(ViewHolder holder, String s, int position) {
                if (position ==0 || list.get(position-1).equals(list.get(position))){
                    holder.setVisible(R.id.tv_index,true);
                    holder.setText(R.id.tv_index,list.get(position));
                }else
                    {
                        holder.setVisible(R.id.tv_index,false);
                    }
            }
        };
        adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                Intent intent = new Intent(mContext,ClassmateInfoActivity.class);
                intent.putExtra("name",name[position]);
                startActivity(intent);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        mRecycler.setAdapter(adapter);
        mBar.setIndexItems(data);
        mBar.setOnSelectIndexItemListener(new WaveSideBar.OnSelectIndexItemListener() {
            @Override
            public void onSelectIndexItem(String s) {
                for (int i = 0;i<data.length;i++){
                    if (data[i].equals(s)){
                        ((LinearLayoutManager) mRecycler.getLayoutManager()).scrollToPositionWithOffset(i, 0);
                    }
                }
            }
        });
    }
    @Override
    public int getLayoutId() {
        return R.layout.classmate_main_activity;
    }
}
