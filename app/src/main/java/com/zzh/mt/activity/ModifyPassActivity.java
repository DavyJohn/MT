package com.zzh.mt.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.widget.EditText;
import android.widget.TextView;

import com.zhy.http.okhttp.callback.StringCallback;
import com.zzh.mt.R;
import com.zzh.mt.base.BaseActivity;
import com.zzh.mt.base.MyApplication;
import com.zzh.mt.http.callback.SpotsCallBack;
import com.zzh.mt.mode.BaseData;
import com.zzh.mt.mode.UserData;
import com.zzh.mt.utils.CommonUtil;
import com.zzh.mt.utils.Contants;
import com.zzh.mt.utils.SharedPreferencesUtil;

import java.util.LinkedHashMap;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Response;

/**
 * Created by 腾翔信息 on 2017/5/24.
 */

public class ModifyPassActivity extends BaseActivity {
    private static final String TAG = ModifyPassActivity.class.getSimpleName();
    @BindView(R.id.modify_nickname)
    TextView mNickName;
    @BindView(R.id.original_password)
    EditText mOldPassword;
    @BindView(R.id.new_password)
    EditText mNewPass;
    @BindView(R.id.new_password_second)
    EditText mSecNewPass;
    @OnClick(R.id.modify_btn) void change(){
        if (TextUtils.isEmpty(mOldPassword.getText().toString())){
            showMessageDialog("请填写当前登录密码!",mContext);
        }else if (TextUtils.isEmpty(mNewPass.getText().toString())){
            showMessageDialog("请输入新密码!",mContext);
        }else if (TextUtils.isEmpty(mSecNewPass.getText().toString())){
            showMessageDialog("请再次输入新密码!",mContext);
        }else if (!mNewPass.getText().toString().equals(mSecNewPass.getText().toString())){
            showMessageDialog("两次密码输入不一致！",mContext);
        }else {
            modpass();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyApplication.getInstance().add(this);
        getToolBar().setTitle("密码更新");
        initview();

    }

    private void initview(){
        //个人资料
        mOldPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
        mNewPass.setTransformationMethod(PasswordTransformationMethod.getInstance());
        mSecNewPass.setTransformationMethod(PasswordTransformationMethod.getInstance());

        LinkedHashMap<String,String> map = new LinkedHashMap<>();
        map.put("appVersion", CommonUtil.getVersion(mContext));
        map.put("digest","");
        map.put("ostype","android");
        map.put("uuid",CommonUtil.android_id(mContext));
        map.put("userId",SharedPreferencesUtil.getInstance(mContext).getString("userid"));
        mOkHttpHelper.post(mContext, Contants.BASEURL + Contants.GETUSER, map, TAG, new SpotsCallBack<UserData>(mContext) {
            @Override
            public void onSuccess(Response response, UserData data) {
                if (data.getCode().equals("200")){
                    mNickName.setText(data.getUserInfo().getNickName());
                }
            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });

    }
    private void modpass(){
        LinkedHashMap<String,String> map = new LinkedHashMap<>();
        map.put("appVersion", CommonUtil.getVersion(mContext));
        map.put("digest","");
        map.put("ostype","android");
        map.put("uuid",CommonUtil.android_id(mContext));
        map.put("newPassword",mNewPass.getText().toString());
        map.put("oldPassword",mOldPassword.getText().toString());
        map.put("userId", SharedPreferencesUtil.getInstance(mContext).getString("userid"));
        mOkHttpHelper.post(mContext, Contants.BASEURL + Contants.CHANGEPASSWORD, map, TAG, new SpotsCallBack<BaseData>(mContext) {
            @Override
            public void onSuccess(Response response, BaseData data) {
                if (data.getCode().equals("200"));
                startActivity(new Intent(mContext,LoginActivity.class));
                SharedPreferencesUtil.getInstance(mContext).putString("userid","");
                showToast("修改成功");
            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });
    }
    @Override
    public int getLayoutId() {
        return R.layout.modify_pass_main_layout;
    }
}
