package com.example.appmusic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

public class Player extends AppCompatActivity {

    Bundle  songExtraData;
    ArrayList<File> songFileList;
    int position;
    SeekBar mSeekBar;
    TextView songTitle;
    ImageView prevBtn;
    ImageView playBtn;
    ImageView nextBtn;
    MediaPlayer mediaPlayer;
    TextView startTime;
    TextView totalTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        mSeekBar = findViewById(R.id.seekBar);
        songTitle = findViewById(R.id.songTitle);
        prevBtn = findViewById(R.id.prevBtn);
        playBtn = findViewById(R.id.playBtn);
        nextBtn = findViewById(R.id.nextBtn);
        startTime = findViewById(R.id.startTime);
        totalTime = findViewById(R.id.totalTime);


        if(mediaPlayer != null){
            mediaPlayer.stop();
        }

        Intent songData = getIntent();
        songExtraData = songData.getExtras();

        songFileList = (ArrayList) songExtraData.getParcelableArrayList("songFileList");

       position = songExtraData.getInt("position",0);
        initMusicPlayer(position);


        // Cai dat nut Pause
        playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //goi ham play()
                play();
            }
        });

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(position < songFileList.size()-1){
                    position++;
                }else{
                    position = 0;
                }
                initMusicPlayer(position);
            }
        });

        prevBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(position <= 0){
                    position = songFileList.size();
                }else {
                    position--;
                }
                initMusicPlayer(position);
            }
        });

    }

    private void initMusicPlayer(final int position){
        if(mediaPlayer != null && mediaPlayer.isPlaying()){
            mediaPlayer.reset();
        }

        // lay ten nhac tu list hien thi TView
        String name = songFileList.get(position).getName();
        songTitle.setText(name);

        // Lay duong dan nhac tu Dien Thoai
        Uri songResouseUri = Uri.parse(songFileList.get(position).toString());

        // tao moi trinh phat nhac
        mediaPlayer = MediaPlayer.create(getApplicationContext(),songResouseUri);

        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {

                //thiet lap thoi luong toi da cho thanh seekbar
                mSeekBar.setMax(mediaPlayer.getDuration());

                String tottTime = realTime(mediaPlayer.getDuration());
                totalTime.setText(tottTime);

                mediaPlayer.start();

                // thiet lap icon pause
                playBtn.setImageResource(R.drawable.pause_ic);
            }
        });

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                // khi bai nhac ket thuc
                playBtn.setImageResource(R.drawable.play_icon);
            }
        });

        // setting seekbar khi thay doi(tua)
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser){
                    mediaPlayer.seekTo(progress);
                    mSeekBar.setProgress(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        // thiet lap thanh seekbar chay theo thoi gian bai het
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (mediaPlayer != null){
                    try {
                        if(mediaPlayer.isPlaying()){
                            Message message = new Message();
                            message.what = mediaPlayer.getCurrentPosition();
                            handler.sendMessage(message);
                            Thread.sleep(100);
                        }
                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }
                }
            }
        }).start();

    }


    //Handler như là một cơ chế cao cấp để xử lý hàng đợi. Việc hàng đợi này chứa Messages hay Runnables,
    // hay việc chúng nên được xử lý trên main thread hay các background thread không quan trọng.
    //Handler vẫn sẽ được tạo ra để xử lý đống Messages này, từng cái một. Và đây chính là điều cần phải nhớ.

    private Handler handler = new Handler(){
        @SuppressLint("HandlerLeak")
        @Override
        public void handleMessage(Message msg) {
            startTime.setText(realTime(msg.what));
            mSeekBar.setProgress(msg.what);
        }
    };

    private String realTime(int duration){
        String timerTV =  "";
        int min = duration / 1000 / 60;
        int sec = duration / 1000 % 60;

        timerTV += min+":";
        if(sec < 10 ){
            timerTV+="0";
        }
        timerTV += sec;
        return  timerTV;
    }

    private void play(){
        if( mediaPlayer != null && mediaPlayer.isPlaying()){
            mediaPlayer.pause();
            playBtn.setImageResource(R.drawable.play_icon);
        }else{
            mediaPlayer.start();
            playBtn.setImageResource(R.drawable.pause_ic);
        }
    }
}