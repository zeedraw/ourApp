package com.example.administrator.ourapp.user_information;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import com.example.administrator.ourapp.CheckMission;
import com.example.administrator.ourapp.ChoosePicPopupWindow;
import com.example.administrator.ourapp.EditIntro;
import com.example.administrator.ourapp.ListenerManager;
import com.example.administrator.ourapp.LocationPickerDialog;
import com.example.administrator.ourapp.MainActivity;
import com.example.administrator.ourapp.MyUser;
import com.example.administrator.ourapp.R;
import com.example.administrator.ourapp.RatingUser;
import com.example.administrator.ourapp.UserInfo;
import com.example.administrator.ourapp.widget.PullScrollView;

import java.util.Calendar;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;

/**
 * Created by Administrator on 2016/8/21.
 */
public class MyAccount extends AppCompatActivity implements PullScrollView.OnTurnListener, DatePickerDialog.OnDateSetListener, DialogInterface.OnClickListener {
    private PullScrollView mScrollView;
    private ImageView mHeadImg;
    private TextView rt_button;
    private TextView title;
    private TextView name_tv,sex_tv,age_tv,location_tv,intro_tv;
    private ImageView user_image_iv;
    private MyUser current;
    private TextView studentDes,organizationDes;
    private TextView checkMission;
    private TextView number,otherContact;
    private RatingBar ratingBar;


    private static final int CHOOSE_PICTURE = 0;
    private static final int TAKE_PICTURE = 1;
    private static final int CROP_SMALL_PICTURE = 2;
    private static final int FOR_INTRO=1000;
    private static final int GET_INTRO=1001;
    private static Uri tempUri;

    private ChoosePicPopupWindow choosePicPopupWindow;
    private LocationPickerDialog locationPickerDialog;//选择地点

    private RelativeLayout rlName,rlSex,rlAge,rlLocation,rlIntro;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.myaccount);
                initWidget();
                Intent intent=getIntent();
                current=(MyUser)intent.getSerializableExtra("user");
                if (current!=null)//他人查看
                {
                    initInfo(false);
                    setUnEditable();
                }
                else {//本人查看
                    BmobQuery<MyUser> query=new BmobQuery<MyUser>();
                    query.getObject(BmobUser.getCurrentUser(MyUser.class).getObjectId(), new QueryListener<MyUser>() {
                        @Override
                        public void done(MyUser myUser, BmobException e) {
                            if(e==null)
                            {
                                current=myUser;


                            }
                            else
                            {
                                current=BmobUser.getCurrentUser(MyUser.class);
                            }
                            initInfo(true);
                        }
                    });
                }

    }

    private void initWidget()
    {
        //设置上部弹性布局
        mScrollView = (PullScrollView) findViewById(R.id.scroll_view);
        mHeadImg = (ImageView) findViewById(R.id.account_header_img);
        mScrollView.setHeader(mHeadImg);
        mScrollView.setOnTurnListener(this);

        user_image_iv=(ImageView)findViewById(R.id.info_user_image_iv);

        studentDes=(TextView)findViewById(R.id.studentDes);
        organizationDes=(TextView)findViewById(R.id.organizationDes);

        name_tv=(TextView)findViewById(R.id.info_name_tv);
        sex_tv=(TextView)findViewById(R.id.info_sex_tv);
        age_tv=(TextView)findViewById(R.id.info_age_tv);
        location_tv=(TextView)findViewById(R.id.info_location_tv);
        intro_tv=(TextView)findViewById(R.id.info_intro_tv);

        checkMission=(TextView)findViewById(R.id.checkMission);
        checkMission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MyAccount.this, CheckMission.class);
                startActivity(intent);
            }
        });

        ratingBar=(RatingBar)findViewById(R.id.ratingbar);
        number=(TextView)findViewById(R.id.contact_number);
        otherContact=(TextView)findViewById(R.id.other_contact_tv);



        rt_button=(TextView)findViewById(R.id.lbt);
   //     edit_button=(TextView)findViewById(R.id.rbt);
        title=(TextView)findViewById(R.id.mission_title);

        rt_button.setText("返回");
     //   edit_button.setText("编辑");
        title.setText("个人信息");
     //   rt_button.setVisibility(View.VISIBLE);
    //    edit_button.setVisibility(View.VISIBLE);

        rt_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

