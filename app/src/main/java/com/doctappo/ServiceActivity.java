package com.doctappo;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import util.CommonClass;

public class ServiceActivity extends CommonActivity {

ImageView btn_1, btn_2, btn_3, btn_4, btn_5 , btn_6 , btn_7;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CommonClass.initRTL(this, CommonClass.getLanguage(this));
        setContentView(R.layout.activity_service);
        setHeaderTitle(getString(R.string.services));

        btn_1 = findViewById(R.id.btnBowie);
        btn_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView txt = findViewById(R.id.txtBowie);
                if (txt.getMaxLines() == Integer.MAX_VALUE){
                    txt.setMaxLines(0);
                }
                else {
                    txt.setMaxLines(Integer.MAX_VALUE);
                    btn_1.setImageResource(R.drawable.up_arrow);
                }
            }
        });

        btn_2 = findViewById(R.id.btnGreenbelt);
        btn_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView txt = findViewById(R.id.txtGreenbelt);
                if (txt.getMaxLines() == Integer.MAX_VALUE)
                    txt.setMaxLines(0);
                else{
                    txt.setMaxLines(Integer.MAX_VALUE);
                    btn_2.setImageResource(R.drawable.up_arrow);
                }
            }
        });

        btn_3 = findViewById(R.id.btnOlney);
        btn_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView txt = findViewById(R.id.textOnley);
                if (txt.getMaxLines() == Integer.MAX_VALUE)
                    txt.setMaxLines(0);
                else {
                    txt.setMaxLines(Integer.MAX_VALUE);
                    btn_3.setImageResource(R.drawable.up_arrow);
                }
            }
        });

        btn_4 = findViewById(R.id.btnSilver);
        btn_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView txt = findViewById(R.id.textSilver);
                if (txt.getMaxLines() == Integer.MAX_VALUE)
                    txt.setMaxLines(0);
                else {
                    txt.setMaxLines(Integer.MAX_VALUE);
                    btn_4.setImageResource(R.drawable.up_arrow);
                }
            }
        });

        btn_5 = findViewById(R.id.btnRiverdale);
        btn_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView txt = findViewById(R.id.textRiverDale);
                if (txt.getMaxLines() == Integer.MAX_VALUE)
                    txt.setMaxLines(0);
                else {
                    txt.setMaxLines(Integer.MAX_VALUE);
                    btn_5.setImageResource(R.drawable.up_arrow);
                }
            }
        });

        btn_6 = findViewById(R.id.btnGlen);
        btn_6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView txt = findViewById(R.id.textGlen);
                if (txt.getMaxLines() == Integer.MAX_VALUE)
                    txt.setMaxLines(0);
                else {
                    txt.setMaxLines(Integer.MAX_VALUE);
                    btn_6.setImageResource(R.drawable.up_arrow);
                }
            }
        });

        btn_7 = findViewById(R.id.btnLaurel);
        btn_7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView txt = findViewById(R.id.textLaurel);
                if (txt.getMaxLines() == Integer.MAX_VALUE)
                    txt.setMaxLines(0);
                else {
                    txt.setMaxLines(Integer.MAX_VALUE);
                    btn_7.setImageResource(R.drawable.up_arrow);
                }
            }
        });

    }
}
