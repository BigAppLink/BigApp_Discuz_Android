package com.kit.widget.dialog;

import java.lang.reflect.Field;

import android.app.DatePickerDialog;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.LinearLayout;

public class HiddenPickerDialog extends DatePickerDialog {
	
	private boolean isShowDay;
	
	 public HiddenPickerDialog(Context context,
             OnDateSetListener callBack, int year, int monthOfYear) {
         super(context, callBack, year, monthOfYear, 3);
         this.setTitle(year+"年"+(monthOfYear + 1) + "月" );
     }

	 public HiddenPickerDialog(Context context,
             OnDateSetListener callBack, int year, int monthOfYear,int day,boolean isShowDay) {
         super(context, callBack, year, monthOfYear, day);
       
         this.isShowDay=isShowDay;
         if(isShowDay){
        	  this.setTitle(year+"年"+(monthOfYear + 1) + "月" +day+"日");
         }else{
        	  this.setTitle(year+"年"+(monthOfYear + 1) + "月" );
         }
       
     }
	 
     @Override
     public void onDateChanged(DatePicker view, int year, int month, int day) {
         super.onDateChanged(view, year, month, day);
         this.setTitle(year+"年"+(month + 1) + "月" );
     }

	/* (non-Javadoc)
	 * @see android.app.DatePickerDialog#show()
	 */
	@Override
	public void show() {
		// TODO Auto-generated method stub
		super.show();
		 DatePicker dp = findDatePicker((ViewGroup) this.getWindow().getDecorView());
	        if (dp != null) {
	        	Class c=dp.getClass();
	        	
	        	Field[] fields = c.getDeclaredFields();
	        	
	        	Field f;
				try {
					
					for (Field field : fields) {
		        		String fieldName = field.getName();
		        		if ("mDayPicker".equals(fieldName) || "mDaySpinner".equals(fieldName)) {
		        			field.setAccessible(true);
		        			View dayView = (View) field.get(dp);
		        			if(!isShowDay){
		        			   dayView.setVisibility(View.GONE);
		        			}
		        			break;
		        		}
		        	}
					
					
//					f = c.getDeclaredField("mDayPicker" );
//					f.setAccessible(true );  
//					LinearLayout l= (LinearLayout)f.get(dp);   
//					l.setVisibility(View.GONE);
				} catch (SecurityException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}  
	        	
	        } 
	}
	/**
     * 从当前Dialog中查找DatePicker子控件
     * 
     * @param group
     * @return
     */
    private DatePicker findDatePicker(ViewGroup group) {
        if (group != null) {
            for (int i = 0, j = group.getChildCount(); i < j; i++) {
                View child = group.getChildAt(i);
                if (child instanceof DatePicker) {
                    return (DatePicker) child;
                } else if (child instanceof ViewGroup) {
                    DatePicker result = findDatePicker((ViewGroup) child);
                    if (result != null)
                        return result;
                }
            }
        }
        return null;

    } 
    
    public void hideWhich(boolean isYear, boolean isMonth, boolean isDay){
    	
    }
      
}