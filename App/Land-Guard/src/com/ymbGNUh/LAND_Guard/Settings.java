package com.ymbGNUh.LAND_Guard;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by jkahn on 11/14/14.
 */
public class Settings {
    final static String url = "http://172.20.10.9:3001/handle_request";
    //final static String url = "https://mysterious-lake-9233.herokuapp.com/handle_request";
    final static ArrayList<String> possibleEmergencies = populateEmergencies();

    private static ArrayList<String> populateEmergencies() {
        ArrayList<String> list = new ArrayList<String>();
        list.add("Stroke");
        list.add("Heat Stroke");
        list.add("Heart Attack");
        list.add("Laceration");
        list.add("Suicide");
        list.add("Broken Bone");
        list.add("Seizure");
        list.add("Choking");
        list.add("Allergic Reaction");
        list.add("Asthma Attack");
        list.add("Concussion");
        list.add("Frostbite");
        list.add("Hypothermia");
        list.add("Sprain");
        list.add("Burns");
        list.add("Neck/Spine Injury");
        list.add("Panic Attack");
        list.add("Rape");
        list.add("Shock");
        list.add("Unconscious");
        list.add("Vomiting/Nausea");
        list.add("Whiplash");
        list.add("Foreign Body Object");
        Collections.sort(list);
        return list;
    }
}
