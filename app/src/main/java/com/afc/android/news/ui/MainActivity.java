package com.afc.android.news.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.SQLException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.afc.android.news.R;
import com.afc.android.news.adapters.NewsAdapter;
import com.afc.android.news.database.DataSource;
import com.afc.android.news.model.NewsItem;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    public ActionBarDrawerToggle mDrawerToggle;

    public String[] mNavListItems;
    public String mAitnews = "https://aitnews.com/feed/";
    public DataSource mDataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Aitnews");

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        mDataSource = new DataSource(this);
        mDataSource.open();

        getNews();

        // Code to handle the SwipeRefreshLayout
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getNews();
            }
        });
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);

        // Code to handle the Loading and RecyclerView animation
        mRecyclerView.addItemDecoration(new RVVerticalSpace(50));

        // Code to manage sliding Navigation drawer
        mNavListItems = getResources().getStringArray(R.array.nav_list_items);
        mDrawerList.setAdapter(new ArrayAdapter<>(this, R.layout.drawer_list_item, mNavListItems));
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                mDrawerLayout.closeDrawer(mDrawerList);
                switch (position) {
                    case 0:
                        Intent intent = new Intent(MainActivity.this, AboutActivity.class);
                        startActivity(intent);
                        break;
                }
            }
        });
        setupDrawer();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDataSource.close();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mDataSource.open();
    }

    // Code to manage the Navigation Drawer
    private void setupDrawer() {
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close) {

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    // Code to get the news
    private void getNews() {

        if (isNetworkAvailable()) {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(mAitnews)
                    .build();
            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    mSwipeRefreshLayout.setRefreshing(false);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    try {
                        if (response.isSuccessful()) {

                            ArrayList<NewsItem> newsItems = getNewsItems(mAitnews);
                            for (NewsItem newsItem : newsItems) {
                                try {
                                    mDataSource.createNews(newsItem);
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                            }
                        } else {

                        }
                    } catch (IOException | ParserConfigurationException | SAXException e) {
                        e.printStackTrace();
                    }
                }
            });

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    NewsAdapter adapter = new NewsAdapter(MainActivity.this, mDataSource.getAllNews());
                    mRecyclerView.setAdapter(adapter);
                    mRecyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                    // Stop refresh animation
                    mSwipeRefreshLayout.setRefreshing(false);
                }
            });
        } else {
            mSwipeRefreshLayout.setRefreshing(false);
            Toast.makeText(this, R.string.network_unavialable_message, Toast.LENGTH_SHORT).show();
        }
    }

    private ArrayList<NewsItem> getNewsItems(String link) throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = builderFactory.newDocumentBuilder();
        Document xmlDoc = builder.parse(link);
        ArrayList<NewsItem> newsItems = new ArrayList<>();

        if (xmlDoc != null) {
            Element root = xmlDoc.getDocumentElement();
            Node channel = root.getChildNodes().item(1);
            NodeList allItems = channel.getChildNodes();
            for (int i = 0; i < allItems.getLength(); i++) {

                Node currentItem = allItems.item(i);
                if (currentItem.getNodeName().equalsIgnoreCase("item")) {
                    NewsItem newsItem = new NewsItem();
                    NodeList currentItemElements = currentItem.getChildNodes();

                    for (int j = 0; j < currentItemElements.getLength(); j++) {

                        Node currentItemDetail = currentItemElements.item(j);
                        if (currentItemDetail.getNodeName().equalsIgnoreCase("title")) {
                            newsItem.setTitle(currentItemDetail.getTextContent());
                        } else if (currentItemDetail.getNodeName().equalsIgnoreCase("link")) {
                            newsItem.setLink(currentItemDetail.getTextContent());
                        }
//                        else if(currentItemDetail.getNodeName().equalsIgnoreCase("description")) {
//                            newsItem.setDescription(currentItemDetail.getTextContent());
//                        }
                        else if (currentItemDetail.getNodeName().equalsIgnoreCase("pubDate")) {
                            newsItem.setPubDate(currentItemDetail.getTextContent());
                        } else if (currentItemDetail.getNodeName().equalsIgnoreCase("media:content")) {
                            String thumbnailUrl = currentItemDetail.getAttributes().item(1).getTextContent();
                            newsItem.setThumbnail(thumbnailUrl);
                        }
                    }
                    newsItems.add(newsItem);
                }
            }
        }
        return newsItems;
    }

    // This method checks if the there's network or not
    private boolean isNetworkAvailable() {
        ConnectivityManager manager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        boolean isAvailable = false;
        if (networkInfo != null && networkInfo.isConnected()) {
            isAvailable = true;
        }
        return isAvailable;
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

        // Activate the navigation drawer toggle
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
