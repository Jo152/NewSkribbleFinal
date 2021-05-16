package com.example.newskribble;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.util.Locale;

public class LocalisationFragment extends Fragment {

    ImageView engFlag, freFlag;
    TextView selectLanguage;
    int selectedFlag = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_localisation_fragment, container, false);

        engFlag = (ImageView)v.findViewById(R.id.engFlag);
        freFlag = (ImageView)v.findViewById(R.id.freFlag);
        selectLanguage = (TextView)v.findViewById(R.id.selectLangText);

        engFlag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                engFlag.setBackgroundResource(R.color.blue);
                freFlag.setBackgroundResource(R.color.empty);
                selectedFlag = 1;
            }
        });
        freFlag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                freFlag.setBackgroundResource(R.color.blue);
                engFlag.setBackgroundResource(R.color.empty);
                selectedFlag = 2;

            }
        });


        selectLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectedFlag == 1){
                    setLocal(v, "en");
                    getFragmentManager().beginTransaction().replace(R.id.fragment_container, new LocalisationFragment()).commit();
                }
                else if(selectedFlag == 2){
                    setLocal(v, "fr");
                    getFragmentManager().beginTransaction().replace(R.id.fragment_container, new LocalisationFragment()).commit();
                }
                else{
                    Toast.makeText(v.getContext(), "Please Select A Language", Toast.LENGTH_LONG).show();
                }
            }
        });






        return v;
    }

    public  void setLocal(View myView, String langCode) {
        Locale locale = new Locale(langCode);
        locale.setDefault(locale);


        Resources resources = myView.getResources();
        Configuration configuration = resources.getConfiguration();
        configuration.setLocale(locale);
        resources.updateConfiguration(configuration,resources.getDisplayMetrics());

    }

}