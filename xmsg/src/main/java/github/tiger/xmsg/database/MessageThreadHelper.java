package github.tiger.xmsg.database;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import github.tiger.xmsg.model.Message;
import github.tiger.xmsg.model.MsgThread;

/**
 * Author: Tiger zhang
 * Date:   2016/4/25 0025
 * Email:  Tiger.zhag@gmail.com
 * Github: https://github.com/TigerZhag
 */
public class MessageThreadHelper {
    private static final int ID             = 0;
    private static final int DATE           = 1;
    private static final int MESSAGE_COUNT  = 2;
    private static final int RECIPIENT_IDS  = 3;
    private static final int SNIPPET        = 4;
    private static final int SNIPPET_CS     = 5;
    private static final int READ           = 6;
    private static final int TYPE           = 7;
    private static final int ERROR          = 8;
    private static final int HAS_ATTACHMENT = 9;

    public static void getThreads(Context context){
        final String SMS_URI_ALL = "content://mms-sms/conversations";

        Uri uri = Uri.parse(SMS_URI_ALL);
        String[] projection = new String[] { "_id", "date", "message_count", "recipient_ids",
                "snippet", "snippet_cs", "read", "error", "has_attachment"  };
        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, "date desc");      // 获取手机内部短信

        List<MsgThread> threads = new ArrayList<>();
        while (cursor.moveToNext()){
            MsgThread thread = new MsgThread();
            thread.id = cursor.getLong(ID);
            thread.date = cursor.getLong(DATE);
            thread.msg_count = cursor.getInt(MESSAGE_COUNT);
            thread.recipient_id = cursor.getInt(RECIPIENT_IDS);
            thread.snippet = cursor.getString(SNIPPET);

            threads.add(thread);
        }
        if (!cursor.isClosed()) {
            cursor.close();
            cursor = null;
        }

        EventBus.getDefault().post(threads);
    }
}
