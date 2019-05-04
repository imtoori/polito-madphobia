package com.mad.delivery.consumerApp.search;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.mad.delivery.consumerApp.R;
import com.mad.delivery.resources.MenuItemRest;

import java.util.List;

public class MenuItemAdapter extends RecyclerView.Adapter<MenuItemAdapter.ViewHolder> {
    private final int MAX_LIMIT_ORDER = 100;
    private List<MenuItemRest> items;
    private View view;
    public MenuItemAdapter(List<MenuItemRest> items) {
        this.items = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.menuitems_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int index) {
        MenuItemRest item = items.get(index);
        holder.mItem = item;
        holder.name.setText(item.name);

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final ImageView menuItemImage;
        public final TextView name;
        public final TextView desc;
        public final ImageButton btnPlus, btnMinus;
        public final TextView etQuantity;
        public MenuItemRest mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            menuItemImage = mView.findViewById(R.id.menu_item_image);
            name = mView.findViewById(R.id.tv_menuitem_name);
            desc = mView.findViewById(R.id.tv_menuitem_desc);
            btnPlus = mView.findViewById(R.id.btn_add_item);
            btnMinus = mView.findViewById(R.id.btn_rem_item);
            etQuantity = mView.findViewById(R.id.tv_menuitem_qty);

            btnPlus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int qty = Integer.valueOf(etQuantity.getText().toString());
                    if(qty < MAX_LIMIT_ORDER) {
                        qty++;
                        etQuantity.setText(String.valueOf(qty));
                    }
                }
            });

            btnMinus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int qty = Integer.valueOf(etQuantity.getText().toString());
                    if(qty > 0) {
                        qty--;
                        etQuantity.setText(String.valueOf(qty));
                    }
                }
            });
        }

        @Override
        public String toString() {
            return "View Holder";
        }
    }
}
