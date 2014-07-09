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

package com.datastax.gui;

import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;

public class ProgressWrapper {
    private final ProgressIndicator indicator;
    private final Label label;

    public ProgressWrapper(ProgressIndicator indicator, Label label) {
        this.indicator = indicator;
        this.label = label;
    }

    public void complete() {

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                String startingText = label.getText();
                label.setText(startingText + " completed");
                indicator.setProgress(1);
            }
        });
    }

    public void fail(final String startingText) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                label.setText(startingText + " failed!");
                indicator.setProgress(0);
            }
        });
    }
}