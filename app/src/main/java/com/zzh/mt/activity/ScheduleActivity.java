package com.zzh.mt.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.zzh.mt.R;
import com.zzh.mt.base.BaseActivity;
import com.zzh.mt.base.CommonAdapter;
import com.zzh.mt.base.MyApplication;
import com.zzh.mt.base.ViewHolder;

import java.util.LinkedList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by 腾翔信息 on 2017/5/15.
 */

public class ScheduleActivity extends BaseActivity {
    private static final String TAG = ScheduleActivity.class.getSimpleName();
    CommonAdapter<Integer> adapter;
    private LinkedList<Integer> list = new LinkedList<>();
    @BindView(R.id.schedule_recycler)
    RecyclerView mReycler;
    @BindView(R.id.compulsory)
    ImageView mCompulsory;
    @BindView(R.id.xuanxiu)
    ImageView mXuanxiu;
    //必修
    @OnClick(R.id.obligatory_layout) void obligatory(){
        //更改图标刷新界面
        mCompulsory.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.compulsory_sel));
        initview(3);
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
        initview(4);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getToolBar().setTitle(getString(R.string.class_schedule));
        MyApplication.getInstance().add(this);
        //默认选中第一个
        mCompulsory.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.compulsory_sel));
        initview(3);
    }

    private void initview(int index){

        list.clear();
        for (int i=0;i<index;i++){
            list.add(i);
        }
        mReycler.setHasFixedSize(true);
        mReycler.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CommonAdapter<Integer>(mContext,R.layout.schedule_recyclerview_item_layout,list) {
            @Override
            protected void convert(ViewHolder holder, final Integer integer, int position) {
                if (position == 1){
                    holder.setVisible(R.id.schedule_recycler_item_image_schedule,false);
                }
                //详情
                holder.setOnClickListener(R.id.schedule_recycler_item_details, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext,CourseDetailsActivity.class);
                        intent.putExtra("Course",integer+"");
                        startActivity(intent);
                    }
                });
                //时间安排
                holder.setOnClickListener(R.id.schedule_recycler_item_image_schedule, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext,ScheduleDateActivity.class);
                        intent.putExtra("Course",integer+"");
                        startActivity(intent);
                    }
                });
            }
        };
        mReycler.setAdapter(adapter);
    }
    @Override
    public int getLayoutId() {
        return R.layout.schedule_main_layout;
    }
}
