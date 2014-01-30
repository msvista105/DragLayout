package com.example.dragtest;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.view.View.OnLongClickListener;
import android.util.Log;

public class DragTest extends Activity {
	private static final String TAG ="MainActivity";
	private boolean DEBUG = true;
	private CellItem mWifiButton;
	private CellItem mGpsButton;
	private Layer mParent;
	
	private int[] position = new int[]{0,0};
	
	private final int WIFI =1;
	private final int GPS =2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mParent = (Layer)this.findViewById(R.id.parent);
    	initButtons();
    	if(null != mParent){
    		mParent.addView(mWifiButton);
    		mParent.addView(mGpsButton);
    	}
    	//mParent.setOnLongClickListener(mOnLongClickListener);
    }

    @Override
    protected void onResume(){
    	super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    //init add the buttons,whether used depend on the used-list
    private void initButtons(){
    	initWifiButton();
    	initGpsButton();
    }
    
    //wifi
    private void initWifiButton(){
    	if(DEBUG){
    		Log.d(TAG,"initWifiButton");
    	}
    	mWifiButton = (CellItem)View.inflate(this,R.layout.shortcut_botton_item , null);
    	mWifiButton.setText(R.string.wifi);
    	mWifiButton.setLayer(mParent);
    	mWifiButton.setId(WIFI);
    	//mWifiButton.setBackgroundResource(R.drawable.ic_launcher);
    	mWifiButton.setOnLongClickListener(mOnLongClickListener);
    	mWifiButton.setTag(WIFI);
    }
    //gps
    private void initGpsButton(){
    	if(DEBUG){
    		Log.d(TAG,"initGpsButton");
    	}
    	mGpsButton = (CellItem)View.inflate(this,R.layout.shortcut_botton_item , null);
    	mGpsButton.setText(R.string.gps);
    	mGpsButton.setLayer(mParent);
    	mGpsButton.setId(GPS);
    	mGpsButton.setOnLongClickListener(mOnLongClickListener);
    	mGpsButton.setTag(GPS);
    }
    
    private OnLongClickListener mOnLongClickListener = new OnLongClickListener(){

		@Override
		public boolean onLongClick(View arg0) {
			Log.d(TAG,"onLongClick");
			if(arg0 instanceof TextView){
				Log.d(TAG,"onLongClick view is button ,button'tag = "+arg0.getTag());
				dragController(arg0);
			}
			return false;
		}
    };
    
    private void dragController(View arg0){
    	Log.d(TAG,"dragController");
    	arg0.setVisibility(View.INVISIBLE);
    	arg0.getLocationOnScreen(position);
    	for(int i=0;i<position.length;i++){
    		Log.d(TAG,"dragController:mPosition["+i+"]"+" = "+position[i]);
    	}
    	Log.d(TAG,"dragController:mGpsButton getDrawingCache = "+(mGpsButton.getDrawingCache()!=null?"not null":"null"));
    	//mParent.isReadyToDrag = true;
    	mParent.readyToDrag(position,(Integer)arg0.getTag(),true);
    }
    
}
