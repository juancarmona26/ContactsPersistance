package co.mobilemakers.contacts;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by Juan on 05/02/2015.
 */
public class ContactAdapter extends ArrayAdapter<Contact> {

    private static final String LOG_TAG = ContactAdapter.class.getSimpleName();
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
            TextView textViewTaskTitle = (TextView) rowView.findViewById(R.id.text_view_contact_name);
            textViewTaskTitle.setText(mContacts.get(position).getFirstName());
            File imageFile = new File(mContacts.get(position).getImageUrl());
            Uri imageUri = Uri.fromFile(imageFile);
            Bitmap image = null;
            try {
                image = MediaStore.Images.Media.getBitmap(mContext.getContentResolver(), imageUri);
            } catch (IOException e) {
                Log.d(LOG_TAG, "Somthing happens to image", e);
            }
            ImageView imageView = (ImageView) rowView.findViewById(R.id.image_view_contact);
            if(image != null ) {

                imageView.setImageBitmap(Bitmap.createScaledBitmap(image, 100, 100, false));
            } else imageView.setImageResource(R.drawable.image_default_globe);
        }
        return rowView;
    }
}
