package com.example.administrator.ourapp.authenticate;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.ourapp.MainActivity;
import com.example.administrator.ourapp.MyUser;
import com.example.administrator.ourapp.R;
import com.example.administrator.ourapp.UserInfo;
import com.example.administrator.ourapp.message.Message;
import com.example.administrator.ourapp.message.Message_tools;

import org.w3c.dom.Text;

import java.io.File;
import java.io.IOException;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadBatchListener;
import cn.bmob.v3.listener.UploadFileListener;

/**
 * Created by dell-pc on 2016/9/10.
 * 实名认证的java文件
 */
public class real_name_authenticate extends AppCompatActivity {
    private final String IMAGE_TYPE = "image/*";
    private final int IMAGE_CODE = 0;   //这里的IMAGE_CODE是自己任意定义的
    private ImageView cardFront=null;
    private ImageView cardBack=null;
    private ImageView halfPic=null;
    private ImageView studentCard=null;
    private TextView up_load = null;
    private EditText real_name = null;
    private EditText ID_number = null;
    private EditText school_name = null;
    private TextView return_bt = null;
    private TextView info_title = null;
    private String pic_path[] = new String[4];
    private int num = 0; //num代表图片位置 0为身份证正面 1 为身份证反面 2为持身份证半身照 3为学生证照
    private boolean is_upload_pic[] = {false, false, false, false};

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_authenticate);
        init();
    }//onCreate

    private void init() {

        cardFront = (ImageView) findViewById(R.id.card_front);
        cardBack = (ImageView) findViewById(R.id.card_back);
        halfPic = (ImageView) findViewById(R.id.half_pic);
        studentCard = (ImageView) findViewById(R.id.student_card);
        up_load = (TextView) findViewById(R.id.submit);
        real_name = (EditText) findViewById(R.id.real_name);
        ID_number = (EditText) findViewById(R.id.ID_number);
        school_name= (EditText) findViewById(R.id.school_name);
        return_bt = (TextView) findViewById(R.id.lbt);
        info_title = (TextView) findViewById(R.id.mission_title);

        return_bt.setText("返回");
        info_title.setText("实名认证");
        return_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                real_name_authenticate.this.finish();
            }
        });

        cardFront.setOnClickListener(listener);
        cardBack.setOnClickListener(listener);
        halfPic.setOnClickListener(listener);
        studentCard.setOnClickListener(listener);
        up_load.setOnClickListener(upLoad_listener);
    }//init

    private View.OnClickListener upLoad_listener = new View.OnClickListener(){
        @Override
        public void onClick(View v){

                if(real_name.getText().length() !=0 &&
                        ID_number.getText().length() !=0  &&
                        school_name.getText().length() !=0){
                    if(is_upload_pic[0] && is_upload_pic[1] && is_upload_pic[2] && is_upload_pic[3]) {
                        UpLoad();
                    }//if 图片
                    else{
                        Toast.makeText(getApplicationContext(),"您还有证件照没有上传，不能提交！",
                                Toast.LENGTH_SHORT).show();
                    }
                }//if 信息
                else{
                    Toast.makeText(getApplicationContext(),"您还有必填信息没有填写，不能提交！",
                            Toast.LENGTH_SHORT).show();
                }// else 信息


        }//onClick
    };//upLoad_listener

    private View.OnClickListener listener=new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            ImageView img = (ImageView) v;

            switch (img.getId()) {
                case R.id.card_front:
                    setImage(0);
                    break;
                case R.id.card_back:
                    setImage(1);
                    break;
                case R.id.half_pic:
                    setImage(2);
                    break;
                case R.id.student_card:
                    setImage(3);
                    break;
            }//switch

        }//onClick

        private void setImage(int i) { //i代表图片位置 0为身份证正面 1 为身份证反面 2为持身份证半身照
            //使用intent调用系统提供的相册功能，使用startActivityForResult是为了获取用户选择的图片
            num = i; //供onActivityResult判断是发出ImageView的组件是哪个
            Intent getAlbum = new Intent(Intent.ACTION_GET_CONTENT);
            getAlbum.setType(IMAGE_TYPE);
            startActivityForResult(getAlbum, IMAGE_CODE);

        }//setImage
    };

    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.activity_picture, menu);
