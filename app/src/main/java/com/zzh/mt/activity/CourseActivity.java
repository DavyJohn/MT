package com.zzh.mt.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.zzh.mt.R;
import com.zzh.mt.base.BaseActivity;
import com.zzh.mt.base.CommonAdapter;
import com.zzh.mt.base.ViewHolder;
import com.zzh.mt.http.callback.SpotsCallBack;
import com.zzh.mt.mode.ClassTimeData;
import com.zzh.mt.utils.CommonUtil;
import com.zzh.mt.utils.Contants;
import com.zzh.mt.utils.SharedPreferencesUtil;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Response;

/**
 * Created by 腾翔信息 on 2017/5/15.
 */

public class CourseActivity extends BaseActivity {
    private static final String TAG = CourseActivity.class.getSimpleName();
    private CommonAdapter<String> adapter;
    private MenuItem item = null;
    private String[] date = {"1天","2天","3天"};
    private LinkedList<String> list = new LinkedList<>();
    @OnClick(R.id.elective_layout) void elective(){
        startActivity(new Intent(mContext,ElectiveListActivity.class));
    }
    @BindView(R.id.elective_recycler)
    RecyclerView mRecycler;
    @BindView(R.id.class_time)
    TextView mTextTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getToolBar().setTitle(getString(R.string.my_courde));
        classTime();
        initview();
    }


    private void initview(){
        //利用item.setText来跟新menu
        list.clear();
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
//                item.setTitle("已修：4/10");
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    //获取 开始-截至时间 已选课时 总课时
    private void classTime(){
        LinkedHashMap<String,String> map = new LinkedHashMap<>();
        map.put("userId", SharedPreferencesUtil.getInstance(mContext).getString("userid"));
        map.put("appVersion", CommonUtil.getVersion(mContext));
        map.put("digest","");
        map.put("ostype","android");
        map.put("uuid",CommonUtil.android_id(mContext));
        mOkHttpHelper.post(mContext, Contants.BASEURL + Contants.CLASSGOALS, map, TAG, new SpotsCallBack<ClassTimeData>(mContext) {
            @Override
            public void onSuccess(Response response, ClassTimeData data) {
                if (data.getCode().equals("200")){
                    int sumtotal = Integer.parseInt(data.getTotalClassHoursElective())+Integer.parseInt(data.getTotalClassHoursRequired());
                    int sunhave = Integer.parseInt(data.getHaveClassHoursElective())+Integer.parseInt(data.getHaveClassHoursRequired());
                    item.setTitle("已修："+sunhave+"/"+sumtotal);
                    mTextTime.setText(data.getStartTime().substring(6,7)+"月"+data.getStartTime().substring(8,10)+"日"+"——"+data.getEndTime().substring(6,7)+"月"+data.getEndTime().substring(8,10)+"日");
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
        return R.layout.course_main_activity;
    }
}
