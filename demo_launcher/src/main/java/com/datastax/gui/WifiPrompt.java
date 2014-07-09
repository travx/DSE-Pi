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

import com.datastax.ResourceLoader;
import com.datastax.commands.WifiControl;
import com.datastax.configuration.Configuration;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Component
public class WifiPrompt {

    private final List<MouseEvent> clicks = new ArrayList<>();
    private final List<String> failures = new ArrayList<>();
    final Stage stage = new Stage();

    @Autowired
    private WifiControl control;
    @Autowired
    private Configuration configuration;
    @Autowired
    private ResourceLoader loader;

    public List<MouseEvent> getClicks(){
        return this.clicks;
    }
    public List<String> getFailures() { return this.failures; }
    public void show(){

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(25, 25, 25, 25));
        grid.setHgap(10);
        grid.setVgap(10);
        final String ssid = configuration.getWifiCreds().getSSID();
        if(ssid==null){
            throw new RuntimeException("critical error cluster network SSID cannot be null");
        }
        Text text = new Text("You're current configured network is not " + ssid + ".\nWould you like to change it?");
        grid.add(text, 0, 0,3,2);
        final Button yesButton = new Button("Yes");
        yesButton.getStyleClass().add("green");
        yesButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                control.switchToClusterNetwork();
                clicks.add(mouseEvent);
                stage.close();

            }
        });
        final Button noButton = new Button("No");
        noButton.setCancelButton(true);
        noButton.getStyleClass().add("red");

        noButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(final MouseEvent mouseEvent) {
                Platform.runLater(new Runnable() {
                                      @Override
                                      public void run() {
                                          clicks.add(mouseEvent);
                                          yesButton.setDisable(true);
                                          noButton.setDisable(true);
                                          failures.add("demo cannot proceed without being connected to the "+ ssid + " please switch and try again");
                                      }
                                  });
            }
        });
        grid.add(yesButton, 0, 3,1,1);
        grid.add(noButton, 2, 3,1,1);
        Scene modal = new Scene(grid);
        URL resource = loader.getGlobalResource("app.css");
        ObservableList<String> stylesheets = modal.getStylesheets();
        stylesheets.add(resource.toExternalForm());
        stage.setScene(modal);
        stage.show();
    }
    public void close(){
        stage.close();
    }
}
