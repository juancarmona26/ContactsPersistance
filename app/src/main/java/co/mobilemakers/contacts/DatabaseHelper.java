package co.mobilemakers.contacts;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    private static final String LOG_TAG= DatabaseHelper.class.getSimpleName();
    private final static String DATABASE_NAME = "Contacts.db";
    private final static int DATABASE_VERSION = 1;
    Dao<Contact,Integer> mContactDao;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public Dao<Contact, Integer> getContactDao () throws SQLException {
        if(mContactDao == null) {
            mContactDao = getDao(Contact.class);
        }
        return mContactDao;
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            Log.i(LOG_TAG, "Creating database");
            TableUtils.createTable(connectionSource, Contact.class);
        } catch (SQLException e) {
            Log.i(LOG_TAG, "Error creating database", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {

    }
}
