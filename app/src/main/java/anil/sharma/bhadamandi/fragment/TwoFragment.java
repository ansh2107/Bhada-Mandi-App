package anil.sharma.bhadamandi.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import anil.sharma.bhadamandi.R;
import anil.sharma.bhadamandi.adapters.DataAdapter;
import anil.sharma.bhadamandi.model.getDataModel;
import anil.sharma.bhadamandi.rest.API_Interface;
import anil.sharma.bhadamandi.utils.AlertDialogManager;
import anil.sharma.bhadamandi.utils.ConnectionDetector;
import anil.sharma.bhadamandi.utils.PrefManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class TwoFragment extends Fragment {

    PrefManager pref;
    ArrayList<String> from;
    ArrayList<String> to;
    ArrayList<String> vehicle;
    ArrayList<String> frieght;
    ArrayList<String> remark, date, time, s_no, mobile, logo, title;
    // private RecyclerView recyclerView;
    RecyclerView lv;
    DataAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    //private Handler handler = new Handler();


    public TwoFragment() {
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
        View v = inflater.inflate(R.layout.fragment_two, container, false);

        pref = new PrefManager(getActivity());
        swipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipe_refresh_layout);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {

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
                testData();
            }
        });


        lv = (RecyclerView) v.findViewById(R.id.lvlist);
        AlertDialogManager alert = new AlertDialogManager();
        ConnectionDetector cd = new ConnectionDetector(getActivity());

        // Check if Internet present
        if (!cd.isConnectingToInternet()) {
            // Internet Connection is not present
            alert.showAlertDialog(getActivity(),
                    "Internet Connection Error",
                    "Please connect to working Internet connection", false);
            // stop executing code by return
            return v;
        }
        testData();


        return v;
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
        logo = new ArrayList<String>();
        title = new ArrayList<String>();


        final ProgressDialog mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Loading...");
        mProgressDialog.show();
        API_Interface.Factory.getInstance().getDataList().enqueue(new Callback<getDataModel>() {


            @Override
            public void onResponse(Call<getDataModel> call, Response<getDataModel> response) {
                getDataModel model = response.body();

                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();
                for (int i = model.fieldsArray.size() - 1; i >= 0; i--) {
                    String s0 = null, s13 = null, s15 = null, s14 = null, s1 = null, s2 = null, s3 = null, s5 = null, s7 = null, s8 = null, s9 = null, s10 = null;

                    s0 = model.fieldsArray.get(i).S_NO;
                    s1 = model.fieldsArray.get(i).FROM1;
                    s2 = model.fieldsArray.get(i).TO1;
                    s3 = model.fieldsArray.get(i).VEHICLE;
                    s5 = model.fieldsArray.get(i).FRIEGHT;
                    s7 = model.fieldsArray.get(i).REMARK;
                    s8 = model.fieldsArray.get(i).DATE;
                    s9 = model.fieldsArray.get(i).USERNAME;
                    s10 = model.fieldsArray.get(i).TIME;
                    s13 = model.fieldsArray.get(i).MOBILE;

                    s14 = model.fieldsArray.get(i).IMAGE_URL;
                    s15 = model.fieldsArray.get(i).NAME;
                    if (s9.equals(pref.getUsername())) {

                        s_no.add(s0);

                        from.add(s1);

                        to.add(s2);

                        vehicle.add(s3);


                        frieght.add(s5);


                        remark.add(s7);

                        date.add(s8);

                        time.add(s10);
                        mobile.add(s13);
                        logo.add(s14);
                        title.add(s15);
                    }


                }

                pref.setValue("1");
                lv.setHasFixedSize(true);

                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
                lv.setLayoutManager(layoutManager);
                RecyclerView.Adapter adapter = new DataAdapter(getActivity(), s_no,
                        from, to, vehicle, frieght, remark, date, time, mobile, logo, title);
                lv.setAdapter(adapter);


                swipeRefreshLayout.setRefreshing(false);

            }


            @Override
            public void onFailure(Call<getDataModel> call, Throwable t) {
                swipeRefreshLayout.setRefreshing(false);
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();
                Log.e("Failed", t.getMessage());
            }

        });

    }

}
