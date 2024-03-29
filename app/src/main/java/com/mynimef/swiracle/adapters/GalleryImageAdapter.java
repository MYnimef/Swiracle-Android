package com.mynimef.swiracle.adapters;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.mynimef.swiracle.R;
import com.mynimef.swiracle.fragments.navigation.create.pickImage.PickImageFragment;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public final class GalleryImageAdapter extends RecyclerView.Adapter<GalleryImageAdapter.GalleryView> {
    private final List<Uri> imagesList;
    private final PickImageFragment fragment;
    private int selectedId;
    private boolean[] selectedField;

    public GalleryImageAdapter(List<Uri> imagesList, PickImageFragment fragment) {
        this.imagesList = imagesList;
        this.fragment = fragment;

        this.selectedField = new boolean[imagesList.size()];
        this.selectedId = 0;
        this.selectedField[selectedId] = true;
    }

    public Uri clearPicked() {
        selectedField = new boolean[imagesList.size()];
        selectedField[selectedId] = true;
        return imagesList.get(selectedId);
    }

    @NotNull
    @Override
    public GalleryView onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.adapter_image, viewGroup, false);
        return new GalleryView(view);
    }

    @Override
    public void onBindViewHolder(GalleryView galleryView, final int position) {
        ImageView pic = galleryView.getPic();

        Glide
                .with(fragment)
                .load(imagesList.get(position))
                .thumbnail(0.05f)
                .centerInside()
                .into(pic);

        pic.setActivated(selectedField[position]);
    }

    @Override
    public void onViewRecycled(@NonNull GalleryView holder) {
        super.onViewRecycled(holder);
        Glide.with(fragment).clear(holder.getPic());
    }

    @Override
    public int getItemCount() {
        return imagesList.size();
    }

    class GalleryView extends RecyclerView.ViewHolder {
        private final ImageView pic;

        public GalleryView(View view) {
            super(view);

            view.setOnClickListener(v -> {
                int position = getBindingAdapterPosition();
                if (!selectedField[position]) {
                    selectedField[position] = true;
                    fragment.setImageView(imagesList.get(position));
                    fragment.addToPicked(imagesList.get(position));
                    notifyItemChanged(position);

                    if (!fragment.getMultiple()) {
                        selectedField[selectedId] = false;
                        fragment.removeFromPicked(imagesList.get(selectedId));
                        notifyItemChanged(selectedId);
                    }

                    selectedId = position;
                } else {
                    if (fragment.getMultiple()) {
                        selectedField[position] = false;
                        fragment.removeFromPicked(imagesList.get(position));
                        notifyItemChanged(position);
                    }
                }
            });
            pic = view.findViewById(R.id.imageView);
        }

        public ImageView getPic() {
            return pic;
        }
    }
}