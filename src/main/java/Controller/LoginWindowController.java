package Controller;

import Model.LoginWindowModel;
import Model.Util;
import View.LoginWindowView;
import com.vaadin.server.VaadinService;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Button;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;

public class LoginWindowController extends AbstractController<LoginWindowView, LoginWindowModel> {

  private boolean validated;

  public boolean isValidated() {
    return validated;
  }

  public LoginWindowController(LoginWindowView nView, LoginWindowModel nModel) {
    super(nView, nModel);
  }

  @Override
  public void addChangedListeners() {

  }

  public void startSession(){
    Notification.show("Bienvenido " + model.getUsername().getValue());
    validated = true;

    view.close();

    Util.getMainWindow().loadMainApplication();
  }

  @Override
  public void addListeners() {
    view.cancel.addClickListener(new Button.ClickListener() {
      @Override
      public void buttonClick(Button.ClickEvent clickEvent) {
        view.close();
      }
    });

    view.login.addClickListener(new Button.ClickListener() {
      @Override
      public void buttonClick(Button.ClickEvent clickEvent) {
        if ( !model.checkLogin() ){
          Notification.show("La combinación Usuario/Contraseña no es correcta");
        }else{
          VaadinService.getCurrentRequest().getWrappedSession().setAttribute("username", model.getUsername().getValue());

          startSession();
        }
      }
    });
  }

  @Override
  public void addBinding() {
    view.username.setPropertyDataSource(model.getUsername());
    view.password.setPropertyDataSource(model.getPassword());
  }

  @Override
  public void addDataSource() {

  }

  @Override
  public void addComponents() {
    view.getLayout().addComponent(view.welcomeMessage, "welcomeMessage");
    view.getLayout().addComponent(view.username, "username");
    view.getLayout().addComponent(view.password, "password");
    view.getLayout().addComponent(view.login, "login");
    view.getLayout().addComponent(view.cancel, "cancel");
  }

  @Override
  public void initializeComponents() {
    view.setWidth("300px");
    view.setHeight("200px");

    view.username.setInputPrompt("Nombre de Usuario");
    view.password.setInputPrompt("Contraseña");

  }

  @Override
  public void entryActions() {
    String username = Util.getCurrentUsername();
    if ( username != null ){
      view.username.setValue(username);
      startSession();
    }
  }
}
