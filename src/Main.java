import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.IntStream;

public class Main extends Application {
    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        GridPane gridPane = new GridPane();
        gridPane.setPrefSize(800, 800);
        Scene scene = new Scene(gridPane);
        primaryStage.setScene(scene);
        Rule rule = new SimpleRule();
        AtomicBoolean isBlack = new AtomicBoolean();

        Button[] buttons = IntStream.range(0, 64).mapToObj(i -> new Button()).toArray(Button[]::new);

        for (int r = 0; r < 8; ++r) {
            for (int c = 0; c < 8; ++c) {
                gridPane.add(buttons[r * 8 + c], r, c);
                buttons[r*8+c].setPrefSize(100, 100);
                buttons[r*8+c].setFocusTraversable(false);
                int fr = r;
                int fc = c;
                buttons[r*8+c].addEventHandler(MouseEvent.MOUSE_RELEASED, new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        boolean result = rule.setPoi(fr, fc, isBlack.get());
                        if (result) isBlack.set(!isBlack.get());
                    }
                });
            }
        }

        Function<Boolean, BiConsumer<Integer, Integer>> defaultOp = new Function<Boolean, BiConsumer<Integer, Integer>>() {
            @Override
            public BiConsumer<Integer, Integer> apply(Boolean aBoolean) {
                return (integer, integer2) -> buttons[integer * 8 + integer2].setBackground(new Background(
                        new BackgroundFill(
                                aBoolean ? new Color(1.0, 0, 0, 0.5)
                                : new Color(0, 1.0, 0, 0.5),
                        new CornerRadii(1), new Insets(1))));
            }
        };

        rule.setSize(8);

        rule.register(defaultOp);

        rule.init();
        rule.forcePoi(3, 3, true);
        rule.forcePoi(4, 4, true);
        rule.forcePoi(3, 4, false);
        rule.forcePoi(4, 3, false);

        primaryStage.show();
    }
}
