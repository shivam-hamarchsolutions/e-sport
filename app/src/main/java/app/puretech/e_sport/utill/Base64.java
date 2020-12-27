package app.puretech.e_sport.utill;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;

public class Base64 {
    /* get bitmap to base64*/
    public static String encodeImage(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 70, baos);
        byte[] b = baos.toByteArray();
        String encImage = android.util.Base64.encodeToString(b, android.util.Base64.DEFAULT);

        return encImage;

    }

    /* get base64 to bitmap*/
    public static Bitmap getBitmap(String base64) {

        byte[] decodedString = android.util.Base64.decode(base64, android.util.Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        return decodedByte;
    }
}
