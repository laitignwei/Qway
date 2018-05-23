package com.example.administrator.qway;

import android.util.Log;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpUtils {
  public static String getJsonContent(String urlPath) throws IOException {
      ByteArrayOutputStream outStream = new ByteArrayOutputStream();
                byte[] data = new byte[1024];
                  int len = 0;
                      URL url = new URL(urlPath);
                   HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    InputStream inStream = conn.getInputStream();
                     while ((len = inStream.read(data)) != -1) {
                          outStream.write(data, 0, len);
                        }
                   inStream.close();
                   return new String(outStream.toByteArray());//通过out.Stream.toByteArray获取到写的数据

  }
}