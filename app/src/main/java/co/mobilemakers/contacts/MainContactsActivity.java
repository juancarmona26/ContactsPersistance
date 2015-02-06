package co.mobilemakers.contacts;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class MainContactsActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_contacts);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_contacts, menu);
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
    public static class PlaceholderFragment extends ListFragment {

        private static final int REQUEST_CODE_CREAT_CONTACT = 0 ;
        private ArrayAdapter<Contact> mArrayAdapter;

        public PlaceholderFragment() {
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setHasOptionsMenu(true);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_contacts_list, container, false);
            return rootView;
        }

        @Override
        public void onActivityCreated(@Nullable Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
            perpareListView();
        }

        private void perpareListView() {
            List<Contact> contacts = new ArrayList<>();
            mArrayAdapter  = new ContactAdapter(getActivity(), contacts);
            setListAdapter(mArrayAdapter);
            getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Contact contact = (Contact) parent.getItemAtPosition(position);
                    String message = String.format(getString(R.string.message_received),contact.getNickname() );
                    Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                }
            });
        }

        @Override
        public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
            inflater.inflate(R.menu.menu_contacts, menu);
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            boolean handle = false;

            switch (item.getItemId()) {
                case R.id.add_toolbar_button:
                    goToCreateContactActivity();
                    handle = true;
                break;
            }

            if(!handle) {
                return super.onOptionsItemSelected(item);
            }

            return handle;
        }

        private void goToCreateContactActivity() {
            Intent intent = new Intent(getActivity(), ContactCreationActivity.class);
            startActivityForResult(intent, REQUEST_CODE_CREAT_CONTACT);
        }

        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            if(resultCode == RESULT_OK) {
                if (data != null) {
                    Contact contact = new Contact(data.getExtras().getString("nickname"), data.getExtras().getString("imageUrl"));
                    mArrayAdapter.add(contact);

                    Toast.makeText(getActivity(), contact.toString(), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getActivity(), "Anything to add, sorry :(", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getActivity(), "Anything to add, sorry :(", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
