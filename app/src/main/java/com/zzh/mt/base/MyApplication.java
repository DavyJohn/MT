package com.zzh.mt.base;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.StrictMode;
import android.support.annotation.RequiresApi;
import android.support.multidex.MultiDex;

import com.tencent.bugly.Bugly;
import com.tencent.bugly.beta.Beta;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zzh.mt.activity.MainActivity;
import com.zzh.mt.R;
import com.zzh.mt.utils.NetworkUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;

//import cn.jpush.android.api.JPushInterface;

public class MyApplication extends Application {
	private static MyApplication mcontext;
	private static MyApplication instance;
	public static int mNetWorkState = -1;
	private static Handler mHandler;
	private static Thread mMainThread;
	private static long		mMainThreadId;
	private static Looper mMainThreadLooper;
	int cacheSize = 100*1024*1024;//100M

//	private List<AlipayData> AlipayData = new ArrayList<AlipayData>();
//	private List<Cart> orderSelectList = new ArrayList<Cart>();

	private static List<Activity> activities = new ArrayList<Activity>();

	@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
	@Override
	public void onCreate() {
		super.onCreate();
		mcontext = this;
		StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
		StrictMode.setVmPolicy(builder.build());
		builder.detectFileUriExposure();

		// handler,用来子线程和主线程通讯
		mHandler = new Handler();
		// 主线程
		mMainThread = Thread.currentThread();
		// id
		// mMainThreadId = mMainThread.getId();
		mMainThreadId = android.os.Process.myTid();
		// looper
		mMainThreadLooper = getMainLooper();
		File cacheFile = new File(getDiskCacheDir(mcontext),"cache");
		final Cache cache = new Cache(cacheFile,cacheSize);
		OkHttpClient okHttpClient = new OkHttpClient.Builder()
//                .addInterceptor(new LoggerInterceptor("TAG"))
				.connectTimeout(10000L, TimeUnit.MILLISECONDS)
				.readTimeout(10000L, TimeUnit.MILLISECONDS)
						//其他配置
//				.cache(cache)
				.build();

		OkHttpUtils.initClient(okHttpClient);


		//自定义崩溃
//		CrashHandler crashHandler = CrashHandler.getInstance();
//		crashHandler.init(getApplicationContext());
//	 	JPushInterface.setDebugMode(true);
//		JPushInterface.init(this);
		//注意：如果您之前使用过Bugly SDK，请将以下这句注释掉。
//		CrashReport.init(getApplicationContext(),"7a9e1dda22",false);
		Beta.autoInit = true;//自动初始化开关
		Beta.autoCheckUpgrade = true;//自动检查开关
		Beta.upgradeCheckPeriod = 60 * 1000;//升级检查周期
		Beta.initDelay = 1 * 1000;//延迟初始化
		Beta.largeIconId = R.mipmap.ic_launcher;//
		Beta.smallIconId = R.mipmap.ic_launcher;
		Beta.defaultBannerId = R.mipmap.ic_launcher;
		Beta.storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
		Beta.showInterruptedStrategy = true;
		Beta.canShowUpgradeActs.add(MainActivity.class);
		Bugly.init(getApplicationContext(),"91cb31c05e",true);

	}

	public static Handler getHandler()
	{
		return mHandler;
	}

	public static Thread getMainThread()
	{
		return mMainThread;
	}

	public static long getMainThreadId()
	{
		return mMainThreadId;
	}

	public static Looper getMainThreadLooper()
	{
		return mMainThreadLooper;
	}

	public MyApplication() {

	}

	public static MyApplication getInstance() {
		if (instance == null)
			instance = new MyApplication();
		return instance;
	}


	/**
	 * 获取网络类型
	 * 
	 * @return
	 */
	public void getConnType() {
		mNetWorkState = NetworkUtils.getNetworkState(mcontext);
	}


	public static MyApplication getAppContext() {
		return mcontext;
	}


	public void add(Activity activity) {
		if (activity != null) {
			activities.add(activity);
		}
	}

	/**
	 * 销毁当前集合中所有的Activity。
	 */
	public void finishAll() {
		try {
			for (Activity activity : activities) {
				if (activity != null)
					activity.finish();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			System.exit(0);
		}

	}

	public void finishAllExceptHome() {
		try {
			for (Activity activity : activities) {
				if (activity != null && !(activity instanceof MainActivity))
					activity.finish();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void attachBaseContext(Context base) {
		super.attachBaseContext(base);
		MultiDex.install(base);
		Beta.installTinker();
	}

	public String getDiskCacheDir(Context context){
		String cachePath = null;
		if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())||!Environment.isExternalStorageRemovable()){
			cachePath = context.getExternalCacheDir().getPath();
		}else {
			cachePath = context.getCacheDir().getPath();
		}
		return cachePath;
	}

}
