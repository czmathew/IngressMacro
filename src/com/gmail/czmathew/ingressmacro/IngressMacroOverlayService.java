package com.gmail.czmathew.ingressmacro;

import java.util.Set;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.os.IBinder;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;


public class IngressMacroOverlayService extends Service {
    HUDViewImage mView;
    HUDViewOverlay overlay;
    Intent service;
    final Service ser = this;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    
    @Override
	  public int onStartCommand(Intent intent, int flags, int startId) {
	    //TODO do something useful
		
		//Toast.makeText(this, "service starting", Toast.LENGTH_SHORT).show();
		//Log.w("SERVICE ON START","SERVICE ON START");
		service = intent;
		
		return super.onStartCommand(intent,flags,startId);
	  }
    
    @Override
    public void onCreate() {
    	super.onCreate();
        Toast.makeText(getBaseContext(),"onCreate", Toast.LENGTH_LONG).show();
        Log.d("OVERLAY SERVICE"," oncreate method ");
        
        final Bitmap kangoo = BitmapFactory.decodeResource(getResources(),
                R.drawable.ic_launcher);
        
        mView = new HUDViewImage(this,kangoo);
        mView.setOnTouchListener(new OnTouchListener() {

        	@Override
            public boolean onTouch(View arg0, MotionEvent event) {
        		
        		if (event.getAction() == MotionEvent.ACTION_DOWN) {
           		 Toast.makeText(getBaseContext(),"Touch BUTTON "+event.getX()+" x "+event.getY(), Toast.LENGTH_SHORT).show();
           		 
           		WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
           		wm.removeView(arg0);
           		Log.d("OVERLAY SERVICE"," wm.removeView(arg0); ");
           		
           		 
           		 overlay = new HUDViewOverlay(getBaseContext());
           		 overlay.setOnTouchListener(new OnTouchListener() {

                	@Override
                    public boolean onTouch(View arg0, MotionEvent event) {
                		
                		if (event.getAction() == MotionEvent.ACTION_DOWN) {
	                   		Toast.makeText(getBaseContext(),"Touch DOWN OVERLAY "+event.getX()+" x "+event.getY(), Toast.LENGTH_SHORT).show();
	                   		 
	                   		 
	                   		WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
	                   		wm.removeView(arg0);
	                   		Log.d("OVERLAY SERVICE"," wm.removeView(arg0) - whole screen overlay ");
	                   		
	                   		try {
	                   			getBaseContext().stopService(service);
	                   		} catch (Exception e) {
	                   	        // nothing
	                   	    }
                		}
                       
                        return false;
                    }
                });
           		 
           		Display display = wm.getDefaultDisplay();
           		Point size = new Point();
           		display.getSize(size);
           		
           		Log.d("screen size","SCREEN SIZE: "+size.x+" x "+size.y);
           		 
           		WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                		size.x, 
                		size.y,
                        WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
                        WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH
                        |WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
        |             WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                        PixelFormat.TRANSLUCENT);
                params.gravity = Gravity.LEFT | Gravity.TOP;
                //params.setTitle("Load Average");
                
                wm.addView(overlay, params);
                Log.d("OVERLAY SERVICE"," wm.addView(mView, params) - overlay");
           		 
           		 
        		}
               
                return false;
            }
        });
        
        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
        		kangoo.getWidth(), 
                kangoo.getHeight(),
                WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
                WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH
                |WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
|             WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
 //                     | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                PixelFormat.TRANSLUCENT);
        params.gravity = Gravity.LEFT | Gravity.TOP;
        params.x = 150;
        //params.setTitle("Load Average");
        WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        wm.addView(mView, params);
        Log.d("OVERLAY SERVICE"," wm.addView(mView, params) - button");
        
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //Toast.makeText(getBaseContext(),"onDestroy", Toast.LENGTH_LONG).show();
        if(mView != null)
        {
        	try {
        		((WindowManager) getSystemService(WINDOW_SERVICE)).removeView(mView);
        	} catch (Exception e) {
                // nothing
            }
            mView = null;
        }
    }
}

class HUDViewImage extends ViewGroup {
    private Paint mLoadPaint;
    Bitmap kangoo;

    public HUDViewImage(Context context,Bitmap kangoo) {
    	super(context);
    	this.kangoo=kangoo;
        //Toast.makeText(getContext(),"HUDView", Toast.LENGTH_SHORT).show();

        mLoadPaint = new Paint();
        mLoadPaint.setAntiAlias(true);
        mLoadPaint.setTextSize(30);
        mLoadPaint.setARGB(255, 255, 0, 0);
        
    }
    
    
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //canvas.drawText("Hello World", 5, 45, mLoadPaint);
        canvas.drawColor(Color.BLACK);

        canvas.drawBitmap(kangoo,0 , 0, null); 
        
    }

    @Override
    protected void onLayout(boolean arg0, int arg1, int arg2, int arg3, int arg4) {
    }
}
class HUDViewOverlay extends ViewGroup {
    private Paint mLoadPaint;
    Bitmap kangoo;

    public HUDViewOverlay(Context context) {
    	super(context);
    	//Toast.makeText(getContext(),"HUDView", Toast.LENGTH_SHORT).show();

        mLoadPaint = new Paint();
        mLoadPaint.setAntiAlias(true);
        mLoadPaint.setTextSize(30);
        mLoadPaint.setARGB(255, 255, 0, 0);
        
    }
    
    
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawText("Select click position", 5, 45, mLoadPaint);
                
    }

    @Override
    protected void onLayout(boolean arg0, int arg1, int arg2, int arg3, int arg4) {
    }
}