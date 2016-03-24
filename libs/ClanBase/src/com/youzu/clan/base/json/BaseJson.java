package com.youzu.clan.base.json;

import com.youzu.android.framework.json.annotation.JSONField;
import com.youzu.clan.base.json.model.Message;
import com.youzu.clan.base.json.model.Variables;

import java.io.Serializable;


public class BaseJson implements Serializable {
	private static final long serialVersionUID = 3826982462252968763L;
	private String version;
	private String charset;
	private Message message;
	private String requestId;




	public String getVersion() {
		return version;
	}
	@JSONField(name="Version")
	public void setVersion(String version) {
		this.version = version;
	}

	public String getCharset() {
		return charset;
	}
	@JSONField(name="Charset") 
	public void setCharset(String charset) {
		this.charset = charset;
	}

	public Message getMessage() {
		return message;
	}
	@JSONField(name="Message") 
	public void setMessage(Message message) {
		this.message = message;
	}


	public String getRequestId() {
		return requestId;
	}
	@JSONField(name="request_id")
	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	public Variables getVariables() {
		return null;
	}
//
//	public void setVariables(Variables variables) {
//		this.variables = variables;
//	}
}
