package com.example.administrator.ourapp;


import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity implements View.OnClickListener{
    CheckedTextView tv_main,tv_mes,tv_mine;//下方的3个tab
    TextView title;//上方标题
    FragmentManager mfragManager;
    FragmentTransaction mTransaction;
//    private ViewPager viewPager;
//    private ImageView cursor;//上部tabs下方的光标
//    private int bmpw = 0; // 游标宽度
//    private int offset = 0;// // 动画图片偏移量
//    private int currIndex = 0;// 当前页卡编号
//    List<Fragment> fragments;//frag集
//    private TextView t1, t2, t3,t4;// 页卡头标

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.maininterface);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//设置竖屏
        initWidget();
        //初始化下部tab的fragement


//        //构造适配器
//        fragments=new ArrayList<Fragment>();
//        fragments.add(new Fragment1());
//        fragments.add(new Fragment2());
//        fragments.add(new Fragment3());
//        fragments.add(new Fragment4());

     //   initCursorPos();
       // InitTextView();

       // FragAdapter adapter = new FragAdapter(getSupportFragmentManager(), fragments);

//        //设定适配器
//        viewPager.setAdapter(adapter);
//        viewPager.setOnPageChangeListener(new MyPageChangeListener());
//        viewPager.setCurrentItem(0);

    }
    private  void initWidget(){
        //下部tabs中的3个组件
        tv_main=(CheckedTextView)findViewById(R.id.main);
        tv_main.setOnClickListener(this);
        tv_mes=(CheckedTextView)findViewById(R.id.mes);
        tv_mes.setOnClickListener(this);
        tv_mine=(CheckedTextView)findViewById(R.id.mine);
        tv_mine.setOnClickListener(this);
        tv_main.setChecked(true);

        //设置activity的标题
        title=(TextView)findViewById(R.id.title);
        title.setText(tv_main.getText());
        reverse(tv_mine);
        reverse(tv_mes);
        reverse(tv_main);

        //默认首页
         mfragManager=getSupportFragmentManager();
         mTransaction=mfragManager.beginTransaction();
        mTransaction.add(R.id.frag_container,new MineFrag(),"mine")
                .add(R.id.frag_container,new MesFrag(),"mes")
                .add(R.id.frag_container,new MainFrag(),"main")
                .commit();

       // viewPager = (ViewPager) findViewById(R.id.vPager);

    }

//    /**
//     2      * 初始化头标
//     3 */
//         private void InitTextView() {
//                 t1 = (TextView) findViewById(R.id.text1);
//                 t2 = (TextView) findViewById(R.id.text2);
//                 t3 = (TextView) findViewById(R.id.text3);
//                 t4 = (TextView) findViewById(R.id.text4);
//
//                 t1.setOnClickListener(new MyOnClickListener(0));
//                 t2.setOnClickListener(new MyOnClickListener(1));
//                 t3.setOnClickListener(new MyOnClickListener(2));
//                 t4.setOnClickListener(new MyOnClickListener(3));
//             }

    @Override
    public void onClick(View view) {//下部tab的监听事件

        if(view==tv_main)
        {
            tv_main.setChecked(true);
            tv_mes.setChecked(false);
            tv_mine.setChecked(false);
            title.setText(tv_main.getText());
            mTransaction=mfragManager.beginTransaction();
            mTransaction.show(mfragManager.findFragmentByTag("main"))
                    .hide(mfragManager.findFragmentByTag("mes"))
                    .hide(mfragManager.findFragmentByTag("mine")).commit();

        }

        else if (view==tv_mes)
        {
            tv_main.setChecked(false);
            tv_mes.setChecked(true);
            tv_mine.setChecked(false);
            title.setText(tv_mes.getText());
            mTransaction=mfragManager.beginTransaction();
            mTransaction.hide(mfragManager.findFragmentByTag("main"))
                    .show(mfragManager.findFragmentByTag("mes"))
                    .hide(mfragManager.findFragmentByTag("mine")).commit();

        }
        else if (view==tv_mine)
        {
            tv_main.setChecked(false);
            tv_mes.setChecked(false);
            tv_mine.setChecked(true);
            title.setText(tv_mine.getText());
            mTransaction=mfragManager.beginTransaction();
            mTransaction.hide(mfragManager.findFragmentByTag("main"))
                    .hide(mfragManager.findFragmentByTag("mes"))
                    .show(mfragManager.findFragmentByTag("mine")).commit();
        }
        reverse(tv_mine);
        reverse(tv_mes);
        reverse(tv_main);

    }

    public void reverse(CheckedTextView view)//翻转图标
    {
        if(view.isChecked())
        {
            view.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.edu45,0,0);
            view.setTextColor(getResources().getColor(R.color.colorblue));
        }
        else
        {
            view.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.checked,0,0);
            view.setTextColor(getResources().getColor(R.color.colorGrey1));
        }
    }

