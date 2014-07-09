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

import com.datastax.configuration.Configuration;
import com.datastax.gui.Output;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

@Component
public class Network {
    @Autowired
    private Configuration configuration;
    @Autowired
    private Output output;

    private void checkHost(String hostname) {

        InetAddress address;
        try {
            address = InetAddress.getByName(hostname);
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
        try {
            boolean reachable = address.isReachable(1000);
            if(!reachable){
                String messsage = "Unable to reach " +hostname + "\nMake sure it is on and configured with the correct hostname";
                throw new RuntimeException(messsage);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        output.writeLn("Found host " + hostname);
    }

    public void checkCassandraHosts() {
        for (String hostname: configuration.getCassandraNodes()) {
            checkHost(hostname);
        }
    }

    public void checkOpsCenter() {
         checkHost(configuration.getAppControlHost());
    }


}
