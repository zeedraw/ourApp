package com.example.administrator.ourapp;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NotificationCompatSideChannelService;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.ourapp.message.Message_tools;
import com.example.administrator.ourapp.question_and_answer.question_and_answer;
import com.example.administrator.ourapp.user_information.MyAccount;
import com.example.administrator.ourapp.wish.wish_list;
import com.example.administrator.ourapp.wish.wish_pub_with_mission;

import java.io.InputStream;
import java.lang.annotation.ElementType;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.UpdateListener;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;


/**
 * Created by Administrator on 2016/8/20.
 */
public class MissionInfo extends SwipeBackActivity {

    private TextView return_bt,commit_bt;//标题上的左右按钮
    private TextView info_title;//标题
    private RelativeLayout QA;    //问答
    private RelativeLayout wish;    //心愿
    private ImageView userimage;
    private TextView username;
    private TextView orgDescription;
    private Mission mMission;
    private TextView mName,mLocation,mTime,mLocDtails,mDetails,mState;
    private TextView mContact;
    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task);
        Intent intent=getIntent();
        mMission=(Mission)intent.getSerializableExtra("mission");
        initWidget();

    }

    private void initWidget(){

        return_bt=(TextView)findViewById(R.id.lbt);
        commit_bt=(TextView)findViewById(R.id.rbt);
        QA = (RelativeLayout) findViewById(R.id.question_and_answer);
        wish = (RelativeLayout) findViewById(R.id.wish);
        //在textview左侧添加drawable
//        return_bt.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_keyboard_arrow_left_black_24dp, 0, 0, 0);
        return_bt.setText("返回");
//        return_bt.setTextSize(21);
        mState=(TextView)findViewById(R.id.mission_state);
        switch (mMission.getState())
        {
            case 2:
                mState.setText("申请中");
                break;
            case 3:
                mState.setText("进行中");
                commit_bt.setVisibility(View.INVISIBLE);
                break;
            case 4:
                mState.setText("已结束");
                commit_bt.setVisibility(View.INVISIBLE);
                break;
        }
//        commit_bt.setTextSize(21);
        if (mMission.getPub_user().getObjectId().equals(BmobUser.getObjectByKey("objectId")))
        {
           commit_bt.setVisibility(View.INVISIBLE);
        }
        commit_bt.setText("报名");

        info_title=(TextView)findViewById(R.id.mission_title);
        info_title.setText("任务详情");

        return_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                MissionInfo.this.finish();
            }
        });

        commit_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(BmobUser.getCurrentUser(MyUser.class) == null){
                    Intent intent=new Intent(MissionInfo.this,Login.class);
                    startActivity(intent);
                }//if 未登录
                else{
                    //TODO　判断非认证bu报名
                    AlertDialog.Builder mybuilder=new AlertDialog.Builder(MissionInfo.this);
                    mybuilder.setMessage("确定以当前用户报名");
                    mybuilder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        Dialog loading=MainActivity.createLoadingDialog(MissionInfo.this);
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            loading.show();
                            //判断是否重复报名
                            final List<MyUser> users=new ArrayList<MyUser>();
                            BmobQuery<MyUser> query=new BmobQuery<MyUser>();
                            final Mission mission=new Mission();
                            mission.setObjectId(mMission.getObjectId());
                            query.addWhereRelatedTo("cur_people",new BmobPointer(mission));
                            query.findObjects(new FindListener<MyUser>() {
                                @Override
                                public void done(List<MyUser> list, BmobException e) {users.addAll(list);
                                    BmobQuery<MyUser> query1=new BmobQuery<MyUser>();
                                    query1.addWhereRelatedTo("get_user",new BmobPointer(mission));
                                    query1.findObjects(new FindListener<MyUser>() {
                                        @Override
                                        public void done(List<MyUser> list1, BmobException e) {
                                            users.addAll(list1);
                                            boolean flag=true;
                                            for (int i=0;i<users.size();i++)
                                            {
                                                if (users.get(i).getObjectId().equals(BmobUser.getCurrentUser().getObjectId()))
                                                {
                                                    flag=false;
                                                }
                                            }
                                            if (flag)//true则说明不重复
                                            {
                                                final MyUser user = new MyUser();
                                                user.setObjectId((String) BmobUser.getObjectByKey("objectId"));
                                                final Mission mission = new Mission();
                                                mission.setObjectId(mMission.getObjectId());
                                                BmobRelation relation = new BmobRelation();
                                                relation.add(user);
                                                mission.setCur_people(relation);
                                                mission.update(new UpdateListener() {
                                                    @Override
                                                    public void done(BmobException e) {
                                                        AlertDialog.Builder mes=new AlertDialog.Builder(MissionInfo.this);
                                                        if (e == null) {
                                                            loading.dismiss();
                                                            mes.setMessage("报名成功");
                                                            Message_tools sm = new Message_tools();
                                                            sm.send(BmobUser.getCurrentUser(MyUser.class), mMission.getPub_user(),
                                                                    "用户" + BmobUser.getCurrentUser(MyUser.class).getName()
                                                                            + "刚刚报名了您发布的任务", 4, false, mMission.getObjectId(), MissionInfo.this);
                                                            //4代表报名任务成功的消息

                                                        } else {
                                                            loading.dismiss();
                                                            mes.setMessage("报名失败" + e.getMessage());
                                                            Toast.makeText(MissionInfo.this, "失败:" + e.getMessage(), Toast.LENGTH_SHORT).show();

                                                        }
                                                        mes.create().show();
                                                    }
                                                });
                                            }//if
                                            else
                                            {
                                                loading.dismiss();
                                                AlertDialog.Builder builder=new AlertDialog.Builder(MissionInfo.this);
                                                builder.setMessage("您已经报过该任务，无法重复报名");
                                                builder.create().show();
                                            }

                                        }
                                    });
                                }
                            });//判断是否重复报名
                        }
                    });
                    mybuilder.setNegativeButton("取消",null);
                    mybuilder.create().show();
                }//else

            }//onClcik
        });


        QA.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
