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
import com.zzh.mt.mode.CurriculumData;
import com.zzh.mt.utils.CommonUtil;
import com.zzh.mt.utils.Contants;
import com.zzh.mt.utils.MdTools;
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
    private CommonAdapter<CurriculumData.Curriculum> adapter;
    private MenuItem item = null;
    private LinkedList<CurriculumData.Curriculum> list = new LinkedList<>();

    @OnClick(R.id.elective_layout) void elective(){
        //我要选课 进去需要判断inSelectTime 是否位true
        isSelect();
    }
    @BindView(R.id.courrse_time)
    TextView mTextCourrseTime;

    @BindView(R.id.elective_recycler)
    RecyclerView mRecycler;
    @BindView(R.id.class_time)
    TextView mTextTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getToolBar().setTitle(getString(R.string.my_courde));
        classTime();
        getCurriculumChoice();

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        classTime();
        getCurriculumChoice();
    }

    private void initview(){
        //利用item.setText来跟新menu

        mRecycler.setLayoutManager(new LinearLayoutManager(this));
        mRecycler.setHasFixedSize(true);
        adapter = new CommonAdapter<CurriculumData.Curriculum>(mContext,R.layout.elevtive_recycer_item_layout,list) {
            @Override
            protected void convert(ViewHolder holder, final CurriculumData.Curriculum s, final int position) {
                holder.setText(R.id.elevtive_date,s.getClassHours()+getString(R.string.day));
                if (Contants.LANGUAGENEM == 0){
                    holder.setText(R.id.course_name,s.getChineseName());
                }else if (Contants.LANGUAGENEM == 1){
                    holder.setText(R.id.course_name,s.getEnglishName());
                }

                holder.setImageUrl(R.id.courrse_image,s.getPictureUrl(),"2");
                holder.setOnClickListener(R.id.elevtive_recycler_item_detail, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext,CourseDetailsActivity.class);
                        if (Contants.LANGUAGENEM == 0){
                            intent.putExtra("Course",s.getChineseName());
                        }else if (Contants.LANGUAGENEM == 1){
                            intent.putExtra("Course",s.getEnglishName());
                        }
                        intent.putExtra("courseId",s.getId());
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
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    //获取 开始-截至时间 已选课时 总课时
    private void classTime(){
        LinkedHashMap<String,String> map = new LinkedHashMap<>();
        map.put("userId", SharedPreferencesUtil.getInstance(mContext).getString("userid"));
        map.put("appVersion", CommonUtil.getVersion(mContext));
        map.put("ostype","android");
        map.put("uuid",CommonUtil.android_id(mContext));
        map.put("digest", MdTools.sign_digest(map));
        mOkHttpHelper.post(mContext, Contants.BASEURL + Contants.CLASSGOALS, map, TAG, new SpotsCallBack<ClassTimeData>(mContext) {
            @Override
            public void onSuccess(Response response, ClassTimeData data) {
                if (data.getCode().equals("200")){
//                    int sumtotal = Integer.parseInt(data.getTotalClassHoursElective());
//                    int sunhave = Integer.parseInt(String.valueOf(data.getHaveClassHoursElective()));

                    if (Contants.LANGUAGENEM == 0){
                        item.setTitle(getString(R.string.has)+"："+data.getHaveClassHoursElective()+"/"+data.getTotalClassHoursElective());
                    }else {
                        item.setTitle(getString(R.string.has)+"："+data.getHaveClassHoursElective()+"/"+data.getTotalClassHoursElective());
                    }

                    mTextTime.setText(data.getStartTime().substring(6,7)+getString(R.string.M)+data.getStartTime().substring(8,10)+getString(R.string.D)+"——"+data.getEndTime().substring(6,7)+getString(R.string.M)+data.getEndTime().substring(8,10)+getString(R.string.D));
                }else {
                    showMessageDialog(data.getMessage(),mContext);
                }

            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });
    }

    //获取选修课程情况和累计课时
    private void getCurriculumChoice(){
        LinkedHashMap<String,String> map = new LinkedHashMap<>();
        map.put("appVersion", CommonUtil.getVersion(mContext));
        map.put("ostype","android");
        map.put("uuid",CommonUtil.android_id(mContext));
        map.put("type","1");
        map.put("userId",SharedPreferencesUtil.getInstance(mContext).getString("userid"));
        map.put("digest", MdTools.sign_digest(map));
        mOkHttpHelper.post(mContext, Contants.BASEURL + Contants.CurriculumChoice, map, TAG, new SpotsCallBack<CurriculumData>(mContext) {
            @Override
            public void onSuccess(Response response, CurriculumData data) {
                if (data.getCode().equals("200")){
                    if (data.getCourseList().size() != 0){
                        findViewById(R.id.courrse_layout).setVisibility(View.VISIBLE);
                        Double add = 0.0;
                        for (int i=0;i<data.getCourseList().size();i++){
                            add = CommonUtil.add(add,Double.valueOf(data.getCourseList().get(i).getClassHours()).doubleValue());
                            if (Contants.LANGUAGENEM == 0){
                                mTextCourrseTime.setText(String.valueOf(add)+getString(R.string.day));
                            }else {
                                mTextCourrseTime.setText(String.valueOf(add)+getString(R.string.day));
                            }

                        }
                    }else {
                        //模块消失
                        findViewById(R.id.courrse_layout).setVisibility(View.GONE);
                    }
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

    private void isSelect(){
        LinkedHashMap<String,String> map = new LinkedHashMap<>();
        map.put("appVersion", CommonUtil.getVersion(mContext));
        map.put("ostype","android");
        map.put("uuid",CommonUtil.android_id(mContext));
        map.put("userId", SharedPreferencesUtil.getInstance(mContext).getString("userid"));
        map.put("digest", MdTools.sign_digest(map));
        mOkHttpHelper.post(mContext, Contants.BASEURL + Contants.CurriculumNoChoice, map, TAG, new SpotsCallBack<CurriculumData>(mContext) {
            @Override
            public void onSuccess(Response response, CurriculumData data) {
                if (data.getCode().equals("200")){
                  if (data.getInSelectTime() == true){
                      startActivity(new Intent(mContext,ElectiveListActivity.class));
                  }else {
                      showMessageDialog("当前时间没有开放选课！",mContext);
                  }
                }else if (data.getCode().equals("110")){
                    goBack(data.getMessage(),mContext);
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
