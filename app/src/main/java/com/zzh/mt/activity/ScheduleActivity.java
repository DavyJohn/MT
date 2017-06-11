package com.zzh.mt.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zzh.mt.R;
import com.zzh.mt.base.BaseActivity;
import com.zzh.mt.base.CommonAdapter;
import com.zzh.mt.base.MyApplication;
import com.zzh.mt.base.ViewHolder;
import com.zzh.mt.http.callback.SpotsCallBack;
import com.zzh.mt.mode.ClassTimeData;
import com.zzh.mt.mode.CurriculumNoByUser;
import com.zzh.mt.utils.CommonUtil;
import com.zzh.mt.utils.Contants;
import com.zzh.mt.utils.SharedPreferencesUtil;

import java.util.LinkedHashMap;
import java.util.LinkedList;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Response;

/**
 * Created by 腾翔信息 on 2017/5/15.
 */

public class ScheduleActivity extends BaseActivity {
    private static final String TAG = ScheduleActivity.class.getSimpleName();
    CommonAdapter<CurriculumNoByUser.courseNoListData> adapter;
    private LinkedList<CurriculumNoByUser.courseNoListData> list = new LinkedList<>();
    @BindView(R.id.schedule_recycler)
    RecyclerView mReycler;
    @BindView(R.id.compulsory)
    ImageView mCompulsory;
    @BindView(R.id.xuanxiu)
    ImageView mXuanxiu;
    @BindView(R.id.schedule_elective)
    TextView mTextelective;
    @BindView(R.id.schedule_required)
    TextView mTextrequired;

    //必修
    @OnClick(R.id.obligatory_layout) void obligatory(){
        //更改图标刷新界面
        mCompulsory.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.compulsory_sel));
        getInfo("1");
        //初始化宁一个选项卡
        mXuanxiu.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.xuanxiu_icon));
        //// TODO: 2017/5/27  quxiao  ObligatoryActivity
//        startActivity(new Intent(mContext,ObligatoryActivity.class));
    }
    //选修
    @OnClick(R.id.my_elective_layout) void elective(){
//        startActivity(new Intent(mContext,ObligatoryActivity.class));
        mCompulsory.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.compulsory_icon));
        mXuanxiu.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.xuanxiu_sel));
        getInfo("2");
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getToolBar().setTitle(getString(R.string.class_schedule));
        MyApplication.getInstance().add(this);
        //默认选中第一个
        mCompulsory.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.compulsory_sel));
        classTime();
        getInfo("1");
    }

    private void initview(){

        mReycler.setHasFixedSize(true);
        mReycler.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CommonAdapter<CurriculumNoByUser.courseNoListData>(mContext,R.layout.schedule_recyclerview_item_layout,list) {
            @Override
            protected void convert(ViewHolder holder, final CurriculumNoByUser.courseNoListData data, int position) {
                if (data.getIsArrange().equals("1")){
                    holder.setVisible(R.id.schedule_recycler_item_image_schedule,true);
                }else if (data.getIsArrange().equals("0")){
                    holder.setVisible(R.id.schedule_recycler_item_image_schedule,false);
                }
                holder.setImageUrl(R.id.schedule_item_image,data.getPictureUrl(),"2");
                if (Contants.LANGUAGENEM == 0){
                    holder.setText(R.id.schedule_recycler_item_title,data.getChineseName());
                }else if (Contants.LANGUAGENEM == 1){
                    holder.setText(R.id.schedule_recycler_item_title,data.getEnglishName());
                }


                //详情
                holder.setOnClickListener(R.id.schedule_recycler_item_details, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext,CourseDetailsActivity.class);
                        if (Contants.LANGUAGENEM == 0){
                            intent.putExtra("Course",data.getChineseName());
                        }else if (Contants.LANGUAGENEM == 1){
                            intent.putExtra("Course",data.getEnglishName());
                        }
                        intent.putExtra("courseId",data.getCurriculumId());
                        startActivity(intent);
                    }
                });
                //时间安排
                holder.setOnClickListener(R.id.schedule_recycler_item_image_schedule, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext,ScheduleDateActivity.class);
                        intent.putExtra("courseNoId",data.getId());
                        if (Contants.LANGUAGENEM == 0){
                            intent.putExtra("Course",data.getChineseName());
                        }else if (Contants.LANGUAGENEM == 1){
                            intent.putExtra("Course",data.getEnglishName());
                        }

                        startActivity(intent);
                    }
                });
            }
        };
        mReycler.setAdapter(adapter);
    }
    private void getInfo(String index){
        LinkedHashMap<String,String> map = new LinkedHashMap<>();
        map.put("type",index);
        map.put("userId", SharedPreferencesUtil.getInstance(mContext).getString("userid"));
        map.put("appVersion", CommonUtil.getVersion(mContext));
        map.put("digest","");
        map.put("ostype","android");
        map.put("uuid",CommonUtil.android_id(mContext));
        mOkHttpHelper.post(mContext, Contants.BASEURL + Contants.CurriculumNoByUserId, map, TAG, new SpotsCallBack<CurriculumNoByUser>(mContext) {
            @Override
            public void onSuccess(Response response, CurriculumNoByUser data) {
                if (data.getCode().equals("200")){
                    list.clear();
                    list.addAll(data.getCourseNoList());
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

    //获取 已选课时 总课时
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
                    mTextrequired.setText(getString(R.string.has)+":"+data.getHaveClassHoursRequired()+"/"+data.getTotalClassHoursRequired());
                    mTextelective.setText(getString(R.string.has)+":"+data.getHaveClassHoursElective()+"/"+data.getTotalClassHoursElective());
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
        return R.layout.schedule_main_layout;
    }
}
