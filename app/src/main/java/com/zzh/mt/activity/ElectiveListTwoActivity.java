package com.zzh.mt.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.zzh.mt.R;
import com.zzh.mt.adapter.ElectiveListAdapter;
import com.zzh.mt.base.BaseActivity;
import com.zzh.mt.base.CommonAdapter;
import com.zzh.mt.base.MyApplication;
import com.zzh.mt.base.ViewHolder;
import com.zzh.mt.http.callback.SpotsCallBack;
import com.zzh.mt.mode.BaseData;
import com.zzh.mt.mode.CourseInfoData;
import com.zzh.mt.mode.CoursesTrainingSessionsData;
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
 * Created by 腾翔信息 on 2017/5/22.
 */

public class ElectiveListTwoActivity extends BaseActivity {

    private static final String TAG = ElectiveListActivity.class.getSimpleName();
    private ElectiveListAdapter adapter;
    private LinkedList<CoursesTrainingSessionsData.CourseNoListData> list = new LinkedList<>();
    private String courseNoId = null;
    @BindView(R.id.elective_list_two_recycler)
    RecyclerView mRecycler;
    @BindView(R.id.elective_list_title)
    TextView mText;
    @BindView(R.id.elective_change)
    ImageView mChange;
    @BindView(R.id.elective_cancel)
    ImageView mCancel;
    @BindView(R.id.elective_two_time)
    TextView mTime;
    @BindView(R.id.elective_two_image)
    ImageView mImage;
    @OnClick(R.id.elective_list_two_dedails) void details(){
        Intent intent = new Intent(mContext,CourseDetailsActivity.class);
        intent.putExtra("Course",mText.getText().toString());
        intent.putExtra("courseId",getIntent().getStringExtra("courseId"));
        startActivity(intent);
    }
    //更改
    @OnClick(R.id.elective_change) void  change(){
        if (courseNoId != null){
            setchange("modify");
            CoursesTrainingSessions();
        }

    }
    //取消
    @OnClick(R.id.elective_cancel) void  cancel(){
        setchange("cancel");
        CoursesTrainingSessions();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyApplication.getInstance().add(this);
        getToolBar().setTitle("选修列表");
        getInfo();
        CoursesTrainingSessions();
        mRecycler.setLayoutManager(new LinearLayoutManager(this));
        mRecycler.setHasFixedSize(true);
        mRecycler.addItemDecoration(new DividerItemDecoration(mContext,DividerItemDecoration.VERTICAL_LIST));

    }

    private void initview(){

        //// TODO: 2017/5/25 checkbox 修改
        adapter = new ElectiveListAdapter(mContext);
        mRecycler.setAdapter(adapter);
        adapter.addData(list);
        adapter.setOnClickItemListener(new ElectiveListAdapter.OnClickItemListener() {
            @Override
            public void onClickItem(View itemview, ImageView view, int postion) {
                courseNoId = list.get(postion).getId();
                adapter.addPostion(postion);
            }
        });
    }

    //获取培训场次
    private void CoursesTrainingSessions(){
        LinkedHashMap<String,String> map = new LinkedHashMap<>();
        map.put("courseId",getIntent().getStringExtra("courseId"));
        map.put("userId", SharedPreferencesUtil.getInstance(mContext).getString("userid"));
        map.put("appVersion", CommonUtil.getVersion(mContext));
        map.put("digest","");
        map.put("ostype","android");
        map.put("uuid",CommonUtil.android_id(mContext));
        mOkHttpHelper.post(mContext, Contants.BASEURL + Contants.CoursesTrainingSessions, map, TAG, new SpotsCallBack<CoursesTrainingSessionsData>(mContext) {
            @Override
            public void onSuccess(Response response, CoursesTrainingSessionsData data) {
                if (data.getCode().equals("200")){
                    if (data.getCourseNoList().size() != 0){
                        list.clear();
                        list.addAll(data.getCourseNoList());
                        if (data.getCanModify() == true){
                            Picasso.with(mContext).load(R.drawable.change_sel).into(mChange);
                        }else {
                            Picasso.with(mContext).load(R.drawable.change_unsel).into(mChange);
                        }
                        for (int i=0;i<data.getCourseNoList().size();i++){

                            if (data.getCourseNoList().get(i).getIsSelected().equals("1")){
                                Picasso.with(mContext).load(R.drawable.cancel_sel).into(mCancel);
                                mCancel.setClickable(true);
                                courseNoId = data.getCourseNoList().get(i).getId();
                                break;
                            }else {
                                Picasso.with(mContext).load(R.drawable.cancel_unsel).into(mCancel);
                                mCancel.setClickable(false);
                            }
                        }
                        initview();
//                        adapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });
    }

    private void getInfo(){
        LinkedHashMap<String,String> map = new LinkedHashMap<>();
        map.put("courseId",getIntent().getStringExtra("courseId"));
        map.put("userId", SharedPreferencesUtil.getInstance(mContext).getString("userid"));
        map.put("appVersion", CommonUtil.getVersion(mContext));
        map.put("digest","");
        map.put("ostype","android");
        map.put("uuid",CommonUtil.android_id(mContext));
        mOkHttpHelper.post(mContext, Contants.BASEURL + Contants.CurriculumDetails, map, TAG, new SpotsCallBack<CourseInfoData>(mContext) {
            @Override
            public void onSuccess(Response response, CourseInfoData data) {
                if (data.getCode().equals("200")){
                    Picasso.with(mContext).load(data.getCourseInfo().getPictureUrl()).placeholder(R.drawable.image_ing).error(R.drawable.image_ing).into(mImage);
                    if (Contants.LANGUAGENEM == 0){
                        mText.setText(data.getCourseInfo().getChineseName());
                    }else if (Contants.LANGUAGENEM ==1){
                        mText.setText(data.getCourseInfo().getEnglishName());
                    }

                    mTime.setText(data.getCourseInfo().getClassHours()+"天");
                }
            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });
    }


    private void setchange(String operation){
        LinkedHashMap<String,String> map = new LinkedHashMap<>();
        map.put("courseId",getIntent().getStringExtra("courseId"));
        map.put("userId", SharedPreferencesUtil.getInstance(mContext).getString("userid"));
        map.put("appVersion", CommonUtil.getVersion(mContext));
        map.put("digest","");
        map.put("ostype","android");
        map.put("uuid",CommonUtil.android_id(mContext));
        map.put("operation",operation);
        map.put("courseNoId",courseNoId);
        mOkHttpHelper.post(mContext, Contants.BASEURL + Contants.CurriculumNo, map, TAG, new SpotsCallBack<BaseData>(mContext) {
            @Override
            public void onSuccess(Response response, BaseData data) {
                if (data.getCode().equals("200")){
                    courseNoId = null;
                    showToast(data.getMessage());
                }else {
                    showToast(data.getMessage());
                }
            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });

    }

    @Override
    public int getLayoutId() {
        return R.layout.elective_list_two_main_layout;
    }
}
