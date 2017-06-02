package anil.sharma.bhadamandi.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

import anil.sharma.bhadamandi.R;
import anil.sharma.bhadamandi.adapters.DataAdapterNames;
import anil.sharma.bhadamandi.adapters.GooglePlacesAutocompleteAdapter;

public class MessageActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    EditText vech, wt, frieght, adv, bal, pan, mobno1, mobno2, broker_name, rate, bank_name, account_holder_name, account_number, ifsc_code;
    AutoCompleteTextView from, to;
    String s1 = "", s2 = "", s3 = "", s4 = "", s5 = "", s6 = "", s7 = "", s8 = "", s9 = "", s10 = "", s11 = "", s12 = "", s13 = "", s14 = "", s15 = "", s16 = "", ans = "";
    Button but;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        from = (AutoCompleteTextView) findViewById(R.id.msg_from);
        to = (AutoCompleteTextView) findViewById(R.id.msg_to);


        from.setAdapter(new GooglePlacesAutocompleteAdapter(this, R.layout.list_item));
        from.setOnItemClickListener(this);

        to.setAdapter(new GooglePlacesAutocompleteAdapter(this, R.layout.list_item));
        to.setOnItemClickListener(this);
        vech = (EditText) findViewById(R.id.msg_vechile);
        wt = (EditText) findViewById(R.id.msg_wt);
        frieght = (EditText) findViewById(R.id.msg_fri);
        adv = (EditText) findViewById(R.id.msg_adv);
        bal = (EditText) findViewById(R.id.msg_bal);
        pan = (EditText) findViewById(R.id.msg_pan);
        mobno1 = (EditText) findViewById(R.id.msg_mob1);
        mobno2 = (EditText) findViewById(R.id.msg_mob2);

        broker_name = (EditText) findViewById(R.id.broker_name);
        rate = (EditText) findViewById(R.id.rate);

        bank_name = (EditText) findViewById(R.id.bank_name);
        account_holder_name = (EditText) findViewById(R.id.account_holder_name);
        account_number = (EditText) findViewById(R.id.account_number);
        ifsc_code = (EditText) findViewById(R.id.ifsc_code);


        but = (Button) findViewById(R.id.message_act);
        but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                s1 = from.getText().toString();
                s2 = to.getText().toString();
                s3 = vech.getText().toString();
                s4 = wt.getText().toString();
                s5 = frieght.getText().toString();
                s6 = adv.getText().toString();
                s7 = bal.getText().toString();
                s8 = pan.getText().toString();
                s9 = mobno1.getText().toString();
                s10 = mobno2.getText().toString();


                s11 = broker_name.getText().toString();
                s12 = rate.getText().toString();
                s13 = bank_name.getText().toString();
                s14 = account_holder_name.getText().toString();
                s15 = account_number.getText().toString();
                s16 = ifsc_code.getText().toString();

                ans = "Broker Name : " + s11
                        + "   " + "From: " + s1
                        + "   " + "To: " + s2
                        + "   " + "Vechile No.: " + s3
                        + "   " + "Weight/Quantity: " + s4
                        + "   " + "Rate: " + s12
                        + "   " + "Frieght: " + s5
                        + "   " + "Advance: " + s6
                        + "   " + "Balance: " + s7
                        + "   " + "Owner Pan No.: " + s8
                        + "   " + "Owner Mobile No.: " + s9
                        + "   " + "Owner Mobile No.: " + s10

                        + "   " + "BANK DETAILS - "
                        + "   " + "Bank Name : " + s13
                        + "   " + "Account Holder Name : " + s14
                        + "   " + "Account Number : " + s15
                        + "   " + "IFSC Code : " + s16;


                SmsManager sms = SmsManager.getDefault();
                ArrayList<String> parts = sms.divideMessage(ans);
                sms.sendMultipartTextMessage(DataAdapterNames.mobile.get(DataAdapterNames.idx), null, parts, null, null);
                Toast.makeText(MessageActivity.this, "Message Sent", Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String str = (String) parent.getItemAtPosition(position);
        String str1 = (String) parent.getItemAtPosition(position);
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }
}
