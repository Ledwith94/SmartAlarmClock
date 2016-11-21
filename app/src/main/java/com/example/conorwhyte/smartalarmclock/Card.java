package com.example.conorwhyte.smartalarmclock;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.view.View;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;
import static android.graphics.Color.*;

/**
 * Created by conorwhyte on 16/11/2016.
 */

public class Card {
    private String line1;
    private String line2;
    private int id;

    public Card(String line1, String line2, int id) {
        this.line1 = line1;
        this.line2 = line2;
        this.id = id;
    }



    public String getLine1() {
        return line1;
    }

    public String getLine2() {
        return line2;
    }

    public int getId(){
        return id;
    }


}