//        edit_button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent=new Intent(MyAccount.this,EditInfo.class);
//                startActivity(intent);
//            }
//        });




    }
//
    private void initInfo(boolean state)//state true 加载本地用户，false加载其他用户
    {
        if (state) {
            Bitmap bitmap = null;
            try {
                bitmap = BitmapFactory.decodeFile(MainActivity.getDiskFileDir(getApplicationContext()) + "/user_image.png");
                user_image_iv.setImageBitmap(bitmap);
                Log.i("z", "获取更新iv--" + MainActivity.getDiskFileDir(getApplicationContext()) + "/user_image.png"
                        + "userid:" + current);
            } catch (Exception e) {
                // TODO: handleResult exception
            }
        }
        else
        {
            new LoadImage().execute(current.getUserimage().getUrl());
        }

        if(current.getIdentifiedStudent())
        {
            studentDes.setText(current.getStuDescription());
        }
        if (current.getIdentifiedPublish())
        {
            organizationDes.setText(current.getOrgDescription());
        }
        name_tv.setText(current.getName());
        sex_tv.setText(current.getSex()?"男":"女");


        if (current.getBorndate()!=null&&current.getBorndate().length()!=0)
        { age_tv.setText(getAge(current.getBorndate())+"");}
        if (current.getLocation()!=null)
        {location_tv.setText(current.getLocation());}
        if (current.getIntroduction()!=null)
        {intro_tv.setText(current.getIntroduction());}

        //初始化评分信息
        BmobQuery<UserInfo> query=new BmobQuery<UserInfo>();
        MyUser user=new MyUser();
        user.setObjectId(current.getObjectId());
        query.addWhereEqualTo("user",user);
        query.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ELSE_CACHE);
        query.findObjects(new FindListener<UserInfo>() {
            @Override
            public void done(List<UserInfo> list, BmobException e) {
                if (e==null)
                {
                    ratingBar.setRating(list.get(0).getRating().floatValue());
                }
                else
                {
                    Log.i("z","查询评分失败");
                }
            }
        });

    }

