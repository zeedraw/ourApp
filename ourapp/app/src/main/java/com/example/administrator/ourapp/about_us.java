package com.example.administrator.ourapp;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

/**
 * Created by Longze on 2016/10/27.
 */
public class about_us extends SwipeBackActivity {
    private TextView return_bt,commit_bt;//标题上的左右按钮
    private TextView info_title;//标题
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_us);
        return_bt = (TextView) findViewById(R.id.lbt);
        info_title = (TextView) findViewById(R.id.mission_title);

        return_bt.setText("返回");
        info_title.setText("提问");
        return_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                about_us.this.finish();
            }
        });

    }
}
