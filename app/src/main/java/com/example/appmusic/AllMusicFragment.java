package com.example.appmusic;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.util.ArrayList;

public class AllMusicFragment extends Fragment {
    ListView musicList;
    ArrayAdapter<String> musicArrAdapter;
    String songs[];
    ArrayList<File> musics;

    public AllMusicFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_all_music, container, false);

        musicList = view.findViewById(R.id.musicList);

        Dexter.withActivity(getActivity()).withPermission(Manifest.permission.READ_EXTERNAL_STORAGE).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                //hien thi music
                musics = findMusicFiles(Environment.getExternalStorageDirectory());
                songs = new String[musics.size()];

                for (int i=0;i<musics.size();i++){
                    songs[i] = musics.get(i).getName();
                }

                musicArrAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1,songs);
                musicList.setAdapter(musicArrAdapter);

                // Xu ly click vao bai hat trong list
                musicList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent player = new Intent(getActivity(),Player.class);

                        player.putExtra("songFileList",musics);
                        player.putExtra("position",position);
                        startActivity(player);
                    }
                });

            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {

            }
        }).check();

        return  view;
    }
    private  ArrayList<File> findMusicFiles(File file){
        ArrayList<File> allMusicFilesObject = new ArrayList<>();
        File files[] = file.listFiles();

        for (File currentFile: files) {
            if(currentFile.isDirectory() && !currentFile.isHidden()){
                allMusicFilesObject.addAll(findMusicFiles(currentFile));
            }else{
                //chi chon dinh dang duoi file nhu tren
                if(currentFile.getName().endsWith(".mp3") || currentFile.getName().endsWith(".mp4") || currentFile.getName().endsWith(".wav")){
                    allMusicFilesObject.add(currentFile);
                }

            }
        }
        return  allMusicFilesObject;
    }
}