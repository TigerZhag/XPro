package github.tiger.xmsg.control;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import github.tiger.xmsg.UI.PasswordActivity;

/**
 * Author: Tiger zhang
 * Date:   2016/4/22
 * Email:  Tiger.zhag@gmail.com
 * GitHub: https://github.com/TigerZhag
 */
public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //通过判断Activity启动之前是否有本应用Activity可见，若无，启动密码界面
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

            }

            @Override
            public void onActivityStarted(Activity activity) {

            }

            @Override
            public void onActivityResumed(Activity activity) {
                if (activityNum == 0){
                    PasswordActivity.launch(activity);
                }
                activityNum += 1;
            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {
                activityNum -= 1;
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {

            }
        });
    }
    private int activityNum = 0;
}
