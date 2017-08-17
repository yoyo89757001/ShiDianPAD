package com.example.xiaojun.shidianpad.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.example.xiaojun.shidianpad.R;
import com.example.xiaojun.shidianpad.dialog.JiaZaiDialog;

public class JiaZaiActivity extends Activity {
    public  JiaZaiDialog dialog=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jia_zai);
        dialog=new JiaZaiDialog(this);
        dialog.setText("系统初始化中...");
        dialog.show();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(8000);
                    if (dialog!=null){
                        dialog.dismiss();
                    }

                    finish();

                    startActivity(new Intent(JiaZaiActivity.this,InFoActivity.class));

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }
}
