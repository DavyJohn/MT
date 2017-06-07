package com.zzh.mt.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.ArrayMap;
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
import com.zzh.mt.utils.StringUtils;
import com.zzh.mt.widget.DividerItemDecoration;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

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
    private LinkedList<CourseActivityArrangement.activityListData> da = new LinkedList<>();
    int num ;//用来记录选中的第几个
    Map<String,LinkedList<CourseActivityArrangement.activityListData>> hashMap = new ArrayMap<>();
    @BindView(R.id.date_schedule_text)
    TextView mDate;
    @BindView(R.id.schedule_date_recycler)
    RecyclerView mRecycler;
    LinkedList<String> listData = new LinkedList<>();

    @OnClick(R.id.schedule_left) void sub (){
        if (num != 0){
            num = num -1;
            mDate.setText(listData.get(num));
            findViewById(R.id.schedule_right).setVisibility(View.VISIBLE);

            switch (listData.size()){
                case 1:
                    findViewById(R.id.schedule_left).setVisibility(View.INVISIBLE);
                    findViewById(R.id.schedule_right).setVisibility(View.INVISIBLE);
                    break;
                case 2:
                    if (num==0){
                        findViewById(R.id.schedule_left).setVisibility(View.INVISIBLE);
                    }else if (num == 1){
                        findViewById(R.id.schedule_right).setVisibility(View.INVISIBLE);
                    }
                    break;
                case 3:
                    if (num==0){
                        findViewById(R.id.schedule_left).setVisibility(View.INVISIBLE);
                    }else if (num == 1){
                        findViewById(R.id.schedule_left).setVisibility(View.VISIBLE);
                        findViewById(R.id.schedule_right).setVisibility(View.VISIBLE);
                    }else if (num == 2){
                        findViewById(R.id.schedule_right).setVisibility(View.INVISIBLE);
                    }
                    break;

            }
        }
        //处理数据
        initview();

    }
    @OnClick(R.id.schedule_right) void add(){
        if (num+1< listData.size()){
            num = num +1;
            mDate.setText(listData.get(num));
            findViewById(R.id.schedule_left).setVisibility(View.VISIBLE);
            switch (listData.size()){
                case 1:
                    findViewById(R.id.schedule_left).setVisibility(View.INVISIBLE);
                    findViewById(R.id.schedule_right).setVisibility(View.INVISIBLE);
                    break;
                case 2:
                    if (num==0){
                        findViewById(R.id.schedule_left).setVisibility(View.INVISIBLE);
                    }else if (num == 1){
                        findViewById(R.id.schedule_right).setVisibility(View.INVISIBLE);
                    }
                    break;
                case 3:
                    if (num==0){
                        findViewById(R.id.schedule_left).setVisibility(View.INVISIBLE);
                    }else if (num == 1){
                        findViewById(R.id.schedule_left).setVisibility(View.VISIBLE);
                        findViewById(R.id.schedule_right).setVisibility(View.VISIBLE);
                    }else if (num == 2){
                        findViewById(R.id.schedule_right).setVisibility(View.INVISIBLE);
                    }
                    break;

            }
        }
        //处理数据
        initview();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyApplication.getInstance().add(this);
        getToolBar().setTitle(getIntent().getStringExtra("Course"));
        mRecycler.setLayoutManager(new LinearLayoutManager(this));
        mRecycler.setHasFixedSize(true);
        mRecycler.addItemDecoration(new DividerItemDecoration(mContext,DividerItemDecoration.VERTICAL_LIST));
        getInfo();
    }

    private void initview(){

        LinkedList<CourseActivityArrangement.activityListData> hasdata = new LinkedList<>();
        hasdata.addAll(hashMap.get(mDate.getText().toString()));
        adapter = new CommonAdapter<CourseActivityArrangement.activityListData>(mContext,R.layout.schedule_date_recycler_item_layout,hasdata) {
            @Override
            protected void convert(ViewHolder holder, final CourseActivityArrangement.activityListData s, final int position) {
                holder.setText(R.id.schedule_date_item_time,hashMap.get(mDate.getText().toString()).get(position).getStartTime().substring(0,10));
                holder.setText(R.id.schedule_date_item_title,hashMap.get(mDate.getText().toString()).get(position).getGroupName());
                //设置小组颜色 和 备注图标
                if (s.getType().equals("1")){
                    //普通
                    holder.setVisible(R.id.group_view,true);
                    holder.setVisible(R.id.schedule_date_remarks_image,true);
                    if (hashMap.get(mDate.getText().toString()).get(position).getColorLabel() == null || TextUtils.isEmpty(hashMap.get(mDate.getText().toString()).get(position).getColorLabel()) ){
                        holder.setBackgroundColor(R.id.group_view, ContextCompat.getColor(mContext,R.color.main_color));
                    }else {
                        holder.setBackgroundColor(R.id.group_view, Color.parseColor(hashMap.get(mDate.getText().toString()).get(position).getColorLabel()));

                    }
                    if (s.getHasRemark().equals("1")){
                        holder.setVisible(R.id.schedule_date_remarks_show_image,true);
                    }
                }else if (s.getType().equals("2")){
                    //休息
                    holder.setVisible(R.id.group_view,true);
                    holder.setVisible(R.id.schedule_date_remarks_image,true);
                    if (hashMap.get(mDate.getText().toString()).get(position).getColorLabel() == null || TextUtils.isEmpty(hashMap.get(mDate.getText().toString()).get(position).getColorLabel()) ){
                        holder.setBackgroundColor(R.id.group_view, ContextCompat.getColor(mContext,R.color.main_color));
                    }else {
                        holder.setBackgroundColor(R.id.group_view, Color.parseColor(hashMap.get(mDate.getText().toString()).get(position).getColorLabel()));

                    }
                    if (s.getHasRemark().equals("1")){
                        holder.setVisible(R.id.schedule_date_remarks_show_image,true);
                    }
                }else if (s.getType().equals("3")){
                    //小组活动
                    if (s.getHasRemark().equals("1")){
                        holder.setVisible(R.id.schedule_date_remarks_show_image,true);
                    }
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
        final LinkedHashMap<String,String> map = new LinkedHashMap<>();
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
                    listData.clear();
                    list.addAll(data.getActivityList());
                    if (list.size() != 0){
                        for (int i=0;i<list.size();i++){
                            listData.add(data.getActivityList().get(i).getStartTime().substring(0,10));
                        }
                        Set<String> set = new LinkedHashSet<String>();
                        set.addAll(listData);
                        listData.clear();
                        listData.addAll(set);

                        for (int b=0;b<listData.size();b++){
                            String key = listData.get(b);
                            LinkedList<CourseActivityArrangement.activityListData> da = new LinkedList<>();
                            for (int a=0;a<list.size();a++){
                                String tie = list.get(a).getStartTime().substring(0,10);
                                if (key.equals(tie)){
                                    da.add(list.get(a));
                                    hashMap.put(key,da);
                                }
                            }
                            System.out.print(hashMap);
                        }

                        for (int m=0;m<listData.size();m++){
                            if (listData.get(m).equals(CommonUtil.getData())){
                                mDate.setText(listData.get(m));
                                num = m;
                                break;
                            }else {
                                mDate.setText(listData.get(0));
                                num = 0;
                            }
                        }
                        switch (listData.size()){
                            case 1:
                                findViewById(R.id.schedule_left).setVisibility(View.INVISIBLE);
                                findViewById(R.id.schedule_right).setVisibility(View.INVISIBLE);
                                break;
                            case 2:
                                if (num==0){
                                    findViewById(R.id.schedule_left).setVisibility(View.INVISIBLE);
                                }else if (num == 1){
                                    findViewById(R.id.schedule_right).setVisibility(View.INVISIBLE);
                                }
                                break;
                            case 3:
                                if (num==0){
                                    findViewById(R.id.schedule_left).setVisibility(View.INVISIBLE);
                                }else if (num == 1){
                                    findViewById(R.id.schedule_left).setVisibility(View.VISIBLE);
                                    findViewById(R.id.schedule_right).setVisibility(View.VISIBLE);
                                }else if (num == 2){
                                    findViewById(R.id.schedule_right).setVisibility(View.INVISIBLE);
                                }
                                break;

                        }

                    }
                    initview();
//                    adapter.notifyDataSetChanged();
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
