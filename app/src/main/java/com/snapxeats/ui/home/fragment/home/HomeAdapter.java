package com.snapxeats.ui.home.fragment.home;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.snapxeats.R;
import com.snapxeats.common.model.preference.Cuisines;
import com.snapxeats.ui.cuisinepreference.OnDoubleTapListenr;
import com.squareup.picasso.Picasso;
import java.util.List;

/**
 * Created by Snehal Tembare on 7/3/18.
 */

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder> {
    private double UNSELECT_OPACITY = 1.0;
    private double SELECT_OPACITY = 0.4;

    private List<Cuisines> cuisineArrayList;
    private Context mContext;
    private OnDoubleTapListenr onDoubleTapListenr;

    HomeAdapter(Context mContext,
                List<Cuisines> cuisineArrayList,
                OnDoubleTapListenr onDoubleTapListenr) {
        this.cuisineArrayList = cuisineArrayList;
        this.mContext = mContext;
        this.onDoubleTapListenr = onDoubleTapListenr;
    }

    @Override
    public HomeAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(mContext).inflate(R.layout.cuisine_recycler_view, parent,
                false);
        return new HomeAdapter.ViewHolder(row);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Cuisines cuisines = cuisineArrayList.get(position);
        Picasso.with(mContext).load(cuisines.getCuisine_image_url()).into(holder.imgCuisinePref);
        holder.txtCuisineName.setText(cuisines.getCuisine_name());

        if (cuisines.is_cuisine_like()) {
            holder.imgCuisineSelected.setVisibility(View.VISIBLE);
            holder.linearLayoutCuisine.setAlpha((float) SELECT_OPACITY);
        } else if (cuisines.is_cuisine_favourite()) {
            holder.imgCuisineSelected.setVisibility(View.VISIBLE);
            holder.linearLayoutCuisine.setAlpha((float) SELECT_OPACITY);
        } else if (!cuisines.is_cuisine_like()) {
            holder.imgCuisineSelected.setVisibility(View.GONE);
            holder.linearLayoutCuisine.setAlpha((float) UNSELECT_OPACITY);
        } else if (!cuisines.is_cuisine_favourite()) {
            holder.imgCuisineSelected.setVisibility(View.GONE);
            holder.linearLayoutCuisine.setAlpha((float) UNSELECT_OPACITY);
        }
    }

    @Override
    public int getItemCount() {
        return cuisineArrayList.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView txtCuisineName;
        private ImageView imgCuisinePref;
        private ImageView imgCuisineSelected;
        private LinearLayout linearLayoutCuisine;


        public ViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);

            txtCuisineName = view.findViewById(R.id.txt_cuisine_name);
            imgCuisinePref = view.findViewById(R.id.img_cuisine);
            imgCuisineSelected = view.findViewById(R.id.img_cuisine_selected);
            linearLayoutCuisine = view.findViewById(R.id.layout_cuisine_cardview);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if (cuisineArrayList.get(position).is_cuisine_like() ||
                    cuisineArrayList.get(position).is_cuisine_favourite()) {
                onDoubleTapListenr.onSingleTap(position, false);
            } else {
                onDoubleTapListenr.onSingleTap(position, true);
            }
        }
    }
}
