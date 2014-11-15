package com.ymbGNUh.LAND_Guard;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.SubMenu;
import android.view.View;
import android.widget.Button;

public class MyActivity extends Activity {
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        Button startUsing = (Button) findViewById(R.id.startButton);
        startUsing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyActivity.this, SubmitActivity.class);
                MyActivity.this.startActivity(intent);
            }
        });
    }
}
