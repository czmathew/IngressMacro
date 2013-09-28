package com.gmail.czmathew.ingressmacro;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import android.os.Bundle;
import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.NotificationCompat;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import java.lang.Thread;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class MainActivity extends Activity {

	private static final String PREFS_NAME = "MacroPref";
	PendingIntent pendingIntent;
	Context mContext;
	Thread scriptThread;
	Boolean canRun = true;
	
	Intent overlayIntent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		mContext = getApplicationContext();

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		File fileDrop = mContext.getFileStreamPath("dropItems.xml");
		if(!fileDrop.exists()){
			createXML.createDrop(mContext);
			Toast.makeText(this,"File dropItems.xml created.",Toast.LENGTH_SHORT).show();
		}
		
		File fileRecycle = mContext.getFileStreamPath("recycleItems.xml");
		if(!fileRecycle.exists()){
			createXML.createRecycle(mContext);
			Toast.makeText(this,"File recycleItems.xml created.",Toast.LENGTH_SHORT).show();
		}
		TextView lblBottom = (TextView) findViewById(R.id.lblBottom);
		//following line enables links in TextView
		lblBottom.setMovementMethod(LinkMovementMethod.getInstance());
		lblBottom.setText(Html.fromHtml("Your device has to be rooted in order to make click work.<br>" +
				"<a href=\"https://play.google.com/store/apps/details?id=com.joeykrim.rootcheck\">Root Checker</a>"));

		File fileInputs = mContext.getFileStreamPath("inputEvents.xml");
		if(!fileInputs.exists()){
			createListOfInputs();
			//Toast.makeText(this,"File recycleItems.xml created.",Toast.LENGTH_SHORT).show();
		}
		
		/*
		 * //Intent intent = new Intent(MainActivity.this, MainActivity.class);
		 * Intent notifyIntent = new Intent(MainActivity.this,
		 * MainActivity.class); //notifyIntent.setClass(getApplicationContext(),
		 * MainActivity.class); //MainActivity.this.startActivity(intent);
		 * //intent.setClass(mContext,MainActivity.class); pendingIntent =
		 * PendingIntent.getActivity(mContext, 0, notifyIntent, 0);
		 * 
		 * NotificationCompat.Builder mBuilder = new
		 * NotificationCompat.Builder(this)
		 * .setSmallIcon(R.drawable.ic_launcher)
		 * .setContentTitle("IM notification")
		 * .setContentText("Click to run scripts") .setOngoing(true)
		 * .addAction(R.drawable.ic_launcher, title, pendingIntent);
		 * 
		 * 
		 * NotificationManager notificationManager = (NotificationManager)
		 * getSystemService(Context.NOTIFICATION_SERVICE);
		 * notificationManager.notify(mId, mBuilder.build());
		 */

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		/*case R.id.dropSettings:
			Intent i = new Intent(getApplicationContext(),
					DropSettingsActivity.class);
			startActivity(i);

			// Toast.makeText(this, "Menu drop", Toast.LENGTH_SHORT).show();
			break;
		case R.id.recycleSettings:
			Intent in = new Intent(getApplicationContext(),
					RecycleSettingsActivity.class);
			startActivity(in);
			// Toast.makeText(this, "Menu recycle", Toast.LENGTH_SHORT).show();
			break;
			*/
		case R.id.action_settings:
			Intent in = new Intent(getApplicationContext(),
					SettingsActivity.class);
			startActivity(in);
			
			break;

		default:
			break;
		}

		return true;
	}

	/*
	 * @Override public boolean onKeyDown(int keyCode, KeyEvent event) {
	 * switch(keyCode){ case KeyEvent.KEYCODE_VOLUME_DOWN: canRun = true;
	 * Toast.makeText(this,"Volumen Down pressed", Toast.LENGTH_SHORT).show();
	 * return true; } return super.onKeyDown(keyCode, event); }
	 */

	public void loadOverlay(View view) {
		overlayIntent = new Intent(mContext, IngressMacroOverlayService.class);
		startService(overlayIntent);
	}

	public void hideOverlay(View view) {
		stopService(overlayIntent);
	}

	public void recItems(View view) {
		EditText editText = (EditText) findViewById(R.id.dropNumber);
		// String cmd =
		// "sh /data/local/tmp/recitem.sh "+editText.getText().toString();
		// execCommandLine("su");
		// execCommandLine(cmd);

		final Intent service = new Intent(mContext, IngressMacroService.class);
		// mContext.startService(service);

		scriptThread = new Thread(new Runnable() {
			public void run() {
				EditText editText = (EditText) findViewById(R.id.dropNumber);
				int i = Integer.parseInt(editText.getText().toString());

				try {
					canRun = true;
					startIngress();
					Thread.sleep(5000);
					clickOPS();
					Thread.sleep(500);
					while (canRun && (i > 0)) {
						clickMiddleItem();
						Thread.sleep(500);
						clickRecycle();
						Thread.sleep(1500);
						i--;
					}
					// mContext.stopService(service);

				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} finally {
					mContext.stopService(service);
				}

			}
		});

		// start service after thread is initialized

		service.putExtra("threadId", scriptThread.getId());

		service.putExtras(service);

		if (startService(service) != null) {
			Toast.makeText(this, "Service is already running",
					Toast.LENGTH_SHORT).show();
		} else {
			Log.w("STARTIGN", "STARTING");
			Toast.makeText(this,
					"There is no service running, starting service..",
					Toast.LENGTH_SHORT).show();
		}

		scriptThread.start();

	}
	
	public void recycleItemsXML(View view) {
		final Intent service = new Intent(mContext, IngressMacroService.class);

		// names of the XML tags
		final String STEPS = "steps";
		final String STEP_INIT = "stepInit";
		final String STEP_REPEAT = "stepRepeate";
		final String CLICK = "click";
		final String RUN = "run";
		final String SLEEP = "sleep";
		final String NAME = "name";
		final String POSX = "posx";
		final String POSY = "posy";

		scriptThread = new Thread(new Runnable() {
			public void run() {

				DocumentBuilderFactory factory = DocumentBuilderFactory
						.newInstance();
				// List<Message> messages = new ArrayList<Message>();
				try {
					DocumentBuilder builder = factory.newDocumentBuilder();
					// InputStream in_s =
					// getApplicationContext().getAssets().open("dropItems.xml");
					InputStream in_s = openFileInput("recycleItems.xml");
					Document dom = builder.parse(in_s);
					Element root = dom.getDocumentElement();

					// get init steps
					NodeList tags = root.getElementsByTagName(STEP_INIT);
					// get first occurrence
					Node stepinit = tags.item(0);
					NodeList steps = stepinit.getChildNodes();
					for (int i = 0; i < steps.getLength(); i++) {
						// Message message = new Message();
						Node action = steps.item(i);
						String nodeName = action.getNodeName();
						if (nodeName.equalsIgnoreCase(RUN)) {
							String runValue = action.getFirstChild()
									.getNodeValue();
							if (runValue.equals("Ingress")) {
								startIngress();
							}
						} else if (nodeName.equalsIgnoreCase(SLEEP)) {
							int sleepValue = Integer.parseInt(action
									.getFirstChild().getNodeValue());

							Thread.sleep(sleepValue);
						} else if (nodeName.equalsIgnoreCase(CLICK)) {
							String posx = "";
							String posy = "";
							NodeList params = action.getChildNodes();
							for (int j = 0; j < params.getLength(); j++) {
								Node param = params.item(j);
								String paramName = param.getNodeName();
								if (paramName.equalsIgnoreCase(POSX)) {
									posx = param.getFirstChild().getNodeValue();
								} else if (paramName.equalsIgnoreCase(POSY)) {
									posy = param.getFirstChild().getNodeValue();
								}
							}
							runClickAction(posx, posy);
						} else {
							Log.w("UNKNOWN XML ELEMENT",
									"UNKNOWN XML ELEMENT: " + nodeName);

						}
					}

					// REPEAT STEPS
					NodeList tagsR = root.getElementsByTagName(STEP_REPEAT);
					// get first occurrence
					Node stepRepeat = tagsR.item(0);
					NodeList stepsR = stepRepeat.getChildNodes();

					EditText editText = (EditText) findViewById(R.id.dropNumber);
					int runcount = Integer.parseInt(editText.getText()
							.toString());
					canRun = true;

					while (canRun && (runcount > 0)) {

						for (int i = 0; i < stepsR.getLength(); i++) {
							// Message message = new Message();
							Node action = stepsR.item(i);
							String nodeName = action.getNodeName();
							if (nodeName.equalsIgnoreCase(SLEEP)) {
								int sleepValue = Integer.parseInt(action
										.getFirstChild().getNodeValue());

								Thread.sleep(sleepValue);
							} else if (nodeName.equalsIgnoreCase(CLICK)) {
								String posx = "";
								String posy = "";
								NodeList params = action.getChildNodes();
								for (int j = 0; j < params.getLength(); j++) {
									Node param = params.item(j);
									String paramName = param.getNodeName();
									if (paramName.equalsIgnoreCase(POSX)) {
										posx = param.getFirstChild()
												.getNodeValue();
									} else if (paramName.equalsIgnoreCase(POSY)) {
										posy = param.getFirstChild()
												.getNodeValue();
									}
								}
								runClickAction(posx, posy);
							} else {
								Log.w("UNKNOWN XML ELEMENT",
										"UNKNOWN XML ELEMENT: " + nodeName);

							}
						}
						runcount--;
					}

				} catch (Exception e) {
					throw new RuntimeException(e);
				} finally {
					stopService(service);
				}
			}
		});

		service.putExtra("threadId", scriptThread.getId());

		service.putExtras(service);

		if (startService(service) != null) {
			Toast.makeText(this, "Service is already running",
					Toast.LENGTH_SHORT).show();
		} else {
			Log.w("STARTIGN", "STARTING");
			Toast.makeText(this,
					"There is no service running, starting service..",
					Toast.LENGTH_SHORT).show();
		}

		scriptThread.start();
		// return messages;
	
	}

	public void btnDropSettingsClick (View view) {
		Intent in = new Intent(getApplicationContext(),
				DropSettingsActivity.class);
		startActivity(in);
	}
	
	public void btnRecycleSettingsClick (View view) {
		Intent in = new Intent(getApplicationContext(),
				RecycleSettingsActivity.class);
		startActivity(in);
	}
	

	
	public void dropItemsXML(View view) {
		final Intent service = new Intent(mContext, IngressMacroService.class);

		// names of the XML tags
		final String STEPS = "steps";
		final String STEP_INIT = "stepInit";
		final String STEP_REPEAT = "stepRepeate";
		final String CLICK = "click";
		final String RUN = "run";
		final String SLEEP = "sleep";
		final String NAME = "name";
		final String POSX = "posx";
		final String POSY = "posy";

		scriptThread = new Thread(new Runnable() {
			public void run() {

				DocumentBuilderFactory factory = DocumentBuilderFactory
						.newInstance();
				// List<Message> messages = new ArrayList<Message>();
				try {
					DocumentBuilder builder = factory.newDocumentBuilder();
					// InputStream in_s =
					// getApplicationContext().getAssets().open("dropItems.xml");
					InputStream in_s = openFileInput("dropItems.xml");
					Document dom = builder.parse(in_s);
					Element root = dom.getDocumentElement();

					// get init steps
					NodeList tags = root.getElementsByTagName(STEP_INIT);
					// get first occurrence
					Node stepinit = tags.item(0);
					NodeList steps = stepinit.getChildNodes();
					for (int i = 0; i < steps.getLength(); i++) {
						// Message message = new Message();
						Node action = steps.item(i);
						String nodeName = action.getNodeName();
						if (action.getNodeType() == Node.ELEMENT_NODE) {
							if (nodeName.equalsIgnoreCase(RUN)) {
								String runValue = action.getFirstChild()
										.getNodeValue();
								if (runValue.equals("Ingress")) {
									startIngress();
								}
							} else if (nodeName.equalsIgnoreCase(SLEEP)) {
								int sleepValue = Integer.parseInt(action
										.getFirstChild().getNodeValue());
	
								Thread.sleep(sleepValue);
							} else if (nodeName.equalsIgnoreCase(CLICK)) {
								String posx = "";
								String posy = "";
								NodeList params = action.getChildNodes();
								for (int j = 0; j < params.getLength(); j++) {
									Node param = params.item(j);
									String paramName = param.getNodeName();
									if (paramName.equalsIgnoreCase(POSX)) {
										posx = param.getFirstChild().getNodeValue();
									} else if (paramName.equalsIgnoreCase(POSY)) {
										posy = param.getFirstChild().getNodeValue();
									}
								}
								runClickAction(posx, posy);
							} else {
								Log.w("UNKNOWN XML ELEMENT",
										"UNKNOWN XML ELEMENT: " + nodeName);
	
							}
						}
					}

					// REPEAT STEPS
					NodeList tagsR = root.getElementsByTagName(STEP_REPEAT);
					// get first occurrence
					Node stepRepeat = tagsR.item(0);
					NodeList stepsR = stepRepeat.getChildNodes();

					EditText editText = (EditText) findViewById(R.id.dropNumber);
					int runcount = Integer.parseInt(editText.getText()
							.toString());
					canRun = true;

					while (canRun && (runcount > 0)) {

						for (int i = 0; i < stepsR.getLength(); i++) {
							// Message message = new Message();
							Node action = stepsR.item(i);
							String nodeName = action.getNodeName();
							if (nodeName.equalsIgnoreCase(SLEEP)) {
								int sleepValue = Integer.parseInt(action
										.getFirstChild().getNodeValue());

								Thread.sleep(sleepValue);
							} else if (nodeName.equalsIgnoreCase(CLICK)) {
								String posx = "";
								String posy = "";
								NodeList params = action.getChildNodes();
								for (int j = 0; j < params.getLength(); j++) {
									Node param = params.item(j);
									String paramName = param.getNodeName();
									if (paramName.equalsIgnoreCase(POSX)) {
										posx = param.getFirstChild()
												.getNodeValue();
									} else if (paramName.equalsIgnoreCase(POSY)) {
										posy = param.getFirstChild()
												.getNodeValue();
									}
								}
								runClickAction(posx, posy);
							} else {
								Log.w("UNKNOWN XML ELEMENT",
										"UNKNOWN XML ELEMENT: " + nodeName);

							}
						}
						runcount--;
					}

				} catch (Exception e) {
					throw new RuntimeException(e);
				} finally {
					stopService(service);
				}
			}
		});

		service.putExtra("threadId", scriptThread.getId());

		service.putExtras(service);

		if (startService(service) != null) {
			Toast.makeText(this, "Service is already running",
					Toast.LENGTH_SHORT).show();
		} else {
			Log.w("STARTIGN", "STARTING");
			Toast.makeText(this,
					"There is no service running, starting service..",
					Toast.LENGTH_SHORT).show();
		}

		scriptThread.start();
		// return messages;
	}

	public void dropItems(View view) {

		final Intent service = new Intent(mContext, IngressMacroService.class);
		// mContext.startService(service);

		scriptThread = new Thread(new Runnable() {
			public void run() {
				EditText editText = (EditText) findViewById(R.id.dropNumber);
				// String cmd =
				// "sh /data/local/tmp/dropitem.sh "+editText.getText().toString();
				// execCommandLine("su");
				// execCommandLine(cmd);
				int i = Integer.parseInt(editText.getText().toString());

				try {
					canRun = true;
					startIngress();
					Thread.sleep(5000);
					while (canRun && (i > 0)) {
						clickOPS();
						Thread.sleep(500);
						clickMiddleItem();
						Thread.sleep(500);
						clickDrop();
						Thread.sleep(500);
						i--;
					}
					// mContext.stopService(service);

				} catch (InterruptedException e) {
					e.printStackTrace();
				} finally {
					mContext.stopService(service);
				}

			}
		});

		// start service after thread is initialized

		service.putExtra("threadId", scriptThread.getId());

		service.putExtras(service);

		if (startService(service) != null) {
			Toast.makeText(this, "Service is already running",
					Toast.LENGTH_SHORT).show();
		} else {
			Log.w("STARTIGN", "STARTING");
			Toast.makeText(this,
					"There is no service running, starting service..",
					Toast.LENGTH_SHORT).show();
		}

		scriptThread.start();

	}

	
	public void createListOfInputs() {
		String output = execCommandLineReturnText("getevent -c 3");
		String[] lines = output.split(System.getProperty("line.separator"));
		String lastInput = "";
		ArrayList<String[]> inputs = new ArrayList<String[]>();
		
		for (int i = 0; i < lines.length;i++) {
			String[] tmp = new String[2];
			Pattern pInput = Pattern.compile("/dev/input/(event[\\d]*)");
			//Pattern pName = Pattern.compile("name:[\\s]*.*(ts|touchscreen).*"); //only TS and touchscreen
			Pattern pName = Pattern.compile("name:[\\s]*.*\"(.*)\"");
			Matcher mInput = pInput.matcher(lines[i]);
			Matcher mName = pName.matcher(lines[i+1]);
			i++; //extra ++ since it reads two lines at once
			if (mInput.find() && mName.find()) {
				tmp[0] = mInput.group(1);
				tmp[1] = mName.group(1);
				inputs.add(tmp);
			}
		}
		String as = inputs.toString();
		createXML.createInputXML(mContext,inputs);
		
	}
	
	public void loadInputs(View view) {
		getevent();
		
		
	}
	private void startIngress() {
		execCommandLine("am start -a android.intent.action.MAIN -n com.nianticproject.ingress/.NemesisActivity");
	}

	private void runClickAction(String posx, String posy) {
		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
		String event = settings.getString("event", "event2");
		execCommandLine("sendevent /dev/input/"+event+" 3 57 7955\n"
				+ "sendevent /dev/input/"+event+" 3 50 13\n"
				+ "sendevent /dev/input/"+event+" 3 53 " + posx + "\n"
				+ "sendevent /dev/input/"+event+" 3 54 " + posy + "\n"
				+ "sendevent /dev/input/"+event+" 3 49 14\n"
				+ "sendevent /dev/input/"+event+" 3 60 0\n"
				+ "sendevent /dev/input/"+event+" 0 0 0\n"
				+ "sendevent /dev/input/"+event+" 3 57 4294967295\n"
				+ "sendevent /dev/input/"+event+" 0 0000 0\n");

	}

	private void clickOPS() {
		execCommandLine("sendevent /dev/input/event2 3 57 7955\n"
				+ "sendevent /dev/input/event2 3 50 13\n"
				+ "sendevent /dev/input/event2 3 53 628\n"
				+ "sendevent /dev/input/event2 3 54 93\n"
				+ "sendevent /dev/input/event2 3 49 14\n"
				+ "sendevent /dev/input/event2 3 60 0\n"
				+ "sendevent /dev/input/event2 0 0 0\n"
				+ "sendevent /dev/input/event2 3 57 4294967295\n"
				+ "sendevent /dev/input/event2 0 0000 0\n");

	}

	private void getevent() {
		execCommandLineReturnText("getevent -c 3");
	}
	
	private void clickMiddleItem() {
		execCommandLine("sendevent /dev/input/event2 3 57 8662\n"
				+ "sendevent /dev/input/event2 3 53 384\n"
				+ "sendevent /dev/input/event2 3 54 610\n"
				+ "sendevent /dev/input/event2 3 48 12\n"
				+ "sendevent /dev/input/event2 3 49 7\n"
				+ "sendevent /dev/input/event2 3 60 15\n"
				+ "sendevent /dev/input/event2 0 0 0\n"
				+ "sendevent /dev/input/event2 3 57 4294967295\n"
				+ "sendevent /dev/input/event2 0 0 0");
	}

	private void clickDrop() {
		execCommandLine("sendevent /dev/input/event2 0003 57 184\n"
				+ "sendevent /dev/input/event2 0003 50 8\n"
				+ "sendevent /dev/input/event2 0003 53 350\n"
				+ "sendevent /dev/input/event2 0003 54 1196\n"
				+ "sendevent /dev/input/event2 0000 0000 00000000\n"
				+ "sendevent /dev/input/event2 0003 57 4294967295\n"
				+ "sendevent /dev/input/event2 0000 0000 00000000");
	}

	private void clickRecycle() {
		execCommandLine("sendevent /dev/input/event2 0003 57 184\n"
				+ "sendevent /dev/input/event2 0003 50 8\n"
				+ "sendevent /dev/input/event2 0003 53 550\n"
				+ "sendevent /dev/input/event2 0003 54 1196\n"
				+ "sendevent /dev/input/event2 0000 0000 00000000\n"
				+ "sendevent /dev/input/event2 0003 57 4294967295\n"
				+ "sendevent /dev/input/event2 0000 0000 00000000");
	}
	
	private String execCommandLineReturnText(String command) {
		final int BUFF_LEN = 2048;
		Process p;
		StringBuilder total = null;
		try {
			//p = Runtime.getRuntime().exec(new String[]{"su", "-c", "system/bin/sh"});
			p = Runtime.getRuntime().exec("su");
		
			DataOutputStream stdin = new DataOutputStream(p.getOutputStream());
			//from here all commands are executed with su permissions
			stdin.writeBytes(command+"\n"); // \n executes the command
			InputStream stdout = p.getInputStream();
			BufferedReader r = new BufferedReader(new InputStreamReader(stdout));
			total = new StringBuilder();
			String line;
			while ((line = r.readLine()) != null) {
				if (line.startsWith("/dev/input")) break;
			    total.append(line);
			    total.append("\n");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//Toast.makeText(mContext, total.toString(), Toast.LENGTH_SHORT).show();
		return total.toString();
		
	}

	void execCommandLine(String command) {
		Runtime runtime = Runtime.getRuntime();
		Process proc = null;
		OutputStreamWriter osw = null;
		String line;

		try {
			proc = runtime.exec("su");
			osw = new OutputStreamWriter(proc.getOutputStream());
			osw.write(command);
			osw.flush();
			/*BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(proc.getInputStream()));
			while ((line = bufferedReader.readLine()) != null) {
				Log.d("READER",line);
			}
			*/
			osw.close();
		} catch (IOException ex) {
			Log.e("execCommandLine()", "Command resulted in an IO Exception: "
					+ command);
			Log.w("execCommandLine()", "Command resulted in an IO Exception: "
					+ ex.toString());
			return;
		} finally {
			if (osw != null) {
				try {
					osw.close();
					//Toast.makeText(mContext, "Command error", Toast.LENGTH_SHORT).show();
				} catch (IOException e) {
				}
			}
		}

		try {
			proc.waitFor();
		} catch (InterruptedException e) {
		}

		if (proc.exitValue() != 0) {
			Log.e("execCommandLine()", "Command returned error: " + command
					+ "\n  Exit code: " + proc.exitValue());
		}
	}

}
