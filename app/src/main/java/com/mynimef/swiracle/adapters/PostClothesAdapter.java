package com.mynimef.swiracle.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mynimef.swiracle.R;
import com.mynimef.swiracle.api.database.ClothesElement;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class PostClothesAdapter extends RecyclerView.Adapter<PostClothesAdapter.ClothesView> {
    private final List<ClothesElement> clothesList;

    public PostClothesAdapter(List<ClothesElement> clothesList) {
        this.clothesList = clothesList;
        Log.d("size", String.valueOf(clothesList.size()));
    }

    @NotNull
    @Override
    public ClothesView onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.adapter_clothes_post, viewGroup, false);
        return new ClothesView(view);
    }

    @Override
    public void onBindViewHolder(ClothesView clothesView, final int position) {
        ClothesElement element = clothesList.get(position);
        clothesView.getBrand().setText(element.getBrand());
        clothesView.getDescription().setText(element.getDescription());
        clothesView.getPrice().setText(element.getPrice().getRub() + " RUB");
    }

    @Override
    public void onViewRecycled(@NonNull ClothesView holder) {
        super.onViewRecycled(holder);
    }

    @Override
    public int getItemCount() {
        return clothesList.size();
    }

    static class ClothesView extends RecyclerView.ViewHolder {
        private final TextView brand;
        private final TextView description;
        private final TextView price;

        public ClothesView(View view) {
            super(view);
            this.brand = view.findViewById(R.id.elementBrand);
            this.description = view.findViewById(R.id.elementDescription);
            this.price = view.findViewById(R.id.elementPrice);
        }

        public TextView getBrand() { return brand; }
        public TextView getDescription() { return description; }
        public TextView getPrice() { return price; }
    }
}
