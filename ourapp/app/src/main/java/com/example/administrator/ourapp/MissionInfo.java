package com.example.administrator.ourapp;

import android.content.ComponentName;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.ourapp.question_and_answer.question_and_answer;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;


/**
 * Created by Administrator on 2016/8/20.
 */
public class MissionInfo extends AppCompatActivity {

    TextView return_bt,commit_bt;//标题上的左右按钮
    TextView info_title;//标题
    TextView QA;    //问答
    private ImageView userimage;
    private TextView username;
    private Mission mMission;
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
                ComponentName comp=new ComponentName(MissionInfo.this,apply.class);
                Intent intent=new Intent();
                intent.setComponent(comp);
                startActivity(intent);
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


    }


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
