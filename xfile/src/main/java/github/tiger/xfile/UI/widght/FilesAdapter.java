package github.tiger.xfile.UI.widght;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.math.BigInteger;
import java.security.spec.KeySpec;
import java.util.Arrays;
import java.util.List;

import javax.crypto.spec.SecretKeySpec;

import github.tiger.greendao.Key;
import github.tiger.greendao.KeyDao;
import github.tiger.xfile.R;
import github.tiger.xfile.UI.Activity.PasswordActivity;
import github.tiger.xfile.constants.Constant;
import github.tiger.xfile.control.MyApp;
import github.tiger.xfile.event.ChangePathEvent;
import github.tiger.xfile.safe.KeyStoreManager;
import github.tiger.xfile.safe.PasswordManager;

/**
 * Author: Tiger zhang
 * Date:   2016/5/3
 * Email:  Tiger.zhag1993@gmail.com
 * Github: https://github.com/TigerZhag
 */
public class FilesAdapter extends RecyclerView.Adapter<FileViewHolder>{
    private Context context;
    private List<File> files;
    private File parent;

    private static int DIRECTORY = 1;
    private static int FILE = 2;

    private static final String TAG = "FilesAdapter";
    private static String ROOT_PATH = "/storage/emulated";
    public FilesAdapter(Context context,List<File> files){
        this.context = context;
        this.files = files;
        parent = files.get(0).getParentFile();
    }
    @Override
    public int getItemViewType(int position) {
        if (files.get(position).isDirectory()) return DIRECTORY;
        else return FILE;
    }

