package Model;

import com.vaadin.data.util.ObjectProperty;

public class ShowCombinatorModel extends AbstractModel{
  private ObjectProperty<String> combinator = new ObjectProperty<String>("");

  public ObjectProperty<String> getCombinator() {
    return combinator;
  }
}
