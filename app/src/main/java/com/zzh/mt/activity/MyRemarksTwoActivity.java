package com.zzh.mt.activity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

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
    EditText mText;
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
        item.setTitle(R.string.remarks_menu_complete);
        return super.onOptionsItemSelected(item);
    }

    @Override
    public int getLayoutId() {
        return R.layout.my_remarks_two_main_layout;
    }


}
