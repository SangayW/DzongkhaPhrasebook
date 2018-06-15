package bt.gov.dzongkha.dzongkhaenglishphrasebook.CategoryDetails;

import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.Spannable;
import android.text.SpannableString;
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
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import bt.gov.dzongkha.dzongkhaenglishphrasebook.Activity.SearchActivity;
import bt.gov.dzongkha.dzongkhaenglishphrasebook.Database.MyDatabase;
import bt.gov.dzongkha.dzongkhaenglishphrasebook.Favorites.FavoriteActivity;
import bt.gov.dzongkha.dzongkhaenglishphrasebook.ModalClass.CustomTypefaceSpan;
import bt.gov.dzongkha.dzongkhaenglishphrasebook.ModalClass.GetCategoryDetailID;
import bt.gov.dzongkha.dzongkhaenglishphrasebook.Adapters.GetDzoPhrasesAdapter;
import bt.gov.dzongkha.dzongkhaenglishphrasebook.ModalClass.GetDzongkhaPhrases;
import bt.gov.dzongkha.dzongkhaenglishphrasebook.ModalClass.TextSize;
import bt.gov.dzongkha.dzongkhaenglishphrasebook.R;

public class DzoPhrasesDetails extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, SearchView.OnQueryTextListener {

    ArrayList<String> dzo_phrases_list;
    ArrayList<Integer> category_detail_id_list;

    RecyclerView recyclerView;
    GetDzoPhrasesAdapter getDzoPhrasesAdapter;

    List<GetDzongkhaPhrases> getDzongkhaPhrasesList;
    List<GetCategoryDetailID> getCategoryDetailIDList;

    Integer categoryID;
    private SharedPreferences prefs;
    private static final String FONT_SIZE_KEY = "fontsize";

    TextSize ts;
    TextView txtAbout, txtAboutTitle, txtSetting, txtSettingtitle, txtSettingtitle2, txtSettingtitle3,txtSettingtitle4, txtplayall;
    Button settingBtn;

