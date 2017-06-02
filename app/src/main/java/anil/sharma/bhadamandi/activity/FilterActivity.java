package anil.sharma.bhadamandi.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import anil.sharma.bhadamandi.R;
import anil.sharma.bhadamandi.adapters.DataAdapter;
import anil.sharma.bhadamandi.adapters.GooglePlacesAutocompleteAdapter;
import anil.sharma.bhadamandi.model.getFilterModel;
import anil.sharma.bhadamandi.rest.API_Interface;
import anil.sharma.bhadamandi.utils.GPSTracker;
import anil.sharma.bhadamandi.utils.PrefManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FilterActivity extends AppCompatActivity {
    static String f, t, nf;
    static AutoCompleteTextView from, to, name_filter1;
    ArrayList<String> from_array;
    ArrayList<String> to_array;
    ArrayList<String> vehicle;
    ArrayList<String> frieght;
    ArrayList<String> remark, date, time, s_no, mobile, logo, title;
    RecyclerView lv;
    PrefManager pref;
    GPSTracker gps;
    double latitude, longitude;
    Button byPlace, byName;
    Geocoder geocoder;
    List<Address> addresses = null;
    TextView emptyText;
    Dialog dialog, dialog1;
    Button filter_button, filter_button1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter2);
        pref = new PrefManager(this);
        byPlace = (Button) findViewById(R.id.byPlace);
        byName = (Button) findViewById(R.id.byName);
        emptyText = (TextView) findViewById(R.id.emptyData);

        byPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog1();
            }
        });

        byName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog2();
            }
        });
        from_array = new ArrayList<String>();
        to_array = new ArrayList<String>();
        vehicle = new ArrayList<String>();
        frieght = new ArrayList<String>();
        remark = new ArrayList<String>();
        date = new ArrayList<String>();
        time = new ArrayList<String>();
        s_no = new ArrayList<String>();
        mobile = new ArrayList<String>();
        logo = new ArrayList<String>();
        title = new ArrayList<String>();
        lv = (RecyclerView) findViewById(R.id.lvlist2);

    }

    public void Dialog1() {

        // custom dialog
        dialog1 = new Dialog(FilterActivity.this);
        dialog1.setContentView(R.layout.activity_filter);
        dialog1.setTitle("Filter By Place");

        // name_filter= (EditText) dialog.findViewById(R.id.name_filter);
        // from = (AutoCompleteTextView)dialog1.findViewById(R.id.from_filter);
        to = (AutoCompleteTextView) dialog1.findViewById(R.id.to_filter);
        // from.setAdapter(new GooglePlacesAutocompleteAdapter(FilterActivity.this, R.layout.list_item));
        // from.setOnItemClickListener((AdapterView.OnItemClickListener) getActivity());

        to.setAdapter(new GooglePlacesAutocompleteAdapter(FilterActivity.this, R.layout.list_item));
        //  to.setOnItemClickListener((AdapterView.OnItemClickListener) getActivity());


        filter_button = (Button) dialog1.findViewById(R.id.filter_button);

        filter_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                t = to.getText().toString();
                getLatLong();

            }
        });


        dialog1.show();
    }

    public void getLatLong() {
        gps = new GPSTracker(FilterActivity.this, FilterActivity.this);
        if (gps.canGetLocation()) {
            latitude = gps.getLatitude();
            longitude = gps.getLongitude();
            new GeocoderLocation().execute();

        } else {
            gps.showSettingsAlert();
        }
    }

    public void Dialog2() {

        // custom dialog
        dialog = new Dialog(FilterActivity.this);
        dialog.setContentView(R.layout.activity_filter1);
        dialog.setTitle("Filter");

        // to = (AutoCompleteTextView)dialog1.findViewById(R.id.to_filter);\
        //to.setAdapter(new GooglePlacesAutocompleteAdapter(FilterActivity.this, R.layout.list_item));
        name_filter1 = (AutoCompleteTextView) dialog.findViewById(R.id.name_filter);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_dropdown_item_1line, BrokerActivity.autonames);
        name_filter1.setAdapter(adapter);

        filter_button1 = (Button) dialog.findViewById(R.id.filter_button1);

        filter_button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                nf = name_filter1.getText().toString();
                filterData2();
            }
        });


        dialog.show();
    }

    public void filterData1() {

        from_array.clear();
        to_array.clear();
        vehicle.clear();
        frieght.clear();
        remark.clear();
        date.clear();
        time.clear();
        s_no.clear();
        mobile.clear();
        logo.clear();
        title.clear();
        final ProgressDialog mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Loading...");
        mProgressDialog.show();

        API_Interface.Factory.getInstance().getNameList1().enqueue(new Callback<getFilterModel>() {
            @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
            @Override
            public void onResponse(Call<getFilterModel> call, Response<getFilterModel> response) {
                getFilterModel model = response.body();

                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();
                for (int i = model.fieldsArray.size() - 1; i >= 0; i--) {
                    String s0 = null, s13 = null, s14 = null, s15 = null, s1 = null, s2 = null, s3 = null, s5 = null, s7 = null, s8 = null, s9 = null, s10 = null, s11 = null, s12 = null;
                    s0 = model.fieldsArray.get(i).S_NO;
                    s1 = model.fieldsArray.get(i).FROM1;
                    s2 = model.fieldsArray.get(i).TO1;
                    s3 = model.fieldsArray.get(i).VEHICLE;
                    s5 = model.fieldsArray.get(i).FRIEGHT;
                    s7 = model.fieldsArray.get(i).REMARK;
                    s8 = model.fieldsArray.get(i).DATE;
                    s9 = model.fieldsArray.get(i).USERNAME;
                    s10 = model.fieldsArray.get(i).IMAGE_URL;
                    s11 = model.fieldsArray.get(i).NAME;
                    s12 = model.fieldsArray.get(i).TIME;
                    s13 = model.fieldsArray.get(i).MOBILE;


                    if (s1.equals("") || s2.equals("") || s3.equals("") || s8.equals("") || s12.equals("")) {

                    } else {
                        if (s1.contains(f) && s2.contains(t)) {

                            s_no.add(s0);


                            from_array.add(s1);

                            to_array.add(s2);

                            vehicle.add(s3);


                            frieght.add(s5);


                            remark.add(s7);

                            date.add(s8);

                            time.add(s12);
                            mobile.add(s13);
                            logo.add(s10);
                            title.add(s11);
                        }
                    }
                }


                if (from_array.isEmpty()) {
                    emptyText.setVisibility(View.VISIBLE);
                } else {
                    emptyText.setVisibility(View.INVISIBLE);
                    lv.setHasFixedSize(true);
                    pref.setValue("0");
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(FilterActivity.this);
                    lv.setLayoutManager(layoutManager);

                    RecyclerView.Adapter adapter = new DataAdapter(FilterActivity.this, s_no,
                            from_array, to_array, vehicle, frieght, remark, date, time, mobile, logo, title);
                    lv.setAdapter(adapter);
                }
                dialog1.dismiss();
                //       swipeRefreshLayout.setRefreshing(false);

            }


            @Override
            public void onFailure(Call<getFilterModel> call, Throwable t) {
                //   swipeRefreshLayout.setRefreshing(false);
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();
                dialog1.dismiss();
                Log.e("Failed", t.getMessage());
            }

        });

    }

    public void filterData2() {

        from_array.clear();
        to_array.clear();
        vehicle.clear();
        frieght.clear();
        remark.clear();
        date.clear();
        time.clear();
        s_no.clear();
        mobile.clear();
        logo.clear();
        title.clear();
        final ProgressDialog mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Loading...");
        mProgressDialog.show();

        API_Interface.Factory.getInstance().getNameList2(
                nf
        ).enqueue(new Callback<getFilterModel>() {
            @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
            @Override
            public void onResponse(Call<getFilterModel> call, Response<getFilterModel> response) {
                getFilterModel model = response.body();

                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();
                for (int i = model.fieldsArray.size() - 1; i >= 0; i--) {
                    String s0 = null, s13 = null, s14 = null, s15 = null, s1 = null, s2 = null, s3 = null, s5 = null, s7 = null, s8 = null, s9 = null, s10 = null, s11 = null, s12 = null;
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
                    if (s1.equals("") || s2.equals("") || s3.equals("") || s8.equals("") || s12.equals("")) {

                    } else {
                        s_no.add(s0);

                        from_array.add(s1);

                        to_array.add(s2);

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


                if (from_array.isEmpty()) {
                    emptyText.setVisibility(View.VISIBLE);
                } else {
                    emptyText.setVisibility(View.INVISIBLE);
                    lv.setHasFixedSize(true);
                    pref.setValue("0");
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(FilterActivity.this);
                    lv.setLayoutManager(layoutManager);
                    RecyclerView.Adapter adapter = new DataAdapter(FilterActivity.this, s_no,
                            from_array, to_array, vehicle, frieght, remark, date, time, mobile, logo, title);
                    lv.setAdapter(adapter);

                    //       swipeRefreshLayout.setRefreshing(false);
                }
                dialog.dismiss();
            }


            @Override
            public void onFailure(Call<getFilterModel> call, Throwable t) {
                //   swipeRefreshLayout.setRefreshing(false);
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();
                Log.e("Failed", t.getMessage());
                dialog.dismiss();
            }

        });

    }

    private class GeocoderLocation extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {


            geocoder = new Geocoder(FilterActivity.this, Locale.getDefault());
            try {
                addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5


                String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                f = addresses.get(0).getLocality();
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
            //choose_city.setText(f);
            filterData1();
        }
    }
}
