package tiger.xmsg2.control;

/**
 * Author: Tiger zhang
 * Date:   2016/5/20
 * Email:  Tiger.zhag1993@gmail.com
 * Github: https://github.com/TigerZhag
 */
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class HeadlessSmsSendService extends Service{

    @Override
    public IBinder onBind(Intent intent) {
        Log.i("cky","HeadlessSmsSendService: "+intent);
        return null;
    }

}