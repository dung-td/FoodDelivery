package com.example.fooddelivery.activity.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.example.fooddelivery.activity.login.LoginActivity;
import com.example.fooddelivery.adapter.ItemOnMainAdapter;
import com.example.fooddelivery.adapter.SearchingItemAdapter;
import com.example.fooddelivery.R;
import com.example.fooddelivery.fragment.HomeFragment;
import com.example.fooddelivery.model.OnGetDataListener;
import com.example.fooddelivery.model.Product;
import com.example.fooddelivery.model.SearchString;

import java.util.ArrayList;
import java.util.List;

public class SearchingActivity extends AppCompatActivity {

    TextView textViewDeleteSearches, textViewNoSearches;
    EditText editTextSearchInput;
    RecyclerView recyclerViewSearches;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searching);

        initView();
        initRecyclerViewSearches();
        deleteSearches();
    }

    private void initView() {
        editTextSearchInput = findViewById(R.id.et_search_input);
        recyclerViewSearches = findViewById(R.id.recycler_view_searches);
        textViewDeleteSearches = findViewById(R.id.btn_delete_searches);
        textViewNoSearches = findViewById(R.id.tv_no_search_data);
    }

    public void initRecyclerViewSearches() {
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerViewSearches.setLayoutManager(layoutManager);
        getSearchesList();
    }

    private void setRecyclerViewSearchesAdapter () {
        ArrayList<String> searchData = new ArrayList<>();
        for (SearchString search : LoginActivity.firebase.searchList) {
            searchData.add(search.getDetail());
        }
        if (searchData.isEmpty()) {
            textViewDeleteSearches.setVisibility(View.INVISIBLE);
            textViewNoSearches.setVisibility(View.VISIBLE);
        }
        else {
            textViewNoSearches.setVisibility(View.INVISIBLE);
            textViewDeleteSearches.setVisibility(View.VISIBLE);
        }
        SearchingItemAdapter searchingItemAdapter = new SearchingItemAdapter(this, searchData);
        recyclerViewSearches.setAdapter(searchingItemAdapter);
    }

    private void getSearchesList() {
        if (HomeFragment.isSearchFirstClick) {
            Log.d("1", "get data");
            LoginActivity.firebase.getSearchData(new OnGetDataListener() {
                @Override
                public void onStart() {

                }

                @Override
                public void onSuccess() {
                    Log.d("3", "onSuccess");
                    HomeFragment.isSearchFirstClick = false;
                    if (LoginActivity.firebase.searchList.size() == 0) {
                        textViewNoSearches.setVisibility(View.VISIBLE);
                    }
                    else {
                        setRecyclerViewSearchesAdapter();
                    }
                }
            });
        }
        else {
            Log.d("2", "no need get data");
            setRecyclerViewSearchesAdapter();
        }
    }

    public void setSearchInputFromUp (String input) {
        editTextSearchInput.setText(input);
    }

    public void deleteSearches () {
        textViewDeleteSearches.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginActivity.firebase.removeSearchData(new OnGetDataListener() {
                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onSuccess() {
                        recyclerViewSearches.removeAllViewsInLayout();
                        recyclerViewSearches.invalidate();
                        textViewNoSearches.setVisibility(View.VISIBLE);
                        textViewDeleteSearches.setVisibility(View.INVISIBLE);
                    }
                });
            }
        });
    }

    private void startSearchingInput() {
        editTextSearchInput.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    return true;
                }
                return false;
            }
        });
    }
}