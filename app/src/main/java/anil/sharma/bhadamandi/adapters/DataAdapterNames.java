package anil.sharma.bhadamandi.adapters;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.RecyclerView;
import android.telephony.PhoneNumberUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import anil.sharma.bhadamandi.R;
import anil.sharma.bhadamandi.activity.BrokerNameActivity;
import anil.sharma.bhadamandi.activity.MessageActivity;
import anil.sharma.bhadamandi.utils.CircleTransform;
import anil.sharma.bhadamandi.utils.PrefManager;


public class DataAdapterNames extends RecyclerView.Adapter<DataAdapterNames.ViewHolder> {

    public static ArrayList<String> name, mobile;
    public static int idx = 0;
    private final ArrayList<String> img;
    PrefManager pref;
    private Context mContext;


    public DataAdapterNames(Context c, ArrayList<String> sn, ArrayList<String> mob, ArrayList<String> img) {
        mContext = c;

        this.name = sn;
        this.img = img;
        this.mobile = mob;
        pref = new PrefManager(c);
    }

    @Override
    public DataAdapterNames.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_in_grid_name, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DataAdapterNames.ViewHolder viewHolder, int i) {

        //   viewHolder.name.setText(sn.get(i));
        viewHolder.name1.setText(name.get(i));
        Picasso.with(mContext)
                .load(img.get(i))
                .placeholder(R.drawable.logo5)
                .transform(new CircleTransform())
                .resize(150, 150)
                .into(viewHolder.logo);
    }

    @Override
    public int getItemCount() {
        return name.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView name1;
        private ImageView mob, msg;
        private ImageView photo, logo;

        public ViewHolder(View lv) {
            super(lv);


            name1 = (TextView) lv.findViewById(R.id.title_name);
            mob = (ImageView) lv.findViewById(R.id.call);
            logo = (ImageView) lv.findViewById(R.id.logo);
            msg = (ImageView) lv.findViewById(R.id.message);
            photo = (ImageView) lv.findViewById(R.id.photo);

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
            lv.setOnClickListener(this);
        }

        @Override
        public void onClick(View lv) {
            idx = getPosition();
            Intent i = new Intent(mContext, BrokerNameActivity.class);
            mContext.startActivity(i);
        }


    }


}