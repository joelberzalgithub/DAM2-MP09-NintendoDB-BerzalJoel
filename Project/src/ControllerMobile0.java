import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class ControllerMobile0 {

    public static String opcio;

    public static String getOpcio() {
        return opcio;
    }

    public void setOpcio(String opcio) {
        this.opcio = opcio;
    }

    @FXML
    private void animateToPersonatges(ActionEvent event) {
        setOpcio("Personatges");
        UtilsViews.setViewAnimating("Mobile1");
    }

    @FXML
    private void animateToJocs(ActionEvent event) {
        setOpcio("Jocs");
        UtilsViews.setViewAnimating("Mobile1");
    }

    @FXML
    private void animateToConsoles(ActionEvent event) {
        setOpcio("Consoles");
        UtilsViews.setViewAnimating("Mobile1");
    }
}
