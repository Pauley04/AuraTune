package com.example.auratune.Repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.auratune.Domain.BannerModel;
import com.example.auratune.Domain.CategoryModel;
//import com.example.auratune.Domain.FavoriteModel;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainRepository {
    private final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

    public LiveData<ArrayList<CategoryModel>> loadCategory() {
        MutableLiveData<ArrayList<CategoryModel>> listData = new MutableLiveData<>();
        DatabaseReference ref = firebaseDatabase.getReference("Category");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<CategoryModel> list = new ArrayList<>();
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    CategoryModel item = childSnapshot.getValue(CategoryModel.class);
                    if (item != null) list.add(item);
                }
                listData.setValue(list);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        return listData;
    }

    // CRUD //
    public Task<Void> createCategory(@NonNull CategoryModel categoryModel) {
        DatabaseReference ref = firebaseDatabase.getReference("Category");
        return ref.child(String.valueOf(categoryModel.getId())).setValue(categoryModel);
    }

    public Task<Void> updateCategory(@NonNull String id, @NonNull CategoryModel categoryModel) {
        Map<String, Object> updates = new HashMap<>();
        updates.put("id", categoryModel.getId());
        updates.put("title", categoryModel.getTitle());
        updates.put("picUrl", categoryModel.getPicUrl());
        DatabaseReference ref = firebaseDatabase.getReference("Category");
        return ref.child(id).updateChildren(updates);
    }

    public Task<Void> deleteCategory(@NonNull String id) {
        DatabaseReference ref = firebaseDatabase.getReference("Category");
        return ref.child(id).removeValue();
    }

    public LiveData<ArrayList<BannerModel>> loadBanner() {
        MutableLiveData<ArrayList<BannerModel>> listData = new MutableLiveData<>();
        DatabaseReference ref = firebaseDatabase.getReference("Banner");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<BannerModel> list = new ArrayList<>();
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    BannerModel item = childSnapshot.getValue(BannerModel.class);
                    if (item != null) list.add(item);
                }
                listData.setValue(list);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        return listData;
    }

    // CRUD //
    public Task<Void> createBanner(@NonNull String id, @NonNull BannerModel bannerModel) {
        DatabaseReference ref = firebaseDatabase.getReference("Banner");
        return ref.child(id).setValue(bannerModel);
    }

    public Task<Void> updateBanner(@NonNull String id, @NonNull BannerModel bannerModel) {
        Map<String, Object> updates = new HashMap<>();
        updates.put("url", bannerModel.getUrl());
        DatabaseReference ref = firebaseDatabase.getReference("Banner");
        return ref.child(id).updateChildren(updates);
    }

    public Task<Void> deleteBanner(@NonNull String id) {
        DatabaseReference ref = firebaseDatabase.getReference("Banner");
        return ref.child(id).removeValue();
    }
}
