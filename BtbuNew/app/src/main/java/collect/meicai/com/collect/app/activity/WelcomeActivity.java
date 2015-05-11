package collect.meicai.com.collect.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

import com.umeng.analytics.MobclickAgent;

import collect.meicai.com.collect.R;

/**
 * Coder: 何毅
 * Desc : 欢迎界面的Activity
 * Date : 2015-04-04
 * Time : 13:38
 * Version:1.0
 */
public class WelcomeActivity extends  BaseActivity {

	private AlphaAnimation start_anima;
	View view;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		view = View.inflate(this, R.layout.welcome, null);
		setContentView(view);
		initView();
		initData();
	}

	public void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}
	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}

	private void initData() {
		start_anima = new AlphaAnimation(0.3f, 1.0f);
		start_anima.setDuration(2000);
		view.startAnimation(start_anima);
		start_anima.setAnimationListener(new Animation.AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {

			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				redirectTo();
			}
		});
	}

	private void initView() {

	}

	private void redirectTo() {
		startActivity(new Intent(getApplicationContext(), MainActivity.class));
		finish();
	}

}
