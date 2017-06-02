package anil.sharma.bhadamandi.activity;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import anil.sharma.bhadamandi.R;
import anil.sharma.bhadamandi.adapters.GooglePlacesAutocompleteAdapter;
import anil.sharma.bhadamandi.model.postString;
import anil.sharma.bhadamandi.rest.API_Interface;
import anil.sharma.bhadamandi.utils.GPSTracker;
import anil.sharma.bhadamandi.utils.PrefManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    private static final int STORAGE_PERMISSION_CODE = 123;
    private static final String TAG = "1";
    public static double latitude = 0.0, longitude = 0.0;
    static public String set_city;
    GPSTracker gps;
    PrefManager pref;
    Button transport, skip;
    AutoCompleteTextView city;
    AutoCompleteTextView choose_city;
    String city_geocoder;
    ImageView g;
    Dialog dialog;
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (Build.VERSION.SDK_INT >= 23) {
            // Call some material design APIs here
            checkAndRequestPermissions();
        }


        mContext = this;
        pref = new PrefManager(MainActivity.this);
        transport = (Button) findViewById(R.id.trans_company);
        skip = (Button) findViewById(R.id.but);
        //   city = (AutoCompleteTextView) findViewById(R.id.but);

        deleteDate();
        transport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s1 = pref.getPassword();
                String s2 = pref.getUsername();
                if (s1 == null || s2 == null) {
                    Intent i = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(i);
                    finish();
                } else {
                    Intent i = new Intent(MainActivity.this, DrawerActivity.class);
                    startActivity(i);
                    // finish();
                }
            }
        });

        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // custom dialog
                dialog = new Dialog(MainActivity.this);
                dialog.setContentView(R.layout.activity_city);
                dialog.setTitle("Choose City");
                choose_city = (AutoCompleteTextView) dialog.findViewById(R.id.choose_city);
                choose_city.setAdapter(new GooglePlacesAutocompleteAdapter(MainActivity.this, R.layout.list_item));


                g = (ImageView) dialog.findViewById(R.id.gps);
                g.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getLatLong();


                    }


                });


                Button dialogButton = (Button) dialog.findViewById(R.id.submit_city);
                // if button is clicked, close the custom dialog
                dialogButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (choose_city.getText().toString().contains(",")) {
                            String a[] = choose_city.getText().toString().split(",");
                            set_city = a[0];
                        } else {
                            set_city = choose_city.getText().toString();
                        }
                        Intent i = new Intent(MainActivity.this, BrokerActivity.class);
                        startActivity(i);
                        dialog.dismiss();
                    }
                });

                dialog.show();

            }
        });
    }

    public void getLatLong() {
        gps = new GPSTracker(mContext, MainActivity.this);
        if (gps.canGetLocation()) {
            latitude = gps.getLatitude();
            longitude = gps.getLongitude();
            new GeocoderLocation().execute();
        } else {

            //dialog.dismiss();
            gps.showSettingsAlert();


        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        Log.d(TAG, "Permission callback called-------");
        switch (requestCode) {
            case REQUEST_ID_MULTIPLE_PERMISSIONS: {

                Map<String, Integer> perms = new HashMap<>();
                // Initialize the map with both permissions
                perms.put(android.Manifest.permission.CAMERA, PackageManager.PERMISSION_GRANTED);
                perms.put(android.Manifest.permission.CALL_PHONE, PackageManager.PERMISSION_GRANTED);
                perms.put(android.Manifest.permission.RECORD_AUDIO, PackageManager.PERMISSION_GRANTED);
                perms.put(android.Manifest.permission.READ_CONTACTS, PackageManager.PERMISSION_GRANTED);
                perms.put(android.Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                perms.put(android.Manifest.permission.READ_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                perms.put(android.Manifest.permission.ACCESS_FINE_LOCATION, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.ACCESS_COARSE_LOCATION, PackageManager.PERMISSION_GRANTED);
                perms.put(android.Manifest.permission.SEND_SMS, PackageManager.PERMISSION_GRANTED);


                // Fill with actual results from user
                if (grantResults.length > 0) {
                    for (int i = 0; i < permissions.length; i++)
                        perms.put(permissions[i], grantResults[i]);
                    // Check for both permissions
                    if (perms.get(android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                            && perms.get(android.Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED
                            && perms.get(android.Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED
                            && perms.get(android.Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED
                            && perms.get(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                            && perms.get(android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                            && perms.get(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                            && perms.get(android.Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED
                            && perms.get(android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        Log.d(TAG, "sms & location services permission granted");
                        // process the normal flow
                        //else any one or both the permissions are not granted
                    } else {
                        Log.d(TAG, "Some permissions are not granted ask again ");
                        //permission is denied (this is the first time, when "never ask again" is not checked) so ask again explaining the usage of permission
//                        // shouldShowRequestPermissionRationale will return true
                        //show the dialog or snackbar saying its necessary and try again otherwise proceed with setup.
                        if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.CAMERA)
                                || ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.CALL_PHONE)
                                || ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.RECORD_AUDIO)
                                || ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.READ_CONTACTS)
                                || ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                                || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                                || ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.SEND_SMS)
                                || ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                || ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)
                                ) {
                            showDialogOK("SMS and Location Services Permission required for this app",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            switch (which) {
                                                case DialogInterface.BUTTON_POSITIVE:
                                                    checkAndRequestPermissions();
                                                    break;
                                                case DialogInterface.BUTTON_NEGATIVE:
                                                    // proceed with logic by disabling the related features or quit the app.
                                                    break;
                                            }
                                        }
                                    });
                        }
                        //permission is denied (and never ask again is  checked)
                        //shouldShowRequestPermissionRationale will return false
                        else {
                            Toast.makeText(this, "Go to settings and enable permissions", Toast.LENGTH_LONG)
                                    .show();
                            //                            //proceed with logic by disabling the related features or quit the app.
                        }
                    }
                }
            }
        }
    }

    private void showDialogOK(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", okListener)
                .create()
                .show();
    }

    private boolean checkAndRequestPermissions() {
        int cam = ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.CAMERA);
        int call_phone = ContextCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE);
        int record_audio = ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.RECORD_AUDIO);
        int read_contacts = ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_CONTACTS);
        int send = ContextCompat.checkSelfPermission(this, android.Manifest.permission.SEND_SMS);
        int write = ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int read = ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE);
        int access = ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION);
        int access_coarse = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
        List<String> listPermissionsNeeded = new ArrayList<>();

        if (cam != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.CAMERA);
        }
        if (send != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.SEND_SMS);
        }
        if (access != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (access_coarse != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        }
        if (call_phone != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.CALL_PHONE);
        }
        if (record_audio != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.RECORD_AUDIO);
        }
        if (read_contacts != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.READ_CONTACTS);
        }
        if (write != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (read != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.READ_EXTERNAL_STORAGE);
        }

        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }

    public void deleteDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        //Date myDate = dateFormat.parse(dateString);
        Calendar cal = GregorianCalendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.DAY_OF_YEAR, -3);
        Date daysBeforeDate = cal.getTime();
        String date = dateFormat.format(daysBeforeDate);

        final ProgressDialog mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Loading...");
        mProgressDialog.show();
        API_Interface.Factory.getInstance().postDate(
                date
        ).enqueue(new Callback<postString>() {


            @Override
            public void onResponse(Call<postString> call, Response<postString> response) {
                postString model = response.body();
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();
                if (model.responseCode.equals("SUCCESS")) {
                    //  Toast.makeText(MainActivity.this, "sucess", Toast.LENGTH_SHORT).show();

                } else {
                    //Toast.makeText(MainActivity.this, "fail", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<postString> call, Throwable t) {
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();
                Log.e("Failed", t.getMessage());
            }
        });

    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        MainActivity.this.finish();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    private class GeocoderLocation extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            Geocoder geocoder;
            geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
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

