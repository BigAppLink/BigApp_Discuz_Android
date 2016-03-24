package com.kit.widget.energycurve;

import java.util.ArrayList;

import com.kit.extend.widget.R;

import android.os.Bundle;
import android.app.Activity;
import android.util.DisplayMetrics;
import android.view.Menu;

public class MainActivity extends Activity {

    private EnergyCurveView erenergyCurve;
    private DisplayMetrics dm = new DisplayMetrics();

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        erenergyCurve = (EnergyCurveView) findViewById(R.id.erenergycurve);
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        erenergyCurve.setWindowsWH(dm);
        ArrayList<EnergyItem> energys = new ArrayList<EnergyItem>();
        energys.add(new EnergyItem("1", 0.0f, "无"));
        energys.add(new EnergyItem("2", 0.0f, "无"));
        energys.add(new EnergyItem("3", 8.0f, "无"));
        energys.add(new EnergyItem("4", 6.0f, "无"));
        energys.add(new EnergyItem("5", 2.8f, "无"));
        energys.add(new EnergyItem("6", 0.0f, "无"));
        energys.add(new EnergyItem("7", 10.0f, "无"));
        erenergyCurve.setData(energys, EnergyType.DAY);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
}
