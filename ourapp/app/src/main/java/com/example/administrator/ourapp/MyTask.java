package com.example.administrator.ourapp;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.CheckedTextView;
import android.support.v4.app.Fragment;
import android.widget.TextView;

import com.example.administrator.ourapp.authenticate.agency_authenticate;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;

/**
 * Created by dell-pc on 2016/8/21.
 */
public class MyTask extends FragmentActivity implements View.OnClickListener {
    private CheckedTextView pub_tv,do_tv;
    private TextView rt_button,pub_button;
    private TextView missionTitle;
//    private FragmentManager mfragManager;
//    private FragmentTransaction mTransaction;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_task);
//        ViewPager pager = (ViewPager) findViewById(R.id.pager);
//
//        SlidingTabLayout tab = (SlidingTabLayout) findViewById(R.id.tab);
//
//        List<Fragment> frg = new ArrayList<Fragment>();
//        frg.add(new Frg_task_applying());
//        frg.add(new Frg_task_ing());
//        frg.add(new Frg_task_completed());
//        Mission_Do_FragAdapter adapter = new Mission_Do_FragAdapter(getSupportFragmentManager(), frg);
//
//        pager.setAdapter(adapter);
//        tab.setCustomTabView(R.layout.tab, 0);
//        tab.setViewPager(pager);

        initWidget();


    }//onCreate

    private void initWidget()
    {
        pub_tv=(CheckedTextView)findViewById(R.id.pub_tv);
        pub_tv.setOnClickListener(this);
        do_tv=(CheckedTextView)findViewById(R.id.do_tv);
        do_tv.setOnClickListener(this);

        //默认页面
        pub_tv.setChecked(true);
        myCheckedChange(pub_tv);
//        mfragManager=getSupportFragmentManager();
//        mTransaction=mfragManager.beginTransaction();
//        mTransaction.add(R.id.frag_mission_container,new Frag_do(),"do")
//                .add(R.id.frag_mission_container,new Frag_pub(),"pub")
//                .commit();



        rt_button=(TextView)findViewById(R.id.lbt);
        rt_button.setText("返回");
        rt_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        rt_button.setVisibility(View.VISIBLE);

        pub_button=(TextView)findViewById(R.id.rbt);
        pub_button.setText("发布");
        pub_button.setVisibility(View.VISIBLE);
        pub_button.setOnClickListener(new View.OnClickListener() {
            Dialog loading;
            @Override
            public void onClick(View view) {
                loading=MainActivity.createLoadingDialog(MyTask.this);
                loading.show();
                BmobQuery<MyUser> query=new BmobQuery<MyUser>();
                query.getObject(BmobUser.getCurrentUser(MyUser.class).getObjectId(), new QueryListener<MyUser>() {
                    @Override
                    public void done(MyUser myUser, BmobException e) {
                        if (e==null)
                        {   loading.dismiss();
                            if (myUser.getIdentifiedPublish())
                            {
                                Intent intent=new Intent(MyTask.this,MissionPub.class);
                                startActivity(intent);
                            }
                            else
                            {

                                AlertDialog.Builder builder=new AlertDialog.Builder(MyTask.this);
                                builder.setMessage("您还未获得机构认证，是否现在认证");
                                builder.setPositiveButton("是的", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        Intent intent=new Intent(MyTask.this,agency_authenticate.class);
                                        startActivity(intent);
                                    }
                                });
                                builder.setNegativeButton("取消",null);
                                builder.create().show();
                            }
                        }
                    }
                });
            }
        });

        missionTitle=(TextView)findViewById(R.id.mission_title);
        missionTitle.setText("我的任务");
    }


    @Override
    public void onClick(View view) {
        if (view==pub_tv)
        {
            pub_tv.setChecked(true);
            do_tv.setChecked(false);
           myCheckedChange(view);
        }

        else if(view==do_tv)
        {
            pub_tv.setChecked(false);
            do_tv.setChecked(true);
            myCheckedChange(view);
        }
    }

    private void myCheckedChange(View view)
    {
        FragmentManager fm=getSupportFragmentManager();
        FragmentTransaction ft=fm.beginTransaction();
        Fragment doFrag=fm.findFragmentByTag("do");
        Fragment pubFrag=fm.findFragmentByTag("pub");

        if (view==do_tv)
        {
            if (pubFrag!=null)
            {
                ft.hide(pubFrag);
            }
            if (doFrag==null)
            {
                doFrag=new Frag_do();
                ft.add(R.id.frag_mission_container,doFrag,"do");
            }
            else
            {
                ft.show(doFrag);
            }
        }

        else if (view==pub_tv)
        {
            if (doFrag!=null)
            {
                ft.hide(doFrag);
            }
            if (pubFrag==null)
            {
                pubFrag=new Frag_pub();
                ft.add(R.id.frag_mission_container,pubFrag,"pub");
            }
            else
            {
                ft.show(pubFrag);
            }
        }

        ft.commit();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==1000&&resultCode==1001) {
            Log.i("z", "activity收到信息"+data.getIntExtra("position",-1));
            FragmentManager fm = getSupportFragmentManager();
            Frag_pub pubFrag = (Frag_pub) fm.findFragmentByTag("pub");
            Frg_task_ing ing=(Frg_task_ing)pubFrag.getFragment(2);
            ing.handleResult(data);

        }
    }
}
