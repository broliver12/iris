package com.strasz.color_value_calculator.fragment;

import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.squareup.picasso.Picasso;
import com.strasz.color_value_calculator.R;
import com.strasz.color_value_calculator.view.IColorSelectorView;
import com.strasz.color_value_calculator.viewmodel.ColorSelectorViewModel;

import java.io.File;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import io.reactivex.Observable;

public class ColorSelectorFragment extends BaseFragment implements IColorSelectorView {

    @BindView(R.id.main_image_container)
    ImageView mainImageView;
    @BindView(R.id.selected_color_view)
    TextView selectedColorView;
    @BindView(R.id.x_value_text)
    TextView xValueText;
    @BindView(R.id.y_value_text)
    TextView yValueText;
    @BindView(R.id.hex_result)
    TextView hexResultTextView;
    @BindView(R.id.rgb_result)
    TextView rgbResultTextView;
    @BindView(R.id.cmyk_result)
    TextView cmykResultTextView;
    @BindView(R.id.selector_main_container)
    ConstraintLayout mainContainer;

    private ColorSelectorViewModel viewModel = new ColorSelectorViewModel();
    private int xval;
    private int yval;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.color_selector_fragment, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        Picasso.get().load(R.drawable.wwwo_cvs).fit().centerCrop().into(mainImageView);

        viewModel.init(this);

        bind();
    }

    private void bind() {
        selectedColorView.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark, null));


        viewModel.xValueText()
                .subscribe(x -> xValueText.setText(x));

        viewModel.yValueText()
                .subscribe(x -> yValueText.setText(x));

        viewModel.rgbText()
                .subscribe(x -> rgbResultTextView.setText(x));

        viewModel.pixelColorInt()
                .subscribe(x -> selectedColorView.setBackgroundColor(x));

        viewModel.hexText()
                .subscribe(x -> hexResultTextView.setText(x));

        viewModel.cmykText()
                .subscribe(x -> cmykResultTextView.setText(x));
    }


    public Observable<MotionEvent> getImageClicked() {
        return RxView.touches(mainImageView).filter(event -> event.getAction() == MotionEvent.ACTION_DOWN);
    }

    public Bitmap getCurrentImageBitmap() {
        mainImageView.setDrawingCacheEnabled(true);
        Bitmap currentScreenshot = Bitmap.createBitmap(mainImageView.getDrawingCache());
        mainImageView.setDrawingCacheEnabled(false);
        return currentScreenshot;
    }
}

