package collect.meicai.com.collect.app.listener;

import android.view.GestureDetector;
import android.view.MotionEvent;

import collect.meicai.com.collect.app.activity.BaseActivity;

/**
 * Coder: 何毅
 * Desc :放回手势监听接口
 * Date : 2015-04-04
 * Time : 13:36
 * Version:1.0
 */
public class BackGestureListener    implements GestureDetector.OnGestureListener {


	BaseActivity activity;

	public BackGestureListener(BaseActivity activity) {
		this.activity = activity;
	}

	@Override
	public boolean onDown(MotionEvent e) {
		return false;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
						   float velocityY) {
		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {

	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
							float distanceY) {
		if ((e2.getX() - e1.getX()) >100 && Math.abs(e1.getY() - e2.getY()) < 60 ) {
			activity.onBackPressed();
			return true;
		}
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {

	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		return false;
	}


}
