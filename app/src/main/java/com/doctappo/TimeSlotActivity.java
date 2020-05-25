package com.doctappo;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import Config.ApiParams;
import Config.ConstValue;
import dialogues.DoctorChooseDialog;
import fragments.TimeSlotFragment;
import models.ActiveModels;
import models.BusinessModel;
import models.DoctorModel;
import models.PhotosModel;
import models.TimeSlotModel;
import util.CommonClass;
import util.NameValuePair;
import util.RoundedImageView;
import util.VJsonRequest;

public class TimeSlotActivity extends CommonActivity {

    private BusinessModel selected_business;
    private TextView textSalonName;
    private RoundedImageView imageLogo;

    private TabLayout tabLayout;
    private Calendar calender;
    private int c_day;
    private int c_month;
    private int c_year;
    private SimpleDateFormat df, df2;
    private String currentdate;
    private Button buttonChooseDate;
    private Button buttonChooseDoctor;
    private ViewPager viewPager;
    //HashMap<String,String> timeslotdata;
    private SampleFragmentPagerAdapter sampleFragmentPagerAdapter;
    private ArrayList<DoctorModel> mDoctorArray;

    public static TimeSlotModel timeSlotModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_slot);
        allowBack();
        setHeaderTitle(getString(R.string.appointment_time));
        mDoctorArray = new ArrayList<>();
        textSalonName = (TextView) findViewById(R.id.textSalonName);
        imageLogo = (RoundedImageView) findViewById(R.id.salonImage);
        textSalonName.setTypeface(common.getCustomFont());

        selected_business = ActiveModels.BUSINESS_MODEL;
        Picasso.with(this).load(ConstValue.BASE_URL + "/uploads/business/" + selected_business.getBus_logo()).into(imageLogo);
        textSalonName.setText(Html.fromHtml(selected_business.getBus_title()));

        calender = Calendar.getInstance(TimeZone.getDefault());
        c_day = calender.get(Calendar.DAY_OF_MONTH);
        c_month = calender.get(Calendar.MONTH) + 1;
        c_year = calender.get(Calendar.YEAR);
        //df = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        df2 = new SimpleDateFormat("HH:mm aa", Locale.ENGLISH);
        currentdate = c_year + "-" + c_month + "-" + c_day;
        //df.format(calender.getTime());

        buttonChooseDate = (Button) findViewById(R.id.buttonChooseDate);
        buttonChooseDate.setText(currentdate);

        buttonChooseDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateDialog();
            }
        });

        buttonChooseDoctor = (Button) findViewById(R.id.buttonChooseDoctor);
        buttonChooseDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mDoctorArray != null && mDoctorArray.size() > 0) {
                    DoctorChooseDialog doctorChooseDialog = new DoctorChooseDialog(TimeSlotActivity.this, mDoctorArray);
                    doctorChooseDialog.show();
                }
            }
        });

        loadDoctorData();

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        //new getTimeSlotTask().execute();

        TextView txtTotalTime = (TextView) findViewById(R.id.totalTime);
        TextView txtTotalAmount = (TextView) findViewById(R.id.totalAmount);
        txtTotalAmount.setText(ConstValue.CURRENCY + (common.get_service_total_amount() + Double.valueOf(selected_business.getBus_fee())));
        txtTotalTime.setText(common.get_service_total_times_string());

    }

    // load doctor from api
    public void loadDoctorData() {
        HashMap<String, String> params = new HashMap<>();
        params.put("bus_id", selected_business.getBus_id());
        // this class for handle request response thread and return response data
        VJsonRequest vJsonRequest = new VJsonRequest(TimeSlotActivity.this, ApiParams.GET_DOCTORS, params,
                new VJsonRequest.VJsonResponce() {
                    @Override
                    public void VResponce(String responce) {

                        Gson gson = new Gson();
                        Type listType = new TypeToken<List<DoctorModel>>() {
                        }.getType();
                        mDoctorArray.clear();
                        mDoctorArray.addAll((Collection<? extends DoctorModel>) gson.fromJson(responce, listType));
                        if (mDoctorArray != null && mDoctorArray.size() > 0) {
                            ActiveModels.DOCTOR_MODEL = mDoctorArray.get(0);

                            loadSlotTask();

                        }
                        //progressBar1.setVisibility(View.GONE);

                    }

                    @Override
                    public void VError(String responce) {
                        //progressBar1.setVisibility(View.GONE);
                    }
                });


    }

    // load time slot from api
    public void loadSlotTask() {
        buttonChooseDoctor.setText(ActiveModels.DOCTOR_MODEL.getDoct_name() + "(" + ActiveModels.DOCTOR_MODEL.getDoct_degree() + ")");
        HashMap<String, String> params = new HashMap<>();
        params.put("bus_id", selected_business.getBus_id());
        params.put("doct_id", ActiveModels.DOCTOR_MODEL.getDoct_id());
        params.put("date", currentdate);

        // this class for handle request response thread and return response data
        VJsonRequest vJsonRequest = new VJsonRequest(this, ApiParams.TIME_SLOT_URL, params,
                new VJsonRequest.VJsonResponce() {
                    @Override
                    public void VResponce(String responce) {
                        try {
                            Object json = new JSONTokener(responce).nextValue();
                            if (json instanceof JSONObject) {


                                Gson gson = new Gson();
                                Type listType = new TypeToken<TimeSlotModel>() {
                                }.getType();
                                timeSlotModel = gson.fromJson(responce, listType);

                                sampleFragmentPagerAdapter = new SampleFragmentPagerAdapter(getSupportFragmentManager());
                                viewPager.setAdapter(sampleFragmentPagerAdapter);
                                tabLayout.setupWithViewPager(viewPager);

                            } else {
                                Toast.makeText(getApplicationContext(), getString(R.string.not_timetable_set), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void VError(String responce) {
                        //progressBar1.setVisibility(View.GONE);
                    }
                });

    }

    // show date picker dialog
    public void DateDialog() {

        DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                currentdate = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                //txtDate.setText(currentdate);
                c_year = year;
                c_day = dayOfMonth;
                c_month = monthOfYear + 1;
                buttonChooseDate.setText(currentdate);
                if (mDoctorArray != null && mDoctorArray.size() > 0) {
                    loadSlotTask();
                }

            }
        };

        DatePickerDialog dpDialog = new DatePickerDialog(this, listener, c_year, c_month - 1, c_day);

        final Calendar minDate = Calendar.getInstance();
        minDate.setTimeInMillis(new Date().getTime());
        minDate.set(Calendar.HOUR_OF_DAY, minDate.getMinimum(Calendar.HOUR_OF_DAY));
        minDate.set(Calendar.MINUTE, minDate.getMinimum(Calendar.MINUTE));
        minDate.set(Calendar.SECOND, minDate.getMinimum(Calendar.SECOND));
        minDate.set(Calendar.MILLISECOND, minDate.getMinimum(Calendar.MILLISECOND));

        dpDialog.getDatePicker().setMinDate(minDate.getTimeInMillis());

        dpDialog.show();

    }

    /*private final Handler handler = new Handler() {
        public void handleMessage(Message message) {
            if (message.getData().containsKey(ApiParams.PARM_RESPONCE)){
                if (message.getData().getBoolean(ApiParams.PARM_RESPONCE)){
                    ArrayList<HashMap<String,String>> loginArray =  (ArrayList<HashMap<String,String>>) message.getData().getSerializable(ApiParams.PARM_DATA);
                    if(loginArray != null && loginArray.size() > 0) {
                        timeslotdata = loginArray.get(0);

                        sampleFragmentPagerAdapter = new SampleFragmentPagerAdapter(getSupportFragmentManager());
                        viewPager.setAdapter(sampleFragmentPagerAdapter);
                        tabLayout.setupWithViewPager(viewPager);
                    }


                }else{
                    common.setToastMessage(message.getData().getString(ApiParams.PARM_ERROR));
                }
            }


        }
    };
    */
    /*private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_service);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_contact);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_chat_s);
        tabLayout.getTabAt(3).setIcon(R.drawable.ic_image);
    }*/
    public class SampleFragmentPagerAdapter extends FragmentStatePagerAdapter {
        final int PAGE_COUNT = 3;
        private String tabTitles[] = new String[]{getString(R.string.tab_morning), getString(R.string.tab_afternoon), getString(R.string.tab_evening)};
        //private int tabIcons[] = {R.drawable.ic_service, R.drawable.ic_contact, R.drawable.ic_chat_s, R.drawable.ic_image};

        public SampleFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return PAGE_COUNT;
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = new TimeSlotFragment();


            Bundle args = new Bundle();
            args.putString("date", currentdate);
            args.putInt("position", position);
            fragment.setArguments(args);

            return fragment;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            // Generate title based on item position
            /*Drawable image =  ContextCompat.getDrawable(getApplicationContext(), tabIcons[0]) ;//getApplicationContext().getResources().getDrawable(tabIcons[0],getApplicationContext().getTheme()); //ContextCompat.getDrawable(getApplicationContext(), tabIcons[position]);
            image.setBounds(0, 0, image.getIntrinsicWidth(), image.getIntrinsicHeight());
            // Replace blank spaces with image icon
            SpannableString sb = new SpannableString("   " + tabTitles[position]);
            ImageSpan imageSpan = new ImageSpan(image, ImageSpan.ALIGN_BOTTOM);
            sb.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            return sb;
            */

            return tabTitles[position];
        }
    }

}
