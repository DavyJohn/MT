package com.zzh.mt.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.SearchView;
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
 * Created by 腾翔信息 on 2017/5/24.
 */

public class MaterialsActivity extends BaseActivity implements SearchView.OnQueryTextListener {
    @BindView(R.id.materials_recycler)
    RecyclerView mRecycler;
    @BindView(R.id.all_check)
    TextView mTextCheck;
    @BindView(R.id.downing)
    TextView mTextDown;
    @BindView(R.id.searchview_root)
    LinearLayout root;
    @BindView(R.id.search_materials)
    SearchView mSearch;
    @OnClick(R.id.all_check) void check(){

        if (mTextCheck.getText().toString().equals("全选")){
            mTextCheck.setText("取消");
            initview();
        }else {
            mTextCheck.setText("全选");
            initview();
        }
    }
    @OnClick(R.id.downing) void down(){

        if (mTextDown.getText().toString().equals("确认下载")){
            startActivity(new Intent(mContext,DownActivity.class));
            mTextDown.setText("正在下载");
        }else {
            mTextDown.setText("确认下载");
        }
    }
    CommonAdapter<String> adapter;
    private LinkedList<String> list = new LinkedList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getToolBar().setTitle(R.string.Course_materials);
        MyApplication.getInstance().add(this);
        initview();


    }

    private void initview(){
        list.clear();
        for (int i=0;i<8;i++){
            list.add(i+"");
        }
        mSearch.clearFocus();
        mSearch.setOnQueryTextListener(this);
        mSearch.onActionViewExpanded();
        mSearch.setIconifiedByDefault(false);
        mRecycler.setLayoutManager(new LinearLayoutManager(this));
        mRecycler.setHasFixedSize(true);
        mRecycler.addItemDecoration(new DividerItemDecoration(mContext,DividerItemDecoration.VERTICAL_LIST));
        adapter = new CommonAdapter<String>(mContext,R.layout.materials_item_main_layout,list) {
            @Override
            protected void convert(final ViewHolder holder, String s, int position) {
//                holder.setOnCheckedChangeListener(R.id.materials_check_box, new CompoundButton.OnCheckedChangeListener() {
//                    @Override
//                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                        if (isChecked){
//                            holder.setChecked(R.id.materials_check_box,false);
//                        }else {
//                            holder.setChecked(R.id.materials_check_box,true);
//                        }
//                    }
//                });
                if (mTextCheck.getText().toString().equals("取消")){
                    holder.setChecked(R.id.materials_check_box,true);
                }else {
                    holder.setChecked(R.id.materials_check_box,false);
                }
            }
        };
        mRecycler.setAdapter(adapter);
        adapter.setOnItemClickListener(new CommonAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {

            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
    }
    @Override
    public int getLayoutId() {
        return R.layout.materials_main_layout;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        showToast(query);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {

        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        root.setFocusable(true);
        root.setFocusableInTouchMode(true);
        root.requestFocus();
    }
}
