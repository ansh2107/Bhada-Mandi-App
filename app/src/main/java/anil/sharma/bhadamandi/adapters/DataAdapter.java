package anil.sharma.bhadamandi.adapters;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.RecyclerView;
import android.telephony.PhoneNumberUtils;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.TimeZone;

import anil.sharma.bhadamandi.R;
import anil.sharma.bhadamandi.activity.MessageActivity;
import anil.sharma.bhadamandi.activity.TransportMessageActivity;
import anil.sharma.bhadamandi.model.deleteDataModel;
import anil.sharma.bhadamandi.rest.API_Interface;
import anil.sharma.bhadamandi.utils.CircleTransform;
import anil.sharma.bhadamandi.utils.PrefManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class DataAdapter extends RecyclerView.Adapter<DataAdapter.ViewHolder> {
    public static ArrayList<String> date;
    public static int idx;
    public static ArrayList<String> from, to, vehicle, frieght, remark;
    private final ArrayList<String> time, mobile;
    private final ArrayList<String> sn;
    private final ArrayList<String> logo;
    private final ArrayList<String> title;
    PrefManager pref;
    private Context mContext;


    public DataAdapter(Context c, ArrayList<String> sn, ArrayList<String> from, ArrayList<String> name,
                       ArrayList<String> rate,
                       ArrayList<String> location,
                       ArrayList<String> longitude,
                       ArrayList<String> date,
                       ArrayList<String> time,
                       ArrayList<String> mobile,
                       ArrayList<String> logo,
                       ArrayList<String> title) {
        mContext = c;
        this.sn = sn;
        this.from = from;
        this.to = name;
        this.vehicle = rate;
        this.frieght = location;
        this.remark = longitude;
        this.date = date;
        this.time = time;
        this.mobile = mobile;
        this.logo = logo;
        this.title = title;
        pref = new PrefManager(c);
    }

    @Override
    public DataAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cards_in_grid, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DataAdapter.ViewHolder viewHolder, final int i) {

        //   viewHolder.name.setText(sn.get(i));
        viewHolder.from.setText(from.get(i));
        viewHolder.to.setText(to.get(i));
        viewHolder.frieght.setText(frieght.get(i));
        viewHolder.vehicle.setText(vehicle.get(i));
        viewHolder.remark.setText(remark.get(i));
        viewHolder.title.setText(title.get(i));
        //  viewHolder.date_1.setText(date.get(i));
        Picasso.with(mContext)
                .load(logo.get(i))
                .placeholder(R.drawable.logo5)
                .transform(new CircleTransform())
                .resize(150, 150)
                .into(viewHolder.logo);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        //  long time = sdf.parse("2016-01-24T16:00:00.000Z").getTime();
        long now = System.currentTimeMillis();

        CharSequence ago = DateUtils.getRelativeTimeSpanString(Long.valueOf(time.get(i)), now, DateUtils.MINUTE_IN_MILLIS);

        viewHolder.time_1.setText(ago);

        viewHolder.v.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                //  final int id_remove = i;
                if (pref.getValue().equals("0")) {
                    // fwd.setVisibility(View.INVISIBLE);
                } else {
                    AlertDialog.Builder alert = new AlertDialog.Builder(
                            mContext);
                    alert.setTitle("Alert!!");
                    alert.setMessage("Are you sure to delete record");
                    alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //do your work here

                            Toast.makeText(mContext, "SUCCESS", Toast.LENGTH_SHORT).show();
                            removeData(sn.get(i));
                            dialog.dismiss();

                        }
                    });
                    alert.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            dialog.dismiss();
                        }
                    });
                    alert.show();
                }


                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return from.size();
    }

    public void removeData(String sn) {


      /*  final ProgressDialog mProgressDialog = new ProgressDialog(mContext);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Loading...");
        mProgressDialog.show();*/


        API_Interface.Factory.getInstance().removeData(sn)
                .enqueue(new Callback<deleteDataModel>() {
                    @Override
                    public void onResponse(Call<deleteDataModel> call, Response<deleteDataModel> response) {
                        deleteDataModel model = response.body();
            /*    if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();
              */
                        if (model.responseCode1.equals("SUCCESS")) {
                            Toast.makeText(mContext, "sucess", Toast.LENGTH_SHORT).show();
                            //  vehicle.setSe("");

                        } else {
                            Toast.makeText(mContext, "fail", Toast.LENGTH_SHORT).show();

                        }
                    }

                    @Override
                    public void onFailure(Call<deleteDataModel> call, Throwable t) {
             /*   if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();
             */
                        Log.e("Failed", t.getMessage());
                    }
                });

    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public View v;
        ImageView fwd, msg, photo, mob, logo;
        private TextView from, to, vehicle, frieght, remark, date_1, time_1, title;

        public ViewHolder(View lv) {

            super(lv);
            this.v = lv;
            mob = (ImageView) lv.findViewById(R.id.call1);
            logo = (ImageView) lv.findViewById(R.id.logo1);
            msg = (ImageView) lv.findViewById(R.id.message1);
            photo = (ImageView) lv.findViewById(R.id.photo1);
            title = (TextView) lv.findViewById(R.id.title1);
            from = (TextView) lv.findViewById(R.id.from_1);
            time_1 = (TextView) lv.findViewById(R.id.time);
            to = (TextView) lv.findViewById(R.id.to_1);
            vehicle = (TextView) lv.findViewById(R.id.vehicle_1);
            frieght = (TextView) lv.findViewById(R.id.frieght_1);
            remark = (TextView) lv.findViewById(R.id.remark_1);
            // date_1 = (TextView) lv.findViewById(R.id.date);
            fwd = (ImageView) lv.findViewById(R.id.fwd);
            if (pref.getValue().equals("0")) {
                fwd.setVisibility(View.INVISIBLE);
            } else {

            }
            fwd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    idx = getPosition();
                    Intent i = new Intent(mContext, TransportMessageActivity.class);
                    mContext.startActivity(i);
                }
            });
            mob.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    idx = getPosition();
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:" + mobile.get(idx)));
                    if (ActivityCompat.checkSelfPermission(mContext, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    mContext.startActivity(callIntent);
                }
            });
            msg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    idx = getPosition();
                    Intent i = new Intent(mContext, MessageActivity.class);
                    mContext.startActivity(i);
                }
            });

            photo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    idx = getPosition();

                    Intent sendIntent = new Intent("android.intent.action.MAIN");
                    sendIntent.setComponent(new ComponentName("com.whatsapp", "com.whatsapp.Conversation"));
                    sendIntent.putExtra("jid", PhoneNumberUtils.stripSeparators(mobile.get(idx)) + "@s.whatsapp.net");//phone number without "+" prefix

                    mContext.startActivity(sendIntent);
                }
            });
        }

        @Override
        public void onClick(View v) {

        }


    }

}