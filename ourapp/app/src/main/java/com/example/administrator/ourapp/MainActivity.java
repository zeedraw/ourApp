package com.example.administrator.ourapp;


import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.ourapp.authenticate.agency_authenticate;
import com.example.administrator.ourapp.authenticate.real_name_authenticate;
import com.example.administrator.ourapp.friends.FreFrag;
import com.example.administrator.ourapp.friends.search_user;
import com.example.administrator.ourapp.message.MesFrag;
import com.example.administrator.ourapp.message.Message_tools;
import com.example.administrator.ourapp.user_information.MyAccount;
import com.example.administrator.ourapp.user_information.other_information;

import java.util.List;

import cn.bmob.push.BmobPush;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;


public class MainActivity extends FragmentActivity implements View.OnClickListener{
    private RelativeLayout tv_main,tv_mes,tv_mine,tv_fre;//下方的3个tab
    private TextView title;//上方标题
    private ImageView mesSignal;
//    private FragmentManager mfragManager;
//    private FragmentTransaction mTransaction;
    private TextView r_button;
    private Integer state_stu;
    private Integer state_pub;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //第一：默认初始化Bmob
        Bmob.initialize(this, "f7ff174553704fa24b1a4f83dea2e4aa");
        setContentView(R.layout.maininterface);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//设置竖屏

        // 初始化BmobSDK
//        Bmob.initialize(this, "f7ff174553704fa24b1a4f83dea2e4aa");
//        // 使用推送服务时的初始化操作
//        BmobInstallation.getCurrentInstallation().save1();
//        // 启动推送服务
//        BmobPush.startWork(this);

        initWidget();



    }
    private  void initWidget(){
        //新消息的提示红点
        mesSignal=(ImageView)findViewById(R.id.mesSignal);
        //下部tabs中的3个组件
        tv_main=(RelativeLayout) findViewById(R.id.main);
        tv_main.setOnClickListener(this);
        tv_mes=(RelativeLayout) findViewById(R.id.mes);
        tv_mes.setOnClickListener(this);
        tv_mine=(RelativeLayout) findViewById(R.id.mine);
        tv_mine.setOnClickListener(this);
        tv_fre=(RelativeLayout) findViewById(R.id.fre);
        tv_fre.setOnClickListener(this);

        //设置activity的标题
        title=(TextView)findViewById(R.id.title);
        title.setText("首页");

        //默认首页
        tv_main.setSelected(true);
        myCheckedChaged(tv_main);
//        FragmentManager mfragManager=getSupportFragmentManager();
//       FragmentTransaction  mTransaction=mfragManager.beginTransaction();
//        mTransaction.add(R.id.frag_container,new MineFrag(),"mine")
//                .add(R.id.frag_container,new MesFrag(),"mes")
//                .add(R.id.frag_container,new FreFrag(),"fre")
//                .add(R.id.frag_container,new MainFrag(),"main")
//                .commit();




        r_button=(TextView)findViewById(R.id.rbt);

    }



    @Override
    public void onClick(View view) {//下部tab的监听事件

        if(view==tv_main)
        {
            tv_main.setSelected(true);
            tv_mes.setSelected(false);
            tv_mine.setSelected(false);
            tv_fre.setSelected(false);
            title.setText("首页");
            r_button.setVisibility(View.INVISIBLE);
            myCheckedChaged(view);
//            mTransaction=mfragManager.beginTransaction();
//            mTransaction.show(mfragManager.findFragmentByTag("main"))
//                    .hide(mfragManager.findFragmentByTag("mes"))
//                    .hide(mfragManager.findFragmentByTag("mine"))
//                    .hide(mfragManager.findFragmentByTag("fre")).commit();

        }

        else if (view==tv_mes)
        {
            if(BmobUser.getCurrentUser() == null){
                //TODO 新的界面
            }
            tv_main.setSelected(false);
            tv_mes.setSelected(true);
            tv_mine.setSelected(false);
            tv_fre.setSelected(false);
            title.setText("消息");
            r_button.setVisibility(View.INVISIBLE);
            myCheckedChaged(view);
//            mTransaction=mfragManager.beginTransaction();
//            mTransaction.hide(mfragManager.findFragmentByTag("main"))
//                    .show(mfragManager.findFragmentByTag("mes"))
//                    .hide(mfragManager.findFragmentByTag("mine"))
//                    .hide(mfragManager.findFragmentByTag("fre")).commit();

        }
        else if (view==tv_mine)
        {
            tv_main.setSelected(false);
            tv_mes.setSelected(false);
            tv_mine.setSelected(true);
            tv_fre.setSelected(false);
            title.setText("我的");
            r_button.setVisibility(View.INVISIBLE);
            myCheckedChaged(view);
//            mTransaction=mfragManager.beginTransaction();
//            mTransaction.hide(mfragManager.findFragmentByTag("main"))
//                    .hide(mfragManager.findFragmentByTag("mes"))
//                    .show(mfragManager.findFragmentByTag("mine"))
//                    .hide(mfragManager.findFragmentByTag("fre")).commit();

        }

        else if(view==tv_fre)
        {
            tv_fre.setSelected(true);
            tv_main.setSelected(false);
            tv_mine.setSelected(false);
            tv_mes.setSelected(false);
            title.setText("好友");
            r_button.setText("添加");
            r_button.setVisibility(View.VISIBLE);
            myCheckedChaged(view);
            r_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ComponentName comp=new ComponentName(MainActivity.this,search_user.class);
                    Intent intent=new Intent();
                    intent.setComponent(comp);
                    startActivity(intent);
                }
            });

