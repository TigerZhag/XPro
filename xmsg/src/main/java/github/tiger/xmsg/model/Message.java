package github.tiger.xmsg.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.nio.ByteBuffer;

/**
 * Author: Tiger zhang
 * Date:   2016/4/21 0021
 * Email:  Tiger.zhag@gmail.com
 *
 * 封装的Message类
 */

public class Message implements Parcelable{
    public int type;
    public String from;
    public String content;

    // 1.必须实现Parcelable.Creator接口,否则在获取Person数据的时候，会报错，如下：
    // android.os.BadParcelableException:
    // Parcelable protocol requires a Parcelable.Creator object called  CREATOR on class com.um.demo.Person
    // 2.这个接口实现了从Percel容器读取Person数据，并返回Person对象给逻辑层使用
    // 3.实现Parcelable.Creator接口对象名必须为CREATOR，不如同样会报错上面所提到的错；
    // 4.在读取Parcel容器里的数据事，必须按成员变量声明的顺序读取数据，不然会出现获取数据出错
    // 5.反序列化对象
    public static final Parcelable.Creator<Message> CREATOR = new Creator(){
        @Override
        public Message createFromParcel(Parcel source) {
            // TODO Auto-generated method stub
            // 必须按成员变量声明的顺序读取数据，不然会出现获取数据出错
            Message message= new Message();
            message.type = source.readInt();
            message.from = source.readString();
            message.content = source.readString();
            return message;
        }
        @Override
        public Message[] newArray(int size) {
            // TODO Auto-generated method stub
            return new Message[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(type);
        parcel.writeString(from);
        parcel.writeString(content);
    }
}
