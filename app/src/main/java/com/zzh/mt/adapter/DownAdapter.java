package com.zzh.mt.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zzh.mt.R;
import com.zzh.mt.widget.HorizontalProgressBarWithNumber;

import java.util.LinkedList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 腾翔信息 on 2017/5/24.
 */

public class DownAdapter extends RecyclerView.Adapter<DownAdapter.ViewHolder> {

    private Context context;
    private LayoutInflater inflater;
    private LinkedList<String> list = new LinkedList<>();
    public DownAdapter(Context context,LinkedList<String> data){
        this.context = context;
        this.list = data;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.down_item_main_layout,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        if (onClickItemListener!= null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onClickItemListener.onClickItem(holder.progress,holder.mText,holder.mTitle,position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.progress_down)
        HorizontalProgressBarWithNumber progress;
        @BindView(R.id.num_progress)
        TextView mText;
        @BindView(R.id.down_title)
        TextView mTitle;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
    public interface OnClickItemListener{
        void onClickItem(HorizontalProgressBarWithNumber view,TextView text,TextView title, int postion);
    }

    public OnClickItemListener onClickItemListener;

    public void setOnClickItemListener(OnClickItemListener onClickItemListener){
        this.onClickItemListener = onClickItemListener;
    }
}
