package main;

import javafx.scene.Scene;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;
import javafx.scene.chart.ScatterChart;
import javafx.scene.Node;

import java.util.Set;

public class ScatterPlot {

    public static void plot(Stage stage, int[][] points, int categories) {
        categories++;

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
                seriesArray[points[i][j]].getData().add(new XYChart.Data(j+0.5, i+0.5));
            }
        }

        for (int i = 0; i < categories; i++) {
            scatterChart.getData().addAll(seriesArray[i]);
        }

        Scene scene = new Scene(scatterChart, 500, 500);
        stage.setTitle("Scatter Chart");
        stage.setScene(scene);
        stage.show();

        int[][] randomIndexes = new int[3][categories];
        for (int i = 0; i < categories; i++) {
            randomIndexes[0][i] = i;
            randomIndexes[1][i] = i;
            randomIndexes[2][i] = i;
        }
        for (int i = 0; i < categories; i++) {
            int a = randomIndexes[0][i];
            int b = randomIndexes[1][i];
            int c = randomIndexes[2][i];
            int index = Rand.r.nextInt(categories);
            int index2 = Rand.r.nextInt(categories);
            int index3 = Rand.r.nextInt(categories);
            randomIndexes[0][i] = randomIndexes[0][index];
            randomIndexes[1][i] = randomIndexes[1][index];
            randomIndexes[2][i] = randomIndexes[2][index];
            randomIndexes[0][index] = a;
            randomIndexes[1][index2] = b;
            randomIndexes[2][index3] = c;
        }

        int size = (int)Math.round(240/(double)Start.latticeSize);
        for (int i = 0; i < categories; i++) {
            int color = 255/categories*randomIndexes[0][i];
            int color2 = 255/categories*randomIndexes[1][i];
            int color3 = 255/categories*randomIndexes[2][i];
            String c = "rgb("+color+","+color2+","+color3+")";
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
