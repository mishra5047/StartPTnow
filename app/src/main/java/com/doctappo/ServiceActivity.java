package com.doctappo;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import util.CommonClass;

public class ServiceActivity extends CommonActivity {

    LinearLayout lay_1, lay_2, lay_3, lay_4, lay_5, lay_6, lay_7;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CommonClass.initRTL(this, CommonClass.getLanguage(this));
        setContentView(R.layout.activity_service);
        setHeaderTitle(getString(R.string.services));


        lay_1 = findViewById(R.id.bowie);
        lay_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView txt = findViewById(R.id.txtBowie);
                if (txt.getMaxLines() == Integer.MAX_VALUE)
                    txt.setMaxLines(0);
                else txt.setMaxLines(Integer.MAX_VALUE);
            }
        });

        lay_2 = findViewById(R.id.greenbelt);
        lay_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView txt = findViewById(R.id.txtGreenbelt);
                if (txt.getMaxLines() == Integer.MAX_VALUE)
                    txt.setMaxLines(0);
                else txt.setMaxLines(Integer.MAX_VALUE);
            }
        });

        lay_3 = findViewById(R.id.onley);
        lay_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView txt = findViewById(R.id.txtolney);
                if (txt.getMaxLines() == Integer.MAX_VALUE)
                    txt.setMaxLines(0);
                else txt.setMaxLines(Integer.MAX_VALUE);
            }
        });

        lay_4 = findViewById(R.id.silver_spring);
        lay_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView txt = findViewById(R.id.txtsilver);
                if (txt.getMaxLines() == Integer.MAX_VALUE)
                    txt.setMaxLines(0);
                else txt.setMaxLines(Integer.MAX_VALUE);
            }
        });

        lay_5 = findViewById(R.id.riverdale);
        lay_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView txt = findViewById(R.id.txtriverDale);
                if (txt.getMaxLines() == Integer.MAX_VALUE)
                    txt.setMaxLines(0);
                else txt.setMaxLines(Integer.MAX_VALUE);
            }
        });

        lay_6 = findViewById(R.id.glen_burnie);
        lay_6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView txt = findViewById(R.id.txtburnie);
                if (txt.getMaxLines() == Integer.MAX_VALUE)
                    txt.setMaxLines(0);
                else txt.setMaxLines(Integer.MAX_VALUE);
            }
        });

        lay_7 = findViewById(R.id.laurel);
        lay_7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView txt = findViewById(R.id.textlaurel);
                if (txt.getMaxLines() == Integer.MAX_VALUE)
                    txt.setMaxLines(0);
                else txt.setMaxLines(Integer.MAX_VALUE);
            }
        });

    }
}
