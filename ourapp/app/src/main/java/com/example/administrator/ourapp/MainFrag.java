package com.example.administrator.ourapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/8/18.
 */
public class MainFrag extends Fragment {
    private ViewPager viewPager;
//    private ImageView cursor;//上部tabs下方的光标
//    private int bmpw = 0; // 游标宽度
//    private int offset = 0;// // 动画图片偏移量
//    private int currIndex = 0;// 当前页卡编号
    private List<Fragment> fragments;//frag集
//    private TextView t1, t2, t3,t4;// 页卡头标
    private View rootView;
    private SlidingTabLayout tab;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //构造适配器
        fragments=new ArrayList<Fragment>();
        fragments.add(new Frag_edu());
        fragments.add(new Frag_activity());
        fragments.add(new Frag_trans());
        fragments.add(new Frag_community());

    }

    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container, Bundle savedInstanceState) {

        rootView=inflater.inflate(R.layout.main_frag,container,false);
        viewPager = (ViewPager)rootView.findViewById(R.id.vPager);
        tab = (SlidingTabLayout)rootView.findViewById(R.id.main_tabs);

//        initCursorPos();
//        InitTextView();
        MainFragAdapter adapter = new MainFragAdapter(getActivity().getSupportFragmentManager(), fragments);
//        tab.setSelectedIndicatorColors(R.color.white);//滑动条颜色
        //设定适配器
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(3);
        tab.setCustomTabView(R.layout.tab, 0);
        tab.setViewPager(viewPager);
//        viewPager.setOnPageChangeListener(new MyPageChangeListener());
//        viewPager.setCurrentItem(0);
        return  rootView;
    }

//    public void initCursorPos() {
//        // 初始化动画
//        cursor = (ImageView)rootView.findViewById(R.id.cursor);
//        bmpw = BitmapFactory.decodeResource(getResources(), R.drawable.youbiao)
//                .getWidth();// 获取图片宽度
//
//        DisplayMetrics dm = new DisplayMetrics();
//        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
//        int screenW = dm.widthPixels;// 获取分辨率宽度
//        offset = (screenW / fragments.size() - bmpw) / 2;// 计算偏移量
//
//
//        Matrix matrix = new Matrix();
//        matrix.postTranslate(offset, 0);
//        cursor.setImageMatrix(matrix);// 设置动画初始位置
//    }
//
//    private void InitTextView() {//初始化页卡
//        t1 = (TextView)rootView.findViewById(R.id.text1);
//        t2 = (TextView)rootView.findViewById(R.id.text2);
//        t3 = (TextView)rootView.findViewById(R.id.text3);
//        t4 = (TextView)rootView.findViewById(R.id.text4);
//
//        t1.setOnClickListener(new MyOnClickListener(0));
//        t2.setOnClickListener(new MyOnClickListener(1));
//        t3.setOnClickListener(new MyOnClickListener(2));
//        t4.setOnClickListener(new MyOnClickListener(3));
//    }
//       //页卡监听器
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
//
//    //viewpaper里的frag的监听
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
//                    else if(currIndex ==3){
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

}
