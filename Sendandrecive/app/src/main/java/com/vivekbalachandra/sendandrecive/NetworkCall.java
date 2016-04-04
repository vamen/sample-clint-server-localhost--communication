package com.vivekbalachandra.sendandrecive;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Vivek Balachandran on 4/1/2016.
 * AsyncTask is a class/library used to handle small network calls in background
 * refer developers.android.com for more info
 * Note: all network related calls should not be done in main/UI thread
 * use good  libraries  like retrofit,volly  etc for long running network calls or use intent-services
 */
public class NetworkCall extends AsyncTask<String, String, String> {
    HashMap<String, String> data;
    String userName,password;
    Context context;
    public NetworkCall(String name, String passwd,Context ctx) {
        userName=name;
        password=passwd;
        context=ctx;
    }


    @Override
    protected String doInBackground(String... params) {
        try {
            //192.168.0.x is the ip assigned to the server host laptop by router
            //index.php is the php file that handles the post request
            URL url = new URL("http://192.168.0.103/project/index.php");
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);

            data = getPerams();
           //open the urls output stream and write to it, which is sent to server
            OutputStream outputStream = urlConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            bufferedWriter.write(getQuery(data));
            bufferedWriter.flush();
            bufferedWriter.close();

            int responseCode=urlConnection.getResponseCode();

            if(responseCode==HttpURLConnection.HTTP_OK) {

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer stringBuffer = new StringBuffer();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    stringBuffer.append(line + "\n");
                }
                Log.d(MainActivity.class.getName() + "network",responseCode+":"+stringBuffer.toString());
                if(stringBuffer.toString().contains("Success")) {
                    Intent intent=new Intent(context,Trinfo.class);
                    context.startActivity(intent);
                }
                else
                    Log.d(MainActivity.class.getName() + "network",responseCode+":"+stringBuffer.toString());
            }
            else
            {
                Log.e("Response code",responseCode+"");
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return null;
    }

   //method that encodes the key value pairs for post request
    String getQuery(HashMap<String, String> params) throws UnsupportedEncodingException {
        StringBuilder query=new StringBuilder();
        boolean first = true;
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (first)
                first=false;
            else
                query.append('&');

            String key=entry.getKey();
            Log.d("NetworkCall",key);
            query.append(URLEncoder.encode(key, "UTF-8"));
            query.append("=");
            query.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }
            return query.toString();
    }
    //method that gets and stores post data
    public HashMap<String, String> getPerams() {
        HashMap<String, String> perams = new HashMap<>();
        /* get the data from the local memory
        that is to  be sent to server*/
        perams.put("user_name", userName.trim());
        perams.put("passwd", password.trim());//use encryption's like md5 etc while storing password
        return perams;
    }
}
