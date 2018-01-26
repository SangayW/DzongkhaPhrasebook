package bt.gov.dzongkha.dzongkhaenglishphrasebook.Database;

/**
 * Created by sangay on 1/24/2018.
 */

import android.content.Context;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;



public class MyDatabase extends SQLiteAssetHelper {

    private static final String DATABASE_NAME = "phrasebookDB.db";
    private static final int DATABASE_VERSION = 1;

    public MyDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

}

