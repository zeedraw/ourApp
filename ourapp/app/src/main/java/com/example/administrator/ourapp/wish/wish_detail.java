package com.example.administrator.ourapp.wish;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.administrator.ourapp.MyUser;
import com.example.administrator.ourapp.R;
import com.example.administrator.ourapp.question_and_answer.Mission_question;

import cn.bmob.v3.BmobUser;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

/**
 * Created by Longze on 2016/12/13.
 */
public class wish_detail extends SwipeBackActivity {
    TextView return_bt;//标题上的返回按钮
    TextView info_title;//标题
    TextView wish_content;
    TextView wish_title;
    TextView wish_date;
    TextView completed;
    private Wish wish;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wish_detail);
        initWidget();
    }//onCreate

    private void initWidget() {
        return_bt = (TextView) findViewById(R.id.lbt);
        info_title = (TextView) findViewById(R.id.mission_title);
        wish_title = (TextView) findViewById(R.id.wish_title);
        wish_content = (TextView) findViewById((R.id.wish_content));
        wish_date = (TextView) findViewById(R.id.wish_date);
        completed = (TextView) findViewById(R.id.completed);
        Intent intent = getIntent();
        return_bt.setText("返回");
        info_title.setText("心愿详情");
        wish = (Wish)intent.getSerializableExtra("wish");

//TODO 解决 include问题后 取消注释
//       if(wish.getWish_user().getObjectId().equals(BmobUser.getCurrentUser(MyUser.class).getObjectId())){
//            completed.setVisibility(View.VISIBLE);
//        }//if

        if(wish.is_finished()){
            completed.setBackgroundColor(getResources().getColor(R.color.colorGrey2));
        }
        wish_title.setText(wish.getTitle());
        wish_content.setText(wish.getContent());
        wish_date.setText(wish.getCreatedAt());

        return_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                wish_detail.this.finish();
            }
        });

        completed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO 将状态改为已完成
            }
        });
    }//initWidget
}