    Typeface typeface;
    LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_details);
        Intent intent=getIntent();
        categoryID=Integer.valueOf(intent.getStringExtra("cat_id"));

        setTitle("");


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        AssetManager am = getApplicationContext().getAssets();
        Typeface typeface = Typeface.createFromAsset(am,String.format(Locale.US, "fonts/%s", "Jomolhari.ttf"));
        mTitle.setTypeface(typeface);
        mTitle.setText(intent.getStringExtra("cat_name"));
        mTitle.setTextSize(25);
        setSupportActionBar(toolbar);

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
                ts=new TextSize(22,55);
        }
        /*ends*/

        //function to display dzongkha phrases
        listPhrasesDetails();

        Toolbar toolbar1 = (Toolbar) findViewById(R.id.toolbar1);
        LayoutInflater li = LayoutInflater.from(this);
        View customView = li.inflate(R.layout.custom_layout, null);
        toolbar1.addView(customView);

        /*when pressing the back icon*/
        ImageButton back=customView.findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDzoPhrasesAdapter.destroyOnBack();
                finish();
            }
        });

        txtplayall = findViewById(R.id.text_playall);
        am = getApplicationContext().getAssets();
        typeface = Typeface.createFromAsset(am,String.format(Locale.US, "fonts/%s", "Jomolhari.ttf"));
        txtplayall.setTypeface(typeface);

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
        getDzoPhrasesAdapter.getPlayAll(playAll,categoryID,linearLayoutManager);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //adding the custom font to the items in navigation drawer
        Menu m = navigationView.getMenu();
        for (int i=0;i<m.size();i++) {
            MenuItem mi = m.getItem(i);

            //the method we have create in activity
            applyFontToMenuItem(mi);
        }
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
        getMenuInflater().inflate(R.menu.category_details, menu);

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
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
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
        }
         else if (id == R.id.nav_fav) {
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

        }
        else if (id == R.id.nav_exit) {
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
        AlertDialog.Builder builder=new AlertDialog.Builder(DzoPhrasesDetails.this);
        builder.setTitle("རྫོང་ཁའི་ཚོགས་ཚིག");
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setMessage("རིམ་ལུགས་ལས་འཐོན་འགྱོ་ནི་ཨིན་ན?").setCancelable(false).setPositiveButton("ཨིན།", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        })
                .setNegativeButton("མེན།", new DialogInterface.OnClickListener() {
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
    public void viewFavorite(){
        Intent intent=new Intent(getApplicationContext(), FavoriteActivity.class);
        startActivity(intent);
    }
    public void listPhrasesDetails(){

        MyDatabase helper = null;
        try {
            helper = new MyDatabase(getApplicationContext());
        } catch (IOException e) {
            e.printStackTrace();
        }

        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from categoryDetails where cat_id="+categoryID,null);
        dzo_phrases_list=new ArrayList<String>();
        category_detail_id_list=new ArrayList<Integer>();

        if (cursor.getCount() != 0)
        {
            if (cursor.moveToFirst())
            {
                do
                {
                    dzo_phrases_list.add(cursor.getString(cursor.getColumnIndexOrThrow("dzo_phrase")));
                    category_detail_id_list.add(cursor.getInt(cursor.getColumnIndexOrThrow("cat_details_id")));
                } while (cursor.moveToNext());
            }
        }
        cursor.close();
        db.close();

        getDzongkhaPhrasesList=new ArrayList<>();
        getCategoryDetailIDList=new ArrayList<>();

        recyclerView=findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(false);
        linearLayoutManager=new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);
       // recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        for (int i = 0; i < dzo_phrases_list.size(); i++) {
            getDzongkhaPhrasesList.add(
                    new GetDzongkhaPhrases(
                            dzo_phrases_list.get(i)
                    ));
        }
        for (int i = 0; i < category_detail_id_list.size(); i++) {
            getCategoryDetailIDList.add(
                    new GetCategoryDetailID(
                            category_detail_id_list.get(i)
                    ));
        }

        getDzoPhrasesAdapter = new GetDzoPhrasesAdapter(getApplicationContext(),getDzongkhaPhrasesList,getCategoryDetailIDList,ts.getTextSize(),ts.getDzo_size());
        recyclerView.setAdapter(getDzoPhrasesAdapter);
    }

    //changing the text size and clear favorites
    public void setting() {

        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        final LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.settingdzo, null);

        txtSetting = (alertLayout).findViewById(R.id.textView);
        txtSettingtitle =(alertLayout).findViewById(R.id.textView2);
        txtSettingtitle2 =(alertLayout).findViewById(R.id.textView3);
        txtSettingtitle3 =(alertLayout).findViewById(R.id.textView4);
        txtSettingtitle4 = (alertLayout). findViewById(R.id.textView5);
        settingBtn = (alertLayout). findViewById(R.id.clearfav);
        AssetManager am = getApplicationContext().getAssets();
        Typeface typeface = Typeface.createFromAsset(am,String.format(Locale.US, "fonts/%s", "Wangdi_29.ttf"));
        txtSetting.setTypeface(typeface);
        txtSettingtitle.setTypeface(typeface);
        txtSettingtitle2.setTypeface(typeface);
        txtSettingtitle3.setTypeface(typeface);
        txtSettingtitle4.setTypeface(typeface);
        settingBtn.setTypeface(typeface);

        //using seekbar for changing font size
        SeekBar seekBar = alertLayout.findViewById(R.id.seekBar);
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
                    getDzoPhrasesAdapter.changeTextSize(12,25);

                } else if (progressValue == 1) {
                    prefs = getPreferences(MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString(FONT_SIZE_KEY, "1");
                    editor.commit();
                    getDzoPhrasesAdapter.changeTextSize(15,35);
                }
                else if (progressValue == 2) {
                    prefs = getPreferences(MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    //---save the values in the EditText view to preferences---
                    editor.putString(FONT_SIZE_KEY, "2");
                    editor.commit();
                    getDzoPhrasesAdapter.changeTextSize(18,45);

                }
                else {
                    prefs = getPreferences(MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString(FONT_SIZE_KEY, "3");
                    editor.commit();
                    getDzoPhrasesAdapter.changeTextSize(22,55);
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
                Toast.makeText(getApplicationContext(),"དགའ་ཤོས་ག་ར་གསལ་ཡི།", Toast.LENGTH_LONG).show();

            }
        });
    }
    public void aboutUs()
    {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        final LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.about_us_dzo, null);
        //using seekbar for changing font size
        txtAbout=(alertLayout).findViewById(R.id.textView7);
        txtAboutTitle= (alertLayout).findViewById(R.id.textView);
        AssetManager am = getApplicationContext().getAssets();
        Typeface typeface = Typeface.createFromAsset(am,String.format(Locale.US, "fonts/%s", "Wangdi_29.ttf"));
        txtAbout.setTypeface(typeface);
        txtAbout.setTextSize(29);
        txtAboutTitle.setTypeface(typeface);
        txtAboutTitle.setTextSize(35);
        alert.setView(alertLayout);
        final AlertDialog dialog = alert.create();
        dialog.show();

        ImageButton closeButton =  alertLayout.findViewById(R.id.close);
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

    private void applyFontToMenuItem(MenuItem mi) {
        AssetManager am = getApplicationContext().getAssets();
        Typeface typeface = Typeface.createFromAsset(am,String.format(Locale.US, "fonts/%s", "Wangdi_29.ttf"));
        SpannableString mNewTitle = new SpannableString(mi.getTitle());
        mNewTitle.setSpan(new CustomTypefaceSpan("" , typeface), 0 , mNewTitle.length(),  Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        mi.setTitle(mNewTitle);
    }

}

