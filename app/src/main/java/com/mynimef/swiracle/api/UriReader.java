package com.mynimef.swiracle.api;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

public class UriReader {
    public static String getRealPathFromUri(Context context, String contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media._ID };
            cursor = context.getContentResolver().query(Uri.parse(contentUri), proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }
}
