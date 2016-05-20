package tiger.xmsg2.safe;

import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * Author: Tiger zhang
 * Date:   2016/5/20
 * Email:  Tiger.zhag1993@gmail.com
 * Github: https://github.com/TigerZhag
 */
public class Safeparam{
    private static String ALGORITHM = "AES";
    private static String CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding";
    private static int KEYSIZE = 128;
    public static String key = "";

    public static Key tokey(byte[] key){
        return new SecretKeySpec(key,ALGORITHM);
    }

    public static byte[] generateActiveKey(){
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance(ALGORITHM);
            keyGenerator.init(KEYSIZE);
            SecretKey secretKey = keyGenerator.generateKey();
            return secretKey.getEncoded();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**将二进制转换16进制
     * @param buf
     * @return
     */
    public static String parseByte2HexStr(byte buf[]) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < buf.length; i++) {
            String hex = Integer.toHexString(buf[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex.toUpperCase());
        }
        return sb.toString();
    }

    /**16进制转换为二进制
     * @param hexStr
     * @return
     */
    public static byte[] parseHexStr2Byte(String hexStr) {
        if (hexStr.length() < 1)
            return null;
        byte[] result = new byte[hexStr.length()/2];
        for (int i = 0;i< hexStr.length()/2; i++) {
            int high = Integer.parseInt(hexStr.substring(i*2, i*2+1), 16);
            int low = Integer.parseInt(hexStr.substring(i*2+1, i*2+2), 16);
            result[i] = (byte) (high * 16 + low);
        }

        try {
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());

        } catch (KeyStoreException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String encrypeMsg(String message) {
        Key key = tokey(parseHexStr2Byte(Safeparam.key));
        try {
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE,key);
            return new String(cipher.doFinal(message.getBytes()));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String decryptMsg(String message){
        Key key = tokey(parseHexStr2Byte(Safeparam.key));
        try {
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE,key);
            return new String(cipher.doFinal(message.getBytes()));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        return null;
    }
}
