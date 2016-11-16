package com.example.conorwhyte.smartalarmclock;

import android.location.Location;

/**
 * Created by conorwhyte on 14/11/2016.
 */

public class UserDetails {

    //Possible global variables needed from the user
    public long punisherContact ;
    public String punisherMessage ;
    public int prepTime ;
    public Location destination ;

    public String alarmType ;
    public void chooseAlarmType(int chooseAlarm){
        switch (chooseAlarm){
            case 0:
                alarmType = "normal";
                break;
            case 1:
                alarmType = "movement";
                break;
            case 2:
                alarmType = "puzzle";
                break;
            case 3:
                alarmType = "punisher";
                break;
            default :
                alarmType = "normal";
                break;
        }
    }

    public void setPrepTime(int time){
        prepTime = time ;
    }

    public int returnPrepTime(){
        return prepTime;
    }


}
