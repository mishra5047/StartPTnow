package dialogues;

import android.app.Activity;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.doctappo.R;
import com.doctappo.TimeSlotActivity;

import java.util.ArrayList;

import adapters.DoctorAdapter;
import models.ActiveModels;
import models.DoctorModel;
import util.RecyclerItemClickListener;


/**
 * Created by Dell on 21-11-2016.
 */

public class DoctorChooseDialog extends Dialog {

    private Activity mContext;
    private SharedPreferences sharedPreferences;
    private ArrayList<DoctorModel> mDoctorArray;
    private DoctorAdapter doctorAdapter;
    private RecyclerView recyclerView;

    public DoctorChooseDialog(final Activity context, ArrayList<DoctorModel> arrayList) {
        super(context);
        this.requestWindowFeature(1);
        this.setContentView(R.layout.dialog_doctors);
        this.setCanceledOnTouchOutside(true);
        this.setCancelable(true);
        this.mContext = context;
        mDoctorArray = arrayList;

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        try {
            getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        } catch (Exception ex) {

        }

        recyclerView = (RecyclerView) this.findViewById(R.id.rv_list);
        final LinearLayoutManager layoutManager
                = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(context, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                ActiveModels.DOCTOR_MODEL = mDoctorArray.get(position);
                if (mContext instanceof TimeSlotActivity) {
                    ((TimeSlotActivity) mContext).loadSlotTask();
                }
                dismiss();
            }
        }));

        doctorAdapter = new DoctorAdapter(context, mDoctorArray);
        recyclerView.setAdapter(doctorAdapter);
    }


    public void dismiss() {
        super.dismiss();
    }

}