//    @Override
//    public void upData() {
//       initInfo(true);
//    }

    private int getAge(String bd)
    {
        int age,y,m,d,my,mm,md;
        Calendar calendar=Calendar.getInstance();
        y=calendar.get(Calendar.YEAR);
        m=calendar.get(Calendar.MONTH)+1;
        d=calendar.get(Calendar.DATE);
        String[] sourceStrArray = bd.split("-");
        my=Integer.parseInt(sourceStrArray[0]);
        mm=Integer.parseInt(sourceStrArray[1]);
        md=Integer.parseInt(sourceStrArray[2]);
        if (m<mm)
        {
            age=y-my;
        }
        else if (m==mm)
        {
            if (d<md)
            {
                age=y-my;
            }
            else
            {
                age=y-my+1;
            }
        }
        else
        {
            age=y-my+1;
        }
            return age;
    }



    //
    //异步加载图片
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
            user_image_iv.setImageDrawable(drawable);
        }
    }
    //选择图片
    private void showChoosePicDialog()
    {
          choosePicPopupWindow=new ChoosePicPopupWindow(this, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choosePicPopupWindow.dismiss();
                switch (view.getId()) {
                    case R.id.pick_photo: // 选择本地照片
                        Intent openAlbumIntent = new Intent(
                                Intent.ACTION_GET_CONTENT);
                        openAlbumIntent.setType("image/*");
                        startActivityForResult(openAlbumIntent, CHOOSE_PICTURE);
                        break;
                    case R.id.take_photo: // 拍照
                        Intent openCameraIntent = new Intent(
                                MediaStore.ACTION_IMAGE_CAPTURE);
                        tempUri = Uri.fromFile(new File(MainActivity.getDiskCacheDir(getApplicationContext()), "image.jpg"));
                        // 指定照片保存路径（内存），image.jpg为一个临时文件，每次拍照后这个图片都会被替换
                        openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, tempUri);
                        startActivityForResult(openCameraIntent, TAKE_PICTURE);
                        break;
                }

            }
        });
        choosePicPopupWindow.showAtLocation(findViewById(R.id.main),Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) { // 如果返回码是可以用的
            switch (requestCode) {
                case TAKE_PICTURE:
                    startPhotoZoom(tempUri); // 开始对图片进行裁剪处理
                    break;
                case CHOOSE_PICTURE:
                    startPhotoZoom(data.getData()); // 开始对图片进行裁剪处理
                    break;
                case CROP_SMALL_PICTURE:
                    if (data != null) {
                        setImageToViewAndSave(data); // 让刚才选择裁剪得到的图片显示在界面上

                    }
                    break;
            }
        }
        else if(resultCode==GET_INTRO&&requestCode==FOR_INTRO)
        {
            final String intro=data.getStringExtra("info");
            final Dialog dialog=MainActivity.createLoadingDialog(MyAccount.this);
            dialog.show();
            MyUser myUser=new MyUser();
            myUser.setIntroduction(intro);
            myUser.update(current.getObjectId(), new UpdateListener() {
                @Override
                public void done(BmobException e) {
                    if (e==null)
                    {
                        intro_tv.setText(intro);
                        dialog.dismiss();
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "修改简介失败"+e.getMessage(), Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                }
            });

        }
    }

    /**
     * 裁剪图片方法实现
     *
     * @param uri
     */
    protected void startPhotoZoom(Uri uri) {
        if (uri == null) {
            Log.i("naosumi", "The uri is not exist.");
        }
        tempUri = uri;
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // 设置裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, CROP_SMALL_PICTURE);
    }

    //设置头像
    protected void setImageToViewAndSave(Intent data) {
        Bundle extras = data.getExtras();
        if (extras != null) {
            Bitmap photo = extras.getParcelable("data");
            final Bitmap photoFinal =toRoundBitmap(photo, tempUri); // 这个时候的图片已经被处理成圆形的了
            String imagePath=savePhoto(photoFinal,MainActivity.getDiskCacheDir(getApplicationContext()),"user_image");
            final Dialog loadingDialog=MainActivity.createLoadingDialog(this);
            loadingDialog.show();
            //重新设置头像，用户头像缓存到本地
//            user_image_iv.setImageBitmap(photo);
//            String imagePath=savePhoto(photo,MainActivity.getDiskFileDir(getApplicationContext()),"user_image");
//            ListenerManager.getInstance().sendBroadCast(new String[]{"MineFrag"}
            //上传头像
            final MyUser user=new MyUser();
            final BmobFile newbf=new BmobFile(new File(imagePath));
            //上传新头像
            newbf.uploadblock(new UploadFileListener() {

                @Override
                public void doneError(int code, String msg) {
                    super.doneError(code, msg);
                }

                @Override
                public void done(BmobException e) {

                    if(e==null){
                        //bmobFile.getFileUrl()--返回的上传文件的完整地址
                        Log.i("z","上传头像成功:" + newbf.getFileUrl());
                        //删除原头像
                        if (current.getUserimage().getUrl()!=null) {
                            BmobFile oldbf = new BmobFile();
                            oldbf.setUrl(current.getUserimage().getUrl());
                            oldbf.delete(new UpdateListener() {
                                @Override
                                public void done(BmobException e) {
                                    if(e==null)
                                    {
                                        Log.i("z","原头像删除成功");
                                    }
                                    else {
                                        Log.i("z","原头像删除失败");
                                    }
                                }
                            });
                        }

                        user.setUserimage(newbf);
                        user.update(current.getObjectId(), new UpdateListener() {

                            @Override
                            public void done(BmobException e) {
                                if(e==null){
                                    Log.i("z","更新用户信息成功");
                                    //重新设置头像，用户头像缓存到本地
                                    user_image_iv.setImageBitmap(photoFinal);
                                    savePhoto(photoFinal,MainActivity.getDiskFileDir(getApplicationContext()),"user_image");
                                    ListenerManager.getInstance().sendBroadCast(new String[]{"MineFrag"});
                                    loadingDialog.dismiss();
                                }else{
                                    Toast.makeText(getApplicationContext(), "修改头像失败", Toast.LENGTH_SHORT).show();
                                    Log.i("z","更新用户信息失败:" + e.getMessage());
                                }
                            }
                        });
                    }else{
                        Log.i("z","上传文件失败：" + e.getMessage());
                    }

                }


                @Override
                public void onProgress(Integer value) {
                    super.onProgress(value);
                }
            });

        }


        }

    private Bitmap toRoundBitmap(Bitmap bitmap, Uri tempUri) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float roundPx;
        float left, top, right, bottom, dst_left, dst_top, dst_right, dst_bottom;
        if (width <= height) {
            roundPx = width / 2;
            left = 0;
            top = 0;
            right = width;
            bottom = width;
            height = width;
            dst_left = 0;
            dst_top = 0;
            dst_right = width;
            dst_bottom = width;
        } else {
            roundPx = height / 2;
            float clip = (width - height) / 2;
            left = clip;
            right = width - clip;
            top = 0;
            bottom = height;
            width = height;
            dst_left = 0;
            dst_top = 0;
            dst_right = height;
            dst_bottom = height;
        }

        Bitmap output = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect src = new Rect((int) left, (int) top, (int) right,
                (int) bottom);
        final Rect dst = new Rect((int) dst_left, (int) dst_top,
                (int) dst_right, (int) dst_bottom);
        final RectF rectF = new RectF(dst);

        paint.setAntiAlias(true);// 设置画笔无锯齿

        canvas.drawARGB(0, 0, 0, 0); // 填充整个Canvas
        paint.setColor(color);

        // 以下有两种方法画圆,drawRounRect和drawCircle
        // canvas.drawRoundRect(rectF, roundPx, roundPx, paint);//
        // 画圆角矩形，第一个参数为图形显示区域，第二个参数和第三个参数分别是水平圆角半径和垂直圆角半径。
        canvas.drawCircle(roundPx, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));// 设置两张图片相交时的模式,参考http://trylovecatch.iteye.com/blog/1189452
        canvas.drawBitmap(bitmap, src, dst, paint); // 以Mode.SRC_IN模式合并bitmap和已经draw了的Circle

        return output;
    }

    //图片缓存到本地
    public  String savePhoto(Bitmap photoBitmap, String path,
                             String photoName) {
        String localPath = null;

        File photoFile = new File(path, photoName + ".png");
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(photoFile);
            if (photoBitmap != null) {
                if (photoBitmap.compress(Bitmap.CompressFormat.PNG, 100,
                        fileOutputStream)) { // 转换完成
                    localPath = photoFile.getPath();
                    fileOutputStream.flush();
                    Log.i("z","转换成功----");
                }
                Log.i("z","bitmap不为空----");
            }
        } catch (FileNotFoundException e) {
            photoFile.delete();
            localPath = null;
            e.printStackTrace();
        } catch (IOException e) {
            photoFile.delete();
            localPath = null;
            e.printStackTrace();
        } finally {
            try {
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                    fileOutputStream = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return localPath;
    }





    @Override
    public void onTurn() {

    }
    public void imageClick(View view)
    {
        showChoosePicDialog();
    }
    public void nameClick(View view)
    {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("设置昵称");
        LayoutInflater infalater=LayoutInflater.from(this);
        LinearLayout editForm=(LinearLayout)infalater.inflate(R.layout.editname,null);
        final EditText name_et=(EditText)editForm.findViewById(R.id.name_et);
        builder.setView(editForm);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
               final Dialog dialog=MainActivity.createLoadingDialog(MyAccount.this);
                dialog.show();
                MyUser user=new MyUser();
                user.setName(name_et.getText().toString().trim());
                user.update(current.getObjectId(), new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e==null)
                        {
                            Log.i("z","更新用户姓名成功");
                            name_tv.setText(name_et.getText().toString().trim());
                            ListenerManager.getInstance().sendBroadCast(new String[]{"MineFrag"});
                            dialog.dismiss();
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(), "修改昵称失败", Toast.LENGTH_SHORT).show();
                            Log.i("z","更新用户姓名失败");
                            dialog.dismiss();
                        }
                    }
                });
            }
        })
         .setNegativeButton("取消", null).create().show();


    }


    public void sexClick(View view)
    {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("设置性别")
                .setSingleChoiceItems(new String[]{"男", "女"}, sex_tv.getText().toString().trim().equals("男") ? 0 : 1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (i==0)
                        {
                            sex_tv.setText("男");
                        }
                        else if(i==1)
                        {
                            sex_tv.setText("女");}
                    }
                });
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                final Dialog dialog=MainActivity.createLoadingDialog(MyAccount.this);
                dialog.show();
                    MyUser myUser=new MyUser();
                    myUser.setSex(sex_tv.getText().toString().trim()=="男"?true:false);
                    myUser.update(current.getObjectId(), new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e==null)
                            {
                                dialog.dismiss();
                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(), "修改性别失败", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        }
                });
            }
        });
        builder.setNegativeButton("取消",null);
        builder.create().show();
    }

    public void ageClick(View view)
    {
        DatePickerDialog dpl=new DatePickerDialog(this,this,1990,0,1);
        dpl.setTitle("请设置你的生日");
        dpl.show();
    }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        String bd=null;
        if (i1<10) {
            if (i2<10) {
                bd=i + "-0" + (i1 + 1) + "-0" + i2;
            }
            else {
                bd=i + "-0" + (i1 + 1) + "-" + i2;
            }
        }
        else {
            if (i2 < 10) {
                bd=i + "-" + (i1 + 1) + "-0" + i2;
            } else {
                bd=i + "-" + (i1 + 1) + "-" + i2;
            }
        }
        final Dialog dialog=MainActivity.createLoadingDialog(MyAccount.this);
        dialog.show();
        final String bdFinal=bd;
        MyUser myUser=new MyUser();
        myUser.setBorndate(bd);
        myUser.update(current.getObjectId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e==null)
                {
                    age_tv.setText(getAge(bdFinal)+"");
                    dialog.dismiss();
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "修改年龄失败"+e.getMessage(), Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            }
        });



    }

    public void locationClick(View view)
    {
        locationPickerDialog=new LocationPickerDialog(this,this);
        locationPickerDialog.show();
    }
    //locationpicker的点击事件
    @Override
    public void onClick(DialogInterface dialogInterface, int i) {
        final Dialog dialog=MainActivity.createLoadingDialog(MyAccount.this);
        dialog.show();
        MyUser myUser=new MyUser();
        myUser.setLocation(locationPickerDialog.getLocation());
        myUser.update(current.getObjectId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e==null)
                {
                    location_tv.setText(locationPickerDialog.getLocation());
                    dialog.dismiss();
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "修改所在地失败"+e.getMessage(), Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            }
        });
    }

    public void introClick(View view)
    {
        Intent intent=new Intent(this, EditIntro.class);
        intent.putExtra("mes",intro_tv.getText().toString().trim());
        intent.putExtra("from","for_intro");
        startActivityForResult(intent,FOR_INTRO);
    }

    public void otherClick(View view)
    {

    }
    private void setUnEditable()
    {

        rlSex=(RelativeLayout)findViewById(R.id.rl_sex);
        rlName=(RelativeLayout)findViewById(R.id.rl_name);
         rlAge=(RelativeLayout)findViewById(R.id.rl_age);
        rlLocation=(RelativeLayout)findViewById(R.id.rl_location);
        rlIntro=(RelativeLayout)findViewById(R.id.rl_intro);

        user_image_iv.setClickable(false);
        rlIntro.setClickable(false);
        rlLocation.setClickable(false);
        rlName.setClickable(false);
        rlSex.setClickable(false);
        rlAge.setClickable(false);

        Log.i("z","设置不可编辑222");

    }

}


