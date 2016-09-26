package com.example.administrator.ourapp.question_and_answer;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.administrator.ourapp.MyUser;
import com.example.administrator.ourapp.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by Longze on 2016/9/18.
 * 问答界面
 */
public class question_and_answer extends Activity{
    TextView return_bt,commit_bt;//标题上的左右按钮
    TextView info_title;//标题
    private ListView listView;
    private List<Mission_question> qa_list;
    private Vector<String> question_date = new Vector<String>();       //问题的发布日期
    private Vector<String> question_content = new Vector<String>();    //问题的内容
    private Vector<String> answer_content = new Vector<String>(); //问题的回答
    private Vector<String> question_ID = new Vector<String>();//问题的ID
    private Vector<String> user_ID = new Vector<String>();//问题的ID
    private QA_adapter qa_adapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.questoin_and_answer_listview);
        listView  = (ListView) findViewById(R.id.QAlistview);
        final Context context = this.getBaseContext();
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
//        Mission mis = new Mission();
//        mis.setObjectId("a28906b359");
         BmobQuery<Mission_question> query = new BmobQuery<Mission_question>();
//        Log.i("test","step 1");
        query.include("answer[content],User[objectId|userimage]");
        query.addWhereEqualTo("mission", "a28906b359");
        query.order("-createdAt");
//        Log.i("test","step 2");
//返回100条数据，如果不加上这条语句，默认返回10条数据
        query.setLimit(100);
//        Log.i("test","step 3");
//执行查询方法
        query.findObjects(new FindListener<Mission_question>() {

            @Override
            public void done(List<Mission_question> object, BmobException e) {
//                Log.i("test","step 4");
                if(e==null){
                    Log.i("z","查询数据成功");
                    if (object.size()!=0) {

                        for (Mission_question question : object) {
                            //获得createdAt数据创建时间（注意是：createdAt，不是createAt）
                            question_date.add(question.getCreatedAt());
                            //获得问题的内容
                            question_content.add(question.getContent());
                            answer_content.add(question.getanswer().getContent());
                            question_ID.add(question.getObjectId());
                            user_ID.add(question.getUser().getObjectId());
                        }//for

                        qa_list = new ArrayList<Mission_question>(object);
                        qa_adapter = new QA_adapter(context, R.layout.question_and_answer_item, qa_list);
                        listView.setAdapter(qa_adapter);
                    }
                    else
                    {
                        //Todo: 提示 还有没有人提问
                    }

//                        List<Map<String, Object>> listItems = new ArrayList<Map<String, Object>>();
//                        for(int i = 0; i < question_date.size(); ++i){
//                            Map<String, Object> listItem = new HashMap<String, Object>();
//                            listItem.put("question_date", question_date.get(i));
//                            listItem.put("question_content", question_content.get(i));
//                            listItem.put("answer_content", answer_content.get(i));
////                            listItem.put("user_avatars", returnBitMap(user_avatars.get(i).getUrl()));
//                            listItems.add(listItem);
//                        }//for i
////                        Log.i("test","step 10");
//                        //创建一个SimpleAdapter
//                        SimpleAdapter simpleAdapter = new SimpleAdapter(context, listItems,
//                                R.layout.question_and_answer_item,
//                                new String[] {"question_date", "question_content", "answer_content", "user_avatars"},
//                                new int[] {R.id.date, R.id.question, R.id.answer, R.id.user_avatars});
//
//                        listView.setAdapter(simpleAdapter);

                }//if
                else{
//                    Log.i("test","step 6");

                    Log.i("bmob","获取数据失败："+e.getMessage()+","+e.getErrorCode());
                }//else
            }//done
        });


//        Log.i("test","step 7");
    }//onCreate

