package com.deepesh.epaperenglish.Activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewStub;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.deepesh.epaperenglish.Adapter.GridViewAdapter;
import com.deepesh.epaperenglish.Model.Product;
import com.deepesh.epaperenglish.Model.Util;
import com.deepesh.epaperenglish.R;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{


    private ViewStub stubGrid;
    private GridView gridView;
    private GridViewAdapter gridViewAdapter;
    private List<Product> productList;
    private int currentViewMode = 0;
    private Menu mymenu;

    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 2;

    static final int VIEW_MODE_LISTVIEW = 0;
    static final int VIEW_MODE_GRIDVIEW = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        stubGrid = (ViewStub) findViewById(R.id.stub_grid);

        //Inflate ViewStub before get view
        stubGrid.inflate();

        gridView = (GridView) findViewById(R.id.mygridview);

        //get list of product
        getProductList();

        //Get current view mode in share reference
        SharedPreferences sharedPreferences = getSharedPreferences("ViewMode", MODE_PRIVATE);
        currentViewMode = sharedPreferences.getInt("currentViewMode", VIEW_MODE_LISTVIEW);//Default is view listview
        //Register item lick
        gridView.setOnItemClickListener(this);

        //switchView();

        gridViewAdapter = new GridViewAdapter(this, R.layout.grid_item1, productList);
        gridView.setAdapter(gridViewAdapter);

        if (Build.VERSION.SDK_INT >= 23) {
            // Pain in A$$ Marshmallow+ Permission APIs
            checkAndRequestPermissions();

        } else {
            // Pre-Marshmallow

        }

    }


    private  boolean checkAndRequestPermissions() {
        int permissionStorage = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        List<String> listPermissionsNeeded = new ArrayList<>();
        if (permissionStorage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }

        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        //setUpView();

        return true;
    }
    public static boolean isConnectingToInternet(Context context)
    {
        ConnectivityManager connectivity =
                (ConnectivityManager) context.getSystemService(
                        Context.CONNECTIVITY_SERVICE);
        if (connectivity != null)
        {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED)
                    {
                        return true;
                    }
        }
        return false;
    }

    public List<Product> getProductList() {
        //pseudo code to get product, replace your code to get real product here
        productList = new ArrayList<>();

        productList.add(new Product(R.drawable.epaper_toi, "times of india", "https://epaper.timesgroup.com/Olive/ODN/TimesOfIndia/#"));
        productList.add(new Product(R.drawable.epaper_tie, "indian express", "http://epaper.indianexpress.com/"));
        productList.add(new Product(R.drawable.epaper_dh, "deccan herald", "http://www.deccanheraldepaper.com/"));
        productList.add(new Product(R.drawable.epaper_dna, "DNA", "http://epaper2.dnaindia.com/"));
        productList.add(new Product(R.drawable.epaper_taa, "asian age", "http://onlineepaper.asianage.com/"));
        productList.add(new Product(R.drawable.epaper_dc, "deccan chronicle", "http://epaper.deccanchronicle.com/states.aspx"));
        productList.add(new Product(R.drawable.epaper_tet, "the echonomic times", "https://epaperlive.timesofindia.com/?AspxAutoDetectCookieSupport=1"));
        productList.add(new Product(R.drawable.epaper_tribune, "the tribune", "http://epaper.tribuneindia.com/t/106"));
        productList.add(new Product(R.drawable.epaper_ht, "hindustan times", "http://paper.hindustantimes.com/epaper/viewer.aspx"));
        productList.add(new Product(R.drawable.epaper_ht, "hindustan times", "http://www.hindustantimes.com/"));

        productList.add(new Product(R.drawable.epaper_bs, "business standard", "http://www.business-standard.com/"));
        productList.add(new Product(R.drawable.epaper_tfe, "financial express", "http://epaper.financialexpress.com/"));
        productList.add(new Product(R.drawable.epaper_telegraph, "the telegraph", "https://www.telegraphindia.com/"));
        productList.add(new Product(R.drawable.epaper_mint, "live mint", "http://epaper.livemint.com/epaper/viewer.aspx"));
        return productList;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(getApplicationContext(), productList.get(position).getTitle(), Toast.LENGTH_SHORT).show();
        if(isConnectingToInternet(MainActivity.this) &&productList.get(position).getDescription()!=null)
        {
            //Toast.makeText(getApplicationContext(),"internet is available",Toast.LENGTH_LONG).show();
            if(productList.get(position).getDescription()!=null){
                Intent intent=new Intent(this,WebActivity.class);
                intent.putExtra(Util.URL_KEY,productList.get(position).getDescription());
                intent.putExtra(Util.TITLE_KEY,productList.get(position).getTitle());
                startActivity(intent);
            }else {

            }
        }
        else {
            Toast.makeText(this, "internet is not available", Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        mymenu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id=item.getItemId();
        switch (id) {
            case R.id.shareIcon:
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                StringBuilder sb = new StringBuilder();
                //sb.append("Hindi Newspaper-हिंदी समाचार,All Hindi News Paper.");
                sb.append("https://play.google.com/store/apps/details?id="+ this.getPackageName());
                //sharingIntent.addFlags(ActivityFlags.ClearWhenTaskReset);
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "AndroidSolved");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, sb.toString());
                startActivity(Intent.createChooser(sharingIntent, "Share via"));
                break;

            case R.id.starIcon:
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("market://details?id=" + this.getPackageName())));
                } catch (android.content.ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://play.google.com/store/apps/details?id=" + this.getPackageName())));
                }
                /*Intent intent = new Intent(android.content.Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://play.google.com/store/apps/details?id=com.google.android.youtube"));
                startActivity(intent);*/
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
