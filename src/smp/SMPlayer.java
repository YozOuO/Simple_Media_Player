package smp;

import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.value.*;
import javafx.event.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.media.*;
import javafx.scene.paint.Color;
import javafx.scene.layout.*;
import javafx.scene.input.*;
import javafx.stage.Stage;
import javafx.animation.*;
import javafx.util.*;
import java.io.*;
import java.util.*;
/**
 *
 * @author NUTN
 */
public class SMPlayer extends Application {
    boolean mousePress = false;
    
   
    
    @Override
    public void start(Stage primaryStage) {
    	
    	 int num = 0;
    	 File folder = new File("./MP4");
    	 String allList = "Total Video List: \n";
    	 String[] temp_list = folder.list();
    	 ArrayList<String> true_list = new ArrayList();    	
    	 ArrayList<String> mp4_list = new ArrayList();
    	 for(int i = 0;i<temp_list.length;i++)
    	 {
    		 System.out.println(temp_list[i]);
    		 if(temp_list[i].toString().contains(".mp4"))
    		 {
    			 mp4_list.add(("file:///./" + temp_list[i]));
    			 true_list.add(temp_list[i]);
    			 allList = allList + temp_list[i] + "\n";
    		 }
    	 }
    	
        StackPane root = new StackPane();
        Media media = new Media(mp4_list.get(num));
        
        
        MediaPlayer player = new MediaPlayer(media);
        MediaView view = new MediaView(player);
        
        VBox vbox = new VBox();
        Slider slider = new Slider();
        vbox.getChildren().add(slider);
        
        /*
        VBox vbox = new VBox();
        Slider slider = new Slider();
        
        /*Timeline slideIn = new Timeline();
        Timeline slideOut = new Timeline();
        root.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                slideOut.play();
            }
        });
        root.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                slideIn.play();
            }
        });*/
        
        
        view.fitWidthProperty().bind(Bindings.selectDouble(view.sceneProperty(), "width"));
        view.fitHeightProperty().bind(Bindings.selectDouble(view.sceneProperty(), "height"));
        view.setPreserveRatio(true);
        GridPane grid = new GridPane();
        ScrollPane listPane = new ScrollPane();
        grid.setVgap(52);
        grid.setHgap(100);
        
        Button btn_PLAY = new Button("Play");
        btn_PLAY.setMinWidth(100);
        btn_PLAY.setOnAction((ActionEvent event)->{
            if(player.getStatus()!=MediaPlayer.Status.PLAYING)
            {
                player.setCycleCount(MediaPlayer.INDEFINITE);
                player.play();
            }
        });
        Button btn_STOP = new Button("Stop");
        btn_STOP.setMinWidth(100);
        btn_STOP.setOnAction((ActionEvent event)-> {
            player.stop();
        });
        Button btn_PAUSE = new Button("Pause");
        btn_PAUSE.setMinWidth(100);
        btn_PAUSE.setOnAction((ActionEvent event)->{
            player.pause();
        });
        
        TextArea viewList = new TextArea();
        TextArea nowPlay = new TextArea();
        viewList.setMaxHeight(10);
        viewList.setMinWidth(200);
        nowPlay.setMaxHeight(10);
        nowPlay.setMinWidth(200);
        viewList.setText(allList);
        nowPlay.setText("Now Playing: \n" + true_list[num]);
        
        Button btn_NEXT = new Button("Next");
        btn_NEXT.setMinWidth(100);
        btn_NEXT.setOnAction((ActionEvent event)->{
            //num = num+1;
            nowPlay.setText("Now Playing: " + temp_list[1]);
            
        });
        
        grid.add(viewList,0,0);
        grid.add(nowPlay,7,0);	
        grid.add(btn_PLAY, 3,16);
        grid.add(btn_STOP, 4,16);
        grid.add(btn_PAUSE,5,16);
        grid.add(btn_NEXT, 4, 0);
        
        listPane.setMaxSize(100, 100);
        listPane.setContent(viewList);
        
        //listPane.add(view_Lbl,0,0);
        
        root.getChildren().add(view);
        //root.getChildren().add(listPane);
        root.getChildren().add(grid);
        root.getChildren().add(vbox);
        
        
        Scene scene = new Scene(root,1400,950,Color.BLACK);
        player.play();
        player.setOnReady(new Runnable(){
            @Override
            public void run()
            {
                int w = player.getMedia().getWidth();
                int h = player.getMedia().getHeight();
                
                

                primaryStage.setMinWidth(w);
                primaryStage.setMinHeight(h+200);
                primaryStage.setTitle("MyPlayer");
                primaryStage.setScene(scene);
                
                primaryStage.show();
                vbox.setMinSize(w, h);
                vbox.setTranslateY(scene.getHeight()-30);
                
                slider.setMin(0.0);
                slider.setValue(0.0);
                slider.setMax(player.getTotalDuration().toSeconds());
            }
        });
               
                
        
        
        player.currentTimeProperty().addListener(new ChangeListener<Duration>() {
            @Override
            public void changed(ObservableValue<? extends Duration> observableValue, Duration duration, Duration current) {
                slider.setValue(current.toSeconds());
            }
        });
        slider.maxProperty().bind(Bindings.createDoubleBinding(() -> player.getTotalDuration().toSeconds(),player.totalDurationProperty()));
        /*slider.setOnMousePressed(new EventHandler<MouseEvent>(){
            player.pause();
        });*/
        slider.setOnMouseDragged(new EventHandler<MouseEvent>() {
            
            @Override
            public void handle(MouseEvent mouseEvent) {
                
                player.seek(Duration.seconds(slider.getValue()));
                
            }
            
        });
        slider.setOnMousePressed(new EventHandler<MouseEvent>() {
            
            @Override
            public void handle(MouseEvent mouseEvent) {
                
                player.seek(Duration.seconds(slider.getValue()));
                
            }
            
        });
        
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}