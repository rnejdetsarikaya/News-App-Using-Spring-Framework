package com.example.necoo.yazlab2_2newsapplication;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.RingtoneManager;
import android.net.Uri;
import android.net.wifi.WifiConfiguration;
import android.os.Bundle;
import android.os.StrictMode;
import android.os.SystemClock;
import android.preference.ListPreference;
import android.provider.Settings;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import android.provider.Settings.Secure;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ScrollingActivity extends AppCompatActivity {

    Intent mServiceIntent;
    private NotificationService mSensorService;
    Context ctx;
    public Context getCtx() {
        return ctx;
    }
    int index;
    static String apiPath = null;
    boolean isViewed = false;
    TextView text = null;
    TextView likeText = null;
    TextView dislikeText = null;
    TextView viewText = null;
    TextView viewText2 = null;
    String jsonStr = null;
    List<News> newsList;
    Toolbar toolbar;
    CollapsingToolbarLayout ctlayout;
    FloatingActionButton refreshFab;
    FloatingActionButton likeFab;
    FloatingActionButton dislikeFab;
    FloatingActionButton nextFab;
    FloatingActionButton backFab;
    FloatingActionButton viewFab;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        super.onCreate(savedInstanceState);
        SettingsActivity.flag = true;
        //apiPath = getResources().getString(R.string.pref_default_display_name);

        ctx = this;
        setContentView(R.layout.activity_scrolling);

        FileOps fileOps = new FileOps();
        apiPath=fileOps.read(this,"api.txt");

        String a = getNotificationState();
        if(!a.equals("exit"))
            fileOps.write(this,"notificationState.txt",a);


        mSensorService = new NotificationService(getCtx());
        mServiceIntent = new Intent(getCtx(), mSensorService.getClass());
        if (!isMyServiceRunning(mSensorService.getClass())) {
            startService(mServiceIntent);
        }

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ctlayout = findViewById(R.id.toolbar_layout);
        refreshFab = (FloatingActionButton) findViewById(R.id.refresh);
        viewFab = (FloatingActionButton) findViewById(R.id.view);
        likeFab = (FloatingActionButton) findViewById(R.id.like);
        dislikeFab = (FloatingActionButton) findViewById(R.id.dislike);
        nextFab = (FloatingActionButton) findViewById(R.id.next);
        backFab = (FloatingActionButton) findViewById(R.id.back);
        likeText = (TextView) findViewById(R.id.likeText);
        dislikeText = (TextView) findViewById(R.id.dislikeText);
        viewText = (TextView) findViewById(R.id.viewText);
        viewText2 = (TextView) findViewById(R.id.text2);
        text = (TextView) findViewById(R.id.textView1);

        System.out.println("Android id = " + Secure.getString(ScrollingActivity.this.getContentResolver(), Secure.ANDROID_ID));
        newsList = getNews(jsonStr = fetchData());
        System.out.println(newsList.size());
        showNewsPreview(newsList.size() - 1);
        getInfoByID(newsList.get(newsList.size() - 1).getNewsId());
        buttonStateControl();
        nextFab.setBackgroundTintList(ColorStateList.valueOf(Color.CYAN));
        refreshFab.setBackgroundTintList(ColorStateList.valueOf(Color.CYAN));
        backFab.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));
        backFab.setEnabled(false);
        //next.setBackgroundColor(Color.CYAN);
        // back.setBackgroundColor(Color.CYAN);

        //  likeFab.setImageResource(R.drawable.like);
        //   dislikeFab.setImageResource(R.drawable.dislike);
        refreshFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Haberler Yenilendi.", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                String filter;
                List<News> temp = new ArrayList<>();
                newsList = getNews(jsonStr = fetchData());

                try {
                    SettingsActivity.write(getApplicationContext());
                    filter = new FileOps().read(ScrollingActivity.this, "filter.txt");
                    if (filter.equals("Hepsi")){
                        showNewsPreview(newsList.size() - 1);
                        getInfoByID(newsList.get(index).getNewsId());
                        buttonStateControl();
                    }
                    else {
                        for (News element : newsList) { // filtrele
                            if (element.getType().equals(filter)) {
                                temp.add(element);
                            }
                        }
                        if(!newsList.get(0).getNewsTitle().equals("Failed to connect")){
                            newsList.clear();
                            newsList.addAll(temp);
                        }
                        showNewsPreview(newsList.size() - 1);
                        getInfoByID(newsList.get(index).getNewsId());
                        buttonStateControl();
                    }
                } catch (Exception ex) {

                    showNewsPreview(newsList.size() - 1);
                    getInfoByID(newsList.get(index).getNewsId());
                    buttonStateControl();

                }


                backFab.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));
                backFab.setEnabled(false);
                // nextFab.setBackgroundTintList(ColorStateList.valueOf(Color.CYAN));
                // nextFab.setEnabled(true);
            }
        });

        likeFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v, "Bu haberi beğendinn :).", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                int like = 0;
                int count = Integer.parseInt(dislikeText.getText().toString());
                if (!dislikeFab.isEnabled()) {
                    updateLikeDislikeView("dislike", newsList.get(index).getNewsId(), 1);
                    updateLikeDislikeView("like", newsList.get(index).getNewsId(), 0);
                    dislikeFab.setEnabled(true);
                    dislikeFab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));
                    likeFab.setEnabled(false);
                    likeFab.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));
                    dislikeText.setText(newsList.get(index).getDislikeCount() + "");
                } else {
                    updateLikeDislikeView("like", newsList.get(index).getNewsId(), 0);
                    likeFab.setEnabled(false);
                    likeFab.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));
                    dislikeFab.setEnabled(true);
                    dislikeFab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));
                }
                like = newsList.get(index).getLikeCount();
                likeText.setText(like + "");
                newsList.get(index).setLikeCount(like);
                setStates();
            }
        });

        dislikeFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v, "Bu haberi beğenmedin :(.", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                int count = Integer.parseInt(likeText.getText().toString());
                if (!likeFab.isEnabled()) {
                    updateLikeDislikeView("dislike", newsList.get(index).getNewsId(), 0);
                    updateLikeDislikeView("like", newsList.get(index).getNewsId(), 1);
                    dislikeFab.setEnabled(false);
                    dislikeFab.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));
                    likeFab.setEnabled(true);
                    likeFab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));
                    likeText.setText(newsList.get(index).getLikeCount() + "");
                } else {
                    updateLikeDislikeView("dislike", newsList.get(index).getNewsId(), 0);
                    dislikeFab.setEnabled(false);
                    dislikeFab.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));
                    likeFab.setEnabled(true);
                    likeFab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));
                }

                int dislike = newsList.get(index).getDislikeCount();
                dislikeText.setText(dislike + "");
                newsList.get(index).setDislikeCount(dislike);
                setStates();

            }
        });

        viewFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // System.out.println(SettingsActivity.read());


                DateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
                //sdf.applyPattern("dd-MM-yyyy HH:mm:ss");
                Date date = null;
                long longDate = Long.parseLong(newsList.get(index).getDate());
                try {
                    //date = sdf.parse(longDate);
                    date = new Date(longDate);
                }
                catch (Exception ex){
                    ex.printStackTrace();
                }
                String d = sdf.format(date);

                //formatlı yazmak için html formatlaması kullandık.
                String strView = "<h2>" + newsList.get(index).getNewsTitle() + "</h2>" +" <br><br>"+
                        newsList.get(index).getText() +"<br><br><br>"+
                        "<b>" + d +"</b>"+" <br><br><br>";

                //text.setText("\n" + newsList.get(index).getText() + "\n\n\n"+d+"\n \n \n");
                text.setText(Html.fromHtml(strView));
                viewText2.setText("-");
                int view = newsList.get(index).getViewCount();
                if (!isViewed) {
                    view += 1;
                    updateLikeDislikeView("view", newsList.get(index).getNewsId(), 0);
                    isViewed = true;
                }

                viewText.setText(view + "");
                newsList.get(index).setViewCount(view);
                setStates();

            }
        });

        nextFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNewsPreview(index - 1);
                viewText2.setText("+");
                getInfoByID(newsList.get(index).getNewsId());
                buttonStateControl();


                if (index == 0) {
                    nextFab.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));
                    nextFab.setEnabled(false);
                }

                if (index != newsList.size() - 1) {
                    backFab.setBackgroundTintList(ColorStateList.valueOf(Color.CYAN));
                    backFab.setEnabled(true);
                }

            }
        });
        backFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNewsPreview(index + 1);
                viewText2.setText("+");
                getInfoByID(newsList.get(index).getNewsId());

                buttonStateControl();

                if (index != 0) {
                    nextFab.setBackgroundTintList(ColorStateList.valueOf(Color.CYAN));
                    nextFab.setEnabled(true);
                }
                if (index == newsList.size() - 1) {
                    backFab.setEnabled(false);
                    backFab.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));
                }

            }
        });


    }

    public void showNewsPreview(int index) {

        if (newsList.size() <= 1) {
            nextFab.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));
            nextFab.setEnabled(false);
        } else {
            nextFab.setBackgroundTintList(ColorStateList.valueOf(Color.CYAN));
            nextFab.setEnabled(true);
        }

        this.index = index;
        ctlayout.setTitle(newsList.get(index).getNewsTitle());
        Drawable d = new BitmapDrawable(getResources(), getBitmapFromUrl(newsList.get(index).getImage()));
        ctlayout.setBackground(d);
        likeText.setText(newsList.get(index).getLikeCount() + "");
        dislikeText.setText(newsList.get(index).getDislikeCount() + "");
        viewText.setText(newsList.get(index).getViewCount() + "");
        String t = newsList.get(index).getText();
        text.setText("\n" + t.substring(0, t.length() / 3) + "... \n \n \n");
        //text.setText( text.getTextSize()+"");
        text.setTextSize(25);
    }

    public List<News> getNews(String jsonStr) {
        System.out.println(jsonStr);
        List<News> list = new ArrayList<News>();
        if (jsonStr == null) {
            News news = new News();
            news.setNewsTitle("Failed to connect");
            news.setNewsId(-1);
           // news.setImage("https://kinsta.com/wp-content/uploads/2017/06/error-establishing-a-database-connection.png");
            news.setText("Failed to connect to /" + apiPath);
            news.setType(" ");
            news.setDate(" ");
            news.setLikeCount(-1);
            news.setDislikeCount(-1);
            news.setViewCount(-1);
            list.add(news);
            likeFab.setEnabled(false);
            dislikeFab.setEnabled(false);
            nextFab.setEnabled(false);
            backFab.setEnabled(false);
            viewFab.setEnabled(false);
            nextFab.setEnabled(false);
            return list;
        }

        try {

            List<JSONObject> jsonList = getJsonObjects(jsonStr);

            for (JSONObject json : jsonList) {
                News news = new News();
                news.setNewsTitle(json.getString("newsTitle"));
                news.setNewsId(json.getInt("newsId"));
                news.setImage(json.getString("image"));
                news.setText(json.getString("text"));
                news.setType(json.getString("type"));
                news.setDate(json.getString("date"));
                news.setLikeCount(json.getInt("likeCount"));
                news.setDislikeCount(json.getInt("dislikeCount"));
                news.setViewCount(json.getInt("viewCount"));
                list.add(news);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        likeFab.setEnabled(true);
        dislikeFab.setEnabled(true);
        //nextFab.setEnabled(true);
        backFab.setEnabled(true);
        viewFab.setEnabled(true);
        return list;
    }

    public List<JSONObject> getJsonObjects(String jsonArray) throws Exception {
        jsonArray = jsonArray.substring(1, jsonArray.length() - 1);
        List<JSONObject> list = new ArrayList<>();
        String json;
        for (int i = 0; i < jsonArray.length(); i++) {
            int temp = i;
            i = jsonArray.indexOf("}", i);
            json = jsonArray.substring(temp, i + 1);
            list.add(new JSONObject(json));
            i += 1;
        }
        return list;
    }

    public Bitmap getBitmapFromUrl(String url) {
        try {
            URL u = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) u.openConnection();
            conn.setDoInput(true);
            conn.connect();
            InputStream input = conn.getInputStream();
            Bitmap image = BitmapFactory.decodeStream(input);
            return image;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_scrolling, menu);
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
            Intent intent = new Intent(ScrollingActivity.this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    protected String fetchData(Void... urls) {
        // String email = emailText.getText().toString();
        // Do some validation here
        String path = "http://" + apiPath + "/news";
        try {
            URL url = new URL(path);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setConnectTimeout(10000);
            try {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                StringBuilder stringBuilder = new StringBuilder();
                String line;

                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line).append("\n");
                }
                bufferedReader.close();
                return stringBuilder.toString();
            } finally {
                urlConnection.disconnect();
            }
        } catch (Exception e) {
            System.out.println("exception");
            return null;
        }
    }

    public void setStates(){
        int viewState = isViewed ? 1:0;
        int likeState = 0;

        String deviceID = Secure.getString(this.getContentResolver(),Secure.ANDROID_ID);
        if(dislikeFab.isEnabled() && likeFab.isEnabled())
            likeState = 2; // not rated
        else if(dislikeFab.isEnabled())
            likeState = 1;
        else
            likeState = 0;

        ///states/{id}/{deviceId}/{viewState}/{likeState}
        String path = "http://" + apiPath + "/news/states/"+newsList.get(index).getNewsId()+"/"+deviceID+
                "/"+viewState+"/"+likeState;
        News temp = null;
        HttpURLConnection urlConnection = null;

        try {
            URL url = new URL(path);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.getInputStream();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            urlConnection.disconnect();
        }
    }

    public void updateLikeDislikeView(String label, int id, int mode) {
        String path = "";
        News temp = null;
        HttpURLConnection urlConnection = null;
        if (label.equals("view"))
            path = "http://" + apiPath + "/news/" + label + "/" + id;
        else
            path = "http://" + apiPath + "/news/" + label + "/" + id + "/" + mode;
        System.out.println(path);

        if (label.equals("dislike") && mode == 0) {
            temp = newsList.get(index);
            temp.setDislikeCount(temp.getDislikeCount() + 1);
        }
        if (label.equals("dislike") && mode == 1) {
            temp = newsList.get(index);
            temp.setDislikeCount(temp.getDislikeCount() - 1);
        }
        if (label.equals("like") && mode == 0) {
            temp = newsList.get(index);
            temp.setLikeCount(temp.getLikeCount() + 1);
        }
        if (label.equals("like") && mode == 1) {
            temp = newsList.get(index);
            temp.setLikeCount(temp.getLikeCount() - 1);
        }
        try {
            URL url = new URL(path);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.getInputStream();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            urlConnection.disconnect();
        }

    }

    public int[] getInfoByID(int newsID) {

        // burada newsID ile newsStateOnDevice tablosuna sorgu atıcaz ve like ve view statelerini döndürcez
        int[] states = null;
        String DeviceID = Secure.getString(ScrollingActivity.this.getContentResolver(), Secure.ANDROID_ID);

        String path = "http://" + apiPath + "/news/states/" + newsID + "/" + DeviceID;

        HttpURLConnection urlConnection = null;

        try {
            URL url = new URL(path);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setConnectTimeout(10000);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String line = bufferedReader.readLine();
            line = line.substring(1, line.length() - 1);
            String[] temp = line.split(",");
            states = new int[2];
            states[0] = Integer.parseInt(temp[0]);
            states[1] = Integer.parseInt(temp[1]);

            //switch like state
            switch (states[0]) {
                // not exists in db (not rated)
                case -1:
                    dislikeFab.setEnabled(true);
                    likeFab.setEnabled(true);
                    break;
                //disliked
                case 0:
                    dislikeFab.setEnabled(false);
                    likeFab.setEnabled(true);
                    break;
                //liked
                case 1:
                    dislikeFab.setEnabled(true);
                    likeFab.setEnabled(false);
                    break;
                //not rated
                case 2:
                    dislikeFab.setEnabled(true);
                    likeFab.setEnabled(true);
                    break;
            }

            //view state
            switch (states[1]) {
                //not exists in db (not viewed)
                case -1:
                    isViewed = false;
                    break;
                // not viewed
                case 0:
                    isViewed = false;
                    break;
                //viewed
                case 1:
                    isViewed = true;
                    break;
            }


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            urlConnection.disconnect();
        }


        /*
            Like State at 0th index
                    0 = Disliked
                    1 = Liked
                    2 = Not Rated
            View State at 1st index
                    0 = Not read yet
                    1 = Read

            0 . index Like State 1. index view State eğer sorgu boş dönerse
            ilk kez girimişizdir o zamanda 0. index '2' olacak
          */
        return states;
    }

    public void buttonStateControl() {
        if (!dislikeFab.isEnabled())
            dislikeFab.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));
        else
            dislikeFab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));
        if (!likeFab.isEnabled())
            likeFab.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));
        else
            likeFab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));
        if (index == 0) {
            nextFab.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));
            nextFab.setEnabled(false);
        }


    }

    public static String getNotificationState(){
        String path = "http://" + apiPath + "/news/notificationstate";

        HttpURLConnection urlConnection = null;

        try {
            URL url = new URL(path);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setConnectTimeout(10000);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String line = bufferedReader.readLine();
            return line;
        }catch (Exception e){
            e.printStackTrace();
        }
        return "exit";
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                Log.i ("isMyServiceRunning?", true+"");
                return true;
            }
        }
        Log.i ("isMyServiceRunning?", false+"");
        return false;
    }

    @Override
    protected void onDestroy() {
        stopService(mServiceIntent);
        Log.i("MAINACT", "onDestroy!");
        super.onDestroy();

    }
}
