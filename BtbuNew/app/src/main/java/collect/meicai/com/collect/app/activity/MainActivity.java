package collect.meicai.com.collect.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.meicai.util.util;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import collect.meicai.com.collect.R;
import collect.meicai.com.collect.app.M_Application;
import collect.meicai.com.collect.app.adapter.NewsFragmentPagerAdapter;
import collect.meicai.com.collect.app.bean.ChannelItem;
import collect.meicai.com.collect.app.bean.ChannelManage;
import collect.meicai.com.collect.app.fregment.NewsFragment;
import collect.meicai.com.collect.app.net.NewApi;
import collect.meicai.com.collect.app.utils.BaseTools;
import collect.meicai.com.collect.app.utils.Constants;
import collect.meicai.com.collect.app.utils.ListUtil;
import collect.meicai.com.collect.app.view.ColumnHorizontalScrollView;
import collect.meicai.com.collect.app.view.DrawerView;


/**
 * 主Activity
 */
public class MainActivity extends FragmentActivity {

	/** 自定义HorizontalScrollView */
	private ColumnHorizontalScrollView mColumnHorizontalScrollView;
	private LinearLayout mRadioGroup_content;
	private LinearLayout ll_more_columns;
	private RelativeLayout rl_column;
	private ViewPager mViewPager;
	private ImageView button_more_columns;


	/** 用户选择的新闻分类列表*/
	private List<ChannelItem> userChannelList=new ArrayList<ChannelItem>();
	/** 当前选中的栏目*/
	private int columnSelectIndex = 0;
	/** 左阴影部分*/
	public ImageView shade_left;
	/** 右阴影部分 */
	public ImageView shade_right;
	/** 屏幕宽度 */
	private int mScreenWidth = 0;
	/** Item宽度 */
	private int mItemWidth = 0;
	private ArrayList<Fragment> fragments = new ArrayList<Fragment>();

	protected SlidingMenu side_drawer;

