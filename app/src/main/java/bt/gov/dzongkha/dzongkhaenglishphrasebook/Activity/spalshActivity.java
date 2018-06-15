package bt.gov.dzongkha.dzongkhaenglishphrasebook.Activity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import java.io.IOException;
import bt.gov.dzongkha.dzongkhaenglishphrasebook.Database.MyDatabase;
import bt.gov.dzongkha.dzongkhaenglishphrasebook.R;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class spalshActivity extends AppCompatActivity {
  private final int splash=2000;
  public boolean upgrade=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            MyDatabase dbHelper=new MyDatabase(getApplicationContext());
            SQLiteDatabase sqLiteDatabase =dbHelper.getReadableDatabase();
            upgrade= dbHelper.alterTable(sqLiteDatabase);

        } catch (IOException e) {
            e.printStackTrace();
        }

        setContentView(R.layout.activity_spalsh);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i=new Intent(spalshActivity.this, UserManualActivity.class);
                startActivity(i);
                finish();
            }
        },splash);

    }
}
