package View;

import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;

public class LoginWindowView extends AbstractView{
  final public Label welcomeMessage = new Label("Coloque el nombre de usuario " +
    "si desea recargar los macros guardados previamente");
  final public TextField username = new TextField();
  final public PasswordField password = new PasswordField();
  final public Button login = new Button("Iniciar Sesión");
  final public Button cancel = new Button("Cancelar");

  public LoginWindowView() {
    super("Inicio de Sesion");
    super.setLayout("../../../html/LoginLayout");

    setResizable(false);
    setClosable(false);
    setModal(true);
  }
}
