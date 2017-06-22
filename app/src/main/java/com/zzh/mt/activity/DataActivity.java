package com.zzh.mt.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.SimpleCursorTreeAdapter;

import com.zzh.mt.R;
import com.zzh.mt.base.BaseActivity;
import com.zzh.mt.base.CommonAdapter;
import com.zzh.mt.base.MyApplication;
import com.zzh.mt.base.ViewHolder;
import com.zzh.mt.http.callback.SpotsCallBack;
import com.zzh.mt.mode.CoursewareById;
import com.zzh.mt.mode.CurriculumData;
import com.zzh.mt.utils.CommonUtil;
import com.zzh.mt.utils.Contants;
import com.zzh.mt.utils.MdTools;
import com.zzh.mt.utils.SharedPreferencesUtil;

import java.util.LinkedHashMap;
import java.util.LinkedList;

import butterknife.BindView;
import okhttp3.Response;

/**
 * Created by 腾翔信息 on 2017/5/15.
 */

public class DataActivity extends BaseActivity {
    private static final String TAG = DataActivity.class.getSimpleName();
    @BindView(R.id.data_recycler)
    RecyclerView mRecycler;
    private LinkedList<CurriculumData.Curriculum> list = new LinkedList<>();
    private CommonAdapter<CurriculumData.Curriculum> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getToolBar().setTitle(R.string.Course_materials);
        MyApplication.getInstance().add(this);
        getInfo();
    }

    private void initview(){

        mRecycler.setHasFixedSize(true);
        mRecycler.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CommonAdapter<CurriculumData.Curriculum>(mContext,R.layout.data_recyclerview_item_layout,list) {
            @Override
            protected void convert(final ViewHolder holder, final CurriculumData.Curriculum s, int position) {
                isNull(s.getId(), (Button) holder.getView(R.id.data_recycler_item_image_materials));
                if (Contants.LANGUAGENEM == 0){
                    holder.setText(R.id.data_recycler_item_title,s.getChineseName());
                }else if (Contants.LANGUAGENEM == 1){
                    holder.setText(R.id.data_recycler_item_title,s.getEnglishName());
                }
                if (s.getPictureUrl() != null || !TextUtils.isEmpty(s.getPictureUrl())){
                    if (!s.getPictureUrl().equals("")){
                        holder.setImageUrl(R.id.data_recycler_item_image,s.getPictureUrl(),"2");
                    }else {
                        holder.setImageDrawable(R.id.data_recycler_item_image,ContextCompat.getDrawable(mContext,R.drawable.imag_demo));
                    }
                }else {
                    holder.setImageDrawable(R.id.data_recycler_item_image,ContextCompat.getDrawable(mContext,R.drawable.imag_demo));
                }
                holder.setOnClickListener(R.id.data_recycler_item_details, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext,CourseDetailsActivity.class);
                        intent.putExtra("courseId",s.getId());
                        if (Contants.LANGUAGENEM == 0) {
                            intent.putExtra("Course", s.getChineseName());
                        }else if (Contants.LANGUAGENEM == 1){
                            intent.putExtra("Course", s.getEnglishName());
                        }
                        startActivity(intent);
                    }
                });

                //课程资料

                holder.setOnClickListener(R.id.data_recycler_item_image_materials, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // TODO: 2017/6/12 xutils  测试
                            Intent intent  = new Intent(mContext,MaterialsTwoActivity.class);
                            intent.putExtra("courseId",s.getId());
                            startActivity(intent);

                    }
                });
            }
        };
        mRecycler.setAdapter(adapter);
    }

    private void getInfo(){
        LinkedHashMap<String,String> map = new LinkedHashMap<>();
        map.put("appVersion", CommonUtil.getVersion(mContext));
        map.put("ostype","android");
        map.put("uuid",CommonUtil.android_id(mContext));
        map.put("type","3");
        map.put("userId", SharedPreferencesUtil.getInstance(mContext).getString("userid"));
        map.put("digest", MdTools.sign_digest(map));
        mOkHttpHelper.post(mContext, Contants.BASEURL + Contants.CurriculumChoice, map, TAG, new SpotsCallBack<CurriculumData>(mContext) {
            @Override
            public void onSuccess(Response response, CurriculumData data) {
                if (data.getCode().equals("200")){
                    list.clear();
                    list.addAll(data.getCourseList());
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

    private void isNull(final String id , final Button v){
        LinkedHashMap<String,String> map = new LinkedHashMap<>();
        map.put("courseId",id);
        map.put("appVersion", CommonUtil.getVersion(mContext));
        map.put("ostype","android");
        map.put("uuid",CommonUtil.android_id(mContext));
        map.put("digest", MdTools.sign_digest(map));
        mOkHttpHelper.post(mContext, Contants.BASEURL + Contants.getCoursewareById, map, TAG, new SpotsCallBack<CoursewareById>(mContext) {
            @Override
            public void onSuccess(Response response, CoursewareById data) {
                if (data.getCode().equals("200")){
                    if (data.getFileList().size() == 0){
                        v.setBackground(ContextCompat.getDrawable(mContext,R.drawable.button_unclick_shape) );
                        v.setTextColor(ContextCompat.getColor(mContext,R.color.button_unclick_shape_text_color));
                        v.setEnabled(false);
                    }else{
                        v.setBackground(ContextCompat.getDrawable(mContext,R.drawable.button_click_shape) );
                        v.setTextColor(ContextCompat.getColor(mContext,R.color.white));
                        v.setEnabled(true);
                    }


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
        return R.layout.data_main_layout;
    }
}
