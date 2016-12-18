package com.example.administrator.ourapp.wish;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.TextView;

import com.example.administrator.ourapp.IListener;
import com.example.administrator.ourapp.ListenerManager;
import com.example.administrator.ourapp.Login;
import com.example.administrator.ourapp.Mission;
import com.example.administrator.ourapp.MyUser;
import com.example.administrator.ourapp.question_and_answer.QAFrag;
import com.example.administrator.ourapp.R;
import com.example.administrator.ourapp.question_and_answer.ask_question;

import cn.bmob.v3.BmobUser;

/**
 * Created by Longze on 2016/12/7.
 */
public class my_wish_list extends FragmentActivity implements IListener {
    TextView commit_bt;//标题上的左右按钮
    TextView info_title;//标题

    private Wish wish;
    private Mission mission;
    private WishFrag_with_mission wishFrag;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.refresh_listview);
        ListenerManager.getInstance().registerListtener("QA",this);
//        listView  = (ListView) findViewById(R.id.QAlistview);
//        final Context context = this.getBaseContext();
        initWidget();
////        getQuestionContent();
//        Log.i("test","step 8");
//        question_content.add("问题测试");
//        question_date.add("2016-9-19");
//        Log.i("test","step 9");
//        List<Map<String, Object>> listItems = new ArrayList<Map<String, Object>>();
//        for(int i = 0; i < question_date.size(); ++i){
//            Map<String, Object> listItem = new HashMap<String, Object>();
//            listItem.put("question_date", question_date.get(i));
//            listItem.put("question_content", question_content.get(i));
//            listItems.add(listItem);
//        }//for i
//        Log.i("test","step 10");
//        //创建一个SimpleAdapter
//        SimpleAdapter simpleAdapter = new SimpleAdapter(this, listItems, R.layout.question_and_answer_item,
//                new String[] {"question_date", "question_content"}, new int[] {R.id.date, R.id.question});
//        listView.setAdapter(simpleAdapter);
////        Mission mis = new Mission();
////        mis.setObjectId("a28906b359");
//        BmobQuery<Mission_question> query = new BmobQuery<Mission_question>();
//        query.include("answer[content],User[objectId|userimage]");
//        query.addWhereEqualTo("mission", mission_ID);
//        query.order("-createdAt");
////        Log.i("test","step 2");
////返回100条数据，如果不加上这条语句，默认返回10条数据
//        query.setLimit(100);
//        Log.i("test","step 3");
//执行查询方法
//        query.findObjects(new FindListener<Mission_question>() {
//
//            @Override
//            public void done(List<Mission_question> object, BmobException e) {
////                Log.i("test","step 4");
//                if(e==null){
//                    Log.i("z","查询数据成功");
//                    if (object.size()!=0) {
//                        for (Mission_question question : object) {
//                            //获得createdAt数据创建时间（注意是：createdAt，不是createAt）
//                            question_date.add(question.getCreatedAt());
//                            Log.i("z","查询问题创建时间1");
//                            //获得问题的内容
//                            question_content.add(question.getContent());
//                            Log.i("z","查询问题的内容2");
//                            answer_content.add(question.getanswer().getContent());
//                            Log.i("z","查询答案的内容3");
//                            question_ID.add(question.getObjectId());
//                            Log.i("z","查询问题的ID4");
//                            user_ID.add(question.getUser().getObjectId());
//                            Log.i("z","查询发布者的ID5");
//                        }//for
//
//                        qa_list = new ArrayList<Mission_question>(object);
//                        qa_adapter = new QA_adapter(context, R.layout.question_and_answer_item, qa_list);
//                        listView.setAdapter(qa_adapter);
//                    }
//                    else
//                    {
//                    }
//                }//if
//                else{
////                    Log.i("test","step 6");
//
//                    Log.i("bmob","获取数据失败123："+e.getMessage()+","+e.getErrorCode());
//                }//else
//            }//done
//        });


//        Log.i("test","step 7");
    }//onCreate



    private void initWidget() {
        commit_bt=(TextView)findViewById(R.id.rbt);
        //在textview左侧添加drawable
//        return_bt.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_keyboard_arrow_left_black_24dp, 0, 0, 0);
//        return_bt.setTextSize(21);
        commit_bt.setText("许愿");
//        commit_bt.setTextSize(21);

        info_title=(TextView)findViewById(R.id.mission_title);
        info_title.setText("我的心愿");

//        Bundle bundle = new Bundle();
//        bundle = this.getIntent().getExtras();
//        mission = (Mission) bundle.getSerializable("mission");


//        WishFrag_with_mission myFragment = new WishFrag_with_mission();
//        Bundle bundle1 = new Bundle();
//        bundle.putString("mission_id",mission.getObjectId());//这里的values就是我们要传的值
//        myFragment.setArguments(bundle1);

        commit_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO 点击事件
                if(BmobUser.getCurrentUser(MyUser.class) == null){
                    Intent intent=new Intent(my_wish_list.this,Login.class);
                    startActivity(intent);
                }//if 未登录
                else{
                    Intent intent=new Intent(my_wish_list.this,wish_pub_personnal.class);
                    Bundle bundle=new Bundle();
                    bundle.putSerializable("mission",mission);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }//else
            }//onClick
        });

        FragmentManager fm=getSupportFragmentManager();
        FragmentTransaction ft=fm.beginTransaction();
        wishFrag =new WishFrag_with_mission(mission);
        ft.replace(R.id.container, wishFrag);
        ft.commit();
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//
//                if(mission.getPub_user().getObjectId().equals(BmobUser.getCurrentUser(MyUser.class).getObjectId())){
//                    //若是任务发布者则跳转到编辑回答界面
//                    // 在Intent中传递数据
//                    Intent intent = new Intent(question_and_answer.this, question_and_answer_detail_publisher.class);
////                    Bundle bundle=new Bundle();
////                    bundle.putSerializable("mission",mission);
////                    intent.putExtras(bundle);
//                    intent.putExtra("question_content", question_content.get(i));
//                    intent.putExtra("answer_content",answer_content.get(i));
//                    intent.putExtra("question_ID", question_ID.get(i));
//                    intent.putExtra("question_date",  question_date.get(i));
//                    // 启动Intent
//                    startActivity(intent);
//                }//if
//                //若不是任务发布者则跳转到提问界面
//                else{
//                    Intent intent = new Intent(question_and_answer.this, question_and_answer_detail.class);
//                    // 在Intent中传递数据
//                    intent.putExtra("question_content", question_content.get(i));
//                    intent.putExtra("answer_content",answer_content.get(i));
//                    intent.putExtra("question_date",  question_date.get(i));
////                    intent.putExtra("question", qa_list.get(i));
//                    intent.putExtra("question_ID", question_ID.get(i));
//                    // 启动Intent
//                    startActivity(intent);
//                }//else
//
//
//            }
//        });


    }//initwidget

    @Override
    public void upData() {
        wishFrag.upData();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        wishFrag.upData();

    }
}