//    public Bitmap returnBitMap(String url) {
//        URL myFileUrl = null;
//        Bitmap bitmap = null;
//        try {
//            myFileUrl = new URL(url);
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        }
//        try {
//            HttpURLConnection conn = (HttpURLConnection) myFileUrl
//                    .openConnection();
//            conn.setDoInput(true);
//            conn.connect();
//            InputStream is = conn.getInputStream();
//            bitmap = BitmapFactory.decodeStream(is);
//            is.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return bitmap;
//    }

    private void initWidget() {
        return_bt=(TextView)findViewById(R.id.lbt);
        commit_bt=(TextView)findViewById(R.id.rbt);
        //在textview左侧添加drawable
//        return_bt.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_keyboard_arrow_left_black_24dp, 0, 0, 0);
        return_bt.setText("返回");
//        return_bt.setTextSize(21);
        commit_bt.setText("提问");
//        commit_bt.setTextSize(21);

        info_title=(TextView)findViewById(R.id.title);
        info_title.setText("问答");

        return_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                question_and_answer.this.finish();
            }
        });

        commit_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ComponentName comp=new ComponentName(question_and_answer.this,ask_question.class);
                Intent intent=new Intent();
                intent.setComponent(comp);
                startActivity(intent);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                if(user_ID.get(i).equals(BmobUser.getCurrentUser(MyUser.class).getObjectId())){
                    //若是任务发布者则跳转到编辑回答界面
                    Intent intent = new Intent(question_and_answer.this, edit_answer.class);
                    // 在Intent中传递数据
                    intent.putExtra("question_content", question_content.get(i));
                    intent.putExtra("answer_content",answer_content.get(i));
                    intent.putExtra("question_ID", question_ID.get(i));
                    // 启动Intent
                    startActivity(intent);
                }//if
                //若不是任务发布者则跳转到提问界面
                else{
                    Intent intent = new Intent(question_and_answer.this, question_and_answer_detail.class);
                    // 在Intent中传递数据
                    intent.putExtra("question_content", question_content.get(i));
                    intent.putExtra("answer_content",answer_content.get(i));
                    intent.putExtra("question_date",  question_date.get(i));
                    intent.putExtra("question", qa_list.get(i));
                    intent.putExtra("question_ID", question_ID.get(i));
                    // 启动Intent
                    startActivity(intent);
                }//else


            }
        });


    }//initwidget


//    private void getQuestionContent(){
////        BmobQuery<Mission_question> query = new BmobQuery<Mission_question>();
////        Log.i("test","step 1");
//////查询playerName叫“比目”的数据
////        query.addWhereEqualTo("objectId", "KNxs444I");
////        Log.i("test","step 2");
//////返回100条数据，如果不加上这条语句，默认返回10条数据
////        query.setLimit(100);
////        Log.i("test","step 3");
//////执行查询方法
////        query.findObjects(new FindListener<Mission_question>() {
////
////            @Override
////            public void done(List<Mission_question> object, BmobException e) {
////                Log.i("test","step 4");
////                if(e==null){
////                    for (Mission_question question : object) {
////                        Log.i("test","step 5");
////                        //获得createdAt数据创建时间（注意是：createdAt，不是createAt）
////                        question_date.add(question.getCreatedAt());
////                        //获得问题的内容
////                        question_content.add(question.getContent());
////                        List<Map<String, Object>> listItems = new ArrayList<Map<String, Object>>();
////                        for(int i = 0; i < question_date.size(); ++i){
////                            Map<String, Object> listItem = new HashMap<String, Object>();
////                            listItem.put("question_date", question_date.get(i));
////                            listItem.put("question_content", question_content.get(i));
////                            listItems.add(listItem);
////                        }//for i
////                        Log.i("test","step 10");
////                        //创建一个SimpleAdapter
////                        SimpleAdapter simpleAdapter = new SimpleAdapter(this, listItems, R.layout.question_and_answer_item,
////                                new String[] {"question_date", "question_content"}, new int[] {R.id.date, R.id.question});
////                        listView.setAdapter(simpleAdapter);
////                    }//for
////                }//if
////                else{
////                    Log.i("test","step 6");
////
////                    Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
////                }//else
////            }//done
////        });
////
////        Log.i("test","step 7");
//
//    }//getQuestionContent

}
