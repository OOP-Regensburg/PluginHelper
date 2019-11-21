package de.ur.mi.pluginhelper.com;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.File;
import java.io.IOException;


public class UploadClient {

    private static final String DEFAULT_UPLOAD_URL = "http://localhost:8080/upload";
    private String uploadURL = DEFAULT_UPLOAD_URL;

    public UploadClient() {

    }

    public UploadClient(String uploadURL) {
        this.uploadURL = uploadURL;
    }

    public void uploadFile(File file, String experimentID, String userID, String sessionID, UploadClientListener listener) {
        CloseableHttpClient client = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost(uploadURL);
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.addTextBody("experiment", experimentID);
        builder.addTextBody("user", userID);
        builder.addTextBody("session", sessionID);
        builder.addBinaryBody("file", file);
        HttpEntity multipart = builder.build();
        post.setEntity(multipart);

        try {
            CloseableHttpResponse response = client.execute(post);
            listener.onUploadFinished();
            client.close();
        } catch (IOException e) {
            listener.onUploadFailed();
            e.printStackTrace();
        }
    }


}
