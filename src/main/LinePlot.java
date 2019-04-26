package main;

import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;

public class LinePlot {

    public  static <T> void plot(Stage stage, T[] data, String dataType) {
        stage.setTitle("Line plot");
        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Generations");
        yAxis.setLabel(dataType);
        final LineChart<Number,Number> lineChart =
                new LineChart<Number,Number>(xAxis,yAxis);

        lineChart.setTitle(dataType+" over generations");
        lineChart.setCreateSymbols(false);

        XYChart.Series series = new XYChart.Series();
        series.setName(dataType);
        for (int i = 0; i < data.length; i++) {
            series.getData().add(new XYChart.Data(i, data[i]));
        }
        lineChart.getData().add(series);

        Scene scene  = new Scene(lineChart,800,600);

        stage.setScene(scene);
        stage.show();
    }

}
