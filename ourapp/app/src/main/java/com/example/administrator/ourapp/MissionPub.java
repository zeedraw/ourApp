package com.example.administrator.ourapp;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by Administrator on 2016/9/17.
 */
public class MissionPub extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    private TextView tag,start_time,end_time,rt,save,title;
    private EditText name,location,needppeople,details;
    private DatePickerDialog start_dpl,end_dpl;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mission_publish);
        initWidget();
    }

    private void initWidget()
    {

        tag=(TextView)findViewById(R.id.mission_pub_tag_tv);
        tag.setText((String)BmobUser.getObjectByKey("tag"));

        name=(EditText)findViewById(R.id.mission_pub_name_et);

        location=(EditText)findViewById(R.id.mission_pub_location_et);

        needppeople=(EditText)findViewById(R.id.mission_pub_needpeople_et);

        final Calendar calendar=Calendar.getInstance();
        start_time=(TextView)findViewById(R.id.mission_pub_start_et);
        start_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                start_dpl=new DatePickerDialog(MissionPub.this,MissionPub.this,calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH)+1,calendar.get(Calendar.DATE));
                start_dpl.setTitle("请设置开始时间");
                start_dpl.show();
            }
        });

        end_time=(TextView)findViewById(R.id.mission_pub_end_et);
        end_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                end_dpl=new DatePickerDialog(MissionPub.this,MissionPub.this,calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH)+1,calendar.get(Calendar.DATE));
                end_dpl.setTitle("请设置结束时间");
                end_dpl.show();
            }
        });

        details=(EditText)findViewById(R.id.mission_pub_details_et);

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

        title=(TextView)findViewById(R.id.title);
        title.setText("发布任务");
    }

    private boolean isEmpty(TextView view)
    {
        if (view.getText().toString().length()==0)
        {
           AlertDialog.Builder builder=new AlertDialog.Builder(this);
            builder.setMessage("信息未填写完整");
            return false;
        }
        else
        {
            return true;
        }
    }

    private void publish()
    {
        if (isEmpty(name)&&isEmpty(location)&&isEmpty(needppeople)&&isEmpty(start_time)&&isEmpty(end_time)&&isEmpty(details))
        {
            Mission mission=new Mission();
            mission.setName(name.getText().toString().trim());
            mission.setNeed_people(Integer.valueOf(needppeople.getText().toString()));
            mission.setDetail(details.getText().toString().trim());
            mission.setLocation(location.getText().toString().trim());
            mission.setPub_user(BmobUser.getCurrentUser(MyUser.class));
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            mission.setPub_time(df.format(new Date()));
            mission.setStart_time(start_time.getText().toString().trim());
            mission.setEnd_time(end_time.getText().toString().trim());
            mission.setState(new Integer(1));
            mission.setTag(tag.getText().toString().trim());

            mission.save(new SaveListener<String>() {
                @Override
                public void done(String s, BmobException e) {
                    AlertDialog.Builder builder=new AlertDialog.Builder(MissionPub.this);
                    if (e==null)
                    {
                        builder.setMessage("发布成功").setCancelable(false).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                finish();
                            }
                        }).create().show();

                    }
                    else
                    {
                        builder.setMessage("发布失败").create().show();
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
}
