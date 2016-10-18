package com.example.administrator.ourapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by Administrator on 2016/10/18.
 */

public class FragMesOnNotLogin extends Fragment  {
    private RelativeLayout rootView;
    private TextView signup,login;
    @Nullable
    @Override
    public View onCreateView( LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView=(RelativeLayout)inflater.inflate(R.layout.mes_not_login,null);
        signup=(TextView)rootView.findViewById(R.id.signup);
        login=(TextView)rootView.findViewById(R.id.login);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getContext(),SignUp.class);
                startActivity(intent);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getContext(),Login.class);
                startActivity(intent);
            }
        });
        return rootView;
    }

}
