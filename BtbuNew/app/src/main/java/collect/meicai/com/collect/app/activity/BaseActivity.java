package collect.meicai.com.collect.app.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import collect.meicai.com.collect.app.listener.BackGestureListener;

/**
 * Coder: 何毅
 * Desc : 所有Activity 基类
 * Date : 2015-04-04
 * Time : 13:34
 * Version:1.0
 */
public class BaseActivity extends Activity{



	/** 手势监听 */
	GestureDetector mGestureDetector;

	/** 是否需要监听手势关闭功能 */
	private boolean mNeedBackGesture = false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initGestureDetector();
	}

	private void initGestureDetector() {
		if (mGestureDetector == null) {
			mGestureDetector = new GestureDetector(getApplicationContext(),
					new BackGestureListener(this));
		}
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		if(mNeedBackGesture){
			return mGestureDetector.onTouchEvent(ev) || super.dispatchTouchEvent(ev);
		}
		return super.dispatchTouchEvent(ev);
	}

	/**
	 * 设置是否进行手势监听
	 */
	public void setNeedBackGesture(boolean mNeedBackGesture){
		this.mNeedBackGesture = mNeedBackGesture;
	}
	/**
	 * 返回
	 */
	public void doBack(View view) {
		onBackPressed();
	}

}
