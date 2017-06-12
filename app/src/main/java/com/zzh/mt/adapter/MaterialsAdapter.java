package com.zzh.mt.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.zzh.mt.R;
import com.zzh.mt.mode.CoursewareById;
import com.zzh.mt.sql.MyProvider;
import com.zzh.mt.utils.CommonUtil;
import com.zzh.mt.widget.HorizontalProgressBarWithNumber;

import java.util.LinkedList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 腾翔信息 on 2017/6/8.
 */

public class MaterialsAdapter extends RecyclerView.Adapter<MaterialsAdapter.ViewHolder> {
    private Context context;
    private Cursor cursor;
    private LayoutInflater inflater;
    private LinkedList<CoursewareById.CoursewareByIdData> list = new LinkedList<>();
    public MaterialsAdapter(Context context){
        this.context = context;
        inflater = LayoutInflater.from(context);
    }
    public void addData(LinkedList<CoursewareById.CoursewareByIdData> data ){
        list.clear();
        list.addAll(data);
        notifyDataSetChanged();
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.materials_item_main_layout,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        if (onClickItemListener != null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickItemListener.onClickItem(holder.mProgress,holder.mSize,holder.mDown,list.get(position).getId(),position);
                }
            });
        }

        cursor = context.getContentResolver().query(MyProvider.URI,null,null,null,null);
        holder.mSize.setText(CommonUtil.getDataSize(Long.parseLong(list.get(position).getCoursewareSize())));
        holder.mProgress.setVisibility(View.GONE);
        holder.mDown.setText(R.string.click_down);
        Picasso.with(context).load(list.get(position).getCoursewareUrl()).placeholder(R.drawable.imag_demo).error(R.drawable.imag_demo).into(holder.image
        );
        //数据
        holder.mTitle.setText(list.get(position).getCoursewareName()+"."+list.get(position).getCoursewareType());
        while (cursor.moveToNext()){
                if (list.get(position).getId().equals(cursor.getString(cursor.getColumnIndex("url")))){
                    holder.mProgress.setVisibility(View.GONE);
                    holder.mDown.setText(R.string.Finished);
                }else {
                    holder.mSize.setText(CommonUtil.getDataSize(Long.parseLong(list.get(position).getCoursewareSize())));
                    holder.mProgress.setVisibility(View.GONE);
                }
            }


        }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.materials_title)
        TextView mTitle;
        @BindView(R.id.progress)
        HorizontalProgressBarWithNumber mProgress;
        @BindView(R.id.data_image)
        ImageView image;
        @BindView(R.id.materials_size)
        TextView mSize;
        @BindView(R.id.materials_down)
        TextView mDown;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    public interface OnClickItemListener{
        void onClickItem(HorizontalProgressBarWithNumber progress,TextView size,TextView down,String id, int postion);
    }

    public MaterialsAdapter.OnClickItemListener onClickItemListener;

    public void setOnClickItemListener(MaterialsAdapter.OnClickItemListener onClickItemListener){
        this.onClickItemListener = onClickItemListener;
    }
}
