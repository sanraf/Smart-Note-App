package com.smartdevapp.smartnote.SlipAdapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import androidx.recyclerview.widget.RecyclerView;

import com.smartdevapp.smartnote.Models.Shopping;
import com.smartdevapp.smartnote.R;
import com.smartdevapp.smartnote.ShopListener;

import java.util.List;

public class SlipListAdapter  extends RecyclerView.Adapter<SlipViewHolder>{

    Context context;
    List<Shopping> list;
    ShopListener listener;

    public SlipListAdapter(Context context, List<Shopping> list) {
        this.context = context;
        this.list = list;
        //this.listener = listener;
    }
    public SlipListAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public SlipViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SlipViewHolder(LayoutInflater.from(context).inflate(R.layout.shop_list_slip,parent,false));
    }
    @SuppressLint({"SetTextI18n", "ResourceAsColor"})
    @RequiresApi(api = Build.VERSION_CODES.M)

    @Override
    public void onBindViewHolder(@NonNull SlipViewHolder holder, int position) {




        try{

            int IntQuantity = list.get(position).getQuantity();
            double DoublePrice = list.get(position).getPrice();
            double totalAmountDouble =  (IntQuantity * DoublePrice);
            @SuppressLint("DefaultLocale") String totalAmountString = String.format("%.2f",totalAmountDouble);
            totalAmountString = totalAmountString.replace(",",".");


            holder.item.setText(list.get(position).getItem());
            holder.quantity.setText(String.valueOf(IntQuantity));
            holder.price.setText( totalAmountString);
        }catch (Exception e){
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }



        /*if (list.get(position).isLongClicked()){
            holder.colorHolder.setBackground(ContextCompat.getDrawable(context,R.drawable.shopcardbck_2));
            holder.shop_holder.setStrokeColor(ContextCompat.getColor(context,R.color.red_mode_stroke));

        }else {
            holder.colorHolder.setBackground(ContextCompat.getDrawable(context,R.drawable.shopcardbck_1));
            holder.shop_holder.setStrokeColor(ContextCompat.getColor(context,R.color.purple_mode_stroke));
        }*/



        /*holder.shop_holder.setOnClickListener(new View.OnClickListener() {
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
        });*/

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

class SlipViewHolder extends RecyclerView.ViewHolder {


    //MaterialCardView shop_holder;
    //RelativeLayout colorHolder;
    TextView item, quantity,price;


    public SlipViewHolder(View itemView) {

        super(itemView);
        //shop_holder = itemView.findViewById(R.id.shopHolder);
        //colorHolder = itemView.findViewById(R.id.colorHolder);
        item = itemView.findViewById(R.id.slip_item);
        quantity = itemView.findViewById(R.id.slip_quantity);
        price = itemView.findViewById(R.id.slip_price);



    }

}
