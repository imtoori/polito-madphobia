package com.mad.delivery.bikerApp;
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
public class ProductRecyclerViewAdapter extends RecyclerView.Adapter<ProductRecyclerViewAdapter.ViewHolder> {

    private final List<Product> products;
    private View view;
    public ProductRecyclerViewAdapter(List<Product> items) {
        products = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.products_recview, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = products.get(position);
        holder.name.setText(holder.mItem.name);
        holder.quantity.setText(String.valueOf(holder.mItem.quantity));
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView name;
        public final TextView quantity;
        public Product mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            name = mView.findViewById(R.id.tv_product_name);
            quantity = mView.findViewById(R.id.tv_product_qty);
        }

    }
}
