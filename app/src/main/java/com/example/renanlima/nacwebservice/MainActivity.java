package com.example.renanlima.nacwebservice;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONStringer;

import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private EditText mCf;
    private Spinner mSpinner;
    private CheckBox mCheckbox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mCf = (EditText)findViewById(R.id.et_cf);
        mSpinner = (Spinner)findViewById(R.id.sp_items);
        mCheckbox = (CheckBox)findViewById(R.id.cb_torf);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setVisibility(View.INVISIBLE);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();


        if (id == R.id.action_cadastrar) {
            return true;
        }
        if (id == R.id.action_chamados) {

            Intent intent = new Intent(this,Chamados.class);
            startActivity(intent);
            finish();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void cadastrar(View view) {

        CadastrarFuncionario cadastrarFuncionario = new CadastrarFuncionario();
        cadastrarFuncionario.execute(mCf.getText().toString(),mSpinner.getSelectedItem().toString(),mCheckbox.isChecked()?"true":"false");
    }


    public class CadastrarFuncionario extends AsyncTask<String, Void, Integer>{



        @Override
        protected Integer doInBackground(String... params) {

            try{
                URL url = new URL("https://assistenciaapi.herokuapp.com/rest/chamado/");
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("Content-Type","application/json");

                JSONStringer jsonStringer = new JSONStringer();
                jsonStringer.object();
                jsonStringer.key("codigoFuncionario").value(params[0]);
                jsonStringer.key("finalizado").value(params[2]);
                jsonStringer.key("descricao").value(params[1]);
                jsonStringer.endObject();

                //enviar para o servidor
                OutputStreamWriter stream = new OutputStreamWriter(urlConnection.getOutputStream());
                stream.write(jsonStringer.toString());
                stream.close();

                return urlConnection.getResponseCode();

            }catch (Exception e){
                e.printStackTrace();
            }

            return null;
        }


        @Override
        protected void onPostExecute(Integer integer) {

            if(integer==201){
                Toast.makeText(getApplicationContext(),"Dados gravados com sucesso",Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(getApplicationContext(),"Erro ao gravar dados",Toast.LENGTH_SHORT).show();
            }

            super.onPostExecute(integer);
        }
    }
}