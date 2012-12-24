package progresssample;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class ProgressSample extends Application {
    private ExecutorService service;
    private Task<String> task1;
    private Task<String> task2;
    
    @Override
    public void start(Stage primaryStage) { 
        service = Executors.newFixedThreadPool(2);
        VBox vBox = new VBox();
        vBox.setSpacing(5);
        vBox.setAlignment(Pos.CENTER);
        final Scene scene = new Scene(vBox, 500, 250);
        // ★1行目
        HBox hBox1 = new HBox();
        hBox1.setSpacing(5);
        Text title1 = new Text("Task1");
        final Button btn1 = new Button("start");
        final Button btnC1 = new Button("cancel");
        btnC1.setDisable(true);
        final ProgressBar bar1 = new ProgressBar(0);
        final Text text1 = new Text("ready");
        text1.setWrappingWidth(130);
        hBox1.setAlignment(Pos.CENTER);
        hBox1.getChildren().addAll(title1, btn1, bar1, text1, btnC1);
        btn1.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                btn1.setDisable(true);
                btnC1.setDisable(false);
                task1 = new ProgressTask("Task1", 10);
                bar1.progressProperty().bind(task1.progressProperty());
                text1.textProperty().bind(task1.messageProperty());
                service.submit(task1);
                task1.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED, new EventHandler<WorkerStateEvent>() {
                    @Override
                    public void handle(WorkerStateEvent t) {
                        btn1.setDisable(false);
                        btnC1.setDisable(true);
                    }
                });
                task1.addEventHandler(WorkerStateEvent.WORKER_STATE_CANCELLED, new EventHandler<WorkerStateEvent>() {
                    @Override
                    public void handle(WorkerStateEvent t) {
                        btn1.setDisable(false);
                        btnC1.setDisable(true);
                    }
                });
            }
        });
        btnC1.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                task1.cancel();
            }
        });
        
        // ★2行目
        HBox hBox2 = new HBox();
        hBox2.setSpacing(5);
        Text title2 = new Text("Task2");
        final Button btn2 = new Button("start");
        final Button btnC2 = new Button("cancel");
        btnC2.setDisable(true);
        final ProgressBar bar2 = new ProgressBar(0);
        final Text text2 = new Text("ready");
        text2.setWrappingWidth(130);
        hBox2.setAlignment(Pos.CENTER);
        hBox2.getChildren().addAll(title2, btn2, bar2, text2, btnC2);
        btn2.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                btn2.setDisable(true);
                btnC2.setDisable(false);
                task2 = new ProgressTask("Task2", 20);
                bar2.progressProperty().bind(task2.progressProperty());
                text2.textProperty().bind(task2.messageProperty());                        
                service.submit(task2);
                task2.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED, new EventHandler<WorkerStateEvent>() {
                    @Override
                    public void handle(WorkerStateEvent t) {
                        btn2.setDisable(false);
                        btnC2.setDisable(true);
                    }
                });
                task2.addEventHandler(WorkerStateEvent.WORKER_STATE_CANCELLED, new EventHandler<WorkerStateEvent>() {
                    @Override
                    public void handle(WorkerStateEvent t) {
                        btn2.setDisable(false);
                        btnC2.setDisable(true);
                    }
                });
            }
        });
        btnC2.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                task2.cancel();
            }
        });
        // start
        vBox.getChildren().add(hBox1);
        vBox.getChildren().add(hBox2);
        primaryStage.setTitle("Progress Sample");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String... args) {
        launch(args);
    }

    @Override
    public void stop() throws Exception {
        shutdownService();
        super.stop();
    }

    private void shutdownService() {
        if (task1 != null && task1.isRunning()) {
            task1.cancel();                   
        }
        if (task2 != null && task2.isRunning()) {
            task2.cancel();
        }
        if (service != null && !service.isShutdown()) {
            service.shutdownNow();
            System.out.println("shutdown service");
        }
    }
    
}
