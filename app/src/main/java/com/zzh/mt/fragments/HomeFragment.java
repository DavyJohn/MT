package com.zzh.mt.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zzh.mt.R;
import com.zzh.mt.activity.*;
import com.zzh.mt.base.BaseFragment;
import com.zzh.mt.base.CommonAdapter;
import com.zzh.mt.base.MultiItemTypeAdapter;
import com.zzh.mt.base.ViewHolder;
import com.zzh.mt.http.callback.SpotsCallBack;
import com.zzh.mt.mode.BannerEntity;
import com.zzh.mt.mode.LatelyMode;
import com.zzh.mt.mode.MyGroupInfo;
import com.zzh.mt.mode.QuestionData;
import com.zzh.mt.mode.UserData;
import com.zzh.mt.utils.CommonUtil;
import com.zzh.mt.utils.Contants;
import com.zzh.mt.utils.MdTools;
import com.zzh.mt.utils.SharedPreferencesUtil;
import com.zzh.mt.widget.banner.BannerView;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Response;

/**
 * Created by 腾翔信息 on 2017/10/26.
 */

public class HomeFragment extends BaseFragment {

    private static final String TAG = HomeFragment.class.getSimpleName();
    private ImageView mNavImage;
    private UserData userData;
    private TextView mNickName,mInfo;
    private LinkedList<BannerEntity.ImageData> banners = new LinkedList<>();
    private CommonAdapter<Integer> adapter;
    private CommonAdapter<MyGroupInfo.GroupList> groupAdapter;//组
    private CommonAdapter<QuestionData.QInfo> questionAdapter;//提问
    private Integer[] imageData = {R.drawable.index_remark,R.drawable.index_selection,R.drawable.index_curriculum,R.drawable.index_material};
    private Integer[] data = {R.string.my_remarks,R.string.my_courde,R.string.class_schedule,R.string.Course_materials};
    private LinkedList<Integer> list = new LinkedList<>();
    //获取小组信息
    private List<MyGroupInfo.GroupList> groupInfo = new LinkedList<>();
    //处理 只存入两条数据
    private List<MyGroupInfo.GroupList> groupTwoInfo = new LinkedList<>();

    private List<QuestionData.QInfo> qInfo = new LinkedList<>();
    private List<QuestionData.QInfo> qTwoInfo = new LinkedList<>();
    @BindView(R.id.banner_copy)
    ImageView mImage;
    @BindView(R.id.banner)
    BannerView mBanner;
    @BindView(R.id.main_recyclerview)
    RecyclerView mRecycler;
    @BindView(R.id.home_group)
    RecyclerView mGroupRecycler;
    @BindView(R.id.home_question)
    RecyclerView mQuestionRecycler;
    @BindView(R.id.notice_view)
    TextView mNotice;
    //日历修改为消息
    @OnClick(R.id.home_message) void ri(){
        startActivity(new Intent(mContext, com.zzh.mt.activity.NewsActivity.class));
        //生产改回Basurl
//        Intent intent = new Intent(mContext,BirthdayActivity.class);
//        String url = Contants.BASEURL+Contants.CalView+SharedPreferencesUtil.getInstance(mContext).getString("userid");
//        intent.putExtra("url",Contants.BASEURL+Contants.CalView+SharedPreferencesUtil.getInstance(mContext).getString("userid"));
//        startActivity(intent);
    }
    @OnClick(R.id.notice_view) void notice(){
        Intent intent = new Intent(mContext,BirthdayActivity.class);
        intent.putExtra("url",Contants.BASEURL+Contants.Notice);
        startActivity(intent);
    }
    @OnClick(R.id.birthday) void birthday(){
        Intent intent = new Intent(mContext,BirthdayActivity.class);
        intent.putExtra("url",Contants.BASEURL+Contants.Birthday+"?userId="+SharedPreferencesUtil.getInstance(mContext).getString("userid"));
        startActivity(intent);
    }
    @OnClick(R.id.more_group) void moreGroup(){
        Intent intent = new Intent(mContext,BirthdayActivity.class);
        intent.putExtra("url",Contants.BASEURL+Contants.MoreGroup+"?userId="+SharedPreferencesUtil.getInstance(mContext).getString("userid"));
        startActivity(intent);
    }
    //问答更多
    @OnClick(R.id.more_que) void m_q(){
        Intent intent = new Intent(mContext,BirthdayActivity.class);
        intent.putExtra("url",Contants.BASEURL+Contants.WevProblem+"?userId="+SharedPreferencesUtil.getInstance(mContext).getString("userid"));
        startActivity(intent);
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment_layout,container,false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        ButterKnife.bind(this,view);
        Toolbar toolbar = (Toolbar)view.findViewById(R.id.main_toolbar);
        toolbar.setTitle("");
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        TextView textView = (TextView) toolbar.findViewById(R.id.toolbar_text);
        textView.setText(R.string.main_page);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setHomeButtonEnabled(true);
        initRecycler();
        getLately();
        banner();
        MyGroup();
        getQuestion();
    }

