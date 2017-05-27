package com.zzh.mt.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import com.zzh.mt.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 腾翔信息 on 2017/5/27.
 */

public class ElectiveListAdapter extends RecyclerView.Adapter<ElectiveListAdapter.ViewHolder> {
    private Context context;
    private LayoutInflater inflater;
    public ElectiveListAdapter(Context context){
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    private int index;

    public void addPostion(int postion){
        index = postion;
        notifyDataSetChanged();

    }
    @Override
    public ElectiveListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder holder = null;
        View view = inflater.inflate(R.layout.elective_list_two_recycler_item_layout,parent,false);
        holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ElectiveListAdapter.ViewHolder holder, final int position) {
        holder.mBox.setClickable(false);
        if (position == index){
            holder.mBox.setChecked(true);
        }else {
            holder.mBox.setChecked(false);
        }
        if (onClickItemListener!= null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onClickItemListener.onClickItem(holder.itemView,holder.mBox,position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return 5;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.elective_checkbox)
        CheckBox mBox;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
    public interface OnClickItemListener{
        void onClickItem(View itemview,CheckBox view, int postion);
    }

    public ElectiveListAdapter.OnClickItemListener onClickItemListener;

    public void setOnClickItemListener(ElectiveListAdapter.OnClickItemListener onClickItemListener){
        this.onClickItemListener = onClickItemListener;
    }
}
