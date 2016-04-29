package tiger.xmsg2;

import com.lidroid.xutils.util.LogUtils;

import java.util.Map;

/**
 * Author: Tiger zhang
 * Date:   2016/4/28 0028
 * Email:  Tiger.zhag@gmail.com
 * Github: https://github.com/TigerZhag
 */
public class PasswordManager {
    public static String lastContact;
    public static String publicKey;
    public static String privateKey;

    public static String bPublicKey;

    public static String FLAG_BACK_KEY = "bky:";
    public static String FLAG_REQUEST_KEY = "rky:";
    public static String FLAG_MSG = "msg:";

    public static void generateActiveKey(){
        try {
            Map<String, Object> aKeyMap = DHCoder.initKey();
            publicKey = DHCoder.getPublicKey(aKeyMap);
            privateKey = DHCoder.getPrivateKey(aKeyMap);
        } catch (Exception e) {
            e.printStackTrace();
        }

        LogUtils.d("我是甲方：甲方公钥:\r" + publicKey);
        LogUtils.d("甲方私钥:\r" + privateKey);
    }

    public static void generatePositiveKey(String aPublicKey){
        try {
            Map<String, Object> aKeyMap = DHCoder.initKey(aPublicKey);
            publicKey = DHCoder.getPublicKey(aKeyMap);
            privateKey = DHCoder.getPrivateKey(aKeyMap);
        } catch (Exception e) {
            e.printStackTrace();
        }

        LogUtils.d("我是乙方：乙方公钥:\r" + publicKey);
        LogUtils.d("乙方私钥:\r" + privateKey);
    }

    public static String encrypeMsg(String msg){
        byte[] bCode = new byte[0];
        try {
            bCode = DHCoder.encrypt(msg.getBytes(), bPublicKey,
                    privateKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new String(bCode);
    }

    public static String decryptMsg(String msg){
        byte[] bDecode = new byte[0];
        try {
            bDecode = DHCoder.decrypt(msg.getBytes(), bPublicKey, privateKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new String(bDecode);
    }
}
