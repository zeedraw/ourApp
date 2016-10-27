package com.example.administrator.ourapp;

        import android.app.Activity;
        import android.content.Intent;
        import android.os.Bundle;
        import android.util.Log;
        import android.view.View;
        import android.widget.Button;
        import android.widget.EditText;

        import com.example.administrator.ourapp.message.Message_tools;

        import cn.bmob.v3.BmobUser;
        import cn.bmob.v3.exception.BmobException;
        import cn.bmob.v3.listener.UpdateListener;
        import me.imid.swipebacklayout.lib.app.SwipeBackActivity;


public class mission_audit_reply extends SwipeBackActivity {

    private Button pass;
    private Button reject;
    private EditText reason;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.authentication_reply);
        pass = (Button) findViewById(R.id.pass_authentication);
        reject = (Button) findViewById(R.id.reject_authentication);
        reason = (EditText) findViewById(R.id.reject_reason);

        Intent intent = getIntent();
        final MyUser receiver = (MyUser)intent.getSerializableExtra("user");
        final String mission_ID = (String)intent.getSerializableExtra("mission_ID");

        pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Mission mission = new Mission();
                mission.setState(2);
                mission.update(mission_ID, new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if(e==null){
                            Log.i("bmob","更新成功");
                        }else{
                            Log.i("bmob","更新失败："+e.getMessage()+","+e.getErrorCode());
                        }
                    }
                });


                Message_tools ms = new Message_tools();
                ms.send(BmobUser.getCurrentUser(MyUser.class),
                        receiver, "恭喜您，您提交的任务通过了审核！", 18,
                        false, "", mission_audit_reply.this);
            }
        });

        reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String reject_reason = null;
                if(reason.getText().length() == 0){
                    reject_reason = "暂无说明，请再认证一次试试。";
                }
                else{
                    reject_reason = reason.getText().toString().trim();
                }

                Message_tools ms = new Message_tools();
                ms.send(BmobUser.getCurrentUser(MyUser.class),
                        receiver, "很遗憾，您提交的任务审核没有通过。", 19,
                        false, reject_reason,
                        mission_audit_reply.this);
            }
        });
    }//onCreate
}
