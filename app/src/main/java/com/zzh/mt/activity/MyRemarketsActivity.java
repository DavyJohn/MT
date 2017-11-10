package com.zzh.mt.activity;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import com.zzh.mt.R;
import com.zzh.mt.adapter.RemarketAdapter;
import com.zzh.mt.base.BaseActivity;
import com.zzh.mt.base.CommonAdapter;
import com.zzh.mt.base.MultiItemTypeAdapter;
import com.zzh.mt.base.MyApplication;
import com.zzh.mt.base.ViewHolder;
import com.zzh.mt.http.callback.SpotsCallBack;
import com.zzh.mt.mode.AppRemarks;
import com.zzh.mt.mode.BaseData;
import com.zzh.mt.utils.CommonUtil;
import com.zzh.mt.utils.Contants;
import com.zzh.mt.utils.MdTools;
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
    private RemarketAdapter adapter;
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
        adapter = new RemarketAdapter(mContext);
        adapter.addData(list);
//        adapter = new CommonAdapter<AppRemarks.remarkListData>(mContext,R.layout.my_remarks_recycler_item_layout,list) {
//            @Override
//            protected void convert(ViewHolder holder, AppRemarks.remarkListData s, int position) {
//                // TODO: 2017/6/13 活动类型中英文
//                holder.setText(R.id.my_remarks_recycler_item_typename,s.getGroupName());
//                holder.setText(R.id.my_remarks_recycler_item_center,s.getChineseName());
//                holder.setText(R.id.my_remarks_recycler_item_attendtime,s.getStartTime().substring(0,10));
//                holder.setText(R.id.my_remarks_recycler_item_star_end,CommonUtil.getTime(s.getStartTime())+"—"+CommonUtil.getTime(s.getEndTime()));
//
//
//            }
//        };
        mRecyler.setAdapter(adapter);
        addItemSwipeHelper(mRecyler);

        adapter.setOnClickItemListener(new RemarketAdapter.OnClickItemListener() {
            @Override
            public void onClickItem(View view, int position) {
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
        });
    }
    private void getInfo(){
        LinkedHashMap<String,String> map = new LinkedHashMap<>();
        map.put("appVersion", CommonUtil.getVersion(mContext));
        map.put("ostype","android");
        map.put("uuid",CommonUtil.android_id(mContext));
        map.put("userId", SharedPreferencesUtil.getInstance(mContext).getString("userid"));
        map.put("digest", MdTools.sign_digest(map));
        mOkHttpHelper.post(mContext, Contants.BASEURL + Contants.AppRemarks, map, TAG, new SpotsCallBack<AppRemarks>(mContext) {
            @Override
            public void onSuccess(Response response, AppRemarks data) {
                mSwipe.setRefreshing(false);
                if (data.getCode().equals("200")){
                    list.clear();
                    list.addAll(data.getRemarkList());
                    initview();
                }else if (data.getCode().equals("110")){
                    goBack(data.getMessage(),mContext);
                }else {
                    showMessageDialog(data.getMessage(),mContext);
                }

            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });
    }

    private void addItemSwipeHelper(RecyclerView recyclerView) {
        ItemTouchHelper swipeToDismissTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,  ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false; //no drag-n-drop
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int index = viewHolder.getAdapterPosition();
                // TODO: 2016/8/3
                delete(list.get(index).getId());
//                adapter.notifyItemRemoved(index);
            }

            @Override
            public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                getDefaultUIUtil().clearView(((RemarketAdapter.ViewHolder) viewHolder).vItem);
                ((RemarketAdapter.ViewHolder) viewHolder).vBackground.setBackgroundColor(Color.TRANSPARENT);

            }

            @Override
            public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
                if (viewHolder != null) {
                    getDefaultUIUtil().onSelected(((RemarketAdapter.ViewHolder) viewHolder).vItem);
                }
            }

            @Override
            public void onChildDraw(Canvas canvas, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

                getDefaultUIUtil().onDraw(canvas, recyclerView, ((RemarketAdapter.ViewHolder) viewHolder).vItem, dX, dY, actionState, isCurrentlyActive);
                if (dX < 0) { // 向左滑动是的提示
                    ((RemarketAdapter.ViewHolder) viewHolder).vBackground.setBackgroundResource(R.color.red_light);
                }
            }
        });
        swipeToDismissTouchHelper.attachToRecyclerView(recyclerView);
    }
    //删除
    private void delete(String id){
        LinkedHashMap<String,String> map = new LinkedHashMap<>();
        map.put("appVersion", CommonUtil.getVersion(mContext));
        map.put("ostype","android");
        map.put("uuid",CommonUtil.android_id(mContext));
        map.put("userId", SharedPreferencesUtil.getInstance(mContext).getString("userid"));
        map.put("remarkId",id);
        map.put("digest", MdTools.sign_digest(map));
        mOkHttpHelper.post(mContext,Contants.BASEURL+Contants.DeleteId,map,TAG, new SpotsCallBack<BaseData>(mContext) {
            @Override
            public void onSuccess(Response response, BaseData data) {
                showToast(data.getMessage());
                getInfo();
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
