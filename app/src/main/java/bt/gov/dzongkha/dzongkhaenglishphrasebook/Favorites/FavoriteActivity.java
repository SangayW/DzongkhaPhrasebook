package bt.gov.dzongkha.dzongkhaenglishphrasebook.Favorites;

import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.SearchView;
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
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.Locale;

import bt.gov.dzongkha.dzongkhaenglishphrasebook.Activity.SearchActivity;
import bt.gov.dzongkha.dzongkhaenglishphrasebook.Adapters.GetDzoPhrasesAdapter;
import bt.gov.dzongkha.dzongkhaenglishphrasebook.Database.MyDatabase;

import bt.gov.dzongkha.dzongkhaenglishphrasebook.Fragments.ListDzoFavoriteFragment;
import bt.gov.dzongkha.dzongkhaenglishphrasebook.Fragments.ListEngFavoriteFragment;
import bt.gov.dzongkha.dzongkhaenglishphrasebook.ModalClass.TextSize;
import bt.gov.dzongkha.dzongkhaenglishphrasebook.R;
import bt.gov.dzongkha.dzongkhaenglishphrasebook.tabsPager;

public class FavoriteActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, SearchView.OnQueryTextListener {

    TabLayout tabLayout;
    ViewPager viewPager;
    tabsPager tabsPager;
    private SharedPreferences prefs;
    private static final String FONT_SIZE_KEY = "fontsize";
    TextSize ts;
    GetDzoPhrasesAdapter getDzoPhrasesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);
        setTitle("Favorites Phrases");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        Toolbar toolbar1 = (Toolbar) findViewById(R.id.toolbar1);
//        setSupportActionBar(toolbar1);

        tabLayout= (TabLayout) findViewById(R.id.tabs);
        viewPager =(ViewPager) findViewById(R.id.viewpager);
        tabsPager= new tabsPager(getSupportFragmentManager());
        tabsPager.addFragment(new ListDzoFavoriteFragment(), "རྫོང་ཁ།");
        tabsPager.addFragment(new ListEngFavoriteFragment(), "English");
        viewPager.setAdapter(tabsPager);
        tabLayout.setupWithViewPager(viewPager);

//        LayoutInflater li = LayoutInflater.from(this);
//        View customView = li.inflate(R.layout.eng_custom_layout, null);
//        toolbar1.addView(customView);
//
//        ImageButton back=customView.findViewById(R.id.back);
//        back.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                finish();
//            }
//        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        setCustomFont();

    }

    public void setCustomFont() {
        ViewGroup vg = (ViewGroup) tabLayout.getChildAt(0);
        int tabsCount = 1;
        for (int j = 0; j < tabsCount; j++) {
            ViewGroup vgTab = (ViewGroup) vg.getChildAt(j);
            int tabChildsCount = vgTab.getChildCount();
            for (int i = 0; i < tabChildsCount; i++) {
                View tabViewChild = vgTab.getChildAt(i);
                if (tabViewChild instanceof TextView) {
                    //Put your font in assests folder
                    //assign name of the font here (Must be case sensitive)
                    // ((TextView) tabViewChild).setTypeface(Typeface.createFromAsset(getAssets(), "Nosifer-Regular.ttf"));
                    AssetManager am = getApplicationContext().getAssets();
                    Typeface typeface = Typeface.createFromAsset(am, String.format(Locale.US, "fonts/%s", "Jomolhari.ttf"));
                    ((TextView) tabViewChild).setTypeface(typeface);


                }
            }
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
        getMenuInflater().inflate(R.menu.favorite, menu);

          /*implementation of search functionalities*/
        MenuItem searchViewItem= menu.findItem(R.id.action_search);
        SearchView searchView= (SearchView)searchViewItem.getActionView();
        searchView.setOnQueryTextListener(this);

        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
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

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Fragment fragment = null;

        if (id == R.id.nav_setting) {
            //setting();
        }
        else if (id == R.id.nav_fav) {
        }
        else if (id == R.id.nav_share) {
            ApplicationInfo applicationInfo= getApplicationContext().getApplicationInfo();
            String apkPath= applicationInfo.sourceDir;

            Intent intent= new Intent(Intent.ACTION_SEND);
            intent.setType( "application/vnd.android.package-archive");
            intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(apkPath)));
            startActivity(Intent.createChooser(intent, "Share App Using"));

        }

        else if (id == R.id.nav_rate) {
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
            //ft.replace(R.id.content_frame, fragment);
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
        AlertDialog.Builder builder=new AlertDialog.Builder(FavoriteActivity.this);
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


                   // getDzoPhrasesAdapter.changeTextSize(12);

                } else if (progressValue == 1) {
                    prefs = getPreferences(MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString(FONT_SIZE_KEY, "1");
                    editor.commit();
                  //  getDzoPhrasesAdapter.changeTextSize(15);

                }
                else if (progressValue == 2) {
                    prefs = getPreferences(MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    //---save the values in the EditText view to preferences---
                    editor.putString(FONT_SIZE_KEY, "2");
                    editor.commit();

                   // getDzoPhrasesAdapter.changeTextSize(18);

                }
                else {
                    prefs = getPreferences(MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString(FONT_SIZE_KEY, "3");
                    editor.commit();
                   // getDzoPhrasesAdapter.changeTextSize(21);

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

            }
        });
    }

    //about us functions
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
