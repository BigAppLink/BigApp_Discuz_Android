 //XRTextView类 
 package com.kit.widget.textview;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
public class XRTextView extends TextView{ 
 private final String namespace = "rong.android.TextView";
 private String text;
 private float textSize;
 private float paddingLeft;
 private float paddingRight;
 private float marginLeft;
 private float marginRight;
 private int textColor;
 private JSONArray colorIndex;
 private Paint paint1 = new Paint();
 private Paint paintColor = new Paint();
 private float textShowWidth;
 private float Spacing = 0;
 private float LineSpacing = 1.3f;//行与行的间距

 public XRTextView(Context context, AttributeSet attrs) {
  super(context, attrs);
  text = attrs.getAttributeValue(
    "http://schemas.android.com/apk/res/android", "text");
  textSize = attrs.getAttributeIntValue(namespace, "textSize", 25);//字体大小
  textColor = attrs.getAttributeIntValue(namespace, "textColor",Color.BLUE);//字体颜色
  paddingLeft = attrs.getAttributeIntValue(namespace, "paddingLeft", 0);
  paddingRight = attrs.getAttributeIntValue(namespace, "paddingRight", 0);
  marginLeft = attrs.getAttributeIntValue(namespace, "marginLeft", 0);
  marginRight = attrs.getAttributeIntValue(namespace, "marginRight", 0);
  paint1.setTextSize(textSize);
  paint1.setColor(textColor);
  paint1.setAntiAlias(true);
  paintColor.setAntiAlias(true);
  paintColor.setTextSize(textSize);
  paintColor.setColor(Color.BLUE);
 }
 public XRTextView(Context context, float textSize, int textColor, float paddingLeft, float paddingRight, float marginLeft, float marginRight){
  super(context);
  this.textSize = textSize;
  this.textColor = textColor;
  this.paddingLeft = paddingLeft;
  this.paddingRight = paddingRight;
  this.marginLeft = marginLeft;
  this.marginRight = marginRight;
  paint1.setTextSize(textSize);
  paint1.setColor(textColor);
  paint1.setAntiAlias(true);
  paintColor.setAntiAlias(true);
  paintColor.setTextSize(textSize);
  paintColor.setColor(Color.BLUE);
 }

 public JSONArray getColorIndex() {
  return colorIndex;
 }
 public void setColorIndex(JSONArray colorIndex) {
  this.colorIndex = colorIndex;
 }
 /**
  * 传入一个索引，判断当前字是否被高亮
  * @param index
  * @return
  * @throws JSONException
  */
 public boolean isColor(int index) throws JSONException{
  if(colorIndex == null){
   return false;
  }
  for(int i = 0 ; i < colorIndex.length() ; i ++){
   JSONArray array = colorIndex.getJSONArray(i);
   int start = array.getInt(0);
   int end = array.getInt(1)-1;
   if(index >= start && index <= end){
    return true;
   }

  }


  return false;
 }

 @Override
 protected void onDraw(Canvas canvas) {
//  super.onDraw(canvas);
  View view=(View)this.getParent();
  textShowWidth=view.getMeasuredWidth()-paddingLeft - paddingRight - marginLeft - marginRight;
  int lineCount = 0;

  text = this.getText().toString();//.replaceAll("\n", "\r\n");
  if(text==null)return;
  char[] textCharArray = text.toCharArray();
  // 已绘的宽度
  float drawedWidth = 0;
  float charWidth;
  for (int i = 0; i < textCharArray.length; i++) {
   charWidth = paint1.measureText(textCharArray, i, 1);

   if(textCharArray[i]=='\n'){
    lineCount++;
    drawedWidth = 0;
    continue;
   }
   if (textShowWidth - drawedWidth < charWidth) {
    lineCount++;
    drawedWidth = 0;
   }
   boolean color = false;
   try {
    color = isColor(i);
   } catch (JSONException e1) {
    // TODO Auto-generated catch block
    e1.printStackTrace();
   }

   if(color){

    canvas.drawText(textCharArray, i, 1, paddingLeft + drawedWidth,
      (lineCount + 1) * textSize * LineSpacing, paintColor);
   }else{

    canvas.drawText(textCharArray, i, 1, paddingLeft + drawedWidth,
      (lineCount + 1) * textSize * LineSpacing, paint1);
   }
   if(textCharArray[i] > 127 && textCharArray[i] != '、' && textCharArray[i] != '，' && textCharArray[i] != '。' && textCharArray[i] != '：' && textCharArray[i] != '！'){
    drawedWidth += charWidth + Spacing;

   }else{
    drawedWidth += charWidth;
   }
  }
  setHeight((int) ((lineCount + 1) * (int) textSize * LineSpacing + 10));
 }
 public float getSpacing() {
  return Spacing;
 }
 public void setSpacing(float spacing) {
  Spacing = spacing;
 }
 public float getMYLineSpacing() {
  return LineSpacing;
 }
 public void setMYLineSpacing(float lineSpacing) {
  LineSpacing = lineSpacing;
 }
 public float getMYTextSize() {
  return textSize;
 }
 public void setMYTextSize(float textSize) {
  this.textSize = textSize;
  paint1.setTextSize(textSize);
  paintColor.setTextSize(textSize);
 }

  
} 