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

import com.datastax.gui.Output;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;

@Component
public class ShellRunner {

    @Autowired
    private Output output;
    private Process currentProcess;

    public void destroyCurrentProcess(){

        if(currentProcess!=null){
            currentProcess.destroy();
        };
    }

    public String runAndOutput(String... args){
        FunctionObject<String> exec = new FunctionObject<String>() {
            @Override
            public void exec(String s) {
                output.writeLn(s);
            }
        };
        return writeTo(exec, args);
    }

    private String writeTo(FunctionObject<String> functionObject, String[] args) {
        InputStream inputStream = null;
        BufferedReader stream = null;

        InputStream errorInputStream = null;
        BufferedReader errorStream = null;
        String messageOutput = "";

        try {
            ProcessBuilder processBuilder = new ProcessBuilder(args);
            currentProcess = processBuilder.start();
            inputStream = currentProcess.getInputStream();
            stream = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while((line = stream.readLine())!=null){
                functionObject.exec(line);
                messageOutput += line + "\n";
            }
            if(currentProcess.waitFor()>0){
                errorInputStream = currentProcess.getErrorStream();
                errorStream = new BufferedReader(new InputStreamReader(errorInputStream));
                StringWriter writer = new StringWriter();
                while((line = errorStream.readLine())!=null){
                    writer.write(line);
                }
                throw new RuntimeException(writer.toString());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }finally{
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (stream != null) {
                    stream.close();
                }
                if (errorInputStream != null) {
                    errorInputStream.close();
                }
                if (errorStream != null) {
                    errorStream.close();
                }
            }catch (IOException ex){
                throw new RuntimeException(ex);
            }
            return messageOutput;
        }
    }
}
