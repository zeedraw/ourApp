package com.example.administrator.ourapp;

import android.app.DatePickerDialog;
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
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;

/**
 * Created by Administrator on 2016/9/9.
 */
public class EditInfo extends AppCompatActivity implements DatePickerDialog.OnDateSetListener,DialogInterface.OnClickListener{
    private RelativeLayout userimage;
    private TextView rt_bt,title,save_bt,bd_tv,location_tv;
    private EditText name_et,intro_et;
    private RadioGroup sex_rg;
    private RelativeLayout bd_rl,location_rl;
    private LocationPickerDialog locationPickerDialog;
    private DatePickerDialog dpl;
    private MyUser current;

    protected static final int CHOOSE_PICTURE = 0;
    protected static final int TAKE_PICTURE = 1;
    private static final int CROP_SMALL_PICTURE = 2;
    protected static Uri tempUri;
    private ImageView personal_icon_iv;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_info);
        current=BmobUser.getCurrentUser(MyUser.class);
        initWidget();
        initInfo();

    }

    private void initWidget()
    {
        userimage=(RelativeLayout)findViewById(R.id.userimage);
        userimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showChoosePicDialog();
            }
        });

        name_et=(EditText)findViewById(R.id.edit_name_et);

        sex_rg=(RadioGroup)findViewById(R.id.edit_sex_rg);

        bd_rl=(RelativeLayout)findViewById(R.id.edit_bd_rl);
        bd_tv=(TextView)findViewById(R.id.edit_bd_tv);
        bd_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 dpl=new DatePickerDialog(EditInfo.this,EditInfo.this,1990,0,1);
            dpl.setTitle("请设置你的生日");
            dpl.show();
            }
        });


        location_rl=(RelativeLayout)findViewById(R.id.edit_location_rl);
        location_tv=(TextView)findViewById(R.id.edit_location_tv);
        location_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                locationPickerDialog=new LocationPickerDialog(EditInfo.this,EditInfo.this);

                locationPickerDialog.show();
            }
        });

        intro_et=(EditText)findViewById(R.id.edit_intro_et);

        rt_bt=(TextView)findViewById(R.id.lbt);
        rt_bt.setText("取消");
        rt_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        rt_bt.setVisibility(View.VISIBLE);

        title=(TextView)findViewById(R.id.mission_title);
        title.setText("编辑");

        personal_icon_iv=(ImageView)findViewById(R.id.personal_icon_iv);

        save_bt=(TextView)findViewById(R.id.rbt);
        save_bt.setText("完成");
        save_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                savaInfo();

            }
        });

    }

    private void initInfo()
    {
        Bitmap bitmap=null;
        try
        {
            bitmap = BitmapFactory.decodeFile(MainActivity.getDiskFileDir(getApplicationContext())+"/user_image.png");
            personal_icon_iv.setImageBitmap(bitmap);
        } catch (Exception e)
        {
            // TODO: handleResult exception
        }
        name_et.setText(current.getName());
        int sexId=current.getSex()?R.id.edit_male:R.id.female;
        sex_rg.check(sexId);
        if (current.getBorndate()!=null)
        {
            bd_tv.setText(current.getBorndate());
        }
        if (current.getLocation()!=null)
        {
            location_tv.setText(current.getLocation());
        }
        if (current.getIntroduction()!=null)
        {
            intro_et.setText(current.getIntroduction());
        }
        Log.i("z","初始化信息成功");
    }
    //locationpicker的点击事件
    @Override
    public void onClick(DialogInterface dialogInterface, int i) {
        location_tv.setText(locationPickerDialog.getLocation());
    }

    /**
     * 显示修改头像的对话框
     */
    protected void showChoosePicDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("设置头像");
        String[] items = { "选择本地照片", "拍照" };
        builder.setNegativeButton("取消", null);
        builder.setItems(items, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case CHOOSE_PICTURE: // 选择本地照片
                        Intent openAlbumIntent = new Intent(
                                Intent.ACTION_GET_CONTENT);
                        openAlbumIntent.setType("image/*");
                        startActivityForResult(openAlbumIntent, CHOOSE_PICTURE);
                        break;
                    case TAKE_PICTURE: // 拍照
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
        builder.create().show();
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
                    setImageToView(data); // 让刚才选择裁剪得到的图片显示在界面上
                }
                    break;
            }
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
    protected void setImageToView(Intent data) {
        Bundle extras = data.getExtras();
        if (extras != null) {
            Bitmap photo = extras.getParcelable("data");
            photo =toRoundBitmap(photo, tempUri); // 这个时候的图片已经被处理成圆形的了
            personal_icon_iv.setImageBitmap(photo);

        }
    }

    //处理成圆形
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

//缓存头像到本地/files目录下
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

        //datepickerdialog按确认后回调
    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        if (i1<10) {
             if (i2<10) {
                bd_tv.setText(i + "-0" + (i1 + 1) + "-0" + i2);
            }
            else {
                bd_tv.setText(i + "-0" + (i1 + 1) + "-" + i2);
            }
        }
        else {
            if (i2<10) {
                bd_tv.setText(i + "-" + (i1 + 1) + "-0" + i2);
            }
            else
            {
                bd_tv.setText(i + "-" + (i1 + 1) + "-" + i2);
            }

        }
    }

    private void savaInfo()
    {
        Bitmap bm = ((BitmapDrawable)personal_icon_iv.getDrawable()).getBitmap();
        String imagePath = savePhoto(bm,MainActivity.getDiskFileDir(getApplicationContext()), "user_image");
        Log.i("z","本地保存成功--"+imagePath);
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
                    Log.i("z","上传文件成功:" + newbf.getFileUrl());
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
                                    Toast.makeText(EditInfo.this, "失败:" + e.getMessage(), Toast.LENGTH_SHORT).show();

                                }
                            }
                        });
                    }

                    user.setUserimage(newbf);
                    user.setName(name_et.getText().toString().trim());
                    user.setSex(sex_rg.getCheckedRadioButtonId()==R.id.edit_male?true:false);
                    user.setBorndate(bd_tv.getText().toString().trim());
                    user.setLocation(location_tv.getText().toString().trim());
                    user.setIntroduction(intro_et.getText().toString());
                    user.update(current.getObjectId(), new UpdateListener() {

                        @Override
                        public void done(BmobException e) {
                            if(e==null){
                                ListenerManager.getInstance().sendBroadCast(new String[]{"MyAccount","MineFrag"});
                                Log.i("z","更新用户信息成功");
                                EditInfo.this.finish();
                            }else{
                                Log.i("z","更新用户信息失败:" + e.getMessage());
                                Toast.makeText(EditInfo.this, "失败:" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }else{
                    Log.i("z","上传文件失败：" + e.getMessage());
                    AlertDialog.Builder builder=new AlertDialog.Builder(EditInfo.this);
                    builder.setMessage("保存失败").create().show();
                }

            }


            @Override
            public void onProgress(Integer value) {
                super.onProgress(value);
            }
        });

    }


}


