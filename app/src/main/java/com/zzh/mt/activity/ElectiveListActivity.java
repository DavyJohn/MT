package com.zzh.mt.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.zzh.mt.R;
import com.zzh.mt.base.BaseActivity;
import com.zzh.mt.base.CommonAdapter;
import com.zzh.mt.base.MyApplication;
import com.zzh.mt.base.ViewHolder;
import com.zzh.mt.http.callback.SpotsCallBack;
import com.zzh.mt.mode.CurriculumData;
import com.zzh.mt.utils.CommonUtil;
import com.zzh.mt.utils.Contants;
import com.zzh.mt.utils.SharedPreferencesUtil;

import java.util.LinkedHashMap;
import java.util.LinkedList;

import butterknife.BindView;
import okhttp3.Response;

/**
 * Created by 腾翔信息 on 2017/5/22.
 */

public class ElectiveListActivity extends BaseActivity{

    private static final String TAG = ElectiveListActivity.class.getSimpleName();
    private LinkedList<CurriculumData.Curriculum> list =  new LinkedList<>();
    private CommonAdapter<CurriculumData.Curriculum> adapter;

    @BindView(R.id.selctive_list_swipe)
    SwipeRefreshLayout mSwipe;
    @BindView(R.id.elective_list_recycler)
    RecyclerView mRecycler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyApplication.getInstance().add(this);
        getToolBar().setTitle(getString(R.string.elective_data));
        getinfo();
        initview();
    }

    private void initview(){

        mSwipe.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);

        mRecycler.setLayoutManager(new LinearLayoutManager(this));
        mRecycler.setHasFixedSize(true);
        mSwipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (mSwipe.isRefreshing() == true){
                    mSwipe.setRefreshing(false);
                    getinfo();
                }
            }
        });
        adapter = new CommonAdapter<CurriculumData.Curriculum>(mContext,R.layout.elective_list_recycler_item_layout,list) {
            @Override
            protected void convert(ViewHolder holder, final CurriculumData.Curriculum d, int position) {
                holder.setOnClickListener(R.id.training_num, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //查询课程培训场次
                        Intent intent = new Intent(mContext,ElectiveListTwoActivity.class);
                        if (Contants.LANGUAGENEM == 0){
                            intent.putExtra("Course",d.getChineseName());
                        }else if (Contants.LANGUAGENEM == 1){
                            intent.putExtra("Course",d.getEnglishName());
                        }

                        intent.putExtra("courseId",d.getId());
                        startActivity(intent);
                    }
                });

                holder.setOnClickListener(R.id.elective_list_details, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext,CourseDetailsActivity.class);
                        if (Contants.LANGUAGENEM == 0){
                            intent.putExtra("Course",d.getChineseName());
                        }else if (Contants.LANGUAGENEM == 1){
                            intent.putExtra("Course",d.getEnglishName());
                        }
                        intent.putExtra("courseId",d.getId());
                        startActivity(intent);
                    }
                });
                if (d.getCanModify().equals("0")){
                    holder.setClick(R.id.training_num,false);
                    holder.setImageDrawable(R.id.training_num, ContextCompat.getDrawable(mContext,R.drawable.click_training_unsel));
                }
                holder.setImageUrl(R.id.elective_list_image,d.getPictureUrl(),"2");
                if (Contants.LANGUAGENEM == 0){
                    holder.setText(R.id.course_title,d.getChineseName());
                }else if (Contants.LANGUAGENEM == 1){
                    holder.setText(R.id.course_title,d.getEnglishName());
                }
                if (Contants.LANGUAGENEM == 0){
                    holder.setText(R.id.elective_list_time,d.getClassHours()+getString(R.string.day));
                }else {
                    holder.setText(R.id.elective_list_time,d.getClassHours()+getString(R.string.day));
                }

            }

        };

        mRecycler.setAdapter(adapter);

    }

    private void getinfo(){
        LinkedHashMap<String,String> map = new LinkedHashMap<>();
        map.put("appVersion", CommonUtil.getVersion(mContext));
        map.put("digest","");
        map.put("ostype","android");
        map.put("uuid",CommonUtil.android_id(mContext));
        map.put("userId", SharedPreferencesUtil.getInstance(mContext).getString("userid"));
        mOkHttpHelper.post(mContext, Contants.BASEURL + Contants.CurriculumNoChoice, map, TAG, new SpotsCallBack<CurriculumData>(mContext) {
            @Override
            public void onSuccess(Response response, CurriculumData data) {
                if (data.getCode().equals("200")){
                    list.clear();
                    list.addAll(data.getCourseList());
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
        return R.layout.elective_list_main_layout;
    }
}
