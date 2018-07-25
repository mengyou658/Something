package com.moons.xst.track.widget.PieChart.piechartview;

import java.text.DecimalFormat;
import java.util.LinkedList;
import java.util.List;

import com.moons.xst.track.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
import android.text.Layout.Alignment;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector.OnGestureListener;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * 
 * @version 1.0
 * @author ZGY
 */
@SuppressLint("DrawAllocation")
public class PieChartView extends View implements Runnable, OnGestureListener {

	/**
	 * 被选中的饼状图上的块显示在右侧
	 */
	public static final int TO_RIGHT = 0;
	/**
	 * 被选中的饼状图上的块显示在底部
	 */
	public static final int TO_BOTTOM = 1;
	/**
	 * 被选中的饼状图上的块显示在左侧
	 */
	public static final int TO_LEFT = 2;
	/**
	 * 被选中的饼状图上的块显示在顶部
	 */
	public static final int TO_TOP = 3;
	/**
	 * 被选中的饼状图不旋转
	 */
	public static final int NO_ROTATE = -1;

	/**
	 * 圆内切换图标的坐标，用于监听点击事件
	 */
	private float triggerX, triggerY;

	// 默认颜色
	private static final String[] DEFAULT_ITEMS_COLORS = { "#8A2BE2",
			"#FFD700", "#7CFC00", "#FF6347", "#DA70D6", "#FFFF00", "#A52A2A",
			"#A9A9A9", "#228B22", "#DC143C", "#94d4d4", "#88b4bb", "#76aeaf",
			"#2a6d82", "#d9508a", "#fe9507", "#fef778", "#6aa786", "#35c2d1",
			"#405980", "#95a57c", "#d9b8a2", "#bf8686", "#b33050", "#c12552" };
	private static final String DEAFULT_BORDER_COLOR = "#000000";// 饼状图边缘圆环默认颜色
	private static final int DEFAULT_STROLE_WIDTH = 2;// 默认圆边的宽度
	private static final int DEFAULT_RADIUS = 100;// 默认饼状图半径，不包括边缘
	private static final int DEFAULT_SEPARATE_DISTENCE = 10;// 被选中的item分离的距离
	private static final int TIME_HANDLER_DELY = 10;// 旋转间隔10毫秒
	private static final float MIN_ANIMSPEED = (float) 0.5;// 旋转间隔最小度数
	private static final float MAX_ANIMSPEED = (float) 10.0;// 旋转间隔最大度数
	// private static final float DEFAULT_ANIM_SPEED = (float) 1.7;// 默认速度

	private float rotateSpeed = (float) 1; // 旋转间隔度数，用来表示速度

	private float total;// 总大小
	private float[] itemSizesTemp;// 传递过来的items
	private float[] itemsSizes;// 最终的items
	private String[] itemsColors;// 每一项的颜色
	private float[] itemsAngle;// 每一项所占的角度
	private float[] itemsBeginAngle;// 每一项的起始角度
	private float[] itemsRate;// 每一块占的比例
	private float rotateAng = 0;// 用于旋转时的递减角度
	private float lastAng = 0;// 旋转至角度
	private boolean bClockWise; // 顺时针旋转
	private boolean isRotating;// 正在旋转，则点击无效
	private boolean isAnimEnabled = true;
	private String radiusBorderStrokeColor;// 边缘颜色

	private Bitmap bitmap;// 圆内图标

	private float strokeWidth = 0;// 边缘半径
	private float radius;// 内圆半径
	private int itemPostion = -1;// 被点击的块的id
	private int rotateWhere = 1;// 转到什么位置
	private float separateDistence = 10;// 被选中的块分离的距离

	private Handler rotateHandler = new Handler();
	private static final String TAG = "ParBarView";
	int position;

	private String showInfo = "";

	private int totalNum;

	private Context context;

	private RectF oval;

	// 是否拖动
	private boolean isDraging = false;
	// 这个是饼图的中心位置
	private Point center;
	// 事件距离饼图中心的距离
	private int eventRadius = 0;
	// 上一个事件的点
	private Point lastEventPoint;

	private int currentTargetIndex = -1;

	private GestureDetector mGDetector;
	private int extraHeight = 50;

	/**
	 * 若需要用java代码显示布局，则使用此构造方法
	 * 
	 * @param context
	 *            上下文
	 * @param itemColors
	 *            饼状图上从第一个块开始，各个块的颜色； 如两块：传值String[] itemColors = {"#000000",
	 *            "#FFFFFF"}， 为空，则显示随机颜色。参见： {@link #setItemsColors}
	 * @param itemSizes
	 *            饼状图上各个块的大小；如两块：传值float[] itemSizes = {(float) 50.0, (float)
	 *            50.0}，为空，则只显示圆环。参见： {@link #setItemsSizes}
	 * @param total
	 *            饼状图所表示的整体大小；大于或等于 itemSizes 各项数据之和，若小于，则等于，默认等于。参见：
	 *            {@link #setTotal}
	 * @param radius
	 *            饼状图的半径，但不包括边缘圆环的粗度。默认为 100。参见：{@link #setRaduis}
	 * @param strokeWidth
	 *            饼状图边缘圆环的粗度；默认为 3。参见：{@link #setStrokeWidth}
	 * @param strokeColor
	 *            饼状图边缘圆环的颜色，默认为黑色(#000000).参见：{@link #setStrokeColor}
	 * @param rotateWhere
	 *            若动画已开启，则饼状图上被点击的块旋转到的位置；左侧：{@link #TO_LEFT}； 顶部：
	 *            {@link #TO_TOP}； 右侧：{@link #TO_RIGHT}； 底部： {@link #TO_BOTTOM}；
	 *            不旋转：{@link #NO_ROTATE}； 默认为 ： {@link #TO_RIGHT}； 参见：
	 *            {@link #setRotateWhere}
	 * @param separateDistence
	 *            被选中的块的偏移距离；为了凸显备选中的块，偏移凸显；若为负或未设置，默认为 ：
	 *            {@link #DEFAULT_SEPARATE_DISTENCE}。 参见：
	 *            {@link #setSeparateDistence};
	 * @param rotateSpeed
	 *            若动画开启，则表示旋转动画的速度；若为负或未设置，默认为 ：{@link #DEFAULT_ANIM_SPEED}。 参见：
	 *            {@link #setRotateSpeed};
	 */
	public PieChartView(Context context, String[] itemColors,
			float[] itemSizes, float total, int radius, int strokeWidth,
			String strokeColor, int rotateWhere, float separateDistence,
			float rotateSpeed) {
		super(context);
		this.context = context;
		this.rotateWhere = rotateWhere;

		if (itemSizes != null && itemSizes.length > 0) {
			this.itemSizesTemp = itemSizes;
			this.total = total;
			reSetTotal();
			refreshItemsAngs();
		}

		if (radius < 0) {
			this.radius = DEFAULT_RADIUS;
		} else {
			this.radius = radius;
		}
		if (strokeWidth < 0) {
			strokeWidth = DEFAULT_STROLE_WIDTH;
		} else {
			this.strokeWidth = strokeWidth;
		}

		this.radiusBorderStrokeColor = strokeColor;

		if (itemColors == null) {
			// 设置默认颜色
			setDefaultColor();
		} else if (itemColors.length < itemSizes.length) {
			// 颜色不足，设置剩余颜色
			this.itemsColors = itemColors;
			setLeftColor();
		} else {
			this.itemsColors = itemColors;
		}

		if (separateDistence < 0) {
			this.separateDistence = DEFAULT_SEPARATE_DISTENCE;
		} else {
			this.separateDistence = separateDistence;
		}

		// 控制最大最小范围内
		if (rotateSpeed < MIN_ANIMSPEED) {
			rotateSpeed = MIN_ANIMSPEED;
		}
		if (rotateSpeed > MAX_ANIMSPEED) {
			rotateSpeed = MAX_ANIMSPEED;
		}
		this.rotateSpeed = rotateSpeed;
		mGDetector = new GestureDetector(context, this);
		invalidate();
	}

