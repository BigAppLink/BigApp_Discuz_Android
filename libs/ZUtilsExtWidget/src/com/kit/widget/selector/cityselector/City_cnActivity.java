//package com.kit.widget.selector.cityselector;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import com.kit.extend.widget.R;
//
//import android.app.Activity;
//import android.database.Cursor;
//import android.database.sqlite.SQLiteDatabase;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.View;
//import android.widget.AdapterView;
//import android.widget.ArrayAdapter;
//import android.widget.Spinner;
//import android.widget.AdapterView.OnItemSelectedListener;
//import android.widget.Toast;
//
//
//public class City_cnActivity extends Activity {
//    /** Called when the activity is first created. */
//	private DBManager dbm;
//	private SQLiteDatabase db;
//	private Spinner spinner1 = null;
//	private Spinner spinner2=null;
//	private Spinner spinner3=null;
//	private String province=null;
//	private String city=null;
//	private String district=null;
//	
//	
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.city_selector_spinner_activity);
//        spinner1=(Spinner)findViewById(R.id.spinner1);
//        spinner2=(Spinner)findViewById(R.id.spinner2);
//        spinner3=(Spinner)findViewById(R.id.spinner3);
//		spinner1.setPrompt("省");
//		spinner2.setPrompt("城市");		
//		spinner3.setPrompt("地区");
//		
//        initSpinner1();
//    }
//    
//    public void initSpinner1(){
//		dbm = new DBManager(this);
//	 	dbm.openDatabase();
//	 	db = dbm.getDatabase();
//	 	List<Place> list = new ArrayList<Place>();
//		
//	 	try {    
//	        String sql = "select * from province";  
//	        Cursor cursor = db.rawQuery(sql,null);  
//	        cursor.moveToFirst();
//	        while (!cursor.isLast()){ 
//		        String code=cursor.getString(cursor.getColumnIndex("code")); 
//		        byte bytes[]=cursor.getBlob(2); 
//		        String name=new String(bytes,"gbk");
//		        Place myListItem=new Place();
//		        myListItem.setName(name);
//		        myListItem.setPcode(code);
//		        list.add(myListItem);
//		        cursor.moveToNext();
//	        }
//	        String code=cursor.getString(cursor.getColumnIndex("code")); 
//	        byte bytes[]=cursor.getBlob(2); 
//	        String name=new String(bytes,"gbk");
//	        Place myListItem=new Place();
//	        myListItem.setName(name);
//	        myListItem.setPcode(code);
//	        list.add(myListItem);
//	        
//	    } catch (Exception e) {  
//	    } 
//	 	dbm.closeDatabase();
//	 	db.close();	
//	 	
//	 	SpinnerAdapter myAdapter = new SpinnerAdapter(this,list);
//	 	spinner1.setAdapter(myAdapter);
//		spinner1.setOnItemSelectedListener(new SpinnerOnSelectedListener1());
//	}
//    public void initSpinner2(String pcode){
//		dbm = new DBManager(this);
//	 	dbm.openDatabase();
//	 	db = dbm.getDatabase();
//	 	List<Place> list = new ArrayList<Place>();
//		
//	 	try {    
//	        String sql = "select * from city where pcode='"+pcode+"'";  
//	        Cursor cursor = db.rawQuery(sql,null);  
//	        cursor.moveToFirst();
//	        while (!cursor.isLast()){ 
//		        String code=cursor.getString(cursor.getColumnIndex("code")); 
//		        byte bytes[]=cursor.getBlob(2); 
//		        String name=new String(bytes,"gbk");
//		        Place myListItem=new Place();
//		        myListItem.setName(name);
//		        myListItem.setPcode(code);
//		        list.add(myListItem);
//		        cursor.moveToNext();
//	        }
//	        String code=cursor.getString(cursor.getColumnIndex("code")); 
//	        byte bytes[]=cursor.getBlob(2); 
//	        String name=new String(bytes,"gbk");
//	        Place myListItem=new Place();
//	        myListItem.setName(name);
//	        myListItem.setPcode(code);
//	        list.add(myListItem);
//	        
//	    } catch (Exception e) {  
//	    } 
//	 	dbm.closeDatabase();
//	 	db.close();	
//	 	
//	 	SpinnerAdapter myAdapter = new SpinnerAdapter(this,list);
//	 	spinner2.setAdapter(myAdapter);
//		spinner2.setOnItemSelectedListener(new SpinnerOnSelectedListener2());
//	}
//    public void initSpinner3(String pcode){
//		dbm = new DBManager(this);
//	 	dbm.openDatabase();
//	 	db = dbm.getDatabase();
//	 	List<Place> list = new ArrayList<Place>();
//		
//	 	try {    
//	        String sql = "select * from district where pcode='"+pcode+"'";  
//	        Cursor cursor = db.rawQuery(sql,null);  
//	        cursor.moveToFirst();
//	        while (!cursor.isLast()){ 
//		        String code=cursor.getString(cursor.getColumnIndex("code")); 
//		        byte bytes[]=cursor.getBlob(2); 
//		        String name=new String(bytes,"gbk");
//		        Place myListItem=new Place();
//		        myListItem.setName(name);
//		        myListItem.setPcode(code);
//		        list.add(myListItem);
//		        cursor.moveToNext();
//	        }
//	        String code=cursor.getString(cursor.getColumnIndex("code")); 
//	        byte bytes[]=cursor.getBlob(2); 
//	        String name=new String(bytes,"gbk");
//	        Place myListItem=new Place();
//	        myListItem.setName(name);
//	        myListItem.setPcode(code);
//	        list.add(myListItem);
//	        
//	    } catch (Exception e) {  
//	    } 
//	 	dbm.closeDatabase();
//	 	db.close();	
//	 	
//	 	SpinnerAdapter myAdapter = new SpinnerAdapter(this,list);
//	 	spinner3.setAdapter(myAdapter);
//		spinner3.setOnItemSelectedListener(new SpinnerOnSelectedListener3());
//	}
//    
//	class SpinnerOnSelectedListener1 implements OnItemSelectedListener{
//		
//		public void onItemSelected(AdapterView<?> adapterView, View view, int position,
//				long id) {
//			province=((Place) adapterView.getItemAtPosition(position)).getName();
//			String pcode =((Place) adapterView.getItemAtPosition(position)).getPcode();
//			
//			initSpinner2(pcode);
//			initSpinner3(pcode);
//		}
//
//		public void onNothingSelected(AdapterView<?> adapterView) {
//			// TODO Auto-generated method stub
//		}		
//	}
//	class SpinnerOnSelectedListener2 implements OnItemSelectedListener{
//		
//		public void onItemSelected(AdapterView<?> adapterView, View view, int position,
//				long id) {
//			city=((Place) adapterView.getItemAtPosition(position)).getName();
//			String pcode =((Place) adapterView.getItemAtPosition(position)).getPcode();
//
//			initSpinner3(pcode);
//		}
//
//		public void onNothingSelected(AdapterView<?> adapterView) {
//			// TODO Auto-generated method stub
//		}		
//	}
//	
//	class SpinnerOnSelectedListener3 implements OnItemSelectedListener{
//		
//		public void onItemSelected(AdapterView<?> adapterView, View view, int position,
//				long id) {
//			district=((Place) adapterView.getItemAtPosition(position)).getName();
//			Toast.makeText(City_cnActivity.this, province+" "+city+" "+district, Toast.LENGTH_LONG).show();
//		}
//
//		public void onNothingSelected(AdapterView<?> adapterView) {
//			// TODO Auto-generated method stub
//		}		
//	}
//}