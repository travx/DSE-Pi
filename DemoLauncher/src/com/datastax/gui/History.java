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
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import org.springframework.stereotype.Component;


@Component
public class History {

    private final ScrollPane scrollPane;
    private final GridPane stackPane;
    private int index = 1000;

    public History() {
        scrollPane = new ScrollPane();
        stackPane = new GridPane();
        scrollPane.setContent(stackPane);
    }

    public Label add(final ProgressIndicator indicator, String startingText) {
        final Label label = new Label(startingText);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                GridPane sp = new GridPane();

                label.setMinWidth(350);
                sp.add(label, 0, 0);
                sp.add(indicator, 1, 0);
                stackPane.add(sp, 0, index);
                index--;

            }
        });
        return label;
    }

    public void addToPane(final GridPane pane) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                pane.add(new Label("Command History"), 0, 2, 4, 1);
                pane.add(scrollPane, 0, 3, 4, 1);
            }
        });
    }
}