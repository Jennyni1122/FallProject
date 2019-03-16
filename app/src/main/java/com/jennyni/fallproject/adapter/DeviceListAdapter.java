package com.jennyni.fallproject.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;


import android.support.v7.view.menu.MenuView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jennyni.fallproject.Bean.UserUpdateBean;
import com.jennyni.fallproject.activity.devicelocation.DevUserDetailActivity;
import com.jennyni.fallproject.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 设备列表适配器
 * Created by Jenny on 2019/2/25.
 */

public class DeviceListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>implements View.OnClickListener, View.OnLongClickListener {
    private Context context;
    private List<UserUpdateBean.ResultBean> devicelist=new ArrayList<>();
    private OnItemClickListener mOnItemClickListener = null;

    public DeviceListAdapter(Context context) {
        this.context = context;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }
    public void setData(List<UserUpdateBean.ResultBean> devicelist){
        this.devicelist = devicelist;
        notifyDataSetChanged();     //更新
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_device_list,viewGroup,false);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setOnClickListener(this);
        view.setOnLongClickListener(this);
        return viewHolder;
    }

    @SuppressLint("ResourceType")
    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int i) {
        if (devicelist == null) return;
        final UserUpdateBean.ResultBean bean = devicelist.get(i);

        //每个列表项，显示：设备用户图片，设备名和设备号
        ((ViewHolder) holder).tv_device_username.setText(bean.getDev_name());
        ((ViewHolder) holder).tv_device_name.setText(bean.getCard_id());
        //头像根据性别变化
        if ( bean.getDev_sex().equals("男")) {
            ((ViewHolder) holder).iv_img_sex.setImageResource(R.drawable.icon_male);
        } else {
            ((ViewHolder) holder).iv_img_sex.setImageResource(R.drawable.icon_female);
        }
        //将i保存在itemView的Tag中，以便点击时进行获取
        holder.itemView.setTag(i);

    }

    @Override
    public int getItemCount() {
        return devicelist == null ? 0 : devicelist.size();
    }

    @Override
    public void onClick(View view) {
        if (mOnItemClickListener != null) {
            //这里使用getTag方法获取position
            mOnItemClickListener.onItemClick(view, (int) view.getTag());
        }
    }

    @Override
    public boolean onLongClick(View view) {
        mOnItemClickListener.onItemLongClick(view, (int) view.getTag());
        return false;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView tv_device_username,tv_device_name;
        public ImageView iv_img_sex;
        public ViewHolder(View itemView) {
            super(itemView);
            tv_device_username = itemView.findViewById(R.id.tv_device_username);
            tv_device_name = itemView.findViewById(R.id.tv_device_name);
            iv_img_sex = itemView.findViewById(R.id.iv_img_sex);

        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
        void onItemLongClick(View view, int position);
    }
}
