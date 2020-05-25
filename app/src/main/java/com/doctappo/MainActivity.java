package com.doctappo;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import com.google.android.material.navigation.NavigationView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;


import java.util.ArrayList;

import Config.ApiParams;
import Config.ConstValue;
import adapters.CategoryAdapter;
import configfcm.MyFirebaseRegister;
import dialogues.LanguagePrfsDialog;
import models.CategoryModel;
import util.CommonClass;

public class MainActivity extends CommonActivity implements NavigationView.OnNavigationItemSelectedListener {

    private ArrayList<CategoryModel> categoryArray;
    private RecyclerView categoryRecyclerView;
    private CategoryAdapter categoryAdapter;
    private ProgressBar progressBar1;
    private Toolbar toolbar;
    private Button btnLanguage;
    private LanguagePrfsDialog languagePrfsDialog;

    ImageView imageInstagram, imageFacebook, imageTwitter;
    TextView website, register;
    RelativeLayout services, patLogin, appointment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CommonClass.initRTL(this, CommonClass.getLanguage(this));
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);

        setHeaderTitle(getString(R.string.app_name));

        categoryArray = new ArrayList<>();
        languagePrfsDialog = new LanguagePrfsDialog(this);

        if (!common.getSessionBool("fcm_registered") && common.is_user_login()) {
            MyFirebaseRegister fireReg = new MyFirebaseRegister(this);
            fireReg.RegisterUser(common.get_user_id());
        }

        TextView textView1 = (TextView) findViewById(R.id.textView);
        textView1.setTypeface(getCustomFont());

        register =  findViewById(R.id.registerTxt);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        services = findViewById(R.id.servicesLay);
        services.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ServiceActivity.class);
                startActivity(intent);
            }
        });

        patLogin = findViewById(R.id.patLogin);
        patLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        appointment = findViewById(R.id.makeAppointment);
        appointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ThanksActivity.class);
                startActivity(intent);
            }
        });
        imageInstagram = findViewById(R.id.instagram);
        imageInstagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://www.instagram.com/startptnow/";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

        imageFacebook = findViewById(R.id.facebook);
        imageFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String url = "https://www.facebook.com/StartPTNow/";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

        imageTwitter = findViewById(R.id.twitter);
        imageTwitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://twitter.com/startptnow";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

        website = findViewById(R.id.idWeb);
        website.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://startptnow.com/";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });
    }

    // get xml element from xml file
    public void bindView() {
        categoryRecyclerView = (RecyclerView) findViewById(R.id.rv_artist);
        GridLayoutManager layoutManager
                = new GridLayoutManager(this, 3);
        categoryRecyclerView.setLayoutManager(layoutManager);

        categoryAdapter = new CategoryAdapter(this, categoryArray);
        categoryRecyclerView.setAdapter(categoryAdapter);
        progressBar1 = (ProgressBar) findViewById(R.id.progressBar1);
        setProgressBarAnimation(progressBar1);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();


    }

    // show language dialog
    public void chooseLanguage(View view) {
        languagePrfsDialog.show();
    }

    @Override
    protected void onResume() {
        CommonClass.initRTL(this, CommonClass.getLanguage(this));

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View navHeader = navigationView.getHeaderView(0);
        ((TextView) navHeader.findViewById(R.id.menu_title)).setTypeface(common.getCustomFont());

        btnLanguage = (Button) navHeader.findViewById(R.id.languagebtn);
        btnLanguage.setText(CommonClass.getLanguage(this));
        if (ConstValue.ENABLE_SPANISH_LANG_TO_CHOOSE) {
            btnLanguage.setVisibility(View.VISIBLE);
        } else {
            btnLanguage.setVisibility(View.GONE);
        }
        navigationView.setNavigationItemSelectedListener(this);
        Menu nav_Menu = navigationView.getMenu();
        if (!common.is_user_login()) {

            nav_Menu.findItem(R.id.nav_appointment).setVisible(false);
            nav_Menu.findItem(R.id.nav_logout).setVisible(false);
            nav_Menu.findItem(R.id.nav_password).setVisible(false);
            nav_Menu.findItem(R.id.nav_profile).setVisible(false);
            nav_Menu.findItem(R.id.nav_login).setVisible(true);
            navHeader.findViewById(R.id.txtFullName).setVisibility(View.GONE);
            navHeader.findViewById(R.id.textEmailId).setVisibility(View.GONE);
        } else {
            nav_Menu.findItem(R.id.nav_appointment).setVisible(true);
            nav_Menu.findItem(R.id.nav_logout).setVisible(true);
            nav_Menu.findItem(R.id.nav_password).setVisible(true);
            nav_Menu.findItem(R.id.nav_profile).setVisible(true);
            nav_Menu.findItem(R.id.nav_login).setVisible(false);
            navHeader.findViewById(R.id.txtFullName).setVisibility(View.VISIBLE);
            navHeader.findViewById(R.id.textEmailId).setVisibility(View.VISIBLE);
            ((TextView) navHeader.findViewById(R.id.txtFullName)).setText(common.getSession(ApiParams.USER_FULLNAME));
            ((TextView) navHeader.findViewById(R.id.textEmailId)).setText(common.getSession(ApiParams.USER_EMAIL));
        }
        super.onResume();
    }

    // load category in gridview
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        //inflater.inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_location:

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.

        int id = item.getItemId();

        if (id == R.id.nav_profile) {
            Intent intent = new Intent(MainActivity.this, UpdateProfileActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_password) {
            Intent intent = new Intent(MainActivity.this, ChangePasswordActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_appointment) {
            Intent intent = new Intent(MainActivity.this, MyAppointmentsActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_logout) {
            common.logOut();
        } else if (id == R.id.nav_login) {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_share) {
            shareApp();
        } else if (id == R.id.nav_rating) {
            reviewOnApp();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    // search click event
    public void searchViewClick(View view) {
        Intent intent = new Intent(MainActivity.this, SearchActivity.class);
        startActivity(intent);
    }

    // share app link to shareable apps
    public void shareApp() {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "Hi friends i am using ." + " http://play.google.com/store/apps/details?id=" + getPackageName() + " APP");
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }

    // review intent to redirect on playstore
    public void reviewOnApp() {
        Uri uri = Uri.parse("market://details?id=" + getPackageName());
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        // To count with Play market backstack, After pressing back button,
        // to taken back to our application, we need to add following flags to intent.
        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        try {
            startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + getPackageName())));
        }
    }
}
