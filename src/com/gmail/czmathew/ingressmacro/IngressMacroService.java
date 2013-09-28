package com.gmail.czmathew.ingressmacro;

import java.util.Set;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

public class IngressMacroService extends Service {
	Long threadId;
	Intent service;
	SettingsContentObserver mSettingsContentObserver;
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	  public int onStartCommand(Intent intent, int flags, int startId) {
	    //TODO do something useful
		
		//Toast.makeText(this, "service starting", Toast.LENGTH_SHORT).show();
		Log.w("SERVICE ON START","SERVICE ON START");
		service = intent;
		
		threadId = intent.getLongExtra("threadId",0);
		mSettingsContentObserver.setIntent(intent);
		mSettingsContentObserver.setService(this);
		
		Set<Thread> keyset = Thread.getAllStackTraces().keySet();
        for (Thread t : keyset) {
        	if (t.getId()==threadId) {
        		mSettingsContentObserver.setScriptThread(t);
        		break;
        	}
        }
		
	    return super.onStartCommand(intent,flags,startId);
	  }
	
	@Override
	public void onCreate() {
		super.onCreate();
		Log.w("SERVICE ON CREATE","SERVICE ON CREATE");
		mSettingsContentObserver = new SettingsContentObserver(this,new Handler());
		getApplicationContext().getContentResolver().registerContentObserver(android.provider.Settings.System.CONTENT_URI, true, mSettingsContentObserver );
		getApplicationContext().getContentResolver().notifyChange(android.provider.Settings.System.CONTENT_URI, mSettingsContentObserver);
		
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.w("SERVICE ON DESTROY","SERVICE ON DESTROY");
		getApplicationContext().getContentResolver().unregisterContentObserver(mSettingsContentObserver);
	}

}
