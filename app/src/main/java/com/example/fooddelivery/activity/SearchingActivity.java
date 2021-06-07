package com.example.fooddelivery.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.fooddelivery.adapter.ItemOnMainAdapter;
import com.example.fooddelivery.adapter.SearchingItemAdapter;
import com.example.fooddelivery.R;
import com.example.fooddelivery.model.Product;

import java.util.ArrayList;
import java.util.List;

public class SearchingActivity extends AppCompatActivity {

    TextView textViewDeleteSearches, textViewDeleteWatches, textViewNoSearches, textViewNoWatches;
    EditText editTextSearchInput;
    RecyclerView recyclerViewSearches, recyclerViewWatches;
    List<String> searchList;
    List<Product> watchList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searching);

        initView();
        initRecyclerViewSearches();
        initRecyclerViewWatches();
        deleteSearches();
        deleteWatches();
    }

    private void initView() {
        editTextSearchInput = findViewById(R.id.et_search_input);
        recyclerViewSearches = findViewById(R.id.recycler_view_searches);
        recyclerViewWatches = findViewById(R.id.recycler_view_watches);
        textViewDeleteSearches = findViewById(R.id.btn_delete_searches);
        textViewNoSearches = findViewById(R.id.tv_no_search_data);
        textViewNoWatches = findViewById(R.id.tv_no_watch_data);
        textViewDeleteWatches = findViewById(R.id.btn_delete_watches);
    }

    public void initRecyclerViewSearches() {
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerViewSearches.setLayoutManager(layoutManager);
        getSearchesList();
        setRecyclerViewSearchesAdapter();
    }

    private void setRecyclerViewSearchesAdapter () {
        SearchingItemAdapter searchingItemAdapter = new SearchingItemAdapter(this, searchList);
        recyclerViewSearches.setAdapter(searchingItemAdapter);
    }

    private void getSearchesList() {
        searchList = new ArrayList<>();
        searchList.add("Bánh mì Hoa Sứ");
        searchList.add("Highlands Dĩ An");
        searchList.add("Cà phê sữa");
        if (searchList.size() == 0) {
            textViewNoSearches.setVisibility(View.VISIBLE);
        }
    }

    private void getWatchesList() {
        watchList = new ArrayList<>();
        watchList.add(new Product("Green Tea Freeze", "4.6", "39.000"));
        watchList.add(new Product("Green Tea Freeze", "4.6", "39.000"));
        watchList.add(new Product("Green Tea Freeze", "4.6", "39.000"));
    }

    public void setSearchInputFromUp (String input) {
        editTextSearchInput.setText(input);
    }

    public void deleteSearches () {
        textViewDeleteSearches.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchList = new ArrayList<>();
                setRecyclerViewSearchesAdapter();
                textViewNoSearches.setVisibility(View.VISIBLE);
            }
        });
    }

    public void deleteWatches () {
        textViewDeleteWatches.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                watchList = new ArrayList<>();
                setRecyclerViewWatchesAdapter();
                textViewNoWatches.setVisibility(View.VISIBLE);
            }
        });
    }

    public void initRecyclerViewWatches() {
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerViewWatches.setLayoutManager(layoutManager);
        getWatchesList();
        setRecyclerViewWatchesAdapter();
    }

    private void setRecyclerViewWatchesAdapter () {
        ItemOnMainAdapter itemOnMainAdapter = new ItemOnMainAdapter(this, watchList);
        recyclerViewWatches.setAdapter(itemOnMainAdapter);
    }
}