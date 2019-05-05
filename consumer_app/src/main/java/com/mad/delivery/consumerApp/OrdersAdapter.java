package com.mad.delivery.consumerApp;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

import com.mad.delivery.resources.Order;
import com.mad.delivery.resources.Product;


public class OrdersAdapter extends RecyclerView.Adapter<com.mad.delivery.consumerApp.OrdersAdapter.ViewHolder> {

        private List<Order> orders;
        private View view;
        private final WalletFragment.OnOrderSelected mListener;

        public OrdersAdapter(List<Order> items, WalletFragment.OnOrderSelected listener) {
            orders = items;
            mListener = listener;
        }

        @Override
        public com.mad.delivery.consumerApp.OrdersAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_layout, parent, false);
            return new com.mad.delivery.consumerApp.OrdersAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final com.mad.delivery.consumerApp.OrdersAdapter.ViewHolder holder, int position) {
            holder.order = orders.get(position);
            holder.nameRestaurant.setText(holder.order.restaurant.name);
            int price = 0;
            holder.price.setText("Price: "+ holder.order.totalPrice.toString());
            holder.status.setText("Delivery: "+ holder.order.status.toString());
            if(holder.order.restaurant.imageUri != null)
                holder.imgRestaurant.setImageURI(holder.order.restaurant.imageUri);

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != mListener) {
                        // Notify the active callbacks interface (the activity, if the
                        // fragment is attached to one) that an item has been selected.
                        mListener.openOrder();
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return orders.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final TextView nameRestaurant;
            public final TextView price;
            public final TextView status;
            public final ImageView imgRestaurant;

            public Order order;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                nameRestaurant = mView.findViewById(R.id.nameRestaurant);
                price = mView.findViewById(R.id.price);
                status=mView.findViewById(R.id.status);
                imgRestaurant = mView.findViewById(R.id.imgRestaurant);


            }

            @Override
            public String toString() {
                return "ViewHolder{" +
                        "name=" + order.restaurant.name;
            }
        }
    }

