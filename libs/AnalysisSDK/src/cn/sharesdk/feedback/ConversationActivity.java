package cn.sharesdk.feedback;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import cn.sharesdk.analysis.R;
import cn.sharesdk.feedback.model.Reply;
import cn.sharesdk.feedback.model.Store;
import cn.sharesdk.feedback.model.UserReply;

public class ConversationActivity extends Activity implements OnClickListener{
	
	private View backView;
	private TextView tvTitle;
	private EditText etContent;
	private Button btnSendMsg;
	private ImageView imvToContact;
	private ConversationAdapter adapter;
	private ConversationListView listView;
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.setContentView(R.layout.analysissdk_fb_conversation_activity);
		
		initView();
	}
	
	private void initView(){
		backView = findViewById(R.id.ll_back);
		tvTitle = (TextView) findViewById(R.id.tv_title);
		etContent = (EditText) findViewById(R.id.et_msg);
		btnSendMsg = (Button) findViewById(R.id.btn_send_msg);	
		imvToContact = (ImageView) findViewById(R.id.imv_add_contact);
		tvTitle.setText(R.string.analysissdk_fb_tv_title_conversation);
		backView.setOnClickListener(this);
		btnSendMsg.setOnClickListener(this);
		imvToContact.setOnClickListener(this);
		
		listView = (ConversationListView) findViewById(R.id.listview);
		adapter = new ConversationAdapter(listView);
		adapter.setHandler(new Handler(adapter));
		listView.setAdapter(adapter);
		listView.performPulling(true);
		
	}
	
	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.btn_send_msg){
			String msg = etContent.getText().toString();
			if(!TextUtils.isEmpty(msg)){
				adapter.addNewRelyToList(new UserReply(msg));
				adapter.notifyDataSetChanged();
				//后台发送
				etContent.getText().clear();
				FeedbackAgent.sendFeedback(this, msg);
			}
		}else if(v.getId() == R.id.ll_back){
			this.finish();
		}else if(v.getId() == R.id.imv_add_contact){
			Intent intent = new Intent();
			intent.setClass(this, ContactActivity.class);
			startActivity(intent);				
		}
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		if(adapter == null){
			return;
		}
		ArrayList<Reply> conversations = adapter.getConversations();
		if(conversations != null && conversations.size() > 0){
			//退出时，保存数据列表
			Store.getInstance(ConversationActivity.this).saveCoversation(conversations);
		}
	}
	
}
