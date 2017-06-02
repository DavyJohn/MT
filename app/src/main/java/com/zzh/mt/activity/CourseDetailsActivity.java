package com.zzh.mt.activity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.zzh.mt.R;
import com.zzh.mt.base.BaseActivity;
import com.zzh.mt.base.MyApplication;
import com.zzh.mt.http.callback.SpotsCallBack;
import com.zzh.mt.mode.CourseInfoData;
import com.zzh.mt.utils.CommonUtil;
import com.zzh.mt.utils.Contants;
import com.zzh.mt.utils.SharedPreferencesUtil;

import org.w3c.dom.Text;

import java.util.LinkedHashMap;

import butterknife.BindView;
import okhttp3.Response;

/**
 * Created by 腾翔信息 on 2017/5/22.
 */

public class CourseDetailsActivity extends BaseActivity {

    private static final String TAG = CourseDetailsActivity.class.getSimpleName();
    @BindView(R.id.course_details_date)
    TextView mCdd;
    @BindView(R.id.course_details_where)
    TextView mCdw;
    @BindView(R.id.course_details_clock)
    TextView mCdc;
    @BindView(R.id.course_details_title)
    TextView mTextTitle;
    @BindView(R.id.course_details_image)
    ImageView mImage;
    @BindView(R.id.course_details_introduce)
    TextView mIntroduce;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyApplication.getInstance().add(this);
        getToolBar().setTitle(getIntent().getStringExtra("Course"));
        getInfo();
    }
    private void getInfo(){
        LinkedHashMap<String,String> map = new LinkedHashMap<>();
        map.put("userId", SharedPreferencesUtil.getInstance(mContext).getString("userid"));
        map.put("appVersion", CommonUtil.getVersion(mContext));
        map.put("digest","");
        map.put("ostype","android");
        map.put("uuid",CommonUtil.android_id(mContext));
        map.put("courseId",getIntent().getStringExtra("courseId"));
        mOkHttpHelper.post(mContext, Contants.BASEURL + Contants.CurriculumDetails, map, TAG, new SpotsCallBack<CourseInfoData>(mContext) {
            @Override
            public void onSuccess(Response response, CourseInfoData data) {
                Picasso.with(mContext).load(data.getCourseInfo().getPictureUrl()).into(mImage);
                mTextTitle.setText(data.getCourseInfo().getChineseName());
                mIntroduce.setText(data.getCourseInfo().getIntroduce());
                if (data.getCourseInfo().getCurriculumNo() != null){
                    findViewById(R.id.courrse_details_layout).setVisibility(View.VISIBLE);
                    if (data.getCourseInfo().getCurriculumNo().getClassHours() == null){
                        mCdc.setText("");
                    }else {
                        mCdc.setText(data.getCourseInfo().getCurriculumNo().getClassHours()+"天");
                    }
                    if (data.getCourseInfo().getCurriculumNo().getAttendPlace() == null){
                        mCdw.setText("");
                    }else {
                        mCdw.setText(data.getCourseInfo().getCurriculumNo().getAttendPlace());
                    }
                    if (data.getCourseInfo().getCurriculumNo().getAttendTime() == null){
                        mCdd.setText("");
                    }else {
                        mCdd.setText(data.getCourseInfo().getCurriculumNo().getAttendTime().substring(0,10));
                    }
                }else {
                    //消失课程信息
                    findViewById(R.id.courrse_details_layout).setVisibility(View.GONE);
                }
            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });
    }
    @Override
    public int getLayoutId() {
        return R.layout.course_details_main_layout;
    }
}
