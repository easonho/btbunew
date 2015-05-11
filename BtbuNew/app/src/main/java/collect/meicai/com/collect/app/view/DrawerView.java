package collect.meicai.com.collect.app.view;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.OnClosedListener;

import collect.meicai.com.collect.R;
import collect.meicai.com.collect.app.activity.SettingsActivity;
import collect.meicai.com.collect.app.utils.ToastUtil;

/**
 * 自定义SlidingMenu 测拉菜单类
 * */
public class DrawerView implements OnClickListener{
	private final Activity activity;
	private SlidingMenu localSlidingMenu;
	private SwitchButton night_mode_btn;
	private TextView night_mode_text;
	/**学院的点击事件信息*/
	private RelativeLayout jixin_rl_btn,jingji_rl_btn,shang_rl_btn,
			li_rl_btn,shipin_rl_btn,caiji_rl_btn,fa_rl_btn,feedback_rl_btn,
			setting_btn;
	public DrawerView(Activity activity) {
		this.activity = activity;
	}

	public SlidingMenu initSlidingMenu() {
		localSlidingMenu = new SlidingMenu(activity);
		/**设置左右滑菜单*/
		localSlidingMenu.setMode(SlidingMenu.LEFT_RIGHT);
		/**设置要使菜单滑动，触碰屏幕的范围*/
		localSlidingMenu.setTouchModeAbove(SlidingMenu.SLIDING_WINDOW);
		/**设置阴影图片的宽度*/
		localSlidingMenu.setShadowWidthRes(R.dimen.shadow_width);
		/**设置阴影图片*/
		localSlidingMenu.setShadowDrawable(R.drawable.shadow);
		/**SlidingMenu划出时主页面显示的剩余宽度*/
		localSlidingMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		/**SlidingMenu滑动时的渐变程度*/
		localSlidingMenu.setFadeDegree(0.35F);
		/**使SlidingMenu附加在Activity右边*/
		localSlidingMenu.attachToActivity(activity, SlidingMenu.RIGHT);
		/**设置menu的布局文件*/
		localSlidingMenu.setMenu(R.layout.left_drawer_fragment);
		/**动态判断自动关闭或开启SlidingMenu*/
//		localSlidingMenu.toggle();
		localSlidingMenu.setSecondaryMenu(R.layout.profile_drawer_right);
		localSlidingMenu.setSecondaryShadowDrawable(R.drawable.shadowright);
		localSlidingMenu.setOnOpenedListener(new SlidingMenu.OnOpenedListener() {
			public void onOpened() {

			}
		});
		localSlidingMenu.setOnClosedListener(new OnClosedListener() {

			@Override
			public void onClosed() {

			}
		});
		initView();
		return localSlidingMenu;
	}

	private void initView() {

		jixin_rl_btn  = (RelativeLayout)localSlidingMenu.findViewById(R.id.jixin_rl_btn);
		jingji_rl_btn = (RelativeLayout)localSlidingMenu.findViewById(R.id.jingji_rl_btn);
		shang_rl_btn = (RelativeLayout)localSlidingMenu.findViewById(R.id.shang_rl_btn);
		li_rl_btn = (RelativeLayout)localSlidingMenu.findViewById(R.id.li_rl_btn);
		shipin_rl_btn = (RelativeLayout)localSlidingMenu.findViewById(R.id.shipin_rl_btn);
		caiji_rl_btn = (RelativeLayout)localSlidingMenu.findViewById(R.id.caiji_rl_btn);
		fa_rl_btn = (RelativeLayout)localSlidingMenu.findViewById(R.id.fa_rl_btn);
		feedback_rl_btn = (RelativeLayout)localSlidingMenu.findViewById(R.id.feedback_rl_btn);
		setting_btn =(RelativeLayout)localSlidingMenu.findViewById(R.id.setting_rl_btn);

		li_rl_btn.setOnClickListener(this);
		shipin_rl_btn.setOnClickListener(this);
		fa_rl_btn.setOnClickListener(this);
		caiji_rl_btn.setOnClickListener(this);
		shang_rl_btn.setOnClickListener(this);
		jingji_rl_btn.setOnClickListener(this);
		jixin_rl_btn.setOnClickListener(this);
		feedback_rl_btn.setOnClickListener(this);
		setting_btn.setOnClickListener(this);
		/*night_mode_btn = (SwitchButton)localSlidingMenu.findViewById(R.id.night_mode_btn);
		night_mode_text = (TextView)localSlidingMenu.findViewById(R.id.night_mode_text);
		night_mode_btn.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked){
					night_mode_text.setText(activity.getResources().getString(R.string.action_night_mode));
				}else{
					night_mode_text.setText(activity.getResources().getString(R.string.action_day_mode));
				}
			}
		});
		night_mode_btn.setChecked(false);
		if(night_mode_btn.isChecked()){
			night_mode_text.setText(activity.getResources().getString(R.string.action_night_mode));
		}else{
			night_mode_text.setText(activity.getResources().getString(R.string.action_day_mode));
		}
*/

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.setting_rl_btn:
				activity.startActivity(new Intent(activity,SettingsActivity.class));
				activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
				break;

			case R.id.jixin_rl_btn: /**计信学院*/
				//activity.startActivity(new Intent(activity,ComputerInformationEngineerActivity.class));
				//activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
				break;
			default:
				ToastUtil.show(activity,"未开发");
				break;
		}
	}
}
