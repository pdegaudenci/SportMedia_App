package com.example.sportsmedia;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.example.sportsmedia.auxiliar.DatePickerFragment;
import com.example.sportsmedia.models.Usuario;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.UUID;
import java.util.logging.Logger;

public class RegisterActivity extends AppCompatActivity {
    EditText nombre , apellido, password,email,username, passwdConfirm, et_fechaNac ;
    Button btnRegister;
    FirebaseDatabase firebase;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        binding();
        initFirebaseService();
        listeners();

    }

    /**
     * Obtener elementos graficos de la vista xml a través de su id.
     */
    public void binding()
    {
        nombre= (EditText) findViewById(R.id.edt_name);
        apellido=(EditText) findViewById(R.id.edt_apellido);
        password = (EditText) findViewById(R.id.editTextPassword);
        passwdConfirm = (EditText) findViewById(R.id.edt_confirm);
        username= (EditText) findViewById(R.id.edt_username);
        email = (EditText) findViewById(R.id.edt_email);
        et_fechaNac = (EditText)findViewById(R.id.edt_fecha);
        btnRegister =(Button) findViewById(R.id.btn_crearUsuario);
    }

    /**
     * Inicializacion de los listeners y handlers de eventos para los elementos del formulario
     */
    public void listeners(){
        et_fechaNac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.edt_fecha:
                        showDatePickerDialog();
                        break;
                }
            }
        });
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Usuario usuario = validarCampos();
             if (usuario!=null) {
                 registroUsuario(usuario);
                 Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                 startActivity(intent);
             }
             setearEdt();
            }
        });

    }

    private void showDatePickerDialog() {
        DatePickerFragment newFragment = DatePickerFragment.newInstance(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                // +1 because January is zero
                final String selectedDate = day + " / " + (month+1) + " / " + year;
                et_fechaNac.setText(selectedDate);
            }
        });

        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    /**
     * Limpia los editText, borrando su contenido anterior
     */
    public void setearEdt(){
        nombre.setText("");
        apellido.setText("");
        password.setText("");
        passwdConfirm.setText("");
        username.setText("");
        email.setText("");
        et_fechaNac.setText("");
    }
    /**
     *  Dos validaciones: Contraseñas deben coincidir y ningun campo puede quedar vacio
     *  Si cumple validaciones, instancia un objeto de la clase Usuario
     * @return Usuario : instancia de usuario . Retorna null si hay campos inválidos
     */
    public Usuario validarCampos(){
        Usuario usuario = null;
        String user = username.getText().toString();
        String name = nombre.getText().toString();
        String apell = apellido.getText().toString();
        String passwd = password.getText().toString();
        String pwdConfirm = passwdConfirm.getText().toString();
        String correo = email.getText().toString();
        String nacimiento = et_fechaNac.getText().toString();

        if(user.equals("")||name.equals("")||apell.equals("")||passwd.equals("")|| correo.equals("") || nacimiento.equals(""))
            Toast.makeText(getApplicationContext(), "Debe completar todos los campos", Toast.LENGTH_SHORT).show();
        else if(!validatePassword(passwd,pwdConfirm))
            Toast.makeText(getApplicationContext(), "Las contraseñas deben coincidir", Toast.LENGTH_SHORT).show();
        else{
            usuario = new Usuario();
            usuario.setUid(UUID.randomUUID().toString());
            usuario.setUsername(user);
            usuario.setApellido(apell);
            usuario.setNombre(name);
            usuario.setPassword(passwd);
            usuario.setEmail(correo);
            usuario.setFechanac(nacimiento);

        }

        return usuario;
    }

    public void registroUsuario(Usuario user){

        reference.child("Usuarios").child(user.getUid()).setValue(user);
        System.err.println("AGREGADO USUARIO");
    }

    public boolean validatePassword(String pass, String confirm){
        return pass.equals(confirm);
    }
    public void initFirebaseService(){
        FirebaseApp.initializeApp(this);
        firebase = FirebaseDatabase.getInstance();
        reference= firebase.getReference();
    }

}