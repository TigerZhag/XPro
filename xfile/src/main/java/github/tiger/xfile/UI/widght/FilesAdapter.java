package github.tiger.xfile.UI.widght;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
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
        Log.d(TAG, "path:" + files.get(0).getAbsolutePath());
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

    @Override
    public void onBindViewHolder(FileViewHolder holder, int position) {
        //init the UI
        File file = files.get(position);
        holder.name.setText(file.getName());
        if (file.isDirectory()){
            holder.icon.setImageResource(R.drawable.directory_icon);
            holder.name.setOnClickListener(v -> {
                //点击文件夹,进入下一层
                if (!file.canRead()){
                    Toast.makeText(v.getContext(), "您没有权限查看此文件夹", Toast.LENGTH_SHORT).show();
                    return;
                }
                parent = file;
                files = Arrays.asList(file.listFiles());
                EventBus.getDefault().post(new ChangePathEvent(file.getAbsolutePath()));
                notifyDataSetChanged();
            });
            holder.name.setOnLongClickListener(v -> {
                //长按文件夹
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setItems(R.array.file_items, (dialog, which) -> {
                    switch (which){
                        case 0:
                            //随机生成密钥

                            // 保存密钥至数据库
                            // 加密

                            //
                            break;
                    }
                });
                builder.show();
                return true;
            });
        }else {
            holder.icon.setImageResource(R.drawable.file_icon);
            holder.name.setOnClickListener(v -> {
                //判断是否是加密文件，是则弹出解密
                if (file.getName().endsWith(Constant.FILE_SUFFIX)){
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setItems(R.array.en_file_items, (dialog, which) -> {
                        switch (which) {
                            case 0:
                                //进度

                                //检索种子
                                KeyDao keyDao = MyApp.getDaoSession().getKeyDao();
                                Key key = keyDao.queryBuilder().where(KeyDao.Properties.Path.eq(file.getAbsolutePath())).unique();
                                long id = key.getId();
                                String encodeKey = key.getKey();
                                Log.d(TAG, "onBindViewHolder: 揭秘时的key:" + encodeKey);
                                byte[] encoded = PasswordManager.parseHexStr2Byte(encodeKey);
                                Log.d(TAG, "onBindViewHolder: keyLength;" + encoded.length);
                                //解密
                                SecretKeySpec keySpec = new SecretKeySpec(encoded,"AES");
                                if (PasswordManager.decrypteFile(file,keySpec)){
                                    //解密成功
                                    Toast.makeText(v.getContext(), R.string.decryptedSuccess, Toast.LENGTH_SHORT).show();
                                    files = Arrays.asList(file.getParentFile().listFiles());
                                    notifyDataSetChanged();

                                    //删除种子
                                    keyDao.deleteByKey(id);
                                }else {
                                    //解密失败
                                    Toast.makeText(v.getContext(), R.string.decryptedFailure, Toast.LENGTH_SHORT).show();
                                }
                                break;
                        }
                    });
                    builder.create().show();
                }else {
                    //不是则弹出加密
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setItems(R.array.file_items, (dialog, which) -> {
                        switch (which) {
                            case 0:
                                //进度

                                //密钥种子
                                TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                                String seed = manager.getDeviceId() + System.currentTimeMillis() + new MyApp().psw;
                                SecretKeySpec keySpec = PasswordManager.generatekey(v.getContext(),seed);

                                if (PasswordManager.encryptFile(file,keySpec)){
                                    //加密成功
                                    Toast.makeText(v.getContext(), R.string.encryptedSuccess, Toast.LENGTH_SHORT).show();
                                    files = Arrays.asList(file.getParentFile().listFiles());
                                    notifyDataSetChanged();

                                    //保存种子
                                    KeyDao keyDao = MyApp.getDaoSession().getKeyDao();
                                    String encodeKey = PasswordManager.parseByte2HexStr(keySpec.getEncoded());
                                    Key key = new Key(keyDao.count(),file.getAbsolutePath() + Constant.FILE_SUFFIX, encodeKey);
                                    Log.d(TAG, "onBindViewHolder: encodeKey:" + encodeKey);
                                    keyDao.insert(key);
                                }else {
                                    //加密失败
                                    Toast.makeText(v.getContext(), R.string.encryptedFailure, Toast.LENGTH_SHORT).show();

                                }
                                break;
                        }
                    });
                    builder.create().show();
                }
            });
        }
    }

    public boolean backToParent(){
        if (parent.getParentFile() != null) {
            parent = parent.getParentFile();
            EventBus.getDefault().post(new ChangePathEvent(parent.getAbsolutePath()));
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
