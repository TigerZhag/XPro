package github.tiger.xfile.safe;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.lang.ref.WeakReference;
import java.security.Key;
import java.security.KeyStore;

import javax.crypto.spec.SecretKeySpec;

import github.tiger.xfile.control.MyApp;

/**
 * Author: Tiger zhang
 * Date:   2016/5/10
 * Email:  Tiger.zhag1993@gmail.com
 * Github: https://github.com/TigerZhag
 */
public class KeyStoreManager {
    public static WeakReference<KeyStore> keyStoreWeakReference;
    public static String KEYSTORENAME = "key.keystore";
    public static String TESTALIAS = "testAlias";
    public static void createKeyStore(String psw, Context context){
        try {
            //生成一个KeyStore
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            Log.d(TAG, "createKeyStore: psw:" + psw.toCharArray());
            keyStore.load(null,psw.toCharArray());

            //存储KeyStore
            File file = new File(context.getFilesDir().getAbsolutePath() + "/" + KEYSTORENAME);
            if (!file.exists()) file.createNewFile();
//            SecretKeySpec keySpec = PasswordManager.generatekey(context,psw);
//
//            keyStore.setEntry(TESTALIAS,new KeyStore.SecretKeyEntry(keySpec),new KeyStore.PasswordProtection(psw.toCharArray()));

            Log.d(TAG, "createKeyStore: psw:" + psw.toCharArray());
            keyStore.store(new FileOutputStream(file),psw.toCharArray());
            keyStoreWeakReference = new WeakReference<KeyStore>(keyStore);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static final String TAG = "KeyStoreManager";
    public static boolean checkPsw(String psw,Context context){
        try {
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            Log.d(TAG, "checkPsw: psw:" + psw.toCharArray());
            FileInputStream in = new FileInputStream(new File(context.getFilesDir().getAbsolutePath() + "/" + KEYSTORENAME));
            Log.d(TAG, "checkPsw: psw:" + psw.toCharArray());
            keyStore.load(in,psw.toCharArray());
            keyStoreWeakReference = new WeakReference<>(keyStore);
            in.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static SecretKeySpec getKey(String path,Context context){
        try {
            KeyStore keyStore;
            if (keyStoreWeakReference == null || keyStoreWeakReference.get() == null) {
                keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            }else {
                keyStore = keyStoreWeakReference.get();
            }
            FileInputStream in = new FileInputStream(new File(context.getFilesDir().getAbsolutePath() + "/" + KEYSTORENAME));
            keyStore.load(in,MyApp.psw.toCharArray());

            Log.d(TAG, "getKey: getPath:" + path);
            Key key = keyStore.getKey(path, MyApp.psw.toCharArray());
            if (key != null) {
                Log.d(TAG, "checkPsw: key:" + PasswordManager.parseByte2HexStr(key.getEncoded()));
            }else {
                Toast.makeText(context,"位于" + path + "处的文件密钥丢失，可能原因是您移动了该文件，请将文件放回原位然后进行解密", Toast.LENGTH_SHORT).show();
            }

            in.close();
            return (SecretKeySpec) key;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void saveKey(String path, SecretKeySpec keySpec,Context context){
        try {
            KeyStore keyStore;
            FileInputStream in = null;
            if (keyStoreWeakReference == null || keyStoreWeakReference.get() == null) {
                keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
                in = new FileInputStream(context.getFilesDir().getAbsolutePath() + "/" + KEYSTORENAME);
                keyStore.load(in,MyApp.psw.toCharArray());
            }else {
                keyStore = keyStoreWeakReference.get();
            }
            Log.d(TAG, "saveKey: savepath：" + path);
            keyStore.setEntry(path,new KeyStore.SecretKeyEntry(keySpec),new KeyStore.PasswordProtection(MyApp.psw.toCharArray()));
            FileOutputStream out = new FileOutputStream(context.getFilesDir().getAbsolutePath() + "/" + KEYSTORENAME);
            keyStore.store(out,MyApp.psw.toCharArray());

            if (in != null) in.close();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
