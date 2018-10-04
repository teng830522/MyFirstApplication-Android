package com.example.teng.myapplication;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.VideoView;

public class LatestNews extends android.support.v4.app.Fragment {

    private View view ;
    VideoView videoView ;
    MediaController controller ;
    int position ;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.tablayout_latest_news,container,false);
        return view ;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setVideoView();
    }

    private void setVideoView(){
        videoView = view.findViewById(R.id.videoView);
        controller = new MediaController(getActivity());
        videoView.setMediaController(controller);
        Uri uri = Uri.parse("android.resource://" + getActivity().getPackageName() + "/" + R.raw.black_hole);
        videoView.setVideoURI(uri);
//        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener(){
//
//            @Override
//            public void onPrepared(MediaPlayer mp) {
//                if(position == 0){
//                    mp.start();
//                }
//            }
//        });
    }
}
