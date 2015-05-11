package com.meicai.util.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.AdapterView;
import android.widget.ListView;

import com.meicai.util.R;

/**
 * Created by meicai on 2015/2/5.
 */
public class CornerListView extends ListView {
	public CornerListView(Context context) {
		super(context);
	}

	public CornerListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public CornerListView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
			case MotionEvent.ACTION_DOWN:
				int x = (int) ev.getX();
				int y = (int) ev.getY();
				int itemNum = pointToPosition(x, y);
				if (itemNum == AdapterView.INVALID_POSITION) {
					break;
				} else {
					if (itemNum == 0) {
						if (itemNum == (getAdapter().getCount() - 1)) {
							setSelector(R.drawable.app_list_corner_round);
						} else {
							setSelector(R.drawable.app_list_corner_round_top);
						}
					} else if (itemNum == (getAdapter().getCount() - 1))
						setSelector(R.drawable.app_list_corner_round_bottom);
					else {
//						setSelector(R.drawable.app_list_corner_shape);
					}
				}
				break;
		}
		return super.onInterceptTouchEvent(ev);
	}
}
