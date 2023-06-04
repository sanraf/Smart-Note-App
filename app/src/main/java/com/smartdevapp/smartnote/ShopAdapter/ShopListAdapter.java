package com.smartdevapp.smartnote.ShopAdapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.smartdevapp.smartnote.Models.Shopping;
import com.smartdevapp.smartnote.R;
import com.smartdevapp.smartnote.ShopListener;

import java.util.List;


public class ShopListAdapter  extends RecyclerView.Adapter<ShopViewHolder>{

    Context context;
    List<Shopping> list;
    ShopListener listener;

    public ShopListAdapter(Context context, List<Shopping> list, ShopListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }
    public ShopListAdapter(Context context) {
        this.context = context;
    }

    @Override
    public ShopViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
         return new ShopViewHolder(LayoutInflater.from(context).inflate(R.layout.shop_list,parent,false));
    }
    @SuppressLint({"SetTextI18n", "ResourceAsColor"})
    @RequiresApi(api = Build.VERSION_CODES.M)

    @Override
    public void onBindViewHolder( ShopViewHolder holder, int position) {




        try{

            String myItem;
            int IntQuantity = list.get(position).getQuantity();
            double DoublePrice = list.get(position).getPrice();
            double totalAmountDouble =  (IntQuantity * DoublePrice);
            @SuppressLint("DefaultLocale") String totalAmountString = String.format("%.2f",totalAmountDouble);
            totalAmountString = totalAmountString.replace(",",".");

            if(IntQuantity==1){
                myItem = "Item";
            }else{
                myItem =" Items";
            }


            holder.item.setText(list.get(position).getItem());
            holder.quantity.setText(String.valueOf(IntQuantity));
            holder.spacer.setText(" "+myItem);
            holder.price.setText( totalAmountString);
        }catch (Exception e){
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        if(list.get(position).isChecked()){
            holder.check.setImageResource(R.drawable.green_check);

        }
        else {
            holder.check.setImageResource(0);

        }

        if (list.get(position).isLongClicked()){
            holder.colorHolder.setBackground(ContextCompat.getDrawable(context,R.drawable.shopcardbck_lpress));
            holder.priority.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.ic_baseline_priority_high_24));

        }else {
            holder.colorHolder.setBackground(ContextCompat.getDrawable(context,R.drawable.shopcardbck_1));
            holder.priority.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.ic_baseline_low_priority_24));
        }

        holder.priority.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(list.get(holder.getAdapterPosition()),holder.priority);
            }
        });

        holder.checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(list.get(holder.getAdapterPosition()),holder.checkbox);
            }
        });


        holder.shop_holder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(list.get(holder.getAdapterPosition()));

            }
        });

        holder.shop_holder.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                listener.onLongClick(list.get(holder.getAdapterPosition()),holder.shop_holder);
                return true;
            }
        });

    }


    @Override
    public int getItemCount() {
        return list.size();
    }
    public void filterShopList(List<Shopping> filterList){
        list = filterList;
        notifyDataSetChanged();
    }

    public Shopping getShopList(int position){
        return list.get(position);
    }


}
class ShopViewHolder extends RecyclerView.ViewHolder {


    RelativeLayout shop_holder;
    RelativeLayout colorHolder;
    TextView item, quantity,price,spacer;
    ImageView check,checkbox;
    ImageButton priority;

    public ShopViewHolder(View itemView) {

        super(itemView);
        shop_holder = itemView.findViewById(R.id.shopHolder);
        colorHolder = itemView.findViewById(R.id.colorHolder);
        item = itemView.findViewById(R.id.item);
        quantity = itemView.findViewById(R.id.quantity);
        price = itemView.findViewById(R.id.price);
        spacer = itemView.findViewById(R.id.spacer);
        check = itemView.findViewById(R.id.check);
        priority = itemView.findViewById(R.id.priority);
        checkbox = itemView.findViewById(R.id.checkbox);

    }

}