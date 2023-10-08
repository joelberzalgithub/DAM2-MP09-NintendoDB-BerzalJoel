import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;

import org.json.JSONArray;
import org.json.JSONObject;
import javafx.scene.control.Label;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ProgressIndicator;

public class ControllerDesktop implements Initializable {

    @FXML
    private ChoiceBox<String> choiceBox;

    @FXML
    private VBox yPane;

    @FXML
    private AnchorPane info;

    @FXML
    private Rectangle color = new Rectangle();

    String opcions[] = { "Personatges", "Jocs", "Consoles" };

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Afegeix les opcions al ChoiceBox
        choiceBox.getItems().addAll(opcions);
        // Selecciona la primera opció
        choiceBox.setValue(opcions[0]);
        // Callback que s’executa quan l’usuari escull una opció
        choiceBox.setOnAction((event) -> { loadList(); });
        // Carregar automàticament les dades de ‘Personatges’
        loadList();
    }

    public void loadList() {

        // Obtenir l'opció seleccionada
        String opcio = choiceBox.getValue();

        // Obtenir una referència a AppData que gestiona les dades
        AppData appData = AppData.getInstance();
        
        // Mostrar el missatge de càrrega
        showLoading();
        
        // Demanar les dades
        appData.load(opcio, (result) -> {
            if (result == null) {
                System.out.println("ControllerDesktop: Error loading data.");
            } else {
                // Cal afegir el try/catch a la crida de ‘showList’
                try {
                    showList();
                } catch (Exception e) {
                    System.out.println("ControllerDesktop: Error showing list.");
                }
            }
        });
    }

    public void showList() throws Exception {

        String opcioSeleccionada = choiceBox.getValue();

        // Obtenir una referència a l'ojecte AppData que gestiona les dades
        AppData appData = AppData.getInstance();

        // Obtenir les dades de l'opció seleccionada
        JSONArray dades = appData.getData(opcioSeleccionada);

        // Carregar la plantilla
        URL resource = this.getClass().getResource("assets/template_list_item.fxml");

        // Esborrar la llista actual
        yPane.getChildren().clear();

        // Carregar la llista amb les dades
        for (int i = 0; i < dades.length(); i++) {
            JSONObject consoleObject = dades.getJSONObject(i);

            if (consoleObject.has("nom")) {
                String nom = consoleObject.getString("nom");
                String imatge = "assets/images/" + consoleObject.getString("imatge");
                FXMLLoader loader = new FXMLLoader(resource);
                Parent itemTemplate = loader.load();
                ControllerListItem itemController = loader.getController();
                itemController.setText(nom);
                itemController.setImage(imatge);

                // Defineix el callback que s'executarà quan l'usuari seleccioni un element
                // (cal passar final perquè es pugui accedir des del callback)
                final String type = opcioSeleccionada;
                final int index = i;
                itemTemplate.setOnMouseClicked(event -> {
                    showInfo(type, index);
                });

                yPane.getChildren().add(itemTemplate);
            }
        }
    }

    // Funció ‘showLoading’, mostrar una càrrega

    public void showLoading() {

        // Esborrar la llista actual
        yPane.getChildren().clear();

        // Afegeix un indicador de progrés com a primer element de la llista
        ProgressIndicator progressIndicator = new ProgressIndicator();
        yPane.getChildren().add(progressIndicator);
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

    void showInfo(String type, int index) {

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
