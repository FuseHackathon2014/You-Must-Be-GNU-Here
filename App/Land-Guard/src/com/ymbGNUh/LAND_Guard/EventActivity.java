package com.ymbGNUh.LAND_Guard;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

/**
 * Created by jkahn on 11/15/14.
 */
public class EventActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event);
        Intent intent = getIntent();
        String json = intent.getExtras().getString("json");
        try {
            JSONObject data = new JSONObject(json);
            String emergency = data.getString("e");
            String severity = data.getString("sev");
            final String latitude = data.getString("lat");
            final String longitude = data.getString("lon");
            String specialInstructions = data.getString("sp");
            String locationExtra = data.getString("loc");

            TextView emergencyText = (TextView) findViewById(R.id.emergencyType);
            emergencyText.setText(emergency);
            TextView severityText = (TextView) findViewById(R.id.severenesType);
            severityText.setText(severity);
            TextView latitudeText = (TextView) findViewById(R.id.latitudeType);
            latitudeText.setText(latitude);
            TextView longitudeText = (TextView) findViewById(R.id.longitudeType);
            longitudeText.setText(longitude);
            TextView specialInstructionsText = (TextView) findViewById(R.id.specialInstructionsType);
            specialInstructionsText.setText(specialInstructions.length() == 0 ? "N/A" : specialInstructions);
            TextView locationExtraText = (TextView) findViewById(R.id.locationExtraType);
            locationExtraText.setText(locationExtra.length() == 0 ? "N/A" : locationExtra);

            Button mapsButton = (Button) findViewById(R.id.mapsButton);
            mapsButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String label = "Person of Interest";
                    String uriBegin = "geo:" + latitude + "," + longitude;
                    String query = latitude + "," + longitude + "(" + label + ")";
                    String encodedQuery = Uri.encode(query);
                    String uriString = uriBegin + "?q=" + encodedQuery + "&z=16";
                    Uri uri = Uri.parse(uriString);
                    Intent maps = new Intent(android.content.Intent.ACTION_VIEW, uri);
                    maps.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(maps);
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
