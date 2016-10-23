package Controller;

import com.vaadin.annotations.PreserveOnRefresh;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinService;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.*;

import javax.servlet.annotation.WebServlet;

@Theme("mytheme")
@PreserveOnRefresh
@SuppressWarnings("serial")
public class SettingReadingSessionAttributesUI extends UI {

  private String value;

  private VerticalLayout statusHolder = new VerticalLayout();
  private TextField textField = new TextField();


  @WebServlet(value = "/*", asyncSupported = true)
  @VaadinServletConfiguration(productionMode = false, ui = Main.class, widgetset = "CombinatorEditorVaadin.AppWidgetSet")
  public static class Servlet extends VaadinServlet {

  }


  @Override
  protected void init(VaadinRequest request) {
    VerticalLayout verticalLayout = new VerticalLayout();
    verticalLayout.addComponent(statusHolder);
    verticalLayout.addComponent(textField);
    verticalLayout.addComponent(new Button("Set new values", new Button.ClickListener() {
      @Override
      public void buttonClick(Button.ClickEvent event) {
        String value = textField.getValue();

        saveValue(SettingReadingSessionAttributesUI.this, value);
      }
    }));
    verticalLayout.addComponent(new Button("Reload page", new Button.ClickListener() {
      @Override
      public void buttonClick(Button.ClickEvent event) {
        getPage().setLocation(getPage().getLocation());
      }
    }));
    UI.getCurrent().setContent(verticalLayout);

    showValue(this);
  }

  private static void saveValue(SettingReadingSessionAttributesUI ui,
                                String value) {
    // Save to UI instance
    ui.value = value;
    // Save to VaadinServiceSession
    ui.getSession().setAttribute("myValue", value);
    // Save to HttpSession
    VaadinService.getCurrentRequest().getWrappedSession()
      .setAttribute("myValue", value);

    // Show new values
    showValue(ui);
  }

  private static void showValue(SettingReadingSessionAttributesUI ui) {
    ui.statusHolder.removeAllComponents();
    ui.statusHolder.addComponent(new Label("Value in UI: " + ui.value));
    ui.statusHolder.addComponent(new Label(
      "Value in VaadinServiceSession: "
        + ui.getSession().getAttribute("myValue")));
    ui.statusHolder.addComponent(new Label("Value in HttpSession: "
      + VaadinService.getCurrentRequest().getWrappedSession()
      .getAttribute("myValue")));
  }

}