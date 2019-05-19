package com.mad.delivery.restaurant_app.menu;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.mad.delivery.resources.MenuItemRest;
import com.mad.delivery.resources.MenuOffer;
import com.mad.delivery.resources.MyDateFormat;
import com.mad.delivery.restaurant_app.R;

import org.joda.time.DateTime;

import java.util.List;

public class OffersListAdapter extends RecyclerView.Adapter<OffersListAdapter.ViewHolder> {
    private List<MenuOffer> items;
    private View view;
    private OnMenuChanged mListener;

    public OffersListAdapter(List<MenuOffer> items, OnMenuChanged mListener) {
        this.items = items;
        this.mListener = mListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.offer_item_layout_forlist, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);


        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int index) {
        MenuOffer item = items.get(index);
        holder.mItem = item;
        holder.products.setText("");
        holder.mItem.items.forEach( i -> holder.products.append(i.name + "\n"));
        holder.price.setText(item.price+ " €");
        DateTime expiringDate = new DateTime(holder.mItem.endDateTime);
        if(expiringDate.isBeforeNow()) {
            holder.expiring.setText("expired");
        } else {
            holder.expiring.setText(" "+MyDateFormat.parse(new DateTime(holder.mItem.endDateTime)));
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView products;
        public final TextView price;
        public final TextView expiring;
        public final ImageButton btnMinus;
        public MenuOffer mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            products = mView.findViewById(R.id.tv_offer_products);
            price = mView.findViewById(R.id.tv_offer_price);
            expiring = mView.findViewById(R.id.tv_offer_expiringdate);
            btnMinus = mView.findViewById(R.id.tv_remove_offer);
            btnMinus.setOnClickListener(v -> {
                mListener.offerRemoved(mItem);
            });
        }

        @Override
        public String toString() {
            return "View Holder";
        }
    }
}
