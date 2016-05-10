package github.tiger.xfile.UI.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
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
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(v -> {
                Snackbar.make(v, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
        });

        initData();
    }

    private void initData() {
        File file = Environment.getExternalStorageDirectory();
        toolbar.setTitle(file.getAbsolutePath());
        adapter = new FilesAdapter(this, Arrays.asList(file.listFiles()));

        files.setAdapter(adapter);
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
