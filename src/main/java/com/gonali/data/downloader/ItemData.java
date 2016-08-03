package com.gonali.data.downloader;

/**
 * Created by Administrator on 2016/8/2.
 */
public class ItemData {

    private static final String url = "http://dataapi.skieer.com/SkieerDataAPI/GetData";
    private String name;
    private String key;
    private String startTime;
    private String stopTime;

    public ItemData(){

        this.name = "";
        this.key = "";
        this.startTime = "";
        this.stopTime = "";
    }

    public ItemData(String name, String key, String startTime, String stopTime){

        this.name = name;
        this.key = key;
        this.startTime = startTime;
        this.stopTime = stopTime;
    }

    public static String getUrl() {
        return url;
    }

    public String getName() {

        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getStopTime() {
        return stopTime;
    }

    public void setStopTime(String stopTime) {
        this.stopTime = stopTime;
    }

    public String getRequestDataUrl() {

        return url + "?key=" + key + "&from=" + startTime + "&to=" + stopTime;
    }
}