//    //初始化指示器位置
//    public void initCursorPos() {
//        // 初始化动画
//        cursor = (ImageView) findViewById(R.id.cursor);
//        bmpw = BitmapFactory.decodeResource(getResources(), R.drawable.youbiao)
//                .getWidth();// 获取图片宽度
//
//        DisplayMetrics dm = new DisplayMetrics();
//        getWindowManager().getDefaultDisplay().getMetrics(dm);
//        int screenW = dm.widthPixels;// 获取分辨率宽度
//        offset = (screenW / fragments.size() - bmpw) / 2;// 计算偏移量
//
//
//        Matrix matrix = new Matrix();
//        matrix.postTranslate(offset, 0);
//        cursor.setImageMatrix(matrix);// 设置动画初始位置
//    }

//    //页面改变监听器
//    public class MyPageChangeListener implements ViewPager.OnPageChangeListener {
//
//        int one = offset * 2 + bmpw;// 页卡1 -> 页卡2 偏移量
//        int two = one * 2;// 页卡1 -> 页卡3 偏移量
//        int three = one * 3;// 页卡1-> 页卡4 偏移量
//
//        @Override
//        public void onPageSelected(int arg0) {
//            Animation animation = null;
//            switch (arg0) {
//                case 0:
//                    if (currIndex == 1) {
//                        animation = new TranslateAnimation(one, 0, 0, 0);
//                    } else if (currIndex == 2) {
//                        animation = new TranslateAnimation(two, 0, 0, 0);
//                    }
//                      else if(currIndex ==3){
//                        animation=new TranslateAnimation(three,0,0,0);
//                    }
//                    break;
//                case 1:
//                    if (currIndex == 0) {
//                        animation = new TranslateAnimation(0, one, 0, 0);
//                    } else if (currIndex == 2) {
//                        animation = new TranslateAnimation(two, one, 0, 0);
//                    }
//                    else if (currIndex==3)
//                    {
//                        animation=new TranslateAnimation(three,one,0,0);
//                    }
//                    break;
//                case 2:
//                    if (currIndex == 0) {
//                        animation = new TranslateAnimation(0, two, 0, 0);
//                    } else if (currIndex == 1) {
//                        animation = new TranslateAnimation(one, two, 0, 0);
//                    }
//                    else if(currIndex==3){
//                        animation =new TranslateAnimation(three,two,0,0);
//                    }
//                    break;
//                case 3:
//                    if (currIndex==0)
//                    {
//                        animation =new TranslateAnimation(0,three,0,0);
//                    }
//                    else if(currIndex==1)
//                    {
//                        animation=new TranslateAnimation(one,three,0,0);
//                    }
//                    else if(currIndex==2)
//                    {
//                        animation=new TranslateAnimation(two,three,0,0);
//                    }
//                    break;
//            }
//            currIndex = arg0;
//            animation.setFillAfter(true);// True:图片停在动画结束位置
//            animation.setDuration(300);
//            cursor.startAnimation(animation);
//        }
//
//        @Override
//        public void onPageScrolled(int arg0, float arg1, int arg2) {
//        }
//
//        @Override
//        public void onPageScrollStateChanged(int arg0) {
//        }
//    }
//
//    /**
//     * 头标点击监听
//     */
//    public class MyOnClickListener implements View.OnClickListener {
//        private int index = 0;
//
//        public MyOnClickListener(int i) {
//            index = i;
//        }
//
//        @Override
//        public void onClick(View v) {
//            viewPager.setCurrentItem(index);
//        }
//    }

    //以下三个方法对应MineFrag中的点击事件
    public void Myaccount_click(View view)
{
    ComponentName comp=new ComponentName(MainActivity.this,MyAccount.class);
    Intent intent=new Intent();
    intent.setComponent(comp);
    startActivity(intent);
}

    public void Mysetting_click(View view)
    {
        ComponentName comp=new ComponentName(MainActivity.this,MySetting.class);
        Intent intent=new Intent();
        intent.setComponent(comp);
        startActivity(intent);

    }

    public  void Mymssion_click(View view)
    {
        ComponentName comp=new ComponentName(MainActivity.this,MyMission.class);
        Intent intent=new Intent();
        intent.setComponent(comp);
        startActivity(intent);
    }



}



