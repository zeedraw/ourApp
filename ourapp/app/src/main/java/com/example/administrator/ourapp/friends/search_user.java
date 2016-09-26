package com.example.administrator.ourapp.friends;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.ourapp.MyUser;
import com.example.administrator.ourapp.R;
import com.example.administrator.ourapp.user_information.MyAccount;
import com.example.administrator.ourapp.user_information.other_information;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by Longze on 2016/9/22.
 * 搜索用户界面
 */
public class search_user extends Activity {
    private TextView return_bt,commit_bt;//标题上的左右按钮
    private TextView info_title;//标题
    private EditText search_bar;

    private ListView listView;
    private List<MyUser> query_list;

    private Vector<String> introduction = new Vector<String>();       //用户的个人介绍
    private Vector<String> username = new Vector<String>();    //用户名
    private Vector<String> user_ID = new Vector<String>();//用户的objectId
    private friends_adapter fre_adapter;
//    final Context context = this.getBaseContext();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_user);
        listView  = (ListView) findViewById(R.id.Search_listview);
//        final Context context = this.getBaseContext();
        initwidget();
    }//onCreate

    private void initwidget() {
        return_bt = (TextView) findViewById(R.id.lbt);
        info_title = (TextView) findViewById(R.id.title);
        search_bar = (EditText) findViewById(R.id.search_bar);
        return_bt.setText("返回");
        info_title.setText("搜索");

        return_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search_user.this.finish();
            }
        });
        search_bar.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEND ||
                        (keyEvent != null && keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    switch (keyEvent.getAction()) {
                        case KeyEvent.ACTION_UP:
//                            Toast.makeText(search_user.this, search_bar.getText(), Toast.LENGTH_SHORT).show();
                            query_user_by_username_and_load_ListView(search_bar.getText().toString());
                            //收起软键盘
                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                            return true;
                        default:
                            return true;
                    }
                }
                return false;

//                return false;
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                if(user_ID.get(i).equals(BmobUser.getCurrentUser(MyUser.class).getObjectId())){
                    //如果点击的用户ID是用户本身则跳转到用户的个人信息界面
                    Intent intent = new Intent(search_user.this, MyAccount.class);
                    startActivity(intent);
                }//if
                //若选中的用户的ID不是本人则跳转到他人信息界面（然后判断是否为自己的好友显示不同的接口）
                else{
                    Intent intent = new Intent(search_user.this, other_information.class);
                    // 在Intent中传递数据
                    intent.putExtra("other_ID", user_ID.get(i));
                    // 启动Intent
                    startActivity(intent);
                }//else
            }//onItemClick
        });
    }//initwidget

    private void query_user_by_username_and_load_ListView(final String query_username){

        BmobQuery<MyUser> query = new BmobQuery<MyUser>();
        query.addWhereEqualTo("username", query_username);
//返回50条数据，如果不加上这条语句，默认返回10条数据
        query.setLimit(50);
//执行查询方法
        query.findObjects(new FindListener<MyUser>() {
            @Override
            public void done(List<MyUser> object, BmobException e) {
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
                        fre_adapter = new friends_adapter(getBaseContext(), R.layout.friend_lv_item, query_list);
                        listView.setAdapter(fre_adapter);
                    }//if object.size() != 0
                    else{
                        AlertDialog.Builder builder=new AlertDialog.Builder(search_user.this);
                        builder.setMessage("没有找到符合条件的用户").setCancelable(false)
                                .setPositiveButton("确定", null).create().show();
                    }//else
                }else{
                    AlertDialog.Builder builder=new AlertDialog.Builder(search_user.this);
                    builder.setMessage("查询失败").setCancelable(false)
                            .setPositiveButton("确定", null).create().show();
                    Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                }
            }
        });

    }//query_user

}
