package github.tiger.xmsg.UI;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import github.tiger.xmsg.Constant.BundleFlag;
import github.tiger.xmsg.R;

/**
 * Author: Tiger zhang
 * Date:   2016/4/21 0021
 * Email:  Tiger.zhag@gmail.com
 */
public class PasswordActivity extends Activity {

    public static void launch(Activity activity){
        Intent intent = new Intent(activity,PasswordActivity.class);
        activity.startActivityForResult(intent, BundleFlag.REQUESTPASSWORD);
//        activity.overridePendingTransition(R.anim.);
    }
}
