package github.tiger.xmsg.database;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import org.greenrobot.eventbus.EventBus;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import github.tiger.xmsg.model.Message;

/**
 * Created by Administrator on 2016/4/20.
 */
public class MessageHelper {
    public static void getMsgs(Context context){
        final String SMS_URI_ALL = "content://sms/";

        Uri uri = Uri.parse(SMS_URI_ALL);
        String[] projection = new String[] { "_id", "address", "person", "body", "date", "type" };
        Cursor cur = context.getContentResolver().query(uri, projection, null, null, "date desc");      // 获取手机内部短信

        List<Message> msgs = new ArrayList<>();
        if (cur.moveToFirst()) {
            int index_Address = cur.getColumnIndex("address");
            int index_Person = cur.getColumnIndex("person");
            int index_Body = cur.getColumnIndex("body");
            int index_Date = cur.getColumnIndex("date");
            int index_Type = cur.getColumnIndex("type");

            do {
                Message msg = new Message();
                msg.address = cur.getString(index_Address);
                msg.intPerson = cur.getInt(index_Person);
                msg.getTypeAndBody(cur.getString(index_Body));
                msg.date = cur.getLong(index_Date);
                msg.sendType = cur.getInt(index_Type);
                msgs.add(msg);
            } while (cur.moveToNext());

            if (!cur.isClosed()) {
                cur.close();
                cur = null;
            }
        }

        EventBus.getDefault().post(msgs);
    }
}
