package com.example.administrator.ourapp.friends;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.administrator.ourapp.IListener;
import com.example.administrator.ourapp.ListenerManager;
import com.example.administrator.ourapp.MyUser;
import com.example.administrator.ourapp.R;
import com.example.administrator.ourapp.friends.friends_adapter;
import com.example.administrator.ourapp.user_information.MyAccount;
import com.example.administrator.ourapp.user_information.other_information;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by Administrator on 2016/8/23.
 */
public class FreFrag extends Fragment implements IListener {
    private ListView listView;
    private friends_adapter fre_adapter;
    private List<MyUser> query_list;

    private Vector<String> introduction = new Vector<String>();       //用户的个人介绍
    private Vector<String> username = new Vector<String>();    //用户名
    private Vector<String> user_ID = new Vector<String>();//用户的objectId
    final private boolean IS_FRIEND = true; //表明从本页面点击跳转到的用户的界面都是自己的好友界面
//    final Context context = this.getBaseContext();
    //TODO 添加刷新机制
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ListenerManager.getInstance().registerListtener("FreFrag",this);
        View rootview=inflater.inflate(R.layout.fre_frag, container, false);
        listView=(ListView)rootview.findViewById(R.id.mes_listview);
        String User_ID = BmobUser.getCurrentUser(MyUser.class).getObjectId();
        Load_User_Friends(User_ID);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                if(user_ID.get(i).equals(BmobUser.getCurrentUser(MyUser.class).getObjectId())){
                    //如果点击的用户ID是用户本身则跳转到用户的个人信息界面
                    Intent intent = new Intent(getActivity(), MyAccount.class);
                    startActivity(intent);
                }//if
                //若选中的用户的ID不是本人则跳转到好友信息界面（无添加好友接口，但有删除好友接口）
                else{
                    Intent intent = new Intent(getActivity(), other_information.class);
                    // 在Intent中传递数据
                    intent.putExtra("other_ID", user_ID.get(i));
                    intent.putExtra("IS_FRIEND", IS_FRIEND);
                    // 启动Intent
                    startActivity(intent);
                }//else
            }//onItemClick
        });
        return rootview;

    }

    @Override
    public void upData() {

    }

    private void Load_User_Friends(final String User_ID){

        BmobQuery<MyUser> query = new BmobQuery<MyUser>();
        MyUser friends = new MyUser();
        friends.setObjectId(User_ID);
        query.addWhereRelatedTo("friends", new BmobPointer(friends));
        query.findObjects(new FindListener<MyUser>() {

            @Override
            public void done(List<MyUser> object,BmobException e) {
                if(e==null){
                    if (object.size() != 0){
                        for (MyUser user : object) {
                            username.add(user.getUsername());
                            Log.i("z","查询用户名");
                            introduction.add(user.getIntroduction());
                            Log.i("z","查询个人介绍");
                            user_ID.add(user.getObjectId());
                            Log.i("z","查询用户ID");
                        }//for

                        query_list = new ArrayList<MyUser>(object);
                        fre_adapter = new friends_adapter(getContext(), R.layout.friend_lv_item, query_list);
                        listView.setAdapter(fre_adapter);
                    }//if object.size() != 0
                    Log.i("bmob","查询个数："+object.size());
                }else{
                    Log.i("bmob","失败："+e.getMessage());
                }
            }

        });


    }//Load_User_Friends
}
