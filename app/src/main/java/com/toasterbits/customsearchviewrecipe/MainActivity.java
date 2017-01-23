package com.toasterbits.customsearchviewrecipe;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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

        // Cache Users
        User.deleteAll(User.class);
        ArrayList<User> usersList = new ArrayList<>();
        usersList.add(new User("Chris", "Jeon", "chris0374@gmail.com"));
        usersList.add(new User("Avalon", "Bauman", "avalon.bauman@gmail.com"));
        usersList.add(new User("John", "Smith", "john.smith@gmail.com"));
        usersList.add(new User("Donald", "Trump", "donald.trump@gmail.com"));
        usersList.add(new User("Tim", "Castro", "tim.castro@gmail.com"));
        User.saveInTx(usersList);
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
        } else if (id == R.id.search_item) {
            Intent searchResultsIntent = new Intent(this, SearchResultsActivity.class);
            startActivity(searchResultsIntent);
        }

        return super.onOptionsItemSelected(item);
    }
}
