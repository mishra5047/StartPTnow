package adapters;

import android.app.Activity;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.doctappo.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import Config.ConstValue;
import models.DoctorModel;


/**
 * Created by Light link on 04/07/2016.
 */
public class DoctorAdapter extends RecyclerView.Adapter<DoctorAdapter.ProductHolder> {

    private ArrayList<DoctorModel> list;
    private Activity activity;

    public DoctorAdapter(Activity activity, ArrayList<DoctorModel> list) {
        this.list = list;
        this.activity = activity;

    }

    @Override
    public ProductHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;

        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_doctors, parent, false);

        return new ProductHolder(view);
    }

    @Override
    public void onBindViewHolder(final ProductHolder holder, final int position) {
        final DoctorModel categoryModel = list.get(position);
        String path = categoryModel.getDoct_photo();

        Picasso.with(activity).load(ConstValue.BASE_URL + "/uploads/business/" + path).into(holder.icon_image);

        holder.lbl_title.setText(categoryModel.getDoct_name());
        holder.lbl_degree.setText(categoryModel.getDoct_degree());

    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    class ProductHolder extends RecyclerView.ViewHolder {
        ImageView icon_image;
        TextView lbl_title;
        TextView lbl_degree;

        public ProductHolder(View itemView) {
            super(itemView);
            icon_image = (ImageView) itemView.findViewById(R.id.imageView);
            lbl_title = (TextView) itemView.findViewById(R.id.title);
            lbl_degree = (TextView) itemView.findViewById(R.id.degree);
        }
    }


}
