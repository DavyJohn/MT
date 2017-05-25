package com.zzh.mt.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.zzh.mt.R;
import com.zzh.mt.adapter.DownAdapter;
import com.zzh.mt.base.BaseActivity;
import com.zzh.mt.base.CommonAdapter;
import com.zzh.mt.base.MultiItemTypeAdapter;
import com.zzh.mt.base.MyApplication;
import com.zzh.mt.base.ViewHolder;
import com.zzh.mt.widget.HorizontalProgressBarWithNumber;

import org.w3c.dom.Text;

import java.util.LinkedList;

import butterknife.BindView;

/**
 * Created by 腾翔信息 on 2017/5/24.
 */

public class DownActivity extends BaseActivity {
    private static final String TAG = DownActivity.class.getSimpleName();
    private DownAdapter adapter;
    Handler handler = null;
    private LinkedList<String> list = new LinkedList<>();
    @BindView(R.id.down_recycler)
    RecyclerView mRecycler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getToolBar().setTitle(getString(R.string.down));
        MyApplication.getInstance().add(this);
        initview();
    }
    private void initview(){
        list.clear();
        for (int i=0;i<8;i++){
            list.add(i+"");
        }
        mRecycler.setHasFixedSize(true);
        mRecycler.setLayoutManager(new LinearLayoutManager(this));
        adapter = new DownAdapter(mContext,list);
        mRecycler.setAdapter(adapter);
        adapter.setOnClickItemListener(new DownAdapter.OnClickItemListener() {
            @Override
            public void onClickItem(final HorizontalProgressBarWithNumber view, TextView text, TextView title, int postion) {
                view.setVisibility(View.VISIBLE);
                text.setVisibility(View.INVISIBLE);
                view.setMax(100);
                title.setTextColor(ContextCompat.getColor(mContext,R.color.text_down_color));
                Thread thread = new Thread(){
                    @Override
                    public void run() {
                        super.run();
                        try {
                            sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        handler.sendEmptyMessage(1);
                    }
                };
                thread.start();
                handler = new Handler(){
                    @Override
                    public void handleMessage(Message msg) {
                        super.handleMessage(msg);
                            switch (msg.what){
                                case 1:
                                    view.setVisibility(View.VISIBLE);
                                    for (int i=1;i<=100;i++){
                                        view.setProgress(i);
                                    }
                                    if (view.getProgress() == 100){
                                        showToast("下载完成");
                                    }
                                    break;
                            }
                    }
                };


            }
        });

    }

    @Override
    public int getLayoutId() {
        return R.layout.down_main_layout;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (handler != null){
            handler.removeCallbacksAndMessages(null);
        }

    }
}
