package com.example.administrator.ourapp;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.example.administrator.ourapp.mymissionadapter.TestAdpter;
import com.example.administrator.ourapp.mymissionadapter.UserItemAdapter;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobBatch;
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BatchResult;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by Administrator on 2016/10/11.
 */

public class RatingUser extends AppCompatActivity {
    private ListView listView;
    private RatingAdapter ratingAdapter;
    private String missionId;
    private int position;
    private List<MyUser> userList;
    private TextView title,complete;
    private Float scoreList[];//储存评分
    private String commentList[];//储存评语
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ratinguser);
        initWidget();
        initInfo();
    }

    private void initWidget()
    {
        listView=(ListView)findViewById(R.id.user_list);
        title=(TextView)findViewById(R.id.title);
        complete=(TextView)findViewById(R.id.rbt);
        complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder builder=new AlertDialog.Builder(RatingUser.this);
                builder.setMessage("确认以当前信息完成评价？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            int flagOfRating=userList.size();
                            int flagOfComment=userList.size();
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Log.i("z","size="+flagOfComment+"-"+flagOfComment);
                                scoreList=ratingAdapter.getScoreList();
                                commentList=ratingAdapter.getCommentList();
                                final Dialog loadingDialog=MainActivity.createLoadingDialog(RatingUser.this);
                                loadingDialog.show();
                            //    更新分数
//            test                    String[] id=new String[]{"TJRU555B","86afe242a6","2082f3971f","GXYIUUUV","aPUDSSSe"};
//                                        for(int t=0;t<id.length;t++) {
//                                            UserInfo userInfo = new UserInfo();
//                                            MyUser user = new MyUser();
//                                            user.setObjectId(id[t]);
//                                            userInfo.setUser(user);
//                                            userInfo.setRating(new Float(0));
//                                            userInfo.setMissionSum(new Integer(0));
//                                            userInfo.save(new SaveListener<String>() {
//                                                @Override
//                                                public void done(String s, BmobException e) {
//                                                    if (e == null) {
//                                                        Log.i("z", "创建数据成功" + s);
//                                                        BmobQuery<UserInfo> query = new BmobQuery<UserInfo>();
//                                                        query.include("user");
//                                                        query.getObject(s, new QueryListener<UserInfo>() {
//                                                            @Override
//                                                            public void done(UserInfo userInfo, BmobException e) {
//                                                                if (e == null) {
//                                                                    Log.i("z", "测试成功" + userInfo.getUser().getName());
//
//                                                                } else {
//                                                                    Log.i("z", "测试失败");
//                                                                }
//                                                            }
//                                                        });
//                                                    } else {
//                                                        Log.i("z", "创建数据失败" + e.getMessage() + e.getErrorCode());
//                                                    }
//                                                }
//                                            });
//
//                                        }test

                                for (int x=0;x<userList.size();x++)
                                {
                                    final Float newScore=scoreList[x];
                                    BmobQuery<UserInfo> query=new BmobQuery<UserInfo>();
                                    MyUser user=new MyUser();
                                    user.setObjectId(userList.get(x).getObjectId());
                                    query.addWhereEqualTo("user",user);
                                    query.findObjects(new FindListener<UserInfo>() {
                                        @Override
                                        public void done(List<UserInfo> list, BmobException e) {
                                            if (e==null)
                                            {
                                                UserInfo info=list.get(0);
                                                Float rating=info.getRating();
                                                Integer sum=info.getMissionSum();
                                                rating=(rating*sum+newScore)/(sum+1);
                                                Log.i("z","newScore="+newScore+"............");
                                                rating=convert(rating);
                                                sum++;
                                                info.setRating(rating);
                                                info.setMissionSum(sum);
                                                info.update(info.getObjectId(), new UpdateListener() {
                                                    @Override
                                                    public void done(BmobException e) {
                                                        if (e==null)
                                                        {
                                                            Log.i("z","更新分数成功");
                                                            flagOfRating--;
                                                            if (flagOfRating==0&&flagOfComment==0)
                                                            {
                                                                Log.i("z","更新全部数据成功");
                                                                loadingDialog.dismiss();
                                                                complete();
                                                            }
                                                        }
                                                        else
                                                        {
                                                            Log.i("z","更新分数失败");
                                                            loadingDialog.dismiss();
                                                        }
                                                    }
                                                });


                                            }
                                            else
                                            {
                                                Log.i("z","查询用户分数信息失败"+e.getMessage());
                                                loadingDialog.dismiss();
                                            }
                                        }
                                    });
                                }


                                //更新comment
                                List<BmobObject> comments=new ArrayList<BmobObject>();
                                {
                                    for (int x=0;x<userList.size();x++)
                                    {
                                        Comment comment=new Comment();
                                        Mission mission=new Mission();
                                        mission.setObjectId(missionId);
                                        comment.setMission(mission);
                                        comment.setUser(userList.get(x));
                                        comment.setComment(commentList[x]);
                                        comments.add(comment);
                                    }
                                }
                                new BmobBatch().insertBatch(comments).doBatch(new QueryListListener<BatchResult>() {
                                    @Override
                                    public void done(List<BatchResult> list, BmobException e) {
                                        if(e==null){
                                            for(int i=0;i<list.size();i++){
                                                BatchResult result = list.get(i);
                                                BmobException ex =result.getError();
                                                if(ex==null){
                                                    Log.i("z","第"+i+"个数据批量添加成功："+result.getCreatedAt()+","+result.getObjectId()+","+result.getUpdatedAt());
                                                    flagOfComment--;
                                                    if (flagOfComment==0&&flagOfRating==0)
                                                    {
                                                        Log.i("z","更新全部数据成功");
                                                        loadingDialog.dismiss();
                                                        complete();
                                                    }
                                                }else{
                                                    Log.i("z","第"+i+"个数据批量添加失败："+ex.getMessage()+","+ex.getErrorCode());
                                                    loadingDialog.dismiss();

                                                }
                                            }
                                        }else{
                                            Log.i("z","失败："+e.getMessage()+","+e.getErrorCode());
                                            loadingDialog.dismiss();
                                        }

                                    }
                                });
                             //  更新comment
//                                while (flagOfComment!=0||flagOfRating!=0)
//                                {
//                                    Log.i("z","等待更新操作完成"+"size="+flagOfComment+"-"+flagOfComment);
//                                }

                            }
                        });
                builder.setNegativeButton("取消",null);
                builder.create().show();
            }
        });
    }

    private void initInfo()
    {
        missionId= getIntent().getStringExtra("missionId");
        position=getIntent().getIntExtra("position",-1);
        title.setText("评价");
        complete.setText("完成");
        query();
    }

    private void query()
    {
        BmobQuery<MyUser> query=new BmobQuery<MyUser>();
        Mission mission=new Mission();
        mission.setObjectId(missionId);
        query.addWhereRelatedTo("get_user",new BmobPointer(mission));
        query.include("userimage");
        query.findObjects(new FindListener<MyUser>() {
            @Override
            public void done(List<MyUser> list, BmobException e) {
                if (e==null)
                {
                    userList=new ArrayList<MyUser>(list);
                    ratingAdapter=new RatingAdapter(RatingUser.this,R.layout.ratinguser_item,userList);
                    listView.setAdapter(ratingAdapter);
                }
                else
                {
                    Log.i("z","查询筛选人员失败");
                }
            }
        });
    }

    private Float convert(Float f)
    {
        int i=f.intValue();
        if(f%(0.5)==0)
        {
            return f;
        }
        else {
            if (i + 0.5 < f) {
                return new Float(i + 1);
            } else {
                return new Float(i + 0.5);
            }
        }
    }

    private void complete()
    {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setMessage("完成评价成功，任务结束！");
        builder.setCancelable(false);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent=new Intent();
                intent.putExtra("position",position);
                setResult(1001,intent);
                finish();
            }
        });
        builder.create().show();
    }

}
