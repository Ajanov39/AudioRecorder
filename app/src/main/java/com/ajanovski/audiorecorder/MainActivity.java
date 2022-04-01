package com.ajanovski.audiorecorder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContextWrapper;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    Button btnRec,btnStop,btnPlay;
    private static int MIC_PERMISSION_CODE = 200;
    MediaRecorder mediaRec;
    MediaPlayer mediaPlay;
    TextView txRec;
    ImageButton btnVideo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(isMicPresent()){
            getMicPermission();
        }

        btnRec = findViewById(R.id.btnRec);
        btnStop = findViewById(R.id.btnStop);
        btnPlay = findViewById(R.id.btnPlay);
        txRec = findViewById(R.id.txRec);
        btnVideo = findViewById(R.id.btnVideo);
        txRec.setText(getRecFilePath());

        btnVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


        txRec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlay.start();
            }
        });


        btnRec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try{
                    mediaRec = new MediaRecorder();
                    mediaRec.setAudioSource(MediaRecorder.AudioSource.MIC);
                    mediaRec.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                    mediaRec.setOutputFile(getRecFilePath());
                    mediaRec.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                    mediaRec.prepare();
                    mediaRec.start();



                } catch (Exception e){
                    Toast.makeText(MainActivity.this, e.getLocalizedMessage(),Toast.LENGTH_LONG).show();
                }
            }
        });

        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            mediaRec.stop();
            mediaRec.release();
            mediaRec=null;
                Toast.makeText(MainActivity.this, "Rec stoped",Toast.LENGTH_LONG).show();
            }
        });

        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try{
                    mediaPlay = new MediaPlayer();
                    mediaPlay.setDataSource(getRecFilePath());
                    mediaPlay.prepare();
                    mediaPlay.start();
                    Toast.makeText(MainActivity.this, "Play started",Toast.LENGTH_LONG).show();
                }catch (Exception e){
                    Toast.makeText(MainActivity.this, e.getLocalizedMessage(),Toast.LENGTH_LONG).show();
                }


            }
        });

    }

    private boolean isMicPresent(){
        if(this.getPackageManager().hasSystemFeature(PackageManager.FEATURE_MICROPHONE)){
            return true;
        } else {
            return false;
        }
    }

    private void getMicPermission(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)==PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO},MIC_PERMISSION_CODE );
        }
    }

    // context wrapper PATH
    private  String getRecFilePath(){
        ContextWrapper contextWrapper = new ContextWrapper(getApplicationContext());
        File musicDirectory = contextWrapper.getExternalFilesDir(Environment.DIRECTORY_MUSIC);
        File file = new File(musicDirectory, "testRecFile" + ".mp3");
        return file.getPath();
    }



}