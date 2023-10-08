import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;

import org.json.JSONArray;
import org.json.JSONObject;
import javafx.scene.control.Label;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ProgressIndicator;

public class ControllerMobile1 implements Initializable {

    public static String type;

    public static int index;

    public static String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public static int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    @FXML
    private Label title;
    
    @FXML
    private VBox yPane;

    @FXML
    private AnchorPane info;

    @FXML
    private void animateToMobile0(ActionEvent event) {
        UtilsViews.setViewAnimating("Mobile0");
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        title.setText(ControllerMobile0.getOpcio());
    }

    public void loadList() {

        // Obtenir l'opció seleccionada
        String opcio = ControllerMobile0.getOpcio();

        // Obtenir una referència a AppData que gestiona les dades
        AppData appData = AppData.getInstance();
        
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

        String opcioSeleccionada = ControllerMobile0.getOpcio();

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
                setType(opcioSeleccionada);
                setIndex(i);
                itemTemplate.setOnMouseClicked(event -> {
                    UtilsViews.setViewAnimating("Mobile2");
                });

                yPane.getChildren().add(itemTemplate);
            }
        }
    }
}
