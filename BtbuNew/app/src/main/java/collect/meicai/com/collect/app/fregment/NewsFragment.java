package collect.meicai.com.collect.app.fregment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;

import collect.meicai.com.collect.R;
import collect.meicai.com.collect.app.activity.DetailsActivity;
import collect.meicai.com.collect.app.adapter.NewsAdapter;
import collect.meicai.com.collect.app.bean.NewsEntity;
import collect.meicai.com.collect.app.service.NEWService;
import collect.meicai.com.collect.app.view.HeadListView;

/***
 * 详细界面信息表
 */
public class NewsFragment extends Fragment  implements SwipeRefreshLayout.OnRefreshListener{
	private Activity activity;
	private ArrayList<NewsEntity> newsList  = new ArrayList<>();
	private HeadListView mListView;
	private NewsAdapter mAdapter;
	private String text;
	private int channel_id;
	private ImageView detail_loading;
	public final static int SET_NEWSLIST = 0;
	//Toast提示框
	private RelativeLayout notify_view;
	private TextView notify_view_text;
	private SwipeRefreshLayout swipeLayout;

	/**记录当前页的信息*/
	private Integer mCurrentPage = 1;

	private String serviceUrl;

	/**需要查询Div 的Class*/
	private String listNewClassName;

	/**详细信息Div 的Class*/
	private String dateilNewClassName;


	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case SET_NEWSLIST:
					detail_loading.setVisibility(View.GONE);
					setmAdapter();
					mListView.setOnScrollListener(mAdapter);
					mListView.setPinnedHeaderView(LayoutInflater.from(activity).inflate(R.layout.list_item_section, mListView, false));
					mListView.setOnItemClickListener(new OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> parent, View view,
												int position, long id) {
							Intent intent = new Intent(activity, DetailsActivity.class);
							intent.putExtra("news", mAdapter.getItem(position));
							intent.putExtra("dateilNewClassName",dateilNewClassName);
							startActivity(intent);
							activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
						}
					});
					break;
				default:
					break;
			}
			super.handleMessage(msg);
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		Bundle args = getArguments();
		this.text = args != null ? args.getString("text") : "";
		this.channel_id = args != null ? args.getInt("id", 0) : 0;
		this.listNewClassName =args != null ?args.getString("listNewClassName"):"";
		this.dateilNewClassName = args != null ?args.getString("dateilNewClassName"):"";
		this.serviceUrl = args != null ?args.getString("serviceUrl"):"";
		initData();
		super.onCreate(savedInstanceState);
	}

	public void onResume() {
		super.onResume();
		MobclickAgent.onResume(getActivity());       //统计时长
	}
	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(getActivity());
	}


	private void initData() {
		this.newsList = NEWService.getNewsByXutils(activity, serviceUrl, 1, listNewClassName);
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		setListener();
	}

	/**设置事件发生滚动的时候加载更多的数据*/
	private  void  setListener(){

		mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

				if(visibleItemCount == newsList.size()-1){
					//TODO 加载更多数据
					ArrayList<NewsEntity> mNewsList =NEWService.getNewsByXutils(activity, serviceUrl, ++mCurrentPage, listNewClassName);
					newsList.addAll(mNewsList);
					mAdapter.notifyDataSetChanged();
				}
			}
		});

	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.activity = activity;
	}


	/** 此方法意思为fragment是否可见 ,可见时候加载数据 */
	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		if (isVisibleToUser) {
			//fragment可见时加载数据
			if(newsList !=null && newsList.size() !=0){
				handler.obtainMessage(SET_NEWSLIST).sendToTarget();
			}else{
				new Handler().postDelayed(new Runnable() {
					@Override
					public void run() {
						handler.obtainMessage(SET_NEWSLIST).sendToTarget();
					}
				},2);
			}
		}
		super.setUserVisibleHint(isVisibleToUser);
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View view = LayoutInflater.from(getActivity()).inflate(R.layout.news_fragment, null);
		swipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
		swipeLayout.setOnRefreshListener(this);
		swipeLayout.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
				android.R.color.holo_orange_light, android.R.color.holo_red_light);

		mListView = (HeadListView) view.findViewById(R.id.mListView);
		TextView item_textview = (TextView)view.findViewById(R.id.item_textview);
		detail_loading = (ImageView)view.findViewById(R.id.detail_loading);
		//Toast提示框
		notify_view = (RelativeLayout)view.findViewById(R.id.notify_view);
		notify_view_text = (TextView)view.findViewById(R.id.notify_view_text);
		item_textview.setText(text);
		return view;
	}

	/***刷新操作**/
	@Override
	public void onRefresh() {
		new Handler().postDelayed(new Runnable() {
			public void run() {
				swipeLayout.setRefreshing(false);
				mAdapter.notifyDataSetChanged();
			}
		}, 100);
	}


	/**
	 * 设置数据适配器
	 */
	private  void setmAdapter(){
		if(mAdapter == null){
			mAdapter = new NewsAdapter(activity, newsList);
			mListView.setAdapter(mAdapter);
		}
		mAdapter.notifyDataSetChanged();
	}

	/** 摧毁视图 */
	@Override
	public void onDestroyView() {
		super.onDestroyView();
		mAdapter = null;
	}
	/** 摧毁该Fragment，一般是FragmentActivity 被摧毁的时候伴随着摧毁 */
	@Override
	public void onDestroy() {
		super.onDestroy();
	}


}
