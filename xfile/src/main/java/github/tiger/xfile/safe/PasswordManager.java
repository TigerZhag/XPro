package github.tiger.xfile.safe;

import java.io.File;

/**
 * Author: Tiger zhang
 * Date:   2016/5/3
 * Email:  Tiger.zhag1993@gmail.com
 * Github: https://github.com/TigerZhag
 *
 * use to manage the key of file and keystore
 */
public class PasswordManager {
    public static String generatekey(){
        return "temp";
    }

    public static void saveKey(){

    }

    public static void deleteKey(){

    }

    public static void encryptFile(File file){

    }

    public static void decrypteFile(File file){

    }

    /**
     *
     * @param key
     * @return true:decrypt successful ; false: decrypt failure
     *
     */
    public static boolean decrypKeyStore(String key){

        return key.equals("4321");
    }

    public static void encryptKeyStore(){

    }
}
