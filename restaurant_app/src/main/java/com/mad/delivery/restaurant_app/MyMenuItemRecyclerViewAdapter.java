package com.mad.delivery.restaurant_app;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Order} and makes a call to the
 * specified {@link PendingOrdersFragment.OnPendingOrderListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyMenuItemRecyclerViewAdapter extends RecyclerView.Adapter<MyMenuItemRecyclerViewAdapter.ViewHolder> {

    private final List<MenuItemRest> menuItems;
    private View view;
    private Context context;
    public MyMenuItemRecyclerViewAdapter(List<MenuItemRest> menuItems,Context context) {
        this.menuItems = menuItems;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.menu_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final MenuItemRest mItem = menuItems.get(position);
        NumberFormat format = NumberFormat.getCurrencyInstance(Locale.ITALY);
        String currency = format.format(Double.parseDouble(mItem.price.toString()));
        holder.price.setText(currency);
        holder.description.setText(mItem.description.toString());
        holder.availability.setText(mItem.availability.toString());
        holder.name.setText(mItem.name);
        holder.mView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Do you want to delete the item?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Database.getInstance().removeMenuItem(mItem);
                        menuItems.remove(mItem);
                        notifyItemRemoved(position);
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
                builder.show();
                return true;
            }
        });
        Uri url = Uri.parse(mItem.imgUrl);

        if(url!= Uri.EMPTY)
            holder.image.setImageURI(url);



        Database.getInstance().getImage(mItem.imageName, "/images/menuItems/", new Callback() {
            @Override
            public void onCallback(Uri item) {
                if (item != null) {
                    if (item == Uri.EMPTY || item.toString().equals("")) {
                        Log.d("MADAPP", "Setting user default image");
                        //imageProfileUri = Uri.EMPTY;
                        holder.image.setImageResource(R.drawable.restaurant_default);
                    } else {
                        Log.d("MADAPP", "Setting custom user image");
                        //  imageProfileUri = item;
                        // imgProfile.setImageURI(item);
                        Picasso.get().load(item.toString()).into(holder.image);
                        // u.imageUri = saveImage(item,EditProfileActivity.this).toString();

                    }
                }

            }
        });
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, NewMenuItemActivity.class);
                intent.putExtra("id", mItem.id);
                context.startActivity(intent);

            }

        });
    }

    @Override
    public int getItemCount() {
        return menuItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        // final TextView titleView;
        public final TextView name;
        public final TextView description;
        public final TextView availability;
        public final TextView price;
        public final ImageView image;
        /*    public final TextView availability;
            public final TextView category;*/
        public Button button;


        public ViewHolder(View view) {
            super(view);
            mView = view;
            //   titleView = mView.findViewById(R.id.menuItem).
            name = mView.findViewById(R.id.tv_menuItemsName);
            description = mView.findViewById(R.id.tv_menuItemsDescription);
            image = mView.findViewById(R.id.imageView2);
            price = mView.findViewById(R.id.tv_menuItemsPrice);
            button = mView.findViewById(R.id.newMenuItem);
            availability = mView.findViewById(R.id.tv_menuItemsAvailability);
        }
    }

    public static int LONG_PRESS_TIME = 500; // Time in miliseconds



}