//            mTransaction=mfragManager.beginTransaction();
//            mTransaction.hide(mfragManager.findFragmentByTag("main"))
//                    .hide(mfragManager.findFragmentByTag("mes"))
//                    .hide(mfragManager.findFragmentByTag("mine"))
//                    .show(mfragManager.findFragmentByTag("fre")).commit();

        }


    }

    private void myCheckedChaged(View view)
    {
        FragmentManager fm=getSupportFragmentManager();
        FragmentTransaction ft=fm.beginTransaction();
        Fragment mine=fm.findFragmentByTag("mine");
        Fragment main=fm.findFragmentByTag("main");
        Fragment mes=fm.findFragmentByTag("mes");
        Fragment fre=fm.findFragmentByTag("fre");

        if (mine!=null)
        {
            ft.hide(mine);
        }
        if (main!=null)
        {
            ft.hide(main);
        }
        if (mes!=null)
        {
            ft.hide(mes);
        }
        if (fre!=null)
        {
            ft.hide(fre);
        }
        //判断显示的frag
        if (view==tv_main)
        {
            if (main==null)
            {
                main=new MainFrag();
                ft.add(R.id.frag_container,main,"main");
            }
            else
            {
                ft.show(main);
            }
        }

        else if(view==tv_mes)
        {
            if (mes==null)
            {
                mes=new MesFrag();
                ft.add(R.id.frag_container,mes,"mes");
            }
            else
            {

                ft.show(mes);
                if (mesSignal.getVisibility()==View.VISIBLE) {
                    ((MesFrag) mes).hasNesMesRefresh();
//                    mesSignal.setVisibility(View.INVISIBLE);
                }

            }
        }

        else if (view==tv_mine)
        {
            if (mine==null)
            {
                mine=new MineFrag();
                ft.add(R.id.frag_container,mine,"mine");
            }
            else
            {
                ft.show(mine);
            }
        }

        else if(view==tv_fre)
        {
            if (fre==null)
            {
                fre=new FreFrag();
                ft.add(R.id.frag_container,fre,"fre");
            }
            else
            {
                ft.show(fre);
            }
        }

        ft.commit();

    }



    //以下五个方法对应MineFrag中的点击事件
    public void Myaccount_click(View view)
{
    MyUser user= BmobUser.getCurrentUser(MyUser.class);
    if(user!=null) {
        ComponentName comp = new ComponentName(MainActivity.this, MyAccount.class);
        Intent intent = new Intent();
        intent.setComponent(comp);
        startActivity(intent);
    }
    else
    {
        Intent intent=new Intent(MainActivity.this,Login.class);
        startActivity(intent);
    }
}

    public void Myauthenticate_click(View view)
    {

        /**
         * 显示选择实名认证还是机构认证的对话框
         */
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("请选择");
        String[] items = { "实名认证", "机构认证" };
        builder.setNegativeButton("取消", null);
        builder.setItems(items, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, final int which) {

                BmobQuery<MyUser> query = new BmobQuery<MyUser>();
                query.getObject(BmobUser.getCurrentUser(MyUser.class).getObjectId(), new QueryListener<MyUser>() {

                    @Override
                    public void done(MyUser object, BmobException e) {
                        if(e==null){
                            state_stu = object.getIdent_state_stu();
                            state_pub = object.getIdent_state_pub();

                            switch (which) {
                                case 0: // 选择实名认证
                                    if(state_stu == 1){
//                                        AlertDialog.Builder builder = new AlertDialog.Builder(getBaseContext());
//                                        builder.setMessage("正在认证");
                                        Toast.makeText(getBaseContext(), "认证已上传，正在审核中", Toast.LENGTH_SHORT).show();

                                    }
                                    else if(state_stu == 2){
//                                        AlertDialog.Builder builder = new AlertDialog.Builder(getBaseContext());
//                                        builder.setMessage("认证已通过，不能重复认证");
                                        Toast.makeText(getBaseContext(), "认证已通过，不能重复认证", Toast.LENGTH_SHORT).show();

                                    }else{
                                        ComponentName comp=new ComponentName(MainActivity.this,real_name_authenticate.class);
                                        Intent intent=new Intent();
                                        intent.setComponent(comp);
                                        startActivity(intent);
                                    }
                                    break;
                                case 1: // 选择机构认证
                                    if(state_pub == 1){
//                                        AlertDialog.Builder builder = new AlertDialog.Builder(getBaseContext());
//                                        builder.setMessage("正在认证");
                                        Toast.makeText(getBaseContext(), "认证已上传，正在审核中", Toast.LENGTH_SHORT).show();
                                    }
                                    else if(state_pub == 2){
//                                        AlertDialog.Builder builder = new AlertDialog.Builder(getBaseContext());
//                                        builder.setMessage("认证已通过，不能重复认证");
                                        Toast.makeText(getBaseContext(), "认证已通过，不能重复认证", Toast.LENGTH_SHORT).show();

                                    }else{
                                        ComponentName comp=new ComponentName(MainActivity.this,agency_authenticate.class);
                                        Intent intent=new Intent();
                                        intent.setComponent(comp);
                                        startActivity(intent);
                                    }
                                    break;
                            }//swtich


                        }else{
                            Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                        }
                    }

                });

            }//onClick
        });
        builder.create().show();
    }


    public void Mysetting_click (View view)
    {
        MyUser user= BmobUser.getCurrentUser(MyUser.class);
        if(user!=null) {
            ComponentName comp = new ComponentName(MainActivity.this, MySetting.class);
            Intent intent = new Intent();
            intent.setComponent(comp);
            startActivity(intent);
        }
        else
        {
            Intent intent=new Intent(MainActivity.this,Login.class);
            startActivity(intent);
        }

    }

    public  void Mymssion_click(View view)
    {
        MyUser user= BmobUser.getCurrentUser(MyUser.class);
        if (user!=null) {
            ComponentName comp = new ComponentName(MainActivity.this, MyTask.class);
            Intent intent = new Intent();
            intent.setComponent(comp);
            startActivity(intent);
        }

        else {
            Intent intent=new Intent(MainActivity.this,Login.class);
            startActivity(intent);
        }
    }

    public void Myteam_click(View view)
    {
        MyUser user= BmobUser.getCurrentUser(MyUser.class);
        if (user!=null)
        {

        }
        else
        {
            Intent intent=new Intent(MainActivity.this,Login.class);
            startActivity(intent);
        }
    }

    //获取缓存路径
    public static String getDiskCacheDir(Context context) {
        String cachePath = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            cachePath = context.getExternalCacheDir().getPath();
        } else {
            cachePath = context.getCacheDir().getPath();
        }
        return cachePath;
    }

    public static String getDiskFileDir(Context context) {
        String filePath = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            filePath = context.getExternalFilesDir(null).getPath();
        } else {
            filePath = context.getFilesDir().getPath();
        }
        return filePath;
    }

    /**
     * 得到自定义的progressDialog
     * @param context
     * @return
     */
    //自定义的loading框
    public static Dialog createLoadingDialog(Context context) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.loadingdialog, null);// 得到加载view
        LinearLayout layout = (LinearLayout) v.findViewById(R.id.dialog_view);// 加载布局
        // main.xml中的ImageView
        ImageView spaceshipImage = (ImageView) v.findViewById(R.id.img);
        // 加载动画
        Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(
                context, R.anim.loading_animation);
        // 使用ImageView显示动画
        spaceshipImage.startAnimation(hyperspaceJumpAnimation);

        Dialog loadingDialog = new Dialog(context, R.style.loading_dialog);// 创建自定义样式dialog

        loadingDialog.setCancelable(false);// 不可以用“返回键”取消
        loadingDialog.setContentView(layout);// 设置布局
        return loadingDialog;

    }

    public void checkNewMes()
    {
        Message_tools mt = new Message_tools();
        Log.i("z","开始检测消息");

        BmobQuery<UserInfo> query = new BmobQuery<UserInfo>();
        query.addWhereEqualTo("user", BmobUser.getCurrentUser());
        query.setLimit(50);
        query.findObjects(new FindListener<UserInfo>() {
            @Override
            public void done(List<UserInfo> object, BmobException e) {
                if(e==null){
                    if(object.get(0).getUnread_message_num() > 0){
                        Log.i("z","检测到新消息");
                        mesSignal.setVisibility(View.VISIBLE);
                    }//if

                    else if (object.get(0).getUnread_message_num() == 0){
                        Log.i("z","检测到没有新消息");
                        mesSignal.setVisibility(View.INVISIBLE);
                    }//else if
                }else{
                    Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                }
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(BmobUser.getCurrentUser() != null){
            checkNewMes();
            FragmentManager fm=getSupportFragmentManager();
            Fragment mes=fm.findFragmentByTag("mes");
            if (mes!=null) {
                ((MesFrag) mes).hasNesMesRefresh();
            } //if mes!=null
        }//if

    }

    public void change_signal(){
        Log.i("z","没有未读消息");
        mesSignal.setVisibility(View.INVISIBLE);
    }//change_signal
}



