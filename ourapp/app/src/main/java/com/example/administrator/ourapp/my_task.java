package com.example.administrator.ourapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.support.v4.app.Fragment;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dell-pc on 2016/8/21.
 */
public class my_task extends AppCompatActivity {
    TextView rt_button,pub_button;
    TextView missionTitle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_task);
        ViewPager pager = (ViewPager) findViewById(R.id.pager);

        SlidingTabLayout tab = (SlidingTabLayout) findViewById(R.id.tab);

        List<Fragment> frg = new ArrayList<Fragment>();
        frg.add(new Frg_task_applying());
        frg.add(new Frg_task_ing());
        frg.add(new Frg_task_completed());
        task_fragmentAdapter adapter = new task_fragmentAdapter(getSupportFragmentManager(), frg);

        pager.setAdapter(adapter);
        tab.setCustomTabView(R.layout.tab, 0);
        tab.setViewPager(pager);

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

        missionTitle=(TextView)findViewById(R.id.title);
        missionTitle.setText("我的任务");





    }//onCreate


}
