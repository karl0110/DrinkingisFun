package com.jaimehall.drinkingisfun.minigames;

public interface MiniGameInterface {

    boolean isSurfaceView();
    MiniGameType getMiniGameType();
    void onPause();
    void onResume();
}
