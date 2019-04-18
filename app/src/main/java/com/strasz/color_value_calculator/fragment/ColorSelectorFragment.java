package com.strasz.color_value_calculator.fragment;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;
import com.strasz.color_value_calculator.R;
import com.strasz.color_value_calculator.TouchImageView;
import com.strasz.color_value_calculator.main.MainActivity;
import com.strasz.color_value_calculator.view.IColorSelectorView;
import com.strasz.color_value_calculator.viewmodel.ColorSelectorViewModel;

import java.io.File;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import io.reactivex.Observable;

public class ColorSelectorFragment extends BaseFragment implements IColorSelectorView {

    @BindView(R.id.main_image_container)
    TouchImageView mainImageView;
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
    @BindView(R.id.main_toolbar)
    Toolbar toolbar;

    MenuItem accessGalleryButton;
    private ColorSelectorViewModel viewModel = new ColorSelectorViewModel();
    private static int GALLERY = 1000;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((MainActivity) getActivity()).setSupportActionBar(toolbar);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.color_selector_fragment, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.color_selector_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);

        accessGalleryButton = menu.getItem(0);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_go_to_gallery) {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), GALLERY);

            return true;
        }
        return false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == getActivity().RESULT_OK) {
            if (requestCode == GALLERY) {
                Uri uri = data.getData();
                mainImageView.setImageURI(uri);
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        viewModel.init(this);

        bind();
    }

    private void bind() {

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


    public Observable<MotionEvent> getImageTouched() {
        return RxView.touches(mainImageView);
    }

    public Bitmap getCurrentImageBitmap() {
        mainImageView.setDrawingCacheEnabled(true);
        Bitmap currentScreenshot = Bitmap.createBitmap(mainImageView.getDrawingCache());
        mainImageView.setDrawingCacheEnabled(false);
        return currentScreenshot;
    }
}

