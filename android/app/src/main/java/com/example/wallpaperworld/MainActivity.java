package com.example.wallpaperworld;

import io.flutter.embedding.android.FlutterActivity;
import android.app.WallpaperManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import io.flutter.embedding.android.FlutterActivity;
import io.flutter.embedding.engine.FlutterEngine;
import io.flutter.plugin.common.MethodChannel;


public class MainActivity extends FlutterActivity {
    private static final String CHANNEL = "wallpaper_channel";

    @Override
   public void configureFlutterEngine(FlutterEngine flutterEngine){
        super.configureFlutterEngine(flutterEngine);
new MethodChannel(flutterEngine.getDartExecutor().getBinaryMessenger(), CHANNEL)
        .setMethodCallHandler(
                (call, result) -> {
                    if (call.method.equals("setWallpaper")){
                        String path = call.argument("path");
                        setWallpaper(path);
                        result.success(true);
                    }else {
                        result.notImplemented();
                    }
                }
        );
    }

    private void setWallpaper(String path){

        new SetWallpaperTask().execute(path);
    }

    private class SetWallpaperTask extends AsyncTask<String, Void, Boolean> {
        @Override
        protected Boolean doInBackground(String... params) {
            try {
                String imageUrl = params[0];
                Bitmap bitmap = getBitmapFromURL(imageUrl);
                if (bitmap != null) {
                    WallpaperManager wallpaperManager = WallpaperManager.getInstance(getApplicationContext());
//                    wallpaperManager.setBitmap(bitmap);
                    wallpaperManager.setBitmap(bitmap, null, true, WallpaperManager.FLAG_SYSTEM);
                    return true; // Wallpaper set success
                    // fully
                }
            } catch (IOException e) {
                Log.e("WallpaperSetter", "Error setting wallpaper: " + e.getMessage());
            }
            return false; // Failed to set wallpaper
        }

        private Bitmap getBitmapFromURL(String imageUrl) throws IOException {
            HttpURLConnection connection = null;
            try {
                URL url = new URL(imageUrl);
                connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                return BitmapFactory.decodeStream(input);
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if (success) {
                Log.d("WallpaperSetter", "Wallpaper set successfully.");
            } else {
                Log.e("WallpaperSetter", "Failed to set wallpaper.");
            }
        }
    }
}
