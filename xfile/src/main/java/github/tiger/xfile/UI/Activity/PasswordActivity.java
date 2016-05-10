package github.tiger.xfile.UI.Activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import github.tiger.xfile.R;
import github.tiger.xfile.constants.BundleFlag;
import github.tiger.xfile.control.MyApp;
import github.tiger.xfile.safe.KeyStoreManager;
import github.tiger.xfile.safe.PasswordManager;

/**
 * Author: Tiger zhang
 * Date:   2016/5/3
 * Email:  Tiger.zhag1993@gmail.com
 * Github: https://github.com/TigerZhag
 */
public class PasswordActivity extends BaseActivity implements View.OnClickListener {

    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);
        if (isFirstRun())
            Toast.makeText(PasswordActivity.this, R.string.hasnot_psw, Toast.LENGTH_SHORT).show();
        initView();
    }

    private boolean isFirstRun() {
        SharedPreferences sp = getSharedPreferences(BundleFlag.SP_NAME,MODE_PRIVATE);
        return !sp.getBoolean(BundleFlag.HAS_PASSWORD,false);
    }

    private void initView() {
        text_0 = (Button) findViewById(R.id.psw_0);
        text_1 = (Button) findViewById(R.id.psw_1);
        text_2 = (Button) findViewById(R.id.psw_2);
        text_3 = (Button) findViewById(R.id.psw_3);
        text_4 = (Button) findViewById(R.id.psw_4);
        text_5 = (Button) findViewById(R.id.psw_5);
        text_6 = (Button) findViewById(R.id.psw_6);
        text_7 = (Button) findViewById(R.id.psw_7);
        text_8= (Button) findViewById(R.id.psw_8);
        text_9 = (Button) findViewById(R.id.psw_9);
        delete = (Button) findViewById(R.id.psw_delete);

        text_0.setOnClickListener(this);
        text_1.setOnClickListener(this);
        text_2.setOnClickListener(this);
        text_3.setOnClickListener(this);
        text_4.setOnClickListener(this);
        text_5.setOnClickListener(this);
        text_6.setOnClickListener(this);
        text_7.setOnClickListener(this);
        text_8.setOnClickListener(this);
        text_9.setOnClickListener(this);
        delete.setOnClickListener(this);
    }

    private StringBuilder password = new StringBuilder();
    private Button text_1;
    private Button text_2;
    private Button text_3;
    private Button text_4;
    private Button text_5;
    private Button text_6;
    private Button text_7;
    private Button text_8;
    private Button text_9;
    private Button text_0;
    private Button delete;

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.psw_delete){
            if (password.length() > 0) {
                password.deleteCharAt(password.length() - 1);
                RadioButton button = (RadioButton) findViewById(R.id.psw_a + password.length());
                button.setChecked(false);
            }
            return;
        }
        password.append(((TextView)v).getText());
        if (password.length() <= 3){
            RadioButton button = (RadioButton) findViewById(R.id.psw_a + password.length() - 1);
            button.setChecked(true);
        }else {
            if (isFirstRun()){
                Toast.makeText(PasswordActivity.this, R.string.set_psw_sucess, Toast.LENGTH_SHORT).show();
                SharedPreferences sharedPreferences = getSharedPreferences(BundleFlag.SP_NAME,MODE_PRIVATE);
                sharedPreferences.edit().putBoolean(BundleFlag.HAS_PASSWORD,true).commit();
                KeyStoreManager.createKeyStore(password.toString(),this);
                MainActivity.launch(this);
            }else {
                //校验密码
                MyApp.psw = password.toString();
                if (KeyStoreManager.checkPsw(password.toString(),this)){//PasswordManager.decrypKeyStore(this,password.toString())){
                    //检验成功，进入主界面
                    MainActivity.launch(this);
                }else {
                    //检验失败，重新输入密码
                    Toast.makeText(PasswordActivity.this, R.string.psw_wrong, Toast.LENGTH_SHORT).show();
                    for (int i = 0; i < password.length() - 1; i++) {
                        RadioButton button = (RadioButton) findViewById(R.id.psw_a + i);
                        button.setChecked(false);
                    }
                    password.delete(0,password.length());
                }
            }
        }
    }

    public static void launch(Activity activity) {
        Intent intent = new Intent(activity,PasswordActivity.class);
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.left_in,R.anim.right_out);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
