/*
 * Copyright (C) 2014 DataStax
 *
 *
 *     Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 *     documentation files (the "Software"), to deal in the Software without restriction, including without limitation
 *     the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and
 *     to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 *     The above copyright notice and this permission notice shall be included in all copies or substantial portions of
 *     the Software.
 *
 *     THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO
 *     THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *     AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF
 *     CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS
 *     IN THE SOFTWARE.
 */

package com.datastax.gui;

import com.datastax.DemoStartService;
import com.datastax.ExceptionWriter;
import com.datastax.ResourceLoader;
import com.datastax.SimulationService;
import com.datastax.commands.Simulation;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.URL;
import java.util.concurrent.ExecutorService;

/**
 * primary start into most of the custom application logic
 */
public class MainWindow {
    private ToggleButton simulatorButton;
    private Stage stage;

    @Autowired
    private ResourceLoader loader;
    @Autowired
    private DemoStartService commandRunner;
    @Autowired
    private Output output;
    @Autowired
    private History history;
    @Autowired
    private ExceptionWriter exceptionWriter;

    public void setStage(Stage stage){
        this.stage = stage;
    }

    private void setStyleSheet(Scene scene) {
        URL resource = loader.getGlobalResource("app.css");
        ObservableList<String> stylesheets = scene.getStylesheets();
        stylesheets.add(resource.toExternalForm());
    }

    private GridPane createLayoutPane() {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        return grid;
    }

    private Scene createScene(Parent grid) {
        Scene scene = new Scene(grid, 500, 400);
        stage.setScene(scene);
        stage.setResizable(false);
        return scene;
    }

    /**
     * Shows the main form.
     */
    public void show() {
        //TODO: make configuration setting for debug mode
        exceptionWriter.setDebugMode(false);

        GridPane pane = createLayoutPane();
        output.addToPane(pane);
        history.addToPane(pane);
        simulatorButton = createSimulatorButton();
        pane.add(simulatorButton, 0,1,4,1);


        Scene scene = createScene(pane);
        setStyleSheet(scene);
        Text sceneTitle = new Text("DataStax Demo");
        sceneTitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        pane.add(sceneTitle, 0, 0, 4, 1);
        stage.show();
        commandRunner.setOnFailed(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent workerStateEvent) {
                output.writeLn("demo failed!");
                exceptionWriter.writeExceptionToOutput(workerStateEvent.getSource().getException());
            }
        });
        commandRunner.setOnScheduled(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent workerStateEvent) {
                output.writeLn("demo process starting");
            }
        });
        commandRunner.start();
    }

    private ToggleButton createSimulatorButton() {

                String simulatorText = "Simulator";
        simulatorButton = new ToggleButton(simulatorText);
        final SimulationService service = new SimulationService(commandRunner);
        simulatorButton.setVisible(false);
        simulatorButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if(simulatorButton.isSelected()) {
                    service.start();
                }else{
                    service.cancel();
                }
            }
        });
        return simulatorButton;
    }


    public void close(){
        commandRunner.endProcess();
    }

    public void enableSimulator() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                simulatorButton.setVisible(true);
            }
        });
    }
}
