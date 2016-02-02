package com.hhdd.testkakaoapplication;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.hhdd.testkakaoapplication.event.ImageParserListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements ImageParserListener {

    private ProgressDialog mDialog;
    private ImageParser mParser;
    private ImageLoader mImageLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mImageLoader  = MySingleton.getInstance(this).getImageLoader();
        mParser = new ImageParser();
        mParser.setListener(this);
        startImageLoad();
    }

    private void startImageLoad()
    {
        mDialog = ProgressDialog.show(this, "", "정보를 불러오는 중입니다. 잠시만 기다려주십시오.", true);
        mParser.getImageLists();
    }

    public void onResult(ArrayList<String> list)
    {
        mDialog.hide();
        GridView mainGridView = (GridView) findViewById(R.id.gv_main);
        GridAdapter adapter = new GridAdapter(this, R.layout.item_image, list);
        mainGridView.setAdapter(adapter);

    }

    public void onFault(int code)
    {
        Toast.makeText(this, "이미지 목록을 가져오는데 실패했습니다. 코드 : "+code, Toast.LENGTH_SHORT).show();
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

    public class GridAdapter extends ArrayAdapter<String> {
        public ArrayList<String> items;

        public GridAdapter(Context context, int textViewResourceId, ArrayList<String> items) {
            super(context, textViewResourceId, items);
            this.items = items;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;
            if (v == null) {
                LayoutInflater vi = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(R.layout.item_image, null);
            }

            String src = items.get(position);
            NetworkImageView iv = (NetworkImageView) v.findViewById(R.id.thumbnail);
            iv.setImageUrl(items.get(position), mImageLoader);
            return v;
        }
    }
}
