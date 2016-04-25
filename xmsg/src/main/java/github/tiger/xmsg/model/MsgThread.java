package github.tiger.xmsg.model;

/**
 * Author: Tiger zhang
 * Date:   2016/4/25 0025
 * Email:  Tiger.zhag@gmail.com
 * Github: https://github.com/TigerZhag
 */

/**
 * id标示它的唯一性
 * date:表示最后接收或者发送消息的时间
 * message_count:表示发送消息的数量，这里我接收到了一条消息也回复了一条消息那么它的数量就为2
 * recipient_ids:联系人ID，指向表canonical_addresses里的id。
 * snippet :最后收到或者发送的消息内容，就是上图body中存的东西
 */
public class MsgThread {
    public long id;
    public long date;
    public int msg_count;
    public int recipient_id;
    public String snippet;


}