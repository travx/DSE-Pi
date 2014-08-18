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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class VmControl {

    @Autowired
    private Output output;
    @Autowired
    private Configuration configuration;
    @Autowired
    private ShellRunner shellRunner;

    public void start() {
        output.reset();
        output.writeLn("Starting VM");
        operateOnVm("start");
    }

    public void suspend() {
        output.reset();
        output.writeLn("Suspending VM");
        operateOnVm("suspend");
    }

    private void operateOnVm(final String command) {
        String vmrun = "/Applications/VMware Fusion.app/Contents/Library/vmrun";
        String[] args = new String[]{
                vmrun, "-T",
                "ws",
                command,
                configuration.getVmLocation()
        };
        shellRunner.runAndOutput(args);
    }

}
