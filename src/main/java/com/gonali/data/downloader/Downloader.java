package com.gonali.data.downloader;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BufferedHeader;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by Administrator on 2016/8/2.
 */
public class Downloader {

    private byte[] buffer;

    //private HttpClient httpClient;

    public Downloader() {

        buffer = new byte[1024];
    }

    public synchronized int downloadingByHttpClient(String dataUrl, String directory, String fileName, DoAction doAction) {

        int bytesum = 0;
        int byteread;
        long dataLength;

        RequestConfig defaultRequestConfig = RequestConfig.custom()
                .setSocketTimeout(10000)
                .setConnectTimeout(10000)
                .setConnectionRequestTimeout(10000)
                .setStaleConnectionCheckEnabled(true)
                .build();

        // 生成一个httpclient对象
        CloseableHttpClient httpClient = HttpClients.custom()
                .setDefaultRequestConfig(defaultRequestConfig)
                .build();

        HttpGet httpget = new HttpGet(dataUrl);

        InputStream in = null;
        BufferedHeader reader = null;
        HttpResponse response;

        try {

            response = httpClient.execute(httpget);
            System.out.println("response Code: " + response.getStatusLine().getStatusCode());
            DoAction.printerToFile("response Code: " + response.getStatusLine().getStatusCode() + "\n", doAction);
            HttpEntity entity = response.getEntity();
            System.out.println("entity:" + entity.toString());
            DoAction.printerToFile("entity:" + entity.toString() + "\n", doAction);
            dataLength = entity.getContentLength();
            in = new BufferedInputStream(entity.getContent());
        } catch (IOException e) {

            e.printStackTrace();
            return 1;
        }

        File fileD = new File(directory);
        //如果文件夹不存在则创建
        if (!fileD.exists() && !fileD.isDirectory()) {
            System.out.println("making the directory: " + directory + "\n");
            DoAction.printerToFile("making the directory: " + directory + "\n", doAction);
            fileD.mkdirs();
        }

        File file = new File(directory + fileName);

        try {
            FileOutputStream fout = new FileOutputStream(file);

            try {
                while ((byteread = in.read(buffer, 0, 1024)) != -1) {
                    fout.write(buffer, 0, byteread);
                    bytesum += byteread;
                    if (bytesum >= dataLength)
                        break;
                }
            } catch (IOException e) {
                fout.flush();
                fout.close();
                e.printStackTrace();
                DoAction.printerToFile(e.getStackTrace().toString() + "\n", doAction);
                return this.downloadingByHttpClient(dataUrl, directory, fileName, doAction);
            }

            fout.flush();
            fout.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            return 1;
        } finally {
            // 关闭低层流。
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        System.out.println("downloading OK. byteSum: " + bytesum + " bytes.");
        DoAction.printerToFile("downloading OK. byteSum: " + bytesum + " bytes.\n\n", doAction);
        return bytesum;
        //httpClient.close();
    }


    public synchronized int downloading(String dataUrl, String directory, String fileName) {


        int bytesum = 0;
        int byteread;
        FileOutputStream fs;
        try {

            URL url = new URL(dataUrl);
            URLConnection conn = url.openConnection();
            InputStream inStream = conn.getInputStream();
            File fileD = new File(directory);

            //如果文件夹不存在则创建
            if (!fileD.exists() && !fileD.isDirectory()) {
                System.out.println("making the directory: " + directory);
                fileD.mkdirs();
            }

            fs = new FileOutputStream(directory + fileName);

            while ((byteread = inStream.read(buffer, 0, 1024)) != -1) {
                bytesum += byteread;
                fs.write(buffer, 0, byteread);

            }

            fs.flush();
            fs.close();
            inStream.close();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        } finally {

        }
        System.out.println("downloading OK. byteSum: " + bytesum + " bytes.");
        return bytesum;

    }


}
