package github.tiger;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class AppDefender {
    public static void main(String[] args){
        String origin = "SQLite format 3.";
        System.out.println(origin.getBytes().length);
        byte[] key = generatekey();
        System.out.println("key: "+ key);
        System.out.println("origin: " + origin);
        String secret = null;
        try {
            secret = parseByte2HexStr(encrypt(key,origin.getBytes("GBK")));
            System.out.println("encrypted: " + secret);
            String clear = new String(decrypt(key,parseHexStr2Byte(secret)),"GBK");
            System.out.println("decrypted: "+ clear);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }

    private static int keySize = 128;
    private static String Algorithm = "AES";
    private static final String TAG = "PasswordManager";
    public static byte[] generatekey(){
        try {
            //AES key生产�?
            KeyGenerator keyGenerator = KeyGenerator.getInstance(Algorithm);
            //利用用户密码初始化key生产�?
            keyGenerator.init(keySize, new SecureRandom("4321".getBytes()));
            //生成密钥
            SecretKey key = keyGenerator.generateKey();
            //返回基本编码格式的密钥，如果不支持编码，返回null
            byte[] encodeFormat = key.getEncoded();
            //转换成AES专用密钥
//            SecretKeySpec keySpec = new SecretKeySpec(encodeFormat,Algorithm);
            return encodeFormat;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static void saveKey(){

    }

    public static void deleteKey(){

    }

    private static byte[] encrypt(byte[] key,byte[] content){
        try {
            SecretKeySpec keySpec = new SecretKeySpec(key,"AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE,keySpec);
            return cipher.doFinal(content);
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

    private static byte[] decrypt(byte[] key,byte[] content){
        try {
            SecretKeySpec keySpec = new SecretKeySpec(key,"AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE,keySpec);
            return cipher.doFinal(content);
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
        return result;
    }
}
