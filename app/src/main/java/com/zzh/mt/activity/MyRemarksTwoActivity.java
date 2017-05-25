package com.zzh.mt.activity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.zzh.mt.R;
import com.zzh.mt.base.BaseActivity;
import com.zzh.mt.base.MyApplication;

import butterknife.BindView;

/**
 * Created by 腾翔信息 on 2017/5/23.
 */

public class MyRemarksTwoActivity extends BaseActivity {

    private MenuItem  item = null;
    @BindView(R.id.my_remarks_two_et)
    EditText mEtText;
    @BindView(R.id.remarks_two_text)
    TextView mText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getToolBar().setTitle(R.string.my_remarks);
        MyApplication.getInstance().add(this);
        initview();
    }

    private void initview(){

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.remark_edit_menu,menu);
        item = menu.findItem(R.id.remarks_menu_edit);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getTitle().equals("完成")){
            mEtText.setVisibility(View.VISIBLE);
            mText.setVisibility(View.GONE);
            item.setTitle(R.string.edit_menu);

        }else if (item.getTitle().equals("编辑")){
            mEtText.setVisibility(View.GONE);
            mText.setVisibility(View.VISIBLE);
            mText.setText(mEtText.getText().toString());
            item.setTitle("完成");
        }





        return super.onOptionsItemSelected(item);
    }

    @Override
    public int getLayoutId() {
        return R.layout.my_remarks_two_main_layout;
    }


}
