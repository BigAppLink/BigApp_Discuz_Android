package com.kit.widget.sliderview;
import com.kit.extend.widget.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
 
public class SampleActivity extends Activity implements OnClickListener,
        OnItemClickListener {
    SliderView sliderView;
    ListView listView;
 
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.silderview_activity);
        sliderView = (SliderView) findViewById(R.id.slider_view);
        Button button1 = (Button) findViewById(R.id.button1);
        Button button2 = (Button) findViewById(R.id.button2);
        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        listView = (ListView) findViewById(R.id.listView);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, new String[] { "GOOD",
                        "HAHA", "What is that?", "FUUUUCK" });
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
    }
 
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }
 
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        sliderView.setSlided(!sliderView.isSlided());
        return true;
    }
 
    @Override
    public void onClick(View v) {
        Toast.makeText(this, ((Button) v).getText().toString(),
                Toast.LENGTH_LONG).show();
    }
 
    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        String string = (String) arg0.getItemAtPosition(arg2);
        Toast.makeText(this, string, Toast.LENGTH_LONG).show();
    }
}
