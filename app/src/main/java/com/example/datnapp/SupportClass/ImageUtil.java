package com.example.datnapp.SupportClass;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Base64;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.camera.core.ImageProxy;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public class ImageUtil {
    public static String convertImageUriToBase64(Context context, Uri imageUri) {
        String base64String = null;

        try {
            ContentResolver contentResolver = context.getContentResolver();
            InputStream inputStream = contentResolver.openInputStream(imageUri);
            byte[] bytes = getBytes(inputStream);
            base64String = "data:image/png;base64," + Base64.encodeToString(bytes, Base64.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return base64String;
    }

    public static Bitmap convertImageToBitmap(@NonNull ImageProxy image) {
        ByteBuffer buffer = image.getPlanes()[0].getBuffer();
        byte[] bytes = new byte[buffer.remaining()];
        buffer.get(bytes);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    public static Uri bitmapToUri(Context context, Bitmap bitmap, int maxSizeKb, int maxWidth, int maxHeight) {
        bitmap = resizeBitmap(bitmap, maxWidth, maxHeight);
        // Compress the Bitmap to reduce size
        int quality = 100;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, byteArrayOutputStream);

        // Adjust quality to meet the desired max size
        while (byteArrayOutputStream.toByteArray().length / 1024 > maxSizeKb) {
            quality -= 10;
            byteArrayOutputStream.reset();
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, byteArrayOutputStream);
        }

        // Convert Bitmap to Uri without saving to storage
        String path = MediaStore.Images.Media.insertImage(
                context.getContentResolver(),
                bitmap,
                "Title",
                null);

        // Schedule the deletion of the temporary file
        scheduleDelete(context, Uri.parse(path));
        return Uri.parse(path);
    }

    private static void scheduleDelete(final Context context, final Uri uriToDelete) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Delete the temporary file after a short delay
                context.getContentResolver().delete(uriToDelete, null, null);
            }
        }, 300000); // delay 5 minutes
    }

    private static Bitmap resizeBitmap(Bitmap bitmap, int maxWidth, int maxHeight) {
        if (bitmap.getWidth() > maxWidth || bitmap.getHeight() > maxHeight) {
            float scale = Math.min((float) maxWidth / bitmap.getWidth(), (float) maxHeight / bitmap.getHeight());
            int newWidth = Math.round(bitmap.getWidth() * scale);
            int newHeight = Math.round(bitmap.getHeight() * scale);
            return Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true);
        } else {
            return bitmap;
        }
    }


    private static byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }

        return byteBuffer.toByteArray();
    }
}

