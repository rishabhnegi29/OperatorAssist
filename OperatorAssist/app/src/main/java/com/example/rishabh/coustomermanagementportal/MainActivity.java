package com.example.rishabh.coustomermanagementportal;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    Spinner loc,stb;
    EditText sc,name,stbid,mobileno;

    ArrayAdapter<String> adapter,adapter2;
    ArrayList al,al2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loc= (Spinner) findViewById(R.id.area);
        stb= (Spinner) findViewById(R.id.stb);
        sc= (EditText) findViewById(R.id.scno);
        name= (EditText) findViewById(R.id.name);
        stbid= (EditText) findViewById(R.id.stbid);
        mobileno=(EditText) findViewById(R.id.mobileno);
        al=new ArrayList();
        al2=new ArrayList();
        loadAreaData();
        loadSetTopBOXData();



    }

    public void loadAreaData()
    {
        class backgroundWorker extends AsyncTask<String,String,String> {
            Context ctx;

            public backgroundWorker(Context ctx) {
                this.ctx = ctx;
            }

            protected String doInBackground(String[] params) {

                String login_url = "http://www.rishabhnegi.esy.es/load.php";

                try {

                    URL url = new URL(login_url);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);


                    InputStream inputStream = httpURLConnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                    String result = "";
                    String line = "";
                    while ((line = bufferedReader.readLine()) != null) {
                        result += line;
                    }
                    bufferedReader.close();
                    inputStream.close();
                    httpURLConnection.disconnect();
                    return result;
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return null;
            }

            protected void onPostExecute(String s) {

                  try{
                   JSONObject jsonObject=new JSONObject(s);
                   JSONArray jsonArray=jsonObject.getJSONArray("server_response");
                    int count=0;
                    al.add("SELECT AREA!!!");

                    while (count<jsonArray.length())
                    {
                        JSONObject jo=jsonArray.getJSONObject(count);

                        al.add(jo.getString("area"));
                        count++;

                    }
                }catch (Exception e){}


                ArrayAdapter<String>adapter = new ArrayAdapter<String>(MainActivity.this,
                        android.R.layout.simple_spinner_item,al);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                loc.setAdapter(adapter);

            }
        }

        new backgroundWorker(this).execute();
    }

    public void loadSetTopBOXData()
    {
        class backgroundWorker extends AsyncTask<String,String,String> {
            Context ctx;

            public backgroundWorker(Context ctx) {
                this.ctx = ctx;
            }

            protected String doInBackground(String[] params) {

                String login_url = "http://www.rishabhnegi.esy.es/stbdata.php";

                try {

                    URL url = new URL(login_url);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);


                    InputStream inputStream = httpURLConnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                    String result = "";
                    String line = "";
                    while ((line = bufferedReader.readLine()) != null) {
                        result += line;
                    }
                    bufferedReader.close();
                    inputStream.close();
                    httpURLConnection.disconnect();
                    return result;
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return null;
            }

            protected void onPostExecute(String s) {


                try{
                    JSONObject jsonObject=new JSONObject(s);
                    JSONArray jsonArray=jsonObject.getJSONArray("server_response");
                    int count=0;
                    al2.add("SELECT STB TYPE!!!");

                    while (count<jsonArray.length())
                    {
                        JSONObject jo=jsonArray.getJSONObject(count);

                        al2.add(jo.getString("stbdata"));
                        count++;

                    }
                }catch (Exception e){}


                ArrayAdapter<String>adapter2 = new ArrayAdapter<String>(MainActivity.this,
                        android.R.layout.simple_spinner_item,al2);
                adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                stb.setAdapter(adapter2);

            }
        }

        new backgroundWorker(this).execute();
    }
    public void submit(View v)throws Exception
    {
        String resp="";

        if(stbid.getText().equals("")||sc.getText().equals("") || name.getText().equals(""))
        {
            Toast.makeText(this, "Feilds Cannot be left blank!!", Toast.LENGTH_SHORT).show();
        }
        else if(loc.getSelectedItem().toString()=="SELECT AREA!!!")
        {
            Toast.makeText(this, "select area first", Toast.LENGTH_SHORT).show();
        }
        else if (stb.getSelectedItem().toString()=="SELECT STB TYPE!!!")
        {
            Toast.makeText(this, "select STB TYPE", Toast.LENGTH_SHORT).show();
        }
        else {
            resp = createJsonResopse();
            InsertBackgroundWorker obj=new InsertBackgroundWorker(this);
            obj.execute(resp);
        }
    }
    public String createJsonResopse() throws Exception {

        JSONArray jsonArray = new JSONArray();
        int i = 0;




            JSONObject obj = new JSONObject();
            obj.put("stbid",stbid.getText());
            obj.put("scid",sc.getText() );
            obj.put("name",name.getText() );
            obj.put("location",loc.getSelectedItem().toString());
            obj.put("stbType",stb.getSelectedItem().toString());
            obj.put("mobileno",mobileno.getText() );

            jsonArray.put(obj);
        return jsonArray.toString();
    }
}