    private void initRecycler(){
        list.clear();
        for (int i=0;i<data.length;i++ ){
            list.add(data[i]);
        }
        mRecycler.setHasFixedSize(true);
        mRecycler.setNestedScrollingEnabled(false);
        mRecycler.setLayoutManager(new GridLayoutManager(mContext,4));
        adapter = new CommonAdapter<Integer>(getActivity(),R.layout.main_recycler_item_layout,list) {

            @Override
            protected void convert(ViewHolder holder, Integer integer, int position) {
                holder.setTextid(R.id.main_recycler_item_text,integer);
                switch (position){
                    case 0:
                        holder.setImageResource(R.id.main_recycle_item_image,imageData[0]);
                        break;
                    case 1:
                        holder.setImageResource(R.id.main_recycle_item_image,imageData[1]);
                        break;
                    case 2:
                        holder.setImageResource(R.id.main_recycle_item_image,imageData[2]);
                        break;
                    case 3:
                        holder.setImageResource(R.id.main_recycle_item_image,imageData[3]);
                        break;
                }
            }
        };
        mRecycler.setAdapter(adapter);
        adapter.setOnItemClickListener(new CommonAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                switch (position){
                    case 0:
                       //我的备注
                        startActivity(new Intent(mContext,MyRemarketsActivity.class));
                        break;
                    case 1:
                        //我要选课
                        startActivity(new Intent(mContext,CourseActivity.class));
                        break;
                    case 2:
                        //课程安排
                        startActivity(new Intent(mContext,ScheduleActivity.class));
                        break;
                    case 3:
                        //课程材料
                        startActivity(new Intent(mContext,DataActivity.class));
                        break;
                }
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
    }
    private void initGroup(){

        mGroupRecycler.setNestedScrollingEnabled(false);
        mGroupRecycler.setHasFixedSize(true);
        mGroupRecycler.setLayoutManager(new LinearLayoutManager(mContext));
        groupAdapter = new CommonAdapter<MyGroupInfo.GroupList>(mContext,R.layout.home_group_item_layout,groupTwoInfo) {
            @Override
            protected void convert(ViewHolder holder, MyGroupInfo.GroupList data, int position) {
                holder.setText(R.id.group_title,data.getName());
                holder.setText(R.id.group_num,data.getNum()+"人");
                holder.setUrl(R.id.group_image, data.getPictureUrl());

            }
        };
        mGroupRecycler.setAdapter(groupAdapter);
        groupAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                Intent intent = new Intent(mContext,BirthdayActivity.class);
                intent.putExtra("url",Contants.BASEURL+Contants.GroupDetali+groupInfo.get(position).getId());
                startActivity(intent);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
    }

    private void initQuestion() {

        mQuestionRecycler.setHasFixedSize(true);
        mQuestionRecycler.setNestedScrollingEnabled(false);
        mQuestionRecycler.setLayoutManager(new LinearLayoutManager(mContext));
        //布局跟小组差不都可复用
        questionAdapter = new CommonAdapter<QuestionData.QInfo>(mContext, R.layout.home_group_item_layout, qTwoInfo) {
            @Override
            protected void convert(ViewHolder holder, QuestionData.QInfo info, int position) {
                holder.setText(R.id.group_title, info.getProblemTitle());
                if (info.getAnswers().size()>0){
                    if (!TextUtils.isEmpty(info.getAnswers().get(position).getAnswer())){
                        holder.setText(R.id.group_num, "答："+info.getAnswers().get(position).getAnswer());
                    }
                }else {
                    holder.setText(R.id.group_num, "答："+"暂无回答");
                }
                holder.setImageDrawable(R.id.group_image, ContextCompat.getDrawable(mContext, R.drawable.index_interactive));
            }
        };
        mQuestionRecycler.setAdapter(questionAdapter);
        questionAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                Intent intent = new Intent(mContext,BirthdayActivity.class);
                intent.putExtra("url",Contants.BASEURL+Contants.WebProblemDetali+"?userId="+SharedPreferencesUtil.getInstance(mContext).getString("userid")+"&id="+qInfo.get(position).getId());
                startActivity(intent);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
    }
    private void banner(){
        LinkedHashMap<String,String> map = new LinkedHashMap<>();
        map.put("userId", SharedPreferencesUtil.getInstance(mContext).getString("userid"));
        map.put("appVersion", CommonUtil.getVersion(mContext));
        map.put("ostype","android");
        map.put("uuid",CommonUtil.android_id(mContext));
        map.put("digest", MdTools.sign_digest(map));
        mOkHttpHelper.post(mContext, Contants.BASEURL+Contants.BANNERURL, map, TAG, new
                SpotsCallBack<BannerEntity>(mContext) {
                    @Override
                    public void onSuccess(Response response, BannerEntity data) {
                        if (data.getCode().equals("200")){
                            banners.clear();
                            banners.addAll(data.getImageList());
                            if (banners.size() == 0 ){
                                mImage.setVisibility(View.VISIBLE);
                                mBanner.setVisibility(View.GONE);
                            }else if (banners.size() == 1){
                                mImage.setVisibility(View.GONE);
                                mBanner.setVisibility(View.VISIBLE);
                                mBanner.build(banners);
                            }else if (banners.size()>1){
                                mImage.setVisibility(View.GONE);
                                mBanner.setVisibility(View.VISIBLE);
                                mBanner.delayTime(5).build(banners);
                            }

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
    //查询最近一期公告信息
    private void getLately(){
        LinkedHashMap<String,String> map = new LinkedHashMap<>();
        map.put("appVersion", CommonUtil.getVersion(mContext));
        map.put("ostype","android");
        map.put("uuid",CommonUtil.android_id(mContext));
        map.put("digest", MdTools.sign_digest(map));
        mOkHttpHelper.post(mContext, Contants.BASEURL + Contants.Lately, map, TAG, new SpotsCallBack<LatelyMode>(mContext) {
            @Override
            public void onSuccess(Response response, LatelyMode data) {
                if (data.getCode().equals("200")){
                    if (data.getNotice() != null){
                        mNotice.setText(data.getNotice().getNoticeTitle());
                    }
                }else {
                    showToast(data.getMessage(),mContext);
                }

            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });
    }

    //获取小组
    private void MyGroup(){
        LinkedHashMap<String,String> map = new LinkedHashMap<>();
        map.put("appVersion", CommonUtil.getVersion(mContext));
        map.put("ostype","android");
        map.put("uuid",CommonUtil.android_id(mContext));
        map.put("userId",SharedPreferencesUtil.getInstance(mContext).getString("userid"));
        map.put("digest", MdTools.sign_digest(map));
        mOkHttpHelper.post(mContext, Contants.BASEURL + Contants.MyGroup , map, TAG, new SpotsCallBack<MyGroupInfo>(mContext) {
            @Override
            public void onSuccess(Response response, MyGroupInfo data) {
                if (data.getCode().equals("200")){
                    groupInfo.clear();
                    groupInfo.addAll(data.getGroups());
                    groupTwoInfo.clear();
                    if (data.getGroups().size()>0&& data.getGroups().size()<2){

                        for (int i=0;i<data.getGroups().size();i++){
                            groupTwoInfo.add(data.getGroups().get(i));
                        }
                    }else if (data.getGroups().size()>2){
                        for (int i=0;i<2;i++){
                            groupTwoInfo.add(data.getGroups().get(i));
                        }
                    }
                    initGroup();

                }
            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });
    }

    private void getQuestion(){
        LinkedHashMap<String,String> map = new LinkedHashMap<>();
        map.put("appVersion", CommonUtil.getVersion(mContext));
        map.put("ostype","android");
        map.put("uuid",CommonUtil.android_id(mContext));
        map.put("userId",SharedPreferencesUtil.getInstance(mContext).getString("userid"));
        map.put("digest", MdTools.sign_digest(map));
        mOkHttpHelper.post(mContext,Contants.BASEURL + Contants.Problem, map, TAG, new SpotsCallBack<QuestionData>(mContext) {
            @Override
            public void onSuccess(Response response, QuestionData data) {
                    if (data.getCode().equals("200")){
                        qInfo.clear();
                        qTwoInfo.clear();
                        qInfo.addAll(data.getProblems());
                        if (data.getProblems().size()>0 && data.getProblems().size()<2){
                            for (int i=0;i<data.getProblems().size();i++){
                                qTwoInfo.add(data.getProblems().get(i));
                            }
                        }else if (data.getProblems().size()>2){
                            for (int i=0;i<2;i++){
                                qTwoInfo.add(data.getProblems().get(i));
                            }
                        }
                        initQuestion();

                    }
            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });
    }
}
