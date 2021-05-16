package com.example.newskribble;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentMusic extends Fragment {

    private static final String TAG = "FragmentMusic";
    static ArrayList<String> titles;
    static ArrayList<String> preview;
    static ArrayList<String> albumPics;
    static TextView title;
    ImageView nextImg, prevImg, playImg, pauseImg;
    static int songIdx = 0;
    static int maxIdx = 0;
    static MediaPlayer mp;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_music, container, false);


        title = (TextView) v.findViewById(R.id.musicTitle);
        playImg = (ImageView) v.findViewById(R.id.playBtn);
        pauseImg = (ImageView) v.findViewById(R.id.pauseBtn);

        nextImg = (ImageView) v.findViewById(R.id.nextBtn);
        prevImg = (ImageView) v.findViewById(R.id.prevBtn);
        pauseImg.setVisibility(View.INVISIBLE);


        RetrofitInterface ri = RetrofitClient.getRetrofitInstance()
                .create(RetrofitInterface.class);
        Call<MusicModel> call = ri.getAllData();

        call.enqueue(new Callback<MusicModel>() {
            @Override
            public void onResponse(Call<MusicModel> call, Response<MusicModel> response) {
                Log.e(TAG, "onResponse: code : " + response.code());
                ArrayList<MusicModel.data> data = response.body().getData();
                ArrayList<MusicModel.data> album = response.body().getData();

                titles = new ArrayList<>();
                preview = new ArrayList<>();
                albumPics = new ArrayList<>();

                for (MusicModel.data data1 : data) {
                    Log.e(TAG, "onResponse: All titles : " + data1.getTitle());
                    titles.add(data1.getTitle());
                    Log.e(TAG, "onResponse: All preview : " + data1.getPreview());
                    preview.add(data1.getPreview());

                }


                maxIdx = titles.size() - 1;


                title.setText(titles.get(songIdx));
                title.setMovementMethod(new ScrollingMovementMethod());
                mp = new MediaPlayer();
                mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
                try {
                    mp.setDataSource(preview.get(songIdx));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    mp.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                playImg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        playImg.setVisibility(View.INVISIBLE);
                        pauseImg.setVisibility(View.VISIBLE);
                        playMusic();
                    }
                });
                pauseImg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        playImg.setVisibility(View.VISIBLE);
                        pauseImg.setVisibility(View.INVISIBLE);
                        pauseMusic();
                    }
                });
                nextImg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        nextMusic();
                    }
                });
                prevImg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        prevMusic();
                    }
                });


            }

            @Override
            public void onFailure(Call<MusicModel> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage());

            }
        });

        return v;
    }

    public static void playMusic() {
        mp.start();
        mp.setLooping(true);
    }

    public static void pauseMusic() {
        mp.pause();
    }

    public static void nextMusic() {
        songIdx++;
        System.out.print("songIdx: " + songIdx);
        if (songIdx > maxIdx) {
            songIdx = 0;
            title.setText(titles.get(songIdx));
            title.setMovementMethod(new ScrollingMovementMethod());

        } else {
            title.setText(titles.get(songIdx));
            title.setMovementMethod(new ScrollingMovementMethod());
        }
        mp.reset();
        mp.start();
        mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            mp.setDataSource(preview.get(songIdx));
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            mp.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mp.start();

    }

    public static void prevMusic() {
        songIdx--;
        System.out.print("songIdx: " + songIdx);
        if (songIdx < 0) {
            songIdx = maxIdx;
            title.setText(titles.get(songIdx));

        } else {
            title.setText(titles.get(songIdx));
        }
        mp.reset();
        mp.start();
        mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            mp.setDataSource(preview.get(songIdx));
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            mp.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mp.start();

    }


    }




