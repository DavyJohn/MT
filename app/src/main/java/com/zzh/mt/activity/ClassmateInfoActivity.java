package com.zzh.mt.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.zzh.mt.R;
import com.zzh.mt.base.BaseActivity;
import com.zzh.mt.base.CommonAdapter;
import com.zzh.mt.base.MyApplication;
import com.zzh.mt.base.ViewHolder;
import com.zzh.mt.widget.DividerItemDecoration;

import java.util.LinkedList;

import butterknife.BindView;

/**
 * Created by 腾翔信息 on 2017/5/24.
 */

public class ClassmateInfoActivity extends BaseActivity {

    @BindView(R.id.class_info_recycler)
    RecyclerView mRecycler;
    private LinkedList<String> list = new LinkedList<>();
    private String [] data = {"中文名字","英文名字","品牌","部门","性别","星座","公司邮箱"};
    private String [] da = {"张杰","jason","牌牌","生产部","难","巨蟹","1111111@163.com"};
    CommonAdapter<String> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getToolBar().setTitle(getIntent().getStringExtra("name"));
        MyApplication.getInstance().add(this);
        initview();
    }

    private void initview(){
        for (int i=0;i<data.length;i++){
            list.add(data[i]);
        }
        mRecycler.setLayoutManager(new LinearLayoutManager(this));
        mRecycler.setHasFixedSize(true);
        mRecycler.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL_LIST));
        adapter = new CommonAdapter<String>(mContext,R.layout.classmate_info_item_main_layout,list) {
            @Override
            protected void convert(ViewHolder holder, String s, int position) {
                holder.setText(R.id.classmate_info_item_key,s);
                holder.setText(R.id.classmate_info_item_view,da[position]);
            }
        };
        mRecycler.setAdapter(adapter);
    }
    @Override
    public int getLayoutId() {
        return R.layout.classmate_info_main_layout;
    }
}
