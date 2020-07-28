package com.mobile.stu.base;

import android.content.Context;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

/**
 * RecyclerView基础适配器<BR>
 *
 * @param <E>  数据对象
 * @param <VH> ViewHolder
 */
public abstract class BaseRecyclerViewAdapter<E, VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {
    /**
     * Item点击事件监听<BR>
     */
    public interface OnItemClickListener {
        /**
         * Item点击回调<BR>
         *
         * @param view     点击视图
         * @param position 点击位置
         */
        void onItemClick(View view, int position);
    }

    /**
     * 上下文
     */
    protected Context mContext;

    /**
     * 数据源
     */
    protected List<E> mDataSource = new ArrayList<E>();

    /**
     * 外部点击事件
     */
    protected OnItemClickListener mOnItemClickListener;

    /**
     * 构造方法
     *
     * @param context 关联活动
     */
    public BaseRecyclerViewAdapter(Context context) {
        mContext = context;
    }

    /**
     * 设置数据源<BR>
     *
     * @param dataSource 数据源
     */
    public void setDataSource(List<E> dataSource) {
        mDataSource.clear();
        if (dataSource != null && dataSource.size() > 0) {
            mDataSource.addAll(dataSource);
        }
    }

    public List<E> getDataSource() {
        return mDataSource;
    }

    /**
     *
     * @param data
     */
    public void addData(E data) {
        mDataSource.add(data);
        notifyDataSetChanged();
    }

    public void clear() {
        mDataSource.clear();
        notifyDataSetChanged();
    }


    /**
     * 设置Item点击事件回调<BR>
     *
     * @param listener Item点击事件回调
     */
    public void setOnItemClickListener(OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    @Override
    public int getItemCount() {
        return mDataSource.size();
    }
}
