package com.example.administrator.ourapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.HeaderViewListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.administrator.ourapp.mymissionadapter.UserItemAdapter;
import com.example.administrator.ourapp.user_information.MyAccount;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by Administrator on 2016/9/22.
 */
public class ChoosePeople extends AppCompatActivity {
    private ListView listView;
    private UserItemAdapter userItemAdapter;
    private String missionId;
    private List<MyUser> userList;
    private List<MyUser> addList;
    private TextView title,rt_bt,edit_bt;
    private final static int NORMAL=0;
    private final static int EDIT=1;
    private  int state=NORMAL;
    private RelativeLayout edit_bar;
    private Button add;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choosepeople);
        addList=new ArrayList<MyUser>();
        initWidget();
        setListener();
        Intent intent=getIntent();
        missionId=intent.getStringExtra("missionId");
        query();

    }

    private void initWidget()
    {
        edit_bar=(RelativeLayout)findViewById(R.id.edit_bar);
        title=(TextView)findViewById(R.id.title);
        title.setText("筛选人员");
        rt_bt=(TextView)findViewById(R.id.lbt);
        rt_bt.setText("返回");
        rt_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        listView=(ListView)findViewById(R.id.choosepeople_lv);

        add=(Button)findViewById(R.id.choose_add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BmobRelation getRelation=new BmobRelation();
                final Mission mission=new Mission();
                mission.setObjectId(missionId);
                for(MyUser user:addList)
                {
                    MyUser userPointer=new MyUser();
                    userPointer.setObjectId(user.getObjectId());
                    getRelation.add(userPointer);
                }
                mission.setGet_user(getRelation);
                mission.update(new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e==null)
                        {
                            Log.i("z","添加志愿者成功");
                            BmobRelation curRelation=new BmobRelation();
                            for (MyUser user:addList)
                            {
                                MyUser userPointer=new MyUser();
                                userPointer.setObjectId(user.getObjectId());
                                curRelation.remove(userPointer);
                            }
                            mission.setCur_people(curRelation);
                            mission.update(new UpdateListener() {
                                @Override
                                public void done(BmobException e) {
                                    if(e==null) {
                                        Log.i("z", "移除选择成功");
                                        userList.removeAll(addList);
                                        edit_bt.setText("编辑");
                                        state = NORMAL;
                                        edit_bar.setVisibility(View.GONE);
                                        userItemAdapter.setCheckBoxShown(false);
                                        userItemAdapter.notifyDataSetChanged();
                                    }
                                    else
                                    {
                                        Log.i("z","移除失败"+e.getMessage());
                                    }
                                }
                            });
                        }

                    }
                });

            }
        });
    }

    private void setListener()
    {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (state==NORMAL) {
                    if (adapterView.getAdapter() instanceof HeaderViewListAdapter) {
                        HeaderViewListAdapter listAdapter = (HeaderViewListAdapter) adapterView.getAdapter();
                        userItemAdapter = (UserItemAdapter) listAdapter.getWrappedAdapter();
                    } else {
                        userItemAdapter = (UserItemAdapter) adapterView.getAdapter();
                    }
                    MyUser user = userItemAdapter.getUser(i);
                    Intent intent = new Intent(ChoosePeople.this, MyAccount.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("user", user);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
                else if(state==EDIT)
                {
                    CheckBox checkBox=(CheckBox)view.findViewById(R.id.choose_cb);
                    if (checkBox.isChecked())
                    {
                        checkBox.setChecked(false);
                        addList.remove(userList.get(i));
                    }
                    else
                    {
                        checkBox.setChecked(true);
                        addList.add(userList.get(i));
                    }
                }
            }
        });
    }

    private void  query()
    {
        BmobQuery<MyUser> query=new BmobQuery<MyUser>();
        Mission mission=new Mission();
        mission.setObjectId(missionId);
        query.addWhereRelatedTo("cur_people",new BmobPointer(mission));
        query.include("userimage");
        query.findObjects(new FindListener<MyUser>() {
            @Override
            public void done(List<MyUser> list, BmobException e) {
                if (e==null)
                {
                    userList=new ArrayList<MyUser>(list);
                    userItemAdapter=new UserItemAdapter(ChoosePeople.this,R.layout.friend_lv_item,userList);
                    listView.setAdapter(userItemAdapter);
                    initEditButton();
                }
                else
                {
                    Log.i("z","查询筛选人员失败");
                }
            }
        });
    }

    private void initEditButton()
    {
        edit_bt=(TextView)findViewById(R.id.rbt);
        edit_bt.setText("编辑");
        View.OnClickListener edit=new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (state==NORMAL)
                {
                    edit_bt.setText("取消");
                    addList.clear();
                    state=EDIT;
                    edit_bar.setVisibility(View.VISIBLE);
                    userItemAdapter.setCheckBoxShown(true);
                    userItemAdapter.notifyDataSetChanged();

                }
                else if(state==EDIT)
                {
                    edit_bt.setText("编辑");
                    state=NORMAL;
                    edit_bar.setVisibility(View.GONE);
                    userItemAdapter.setCheckBoxShown(false);
                    userItemAdapter.notifyDataSetChanged();
                }

            }
        };
        edit_bt.setOnClickListener(edit);
    }
}
