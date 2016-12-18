package com.example.administrator.ourapp.wish;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.ourapp.MyUser;
import com.example.administrator.ourapp.R;
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
 * Created by Longze on 2016/12/17.
 */
public class wish_detail_for_audit extends SwipeBackActivity {
    TextView return_bt;//标题上的返回按钮
    TextView info_title;//标题
    TextView wish_content;
    TextView wish_title;
    TextView wish_date;
    TextView pass;
    TextView reject;
    ImageView person_image;
    private Wish wish;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wish_detail_for_audit);
        initWidget();
    }//onCreate

    private void initWidget() {
        return_bt = (TextView) findViewById(R.id.lbt);
        info_title = (TextView) findViewById(R.id.mission_title);
        wish_title = (TextView) findViewById(R.id.wish_title);
        wish_content = (TextView) findViewById((R.id.wish_content));
        wish_date = (TextView) findViewById(R.id.wish_date);
        pass = (TextView) findViewById(R.id.pass);
        reject = (TextView) findViewById(R.id.reject);
        person_image = (ImageView) findViewById(R.id.person_image);
        Intent intent = getIntent();
        return_bt.setText("返回");
        info_title.setText("心愿详情");
        wish = (Wish)intent.getSerializableExtra("wish");



        if(wish.getAudit_status() != 1){
            pass.setVisibility(View.INVISIBLE);
            reject.setVisibility(View.INVISIBLE);
        }//if



        wish_title.setText(wish.getTitle());
        wish_content.setText(wish.getContent());
        wish_date.setText(wish.getCreatedAt());

        return_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                wish_detail_for_audit.this.finish();
            }
        });

        pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO 将状态改为已完成
                Wish wish2 = new Wish();
                wish2.setAudit_status(3);
                wish2.update(wish.getObjectId(), new UpdateListener() {

                    @Override
                    public void done(BmobException e) {
                        if(e==null){
                            Log.i("bmob","更新成功");
                            wish_detail_for_audit.this.finish();
                        }else{
                            Log.i("bmob","更新失败："+e.getMessage()+","+e.getErrorCode());
                        }
                    }
                });
            }
        });

        reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO 将状态改为已完成
                Wish wish2 = new Wish();
                wish2.setAudit_status(2);
                wish2.update(wish.getObjectId(), new UpdateListener() {

                    @Override
                    public void done(BmobException e) {
                        if(e==null){
                            Log.i("bmob","更新成功");
                            wish_detail_for_audit.this.finish();
                        }else{
                            Log.i("bmob","更新失败："+e.getMessage()+","+e.getErrorCode());
                        }
                    }
                });
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
                            Intent intent=new Intent(wish_detail_for_audit.this,MyAccount.class);
                            Bundle bundle=new Bundle();
                            bundle.putSerializable("user", object);
                            intent.putExtras(bundle);
                            wish_detail_for_audit.this.startActivity(intent);
                        }else{
                            Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                            Toast.makeText(wish_detail_for_audit.this, "失败:" + e.getMessage(), Toast.LENGTH_SHORT).show();
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
