package com.gmail.czmathew.ingressmacro;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import android.os.Bundle;
import android.R.integer;
import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.support.v4.app.NavUtils;

public class DropSettingsActivity extends Activity {

	private Document document;
	Context mContext;
	private String deviceFolder = "devices";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_drop_settings);
		mContext = getApplicationContext();
		// Show the Up button in the action bar.
		setupActionBar();
		loadValues();
		
		//load values for spinner
		Resources res = getResources(); //if you are in an activity
        AssetManager am = res.getAssets();
        String fileList[] = null;
		try {
			fileList = am.list(deviceFolder);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		final ArrayList<spinnerData> list = new ArrayList<spinnerData>();
		//spinnerData items[] = ;
		Spinner s = (Spinner)findViewById(R.id.spinnerRecycle);
        //items[0] = new spinnerData( "key1","value1" );
        //items[1] = new spinnerData( "key2","value2" );
        //items[2] = new spinnerData( "key3","value3" );
		list.add(new spinnerData("select", ""));

        if (fileList != null)
        {   
            for ( int i = 0;i<fileList.length;i++)
            {
            	Log.d("",fileList[i]); 
            	list.add(getSpinnerData(deviceFolder+"/"+fileList[i]));
            }
        }
        
        ArrayAdapter<spinnerData> adapter = 
                new ArrayAdapter<spinnerData>( 
                    this,
                    android.R.layout.simple_spinner_item,
                    list );
            adapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);
            s.setAdapter(adapter);
            s.setOnItemSelectedListener(
                    new AdapterView.OnItemSelectedListener() {
                        public void onItemSelected(AdapterView<?> parent, View view,int position,long id) {
                        	//list.get(position);
                            //spinnerData d = list[position];
                            //valueTextView.setText( d.getValue() );
                        	if (!list.get(position).getValue().equals("")) {
                        		loadValuesFromDeviceSettings(list.get(position).getValue());
                        	}
                        	//Toast.makeText(mContext, "spinner selected "+list.get(position).getValue(), Toast.LENGTH_SHORT).show();
                        }

                        public void onNothingSelected(AdapterView<?> parent) {
                        }
                    }
                );
	}
	
	private spinnerData getSpinnerData(String fileName) {
		spinnerData dt = null;
		try {
			InputStream in_s = getApplicationContext().getAssets().open(fileName);
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			document = db.parse(in_s);

			in_s.close();
			
			XPathFactory xpf = XPathFactory.newInstance();
			XPath xp = xpf.newXPath();

			// init sleep
			dt = new spinnerData(xp.evaluate("//device/name/text()",document.getDocumentElement()), fileName);
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return dt;
	}

	/**
	 * Set up the {@link android.app.ActionBar}.
	 */
	private void setupActionBar() {

		getActionBar().setDisplayHomeAsUpEnabled(true);

	}

	public void saveXML(View view) {
		EditText txtTmp;
		txtTmp = (EditText) findViewById(R.id.txtOPSposx);
		
		
		Map<String, String> map = new HashMap<String, String>();
		txtTmp = (EditText) findViewById(R.id.txtInitSleep);
		map.put("txtInitSleep", txtTmp.getText().toString());
		txtTmp = (EditText) findViewById(R.id.txtOPSposx);
		map.put("txtOPSposx", txtTmp.getText().toString());
		txtTmp = (EditText) findViewById(R.id.txtOPSposy);
		map.put("txtOPSposy", txtTmp.getText().toString());
		txtTmp = (EditText) findViewById(R.id.txtOPSsleep);
		map.put("txtOPSsleep", txtTmp.getText().toString());
		
		txtTmp = (EditText) findViewById(R.id.txtSelectItemPosx);
		map.put("txtSelectItemPosx", txtTmp.getText().toString());
		txtTmp = (EditText) findViewById(R.id.txtSelectItemPosy);
		map.put("txtSelectItemPosy", txtTmp.getText().toString());
		txtTmp = (EditText) findViewById(R.id.txtSelectItemSleep);
		map.put("txtSelectItemSleep", txtTmp.getText().toString());
		
		txtTmp = (EditText) findViewById(R.id.txtDropPosx);
		map.put("txtDropPosx", txtTmp.getText().toString());
		txtTmp = (EditText) findViewById(R.id.txtDropPosy);
		map.put("txtDropPosy", txtTmp.getText().toString());
		txtTmp = (EditText) findViewById(R.id.txtDropSleep);
		map.put("txtDropSleep", txtTmp.getText().toString());
		
		Toast.makeText(this, "Values for drop saved!", Toast.LENGTH_SHORT).show();
		
		createXML.createDrop(mContext, map);
		

	}

	private void loadValuesFromDeviceSettings(String path) {
		EditText txtTmp;
		String tmp;
		try {
			InputStream in_s = getApplicationContext().getAssets().open(path);
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			document = db.parse(in_s);

			in_s.close();
			
			XPathFactory xpf = XPathFactory.newInstance();
			XPath xp = xpf.newXPath();
			
			// OPS x
						txtTmp = (EditText) findViewById(R.id.txtOPSposx);
						tmp = xp.evaluate(
								"//device/clickops/x/text()",
								document.getDocumentElement());
						txtTmp.setText(tmp);

						// OPS y
						txtTmp = (EditText) findViewById(R.id.txtOPSposy);
						tmp = xp.evaluate(
								"/device/clickops/y/text()",
								document.getDocumentElement());
						txtTmp.setText(tmp);

						// select x
						txtTmp = (EditText) findViewById(R.id.txtSelectItemPosx);
						tmp = xp.evaluate(
								"//device/clickselect/x/text()",
								document.getDocumentElement());
						txtTmp.setText(tmp);

						// select y
						txtTmp = (EditText) findViewById(R.id.txtSelectItemPosy);
						tmp = xp.evaluate(
								"//device/clickselect/y/text()",
								document.getDocumentElement());
						txtTmp.setText(tmp);

						// drop x
						txtTmp = (EditText) findViewById(R.id.txtDropPosx);
						tmp = xp.evaluate(
								"//device/clickdrop/x/text()",
								document.getDocumentElement());
						txtTmp.setText(tmp);

						// drop y
						txtTmp = (EditText) findViewById(R.id.txtDropPosy);
						tmp = xp.evaluate(
								"//device/clickdrop/y/text()",
								document.getDocumentElement());
						txtTmp.setText(tmp);			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void loadValues() {
		EditText txtTmp;
		String tmp;
		InputStream in_s;
		try {

			//Toast.makeText(this, "Load values from XML DROP", Toast.LENGTH_SHORT).show();

			in_s = openFileInput("dropItems.xml");

			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			document = db.parse(in_s);

			in_s.close();

			XPathFactory xpf = XPathFactory.newInstance();
			XPath xp = xpf.newXPath();

			// init sleep
			txtTmp = (EditText) findViewById(R.id.txtInitSleep);
			tmp = xp.evaluate("//steps/stepInit/sleep/text()",
					document.getDocumentElement());
			txtTmp.setText(tmp);

			// OPS x
			txtTmp = (EditText) findViewById(R.id.txtOPSposx);
			tmp = xp.evaluate(
					"//steps/stepRepeate/click[position()=1]/posx/text()",
					document.getDocumentElement());
			txtTmp.setText(tmp);

			// OPS y
			txtTmp = (EditText) findViewById(R.id.txtOPSposy);
			tmp = xp.evaluate(
					"//steps/stepRepeate/click[position()=1]/posy/text()",
					document.getDocumentElement());
			txtTmp.setText(tmp);

			// OPS sleep
			txtTmp = (EditText) findViewById(R.id.txtOPSsleep);
			tmp = xp.evaluate("//steps/stepRepeate/sleep[position()=1]/text()",
					document.getDocumentElement());
			txtTmp.setText(tmp);

			// select x
			txtTmp = (EditText) findViewById(R.id.txtSelectItemPosx);
			tmp = xp.evaluate(
					"//steps/stepRepeate/click[position()=2]/posx/text()",
					document.getDocumentElement());
			txtTmp.setText(tmp);

			// select y
			txtTmp = (EditText) findViewById(R.id.txtSelectItemPosy);
			tmp = xp.evaluate(
					"//steps/stepRepeate/click[position()=2]/posy/text()",
					document.getDocumentElement());
			txtTmp.setText(tmp);

			// select sleep
			txtTmp = (EditText) findViewById(R.id.txtSelectItemSleep);
			tmp = xp.evaluate("//steps/stepRepeate/sleep[position()=2]/text()",
					document.getDocumentElement());
			txtTmp.setText(tmp);

			// drop x
			txtTmp = (EditText) findViewById(R.id.txtDropPosx);
			tmp = xp.evaluate(
					"//steps/stepRepeate/click[position()=3]/posx/text()",
					document.getDocumentElement());
			txtTmp.setText(tmp);

			// drop y
			txtTmp = (EditText) findViewById(R.id.txtDropPosy);
			tmp = xp.evaluate(
					"//steps/stepRepeate/click[position()=3]/posy/text()",
					document.getDocumentElement());
			txtTmp.setText(tmp);

			// drop sleep
			txtTmp = (EditText) findViewById(R.id.txtDropSleep);
			tmp = xp.evaluate("//steps/stepRepeate/sleep[position()=3]/text()",
					document.getDocumentElement());
			txtTmp.setText(tmp);

		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.drop_settings, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
