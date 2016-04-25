package github.tiger.xmsg.UI;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import github.tiger.xmsg.R;
import github.tiger.xmsg.UI.Base.BaseActivity;
import github.tiger.xmsg.database.MessageHelper;
import github.tiger.xmsg.model.Message;
import github.tiger.xmsg.model.MsgListAdapter;

/**
 * Author: Tiger zhang
 * Date:   2016/4/21
 * Email:  Tiger.zhag@gmail.com
 * GitHub: https://github.com/TigerZhag
 */

//An project for XingZiYang to graduate
@ContentView(R.layout.activity_main)
public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewUtils.inject(this);
        EventBus.getDefault().register(this);
        initDataAndView();
    }

    public void onEvent(List<Message> msgs){
        this.msgs.addAll(msgs);
        adapter.notifyItemRangeInserted(0,msgs.size());
    }

    private void initDataAndView() {
        //获取所有短信
        MessageHelper.getMsgs(this);
        //初始化组件
        msgs = new ArrayList<>();
        adapter = new MsgListAdapter(this,msgs);
        msg_list.setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @ViewInject(R.id.msg_lsit) private RecyclerView msg_list;
    private MsgListAdapter adapter;
    private List<Message> msgs;
}
