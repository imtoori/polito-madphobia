package com.mad.delivery.consumerApp.wallet;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

import com.mad.delivery.consumerApp.R;
import com.mad.delivery.resources.MyDateFormat;
import com.mad.delivery.resources.Order;
import com.mad.delivery.resources.OrderStatus;

import org.joda.time.DateTime;


public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.ViewHolder> {

        public List<Order> orders;
        private View view;
        private final WalletFragment.OnOrderSelected mListener;

        public OrdersAdapter(List<Order> items, WalletFragment.OnOrderSelected listener) {
            orders = items;
            mListener = listener;
        }

        @Override
        public OrdersAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_layout, parent, false);
            return new OrdersAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final OrdersAdapter.ViewHolder holder, int position) {
            holder.order = orders.get(position);
            holder.nameRestaurant.setText(holder.order.restaurant.previewInfo.name);
            holder.price.setText(holder.order.totalPrice.toString()+"€");
            holder.data.setText(MyDateFormat.parse(new DateTime(holder.order.orderFor)));
            holder.status.setText(holder.order.status.toString());

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != mListener) {
                        // Notify the active callbacks interface (the activity, if the
                        // fragment is attached to one) that an item has been selected.
                        mListener.openOrder(holder.order);
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
            public final TextView data;
            public final TextView status;
            public final ImageView imgRestaurant;

            public Order order;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                nameRestaurant = mView.findViewById(R.id.nameRestaurant);
                price = mView.findViewById(R.id.price);
                data = mView.findViewById(R.id.data);
                status=mView.findViewById(R.id.status);
                imgRestaurant = mView.findViewById(R.id.imgRestaurant);

            }


        }
    }

