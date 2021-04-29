package com.localtovocal.Rough;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.localtovocal.Java.AttackImplement;
import com.localtovocal.Java.Bee;
import com.localtovocal.R;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class RoughActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rough);
        Bee bee1 = new Bee(1, "red", new AttackImplement("bee move", "bee attack"));
        bee1.attack();
        Log.e("xskldk", bee1.getSize() + "");



     /*   SimpleDateFormat inputFormat = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat outputFormat = new SimpleDateFormat("EEEE");
        Date date;
        String mDate;
        try {
            date = inputFormat.parse("14-11-2020");
            mDate = outputFormat.format(date);
            Log.e("cxnsjnc", mDate);
        } catch (Exception e) {
            Log.e("djnvjd", e.getMessage());
        }*/

        countDupChars("Hello from facebook");

    }


    public void countDupChars(String str) {

        //Create a HashMap
        Map<Character, Integer> map = new HashMap<Character, Integer>();

        //Convert the String to char array
        char[] chars = str.toCharArray();


        /* logic: char are inserted as keys and their count
         * as values. If map contains the char already then
         * increase the value by 1
         */
        for (Character ch : chars) {
            if (map.containsKey(ch)) {
                Log.e("trrerte", ch+"");
                map.put(ch, map.get(ch) + 1);
            } else {
                Log.e("ertetert", ch+"");
                map.put(ch, 1);
            }
        }

        //Obtaining set of keys
        Set<Character> keys = map.keySet();

        /* Display count of chars if it is
         * greater than 1. All duplicate chars would be
         * having value greater than 1.
         */
        for (Character ch : keys) {
            if (map.get(ch) > 1) {
                System.out.println("Char " + ch + " " + map.get(ch));
            }
        }
    }
}