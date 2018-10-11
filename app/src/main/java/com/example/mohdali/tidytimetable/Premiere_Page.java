package com.example.mohdali.tidytimetable;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class Premiere_Page extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_premiere__page);
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

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.premiere__page, menu);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_ajouter_taches) {
            FragmentManager fm = getSupportFragmentManager();
            fm.beginTransaction().replace(R.id.main,new Ajouter_Taches_Frag()).commit();
            Toast.makeText(this,"Ajouter des taches test",Toast.LENGTH_LONG).show();
        } else if (id == R.id.nav_taches) {
            FragmentManager fm = getSupportFragmentManager();
            fm.beginTransaction().replace(R.id.main,new Mes_Taches_Frag()).commit();
            Toast.makeText(this,"Mes Taches test",Toast.LENGTH_LONG).show();
        } else if (id == R.id.nav_routines) {
            FragmentManager fm = getSupportFragmentManager();
            fm.beginTransaction().replace(R.id.main,new Routines_Frag()).commit();
            Toast.makeText(this,"Mettre des Routines test",Toast.LENGTH_LONG).show();
        } else if (id == R.id.nav_profil) {
            FragmentManager fm = getSupportFragmentManager();
            fm.beginTransaction().replace(R.id.main,new Profil_Frag()).commit();
            Toast.makeText(this,"Regarder vos Profil test",Toast.LENGTH_LONG).show();
        } else if (id == R.id.nav_partager) {
            FragmentManager fm = getSupportFragmentManager();
            fm.beginTransaction().replace(R.id.main,new Partager_Frag()).commit();
            Toast.makeText(this,"Partager des taches test",Toast.LENGTH_LONG).show();

        } else if (id == R.id.nav_envoyer) {
            FragmentManager fm = getSupportFragmentManager();
            fm.beginTransaction().replace(R.id.main,new Envoyer_Frag()).commit();
            Toast.makeText(this,"Envoyer des taches test",Toast.LENGTH_LONG).show();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
