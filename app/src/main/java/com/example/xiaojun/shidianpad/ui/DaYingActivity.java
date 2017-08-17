package com.example.xiaojun.shidianpad.ui;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.support.v4.print.PrintHelper;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import com.example.xiaojun.shidianpad.R;

public class DaYingActivity extends Activity {
    private LinearLayout baocun;
    private Button dayin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.daying);


        baocun= (LinearLayout) findViewById(R.id.baocun);
        dayin= (Button) findViewById(R.id.dayin);
        dayin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PrintHelper photoPrinter = new PrintHelper(DaYingActivity.this);
                photoPrinter.setScaleMode(PrintHelper.SCALE_MODE_FILL);
                Bitmap bitmap = createViewBitmap(baocun);
                photoPrinter.printBitmap("churuzheng.jpg - test print", bitmap);
            }
        });

    }

    private Bitmap createViewBitmap(View view){

        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);

        return bitmap;
    }
}
