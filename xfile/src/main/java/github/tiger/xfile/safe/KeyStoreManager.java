package github.tiger.xfile.safe;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
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
    public static String KEYSTORENAME = "key.keystore";
    public static String TESTALIAS = "testAlias";
    public static void createKeyStore(String psw, Context context){
        try {
            //生成一个KeyStore
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null,psw.toCharArray());

            //存储KeyStore
            File file = new File(context.getFilesDir().getAbsolutePath() + "/" + KEYSTORENAME);
            if (!file.exists()) file.createNewFile();
//            SecretKeySpec keySpec = PasswordManager.generatekey(context,psw);
//
//            keyStore.setEntry(TESTALIAS,new KeyStore.SecretKeyEntry(keySpec),new KeyStore.PasswordProtection(psw.toCharArray()));

            Log.d(TAG, "createKeyStore: psw:" + psw.toCharArray());
            keyStore.store(new FileOutputStream(file),psw.toCharArray());
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
            keyStore.load(in,psw.toCharArray());

            in.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static SecretKeySpec getKey(String path,Context context){
        try {
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            FileInputStream in = new FileInputStream(new File(context.getFilesDir().getAbsolutePath() + "/" + KEYSTORENAME));
            keyStore.load(in,MyApp.psw.toCharArray());

            Key key = keyStore.getKey(path, MyApp.psw.toCharArray());
            Log.d(TAG, "checkPsw: key:" + PasswordManager.parseByte2HexStr(key.getEncoded()));

            in.close();
            return (SecretKeySpec) key;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void saveKey(String path, SecretKeySpec keySpec,Context context){
        try {
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null,MyApp.psw.toCharArray());

            keyStore.setEntry(path,new KeyStore.SecretKeyEntry(keySpec),new KeyStore.PasswordProtection(MyApp.psw.toCharArray()));
            FileOutputStream out = new FileOutputStream(context.getFilesDir().getAbsolutePath() + "/" + KEYSTORENAME);
            keyStore.store(out,MyApp.psw.toCharArray());

            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
