package tiger.xmsg2.control;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import com.lidroid.xutils.util.LogUtils;

import tiger.xmsg2.safe.PasswordManager;
import tiger.xmsg2.utils.SMSHelper;

/**
 * Author: Tiger zhang
 * Date:   2016/4/29 0029
 * Email:  Tiger.zhag@gmail.com
 * Github: https://github.com/TigerZhag
 */
public class MessageReceiver extends BroadcastReceiver {
    private static final String TAG = "MessageReceiver";
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive: 收到了一条短信---XMsg");
        if (!intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")) return;
        //判断短信类型
        StringBuilder sb = new StringBuilder();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            Object[] pdus = (Object[]) bundle.get("pdus");
            SmsMessage[] msg = new SmsMessage[pdus.length];
            for (int i = 0; i < pdus.length; i++) {
                msg[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                sb.append(msg[i].getDisplayMessageBody());
            }
            PasswordManager.lastContact = msg[0].getDisplayOriginatingAddress();
        }
        Toast.makeText(context, "收到来自：" + PasswordManager.lastContact + "的短信息：" + sb.toString(), Toast.LENGTH_SHORT).show();
        LogUtils.d("收到来自：" + PasswordManager.lastContact +"的短信息：" + sb.toString());
        String msg = sb.toString();
        //if 协商密钥 生成密钥并回复
        if (msg.startsWith(PasswordManager.FLAG_BACK_KEY)){
            //生成加密密钥并发送短信
            PasswordManager.bPublicKey = msg.substring(PasswordManager.FLAG_BACK_KEY.length());
            LogUtils.d("乙方公钥：" + PasswordManager.bPublicKey);
            SMSHelper.sendActualMsg(context);
        }else if (msg.startsWith(PasswordManager.FLAG_REQUEST_KEY)){
            //根据对方公钥生成密钥对并回复公钥
            PasswordManager.generatePositiveKey(msg.substring(PasswordManager.FLAG_REQUEST_KEY.length()));
            LogUtils.d("甲方公钥：" + PasswordManager.bPublicKey);
            SMSHelper.sendPositivemessage(context);
        }else if (msg.startsWith(PasswordManager.FLAG_MSG)) {
            //加密短信实体,解密
            LogUtils.d("收到加密短信：" + msg.substring(PasswordManager.FLAG_MSG.length()));
            LogUtils.d("解密后：" + PasswordManager.decryptMsg(msg.substring(PasswordManager.FLAG_MSG.length())));
        }else {
            //if 普通短信
            return;
        }
        abortBroadcast();
    }
}
