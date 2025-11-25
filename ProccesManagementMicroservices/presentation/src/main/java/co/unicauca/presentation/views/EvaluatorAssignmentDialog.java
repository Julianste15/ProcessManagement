package co.unicauca.presentation.views;

import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;
import java.util.List;
import java.util.Map;

public class EvaluatorAssignmentDialog extends Dialog<Pair<String, String>> {

    public EvaluatorAssignmentDialog(List<Map<String, Object>> teachers) {
        this.setTitle("Asignar Evaluadores");
        this.setHeaderText("Seleccione dos evaluadores diferentes");

        ButtonType assignButtonType = new ButtonType("Asignar", ButtonBar.ButtonData.OK_DONE);
        this.getDialogPane().getButtonTypes().addAll(assignButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        ComboBox<String> evaluator1Combo = new ComboBox<>();
        ComboBox<String> evaluator2Combo = new ComboBox<>();

        for (Map<String, Object> teacher : teachers) {
            String email = (String) teacher.get("email");
            String name = teacher.get("names") + " " + teacher.get("surnames");
            String item = email + " - " + name;
            evaluator1Combo.getItems().add(item);
            evaluator2Combo.getItems().add(item);
        }

        grid.add(new Label("Evaluador 1:"), 0, 0);
        grid.add(evaluator1Combo, 1, 0);
        grid.add(new Label("Evaluador 2:"), 0, 1);
        grid.add(evaluator2Combo, 1, 1);

        this.getDialogPane().setContent(grid);

        this.setResultConverter(dialogButton -> {
            if (dialogButton == assignButtonType) {
                String e1 = evaluator1Combo.getValue();
                String e2 = evaluator2Combo.getValue();
                if (e1 != null && e2 != null) {
                    return new Pair<>(e1.split(" - ")[0], e2.split(" - ")[0]);
                }
            }
            return null;
        });
    }
}
