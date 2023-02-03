package com.example.sportsmedia;

import static com.google.android.material.internal.ContextUtils.getActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.sportsmedia.fragments.ActividadesFragment;
import com.example.sportsmedia.fragments.CrearActividadFragment;
import com.example.sportsmedia.fragments.HomeFragment;
import com.example.sportsmedia.fragments.RedFragment;
import com.example.sportsmedia.models.Usuario;
import com.google.android.material.navigation.NavigationView;

public class HomeActivity extends AppCompatActivity {
    private static Usuario userglobal;
    private TextView user;
    private NavigationView navigationView;
    public static Usuario getUserglobal() {
        return userglobal;
    }
    public Context context;
    public static void setUserglobal(Usuario userglobal) {
        HomeActivity.userglobal = userglobal;
    }
    DrawerLayout drawerLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        String text = getString(R.string.welcome, getUserglobal().getNombre());


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null)
        {
            actionBar.setTitle(text);
        }
        toggle.syncState();
        navigationView = (NavigationView)findViewById(R.id.navigation_view);
        cargarListener();

    }

    private void cargarListener(){

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment=null;
                boolean fragmentTransaction = false;
                switch (item.getItemId()){
                    case R.id.nav_activities:
                        fragment = new ActividadesFragment();
                        ActividadesFragment actividadesFragment= (ActividadesFragment) fragment;

                        fragmentTransaction = true;
                        break;
                    case R.id.nav_red:
                        fragment = new RedFragment();
                        fragmentTransaction = true;
                        break;
                    case R.id.nav_home:
                        fragment = new HomeFragment();
                        fragmentTransaction = true;
                        break;
                }

                if (fragmentTransaction){
                    changeFragment (fragment , item );
                    drawerLayout.closeDrawers();
                }
                return true;
            }
        });
    }
    /*
   Metodo que visualiza / oculta el icono de apertura del menu lateral ,
   en funcion de que el menu este desplegado o no
     */
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    /**
     * Metodo para cambiar los fragment a visualizar , dentro de la actividad principal
     * Recibe por parámetro, el fragment a cargar en la activity_home , en el elemento con id fragment
     * @param fragment
     * @param item
     */
    private void changeFragment( Fragment fragment, MenuItem item){
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment , fragment)
                .commit();
        item.setChecked(true);
        // Seteo titulo superior del fragment cargado
        getSupportActionBar().setTitle(item.getTitle());
    }



}