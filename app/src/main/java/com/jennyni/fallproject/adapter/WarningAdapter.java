package com.jennyni.fallproject.adapter;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jennyni.fallproject.Bean.AskAllFallInfoBean;
import com.jennyni.fallproject.Bean.AskFallInfoBean;
import com.jennyni.fallproject.Bean.UserUpdateBean;
import com.jennyni.fallproject.R;
import com.jennyni.fallproject.utils.DBUtils;
import com.jennyni.fallproject.utils.JsonParse;
import com.jennyni.fallproject.utils.UtilsHelper;
import com.jennyni.fallproject.view.SlidingButtonView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *创建一个数据适配器WarningAdapter对RecyclerView控件进行数据适配
 *
 * Created by Jenny on 2019/3/1.
 */

public class WarningAdapter extends RecyclerView.Adapter<WarningAdapter.
        MyViewHolder> implements SlidingButtonView.IonSlidingButtonListener{

    private Context mContext;
    IonSlidingViewClickListener mIDeleteBtnClickListener;
    private List<AskAllFallInfoBean.ResultBean> allfallinfolist = new ArrayList<>();
    private SlidingButtonView mMenu = null;

    public WarningAdapter(Context context,IonSlidingViewClickListener mIDeleteBtnClickListener){
        mContext = context;
      this.mIDeleteBtnClickListener = mIDeleteBtnClickListener;
    }

    public void setData(List<AskAllFallInfoBean.ResultBean> allfallinfolist) {
        this.allfallinfolist = allfallinfolist;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return allfallinfolist.size();
    }



    //设置列表菜单中行内显示的内容
    @Override
    public void onBindViewHolder(final WarningAdapter.MyViewHolder holder, int position) {
        //显示界面控件,设备号，设备用户名，设备跌倒时间，头像（设备跌倒类型未添加）
        AskAllFallInfoBean.ResultBean bean = allfallinfolist.get(position);
        holder.tv_num.setText(bean.getCard_id());
        holder.tv_name.setText(bean.getName());
        //时间戳转换
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = sdf.format(new Date(bean.getTime()));   // 时间戳转换成时间
        holder.tv_time.setText(time);

        Glide.with(mContext)
                .load(R.drawable.fall_icon)
                .error(R.mipmap.ic_launcher)
                .into((holder).iv_img);
        //设置内容布局的宽为屏幕宽度
        holder.layout_content.getLayoutParams().width = UtilsHelper.getScreenWidth(mContext);
        //内容的点击事件
        holder.layout_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //判断是否有删除菜单打开
                if (menuIsOpen()) {
                    closeMenu();//关闭菜单
                } else {
                    int n = holder.getLayoutPosition();         //获取要删除行的位置
                    mIDeleteBtnClickListener.onItemClick(v, n); //删除列表中指定的行
                }
            }
        });
        holder.btn_Delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int n = holder.getLayoutPosition();             //获取要删除行的位置
                mIDeleteBtnClickListener.onDeleteBtnCilck(v, n);//删除列表中指定的行
            }
        });

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup arg0, int arg1) {
        //获取列表中每行的布局文件
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_warning, arg0,
                false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView iv_img;
        public TextView btn_Delete, tv_num,tv_name, tv_time,tv_type;
        public ViewGroup layout_content;
        //获取控件
        public MyViewHolder(View itemView) {
            super(itemView);
            btn_Delete = (TextView) itemView.findViewById(R.id.tv_delete);
            layout_content = (ViewGroup) itemView.findViewById(R.id.layout_content);
            iv_img = (ImageView) itemView.findViewById(R.id.iv_img);
            tv_num = (TextView) itemView.findViewById(R.id.tv_num);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            tv_type = (TextView)itemView.findViewById(R.id.tv_type);
            tv_time = (TextView) itemView.findViewById(R.id.tv_time);
            ((SlidingButtonView) itemView).setSlidingButtonListener(WarningAdapter.this);

        }
    }

    //删除列表行中信息的方法
    public void removeData(int position, TextView tv_none,String id) {
//        UserUpdateBean.ResultBean bean = userUpdateinfolist.get(position);
//        //从跌倒信息的数据库中也要删除此数据
//        DBUtils.getInstance(mContext).delAskFallInfo(bean.getId(),bean.getCard_id(),bean.getTime());
        allfallinfolist.remove(position);
        notifyItemRemoved(position);
        if (allfallinfolist.size() == 0)
            tv_none.setVisibility(View.VISIBLE);
    }
    /**
     * 删除菜单打开信息接收
     * @param view
     */
    @Override
    public void onMenuIsOpen(View view) {
        mMenu = (SlidingButtonView) view;
    }

    /**
     * 滑动或者点击了Item监听
     * @param slidingButtonView
     */
    @Override
    public void onDownOrMove(SlidingButtonView slidingButtonView) {
        //判断是否有菜单打开
        if (menuIsOpen()) {
            if (mMenu != slidingButtonView) {
                closeMenu();    //关闭菜单
            }
        }
    }

    /**
     * 关闭菜单
     */
    private void closeMenu() {
        mMenu.closeMenu();
        mMenu = null;
    }

    /**
     * 判断是否有菜单打开
     */
    private boolean menuIsOpen() {
        if (mMenu != null) {
            return true;
        }
        return false;
    }


    public interface IonSlidingViewClickListener {
        void onItemClick(View view, int position);
        void onDeleteBtnCilck(View view, int position);
    }
}
