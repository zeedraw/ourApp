package com.example.administrator.ourapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by Administrator on 2016/10/4.
 */

public class EditIntro extends AppCompatActivity {
    private TextView rt,title,complete;
    private EditText et;
    private final static int GET_INTRO=1001;
    private final static int GET_DETAIL_LOCATION=1;
    private final static int GET_DETAIL=3;
    private final static int GET_OTHER=1003;
    private String key;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_intro);
        Intent intent=getIntent();
        key=intent.getStringExtra("from");
        title=(TextView)findViewById(R.id.mission_title);
        title.setText("编辑信息");

        complete=(TextView)findViewById(R.id.rbt);
        complete.setText("完成");
        complete.setVisibility(View.INVISIBLE);
        complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendResult();
                finish();
            }
        });

        et=(EditText)findViewById(R.id.edit_intro);

        String mes=intent.getStringExtra("mes");
        if(mes!=null)
        {
            mes=mes.trim();
            et.requestFocus();
            et.setText(mes);
            et.setSelection(mes.length());

        }
        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                complete.setVisibility(View.VISIBLE);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });




        rt=(TextView)findViewById(R.id.lbt);
        rt.setText("返回");
        rt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (complete.getVisibility()==View.VISIBLE)
                {
                    AlertDialog.Builder builder=new AlertDialog.Builder(EditIntro.this);
                    builder.setMessage("是否保存");
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                                        sendResult();
                                    finish();
                        }
                    });
                    builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            finish();
                        }
                    });
                    builder.create().show();
                }
                else
                {
                    finish();
                }
            }
        });


    }

    private void sendResult()
    {
        Intent intent=new Intent();
        intent.putExtra("info",et.getText().toString().trim());
        if (key.equals("for_intro")) {
            setResult(GET_INTRO, intent);
        }
        else if(key.equals("for_detail_location"))
        {
            setResult(GET_DETAIL_LOCATION, intent);
        }
        else if(key.equals("for_detail"))
        {
            setResult(GET_DETAIL,intent);
        }
        if (key.equals("for_other")){
            setResult(GET_OTHER,intent);
        }
    }

}
