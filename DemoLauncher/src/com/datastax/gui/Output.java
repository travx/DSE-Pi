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
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import org.springframework.stereotype.Component;

@Component
public class Output {
    private final TextArea textArea;
    private final Label label;

    public Output() {
        textArea = new TextArea();
        textArea.setEditable(false);
        textArea.setWrapText(true);
        textArea.textProperty().addListener(new ChangeListener<Object>() {
            @Override
            public void changed(ObservableValue<?> observable, Object oldValue,
                                Object newValue) {
                textArea.setScrollTop(Double.MIN_VALUE); //this will scroll to the bottom
                //use Double.MAX_VALUE to scroll to the top
            }
        });
        label = new Label("Output");
    }

    public void addToPane(GridPane pane) {
        pane.add(label, 0, 4, 4, 1);
        pane.add(textArea, 0, 5, 4, 1);
    }

    public void writeLn(final String message) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                textArea.appendText("\n");
                textArea.appendText(message);
                textArea.appendText("\n");
            }
        });

    }

    public void reset() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                textArea.setText("");
            }
        });
    }
}
