package com.zzh.mt.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.SimpleCursorTreeAdapter;

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
            protected void convert(ViewHolder holder, final CurriculumData.Curriculum s, int position) {
                if (Contants.LANGUAGENEM == 0){
                    holder.setText(R.id.data_recycler_item_title,s.getChineseName());
                }else if (Contants.LANGUAGENEM == 1){
                    holder.setText(R.id.data_recycler_item_title,s.getEnglishName());
                }


                holder.setImageUrl(R.id.data_recycler_item_image,s.getPictureUrl(),"2");

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
                        Intent intent  = new Intent(mContext,MaterialsActivity.class);
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
        map.put("digest","");
        map.put("ostype","android");
        map.put("uuid",CommonUtil.android_id(mContext));
        map.put("type","3");
        map.put("userId", SharedPreferencesUtil.getInstance(mContext).getString("userid"));
        mOkHttpHelper.post(mContext, Contants.BASEURL + Contants.CurriculumChoice, map, TAG, new SpotsCallBack<CurriculumData>(mContext) {
            @Override
            public void onSuccess(Response response, CurriculumData data) {
                if (data.getCode().equals("200")){
                    list.clear();
                    list.addAll(data.getCourseList());
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
        return R.layout.data_main_layout;
    }
}
