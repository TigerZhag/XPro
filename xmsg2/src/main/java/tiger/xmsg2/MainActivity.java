package tiger.xmsg2;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.util.Map;

import static junit.framework.Assert.assertEquals;

@ContentView(R.layout.activity_main)
public class MainActivity extends AppCompatActivity {
    private static String BUNDLE_PHONENUMBER = "phoneNumber";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewUtils.inject(this);
        initData();
        initView();

    }

    private void initData() {
        String num = getIntent().getStringExtra(BUNDLE_PHONENUMBER);
        if (num != null && !num.equals("")){
            receiver.setText(num);
        }
    }

    private void initView() {
        send.setEnabled(false);
        receiver.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                send.setEnabled(editable.length() == 11 && content.length() > 0);
            }
        });
        content.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                send.setEnabled(editable.length() > 0 && receiver.length() == 11);
            }
        });
    }

    @OnClick(R.id.send)
    private void sendMsg(View view){
        SMSHelper.sendmessage(receiver.getText().toString(),content.getText().toString(),this);
    }

    public void testPrint() throws Exception {
        // 生成甲方密钥对儿
        Map<String, Object> aKeyMap = DHCoder.initKey();
        String aPublicKey = DHCoder.getPublicKey(aKeyMap);
        String aPrivateKey = DHCoder.getPrivateKey(aKeyMap);

        LogUtils.d("甲方公钥:\r" + aPublicKey);
        LogUtils.d("甲方私钥:\r" + aPrivateKey);

        // 由甲方公钥产生本地密钥对儿
        Map<String, Object> bKeyMap = DHCoder.initKey(aPublicKey);
        String bPublicKey = DHCoder.getPublicKey(bKeyMap);
        String bPrivateKey = DHCoder.getPrivateKey(bKeyMap);

        LogUtils.d("乙方公钥:\r" + bPublicKey);
        LogUtils.d("乙方私钥:\r" + bPrivateKey);

        String aInput = content.getText().toString();
        LogUtils.d("原文: " + aInput);

        // 由甲方公钥，乙方私钥构建密文
        byte[] aCode = DHCoder.encrypt(aInput.getBytes(), aPublicKey,
                bPrivateKey);

        // 由乙方公钥，甲方私钥解密
        byte[] aDecode = DHCoder.decrypt(aCode, bPublicKey, aPrivateKey);
        String aOutput = (new String(aDecode));
        LogUtils.d("密文：" + new String(aCode));
        LogUtils.d("解密: " + aOutput);

        assertEquals(aInput, aOutput);

        LogUtils.d(" ===============反过来加密解密================== ");
        String bInput = "def ";
        LogUtils.d("原文: " + bInput);

        // 由乙方公钥，甲方私钥构建密文
        byte[] bCode = DHCoder.encrypt(bInput.getBytes(), bPublicKey,
                aPrivateKey);

        // 由甲方公钥，乙方私钥解密
        byte[] bDecode = DHCoder.decrypt(bCode, aPublicKey, bPrivateKey);
        String bOutput = (new String(bDecode));

        LogUtils.d("密文：" + new String(bCode));
        LogUtils.d("解密: " + bOutput);

        assertEquals(bInput, bOutput);
    }
    
    @ViewInject(R.id.receiver) private EditText receiver;
    @ViewInject(R.id.content) private EditText content;
    @ViewInject(R.id.send) private ImageButton send;
    @ViewInject(R.id.test) private TextView test;
}
