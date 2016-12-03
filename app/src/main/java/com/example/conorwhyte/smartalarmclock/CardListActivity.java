package com.example.conorwhyte.smartalarmclock;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

/**
 * This is the activity where the card list will be displayed, each card is added in depending on
 * which ones the user entered on inital setup, and can be edited in the menu at any time. Its also in
 * this activity where we set off each other individual alarm to signify its time to start a new activity
 * during the morning.
 * <p>
 * Created with the help of following tutorial:
 * http://javapapers.com/android/android-cards-list-view/
 * <p>
 * Author: Conor Whyte
 */

public class CardListActivity extends Activity {

    private static final String TAG = "CardListActivity";
    private ListView listView;
    private CardArrayAdapter cardArrayAdapter;
    private int current = 0;
    public long startTime = System.currentTimeMillis();


    boolean inActivity = true;

    UserDetails user;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listview);
        listView = (ListView) findViewById(R.id.card_listView);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            user = (UserDetails) extras.getSerializable("Object");
        }

        listView.addHeaderView(new View(this));
        listView.addFooterView(new View(this));

        
        cardArrayAdapter = new CardArrayAdapter(getApplicationContext(), R.layout.list_item_card);

        for (int i = 0; i < 10; i++) {
            //  Card card = new Card("Card " + (i+1) + " Line 1", "Card " + (i+1) + " Line 2");
            // cardArrayAdapter.add(card);
        }
        final int[] images = {R.drawable.breakfast, R.drawable.shower, R.drawable.suit, R.drawable.food, R.drawable.car};

        if (user != null) {
            for (int i = 0; i < user.getCardCount(); i++) {

                String name1 = user.getCardNames().get(i);
                String time1 = String.valueOf(user.getCardTimes().get(i));
                Card card = new Card(name1, time1, images[1]);
                cardArrayAdapter.add(card);

            }
        }


        /*
        //String sec = Long.toString(secondsRemaining[0]);
        final Card cardShower = new Card("Shower" , "8:05am", images[1] );
        cardArrayAdapter.add(cardShower);

        Card cardDress = new Card("Get Dressed" , "8:15am", images[2]);
        cardArrayAdapter.add(cardDress);

        final Card cardBreak = new Card("Eat Breakfast" , "8:25am", images[0]);
        cardArrayAdapter.add(cardBreak);

        Card cardLunch = new Card("Prepare Lunch" , "8:30am", images[3]);
        cardArrayAdapter.add(cardLunch);

        Card cardLeave = new Card("Leave for Work" , "8:35am", images[4]);
        cardArrayAdapter.add(cardLeave);
        */

        listView.setAdapter(cardArrayAdapter);
        startTimer();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    //Timer methods used to indicate when the alarm should go off next
    public void startTimer() {
        //timerTextView = (TextView) findViewById(R.id.textView2);
        //startTime = System.currentTimeMillis();
        timerHandler.postDelayed(timerRunnable, 0);
    }

    public void stopTimer() {
        timerHandler.removeCallbacks(timerRunnable);
    }

    int localTimer;
    long localSecond;
    Handler timerHandler = new Handler();
    Runnable timerRunnable = new Runnable() {
        public void run() {
            long millis = System.currentTimeMillis() - startTime;
            int seconds = (int) (millis / 1000);
            int minutes = seconds / 60;
            seconds = seconds % 60;


            localSecond = seconds;
            localTimer = minutes;
            timerHandler.postDelayed(this, 500);
            checkTimer();
        }
    };

    //Method used to set off each individual alarm based on the users input
    boolean last_alarm = false;

    public void checkTimer() {
        int time;
        if (user != null) {
            time = Integer.parseInt(String.valueOf(user.getCardTimes().get(current)));
        } else {
            time = 0;
        }

        String currentmins = Integer.toString(localTimer);

        //  if(current<user.numberOfCards()){
        if (localTimer == time && !last_alarm) { // Alert One
            Intent intent = new Intent(this, StopAlarmActivity.class);
            current++;                              //change time being checked
            if (current == user.getCardCount() - 1) {
                last_alarm = true;
            }
            refreshCards(current);
            startTime = System.currentTimeMillis();
            startActivity(intent);

        } else if (localTimer == time && last_alarm) { // Alert Two
            Intent intent = new Intent(this, StopAlarmActivity.class);
            current++;
            listView.setAdapter(cardArrayAdapter);
            startActivity(intent);
            stopTimer();//old timer for this card
            startMap();


        }
    }

    public void changeColour() {
        FrameLayout layout = (FrameLayout) findViewById(R.id.frame);
        layout.setBackgroundResource(R.drawable.card_state_pressed);
    }

    public void refreshCards(int position) {
        cardArrayAdapter = new CardArrayAdapter(getApplicationContext(), R.layout.list_item_card);
        final int[] images = {R.drawable.breakfast, R.drawable.shower, R.drawable.suit, R.drawable.food, R.drawable.car};
        if (position < user.getCardCount()) {
            for (int i = position; i < user.getCardCount(); i++) {

                String name1 = user.getCardNames().get(i);
                String time1 = user.getCardNames().get(i);
                Card card = new Card(name1, time1, images[1]);
                cardArrayAdapter.add(card);
            }
            listView.setAdapter(cardArrayAdapter);
        }

    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("CardList Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }

    public void openPopUp(View view){
       // Toast.makeText(getApplicationContext(), "Welcome Back", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(CardListActivity.this, MapDirections.class);
        startActivity(intent);

    }

    public void test(){
        LinearLayout app_layer = (LinearLayout) findViewById (R.id.linear);
        app_layer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Welcome Back", Toast.LENGTH_LONG).show();
            }
        });
    }


    public void startMap() {
        String pass = String.valueOf(user.getDestination());             //get value of location from the database
        Toast.makeText(this, pass, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(CardListActivity.this, MapDirections.class);
        CardListActivity.this.startActivity(intent);
    }

}