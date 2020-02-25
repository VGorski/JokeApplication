package edu.quinnipiac.videogame;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.ShareActionProvider;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class SpinnerActivity extends AppCompatActivity {
    private final String LOG_TAG = SpinnerActivity.class.getSimpleName();
    String gameResultsJSONString = null;
    ResultsHandler resultsHandler = new ResultsHandler();
    boolean userSelect = false;
    //these need to be changed later
    private String url1 = "";
    private String url2 = "";
    private ShareActionProvider provider;

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.mainmenu,menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        MenuItem shareItem = menu.findItem(R.id.action_share);
        provider = (ShareActionProvider) MenuItemCompat.getActionProvider(shareItem);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        switch (id) {
            case R.id.action_settings:
                Toast.makeText(this, "Here are my settings", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_fav:
                Toast.makeText(this, "Here are my fav", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_share:
                // populate the share intent with data
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, "This is a message for you");
                provider.setShareIntent(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spinner);

        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ArrayAdapter<String> resultsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, resultsHandler.results);
        resultsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(resultsAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (userSelect) {
                    final String item = (String) parent.getItemAtPosition(position);
                    Log.i("onItemSelected : result", item);
                    new FetchResults().execute(item);
                    userSelect = false;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    @Override
    public void onUserInteraction(){
        super.onUserInteraction();
        userSelect = true;
    }
    private class FetchResults extends AsyncTask<String,Void,String>{

        @Override
        protected String doInBackground(String... params) {
            HttpURLConnection urlConnection =null;
            BufferedReader reader =null;
            String results = null;
            try {
                URL url = new URL(url1 + params[0]
                        + url2);

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setRequestProperty("X-RapidAPI-Key","");

                urlConnection.connect();

                InputStream in = urlConnection.getInputStream();
                if (in == null) {
                    return null;
                }
                reader  = new BufferedReader(new InputStreamReader(in));
                // call getBufferString to get the string from the buffer.
                results = getStringFromBuffer(reader);




            }catch(Exception e){
                Log.e(LOG_TAG,"Error" + e.getMessage());
                return null;
            }finally{
                if (urlConnection != null){
                    urlConnection.disconnect();
                }
                if (reader != null){
                    try{
                        reader.close();
                    }catch (IOException e){
                        Log.e(LOG_TAG,"Error" + e.getMessage());
                        return null;
                    }
                }
            }
            return results;
        }
        protected void onPostExecute(String result){
            if (result != null){
                Log.d(LOG_TAG, result);

                Intent intent = new Intent(SpinnerActivity.this,ResultsActivity.class);
                intent.putExtra("results",result);

                startActivity(intent);

            }
        }
        private String getStringFromBuffer(BufferedReader bufferedReader) throws Exception {
            StringBuffer buffer = new StringBuffer();
            String line;

            while((line = bufferedReader.readLine()) != null){
                buffer.append(line + '\n');

            }
            if (bufferedReader !=null){
                try{
                    bufferedReader.close();
                }catch (IOException e){
                    Log.e("ResultsActivity","Error" + e.getMessage());
                    return null;
                }
            }
            return  resultsHandler.getGameResults(buffer.toString());
        }
    }
}
