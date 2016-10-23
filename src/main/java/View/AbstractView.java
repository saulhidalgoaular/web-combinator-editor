package View;

import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.Window;

public class AbstractView extends Window{
  protected CustomLayout layout = null;

  public AbstractView(String caption) {
    super(caption);
  }

  public CustomLayout getLayout() {
    return layout;
  }

  public void setLayout(CustomLayout layout) {
    this.layout = layout;
  }

  public void setLayout(String layoutURL){
    this.layout = new CustomLayout(layoutURL);
    this.setContent(this.layout);
  }
}
