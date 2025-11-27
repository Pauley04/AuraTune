package com.example.auratune.Repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.auratune.Domain.BannerModel;
import com.example.auratune.Domain.CategoryModel;
//import com.example.auratune.Domain.FavoriteModel;
import com.example.auratune.Domain.FavoriteModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainRepository {
    private final FirebaseDatabase firebaseDatabase= FirebaseDatabase.getInstance();

    public LiveData<ArrayList<CategoryModel>> loadCategory(){
        MutableLiveData<ArrayList<CategoryModel>> listData=new MutableLiveData<>();
        DatabaseReference ref=firebaseDatabase.getReference("Category");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<CategoryModel> list=new ArrayList<>();
                for(DataSnapshot childSnapshot:dataSnapshot.getChildren()){
                    CategoryModel item=childSnapshot.getValue(CategoryModel.class);
                    if(item!=null) list.add(item);
                }
                listData.setValue(list);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError){
            }
        });
        return listData;
    }

    public LiveData<ArrayList<BannerModel>> loadBanner(){
        MutableLiveData<ArrayList<BannerModel>> listData=new MutableLiveData<>();
        DatabaseReference ref=firebaseDatabase.getReference("Banner");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<BannerModel> list=new ArrayList<>();
                for(DataSnapshot childSnapshot:dataSnapshot.getChildren()){
                    BannerModel item=childSnapshot.getValue(BannerModel.class);
                    if(item!=null) list.add(item);
                }
                listData.setValue(list);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError){
            }
        });
        return listData;
    }

    public LiveData<ArrayList<FavoriteModel>> loadFavorite(){
        MutableLiveData<ArrayList<FavoriteModel>> listData=new MutableLiveData<>();
        DatabaseReference ref=firebaseDatabase.getReference("Favorite");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<FavoriteModel> list=new ArrayList<>();
                for(DataSnapshot childSnapshot:dataSnapshot.getChildren()){
                    FavoriteModel item=childSnapshot.getValue(FavoriteModel.class);
                    if(item!=null) list.add(item);
                }
                listData.setValue(list);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError){
            }
        });
        return listData;
    }

//    public LiveData<ArrayList<FavoriteModel>> loadFavorite() {
//        MutableLiveData<ArrayList<FavoriteModel>> listData = new MutableLiveData<>();
//        DatabaseReference ref = firebaseDatabase.getReference("Favorite"); // adjust path as needed
//
//        ref.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                ArrayList<FavoriteModel> list = new ArrayList<>();
//                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
//                    FavoriteModel item = childSnapshot.getValue(FavoriteModel.class);
//                    if (item != null) {
//                        // set id from the node key if your model has an id field
//                        if (item.getId() == null || item.getId().isEmpty()) {
//                            item.setId(childSnapshot.getKey());
//                        }
//                        list.add(item);
//                    }
//                }
//                listData.setValue(list);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                listData.postValue(new ArrayList<>());
//            }
//        });
//
//        return listData;
//    }

}
