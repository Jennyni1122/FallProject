package com.jennyni.fallproject.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jennyni.fallproject.Bean.UserUpdateBean;
import com.jennyni.fallproject.activity.DevUserDetailActivity;
import com.jennyni.fallproject.R;

import java.util.List;

/**
 * 设备列表适配器
 * Created by Jenny on 2019/2/25.
 */

public class DeviceListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private List<UserUpdateBean.ResultBean> devicelist;
    private Context context;
    private AdapterView.OnItemClickListener mOnItemClickListener;

    public DeviceListAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<UserUpdateBean.ResultBean> list){
        this.devicelist = list;
        notifyDataSetChanged();     //更新
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_device_list,viewGroup,false);
        final ViewHolder viewHolder = new ViewHolder(view);


        //长按列表项，解绑设备(参考网上)
        //1.适配器中添加长按点击方法
        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                //获取view对应对应的位置
                int position = viewHolder.getLayoutPosition();
                if (longClickLisenter!=null){
                    //回调监听
                    longClickLisenter.onRecyclerViewItemLongClick(position);
                }
                return true;
            }
        });


        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {

        final UserUpdateBean.ResultBean bean = devicelist.get(position);

        //每个列表项，显示：设备用户图片，设备名和设备号
        Glide
                .with(context)
                .load(bean.getHeadimage())
                .error(R.mipmap.ic_launcher)
                .into(((ViewHolder)holder).iv_img_sex);//根据性别变换头像,暂不用
        ((ViewHolder) holder).tv_device_username.setText(bean.getDev_name());
        ((ViewHolder) holder).tv_device_name.setText(bean.getCard_id());

        //点击进入单个用户的设备定位界面信息,显示设备号，设备用户，电量，信号，经纬度，电子围栏等信息
        if (mOnItemClickListener != null){
            holder.itemView.setOnClickListener(new View.OnClickListener() { //点击子项目
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context,DevUserDetailActivity.class);
                    //显示内容：设备用户名，设备编号，设备信号，设备电量，身份证，安全范围，当前状态
                    intent.putExtra("id",bean.getId());
                    intent.putExtra("dname",bean.getDev_name());
                    intent.putExtra("cardid",bean.getCard_id());
                    intent.putExtra("guardian",bean.getGuardian());
                    intent.putExtra("isgeo",bean.getIsgeo());
                    intent.putExtra("geocenter",bean.getGeocenter());
                    intent.putExtra("georadius",bean.getGeoradius());
                    //注意：协议里缺少信号，电量等。。。
                    context.startActivity(intent);
                }
            });

        }


    }

    @Override
    public int getItemCount() {
        return devicelist == null ? 0 : devicelist.size();
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

    //2.创建接口
    public interface OnLongClickLisenter {
        void onRecyclerViewItemLongClick(int position);
    }

    private OnLongClickLisenter longClickLisenter;

    public void setOnRecyclerViewItemLongClickLisenter(OnLongClickLisenter longClickLisenter) {
        this.longClickLisenter = longClickLisenter;
    }

    //3.定义删除方法
    public void removeItem(int position){
        devicelist.remove(position);
        notifyDataSetChanged();
    }


}
