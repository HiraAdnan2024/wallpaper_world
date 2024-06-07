package com.example.wallpaperworld;

import android.app.WallpaperManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MainActivity extends FlutterActivity {
    private static final String CHANNEL = "wallpaper_channel";
    private final Executor executor = Executors.newSingleThreadExecutor();

    @Override
    public void configureFlutterEngine(@NonNull FlutterEngine flutterEngine) {
        super.configureFlutterEngine(flutterEngine);
        new MethodChannel(flutterEngine.getDartExecutor().getBinaryMessenger(), CHANNEL)
                .setMethodCallHandler(
                        (call, result) -> {
                            if (call.method.equals("setWallpaper")) {
                                String imageUrl = call.argument("imageUrl");
                                setWallpaper(imageUrl, result);
                            } else {
                                result.notImplemented();
                            }
                        }
                );
    }

    private void setWallpaper(String imageUrl, MethodChannel.Result result) {
        executor.execute(() -> {
            try {
                URL url = new URL(imageUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap bitmap = BitmapFactory.decodeStream(input);
                WallpaperManager wallpaperManager = WallpaperManager.getInstance(getApplicationContext());
                wallpaperManager.setBitmap(bitmap);
                result.success(true);
            } catch (Exception e) {
                Log.e("Wallpaper Error", e.getMessage(), e);
                result.success(false);
            }
        });
    }
}
