package com.zzh.mt.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.zzh.mt.R;
import com.zzh.mt.mode.AppRemarks;
import com.zzh.mt.utils.CommonUtil;

import java.util.LinkedList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 腾翔信息 on 2017/8/2.
 */

public class RemarketAdapter extends RecyclerView.Adapter<RemarketAdapter.ViewHolder> implements ItemTouchHelperAdapter {
    private Context context;
    private LayoutInflater inflater;
    private LinkedList<AppRemarks.remarkListData> list = new LinkedList<>();
    public RemarketAdapter(Context context){
        this.context = context;
        inflater = LayoutInflater.from(context);
    }
    public void addData(LinkedList<AppRemarks.remarkListData> data){
        list.clear();
        list.addAll(data);
        notifyDataSetChanged();
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.my_remarks_recycler_item_layout,parent,false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.mTypenname.setText(list.get(position).getGroupName());
        holder.mCenter.setText(list.get(position).getChineseName());
        holder.mAttendTime.setText(list.get(position).getStartTime().substring(0,10));
        holder.mSartEnd.setText(CommonUtil.getTime(list.get(position).getStartTime())+"—"+CommonUtil.getTime(list.get(position).getEndTime()));
        if (onClickItemListener != null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickItemListener.onClickItem(holder.itemView,position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        return false;
    }

    @Override
    public void onItemDismiss(int position) {
        list.remove(position);
        notifyItemRemoved(position);
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.my_remarks_recycler_item_typename)
        TextView mTypenname;
        @BindView(R.id.my_remarks_recycler_item_center)
        TextView mCenter;
        @BindView(R.id.my_remarks_recycler_item_attendtime)
        TextView mAttendTime;
        @BindView(R.id.my_remarks_recycler_item_star_end)
        TextView mSartEnd;
        @BindView(R.id.image_delete)
        public ImageView iSchedule;
        public View vBackground; // 背景
        public View vItem;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            vBackground = itemView.findViewById(R.id.linear_background);
            vItem = itemView.findViewById(R.id.content);
            iSchedule.setImageResource(R.drawable.delete_icon);
        }
    }

    public interface OnClickItemListener{
        void onClickItem(View view, int postion);
    }

    public OnClickItemListener onClickItemListener;

    public void setOnClickItemListener(OnClickItemListener onClickItemListener){
        this.onClickItemListener = onClickItemListener;
    }
}
