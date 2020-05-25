package fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.doctappo.LoginActivity;
import com.doctappo.PersonInfoActivity;
import com.doctappo.R;
import com.doctappo.TimeSlotActivity;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import Config.ConstValue;
import adapters.ReviewsAdapter;
import models.ActiveModels;
import models.ReviewsModel;
import models.SlotsModel;
import util.CommonClass;

/**
 * Created by subhashsanghani on 1/16/17.
 */

public class TimeSlotFragment extends Fragment {

    private ArrayList<SlotsModel> arrayList;

    private CommonClass common;
    private RecyclerView timeSlotRecyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_timeslot, container, false);

        common = new CommonClass(getActivity());
        timeSlotRecyclerView = (RecyclerView) rootView.findViewById(R.id.rv_list);
        final LinearLayoutManager layoutManager
                = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        timeSlotRecyclerView.setLayoutManager(layoutManager);
        TimeAdapter adapter = new TimeAdapter();
        timeSlotRecyclerView.setAdapter(adapter);

        if (getArguments().getInt("position") == 0) {
            arrayList = TimeSlotActivity.timeSlotModel.getMorning();
        } else if (getArguments().getInt("position") == 1) {
            arrayList = TimeSlotActivity.timeSlotModel.getAfternoon();
        } else if (getArguments().getInt("position") == 2) {
            arrayList = TimeSlotActivity.timeSlotModel.getEvening();
        }

        /*timeSlotRecyclerView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                SlotsModel chooseMap = arrayList.get(i);
                if (!chooseMap.getIs_booked()) {
                    Intent intent = new Intent(getActivity(), ServicesActivity.class);
                    Bundle b = new Bundle();
                    b.putSerializable("slot", chooseMap);
                    b.putString("date", bundleE.getString("date"));
                    intent.putExtras(b);
                    startActivity(intent);

                }else{
                    common.setToastMessage(getString(R.string.already_booked));
                }
            }
        });
        */
        return rootView;
    }

    class TimeAdapter extends RecyclerView.Adapter<TimeAdapter.ProductHolder> {


        @Override
        public ProductHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = null;

            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_time_slot, parent, false);

            return new ProductHolder(view);
        }

        @Override
        public void onBindViewHolder(final ProductHolder holder, final int position) {
            final SlotsModel slotsModel = arrayList.get(position);

            if (slotsModel.getIs_booked()) {
                holder.itemView.setBackgroundColor(getResources().getColor(R.color.colorSelected));
                holder.imgClock.setImageResource(R.drawable.ic_clock_white);
            } else {
                holder.itemView.setBackgroundColor(getResources().getColor(R.color.colorUnSelected));
                holder.imgClock.setImageResource(R.drawable.ic_clock_gray);
            }

            holder.timeslot.setText(CommonClass.parseTime(slotsModel.getSlot()));
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SlotsModel chooseMap = arrayList.get(position);
                    if (!chooseMap.getIs_booked()) {
                        if (common.is_user_login()) {
                            ActiveModels.SELECTED_SLOT = slotsModel;
                            Intent intent = new Intent(getActivity(), PersonInfoActivity.class);
                            Bundle b = new Bundle();
                            b.putString("slot", slotsModel.getSlot());
                            b.putString("date", getArguments().getString("date"));
                            intent.putExtras(b);
                            getActivity().startActivity(intent);
                        } else {
                            Intent intent = new Intent(getActivity(), LoginActivity.class);
                            getActivity().startActivity(intent);
                        }
                    } else {
                        common.setToastMessage(getString(R.string.already_booked));
                    }
                }
            });
        }


        @Override
        public int getItemCount() {
            return arrayList.size();
        }

        class ProductHolder extends RecyclerView.ViewHolder {
            ImageView imgClock;
            TextView timeslot;

            public ProductHolder(View itemView) {
                super(itemView);
                imgClock = (ImageView) itemView.findViewById(R.id.clockimage);
                timeslot = (TextView) itemView.findViewById(R.id.timeslot);
            }
        }

    }

}
