package com.sharepay.wifi.helper;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;

public class NetSpeedDownload {

    private final String TAG = "NetSpeedDownload ";
    private String request;
    private NetSpeedCallBack cb;
    private boolean isRunning = false;
    private boolean action = true;

    public NetSpeedDownload(NetSpeedCallBack cb) {
        this.cb = cb;
    }

    public boolean getIsRunning() {
        return isRunning;
    }

    public void stopSpeed() {
        action = false;
    }

    public void startSpeed(String downloadUrl) {
        request = downloadUrl;
        LogHelper.releaseLog(TAG + "start =---" + request);
        if (true) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        action = true;
                        URL url = new URL(request);
                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                        conn.setConnectTimeout(2000);
                        conn.setReadTimeout(3000);
                        conn.connect();
                        InputStream is = conn.getInputStream();
                        long startTime = System.currentTimeMillis();
                        long currenTime = startTime;
                        long countAll = 0;
                        long speed = 0;
                        byte buf[] = new byte[1024 * 4];
                        long numread = 0;
                        isRunning = true;
                        while (action && currenTime - startTime <= 10 * 1000) {
                            if ((numread = is.read(buf)) != -1) {
                                countAll += numread;
                            }
                            currenTime = System.currentTimeMillis();
                            long simpleTime = currenTime - startTime;
                            if (simpleTime > 200 && simpleTime < 10000) {
                                speed = (countAll * 8 * 1000) / (simpleTime * 1024);
                                if (null != cb) {
                                    cb.speed(speed);
                                }
                            }
                        }
                        if (null != cb) {
                            cb.speedFinish();
                            isRunning = false;
                        }
                    } catch (SocketTimeoutException ste) {
                        ste.printStackTrace();
                        if (null != cb) {
                            cb.noNet();
                            isRunning = false;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        if (null != cb) {
                            cb.noNet();
                            isRunning = false;
                        }
                        isRunning = false;
                    }
                }
            }).start();
        } else {
            if (null != cb) {
                cb.noNet();
            }
        }
    }

    public interface NetSpeedCallBack {

        void speed(long kb);

        void noNet();

        void speedFinish();
    }
}
