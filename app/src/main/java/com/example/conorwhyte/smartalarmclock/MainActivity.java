package com.example.conorwhyte.smartalarmclock;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.util.TypedValue;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    static UserDetails user;

    public static SharedPreferences mPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Morning Manager");
        setSupportActionBar(toolbar);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String alarmtime = "Alarm due at - ";
                int temphour, tempminute;
                temphour = user.getAlarmHour();
                tempminute = user.getAlarmMin();
                if(temphour == -1 && tempminute == -1)
                    {alarmtime += "Alarm not set yet, go set it!";}
                else
                    {alarmtime += temphour + ":" + tempminute;}
                Snackbar.make(view, alarmtime, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        boolean newUser = true;             // first time user or no

        mPrefs = getPreferences(MODE_PRIVATE);

        Gson gson = new Gson();
        String json = mPrefs.getString("UserDetails", "");  // try to get UserDetails from memory

        if (!json.isEmpty())         // there is a UserDetails stored in memory
        {
            Toast.makeText(getApplicationContext(), "Welcome Back", Toast.LENGTH_LONG).show();
            // get user
            user = gson.fromJson(json, UserDetails.class);     // load UserDetails into user object
            user.setFirstTime(false);
            newUser = false;
        } else // empty json string, this is the first time
        {
            Toast.makeText(getApplicationContext(), "First Time User Welcome", Toast.LENGTH_LONG).show();
            user = new UserDetails();
            user.setFirstTime(true);
        }

        //startTimer();
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            user = (UserDetails) extras.getSerializable("Object");
        } else if (newUser) {
            user = new UserDetails();
        }

        if (user.getFirstTime() == true && newUser) {
            //Open PopUp
            user.setFirstTime(false);
            popUp();
        }

        //This makes the image button transparent  -Taken from Stack Overflow
        ImageButton ib3 = (ImageButton) findViewById(R.id.ib3);

        TypedValue outValue = new TypedValue();
        getApplicationContext().getTheme().resolveAttribute(
                android.R.attr.selectableItemBackground, outValue, true);
        ib3.setBackgroundResource(outValue.resourceId);
        ib3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAlarm();
            }
        });

        ImageButton ib4 = (ImageButton) findViewById(R.id.ib4);
        TypedValue outValue1 = new TypedValue();
        getApplicationContext().getTheme().resolveAttribute(
                android.R.attr.selectableItemBackground, outValue1, true);
        ib4.setBackgroundResource(outValue.resourceId);
        ib4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDistance();
            }
        });

        SharedPreferences.Editor prefsEditor = MainActivity.mPrefs.edit();
        gson = new Gson();
        json = gson.toJson(user);
        prefsEditor.putString("UserDetails", json);
        prefsEditor.commit();
    }


    //All code below is made by Android studio once a Navigation activity is made
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
        getMenuInflater().inflate(R.menu.nav, menu);
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
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {            // view morning routine
            openCardList();
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {    // edit morning routine
            openAddUserDetails();

        } else if (id == R.id.nav_slideshow) {  // set smart alarm
                                            // need to add smart alarm function in to here
        } else if (id == R.id.nav_manage) {     // set normal alarm
            openAlarm();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void popUp() {
        final Intent intent = new Intent(this, AddUserDetailsActivity.class);
        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
        alertDialog.setTitle("First Time User");
        alertDialog.setMessage("Please fill in some details about your morning routine!\n" +
                "Please turn location services on," +
                " or being in a location where your GPS location cannot be found" +
                " you can enter your location manually.");
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        startActivity(intent);
                        finish();
                    }
                });
        alertDialog.show();
    }

    public void openAlarm() {
        Intent intent = new Intent(this, AlarmActivity.class);
        intent.putExtra("Object", user);
        startActivity(intent);

    }

    public void openDistance() {
        Intent intent = new Intent(this, DistanceActivity.class);
        intent.putExtra("Object", user);
        startActivity(intent);
    }

    public void openAddUserDetails() {
        Intent intent = new Intent(this, AddUserDetailsActivity.class);
        intent.putExtra("Object", user);
        startActivity(intent);
    }

    public void openCardList() {
        Intent intent = new Intent(this, CardListActivity.class);
        intent.putExtra("Object", user);
        startActivity(intent);
        //finish();
    }

    public void openStopAlarm() {
        Intent intent = new Intent(this, AddUserDetailsActivity.class);
        intent.putExtra("Object", user);
        startActivity(intent);
        //finish();
    }
}
