/*
 * Copyright (C) 2014 DataStax
 *
 *
 *     Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 *     The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 *     THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.datastax.commands;

import com.datastax.ShellRunner;
import com.datastax.configuration.Configuration;
import com.datastax.gui.Output;
import com.datastax.gui.WifiPrompt;
import com.datastax.security.WifiCredentials;
import javafx.application.Platform;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

@Component
public class WifiControl {
    @Autowired
    private Configuration configuration;
    @Autowired
    private Output output;
    @Autowired
    private ShellRunner shellRunner;
    @Autowired
    private WifiPrompt wifiPrompt;

    private final String airportCommand = "/System/Library/PrivateFrameworks/Apple80211.framework/Versions/Current/Resources/airport";
    private final String networkSetupCommand = "networksetup";

    private boolean networkIsAvailable(String ssid){
        String scanningOutput = shellRunner.runAndOutput(airportCommand, "-s");
        StringReader stringReader = new StringReader(scanningOutput);
        BufferedReader reader = new BufferedReader(stringReader);
        try {
            String firstLine = reader.readLine();
            Integer ssidColumnEnd = firstLine.indexOf("SSID") + 4;
            if(ssidColumnEnd==null){
                throw new RuntimeException("unable to read SSID of current network");
            }
            String line;
            while((line = reader.readLine())!=null){
                String firstColumn = line.substring(0,ssidColumnEnd);
                String network = firstColumn.trim();
                if(network.equals(ssid)){
                    return true;
                }
            }
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
        throw new RuntimeException(ssid + " is not available");
    }
    private boolean isCurrentNetwork(String ssid){
        String scanningOutput = shellRunner.runAndOutput(airportCommand, "-I");
        StringReader stringReader = new StringReader(scanningOutput);
        BufferedReader reader = new BufferedReader(stringReader);
        try {
            String line;
            while((line = reader.readLine())!=null){
                System.out.println(line);
                if(line.contains("SSID: "+ ssid)){
                    return true;
                }
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    public void verify() {
        WifiCredentials creds = configuration.getWifiCreds();
        String ssid = creds.getSSID();
        if(!isCurrentNetwork(ssid)) {
            if (!networkIsAvailable(ssid)) {
                throw new RuntimeException("unable to find network " + ssid);
            }else {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }

                        wifiPrompt.show();
                    }
                });
                //FIXME hack hack hack hack, need better way of waiting on thread if JavaFX
                while(wifiPrompt.getClicks().size()==0){
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }

                }
                final List<Void> closed = new ArrayList<>();
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        wifiPrompt.close();
                        closed.add(null);
                    }
                });

                //FIXME hack hack hack hack, need better way of waiting on thread if JavaFX
                while(closed.size()==0){
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }

                }
                if(wifiPrompt.getFailures().size()>0){
                    throw new RuntimeException(wifiPrompt.getFailures().get(0));
                }
            }
        }
    }

    public void switchToClusterNetwork() {
        String airportDevice = getAirportInterface();

        WifiCredentials creds = configuration.getWifiCreds();
        shellRunner.runAndOutput(networkSetupCommand, "-setairportnetwork", airportDevice, creds.getSSID(),creds.getPassword());
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private String getAirportInterface() {
        String scanningOutput = shellRunner.runAndOutput(networkSetupCommand, "-listallhardwareports");

        StringReader stringReader = new StringReader(scanningOutput);
        BufferedReader reader = new BufferedReader(stringReader);
        String airportDevice = "";
        try {
            String line;
            while((line = reader.readLine())!=null){
                if(line.contains("Wi-Fi")){
                    String deviceLine = reader.readLine();
                    airportDevice = deviceLine.split(":")[1].trim();
                    break;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return airportDevice;
    }

}
