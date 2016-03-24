package cn.sharesdk.update;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.TextView;
import cn.sharesdk.analysis.R;

public class UpdateDialogActivity extends Activity implements OnClickListener{
	
	private int dialogBtnClick = UpdateStatus.NotNow;
	private UpdateResponse response;
	private CheckBox cBox;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setTheme(R.style.AnalysisSDK_CommonDialog);
		setContentView(R.layout.analysissdk_update_notify_dialog);
		
		//TextView tvTitle = (TextView) findViewById(R.id.tv_dialog_title);
		TextView tvContent = (TextView) findViewById(R.id.update_tv_dialog_content);
		findViewById(R.id.update_btn_dialog_ok).setOnClickListener(this);
		findViewById(R.id.update_btn_dialog_cancel).setOnClickListener(this);

		response = (UpdateResponse) getIntent().getSerializableExtra("response");		
		StringBuilder updateMsg = new StringBuilder();
		updateMsg.append(response.version);
		updateMsg.append(getString(R.string.analysissdk_update_new_impress));
		updateMsg.append("\n");
		updateMsg.append(response.content);
		updateMsg.append("\n");
		updateMsg.append(getString(R.string.analysissdk_update_apk_size, response.size));
		
		tvContent.setText(updateMsg);
		//tvTitle.setText(R.string.analysissdk_update_title);
		
		cBox = (CheckBox) findViewById(R.id.update_cb_ignore);		
		if(UpdateConfig.isUpdateForce()){
			cBox.setVisibility(View.GONE);
		}else {
			cBox.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.update_btn_dialog_ok){
			dialogBtnClick = UpdateStatus.Update;
		}else if(v.getId() == R.id.update_btn_dialog_cancel){
			if(cBox.isChecked()){
				dialogBtnClick = UpdateStatus.Ignore;
			}
		}
		this.finish();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		UpdateAgent.updateDialogDismiss(this, dialogBtnClick, response);
	}
	
}
