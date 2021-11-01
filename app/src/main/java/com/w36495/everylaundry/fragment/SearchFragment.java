package com.w36495.everylaundry.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.w36495.everylaundry.LaundryMapActivity;
import com.w36495.everylaundry.data.Laundry;
import com.w36495.everylaundry.R;
import com.w36495.everylaundry.adapter.LaundrySearchAdapter;

import java.util.ArrayList;

import timber.log.Timber;

/**
 * Laundry Search Fragment
 */
public class SearchFragment extends Fragment {

    private RecyclerView search_recyclerView;
    private LaundrySearchAdapter laundrySearchAdapter;

    private EditText search_laundry;

    private ArrayList<Laundry> laundryList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_list, container, false);
        Timber.plant(new Timber.DebugTree());

        Timber.d("onCreateView");
        setInit(view);
        return view;
    }

    private void setInit(View view) {

        search_recyclerView = view.findViewById(R.id.search_recyclerView);
        search_laundry = view.findViewById(R.id.search_laundry);

        laundryList = new ArrayList<>();
        laundryList.add(new Laundry(000001, "세탁소명", "전화번호", "도로명주소", "우편번호", 0.0, 0.1));
        laundryList.add(new Laundry(000001, "세탁소명", "전화번호", "도로명주소", "우편번호", 0.0, 0.1));
        laundryList.add(new Laundry(000001, "세탁소명", "전화번호", "도로명주소", "우편번호", 0.0, 0.1));

        laundrySearchAdapter = new LaundrySearchAdapter(view.getContext(), laundryList);
        search_recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        search_recyclerView.setAdapter(laundrySearchAdapter);

        // 검색창 버튼 클릭했을 때
        search_laundry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(view.getContext(), LaundryMapActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Timber.d("onAttach() 호출");
    }

    @Override
    public void onStart() {
        super.onStart();
        Timber.d("onStart() 호출");
    }

    @Override
    public void onResume() {
        super.onResume();
        Timber.d("onResume() 호출");
    }

    @Override
    public void onPause() {
        super.onPause();
        Timber.d("onPause() 호출");
    }

    @Override
    public void onStop() {
        super.onStop();
        Timber.d("onStop() 호출");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Timber.d("onDestroyView() 호출");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Timber.d("onDestroy() 호출");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Timber.d("onDetach() 호출");
    }
}
