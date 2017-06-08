package com.zzh.mt.activity;

import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.zzh.mt.R;
import com.zzh.mt.base.BaseActivity;
import com.zzh.mt.base.CommonAdapter;
import com.zzh.mt.base.MyApplication;
import com.zzh.mt.base.ViewHolder;
import com.zzh.mt.http.callback.SpotsCallBack;
import com.zzh.mt.mode.GroupActivityInformation;
import com.zzh.mt.utils.CommonUtil;
import com.zzh.mt.utils.Contants;
import com.zzh.mt.utils.SharedPreferencesUtil;

import java.util.LinkedHashMap;
import java.util.LinkedList;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Response;

/**
 * Created by 腾翔信息 on 2017/5/25.
 */

public class GroupActivity extends BaseActivity {
    private static final String TAG = GroupActivity.class.getSimpleName();
    @OnClick(R.id.group_note) void note (){
        Intent intent = new Intent(mContext,MyRemarksTwoActivity.class);
        intent.putExtra("activityId",getIntent().getStringExtra("activityId"));
        intent.putExtra("courseNoId",getIntent().getStringExtra("courseNoId"));
        intent.putExtra("activityTypeName",getIntent().getStringExtra("activityTypeName"));
        intent.putExtra("time",getIntent().getStringExtra("time"));
        intent.putExtra("name",getIntent().getStringExtra("name"));
        startActivity(intent);
    }
    CommonAdapter<GroupActivityInformation.personListData> adapter ;
    private LinkedList<GroupActivityInformation.personListData> list = new LinkedList<>();
    @BindView(R.id.group_start)
    TextView mStart;
    @BindView(R.id.group_end)
    TextView mEnd;
    @BindView(R.id.group_recycler)
    RecyclerView mRecycler;
    @BindView(R.id.group_remark)
    TextView mTextmRenark;
    @BindView(R.id.group_name)
    TextView mName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyApplication.getInstance().add(this);
        getToolBar().setTitle(getString(R.string.group_activity));
        getInfo();
    }

    private void initview(){
        if (list.size() == 0){
            mRecycler.setVisibility(View.GONE);
            mName.setVisibility(View.VISIBLE);
            mName.setText("随堂分组");
        }else {
            mRecycler.setVisibility(View.VISIBLE);
            mName.setVisibility(View.GONE);
            mRecycler.setHasFixedSize(true);
            mRecycler.setLayoutManager(new LinearLayoutManager(this));
            adapter = new CommonAdapter<GroupActivityInformation.personListData>(mContext,R.layout.group_item_layout,list) {
                @Override
                protected void convert(ViewHolder holder, GroupActivityInformation.personListData s, int position) {
                    holder.setText(R.id.group_iten_name,"-  "+s.getNickName());
                }
            };
            mRecycler.setAdapter(adapter);
        }

    }
    private void getInfo(){
        LinkedHashMap<String,String> map = new LinkedHashMap<>();
        map.put("appVersion", CommonUtil.getVersion(mContext));
        map.put("digest","");
        map.put("userId", SharedPreferencesUtil.getInstance(mContext).getString("userid"));
        map.put("ostype","android");
        map.put("uuid",CommonUtil.android_id(mContext));
        map.put("groupId",getIntent().getStringExtra("activityId"));
        mOkHttpHelper.post(mContext, Contants.BASEURL + Contants.GroupActivityInformation, map, TAG, new SpotsCallBack<GroupActivityInformation>(mContext) {
            @Override
            public void onSuccess(Response response, GroupActivityInformation data) {
                if (data.getCode().equals("200")){
                    list.clear();
                    list.addAll(data.getPersonList());
                    mStart.setText(getIntent().getStringExtra("time").substring(0,10));
                    mEnd.setText(getIntent().getStringExtra("endtime").substring(0,10));
//                    mTextmRenark.setText(data.getRemarks().getInformation());
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
        return R.layout.group_main_layout;
    }
}
