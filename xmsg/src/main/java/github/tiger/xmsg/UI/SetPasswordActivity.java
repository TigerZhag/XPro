package github.tiger.xmsg.UI;

import android.app.Activity;
import android.content.Intent;

import github.tiger.xmsg.UI.Base.BaseActivity;

/**
 * Author: Tiger zhang
 * Date:   2016/4/21
 * Email:  Tiger.zhag@gmail.com
 * GitHub: https://github.com/TigerZhag
 */
public class SetPasswordActivity extends BaseActivity {

    public static void launch(Activity activity){
        Intent intent = new Intent(activity,SetPasswordActivity.class);
        activity.startActivity(intent);
    }
}
