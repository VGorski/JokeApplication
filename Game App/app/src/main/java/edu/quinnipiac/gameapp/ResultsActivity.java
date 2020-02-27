package edu.quinnipiac.gameapp;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

public class ResultsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        Intent intent = getIntent();

        String results = (String) getIntent().getExtras().get("results");

        EditText textView = (EditText) findViewById(R.id.EditText);

        textView.setText(results);
    }
}