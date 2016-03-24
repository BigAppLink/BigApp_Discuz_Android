package cn.sharesdk.applist;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import cn.sharesdk.analysis.R;
import cn.sharesdk.applist.APPDataList.APP;

public class AppDialog extends Dialog implements OnItemClickListener{

	Context context;
	Button btn_back;
	TextView tv_title;
	APPListView listview;
	APPAdapter adapter;
	
	public AppDialog(Context context) {
		super(context);
		this.context = context;
	}
	
	public AppDialog(Context context, int theme){
		super(context, theme);
		this.context = context;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.analysissdk_app_list_activity);

		initView();
		initData();
	}
	
	
	private void initView(){
		btn_back = (Button) findViewById(R.id.btn_back);
		tv_title = (TextView) findViewById(R.id.tv_title);
		listview = (APPListView) findViewById(R.id.listview);
		
		btn_back.setOnClickListener(new android.view.View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dismiss();
			}
		});
		
	}
	
	private void initData(){
		adapter = new APPAdapter(listview);
		adapter.getListView().setOnItemClickListener(this);
		adapter.setHandler(new Handler(adapter));
		listview.setAdapter(adapter);
		listview.performPulling(true);
	}
	
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
		APP app = adapter.getItem(position);
		Toast.makeText(context, "title === iconUrl ====>>>" + app.title + app.iconUrl, Toast.LENGTH_SHORT).show();
		//Intent service = new Intent(context, RemoteService.class);
		// 由intent启动service，后台运行下载进程，在服务中调用notifycation状态栏显示进度条
		//if (position <= 5) {
		//	service.putExtra("url", "http://cdn.market.hiapk.com/data/upload/2014/02_26/19/com.ahzs.s91_190044.apk");
		//} else {
		//	service.putExtra("url", "http://market.hiapk.com/data/upload/market/BarcodeScanner_V3.53.apk");
		//}
		// TODO 以后备用，可以修改
		//service.setAction(RemoteService.DOWNLOAD_APK);
		//context.startService(service);
	}
}
