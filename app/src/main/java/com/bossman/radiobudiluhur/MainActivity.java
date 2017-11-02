package com.bossman.radiobudiluhur;

import android.graphics.Color;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.support.annotation.BoolRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    Button btn_play;
    //String stream = "http://radiobudiluhur.onlivestreaming.net:8999/live";
    //String stream = "http://us2.internet-radio.com:8046/";
    //String stream = "http://streaming.sim-indonesia.com:8000/genfm";
    //String stream = "http://987genfm.com/streaming";
    String stream = "http://radiobudiluhur.onlivestreaming.net:8999/stream?type.flv";
    //String stream[] = {"http://radiobudiluhur.onlivestreaming.net:8999/autodj","http://radiobudiluhur.onlivestreaming.net:8999/live"};

    Calendar CurrentDateTime = Calendar.getInstance();

    /**
     * note things-to-do
     * karena link buat streamnya berubah pada waktu tertentu
     * maka harus dibuat jadi 2 sesi
     * dimana pada pagi hari pada jam sekian akan memutar link stream untuk bagian live
     * dan pada malam hari pada jam sekian akan memutar link stream untuk bagian autodj
     **/


    MediaPlayer mediaPlayer;

    boolean prepared;
    boolean started;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Dari textview sampai singlestation dibawah ini untuk merubah font pada aplikasi
        TextView RadioBL = (TextView) findViewById(R.id.RadioBL);
        TextView SingleStation = (TextView) findViewById(R.id.SingleStation);
        Typeface typeFace=Typeface.createFromAsset(getAssets(),"fonts/orange juice 2.0.ttf");
        RadioBL.setTypeface(typeFace);
        SingleStation.setTypeface(typeFace);

        btn_play = (Button) findViewById(R.id.btn_play);
        btn_play.setEnabled(false);

        mediaPlayer = new MediaPlayer();
        //mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        new PlayerTask().execute(stream);

        btn_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(started){
                    started = false;
                    mediaPlayer.pause();
                }else{
                    started = true;
                    mediaPlayer.start();
                }
            }
        });
    }



    class PlayerTask extends AsyncTask <String, Void, Boolean> {
        @Override
        protected Boolean doInBackground(String... strings) {

            try {
                mediaPlayer.setDataSource(strings[0]);
                mediaPlayer.prepare();
                prepared = true;
            } catch (IOException e) {
                e.printStackTrace();
            }

            return prepared;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            //btn_play.setBackgroundColor(Color.parseColor("#009688"));
            btn_play.setEnabled(true);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(started){
            mediaPlayer.pause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(started){
            mediaPlayer.start();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(prepared){
            mediaPlayer.release();
        }
    }
}
