package Controller;

import Model.ShowCombinatorModel;
import View.ShowCombinatorView;

public class ShowCombinatorController extends AbstractController<ShowCombinatorView, ShowCombinatorModel>{

  public ShowCombinatorController(ShowCombinatorView nView, ShowCombinatorModel nModel, String expression) {
    super(nView, nModel);
    view.combinator.setValue(expression);
  }

  @Override
  public void addChangedListeners() {

  }

  @Override
  public void addListeners() {

  }

  @Override
  public void addBinding() {
    view.combinator.setPropertyDataSource(model.getCombinator());
  }

  @Override
  public void addDataSource() {

  }

  @Override
  public void addComponents() {
    view.getLayout().addComponent(view.combinator, "show");
  }

  @Override
  public void initializeComponents() {

  }

  @Override
  public void entryActions() {

  }
}
