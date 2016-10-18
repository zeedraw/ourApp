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

import com.example.administrator.ourapp.message.Message_tools;
import com.example.administrator.ourapp.mymissionadapter.UserItemAdapter;
import com.example.administrator.ourapp.user_information.MyAccount;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
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
    private int chooseNum=0;
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
        title=(TextView)findViewById(R.id.mission_title);
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

                                        //TODO 给各个用户(addlist中）发送申请成功消息[已完成]

                                        if(e==null) {
                                            Message_tools sm = new Message_tools();
                                            for (int i = 0; i < addList.size(); ++i) {
                                                sm.send(BmobUser.getCurrentUser(MyUser.class), addList.get(i),
                                                        "恭喜您，您参加的" + mission.getName() + "活动已经开始！",
                                                        5, false, mission.getObjectId(), ChoosePeople.this);
                                            }//for
                                        }
//                                        List<BmobObject> messages = new ArrayList<BmobObject>();
//                                        for (int i = 0; i < addList.size(); i++) {
//                                            Message message = new Message();
//                                            message.setSender(BmobUser.getCurrentUser(MyUser.class));
//                                            message.setReceiver(addList.get(i));
//                                            message.setType(3); //3为申请任务成功的消息
//                                            message.setBe_viewed(false);
//                                            message.setRemark(mission.getObjectId());
//                                            message.setContent("恭喜您，您申请的"+ mission.getName() +
//                                                    "活动已经通过！");
//                                            messages.add(message);
//                                        }
//
//                                        new BmobBatch().insertBatch(messages).doBatch(new QueryListListener<BatchResult>() {
//
//                                            @Override
//                                            public void done(List<BatchResult> o, BmobException e) {
//                                                if(e==null){
//                                                    for(int i=0;i<o.size();i++){
//                                                        BatchResult result = o.get(i);
//                                                        BmobException ex =result.getError();
//                                                        if(ex==null){
//                                                            Log.i("给申请者发送成功消息", "第"+i+"个数据批量添加成功："+result.getCreatedAt()+","+result.getObjectId()+","+result.getUpdatedAt());
//                                                        }else{
//                                                            Log.i("给申请者发送成功消息", "第"+i+"个数据批量添加失败："+ex.getMessage()+","+ex.getErrorCode());
//                                                        }
//                                                    }
//                                                }else{
//                                                    Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
//                                                }
//                                            }
//                                        });


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
                        chooseNum--;
                        setAddEnable();
                        addList.remove(userList.get(i));
                    }
                    else
                    {
                        checkBox.setChecked(true);
                        chooseNum++;
                        setAddEnable();
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
                    userItemAdapter=new UserItemAdapter(ChoosePeople.this,R.layout.user_item,userList);
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
                    add.setEnabled(false);
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

    private void setAddEnable()
    {
        if (chooseNum>0)
        {
            add.setEnabled(true);
        }
        else
        {
            add.setEnabled(false);
        }
    }
}
