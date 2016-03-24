package cn.sharesdk.feedback;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import cn.sharesdk.analysis.R;
import cn.sharesdk.feedback.model.Store;
import cn.sharesdk.feedback.model.UserInfo;

public class ContactActivity extends Activity implements OnClickListener {

	private View backView;
	private TextView tvTitle;
	private EditText etName;
	private EditText etContact;
	private Button btnEnsure;

	private UserInfo userInfo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.setContentView(R.layout.analysissdk_fb_add_contact_activity);

		initView();
		initData();
	}

	private void initView() {
		backView = findViewById(R.id.ll_back);
		tvTitle = (TextView) findViewById(R.id.tv_title);
		etName = (EditText) findViewById(R.id.et_name);
		etContact = (EditText) findViewById(R.id.et_contact);
		btnEnsure = (Button) findViewById(R.id.btn_ensure);

		tvTitle.setText(R.string.analysissdk_fb_tv_title_contact);
		backView.setOnClickListener(this);
		btnEnsure.setOnClickListener(this);
	}

	private void initData() {
		userInfo = Store.getInstance(this).getUserInfo();
		if (userInfo == null) {
			return;
		}
		if (!TextUtils.isEmpty(userInfo.userName)) {
			etName.setText(userInfo.userName);
		}
		if (!TextUtils.isEmpty(userInfo.userContact)) {
			etContact.setText(userInfo.userContact);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.ll_back) {
			saveContact();
		} else if (v.getId() == R.id.btn_ensure) {
			saveContact();
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			saveContact();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	private void saveContact() {
		String name = etName.getText().toString();
		String contact = etContact.getText().toString();
		if (!TextUtils.isEmpty(name) || !TextUtils.isEmpty(contact)) {
			Store.getInstance(this).saveUserInfo(new UserInfo(name, contact));
//			if (userInfo != null) {
//				if (!userInfo.userName.equals(name) || !userInfo.userContact.equals(contact)) {
//					FeedbackAgent.sendFeedbackContact(this, name, contact);
//				}
//			}else {
				FeedbackAgent.sendFeedbackContact(this, name, contact);
//			}
		}
		this.finish();
	}

}
