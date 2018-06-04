package com.snapxeats;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;

import com.snapxeats.common.constants.SnapXToast;
import com.snapxeats.common.constants.UIConstants;
import com.snapxeats.common.model.smartphotos.SmartPhotoResponse;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.snapxeats.common.constants.UIConstants.BUFFER_SIZE;
import static com.snapxeats.common.constants.UIConstants.BYTES;
import static com.snapxeats.common.constants.UIConstants.IMAGE_TYPE;
import static com.snapxeats.common.constants.UIConstants.ONE;
import static com.snapxeats.common.constants.UIConstants.ZERO;

/**
 * Created by Snehal Tembare on 1/6/18.
 */

public class DownloadTask extends AsyncTask<String, Integer, String> {
    private ProgressDialog mProgressbar;
    private Context mContext;
    private String downloadedFileName;
    private SmartPhotoResponse mSmartPhoto;
    private OnDownloadCompleted onDownloadCompleted;

    public DownloadTask(OnDownloadCompleted onDownloadCompleted,
                        Context context,
                        SmartPhotoResponse response) {
        mContext = context;
        mSmartPhoto = response;
        this.onDownloadCompleted = onDownloadCompleted;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mProgressbar = new ProgressDialog(mContext);
        mProgressbar.setMessage(mContext.getString(R.string.downloading));
        mProgressbar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressbar.setCancelable(false);
        mProgressbar.setMax(UIConstants.PERCENTAGE);
        mProgressbar.show();
    }

    @Override
    protected String doInBackground(String... urls) {
        int count;
        int lenghtOfFile;
        InputStream input = null;
        for(String url : urls){
                try {
                    URL link = new URL(url);

                    URLConnection connection = link.openConnection();
                    connection.connect();
                    // getting file length
                    lenghtOfFile = connection.getContentLength();

                    // input stream to read file - with 8k buffer
                    input = new BufferedInputStream(link.openStream(), BUFFER_SIZE);
                    String timeStamp = new SimpleDateFormat(mContext.getString(R.string.file_name_pattern)).format(new Date());
                    SimpleDateFormat df = new SimpleDateFormat(mContext.getString(R.string.file_name_pattern));
                    Date date = df.parse(timeStamp);
                    long fileName = date.getTime();

                    File downloadDirectory = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                            + "/" + mContext.getString(R.string.app_name) + "/" + mContext.getString(R.string.downloads) + "/");
                    if (!downloadDirectory.exists()) {
                        if (!downloadDirectory.mkdirs()) {
                            return null;
                        }
                    }
                    // Output stream to write file
                    OutputStream output;
                    File outputFile;

                    String ext = url.substring(url.lastIndexOf("."));
                        outputFile = new File(downloadDirectory, mContext.getString(R.string.download)
                                + fileName + ext);
                        output = new FileOutputStream(outputFile);
                        downloadedFileName = outputFile.getPath();


                    String type = connection.guessContentTypeFromName(downloadedFileName);
                    if(null != type && !type.isEmpty() && type.contains(IMAGE_TYPE)){
                        //It's a image file
                        mSmartPhoto.setDish_image_url(downloadedFileName);
                    } else {
                        //It's a audio file
                        mSmartPhoto.setAudio_review_url(downloadedFileName);
                    }

                    byte data[] = new byte[BYTES];

                    long total = ZERO;

                    while (-ONE != (count = input.read(data))) {
                        total += count;
                        // publishing the progress....
                        // After this onProgressUpdate will be called
                        int percentage = (int) ((total * UIConstants.PERCENTAGE) / lenghtOfFile);
                        publishProgress(percentage);
                        // writing data to file
                        output.write(data, ZERO, count);
                    }

                    // flushing output
                    output.flush();

                    // closing streams
                    output.close();
                    input.close();

                } catch (Exception e) {
                    SnapXToast.error(mContext.getString(R.string.error) + e.getMessage());
                }
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        mProgressbar.setProgress(values[ZERO]);
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        mProgressbar.dismiss();
        onDownloadCompleted.isDownloadComplete(true, mSmartPhoto);
    }

    public interface OnDownloadCompleted {
        void isDownloadComplete(boolean isComplete, SmartPhotoResponse smartPhotoResponse);
    }
}