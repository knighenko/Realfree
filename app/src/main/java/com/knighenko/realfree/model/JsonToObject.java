package com.knighenko.realfree.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.knighenko.realfree.entity.Advertisement;


import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JsonToObject {
    private  String jsonString;

    public JsonToObject(String jsonString) {
        this.jsonString = jsonString;
    }

    /**
     * This method returns ArrayList of Advertisements from OlX
     */
    public ArrayList<Advertisement> getAdvertisements() {
        ArrayList<Advertisement> advertisements = new ArrayList<>();

        Pattern logEntry = Pattern.compile("(\\{(.*?)\\})");
        Matcher matchPattern = logEntry.matcher(jsonString);

        while (matchPattern.find()) {
            ObjectMapper mapper = new ObjectMapper();
            Advertisement adv = null;
            try {
                adv = mapper.readValue(matchPattern.group(1), Advertisement.class);
                advertisements.add(adv);
            } catch (IOException e) {
                e.printStackTrace();
            }
          //  System.out.println(adv);
        }
        return advertisements;
    }
}
