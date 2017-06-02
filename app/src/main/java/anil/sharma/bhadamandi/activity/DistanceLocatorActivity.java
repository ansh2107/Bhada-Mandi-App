package anil.sharma.bhadamandi.activity;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.barcode.Barcode;

import java.io.IOException;
import java.util.List;

import anil.sharma.bhadamandi.R;
import anil.sharma.bhadamandi.adapters.GooglePlacesAutocompleteAdapter;

public class DistanceLocatorActivity extends AppCompatActivity {
    AutoCompleteTextView e1, e2;
    double lat1 = 0.0, long1 = 0.0, lat2 = 0.0, long2 = 0.0;
    TextView t;
    Button but;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_distance_locator);
        e1 = (AutoCompleteTextView) findViewById(R.id.dist_1);
        e2 = (AutoCompleteTextView) findViewById(R.id.dist_2);


        e1.setAdapter(new GooglePlacesAutocompleteAdapter(DistanceLocatorActivity.this, R.layout.list_item));
        //  from.setOnItemClickListener((AdapterView.OnItemClickListener) getActivity());

        e2.setAdapter(new GooglePlacesAutocompleteAdapter(DistanceLocatorActivity.this, R.layout.list_item));

        but = (Button) findViewById(R.id.dist_but);
        t = (TextView) findViewById(R.id.dist_txt);
        but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLocationFromAddress(e1.getText().toString(), 1);
                getLocationFromAddress(e2.getText().toString(), 2);

                if (lat1 == 0.0 || lat2 == 0.0 || long1 == 0.0 || long2 == 0.0) {
                    Toast.makeText(DistanceLocatorActivity.this, "City Not found! ", Toast.LENGTH_SHORT).show();
                } else {
                    /*double ans=Math.sqrt(((Math.abs(lat1-lat2))*(Math.abs(lat1-lat2)))+((Math.abs(long1-long2))*(Math.abs(long1-long2))));
                    String s1 = Double.toString(ans);*/
                    Location loc1 = new Location("");
                    loc1.setLatitude(lat1);
                    loc1.setLongitude(long1);

                    Location loc2 = new Location("");
                    loc2.setLatitude(lat2);
                    loc2.setLongitude(long2);

                    double ans = loc1.distanceTo(loc2);
                    ans /= 1000.0;
                    long dist = (long) ans;
                    String s1 = Long.toString(dist);
                    s1 += " km";
                    t.setText(s1);
                    Toast.makeText(DistanceLocatorActivity.this, "Distance is " + s1, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public Barcode.GeoPoint getLocationFromAddress(String strAddress, int idx) {

        Geocoder coder = new Geocoder(this);
        List<Address> address;
        Barcode.GeoPoint p1 = null;

        try {
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }
            Address location = address.get(0);
            location.getLatitude();
            location.getLongitude();

            p1 = new Barcode.GeoPoint((int) 1, (double) (location.getLatitude()),
                    (double) (location.getLongitude()));
            if (idx == 1) {
                lat1 = p1.lat;
                long1 = p1.lng;
            } else {
                lat2 = p1.lat;
                long2 = p1.lng;
            }

            return p1;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return p1;
    }

}
