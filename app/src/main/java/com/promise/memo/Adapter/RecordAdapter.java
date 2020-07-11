package com.promise.memo.Adapter;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.promise.memo.DB.roomdb.bean.RecordBean;
import com.promise.memo.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecordAdapter extends RecyclerView.Adapter<RecordAdapter.MyViewHolder> {
    private List<RecordBean> list;

    public void setList(List<RecordBean> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public RecordAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_record, null);
        return new MyViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull RecordAdapter.MyViewHolder holder, int position) {
        RecordBean newslistBean = list.get(position);
        holder.tvName.setText(newslistBean.getTypeName());
        String type = newslistBean.getType();
        //如果是支出 前面加  -
        holder.tvMoney.setText(type.equals("1") ? newslistBean.getMoney() + "" : "-" + newslistBean.getMoney());
        holder.imageView.setImageResource(getImageResourceId(newslistBean.getTypepin()));
        holder.tvTime.setText(newslistBean.getCreateDate());
        holder.tvcontent.setText(TextUtils.isEmpty(newslistBean.getContent()) ? "" : newslistBean.getContent());
    }

    /**
     * 通過名字获取 drawable里面的图片
     *
     * @param name
     * @return
     */
    public int getImageResourceId(String name) {
        R.drawable drawables = new R.drawable();
        //默认的id
        int resId = 0x7f02000b;
        try {
            //根据字符串字段名，取字段//根据资源的ID的变量名获得Field的对象,使用反射机制来实现的
            java.lang.reflect.Field field = R.drawable.class.getField(name);
            //取值
            resId = (Integer) field.get(drawables);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return resId;
    }

    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvMoney, tvTime, tvcontent;
        ImageView imageView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_name);
            tvMoney = itemView.findViewById(R.id.tv_money);
            imageView = itemView.findViewById(R.id.iv_type);
            tvTime = itemView.findViewById(R.id.tv_time);
            tvcontent = itemView.findViewById(R.id.tv_content);
        }
    }
}
