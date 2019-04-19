package com.example.firebasedemo.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.firebasedemo.R;
import com.example.firebasedemo.listener.OnRecyclerItemClickListener;
import com.example.firebasedemo.model.Customer;

import java.util.ArrayList;

public class CustomerAdapter extends RecyclerView.Adapter<CustomerAdapter.ViewHolder> {
    Context context;
    int resource;
    ArrayList<Customer> objects;

    OnRecyclerItemClickListener recyclerItemClickListener;

    public void setOnRecyclerItemClickListener(OnRecyclerItemClickListener recyclerItemClickListener) {
        this.recyclerItemClickListener = recyclerItemClickListener;

    }

    public CustomerAdapter(Context context, int resource, ArrayList<Customer> objects) {
        this.context = context;
        this.resource = resource;
        this.objects = objects;
    }


    @NonNull
    @Override
    public CustomerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(resource, parent, false);
        final CustomerAdapter.ViewHolder holder = new CustomerAdapter.ViewHolder(view);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerItemClickListener.onItemClick(holder.getAdapterPosition());
            }
        });
        return holder;
    }

    // onBindViewHolder will be execute n number of time from 0 to n-1 when n is the count whiwh we are returning

    @Override
    public void onBindViewHolder(@NonNull CustomerAdapter.ViewHolder holder, int position) {
        Customer customer = objects.get(position);

        // Extracting Data from News Object and Setting the data on list_item
        holder.txtTitle.setText(customer.name);
        holder.txtUrl.setText(customer.phone);
        holder.txtEmail.setText(customer.email);
    }

    @Override
    public int getItemCount() {

        return objects.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // Attributes of ViewHolder
        TextView txtTitle;
        TextView txtUrl;
        TextView txtEmail;

        public ViewHolder(View itemView) {
            super(itemView);

            txtTitle = itemView.findViewById(R.id.textViewTitle);
            txtUrl = itemView.findViewById(R.id.textViewURL);
            txtEmail = itemView.findViewById(R.id.textViewEmail);
        }
    }
}