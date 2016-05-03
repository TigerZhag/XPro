package tiger.xmsg2.event;

/**
 * Author: Tiger zhang
 * Date:   2016/5/4
 * Email:  Tiger.zhag1993@gmail.com
 * Github: https://github.com/TigerZhag
 */
public class SendMessageResultEvent {
    public SendMessageResultEvent(boolean result){
        this.result = result;
    }
    public boolean result;
}
