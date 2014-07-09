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
import com.datastax.ResourceLoader;
import com.datastax.gui.Output;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

@Component
public class Simulation {

    @Autowired
    private Output output;
    @Autowired
    private ResourceLoader resourceLoader;
    @Autowired
    private ShellRunner shellRunner;

    public void run() {
        output.reset();
        output.writeLn("Running Simulation");
        InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("simulator.jar");
        byte[] bytes = readAndClose(inputStream);
        write(bytes, "/tmp/simulator.jar");
        try {
            String[] args = new String[]{"java", "-jar", "/tmp/simulator.jar"};
            shellRunner.runAndOutput(args);
        }finally {
            try {
                Files.delete(Paths.get("/tmp/simulator.jar"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void interrupt() {
        output.writeLn("interrupting Simulation");
        shellRunner.destroyCurrentProcess();
    }

    /**
     Write a byte array to the given file.
     Writing binary data is significantly simpler than reading it.
     */
    void write(byte[] aInput, String aOutputFileName){
        try {
            OutputStream output = null;
            try {
                output = new BufferedOutputStream(new FileOutputStream(aOutputFileName));
                output.write(aInput);
            }
            finally {
                output.close();
            }
        }
        catch(Exception ex){
            throw new RuntimeException(ex);
        }
    }

    /**
     Read an input stream, and return it as a byte array.
     Sometimes the source of bytes is an input stream instead of a file.
     This implementation closes aInput after it's read.
     */
    byte[] readAndClose(InputStream aInput){
        //carries the data from input to output :
        byte[] bucket = new byte[32*1024];
        ByteArrayOutputStream result = null;
        try  {
            try {
                //Use buffering? No. Buffering avoids costly access to disk or network;
                //buffering to an in-memory stream makes no sense.
                result = new ByteArrayOutputStream(bucket.length);
                int bytesRead = 0;
                while(bytesRead != -1){
                    //aInput.read() returns -1, 0, or more :
                    bytesRead = aInput.read(bucket);
                    if(bytesRead > 0){
                        result.write(bucket, 0, bytesRead);
                    }
                }
            }
            finally {
                aInput.close();
                //result.close(); this is a no-operation for ByteArrayOutputStream
            }
        }
        catch (IOException ex){
            throw new RuntimeException(ex);
        }
        return result.toByteArray();
    }

}
