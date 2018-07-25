package com.moons.xst.track.adapter;

import java.util.List;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
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

import com.moons.xst.track.AppConst;
import com.moons.xst.track.AppContext;
import com.moons.xst.track.R;
import com.moons.xst.track.bean.XJ_ResultHis;
import com.moons.xst.track.common.StringUtils;
import com.moons.xst.track.widget.PinnedHeaderListView;
import com.moons.xst.track.widget.PinnedHeaderListView.PinnedHeaderAdapter;

public class HisDJResultAdapter extends BaseAdapter  
		implements OnScrollListener , PinnedHeaderAdapter {
	
	// ===========================================================
	// Constants
	// ===========================================================

	private static final String TAG = HisDJResultAdapter.class.getSimpleName();
	
	// ===========================================================
	// Fields
	// ===========================================================

	private Context mContext;
	private List<XJ_ResultHis> mData;
	private LayoutInflater mLayoutInflater;
	
	// ===========================================================
	// Constructors
	// ===========================================================

	public HisDJResultAdapter(Context pContext, List<XJ_ResultHis> pData) {
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
            viewHolder.time=(TextView) convertView.findViewById(R.id.djplan_listitem_result);
            viewHolder.timeImage=(ImageView) convertView.findViewById(R.id.djplan_listitem_resulticon);
            viewHolder.moreIcon = (ImageView) convertView.findViewById(R.id.iv_hisdata_more);
            
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // 获取数据
        XJ_ResultHis itemEntity = (XJ_ResultHis) getItem(position);
        if (AppContext.getFontSize().equalsIgnoreCase(AppConst.FontSizeType.Normal.toString())) {
        	viewHolder.content.setTextAppearance(mContext, R.style.widget_listview_title2);
		}
		else if (AppContext.getFontSize().equalsIgnoreCase(AppConst.FontSizeType.Big.toString())) {
			viewHolder.content.setTextAppearance(mContext, R.style.widget_listview_title2_big);
		}
		else if (AppContext.getFontSize().equalsIgnoreCase(AppConst.FontSizeType.Huge.toString())) {
			viewHolder.content.setTextAppearance(mContext, R.style.widget_listview_title2_super);
		}
        viewHolder.content.setText(itemEntity.getPlanDesc_TX());
        String Result_TX =itemEntity.getResult_TX();
        viewHolder.type.setText(Result_TX);
        viewHolder.name.setText(itemEntity.getAppUserName_TX());
        viewHolder.time.setText(itemEntity.getCompleteTime_DT());
        viewHolder.timeImage.setImageResource(R.drawable.count_time_icon);
        
        if (!StringUtils.isEmpty(itemEntity.getDataType_CD())&&itemEntity.getDataType_CD().equalsIgnoreCase(AppConst.DJPLAN_DATACODE_CZ)) {
        	viewHolder.moreIcon.setVisibility(View.VISIBLE);
        } else {
        	viewHolder.moreIcon.setVisibility(View.GONE);
        }
        
        if (itemEntity.getException_YN().equals("1")) {
        	viewHolder.contentIcon.setImageResource(R.drawable.exdjresult_icon);
        	viewHolder.content.setTextColor(Color.RED);
        	viewHolder.name.setTextColor(Color.RED);
        	viewHolder.type.setTextColor(Color.RED);
        	viewHolder.time.setTextColor(Color.RED);
		} else {
			viewHolder.contentIcon.setImageResource(R.drawable.nordjresult_icon);
			viewHolder.content.setTextColor(mContext.getResources().getColor(R.color.listitem_black));
        	viewHolder.name.setTextColor(mContext.getResources().getColor(R.color.listitem_gray));
        	viewHolder.type.setTextColor(mContext.getResources().getColor(R.color.listitem_gray));
        	viewHolder.time.setTextColor(mContext.getResources().getColor(R.color.listitem_gray));
		}
        
        

        if (needTitle(position)) {
            // 显示标题并设置内容 
            viewHolder.title.setText(itemEntity.getCompleteTime_DT().split(" ")[0]);
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
	
	public int getErrorCount() {
		int errorCount = 0;
		if (null != mData) {
			for (int i = 0; i < mData.size(); i++) {
				if (mData.get(i).getException_YN().equals("1"))
					errorCount++;
			}
			return errorCount;
		}
		return errorCount;
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
		XJ_ResultHis itemEntity = (XJ_ResultHis) getItem(position);
		String headerValue = itemEntity.getCompleteTime_DT().split(" ")[0];
		
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
        XJ_ResultHis currentEntity = (XJ_ResultHis) getItem(position);
        XJ_ResultHis previousEntity = (XJ_ResultHis) getItem(position - 1);
		if (null == currentEntity || null == previousEntity) {
            return false;
        }
		
		String currentTitle = currentEntity.getCompleteTime_DT().split(" ")[0];
		String previousTitle = previousEntity.getCompleteTime_DT().split(" ")[0];
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
		XJ_ResultHis currentEntity = (XJ_ResultHis) getItem(position);
		XJ_ResultHis nextEntity = (XJ_ResultHis) getItem(position + 1);
	    if (null == currentEntity || null == nextEntity) {
            return false;
        }

	    // 获取两项header内容
	    String currentTitle = currentEntity.getCompleteTime_DT().split(" ")[0];
	    String nextTitle = nextEntity.getCompleteTime_DT().split(" ")[0];
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
        ImageView timeImage;
        TextView time;
        ImageView moreIcon;
    }
	
}
