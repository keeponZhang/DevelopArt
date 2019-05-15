package com.ryg.chapter_2.serializable;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
//https://blog.csdn.net/fengwen2011/article/details/51115549
class SessionDTO implements Serializable {
    private static final long serialVersionUID = 1L;  
    private transient int data; // Stores session data  
    //在两个方法的开始处，你会发现调用了defaultWriteObject()和defaultReadObject()。
    // 它们做的是默认的序列化进程，就像写/读所有的non-transient和 non-static字段(但他们不会去做serialVersionUID的检查).
    // 通常说来，所有我们想要自己处理的字段都应该声明为transient。这样的话，defaultWriteObject/defaultReadObject便可以专注于其余字段，
    // 而我们则可为这些特定的字段(译者：指transient)定制序列化。使用那两个默认的方法并不是强制的，而是给予了处理复杂应用时更多的灵活性。
    //Session activation time (creation, deserialization)  
    private transient long activationTime;   
  
    public SessionDTO(int data) {  
        this.data = data;  
        this.activationTime = System.currentTimeMillis();  
    }  
  
    private void writeObject(ObjectOutputStream oos) throws IOException {
        oos.defaultWriteObject();  
        oos.writeInt(data);  
        System.out.println("session serialized");  
    }  
  
    private void readObject(ObjectInputStream ois) throws IOException,
            ClassNotFoundException {  
        ois.defaultReadObject();  
        data = ois.readInt();  
        activationTime = System.currentTimeMillis();  
        System.out.println("session deserialized");  
    }  
  
    public int getData() {  
        return data;  
    }  
  
    public long getActivationTime() {  
        return activationTime;  
    }  
}