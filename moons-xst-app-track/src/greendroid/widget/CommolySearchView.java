package greendroid.widget;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import com.moons.xst.track.AppManager;
import com.moons.xst.track.R;
import com.moons.xst.track.ui.OperationBillAty;
import com.moons.xst.track.ui.UnifySearchAty;

/**
 * Created by junweiliu on 16/5/31.
 */
public class CommolySearchView<T> extends LinearLayout {
	/**
	 * 上下文
	 */
	private Context mContext;
	/**
	 * 取消按钮
	 */
	private TextView mcancel;
	/**
	 * 编辑框
	 */
	private EditText mEditText;
	/**
	 * 清除按钮
	 */
	private ImageView mClearImg;

	/**
	 * 适配器
	 */
	private BaseAdapter mAdapter;
	/**
	 * 数据源
	 */
	private List<T> mDatas = new ArrayList<T>();
	/**
	 * 数据源副本
	 */
	private List<T> mDupDatas = new ArrayList<T>();

	/**
	 * 筛选后的数据源
	 */
	private List<T> mFilterDatas = new ArrayList<T>();
	/**
	 * 筛选后的数据源副本
	 */
	private List<T> mDupFilterDatas = new ArrayList<T>();
	/**
	 * 清除图标
	 */
	private Bitmap mClearIcon;
	/**
	 * 清除图标距离左边边距
	 */
	private int mClearIconMarginLeft;
	/**
	 * 清除图标距离右边边距
	 */
	private int mClearIconMarginRight;
	/**
	 * 搜索文字大小
	 */
	private int mSearchTextSize;
	/**
	 * 搜索文字颜色
	 */
	private int mSearchTextColor;

	/**
	 * 回调接口
	 * 
	 * @param <T>
	 */
	public interface SearchDatas<T> {
		/**
		 * 参数一:全部数据,参数二:筛选后的数据,参数三:输入的内容
		 * 
		 * @param datas
		 * @param filterdatas
		 * @param inputstr
		 * @return
		 */
		List<T> filterDatas(List<T> datas, List<T> filterdatas, String inputstr);
	}

	/**
	 * 回调
	 */
	private SearchDatas<T> mListener;

	/**
	 * 设置回调
	 * 
	 * @param listener
	 */
	public void setSearchDataListener(SearchDatas<T> listener) {
		mListener = listener;

	}

	public CommolySearchView(Context context) {
		this(context, null);
	}

	public CommolySearchView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public CommolySearchView(Context context, AttributeSet attrs,
			int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		mContext = context;
		// 自定义属性
		// 绑定布局文件
		LayoutInflater.from(context).inflate(R.layout.searchview_layout, this);
		initView();
	}

	/**
	 * 初始化控件
	 */
	private void initView() {
		mEditText = (EditText) findViewById(R.id.et_search);
		mClearImg = (ImageView) findViewById(R.id.iv_search_clear);
		mcancel = (TextView) findViewById(R.id.tv_cancel);
		// 处理自定义属性
		if (0 != mClearIconMarginLeft || 0 != mClearIconMarginRight) {
			mClearImg.setPadding(mClearIconMarginLeft, 0,
					mClearIconMarginRight, 0);
		}
		if (null != mClearIcon) {
			mClearImg.setImageBitmap(mClearIcon);
		}
		if (0 != mSearchTextSize) {
			mEditText.setTextSize(mSearchTextSize);
		}
		if (0 != mSearchTextColor) {
			mEditText.setTextColor(mSearchTextColor);
		}
		// 清空按钮处理事件
		mClearImg.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				mEditText.setText("");
				mClearImg.setVisibility(View.GONE);
				if (null != mDatas) {
					mDatas.clear();
				}
				mDatas.addAll(mDupDatas);
				mAdapter.notifyDataSetChanged();
				reSetDatas();
			}
		});
		// 搜索栏处理事件
		mEditText.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence charSequence, int i,
					int i1, int i2) {

			}

			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1,
					int i2) {
				// 获取筛选后的数据
				mFilterDatas = mListener.filterDatas(mDupDatas, mFilterDatas,
						charSequence.toString());
				if (charSequence.toString().length() > 0
						&& !charSequence.toString().equals("")) {
					mClearImg.setVisibility(View.VISIBLE);
				} else {
					mClearImg.setVisibility(View.GONE);
				}
				if (null != mDatas) {
					mDatas.clear();
				}
				mDatas.addAll(mFilterDatas);
				mAdapter.notifyDataSetChanged();
				reSetDatas();
			}

			@Override
			public void afterTextChanged(Editable editable) {

			}
		});
		// 点击取消退出页面
		mcancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				AppManager.getAppManager().finishActivity(
						UnifySearchAty.class);
			}
		});
	}

	/**
	 * 获取筛选后的数据
	 * 
	 * @return
	 */
	public List<T> getFilterDatas() {
		return (null != mDupFilterDatas && mDupFilterDatas.size() > 0) ? mDupFilterDatas
				: mDupDatas;
	}

	/**
	 * 设置搜索提示信息
	 */
	public void setHint(String hint) {
		mEditText.setHint(hint);
	}

	/**
	 * 重置数据
	 */
	private void reSetDatas() {
		if (null != mFilterDatas) {
			if (null != mDupFilterDatas) {
				mDupFilterDatas.clear();
				mDupFilterDatas.addAll(mFilterDatas);
			}
			mFilterDatas.clear();
		}
	}

	/**
	 * 设置数据源
	 * 
	 * @param datas
	 */
	public void setDatas(List<T> datas) {
		if (null == datas) {
			return;
		}
		if (null != mDatas) {
			mDatas.clear();
		}
		if (null != mDupDatas) {
			mDupDatas.clear();
		}
		mDatas = datas;
		mDupDatas.addAll(mDatas);
	}

	/**
	 * 设置适配器
	 * 
	 * @param adapter
	 */
	public void setAdapter(BaseAdapter adapter) {
		if (null == adapter) {
			return;
		}
		mAdapter = adapter;
	}

	/**
	 * drawable转bitmap
	 * 
	 * @param drawable
	 * @return
	 */
	private Bitmap drawableToBitamp(Drawable drawable) {
		if (null == drawable) {
			return null;
		}
		if (drawable instanceof BitmapDrawable) {
			BitmapDrawable bd = (BitmapDrawable) drawable;
			return bd.getBitmap();
		}
		int w = drawable.getIntrinsicWidth();
		int h = drawable.getIntrinsicHeight();
		Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		drawable.setBounds(0, 0, w, h);
		drawable.draw(canvas);
		return bitmap;
	}

	/**
	 * 将px值转换为dip或dp值，保证尺寸大小不变
	 * 
	 * @param pxValue
	 * @return
	 * @param（DisplayMetrics类中属性density）
	 */
	public int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	/**
	 * 将px值转换为sp值，保证文字大小不变
	 * 
	 * @param pxValue
	 * @return
	 */
	public static int px2sp(Context context, float pxValue) {
		final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
		return (int) (pxValue / fontScale + 0.5f);
	}
}
