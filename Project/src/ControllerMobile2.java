import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;
import java.net.URL;

import org.json.JSONObject;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ProgressIndicator;

public class ControllerMobile2 {
    
    private String type = ControllerMobile1.getType();

    private int index = ControllerMobile1.getIndex();

    @FXML
    private AnchorPane info;

    @FXML
    private Rectangle color = new Rectangle();

    @FXML
    private void animateToMobile1(ActionEvent event) {
        UtilsViews.setViewAnimating("Mobile1");
    }

    public void setColor(String color) {
        Color paint = switch (color) {
            case "red" -> Color.RED;
            case "blue" -> Color.BLUE;
            case "green" -> Color.GREEN;
            case "orange" -> Color.ORANGE;
            case "brown" -> Color.BROWN;
            case "grey" -> Color.GREY;
            case "white" -> Color.WHITE;
            case "purple" -> Color.PURPLE;
            default -> Color.BLACK;
        };
        this.color.setFill(paint);
    }

    public void showInfo() {

        // Obtenir una referència a l'ojecte AppData que gestiona les dades
        AppData appData = AppData.getInstance();

        // Obtenir les dades de l'opció seleccionada
        JSONObject dades = appData.getItemData(type, index);

        // Carregar la plantilla
        URL resource = null;
        try {
            switch (type) {
                case "Consoles": resource = this.getClass().getResource("assets/template_consoles.fxml"); break;
                case "Jocs": resource = this.getClass().getResource("assets/template_jocs.fxml"); break;
                case "Personatges": resource = this.getClass().getResource("assets/template_personatges.fxml"); break;
            }
        } catch (Exception e) {
            System.out.println("ControllerDesktop: Error loading info.");
            System.out.println(e);
        }

        // Esborrar la informació actual
        info.getChildren().clear();

        // Carregar la llista amb les dades
        try {
            FXMLLoader loader = new FXMLLoader(resource);
            Parent itemTemplate = loader.load();
            ControllerInfoItem itemController = loader.getController();
            itemController.setImage("assets/images/" + dades.getString("imatge"));
            itemController.setTitle(dades.getString("nom"));
            switch (type) {
                case "Consoles": setColor(dades.getString("color"));
                                 itemController.setText(dades.getString("data") + "\n\n" +
                                                        dades.getString("procesador") + "\n\n" +
                                                        dades.getInt("venudes") + " venudes");
                                                        break;
                case "Jocs": itemController.setText(dades.getInt("any") + "\n\n" +
                                                    dades.getString("tipus") + "\n\n" +
                                                    dades.getString("descripcio"));
                                                    break;
                case "Personatges": setColor(dades.getString("color"));
                                    itemController.setText(dades.getString("nom_del_videojoc"));
                                                           break;
            }

            // Afegeix la informació a la vista
            info.getChildren().add(itemTemplate);

            // Estableix que la mida de itemTemplaate s'ajusti a la mida de info
            AnchorPane.setTopAnchor(itemTemplate, 0.0);
            AnchorPane.setRightAnchor(itemTemplate, 0.0);
            AnchorPane.setBottomAnchor(itemTemplate, 0.0);
            AnchorPane.setLeftAnchor(itemTemplate, 0.0);

        } catch (Exception e) {
            System.out.println("ControllerDesktop: Error showing info.");
            System.out.println(e);
        }
    }
}
