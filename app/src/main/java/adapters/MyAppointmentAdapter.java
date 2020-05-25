package adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;
import com.doctappo.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import Config.ApiParams;
import models.AppointmentModel;
import util.CommonClass;
import util.VJsonRequest;

/**
 * Created by Rajesh on 2018-05-05.
 */

public class MyAppointmentAdapter extends RecyclerSwipeAdapter<MyAppointmentAdapter.ProductHolder> {

    ArrayList<AppointmentModel> arrayList;
    Activity activity;
    CommonClass common;

    public MyAppointmentAdapter(Activity activity, ArrayList<AppointmentModel> arrayList) {
        this.arrayList = arrayList;
        this.activity = activity;
        common = new CommonClass(activity);
    }

    @Override
    public ProductHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;

        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_appointment, parent, false);

        return new ProductHolder(view);
    }

    @Override
    public void onBindViewHolder(final ProductHolder holder, final int position) {
        final AppointmentModel appointment = arrayList.get(position);

        holder.txtDate.setText(parseDateToddMM(appointment.getAppointment_date()));
        holder.txtTime.setText(CommonClass.parseTime(appointment.getStart_time()));
        holder.txtName.setText(appointment.getApp_name());
        holder.txtPhone.setText(appointment.getApp_phone());

        holder.txtClinic.setText(appointment.getBus_title());
        holder.txtClinicAddress.setText(appointment.getDoct_name());
        holder.txtClinicPhone.setText(appointment.getDoct_degree());
        if (appointment.getPayment_ref() != null && !appointment.getPayment_ref().equalsIgnoreCase("")) {
            holder.payment.setText(R.string.paid);
        } else {
            holder.payment.setText(R.string.unpaid);
        }

        holder.swipeLayout.setShowMode(SwipeLayout.ShowMode.LayDown);
        holder.iv_Delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mItemManger.removeShownLayouts(holder.swipeLayout);
                mItemManger.closeAllItems();

                //if (appointment.getStatus().equalsIgnoreCase("0")) {
                deleteRow(position);
                //}
            }
        });

        if (appointment.getApp_note() != null && !appointment.getApp_note().isEmpty()) {
            holder.view_note.setVisibility(View.VISIBLE);
            holder.ll_note.setVisibility(View.VISIBLE);
            holder.tv_note.setText(appointment.getApp_note());
        } else {
            holder.view_note.setVisibility(View.GONE);
            holder.ll_note.setVisibility(View.GONE);
        }

        mItemManger.bindView(holder.itemView, position);

        holder.setIsRecyclable(false);

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe;
    }


    class ProductHolder extends RecyclerView.ViewHolder {
        TextView txtDate, txtTime, txtName, txtPhone, payment, txtClinic, txtClinicAddress, txtClinicPhone, tv_note;
        SwipeLayout swipeLayout;
        ImageView iv_Delete;
        LinearLayout ll_note;
        View view_note;

        public ProductHolder(View view) {
            super(view);

            swipeLayout = (SwipeLayout) itemView.findViewById(R.id.swipe);
            iv_Delete = (ImageView) itemView.findViewById(R.id.iv_appointment_delete);

            txtDate = (TextView) view.findViewById(R.id.txtDate);
            txtTime = (TextView) view.findViewById(R.id.txtTime);
            txtName = (TextView) view.findViewById(R.id.txtName);
            txtPhone = (TextView) view.findViewById(R.id.txtPhone);
            payment = (TextView) view.findViewById(R.id.payment);
            txtClinic = (TextView) view.findViewById(R.id.txtClinic);
            txtClinicAddress = (TextView) view.findViewById(R.id.txtClincAddress);
            txtClinicPhone = (TextView) view.findViewById(R.id.txtClincPhone);
            tv_note = view.findViewById(R.id.tv_appointment_note);
            ll_note = view.findViewById(R.id.ll_appointment_note);
            view_note = view.findViewById(R.id.view_appointment_note);

        }
    }

    private String parseDateToddMM(String time) {
        String inputPattern = "yyyy-MM-dd";
        String outputPattern = "dd-MMM";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern, Locale.US);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern, Locale.US);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(time);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }

    private void deleteRow(final int position) {
        AppointmentModel map = arrayList.get(position);
        HashMap<String, String> params = new HashMap<>();
        params.put("user_id", common.get_user_id());
        params.put("app_id", map.getId());

        VJsonRequest vJsonRequest = new VJsonRequest(activity, ApiParams.CANCELAPPOINTMENTS_URL, params,
                new VJsonRequest.VJsonResponce() {
                    @Override
                    public void VResponce(String responce) {

                        arrayList.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, arrayList.size());

                    }

                    @Override
                    public void VError(String responce) {
                        //common.setToastMessage(responce);
                        common.setToastMessage(responce);
                    }
                });
    }

}