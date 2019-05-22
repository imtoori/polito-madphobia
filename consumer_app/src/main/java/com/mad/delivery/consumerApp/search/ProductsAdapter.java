package com.mad.delivery.consumerApp.search;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.mad.delivery.consumerApp.R;
import com.mad.delivery.resources.MenuItemRest;
import com.mad.delivery.resources.Product;

import java.util.List;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ViewHolder> {
    private List<Product> items;
    private View view;
    public ProductsAdapter(List<Product> items) {
        this.items = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.product_item_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);


        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int index) {
        Product item = items.get(index);
        holder.mItem = item;
        holder.name.setText(item.name);
        holder.quantity.setText(String.valueOf(item.quantity));
        holder.totalPrice.setText("€ " + String.valueOf(item.price));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder  {
        public final View mView;
        public final TextView name;
        public final TextView totalPrice;
        public final TextView quantity;
        public Product mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            name = mView.findViewById(R.id.product_name);
            totalPrice = mView.findViewById(R.id.product_totalprice);
            quantity = mView.findViewById(R.id.product_quantity);

        }

        @Override
        public String toString() {
            return "View Holder";
        }
    }
}
