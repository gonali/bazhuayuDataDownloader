package com.gonali.data.ui;

import com.gonali.data.downloader.DoAction;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Date;

/**
 * Created by Administrator on 2016/8/2.
 */
public class DownloaderUI extends JFrame {

    public static void main(String[] args) {

        new DownloaderUI();
    }

    private JLabel labelDirectory;
    private JLabel labelConfig;
    private JLabel labelDate;
    private JTextField downloadDirectoryTextField;
    private JTextField configFileTextField;
    private JButton dirButton;
    private JButton confButton;
    private JButton startButton;
    private DatePickButton datePickButton;

    private JTextArea statusTextArea;


    public DownloaderUI() {

        super("八爪鱼云数据下载器--Downloader");
        super.setSize(740, 600);
        super.setVisible(true);
        super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        centered(this);
        this.setLayout(null);//设置布局管理器为空

        /******************************************************************/

        labelDirectory = new JLabel("下载器工作目录:");
        labelDirectory.setBounds(50, 30, 120, 30);

        downloadDirectoryTextField = new JTextField();
        downloadDirectoryTextField.setBounds(170, 30, 400, 30);
        downloadDirectoryTextField.setForeground(Color.red);

        dirButton = new JButton("选择...");
        dirButton.setBounds(572, 30, 80, 30);
        dirButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {

                downloadDirectoryTextField.setText(getWorkDir());
            }
        });

        /*****************************************************************/

        labelConfig = new JLabel("== 配置文件 ==:");
        labelConfig.setBounds(50, 80, 120, 30);

        configFileTextField = new JTextField();
        configFileTextField.setBounds(170, 80, 400, 30);
        configFileTextField.setForeground(Color.BLUE);

        confButton = new JButton("选择...");
        confButton.setBounds(572, 80, 80, 30);
        confButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                configFileTextField.setText(getConfigFileName());
            }
        });

        /*******************************************************************/

        labelDate = new JLabel("== 选择日期 ==:");
        labelDate.setBounds(50, 130, 120, 30);
        datePickButton = new DatePickButton(new Date());
        datePickButton.setBounds(170, 130, 100, 30);
        System.out.println("date: " + datePickButton.getText());

        /*******************************************************************/

        startButton = new JButton("== 开 = 始 = 下 = 载 ==");
        startButton.setBounds(320, 130, 330, 30);
        startButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startDownload();
            }
        });

        /*******************************************************************/

        statusTextArea = new JTextArea("========  Hello ========");
        statusTextArea.setBounds(50, 210, 600, 300);
        statusTextArea.setLineWrap(true);
        statusTextArea.setWrapStyleWord(true);
        statusTextArea.setBackground(Color.BLACK);
        statusTextArea.setForeground(Color.green);

        /*******************************************************************/

        this.setVisible(true);
        this.add(labelDirectory);
        this.add(downloadDirectoryTextField);
        this.add(dirButton);

        this.add(labelConfig);
        this.add(configFileTextField);
        this.add(confButton);

        this.add(labelDate);
        this.add(datePickButton);

        this.add(startButton);

        this.add(statusTextArea);

        this.setVisible(true);
        this.repaint();

    }


    //布局居中方法
    public void centered(Container container) {

        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();
        int w = container.getWidth();
        int h = container.getHeight();
        container.setBounds((screenSize.width - w) / 2,
                (screenSize.height - h) / 2, w, h);
    }


    public String getWorkDir() {

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fileChooser.showDialog(this, null);
        fileChooser.getToolTipText();

        return fileChooser.getSelectedFile().getPath();
    }


    public String getConfigFileName() {

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        fileChooser.showDialog(this, null);
        fileChooser.getToolTipText();

        return fileChooser.getSelectedFile().getPath();
    }


    public void startDownload() {

        if (downloadDirectoryTextField.getText() == null ||
                downloadDirectoryTextField.getText().equals("") ||
                configFileTextField.getText() == null ||
                configFileTextField.getText().equals("")) {
            JLabel label = new JLabel("参数错误！！！");
            label.setForeground(Color.red);
            label.setBounds(80, 60, 40, 30);
            centered(label);
            JDialog dialog = new JDialog(this);
            dialog.setSize(160, 120);
            dialog.add(label);
            centered(dialog);
            dialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            dialog.setTitle("参数错误!!");
            dialog.show();
            return;
        }

        startButton.setText("正在下载数据......");
        //startButton.setForeground(Color.red);
        setConponetEnabled(false);
        Thread downloadThread = new Thread(new DoAction(downloadDirectoryTextField.getText(),
                configFileTextField.getText(), datePickButton.getText(), this));
        downloadThread.setDaemon(false);

        try {
            downloadThread.start();
        } catch (Exception e) {
            JLabel label = new JLabel("内部错误！！！");
            label.setForeground(Color.red);
            label.setBounds(80, 60, 40, 30);
            centered(label);
            JDialog dialog = new JDialog(this);
            dialog.setSize(160, 120);
            dialog.add(label);
            centered(dialog);
            dialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            dialog.setTitle("内部错误，请检查参数!!");
            dialog.show();
            e.printStackTrace();
            DownloaderUI.resetWindow(this);
        }

    }


    private void setConponetEnabled(boolean status) {

        downloadDirectoryTextField.setEnabled(status);
        configFileTextField.setEnabled(status);
        dirButton.setEnabled(status);
        confButton.setEnabled(status);
        datePickButton.setEnabled(status);
        startButton.setEnabled(status);

    }

    public void printStatus(String msg) {

        statusTextArea.setText(msg);
    }

    public void cleanStatus() {
        statusTextArea.setText("==> ");
    }

    public static void resetWindow(DownloaderUI obj) {

        obj.setConponetEnabled(true);
        obj.startButton.setText("== 开 = 始 = 下 = 载 ==");
        //obj.startButton.setForeground(Color.BLACK);
    }

}
