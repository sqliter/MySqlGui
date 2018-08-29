package ui;

import javafx.geometry.Insets;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polyline;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import java.util.ArrayList;
import java.util.List;

public class PathTitle extends Pane {
    private List<String> titles;
    public PathTitle() {
        titles = new ArrayList<>();
        //this.setFillHeight(true);
        //this.setMinHeight(32);
        //this.setSpacing(3.0);
        this.setHeight(32.0);
        this.setPadding(new Insets(3.0,10.0,3.0,10));
        //this.setStyle("-fx-background-color: #6a6a6a");
    }
    public void setTitles(List<String> ts) {
        titles.clear();
        titles.addAll(ts);
        updateUI();
    }
    public List<String> getTitles() {
        return titles;
    }
    public void updateUI() {
        double x=6.0;
        double y=6.0;
        double width=0;
        //double height=24.0;

        this.getChildren().clear();

        for(String title:titles){
            Text titleText = new Text(x,y+12.0,title);
            //titleText.setFill(Color.DARKGRAY);
            //titleText.setTextOrigin(VPos.BOTTOM);

            //titleText.setStrokeWidth(3.0);
            titleText.setTextAlignment(TextAlignment.CENTER);
            width=titleText.getLayoutBounds().getWidth();
            x = x+width+6.0;
            Polyline pl = new Polyline();
            //pl.setStrokeLineJoin(StrokeLineJoin.ROUND);
            pl.setStroke(Color.DARKGRAY);
            //pl.setStrokeType(StrokeType.OUTSIDE);
            //pl.setStrokeWidth(3.0);
            pl.getPoints().addAll(new Double[]{
                    x,y+4,
                    x+3.0,y+8.0,
                    x,y+12.0
            });
//                Polyline pl2 = new Polyline();
//                //pl2.setStrokeLineJoin(StrokeLineJoin.ROUND);
//                pl2.setStroke(Color.DARKGRAY);
//               // pl2.setStrokeType(StrokeType.INSIDE);
//                pl2.getPoints().addAll(new Double[] {
//                   x+0.5,y+1,
//                   x+4.5,y+8,
//                   x+0.5,y+15
//                });
            x += 10.0;
            this.getChildren().add(titleText);
            this.getChildren().add(pl);
            //this.getChildren().add(pl2);
        }
    }
}
