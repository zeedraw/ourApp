package com.example.administrator.ourapp.wish;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.ourapp.MissionInfo;
import com.example.administrator.ourapp.MyUser;
import com.example.administrator.ourapp.R;
import com.example.administrator.ourapp.question_and_answer.Mission_question;
import com.example.administrator.ourapp.user_information.MyAccount;

import java.io.InputStream;
import java.net.URL;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.UpdateListener;
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
    ImageView person_image;
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
        person_image = (ImageView) findViewById(R.id.person_image);
        Intent intent = getIntent();
        return_bt.setText("返回");
        info_title.setText("心愿详情");
        wish = (Wish)intent.getSerializableExtra("wish");
        new LoadImage().execute(wish.getWish_user().getUserimage().getUrl());


//TODO 解决 include问题后 取消注释
       if(wish.getWish_user().getObjectId().equals(BmobUser.getCurrentUser(MyUser.class).getObjectId()) && !wish.is_finished()){
           completed.setVisibility(View.VISIBLE);
           completed.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   //TODO 将状态改为已完成
                   Wish wish2 = new Wish();
                   wish2.setIs_finished(true);
                   wish2.update(wish.getObjectId(), new UpdateListener() {

                       @Override
                       public void done(BmobException e) {
                           if(e==null){
                               Log.i("bmob","更新成功");
                               wish_detail.this.finish();
                           }else{
                               Log.i("bmob","更新失败："+e.getMessage()+","+e.getErrorCode());
                           }
                       }
                   });
               }
           });
        }//if
        else if(!wish.getWish_user().getObjectId().equals(BmobUser.getCurrentUser(MyUser.class).getObjectId()) && !wish.is_finished()){
           completed.setVisibility(View.VISIBLE);
           completed.setText("联系他/她");
           completed.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   //TODO 直接联系
                   Intent intent=new Intent();
                   intent.setAction(Intent.ACTION_CALL);
                   intent.setData(Uri.parse("tel:" + wish.getContact_number()));
                   Log.i("naosumi","tel:"+wish.getContact_number());
                   //开启系统拨号器
                   startActivity(intent);
               }
           });
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





        person_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                BmobQuery<MyUser> query = new BmobQuery<MyUser>();
                query.getObject(wish.getWish_user().getObjectId(), new QueryListener<MyUser>() {

                    @Override
                    public void done(MyUser object, BmobException e) {
                        if(e==null){
                            Intent intent=new Intent(wish_detail.this,MyAccount.class);
                            Bundle bundle=new Bundle();
                            bundle.putSerializable("user", object);
                            intent.putExtras(bundle);
                            wish_detail.this.startActivity(intent);
                        }else{
                            Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                            Toast.makeText(wish_detail.this, "失败:" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                });


            }
        });
    }//initWidget



    public class LoadImage extends AsyncTask<String,Void,Drawable>
    {
        @Override
        protected Drawable doInBackground(String... strs) {
            URL request;
            InputStream input;
            Drawable drawable = null;

            try {
                request =new URL(strs[0]);
                input=(InputStream)request.getContent();
                drawable = Drawable.createFromStream(input, "src");
            } catch (Exception e) {
                e.printStackTrace();
            }

            return drawable;

        }

        @Override
        protected void onPostExecute(Drawable drawable) {
            person_image.setImageDrawable(drawable);
        }
    }
}
