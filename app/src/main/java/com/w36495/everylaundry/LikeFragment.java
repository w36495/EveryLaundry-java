package com.w36495.everylaundry;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import timber.log.Timber;

public class LikeFragment extends Fragment {

    private RecyclerView like_recyclerView;
    private LaundryLikeAdapter laundryLikeAdapter;

    private ArrayList<Laundry> laundryList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_like_list, container, false);

        Timber.plant(new Timber.DebugTree());
        setInit(view);

        return view;
    }

    private void setInit(View view) {

        like_recyclerView = view.findViewById(R.id.like_recyclerView);

        laundryList.add(new Laundry(000001, "세탁소명", "전화번호", "도로명주소", "우편번호", 0.0, 0.1));
        laundryList.add(new Laundry(000001, "세탁소명", "전화번호", "도로명주소", "우편번호", 0.0, 0.1));
        laundryList.add(new Laundry(000001, "세탁소명", "전화번호", "도로명주소", "우편번호", 0.0, 0.1));

        laundryLikeAdapter = new LaundryLikeAdapter(view.getContext(), laundryList);

        like_recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        like_recyclerView.setAdapter(laundryLikeAdapter);


    }
}
