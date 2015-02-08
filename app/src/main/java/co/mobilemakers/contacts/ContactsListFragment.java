package co.mobilemakers.contacts;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ContactsListFragment extends ListFragment {

    private static final String LOG_TAG = ContactsListFragment.class.getSimpleName();
    public static final int REQUEST_CODE_CREATE_CONTACT = 0 ;
    public static final int REQUEST_CODE_EDIT_CONTACT = 1;
    private ArrayAdapter<Contact> mArrayAdapter;
    private DatabaseHelper mDBHelper;

    public ContactsListFragment() {
    }

    public DatabaseHelper getDBHelper() {
        if(mDBHelper == null) {
            mDBHelper = OpenHelperManager.getHelper(getActivity(), DatabaseHelper.class);
        }
       return mDBHelper;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_contacts_list, container, false);
    }

    private List<Contact> retrieveContacts() {
        List<Contact>contacts = null;
        Dao<Contact,Integer> contactDao;
        try {
            contactDao = getDBHelper().getContactDao();
            contacts = contactDao.queryForAll();
        } catch (SQLException e) {
            Log.e(LOG_TAG, "Error retreaving contacts", e);

        }
        return contacts;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        showContactList();
    }

    private void showContactList() {
        List<Contact> contacts =  retrieveContacts();
        if(contacts != null) {
            mArrayAdapter  = new ContactAdapter(getActivity(), contacts);
        }

        setListAdapter(mArrayAdapter);
        setListViewListener();
    }

    private void setListViewListener() {
        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), ContactCreationActivity.class);
                intent.putExtra("requestCode",REQUEST_CODE_EDIT_CONTACT);
                intent.putExtra("contact", (Contact)parent.getItemAtPosition(position));

                startActivityForResult(intent, REQUEST_CODE_EDIT_CONTACT);
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

        if(!handle) return super.onOptionsItemSelected(item);

        return handle;
    }

    private void goToCreateContactActivity() {
        Intent intent = new Intent(getActivity(), ContactCreationActivity.class);
        intent.putExtra("requestCode",REQUEST_CODE_CREATE_CONTACT);
        startActivityForResult(intent, REQUEST_CODE_CREATE_CONTACT);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Contact contact;
        if(resultCode == Activity.RESULT_OK) {
            if (data != null) {
                contact = data.getExtras().getParcelable("contact");
            switch (requestCode) {
                case REQUEST_CODE_CREATE_CONTACT:

                    saveContact(contact);
                    mArrayAdapter.add(contact);
                    Toast.makeText(getActivity(), contact.getFirstName() + " added.", Toast.LENGTH_LONG).show();
                break;

                case REQUEST_CODE_EDIT_CONTACT:
                    editContact(contact);
                    break;
            }


            } else {
                Toast.makeText(getActivity(), "Anything to add, sorry :(", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getActivity(), "Anything to add, sorry :(", Toast.LENGTH_SHORT).show();
        }
    }

    private void editContact(Contact contact) {
        try {
            Dao<Contact, Integer> contactDao = getDBHelper().getContactDao();
            contactDao.update(contact);
            List<Contact> contacts = retrieveContacts();
            mArrayAdapter.clear();
            showContactList();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void saveContact(Contact contact) {
        try {
            Dao<Contact, Integer> contactDao=getDBHelper().getContactDao();
            contactDao.create(contact);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        if(mDBHelper != null){
            OpenHelperManager.releaseHelper();
            mDBHelper = null;
        }
        super.onDestroy();

    }
}
