package com.navaneethsaj.englishkeyboard;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void Setting(View view){
        startActivity(new Intent("android.settings.INPUT_METHOD_SETTINGS"));
    }

    public void Picker(View view){
        InputMethodManager imeManager = (InputMethodManager)
                getApplicationContext().getSystemService(INPUT_METHOD_SERVICE);
        if (imeManager != null) {
            imeManager.showInputMethodPicker();
        }
    }
}
