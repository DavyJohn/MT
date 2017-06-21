package com.zzh.mt.activity;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.zzh.mt.R;
import com.zzh.mt.base.BaseActivity;
import com.zzh.mt.base.MyApplication;
import com.zzh.mt.http.callback.SpotsCallBack;
import com.zzh.mt.mode.BaseData;
import com.zzh.mt.utils.CommonUtil;
import com.zzh.mt.utils.Contants;
import com.zzh.mt.utils.MdTools;

import java.util.LinkedHashMap;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Response;

/**
 * Created by 腾翔信息 on 2017/5/24.
 */

public class ForgetPassActivity extends BaseActivity {
    private static final String TAG = ForgetPassActivity.class.getSimpleName();
    @BindView(R.id.forget_et_username)
    EditText mEditAddress;
    @BindView(R.id.forget_toolbar)
    Toolbar mTool;

    private AlertDialog dialog;

    @OnClick(R.id.forget_button) void getpass(){
        if (TextUtils.isEmpty(mEditAddress.getText().toString())){
            showMessageDialog("邮箱不能为空！",mContext);
        }else if (!CommonUtil.isEmail(mEditAddress.getText().toString())){
            showMessageDialog("邮箱格式不正确！",mContext);
        }else {
            getPassword();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hasToolBar(false);
        MyApplication.getInstance().add(this);
       initview();
    }

    private void initview(){
        mTool.setTitle("");
        setSupportActionBar(mTool);
        mTool.setNavigationIcon(R.drawable.ic_backback);
        mTool.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               finish();
            }
        });
    }
    public void getPassword(){
        LinkedHashMap<String,String> map = new LinkedHashMap<>();
        map.put("appVersion", CommonUtil.getVersion(mContext));
        map.put("ostype","android");
        map.put("uuid",CommonUtil.android_id(mContext));
        map.put("companyEmail",mEditAddress.getText().toString());
        map.put("digest", MdTools.sign_digest(map));
        mOkHttpHelper.post(mContext, Contants.BASEURL + Contants.FORGETPASSWORD, map, TAG, new SpotsCallBack<BaseData>(mContext) {
            @Override
            public void onSuccess(Response response, BaseData data) {
                if (data.getCode().equals("200")){
                    dialog = new AlertDialog.Builder(mContext)
                            .setMessage(data.getMessage()).setTitle("提示")
                            .setPositiveButton("确认", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    finish();
                                }
                            }).create();
                    dialog.show();


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
    @Override
    public int getLayoutId() {
        return R.layout.forget_pass_main_layout;
    }
}
