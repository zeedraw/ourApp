package com.example.administrator.ourapp.imageloader;

import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.net.URL;
import java.util.HashMap;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.example.administrator.ourapp.R;


/**
 * 异步加载图片类，内部有缓存，可以通过后台线程获取网络图片。首先生成一个实例，并调用loadDrawableByTag方法来获取一个Drawable对象
 */
public class AsyncImageLoader {

    /**
     * 使用软引用SoftReference，可以由系统在恰当的时候更容易的回收
     */
    private HashMap<String, SoftReference<Drawable>> imageCache;
    private Context mContext;

    public AsyncImageLoader(Context context){
        mContext=context;
        imageCache = new HashMap<String, SoftReference<Drawable>>();
    }




    /**
     * 通过传入的TagInfo来获取一个网络上的图片
     * @param tag TagInfo对象，保存了position、url和一个待获取的Drawable对象
     * @param callback ImageCallBack对象，用于在获取到图片后供调用侧进行下一步的处理
     * @return drawable 从网络或缓存中得到的Drawable对象，可为null，调用侧需判断
     */
    public Drawable loadDrawableByTag(final TagInfo tag, final ImageCallBack callback){
        Drawable drawable;

        /**
         * 先在缓存中找，如果通过URL地址可以找到，则直接返回该对象
         */
        if(imageCache.containsKey(tag.getUrl())){
            drawable = imageCache.get(tag.getUrl()).get();
            if(null!=drawable){
                return drawable;
            }
        }



            /**
             * 用于在获取到网络图片后，保存图片到缓存，并触发调用侧的处理
             */
            final Handler handler = new Handler() {

                @Override
                public void handleMessage(Message msg) {

                    TagInfo info = (TagInfo) msg.obj;
                    imageCache.put(info.url, new SoftReference<Drawable>(info.drawable));
                    callback.obtainImage(info);

                    super.handleMessage(msg);
                }

            };


            /**
             * 如果在缓存中没有找到，则开启一个线程来进行网络请求
             */
            new Thread(new Runnable() {

                @Override
                public void run() {

                    TagInfo info = getDrawableIntoTag(tag);
                    Message msg = new Message();
                    msg.what = 0;
                    msg.obj = info;
                    handler.sendMessage(msg);
                }
            }).start();

            return null;

    }



    /**
     * 通过传入的TagInfo对象，利用其URL属性，到网络请求图片，获取到图片后保存在TagInfo的Drawable属性中，并返回该TagInfo
     * @param info TagInfo对象，需要利用里面的url属性
     * @return TagInfo 传入的TagInfo对象，增加了Drawable属性后返回
     */
    public TagInfo getDrawableIntoTag(TagInfo info){
        URL request;
        InputStream input;
        Drawable drawable = null;

        try{
            request = new URL(info.getUrl());
            input = (InputStream)request.getContent();
            drawable = Drawable.createFromStream(input, "src"); // 第二个属性可为空，为DEBUG下使用，网上的说明
        }
        catch(Exception e){
            e.printStackTrace();
            drawable=mContext.getResources().getDrawable(R.drawable.personimg);

        }

        info.drawable = drawable;
        return info;
    }



    /**
     * 获取图片的回调接口，里面的obtainImage方法在获取到图片后进行调用
     */
    public interface ImageCallBack{
        /**
         * 获取到图片后在调用侧执行具体的细节
         * @param info TagInfo对象，传入的info经过处理，增加Drawable属性，并返回给传入者
         */
        public void obtainImage(TagInfo info);
    }



}

