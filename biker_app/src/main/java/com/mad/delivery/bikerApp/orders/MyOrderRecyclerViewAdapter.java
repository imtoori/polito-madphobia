package com.mad.delivery.bikerApp.orders;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mad.delivery.bikerApp.R;
import com.mad.delivery.resources.MyDateFormat;
import com.mad.delivery.resources.Order;
import com.mad.delivery.resources.OrderStatus;

import org.joda.time.DateTime;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Order} and makes a call to the
 * specified {@link PendingOrdersFragment.OnPendingOrderListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyOrderRecyclerViewAdapter extends RecyclerView.Adapter<MyOrderRecyclerViewAdapter.ViewHolder> {

    private List<Order> orders;
    private View view;
    private final PendingOrdersFragment.OnPendingOrderListener mListener;

    public MyOrderRecyclerViewAdapter(List<Order> items, PendingOrdersFragment.OnPendingOrderListener listener) {
        orders = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.pending_order_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = orders.get(position);
        holder.id.setText(holder.mItem.id);
        holder.orderFrom.setText(holder.mItem.client.name);
//        holder.requestedDelivery.setText(MyDateFormat.parse(new DateTime(holder.mItem.orderFor)));
        holder.status.setTextColor(getColor(holder.mItem.status));
        holder.status.setText(holder.mItem.status.toString());
//        holder.orderDate.setText(MyDateFormat.parse(new DateTime(holder.mItem.orderFor)));
        holder.products.setText(view.getResources().getString(R.string.prefix_products) + " " + holder.mItem.products.size() + " " + view.getResources().getString(R.string.suffix_products));

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                   mListener.openOrder(holder.mItem);

                }
            }
        });

    }


    private int getColor(OrderStatus st) {
        switch(st) {
            case pending:
                return view.getResources().getColor(R.color.colorPendingOrder, null);
            case preparing:
                return view.getResources().getColor(R.color.colorPreparingOrder, null);
            case ready:
                return view.getResources().getColor(R.color.colorReadyOrder, null);
            case canceled:
                return view.getResources().getColor(R.color.colorCanceledOrder, null);
            case completed:
                return view.getResources().getColor(R.color.colorCompletedOrder, null);
            default:
                return view.getResources().getColor(R.color.colorCanceledOrder, null);
        }
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView id;
        public final TextView orderFrom;
        public final TextView orderDate;
        public final TextView products;
        public final TextView status;
        public final TextView requestedDelivery;
        public Order mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            id = mView.findViewById(R.id.tv_orderNumberValue);
            orderFrom = mView.findViewById(R.id.tv_orderFromValue);
            requestedDelivery = mView.findViewById(R.id.tv_orderRequestedAtValue);
            orderDate =  mView.findViewById(R.id.tv_dateOrderForValue);
            products = mView.findViewById(R.id.tv_products);
            status = mView.findViewById(R.id.tv_orderStatusValue);
        }

        @Override
        public String toString() {
            return "ViewHolder{" +
                    "orderFrom=" + orderFrom +
                    ", requestedDate=" + orderDate +
                    ", products=" + products +
                    '}';
        }
    }
}
