package com.mad.delivery.consumerApp.search.Order;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.mad.delivery.consumerApp.R;
import com.mad.delivery.resources.Product;

import java.util.List;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ViewHolder> {
    private List<Product> items;
    private View view;
    private OnProductListener myListener;
    public ProductsAdapter(List<Product> items, OnProductListener myListener) {
        this.items = items;
        this.myListener = myListener;
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
        holder.quantity.setText("x" +item.quantity);
        holder.totalPrice.setText("€ " + item.price);
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
        public final ImageButton remove;
        public Product mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            name = mView.findViewById(R.id.product_name);
            totalPrice = mView.findViewById(R.id.product_totalprice);
            quantity = mView.findViewById(R.id.product_quantity);
            remove = mView.findViewById(R.id.btn_delete);

            remove.setOnClickListener(v -> {
                myListener.onRemoved(mItem);
            });
        }

        @Override
        public String toString() {
            return "View Holder";
        }
    }
}
