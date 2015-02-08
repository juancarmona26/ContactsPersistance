package co.mobilemakers.contacts;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.EnumSet;

public class ContactCreationFragment extends Fragment {

    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1;
    private static final String LOG_TAG = ContactCreationFragment.class.getSimpleName();
    private Uri fileUri;
    private int requestCode;
    private Contact mContact;
    private EditText mEditTextFirstName;
    private EditText mEditTextLastName;
    private EditText mEditTextUserNick;
    private ImageButton mImageButtonSelector;
    private Button mButtonAddContact;
    private ActionBar actionBar;
    private enum FilledFields {
        FirstName,
        LastName,
    }

    public ContactCreationFragment() {
    }

    EnumSet<FilledFields> mFilledFieldsEnumSet = EnumSet.noneOf(FilledFields.class);

    public class ContentWatcher implements TextWatcher {
        FilledFields mField;

        public ContentWatcher(FilledFields field) {
            mField = field;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (TextUtils.isEmpty(s)) {
                mFilledFieldsEnumSet.remove(mField);
            } else {
                mFilledFieldsEnumSet.add(mField);
            }
            changeButtonStatus();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null){
            requestCode = getArguments().getInt("requestCode");
            mContact = getArguments().getParcelable("contact");
        }
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_contact_creation, container, false);
        prepareViews(rootView);
        setEditTextEvents();
        setButtonEvent();
        return rootView;
        }

    private void setEditTextEvents() {

        mEditTextFirstName.addTextChangedListener(new ContentWatcher(FilledFields.FirstName));
        mEditTextLastName.addTextChangedListener(new ContentWatcher(FilledFields.LastName));

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(requestCode == ContactsListFragment.REQUEST_CODE_EDIT_CONTACT) {
            mEditTextFirstName.setText(mContact.getFirstName());
            mEditTextLastName.setText(mContact.getLastName());
            mEditTextUserNick.setText(mContact.getNickname());
            if (!mContact.getImageUrl().equals("") && mContact.getImageUrl() != null)
                mImageButtonSelector.setImageBitmap(Bitmap.createScaledBitmap(prepareBitmap(mContact.getImageUrl()), 500, 500, false));
            mButtonAddContact.setText(R.string.button_edit_contact);
        }
    }

    private Bitmap prepareBitmap(String imagePath) {
        File imageFile = new File(mContact.getImageUrl());
        fileUri = Uri.fromFile(imageFile);
        Bitmap image = null;
        try {
            image = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), fileUri);
        } catch (IOException e) {
            Log.d(LOG_TAG, "Something happens to image", e);
        }
        return image;
    }

    View.OnClickListener clickListener = new View.OnClickListener(){

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.button_add_contact:
                    returnResultContact();
                break;

                case R.id.image_button_selector:
                    cameraIntent();
                break;

            }
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {

                    Log.d(LOG_TAG, "Ok, si pasa");

                    try {
                        Bitmap image = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), fileUri);
                        mImageButtonSelector.setImageBitmap(Bitmap.createScaledBitmap(image, 500, 500, false));
                    } catch (IOException e) {
                        Log.d(LOG_TAG, "Error en bitmap",e);
                    }

            } else if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(getActivity(), "Any image was taken", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getActivity(), "There is an error on your camera application", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
    }

    private void setButtonEvent() {
        mButtonAddContact.setOnClickListener(clickListener);
        mImageButtonSelector.setOnClickListener(clickListener);
    }

    private void returnResultContact() {
        Activity activity = getActivity();
        Intent intent = new Intent();
        Contact contact = new Contact();
        if(requestCode == ContactsListFragment.REQUEST_CODE_EDIT_CONTACT) {
            contact.setId(mContact.getId());
        }
        contact.setFirstName(mEditTextFirstName.getText().toString());
        contact.setLastName(mEditTextLastName.getText().toString());
        contact.setNickname(mEditTextUserNick.getText().toString());
        contact.setImageUrl(fileUri != null ? fileUri.getPath(): "");
        intent.putExtra("contact", contact );
        intent.putExtra("delete", 0);

        activity.setResult(Activity.RESULT_OK, intent);
        activity.finish();
    }

    private void prepareViews(View rootView) {
        mEditTextFirstName = (EditText) rootView.findViewById(R.id.edit_text_first_name);
        mEditTextLastName = (EditText) rootView.findViewById(R.id.edit_text_last_name);
        mEditTextUserNick = (EditText) rootView.findViewById(R.id.edit_user_nickname);
        mImageButtonSelector = (ImageButton) rootView.findViewById(R.id.image_button_selector);
        mButtonAddContact = (Button) rootView.findViewById(R.id.button_add_contact);
        mButtonAddContact.setEnabled(false);


    }

    private void changeButtonStatus () {
        if (mFilledFieldsEnumSet.containsAll(EnumSet.allOf(FilledFields.class))) {
            mButtonAddContact.setEnabled(true);
        } else {
            mButtonAddContact.setEnabled(false);
        }
    }

    private static Uri getOutputMediaFileUri(int type){
        return Uri.fromFile(getOutputMediaFile(type));
    }

    private static File getOutputMediaFile(int type){
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), LOG_TAG);
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                Log.d(LOG_TAG, "Failed to create directory");
                return null;
            }
        }
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE){
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "IMG_"+ timeStamp + ".jpg");
        }  else {
            return null;
        }

        return mediaFile;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_contact_edition, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean handle = false;

        switch (item.getItemId()) {
            case R.id.menu_delete_contact:
                Intent intent = new Intent();
                intent.putExtra("delete", 1);
                intent.putExtra("contact", mContact);
                Activity activity = getActivity();

                activity.setResult(Activity.RESULT_OK, intent);
                activity.finish();

                handle = true;
            break;
        }

        if(!handle)
            handle = super.onOptionsItemSelected(item);

        return handle;
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        if(requestCode == ContactsListFragment.REQUEST_CODE_CREATE_CONTACT)
            menu.getItem(0).setEnabled(false);
    }
}
