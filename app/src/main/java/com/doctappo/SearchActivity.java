package com.doctappo;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

public class SearchActivity extends CommonActivity {

    private SeekBar seekBar;
    private TextView txtArea;
    private Switch switch1;
    private EditText searchText;
    private Bundle bundleloclaity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        allowBack();
        setHeaderTitle(getString(R.string.hint_search));

        txtArea = (TextView) findViewById(R.id.textArea);
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        switch1 = (Switch) findViewById(R.id.switch1);
        searchText = (EditText) findViewById(R.id.search_keyword);

        seekBar.setMax(100);
        txtArea.setText(common.getSessionInt("radius") + getString(R.string.KM));
        seekBar.setProgress(common.getSessionInt("radius"));

        if (!common.containKeyInSession("nearby_enable")) {
            common.setSessionBool("nearby_enable", true);
        }
        switch1.setChecked(common.getSessionBool("nearby_enable"));
        switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                common.setSessionBool("nearby_enable", b);
            }
        });
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                common.setSessionInt("radius", i);
                txtArea.setText(common.getSessionInt("radius") + getString(R.string.KM));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    // search button click event
    public void SearchButtonClick(View view) {

        Intent intent = new Intent(SearchActivity.this, ListActivity.class);
        Bundle b = new Bundle();
        if (!TextUtils.isEmpty(searchText.getText())) {
            b.putString("search", searchText.getText().toString());
        }
        if (bundleloclaity != null) {
            b.putString("lat", bundleloclaity.getString("latitude"));
            b.putString("lon", bundleloclaity.getString("longitude"));
            b.putString("locality", bundleloclaity.getString("locality"));
            b.putString("locality_id", bundleloclaity.getString("locality_id"));
        }
        intent.putExtras(b);
        startActivity(intent);

    }

    // locality click event
    public void LocalityViewClick(View view) {
        Intent intent = new Intent(SearchActivity.this, LocationActivity.class);

        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            ((TextView) findViewById(R.id.chooseLocality)).setText(data.getExtras().getString("locality"));
            bundleloclaity = data.getExtras();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}
