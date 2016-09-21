package com.example.administrator.ourapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.LinearLayout;
import android.support.v4.app.Fragment;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobUser;

/**
 * Created by dell-pc on 2016/8/21.
 */
public class my_task extends FragmentActivity implements View.OnClickListener {
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
            @Override
            public void onClick(View view) {
                if (BmobUser.getObjectByKey("tag")!=null)
                {
                    Intent intent=new Intent(my_task.this,MissionPub.class);
                    startActivity(intent);
                }
            }
        });

        missionTitle=(TextView)findViewById(R.id.title);
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
}
