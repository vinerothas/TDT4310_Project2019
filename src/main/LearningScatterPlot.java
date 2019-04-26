package main;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;

import java.util.Set;

public class LearningScatterPlot {

    public static void plot(Stage stage) {
        int categories = 255;

        NumberAxis xAxis = new NumberAxis(0, Start.latticeSize, 0);
        xAxis.setLabel("x");
        NumberAxis yAxis = new NumberAxis(0, Start.latticeSize, 0);
        yAxis.setLabel("y");

        ScatterChart<String, Number> scatterChart =
                new ScatterChart(xAxis, yAxis);

        scatterChart.setMinHeight(1000);
        scatterChart.setMinWidth(1000);
        scatterChart.setLegendVisible(false);

        XYChart.Series[] seriesArray = new XYChart.Series[categories];
        for (int i = 0; i < categories; i++) {
            seriesArray[i] = new XYChart.Series();
        }

        for (int i = 0; i < Start.latticeSize; i++) {
            for (int j = 0; j < Start.latticeSize; j++) {
                int color = (int)Math.round(Start.lattice[i][j].learningAbility*(categories-1));
                seriesArray[color].getData().add(new XYChart.Data(j+0.5, i+0.5));
            }
        }

        for (int i = 0; i < categories; i++) {
            scatterChart.getData().addAll(seriesArray[i]);
        }

        Scene scene = new Scene(scatterChart, 500, 500);
        stage.setTitle("Scatter Chart");
        stage.setScene(scene);
        stage.show();


        int size = (int)Math.round(240/(double)Start.latticeSize);
        for (int i = 0; i < categories; i++) {
            String c = "rgb("+i+","+i+","+i+")";
            Set<Node> nodes = scatterChart.lookupAll(".series" + i);
            for (Node n : nodes) {
                n.setStyle("-fx-background-color: "+c+","+c+";\n"
                        + "    -fx-background-insets: 0, 0;\n"
                        + "    -fx-background-radius: "+size+"px;\n"
                        + "    -fx-padding: "+size+"px;"
                        +"  -fx-shape: \"M0,0 L0,1 L1,1 L1,0 Z\";");
            }
        }

    }

}
