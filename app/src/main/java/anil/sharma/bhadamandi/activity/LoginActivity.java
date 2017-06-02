package anil.sharma.bhadamandi.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import anil.sharma.bhadamandi.R;
import anil.sharma.bhadamandi.model.Movie;
import anil.sharma.bhadamandi.rest.API_Interface;
import anil.sharma.bhadamandi.utils.AlertDialogManager;
import anil.sharma.bhadamandi.utils.ConnectionDetector;
import anil.sharma.bhadamandi.utils.PrefManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    Button register, submit;
    EditText username, password;
    PrefManager pref;
    int flag = 0;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        pref = new PrefManager(LoginActivity.this);
        register = (Button) findViewById(R.id.reg_but);
        submit = (Button) findViewById(R.id.login_but);
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);

        pd = new ProgressDialog(LoginActivity.this);


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialogManager alert = new AlertDialogManager();
                ConnectionDetector cd = new ConnectionDetector(getApplicationContext());

                // Check if Internet present
                if (!cd.isConnectingToInternet()) {
                    // Internet Connection is not present
                    alert.showAlertDialog(LoginActivity.this,
                            "Internet Connection Error",
                            "Please connect to working Internet connection", false);
                    // stop executing code by return
                    return;
                }

                testData();
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, RegActivity.class);
                startActivity(i);
                finish();
            }
        });
    }


    public void testData() {
        final ProgressDialog mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Loading...");
        mProgressDialog.show();


        API_Interface.Factory.getInstance().getData().enqueue(new Callback<Movie>() {

            @Override
            public void onResponse(Call<Movie> call, Response<Movie> response) {
                Movie model = response.body();
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();

                for (int i = model.accounts.size() - 1; i >= 0; i--) {

                    String s1 = model.accounts.get(i).USERNAME;
                    String s2 = model.accounts.get(i).PASSWORD;
                    String s3 = model.accounts.get(i).NAME;
                    String s4 = model.accounts.get(i).CITY;
                    String s5 = model.accounts.get(i).MOBILE;
                    String s6 = model.accounts.get(i).IMAGE_URL;
                    if (s1.equals(username.getText().toString()) && s2.equals(password.getText().toString())) {
                        flag = 1;
                        pref.setUsername(s1);
                        pref.setPassword(s2);
                        pref.setName(s3);
                        pref.setCity(s4);
                        pref.setmobile(s5);
                        pref.setimage(s6);
                        Intent idx = new Intent(LoginActivity.this, DrawerActivity.class);
                        startActivity(idx);
                        finish();

                    } else if (i == 0 && flag == 0) {
                        Toast.makeText(LoginActivity.this, "Please Enter Correct Credentials", Toast.LENGTH_SHORT).show();
                    }
                    /*String s1 = model.fieldsArray.get(i).SN;
                    sn.add(s1);
                    String s2 = model.fieldsArray.get(i).NAME;
                    name.add(s2);
                    String s3 = model.fieldsArray.get(i).RATE;
                    rate.add(s3);
                    String s4 = model.fieldsArray.get(i).OVERVIEW;
                    overview.add(s4);
                    String s5 = model.fieldsArray.get(i).LOCATION;
                    location.add(s5);
                    String s6 = model.fieldsArray.get(i).LATTITUDE;
                    lattitude.add(s6);
                    String s7 = model.fieldsArray.get(i).LONGITUDE;
                    longitude.add(s7);
*/
                }

            }


            @Override
            public void onFailure(Call<Movie> call, Throwable t) {
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();
                Log.e("Failed", t.getMessage());
            }
        });

    }
}
