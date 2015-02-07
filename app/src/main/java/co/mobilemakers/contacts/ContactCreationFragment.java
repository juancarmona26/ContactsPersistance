package co.mobilemakers.contacts;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
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
    private EditText mEditTextFirstName;
    private EditText mEditTextLastName;
    private EditText mEditTextUserNick;
    private ImageButton mImageButtonSelector;
    private Button mButtonAddContact;
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

    View.OnClickListener clickListener = new View.OnClickListener(){

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.button_add_contact:
                    returnContactProperties();
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

    private void returnContactProperties() {
        Activity activity = getActivity();
        Intent intent = new Intent();

        intent.putExtra("firstName", mEditTextFirstName.getText().toString());
        intent.putExtra("lastName", mEditTextLastName.getText().toString());
        intent.putExtra("nickname", mEditTextUserNick.getText().toString());
        intent.putExtra("imageUri", fileUri != null ? fileUri.getPath(): "");

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
}
