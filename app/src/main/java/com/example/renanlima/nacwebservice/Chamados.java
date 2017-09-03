package com.example.renanlima.nacwebservice;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Chamados extends AppCompatActivity {


    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chamados);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mListView = (ListView)findViewById(R.id.lv_itens);

        CarregarChamados cc = new CarregarChamados();

        cc.execute();

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

            Intent intent = new Intent(this,MainActivity.class);
            startActivity(intent);
            finish();

            return true;
        }
        if (id == R.id.action_chamados) {


            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private class CarregarChamados extends AsyncTask<Void,Void,String>{


        private ProgressDialog mProgressDialog;

        @Override
        protected void onPreExecute() {
            mProgressDialog = ProgressDialog.show(Chamados.this,"Aguarde","Buscando dados no Servidor");
        }

        @Override
        protected String doInBackground(Void... params) {

            try {
                URL mUrl = new URL("https://assistenciaapi.herokuapp.com/rest/chamado/");
                HttpURLConnection connection = (HttpURLConnection) mUrl.openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("Accept","application/json");

                if(connection.getResponseCode()==200){
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                    String linha;

                    StringBuilder builder = new StringBuilder();
                    while((linha=reader.readLine())!=null){
                        builder.append(linha);
                    }

                    connection.disconnect();
                    return builder.toString();
                }


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


            return null;
        }


        @Override
        protected void onPostExecute(String s) {

            mProgressDialog.dismiss();

            if(s!=null){
                try{
                    /*JSONObject jsonObject = new JSONObject();
                    JSONArray jsonArray = jsonObject.getJSONArray();*/

                    JSONArray jsonArray = new JSONArray(s);

                    List<String> lista = new ArrayList<>();

                    for(int i= 0;i<jsonArray.length();i++){

                        JSONObject item = (JSONObject)jsonArray.get(i);
                        String codigoFunc = item.getString("codigoFuncionario");
                        String descricao = item.getString("descricao");
                        String finalizado = item.getString("finalizado");

                        lista.add(codigoFunc+", "+descricao+", "+finalizado);
                    }


                    ListAdapter adapter = new ArrayAdapter<>(Chamados.this,android.R.layout.simple_list_item_1,lista);
                    mListView.setAdapter(adapter);

                }catch (JSONException e) {
                    e.printStackTrace();
                }


            }


            super.onPostExecute(s);
        }
    }

}
