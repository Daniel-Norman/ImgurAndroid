# ImgurAndroid
Imgur helper classes for Android for image uploading without account login and image downloading.

## Installation
Download the **httpcore** and **httpmime** jar files from Apache, and add them to your project's libs folder.

Add the following lines to your app's build.gradle file:
```
compile files('libs/httpcore-4.4.1.jar')
compile files('libs/httpmime-4.3.jar')
```
replacing version numbers with the numbers of your downloaded jar files.

Also add the following lines to the build.gradle file:
```
android {
  ...
  packagingOptions {
    exclude 'META-INF/LICENSE'
    exclude 'META-INF/DEPENDENCIES'
    exclude 'META-INF/NOTICE'
  }
  ...
}
```

Next, [register an application with Imgur](https://api.imgur.com/oauth2/addclient) and get a Client ID for the application. Replace the _CLIENT_ID_ String in ImgurUploader.java with your Client ID.

You are good to go! Feel free to change the compression quality in **MainActivity.java** to trade off faster uploading and smaller sizes for lower quality images, and change the required image sizes in **ImageDownloader.java** to allow the class to avoid downloading excessively large images.
