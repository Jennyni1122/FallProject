package com.jennyni.fallproject.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.services.geocoder.GeocodeAddress;
import com.bumptech.glide.Glide;
import com.jennyni.fallproject.R;

import java.util.List;

import cn.smssdk.gui.DefaultContactViewItem;

/**
 * Created by Jenny on 2019/3/12.
 */

public class GetAddressAdapter extends RecyclerView.Adapter<GetAddressAdapter.ViewHolder> {
    private Context mContext;
    private List<GeocodeAddress> geocodeAddressList;

    public GetAddressAdapter(Context mContext, List<GeocodeAddress> geocodeAddressList, ItemClickListener listener) {
        this.mContext = mContext;
        this.geocodeAddressList = geocodeAddressList;
        this.listener = listener;
    }

    public void setData(List<GeocodeAddress> geocodeAddressList) {
        this.geocodeAddressList = geocodeAddressList;
        notifyDataSetChanged();
    }

    public List<GeocodeAddress> getGeocodeAddressList() {
        return geocodeAddressList;
    }

    public void addAllData(List<GeocodeAddress> data) {
        geocodeAddressList.addAll(data);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public GetAddressAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.item_address_fence, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final GetAddressAdapter.ViewHolder holder, int position) {

        final GeocodeAddress geocodeAddress = geocodeAddressList.get(position);
        Glide
                .with(mContext)
                .load(R.drawable.marker)
                .error(R.mipmap.ic_launcher)
                .into(holder.iv_img);
        holder.tv_address.setText(geocodeAddress.getFormatAddress());
        holder.setPosition(position);

    }

    @Override
    public int getItemCount() {
        return geocodeAddressList == null ? 0 : geocodeAddressList.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView iv_img;
        public TextView tv_address;
        private int position;

        public void setPosition(int position) {
            this.position = position;
        }

        public ViewHolder(View itemView) {
            super(itemView);
            iv_img = (ImageView) itemView.findViewById(R.id.iv_img);
            tv_address = (TextView) itemView.findViewById(R.id.tv_address);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onItemClickListener(position);
                    }
                }
            });

        }
    }

    public interface ItemClickListener {
        void onItemClickListener(int position);
    }

    public ItemClickListener listener;

    public void setListener(ItemClickListener listener) {
        this.listener = listener;
    }
}
