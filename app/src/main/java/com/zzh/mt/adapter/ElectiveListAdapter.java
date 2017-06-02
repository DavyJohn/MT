package com.zzh.mt.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.zzh.mt.R;
import com.zzh.mt.mode.CoursesTrainingSessionsData;

import java.util.LinkedList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 腾翔信息 on 2017/5/27.
 */

public class ElectiveListAdapter extends RecyclerView.Adapter<ElectiveListAdapter.ViewHolder> {
    private Context context;
    private LayoutInflater inflater;
    private LinkedList<CoursesTrainingSessionsData.CourseNoListData> list = new LinkedList<>();
    public ElectiveListAdapter(Context context){
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    private int index = -1;
    public void addData(LinkedList<CoursesTrainingSessionsData.CourseNoListData> datas){
        list.clear();
        list.addAll(datas);
        notifyDataSetChanged();
    }
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

        holder.mTime.setText(list.get(position).getAttendTime().substring(0,10));
        holder.mNum.setText(list.get(position).getRemainingSeats());
        if (list.get(position).getIsSelected().equals("0")){
            Picasso.with(context).load(R.drawable.un_checkbox).into(holder.mBox);
        }else if (list.get(position).getIsSelected().equals("1")){
            Picasso.with(context).load(R.drawable.sel_checkbox).into(holder.mBox);
        }
        if ( index != -1){
            if (position == index ) {
                Picasso.with(context).load(R.drawable.sel_checkbox).into(holder.mBox);
            }else {
                Picasso.with(context).load(R.drawable.un_checkbox).into(holder.mBox);
            }
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
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.elective_checkbox)
        ImageView mBox;
        @BindView(R.id.elective_list_two_item_date)
        TextView mTime;
        @BindView(R.id.elective_list_two_item_num)
        TextView mNum;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
    public interface OnClickItemListener{
        void onClickItem(View itemview,ImageView view, int postion);
    }

    public ElectiveListAdapter.OnClickItemListener onClickItemListener;

    public void setOnClickItemListener(ElectiveListAdapter.OnClickItemListener onClickItemListener){
        this.onClickItemListener = onClickItemListener;
    }
}
