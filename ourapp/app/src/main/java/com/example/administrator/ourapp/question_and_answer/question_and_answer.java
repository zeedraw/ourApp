package com.example.administrator.ourapp.question_and_answer;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.example.administrator.ourapp.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import cn.bmob.v3.BmobQuery;
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
    private Vector<String> question_date = new Vector<String>();       //问题的发布日期
    private Vector<String> question_content = new Vector<String>();    //问题的内容



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.questoin_and_answer_listview);
        listView  = (ListView) findViewById(R.id.QAlistview);
        initWidget();
        getQuestionContent();
        question_content.add("问题测试");
        question_date.add("2016-9-19");
        List<Map<String, Object>> listItems = new ArrayList<Map<String, Object>>();
        for(int i = 0; i < question_date.size(); ++i){
            Map<String, Object> listItem = new HashMap<String, Object>();
            listItem.put("question_date", question_date.get(i));
            listItem.put("question_content", question_content.get(i));
            listItems.add(listItem);
        }//for i

        //创建一个SimpleAdapter
        SimpleAdapter simpleAdapter = new SimpleAdapter(this, listItems, R.layout.question_and_answer_item,
                new String[] {"question_date", "question_content"}, new int[] {R.id.date, R.id.question});
        listView.setAdapter(simpleAdapter);
    }//onCreate


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
    }//initwidget


    private int getQuestionContent(){
        BmobQuery<Mission_question> query = new BmobQuery<Mission_question>();
//查询playerName叫“比目”的数据
        query.addWhereEqualTo("objectId", "KNxs444I");
//返回100条数据，如果不加上这条语句，默认返回10条数据
        query.setLimit(100);
//执行查询方法
        query.findObjects(new FindListener<Mission_question>() {
            @Override
            public void done(List<Mission_question> object, BmobException e) {
                if(e==null){
                    for (Mission_question question : object) {

                        //获得createdAt数据创建时间（注意是：createdAt，不是createAt）
                        question_date.add(question.getCreatedAt());
                        //获得问题的内容
                        question_content.add(question.getContent());
                    }//for
                }//if
                else{
                    Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                }//else
            }//done
        });
        return 0;
    }//getQuestionContent

}
