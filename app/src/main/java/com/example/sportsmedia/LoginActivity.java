package com.example.sportsmedia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.sportsmedia.models.Usuario;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {
    private EditText edt_user,edt_password;
    private Button btn_login, btn_register;
    private FirebaseDatabase firebase;
    private DatabaseReference reference;
    private static ArrayList<Usuario> users = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        edt_user = (EditText) findViewById(R.id.username);
        edt_password = (EditText) findViewById(R.id.password);

        btn_login = (Button) findViewById(R.id.btn_login);
        btn_register=(Button) findViewById(R.id.btn_registro);
        initFirebase();
        listeners();


    }

    private void initFirebase() {
        FirebaseApp.initializeApp(this);
        //Instancia a la base de datos
        firebase = FirebaseDatabase.getInstance();
        //Referencia al nodo que queremos acceder
        reference= firebase.getReference("Usuarios");
    }

    private void listeners(){
    btn_register.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
            startActivity(intent);
        }
    });
    btn_login.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            autenticarUsuario();
        }
    });
}


private void setUsers(ArrayList<Usuario> lista){
        users = lista;
}
private boolean autenticarUsuario() {
    HashMap dataUser = new HashMap();
    dataUser.put("username",edt_user.getText().toString());
    dataUser.put("password",edt_password.getText().toString());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                LoginActivity.getUsers().clear();
                for (DataSnapshot element: snapshot.getChildren()){
                    Usuario userAux = element.getValue(Usuario.class);
                    LoginActivity.users.add(userAux);
                }
                for (Usuario user_aux:LoginActivity.users){
                    if ((user_aux.getUsername().equals(dataUser.get("username").toString()) ||
                            user_aux.getEmail().equals(dataUser.get("username").toString())&&
                                    user_aux.getPassword().equals(dataUser.get("password"))))
                    {
                        HomeActivity.setUserglobal(user_aux);
                        Intent i = new Intent(getApplicationContext(), HomeActivity.class);
                        startActivity(i);
                        finish();
                    }
                    else{
                        Toast.makeText(getApplicationContext(),"Credenciales Incorrecta. Ingréselas nuevamente", Toast.LENGTH_SHORT).show();
                        edt_user.setText("");
                        edt_password.setText("");
                    }

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return false;
    }

    public static ArrayList<Usuario> getUsers() {
        return users;
    }

    public Usuario autenticate(ArrayList<Usuario> users_list, HashMap<String,String> dataUser){
        Usuario user_temp=null;

        for (Usuario user_aux:users_list){

            if ((user_aux.getUsername().equals(dataUser.get("username").toString()) ||
                    user_aux.getEmail().equals(dataUser.get("username").toString())&&
                            user_aux.getPassword().equals(dataUser.get("password"))))
            {

                user_temp=user_aux;
            }
        }
        return user_temp;
    }

    public void processFinish(String s) {
        Usuario user = checkUser(s);
        if(user == null){
            Toast.makeText(this,"Verifique usuario y contraseña", Toast.LENGTH_LONG).show();
        }
        else{

        }
    }
    private Usuario checkUser(String s){
        Usuario user = null;
        try {
            JSONObject jsonObject = new JSONObject(s);
            if(jsonObject.length() >= 0){
                String username = jsonObject.getString("username");
                String password = jsonObject.getString("password");
                String nombre = jsonObject.getString("nombre");
                String apellido = jsonObject.getString("apellido");
                String fechanac = jsonObject.getString("fechanac");
                int id = jsonObject.getInt("id");
                //user = new Usuario(id,nombre,apellido,username,password,fechanac);
            }
        } catch (JSONException e) {

            return user;
        }

        return user;
    }
}