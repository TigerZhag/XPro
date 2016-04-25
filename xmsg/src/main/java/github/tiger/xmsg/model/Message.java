package github.tiger.xmsg.model;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;

/**
 * Author: Tiger zhang
 * Date:   2016/4/25 0025
 * Email:  Tiger.zhag@gmail.com
 * Github: https://github.com/TigerZhag
 */

/**
 * id 标示它的唯一性
 * thread_id: 同一个会话中他们的thread_id是一样的，也就是说通过thread_id就可以知道A与B在聊天还是 A与C在聊天
 * person：发件人，返回一个数字就是联系人列表里的序号，陌生人为null
 * date :这条消息发送或接收的时间
 * read:  0 表示未读 1表示已读
 * protocol：协议 0SMS_PROTO, 1 MMS_PROTO
 * status：状态 -1接收，0完成，64等待，128失败
 * type : 1表示接收 2表示发出
 *  body  表示消息的内容
 * service_center短信服务中心号码编号
 */
public class Message {
    public String address;
    public int intPerson;
    public long date;
    public int readType;
    public int sendType;

    //type == 0xff 代表是加密过的短信，否则是普通短信
    public int bodyType;
    public String bodyBody;

    public void getTypeAndBody(String msgBody){
        // TODO: 2016/4/25 0025 need to complete the get method
        bodyType = 1;
        bodyBody = msgBody;
    }
}
