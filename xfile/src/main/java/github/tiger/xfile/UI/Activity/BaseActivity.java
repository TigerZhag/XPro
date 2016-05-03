package github.tiger.xfile.UI.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import github.tiger.xfile.R;

/**
 * Author: Tiger zhang
 * Date:   2016/5/4
 * Email:  Tiger.zhag1993@gmail.com
 * Github: https://github.com/TigerZhag
 */
public class BaseActivity extends AppCompatActivity {

    /**
     * 退出应用，后台运行
     */
    private long mLastExitTime;
    public void exitApp() {
        if(System.currentTimeMillis() - this.mLastExitTime > 1000L) {
            Toast.makeText(BaseActivity.this, R.string.exit_toast, Toast.LENGTH_SHORT).show();
            this.mLastExitTime = System.currentTimeMillis();
        } else {
            Intent var1 = new Intent("android.intent.action.MAIN");
            var1.addFlags(268435456);
            var1.addCategory("android.intent.category.HOME");
            this.startActivity(var1);
        }
    }
}
