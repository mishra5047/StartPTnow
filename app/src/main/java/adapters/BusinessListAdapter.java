package adapters;

import android.app.Activity;
import android.content.Intent;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.doctappo.CommonActivity;
import com.doctappo.DetailsSalonActivity;
import com.doctappo.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import Config.ConstValue;
import models.ActiveModels;
import models.BusinessModel;
import util.CommonClass;

/**
 * Created by LENOVO on 4/19/2016.
 */
public class BusinessListAdapter extends RecyclerView.Adapter<BusinessListAdapter.ProductHolder> {

    private Activity activity;
    private CommonClass common;
    private ArrayList<BusinessModel> postItems;
    private List<String> favoriteList = new ArrayList<>();

    public BusinessListAdapter(Activity con, ArrayList<BusinessModel> array) {
        activity = con;
        postItems = array;
        common = new CommonClass(con);
        updatelist();
    }

    public void updatelist() {
        favoriteList.clear();
        favoriteList.addAll(Arrays.asList(CommonActivity.getFavorite(activity).split(",")));
        Log.e(activity.toString(), "favoriteList:" + favoriteList.toString());
    }

    @Override
    public ProductHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;

        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_business_list, parent, false);

        return new ProductHolder(view);
    }

    @Override
    public void onBindViewHolder(final ProductHolder holder, final int position) {
        final BusinessModel categoryModel = postItems.get(position);
        String path = categoryModel.getBus_logo();

        Picasso.with(activity).load(ConstValue.BASE_URL + "/uploads/business/" + path).into(holder.imageView);

        holder.textName.setText(categoryModel.getBus_title());
        holder.textSubTitle.setText(categoryModel.getBus_google_street());
        holder.ratingbar.setRating(Float.parseFloat(categoryModel.getAvg_rating()));

        categoryModel.setFavorite(favoriteList.contains(categoryModel.getBus_id()));

        if (categoryModel.isFavorite()) {
            holder.iv_favorite.setBackgroundResource(R.mipmap.ic_favorite);
        } else {
            holder.iv_favorite.setBackgroundResource(R.mipmap.ic_unfavorite);
        }

    }


    @Override
    public int getItemCount() {
        return postItems.size();
    }

    class ProductHolder extends RecyclerView.ViewHolder {
        ImageView imageView, iv_favorite;
        TextView textName;
        TextView textSubTitle;
        RatingBar ratingbar;

        public ProductHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.imageView);
            textName = (TextView) itemView.findViewById(R.id.title);
            textSubTitle = (TextView) itemView.findViewById(R.id.subTitle);
            ratingbar = (RatingBar) itemView.findViewById(R.id.ratingBar1);
            iv_favorite = (ImageView) itemView.findViewById(R.id.iv_business_list_favorite);
            textName.setTypeface(common.getCustomFont());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    ActiveModels.BUSINESS_MODEL = postItems.get(position);
                    Intent intent = new Intent(activity, DetailsSalonActivity.class);
                    activity.startActivity(intent);
                }
            });

            iv_favorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    BusinessModel businessModel = postItems.get(position);

                    CommonActivity.setFavorite(activity, businessModel.getBus_id());

                    if (businessModel.isFavorite()) {
                        businessModel.setFavorite(false);
                    } else {
                        businessModel.setFavorite(true);
                    }

                    updatelist();

                    notifyItemChanged(position);
                }
            });

        }
    }


}
