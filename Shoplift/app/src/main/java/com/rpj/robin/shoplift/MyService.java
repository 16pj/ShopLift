package com.rpj.robin.shoplift;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;


public class MyService extends Service {



    // constant
    public static final long NOTIFY_INTERVAL = 20 * 1000; // 10 seconds

    // run on another Thread to avoid crash
    private Handler mHandler = new Handler();
    // timer handling
    private Timer mTimer = null;

    int i;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {


        i = 0;
        // cancel if already existed
        if (mTimer != null) {
            mTimer.cancel();
        } else {
            // recreate new
            mTimer = new Timer();
        }
        // schedule task
        mTimer.scheduleAtFixedRate(new TimeDisplayTimerTask(), 0, NOTIFY_INTERVAL);
    }

    class TimeDisplayTimerTask extends TimerTask {

        @Override
        public void run() {
            // run on another thread
            mHandler.post(new Runnable() {

                @Override
                public void run() {
                    // display toast
                    Backgrounder backgrounder1 = new Backgrounder(getApplicationContext());
                    backgrounder1.execute("show");


                }
            });
        }

        public class Backgrounder extends AsyncTask<String, String, String> {
            String res = "";

            Context context;
            Backgrounder( Context cx){
                context = cx;
            }

            @Override
            protected String doInBackground(String... params) {

                String url_show = "http://robindustbin.comli.com/getcount.php";

                    BufferedReader bufferedReader = null;
                    try {
                        URL url = new URL(url_show);
                        HttpURLConnection con = (HttpURLConnection) url.openConnection();
                        StringBuilder sb = new StringBuilder();

                        bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                        String me;
                        while((me = bufferedReader.readLine())!= null){
                            publishProgress(me);
                            res+=me;
                        }

                        return res;

                    }catch(Exception e){

                    }

                return res;
            }


            @Override
            protected void onPreExecute() {
            }

            @Override
            protected void onPostExecute(String result) {


                    //test = Integer.parseInt(result.trim());
                    //String i =Integer.toString(test);

                    int y = Integer.parseInt(result.trim());
                    SharedPreferences sharedpref = getSharedPreferences("count", Context.MODE_PRIVATE);
                    String s = sharedpref.getString("count", "");
                    int x = Integer.parseInt(s.trim());

                    int z = x - y;

                    if (z > 0) {

                        SharedPreferences sharedpreff = getSharedPreferences("check", Context.MODE_PRIVATE);
                        String checker = sharedpreff.getString("check", "");
                        if(checker.equals("yes")) {

                            NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext());
                            builder.setSmallIcon(R.mipmap.ic_launcher);
                            builder.setContentTitle("Shopper");
                            builder.setContentText("Item(s) Removed from Shopping List");
                            builder.setTicker("List Shrunk");
                            builder.setAutoCancel(true);

                            Intent intent = new Intent(getApplication(), MainActivity.class);
                            TaskStackBuilder stackBuilder = TaskStackBuilder.create(getApplicationContext());
                            stackBuilder.addParentStack(MainActivity.class);
                            stackBuilder.addNextIntent(intent);

                            PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
                            builder.setContentIntent(pendingIntent);

                            NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                            nm.notify(12133, builder.build());
                        }



                      //  Toast.makeText(getApplicationContext(), "New Removed", Toast.LENGTH_SHORT).show();


                    } else if (z < 0) {

                        SharedPreferences sharedpreff = getSharedPreferences("check", Context.MODE_PRIVATE);
                        String checker = sharedpreff.getString("check", "");
                        if(checker.equals("yes")) {

                            NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext());
                            builder.setSmallIcon(R.mipmap.ic_launcher);
                            builder.setContentTitle("Shopper");
                            builder.setContentText("Item(s) Added to Shopping list");
                            builder.setTicker("List Grew");
                            builder.setAutoCancel(true);

                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            TaskStackBuilder stackBuilder = TaskStackBuilder.create(getApplicationContext());
                            stackBuilder.addParentStack(MainActivity.class);
                            stackBuilder.addNextIntent(intent);

                            PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
                            builder.setContentIntent(pendingIntent);

                            NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                            nm.notify(12133, builder.build());


                            // Toast.makeText(getApplicationContext(), "New Added", Toast.LENGTH_SHORT).show();
                        }

                    }

                    String resu = Integer.toString(y);

                    SharedPreferences.Editor editor = sharedpref.edit();
                    editor.putString("count", resu);
                    editor.apply();

                SharedPreferences sharedpreff = getSharedPreferences("check", Context.MODE_PRIVATE);
                SharedPreferences.Editor editorr = sharedpreff.edit();
                editorr.putString("check", "yes");
                editorr.apply();

            }

            @Override
            protected void onProgressUpdate(String... values) {
               // test = Integer.parseInt(values[0]);
               // int x =10;
                //x=x-test;
              //  String i =Integer.toString(test);

               // Toast.makeText(getApplicationContext(),i,Toast.LENGTH_SHORT).show();
              //  Toast.makeText(getApplicationContext(),"Hello",Toast.LENGTH_SHORT).show();
            }
        }


    }
}