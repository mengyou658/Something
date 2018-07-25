package com.moons.xst.track.ui;

import android.util.FloatMath;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.TextView;

/**
 * 文本框放大缩小
 * 
 * @author LKZ
 * 
 */
public class TextZoomListenter implements OnTouchListener {

	private int mode = 0;
	float oldDist;
	float textSize = 0;

	private static final float ZOOM_IN_STEP = 0.8f;//
	private static final float ZOOM_OUT_STEP = 1.25f;//

	private float minFontSize;//
	private float maxFontSize;//

	TextView textView = null;

	public TextZoomListenter(float minSize, float maxSize) {
		minFontSize = minSize;
		maxFontSize = maxSize;
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		textView = (TextView) v;
		if (textSize == 0) {
			textSize = textView.getTextSize();
		}
		switch (event.getAction() & MotionEvent.ACTION_MASK) {
		case MotionEvent.ACTION_DOWN:
			mode = 1;
			break;
		case MotionEvent.ACTION_UP:
			mode = 0;
			break;
		case MotionEvent.ACTION_POINTER_UP:
			mode -= 1;
			break;
		case MotionEvent.ACTION_POINTER_DOWN:
			oldDist = spacing(event);
			mode += 1;
			break;

		case MotionEvent.ACTION_MOVE:
			if (mode >= 2) {
				float newDist = spacing(event);
				if (newDist > oldDist + 10) {
					// zoom(newDist / oldDist);
					zoomOut();//
					oldDist = newDist;
				}
				if (newDist < oldDist - 10) {
					// zoom(newDist / oldDist);
					zoomIn();
					oldDist = newDist;
				}
			}
			break;
		}
		return true;
	}

	/*
	 * private void zoom(float f) {
	 * 
	 * float thisScale = f; if(f >= MAX_SCALE){ thisScale = MAX_SCALE; } if(f <=
	 * MIN_SCALE){ thisScale = MIN_SCALE; }
	 * Toast.makeText(textView.getContext(), String.valueOf(thisScale),
	 * 500).show();
	 * 
	 * textView.setTextSize(textSize *= thisScale); }
	 */

	protected void zoomOut() {
		textSize *= ZOOM_OUT_STEP;
		if (textSize > maxFontSize) {
			textSize = maxFontSize;
		}
		textView.setTextSize(textSize);
	}

	/**
	 * ��С
	 */
	protected void zoomIn() {
		textSize *= ZOOM_IN_STEP;
		if (textSize < minFontSize) {
			textSize = minFontSize;
		}
		textView.setTextSize(textSize);
	}

	private float spacing(MotionEvent event) {
		float x = event.getX(0) - event.getX(1);
		float y = event.getY(0) - event.getY(1);
		return FloatMath.sqrt(x * x + y * y);
	}

}
