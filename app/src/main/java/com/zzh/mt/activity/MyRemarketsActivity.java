package com.zzh.mt.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.zzh.mt.R;
import com.zzh.mt.base.BaseActivity;
import com.zzh.mt.base.CommonAdapter;
import com.zzh.mt.base.MultiItemTypeAdapter;
import com.zzh.mt.base.MyApplication;
import com.zzh.mt.base.ViewHolder;
import com.zzh.mt.http.callback.SpotsCallBack;
import com.zzh.mt.mode.AppRemarks;
import com.zzh.mt.utils.CommonUtil;
import com.zzh.mt.utils.Contants;
import com.zzh.mt.utils.SharedPreferencesUtil;

import java.util.LinkedHashMap;
import java.util.LinkedList;

import butterknife.BindView;
import okhttp3.Response;

/**
 * Created by 腾翔信息 on 2017/5/23.
 */

public class MyRemarketsActivity extends BaseActivity {

    private static final String TAG = MyRemarketsActivity.class.getSimpleName();
    private LinkedList<AppRemarks.remarkListData> list = new LinkedList<>();
    private CommonAdapter<AppRemarks.remarkListData> adapter;
    @BindView(R.id.my_remarks_swipe)
    SwipeRefreshLayout mSwipe;
    @BindView(R.id.my_remarks_recycler)
    RecyclerView mRecyler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getToolBar().setTitle(R.string.my_remarks);
        MyApplication.getInstance().add(this);
        getInfo();
    }

    private void initview(){
        mSwipe.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);

        mSwipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (mSwipe.isRefreshing() == true){
                    mSwipe.setRefreshing(false);
                    getInfo();
                }
            }
        });

        mRecyler.setHasFixedSize(true);
        mRecyler.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CommonAdapter<AppRemarks.remarkListData>(mContext,R.layout.my_remarks_recycler_item_layout,list) {
            @Override
            protected void convert(ViewHolder holder, AppRemarks.remarkListData s, int position) {
                // TODO: 2017/6/13 活动类型中英文
                holder.setText(R.id.my_remarks_recycler_item_typename,s.getActivityTypeName());
                holder.setText(R.id.my_remarks_recycler_item_center,s.getGroupName());
                holder.setText(R.id.my_remarks_recycler_item_attendtime,s.getAttendTime().substring(0,10));
                holder.setText(R.id.my_remarks_recycler_item_star_end,s.getStartTime().substring(0,10)+"—"+s.getEndTime().substring(0,10));


            }
        };
        mRecyler.setAdapter(adapter);
        adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                Intent intent = new Intent(mContext,MyRemarksTwoActivity.class);
                intent.putExtra("activityTypeName",list.get(position).getActivityTypeName());
                intent.putExtra("time",list.get(position).getStartTime());
                if (Contants.LANGUAGENEM == 0){
                    intent.putExtra("name",list.get(position).getChineseName());
                }else if (Contants.LANGUAGENEM == 1){
                    intent.putExtra("name",list.get(position).getEnglishName());
                }
                intent.putExtra("activityId",list.get(position).getActivityId());
                intent.putExtra("courseNoId",list.get(position).getCurriculumNoId());
                startActivity(intent);
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
        map.put("userId", SharedPreferencesUtil.getInstance(mContext).getString("userid"));
        mOkHttpHelper.post(mContext, Contants.BASEURL + Contants.AppRemarks, map, TAG, new SpotsCallBack<AppRemarks>(mContext) {
            @Override
            public void onSuccess(Response response, AppRemarks data) {
                mSwipe.setRefreshing(false);
                if (data.getCode().equals("200")){
                    list.clear();
                    list.addAll(data.getRemarkList());
                    initview();
                }else {
                    showMessageDialog(data.getMessage(),mContext);
                }

            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });
    }
    @Override
    public int getLayoutId() {
        return R.layout.my_remarks_main_layout;
    }
}
