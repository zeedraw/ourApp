package com.example.administrator.ourapp.ViewPaperCycle;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.AccelerateInterpolator;

import java.lang.reflect.Field;

/**
 * 自定义高度的viewpapger
 */
public class BaseViewPager extends ViewPager {
	private int speed = 500;// 毫秒
	private boolean scrollable = true;

	public BaseViewPager(Context context) {
		super(context);
		init();
	}

	public BaseViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	/**
	 * 设置viewpager是否可以滚动
	 * 
	 * @param enable
	 */
	public void setScrollable(boolean enable) {
		scrollable = enable;
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent event) {
		if (scrollable) {
			return super.onInterceptTouchEvent(event);
		} else {
			return false;
		}
	}

	public void init() {
		controlViewPagerSpeed();
	}

	// 设置滚动速度
	public void setSpeed(int speed) {
		this.speed = speed;
	}

	FixedSpeedScroller mScroller = null;

	private void controlViewPagerSpeed() {
		try {
			Field mField;

			mField = ViewPager.class.getDeclaredField("mScroller");
			mField.setAccessible(true);

			mScroller = new FixedSpeedScroller(getContext(), new AccelerateInterpolator());
			mScroller.setmDuration(speed); // 2000ms
			mField.set(this, mScroller);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}