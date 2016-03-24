package com.keyboard.bean;

public class EmoticonBean {

	public final static int FACE_TYPE_DEL = 1;
	public final static int FACE_TYPE_NOMAL = 0;
	public final static int FACE_TYPE_USERDEF = 2;

	private long id;
	/**
	 * 内容
	 */
	private String content;

	/**
	 * 点击处理事件类型
	 */
	private long eventType;

	/**
	 * 组别
	 */
	private String groupName;

	/**
	 * 表情图标
	 */
	private String iconUri;

	/**
	 * 短名称 可设为文件名（不包含路径）
	 */
	private String shortName;

	public EmoticonBean() {
	}

	
	public long getId() {
		return id;
	}


	public void setId(long id) {
		this.id = id;
	}


	public EmoticonBean(long eventType, String iconUri, String content) {
		this.eventType = eventType;
		this.iconUri = iconUri;
		this.content = content;
	}
	
	public static String fromChar(char ch) {
		return Character.toString(ch);
	}
	public static String fromChars(String chars) {
		return chars;
	}
	public static String fromCodePoint(int codePoint) {
		return newString(codePoint);
	}
	public static final String newString(int codePoint) {
		return new String(new int[] { codePoint }, 0, 1);
	}
	
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}

	public long getEventType() {
		return eventType;
	}
	public void setEventType(long eventType) {
		this.eventType = eventType;
	}


	public String getIconUri() {
		return iconUri;
	}
	public void setIconUri(String iconUri) {
		this.iconUri = iconUri;
	}

	public String getShortName() {
		return shortName;
	}
	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
}
