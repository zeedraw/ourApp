package com.example.administrator.ourapp;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.example.administrator.ourapp.message.Message_tools;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

/**
 * Created by Administrator on 2016/9/17.
 */
public class MissionPub extends SwipeBackActivity implements DatePickerDialog.OnDateSetListener, DialogInterface.OnClickListener {
    private TextView tag,start_time,end_time,rt,save,title,location,detail_location,detail_mission;
    private EditText name,intro, contact_number;
    private DatePickerDialog start_dpl,end_dpl;
    private LocationPickerDialog locationPickerDialog;//选择地点
    private Calendar calendar=Calendar.getInstance();

    private final static int FOR_DETAIL_LOCATION=0;
    private final static int GET_DETAIL_LOCATION=1;
    private final static int FOR_DETAIL=2;
    private final static int GET_DETAIL=3;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.missionpub);
        initWidget();
    }

    private void initWidget()
    {

        tag=(TextView)findViewById(R.id.mission_pub_tag_tv);
        BmobQuery<MyUser> query=new BmobQuery<MyUser>();
        query.getObject(BmobUser.getCurrentUser().getObjectId(), new QueryListener<MyUser>() {
            @Override
            public void done(MyUser myUser, BmobException e) {
                if (e==null)
                {
                    tag.setText(myUser.getTag());
                }
                else
                {
                    tag.setText((String)BmobUser.getObjectByKey("tag"));
                }
            }
        });


        name=(EditText)findViewById(R.id.mission_pub_name_et);
        intro=(EditText)findViewById(R.id.mission_pub_intro_et);
        contact_number=(EditText)findViewById(R.id.contact_number);
        location=(TextView)findViewById(R.id.location_tv);
        detail_location=(TextView)findViewById(R.id.detail_location_tv);

        start_time=(TextView)findViewById(R.id.start_tv);
        end_time=(TextView)findViewById(R.id.end_tv);


        detail_mission=(TextView)findViewById(R.id.detail);

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
        title.setText("发布任务");
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
        if (isEmpty(name)&&isEmpty(intro)&&isEmpty(location)&&isEmpty(detail_location)&&isEmpty(start_time)&&isEmpty(end_time)&&isEmpty(detail_mission))
        {
            final Dialog dialog=MainActivity.createLoadingDialog(MissionPub.this);
            dialog.show();
            Mission mission=new Mission();
            mission.setName(name.getText().toString().trim());
            mission.setIntro(intro.getText().toString().trim());
            mission.setContact_number(contact_number.getText().toString().trim());
            String[] sourceStrArray = location.getText().toString().trim().split("-");
            List<String> list= new ArrayList<String>();
            list.add(sourceStrArray[0]);
            list.add(sourceStrArray[1]);
            list.add(sourceStrArray[2]);
            mission.setLocation_abs(list);
            mission.setPub_user(BmobUser.getCurrentUser(MyUser.class));
            mission.setLocation(detail_location.getText().toString().trim());
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            mission.setPub_time(df.format(new Date()));
            mission.setStart_time(start_time.getText().toString().trim());
            mission.setEnd_time(end_time.getText().toString().trim());
            mission.setState(new Integer(1));
            mission.setDetail(detail_mission.getText().toString().trim());
            mission.setTag(tag.getText().toString().trim());

            mission.save(new SaveListener<String>() {
                @Override
                public void done(String s, BmobException e) {
                    AlertDialog.Builder builder=new AlertDialog.Builder(MissionPub.this);
                    if (e==null)
                    {
                        dialog.dismiss();
                        MyUser zeedraw = new MyUser();
                        zeedraw.setObjectId("TJRU555B");
                        Message_tools mt = new Message_tools();
                        mt.send(BmobUser.getCurrentUser(MyUser.class), zeedraw,
                                BmobUser.getCurrentUser(MyUser.class).getObjectId() + "的审核要求",
                                17, false, s, MissionPub.this);
                        builder.setMessage("提交成功，请等待我们的审核结果").setCancelable(false).setPositiveButton("确定", new DialogInterface.OnClickListener() {
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
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        if (datePicker==start_dpl.getDatePicker())
        {
            if (i1<9) {
                if (i2<10) {
                    start_time.setText(i + "-0" + (i1 + 1) + "-0" + i2);
                }
                else {
                    start_time.setText(i + "-0" + (i1 + 1) + "-" + i2);
                }
            }
            else {
                if (i2<10) {
                    start_time.setText(i + "-" + (i1 + 1) + "-0" + i2);
                }
                else
                {
                    start_time.setText(i + "-" + (i1 + 1) + "-" + i2);
                }

            }
        }

        else if (datePicker==end_dpl.getDatePicker())
        {
            if (i1<9) {
                if (i2<10) {
                    end_time.setText(i + "-0" + (i1 + 1) + "-0" + i2);
                }
                else {
                    end_time.setText(i + "-0" + (i1 + 1) + "-" + i2);
                }
            }
            else {
                if (i2<10) {
                    end_time.setText(i + "-" + (i1 + 1) + "-0" + i2);
                }
                else
                {
                    end_time.setText(i + "-" + (i1 + 1) + "-" + i2);
                }

            }
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
            detail_mission.setText(info);
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
    public void startTimeClick(View view)
    {
        start_dpl=new DatePickerDialog(MissionPub.this,MissionPub.this,calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH)+1,calendar.get(Calendar.DATE));
        start_dpl.setTitle("请设置开始时间");
        start_dpl.show();
    }

    public void endTimeClick(View view)
    {
        end_dpl=new DatePickerDialog(MissionPub.this,MissionPub.this,calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH)+1,calendar.get(Calendar.DATE));
        end_dpl.setTitle("请设置结束时间");
        end_dpl.show();
    }

    public void detailClick(View view)
    {
        Intent intent=new Intent(this, EditIntro.class);
        intent.putExtra("from","for_detail");
        intent.putExtra("mes",detail_mission.getText().toString().trim());
        intent.putExtra("length",1000);
        startActivityForResult(intent,FOR_DETAIL);
    }
}
