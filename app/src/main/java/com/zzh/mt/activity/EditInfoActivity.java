package com.zzh.mt.activity;

import android.os.Bundle;

import com.squareup.picasso.Picasso;
import com.zzh.mt.R;
import com.zzh.mt.base.BaseActivity;
import com.zzh.mt.base.MyApplication;
import com.zzh.mt.widget.CircleImageView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by 腾翔信息 on 2017/5/25.
 */

public class EditInfoActivity extends BaseActivity {
    @OnClick(R.id.edit_info_image) void image(){
        //开启相机相册
    }

    @BindView(R.id.edit_info_header)
    CircleImageView mImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getToolBar().setTitle("编辑资料");
        MyApplication.getInstance().add(this);
        initview();
    }

    private void initview(){
        Picasso.with(mContext).load(R.drawable.imag_demo).placeholder(R.drawable.imag_demo).error(R.drawable.imag_demo).into(mImage);
    }
    @Override
    public int getLayoutId() {
        return R.layout.edit_info_layout;
    }
}
