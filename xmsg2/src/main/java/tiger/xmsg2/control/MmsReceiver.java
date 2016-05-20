package tiger.xmsg2.control;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Author: Tiger zhang
 * Date:   2016/5/20
 * Email:  Tiger.zhag1993@gmail.com
 * Github: https://github.com/TigerZhag
 */
public class MmsReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("cky","MmsReceiver: "+intent);
    }

}