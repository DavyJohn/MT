package com.zzh.mt.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.zzh.mt.R;
import com.zzh.mt.base.BaseActivity;
import com.zzh.mt.base.CommonAdapter;
import com.zzh.mt.base.MultiItemTypeAdapter;
import com.zzh.mt.base.MyApplication;
import com.zzh.mt.base.ViewHolder;
import com.zzh.mt.http.callback.SpotsCallBack;
import com.zzh.mt.mode.CourseActivityArrangement;
import com.zzh.mt.utils.CommonUtil;
import com.zzh.mt.utils.Contants;
import com.zzh.mt.utils.SharedPreferencesUtil;
import com.zzh.mt.widget.DividerItemDecoration;

import java.util.LinkedHashMap;
import java.util.LinkedList;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Response;

/**
 * Created by 腾翔信息 on 2017/5/23.
 */

public class ScheduleDateActivity extends BaseActivity {
    private static final String TAG = ScheduleDateActivity.class.getSimpleName();
    private CommonAdapter<CourseActivityArrangement.activityListData> adapter;
    private LinkedList<CourseActivityArrangement.activityListData> list = new LinkedList<>();
    @BindView(R.id.date_schedule_text)
    TextView mDate;
    @BindView(R.id.schedule_date_recycler)
    RecyclerView mRecycler;
    @OnClick(R.id.schedule_left) void sub (){
        showToast("－1");
    }
    @OnClick(R.id.schedule_right) void add(){
        showToast("＋1");
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyApplication.getInstance().add(this);
        getToolBar().setTitle(getIntent().getStringExtra("Course"));
        getInfo();
    }

    private void initview(){

        mRecycler.setLayoutManager(new LinearLayoutManager(this));
        mRecycler.setHasFixedSize(true);
        mRecycler.addItemDecoration(new DividerItemDecoration(mContext,DividerItemDecoration.VERTICAL_LIST));
        adapter = new CommonAdapter<CourseActivityArrangement.activityListData>(mContext,R.layout.schedule_date_recycler_item_layout,list) {
            @Override
            protected void convert(ViewHolder holder, final CourseActivityArrangement.activityListData s, final int position) {
                holder.setText(R.id.schedule_date_item_time,s.getStartTime());
                holder.setText(R.id.schedule_date_item_title,s.getGroupName());
                //设置小组颜色 和 备注图标
                if (s.getGroupId() == null || TextUtils.isEmpty(s.getGroupId())){
                    //普通
                    holder.setVisible(R.id.group_view,false);
                    holder.setVisible(R.id.schedule_date_remarks_image,true);
                }else {
                    //小组活动
                    holder.setVisible(R.id.schedule_date_remarks_image,false);
                    holder.setVisible(R.id.group_view,true);
                }

                // TODO: 2017/6/3  设置备注过的样式

                holder.setOnClickListener(R.id.schedule_date_remarks_image, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext,MyRemarksTwoActivity.class);
                        intent.putExtra("GroupId",s.getGroupId());
                        intent.putExtra("time",s.getStartTime());
                        // TODO: 2017/6/3 课程名称传递
                        intent.putExtra("name",getIntent().getStringExtra("Course"));
                        intent.putExtra("activityTypeName",s.getActivityTypeName());
                        intent.putExtra("activityId",s.getId());
                        intent.putExtra("courseNoId",getIntent().getStringExtra("courseNoId"));
                        startActivity(intent);

                    }
                });
            }
        };
        mRecycler.setAdapter(adapter);
        adapter.setOnItemClickListener(new CommonAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                if (list.get(position).getGroupId() != null || !TextUtils.isEmpty(list.get(position).getGroupId())){
                    Intent intent = new Intent(mContext,GroupActivity.class);
                    intent.putExtra("time",list.get(position).getStartTime());
                    intent.putExtra("endtime",list.get(position).getEndTime());
                    intent.putExtra("GroupId",list.get(position).getId());
                    intent.putExtra("courseNoId",getIntent().getStringExtra("courseNoId"));
                    intent.putExtra("activityTypeName",list.get(position).getActivityTypeName());
                    intent.putExtra("activityId",list.get(position).getId());
                    intent.putExtra("time",list.get(position).getStartTime());
                    intent.putExtra("name",getIntent().getStringExtra("Course"));
                    startActivity(intent);
                }
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
    }

    private void getInfo(){
        LinkedHashMap<String,String> map = new LinkedHashMap<>();
        map.put("courseNoId",getIntent().getStringExtra("courseNoId"));
        map.put("userId", SharedPreferencesUtil.getInstance(mContext).getString("userid"));
        map.put("appVersion", CommonUtil.getVersion(mContext));
        map.put("digest","");
        map.put("ostype","android");
        map.put("uuid",CommonUtil.android_id(mContext));
        mOkHttpHelper.post(mContext, Contants.BASEURL + Contants.CourseActivityArrangement, map, TAG, new SpotsCallBack<CourseActivityArrangement>(mContext) {
            @Override
            public void onSuccess(Response response, CourseActivityArrangement data) {
                if (data.getCode().equals("200")){
                    list.clear();
                    list.addAll(data.getActivityList());
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
        return R.layout.schedule_data_main_layout;
    }
}
