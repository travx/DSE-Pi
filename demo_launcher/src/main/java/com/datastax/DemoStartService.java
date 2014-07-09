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

package com.datastax;

import com.datastax.commands.*;
import com.datastax.gui.*;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DemoStartService extends Service<Void> {

    @Autowired
    private Network network;
    @Autowired
    private Simulation simulation;
    @Autowired
    private TomcatControl tomcatControl;
    @Autowired
    private CassandraControl cassandraControl;
    @Autowired
    private VmControl vmControl;
    @Autowired
    private Progress progress;
    @Autowired
    private History history;
    @Autowired
    private WifiControl wifiControl;
    @Autowired
    private ExceptionWriter exceptionWriter;
    @Autowired
    private Output output;
    @Autowired
    private MainWindow window;

    private boolean tomcatRunning = false;
    private boolean cassandraRunning = false;
    private boolean vmRunning = false;
    private boolean simulationRunning = false;

    public boolean startProcess(){
        if(!execute(new Runnable() {
            public void run() {
                wifiControl.verify();
            }
        },"Verifying Wifi")) {
            return false;
        }
        if(!execute(new Runnable(){
            public void run() {
                network.checkCassandraHosts();
            }
        },"Checking cassandra hosts"
        )){return false;}
        if(!execute(new Runnable(){
                        public void run() {
                            cassandraControl.start();
                        }
                    },"Starting cassandra"
        )){return false;}
        cassandraRunning = true;
        if(!execute(new Runnable(){
                    public void run() {
                        vmControl.start();
                    }
                },"Starting OpsCenter VM.."
        )){return false;}
        vmRunning = true;
        if(!execute(new Runnable(){
                    public void run() {
                        network.checkOpsCenter();
                    }
                },"Checking for vm ip.."
        )){return false;}
        if(!execute(new Runnable(){
                    public void run() {
                        tomcatControl.start();
                    }
                },"Tomcat initialization.."
        )){return false;}
        tomcatRunning = true;
        window.enableSimulator();
        return true;
    }

    @Override
    protected Task createTask() {
        return new Task<Void>(){

            @Override
            protected Void call() throws Exception {
                if(!startProcess()) {
                    output.writeLn("Demo did not finish successfully");
                }else {
                    output.writeLn("Demo is complete");
                }
                return null;
            }
        };
    }


    public void endProcess(){
        if(tomcatRunning){
            execute(new Runnable(){
                        public void run() {
                            tomcatControl.stop();
                        }
                    },"Stopping Tomcat..."
            );
        }
        if(cassandraRunning) {
            execute(new Runnable() {
                        public void run() {
                            cassandraControl.stop();
                        }
                    }, "Stopping Cassandra..."
            );
        }
        if(vmRunning) {
            execute(new Runnable() {
                        public void run() {
                            vmControl.suspend();
                        }
                    }, "Suspending OpsCenter VM.."
            );
        }
    }

    private boolean execute(Runnable runnable, String text){
        ProgressWrapper progressWrapper = progress.runProgress(history, text);
        try {
            runnable.run();
            progressWrapper.complete();
            Thread.sleep(100);
            return true;
        }catch(Exception ex){
            progressWrapper.fail(text);
            exceptionWriter.writeExceptionToOutput(ex);
            return false;
        }
    }

    public void runSimulation() {
        if(simulationRunning){
            simulationRunning = false;
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if(!execute(new Runnable(){
                            public void run() {
                                simulation.interrupt();
                            }
                        },"Stopping Simulation.."
            )){};
        }else{
            simulationRunning = true;
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if(!execute(new Runnable(){
                            public void run() {
                                simulation.run();
                            }
                        },"Running Simulation.."
            )){};
        }


    }
}
