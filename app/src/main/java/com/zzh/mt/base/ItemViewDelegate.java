package com.zzh.mt.base;

/**
 * Created by 腾翔信息 on 2017/5/11.
 */

public interface ItemViewDelegate<T>
{

    int getItemViewLayoutId();

    boolean isForViewType(T item, int position);

    void convert(ViewHolder holder, T t, int position);

}
