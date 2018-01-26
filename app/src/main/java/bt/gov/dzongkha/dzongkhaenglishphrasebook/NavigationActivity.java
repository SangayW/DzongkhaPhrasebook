package bt.gov.dzongkha.dzongkhaenglishphrasebook;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import javax.mail.AuthenticationFailedException;
import javax.mail.MessagingException;

import bt.gov.dzongkha.dzongkhaenglishphrasebook.FeedBackActivity.Mail;

public class NavigationActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    TabLayout tabLayout;
    ViewPager viewPager;
    tabsPager tabsPager;

    String Message,Password,Email;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tabLayout= (TabLayout) findViewById(R.id.tabs);
        viewPager =(ViewPager) findViewById(R.id.viewpager);
        tabsPager= new tabsPager(getSupportFragmentManager());
        tabsPager.addFragment(new DzongkhaFragment(), "རྫོང་ཁ།");
        tabsPager.addFragment(new EnglishFragment(), "English");
        viewPager.setAdapter(tabsPager);
        tabLayout.setupWithViewPager(viewPager);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

//        MediaPlayer ring= MediaPlayer.create(NavigationActivity.this,R.raw.voice);
//        ring.start();

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
        getMenuInflater().inflate(R.menu.navigation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Fragment fragment = null;
        if (id == R.id.nav_setting) {
            // Handle the camera action
        } else if (id == R.id.nav_fav) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_rate) {

        } else if (id == R.id.nav_feedback) {
            giveFeedback();
        } else if (id == R.id.nav_about) {

        }else if (id == R.id.nav_exit) {

        }

        //replacing the fragment
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void giveFeedback() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.activity_feed_back, null);
        dialogBuilder.setView(dialogView);

        final EditText message = dialogView.findViewById(R.id.message);
        final EditText password=dialogView.findViewById(R.id.password);
        final EditText email=dialogView.findViewById(R.id.email);

        dialogBuilder.setTitle("Feed back");
        dialogBuilder.setMessage("Please give us the feed back about the app");
        dialogBuilder.setPositiveButton("Send", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                Message=message.getText().toString();
                Password=password.getText().toString();
                Email=email.getText().toString();

                sendMessage();
            }
        });
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //pass
            }
        });
        AlertDialog b = dialogBuilder.create();
        b.show();

        b.getButton(b.BUTTON_NEGATIVE).setTextColor(Color.RED);
        b.getButton(b.BUTTON_POSITIVE).setTextColor(Color.BLACK);

    }
    private void sendMessage() {
        String[] recipients = { "sanglim2012@gmail.com" };
        SendEmailAsyncTask email1 = new SendEmailAsyncTask();

        email1.m = new Mail(Email, Password);
        email1.m.set_from(Email);
        email1.m.setBody(Message);
        email1.m.set_to(recipients);
        email1.m.set_subject("Feedback");
        email1.execute();
    }

    class SendEmailAsyncTask extends AsyncTask<Void, Void, Boolean> {


        Mail m;

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);

            if(aBoolean){
                Toast.makeText(getApplicationContext(),"feedback sent successfully",Toast.LENGTH_LONG).show();
            }
            else
                Toast.makeText(getApplicationContext(),"feedback sending failed",Toast.LENGTH_LONG).show();
        }

        public SendEmailAsyncTask() {}

        @Override
        protected Boolean doInBackground(Void... voids) {
            try {
                if(m.send()){
                    return true;
                }
                else
                    return false;

            }
            catch (AuthenticationFailedException e) {
                Log.e(SendEmailAsyncTask.class.getName(), "Bad account details");
                e.printStackTrace();
                return false;
            }
            catch (MessagingException e) {
                Log.e(SendEmailAsyncTask.class.getName(), "Email failed");
                e.printStackTrace();
                return false;
            }
            catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
    }
}
