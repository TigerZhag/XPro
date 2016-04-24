package github.tiger.xmsg.safe;

/**
 * Author: Tiger zhang
 * Date:   2016/4/21
 * Email:  Tiger.zhag@gmail.com
 * GitHub: https://github.com/TigerZhag
 */
public class PasswordManager {
    private static String psw = "";


    public static void savePassword(String psw){
        // TODO: 2016/4/21 how to save password
        PasswordManager.psw = psw;
    }

    public static String getPassword(){
        return psw;
    }
}
