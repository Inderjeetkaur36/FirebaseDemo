package com.example.firebasedemo.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.firebasedemo.R;
import com.example.firebasedemo.listener.OnRecyclerItemClickListener;
import com.example.firebasedemo.model.Shoes;
import com.example.firebasedemo.ui.DisplayActivity;

import java.util.ArrayList;

public class CartRecyclerAdapter extends RecyclerView.Adapter<CartRecyclerAdapter.ViewHolder> {

    Context context;
    int resource;
    ArrayList<Shoes> objects;
    OnRecyclerItemClickListener recyclerItemClickListener;

//    public void setOnRecyclerItemClickListener(OnRecyclerItemClickListener recyclerItemClickListener){
//        this.recyclerItemClickListener = recyclerItemClickListener;
//    }

    public CartRecyclerAdapter(Context context, int resource, ArrayList<Shoes> objects) {

        this.context = context;
        this.resource = resource;
        this.objects = objects;
    }

    @NonNull
    @Override
    public CartRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(context).inflate(resource,viewGroup,false);
        final CartRecyclerAdapter.ViewHolder holder = new CartRecyclerAdapter.ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CartRecyclerAdapter.ViewHolder holder, int position) {

        Shoes shoes = objects.get(position);

      //  holder.imageView.setBackgroundResource(shoes.image);
       // holder.textViewName.setText(shoes.name);
        holder.textViewPrice.setText(shoes.price);
        holder.textViewId.setText(shoes.id);
       // holder.textViewColor.setText(shoes.color);
        //holder.textViewSize.setText(shoes.size);
    }

    @Override
    public int getItemCount() {

        return objects.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        //ImageView imageView;
        TextView textViewPrice;
        TextView textViewId;
      //  TextView textViewColor;
      //  TextView textViewSize;
      //  TextView textViewName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

          ///  imageView = itemView.findViewById(R.id.imageView);
          //  textViewName = itemView.findViewById(R.id.textViewName);
            textViewPrice = itemView.findViewById(R.id.textViewPrice);
            textViewId = itemView.findViewById(R.id.textViewId);
          //  textViewColor = itemView.findViewById(R.id.textViewColor);
         //   textViewSize = itemView.findViewById(R.id.textViewSize);

//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    recyclerItemClickListener.onItemClick(getAdapterPosition());
//
//                    Intent i = new Intent(context, DisplayActivity.class);
//                    i.putExtra("keyName",textViewName.getText().toString());
//                    i.putExtra("keyPrice",textViewPrice.getText().toString());
//                    i.putExtra("keyId",textViewId.getText().toString());
//                    i.putExtra("keyColor",textViewColor.getText().toString());
//                    i.putExtra("keySize",textViewSize.getText().toString());
//                    context.startActivity(i);
//
//                }
//            });

        }

    }
}

