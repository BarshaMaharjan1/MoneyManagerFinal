package com.example.moneymanager;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class Converter {



   public static byte[] getBase64Bytes(int drawable, Context context) {

       Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),drawable);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encode(byteArray, Base64.DEFAULT);
    }

    Bitmap myBitmap; //should be initialized to some value

    public  static Bitmap getImageFromBase64Blob(byte[] blob){
        byte[] byteArray = Base64.decode(blob, Base64.DEFAULT);
        Bitmap bitmap= BitmapFactory.decodeByteArray(byteArray , 0, byteArray.length);
        return bitmap;
    }


}
