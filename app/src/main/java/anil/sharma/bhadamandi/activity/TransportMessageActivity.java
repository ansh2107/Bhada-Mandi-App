package anil.sharma.bhadamandi.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import anil.sharma.bhadamandi.R;
import anil.sharma.bhadamandi.adapters.CustomAdapter;
import anil.sharma.bhadamandi.adapters.DataAdapter;
import anil.sharma.bhadamandi.utils.PrefManager;

public class TransportMessageActivity extends Activity {
    private static final int RESULT_PICK_CONTACT = 85500;
    PrefManager pref;
    CustomAdapter ca;
    ListView lv;
    Button but;
    ArrayList<String> name1, mobile, na, mo;
    PrefManager p;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transport_message);
        pref = new PrefManager(this);
        name1 = new ArrayList<String>();
        mobile = new ArrayList<String>();
        na = new ArrayList<String>();
        mo = new ArrayList<String>();
        if (pref.getArrayName() == null || pref.getArrayNumber() == null) {

        } else {
            na.addAll(pref.getArrayName());
            mo.addAll(pref.getArrayNumber());
        }

     /*   for(int i=0;i<pref.getArrayName().size();i++){
            na.add(pref.getArrayName().get(i));
            mo.add(pref.getArrayNumber().get(i));
        }*/
        // mo = pref.getArrayNumber();

        p = new PrefManager(this);
        lv = (ListView) findViewById(R.id.lv_message);
        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           final int pos, long id) {
                // TODO Auto-generated method stub

                AlertDialog.Builder alert = new AlertDialog.Builder(
                        TransportMessageActivity.this);
                alert.setTitle("Alert!!");
                alert.setMessage("Are you sure to delete record");
                alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //do your work here
                        Toast.makeText(TransportMessageActivity.this, "SUCCESS", Toast.LENGTH_SHORT).show();
                        name1.remove(pos);
                        mobile.remove(pos);
                        pref.setArrayName(name1);
                        pref.setArrayNumber(mobile);
                        ca = new CustomAdapter(TransportMessageActivity.this, name1, mobile);
                        lv.setAdapter(ca);
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


                return true;
            }
        });
        but = (Button) findViewById(R.id.send_message);
        if (na == null || mo == null) {

        } else {
            name1.addAll(na);
            mobile.addAll(mo);
            ca = new CustomAdapter(this, name1, mobile);
            lv.setAdapter(ca);
        }
        but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String s1 = "", s2 = "", s3 = "", s4 = "", s5 = "", s6 = "", ans = "";
                s1 = DataAdapter.from.get(DataAdapter.idx);
                s2 = DataAdapter.to.get(DataAdapter.idx);
                s3 = DataAdapter.vehicle.get(DataAdapter.idx);
                s4 = DataAdapter.frieght.get(DataAdapter.idx);
                s5 = DataAdapter.remark.get(DataAdapter.idx);
                s6 = DataAdapter.date.get(DataAdapter.idx);
                ans = "Name:" + p.getName() + "From: " + s1 + "   " + "To: " + s2 + "   " + "Vechile No.: " + s3 + "   " + "Frieght: " + s4 + "   " + "Remark: "
                        + s5 + "   " + "Date Scheduled: " + s6;
                for (int i = 0; i < mobile.size(); i++) {
                    SmsManager sms = SmsManager.getDefault();
                    ArrayList<String> parts = sms.divideMessage(ans);
                    sms.sendMultipartTextMessage(mobile.get(i), null, parts, null, null);
                    Toast.makeText(TransportMessageActivity.this, "Message Sent", Toast.LENGTH_SHORT).show();

                }


            }
        });
    }

    public void pickContact(View v) {
        Intent contactPickerIntent = new Intent(Intent.ACTION_PICK,
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
        startActivityForResult(contactPickerIntent, RESULT_PICK_CONTACT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // check whether the result is ok
        if (resultCode == RESULT_OK) {
            // Check for the request code, we might be usign multiple startActivityForReslut
            switch (requestCode) {
                case RESULT_PICK_CONTACT:
                    contactPicked(data);
                    break;
            }
        } else {
            Log.e("MainActivity", "Failed to pick contact");
        }
    }

    /**
     * Query the Uri and read contact details. Handle the picked contact data.
     *
     * @param data
     */
    private void contactPicked(Intent data) {
        Cursor cursor = null;
        try {
            String phoneNo = null;
            String name = null;
            // getData() method will have the Content Uri of the selected contact
            Uri uri = data.getData();
            //Query the content uri
            cursor = getContentResolver().query(uri, null, null, null, null);
            cursor.moveToFirst();
            // column index of the phone number
            int phoneIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
            // column index of the contact name
            int nameIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
            phoneNo = cursor.getString(phoneIndex);
            name = cursor.getString(nameIndex);
            // Set the value to the textviews
            name1.add(name);
            mobile.add(phoneNo);
            pref.setArrayName(name1);
            pref.setArrayNumber(mobile);
            ca = new CustomAdapter(this, name1, mobile);
            lv.setAdapter(ca);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}