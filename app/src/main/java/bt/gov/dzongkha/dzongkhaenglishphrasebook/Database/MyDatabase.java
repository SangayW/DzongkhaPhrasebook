package bt.gov.dzongkha.dzongkhaenglishphrasebook.Database;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by sangay on 3/28/2018.
 */

public class MyDatabase extends SQLiteOpenHelper {
    private Context cont;
    private String DB_PATH;

    private static String DATABASE_NAME = "dzongkhaPhrasesDb.db";
    public SQLiteDatabase myDataBase;


    public MyDatabase(Context context) throws IOException {
        super(context,DATABASE_NAME,null,1);
        this.cont=context;
        if (android.os.Build.VERSION.SDK_INT >= 17)
            DB_PATH = context.getApplicationInfo().dataDir + "/databases/";
        else
            DB_PATH = "/data/data/" + context.getPackageName() + "/databases/";
        Log.d("path", DB_PATH);

        boolean dbexist = checkdatabase();
        if (dbexist){
            opendatabase();
        }
        else{
            createdatabase();
        }

    }

    public void createdatabase() throws IOException {
        boolean dbexist = checkdatabase();
        if(!dbexist) {
            this.getReadableDatabase();
            copydatabase();
        }

    }

    private boolean checkdatabase() {

        boolean checkdb = false;
        try {
            String myPath = DB_PATH + DATABASE_NAME;
            File dbfile = new File(myPath);
            checkdb = dbfile.exists();
        } catch(SQLiteException e) {
            //System.out.println("Database doesn't exist");
        }
        return checkdb;
    }

    private void copydatabase() throws IOException {

       // Open your local db as the input stream
        InputStream myinput = cont.getAssets().open("databases/"+DATABASE_NAME);

        // Path to the just created empty db
        String outfilename = DB_PATH + DATABASE_NAME;


        //Open the empty db as the output stream
        OutputStream myoutput = new FileOutputStream(outfilename);

        // transfer byte to inputfile to outputfile
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myinput.read(buffer))>0) {
            myoutput.write(buffer,0,length);
        }

        //Close the streams
        myoutput.flush();
        myoutput.close();
        myinput.close();

    }

    public void opendatabase() throws SQLException {
        //Open the database
        String mypath = DB_PATH + DATABASE_NAME;
        myDataBase = SQLiteDatabase.openDatabase(mypath, null, SQLiteDatabase.OPEN_READWRITE);
    }

    public synchronized void close() {
        if(myDataBase != null) {
            myDataBase.close();
        }
        super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("ALTER TABLE categories_detail ADD COLUMN sound string");
    }

    public boolean alterTable(SQLiteDatabase sqLiteDatabase) {
        try{
            sqLiteDatabase.execSQL("ALTER TABLE categories_detail ADD COLUMN sound string");
            return true;
        }catch (Exception e){
            return false;
        }

    }

}