package com.zzh.mt.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.zhy.http.okhttp.callback.StringCallback;
import com.zzh.mt.R;
import com.zzh.mt.base.BaseActivity;
import com.zzh.mt.base.CommonAdapter;
import com.zzh.mt.base.MultiItemTypeAdapter;
import com.zzh.mt.base.MyApplication;
import com.zzh.mt.base.ViewHolder;
import com.zzh.mt.http.callback.SpotsCallBack;
import com.zzh.mt.mode.DepartmentData;
import com.zzh.mt.utils.CommonUtil;
import com.zzh.mt.utils.Contants;
import com.zzh.mt.widget.DividerItemDecoration;

import java.util.LinkedHashMap;
import java.util.LinkedList;

import butterknife.BindView;
import okhttp3.Response;

/**
 * Created by 腾翔信息 on 2017/6/1.
 */

public class DeparNameActivity extends BaseActivity {
    private static final String TAG = DeparNameActivity.class.getSimpleName();

    @BindView(R.id.deparname_recycler)
    RecyclerView mRecycler;
    private CommonAdapter<DepartmentData.DeList> adapter;
    private LinkedList<DepartmentData.DeList> list = new LinkedList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyApplication.getInstance().add(this);
        getToolBar().setTitle(getString(R.string.depar_name));
        getInfo();

    }
    private void initview(){
        mRecycler.addItemDecoration(new DividerItemDecoration(mContext,DividerItemDecoration.VERTICAL_LIST));
        mRecycler.setLayoutManager(new LinearLayoutManager(this));
        mRecycler.setHasFixedSize(true);
        adapter = new CommonAdapter<DepartmentData.DeList>(mContext,R.layout.deparment_item_main_layout,list) {
            @Override
            protected void convert(ViewHolder holder, DepartmentData.DeList deList, int position) {
                holder.setText(R.id.deparment_item_title,deList.getDepartmentName());
            }
        };
        mRecycler.setAdapter(adapter);

        adapter.setOnItemClickListener(new CommonAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                Contants.Deparmentid = list.get(position).getId();
                Contants.Deparmentname = list.get(position).getDepartmentName();
                finish();
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
    }

    private void getInfo(){
        LinkedHashMap<String,String> map = new LinkedHashMap<>();
        map.put("appVersion", CommonUtil.getVersion(mContext));
        map.put("digest","");
        map.put("ostype","android");
        map.put("uuid",CommonUtil.android_id(mContext));
        mOkHttpHelper.post(mContext, Contants.BASEURL + Contants.DEPARTMENTLIST, map, TAG, new SpotsCallBack<DepartmentData>(mContext) {
            @Override
            public void onSuccess(Response response, DepartmentData data) {
                if (data.getCode().equals("200")){
                    list.clear();
                    list.addAll(data.getDepartmentList());
                    initview();
                }
            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });

    }
    @Override
    public int getLayoutId() {
        return R.layout.deparname_main_layout;
    }
}