    @Override
    public FileViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new FileViewHolder(LayoutInflater.from(context).inflate(R.layout.file_item,parent,false));
    }

    private static int WRITE_EXTERNAL_STORAGE_REQUEST_CODE = 0x002;

    @Override
    public void onBindViewHolder(final FileViewHolder holder, int position) {
        //init the UI
        final File file = files.get(position);
        holder.name.setText(file.getName());
        if (file.isDirectory()){
            holder.icon.setImageResource(R.drawable.directory_icon);
            holder.name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //点击文件夹,进入下一层
                    if (!file.canRead()){
                        Toast.makeText(v.getContext(), "您没有权限查看此文件夹", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    parent = file;
                    files = Arrays.asList(file.listFiles());
                    EventBus.getDefault().post(new ChangePathEvent(file.getAbsolutePath()));
                    notifyDataSetChanged();
                }
            });
            holder.name.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(final View v) {
                    //长按文件夹
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setItems(R.array.dir_items, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which){
                                case 0:
                                    //加密所有文件
                                    for (File temp : file.listFiles()) {
                                        if (!temp.canWrite()) {
                                            Toast.makeText(v.getContext(),  "您没有权限查看此文件夹", Toast.LENGTH_SHORT).show();
                                        }
                                        //判断是否是加密文件，不是则加密
                                        if (!temp.getName().endsWith(Constant.FILE_SUFFIX)) {
                                            //密钥种子
                                            TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                                            String seed = manager.getDeviceId() + System.currentTimeMillis() + new MyApp().psw;
                                            SecretKeySpec keySpec = PasswordManager.generatekey(v.getContext(), seed);

                                            if (PasswordManager.encryptFile(temp, keySpec)) {
                                                //加密成功

//                                                files = Arrays.asList(file.getParentFile().listFiles());
//                                                FilesAdapter.this.notifyDataSetChanged();

                                                //保存种子
                                                Log.d(TAG, "onClick: 加密文件夹：success");
                                                KeyStoreManager.saveKey(temp.getAbsolutePath() + Constant.FILE_SUFFIX, keySpec, v.getContext());
                                            } else {
                                                //加密失败
                                                Toast.makeText(v.getContext(), R.string.encryptedFailure, Toast.LENGTH_SHORT).show();
                                                return;
                                            }
                                        }
                                    }
                                    Toast.makeText(v.getContext(), R.string.encryptedSuccess, Toast.LENGTH_SHORT).show();
                                    break;
                                case 1:
                                    //解密所有文件
                                    //判断是否是加密文件，是则解密
                                    for (File temp1 : file.listFiles()) {
                                        if (!temp1.canWrite()) continue;
                                        //判断是否是加密文件，是则解密
                                        if (temp1.getName().endsWith(Constant.FILE_SUFFIX)) {
                                            SecretKeySpec keySpec = KeyStoreManager.getKey(temp1.getAbsolutePath(), v.getContext());//new SecretKeySpec(encoded,"AES");
                                            if (PasswordManager.decrypteFile(temp1, keySpec)) {
                                                //解密成功
//                                               Toast.makeText(v.getContext(), R.string.decryptedSuccess, Toast.LENGTH_SHORT).show();
//                                               files = Arrays.asList(temp.getParentFile().listFiles());
//                                               FilesAdapter.this.notifyDataSetChanged();
                                            } else {
                                                //解密失败
                                                Toast.makeText(v.getContext(), R.string.decryptedFailure, Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    }
                                    break;
                            }
                        }
                    });
                    builder.show();
                    return true;
                }
            });
        }else {
            holder.icon.setImageResource(R.drawable.file_icon);
            holder.name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    if (!file.canWrite()) file.setWritable(true);
                    //判断是否是加密文件，是则弹出解密
                    if (file.getName().endsWith(Constant.FILE_SUFFIX)) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setItems(R.array.en_file_items, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case 0:
                                        SecretKeySpec keySpec = KeyStoreManager.getKey(file.getAbsolutePath(), v.getContext());//new SecretKeySpec(encoded,"AES");
                                        if (PasswordManager.decrypteFile(file, keySpec)) {
                                            //解密成功
                                            Toast.makeText(v.getContext(), R.string.decryptedSuccess, Toast.LENGTH_SHORT).show();
                                            files = Arrays.asList(file.getParentFile().listFiles());
                                            FilesAdapter.this.notifyDataSetChanged();
                                        } else {
                                            //解密失败
                                            Toast.makeText(v.getContext(), R.string.decryptedFailure, Toast.LENGTH_SHORT).show();
                                        }
                                        break;
                                }
                            }
                        });
                        builder.create().show();
                    } else {
                        //不是则弹出加密
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setItems(R.array.file_items, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case 0:
                                        //进度

                                        //密钥种子
                                        if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                                != PackageManager.PERMISSION_GRANTED) {
                                            //申请WRITE_EXTERNAL_STORAGE权限
                                            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_PHONE_STATE},
                                                    WRITE_EXTERNAL_STORAGE_REQUEST_CODE);
                                        }
                                        TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

                                        String seed = manager.getDeviceId() + System.currentTimeMillis() + new MyApp().psw;
                                        SecretKeySpec keySpec = PasswordManager.generatekey(v.getContext(), seed);

                                        if (PasswordManager.encryptFile(file, keySpec)) {
                                            //加密成功
                                            Toast.makeText(v.getContext(), R.string.encryptedSuccess, Toast.LENGTH_SHORT).show();
                                            files = Arrays.asList(file.getParentFile().listFiles());
                                            FilesAdapter.this.notifyDataSetChanged();

                                            //保存种子
                                            KeyStoreManager.saveKey(file.getAbsolutePath() + Constant.FILE_SUFFIX, keySpec, v.getContext());
                                        } else {
                                            //加密失败
                                            Toast.makeText(v.getContext(), R.string.encryptedFailure, Toast.LENGTH_SHORT).show();
                                        }
                                        break;
                                }
                            }
                        });
                        builder.create().show();
                    }
                }
            });
        }
    }

    public boolean backToParent(){
        if (parent.getParentFile() != null) {
            parent = parent.getParentFile();
            EventBus.getDefault().post(new ChangePathEvent(parent.getAbsolutePath()));
            if (!parent.canRead()){
                Toast.makeText(context, "无权查看上级目录，退出应用", Toast.LENGTH_SHORT).show();
                return false;
            }
            files = Arrays.asList(parent.listFiles());
            notifyDataSetChanged();
            return true;
        }else {
            return false;
        }
    }

    @Override
    public int getItemCount() {
        return files == null ? 0 : files.size();
    }
}
