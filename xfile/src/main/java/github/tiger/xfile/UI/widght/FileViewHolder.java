package github.tiger.xfile.UI.widght;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import github.tiger.xfile.R;

/**
 * Author: Tiger zhang
 * Date:   2016/5/3
 * Email:  Tiger.zhag1993@gmail.com
 * Github: https://github.com/TigerZhag
 */
public class FileViewHolder extends RecyclerView.ViewHolder{
    public ImageView icon;
    public TextView name;
    public TextView permission;

    public FileViewHolder(View itemView) {
        super(itemView);
        icon = (ImageView) itemView.findViewById(R.id.file_icon);
        name = (TextView) itemView.findViewById(R.id.file_name);
        permission = (TextView) itemView.findViewById(R.id.file_permission);
    }
}
