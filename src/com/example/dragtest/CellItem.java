package com.example.dragtest;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.TextView;
import android.util.Log;

public class CellItem extends TextView{
	private Layer mLayer;
	private static final String TAG = "cellItem";
	public CellItem(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
	
	public boolean onTouchEvent(MotionEvent ev){
		//use the layer' onTouchEvent
		Log.d(TAG,"onTouchEvent");
//		if(null == mLayer){
//			return false;
//		}
		//return mLayer.onTouchEvent(ev);
		return super.onTouchEvent(ev);
	}
    
	public void setLayer(Layer mLayer){
		this.mLayer = mLayer;
	}

}
