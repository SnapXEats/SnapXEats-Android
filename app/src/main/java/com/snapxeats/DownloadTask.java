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
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.snapxeats.common.constants.UIConstants.BUFFER_SIZE;
import static com.snapxeats.common.constants.UIConstants.BYTES;
import static com.snapxeats.common.constants.UIConstants.IMAGE;
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

    private long fileName;
    private File downloadDirectory;
    private int lenghtOfFile;

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
        InputStream input = null;
        OutputStream output = null;
        for (String url : urls) {
            try {
                URL link = new URL(url);

                URLConnection connection = link.openConnection();
                connection.connect();
                // getting file length
                lenghtOfFile = connection.getContentLength();

                input = inputStreamToFile(link);

                downloadDirectory = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                        + "/" + mContext.getString(R.string.app_name) + "/" + mContext.getString(R.string.downloads) + "/");
                if (!downloadDirectory.exists()) {
                    if (!downloadDirectory.mkdirs()) {
                        return null;
                    }
                }

                output = outputStreamToFile(url, connection);

                updateProgress(input, output);

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

    private void updateProgress(InputStream input, OutputStream output) {
        int count;

        byte data[] = new byte[BYTES];

        long total = ZERO;

        try {
            while (-ONE != (count = input.read(data))) {
                total += count;
                // publishing the progress....
                // After this onProgressUpdate will be called
                int percentage = (int) ((total * UIConstants.PERCENTAGE) / lenghtOfFile);
                publishProgress(percentage);
                // writing data to file
                output.write(data, ZERO, count);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private OutputStream outputStreamToFile(String url, URLConnection connection) {
        // Output stream to write file
        File outputFile;
        OutputStream output = null;
        if (null != url && !url.isEmpty()) {
            String ext = url.substring(url.lastIndexOf("."));
            outputFile = new File(downloadDirectory, mContext.getString(R.string.download)
                    + fileName + ext);
            try {
                output = new FileOutputStream(outputFile);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            downloadedFileName = outputFile.getPath();
        }

        if (null != connection) {
            String type = connection.guessContentTypeFromName(downloadedFileName);
            if (null != type && !type.isEmpty() && type.contains(IMAGE +
                    downloadedFileName.substring(downloadedFileName.lastIndexOf(".") + ONE))) {
                //It's a image file
                mSmartPhoto.setDish_image_url(downloadedFileName);
            } else {
                //It's a audio file
                mSmartPhoto.setAudio_review_url(downloadedFileName);
            }
        }
        return output;
    }

    private InputStream inputStreamToFile(URL link) {
        // input stream to read file - with 8k buffer
        InputStream input = null;
        if (null != link) {
            try {
                input = new BufferedInputStream(link.openStream(), BUFFER_SIZE);
                String timeStamp = new SimpleDateFormat(mContext.getString(R.string.file_name_pattern)).format(new Date());
                SimpleDateFormat df = new SimpleDateFormat(mContext.getString(R.string.file_name_pattern));
                Date date = df.parse(timeStamp);
                fileName = date.getTime();
            } catch (IOException | ParseException e) {
                e.printStackTrace();
            }
        }
        return input;
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