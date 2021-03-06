package com.hss01248.imageloaderdemo;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.text.TextUtils;

import com.elvishew.xlog.XLog;
import com.github.piasy.biv.BigImageViewer;
import com.github.piasy.biv.view.BigImageView;
import com.hss01248.dialog.MyActyManager;
import com.hss01248.dialog.StyledDialog;
import com.hss01248.frescoloader.FrescoLoader;
import com.hss01248.frescoloader.big.BigImageLoader;
import com.hss01248.glideloader.GlideLoader;
import com.hss01248.image.ImageLoader;
import com.hss01248.image.config.GlobalConfig;
import com.hss01248.image.config.SingleConfig;
import com.hss01248.image.interfaces.LoadInterceptor;
import com.hss01248.image.memory.ImageMemoryHookManager;
import com.squareup.leakcanary.LeakCanary;

import es.dmoral.toasty.MyToast;

/**
 * Created by Administrator on 2017/3/15 0015.
 */

public class BaseApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ImageLoader.init(getApplicationContext(), 500,new GlideLoader());
        GlobalConfig.debug = true;
        GlobalConfig.interceptor = new LoadInterceptor() {
            @Override
            public boolean intercept(SingleConfig config) {

                XLog.w(config.toString());
                if(config.getWidth()> 0 || config.getHeight() >0){
                    if(!TextUtils.isEmpty(config.getUrl())){
                        if(config.getUrl().contains("w=") ||config.getUrl().contains("h=")){
                            return false;
                        }
                        String line = "";
                        if(config.getWidth() >0){
                            line += "w="+config.getWidth();
                            if(config.getHeight() >0){
                                line += "&";
                            }
                        }
                        if(config.getHeight() >0){
                            line += "h="+config.getHeight();
                        }

                        if(config.getUrl().contains("?")){
                            config.setUrl(config.getUrl()+"&"+line);
                        }else{
                            config.setUrl(config.getUrl()+"?"+line);
                        }
                    }
                }
                XLog.w(config.toString());
                return false;
            }
        };
       // GlobalConfig.placeHolderResId = R.drawable.im_item_list_opt;
       // GlobalConfig.errorResId = R.drawable.im_item_list_opt_error;
        //BigImageViewer.initialize(GlideBigLoader.with(this));
        //GlobalConfig.setBigImageDark(false);
        LeakCanary.install(this);
        MyToast.init(this,true,true);

       // PhotoUtil.init(getApplicationContext(),new GlideIniter());//第二个参数根据具体依赖库而定
        StyledDialog.init(this);
        //Logger.initialize(new Settings());
        XLog.init();
        //ImageMemoryHookManager.hook(this);

        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

            }

            @Override
            public void onActivityStarted(Activity activity) {

            }

            @Override
            public void onActivityResumed(Activity activity) {
                MyActyManager.getInstance().setCurrentActivity(activity);

            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {

            }
        });
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        ImageLoader.trimMemory(level);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        ImageLoader.clearAllMemoryCaches();
    }
}
