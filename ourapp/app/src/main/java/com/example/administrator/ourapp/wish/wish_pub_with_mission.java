package com.example.administrator.ourapp.wish;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.example.administrator.ourapp.EditIntro;
import com.example.administrator.ourapp.LocationPickerDialog;
import com.example.administrator.ourapp.MainActivity;
import com.example.administrator.ourapp.Mission;
import com.example.administrator.ourapp.MyLocationUtils.MyLocation;
import com.example.administrator.ourapp.MyUser;
import com.example.administrator.ourapp.R;
import com.example.administrator.ourapp.message.Message_tools;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobGeoPoint;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

/**
 * Created by Longze on 2016/12/15.
 */
public class wish_pub_with_mission extends SwipeBackActivity implements DialogInterface.OnClickListener{
    private TextView rt,save,title,location,detail_location, wish_detail;
    private EditText wish_title, contact_number;
    private DatePickerDialog start_dpl,end_dpl;
    private LocationPickerDialog locationPickerDialog;//选择地点
    private Calendar calendar=Calendar.getInstance();
    private BmobGeoPoint bgp;
    private Mission mission;
    private final static int FOR_DETAIL_LOCATION=0;
    private final static int GET_DETAIL_LOCATION=1;
    private final static int FOR_DETAIL=2;
    private final static int GET_DETAIL=3;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wish_pub_with_mission);
        initWidget();
    }

    private void initWidget()
    {


        Bundle bundle = new Bundle();
        bundle = this.getIntent().getExtras();
        mission = (Mission) bundle.getSerializable("mission");
        wish_title =(EditText)findViewById(R.id.wish_title);
        contact_number=(EditText)findViewById(R.id.contact_number);
        location=(TextView)findViewById(R.id.location);
        detail_location=(TextView)findViewById(R.id.detail_location);
        wish_detail =(TextView)findViewById(R.id.wish_detail);
        MyLocation myLocation=new MyLocation();
        myLocation.startLocation(wish_pub_with_mission.this);
        location.setText(myLocation.getLocationInfo());
        bgp = myLocation.getMyPoint();
        rt=(TextView)findViewById(R.id.lbt);
        rt.setText("返回");
        rt.setVisibility(View.VISIBLE);
        rt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        save=(TextView)findViewById(R.id.rbt);
        save.setText("完成");
        save.setVisibility(View.VISIBLE);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                publish();
            }
        });

        title=(TextView)findViewById(R.id.mission_title);
        title.setText("提交心愿");
    }

    private boolean isEmpty(TextView view)
    {
        if (view.getText().toString().length()==0)
        {
            AlertDialog.Builder builder=new AlertDialog.Builder(this);
            builder.setMessage("信息未填写完整");
            builder.create().show();
            return false;
        }
        else
        {
            return true;
        }
    }

    private void publish()
    {
        if (isEmpty(wish_title)&&isEmpty(wish_detail))
        {
            final Dialog dialog= MainActivity.createLoadingDialog(wish_pub_with_mission.this);
            dialog.show();
            Wish wish = new Wish();
            //TODO 上传联系方式 组织 任务
            wish.setTitle(wish_title.getText().toString().trim());
//            wish.set(contact_number.getText().toString().trim());
//            String[] sourceStrArray = location.getText().toString().trim().split("-");
//            List<String> list= new ArrayList<String>();
//            list.add(sourceStrArray[0]);
//            list.add(sourceStrArray[1]);
//            list.add(sourceStrArray[2]);
//            wish.setLocation_abs(list);
            wish.setMission(mission);
            wish.setWish_user(BmobUser.getCurrentUser(MyUser.class));
//            wish.setLocation(detail_location.getText().toString().trim());
            wish.setLocation(bgp);
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            wish.setType(new Integer(2)); //在任务里提出算2
            wish.setAudit_status(1);
            wish.setContent(wish_detail.getText().toString().trim());

            wish.save(new SaveListener<String>() {
                @Override
                public void done(String s, BmobException e) {
                    AlertDialog.Builder builder=new AlertDialog.Builder(wish_pub_with_mission.this);
                    if (e==null)
                    {
                        dialog.dismiss();
                        //TODO 给任务发布者发送消息
//                        MyUser zeedraw = new MyUser();
//                        zeedraw.setObjectId("TJRU555B");
                        Message_tools mt = new Message_tools();
                        mt.send(BmobUser.getCurrentUser(MyUser.class), mission.getPub_user(),
                                BmobUser.getCurrentUser(MyUser.class).getUsername() + "的许愿",
                                20, false, s, wish_pub_with_mission.this);
                        builder.setMessage("提交成功，请等待任务发布者的审核结果").setCancelable(false).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                finish();
                            }
                        }).create().show();

                    }
                    else
                    {
                        builder.setMessage("发布失败"+e.getMessage()).create().show();
                        dialog.dismiss();
                    }
                }
            });

        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==FOR_DETAIL_LOCATION&&resultCode==GET_DETAIL_LOCATION)
        {
            String info=data.getStringExtra("info");
            detail_location.setText(info);
        }
        else if (requestCode==FOR_DETAIL&&resultCode==GET_DETAIL)
        {
            String info=data.getStringExtra("info");
            wish_detail.setText(info);
        }
    }

    public void locationClick(View view)
    {
        locationPickerDialog=new LocationPickerDialog(this,this);
        locationPickerDialog.show();
    }
    public void onClick(DialogInterface dialogInterface, int i) {
        location.setText(locationPickerDialog.getLocation());
    }

    public void detailLocationClick(View view)
    {
        Intent intent=new Intent(this, EditIntro.class);
        intent.putExtra("from","for_detail_location");
        intent.putExtra("length",40);
        intent.putExtra("mes",detail_location.getText().toString().trim());
        startActivityForResult(intent,FOR_DETAIL_LOCATION);
    }


    public void detailClick(View view)
    {
        Intent intent=new Intent(this, EditIntro.class);
        intent.putExtra("from","for_detail");
        intent.putExtra("mes", wish_detail.getText().toString().trim());
        intent.putExtra("length",1000);
        startActivityForResult(intent,FOR_DETAIL);
    }
}
