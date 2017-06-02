package anil.sharma.bhadamandi.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import anil.sharma.bhadamandi.R;

public class CustomAdapter extends BaseAdapter {

    Context mContext;
    ArrayList<String> name, mobile;
    private int lastPosition = -1;

    public CustomAdapter(Context context, ArrayList<String> name, ArrayList<String> mobile) {

        this.mContext = context;
        this.mobile = mobile;
        this.name = name;
    }

    @Override
    public int getCount() {
        return name.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.listview_card, parent, false);
            viewHolder.txtName = (TextView) convertView.findViewById(R.id.trans_name);
            viewHolder.txtNumber = (TextView) convertView.findViewById(R.id.trans_mob);

            result = convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result = convertView;
        }


        viewHolder.txtName.setText(name.get(position));
        viewHolder.txtNumber.setText(mobile.get(position));

        // Return the completed view to render on screen
        return convertView;
    }

    // View lookup cache
    private static class ViewHolder {
        TextView txtName;
        TextView txtNumber;
    }
}