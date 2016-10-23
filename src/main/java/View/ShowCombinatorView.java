package View;

import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;

public class ShowCombinatorView extends AbstractView {

  public final TextArea combinator = new TextArea();

  public ShowCombinatorView(String caption) {
    super(caption);
    super.setLayout("../../../html/ShowCombinator");

    setModal(true);

    combinator.setSizeFull();
  }


}
