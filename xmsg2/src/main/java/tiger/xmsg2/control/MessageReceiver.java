package tiger.xmsg2.control;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import com.lidroid.xutils.util.LogUtils;

import tiger.xmsg2.safe.PasswordManager;
import tiger.xmsg2.safe.Safeparam;
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
//            PasswordManager.bPublicKey = msg.substring(PasswordManager.FLAG_BACK_KEY.length());
//            LogUtils.d("乙方公钥：" + PasswordManager.bPublicKey);
            Toast.makeText(context, "收到密钥回复短信：" + msg, Toast.LENGTH_SHORT).show();
            Safeparam.key = msg.substring(PasswordManager.FLAG_BACK_KEY.length());
            SMSHelper.sendActualMsg(context);
        }else if (msg.startsWith(PasswordManager.FLAG_REQUEST_KEY)){
            //根据对方公钥生成密钥对并回复公钥
//            PasswordManager.generatePositiveKey(msg.substring(PasswordManager.FLAG_REQUEST_KEY.length()));
//            LogUtils.d("甲方公钥：" + PasswordManager.bPublicKey);
            Toast.makeText(context, "收到请求交换密钥短信：" + msg, Toast.LENGTH_SHORT).show();
            Safeparam.key = Safeparam.parseByte2HexStr(Safeparam.generateActiveKey());
            SMSHelper.sendPositivemessage(context);
        }else if (msg.startsWith(PasswordManager.FLAG_MSG)) {
            //加密短信实体,解密
//            LogUtils.d("收到加密短信：" + msg.substring(PasswordManager.FLAG_MSG.length()));
//            LogUtils.d("解密后：" + PasswordManager.decryptMsg(msg.substring(PasswordManager.FLAG_MSG.length())));
            Toast.makeText(context, "收到一条加密短信：" + msg, Toast.LENGTH_SHORT).show();
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("新加密信息");
            builder.setMessage(Safeparam.decryptMsg(msg));
            builder.setPositiveButton("删除", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    abortBroadcast();
                }
            });
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            builder.create().show();
            Toast.makeText(context, Safeparam.decryptMsg(msg), Toast.LENGTH_SHORT).show();
        }else {
            //if 普通短信
            Toast.makeText(context, "收到一条普通短信：" + msg, Toast.LENGTH_SHORT).show();
            return;
        }
        abortBroadcast();
    }
}
