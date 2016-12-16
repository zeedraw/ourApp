package com.example.administrator.ourapp.wish;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.ourapp.MainActivity;
import com.example.administrator.ourapp.MyUser;
import com.example.administrator.ourapp.R;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

/**
 * Created by Longze on 2016/12/14.
 */
public class make_wish_personnal extends SwipeBackActivity {
    private EditText EditTitle = null;   //问题编辑文本框
    private EditText EditContent = null;   //问题编辑文本框
    private TextView submit = null; //提交按钮
    private TextView return_bt,commit_bt;//标题上的左右按钮
    private TextView info_title;//标题
    private Wish wish = new Wish();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.make_wish);
        initWidget();
    }//onCreate

    private void initWidget() {
        EditTitle = (EditText) findViewById(R.id.edit_title);
        EditContent = (EditText) findViewById(R.id.edit_content);
        submit = (TextView) findViewById(R.id.submit);
        return_bt = (TextView) findViewById(R.id.lbt);
        info_title = (TextView) findViewById(R.id.mission_title);

        return_bt.setText("返回");
        info_title.setText("许愿");



        return_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                make_wish_personnal.this.finish();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(EditTitle.length() < 4){
                    Toast.makeText(make_wish_personnal.this, "心愿的标题不得少于4个字符", Toast.LENGTH_SHORT).show();
                    return;
                }else if(EditTitle.length() > 20){
                    Toast.makeText(make_wish_personnal.this, "标题超过字数限制", Toast.LENGTH_SHORT).show();
                    return;
                }else if(EditContent.length() < 10){
                    Toast.makeText(make_wish_personnal.this, "心愿的内容不得少于10个字符", Toast.LENGTH_SHORT).show();
                    return;
                }else if(EditContent.length() > 200){
                    Toast.makeText(make_wish_personnal.this, "内容超过字数限制", Toast.LENGTH_SHORT).show();
                    return;
                }//else

                final Dialog loading_dialog = MainActivity.createLoadingDialog(make_wish_personnal.this);
                loading_dialog.show();
                wish.setContent(EditContent.getText().toString());
                wish.setWish_user(BmobUser.getCurrentUser(MyUser.class));
                //TODO user传不到后台
                wish.setType(1); //从许愿入口进 为个人心愿
                wish.setIs_finished(false);
                wish.setTitle(EditTitle.getText().toString());
//                wish.setLocation();
                wish.save(new SaveListener<String>() {
                    @Override
                    public void done(String objectId, BmobException e) {
                        if(e==null){
                            Log.i("bmob","上传心愿成功");
                            loading_dialog.dismiss();
                            Toast.makeText(make_wish_personnal.this, "许愿成功", Toast.LENGTH_SHORT).show();
                            make_wish_personnal.this.finish();
                        }else{
                            loading_dialog.dismiss();
                            Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                            Toast.makeText(make_wish_personnal.this, "失败:" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });


            }//onClick
        });//submit onClick

    }//initWidget
}
