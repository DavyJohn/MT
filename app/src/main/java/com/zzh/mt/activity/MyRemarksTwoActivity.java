package com.zzh.mt.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.zhy.http.okhttp.callback.StringCallback;
import com.zzh.mt.R;
import com.zzh.mt.base.BaseActivity;
import com.zzh.mt.base.MyApplication;
import com.zzh.mt.http.callback.SpotsCallBack;
import com.zzh.mt.mode.AppRemarksByGroupId;
import com.zzh.mt.mode.BaseData;
import com.zzh.mt.utils.CommonUtil;
import com.zzh.mt.utils.Contants;
import com.zzh.mt.utils.SharedPreferencesUtil;

import java.util.LinkedHashMap;

import butterknife.BindView;
import okhttp3.Response;

/**
 * Created by 腾翔信息 on 2017/5/23.
 */

public class MyRemarksTwoActivity extends BaseActivity {
    private static final String TAG = MyRemarksTwoActivity.class.getSimpleName();
    private MenuItem  item = null;
    @BindView(R.id.my_remarks_two_et)
    EditText mEtText;
    @BindView(R.id.remarks_two_text)
    TextView mText;
    @BindView(R.id.remarks_type)
    TextView mType;
    @BindView(R.id.my_remarks_time)
    TextView mTime;
    @BindView(R.id.my_remarks_title)
    TextView mTitle;
    private String remarkId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getToolBar().setTitle(R.string.my_remarks);
        MyApplication.getInstance().add(this);
        getID();
        Contants.isChange = false;
        initview();
    }

    private void initview(){
        mType.setText(getIntent().getStringExtra("activityTypeName"));
        mTime.setText(getIntent().getStringExtra("time").substring(0,10));
        mTitle.setText(getIntent().getStringExtra("name"));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.remark_edit_menu,menu);
        item = menu.findItem(R.id.remarks_menu_edit);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getTitle().equals(getString(R.string.remarks_menu_complete))){
            if (TextUtils.isEmpty(mEtText.getText().toString())){
                showMessageDialog("备注不能为空",mContext);
            }else {
                setremark();
            }

        }else if (item.getTitle().equals(getString(R.string.edit_menu))){
            mEtText.setVisibility(View.VISIBLE);
            mText.setVisibility(View.GONE);
            mEtText.setText(mText.getText().toString());
            item.setTitle(getString(R.string.remarks_menu_complete));
        }





        return super.onOptionsItemSelected(item);
    }

    private void setremark(){
        LinkedHashMap<String,String> map = new LinkedHashMap<>();
        map.put("appVersion", CommonUtil.getVersion(mContext));
        map.put("digest","");
        map.put("ostype","android");
        map.put("uuid",CommonUtil.android_id(mContext));
        map.put("courseNoId",getIntent().getStringExtra("courseNoId"));
        map.put("activityId",getIntent().getStringExtra("activityId"));
        if (remarkId == null || TextUtils.isEmpty(remarkId)){
            map.put("operation","c");
        }else {
            map.put("operation","u");
            map.put("remarkId",remarkId);
        }
        map.put("remark",mEtText.getText().toString());

        map.put("userId",SharedPreferencesUtil.getInstance(mContext).getString("userid"));
        mOkHttpHelper.post(mContext, Contants.BASEURL + Contants.ReditRemarks, map, TAG, new SpotsCallBack<BaseData>(mContext) {
            @Override
            public void onSuccess(Response response, BaseData data) {
                if (data.getCode().equals("200")){
                    showToast(data.getMessage());
                    getID();
                }else {
                    showMessageDialog(data.getMessage(),mContext);
                }

            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });

    }
    private void getID (){
        LinkedHashMap<String,String> map = new LinkedHashMap<>();
        map.put("appVersion", CommonUtil.getVersion(mContext));
        map.put("digest","");
        map.put("ostype","android");
        map.put("uuid",CommonUtil.android_id(mContext));
        map.put("activityId",getIntent().getStringExtra("activityId"));
        map.put("userId", SharedPreferencesUtil.getInstance(mContext).getString("userid"));
        mOkHttpHelper.post(mContext, Contants.BASEURL + Contants.AppRemarksByGroupId, map, TAG, new SpotsCallBack<AppRemarksByGroupId>(mContext) {
            @Override
            public void onSuccess(Response response, AppRemarksByGroupId data) {
                if (data.getCode().equals("200")){
                    if (data.getRemarks() != null) {
                        remarkId = data.getRemarks().getId();
                        if (remarkId == null || TextUtils.isEmpty(remarkId)) {
                            mEtText.setVisibility(View.VISIBLE);
                            mText.setVisibility(View.GONE);
                            item.setTitle(getString(R.string.remarks_menu_complete));
                        } else {
                            mEtText.setVisibility(View.GONE);
                            mText.setVisibility(View.VISIBLE);
                            item.setTitle(getString(R.string.edit_menu));
                            mText.setText(data.getRemarks().getInformation());
                        }
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
        return R.layout.my_remarks_two_main_layout;
    }


}
