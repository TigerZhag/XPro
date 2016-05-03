package github.tiger.xfile.UI.widght;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import github.tiger.xfile.R;
import github.tiger.xfile.UI.Activity.PasswordActivity;
import github.tiger.xfile.constants.Constant;
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

    private static int DIRECTORY = 1;
    private static int FILE = 2;

    private static final String TAG = "FilesAdapter";
    private static String ROOT_PATH = "/storage/emulated";
    public FilesAdapter(Context context,List<File> files){
        this.context = context;
        this.files = files;
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
                                PasswordManager.decrypteFile(file);
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
                                PasswordManager.encryptFile(file);
                                break;
                        }
                    });
                    builder.create().show();
                }
            });
        }
    }

    public boolean backToParent(){
        if (!files.get(0).getParent().equals("/")) {
            File file = files.get(0).getParentFile().getParentFile();
            EventBus.getDefault().post(new ChangePathEvent(file.getAbsolutePath()));
            files = Arrays.asList(file.listFiles());
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
