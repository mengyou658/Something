package com.moons.xst.track.adapter;

/**
 * CommonAdapter类，封装的公共Adapter类，主要目的是减少每个LISTVIEW配备一个ADAPTER
 * 使用方法：
 * 1.与ViewHolder配合使用，主要用于展示列表数据的LISTVIEW，对LISTVIEW里的ITEM
 * 项有BUTTON,CHECKBOX等按钮操作的，不适用，如果只有ITEM选项事件，则适用。
 * 2.调用方法，直接在ACTIVITY界面,例：
 * mlistview.setAdapter(mAdapter = new CommonAdapter<Bean>(
        		this.getApplicationContext(), list, R.layout.listitem){
        	@Override
        	public void convert(ViewHolder helper, Bean item)  
            {  
                helper.setText(R.id.tv_title, item.getTitle());   
                helper.setText(R.id.tv_time, item.getTime());  
                helper.setChecked(R.id.chk_yes, item.getChecked());              
            }  
        });
 */

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
  
public abstract class CommonAdapter<T> extends BaseAdapter  
{  
    protected LayoutInflater mInflater;  
    protected Context mContext;  
    protected List<T> mDatas;  
    protected final int mItemLayoutId;  
  
    public CommonAdapter(Context context, List<T> mDatas, int itemLayoutId)  
    {  
        this.mContext = context;  
        this.mInflater = LayoutInflater.from(mContext);  
        this.mDatas = mDatas;  
        this.mItemLayoutId = itemLayoutId;  
    }  
  
    @Override  
    public int getCount()  
    {  
        return mDatas.size();  
    }  
  
    @Override  
    public T getItem(int position)  
    {  
        return mDatas.get(position);  
    }  
  
    @Override  
    public long getItemId(int position)  
    {  
        return position;  
    }  
  
    @Override  
    public View getView(int position, View convertView, ViewGroup parent)  
    {  
    	final ViewHolder viewHolder = getViewHolder(position, convertView,  
                parent);  
        convert(viewHolder, getItem(position));  
        
        return viewHolder.getConvertView();  
    }  
  
    public abstract void convert(ViewHolder helper, T item);  
  
    private ViewHolder getViewHolder(int position, View convertView,  
            ViewGroup parent)  
    {  
        return ViewHolder.get(mContext, convertView, parent, mItemLayoutId,  
                position);  
    }  
}