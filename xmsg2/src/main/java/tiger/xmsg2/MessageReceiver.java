package tiger.xmsg2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Author: Tiger zhang
 * Date:   2016/4/29 0029
 * Email:  Tiger.zhag@gmail.com
 * Github: https://github.com/TigerZhag
 */
public class MessageReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "收到了一条短信---XMsg", Toast.LENGTH_SHORT).show();
        //判断短信类型
        //if 协商密钥 生成密钥并回复

        //if 加密短信实体

        //if 普通短信

        abortBroadcast();
    }
}
