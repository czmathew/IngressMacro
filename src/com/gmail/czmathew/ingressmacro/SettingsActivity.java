package com.gmail.czmathew.ingressmacro;

import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v4.app.NavUtils;

public class SettingsActivity extends Activity {
	
	private static final String PREFS_NAME = "MacroPref";

	Context mContext;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		mContext = getApplicationContext();
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		
		// Show the Up button in the action bar.
		//setupActionBar();
		
		loadInputs();
	}

	/**
	 * Set up the {@link android.app.ActionBar}.
	 */
	private void setupActionBar() {

		getActionBar().setDisplayHomeAsUpEnabled(true);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.settings, menu);
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

	public void loadInputs() {
		DocumentBuilderFactory factory = DocumentBuilderFactory
				.newInstance();
		String tmpText = "";
		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
		String event = settings.getString("event", "event2");
		int selectedItem = 0;
		
		final ArrayList<spinnerData> list = new ArrayList<spinnerData>();
		//spinnerData items[] = ;
		Spinner s = (Spinner)findViewById(R.id.spinnerInputsSettings);
        //items[0] = new spinnerData( "key1","value1" );
        //items[1] = new spinnerData( "key2","value2" );
        //items[2] = new spinnerData( "key3","value3" );
		//list.add(new spinnerData("select", ""));
		
		
		// List<Message> messages = new ArrayList<Message>();
		try {
			DocumentBuilder builder = factory.newDocumentBuilder();
			// InputStream in_s =
			// getApplicationContext().getAssets().open("dropItems.xml");
			InputStream in_s = openFileInput("inputEvents.xml");
			Document dom = builder.parse(in_s);
			Element root = dom.getDocumentElement();

			// get init steps
			NodeList inputs = root.getElementsByTagName("input");
			// get first occurrence
			for (int i = 0; i < inputs.getLength(); i++) {
				// Message message = new Message();
				
				Node inputNod = inputs.item(i);
				NodeList params = inputNod.getChildNodes();
				
				spinnerData spinn = new spinnerData();
				
				for (int j = 0; j < params.getLength();j++) {
					Node tempNode = params.item(j);
					if (tempNode.getNodeType() == Node.ELEMENT_NODE) {
						if (tempNode.getNodeName().equals("path")) {
							spinn.setValue(tempNode.getTextContent());
							tmpText += "\n"+tempNode.getNodeName();
							tmpText += ":"+tempNode.getTextContent();
							if (tempNode.getTextContent().equals(event)) {
								selectedItem = j;
							}
						} else if (tempNode.getNodeName().equals("name")) {
							tmpText += "\n"+tempNode.getNodeName();
							tmpText += ":"+tempNode.getTextContent();
							spinn.setSpinnerText(tempNode.getTextContent());
						}
					}
					
				}
				list.add(spinn);
			}
			//get index of default value
			for (int i = 0; i<list.size();i++) {
				if (list.get(i).getValue().equals(event)) {
					selectedItem = i;
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
	                        		//loadValuesFromDeviceSettings(list.get(position).getValue());
	                        		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
	                        		SharedPreferences.Editor editor = settings.edit();
	                        		editor.putString("event", list.get(position).getValue());
	                        		editor.commit();
	                        		Toast.makeText(mContext, "spinner selected "+list.get(position).getValue(), Toast.LENGTH_SHORT).show();
	                        	}
	                        	//Toast.makeText(mContext, "spinner selected "+list.get(position).getValue(), Toast.LENGTH_SHORT).show();
	                        }

	                        public void onNothingSelected(AdapterView<?> parent) {
	                        }
	                    }
	                );
	            s.setSelection(selectedItem);
	            //SelectSpinnerItemByValue(s, event);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	
}
