package com.zzh.mt.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.zzh.mt.R;
import com.zzh.mt.utils.Contants;
import com.zzh.mt.widget.HorizontalProgressBarWithNumber;

import java.util.LinkedList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 腾翔信息 on 2017/5/24.
 */

public class LanguageAdapter extends RecyclerView.Adapter<LanguageAdapter.ViewHolder> {
    private Context context;
    private LayoutInflater inflater;
    private int i;
    public LanguageAdapter(Context context){
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public void addData(int index){
        i=index;
        notifyDataSetChanged();
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.language_setting_item_layout,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        if (onClickItemListener!= null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onClickItemListener.onClickItem(holder.mBox,position);
                }
            });
        }
        holder.mBox.setClickable(false);
        if (position == 0){
            holder.mTitle.setText("简体中文");
        }else if (position == 1){
            holder.mTitle.setText("Engish");
        }

        if (position == Contants.LANGUAGENEM){
            holder.mBox.setChecked(true);
        }else {
            holder.mBox.setChecked(false);
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.language_title)
        TextView mTitle;
        @BindView(R.id.language_checkbox)
        CheckBox mBox;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
    public interface OnClickItemListener{
        void onClickItem(CheckBox view, int postion);
    }

    public LanguageAdapter.OnClickItemListener onClickItemListener;

    public void setOnClickItemListener(LanguageAdapter.OnClickItemListener onClickItemListener){
        this.onClickItemListener = onClickItemListener;
    }
}
