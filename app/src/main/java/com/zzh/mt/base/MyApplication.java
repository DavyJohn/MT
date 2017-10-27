package com.zzh.mt.base;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.StrictMode;
import android.support.annotation.RequiresApi;
import android.support.multidex.MultiDex;

import com.pgyersdk.crash.PgyCrashManager;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.beta.Beta;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.https.HttpsUtils;
import com.zzh.mt.BuildConfig;
import com.zzh.mt.activity.HomeActivity;
import com.zzh.mt.R;
import com.zzh.mt.utils.Contants;
import com.zzh.mt.utils.LocaleUtils;
import com.zzh.mt.utils.NetworkUtils;

import org.xutils.x;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import okhttp3.Cache;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
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

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
			StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
			StrictMode.setVmPolicy(builder.build());
			builder.detectFileUriExposure();
		}
		//语言切换
		Locale _UserLocale= LocaleUtils.getUserLocale(this);
		if (_UserLocale == null){
			_UserLocale = LocaleUtils.LOCALE_CHINESE;
		}
		if (_UserLocale.equals(LocaleUtils.LOCALE_CHINESE)){
			Contants.LANGUAGENEM = 0;
		}else {
			Contants.LANGUAGENEM = 1;
		}
		LocaleUtils.updateLocale(this, _UserLocale);
		//end
		// handler,用来子线程和主线程通讯
		mHandler = new Handler();
		// 主线程
		mMainThread = Thread.currentThread();
		// id
		// mMainThreadId = mMainThread.getId();
		mMainThreadId = android.os.Process.myTid();
		// looper
		mMainThreadLooper = getMainLooper();
		HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory(null, null, null);
		OkHttpClient okHttpClient = new OkHttpClient.Builder()
				.connectTimeout(10000L, TimeUnit.MILLISECONDS)
				.readTimeout(10000L, TimeUnit.MILLISECONDS)
						//其他配置
				.hostnameVerifier(new HostnameVerifier() {
					@Override
					public boolean verify(String s, SSLSession sslSession) {
						return true;
					}
				})
				.sslSocketFactory(sslParams.sSLSocketFactory,sslParams.trustManager)
				.cookieJar(new CookieJar() {//持久化
					private final HashMap<String, List<Cookie>> cookieStore = new HashMap<>();
					@Override
					public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
						cookieStore.put(url.host(), cookies);
					}

					@Override
					public List<Cookie> loadForRequest(HttpUrl url) {
						List<Cookie> cookies = cookieStore.get(url.host());
						return cookies != null ? cookies : new ArrayList<Cookie>();
					}
				})
				.build();

		OkHttpUtils.initClient(okHttpClient);
//xutils
		x.Ext.init(mcontext);
		x.Ext.setDebug(BuildConfig.DEBUG);
		x.Ext.setDefaultHostnameVerifier(new HostnameVerifier() {
			@Override
			public boolean verify(String hostname, SSLSession session) {
				return true;
			}
		});
		//腾讯
		Beta.autoInit = true;//自动初始化开关
		Beta.autoCheckUpgrade = true;//自动检查开关
		Beta.upgradeCheckPeriod = 60 * 1000;//升级检查周期
		Beta.initDelay = 1 * 1000;//延迟初始化
		Beta.largeIconId = R.drawable.logo;//
		Beta.smallIconId = R.drawable.logo;
		Beta.defaultBannerId = R.drawable.logo;
		Beta.storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
		Beta.showInterruptedStrategy = true;
		Beta.canShowUpgradeActs.add(HomeActivity.class);
		Bugly.init(getApplicationContext(),"91cb31c05e",true);
		//end
		PgyCrashManager.register(this);
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
				if (activity != null && !(activity instanceof HomeActivity))
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

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		Locale _UserLocale=LocaleUtils.getUserLocale(this);
		//系统语言改变了应用保持之前设置的语言
		if (_UserLocale != null) {
			Locale.setDefault(_UserLocale);
			Configuration _Configuration = new Configuration(newConfig);
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
				_Configuration.setLocale(_UserLocale);
			} else {
				_Configuration.locale =_UserLocale;
			}
			getResources().updateConfiguration(_Configuration, getResources().getDisplayMetrics());
		}
	}

}
