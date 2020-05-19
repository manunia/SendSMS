package ru.geekbrains.sendsms;

import android.app.admin.DeviceAdminInfo;
import android.content.Context;
import android.content.res.Configuration;
import android.util.Log;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import ru.geekbrains.sendsms.data.Telephone;

public class HttpsConnection {
    private static final String TAG = "SEND_SMS";

    public void createConnection(String incomingNumber,String imei) {
        try {
            final URL uri = new URL(BuildConfig.REQUEST_URL);

            HttpsURLConnection urlConnection = null;

            try {
                urlConnection = (HttpsURLConnection) uri.openConnection();
                urlConnection.setRequestMethod("POST");

                Gson gson = new Gson();
                Telephone telephone = getTelephone(incomingNumber, imei);

                String urlParameters = gson.toJson(telephone);

                urlConnection.setDoOutput(true);
                try(DataOutputStream wr = new DataOutputStream(urlConnection.getOutputStream())) {
                    wr.writeBytes(urlParameters);
                    wr.flush();
                }
                int responseCode = urlConnection.getResponseCode();

                Log.e(TAG, "\nSending 'POST' request to URL : " + uri);
                Log.e(TAG, "Post parameters : " + urlParameters);
                Log.e(TAG, "Response Code : " + responseCode);

            } catch (IOException e) {
                Log.e(TAG, "Fail connection", e);
                e.printStackTrace();
            } finally {
                if (null != urlConnection) {
                    urlConnection.disconnect();
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    private Telephone getTelephone(String incomingNumber, String iemi) {

        Telephone telephone = new Telephone();
        telephone.setNumber(incomingNumber);
        telephone.setTimeStamp(System.currentTimeMillis());
        telephone.setDevice(iemi);
        return telephone;
    }
}
