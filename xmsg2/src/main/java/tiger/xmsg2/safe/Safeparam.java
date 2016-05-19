package tiger.xmsg2.safe;

import java.security.Key;
import java.security.NoSuchAlgorithmException;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * Author: Tiger zhang
 * Date:   2016/5/20
 * Email:  Tiger.zhag1993@gmail.com
 * Github: https://github.com/TigerZhag
 */
public class Safeparam extends PasswordManager{
//    private static String ALGORITHM = "AES";
//    private static int KEYSIZE = 128;
//
//    public static Key tokey(byte[] key){
//        return new SecretKeySpec(key,ALGORITHM);
//    }
//
//    public void byte[] generateActiveKey(){
//        try {
//            KeyGenerator keyGenerator = KeyGenerator.getInstance(ALGORITHM);
//            keyGenerator.init(KEYSIZE);
//            SecretKey secretKey = keyGenerator.generateKey();
//            return secretKey.getEncoded();
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//    public void byte[] generatePositiveKey(){
//
//    }
}
