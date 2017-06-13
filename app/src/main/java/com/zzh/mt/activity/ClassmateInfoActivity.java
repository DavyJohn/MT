package com.zzh.mt.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zzh.mt.R;
import com.zzh.mt.base.BaseActivity;
import com.zzh.mt.base.CommonAdapter;
import com.zzh.mt.base.MyApplication;
import com.zzh.mt.base.ViewHolder;
import com.zzh.mt.http.callback.InfoCallback;
import com.zzh.mt.mode.UserData;
import com.zzh.mt.utils.CommonUtil;
import com.zzh.mt.utils.Contants;
import com.zzh.mt.widget.CircleImageView;
import com.zzh.mt.widget.DividerItemDecoration;

import java.util.LinkedHashMap;
import java.util.LinkedList;

import butterknife.BindView;

/**
 * Created by 腾翔信息 on 2017/5/24.
 */

public class ClassmateInfoActivity extends BaseActivity {
    private static final String TAG = ClassmateActivity.class.getSimpleName();
    @BindView(R.id.class_info_recycler)
    RecyclerView mRecycler;
    @BindView(R.id.nav_header_image)
    CircleImageView mImage;
    @BindView(R.id.class_info_name)
    TextView mText;
    private UserData userData = null;
    private LinkedList<Integer> list = new LinkedList<>();
    private Integer [] data = {R.string.chinese_name,R.string.englich_name,R.string.brandname,R.string.department,R.string.gender,R.string.birthday,R.string.year_of_work,R.string.company_email};
    CommonAdapter<Integer> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (Contants.LANGUAGENEM == 0){
//
//        }
        getToolBar().setTitle(getString(R.string.center_info));
        MyApplication.getInstance().add(this);
        getinfo();
    }

    private void initview(){
        for (int i=0;i<data.length;i++){
            list.add(data[i]);
        }
        if (userData.getUserInfo().getSex().equals("1")){
            Picasso.with(mContext).load(userData.getUserInfo().getHeadUrl()).placeholder(R.drawable.image_b).error(R.drawable.image_b).into(mImage);
        }else {
            Picasso.with(mContext).load(userData.getUserInfo().getHeadUrl()).placeholder(R.drawable.image_g).error(R.drawable.image_g).into(mImage);

        }

        mText.setText(userData.getUserInfo().getNickName());
        mRecycler.setLayoutManager(new LinearLayoutManager(this));
        mRecycler.setHasFixedSize(true);
        mRecycler.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL_LIST));
        adapter = new CommonAdapter<Integer>(mContext,R.layout.classmate_info_item_main_layout,list) {
            @Override
            protected void convert(ViewHolder holder, Integer s, int position) {
                holder.setText(R.id.classmate_info_item_key,getString(s));
                switch (position){
                    case 0:
                        holder.setText(R.id.classmate_info_item_view,userData.getUserInfo().getChineseName());
                        break;
                    case 1:
                        holder.setText(R.id.classmate_info_item_view,userData.getUserInfo().getEnglishName());
                        break;
                    case 2:
                        holder.setText(R.id.classmate_info_item_view,userData.getUserInfo().getBrandName());
                        break;
                    case 3:
                        holder.setText(R.id.classmate_info_item_view,userData.getUserInfo().getDepartment().getDepartmentName());
                        break;
                    case 4:
                        holder.setText(R.id.classmate_info_item_view,userData.getUserInfo().getSex().equals("1")?"男":"女");
                        break;
                    case 5:
                        holder.setText(R.id.classmate_info_item_view,userData.getUserInfo().getBirthday());
                        break;
                    case 6:
                        holder.setText(R.id.classmate_info_item_view,userData.getUserInfo().getEntryYear().substring(0,4));
                        break;
                    case 7:
                        holder.setText(R.id.classmate_info_item_view,userData.getUserInfo().getCompanyEmail());
                        break;
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
        map.put("userId",getIntent().getStringExtra("id"));
        OkHttpUtils.post().url(Contants.BASEURL+Contants.GETUSER).params(map).build().execute(new InfoCallback() {
            @Override
            public void onError(okhttp3.Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(UserData response, int id) {
                if (response.getCode().equals("200")){
                    userData = response;
                    initview();
                }
            }
        });
    }
    @Override
    public int getLayoutId() {
        return R.layout.classmate_info_main_layout;
    }
}