	/** head 头部 的中间的loading*/
	private ProgressBar top_progress;
	/** head 头部 中间的刷新按钮*/
	private ImageView top_refresh;
	/** head 头部 的左侧菜单 按钮*/
	private ImageView top_head;
	/** head 头部 的右侧菜单 按钮*/
	private ImageView top_more;
	/** 请求CODE */
	public final static int CHANNELREQUEST = 1;
	/** 调整返回的RESULTCODE */
	public final static int CHANNELRESULT = 10;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		util.HttpTest(this);
		M_Application.getInstance().addActivity(this);
		setContentView(R.layout.activity_main);
		mScreenWidth = BaseTools.getWindowsWidth(this);
		mItemWidth = mScreenWidth / 7;// 一个Item宽度为屏幕的1/7
		initView();
		initSlidingMenu();
	}

	public void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}
	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}


	/**
	 * 初始化View
	 */
	protected  void initView(){
		mColumnHorizontalScrollView =  (ColumnHorizontalScrollView)findViewById(R.id.mColumnHorizontalScrollView);
		mRadioGroup_content = (LinearLayout) findViewById(R.id.mRadioGroup_content);
		ll_more_columns = (LinearLayout) findViewById(R.id.ll_more_columns);
		rl_column = (RelativeLayout) findViewById(R.id.rl_column);
		button_more_columns = (ImageView) findViewById(R.id.button_more_columns);
		mViewPager = (ViewPager) findViewById(R.id.mViewPager);
		shade_left = (ImageView) findViewById(R.id.shade_left);
		shade_right = (ImageView) findViewById(R.id.shade_right);
		top_head = (ImageView) findViewById(R.id.top_head);
		top_more = (ImageView) findViewById(R.id.top_more);
		top_refresh = (ImageView) findViewById(R.id.top_refresh);
		top_progress = (ProgressBar) findViewById(R.id.top_progress);

		/**显示更多新闻频道按钮*/
		button_more_columns.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent_channel = new  Intent(getApplicationContext(), ChannelActivity.class);
				startActivityForResult(intent_channel, CHANNELREQUEST);
				overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
			}
		});
		top_head.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if(side_drawer.isMenuShowing()){
					side_drawer.showContent();
				}else{
					side_drawer.showMenu();
				}
			}
		});
		top_more.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if(side_drawer.isSecondaryMenuShowing()){
					side_drawer.showContent();
				}else{
					side_drawer.showSecondaryMenu();
				}
			}
		});
		setChangelView();

	}

	/**
	 *  当栏目项发生变化时候调用
	 * */
	private void setChangelView() {
		initColumnData();
		initTabColumn();
		initFragment();
	}
	/** 获取Column栏目 数据*/
	private void initColumnData() {
		userChannelList = ChannelManage
				.getManage(M_Application.getInstance()
						.getSQLHelper()).getUserChannel();
	}


	/**
	 *  初始化Column栏目项
	 * */
	private void initTabColumn() {
		mRadioGroup_content.removeAllViews();
		int count =  userChannelList.size();
		mColumnHorizontalScrollView.setParam(this, mScreenWidth,
				mRadioGroup_content, shade_left, shade_right,
				ll_more_columns, rl_column);
		for(int i = 0; i< count; i++){
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(mItemWidth , ViewGroup.LayoutParams.WRAP_CONTENT);
			params.leftMargin = 5;
			params.rightMargin = 5;
			TextView columnTextView = new TextView(this);
			columnTextView.setTextAppearance(this, R.style.top_category_scroll_view_item_text);
			columnTextView.setGravity(Gravity.CENTER);
			columnTextView.setPadding(5, 5, 5, 5);
			columnTextView.setId(i);
			columnTextView.setText(userChannelList.get(i).getName());
			columnTextView.setTextColor(getResources().getColorStateList(R.color.top_category_scroll_text_color_day));
			if(columnSelectIndex == i){
				columnTextView.setSelected(true);
			}
			columnTextView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					for(int i = 0;i < mRadioGroup_content.getChildCount();i++){
						View localView = mRadioGroup_content.getChildAt(i);
						if (localView != v)
							localView.setSelected(false);
						else{
							localView.setSelected(true);
							mViewPager.setCurrentItem(i);
						}
					}
				}
			});
			mRadioGroup_content.addView(columnTextView, i ,params);
		}
	}



	/**
	 *  选择的Column里面的Tab
	 * */
	private void selectTab(int tab_postion) {
		columnSelectIndex = tab_postion;
		for (int i = 0; i < mRadioGroup_content.getChildCount(); i++) {
			View checkView = mRadioGroup_content.getChildAt(tab_postion);
			int k = checkView.getMeasuredWidth();
			int l = checkView.getLeft();
			int i2 = l + k / 2 - mScreenWidth / 2;
			mColumnHorizontalScrollView.smoothScrollTo(i2, 0);
		}
		/**判断是否选中*/
		for (int j = 0; j <  mRadioGroup_content.getChildCount(); j++) {
			View checkView = mRadioGroup_content.getChildAt(j);
			boolean ischeck;
			if (j == tab_postion) {
				ischeck = true;
			} else {
				ischeck = false;
			}
			checkView.setSelected(ischeck);
		}
	}

	/**
	 *  初始化Fragment
	 *  动态添加Fragment数据
	 * */
	private void initFragment() {
		fragments.clear();
		int count =  userChannelList.size();
		for(int i = 0; i< count;i++){
			Bundle data = new Bundle();
			data.putString("text", userChannelList.get(i).getName());
			data.putInt("id", userChannelList.get(i).getId());
			data.putString("listNewClassName", Constants.listNewClassName);
			data.putString("dateilNewClassName",Constants.dateilNewClassName);
			String serviceUrl = null;
			switch (i){
				case 0:
					serviceUrl = NewApi.ZHXW;
					break;
				case 1:
					serviceUrl = NewApi.XYDT;
					break;
				case 2:
					serviceUrl = NewApi.MTGS;
					break;
				case 3:
					serviceUrl = NewApi.ZXZDT;
					break;
				case 4:
					serviceUrl = NewApi.TJYD;
					break;
				case 5:
					serviceUrl = NewApi.GSRW;
					break;
				case 6:
					serviceUrl = NewApi.XWSP;
					break;
				case 7:
					serviceUrl = NewApi.SYZC;
					break;
				case 8:
					serviceUrl = NewApi.ZTXW;
					break;
			}
			data.putString("serviceUrl", serviceUrl);
			NewsFragment newfragment = new NewsFragment();
			newfragment.setArguments(data);
			fragments.add(newfragment);
		}

		/**
		 * ViewPagerFragment 适配器
		 */
		NewsFragmentPagerAdapter mAdapetr = new NewsFragmentPagerAdapter(getSupportFragmentManager(), fragments);
		mViewPager.setAdapter(mAdapetr);
		mViewPager.setOnPageChangeListener(pageListener);
	}




	/**
	 *  ViewPager切换监听方法
	 * */
	public ViewPager.OnPageChangeListener pageListener= new ViewPager.OnPageChangeListener(){

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageSelected(int position) {
			mViewPager.setCurrentItem(position);
			selectTab(position);
		}
	};

	/**
	 * 初始化的SlindingMenu
	 */
	protected void initSlidingMenu() {
		side_drawer = new DrawerView(this).initSlidingMenu();
	}



	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == CHANNELREQUEST && resultCode ==CHANNELRESULT){
			/**数据已经发生改变了*/
			List<ChannelItem> newChannelList = ((List<ChannelItem>) ChannelManage
					.getManage(M_Application.getInstance()
							.getSQLHelper()).getUserChannel());

			userChannelList = ListUtil.addList(userChannelList,newChannelList);

		}
	}


	private long mExitTime;
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if(side_drawer.isMenuShowing() ||side_drawer.isSecondaryMenuShowing()){
				side_drawer.showContent();
			}else {
				if ((System.currentTimeMillis() - mExitTime) > 2000) {
					Toast.makeText(this, "再按一次退出",
							Toast.LENGTH_SHORT).show();
					mExitTime = System.currentTimeMillis();
				} else {
					finish();
				}
			}
			return true;
		}
		/**拦截MENU按钮点击事件，让他无任何操作*/
		if (keyCode == KeyEvent.KEYCODE_MENU) {
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
