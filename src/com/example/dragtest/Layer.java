package com.example.dragtest;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.util.AttributeSet;
import android.util.Log;

public class Layer extends LinearLayout {
	private boolean DEBUG = true;
	private static final String TAG = "layer";
	
	public boolean isReadyToDrag = false;
	private int mCurrentTag = 1;//default wifi
	private int[] mPosition = new int[]{0,0};
	
	private final int WIFI =1;
	private final int GPS = 2;
	
    private ImageView mDragView;
    private Bitmap mDragBitmap;
    private WindowManager mWindowManager;
    private WindowManager.LayoutParams mWindowParams;
    
	public Layer(Context context, AttributeSet attrs){
		super(context,attrs);
	}
	
	public boolean onTouchEvent(MotionEvent ev){
		if(DEBUG){
			Log.d(TAG,"onTouchEvent");
		}
		int action = ev.getAction();
		switch(action){
			case MotionEvent.ACTION_UP:
			case MotionEvent.ACTION_CANCEL:
				isReadyToDrag = false;
				//refresh the view
				refreshView(ev);
				break;
			case MotionEvent.ACTION_DOWN:
				break;
			case MotionEvent.ACTION_MOVE:
				updateDragViewPosition(ev.getRawX(),ev.getRawY());
				break;
			default:
				Log.d(TAG, "onTouchEvent:default");
		}
		return super.onTouchEvent(ev);
	}

    public boolean onInterceptTouchEvent(MotionEvent ev) {
    	if(DEBUG){
    		Log.d(TAG,"onInterceptTouchEvent");
    	}
    	if(!isReadyToDrag){
    		return super.onInterceptTouchEvent(ev);
    	}else{
    		//the childrens' onTouchEvent will not be used
    		return true;
    	}
    }
    
    public void readyToDrag(int[] position,int tag,boolean isReadyToDrag){
    	if(DEBUG){
    		Log.d(TAG,"readyToDrag");
    		Log.d(TAG,"readyToDrag,tag="+tag);
    	}
    	this.isReadyToDrag = isReadyToDrag;
    	this.mCurrentTag = tag;
    	mPosition = position;
    	Log.d(TAG,"readyToDrag:layer childCount="+getChildCount());
    	TextView mCurrentView = (TextView)findViewById(tag);
    	if(null == mCurrentView){
    		Log.d(TAG,"readyToDrag:mCurrentView = null");
    	}
    	Log.d(TAG,"readyToDrag:mCurrentView.text = "+mCurrentView.getText());
    	//Bitmap mNewBitmap = Bitmap.createBitmap(mCurrentView.getDrawingCache());
    	Bitmap mNewBitmap = getBitmapForView(mCurrentView);
        ImageView v = new ImageView(getContext());
        //int backGroundColor = context.getResources().getColor(R.color.dragndrop_background);
        //v.setBackgroundColor(backGroundColor);
        //v.setBackgroundResource(R.drawable.playlist_tile_drag);
        v.setPadding(0, 0, 0, 0);
        v.setImageBitmap(mNewBitmap);
        mDragView = v;
        mDragBitmap = mNewBitmap;
        
        mWindowParams = new WindowManager.LayoutParams();
        mWindowParams.gravity = Gravity.TOP | Gravity.LEFT;
        mWindowParams.x = position[0];//x - mDragPointX + mXOffset;
        mWindowParams.y = position[1];//y - mDragPointY + mYOffset;

        mWindowParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        mWindowParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        mWindowParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
                | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS;
        mWindowParams.format = PixelFormat.TRANSLUCENT;
        mWindowParams.windowAnimations = 0;
        
        mWindowManager = (WindowManager)getContext().getSystemService(Context.WINDOW_SERVICE);
        mWindowManager.addView(v, mWindowParams);
        
        //updateDragViewPosition();
    }
    
