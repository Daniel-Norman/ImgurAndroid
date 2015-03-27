import android.os.AsyncTask;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.File;

public class ImgurUploader extends AsyncTask<File, Void, String> {
    //TODO: Change CLIENT_ID to your personal Imgur Client ID
    private static String CLIENT_ID = "Your Client ID from the Imgur API. Ex: 123ab4c567890d";

    @Override
    public String doInBackground(File... params) {
        final String upload_to = "https://api.imgur.com/3/upload.json";
        final String API_key = "API_KEY";

        HttpClient httpClient = new DefaultHttpClient();
        HttpContext localContext = new BasicHttpContext();
        HttpPost httpPost = new HttpPost(upload_to);

        httpPost.setHeader("Authorization", "Client-ID "+ CLIENT_ID);

        try {
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
            builder.addTextBody("key", API_key);
            builder.addBinaryBody("image", params[0]);
            httpPost.setEntity(builder.build());

            final HttpResponse response = httpClient.execute(httpPost, localContext);
            final String response_string = EntityUtils.toString(response.getEntity());
            final JSONObject json = new JSONObject(response_string);

            if (json.getBoolean("success")) {
                JSONObject results = (JSONObject) json.get("data");
                return results.getString("link");
            }
            else {
                System.out.println("Error posting image to Imgur.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        if (result != null) {
            System.out.println("Success! Image link: " + result);
        }
    }
}
