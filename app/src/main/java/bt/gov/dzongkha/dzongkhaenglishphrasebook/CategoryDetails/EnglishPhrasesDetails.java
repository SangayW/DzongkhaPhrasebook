package bt.gov.dzongkha.dzongkhaenglishphrasebook.CategoryDetails;

import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageButton;
import android.support.v7.widget.SearchView;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.Toast;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import bt.gov.dzongkha.dzongkhaenglishphrasebook.Activity.SearchActivity;
import bt.gov.dzongkha.dzongkhaenglishphrasebook.Database.MyDatabase;
import bt.gov.dzongkha.dzongkhaenglishphrasebook.Favorites.FavoriteActivity;
import bt.gov.dzongkha.dzongkhaenglishphrasebook.ModalClass.GetCategoryDetailID;
import bt.gov.dzongkha.dzongkhaenglishphrasebook.Adapters.GetEngPhrasesAdapter;
import bt.gov.dzongkha.dzongkhaenglishphrasebook.ModalClass.GetEnglishPhrases;
import bt.gov.dzongkha.dzongkhaenglishphrasebook.ModalClass.TextSize;
import bt.gov.dzongkha.dzongkhaenglishphrasebook.R;

public class EnglishPhrasesDetails extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,SearchView.OnQueryTextListener {

    ArrayList<String> eng_phrases_list;
    ArrayList<Integer> category_detail_id_list;

    public RecyclerView recyclerView;

    ArrayList<GetEnglishPhrases> getEnglishPhrasesList;
    List<GetCategoryDetailID> getCategoryDetailIDList;
    Integer categoryID;

    private SharedPreferences prefs;
    private static final String FONT_SIZE_KEY="font size";

    GetEngPhrasesAdapter getEngPhrasesAdapter;
    TextSize ts;

    LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_english_phrases_details);

        Intent intent=getIntent();
        categoryID=Integer.valueOf(intent.getStringExtra("cat_id"));
        setTitle(intent.getStringExtra("cat_name"));
        final ScrollView scrollView = findViewById(R.id.scroll);

        scrollView.postDelayed(new Runnable() {
            @Override
            public void run() {
                //replace this line to scroll up or down
                scrollView.scrollTo(0,0);
            }
        }, 15L);

        /*begins setting the size of the text based on the value stored in the shared preference*/
        prefs=getPreferences(MODE_PRIVATE);
        if(prefs.getString(FONT_SIZE_KEY,"").isEmpty()){
            ts=new TextSize(15,35);
        }
        else
        {
            if(prefs.getString(FONT_SIZE_KEY,"").equalsIgnoreCase("0")){
                ts=new TextSize(12,25);
            }
            else if(prefs.getString(FONT_SIZE_KEY,"").equalsIgnoreCase("1")){
                ts=new TextSize(15,35);
            }
            else if(prefs.getString(FONT_SIZE_KEY,"").equalsIgnoreCase("2")){
                ts=new TextSize(18,45);
            }
            else
                ts=new TextSize(21,55);
        }
        /*ends*/



        //calling function to display english phrases details
        listEnglishPhrasesDetails();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Toolbar toolbar1 = (Toolbar) findViewById(R.id.toolbar1);
        LayoutInflater li = LayoutInflater.from(this);
        View customView = li.inflate(R.layout.eng_custom_layout, null);
        toolbar1.addView(customView);

        /*when pressing the back icon*/
        ImageButton back=customView.findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               getEngPhrasesAdapter.destroyOnBack();
                   finish();
            }
        });
        /*end of back functionalities*/
        /*when pressing play all button*/
        ImageButton playAll=findViewById(R.id.playAll);
        playAll.setOnClickListener(new View.OnClickListener() {
            Fragment fragment=null;
            @Override
            public void onClick(View v) {
//                ProgressFragment fr= new ProgressFragment();
//                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//                transaction.replace(R.id.content_frame,fr);
//                transaction.commit();
                //fragment=new ProgressFragment();
            }

        });
        getEngPhrasesAdapter.getPlayAll(playAll,categoryID,linearLayoutManager);
        //getEngPhrasesAdapter.getPlayAll(playAll,categoryID,linearLayoutManager);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.english_phrases_details, menu);

        /*implementation of search functionalities*/
        MenuItem searchViewItem= menu.findItem(R.id.action_search);
        SearchView searchView= (SearchView)searchViewItem.getActionView();
        searchView.setOnQueryTextListener(this);

        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query)
    {
       query=query.toLowerCase();
        if(query.length()<3){
            Toast.makeText(getApplicationContext(),"Please enter more than 3 letters",Toast.LENGTH_SHORT).show();
        }
        else{
            Intent intent=new Intent(getApplicationContext(), SearchActivity.class);
            intent.putExtra("search_text",query);
            startActivity(intent);
        }
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
       switch (id){
           case R.id.ac:
               viewFavorite();
               return true;
               default:
                   return super.onOptionsItemSelected(item);

       }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        Fragment fragment = null;
        if (id == R.id.nav_setting) {
            setting();

        } else if (id == R.id.nav_fav) {
            viewFavorite();

        } else if (id == R.id.nav_share) {
            ApplicationInfo applicationInfo= getApplicationContext().getApplicationInfo();
            String apkPath= applicationInfo.sourceDir;

            Intent intent= new Intent(Intent.ACTION_SEND);
            intent.setType( "application/vnd.android.package-archive");
            intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(apkPath)));
            startActivity(Intent.createChooser(intent, "Share App Using"));

        } else if (id == R.id.nav_rate) {
            rateApp();

        } else if (id == R.id.nav_feedback) {
            giveFeedback();
        } else if (id == R.id.nav_about) {
            aboutUs();

        }else if (id == R.id.nav_abbr) {
            abbreviation();

        }else if (id == R.id.nav_exit) {
            exitApp();

        }

        //replacing the fragment
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            // ft.replace(R.id.content_frame, fragment);
            ft.commit();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void giveFeedback() {
        String mailto = "mailto:02042013015.cst@rub.edu.bt";

        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        emailIntent.setData(Uri.parse(mailto));

        try {
            startActivity(emailIntent);
        } catch (ActivityNotFoundException e) {
            //TODO: Handle case where no email app is available
        }
    }
    public void exitApp()
    {
        AlertDialog.Builder builder=new AlertDialog.Builder(EnglishPhrasesDetails.this);
        builder.setTitle(R.string.app_name);
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setMessage("Do you want to exit?").setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
        AlertDialog alert=builder.create();
        alert.show();
        Button nbutton = alert.getButton(DialogInterface.BUTTON_NEGATIVE);
        nbutton.setTextColor(Color.BLACK);
        Button pbutton = alert.getButton(DialogInterface.BUTTON_POSITIVE);
        pbutton.setTextColor(Color.BLACK);

    }

    //---view the list in favorite sections---
    public void viewFavorite(){
        Intent intent=new Intent(getApplicationContext(), FavoriteActivity.class);
        startActivity(intent);
    }

    //function to populate english phrases details into recyclerview
    public void listEnglishPhrasesDetails(){
        MyDatabase helper = null;
        try {
            helper = new MyDatabase(getApplicationContext());
        } catch (IOException e) {
            e.printStackTrace();
        }

        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from categoryDetails where cat_id="+categoryID,null);

        eng_phrases_list=new ArrayList<String>();
        category_detail_id_list=new ArrayList<Integer>();

        if (cursor.getCount() != 0)
        {
            if (cursor.moveToFirst())
            {
                do
                {
                    eng_phrases_list.add(cursor.getString(cursor.getColumnIndexOrThrow("en_phrase")));
                    category_detail_id_list.add(cursor.getInt(cursor.getColumnIndexOrThrow("cat_details_id")));
                } while (cursor.moveToNext());
            }
        }
        cursor.close();
        db.close();

        getEnglishPhrasesList=new ArrayList<>();
        getCategoryDetailIDList=new ArrayList<>();

        recyclerView=findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(false);
        linearLayoutManager=new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);



        for (int i = 0; i < eng_phrases_list.size(); i++) {
            getEnglishPhrasesList.add(
                    new GetEnglishPhrases(
                            eng_phrases_list.get(i)
                    ));
        }

        for (int i = 0; i < category_detail_id_list.size(); i++) {
            getCategoryDetailIDList.add(
                    new GetCategoryDetailID(
                            category_detail_id_list.get(i)
                    ));
        }
        getEngPhrasesAdapter = new GetEngPhrasesAdapter(getApplicationContext(),getEnglishPhrasesList,getCategoryDetailIDList,ts.getTextSize(),ts.getDzo_size());
        recyclerView.setAdapter(getEngPhrasesAdapter);


    }

    //changing the text size and clear favorites
    public void setting() {

        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        final LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.setting, null);

        //using seekbar for changing font size
        SeekBar seekBar = (SeekBar) alertLayout.findViewById(R.id.seekBar);
        alert.setView(alertLayout);
        final AlertDialog dialog = alert.create();
        dialog.show();

        //---load the SharedPreferences object---
        //load saved fontsize
        prefs = getPreferences(MODE_PRIVATE);
        //---set the TextView font size to the previously saved values---
        String fontSize = prefs.getString(FONT_SIZE_KEY, "1");
        //---init the SeekBar---
        seekBar.setProgress(Integer.valueOf(fontSize));

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int progressValue = seekBar.getProgress();
                if (progressValue == 0) {
                    //---get the SharedPreferences object---
                    prefs = getPreferences(MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    //---save the values in the EditText view to preferences---
                    editor.putString(FONT_SIZE_KEY, "0");
                    //---saves the values---
                    editor.commit();
                    //---change the text size---
                    getEngPhrasesAdapter.changeTextSize(12,25);

                } else if (progressValue == 1) {
                    prefs = getPreferences(MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString(FONT_SIZE_KEY, "1");
                    editor.commit();
                    getEngPhrasesAdapter.changeTextSize(15,35);
                }
                else if (progressValue == 2) {
                    prefs = getPreferences(MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    //---save the values in the EditText view to preferences---
                    editor.putString(FONT_SIZE_KEY, "2");
                    editor.commit();
                    getEngPhrasesAdapter.changeTextSize(18,45);
                }
                else {
                    prefs = getPreferences(MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString(FONT_SIZE_KEY, "3");
                    editor.commit();
                    getEngPhrasesAdapter.changeTextSize(21,55);
                }
            }

            public void onStartTrackingTouch(SeekBar arg0) {
                //do something
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                //do something
            }
        });
        ImageButton closeButton = (ImageButton) alertLayout.findViewById(R.id.close);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        // clearfavorite functions
        Button clearBtn=alertLayout.findViewById(R.id.clearfav);
        clearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                MyDatabase helper = null;
                try {
                    helper = new MyDatabase(getApplicationContext());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                SQLiteDatabase db = helper.getReadableDatabase();
                ContentValues contentValues=new ContentValues();
                contentValues.put("favorite",0);
                db.update("categoryDetails",contentValues,"favorite="+1,null);
                db.close();
                Toast.makeText(getApplicationContext(),"All favorites are cleared", Toast.LENGTH_LONG).show();

            }
        });
    }
    public void aboutUs()
    {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        final LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.about_us, null);
        //using seekbar for changing font size
        SeekBar seekBar = (SeekBar) alertLayout.findViewById(R.id.seekBar);
        alert.setView(alertLayout);
        final AlertDialog dialog = alert.create();
        dialog.show();

        ImageButton closeButton = (ImageButton) alertLayout.findViewById(R.id.close);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    //abbreviation functions
    public void abbreviation()
    {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        final LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.abbreviation, null);
        alert.setView(alertLayout);
        final AlertDialog dialog = alert.create();
        dialog.show();

        ImageButton closeButton = (ImageButton) alertLayout.findViewById(R.id.close);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    //rate the app
    public void rateApp()
    {
        Uri uri= Uri.parse("market://details?id="+ getBaseContext().getPackageName());
        Intent gotoM= new Intent(Intent.ACTION_VIEW,uri);
        gotoM.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_NEW_DOCUMENT | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);

        try{
            startActivity(gotoM);
        }catch (ActivityNotFoundException e)
        {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id="+ getBaseContext().getPackageName())));
        }
    }

}
