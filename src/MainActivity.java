import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

public class MainActivity extends ActionBarActivity {
    private static final int REQUEST_CHOOSE_IMAGE = 1;
    private static final int COMPRESSION_QUALITY = 50; //Value between 0 and 100 (inclusive)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void downloadImage(View view) {
        ImageDownloader downloader = new ImageDownloader();
        downloader.execute("Link to a remote image");
    }

    public void openGallery(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQUEST_CHOOSE_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CHOOSE_IMAGE && data != null && data.getData() != null) {
            startImageUpload(data.getData());
        }
    }

    private void startImageUpload(Uri imageUri) {
        try {
            File f = new File(getCacheDir(), "image.jpeg");
            f.createNewFile();

            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, COMPRESSION_QUALITY, bos);
            byte[] bitmapdata = bos.toByteArray();

            FileOutputStream fos = new FileOutputStream(f);
            fos.write(bitmapdata);
            fos.flush();
            fos.close();

            /**
             * Create an AsyncTask to upload the image chosen in the image gallery.
             * The bitmap doesn't have to come from the gallery, just change
             * Bitmap 'bitmap' to the image you would like to upload.
             * Upon success, the ImgurUploader task prints the image's link on Imgur,
             * and can easily be modified to use this link throughout the app.
             */
            ImgurUploader uploader = new ImgurUploader();
            uploader.execute(f);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
