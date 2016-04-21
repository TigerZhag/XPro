package github.tiger.xmsg.control;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Author: Tiger zhang
 * Date:   2016/4/21 0021
 * Email:  Tiger.zhag@gmail.com
 */
public class MessageReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        //弹出通知，点击进入输入密码界面
        abortBroadcast();
    }
}
