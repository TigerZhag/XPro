package github.tiger.xmsg.UI;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Process;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import github.tiger.xmsg.Constant.Constant;
import github.tiger.xmsg.R;
import github.tiger.xmsg.safe.PasswordManager;
import github.tiger.xmsg.widght.circulerProgressButton.CircularProgressButton;

/**
 * Author: Tiger zhang
 * Date:   2016/4/21
 * Email:  Tiger.zhag@gmail.com
 * GitHub: https://github.com/TigerZhag
 */
@ContentView(R.layout.activity_password)
public class PasswordActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewUtils.inject(this);
        login.setEnabled(false);
        login.setIndeterminateProgressMode(true);
        psw.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0){
                    if (!login.isEnabled()) {
                        login.setEnabled(true);
                    }
                }
                else login.setEnabled(false);
            }
        });
        handler = new MyHandler();
    }

    @OnClick(R.id.password_login)
    private void checkPsw(View view){
        login.setProgress(50);
        //检查密码
        if (psw.getText().toString().equals(PasswordManager.getPassword())){
            login.setProgress(100);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    finish();
                    overridePendingTransition(R.anim.left_in,R.anim.right_out);
                }
            },500);
        }else {
            errorTimes += 1;
            if (errorTimes < Constant.PasswordErrorTimes){
                //继续输入，剩余可操作次数减一
                login.setProgress(-1);
                login.setErrorText("还有" + (Constant.PasswordErrorTimes - errorTimes) + "次输入机会");
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        login.setProgress(0);
                    }
                },1000);
            }else {
                //退出程序
                Process.killProcess(Process.myPid());
                System.exit(0);
            }
        }
    }

    public static void launch(Activity activity){
        Intent intent = new Intent(activity,PasswordActivity.class);
        activity.startActivity(intent);
//        activity.overridePendingTransition(R.anim.);
    }

    @ViewInject(R.id.password_edit) private EditText psw;
    @ViewInject(R.id.password_login) private CircularProgressButton login;
    private MyHandler handler;
    private int errorTimes = 0;

    private static class MyHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    }
}
