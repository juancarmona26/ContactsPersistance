package co.mobilemakers.contacts;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.util.EnumSet;


public class ContactCreationActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_creation);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_contact_creation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        private enum FilledFields {
            NicknameFilled,
            ImageUrlFilled,
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

        private EditText mEditTextUserNick;
        private EditText mEditTextImageUrl;
        private Button mButtonAddContact;
        public PlaceholderFragment() {
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
            mEditTextUserNick.addTextChangedListener(new ContentWatcher(FilledFields.NicknameFilled));
            mEditTextImageUrl.addTextChangedListener(new ContentWatcher(FilledFields.ImageUrlFilled));
        }

        private void setButtonEvent() {
            mButtonAddContact.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    returnContactProperties();
                }

                 });
        }

        private void returnContactProperties() {
            Activity activity = getActivity();
            Intent intent = new Intent();
            intent.putExtra("nickname", mEditTextUserNick.getText().toString());
            intent.putExtra("imageUrl", mEditTextImageUrl.getText().toString());
            activity.setResult(Activity.RESULT_OK, intent);
            activity.finish();
        }

        private void prepareViews(View rootView) {
            mEditTextUserNick = (EditText) rootView.findViewById(R.id.edit_user_nickname);
            mEditTextImageUrl = (EditText) rootView.findViewById(R.id.edit_text_image_url);
            mButtonAddContact = (Button) rootView.findViewById(R.id.button_add_contact);
        }

        private void changeButtonStatus () {
            if (mFilledFieldsEnumSet.containsAll(EnumSet.allOf(FilledFields.class))) {
                mButtonAddContact.setEnabled(true);
            } else {
                mButtonAddContact.setEnabled(false);
            }
        }
    }
}
