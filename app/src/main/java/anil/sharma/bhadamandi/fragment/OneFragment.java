package anil.sharma.bhadamandi.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import anil.sharma.bhadamandi.R;
import anil.sharma.bhadamandi.adapters.GooglePlacesAutocompleteAdapter;
import anil.sharma.bhadamandi.model.postDataModel;
import anil.sharma.bhadamandi.rest.API_Interface;
import anil.sharma.bhadamandi.utils.AlertDialogManager;
import anil.sharma.bhadamandi.utils.ConnectionDetector;
import anil.sharma.bhadamandi.utils.PrefManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class OneFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    EditText frieght, remark;
    PrefManager pref;
    AutoCompleteTextView from, to;
    Button submit;
    Spinner vehicle;
    long time_2;
    String f, r;

    public OneFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_one, container, false);
        pref = new PrefManager(getActivity());
        //Get it in an array of strings
       /* String[] vehicles = getResources().getStringArray(R.array.vehicles);
        List<String> monthsList = new ArrayList<String>();
        monthsList = Arrays.asList(vehicles);
*/


        from = (AutoCompleteTextView) v.findViewById(R.id.from);
        to = (AutoCompleteTextView) v.findViewById(R.id.to);
        from.setAdapter(new GooglePlacesAutocompleteAdapter(getActivity(), R.layout.list_item));
        // from.setOnItemClickListener((AdapterView.OnItemClickListener) getActivity());

        to.setAdapter(new GooglePlacesAutocompleteAdapter(getActivity(), R.layout.list_item));
        //  to.setOnItemClickListener((AdapterView.OnItemClickListener) getActivity());

        vehicle = (Spinner) v.findViewById(R.id.vehicle);


        frieght = (EditText) v.findViewById(R.id.frieght);
        remark = (EditText) v.findViewById(R.id.Remark);

        submit = (Button) v.findViewById(R.id.button);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialogManager alert = new AlertDialogManager();
                ConnectionDetector cd = new ConnectionDetector(getActivity());

                // Check if Internet present
                if (!cd.isConnectingToInternet()) {
                    // Internet Connection is not present
                    alert.showAlertDialog(getActivity(),
                            "Internet Connection Error",
                            "Please connect to working Internet connection", false);
                    // stop executing code by return
                    return;
                }

                if (from.getText().toString().equals("") || to.getText().toString().equals("") || vehicle.getSelectedItem().equals("* Choose your Vehicle"))
                    Toast.makeText(getActivity(), "Please fill the star marked fields.", Toast.LENGTH_LONG).show();
                else
                    testdata();
            }
        });

        return v;
    }

    public void testdata() {
        //date
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Calendar cal = GregorianCalendar.getInstance();
        cal.setTime(new Date());
        Date daysBeforeDate = cal.getTime();
        String date1 = dateFormat.format(daysBeforeDate);

        //time
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        long now = System.currentTimeMillis();
        String localTime = String.valueOf(now);


        final ProgressDialog mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Loading...");
        mProgressDialog.show();

        if (frieght.getText().toString().equals("")) {
            f = "N/A";
        } else {
            f = frieght.getText().toString();
        }
        if (remark.getText().toString().equals("")) {
            r = "N/A";
        } else {
            r = remark.getText().toString();
        }


        API_Interface.Factory.getInstance().postData(
                from.getText().toString(),
                to.getText().toString(),
                vehicle.getSelectedItem().toString(),
                f,
                r,
                date1, pref.getName(),
                pref.getUsername(),
                pref.getCity(),
                pref.getmobile(),
                pref.getImage(),
                localTime).enqueue(new Callback<postDataModel>() {
            @Override
            public void onResponse(Call<postDataModel> call, Response<postDataModel> response) {
                postDataModel model = response.body();
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();
                if (model.responseCode.equals("SUCCESS")) {
                    Toast.makeText(getActivity(), "sucess", Toast.LENGTH_SHORT).show();
                    //  vehicle.setSe("");
                    from.setText("");
                    to.setText("");
                    frieght.setText("");
                    remark.setText("");

                } else {
                    Toast.makeText(getActivity(), "fail", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<postDataModel> call, Throwable t) {
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();
                Log.e("Failed", t.getMessage());
            }
        });

    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {
        // On selecting a spinner item
        String item = parent.getItemAtPosition(i).toString();
        String str = (String) parent.getItemAtPosition(i);
        String str1 = (String) parent.getItemAtPosition(i);
        // Showing selected spinner item
        Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
