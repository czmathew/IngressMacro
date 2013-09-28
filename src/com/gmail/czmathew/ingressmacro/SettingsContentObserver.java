package com.gmail.czmathew.ingressmacro;


import java.util.Set;

import android.util.Log;
import android.widget.Toast;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.media.AudioManager;
import android.os.Handler;

public class SettingsContentObserver extends ContentObserver {
    int previousVolumeRing;
    int previousVolumeMusic;
    Thread scriptThread;
    Context context;
    Intent serv;
    Service service;

    public SettingsContentObserver(Context c, Handler handler) {
        super(handler);
        Log.w("OBSERVER", "OBSERVER");
        context=c;
        
        /*Set<Thread> keyset = Thread.getAllStackTraces().keySet();
        for (Thread t : keyset) {
        	if (t.getId()==threadId) {
        		scriptThread = t;
        		break;
        	}
        }*/
        
        AudioManager audio = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        previousVolumeRing = audio.getStreamVolume(AudioManager.STREAM_RING);
        previousVolumeMusic = audio.getStreamVolume(AudioManager.STREAM_MUSIC);
        Log.w("OBSERVER", "OBSERVER");
    }

    @Override
    public boolean deliverSelfNotifications() {
        return super.deliverSelfNotifications();
    }

    @Override
    public void onChange(boolean selfChange) {
        super.onChange(selfChange);
        Log.w("OBSERVER ONCHANGE", "OBSERVER ONCHANGE");
        AudioManager audio = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        int currentVolumeRing = audio.getStreamVolume(AudioManager.STREAM_RING);
        int currentVolumeMusic = audio.getStreamVolume(AudioManager.STREAM_MUSIC);

        int deltaRing=previousVolumeRing-currentVolumeRing;
        int deltaMusic=previousVolumeMusic-currentVolumeMusic;
        
        if(deltaRing!=0 || deltaMusic!=0)
        {
            //Log.w("VOLUME CHANGED", "VOLUME CHANGED");
            previousVolumeRing=currentVolumeRing;
            previousVolumeMusic=currentVolumeMusic;
            Toast.makeText(context,"Script interrunpted", Toast.LENGTH_SHORT).show();
            if (scriptThread != null && scriptThread.isInterrupted() == false) {
            	scriptThread.interrupt();
            	scriptThread = null;
            	context.stopService(serv);
            	Log.w("OBSERVER ONCHANGE", "OBSERVER ONCHANGE INTERRUPTED");
            }
            /*if (serv != null) {
            	Log.w("OBSERVER ONCHANGE", "OBSERVER ONCHANGE SERVICE STOP");
            	context.stopService(serv);
            }*/
        }
        
    }
    public void setIntent(Intent inten) {
    	serv = inten;
    }
    public void setScriptThread(Thread st) {
    	scriptThread = st;
    }
    public void setService(Service se) {
    	service = se;
    }
}
