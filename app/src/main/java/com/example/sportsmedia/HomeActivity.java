package com.example.sportsmedia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;


import android.content.Context;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.sportsmedia.dto.Actividad;
import com.example.sportsmedia.fragments.ActividadesFragment;
import com.example.sportsmedia.fragments.HomeFragment;
import com.example.sportsmedia.fragments.RedFragment;
import com.example.sportsmedia.dto.Usuario;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {
    private static Usuario userglobal;
    private TextView user;
    private NavigationView navigationView;

    private static ArrayList<Actividad> misActividades=new ArrayList<>();

    private static ArrayList<Actividad> actividadesSociales=new ArrayList<>();
    public Context context;
    DrawerLayout drawerLayout;

    public static Usuario getUserglobal() {
        return userglobal;
    }
    public static void setUserglobal(Usuario userglobal) {
        HomeActivity.userglobal = userglobal;
    }
    public static ArrayList<Actividad> getMyActividades() {
        return misActividades;
    }

    public static ArrayList<Actividad> getActividades() {
        return actividadesSociales;
    }

    public static void setMisActividades(ArrayList<Actividad> misActividades) {
        HomeActivity.misActividades = misActividades;
    }

    public static void setActividadesSociales(ArrayList<Actividad> actividadesSociales) {
        HomeActivity.actividadesSociales = actividadesSociales;
    }

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
        cargarMenu();

    }

    /**
     * Carga los items de menu asociado a un listener que la accion de
     * cargar un determinado fragment , en funcion del item seleccionado
     */
    private void cargarMenu(){

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