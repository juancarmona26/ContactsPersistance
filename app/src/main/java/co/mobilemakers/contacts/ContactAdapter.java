package co.mobilemakers.contacts;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Juan on 05/02/2015.
 */
public class ContactAdapter extends ArrayAdapter<Contact> {

    private Context mContext;
    private List<Contact> mContacts;

    public ContactAdapter(Context context,List<Contact> contacts){
        super(context, R.layout.contact_list_entry, contacts );
        mContext = context;
        mContacts = contacts;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = displayView(position, convertView, parent);
        return rowView;
    }

    private View displayView(int position, View convertView, ViewGroup parent) {
        View rowView;
        if(convertView != null) {
            rowView = convertView;
        } else {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(R.layout.contact_list_entry, parent, false);
        }
        if(rowView != null) {
            TextView textViewTaskTitle = (TextView) rowView.findViewById(R.id.text_view_contact_nickname);
            textViewTaskTitle.setText(mContacts.get(position).getNickname());
            ImageView imageView = (ImageView) rowView.findViewById(R.id.image_view_contact);
            imageView.setImageResource(R.drawable.ic_launcher);
        }
        return rowView;
    }
}
