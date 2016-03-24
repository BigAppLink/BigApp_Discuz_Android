package com.kit.widget.scrollview.pullscrollview;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.kit.extend.widget.R;

/**
 * Demo
 *
 * @date 2014-04-30
 */
public class PullScrollViewSampleActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pullscrollview_main);

        findViewById(R.id.pulldown_scrollview_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PullScrollViewSampleActivity.this, PulldownViewActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.stretch_scrollview_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PullScrollViewSampleActivity.this, StretchViewActivity.class);
                startActivity(intent);
            }
        });
    }

}
