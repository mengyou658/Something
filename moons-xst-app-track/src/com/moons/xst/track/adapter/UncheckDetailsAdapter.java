package com.moons.xst.track.adapter;

import java.util.List;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.moons.xst.buss.DJPlanForUnCheck;
import com.moons.xst.track.AppConst;
import com.moons.xst.track.AppContext;
import com.moons.xst.track.R;
import com.moons.xst.track.widget.PinnedHeaderListView;
import com.moons.xst.track.widget.PinnedHeaderListView.PinnedHeaderAdapter;

public class UncheckDetailsAdapter extends BaseAdapter  
		implements OnScrollListener , PinnedHeaderAdapter {
	
	// ===========================================================
	// Constants
	// ===========================================================

	private static final String TAG = UncheckDetailsAdapter.class.getSimpleName();
	
	// ===========================================================
	// Fields
	// ===========================================================

	private Context mContext;
	private List<DJPlanForUnCheck> mData;
	private LayoutInflater mLayoutInflater;
	
	// ===========================================================
	// Constructors
	// ===========================================================

	public UncheckDetailsAdapter(Context pContext, List<DJPlanForUnCheck> pData) {
		mContext = pContext;
		mData = pData;
		
		mLayoutInflater = LayoutInflater.from(mContext);
	}
	
	
	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
	 // 常见的优化ViewHolder
        ViewHolder viewHolder = null;
        if (null == convertView) {
            convertView = mLayoutInflater.inflate(R.layout.listitem_uncheck_details, null);
            
            viewHolder = new ViewHolder();
            viewHolder.title = (TextView) convertView.findViewById(R.id.title); 
            viewHolder.content = (TextView) convertView.findViewById(R.id.djplan_listitem_title);
            viewHolder.type = (TextView) convertView.findViewById(R.id.djplan_listitem_Type);
            viewHolder.name = (TextView) convertView.findViewById(R.id.djplan_listitem_Time);
            viewHolder.contentIcon = (ImageView) convertView.findViewById(R.id.djplan_listitem_flag);
            
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // 获取数据
        DJPlanForUnCheck itemEntity = (DJPlanForUnCheck) getItem(position);
        if (AppContext.getFontSize().equalsIgnoreCase(AppConst.FontSizeType.Normal.toString())) {
        	viewHolder.content.setTextAppearance(mContext, R.style.widget_listview_title2);
		}
		else if (AppContext.getFontSize().equalsIgnoreCase(AppConst.FontSizeType.Big.toString())) {
			viewHolder.content.setTextAppearance(mContext, R.style.widget_listview_title2_big);
		}
		else if (AppContext.getFontSize().equalsIgnoreCase(AppConst.FontSizeType.Huge.toString())) {
			viewHolder.content.setTextAppearance(mContext, R.style.widget_listview_title2_super);
		}
        viewHolder.content.setText(itemEntity.getDJPlan().getPlanDesc_TX());
        String Result_TX = "";
		if (itemEntity.getDJPlan().getDataType_CD()
				.equalsIgnoreCase(AppConst.DJPLAN_DATACODE_GC)) {// 观察类
			Result_TX = mContext.getString(R.string.plan_type_gc);
		} else if (itemEntity.getDJPlan().getDataType_CD()
				.equalsIgnoreCase(AppConst.DJPLAN_DATACODE_JL)) {// 记录类
			Result_TX = mContext.getString(R.string.plan_type_jl);
		} else if (itemEntity.getDJPlan().getDataType_CD()
				.equalsIgnoreCase(AppConst.DJPLAN_DATACODE_CW)) {// 测温类
			Result_TX = mContext.getString(R.string.plan_type_cw);
		} else if (itemEntity.getDJPlan().getDataType_CD()
				.equalsIgnoreCase(AppConst.DJPLAN_DATACODE_CZ)) {// 测振类
			Result_TX = mContext.getString(R.string.plan_type_cz);
		} else {                                                 // 测速类
			Result_TX = mContext.getString(R.string.plan_type_cs);
		}
        viewHolder.type.setText(Result_TX);
        viewHolder.name.setText(itemEntity.getDJPlan().getPlace_TX());
        viewHolder.contentIcon.setImageResource(R.drawable.icon_cp);

        if (needTitle(position)) {
            // 显示标题并设置内容 
            viewHolder.title.setText(itemEntity.getDJPlan().getPlace_TX());
            viewHolder.title.setVisibility(View.VISIBLE);
        } else {
            // 内容项隐藏标题
            viewHolder.title.setVisibility(View.GONE);
        }
        
        return convertView;
	}
	
	@Override
	public int getCount() {
		if (null != mData) {
			return mData.size();
		}
		return 0;
	}

	@Override
	public Object getItem(int position) {
		if (null != mData && position < getCount()) {
			return mData.get(position);
		}
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	
	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		
		if ( view instanceof PinnedHeaderListView) {
			((PinnedHeaderListView) view).controlPinnedHeader(firstVisibleItem);	
		}
	}
	
	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
	}
	
	
	@Override
	public int getPinnedHeaderState(int position) {
		if (getCount() == 0 || position < 0) {
			return PinnedHeaderAdapter.PINNED_HEADER_GONE;
		}
		
		if (isMove(position) == true) {
			return PinnedHeaderAdapter.PINNED_HEADER_PUSHED_UP;
		}
		
		return PinnedHeaderAdapter.PINNED_HEADER_VISIBLE;
	}


	@Override
	public void configurePinnedHeader(View headerView, int position, int alpaha) {
		// 设置标题的内容
		DJPlanForUnCheck itemEntity = (DJPlanForUnCheck) getItem(position);
		String headerValue = itemEntity.getDJPlan().getPlace_TX();
		
		Log.e(TAG, "header = " + headerValue);
		
		if (!TextUtils.isEmpty(headerValue)) {
			TextView headerTextView = (TextView) headerView.findViewById(R.id.header);
			headerTextView.setText( headerValue );
		}
		
	}
	
	// ===========================================================
	// Methods
	// ===========================================================

	/**
	 * 判断是否需要显示标题
	 * 
	 * @param position
	 * @return
	 */
	private boolean needTitle(int position) {
		// 第一个肯定是分类
		if (position == 0) {
			return true;
		}
		
		// 异常处理
        if (position < 0) {
            return false;
        }
		 
		// 当前  // 上一个
        DJPlanForUnCheck currentEntity = (DJPlanForUnCheck) getItem(position);
        DJPlanForUnCheck previousEntity = (DJPlanForUnCheck) getItem(position - 1);
		if (null == currentEntity || null == previousEntity) {
            return false;
        }
		
		String currentTitle = currentEntity.getDJPlan().getPlace_TX();
		String previousTitle = previousEntity.getDJPlan().getPlace_TX();
		if (null == previousTitle || null == currentTitle) {
            return false;
        }
		
		// 当前item分类名和上一个item分类名不同，则表示两item属于不同分类
		if (currentTitle.equals(previousTitle)) {
			return false;
		}
		
		return true;
	}


	private boolean isMove(int position) {
	    // 获取当前与下一项
		DJPlanForUnCheck currentEntity = (DJPlanForUnCheck) getItem(position);
		DJPlanForUnCheck nextEntity = (DJPlanForUnCheck) getItem(position + 1);
	    if (null == currentEntity || null == nextEntity) {
            return false;
        }

	    // 获取两项header内容
	    String currentTitle = currentEntity.getDJPlan().getPlace_TX();
	    String nextTitle = nextEntity.getDJPlan().getPlace_TX();
	    if (null == currentTitle || null == nextTitle) {
            return false;
        }
	    
	    // 当前不等于下一项header，当前项需要移动了
	    if (!currentTitle.equals(nextTitle)) {
            return true;
        }
	    
	    return false;
	}
	
	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
	
	private class ViewHolder {
        TextView title;
        TextView content;
        TextView type;
        TextView name;
        ImageView contentIcon;
    }
	
}
