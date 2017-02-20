package com.rpj.robin.shoplift;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private EditText editTxt;
    private Button btn;
    private ListView list;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> arrayList;
    String countex;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTxt = (EditText) findViewById(R.id.editText);
        btn = (Button) findViewById(R.id.button);
        list = (ListView) findViewById(R.id.listView);
        arrayList = new ArrayList<String>();

        // Adapter: You need three parameters 'the context, id of the layout (it will be where the data is shown),
        // and the array that contains the data
        adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, arrayList);

        // Here, you set the data in your ListView
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String som = adapter.getItem(position);
                editTxt.setText(som);
            }
        });

        SharedPreferences sharedpref = getSharedPreferences("count", Context.MODE_PRIVATE);
        String yes = sharedpref.getString("count","");


        if(yes.isEmpty()) {
            SharedPreferences.Editor editor = sharedpref.edit();
            editor.putString("count", "0");
            editor.apply();
        }


        Backgrounder backgrounder = new Backgrounder(this);
        backgrounder.execute("show");

        startService(new Intent(this, MyService.class));

       /* if (adapter.isEmpty()) {
            Toast.makeText(this, "Empty list or no NET :( ",Toast.LENGTH_LONG).show();
            sqealee.printValues();
        }
        else {
        sqealee.cleantable();
    }
*/
    }


    public void clickDelete(View view) {

        String num = editTxt.getText().toString();
        if(!editTxt.getText().toString().equals("")) {
            adapter.remove(num);
            Backgrounder backgrounder = new Backgrounder(this);
            backgrounder.execute("delete", num);
            Toast.makeText(MainActivity.this, "Deleted " + num, Toast.LENGTH_SHORT).show();
            editTxt.setText("");

            SharedPreferences sharedpreff = getSharedPreferences("check", Context.MODE_PRIVATE);
            SharedPreferences.Editor editorr = sharedpreff.edit();
            editorr.putString("check", "no");
            editorr.apply();


        }
    }

    public void clickAdd(View view) {

        String thing = editTxt.getText().toString();

        if (!thing.equals("")) {
            arrayList.add(thing);
            adapter.notifyDataSetChanged();
            Backgrounder backgrounder = new Backgrounder(this);
            backgrounder.execute("add", thing);

            editTxt.setText("");
            SharedPreferences sharedpreff = getSharedPreferences("check", Context.MODE_PRIVATE);
            SharedPreferences.Editor editorr = sharedpreff.edit();
            editorr.putString("check", "no");
            editorr.apply();



        } else {
            arrayList.clear();
            adapter.notifyDataSetChanged();
            Backgrounder backgrounder1 = new Backgrounder(this);
            backgrounder1.execute("show");
        }
    }


    public void clickRefresh(View view) {

        arrayList.clear();
        adapter.notifyDataSetChanged();
        Backgrounder backgrounder = new Backgrounder(this);
        backgrounder.execute("show");
    }


    public class Backgrounder extends AsyncTask<String, String, String> {

        Context context;

        Backgrounder(Context cx) {
            context = cx;

        }

        @Override
        protected String doInBackground(String... params) {

            String url_add = "http://robindustbin.comli.com/insert_me.php";
            String url_del = "http://robindustbin.comli.com/remove.php";
            String url_show = "http://robindustbin.comli.com/selall.php";


            String type = params[0];

            if (type.equals("add")) {

                try {

                    String username = params[1];
                    String password = "0";
                    URL url = new URL(url_add);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);
                    OutputStream outputStream = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                    String post_data = URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(username, "UTF-8") + "&"
                            + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8");
                    bufferedWriter.write(post_data);
                    bufferedWriter.flush();
                    bufferedWriter.close();
                    outputStream.close();


                    InputStream inputStream = httpURLConnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                    String result = "";
                    String line;

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


            } else if (type.equals("delete")) {

                try {

                    String username = params[1];
                    String password = "0";
                    URL url = new URL(url_del);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);
                    OutputStream outputStream = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                    String post_data = URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(username, "UTF-8") + "&"
                            + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8");
                    bufferedWriter.write(post_data);
                    bufferedWriter.flush();
                    bufferedWriter.close();
                    outputStream.close();


                    InputStream inputStream = httpURLConnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                    String result = "";
                    String line;

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
            } else if (type.equals("show")) {
                BufferedReader bufferedReader = null;
                try {
                    URL url = new URL(url_show);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    StringBuilder sb = new StringBuilder();

                    bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                    String me;
                    while ((me = bufferedReader.readLine()) != null) {
                        publishProgress(me);
                    }

                    return me;

                } catch (Exception e) {

                }

            }
            return null;
        }


        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onPostExecute(String result) {
         /*

          int check = adapter.getCount();

            SharedPreferences sharedpref = getSharedPreferences("count", Context.MODE_PRIVATE);
            String temp = sharedpref.getString("count", "");
           int tempint = Integer.parseInt(temp);
            int sub = check-tempint;

            if(sub > 0){
                //NEW ITEMS ADDED

                Toast.makeText(MainActivity.this, "ITEMS ADDED",Toast.LENGTH_SHORT ).show();
            }
            else if (sub < 0){
                //ITEMS CLEARED
                Toast.makeText(MainActivity.this, "ITEMS REMOVED",Toast.LENGTH_SHORT ).show();
            }

            String subs = Integer.toString(check);

                SharedPreferences.Editor editor = sharedpref.edit();
                editor.putString("count", subs);
                editor.apply();
 */


        }

        @Override
        protected void onProgressUpdate(String... values) {

            arrayList.add(values[0]);
            adapter.notifyDataSetChanged();

        }
    }
 /*@Override
    protected void onDestroy() {
        stopService(new Intent(this, MyService.class));
        super.onDestroy();
    }
    */
}