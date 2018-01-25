package com.snapxeats.ui.preferences;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.snapxeats.R;
import com.snapxeats.common.model.Cuisines;
import com.snapxeats.common.model.RootCuisine;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Prajakta Patil on 19/1/18.
 */
public class PreferenceAdapter extends RecyclerView.Adapter<PreferenceAdapter.ViewHolder> {

    public RootCuisine rootCuisine;
    public ArrayList<Cuisines> cuisineArrayList;
    private Context mContext;
    private int selectedCardPos;
    private OnCardItemClickListener mOnCardItemClickListener;
    private double UNSELECT_OPACITY = 1.0;
    private double SELECT_OPACITY = 0.4;

    public PreferenceAdapter(Context mContext, int selectedCardPos, ArrayList<Cuisines> cuisineArrayList,
                             RootCuisine rootCuisine, OnCardItemClickListener mOnCardItemClickListener) {
        this.mContext = mContext;
        this.selectedCardPos = selectedCardPos;
        this.cuisineArrayList = cuisineArrayList;
        this.rootCuisine = rootCuisine;
        this.mOnCardItemClickListener = mOnCardItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.cuisine_recycler_view, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {

        viewHolder.bind(position, rootCuisine, mOnCardItemClickListener);

        viewHolder.txtCuisineName.setText(cuisineArrayList.get(position).getCuisine_name());
        Picasso.with(mContext).load(cuisineArrayList.get(position).getCuisine_image_url())
                .placeholder(R.drawable.ic_cuisine_placeholder).into(viewHolder.imgCuisine);

        viewHolder.cardView.setOnClickListener(v -> {
            Cuisines cuisines = cuisineArrayList.get(position);
            cuisines.setSelected(!cuisines.isSelected());
            if (cuisines.isSelected()) {
                viewHolder.imgCuisineSelected.setVisibility(View.VISIBLE);
                viewHolder.linearLayoutCuisine.setAlpha((float) SELECT_OPACITY);
            } else {
                viewHolder.imgCuisineSelected.setVisibility(View.GONE);
                viewHolder.linearLayoutCuisine.setAlpha((float) UNSELECT_OPACITY);
            }
        });
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtCuisineName;
        private ImageView imgCuisine, imgCuisineSelected;
        private CardView cardView;
        private LinearLayout linearLayoutCuisine;

        ViewHolder(View view) {
            super(view);
            txtCuisineName = view.findViewById(R.id.txt_cuisine_name);
            imgCuisine = view.findViewById(R.id.img_cuisine);
            cardView = view.findViewById(R.id.cardview_cuisine);
            imgCuisineSelected = view.findViewById(R.id.img_cuisine_selected);
            linearLayoutCuisine = view.findViewById(R.id.layout_cuisine_cardview);
        }

        void bind(final int position,
                  final RootCuisine cuisines,
                  final OnCardItemClickListener onCardItemClickListener) {
            cardView.setOnClickListener(v -> onCardItemClickListener.onCardClick(position, rootCuisine));
            if (mOnCardItemClickListener != null) {
                mOnCardItemClickListener.onCardClick(position, cuisines);
            }
        }
    }

    @Override
    public int getItemCount() {
        return rootCuisine.getCuisineList().size();
    }

    interface OnCardItemClickListener {
        void onCardClick(int position, RootCuisine rootCuisine);
    }
}