package com.zzh.mt.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.zzh.mt.R;
import com.zzh.mt.base.BaseActivity;
import com.zzh.mt.base.CommonAdapter;
import com.zzh.mt.base.MultiItemTypeAdapter;
import com.zzh.mt.base.MyApplication;
import com.zzh.mt.base.ViewHolder;
import com.zzh.mt.http.callback.SpotsCallBack;
import com.zzh.mt.mode.ClassMateData;
import com.zzh.mt.utils.CommonUtil;
import com.zzh.mt.utils.Contants;
import com.zzh.mt.utils.SharedPreferencesUtil;
import com.zzh.mt.widget.MysearchView;
import com.zzh.mt.widget.sidebar.WaveSideBar;

import java.util.LinkedHashMap;
import java.util.LinkedList;

import butterknife.BindView;
import okhttp3.Response;

/**
 * Created by 腾翔信息 on 2017/5/15.
 */

public class ClassmateActivity extends BaseActivity {

    private static final String TAG = ClassmateActivity.class.getSimpleName();
    @BindView(R.id.classmate_recycler)
    RecyclerView mRecycler;
    @BindView(R.id.classmate_side)
    WaveSideBar mBar;
    @BindView(R.id.classmate_search)
    MysearchView mSearch;
    CommonAdapter<ClassMateData.PersonListData> adapter;
    LinkedList<ClassMateData.PersonListData> list = new LinkedList<>();
    LinkedList<String> index = new LinkedList<>();

    String [] data = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getToolBar().setTitle(getString(R.string.check_out_classmate));
        MyApplication.getInstance().add(this);
        classmate();
    }

    private void initview(){
        mRecycler.setLayoutManager(new LinearLayoutManager(this));
        mRecycler.setHasFixedSize(true);
        adapter = new CommonAdapter<ClassMateData.PersonListData>(mContext,R.layout.classmate_item_main_layout,list) {
            @Override
            protected void convert(ViewHolder holder, ClassMateData.PersonListData s, int position) {
                holder.setText(R.id.class_en_name,s.getNickName());
                holder.setText(R.id.class_china_name,s.getChineseName());
                if (s.getHeadUrl() != null ||!TextUtils.isEmpty(s.getHeadUrl())){
                    holder.setImageUrl(R.id.classmate_image,s.getHeadUrl(),s.getSex());
                }

                if (position ==0 || !list.get(position-1).equals(list.get(position))){
                    holder.setVisible(R.id.tv_index,true);
                    holder.setText(R.id.tv_index,index.get(position));
                }else
                    {
                        holder.setVisible(R.id.tv_index,false);
                    }
            }
        };
        adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                Intent intent = new Intent(mContext,ClassmateInfoActivity.class);
                if (Contants.LANGUAGENEM == 0){
                    intent.putExtra("name",list.get(position).getChineseName());
                }else {
                    intent.putExtra("name",list.get(position).getEnglishName());
                }

                intent.putExtra("id",list.get(position).getId());
                startActivity(intent);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        mRecycler.setAdapter(adapter);
        data = index.toArray(new String[index.size()]);
        mBar.setIndexItems(data);
        mBar.setOnSelectIndexItemListener(new WaveSideBar.OnSelectIndexItemListener() {
            @Override
            public void onSelectIndexItem(String s) {
                for (int i = 0;i<data.length;i++){
                    if (data[i].equals(s)){
                        ((LinearLayoutManager) mRecycler.getLayoutManager()).scrollToPositionWithOffset(i, 0);
                    }
                }
            }
        });
    }
    private void classmate(){
        LinkedHashMap<String,String> map = new LinkedHashMap<>();
        map.put("appVersion", CommonUtil.getVersion(mContext));
        map.put("digest","");
        map.put("ostype","android");
        map.put("uuid",CommonUtil.android_id(mContext));
        map.put("userId", SharedPreferencesUtil.getInstance(mContext).getString("userid"));
        mOkHttpHelper.post(mContext, Contants.BASEURL + Contants.CLASSMATE, map, TAG, new SpotsCallBack<ClassMateData>(mContext) {
            @Override
            public void onSuccess(Response response, ClassMateData data) {
                    if (data.getCode().equals("200")){
                        if (data.getPersonList().size() != 0){
                            index.clear();
                            list.clear();
                            for (int i=0;i<data.getPersonList().size();i++){
                                if (i<data.getPersonList().size()-1){
                                    if (data.getPersonList().get(i).getInitial().equals(data.getPersonList().get(i+1).getInitial())){

                                    }else {
                                        index.add(data.getPersonList().get(i).getInitial());
                                    }
                                }else {
                                    index.add(data.getPersonList().get(i).getInitial());
                                }
                            }
                            list.addAll(data.getPersonList());
                            initview();
                        }

                    }

            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });
    }
    @Override
    public int getLayoutId() {
        return R.layout.classmate_main_activity;
    }
}
