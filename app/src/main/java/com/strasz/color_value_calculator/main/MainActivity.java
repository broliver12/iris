package com.strasz.color_value_calculator.main;

import android.os.Bundle;

import com.strasz.color_value_calculator.R;
import com.strasz.color_value_calculator.fragment.BaseFragment;
import com.strasz.color_value_calculator.fragment.ColorSelectorFragment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import butterknife.BindView;

public class MainActivity extends AppCompatActivity {

    private ColorSelectorFragment colorSelectorFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        showColorSelectorFragment();
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
