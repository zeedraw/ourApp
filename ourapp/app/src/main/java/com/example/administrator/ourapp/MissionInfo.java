package com.example.administrator.ourapp;

import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.ourapp.question_and_answer.question_and_answer;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;


/**
 * Created by Administrator on 2016/8/20.
 */
public class MissionInfo extends AppCompatActivity {

    private TextView return_bt,commit_bt;//标题上的左右按钮
    private TextView info_title;//标题
    private TextView QA;    //问答
    private ImageView userimage;
    private TextView username;
    private Mission mMission;
    private TextView mName,mLocation,mTime,mNeedPeople,mPubTime,mDetails;
    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mission_info);
        Intent intent=getIntent();
        mMission=(Mission)intent.getSerializableExtra("mission");
        initWidget();
    }

    private void initWidget(){

        return_bt=(TextView)findViewById(R.id.lbt);
        commit_bt=(TextView)findViewById(R.id.rbt);
        QA = (TextView) findViewById(R.id.question_and_answer);
        //在textview左侧添加drawable
//        return_bt.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_keyboard_arrow_left_black_24dp, 0, 0, 0);
        return_bt.setText("返回");
//        return_bt.setTextSize(21);
        commit_bt.setText("报名");
//        commit_bt.setTextSize(21);
        if (mMission.getPub_user().getObjectId().equals(BmobUser.getObjectByKey("objectId")))
        {
           commit_bt.setVisibility(View.INVISIBLE);
        }

        info_title=(TextView)findViewById(R.id.title);
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
                 AlertDialog.Builder builder=new AlertDialog.Builder(MissionInfo.this);
                builder.setTitle("请选择");
                String[] items={"个人报名","团队报名"};
                builder.setNegativeButton("取消",null);
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (i)
                        {
                            case 0://选择个人报名
                               AlertDialog.Builder mybuilder=new AlertDialog.Builder(MissionInfo.this);
                                mybuilder.setMessage("确定以当前用户报名");
                                mybuilder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    AlertDialog.Builder mes=new AlertDialog.Builder(MissionInfo.this);
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        MyUser user=new MyUser();
                                        user.setObjectId((String) BmobUser.getObjectByKey("objectId"));
                                        Mission mission=new Mission();
                                        mission.setObjectId(mMission.getObjectId());
                                        BmobRelation relation=new BmobRelation();
                                        relation.add(user);
                                        mission.setCur_people(relation);
                                        mission.update(new UpdateListener() {
                                            @Override
                                            public void done(BmobException e) {
                                                if (e==null)
                                                {
                                                    mes.setMessage("报名成功");
                                                }
                                                else
                                                {
                                                    mes.setMessage("报名失败"+e.getMessage());
                                                }
                                                mes.create().show();
                                            }
                                        });

                                    }
                                });
                                mybuilder.create().show();
                                    break;
                            case 1://选择团体报名
                                break;
                        }
                    }
                });
                builder.create().show();


            }
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

        userimage=(ImageView)findViewById(R.id.fre_userimage);
        new LoadImage().execute(mMission.getPub_user().getUserimage().getUrl());

        username=(TextView)findViewById(R.id.fre_username);
        username.setText(mMission.getPub_user().getName());

        mName=(TextView)findViewById(R.id.apply_mission_name);
        mName.setText(mMission.getName());
        mLocation=(TextView)findViewById(R.id.apply_mission_location);
        mLocation.setText(mMission.getLocation());
        mTime=(TextView)findViewById(R.id.apply_mission_time);
        mTime.setText(mMission.getStart_time()+"至"+mMission.getEnd_time());
        mNeedPeople=(TextView)findViewById(R.id.apply_mission_needpeople);
        mNeedPeople.setText(mMission.getNeed_people().toString());
        mPubTime=(TextView)findViewById(R.id.apply_mission_pubtime);
        mPubTime.setText(mMission.getPub_time());
        mDetails=(TextView)findViewById(R.id.apply_mission_details);
        mDetails.setText(mMission.getDetail());


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
