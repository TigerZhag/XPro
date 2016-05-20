package tiger.xmsg2.utils;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

import com.lidroid.xutils.util.LogUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import tiger.xmsg2.event.SendMessageResultEvent;
import tiger.xmsg2.safe.PasswordManager;
import tiger.xmsg2.safe.Safeparam;

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
//        PasswordManager.generateActiveKey();
//        LogUtils.d("公钥长度：" + PasswordManager.publicKey.length());
        Safeparam.key = Safeparam.parseByte2HexStr(Safeparam.generateActiveKey());

        message = msg;
//        sendMsg(phoneNumber,PasswordManager.FLAG_REQUEST_KEY,context);

        sendMsg(phoneNumber,PasswordManager.FLAG_REQUEST_KEY + Safeparam.key,context);
    }

    public static void sendPositivemessage(Context context){
        //生成密钥
        LogUtils.d("公钥长度：" + PasswordManager.publicKey.length());
        Safeparam.key = Safeparam.parseByte2HexStr(Safeparam.generateActiveKey());;
        sendMsg(PasswordManager.lastContact,PasswordManager.FLAG_BACK_KEY + Safeparam.key,context);
    }

    public static void sendActualMsg(Context context){
        //生成密文并发送
        sendMsg(PasswordManager.lastContact,Safeparam.encrypeMsg(message),context);
    }

    private static final String TAG = "SMSHelper";
    public static void sendMsg(String phoneNumber,String msg,Context context){
        //处理返回的发送状态
        Log.d(TAG, "sendMsg: phonenumber： " + phoneNumber);
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
                        Log.d(TAG, "send message success");
                        EventBus.getDefault().post(new SendMessageResultEvent(true));
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        EventBus.getDefault().post(new SendMessageResultEvent(false));
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
                Log.d(TAG, "send message receive success");
                Toast.makeText(context, "对方已成功接收", Toast.LENGTH_SHORT).show();
            }
        }, new IntentFilter(DELIVERED_SMS_ACTION));

        //发送短信
        // 获取短信管理器
        android.telephony.SmsManager smsManager = android.telephony.SmsManager.getDefault();
        // 拆分短信内容（手机短信长度限制）
        List<String> divideContents = smsManager.divideMessage(msg);
        for (String text : divideContents) {
            Log.d(TAG, "sendMsg: 正在发送");
            smsManager.sendTextMessage(phoneNumber, null, text, sendIntent, backIntent);
            Log.d(TAG, "sendMsg: 发送成功");
        }
    }
}
