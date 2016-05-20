package github.tiger.xfile.UI.Activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.File;
import java.io.InterruptedIOException;
import java.util.Arrays;

import github.tiger.xfile.R;
import github.tiger.xfile.UI.widght.FilesAdapter;
import github.tiger.xfile.event.ChangePathEvent;
import github.tiger.xfile.safe.PasswordManager;

public class MainActivity extends BaseActivity {

    private Toolbar toolbar;
    private RecyclerView files;
    private FilesAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);

        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        files = (RecyclerView) findViewById(R.id.files);
        files.setLayoutManager(new LinearLayoutManager(this));

        initData();
    }

    private static final String TAG = "MainActivity";
    private void initData() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            //申请WRITE_EXTERNAL_STORAGE权限
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    WRITE_EXTERNAL_STORAGE_REQUEST_CODE);
        }else {
            File file = Environment.getExternalStorageDirectory();
            toolbar.setTitle(file.getAbsolutePath());
            Log.d(TAG, "initData: filelength:" + file.listFiles().length);
            adapter = new FilesAdapter(this, Arrays.asList(file.listFiles()));

            files.setAdapter(adapter);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            //申请WRITE_EXTERNAL_STORAGE权限
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    READ_PHONE_STATE_REQUEST_CODE);
        }
    }
    private static int READ_PHONE_STATE_REQUEST_CODE = 0x002;
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        doNext(requestCode,grantResults);
    }
    private static int WRITE_EXTERNAL_STORAGE_REQUEST_CODE = 0x001;
    private void doNext(int requestCode, int[] grantResults) {
        if (requestCode == WRITE_EXTERNAL_STORAGE_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission Granted
                File file = Environment.getExternalStorageDirectory();
                toolbar.setTitle(file.getAbsolutePath());
                Log.d(TAG, "initData: filelength:" + file.listFiles().length);
                adapter = new FilesAdapter(this, Arrays.asList(file.listFiles()));

                files.setAdapter(adapter);
            } else {
                // Permission Denied
                Toast.makeText(MainActivity.this, "没有查看文件的权限", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Subscribe
    public void onEvent(ChangePathEvent event){
        toolbar.setTitle(event.path);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            if (adapter.backToParent()){
                //返回上一级目录
            }else {
                finish();
            }
        }
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);

//        PasswordManager.encryptKeyStore(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
//        PasswordActivity.launch(this);
    }

    public static void launch(Activity activity){
        Intent intent = new Intent(activity,MainActivity.class);
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.right_in,R.anim.left_out);
        activity.finish();
    }
}
