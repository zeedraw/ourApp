//package com.example.administrator.ourapp;
//
//import android.app.DatePickerDialog;
//import android.content.DialogInterface;
//import android.content.SharedPreferences;
//import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.support.v7.app.AlertDialog;
//import android.text.method.PasswordTransformationMethod;
//import android.util.Log;
//import android.view.View;
//import android.widget.Button;
//import android.widget.DatePicker;
//import android.widget.EditText;
//import android.widget.LinearLayout;
//import android.widget.RadioGroup;
//import android.widget.Spinner;
//import android.widget.TextView;
//
//import com.example.administrator.ourapp.location.LocationTest;
//
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;
//
//import cn.bmob.v3.BmobSMS;
//import cn.bmob.v3.exception.BmobException;
//import cn.bmob.v3.listener.QueryListener;
//import cn.bmob.v3.listener.UpdateListener;
//import com.example.administrator.ourapp.widget.OnWheelChangedListener;
//import com.example.administrator.ourapp.widget.WheelView;
//import com.example.administrator.ourapp.widget.adapters.ArrayWheelAdapter;
//
///**
// * Created by Administrator on 2016/9/5.
// */
//public class SignUpTest extends LocationTest implements OnWheelChangedListener,View.OnClickListener,DatePickerDialog.OnDateSetListener,View.OnFocusChangeListener {
//    private WheelView mViewProvince;
//    private WheelView mViewCity;
//    private WheelView mViewDistrict;//wheelview组件
//    private Button location_setting,bd_setting,signup_bt,verify_bt;
//    private TimeButton sendsms_bt;
//    private TextView location_tv,bd_tv,pwconfirm_mes, phone_mes,title,rt,user_mes,
//                pw_mes,bd_mes,location_mes,sms_mes;
//    private AlertDialog.Builder builder;
//    private LinearLayout root;
//    private SharedPreferences preferences;
//    private SharedPreferences.Editor editor;
//    private EditText user_et,pw_et,pwconfirm_et,phone_et,intro_et,sms_et;
//    private RadioGroup sex_rg;
//    private Spinner qualification;
//    private boolean isSms;
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.signuptest);
//        initWidget();
//    }
//
//    private void initWidget()
//    {
//        user_et=(EditText)findViewById(R.id.user_info);
//        user_mes=(TextView)findViewById(R.id.user_mes);
//        user_et.setOnFocusChangeListener(this);
//
//
//        pw_et=(EditText)findViewById(R.id.pw_info);
//        pw_et.setTransformationMethod(PasswordTransformationMethod.getInstance());
//        pw_et.setOnFocusChangeListener(this);
//        pw_mes=(TextView)findViewById(R.id.pw_mes);
//
//
//        pwconfirm_et=(EditText)findViewById(R.id.pwcofirm_info);
//        pwconfirm_et.setTransformationMethod(PasswordTransformationMethod.getInstance());
//        pwconfirm_mes=(TextView)findViewById(R.id.pwconfirm_mes);
//        pwconfirm_et.setOnFocusChangeListener(this);
//
//        sex_rg=(RadioGroup)findViewById(R.id.sex_info);
//
//        bd_tv=(TextView)findViewById(R.id.bd_info);
//        bd_mes=(TextView)findViewById(R.id.bd_mes);
//        bd_setting=(Button)findViewById(R.id.bd_setting);
//        bd_setting.setOnClickListener(this);
//
//        qualification=(Spinner)findViewById(R.id.quli_spinner);
//
//        location_tv=(TextView)findViewById(R.id.location_tv);
//        location_mes=(TextView)findViewById(R.id.location_mes);
//        location_setting=(Button)findViewById(R.id.location_setting);
//        location_setting.setOnClickListener(this);
//
//        phone_et=(EditText)findViewById(R.id.phone_info);
//        phone_mes =(TextView)findViewById(R.id.phone_mes);
//        phone_et.setOnFocusChangeListener(this);
//        sendsms_bt=(TimeButton)findViewById(R.id.sendsms_bt);
//        sendsms_bt.setOnClickListener(this);
//
//        sms_et=(EditText)findViewById(R.id.sms_info) ;
//        verify_bt=(Button)findViewById(R.id.verify_bt);
//        verify_bt.setOnClickListener(this);
//        sms_mes=(TextView)findViewById(R.id.sms_mes);
//
//        intro_et=(EditText)findViewById(R.id.intro_info);
//
//        title=(TextView)findViewById(R.id.title);
//        title.setText("注册");
//
//        rt=(TextView)findViewById(R.id.lbt);
//        rt.setOnClickListener(this);
//        rt.setText("返回");
//        rt.setVisibility(View.VISIBLE);
//
//        signup_bt=(Button)findViewById(R.id.signup_bt);
//        signup_bt.setOnClickListener(this);
//
//
//
//        preferences=getSharedPreferences("ourApp",MODE_PRIVATE);
//        editor=preferences.edit();
//    }
//
//    //初始化wheelview组件
//    private void setUpViews() {
//        mViewProvince = (WheelView)root.findViewById(R.id.id_province);
//        mViewCity = (WheelView)root.findViewById(R.id.id_city);
//        mViewDistrict = (WheelView)root.findViewById(R.id.id_district);
//
//    }
//    //为wheelview组件设置监听器
//    private void setUpListener() {
//        // 添加change事件
//        mViewProvince.addChangingListener(this);
//        // 添加change事件
//        mViewCity.addChangingListener(this);
//        // 添加change事件
//        mViewDistrict.addChangingListener(this);
//
//    }
//    //为wheelview组件初始化数据
//    private void setUpData() {
//        initProvinceDatas();
//        mViewProvince.setViewAdapter(new ArrayWheelAdapter<String>(SignUpTest.this, mProvinceDatas));
//        // 设置可见条目数量
//        mViewProvince.setVisibleItems(7);
//        mViewCity.setVisibleItems(7);
//        mViewDistrict.setVisibleItems(7);
//        updateCities();
//        updateAreas();
//        mViewProvince.setCurrentItem(preferences.getInt("province",0));
//        mViewCity.setCurrentItem(preferences.getInt("city",0));
//        mViewDistrict.setCurrentItem(preferences.getInt("district",0));
//    }
//
//    @Override
//    public void onChanged(WheelView wheel, int oldValue, int newValue) {
//        if (wheel == mViewProvince) {
//            updateCities();
//            editor.putInt("province",mViewProvince.getCurrentItem());
//        } else if (wheel == mViewCity) {
//            updateAreas();
//            editor.putInt("city",mViewCity.getCurrentItem());
//        } else if (wheel == mViewDistrict) {
//            mCurrentDistrictName = mDistrictDatasMap.get(mCurrentCityName)[newValue];
//            editor.putInt("district",mViewDistrict.getCurrentItem());
//        }
//        editor.commit();
//    }
//
//    /**
//     * 根据当前的市，更新区WheelView的信息
//     */
//    private void updateAreas() {
//        int pCurrent = mViewCity.getCurrentItem();
//        mCurrentCityName = mCitisDatasMap.get(mCurrentProviceName)[pCurrent];
//        String[] areas = mDistrictDatasMap.get(mCurrentCityName);
//
//        if (areas == null) {
//            areas = new String[] { "" };
//        }
//        mViewDistrict.setViewAdapter(new ArrayWheelAdapter<String>(this, areas));
//        mViewDistrict.setCurrentItem(0);
//    }
//
//    /**
//     * 根据当前的省，更新市WheelView的信息
//     */
//    private void updateCities() {
//       int pCurrent = mViewProvince.getCurrentItem();
//        mCurrentProviceName = mProvinceDatas[pCurrent];
//        String[] cities = mCitisDatasMap.get(mCurrentProviceName);
//        if (cities == null) {
//            cities = new String[] { "" };
//        }
//        mViewCity.setViewAdapter(new ArrayWheelAdapter<String>(this, cities));
//        mViewCity.setCurrentItem(0);
//        updateAreas();
//    }
//
//    @Override
//    public void onClick(View view) {
//
//        if (view==location_setting)
//        {
//            root=(LinearLayout)getLayoutInflater().inflate(R.layout.locationwheel,null);
//
//            builder=new AlertDialog.Builder(SignUpTest.this);
//
//            builder.setView(root).setCancelable(false).setPositiveButton("确定",new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                location_tv.setText(
//                        mCurrentProviceName+"-"+mCurrentCityName
//                                +"-"+mCurrentDistrictName);
//            }
//        });
//            builder.create().show();
//
//            setUpViews();
//            setUpListener();
//            setUpData();
//        }
//        if (view==bd_setting)
//        {
//            DatePickerDialog dpl=new DatePickerDialog(this,this,1990,0,1);
//            dpl.setTitle("请设置你的生日");
//            dpl.show();
//        }
//        if (view==rt)
//        {
//            sendsms_bt.onDestroy();
//            finish();
//        }
//
//        //注册的监听事件
//        if(view==signup_bt)
//        {
//            signUp();
//        }
//
//        //发送sms
//        if(view==sendsms_bt)
//        {
//            if(isPhone())
//            {
//                sendsms_bt.onCreat();//TimeButton的计时时间
//                BmobSMS.requestSMSCode(phone_et.getText().toString(), "注册验证", new QueryListener<Integer>() {
//                    @Override
//                    public void done(Integer smsId, BmobException e) {
//                        if (e == null) {//验证码发送成功
//                            Log.i("smile", "短信id：" + smsId);//用于查询本次短信发送详情
//                        }
//
//                    }
//                });
//            }
//        }
//
//        if (view==verify_bt)
//        {
//            isSms();
//        }
//    }
//
//    //焦点改变
//    @Override
//    public void onFocusChange(View view, boolean b) {
//        if (!b)
//        {
//            if (view==user_et)
//            {
//                isName();
//            }
//            else if(view==pw_et)
//            {
//                isPassword();
//            }
//            else if(view==pwconfirm_et)
//            {
//                isConfirmPw();
//            }
//            else if(view==phone_et)
//            {
//                isPhone();
//            }
//        }
//    }
//
//    //datepickerdialog按确认后回调
//    @Override
//    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
//        if (i1<10) {
//             if (i2<10) {
//                bd_tv.setText(i + "-0" + (i1 + 1) + "-0" + i2);
//            }
//            else {
//                bd_tv.setText(i + "-0" + (i1 + 1) + "-" + i2);
//            }
//        }
//        else {
//            if (i2<10) {
//                bd_tv.setText(i + "-" + (i1 + 1) + "-0" + i2);
//            }
//            else
//            {
//                bd_tv.setText(i + "-" + (i1 + 1) + "-" + i2);
//            }
//
//        }
//    }
//    private boolean isName(){
//        if(user_et.getText().toString().length()!=0)
//        {
//            user_mes.setText("1");
//            return true;
//        }
//        else
//        {
//            user_mes.setText("昵称不能为空");
//            return false;
//        }
//    }
//
//    private boolean isPassword()
//    {
//        if (pw_et.getText().toString().length()==0)
//        {
//            pw_mes.setText("密码不能为空");
//            return false;
//        }
//        else{
//            pw_mes.setText("");
//            return true;
//        }
//    }
//
//    private boolean isConfirmPw()
//    {
//        if (pwconfirm_et.getText().toString().length()==0)
//        {
//            pwconfirm_mes.setText("不能为空");
//            return false;
//        }
//        else if (!(pwconfirm_et.getText().toString().equals(pw_et.getText().toString())))
//        {
//            pwconfirm_mes.setText("与原密码不同");
//            return false;
//        }
//        else
//        {
//            pwconfirm_mes.setText("");
//            return true;
//        }
//    }
//
//    private boolean isBorndate()
//    {
//        if (bd_tv.getText().toString().length()==0)
//        {
//            bd_mes.setText("请设置生日");
//            return false;
//        }
//        else
//        {
//            bd_mes.setText("");
//            return true;
//        }
//    }
//
//    private boolean isLocation()
//    {
//        if(location_tv.getText().toString().length()==0)
//        {
//            location_mes.setText("请设置所在地");
//            return false;
//        }
//        else
//        {
//            location_mes.setText("");
//            return true;
//        }
//    }
//
//    //判断email格式是否正确
//    private boolean isPhone() {
//        String str = "^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$";
//        Pattern p = Pattern.compile(str);
//        Matcher m = p.matcher(phone_et.getText().toString());
//        if (phone_et.getText().toString().length()==0)
//        {
//            phone_mes.setText("手机号不能为空");
//            return false;
//        }
//        else if(!m.matches())
//        {
//            phone_mes.setText("请输入正确的手机号");
//            return false;
//        }
//        else {
//            phone_mes.setText("");
//            return true;
//        }
//    }
//
//    private void isSms()
//    {
//        if (sms_et.getText().toString().length()==0)
//        {
//            sms_mes.setText("验证码不能为空");
//            isSms=false;
//        }
//        else {
//            BmobSMS.verifySmsCode(phone_et.getText().toString(), sms_et.getText().toString(), new UpdateListener() {
//
//                @Override
//                public void done(BmobException ex) {
//                    if (ex == null) {//短信验证码已验证成功
//                        Log.i("smile", "验证通过");
//                        isSms = true;
//                        sms_mes.setText("验证成功");
//                    } else {
//                        Log.i("smile", "验证失败：code =" + ex.getErrorCode() + ",msg = " + ex.getLocalizedMessage());
//                        sms_mes.setText("验证失败");
//                        isSms = true;
//                    }
//                }
//            });
//        }
//
//    }
//
//    //注册
//    private void signUp()
//    {
//        if (isName()&&isPassword()&&isConfirmPw()&&isBorndate()&&isLocation()&&isPhone()&&isSms)
//        {
//
//        }
//    }
//
//
//}
