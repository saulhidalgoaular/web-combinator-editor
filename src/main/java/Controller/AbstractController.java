package Controller;

import Model.AbstractModel;
import View.AbstractView;
import com.vaadin.ui.CustomLayout;

public abstract class AbstractController
  <V extends AbstractView, M extends AbstractModel>{
    protected V view;
    protected M model;
    protected AbstractController father;

    public AbstractController(V nView , M nModel){
      view = nView;
      model = nModel;

      addComponents();
      addListeners();
      addBinding();
      initializeComponents();
      addDataSource();
      addChangedListeners();
      entryActions();
    }

  public AbstractController setFather(AbstractController father){
    this.father = father;
    return this;
  }

  public AbstractController getFather() {
    return father;
  }

  protected CustomLayout getLayout(){
    return view.getLayout();
  }

  public abstract void addChangedListeners();
  public abstract void addListeners();
  public abstract void addBinding();
  public abstract void addDataSource();
  public abstract void addComponents();
  public abstract void initializeComponents();
  public abstract void entryActions();
}
