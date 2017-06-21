package com.zzh.mt.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
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
import com.zzh.mt.utils.MdTools;
import com.zzh.mt.utils.SharedPreferencesUtil;
import com.zzh.mt.widget.MysearchView;
import com.zzh.mt.widget.sidebar.WaveSideBar;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Observable;
import java.util.Set;

import butterknife.BindView;
import okhttp3.Response;

import static java.lang.String.CASE_INSENSITIVE_ORDER;

/**
 * Created by 腾翔信息 on 2017/5/15.
 */

public class ClassmateActivity extends BaseActivity {

    private static final String TAG = ClassmateActivity.class.getSimpleName();
    @BindView(R.id.classmate_recycler)
    RecyclerView mRecycler;
    @BindView(R.id.classmate_search_recycler)
    RecyclerView mSearchList;
    @BindView(R.id.classmate_side)
    WaveSideBar mBar;
    @BindView(R.id.classmate_search)
    MysearchView mSearch;
    CommonAdapter<ClassMateData.PersonListData> adapter;
    LinkedList<ClassMateData.PersonListData> list = new LinkedList<>();
    LinkedList<ClassMateData.PersonListData> listdata = new LinkedList<>();
    LinkedList<ClassMateData.PersonListData> initlist = new LinkedList<>();
    LinkedList<String> index = new LinkedList<>();
    LinkedList<String> num = new LinkedList<>();
    LinkedList<String> id = new LinkedList<>();
    String [] data = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getToolBar().setTitle(getString(R.string.check_out_classmate));
        MyApplication.getInstance().add(this);
        mRecycler.setLayoutManager(new LinearLayoutManager(this));
        mRecycler.setHasFixedSize(true);
        mSearchList.setLayoutManager(new LinearLayoutManager(this));
        mSearchList.setHasFixedSize(true);
        classmate();
    }

    private void initview(){
        id.clear();
        findViewById(R.id.no_search_root).setVisibility(View.VISIBLE);
        adapter = new CommonAdapter<ClassMateData.PersonListData>(mContext,R.layout.classmate_item_main_layout,initlist) {
            @Override
            protected void convert(ViewHolder holder, ClassMateData.PersonListData s, int position) {
                holder.setGone(R.id.tv_index,true);
                holder.setText(R.id.class_en_name,s.getNickName());
                holder.setText(R.id.class_china_name,s.getChineseName());
//                if (Contants.LANGUAGENEM == 0){
//                    holder.setText(R.id.class_china_name,s.getChineseName());
//                }else if (Contants.LANGUAGENEM == 1){
//                    holder.setText(R.id.class_china_name,s.getEnglishName());
//                }
                holder.setImageUrl(R.id.classmate_image,s.getHeadUrl(),s.getSex());
                Collections.sort(index);
                if (position ==0 || !index.get(position-1).equals(index.get(position))){
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
                    intent.putExtra("name",initlist.get(position).getChineseName());
                }else {
                    intent.putExtra("name",initlist.get(position).getEnglishName());
                }

                intent.putExtra("id",initlist.get(position).getId());
                startActivity(intent);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        mRecycler.setAdapter(adapter);
        Collections.sort(num);
        Set<String> set = new LinkedHashSet<>();
        set.addAll(num);
        num.clear();
        num.addAll(set);
        data = num.toArray(new String[num.size()]);
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
        mSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (TextUtils.isEmpty(s)){
                        initview();
                    }else {
                        findViewById(R.id.no_search_root).setVisibility(View.GONE);
                        findViewById(R.id.classmate_search_recycler).setVisibility(View.VISIBLE);
                        listdata.clear();
                        for (int i=0;i<list.size();i++){
                            /// TODO: 2017/6/7 缺少英文字段
                            if (list.get(i).getChineseName().toLowerCase().contains(mSearch.getText().toString().toLowerCase()) == true||list.get(i).getNickName().toLowerCase().contains(mSearch.getText().toString().toLowerCase()) == true){
                                listdata.add(list.get(i));
                            }
                        }
                        initsearch();
                    }
                }


            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        //搜索开发
        mSearch.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == event.KEYCODE_ENTER){
                    if (TextUtils.isEmpty(mSearch.getText().toString())){
                    }else {
                        findViewById(R.id.no_search_root).setVisibility(View.GONE);
                        findViewById(R.id.classmate_search_recycler).setVisibility(View.VISIBLE);
                        listdata.clear();
                        for (int i=0;i<list.size();i++){
                            /// TODO: 2017/6/7 缺少英文字段
                            if (list.get(i).getChineseName().toLowerCase().contains(mSearch.getText().toString().toLowerCase()) == true||list.get(i).getNickName().toLowerCase().contains(mSearch.getText().toString().toLowerCase()) == true){
                                listdata.add(list.get(i));
                            }
                        }
                        initsearch();
                    }
                }
                return false;
            }
        });
    }

    private void initsearch(){
        adapter = new CommonAdapter<ClassMateData.PersonListData>(mContext,R.layout.classmate_item_main_layout,listdata) {
            @Override
            protected void convert(ViewHolder holder, ClassMateData.PersonListData s, int position) {
                holder.setGone(R.id.tv_index,false);
                holder.setText(R.id.class_en_name,s.getNickName());
                holder.setText(R.id.class_china_name,s.getChineseName());
//                if (Contants.LANGUAGENEM == 0){
//                    holder.setText(R.id.class_china_name,s.getChineseName());
//                }else if (Contants.LANGUAGENEM == 1){
//                    holder.setText(R.id.class_china_name,s.getEnglishName());
//                }
                if (s.getHeadUrl() != null ||!TextUtils.isEmpty(s.getHeadUrl())){
                    holder.setImageUrl(R.id.classmate_image,s.getHeadUrl(),s.getSex());
                }
            }
        };
        mSearchList.setAdapter(adapter);
        adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                Intent intent = new Intent(mContext,ClassmateInfoActivity.class);
                if (Contants.LANGUAGENEM == 0){
                    intent.putExtra("name",listdata.get(position).getChineseName());
                }else {
                    intent.putExtra("name",listdata.get(position).getEnglishName());
                }

                intent.putExtra("id",listdata.get(position).getId());
                startActivity(intent);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
    }
    private void classmate(){
        LinkedHashMap<String,String> map = new LinkedHashMap<>();
        map.put("appVersion", CommonUtil.getVersion(mContext));
        map.put("ostype","android");
        map.put("uuid",CommonUtil.android_id(mContext));
        map.put("userId", SharedPreferencesUtil.getInstance(mContext).getString("userid"));
        map.put("digest", MdTools.sign_digest(map));
        mOkHttpHelper.post(mContext, Contants.BASEURL + Contants.CLASSMATE, map, TAG, new SpotsCallBack<ClassMateData>(mContext) {
            @Override
            public void onSuccess(Response response, ClassMateData data) {
                    if (data.getCode().equals("200")){
                        if (data.getPersonList().size() != 0){
                            index.clear();
                            initlist.clear();
                            id.clear();
                            list.clear();num.clear();
                            for (int i=0;i<data.getPersonList().size();i++){
                                index.add(data.getPersonList().get(i).getInitial());
                                num.addAll(index);
                            }

                            list.addAll(data.getPersonList());
                            //将list 排序
                            Collections.sort(index);
                            for (int i=0;i<index.size();i++){
                                for (int m=0;m<list.size();m++){
                                    if (index.get(i).equals(list.get(m).getInitial())){

                                        if (id.contains(list.get(m).getId())){
                                            //包含操作
                                        }else {
                                            //不包含
                                            id.add(list.get(m).getId());
                                            initlist.add(list.get(m));
                                        }
                                    }
                                }
                            }
                            initview();
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
        return R.layout.classmate_main_activity;
    }


}
