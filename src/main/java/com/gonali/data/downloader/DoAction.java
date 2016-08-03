package com.gonali.data.downloader;

import com.gonali.data.ui.DownloaderUI;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/8/2.
 */
public class DoAction implements Runnable {

    private String workDir;
    private String configFile;
    private DownloaderUI ui;
    private List<ItemData> itemDataList;
    private Downloader downloader;
    private String date;

    private String printerFileName;
    private File printerFile;
    private OutputStream printerOurStream;

    public static void openFilePrinter(DoAction doAction) {
        doAction.printerFileName = doAction.workDir + "/" +
                doAction.configFile.substring(doAction.configFile.lastIndexOf("\\") + 1) +
                "-" + doAction.date + "-pid-" +
                ManagementFactory.getRuntimeMXBean().getName().split("@")[0] + "-log.txt";
        try {
            doAction.printerFile = new File(doAction.printerFileName);
            doAction.printerOurStream = new FileOutputStream(doAction.printerFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void closeFilePrinter(DoAction doAction) {

        try {
            if (doAction.printerOurStream != null) {
                doAction.printerOurStream.flush();
                doAction.printerOurStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void printerToFile(String msg, DoAction doAction) {

        try {
            doAction.printerOurStream.write(msg.getBytes(), 0, msg.getBytes().length);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public DoAction(String workDir, String configFile, String date, DownloaderUI ui) {

        this.workDir = workDir + "/";
        this.configFile = configFile;
        this.ui = ui;
        this.itemDataList = new ArrayList<>();
        this.downloader = new Downloader();
        this.date = date;
    }


    private void buildItemDataList() {

        try {
            String[] configItems = FilesOperations.readFileLineByLine(this.configFile);
            for (String s : configItems) {
                if (s.charAt(0) == '#')
                    continue;
                s.trim();
                String[] item = s.split("\\s+");//此正则表示匹配一个或多个空格
                itemDataList.add(new ItemData(item[0], item[1], item[2], item[3]));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {

        DoAction.openFilePrinter(this);
        DoAction.printerToFile("开始下载数据 ...\n\n", this);

        buildItemDataList();

        try {
            for (ItemData i : itemDataList) {

                DoAction.printerToFile("以下下载 [" + i.getName() + "] 的数据\n", this);

                int startHour = Integer.parseInt(i.getStartTime().split(":")[0]);
                int stopHour = Integer.parseInt(i.getStopTime().split(":")[0]);
                int rangeHour = startHour + 1;

                startHour--;
                rangeHour--;

                do {
                    startHour++;
                    rangeHour++;
                    if (startHour > stopHour)
                        break;
                    i.setStartTime(date + "%20" + startHour + ":00");
                    i.setStopTime(date + "%20" + rangeHour + ":00");

                    if (startHour == stopHour)
                        i.setStopTime(date + "%2023:59");

                    String msg = "正在下载：" + i.getName() + "\n" + "日  期：" + date +
                            "\n" + "时 间段：从 " + startHour + ":00 到 " + rangeHour + ":00";
                    String url = i.getRequestDataUrl();
                    System.out.println("API: " + url);
                    DoAction.printerToFile("API: " + url + "\n", this);
                    ui.printStatus(msg);
                } while (downloader.downloadingByHttpClient(
                        i.getRequestDataUrl(),
                        workDir + date + "/" + i.getName() + "/",
                        i.getName() + "_" + date + "_" + startHour + "-00_" + rangeHour + "-00" + ".xml", this) > 0);
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        } finally {
            ui.printStatus("\n\t数据下载完成！");
            DownloaderUI.resetWindow(this.ui);
        }

        DoAction.printerToFile("数据下载完成！！\n", this);
        DoAction.closeFilePrinter(this);
    }
}
