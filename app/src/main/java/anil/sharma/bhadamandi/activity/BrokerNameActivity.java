package anil.sharma.bhadamandi.activity;

import android.app.ProgressDialog;
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
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import anil.sharma.bhadamandi.R;
import anil.sharma.bhadamandi.adapters.DataAdapter;
import anil.sharma.bhadamandi.adapters.DataAdapterNames;
import anil.sharma.bhadamandi.model.getDataModel;
import anil.sharma.bhadamandi.rest.API_Interface;
import anil.sharma.bhadamandi.utils.AlertDialogManager;
import anil.sharma.bhadamandi.utils.ConnectionDetector;
import anil.sharma.bhadamandi.utils.GPSTracker;
import anil.sharma.bhadamandi.utils.PrefManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BrokerNameActivity extends AppCompatActivity {

    String city, city_geocoder;
    GPSTracker gps;
    double latitude, longitude;
    PrefManager pref;
    ArrayList<String> from;
    ArrayList<String> to;
    ArrayList<String> vehicle;
    ArrayList<String> frieght;
    ArrayList<String> remark, date, time, s_no, mobile, image, logo, title;
    // private RecyclerView recyclerView;
    RecyclerView lv;
    Geocoder geocoder;
    List<Address> addresses = null;

    AutoCompleteTextView choose_city;
    DataAdapter adapter;
    ImageView g;
    private SwipeRefreshLayout swipeRefreshLayout;

    //private Handler handler = new Handler();
    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_broker_name);


        // getLatLong();
       /* geocoder = new Geocoder(this, Locale.getDefault());
        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
        } catch (IOException e) {
            e.printStackTrace();
        }

        String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
        city = addresses.get(0).getLocality();
        String state = addresses.get(0).getAdminArea();
        String country = addresses.get(0).getCountryName();
        String postalCode = addresses.get(0).getPostalCode();
        String knownName = addresses.get(0).getFeatureName(); // Only if available else return NULL
*/
        pref = new PrefManager(this);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                AlertDialogManager alert = new AlertDialogManager();
                ConnectionDetector cd = new ConnectionDetector(getApplicationContext());

                // Check if Internet present
                if (!cd.isConnectingToInternet()) {
                    // Internet Connection is not present
                    alert.showAlertDialog(BrokerNameActivity.this,
                            "Internet Connection Error",
                            "Please connect to working Internet connection", false);
                    // stop executing code by return
                    return;
                }
                testData();
            }
        });


        lv = (RecyclerView) findViewById(R.id.lvlist);
        AlertDialogManager alert = new AlertDialogManager();
        ConnectionDetector cd = new ConnectionDetector(getApplicationContext());

        // Check if Internet present
        if (!cd.isConnectingToInternet()) {
            // Internet Connection is not present
            alert.showAlertDialog(BrokerNameActivity.this,
                    "Internet Connection Error",
                    "Please connect to working Internet connection", false);
            // stop executing code by return
            return;
        }
        testData();


    }


    public void getLatLong() {
        gps = new GPSTracker(BrokerNameActivity.this, BrokerNameActivity.this);
        if (gps.canGetLocation()) {
            latitude = gps.getLatitude();
            longitude = gps.getLongitude();
            new GeocoderLocation().execute();
        } else {
            gps.showSettingsAlert();
        }
    }

    public void testData() {
        from = new ArrayList<String>();
        to = new ArrayList<String>();
        vehicle = new ArrayList<String>();
        frieght = new ArrayList<String>();
        remark = new ArrayList<String>();
        date = new ArrayList<String>();
        time = new ArrayList<String>();
        s_no = new ArrayList<String>();
        mobile = new ArrayList<String>();
        image = new ArrayList<String>();
        logo = new ArrayList<String>();
        title = new ArrayList<String>();
        final ProgressDialog mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Loading...");
        mProgressDialog.show();

        API_Interface.Factory.getInstance().getDataList().enqueue(new Callback<getDataModel>() {
            @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
            @Override
            public void onResponse(Call<getDataModel> call, Response<getDataModel> response) {
                getDataModel model = response.body();

                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();
                for (int i = model.fieldsArray.size() - 1; i >= 0; i--) {
                    String s0 = null, s13 = null, s15 = null, s14 = null, s1 = null, s2 = null, s3 = null, s5 = null, s7 = null, s8 = null, s9 = null, s10 = null, s11 = null, s12 = null;
                    s0 = model.fieldsArray.get(i).S_NO;
                    s1 = model.fieldsArray.get(i).FROM1;
                    s2 = model.fieldsArray.get(i).TO1;
                    s3 = model.fieldsArray.get(i).VEHICLE;
                    s5 = model.fieldsArray.get(i).FRIEGHT;
                    s7 = model.fieldsArray.get(i).REMARK;
                    s8 = model.fieldsArray.get(i).DATE;
                    s9 = model.fieldsArray.get(i).USERNAME;
                    s10 = model.fieldsArray.get(i).CITY;
                    s11 = model.fieldsArray.get(i).NAME;
                    s12 = model.fieldsArray.get(i).TIME;
                    s13 = model.fieldsArray.get(i).MOBILE;
                    s14 = model.fieldsArray.get(i).IMAGE_URL;
                    s15 = model.fieldsArray.get(i).NAME;
                    //  s14 = model.fieldsArray.get(i).MOBILE;

                    if (s11.equals(DataAdapterNames.name.get(DataAdapterNames.idx))) {
                        if (s1.equals("") || s2.equals("") || s3.equals("") || s5.equals("") || s7.equals("") || s8.equals("") || s9.equals("") || s10.equals("") || s11.equals("") || s12.equals("")) {

                        } else {
                            s_no.add(s0);

                            from.add(s1);

                            to.add(s2);

                            vehicle.add(s3);


                            frieght.add(s5);


                            remark.add(s7);

                            date.add(s8);

                            time.add(s12);
                            mobile.add(s13);
                            logo.add(s14);
                            title.add(s15);

                        }
                    }

                }

                lv.setHasFixedSize(true);
                pref.setValue("0");
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(BrokerNameActivity.this);
                lv.setLayoutManager(layoutManager);
                RecyclerView.Adapter adapter = new DataAdapter(BrokerNameActivity.this, s_no,
                        from, to, vehicle, frieght, remark, date, time, mobile, logo, title);
                lv.setAdapter(adapter);

                swipeRefreshLayout.setRefreshing(false);

            }


            @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
            @Override
            public void onFailure(Call<getDataModel> call, Throwable t) {
                swipeRefreshLayout.setRefreshing(false);
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();
                Log.e("Failed", t.getMessage());
            }

        });

    }

    private class GeocoderLocation extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            Geocoder geocoder;
            geocoder = new Geocoder(BrokerNameActivity.this, Locale.getDefault());
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
        }
    }

}
