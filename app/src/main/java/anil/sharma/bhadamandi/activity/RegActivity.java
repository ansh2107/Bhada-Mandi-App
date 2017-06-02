package anil.sharma.bhadamandi.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;

import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.util.UUID;

import anil.sharma.bhadamandi.R;
import anil.sharma.bhadamandi.adapters.GooglePlacesAutocompleteAdapter;
import anil.sharma.bhadamandi.model.postString;
import anil.sharma.bhadamandi.rest.API_Interface;
import anil.sharma.bhadamandi.utils.AlertDialogManager;
import anil.sharma.bhadamandi.utils.ConnectionDetector;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private static final String LOG_TAG = "Google Places Autocomplete";
    private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
    private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
    private static final String OUT_JSON = "/json";
    private static final String API_KEY = "AIzaSyDHHzr5BGDwo7ufUWP2ZGOtQPS5LQPAyTM";
    EditText user, name, pass, add, mobile, email, tax_no;
    Button submit;
    ImageView i;
    AutoCompleteTextView autoCompView, city;
    String path;
    ProgressDialog p;
    int i1;
    String imageName = "";
    String service_no;
    TextView iu;
    private int PICK_IMAGE_REQUEST = 1;
    private Bitmap bitmap;
    private Uri filePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg);
        iu = (TextView) findViewById(R.id.iu);
        i = (ImageView) findViewById(R.id.upload_image);
        user = (EditText) findViewById(R.id.reg_user);
        tax_no = (EditText) findViewById(R.id.service_tax);
        email = (EditText) findViewById(R.id.email);
        mobile = (EditText) findViewById(R.id.reg_mob);
        name = (EditText) findViewById(R.id.reg_name);
        pass = (EditText) findViewById(R.id.reg_pass);
        add = (EditText) findViewById(R.id.reg_add);
        autoCompView = (AutoCompleteTextView) findViewById(R.id.city_reg);

        autoCompView.setAdapter(new GooglePlacesAutocompleteAdapter(this, R.layout.list_item));
        autoCompView.setOnItemClickListener(this);
        submit = (Button) findViewById(R.id.register);

        iu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // i.setVisibility(View.INVISIBLE);
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialogManager alert = new AlertDialogManager();
                ConnectionDetector cd = new ConnectionDetector(getApplicationContext());

                // Check if Internet present
                if (!cd.isConnectingToInternet()) {
                    // Internet Connection is not present
                    alert.showAlertDialog(RegActivity.this,
                            "Internet Connection Error",
                            "Please connect to working Internet connection", false);
                    // stop executing code by return
                    return;
                }

                if (user.getText().toString().equals("") ||
                        name.getText().toString().equals("") ||
                        pass.getText().toString().equals("") ||
                        mobile.getText().toString().equals("") ||
                        email.getText().toString().equals("") ||
                        add.getText().toString().equals("") ||
                        autoCompView.getText().toString().equals("") ||
                        tax_no.getText().toString().equals("")
                        ) {
                    Toast.makeText(RegActivity.this, "Please fill the star marked fields.", Toast.LENGTH_LONG).show();

                }
                /*else if(filePath == null)
                {
                    Toast.makeText(RegActivity.this, "Please select logo for your company." , Toast.LENGTH_LONG).show();

                }*/
                else if (mobile.getText().toString().length() < 10) {
                    Toast.makeText(RegActivity.this, "Please enter valid mobile number!", Toast.LENGTH_LONG).show();
                } else if (tax_no.getText().toString().length() < 12) {
                    Toast.makeText(RegActivity.this, "Please enter valid service tax number!", Toast.LENGTH_LONG).show();
                } else if (name.getText().toString().contains("BROKER") || name.getText().toString().contains("COMMISSION") || name.getText().toString().contains("AGENT"))
                    Toast.makeText(RegActivity.this, "Please check if your transport name contains charcters COMMISSION , AGENT , BROKER remove them.", Toast.LENGTH_LONG).show();
                else
                    uploadMultipart();
            }
        });
    }

    //handling the image chooser activity result
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                i.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //method to get the file path from uri
    public String getPath(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();

        cursor = getContentResolver().query(
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();

        File f = new File(path);

        imageName = f.getName();
        return path;
    }

    public void testdata() {
        String url_path;
        if (!imageName.equals("")) {
            url_path = "http://anshuli.webhostingforstudents.com/mytransport/uploads/" + imageName;
        } else {
            url_path = "http://anshuli.webhostingforstudents.com/mytransport/uploads/logo5";
        }

        /*final Calendar c = Calendar.getInstance(TimeZone.getTimeZone("GMT+1:00"));
        int  yy = c.get(Calendar.YEAR);
        int  mm = c.get(Calendar.MONTH);
        int  dd = c.get(Calendar.DAY_OF_MONTH);


        Date currentLocalTime = c.getTime();
        DateFormat date = new SimpleDateFormat("HH:mm a");
        date.setTimeZone(TimeZone.getTimeZone("GMT+1:00"));

        String localTime = date.format(currentLocalTime);
        String date1 = ( new StringBuilder().append(yy).append("-").append(mm + 1).append("-")).append(dd).toString();*/
        //  String te=text.getText().toString();
        String s = autoCompView.getText().toString();
        final ProgressDialog mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Loading...");
        mProgressDialog.show();
        API_Interface.Factory.getInstance().postData1(
                user.getText().toString(),
                name.getText().toString(),
                pass.getText().toString(),
                mobile.getText().toString(),
                email.getText().toString(),
                add.getText().toString(),
                autoCompView.getText().toString(),
                tax_no.getText().toString(),
                url_path).enqueue(new Callback<postString>() {


            @Override
            public void onResponse(Call<postString> call, Response<postString> response) {
                postString model = response.body();
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();
                if (model.responseCode.equals("SUCCESS")) {
                    Toast.makeText(RegActivity.this, "sucess", Toast.LENGTH_SHORT).show();
                    email.setText("");
                    user.setText("");
                    name.setText("");
                    pass.setText("");
                    add.setText("");
                    autoCompView.setText("");
                    mobile.setText("");

                    Intent i = new Intent(RegActivity.this, LoginActivity.class);
                    startActivity(i);
                    finish();
                } else {
                    Toast.makeText(RegActivity.this, "fail", Toast.LENGTH_SHORT).show();

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
    public void onItemClick(AdapterView adapterView, View view, int position, long id) {
        String str = (String) adapterView.getItemAtPosition(position);
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }

    public void uploadMultipart() {
        //  name = editText.getText().toString().trim();

        Random r = new Random();
        i1 = r.nextInt(100000 - 1) + 1;
        if (filePath != null) {
            path = getPath(filePath);
            new UploadImage().execute();
        } else {
            //    Toast.makeText(this,"Please Select Image",Toast.LENGTH_SHORT ).show();
            testdata();
        }

    }

    class UploadImage extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {// handled by UI threads

            p = new ProgressDialog(RegActivity.this);
            p.setMessage("Loading...");
            p.show();
        }

        @Override
        protected String doInBackground(Void... params) {

            //getting name for the image


            //getting the actual path of the image


            //Uploading code
            try {

                String uploadId = UUID.randomUUID().toString();

                //   String service_no = tax_no.getText().toString();
                String upload_url = "http://anshuli.webhostingforstudents.com/mytransport/image_upload.php";
                //Creating a multi part request
                new MultipartUploadRequest(RegActivity.this, uploadId, upload_url)
                        .addFileToUpload(path, "image") //Adding file
                        .addParameter(service_no, "mob_no")
                        .setNotificationConfig(new UploadNotificationConfig())
                        .setMaxRetries(2)
                        .startUpload(); //Starting the upload


            } catch (Exception exc) {
                Toast.makeText(RegActivity.this, exc.getMessage(), Toast.LENGTH_SHORT).show();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            p.dismiss();
            testdata();

        }
    }
}
