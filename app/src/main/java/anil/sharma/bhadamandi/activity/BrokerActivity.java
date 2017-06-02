package anil.sharma.bhadamandi.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import anil.sharma.bhadamandi.R;
import anil.sharma.bhadamandi.adapters.DataAdapter;
import anil.sharma.bhadamandi.adapters.DataAdapterNames;
import anil.sharma.bhadamandi.adapters.GooglePlacesAutocompleteAdapter;
import anil.sharma.bhadamandi.model.getNameModel;
import anil.sharma.bhadamandi.rest.API_Interface;
import anil.sharma.bhadamandi.utils.AlertDialogManager;
import anil.sharma.bhadamandi.utils.ConnectionDetector;
import anil.sharma.bhadamandi.utils.GPSTracker;
import anil.sharma.bhadamandi.utils.PrefManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BrokerActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    static ArrayList<String> autonames;
    static String f, t, nf;
    static AutoCompleteTextView from, to;
    static EditText name_filter;
    String city, city_geocoder;
    GPSTracker gps;
    ImageView g, filter;
    double latitude, longitude;
    PrefManager pref;
    ArrayList<String> name, mob, username, image;
    // private RecyclerView recyclerView;
    RecyclerView lv;
    AutoCompleteTextView choose_city;
    DataAdapter adapter;
    Geocoder geocoder;
    List<Address> addresses = null;
    Button filter_button;
    Dialog dialog;
    TextView emptyText;
    private SwipeRefreshLayout swipeRefreshLayout;

    //private Handler handler = new Handler();
    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_broker);

        AlertDialogManager alert = new AlertDialogManager();
        ConnectionDetector cd = new ConnectionDetector(getApplicationContext());
        choose_city = (AutoCompleteTextView) findViewById(R.id.choose_city1);
        choose_city.setAdapter(new GooglePlacesAutocompleteAdapter(this, R.layout.list_item));

        choose_city.setText(MainActivity.set_city);
        choose_city.setOnItemClickListener(this);
        filter = (ImageView) findViewById(R.id.filter);
        emptyText = (TextView) findViewById(R.id.emptyData);


        name = new ArrayList<String>();
        username = new ArrayList<String>();
        autonames = new ArrayList<String>();
        mob = new ArrayList<String>();
        image = new ArrayList<String>();


        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(BrokerActivity.this, FilterActivity.class);
                startActivity(i);

            }
        });


        g = (ImageView) findViewById(R.id.gps1);
        g.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLatLong();


            }
        });

        //  choose_city.setAdapter(new GooglePlacesAutocompleteAdapter(BrokerActivity.this, R.layout.list_item));
        // Check if Internet present
        if (!cd.isConnectingToInternet()) {
            // Internet Connection is not present
            alert.showAlertDialog(BrokerActivity.this,
                    "Internet Connection Error",
                    "Please connect to working Internet connection", false);
            // stop executing code by return
            return;
        }


        pref = new PrefManager(this);


        lv = (RecyclerView) findViewById(R.id.lvlist_1);

        testData();

       /* final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getLatLong();
            }
        }, 2000);
*/
    }

    public void getLatLong() {

        gps = new GPSTracker(BrokerActivity.this, BrokerActivity.this);

        if (gps.canGetLocation()) {
            latitude = gps.getLatitude();
            longitude = gps.getLongitude();

            new GeocoderLocation().execute();
        } else {
            gps.showSettingsAlert();
        }
    }

    public void testData() {
        String a[] = choose_city.getText().toString().split(",");
        final String myCity = a[0];
        name.clear();
        username.clear();
        autonames.clear();
        mob.clear();
        image.clear();
        final ProgressDialog mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Loading...");
        mProgressDialog.show();

        API_Interface.Factory.getInstance().getNameList().enqueue(new Callback<getNameModel>() {


            @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
            @Override
            public void onResponse(Call<getNameModel> call, Response<getNameModel> response) {
                getNameModel model = response.body();
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();

                for (int i = model.fieldsArray.size() - 1; i >= 0; i--) {
                    String s1 = null, s10 = null, s11 = null, s2 = null, s12 = null;
                    s2 = model.fieldsArray.get(i).USERNAME;
                    s1 = model.fieldsArray.get(i).NAME;
                    s10 = model.fieldsArray.get(i).CITY;
                    s12 = model.fieldsArray.get(i).IMAGE_URL;
                    String ss = "";
                    for (int j = 0; j < s10.length(); j++) {
                        char c = s10.charAt(j);
                        if (c == ',') break;
                        else {
                            ss += c;
                        }
                    }

                    String av = ss;
                    s11 = model.fieldsArray.get(i).MOBILE;
                    if (myCity.contains(ss)) {
                        if (s1.equals("") || s10.equals("")) {

                        } else {
                            if (username.contains(s2)) {
                            } else {
                                username.add(s2);
                                name.add(s1);
                                mob.add(s11);
                                image.add(s12);
                            }

                        }
                    }
                    if (s1.equals("") || s10.equals("")) {

                    } else {
                        if (autonames.contains(s1)) {
                        } else {
                            autonames.add(s1);
                        }

                    }
                }
                if (username.isEmpty()) {
                    emptyText.setVisibility(View.VISIBLE);
                } else {
                    emptyText.setVisibility(View.INVISIBLE);
                    lv.setHasFixedSize(true);

                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(BrokerActivity.this);
                    lv.setLayoutManager(layoutManager);
                    RecyclerView.Adapter adapter = new DataAdapterNames(BrokerActivity.this,
                            name, mob, image);
                    lv.setAdapter(adapter);

                    // swipeRefreshLayout.setRefreshing(false);
                }
            }


            @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
            @Override
            public void onFailure(Call<getNameModel> call, Throwable t) {
                swipeRefreshLayout.setRefreshing(false);
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();
                Log.e("Failed", t.getMessage());
            }

        });

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        testData();
    }

    private class GeocoderLocation extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            Geocoder geocoder;
            geocoder = new Geocoder(BrokerActivity.this, Locale.getDefault());
            List<Address> addresses = null;
            try {
                addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5


                String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                city_geocoder = addresses.get(0).getLocality();
                String state = addresses.get(0).getAdminArea();
                String country = addresses.get(0).getCountryName();
                String postalCode = addresses.get(0).getPostalCode();
                String knownName = addresses.get(0).getFeatureName(); // Only if available else return NULL


            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }


        @Override
        protected void onPostExecute(String result) {
            choose_city.setText(city_geocoder);
            testData();
        }
    }
}
