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
import com.datastax.security.Credentials;
import com.datastax.gui.Output;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.InputStream;

@Component
public class SSHControl {
    private JSch jsch=new JSch();
    @Autowired
    private Configuration configuration;
    @Autowired
    private Output output;

    public void execOnHost(String command, String host){
        Credentials serverCreds = configuration.getServerCreds();
        Session session= null;
        ChannelExec channel = null;
        try {
            session = jsch.getSession(
                    serverCreds.getUsername(),
                    host
            );
            jsch.setConfig("StrictHostKeyChecking", "no");
            session.setPassword(serverCreds.getPassword());
            session.connect();
            channel=(ChannelExec)session.openChannel("exec");
            channel.setInputStream(null);
            channel.setErrStream(System.err);
            channel.setCommand(command);
            InputStream in=channel.getInputStream();

            channel.connect();
            byte[] tmp=new byte[1024];
            while(true){
                while(in.available()>0){
                    int i=in.read(tmp, 0, 1024);
                    if(i<0)break;
                    output.writeLn(new String(tmp, 0, i));
                }
                if(channel.isClosed()){
                    if(in.available()>0) continue;
                    int exitStatus = channel.getExitStatus();
                    if(exitStatus>0) {
                        String message = "unable to run " + command + ". error code: " + exitStatus;
                        output.writeLn(message);

                    }else {
                        output.writeLn(command + " completed.");
                    }
                    break;
                }
                try{Thread.sleep(5000);}catch(Exception ee){throw new RuntimeException(ee);}
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }finally {
            if (channel != null) {
                channel.disconnect();
            }
            if (session != null) {
                session.disconnect();
            }
        }
    }
}
