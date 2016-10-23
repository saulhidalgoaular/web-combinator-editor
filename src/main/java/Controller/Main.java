package Controller;

import Model.LoginWindowModel;
import Model.MainApplicationModel;
import Model.Util;
import View.LoginWindowView;
import View.MainApplicationView;
import com.vaadin.annotations.PreserveOnRefresh;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinService;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.UI;

import javax.servlet.annotation.WebServlet;
import java.util.HashMap;

@Theme("mytheme")
@PreserveOnRefresh
@SuppressWarnings("serial")
public class Main extends UI {
  public HashMap<String, Object> session = new HashMap<String, Object>();
//
//  @WebServlet(value = "/*", asyncSupported = true)
//  @VaadinServletConfiguration(productionMode = false, ui = Main.class, widgetset = "CombinatorEditorVaadin.AppWidgetSet")
//  public static class Servlet extends VaadinServlet {
//
//  }

  @Override
  protected void init(VaadinRequest vaadinRequest) {
    Util.initialize();
    Util.saveMainWindow(Main.this);

    createMainWindow();

    /*Object username = VaadinService.getCurrentRequest().getWrappedSession().getAttribute("username");
    System.out.println(username);*/
    //VaadinService.getCurrentRequest().getWrappedSession().setAttribute("username","Saul");

    LoginWindowController login = new LoginWindowController(new LoginWindowView(), new LoginWindowModel());
    if ( !login.isValidated() ){
      UI.getCurrent().addWindow(login.view);
    }
  }

  protected void loadMainApplication(){
    MainApplicationController mainApplicationController = new MainApplicationController(new MainApplicationView(),
                                                                                        new MainApplicationModel());
    mainApplicationController.getLayout().setParent(null);
    ((CustomLayout)UI.getCurrent().getContent()).addComponent(mainApplicationController.getLayout(), "Main");
    //UI.getCurrent().addWindow(mainApplicationController.view);
  }

  private void createMainWindow(){
    CustomLayout customLayout = new CustomLayout("../../../html/Main");
    UI.getCurrent().setContent(customLayout);
  }
}
