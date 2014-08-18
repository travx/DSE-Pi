#!/bin/bash


# get interface
AIRPORT=`networksetup -listallhardwareports | grep -A2 Wi-Fi | grep Device | sed -e 's/^.*://'`
WIFI_NETWORK_NAME="dse_pi"
WIFI_PASSWORD="26298146"

networksetup -setairportpower $AIRPORT off
networksetup -setairportpower $AIRPORT on
sleep 2

if networksetup -getairportnetwork $AIRPORT | grep -i -a $WIFI_NETWORK_NAME ;
then
    echo 'Connected to DataStax Demo Network';
    exit 0
fi

if networksetup -setairportnetwork $AIRPORT $WIFI_NETWORK_NAME $WIFI_PASSWORD | grep -i -a "Failed" ;
then
    echo 'Failed to connect, just restarting...';
    networksetup -setairportpower $AIRPORT off
    networksetup -setairportpower $AIRPORT on
    sleep 1
fi

networksetup -getairportnetwork $AIRPORT



exit 0;
