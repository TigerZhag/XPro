package github.tiger.xfile.safe;

import android.content.Context;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import github.tiger.xfile.constants.Constant;
import github.tiger.xfile.control.MyApp;

/**
 * Author: Tiger zhang
 * Date:   2016/5/3
 * Email:  Tiger.zhag1993@gmail.com
 * Github: https://github.com/TigerZhag
 * <p>
 * use to manage the key of file and keystore
 */
public class PasswordManager {
    public static int keySize = 128;
    public static String Algorithm = "AES";
    private static final String TAG = "PasswordManager";

    //利用Seed生成密码器
    public static SecretKeySpec generatekey(Context context,String psw) {
        try {
            //AES key生产者
            String seed = new String(Base64.encode(psw.getBytes(),Base64.DEFAULT));
            KeyGenerator keyGenerator = KeyGenerator.getInstance(Algorithm);
            keyGenerator.init(keySize, new SecureRandom(seed.getBytes()));
            //生成密钥
            SecretKey key = keyGenerator.generateKey();
            Log.d(TAG, "generatekey: key:" + key);
            //获取基本编码格式的key
            byte[] encodeFormat = key.getEncoded();
            //转化成AES专用密钥
            SecretKeySpec keySpec = new SecretKeySpec(encodeFormat,Algorithm);
            return keySpec;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static void saveKey() {

    }

    public static void deleteKey() {

    }

    public static boolean encryptFile(File file, Key key) {
        Cipher cipher = null;
        try {
            cipher = Cipher.getInstance(Algorithm);
            cipher.init(Cipher.ENCRYPT_MODE,key);
            Log.d(TAG, "encryptFile: key:" + key.getEncoded());
            CipherInputStream inputStream = new CipherInputStream(new FileInputStream(file),cipher);

            File outFile = new File(file.getAbsolutePath() + Constant.FILE_SUFFIX);
            if (!outFile.exists()) outFile.createNewFile();
            Log.d(TAG, "encryptKeyStore: outFilePath:" + outFile.getAbsolutePath());
            FileOutputStream outputStream = new FileOutputStream(outFile);

            byte[] temp = new byte[1024];
            int len = -1;
            while ((len = inputStream.read(temp)) != -1){
                outputStream.write(temp, 0, len);
            }
            outputStream.flush();
            inputStream.close();
            outputStream.close();
            Log.d(TAG, "encryptKeyStore: 加密成功");
            file.delete();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean decrypteFile(File file, Key key) {
        try {
            Cipher cipher = Cipher.getInstance(Algorithm);
            cipher.init(Cipher.DECRYPT_MODE,key);
            Log.d(TAG, "decrypteFile: key:" + key.getEncoded());
            CipherInputStream inputStream = new CipherInputStream(new FileInputStream(file),cipher);

            File outFile = new File(file.getAbsolutePath().replace(Constant.FILE_SUFFIX,""));
            if (!outFile.exists()) outFile.createNewFile();
            Log.d(TAG, "encryptKeyStore: outFilePath:" + outFile.getAbsolutePath());
            FileOutputStream outputStream = new FileOutputStream(outFile);

            byte[] temp = new byte[1024];
            int len = -1;
            while ((len = inputStream.read(temp)) != -1){
                outputStream.write(temp, 0, len);
            }
            outputStream.flush();
            inputStream.close();
            outputStream.close();
            Log.d(TAG, "encryptKeyStore: 解密成功");
            file.delete();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * @return true:decrypt successful ; false: decrypt failure
     */
    public static boolean decrypKeyStore(Context context,String psw) {
        try {
            File file = context.getDatabasePath(Constant.DATABASE_NAME);
            if (!file.exists()) file.createNewFile();
            File encryptedFile = new File(file.getParent() + file.getName() + Constant.FILE_SUFFIX);
            if (!encryptedFile.exists()){
                Log.d(TAG, "decrypKeyStore: 还没有KeyStore");
                return true;
            }
            //通过用户输入的密码获取密码器
            TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            String seed = manager.getDeviceId();// + psw;
            Log.d(TAG, "decrypKeyStore: seed:" + seed);
            Key keySpec = generatekey(context,seed);
            if (keySpec == null) {
                Log.d(TAG, "decrypKeyStore: 密钥生成失败");
                return false;
            }

            String key = parseByte2HexStr(keySpec.getEncoded());
            Log.d(TAG, "encryptKeyStore: key:" + key);

            Cipher cipher = Cipher.getInstance(Algorithm);
            cipher.init(Cipher.DECRYPT_MODE,keySpec);

            //解密文件
            CipherInputStream inputStream = new CipherInputStream(new FileInputStream(encryptedFile), cipher);
            FileOutputStream outputStream = new FileOutputStream(file);
            byte[] temp = new byte[1024];

            //先判断文件头是否合法，不合法则直接返回密码错误
            int len = -1;
            if ((len = inputStream.read(temp)) != -1) {
                //判断文件头
                byte[] header = new byte[16];
                for (int i = 0; i < 16; i++) {
                    header[i] = temp[i];
                }
                //如果文件头不合法，直接返回false
                if (!header.equals(Constant.DB_HEADER.getBytes())) {
                    return false;
                }
                //如果文件头合法，继续解密文件
                outputStream.write(temp,0,len);
            }
            while ((len = inputStream.read(temp)) != -1) {
                outputStream.write(temp,0,len);
            }
            outputStream.flush();
            inputStream.close();
            outputStream.close();

            encryptedFile.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    public static void encryptKeyStore(Context context)  {
        File file = context.getDatabasePath(Constant.DATABASE_NAME);
        try {
            //生成密码器
            TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            String seed = manager.getDeviceId();// + MyApp.psw;
            Log.d(TAG, "encryptKeyStore: seed:" + seed);
            Key keySpec = generatekey(context,seed);
            if (keySpec == null) {
                Log.d(TAG, "encryptKeyStore: 密钥生成失败");
                return;
            }

            String key = parseByte2HexStr(keySpec.getEncoded());
            Log.d(TAG, "encryptKeyStore: key:" + key);

            Cipher cipher = Cipher.getInstance(Algorithm);
            cipher.init(Cipher.ENCRYPT_MODE,keySpec);
            CipherInputStream inputStream = new CipherInputStream(new FileInputStream(file),cipher);

            File outFile = new File(file.getParent() + file.getName() + Constant.FILE_SUFFIX);
            if (!outFile.exists()) outFile.createNewFile();
            Log.d(TAG, "encryptKeyStore: outFilePath:" + outFile.getAbsolutePath());
            FileOutputStream outputStream = new FileOutputStream(outFile);

            byte[] temp = new byte[1024];
            int len = -1;
            while ((len = inputStream.read(temp)) != -1){
                outputStream.write(temp, 0, len);
            }
            outputStream.flush();
            inputStream.close();
            outputStream.close();
            Log.d(TAG, "encryptKeyStore: 加密成功");
            file.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
}
