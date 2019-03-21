package com.yqing.welcome;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.yqing.servercenter.EasySCM;
import com.yqing.servercenter.ScCallBack;


public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                enteryMain();
            }
        }, 3000);

    }

    private void enteryMain() {
        try {
            EasySCM.getInstance().req(this, "entryHome", new ScCallBack() {
                @Override
                public void onCallback(boolean b, Bundle bundle, String s) {
                    if (b) {
                        finish();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
