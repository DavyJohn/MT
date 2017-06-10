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
import com.zhy.http.okhttp.OkHttpUtils;
import com.zzh.mt.R;
import com.zzh.mt.mode.CoursewareById;
import com.zzh.mt.sql.MyProvider;
import com.zzh.mt.utils.CommonUtil;
import com.zzh.mt.widget.HorizontalProgressBarWithNumber;

import org.w3c.dom.Text;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 腾翔信息 on 2017/6/9.
 */

public class MaterialPlAdapter extends RecyclerView.Adapter<MaterialPlAdapter.ViewHolder> {
    private Context context;
    private LayoutInflater inflater;
    private Cursor cursor;
    private float progressnum ;
    LinkedList<Integer> poslist = new LinkedList<>();
    private LinkedList<CoursewareById.CoursewareByIdData> list = new LinkedList<>();
    public MaterialPlAdapter(Context context){
        this.context = context;
        inflater = LayoutInflater.from(context);

    }

    public void addData(LinkedList<CoursewareById.CoursewareByIdData> data ){
        list.clear();
        list.addAll(data);
        notifyDataSetChanged();
    }

    public void addpostion(LinkedList<Integer> indexs){
        HashSet<Integer> set = new LinkedHashSet<>();
        set.addAll(indexs);
        indexs.clear();
        indexs.addAll(set);
        poslist.addAll(indexs);
        notifyDataSetChanged();
    }

    public void addprogress(float progress){
        progressnum = progress;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.material_pl_main_layout,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.mTitle.setText(list.get(position).getCoursewareName()+"."+list.get(position).getCoursewareType());
        holder.mSize.setText(CommonUtil.getDataSize(Long.parseLong(list.get(position).getCoursewareSize())));
        holder.mProgress.setVisibility(View.GONE);
        if (progressnum >0.0){
            holder.mProgress.setVisibility(View.VISIBLE);
            holder.mProgress.setProgress((int)(100*progressnum));
        }else if (progressnum == 1.1){
            holder.mSize.setText("下载失败");
        }else if (progressnum == 1.0){
            holder.mProgress.setVisibility(View.GONE);
            holder.mSize.setText(R.string.Finished);
        }
        if (poslist.size()!= 0){
            for (int i=0;i<poslist.size();i++){
                if (position == poslist.get(i)){
                    Picasso.with(context).load(R.drawable.sel_checkbox).into(holder.image);
                }else {

                }
            }

        }
        cursor = context.getContentResolver().query(MyProvider.URI,null,null,null,null);
        while (cursor.moveToNext()){
            if (list.get(position).getId().equals(cursor.getString(cursor.getColumnIndex("url")))){
                //已下载的
                holder.mSize.setText(R.string.Finished);
                holder.image.setVisibility(View.INVISIBLE);
            }else {
                //未下载的

            }
        }
        if (onClickItemListener != null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickItemListener.onClickItem(holder.mProgress,position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.pl_image)
        ImageView image;
        @BindView(R.id.pl_title)
        TextView mTitle;
        @BindView(R.id.pl_progressbar)
        public HorizontalProgressBarWithNumber mProgress;
        @BindView(R.id.pl_size)
        TextView mSize;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }


    }
    public interface OnClickItemListener{
        void onClickItem(HorizontalProgressBarWithNumber progressBarWithNumber, int postion);
    }

    public MaterialPlAdapter.OnClickItemListener onClickItemListener;

    public void setOnClickItemListener(MaterialPlAdapter.OnClickItemListener onClickItemListener){
        this.onClickItemListener = onClickItemListener;
    }
}
