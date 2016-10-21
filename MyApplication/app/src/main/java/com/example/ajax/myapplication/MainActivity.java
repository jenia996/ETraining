package com.example.ajax.myapplication;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.ajax.myapplication.download.OnResultCallback;
import com.example.ajax.myapplication.download.impl.ImageLoader;
import com.example.ajax.myapplication.download.impl.LoadOperarion;
import com.example.ajax.myapplication.download.impl.Loader;
import com.example.ajax.myapplication.download.impl.MemCache;
import com.example.ajax.myapplication.download.impl.RetainFragment;
import com.example.ajax.myapplication.model.entity.Book;
import com.example.ajax.myapplication.view.BaseActivity;
import com.example.ajax.myapplication.view.adapters.impl.BookAdapter;

import java.util.List;

public class MainActivity extends BaseActivity {

    private static final String TAG = "MAIN_ACTIVTY";
    private final String ADRESS = "https://lh4.googleusercontent" + "" +
            ".com/-v0soe-ievYE/AAAAAAAAAAI/AAAAAAADnx8/TYw5hefoVmg/s0-c-k-no-ns/photo.jpg";
    ImageLoader loader = new ImageLoader();
    private TextView textView;
    private MainPresenter mainPresenter;
    private MemCache memCache;
    private List<Book> books;
    private BookAdapter adapter;
    private RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainPresenter = new MainPresenter(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        // final ImageView imageView = (ImageView) findViewById(R.id.image);
        adapter = new BookAdapter();
        RetainFragment mRetainFragment = RetainFragment.findOrCreateRetainFragment
                (getSupportFragmentManager());
        memCache = mRetainFragment.memCache;
        if (memCache == null) {
            memCache = new MemCache();
            mRetainFragment.memCache = memCache;
        }

        Loader loader = new Loader();
        loader.execute(new LoadOperarion(memCache), ADRESS, new OnResultCallback<Bitmap, Void>() {
            @Override
            public void onSucess(Bitmap bitmap) {
                memCache.put(ADRESS, bitmap);
                //imageView.setImageBitmap(bitmap);
            }

            @Override
            public void onError(Exception e) {

            }

            @Override
            public void onProgressChange(Void aVoid) {

            }
        });


        recyclerView = (RecyclerView) findViewById(R.id.list_books);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        mainPresenter.onReady();

    }


    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        Log.d(TAG, "Finalize");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "OnDestroy");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showResponce(String responce) {
        textView.setText(responce);
    }

    @Override
    public void showResponce(List<Book> responce) {
        adapter.setBooks(responce);
        adapter.notifyDataSetChanged();
    }
}


