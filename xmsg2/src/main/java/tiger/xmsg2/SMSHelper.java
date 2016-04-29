package tiger.xmsg2;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.telephony.SmsManager;
import android.widget.Toast;

import com.lidroid.xutils.util.LogUtils;

import java.util.List;

/**
 * Author: Tiger zhang
 * Date:   2016/4/28 0028
 * Email:  Tiger.zhag@gmail.com
 * Github: https://github.com/TigerZhag
 */
public class SMSHelper {
    public static String message = "";

    public static void sendActivemessage(String phoneNumber, String msg, Context context){
        //生成密钥
        PasswordManager.generateActiveKey();
        LogUtils.d("公钥长度：" + PasswordManager.publicKey.length());
        message = msg;

        sendMsg(phoneNumber,PasswordManager.FLAG_REQUEST_KEY + PasswordManager.publicKey,context);
    }

    public static void sendPositivemessage(Context context){
        //生成密钥
        LogUtils.d("公钥长度：" + PasswordManager.publicKey.length());
        sendMsg(PasswordManager.lastContact,PasswordManager.FLAG_BACK_KEY + PasswordManager.publicKey,context);
    }

    public static void sendActualMsg(Context context){
        //生成密文并发送
        sendMsg(PasswordManager.lastContact,PasswordManager.encrypeMsg(message),context);
    }

    public static void sendMsg(String phoneNumber,String msg,Context context){
        //处理返回的发送状态
        String SENT_SMS_ACTION = "SENT_SMS_ACTION";
        Intent sentIntent = new Intent(SENT_SMS_ACTION);
        PendingIntent sendIntent= PendingIntent.getBroadcast(context, 0, sentIntent,
                0);
        // register the Broadcast Receivers
        context.registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context _context, Intent _intent) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        Toast.makeText(context,
                                "短信发送成功", Toast.LENGTH_SHORT)
                                .show();
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        break;
                }
            }
        }, new IntentFilter(SENT_SMS_ACTION));

        //处理返回的接收状态
        String DELIVERED_SMS_ACTION = "DELIVERED_SMS_ACTION";
        // create the deilverIntent parameter
        Intent deliverIntent = new Intent(DELIVERED_SMS_ACTION);
        PendingIntent backIntent= PendingIntent.getBroadcast(context, 0,
                deliverIntent, 0);
        context.registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context _context, Intent _intent) {
                Toast.makeText(context,
                        "收信人已经成功接收", Toast.LENGTH_SHORT)
                        .show();
            }
        }, new IntentFilter(DELIVERED_SMS_ACTION));

        //发送短信
        // 获取短信管理器
        android.telephony.SmsManager smsManager = android.telephony.SmsManager.getDefault();
        // 拆分短信内容（手机短信长度限制）
        List<String> divideContents = smsManager.divideMessage(msg);
        for (String text : divideContents) {
            smsManager.sendTextMessage(phoneNumber, null, msg, sendIntent, backIntent);
        }
    }
}
