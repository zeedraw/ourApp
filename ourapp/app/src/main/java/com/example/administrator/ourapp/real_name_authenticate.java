package com.example.administrator.ourapp;

import android.app.Activity;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.List;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
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
    private TextView up_load = null;
    private String pic_path[] = new String[3];
    private int num = 0; //num代表图片位置 0为身份证正面 1 为身份证反面 2为持身份证半身照

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_authenticate);
        init();
    }//onCreate

    private void init() {
        // TODO Auto-generated method stub

        cardFront = (ImageView) findViewById(R.id.card_front);
        cardBack = (ImageView) findViewById(R.id.card_back);
        halfPic = (ImageView) findViewById(R.id.half_pic);
        up_load = (TextView) findViewById(R.id.submit);
        cardFront.setOnClickListener(listener);
        cardBack.setOnClickListener(listener);
        halfPic.setOnClickListener(listener);
        up_load.setOnClickListener(upLoad_listener);

    }//init

    private View.OnClickListener upLoad_listener = new View.OnClickListener(){

        @Override
        public void onClick(View v){
            UpLoad();
        }//onClick
    };//upLoad_listener

    private View.OnClickListener listener=new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
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
            }//switch

        }//onClick

        private void setImage(int i) { //i代表图片位置 0为身份证正面 1 为身份证反面 2为持身份证半身照
            // TODO Auto-generated method stub
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
        //TODO 判断是否三个图片都已经选择
        Toast.makeText(getApplicationContext(), "开始上传",
                Toast.LENGTH_SHORT).show();

        BmobFile.uploadBatch(pic_path, new UploadBatchListener() {

            @Override
            public void onSuccess(List<BmobFile> files,List<String> urls) {
                //1、files-上传完成后的BmobFile集合，是为了方便大家对其上传后的数据进行操作，例如你可以将该文件保存到表中
                //2、urls-上传文件的完整url地址
                if(urls.size()==pic_path.length){//如果数量相等，则代表文件全部上传完成
                    //do something
                    Toast.makeText(getApplicationContext(), "上传成功",
                            Toast.LENGTH_SHORT).show();
                    Log.i("z","上传文件成功");

                }
            }

            @Override
            public void onError(int statuscode, String errormsg) {
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
