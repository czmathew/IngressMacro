package com.gmail.czmathew.ingressmacro;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;

public abstract class createXML {
	
	static void createDrop(Context con, Map<String, String> map){
		
		String dropXML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
        		"<steps>" +
        		"    <stepInit>" +
        		"        <run>Ingress</run>" +
        		"        <sleep>"+map.get("txtInitSleep")+"</sleep>" +
        		"    </stepInit>" +
        		"	<stepRepeate>" +
        		"	    <click>" +
        		"		    <name>OPS</name>" +
        		"			<posx>"+map.get("txtOPSposx")+"</posx>" +
        		"			<posy>"+map.get("txtOPSposy")+"</posy>" +
        		"		</click>" +
        		"		<sleep>"+map.get("txtOPSsleep")+"</sleep>" +
        		"		<click>" +
        		"			<name>Select Item</name>" +
        		"			<posx>"+map.get("txtSelectItemPosx")+"</posx>" +
        		"			<posy>"+map.get("txtSelectItemPosy")+"</posy>" +
        		"		</click>" +
        		"		<sleep>"+map.get("txtSelectItemSleep")+"</sleep>" +
        		"		<click>" +
        		"			<name>Drop</name>" +
        		"			<posx>"+map.get("txtDropPosx")+"</posx>" +
        		"			<posy>"+map.get("txtDropPosy")+"</posy>" +
        		"		</click>" +
        		"		<sleep>"+map.get("txtDropSleep")+"</sleep>" +
        		"	</stepRepeate>" +
        		"</steps>";
        
        //check if XMLs exists...
        FileOutputStream fos;
		try {
			fos = con.openFileOutput("dropItems.xml", Context.MODE_PRIVATE);
			fos.write(dropXML.getBytes());
			fos.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	static void createInputXML(Context con, ArrayList<String[]> inputs) {
		
		String inputXML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
        		"<inputs>\n";
		for (int i = 0; i < inputs.size();i++) {
			inputXML += "    <input>\n" +
        		"        <path>"+inputs.get(i)[0]+"</path>\n" +
        		"        <name>"+inputs.get(i)[1]+"</name>\n" +
        		"    </input>\n";
        		
		}
		inputXML += "</inputs>";
        
        //check if XMLs exists...
        FileOutputStream fos;
		try {
			fos = con.openFileOutput("inputEvents.xml", Context.MODE_PRIVATE);
			fos.write(inputXML.getBytes());
			fos.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	static void createDrop(Context con){
		
		//Setup default values (for galaxy S3 screen)
		Map<String, String> map = new HashMap<String, String>();
		map.put("txtInitSleep", "5000");
		map.put("txtOPSposx", "628");
		map.put("txtOPSposy", "93");
		map.put("txtOPSsleep", "500");
		
		map.put("txtSelectItemPosx", "384");
		map.put("txtSelectItemPosy", "610");
		map.put("txtSelectItemSleep", "300");
		
		map.put("txtDropPosx", "350");
		map.put("txtDropPosy", "1196");
		map.put("txtDropSleep", "500");
		
		createDrop(con, map);
	}
	
	static void createRecycle(Context con, Map<String,String> map){
		String x = map.get("txtRRecyclePosy");
		String recycleXML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
        		"<steps>" +
        		"    <stepInit>" +
        		"        <run>Ingress</run>" +
        		"        <sleep>"+map.get("txtRInitSleep")+"</sleep>" +
        		"	    <click>" +
        		"		    <name>OPS</name>" +
        		"			<posx>"+map.get("txtROPSposx")+"</posx>" +
        		"			<posy>"+map.get("txtROPSposy")+"</posy>" +
        		"		</click>" +
        		"		<sleep>"+map.get("txtROPSsleep")+"</sleep>" +
        		"    </stepInit>" +
        		"	<stepRepeate>" +
        		"		<click>" +
        		"			<name>Select Item</name>" +
        		"			<posx>"+map.get("txtRSelectItemPosx")+"</posx>" +
        		"			<posy>"+map.get("txtRSelectItemPosy")+"</posy>" +
        		"		</click>" +
        		"		<sleep>"+map.get("txtRSelectItemSleep")+"</sleep>" +
        		"		<click>" +
        		"			<name>Drop</name>" +
        		"			<posx>"+map.get("txtRRecyclePosx")+"</posx>" +
        		"			<posy>"+map.get("txtRRecyclePosy")+"</posy>" +
        		"		</click>" +
        		"		<sleep>"+map.get("txtRRecycleSleep")+"</sleep>" +
        		"	</stepRepeate>" +
        		"</steps>";
		//check if XMLs exists...
        FileOutputStream fos;
		try {
			fos = con.openFileOutput("recycleItems.xml", Context.MODE_PRIVATE);
			fos.write(recycleXML.getBytes());
			fos.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	static void createRecycle(Context con){
		
		//Setup default values (for galaxy S3 screen)
		Map<String, String> map = new HashMap<String, String>();
		map.put("txtRInitSleep", "5000");
		map.put("txtROPSposx", "628");
		map.put("txtROPSposy", "93");
		map.put("txtROPSsleep", "500");
		
		map.put("txtRSelectItemPosx", "384");
		map.put("txtRSelectItemPosy", "610");
		map.put("txtRSelectItemSleep", "300");
		
		map.put("txtRRecyclePosx", "550");
		map.put("txtRRecyclePosy", "1196");
		map.put("txtRRecycleSleep", "1500");
		
		createRecycle(con, map);
	}

}
