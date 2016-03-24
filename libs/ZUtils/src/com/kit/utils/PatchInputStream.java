package com.kit.utils;

import java.io.ByteArrayOutputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

public class PatchInputStream extends FilterInputStream{

	public PatchInputStream(InputStream in) {
		super(in);
		// TODO Auto-generated constructor stub
	}
	
	public long skip(long n)throws IOException{
		long m=0l;
		while(m<n){
			long _m=in.skip(n-m);
			if(_m==0l){
				break;
			}
			m+=_m;
		}
		return m;
	}
	
	
	

	
	/*
	 * 得到图片字节流 数组大小
	 * */
	public static byte[] readStream(InputStream inStream) throws Exception{      
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();      
        byte[] buffer = new byte[1024];      
        int len = 0;      
        while( (len=inStream.read(buffer)) != -1){      
            outStream.write(buffer, 0, len);      
        }      
        outStream.close();      
        inStream.close();      
        return outStream.toByteArray();      
    }
}