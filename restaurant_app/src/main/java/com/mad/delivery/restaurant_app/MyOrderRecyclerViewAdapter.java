package com.mad.delivery.restaurant_app;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Order} and makes a call to the
 * specified {@link PendingOrdersFragment.OnPendingOrderListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyOrderRecyclerViewAdapter extends RecyclerView.Adapter<MyOrderRecyclerViewAdapter.ViewHolder> {

    private final List<Order> orders;
    private View view;
    private final PendingOrdersFragment.OnPendingOrderListener mListener;
    private MyDateFormat dateFormat;

    public MyOrderRecyclerViewAdapter(List<Order> items, PendingOrdersFragment.OnPendingOrderListener listener) {
        orders = items;
        mListener = listener;
        dateFormat = new MyDateFormat("dd/MM/yy HH:mm");
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
        holder.requestedDelivery.setText(dateFormat.parse(holder.mItem.orderFor));
        holder.status.setTextColor(getColor(holder.mItem.status));
        holder.orderDate.setText(dateFormat.parse(holder.mItem.orderFor));
        for(Product p : holder.mItem.products) {
            holder.products.append(p.name + "(" + p.quantity+")\n");
        }

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
            case PENDING:
                return view.getResources().getColor(R.color.colorPendingOrder, null);
            case IN_PREPARATION:
                return view.getResources().getColor(R.color.colorPreparingOrder, null);
            case READY:
                return view.getResources().getColor(R.color.colorReadyOrder, null);
            case CANCELED:
                return view.getResources().getColor(R.color.colorCanceledOrder, null);
            case COMPLETED:
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
            products = mView.findViewById(R.id.tv_productsValue);
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
