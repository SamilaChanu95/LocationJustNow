package com.example.locationjustnow;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.locationjustnow.Model.CountryDataSource;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    //constant for speech recognition library
    private  static final int SPEAK_REQUEST = 10;

    //declare variables
    TextView txt_value;
    Button btn_voice_intent;

    //create the variable
    public static CountryDataSource countryDataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //initialize the objects to variables
        txt_value = (TextView) findViewById(R.id.txtValue);
        btn_voice_intent = (Button) findViewById(R.id.btnVoiceIntent);

        btn_voice_intent.setOnClickListener(MainActivity.this);

        //Create Hash Table value
        Hashtable<String, String> countriesAndMessages = new Hashtable<>();
        countriesAndMessages.put("Canada","Welcome to Canada.");
        countriesAndMessages.put("France","Welcome to Canada.");
        countriesAndMessages.put("Srilanka","Welcome to Srilanka.");
        countriesAndMessages.put("England","Welcome to England.");
        countriesAndMessages.put("China","Welcome to China.");
        countriesAndMessages.put("Japan","Welcome to Japan.");
        countriesAndMessages.put("Brazil","Welcome to Brazil.");
        countriesAndMessages.put("Greenland","Welcome to Greenland.");

        countryDataSource = new CountryDataSource(countriesAndMessages);

        // ensure device support or not to speech recognition
        PackageManager packageManager = this.getPackageManager();
        List<ResolveInfo> listOfInformation = packageManager.queryIntentActivities(
                new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0

        );

        if(listOfInformation.size() > 0) {

            Toast.makeText(MainActivity.this,"Your device does support to Speech Recognition!",Toast.LENGTH_SHORT).show();

            //call that method
            listenToTheUsersVoice();

        } else {

            Toast.makeText(MainActivity.this,"Your device doesn't support to Speech Recognition!",Toast.LENGTH_SHORT).show();

        }
    }

    //method for allow to users to use the speech recognition frame work
    private void listenToTheUsersVoice() {

        Intent voiceIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

        voiceIntent.putExtra(RecognizerIntent.EXTRA_PROMPT,"Talk to me!");
        voiceIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        voiceIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 10);
        startActivityForResult(voiceIntent, SPEAK_REQUEST);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == SPEAK_REQUEST && resultCode  == RESULT_OK) {

            //store the voice words
            ArrayList<String> voiceWords = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

            //precise of user's words (get the word or telled something)
            float[] confidLevels = data.getFloatArrayExtra(RecognizerIntent.EXTRA_CONFIDENCE_SCORES);

            //create String type variable
            String countryMatchedWithUserWord = countryDataSource.matchWithMinimumConfidenceLevelOfUserWords(voiceWords,confidLevels);

            //pass this data to map activity
            Intent myMapActivity  = new Intent(MainActivity.this, MapsActivity.class);
            myMapActivity.putExtra(CountryDataSource.COUNTRY_KEY, countryMatchedWithUserWord);
            startActivity(myMapActivity);

            /*int index = 0;

            for(String userWord : voiceWords) {

                if (confidLevels != null && index < confidLevels.length) {

                    txt_value.setText(userWord + " - " + confidLevels[index]);

                }

            }

             */

        }
    }

    @Override
    public void onClick(View view) {

        listenToTheUsersVoice();
    }
}