	/**
	 * XML 布局添加PieView 如：
	 * 
	 * <br/>
	 * <br/>
	 * &lt;com.zgy.piechartview.PieChartView <br/>
	 * android:id="@+id/parbar_view" <br/>
	 * android:layout_width="wrap_content" <br/>
	 * android:layout_height="wrap_content"/&gt; <br/>
	 * 
	 * <br/>
	 * <br/>
	 * 其它参数需调用 set 方法进行设置。
	 * 
	 * @param context
	 * @param attrs
	 */
	public PieChartView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// 若使用xml布局，获得相应参数
		// this.radius = attrs.getAttributeFloatValue(null,
		// "layout_centercircle_radius", DEFAULT_RADIUS);//
		// 内圆半径，默认100
		// this.strokeWidth = attrs.getAttributeFloatValue(null,
		// "layout_borderWidth",
		// DEFAULT_STROLE_WIDTH);// 外环半径，默认2
		// 其它默认设置：
		this.context = context;
		this.radiusBorderStrokeColor = DEAFULT_BORDER_COLOR;// 外环颜色，默认黑色
		mGDetector = new GestureDetector(context, this);
		invalidate();
	}

	/**
	 * 饼状图的半径，但不包括边缘圆环的粗度。默认为 100。
	 * 
	 * @param radius
	 *            默认为 100
	 */
	public void setRaduis(int radius) {
		if (radius < 0) {
			this.radius = DEFAULT_RADIUS;
		} else {
			this.radius = radius;
		}
		invalidate();
	}

	/**
	 * 获得饼状图的半径，但不包括边缘圆环的粗度
	 * 
	 * @return 饼状图的半径，不包括边缘圆环的粗度
	 */
	public float getRaduis() {
		return this.radius;
	}

	/**
	 * 设置饼状图边缘圆环的粗度
	 * 
	 * @param strokeWidth
	 *            默认为 2
	 */
	public void setStrokeWidth(int strokeWidth) {
		if (strokeWidth < 0) {
			strokeWidth = DEFAULT_STROLE_WIDTH;
		} else {
			this.strokeWidth = strokeWidth;
		}
		invalidate();
	}

	/**
	 * 获得饼状图边缘圆环的粗度
	 * 
	 * @return float 饼状图边缘圆环的粗度
	 */
	public float getStrokeWidth() {
		return strokeWidth;
	}

	/**
	 * 饼状图边缘圆环的颜色，默认为黑色(#000000).
	 * 
	 * @param strokeColor
	 *            默认为黑色
	 */
	public void setStrokeColor(String strokeColor) {

		this.radiusBorderStrokeColor = strokeColor;

		invalidate();
	}

	/**
	 * 获得饼状图边缘圆环的颜色
	 * 
	 * @return 返回饼状图边缘圆环的颜色值
	 */
	public String getStrokeColor() {

		return this.radiusBorderStrokeColor;
	}

	/**
	 * 饼状图上从第一个块开始，各个块的颜色； 如两块：传值String[] itemColors = {"#000000", "#FFFFFF"}，
	 * 为空，则显示随机颜色。
	 * 
	 * @param colors
	 *            默认使用既定颜色
	 */
	public void setItemsColors(String[] colors) {
		if (itemsSizes != null && itemsSizes.length > 0) {
			if (colors == null) {
				// 设置默认颜色
				setDefaultColor();
			} else if (colors.length < itemsSizes.length) {
				// 颜色不足，设置剩余颜色
				this.itemsColors = colors;
				setLeftColor();
			} else {
				this.itemsColors = colors;
			}
		}

		invalidate();
	}

	/**
	 * 获得各个item块的颜色
	 * 
	 * @return 返回当前各个块的颜色值所组成的数组。
	 */
	public String[] getItemsColors() {
		return this.itemsColors;
	}

	/**
	 * 饼状图上各个块的大小；如两块：传值float[] itemSizes = {(float) 50.0, (float)
	 * 50.0}，为空，则只显示圆环。
	 * 
	 * @param items
	 *            各个块的大小，由 float 类型数据组成的数组
	 */
	public void setItemsSizes(float[] items) {
		if (items != null && items.length > 0) {
			this.itemSizesTemp = items;
			reSetTotal();
			refreshItemsAngs();
			setItemsColors(itemsColors);
		}
		invalidate();
	}

	/**
	 * 获得各个块的值
	 * 
	 * @return 若传入的各个块的值之和等于整体，返回的即为传入的各个块的大小构成的float数组；<br/>
	 *         若传入的各个块的值之和小于传入整体的值，返回的各个块的值所组成的数组中还包括剩余块的值，在数组的最后一项。
	 */
	public float[] getItemsSizes() {
		return this.itemSizesTemp;
	}

	/**
	 * 设置真实的总数
	 * 
	 * @param total
	 */
	public void setActualTotal(int total) {
		this.totalNum = total;
	}

	/**
	 * 饼状图所表示的整体大小；如果用的百分比的，则为100
	 * 
	 * @param total
	 *            大于或等于 itemSizes 各项数据之和，若小于，则等于，默认等于。
	 */
	public void setTotal(int total) {
		this.total = total;

		reSetTotal();

		invalidate();
	}

	public void relaseTotal(int total) {
		this.total = total;
	}

	/**
	 * 获得整体大小
	 * 
	 * @return 大于等于传入的所有块之和；
	 */
	public float getTotal() {
		return this.total;
	}

	/**
	 * 是否开启动画
	 * 
	 * @param isAnimEnabled
	 *            true为开启
	 */
	public void setAnimEnabled(boolean isAnimEnabled) {
		this.isAnimEnabled = isAnimEnabled;
		invalidate();
	}

	/**
	 * 判断是否开启了旋转动画
	 * 
	 * @return true为开启，false为未开启
	 */
	public boolean isAnimEnabled() {
		return isAnimEnabled;
	}

	/**
	 * 若开启了动画，设置动画的旋转速度；若未开启动画，待动画开启时方可生效。开启动画参见：
	 * {@link #setAnimEnabled(boolean)};
	 * 
	 * @param rotateSpeed
	 *            数值在0.5~5.0之间，小于0.5则为0.5；大于5.0，则为5.0。若为负或未设置，默认为 ：
	 *            {@link #DEFAULT_ANIM_SPEED}
	 */
	public void setRotateSpeed(float rotateSpeed) {
		// 控制最大最小范围内
		if (rotateSpeed < MIN_ANIMSPEED) {
			rotateSpeed = MIN_ANIMSPEED;
		}
		if (rotateSpeed > MAX_ANIMSPEED) {
			rotateSpeed = MAX_ANIMSPEED;
		}
		this.rotateSpeed = rotateSpeed;
	}

	/**
	 * 若开启了动画，获得动画的旋转速度
	 * 
	 * @return 若已开启动画，返回0.5~5之间的数值；否则返回0；
	 */
	public float getRotateSpeed() {
		if (isAnimEnabled()) {
			return rotateSpeed;
		} else {
			return 0;
		}
	}

	// 获取额外高度
	public int getExtraHeight() {
		return extraHeight;
	}

	// 设置额外高度
	public void setExtraHeight(int extraHeight) {
		this.extraHeight = extraHeight;
	}

	/**
	 * 设置要显示的item
	 * 
	 * @param position
	 *            被选中的块的 id；即描述各个块的大小的数组中某项数据的id号。
	 * @param anim
	 *            是否开启动旋转画；true为开启
	 * @param listen
	 *            是否可以监听到此消息；true为可以监听；监听方法参见：
	 *            {@link OnPieChartItemSelectedLinstener#onPieChartItemSelected(PieChartView, int, String, float, float, boolean, float)}
	 */
	public void setShowItem(int position, boolean anim, boolean listen) {
		if (itemsSizes != null && position < itemsSizes.length && position >= 0) {

			this.itemPostion = position;
			this.position = position;

			/**
			 * 是否监听此动作
			 */
			if (listen) {
				notifySelectedListeners(position, itemsColors[position],
						itemsSizes[position], itemsRate[position],
						isPositionFree(position),
						getAnimTime(Math.abs(lastAng - rotateAng)));// 发出选中条目的消息
			}

			if (this.rotateWhere == NO_ROTATE) {

			} else {

				// float offset = getOffsetOfPartCenter(itemsAngle[position],
				// position);
				// startDegree = -46.797714f;
				// Log.i(TAG, "=====>>>offset:"+offset
				// +"  startDegree:"+startDegree);

				// 获取要旋转的角度
				lastAng = getLastRotateAngle(position);
				Log.i(TAG, "=====>>>lastAng:" + lastAng);
				Log.i(TAG, "=====>>>startDegree:" + startDegree);
				if (startDegree == 0)
					startDegree = lastAng;

				if (anim) {
					rotateAng = 0;
					if (lastAng > 0) {
						// 顺时针旋转
						bClockWise = true;
					} else {
						// 逆时针旋转
						bClockWise = false;
					}
					isRotating = true;
				} else {
					rotateAng = lastAng;
				}
				rotateHandler.postDelayed(this, 1);
			}

		}
	}

	/**
	 * 获得显示的item
	 * 
	 * @return 当前显示的块的id
	 */
	public int getShowItem() {
		return itemPostion;
	}

	/**
	 * 设置饼状图上被点击的块转到的位置
	 * 
	 * @param rotateWhere
	 * <br/>
	 *            位置： <li>左侧：{@link #TO_LEFT}</li> <li>顶部：{@link #TO_TOP}</li>
	 *            <li>右侧：{@link #TO_RIGHT}</li> <li>底部：{@link #TO_BOTTOM}</li> <br/>
	 *            <li>不旋转：{@link #NO_ROTATE}；</li> <br/>
	 * <br/>
	 *            默认为 : {@link #TO_RIGHT}
	 */
	public void setRotateWhere(int rotateWhere) {
		this.rotateWhere = rotateWhere;
	}

	/**
	 * 判断饼状图上被点击的块旋转到的位置
	 * 
	 * @return 位置： <li>左侧：{@link #TO_LEFT}</li> <li>顶部：{@link #TO_TOP}</li> <li>
	 *         右侧：{@link #TO_RIGHT}</li> <li>
	 *         底部：{@link #TO_BOTTOM}</li><li>不旋转：{@link #NO_ROTATE}；</li> <br/>
	 * 
	 */
	public int getRotateWhere() {
		return rotateWhere;
	}

	/**
	 * 设置被选中的块分离的距离
	 * 
	 * @param separateDistence
	 *            若为负或未设置，默认为 ：{@link #DEFAULT_SEPARATE_DISTENCE}
	 */
	public void setSeparateDistence(float separateDistence) {
		if (separateDistence < 0) {
			separateDistence = DEFAULT_SEPARATE_DISTENCE;
		}
		this.separateDistence = separateDistence;
		invalidate();
	}

	/**
	 * 返回被选中的块分离的距离
	 * 
	 * @return 距离饼状图的距离
	 */
	public float getSeparateDistence() {
		return separateDistence;
	}

	@SuppressLint("DrawAllocation")
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		float bigRadius = radius + strokeWidth;

		float centerX = getWidth() / 2;
		float centerY = getHeight() / 2;
		// float centerXY = separateDistence + bigRadius;
		Paint paint = new Paint();
		paint.setAntiAlias(true);

		if (strokeWidth != 0) {
			paint.setStyle(Paint.Style.STROKE);
			paint.setColor(Color.parseColor(radiusBorderStrokeColor));
			paint.setStrokeWidth(strokeWidth);
			// canvas.drawCircle(centerXY, centerXY, bigRadius, paint);
			canvas.drawCircle(centerX, centerY, bigRadius, paint);
			// Log.e(TAG, "画边");
		}

		canvas.save();
		// 存在items，则画内圆
		if (itemsAngle != null && itemsBeginAngle != null) {
			float rigthBottom = 2 * (radius + strokeWidth) + separateDistence;
			float leftTop = separateDistence;

			if (!isDraging) {
				Log.i(TAG, "自动旋转");
				// 旋转
				// canvas.rotate(rotateAng, centerXY, centerXY);
				canvas.rotate(rotateAng, centerX, centerY);

				paint.setStrokeWidth(1);
				oval = new RectF(leftTop, leftTop, rigthBottom, rigthBottom);
				for (int i = 0; i < itemsAngle.length; i++) {
					if (itemPostion == i && !isRotating) {
						Log.e(TAG, "draw last  " + i);

						switch (rotateWhere) {
						case TO_RIGHT:
							// 向右移动
							oval = new RectF(leftTop, leftTop, rigthBottom
									+ separateDistence, rigthBottom);
							break;
						case TO_TOP:
							// 向上移动
							oval = new RectF(leftTop, leftTop
									- separateDistence, rigthBottom,
									rigthBottom);
							break;
						case TO_BOTTOM:
							// 向下移动
							oval = new RectF(leftTop, leftTop, rigthBottom,
									rigthBottom + separateDistence);
							break;
						case TO_LEFT:
							// 向左移动
							oval = new RectF(leftTop - separateDistence,
									leftTop, rigthBottom, rigthBottom);
							break;
						default:
							break;
						}
					} else {
						oval = new RectF(leftTop, leftTop, rigthBottom,
								rigthBottom);

					}
					// paint.setStyle(Paint.Style.FILL);
					paint.setColor(Color.parseColor(itemsColors[i]));
					canvas.drawArc(oval, itemsBeginAngle[i], itemsAngle[i],
							true, paint);
				}
			} else {
				Log.i(TAG, "手动拖动");

				float startAngle = this.startDegree;

				// canvas.rotate(rotateAng, centerX, centerY);

				oval = new RectF(leftTop, leftTop, rigthBottom, rigthBottom);
				// 手动拖动
				for (int i = 0; i < itemsAngle.length; i++) {

					if (position == i) {
						Log.e(TAG, "draw last  " + i);

						switch (rotateWhere) {
						case TO_RIGHT:
							// 向右移动
							oval = new RectF(leftTop, leftTop, rigthBottom
									+ separateDistence, rigthBottom);
							break;
						case TO_TOP:
							// 向上移动
							oval = new RectF(leftTop, leftTop
									- separateDistence, rigthBottom,
									rigthBottom);
							break;
						case TO_BOTTOM:
							// 向下移动
							oval = new RectF(leftTop, leftTop, rigthBottom,
									rigthBottom + separateDistence);
							break;
						case TO_LEFT:
							// 向左移动
							oval = new RectF(leftTop - separateDistence,
									leftTop, rigthBottom, rigthBottom);
							break;
						default:
							break;
						}
					} else {
						oval = new RectF(leftTop, leftTop, rigthBottom,
								rigthBottom);

					}

					if (i > 0) {
						startAngle += itemsAngle[i - 1];
					}

					paint.setColor(Color.parseColor(itemsColors[i]));
					canvas.drawArc(oval, startAngle, itemsAngle[i], true, paint);
				}
			}

			canvas.restore();
			Paint p1 = new Paint();
			// p1.setARGB(130, 177, 177, 177);
			p1.setAntiAlias(true);
			// canvas.drawCircle(centerX , centerY,
			// radius/2+context.getResources().getDimension(R.dimen.statistics_circle_radius),
			// p1) ;
			p1.setColor(Color.parseColor("#F0EFF3"));

			canvas.drawCircle(centerX, centerY, radius / 1.6f, p1);
			// drawImg(canvas, centerX, centerY, radius/2);
			// drawText(canvas, centerX, centerY-radius/5, (int)radius/2);

		}

		// canvas.restore();
	}

	/**
	 * 获得当前分割显示的item的颜色
	 * 
	 * @return
	 */
	public String getShowItemColor() {
		return itemsColors[itemPostion];
	}

	public String getShowInfo() {
		return showInfo;
	}

	public void setShowInfo(String showInfo) {
		this.showInfo = showInfo;
	}

	/**
	 * 绘制圆内的文字
	 */
	private void drawText(Canvas canvas, float x, float y, int radius) {

		Log.i(TAG, "drawText");

		TextPaint textPaint = new TextPaint();
		textPaint.setColor(Color.BLACK);
		textPaint.setTextSize(context.getResources().getDimension(
				R.dimen.statistics_in_circle_txt));
		textPaint.setAntiAlias(true);

		// //每次增加的数值 如果想让数字跳动的时间长一点 就让分母变大
		// double step = mTarget / itemsSizes.length;
		//
		// if (mCount <= mTarget){
		// //只有小于目标数值 才刷新界面 无此判断会不断调用invalidate()产生死循环
		//
		// mCount+=step;
		// if (mCount >= mTarget){
		// //由于是+= 所以最后一次计算结果有可能大于mTarget 此处校验一下
		// mCount = mTarget;
		// }
		// }
		//
		// String str = new DecimalFormat("00.00").format(mCount);//保留2位小数
		showInfo = "本月消费金额一共 " + totalNum + " 元";

		StaticLayout layout = new StaticLayout(showInfo, textPaint, radius
				+ radius / 3, Alignment.ALIGN_NORMAL, 1.0F, 0.0F, true);
		canvas.translate(x - (radius / 1.5f), y - 30);
		layout.draw(canvas);
	}

	/**
	 * 在圆内绘制图片
	 */
	private void drawImg(Canvas canvas, float x, float y, float radius) {
		bitmap = ((BitmapDrawable) getResources().getDrawable(
				R.drawable.trigger)).getBitmap();
		triggerX = x - (bitmap.getWidth() / 2);
		triggerY = y + (radius / 2f);
		canvas.drawBitmap(bitmap, triggerX, triggerY, null);
	}

	// private float startDegree = 90;
	private float startDegree = 0;
	private float downX = 0f;
	private float moveX = 0f;

	/**
	 * 可以参考{@link View#onTouchEvent}
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {

		if (!isRotating && itemsSizes != null && itemsSizes.length > 0) {
			switch (event.getAction()) {
			case MotionEvent.ACTION_UP:
				isRotating = false;
				if (isDraging) {
					onStop();
				}
				break;
			default:
				break;
			}

			mGDetector.onTouchEvent(event);
		}

		return true;

		// mGestureDetector.onTouchEvent(event);
		// return true;

		// float x1 = 0;// 点的x坐标
		// float y1 = 0;// 点得y坐标
		//
		// Point eventPoint = getEventAbsoluteLocation(event);
		// computeCenter(); //计算中心坐标
		//
		// int newAngle = getEventAngle(eventPoint, center);
		//
		// int action = event.getAction();
		//
		//
		// float r = radius + strokeWidth;// 圆的半径
		//
		// switch (action) {
		// case MotionEvent.ACTION_DOWN:
		// x1 = event.getX();
		// y1 = event.getY();
		// lastEventPoint = eventPoint;
		//
		// //startAngle = getAngle(event.getX(), event.getY());
		// //position = getShowItem(getTouchedPointAngle(r, r, x1, y1));
		// // startAngle = startAngle + itemsBeginAngle[position] ;
		// break;
		// case MotionEvent.ACTION_MOVE:
		// isDraging = true;
		//
		// rotate(eventPoint,newAngle);
		//
		// //处理之后，记得更新lastEventPoint
		// lastEventPoint = eventPoint;
		// Log.i(TAG, "ACTION_MOVE==============newAngle:"+newAngle);
		// break;
		// case MotionEvent.ACTION_UP:
		// onStop();
		//
		// //isDraging = false;
		// break;
		// default:
		// break;
		// }
		//
		// return true;

		// if (!isRotating && itemsSizes != null && itemsSizes.length > 0) {//
		// 正在旋转，则不响应
		// float x1 = 0;// 点的x坐标
		// float y1 = 0;// 点得y坐标
		// switch (event.getAction()) {
		// case MotionEvent.ACTION_DOWN:
		// x1 = event.getX();
		// y1 = event.getY();
		// float r = radius + strokeWidth;// 圆的半径
		// if ((x1 - r) * (x1 - r) + (y1 - r) * (y1 - r) - r * r <= 0) {
		//
		// if (bitmap != null && triggerX <= x1
		// && x1 <= triggerX + bitmap.getWidth() && triggerY <= y1
		// && y1 <= triggerY + bitmap.getHeight()) {
		// notifyTriggerClickListeners();
		// }else if((getWidth()/2 - x1)*(getWidth()/2 - x1)+(getHeight()/2 -
		// y1)*(getHeight()/2 - y1) - (radius/2)*(radius/2)<=0){
		// //点击了圆内空白区域也触发
		// notifyTriggerClickListeners();
		// }else{
		// // 在点击了扇形
		// position = getShowItem(getTouchedPointAngle(r, r, x1, y1));
		// setShowItem(position, isAnimEnabled(), true);
		// }
		//
		// }
		// break;
		// case MotionEvent.ACTION_MOVE:
		//
		// break;
		// case MotionEvent.ACTION_UP:
		//
		// break;
		//
		// default:
		// break;
		// }
		// }
		//
		// return super.onTouchEvent(event);
	}

	/**
	 * onStop：拖动停止后触发 创建人：yanruyi 修改人：yanruyi void
	 * 
	 * @exception
	 * @since 1.0.0
	 */
	private void onStop() {
		// getTouchedPointAngle

		// int aa = getShowItem(180);
		// setShowItem(position, true, true);
		// Log.e(TAG, "当前aa："+aa );

		float targetAngle = getTargetDegree();
		position = getEventPart(targetAngle);

		float offset = getOffsetOfPartCenter(targetAngle, position);

		startDegree += offset;
		Log.e(TAG, "当前startDegree：" + startDegree);
		lastAng = getLastRotateAngle(position);
		resetBeginAngle(lastAng);

		notifySelectedListeners(position, itemsColors[position],
				itemsSizes[position], itemsRate[position],
				isPositionFree(position),
				getAnimTime(Math.abs(lastAng - rotateAng)));// 发出选中条目的消息

		invalidate();
	}

	protected float getOffsetOfPartCenter(float degree, int targetIndex) {
		float currentSum = 0;
		for (int i = 0; i <= targetIndex; i++) {
			currentSum += itemsAngle[i];
		}

		float offset = degree - (currentSum - itemsAngle[targetIndex] / 2);

		// 超过一半,则offset>0；未超过一半,则offset<0
		return offset;
	}

	protected int getEventPart(float degree) {
		float currentSum = 0;

		for (int i = 0; i < itemsAngle.length; i++) {
			currentSum += itemsAngle[i];
			if (currentSum >= degree) {
				return i;
			}
		}

		return -1;
	}

	// 处理拖动
	private void rotate(Point eventPoint, int newDegree) {
		// 计算上一个位置相对于x轴正方向的角度
		int lastDegree = getEventAngle(lastEventPoint, center);

		startDegree += newDegree - lastDegree;

		Log.i(TAG, "startDegree:" + startDegree);

		// 转多圈的时候，限定startAngle始终在-360-360度之间
		if (startDegree >= 360) {
			startDegree -= 360;
		} else if (startDegree <= -360) {
			startDegree += 360;
		}

		// refreshItemsAngs();
		// test();

		float targetDegree = getTargetDegree();
		currentTargetIndex = getEventPart(targetDegree);
		// position = currentTargetIndex;

		// int aa = getShowItem(270);

		// position = aa;

		if (null != pieChartSlideLinstener) {
			pieChartSlideLinstener.OnPieChartSlide();
		}

		Log.e(TAG, "当前startAngle：" + startDegree + "  currentTargetIndex:"
				+ currentTargetIndex);

		postInvalidate();
	}

	/**
	 * 获取当前下方箭头位置相对于startDegree的角度值 注意，下方箭头相对于x轴正方向是90度
	 * 
	 * @return
	 */
	protected float getTargetDegree() {

		float targetDegree = -1;

		float tmpStart = startDegree;

		/**
		 * 如果当前startAngle为负数，则直接+360，转换为正值
		 */
		if (tmpStart < 0) {
			tmpStart += 360;
		}

		if (tmpStart < 90) {
			/**
			 * 如果startAngle小于90度（可能为负数）
			 */
			targetDegree = 90 - tmpStart;
		} else {
			/**
			 * 如果startAngle大于90，由于在每次计算startAngle的时候，限定了其最大为360度，所以 直接可以按照如下公式计算
			 */
			targetDegree = 360 + 90 - tmpStart;
		}

		// Log.e(TAG, "Taget Angle:"+targetDegree+"startAngle:"+startAngle);

		return targetDegree;
	}

	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		rotateHandler.removeCallbacks(this);
		// Log.e(TAG, "onDetachedFromWindow");
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		// set the size of the view
		// setMeasuredDimension((int) (bitmap.getWidth() * scale), (int)
		// (bitmap.getHeight() * scale));

		float widthHeight = (float) (2 * (radius + strokeWidth + separateDistence));
		// setMeasuredDimension(getWidth() , getHeight() );
		setMeasuredDimension((int) widthHeight, (int) widthHeight);
		// Log.e("setMeasuredDimension", "widthHeight = " + widthHeight);
		// Log.e("setMeasuredDimension", "radius = " + radius);
		// Log.e("setMeasuredDimension", "separateDistence = " +
		// separateDistence);
		// Log.e("setMeasuredDimension", "strokeWidth = " + strokeWidth);
	}

	/**
	 * 可以参考{@link Runnable#run()}
	 */
	public void run() {

		// 是否是顺时针旋转
		if (bClockWise) {
			rotateAng += rotateSpeed;// 每次旋转的速度
			invalidate();
			rotateHandler.postDelayed(this, TIME_HANDLER_DELY);
			// 如果旋转的角度与需要旋转的角度 重叠时则停止旋转
			if (rotateAng - lastAng >= 0) {
				rotateAng = 0;
				rotateHandler.removeCallbacks(this);
				resetBeginAngle(lastAng);
				// invalidate();
				isRotating = false;
			}
		} else {
			rotateAng -= rotateSpeed;
			invalidate();
			rotateHandler.postDelayed(this, TIME_HANDLER_DELY);
			// 如果旋转的角度与需要旋转的角度 重叠时则停止旋转
			if (rotateAng - lastAng <= 0) {
				rotateAng = 0;
				rotateHandler.removeCallbacks(this);
				resetBeginAngle(lastAng);
				// invalidate();
				isRotating = false;
			}
		}

	}

	/**
	 * 根据每个item的大小，获得item所占的角度和起始角度
	 */
	private void refreshItemsAngs() {
		if (itemSizesTemp != null && itemSizesTemp.length > 0) {
			// 如果存在多余无用的数据，则添加
			if (getTotal() > getAllSizes()) {
				itemsSizes = new float[itemSizesTemp.length + 1];
				for (int m = 0; m < itemSizesTemp.length; m++) {
					itemsSizes[m] = itemSizesTemp[m];
				}
				itemsSizes[itemsSizes.length - 1] = getTotal() - getAllSizes();
			} else {
				itemsSizes = new float[itemSizesTemp.length];
				itemsSizes = itemSizesTemp;
			}

			itemsRate = new float[itemsSizes.length];// 每一项所占的比例
			itemsBeginAngle = new float[itemsSizes.length];// 每一个角度临界点
			itemsAngle = new float[itemsSizes.length];// 每一个角度临界点
			float beginAngle = 0;

			for (int i = 0; i < itemsSizes.length; i++) {
				itemsRate[i] = (float) (itemsSizes[i] * 1.0 / getTotal() * 1.0);
			}

			for (int i = 0; i < itemsRate.length; i++) {
				if (i == 1) {
					beginAngle = 360 * itemsRate[i - 1];
				} else if (i > 1) {
					beginAngle = 360 * itemsRate[i - 1] + beginAngle;
				}
				itemsBeginAngle[i] = beginAngle;
				itemsAngle[i] = 360 * itemsRate[i];
				Log.e(TAG, "itemsBeginAngle=" + beginAngle + "   itemsAngle="
						+ itemsAngle[i]);
			}
		}

	}

	/**
	 * 判断最后一个item是否是free的
	 * 
	 * @param position
	 * @return
	 */
	private boolean isPositionFree(int position) {
		if (position == itemsSizes.length - 1 && getTotal() > getAllSizes()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 获得旋转动画的时间
	 * 
	 * @return
	 */
	private float getAnimTime(float ang) {
		return (int) Math.floor((ang / getRotateSpeed()) * TIME_HANDLER_DELY);
	}

	/**
	 * 计算(x1, y1) 到(x, y)的直线距离水平线的角度
	 * 
	 * @param x
	 * @param y
	 * @param x1
	 * @param y1
	 * @return
	 */
	private float getTouchedPointAngle(float x, float y, float x1, float y1) {

		float ax = x1 - x;
		float ay = y1 - y;

		// ax = ax;
		ay = -ay;
		double a = 0;
		double t = ay / Math.sqrt((double) (ax * ax + ay * ay));
		// Log.e(TAG, "ax=" + ax + "   ay=" + ay);
		if (ax > 0) {
			if (ay > 0)
				a = Math.asin(t);
			else
				a = 2 * Math.PI + Math.asin(t);
		} else {
			if (ay > 0)
				a = Math.PI - Math.asin(t);
			else
				a = Math.PI - Math.asin(t);
		}
		return (float) (360 - (a * 180 / (Math.PI)) % (360));
	}

	/**
	 * 判断点击的是哪个部分，并计算出需要旋转的角度
	 * 
	 * @param ang
	 *            :触摸的位置所在圆内的角度
	 * @return
	 */
	private int getShowItem(float ang) {
		int position = 0;
		// Log.v(TAG, "触摸点的角度：" + ang);

		for (int i = 0; i < itemsBeginAngle.length; i++) {
			// Log.v(TAG, "*****itemsBeginAngle=" + itemsBeginAngle[i]);
			if (i != itemsBeginAngle.length - 1) {
				// 如：15、45、75、105、135、165
				if (ang >= itemsBeginAngle[i] && ang < itemsBeginAngle[i + 1]) {
					position = i;
					break;
				}
			} else {
				// 包括大于所有或小于所有; 或最后
				if (ang > itemsBeginAngle[itemsBeginAngle.length - 1]
						&& ang < itemsBeginAngle[0]) {
					// 最后一块
					position = itemsSizes.length - 1;
				} else if (isUpperSort(itemsBeginAngle)
						|| isLowerSort(itemsBeginAngle)) {
					// 最后一块
					position = itemsSizes.length - 1;
				} else {
					// 小于所有，或大于所有,且顺序不递增或递减
					position = getPointItem(itemsBeginAngle);
				}

			}
		}

		return position;
	}

	/**
	 * 根据要显示的item，获得要旋转的角度
	 * 
	 * @param position
	 * @return
	 */
	private float getLastRotateAngle(int position) {

		float result = 0;

		// Log.v(TAG, "要显示的item：" + position);

		result = itemsBeginAngle[position];
		// Log.e(TAG, "maxAng=" + result);
		// Log.e(TAG, "ItemAng=" + itemsAngle[position]);
		result = itemsBeginAngle[position] + (itemsAngle[position]) / 2
				+ getRotateWhereAngle();
		if (result >= 360) {
			result -= 360;
		}
		// Log.v(TAG, "getLastRotateAngle=" + result);
		// 区分顺时针还是逆时针旋转
		if (result <= 180) {
			result = -result;
		} else {
			result = 360 - result;
		}

		Log.e(TAG, "需旋转的角度=" + result);
		return result;
	}

	/**
	 * 判断a是否大于all中的所有数字
	 * 
	 * @param a
	 * @param all
	 * @return
	 */
	private boolean isUpperSort(float[] all) {
		boolean result = true;
		float temp = all[0];
		for (int a = 0; a < all.length - 1; a++) {
			if ((all[a + 1] - temp) > 0) {
				temp = all[a + 1];
			} else {
				return false;
			}
		}

		return result;
	}

	/**
	 * 判断a是否大于all中的所有数字
	 * 
	 * @param a
	 * @param all
	 * @return
	 */
	private boolean isLowerSort(float[] all) {
		boolean result = true;
		float temp = all[0];
		for (int a = 0; a < all.length - 1; a++) {
			if ((all[a + 1] - temp) < 0) {
				temp = all[a + 1];
			} else {
				return false;
			}
		}

		return result;
	}

	/**
	 * 获得数组中后一个小于前一个数的item
	 * 
	 * @return
	 */
	private int getPointItem(float[] all) {
		int item = 0;

		float temp = all[0];
		for (int a = 0; a < all.length - 1; a++) {
			if ((all[a + 1] - temp) > 0) {
				temp = all[a];
			} else {
				return a;
			}
		}

		return item;
	}

	/**
	 * 重置各个起始边的角度
	 * 
	 * @param angle
	 */
	private void resetBeginAngle(float angle) {
		for (int i = 0; i < itemsBeginAngle.length; i++) {
			float newBeginAngle = itemsBeginAngle[i] + angle;

			if (newBeginAngle < 0) {
				itemsBeginAngle[i] = newBeginAngle + 360;
			} else if (newBeginAngle > 360) {
				itemsBeginAngle[i] = newBeginAngle - 360;
			} else {
				itemsBeginAngle[i] = newBeginAngle;
			}

			Log.v(TAG, "itemsBeginAngle  " + i + "=" + itemsBeginAngle[i]);
		}
	}

	/**
	 * 若未设置items的颜色，则添加默认颜色
	 */
	private void setDefaultColor() {

		// 若未设置颜色，则设置默认颜色
		if (itemsSizes != null && itemsSizes.length > 0 && itemsColors == null) {
			// Log.e(TAG, "setDefaultColor");
			itemsColors = new String[itemsSizes.length];
			if (itemsColors.length <= DEFAULT_ITEMS_COLORS.length) {
				System.arraycopy(DEFAULT_ITEMS_COLORS, 0, itemsColors, 0,
						itemsColors.length);
			} else {
				int multiple = itemsColors.length / DEFAULT_ITEMS_COLORS.length;
				int left = itemsColors.length % DEFAULT_ITEMS_COLORS.length;

				for (int a = 0; a < multiple; a++) {
					System.arraycopy(DEFAULT_ITEMS_COLORS, 0, itemsColors, a
							* DEFAULT_ITEMS_COLORS.length,
							DEFAULT_ITEMS_COLORS.length);
				}
				if (left > 0) {
					System.arraycopy(DEFAULT_ITEMS_COLORS, 0, itemsColors,
							multiple * DEFAULT_ITEMS_COLORS.length, left);
				}
			}
			// Log.e(TAG, "itemsColors = " + itemsColors.length);
			// for (String a : itemsColors) {
			// Log.v(TAG, "itemsColors:" + a);
			// }
		}

	}

	/**
	 * 若设置的颜色数目小于 items 的数目，则补充默认颜色
	 */
	private void setLeftColor() {

		if (itemsSizes != null && itemsSizes.length > itemsColors.length) {
			String[] preItemsColors = new String[itemsColors.length];
			preItemsColors = itemsColors;
			int leftall = itemsSizes.length - itemsColors.length;
			itemsColors = new String[itemsSizes.length];
			System.arraycopy(preItemsColors, 0, itemsColors, 0,
					preItemsColors.length);// 先把设定的颜色加上

			// 再把不够的颜色用默认颜色
			if (leftall <= DEFAULT_ITEMS_COLORS.length) {
				System.arraycopy(DEFAULT_ITEMS_COLORS, 0, itemsColors,
						preItemsColors.length, leftall);
			} else {
				int multiple = leftall / DEFAULT_ITEMS_COLORS.length;
				int left = leftall % DEFAULT_ITEMS_COLORS.length;
				for (int a = 0; a < multiple; a++) {
					System.arraycopy(DEFAULT_ITEMS_COLORS, 0, itemsColors, a
							* DEFAULT_ITEMS_COLORS.length,
							DEFAULT_ITEMS_COLORS.length);
				}
				if (left > 0) {
					System.arraycopy(DEFAULT_ITEMS_COLORS, 0, itemsColors,
							multiple * DEFAULT_ITEMS_COLORS.length, left);
				}
			}
			preItemsColors = null;

		}
		// for (String a : itemsColors) {
		// Log.v(TAG, "itemsColors:" + a);
		// }

	}

	/**
	 * 饼状图所表示的整体大小；大于或等于 itemSizes 各项数据之和，若小于，则等于，默认等于
	 */
	private void reSetTotal() {
		// 不允许all小于总和
		float totalSizes = getAllSizes();
		if (getTotal() < totalSizes) {
			this.total = totalSizes;
		}
	}

	/**
	 * 获得所有item的总和
	 * 
	 * @return
	 */
	private float getAllSizes() {
		float tempAll = 0;
		if (itemSizesTemp != null && itemSizesTemp.length > 0) {
			for (float itemsize : itemSizesTemp) {
				tempAll += itemsize;
			}
		}

		return tempAll;
	}

	private float getRotateWhereAngle() {

		float result = 0;
		switch (rotateWhere) {
		case TO_RIGHT:
			result = 0;
			break;
		case TO_LEFT:
			result = 180;
			break;
		case TO_TOP:
			result = 90;
			break;
		case TO_BOTTOM:
			result = 270;
			break;

		default:
			break;
		}
		return result;
	}

	private List<OnPieChartItemSelectedLinstener> itemSelectedListeners = new LinkedList<OnPieChartItemSelectedLinstener>();
	private OnPieChartSlideLinstener pieChartSlideLinstener;

	public OnPieChartSlideLinstener getPieChartSlideLinstener() {
		return pieChartSlideLinstener;
	}

	public void setPieChartSlideLinstener(
			OnPieChartSlideLinstener pieChartSlideLinstener) {
		this.pieChartSlideLinstener = pieChartSlideLinstener;
	}

	/**
	 * 添加选中饼状图上某块的消息监听
	 * 
	 * @param listener
	 *            监听器；参见：{@link OnPieChartItemSelectedLinstener}
	 */
	public void setOnItemSelectedListener(
			OnPieChartItemSelectedLinstener listener) {
		itemSelectedListeners.add(listener);
	}

	/**
	 * 移除选中饼状图上某块的消息监听
	 * 
	 * @param listener
	 *            监听器；参见：{@link OnPieChartItemSelectedLinstener}
	 */
	public void removeItemSelectedListener(
			OnPieChartItemSelectedLinstener listener) {
		itemSelectedListeners.remove(listener);
	}

	/**
	 * Notifies selected listeners
	 * 
	 * @param position
	 */
	protected void notifySelectedListeners(int position, String colorRgb,
			float size, float rate, boolean isFreePart, float animTime) {
		for (OnPieChartItemSelectedLinstener listener : itemSelectedListeners) {
			listener.onPieChartItemSelected(this, position, colorRgb, size,
					rate, isFreePart, animTime);
		}
	}

	protected void notifyTriggerClickListeners() {
		for (OnPieChartItemSelectedLinstener listener : itemSelectedListeners) {
			listener.onTriggerClicked();
		}
	}

	/**
	 * 获取当前饼图的中心坐标，相对于屏幕左上角
	 */
	protected void computeCenter() {
		if (center == null) {
			int x = (int) oval.left + (int) ((oval.right - oval.left) / 2f);
			int y = (int) oval.top + (int) ((oval.bottom - oval.top) / 2f)
					+ extraHeight; // 状态栏的高度是50

			// int[] location = new int[2];
			// this.getLocationOnScreen(location);
			// x = location[0];
			// y = location[1];

			center = new Point(x, y);
			Log.v(TAG, "中心坐标：" + center.toString());
		}
	}

	protected int getEventAngle(Point eventPoint, Point center) {
		int x = eventPoint.x - center.x;// x轴方向的偏移量
		int y = eventPoint.y - center.y; // y轴方向的偏移量

		// Log.v(TAG, "直角三角形两直边长度："+x+","+y);

		double z = Math.hypot(Math.abs(x), Math.abs(y)); // 求直角三角形斜边的长度

		// Log.v(TAG, "斜边长度："+z);

		eventRadius = (int) z;
		double sinA = (double) Math.abs(y) / z;

		// Log.v(TAG, "sinA="+sinA);

		double asin = Math.asin(sinA); // 求反正玄，得到当前点和x轴的角度,是最小的那个

		// Log.v(TAG, "当前相对偏移角度的反正弦："+asin);

		int degree = (int) (asin / 3.14f * 180f);

		// Log.v(TAG, "当前相对偏移角度："+angle);

		// 下面就需要根据x,y的正负，来判断当前点和x轴的正方向的夹角
		int realDegree = 0;
		if (x <= 0 && y <= 0) {
			// 左上方，返回180+angle

			realDegree = 180 + degree;

		} else if (x >= 0 && y <= 0) {
			// 右上方，返回360-angle
			realDegree = 360 - degree;
		} else if (x <= 0 && y >= 0) {
			// 左下方，返回180-angle
			realDegree = 180 - degree;
		} else {
			// 右下方,直接返回
			realDegree = degree;

		}

		// Log.v(TAG, "当前事件相对于中心坐标x轴正方形的顺时针偏移角度为："+realAngle);

		return realDegree;

	}

	/**
	 * 获取当前事件event相对于屏幕的坐标
	 * 
	 * @param event
	 * @return
	 */
	protected Point getEventAbsoluteLocation(MotionEvent event) {
		int[] location = new int[2];
		this.getLocationOnScreen(location); // 当前控件在屏幕上的位置

		int x = (int) event.getX();
		int y = (int) event.getY();

		x += location[0];
		y += location[1]; // 这样x,y就代表当前事件相对于整个屏幕的坐标

		Point p = new Point(x, y);

		Log.v(TAG, "事件坐标：" + p.toString());

		return p;
	}

	@Override
	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		Log.i(TAG, "onDown");

		Point eventPoint = getEventAbsoluteLocation(e);
		computeCenter(); // 计算中心坐标

		isDraging = false;
		lastEventPoint = eventPoint;

		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub
		Point eventPoint = getEventAbsoluteLocation(e);
		lastEventPoint = eventPoint;

		Log.i(TAG, "onShowPress");
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		Log.i(TAG, "onSingleTapUp");

		float x1 = e.getX();
		float y1 = e.getY();

		float clickPoint = (getWidth() / 2 - x1) * (getWidth() / 2 - x1)
				+ (getHeight() / 2 - y1) * (getHeight() / 2 - y1)
				- (radius / 1.6f) * (radius / 1.6f);
		float r = radius + strokeWidth;// 圆的半径

		if (clickPoint <= 0) {
			// 点击圆内
			Log.i(TAG, "点击圆内");
		} else {
			x1 = e.getX();
			y1 = e.getY();
			position = getShowItem(getTouchedPointAngle(r, r, x1, y1));
			setShowItem(position, isAnimEnabled(), true);
			startDegree += lastAng;
		}

		isDraging = false;

		return false;
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// TODO Auto-generated method stub
		Log.i(TAG, "onScroll");

		isDraging = true;

		Point eventPoint = getEventAbsoluteLocation(e2);
		computeCenter(); // 计算中心坐标
		int newAngle = getEventAngle(eventPoint, center);

		position = -1;
		rotate(eventPoint, newAngle);
		lastEventPoint = eventPoint;

		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub
		Log.i(TAG, "onLongPress");
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		// TODO Auto-generated method stub
		Log.i(TAG, "onFling");

		return false;
	}

}
