package github.tiger.xmsg.model;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

import github.tiger.xmsg.R;

/**
 * Author: Tiger zhang
 * Date:   2016/4/25 0025
 * Email:  Tiger.zhag@gmail.com
 * Github: https://github.com/TigerZhag
 */
public class MsgListAdapter extends RecyclerView.Adapter<MsgListViewHolder>{
    private Context context;
    private List<Message> msgs;

    public MsgListAdapter(Context context,List<Message> msgs){
        this.context = context;
        this.msgs = msgs;
    }

    @Override
    public MsgListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MsgListViewHolder(LayoutInflater.from(context).inflate(R.layout.thread_list_item,parent,false));
    }

    @Override
    public void onBindViewHolder(MsgListViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
