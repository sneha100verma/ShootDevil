package com.example.shootdevil;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.Nullable;

import android.os.Bundle;

public class GameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new GameView(this));
    }
}