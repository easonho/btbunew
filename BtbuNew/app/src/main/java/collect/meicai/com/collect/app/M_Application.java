package collect.meicai.com.collect.app;


import android.app.Activity;
import android.app.Application;
import android.content.Context;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import collect.meicai.com.collect.app.db.SQLHelper;

/**
 * Coder: 何毅
 * Desc : 全局的App管理类
 * Date : 2015-04-01
 * Time : 19:49
 * Version:1.0
 */
public class M_Application extends Application {


	private static M_Application instance;
	private SQLHelper sqlHelper;
    private static UMSocialService mController;

	public static M_Application getInstance(){
		if (instance == null){
			instance = new M_Application();
		}
		return  instance;

	}

	@Override
	public void onCreate() {
		super.onCreate();
		initImageLoader(getApplicationContext());   //初始化ImageLoader
        initUmengSdk();
		instance = this;
	}


	/** 获取数据库Helper */
	public SQLHelper getSQLHelper() {
		if (sqlHelper == null)
			sqlHelper = new SQLHelper(instance);
		return sqlHelper;
	}

	@Override
	public void onTerminate() {
		if (sqlHelper != null)
			sqlHelper.close();
		super.onTerminate();
		//整体摧毁的时候调用这个方法
	}


    public static void initUmengSdk(){
        mController = UMServiceFactory
                .getUMSocialService("com.umeng.share");
    }



	/** 初始化ImageLoader */
	public static void initImageLoader(Context context) {
		File cacheDir = StorageUtils.getOwnCacheDirectory(context, "btbunews/Cache");//获取到缓存的目录地址
		//创建配置ImageLoader(所有的选项都是可选的,只使用那些你真的想定制)，这个可以设定在APPLACATION里面，设置为全局的配置参数
		ImageLoaderConfiguration config = new ImageLoaderConfiguration
				.Builder(context)
				//.memoryCacheExtraOptions(480, 800) // max width, max height，即保存的每个缓存文件的最大长宽
				//.discCacheExtraOptions(480, 800, CompressFormat.JPEG, 75, null) // Can slow ImageLoader, use it carefully (Better don't use it)设置缓存的详细信息，最好不要设置这个
				.threadPoolSize(3)//线程池内加载的数量
				.threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory()
						//.memoryCache(new UsingFreqLimitedMemoryCache(2 * 1024 * 1024)) // You can pass your own memory cache implementation你可以通过自己的内存缓存实现
						//.memoryCacheSize(2 * 1024 * 1024)
						///.discCacheSize(50 * 1024 * 1024)
				.discCacheFileNameGenerator(new Md5FileNameGenerator())//将保存的时候的URI名称用MD5 加密
						//.discCacheFileNameGenerator(new HashCodeFileNameGenerator())//将保存的时候的URI名称用HASHCODE加密
				.tasksProcessingOrder(QueueProcessingType.LIFO)
						//.discCacheFileCount(100) //缓存的File数量
				.discCache(new UnlimitedDiscCache(cacheDir))//自定义缓存路径
						//.defaultDisplayImageOptions(DisplayImageOptions.createSimple())
						//.imageDownloader(new BaseImageDownloader(context, 5 * 1000, 30 * 1000)) // connectTimeout (5 s), readTimeout (30 s)超时时间
				.writeDebugLogs() // Remove for release app
				.build();
		// Initialize ImageLoader with configuration.
		ImageLoader.getInstance().init(config);//全局初始化此配置
	}


	private List<Activity> activityList = new LinkedList<Activity>();

	// 添加Activity到容器中
	public void addActivity(Activity activity) {
		activityList.add(activity);
	}

	//从集合中移除Activity
	public void removeActivity(Activity activity){activityList.remove(activity); }

	// 遍历所有Activity并finish
	public void exit() {
		try {
			for (Activity activity : activityList) {
				if (activity != null){
					activity.finish();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			System.exit(0);
		}
	}

}
