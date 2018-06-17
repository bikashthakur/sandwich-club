package com.udacity.sandwichclub;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.udacity.sandwichclub.model.Sandwich;
import com.udacity.sandwichclub.utils.JsonUtils;

import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_POSITION = "extra_position";
    private static final int DEFAULT_POSITION = -1;

    private TextView textViewOtherNames;
    private TextView textViewOrigin;
    private TextView textViewDesc;
    private TextView textViewIngred;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        final ImageView ingredientsIv = (ImageView) findViewById(R.id.image_iv);
        final TextView textViewErrorMsg = (TextView) findViewById(R.id.img_download_error_tv);

        textViewOtherNames = (TextView) findViewById(R.id.also_known_tv);
        textViewOrigin = (TextView) findViewById(R.id.origin_tv);
        textViewDesc = (TextView) findViewById(R.id.description_tv);
        textViewIngred = (TextView) findViewById(R.id.ingredients_tv);

        Intent intent = getIntent();
        if (intent == null) {
            closeOnError();
        }

        int position = intent.getIntExtra(EXTRA_POSITION, DEFAULT_POSITION);
        if (position == DEFAULT_POSITION) {
            // EXTRA_POSITION not found in intent
            closeOnError();
            return;
        }

        String[] sandwiches = getResources().getStringArray(R.array.sandwich_details);
        String json = sandwiches[position];
        Sandwich sandwich = JsonUtils.parseSandwichJson(json);
        if (sandwich == null) {
            // Sandwich data unavailable
            closeOnError();
            return;
        }

        populateUI(sandwich);
        Picasso.with(this)
                .load(sandwich.getImage())
                .into(ingredientsIv, new Callback() {
                    @Override
                    public void onSuccess() {
                        textViewErrorMsg.setVisibility(View.INVISIBLE);
                        ingredientsIv.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onError() {
                        ingredientsIv.setVisibility(View.INVISIBLE);
                        textViewErrorMsg.setVisibility(View.VISIBLE);
                        Log.d("ImageDownloadError","The image could not be downloaded.");
                    }
                });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(sandwich.getMainName());
    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }

    private void populateUI(Sandwich sandwich) {

        textViewOrigin.setText(sandwich.getPlaceOfOrigin());
        textViewOtherNames.setText(getArrayListAsString(sandwich.getAlsoKnownAs()));
        textViewDesc.setText(sandwich.getDescription());
        textViewIngred.setText(getArrayListAsString(sandwich.getIngredients()));

    }

    private String getArrayListAsString(List<String> list) {
        String result = "";

        try {

            for (String s : list) {
                result += s + ", ";
            }
        } catch (Exception e) {
            Log.d("ERROR", e.getMessage());
        }

        return result.substring(0, result.length() - 2);//remove comma and space from the end of string
    }
}
