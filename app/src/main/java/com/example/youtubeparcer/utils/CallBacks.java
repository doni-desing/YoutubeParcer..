package com.example.youtubeparcer.utils;

public interface CallBacks {

    interface playerCallBack {
        void onItemClickOnItem(int albumId);

        void onPlayingEnd();
    }
}
