package com.example.locationjustnow.Model;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

public class CountryDataSource {

    public static final String COUNTRY_KEY = "country";
    public static final float MINIMUM_CONFIDENCE_LEVEL = 0.4F;
    public static final String DEFAULT_COUNTRY_NAME = "Canada";
    public static final double DEFAULT_COUNTRY_LATITUDE = 59.969151;
    public static final double DEFAULT_COUNTRY_LONGITUDE = -111.459050;
    public static final String DEFAULT_MESSAGE = "Welcome!";

    //using key and value for countries and messages
    private Hashtable<String, String> countriesAndMessages;

    //constructor for CountryDataSource
    public CountryDataSource(Hashtable<String, String> countriesAndMessages) {

        this.countriesAndMessages = countriesAndMessages;

    }

    //For access some arguments
    public String matchWithMinimumConfidenceLevelOfUserWords(ArrayList<String> userWords, float[] confidenceLevels) {

        if (userWords == null || confidenceLevels == null) {

            return DEFAULT_COUNTRY_NAME;
        }

        int numberOfUserWords = userWords.size();

        Enumeration<String> countries ;

        for (int index = 0; index < numberOfUserWords && index < confidenceLevels.length; index++) {

            if (confidenceLevels[index] < MINIMUM_CONFIDENCE_LEVEL) {

                break;

            }

            String acceptedUserWord = userWords.get(index);

            countries = countriesAndMessages.keys();

            //hasMoreElements() that tests if this enumeration contains more elements
            while (countries.hasMoreElements()) {


                //nextElement() return the next element of this enumeration if this enumeration object has at  least one or more element to provide
                String selectedCountry = countries.nextElement();

                //ignore the acceptedUserWord contains words are uppercase or lowercase case (considerations ignore)
                if (acceptedUserWord.equalsIgnoreCase(selectedCountry)) {

                    return acceptedUserWord;

                }

            }

        }

        //if we return null that is cause to error
        return DEFAULT_COUNTRY_NAME;

    }

    public String getTheInfoOfTheCountry(String country) {

        return  countriesAndMessages.get(country);

    }


}
