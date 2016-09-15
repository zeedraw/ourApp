package com.example.administrator.ourapp;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by Administrator on 2016/9/8.
 */
public class ListenerManager {
    /**
     * 单例模式
     */
    public static ListenerManager listenerManager;

    /**
     * 注册的接口集合，发送广播的时候都能收到
     */
   // private List<IListener> iListenerList = new CopyOnWriteArrayList<IListener>();
    Map<String,IListener> iListenerMap=new HashMap<String,IListener>();

    /**
     * 获得单例对象
     */
    public static ListenerManager getInstance()
    {
        if(listenerManager == null)
        {
            listenerManager = new ListenerManager();
        }
        return listenerManager;
    }

    /**
     * 注册监听
     */
    public void registerListtener(String tag,IListener iListener)
    {
        //iListenerList.add(iListener);
        iListenerMap.put(tag,iListener);
    }

    /**
     * 注销监听
     */
//    public void unRegisterListener(IListener iListener)
//    {
//        if(iListenerList.contains(iListener))
//        {
//            iListenerList.remove(iListener);
//        }
//    }

    /**
     * 发送广播
     */
    public void sendBroadCast(String tag[])
    {
            for (int i=0;i<tag.length;i++)
            {
                iListenerMap.get(tag[i]).upData();
            }
    }
}
