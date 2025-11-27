package com.example.auratune.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.auratune.Domain.BannerModel;
import com.example.auratune.Domain.CategoryModel;
//import com.example.auratune.Domain.FavoriteModel;
import com.example.auratune.Domain.FavoriteModel;
import com.example.auratune.Repository.MainRepository;

import java.util.ArrayList;

public class MainViewModel extends ViewModel {
    private final MainRepository repository= new MainRepository();

    public LiveData<ArrayList<CategoryModel>> loadCategory(){
        return repository.loadCategory();
    }

    public LiveData<ArrayList<BannerModel>> loadBanner(){
        return repository.loadBanner();
    }

    public LiveData<ArrayList<FavoriteModel>> loadFavorite() {
        return repository.loadFavorite();
    }

}