//                    ComponentName comp=new ComponentName(MissionInfo.this,question_and_answer.class);
//                    Intent intent=new Intent();
//                    intent.setComponent(comp);
//                    startActivity(intent);
                startIntentWithMission(question_and_answer.class);
            }
        });

        wish.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
//                    ComponentName comp=new ComponentName(MissionInfo.this,question_and_answer.class);
//                    Intent intent=new Intent();
//                    intent.setComponent(comp);
//                    startActivity(intent);
                startIntentWithMission(wish_list.class);
            }
        });

        userimage=(ImageView)findViewById(R.id.person_image);
        new LoadImage().execute(mMission.getPub_user().getUserimage().getUrl());

        username=(TextView)findViewById(R.id.publisher_name);
        username.setText(mMission.getPub_user().getName());

        orgDescription=(TextView)findViewById(R.id.organization);
        orgDescription.setText(mMission.getPub_user().getOrgDescription());

        mName=(TextView)findViewById(R.id.missiontitle);
        mName.setText(mMission.getName());
        mLocation=(TextView)findViewById(R.id.mission_location);
        List<String> list=mMission.getLocation_abs();
        String locationAbs=list.get(0)+"-"+list.get(1)+"-"+list.get(2);
        mLocation.setText(locationAbs);

        mState=(TextView)findViewById(R.id.mission_state);
        switch (mMission.getState())
        {
            case 2:
                mState.setText("申请中");
                break;
            case 3:
                mState.setText("进行中");
                break;
            case 4:
                mState.setText("已结束");
                break;
        }

        mTime=(TextView)findViewById(R.id.mission_time);
        mTime.setText(mMission.getStart_time()+"至"+mMission.getEnd_time());

        mLocDtails=(TextView)findViewById(R.id.detail_location);
        mLocDtails.setText(mMission.getLocation());

        mContact=(TextView)findViewById(R.id.contact);
        String userContact=mMission.getContact_number();
        if (userContact!=null) {
            mContact.setText(userContact);
        }

        mDetails=(TextView)findViewById(R.id.detail_mission);
        mDetails.setText(mMission.getDetail());

        userimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                BmobQuery<MyUser> query = new BmobQuery<MyUser>();
                query.getObject(mMission.getPub_user().getObjectId(), new QueryListener<MyUser>() {

                    @Override
                    public void done(MyUser object, BmobException e) {
                        if(e==null){
                            Intent intent=new Intent(MissionInfo.this,MyAccount.class);
                            Bundle bundle=new Bundle();
                            bundle.putSerializable("user", object);
                            intent.putExtras(bundle);
                            MissionInfo.this.startActivity(intent);
                        }else{
                            Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                            Toast.makeText(MissionInfo.this, "失败:" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                });


            }
        });
    }

    //异步加载图片
    public class LoadImage extends AsyncTask<String,Void,Drawable>
    {
        @Override
        protected Drawable doInBackground(String... strs) {
            URL request;
            InputStream input;
            Drawable drawable = null;

            try {
                request =new URL(strs[0]);
                input=(InputStream)request.getContent();
                drawable = Drawable.createFromStream(input, "src");
            } catch (Exception e) {
                e.printStackTrace();
            }

            return drawable;

        }

        @Override
        protected void onPostExecute(Drawable drawable) {
            userimage.setImageDrawable(drawable);
        }
    }

    //携带mission的intent
    private void startIntentWithMission(Class<?> c)
    {
        Intent intent=new Intent(MissionInfo.this,c);
        Bundle bundle=new Bundle();
        bundle.putSerializable("mission",mMission);
        intent.putExtras(bundle);
        startActivity(intent);
    }




}
