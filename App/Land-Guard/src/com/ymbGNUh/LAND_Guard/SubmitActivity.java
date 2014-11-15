package com.ymbGNUh.LAND_Guard;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.*;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * Created by jkahn on 11/14/14.
 */
public class SubmitActivity extends Activity {

    GPSTracker gps;
    double latitude, longitude;

    ProgressDialog pd;
    CountDownLatch latch;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.submit);

        final EditText locationExtra = (EditText) findViewById(R.id.location_box);
        final EditText specialInstructions = (EditText) findViewById(R.id.special_instructions_box);
        final Spinner severitySpinner = (Spinner) findViewById(R.id.severitySpinner);
        final Spinner emergencySpinner = (Spinner) findViewById(R.id.emergencySpinner);

        gps = new GPSTracker(SubmitActivity.this);
        if (gps.canGetLocation()) {
            latitude = gps.getLatitude();
            longitude = gps.getLongitude();
            Log.d("LAND", "Latitude: " + latitude);
            Log.d("LAND", "Longitude: " + longitude);
        } else {
            gps.showSettingsAlert();
        }

        String[] severityData = new String[10];
        for (int i = 0; i < severityData.length; i++) {
            severityData[i] = "" + (i + 1);
        }
        ArrayAdapter<String> severitySpinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, severityData);
        severitySpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        severitySpinner.setAdapter(severitySpinnerAdapter);

        String[] emergenciesData = Settings.possibleEmergencies.toArray(new String[Settings.possibleEmergencies.size()]);
        ArrayAdapter<String> emergenciesSpinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, emergenciesData);
        emergenciesSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        emergencySpinner.setAdapter(emergenciesSpinnerAdapter);

        Button submitButton = (Button) findViewById(R.id.submitButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                latch = new CountDownLatch(1);
                try {
                    final JSONObject data = new JSONObject();

                    data.put("longitude", longitude);
                    data.put("latitude", latitude);

                    String location = locationExtra.getText().toString();
                    data.put("location", location);

                    String instructions = specialInstructions.getText().toString();
                    data.put("special_instructions", instructions);

                    String severity = "" + (severitySpinner.getSelectedItemPosition() + 1);
                    data.put("severity", severity);

                    String emergency = "" + Settings.possibleEmergencies.get(emergencySpinner.getSelectedItemPosition());
                    data.put("emergency", emergency);

                    Log.d("LAND", data.toString());

                    //if (sendDataClient.getConnection().isOpen()) {
                    //    sendDataClient.send(data.toString());
                    //}

                    pd = ProgressDialog.show(SubmitActivity.this, "Submitting", "Please Wait...");

                    Thread t = new Thread(new Runnable() {
                        public void run() {
                            Looper.prepare();
                            try {
//                                URL object = new URL(Settings.url);
//                                HttpURLConnection con = (HttpURLConnection) object.openConnection();
//                                con.setDoOutput(true);
//                                con.setDoInput(true);
//                                con.setRequestProperty("Content-Type", "application/json");
//                                con.setRequestProperty("Accept", "application/json");
//                                con.setRequestMethod("POST");
//                                OutputStreamWriter writer = new OutputStreamWriter(con.getOutputStream());
//                                writer.write(data.toString());
                                HttpResponse response = request(Settings.url, data);
                                Log.d("LAND", "Wrote Data");
                            } catch (HttpHostConnectException e) {
                                // Connection Refused
                                e.printStackTrace();
                                //finish();
                                //Message msg1 = new Message();
                                //msg1.obj = "refused";
                                //handler.sendMessage(msg1);
                            } catch (IOException e) {
                                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            //toast.show();
                            pd.dismiss();
                            latch.countDown();
                        }
                    });
                    t.start();
                    timeOutThread(t);
                } catch (JSONException e) {
                    Toast.makeText(getBaseContext(), "Failed To Send Data", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        });
    }

    public HttpResponse request(String url, JSONObject request)
            throws IOException, IllegalStateException, JSONException {

        DefaultHttpClient client = (DefaultHttpClient) WebClientDevWrapper.getNewHttpClient();

        HttpPost post = new HttpPost(url);
        post.addHeader("Content-Type", "application/json");
        post.addHeader("Accept", "application/json");
        post.setEntity(new StringEntity(request.toString(), "utf-8"));
        HttpResponse response = client.execute(post);
        return response;
    }

    @Override
    public void onDestroy() {
        super.onStop();
        gps.stopUsingGPS();
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.obj.equals("timeout")) {
                Context context = getApplicationContext();
                CharSequence text = "Poor connection, unable send request!";
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }
        }
    };

    private void timeOutThread(final Thread t){//final Handler handler){
        Thread j = new Thread( new Runnable(){
            public void run(){
                Looper.prepare();
                long endTimeMillis = System.currentTimeMillis() + (10 * 1000); //10 * 1000 = 10 sec (10 * 1 second)
                while (t.isAlive()) {
                    if (System.currentTimeMillis() > endTimeMillis) {
                        try {
                            pd.dismiss();
                        }
                        catch (Exception e) {
                        }
                        Message msg0 = new Message();
                        msg0.obj = "timeout";
                        handler.sendMessage(msg0);
                        //finish();
                        break;
                    }
                    try {
                        Thread.sleep(2000);
                    }
                    catch (InterruptedException t) {

                    }
                }
            }
        });
        j.start();
    }
}
