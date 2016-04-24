package github.tiger.xmsg.UI;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import github.tiger.xmsg.R;
import github.tiger.xmsg.UI.Base.BaseActivity;
import github.tiger.xmsg.safe.PasswordManager;
import github.tiger.xmsg.widght.circulerProgressButton.CircularProgressButton;

/**
 * Author: Tiger zhang
 * Date:   2016/4/21
 * Email:  Tiger.zhag@gmail.com
 * GitHub: https://github.com/TigerZhag
 */
@ContentView(R.layout.activity_setpassword)
public class SetPasswordActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        ViewUtils.inject(this);
        setPsw.setOnFocusChangeListener(((v, hasFocus) -> {
            if (hasFocus){
                if (setPsw.length() < 6){
                    Toast.makeText(SetPasswordActivity.this, getString(R.string.psw_notenough), Toast.LENGTH_SHORT).show();
                }
            }
        }));
        confirmPsw.setOnFocusChangeListener(((v, hasFocus) -> {
            if (!confirmPsw.getText().equals(setPsw.getText())){
                Toast.makeText(SetPasswordActivity.this, getString(R.string.psw_notcheck), Toast.LENGTH_SHORT).show();
            }
        }));
    }

    @OnClick(R.id.setpassword_ok)
    private void onOk(View view){
        if (!confirmPsw.getText().equals(setPsw.getText())){
            Toast.makeText(SetPasswordActivity.this, getString(R.string.psw_notcheck), Toast.LENGTH_SHORT).show();
            return;
        }
        if (setPsw.length() < 6){
            Toast.makeText(SetPasswordActivity.this, getString(R.string.psw_notenough), Toast.LENGTH_SHORT).show();
            return;
        }
        //保存密码
        PasswordManager.savePassword(setPsw.getText().toString());
        //跳转至主界面

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            Intent var1 = new Intent("android.intent.action.MAIN");
            var1.addFlags(268435456);
            var1.addCategory("android.intent.category.HOME");
            this.startActivity(var1);
        }
        return super.onKeyDown(keyCode, event);
    }

    public static void launch(Activity activity){
        Intent intent = new Intent(activity,SetPasswordActivity.class);
        activity.startActivity(intent);
    }
    @ViewInject(R.id.setpassword_edit) private EditText setPsw;
    @ViewInject(R.id.setpassword_confirm) private EditText confirmPsw;
    @ViewInject(R.id.setpassword_ok) private CircularProgressButton ok;
}
