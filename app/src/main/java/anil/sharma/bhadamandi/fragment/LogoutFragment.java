package anil.sharma.bhadamandi.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import anil.sharma.bhadamandi.R;
import anil.sharma.bhadamandi.activity.MainActivity;
import anil.sharma.bhadamandi.utils.PrefManager;

/**
 * Created by Ratan on 7/29/2015.
 */
public class LogoutFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.sent_layout, null);

        Button logout = (Button) v.findViewById(R.id.logout);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new PrefManager(getActivity()).logout();
                Intent i = new Intent(getActivity(), MainActivity.class);
                startActivity(i);
                getActivity().finish();
            }
        });


        return v;

    }
}
