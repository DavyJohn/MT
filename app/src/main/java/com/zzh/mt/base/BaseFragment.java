package com.zzh.mt.base;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.zzh.mt.R;
import com.zzh.mt.activity.LoginActivity;
import com.zzh.mt.activity.HomeActivity;
import com.zzh.mt.http.OkHttpHelper;
import com.zzh.mt.http.callback.SpotsCallBack;
import com.zzh.mt.mode.BaseData;
import com.zzh.mt.utils.CommonUtil;
import com.zzh.mt.utils.Contants;
import com.zzh.mt.utils.MdTools;
import com.zzh.mt.utils.SharedPreferencesUtil;

import java.util.LinkedHashMap;

import butterknife.ButterKnife;
import okhttp3.Response;


/**
 * Created by 腾翔信息 on 2017/1/24.
 */

public abstract class BaseFragment extends Fragment {

    private static final String TAG = BaseFragment.class.getSimpleName();
    public Context mContext;
    private android.support.v7.app.AlertDialog dialog;
    public OkHttpHelper mOkHttpHelper = OkHttpHelper.getInstace();
    private Toast toast;
    public BaseFragment(){

    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this.getActivity();

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this,view);

    }

    @Override
    public void startActivity(Intent intent) {
        getActivity().startActivity(intent);
        getActivity().overridePendingTransition(R.anim.anmi_in_right_left, R.anim.anmi_out_right_left);
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
        getActivity().overridePendingTransition(R.anim.anmi_in_right_left, R.anim.anmi_out_right_left);
    }

    @Override
public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
        if (data == null){

        }else {
            getActivity().overridePendingTransition(R.anim.anim_in_left_right, R.anim.anim_out_left_right);

        }
}

    protected void showMessageDialog(String str, Context context) {
        dialog = new AlertDialog.Builder(context)
                .setMessage(str).setTitle("提示")
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                }).create();
        dialog.show();
    }


    public synchronized void showToast(String message, Context context) {
        if (toast == null) {
            toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
        }
        if (!TextUtils.isEmpty(message)) {
            toast.setText(message);
            toast.show();
        }
    }

    //退出
    protected void quite(String str, Context context) {
        dialog = new AlertDialog.Builder(context)
                .setTitle("提示")
                .setMessage(str)
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        logout();
                    }
                }).create();
        dialog.show();
    }
    private void logout(){
        LinkedHashMap<String,String> map = new LinkedHashMap<>();
        map.put("userId",SharedPreferencesUtil.getInstance(mContext).getString("userid"));
        map.put("appVersion", CommonUtil.getVersion(mContext));
        map.put("ostype","android");
        map.put("uuid",CommonUtil.android_id(mContext));
        map.put("digest", MdTools.sign_digest(map));
        mOkHttpHelper.post(mContext, Contants.BASEURL + Contants.LOGOUT, map, TAG, new SpotsCallBack<BaseData>(mContext) {
            @Override
            public void onSuccess(Response response, BaseData data) {
                if (data.getCode().equals("200")){
                    SharedPreferencesUtil.getInstance(mContext).putString("userid","");
                    Intent intent = new Intent(mContext,HomeActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|
                            Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK );
                    startActivity(intent);
                    getActivity().finish();
                }

            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });
    }

    protected void goBack(String str, Context context) {
        dialog = new android.support.v7.app.AlertDialog.Builder(context)
                .setTitle("提示")
                .setMessage(str)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferencesUtil.getInstance(mContext).putString("userid","");
                        startActivity(new Intent(mContext,LoginActivity.class));
                    }
                }).create();
        dialog.show();
    }
}
