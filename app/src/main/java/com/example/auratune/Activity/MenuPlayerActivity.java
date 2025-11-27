package com.example.auratune.Activity;

import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;

import com.example.auratune.Adapter.BannerAdapter;
import com.example.auratune.Adapter.CategoryAdapter;
import com.example.auratune.Adapter.FavoriteAdapter;
import com.example.auratune.Domain.BannerModel;
import com.example.auratune.ViewModel.MainViewModel;
import com.example.auratune.databinding.ActivityMenuPlayerBinding;

import java.util.ArrayList;

public class MenuPlayerActivity extends AppCompatActivity {
    private ActivityMenuPlayerBinding binding;
    private MainViewModel viewModel;

    private FavoriteAdapter favoriteAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityMenuPlayerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Use ViewModelProvider for lifecycle awareness
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);

        initCategory();
        initBanner();
        initFavorite();
    }

    private void initCategory() {
        binding.progressBarCategory.setVisibility(View.VISIBLE);
        viewModel.loadCategory().observe(this, categoryModels -> {
            binding.progressBarCategory.setVisibility(View.GONE);
            binding.categoryView.setLayoutManager(new LinearLayoutManager(
                    MenuPlayerActivity.this, LinearLayoutManager.HORIZONTAL, false));
            binding.categoryView.setAdapter(new CategoryAdapter(categoryModels));
            binding.categoryView.setNestedScrollingEnabled(true);
        });
    }

    private void banners(ArrayList<BannerModel> bannerModels) {
        binding.viewPagerBanner.setAdapter(new BannerAdapter(binding.viewPagerBanner, bannerModels));
        binding.viewPagerBanner.setClipToPadding(false);
        binding.viewPagerBanner.setClipChildren(false);
        binding.viewPagerBanner.setOffscreenPageLimit(5);
        binding.viewPagerBanner.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(40));

        binding.viewPagerBanner.setPageTransformer(compositePageTransformer);
    }

    private void initBanner() {
        binding.progressBarBanner.setVisibility(View.VISIBLE);
        viewModel.loadBanner().observe(this, bannerModels -> {
            if (bannerModels != null && !bannerModels.isEmpty()) {
                banners(bannerModels);
                binding.progressBarBanner.setVisibility(View.GONE);
            }
        });
    }

    // ✅ Favorite section implementation
    private void initFavorite() {
        binding.progressBarFavorite.setVisibility(View.VISIBLE);

        favoriteAdapter = new FavoriteAdapter(this);
        binding.favoriteView.setLayoutManager(new LinearLayoutManager(
                MenuPlayerActivity.this, LinearLayoutManager.VERTICAL, false));
        binding.favoriteView.setAdapter(favoriteAdapter);

        viewModel.loadFavorite().observe(this, favoriteModels -> {
            binding.progressBarFavorite.setVisibility(View.GONE);
            if (favoriteModels != null) {
                favoriteAdapter.setFavoriteList(favoriteModels);
            }
        });
    }
}
