package com.example.firebasedemo.adapter;

import com.example.firebasedemo.R;
import com.example.firebasedemo.listener.OnRecyclerItemClickListener;
import com.example.firebasedemo.model.Shoes;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    Context context;
    int resource;
    ArrayList<Shoes> objects;
    OnRecyclerItemClickListener recyclerItemClickListener;

    public void setOnRecyclerItemClickListener(OnRecyclerItemClickListener recyclerItemClickListener){
        this.recyclerItemClickListener = recyclerItemClickListener;
    }

    public RecyclerAdapter(Context context, int resource, ArrayList<Shoes> objects) {

        this.context = context;
        this.resource = resource;
        this.objects = objects;
    }

    @NonNull
    @Override
    public RecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(context).inflate(resource,viewGroup,false);
        final RecyclerAdapter.ViewHolder holder = new RecyclerAdapter.ViewHolder(view);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerItemClickListener.onItemClick(holder.getAdapterPosition());

            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        Shoes shoes = objects.get(position);

        holder.imageView.setBackgroundResource(shoes.image);
        holder.txtName.setText(shoes.name);
        holder.txtPrice.setText(shoes.price);
        holder.txtId.setText(shoes.id);
        holder.txtColor.setText(shoes.color);
        holder.txtSize.setText(shoes.size);
    }

    @Override
    public int getItemCount() {
        return objects.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView txtPrice;
        TextView txtId;
        TextView txtColor;
        TextView txtSize;
        TextView txtName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageView);
            txtName= itemView.findViewById(R.id.textViewName);
            txtPrice = itemView.findViewById(R.id.textViewPrice);
            txtId = itemView.findViewById(R.id.textViewId);
            txtColor = itemView.findViewById(R.id.textViewColor);
            txtSize = itemView.findViewById(R.id.textViewSize);
        }
    }
}
