package com.alpheus.naturonik.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.alpheus.naturonik.Activities.ProductActivity;
import com.alpheus.naturonik.Models.Product;
import com.alpheus.naturonik.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.MyViewHolder>
        implements Filterable {

    private Context context;
    private List<Product> productList;
    private List<Product> productListFiltered;

    public ProductsAdapter(Context context, List<Product> productList) {

        this.context = context;
        this.productList = productList;
        this.productListFiltered = productList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.product_card_view, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        final Product product = productListFiltered.get(position);

        holder.tv_description.setText(product.getDescription());

        Glide.with(context)
                .load("https://www.naturonik.ru/img/" + product.getImage())
                .apply(RequestOptions.centerCropTransform())
                .into(holder.thumbnail);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, ProductActivity.class);
                intent.putExtra("price", productList.get(position).getPrice());
                intent.putExtra("countrys_name", productList.get(position).getCountryName());
                intent.putExtra("sorts_name", productList.get(position).getSortName());
                intent.putExtra("description", productList.get(position).getDescription());
                intent.putExtra("img", productList.get(position).getImage());
                intent.putExtra("about", productList.get(position).getAbout());
                intent.putExtra("energy_value", productList.get(position).getEnergy_value());
                intent.putExtra("nutritional_value", productList.get(position).getNutritional_value());

                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return productListFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {

                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    productListFiltered = productList;
                } else {
                    List<Product> filteredList = new ArrayList<>();
                    for (Product row : productList) {
                        if (row.getDescription().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }
                    productListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = productListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {

                productListFiltered = (ArrayList<Product>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    //ViewHolder
    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv_description;
        TextView tv_price;
        TextView tv_sort;
        TextView tv_country;
        ImageView thumbnail;
        CardView cardView;

        public MyViewHolder(View view) {

            super(view);
            tv_description = view.findViewById(R.id.tv_description);
            thumbnail = view.findViewById(R.id.thumbnail);
            tv_price = itemView.findViewById(R.id.price);
            tv_sort = itemView.findViewById(R.id.product_sort);
            tv_country = itemView.findViewById(R.id.product_country);
            cardView = itemView.findViewById(R.id.cardview_id);
        }
    }
}