//        return true;
//    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if (resultCode != RESULT_OK) {        //此处的 RESULT_OK 是系统自定义得一个常量
            Log.e("TAG->onresult","ActivityResult resultCode error");
            return;
        }//if

        ImageView imgShow = null;
        switch(num){
            case 0:
                imgShow = cardFront;
                break;
            case 1:
                imgShow = cardBack;
                break;
            case 2:
                imgShow = halfPic;
                break;
            case 3:
                imgShow = studentCard;
        }//swtich

        Bitmap bm = null;

        //外界的程序访问ContentProvider所提供数据 可以通过ContentResolver接口
        ContentResolver resolver = getContentResolver();

        //此处的用于判断接收的Activity是不是你想要的那个
        if (requestCode == IMAGE_CODE) {
            try {
                Uri originalUri = data.getData();        //获得图片的uri
                bm = MediaStore.Images.Media.getBitmap(resolver, originalUri);
                //显得到bitmap图片
                imgShow.setImageBitmap(bm);
                String path = getImageAbsolutePath(this, originalUri);
                pic_path[num] = path;
                is_upload_pic[num] = true;
                Log.i("test_upload", "选中图片"+ num);
            }catch (IOException e) {

                Log.e("TAG-->Error",e.toString());
            }//catch
        }//if
    }//onAcitivtyResult

    public static String getImageAbsolutePath(Activity context, Uri imageUri) {
        if (context == null || imageUri == null)
            return null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT && DocumentsContract.isDocumentUri(context, imageUri)) {
            if (isExternalStorageDocument(imageUri)) {
                String docId = DocumentsContract.getDocumentId(imageUri);
                String[] split = docId.split(":");
                String type = split[0];
                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            } else if (isDownloadsDocument(imageUri)) {
                String id = DocumentsContract.getDocumentId(imageUri);
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                return getDataColumn(context, contentUri, null, null);
            } else if (isMediaDocument(imageUri)) {
                String docId = DocumentsContract.getDocumentId(imageUri);
                String[] split = docId.split(":");
                String type = split[0];
                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                String selection = MediaStore.Images.Media._ID + "=?";
                String[] selectionArgs = new String[] { split[1] };
                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        } // MediaStore (and general)
        else if ("content".equalsIgnoreCase(imageUri.getScheme())) {
            // Return the remote address
            if (isGooglePhotosUri(imageUri))
                return imageUri.getLastPathSegment();
            return getDataColumn(context, imageUri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(imageUri.getScheme())) {
            return imageUri.getPath();
        }
        return null;
    }

    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        String column = MediaStore.Images.Media.DATA;
        String[] projection = { column };
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    //将本页面图片上传到服务器
    public void UpLoad(){
        Toast.makeText(getApplicationContext(), "开始上传,请不要关闭此页面或进行其他操作",
                Toast.LENGTH_SHORT).show();

        final Dialog loading_dialog = MainActivity.createLoadingDialog(real_name_authenticate.this);
        loading_dialog.show();
        BmobFile.uploadBatch(pic_path, new UploadBatchListener() {

            @Override
            public void onSuccess(List<BmobFile> files,List<String> urls) {
                //1、files-上传完成后的BmobFile集合，是为了方便大家对其上传后的数据进行操作，例如你可以将该文件保存到表中
                //2、urls-上传文件的完整url地址
                if(urls.size()==pic_path.length){//如果数量相等，则代表文件全部上传完成
                    //do something
                    MyUser user = new MyUser();
                    user.setRealname(real_name.getText().toString().trim());
                    user.setIdCard(ID_number.getText().toString().trim());
                    user.setSchoolname(school_name.getText().toString().trim());
                    user.setCardfront(new BmobFile("CardFront", null, urls.get(0)));
                    user.setCardback(new BmobFile("CardBack", null, urls.get(1)));
                    user.setHalfpicture(new BmobFile("halfPic", null, urls.get(2)));
                    user.setStudentcard(new BmobFile("studentCard", null, urls.get(3)));
                    user.setIdent_state_stu(1); //1代表审核中
                    user.update(BmobUser.getCurrentUser().getObjectId(), new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e==null)
                            {
                                loading_dialog.dismiss();
//                                Message_tools ms = new Message_tools();
//                                MyUser offical = new MyUser();
//                                offical.setObjectId("TJRU555B");
//                                ms.send(BmobUser.getCurrentUser(MyUser.class), offical,
//                                        BmobUser.getCurrentUser(MyUser.class).getObjectId()+"的个人认证",
//                                        15, false, "", real_name_authenticate.this);

                                final MyUser offical = new MyUser();
                                offical.setObjectId("TJRU555B");

                                final Message message = new Message();
                                message.setContent(BmobUser.getCurrentUser(MyUser.class).getObjectId()+"的个人认证");
                                message.setType(14);
                                message.setReceiver(offical);
                                message.setSender(BmobUser.getCurrentUser(MyUser.class));
                                message.setBe_viewed(false);
                                message.setRemark("no mark");
                                final Dialog loading_dialog = MainActivity.createLoadingDialog(real_name_authenticate.this);
                                loading_dialog.show();
                                message.save(new SaveListener<String>() {

                                    @Override
                                    public void done(String objectId, BmobException e) {
                                        if(e==null){
                                            Log.i("bomb","发送信息成功：" + objectId);

                                            BmobQuery<UserInfo> query = new BmobQuery<UserInfo>();
                                            query.addWhereEqualTo("user" ,offical.getObjectId());
                                            query.setLimit(50);
                                            query.findObjects(new FindListener<UserInfo>() {
                                                @Override
                                                public void done(List<UserInfo> object, BmobException e) {
                                                    if(e==null){

                                                        for (UserInfo user : object) {
                                                            user.addUnread_message_num();
                                                            user.update(user.getObjectId(), new UpdateListener() {

                                                                @Override
                                                                public void done(BmobException e) {
                                                                    if(e==null){
                                                                        Log.i("bmob","未读消息数更新成功");
                                                                        Toast.makeText(getApplicationContext(), "上传成功",
                                                                                Toast.LENGTH_SHORT).show();
                                                                        Log.i("z","上传文件成功");
                                                                        real_name_authenticate.this.finish();
                                                                        Log.i("s","更新用户成功");
                                                                        loading_dialog.dismiss();
                                                                    }else{
                                                                        Log.i("bmob","未读消息数更新失败："+e.getMessage()+","+e.getErrorCode());
                                                                        loading_dialog.dismiss();
                                                                    }
                                                                }
                                                            });
                                                        }
                                                    }else{
                                                        Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                                                        loading_dialog.dismiss();
                                                    }
                                                }
                                            });
                                        }else{
                                            Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                                            loading_dialog.dismiss();
                                        }//else
                                    }//done
                                });
                                Log.i("s","更新用户成功");
                                Toast.makeText(getApplicationContext(), "上传成功",
                                        Toast.LENGTH_SHORT).show();
                                Log.i("z","上传文件成功");
                                real_name_authenticate.this.finish();
                            }
                            else
                            {
                                loading_dialog.dismiss();
                                Log.i("s","更新失败"+e.getMessage());
                            }
                        }
                    });


                }
            }

            @Override
            public void onError(int statuscode, String errormsg) {
                loading_dialog.dismiss();
                Toast.makeText(getApplicationContext(), "上传失败",
                        Toast.LENGTH_SHORT).show();
                Log.i("z","上传文件失败");

            }


            @Override
            public void onProgress(int curIndex, int curPercent, int total,int totalPercent) {
                //1、curIndex--表示当前第几个文件正在上传
                //2、curPercent--表示当前上传文件的进度值（百分比）
                //3、total--表示总的上传文件数
                //4、totalPercent--表示总的上传进度（百分比）
            }
        });
    }//UpLoad
}
