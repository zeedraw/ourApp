package com.example.administrator.ourapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.administrator.ourapp.ViewPaperCycle.ADInfo;
import com.example.administrator.ourapp.ViewPaperCycle.CycleViewPager;
import com.example.administrator.ourapp.ViewPaperCycle.ViewFactory;
import com.example.administrator.ourapp.message.MesFrag;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import java.util.ArrayList;
import java.util.List;

import static cn.bmob.v3.Bmob.getApplicationContext;

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
    private List<ImageView> views = new ArrayList<ImageView>();
    private List<ADInfo> infos = new ArrayList<ADInfo>();
    private CycleViewPager cycleViewPager;

    private String[] imageUrls = {"http://attach.foyuan.net/portal/201404/04/09/201404040915161651.jpg",
            "http://img.tuku.cn/file_big/201505/ef01fc430ac646d6bd142adb08a439c7.jpg",
            "http://image5.tuku.cn/pic/wallpaper/fengjing/langmandushiweimeisheyingkuanpingbizhi/006.jpg",
            "http://www.qqpk.cn/Article/UploadFiles/201205/20120520122144808.jpg",
            };


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
        tab.setSelectedIndicatorColors(R.color.blue);//滑动条颜色
        //设定适配器
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(4);
        tab.setCustomTabView(R.layout.tab, 0);
        tab.setViewPager(viewPager);
//        viewPager.setOnPageChangeListener(new MyPageChangeListener());
//        viewPager.setCurrentItem(0);

        configImageLoader();
        initialize();



        return  rootView;
    }

    private void initialize() {

        cycleViewPager = (CycleViewPager)getActivity().getFragmentManager()
                .findFragmentById(R.id.fragment_cycle_viewpager_content);

        for(int i = 0; i < imageUrls.length; i ++){
            ADInfo info = new ADInfo();
            info.setUrl(imageUrls[i]);
            info.setContent("图片-->" + i );
            infos.add(info);
        }

        // 将最后一个ImageView添加进来
        views.add(ViewFactory.getImageView(getContext(), infos.get(infos.size() - 1).getUrl()));
        for (int i = 0; i < infos.size(); i++) {
            views.add(ViewFactory.getImageView(getContext(), infos.get(i).getUrl()));
        }
        // 将第一个ImageView添加进来
        views.add(ViewFactory.getImageView(getContext(), infos.get(0).getUrl()));

        // 设置循环，在调用setData方法前调用
        cycleViewPager.setCycle(true);

        // 在加载数据前设置是否循环
        cycleViewPager.setData(views, infos, mAdCycleViewListener);
        //设置轮播
        cycleViewPager.setWheel(true);

        // 设置轮播时间，默认5000ms
        cycleViewPager.setTime(7000);
        //设置圆点指示图标组居中显示，默认靠右
        cycleViewPager.setIndicatorCenter();
    }

    /**
     * 配置ImageLoder
     */
    private void configImageLoader() {
        // 初始化ImageLoader
        DisplayImageOptions options = new DisplayImageOptions.Builder().showStubImage(R.drawable.icon_stub) // 设置图片下载期间显示的图片
                .showImageForEmptyUri(R.drawable.icon_empty) // 设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.drawable.icon_error) // 设置图片加载或解码过程中发生错误显示的图片
                .cacheInMemory(true) // 设置下载的图片是否缓存在内存中
                .cacheOnDisc(true) // 设置下载的图片是否缓存在SD卡中
                // .displayer(new RoundedBitmapDisplayer(20)) // 设置成圆角图片
                .build(); // 创建配置过得DisplayImageOption对象

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext()).defaultDisplayImageOptions(options)
                .threadPriority(Thread.NORM_PRIORITY - 2).denyCacheImageMultipleSizesInMemory()
                .discCacheFileNameGenerator(new Md5FileNameGenerator()).tasksProcessingOrder(QueueProcessingType.LIFO).build();
        ImageLoader.getInstance().init(config);
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
    private CycleViewPager.ImageCycleViewListener mAdCycleViewListener = new CycleViewPager.ImageCycleViewListener() {

        @Override
        public void onImageClick(ADInfo info, int position, View imageView) {
            if (cycleViewPager.isCycle()) {
                position = position - 1;
                Toast.makeText(getContext(),
                        "position-->" + info.getContent(), Toast.LENGTH_SHORT)
                        .show();
            }

        }

    };
    public void refreshWithCityLimit(String str)
    {
        if (fragments.size()!=0) {
            Frag_edu edu=(Frag_edu) fragments.get(0);
            Frag_activity activity=(Frag_activity)fragments.get(1);
            Frag_trans trans=(Frag_trans)fragments.get(2);
            Frag_community community=(Frag_community)fragments.get(3);
            edu.setCityLimit(str);
            edu.initListView();
            activity.setCityLimit(str);
            activity.initListView();
            trans.setCityLimit(str);
            trans.initListView();
            community.setCityLimit(str);
            community.initListView();
        }
    }

}