    private Bitmap getBitmapForView(View view){
    	Bitmap bitmap = null;
    	try{
    		int width = view.getWidth();
    		int height = view.getHeight();
    		
			bitmap = Bitmap.createBitmap(width,height,Bitmap.Config.ARGB_8888);
			Canvas canvas = new Canvas(bitmap);
			//view.layout(0, 0, width, height);
			view.draw(canvas);
    		
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    	return bitmap;
    }
    
    private void updateDragViewPosition(float x,float y){
    	Log.d(TAG,"updateDragViewPosition");
    	if(null != mWindowParams && null != mWindowManager){
    		mWindowParams.x = (int)x;
    		mWindowParams.y = (int)y;
    		mWindowManager.updateViewLayout(mDragView, mWindowParams);
    	}
    }
    
    private void refreshView(MotionEvent ev){
    	Log.d(TAG,"refreshView");
    	//int x = (int)ev.getX();
    	//int y = (int)ev.getY();
    	int dragViewIndex;
    	int positionViewIndex;
    	TextView dragView;
    	TextView positionView;
    	int tag = -1;
    	tag = getChilePositionTag(ev);
    	Log.d(TAG,"refreshView mCurrentTag="+mCurrentTag+" tag ="+tag);
    	if(-1 == tag){
    		recycleBitmap();
    		findViewById(mCurrentTag).setVisibility(View.VISIBLE);
    		return;
    	}
    	if(mCurrentTag == tag){
    		recycleBitmap();
    		findViewById(mCurrentTag).setVisibility(View.VISIBLE);
    		return;
    	}
    	Log.d(TAG,"refreshView 2");
    	recycleBitmap();
    	dragView = (TextView)findViewById(mCurrentTag);
    	dragView.setVisibility(View.VISIBLE);
    	dragViewIndex = indexOfChild(dragView);
    	positionView = (TextView)findViewById(tag);
    	positionViewIndex = indexOfChild(positionView);
    	if(dragViewIndex >= positionViewIndex){
	    	removeViewAt(dragViewIndex);
	    	removeViewAt(positionViewIndex);
	    	addView(dragView,positionViewIndex);
	    	addView(positionView,dragViewIndex);
    	}else{
    		removeViewAt(positionViewIndex);
	    	removeViewAt(dragViewIndex);
	    	addView(positionView,dragViewIndex);
	    	addView(dragView,positionViewIndex);
    	}
    	
    	//persistDatabase();
    	invalidate();
    	
    }
    private void recycleBitmap(){
    	Log.d(TAG,"recycleBitmap");
    	if(mDragView != null){
    		mDragView.setVisibility(View.GONE);
    		mWindowManager.removeView(mDragView);
    		mDragView.setImageDrawable(null);
    		mDragView = null;
    	}
        if (mDragBitmap != null) {
            mDragBitmap.recycle();
            mDragBitmap = null;
        }
    }
    private int getChilePositionTag(MotionEvent ev){
    	Log.d(TAG,"getChilePosition");
    	int tag = -1;
    	int count = getChildCount();
    	int x = (int)ev.getRawX();
    	int y = (int)ev.getRawY();
    	int[] startPosition = new int[]{0,0};
    	int[] endPosition = new int[]{0,0};
    	TextView child = null;
    	for(int i=0;i<count;i++){
    		child = (TextView)getChildAt(i);
    		child.getLocationOnScreen(startPosition);
    		endPosition[0]=startPosition[0]+child.getWidth();
    		endPosition[1]=startPosition[1]+child.getHeight();
    		Log.d(TAG,"getChilePosition:x="+x+" y="+y+" startPosition0="+startPosition[0]+" startPosition1="+startPosition[1]
    				+" endPosition0="+endPosition[0]+" endPosition1="+endPosition[1]);
    		if(x>=startPosition[0] && x<=endPosition[0] && y>=startPosition[1] && y<= endPosition[1]){
    			tag = (Integer)child.getTag();
    			break;
    		}
    	}
    	return tag;
    }
}
