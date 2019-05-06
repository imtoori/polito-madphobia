package com.mad.delivery.consumerApp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

import com.mad.delivery.resources.Product;


public class BasketAdapter extends RecyclerView.Adapter<com.mad.delivery.consumerApp.BasketAdapter.ViewHolder> {

    private List<Product> products;
    private View view;



    public BasketAdapter(List<Product> items) {
        products = items;

    }

    @Override
    public com.mad.delivery.consumerApp.BasketAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.basket_item, parent, false);
        return new com.mad.delivery.consumerApp.BasketAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final com.mad.delivery.consumerApp.BasketAdapter.ViewHolder holder, int position) {
        holder.product = products.get(position);
        holder.price.setText(String.valueOf(holder.product.price));
        holder.name.setText(holder.product.name);


    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView price;
        public final TextView name;
        public final ImageView delete;

        public Product product;

        public ViewHolder(View view) {
            super(view);
            mView = view;

            price = mView.findViewById(R.id.price);
            name=mView.findViewById(R.id.name);
            delete=mView.findViewById(R.id.delete);


        }

        @Override
        public String toString() {
            return "ViewHolder{" +
                    "name=" + product.name;
        }
    }

}

