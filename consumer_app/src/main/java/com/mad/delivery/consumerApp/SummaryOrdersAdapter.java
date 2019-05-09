package com.mad.delivery.consumerApp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

import com.mad.delivery.resources.Product;


public class SummaryOrdersAdapter extends RecyclerView.Adapter<com.mad.delivery.consumerApp.SummaryOrdersAdapter.ViewHolder> {

    private List<Product> products;
    private View view;

    public SummaryOrdersAdapter(List<Product> items) {
        products = items;

    }

    @Override
    public com.mad.delivery.consumerApp.SummaryOrdersAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.summary_order_item, parent, false);
        return new com.mad.delivery.consumerApp.SummaryOrdersAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final com.mad.delivery.consumerApp.SummaryOrdersAdapter.ViewHolder holder, int position) {
        holder.product = products.get(position);
        holder.product_name.setText(holder.product.name);
        holder.price.setText(Double.toString(holder.product.price));
        holder.counter.setText(Double.toString(holder.product.quantity));


    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView product_name;
        public final TextView price;
        public final TextView counter;

        public Product product;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            product_name = mView.findViewById(R.id.product_name);
            price = mView.findViewById(R.id.price);
            counter = mView.findViewById(R.id.counter);



        }

        @Override
        public String toString() {
            return "ViewHolder{" +
                    "name=" + product.name;
        }
    }
}

