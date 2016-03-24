package com.kit.widget.wheel.demo;

import com.kit.extend.widget.R;
import com.kit.widget.wheel.OnWheelChangedListener;
import com.kit.widget.wheel.WheelView;
import com.kit.widget.wheel.adapters.ArrayWheelAdapter;
import com.kit.widget.wheel.adapters.NumericWheelAdapter;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

public class SpeedActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.wheel_speed1_layout);
        final WheelView speed = (WheelView) findViewById(R.id.speed);
        final SpeedAdapter speedAdapter = new SpeedAdapter(this, 245, 5);
        speed.setViewAdapter(speedAdapter);
        
        final String unitsValues[] = new String[] {
                "km/h",
                "m/h",
                "m/s",
        };
        
        final WheelView units = (WheelView) findViewById(R.id.units);
        ArrayWheelAdapter<String> unitsAdapter =
            new ArrayWheelAdapter<String>(this, unitsValues);
        unitsAdapter.setItemResource(R.layout.wheel_units_item);
        unitsAdapter.setItemTextResource(R.id.text);
        unitsAdapter.setEmptyItemResource(R.layout.wheel_units_item);
        units.setViewAdapter(unitsAdapter);
        //units.setVisibleItems(3);
        
        units.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                String value = unitsValues[units.getCurrentItem()];
                speedAdapter.setUnits(" " + value);
                speed.invalidateWheel(false);
            }
        });
        
        units.setCurrentItem(1);
    }
    
    /**
     * Speed adapter
     */
    private class SpeedAdapter extends NumericWheelAdapter {
        // Items step value
        private int step;

        /**
         * Constructor
         */
        public SpeedAdapter(Context context, int maxValue, int step) {
            super(context, 0, maxValue / step);
            this.step = step;
            
            setItemResource(R.layout.wheel_text_item);
            setItemTextResource(R.id.text);
        }
        
        /**
         * Sets units
         */
        public void setUnits(String units) {
            //this.units = units;
        }
        
        @Override
        public CharSequence getItemText(int index) {
            if (index >= 0 && index < getItemsCount()) {
                int value = index * step;
                return Integer.toString(value);
            }
            return null;
        }
    }
}
