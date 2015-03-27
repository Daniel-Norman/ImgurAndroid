import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ImageDownloader extends AsyncTask<String, Void, Boolean> {
    private Bitmap mBitmap;

    //The minimum required height and width
    private final static int REQUIRED_WIDTH = 102, REQUIRED_HEIGHT = 102;

    @Override
    public Boolean doInBackground(String... params) {
        mBitmap = null;
        try {
            URL url = new URL(params[0]);
            HttpURLConnection connection_size = (HttpURLConnection) url.openConnection();
            connection_size.setDoInput(true);
            connection_size.connect();
            HttpURLConnection connection_image = (HttpURLConnection) url.openConnection();
            connection_image.setDoInput(true);
            connection_image.connect();

            mBitmap = decodeSampledBitmapFromStream(
                    connection_size.getInputStream(),
                    connection_image.getInputStream(),
                    REQUIRED_WIDTH, REQUIRED_HEIGHT);
            return mBitmap != null;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Boolean success) {
        super.onPostExecute(success);

        if (success) {
            /**
             * TODO
             * Do something with mBitmap, such as notifying the class
             * that called this task and giving it mBitmap.
             */
        }
    }

    public static Bitmap decodeSampledBitmapFromStream(InputStream is_size, InputStream is_image, int reqWidth, int reqHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(is_size, null, options);

        options.inSampleSize = calculateInSampleSize(options.outWidth, options.outHeight, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeStream(is_image, null, options);
    }

    //From http://developer.android.com/training/displaying-bitmaps/load-bitmap.html
    public static int calculateInSampleSize(int width, int height, int reqWidth, int reqHeight) {
        // Raw height and width of image
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }
}
