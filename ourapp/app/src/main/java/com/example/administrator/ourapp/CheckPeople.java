package com.example.administrator.ourapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.administrator.ourapp.mymissionadapter.UserItemAdapter;
import com.example.administrator.ourapp.user_information.MyAccount;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by Administrator on 2016/9/25.
 */
public class CheckPeople extends AppCompatActivity {
    private ListView listView;
    private String missionId;
    private UserItemAdapter userItemAdapter;
    private List<MyUser> userList;
    private TextView title,rt_bt;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.checkpeople);
        Intent intent=getIntent();
        missionId=intent.getStringExtra("missionId");
        initWidget();
        setListener();
        query();
    }

    private void initWidget()
    {
        listView=(ListView)findViewById(R.id.checkpeople_lv);
        title=(TextView)findViewById(R.id.title);
        rt_bt=(TextView)findViewById(R.id.lbt);
        rt_bt.setText("返回");
        rt_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void setListener()
    {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                MyUser user = userList.get(i);
                Intent intent = new Intent(CheckPeople.this, MyAccount.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("user", user);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    private void query()
    {
        BmobQuery<MyUser> query=new BmobQuery<MyUser>();
        Mission mission=new Mission();
        mission.setObjectId(missionId);
        query.addWhereRelatedTo("get_user",new BmobPointer(mission));
        query.include("userimage");
        query.findObjects(new FindListener<MyUser>() {
            @Override
            public void done(List<MyUser> list, BmobException e) {
                if (e==null)
                {
                    userList=new ArrayList<MyUser>(list);
                    userItemAdapter=new UserItemAdapter(CheckPeople.this,R.layout.friend_lv_item,userList);
                    listView.setAdapter(userItemAdapter);
                }
                else
                {
                    Log.i("z","查询筛选人员失败");
                }
            }
        });
    }
}
