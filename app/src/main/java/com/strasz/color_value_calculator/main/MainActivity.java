package com.strasz.color_value_calculator.main;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;

import com.strasz.color_value_calculator.R;
import com.strasz.color_value_calculator.Utils.Utils;
import com.strasz.color_value_calculator.fragment.BaseFragment;
import com.strasz.color_value_calculator.fragment.ColorSelectorFragment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import butterknife.BindView;

public class MainActivity extends AppCompatActivity {

    private ColorSelectorFragment colorSelectorFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        showColorSelectorFragment();
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (!Utils.checkPermission(this.getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE)) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }

        if (!Utils.checkPermission(this.getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
        }
    }

    private void showColorSelectorFragment() {
        if (colorSelectorFragment == null) {
            colorSelectorFragment = new ColorSelectorFragment();
        }

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, colorSelectorFragment)
                .commit();
    }


}
