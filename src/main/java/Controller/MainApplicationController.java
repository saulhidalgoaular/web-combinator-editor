package Controller;

import Model.Expression;
import Model.MainApplicationModel;
import Model.ShowCombinatorModel;
import Model.Util;
import View.MainApplicationView;
import View.ShowCombinatorView;
import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.ui.Button;
import com.vaadin.ui.Notification;

public class MainApplicationController extends AbstractController<MainApplicationView, MainApplicationModel>{

  public MainApplicationController(MainApplicationView nView, MainApplicationModel nModel) {
    super(nView, nModel);
  }

  @Override
  public void addChangedListeners() {
    view.macrosTable.addValueChangeListener(new Property.ValueChangeListener() {
      @Override
      public void valueChange(Property.ValueChangeEvent valueChangeEvent) {
        view.isTableSelected = valueChangeEvent.getProperty().getValue() != null;
        if (!view.isTableSelected) {
          view.macroGenerator.setValue("");
          view.macroName.setValue("");
          model.cleanHierarchicalContainer();
        } else {
          view.macroGenerator.setValue(
            view.macrosTable.getItem(valueChangeEvent.getProperty().getValue())
              .getItemProperty("Generador").getValue().toString());

          view.macroName.setValue(
            view.macrosTable.getItem(valueChangeEvent.getProperty().getValue())
              .getItemProperty("Macro").getValue().toString());


          model.generateHierarchicalContainer();
          regenerateNamesAndExpandTree();
        }
      }

      private void regenerateNamesAndExpandTree() {
        for ( Object itemId : model.getItemIdToName().keySet() ){
          view.macroTree.setItemCaption(itemId, model.getItemIdToName().get(itemId));
          view.macroTree.expandItem(itemId);
        }
      }
    });
  }

  @Override
  public void addListeners() {

    view.macroSaveEditButton.addClickListener(new Button.ClickListener() {
      @Override
      public void buttonClick(Button.ClickEvent clickEvent) {
        Container container = view.macrosTable.getContainerDataSource();
        String nameNewItem = view.macroName.getValue();
        String expression = view.macroGenerator.getValue();

        try {
          Expression.parse(expression);
        } catch (Exception e1) {
          Notification.show("El combinador no es correcto");
          return;
        }

        boolean repeated = false;
        for ( Object o : view.macrosTable.getItemIds() ){
          Item i = container.getItem(o);
          if ( !view.isTableSelected ){
            if ( nameNewItem.toLowerCase().equals(i.getItemProperty("Macro").getValue().toString().toLowerCase()) ){
              repeated = true;
            }
          }else{
            if ( !o.equals(view.macrosTable.getValue()) && nameNewItem.toLowerCase().equals(i.getItemProperty("Macro").getValue().toString().toLowerCase()) ){
              repeated = true;
            }
          }
        }

        if ( repeated ){
          Notification.show("No se puede colocar un nombre de Macro repetido!");
          return;
        }

        Object itemToChange;
        if ( view.isTableSelected ){
          itemToChange = view.macrosTable.getValue();
        }else{
          itemToChange = container.addItem();
        }
        Item item = container.getItem(itemToChange);
        item.getItemProperty("Macro").setValue(nameNewItem);
        item.getItemProperty("Generador").setValue(expression);
        item.getItemProperty("ID").setValue(Util.getCurrentUsername() + nameNewItem);

        view.macrosTable.select(itemToChange);
      }
    });

    view.macroShowButton.addClickListener(new Button.ClickListener() {
      @Override
      public void buttonClick(Button.ClickEvent clickEvent) {
        if ( view.isTableSelected ){
          Item item = view.macrosTable.getContainerDataSource().getItem(view.macrosTable.getValue());
          String generador = item.getItemProperty("Generador").getValue().toString();
          Expression expression = null;
          try {
            expression = Expression.parse(generador);
          } catch (Exception e) {
            Notification.show("Ha ocurrido un error. Combinador incorrecto!");
            return;
          }

          ShowCombinatorController showCombinatorController = new ShowCombinatorController(
            new ShowCombinatorView(item.getItemProperty("Macro").getValue().toString()), new ShowCombinatorModel(),
            expression.toString());

          Util.getMainWindow().addWindow(showCombinatorController.view);
        }else{
          Notification.show("Selecciones el Combinador para mostrar!");
        }
      }
    });

    view.saveAllMacros.addClickListener(new Button.ClickListener() {
      @Override
      public void buttonClick(Button.ClickEvent clickEvent) {
        if ( model.saveContainer() ){
          Notification.show("Los datos se han guardado satisfactoriamente!");
        }else{
          Notification.show("Los datos no han sido guardados! Ha ocurrido un error. :(");
        }
      }
    });
  }

  @Override
  public void addBinding() {
    view.macrosTable.setContainerDataSource(model.getMacroContainer());
    view.macroName.setPropertyDataSource(model.getName());
    view.macroGenerator.setPropertyDataSource(model.getGenerator());
    view.macroTree.setContainerDataSource(model.getHierarchicalContainer());
  }

  @Override
  public void addDataSource() {

  }

  @Override
  public void addComponents() {
    view.getLayout().addComponent(view.macrosTable, "MacrosTable");
    view.getLayout().addComponent(view.macroName, "nameLabel");
    view.getLayout().addComponent(view.macroGenerator, "generatorTextField");
    view.getLayout().addComponent(view.macroSaveEditButton, "EditButton");
    view.getLayout().addComponent(view.macroShowButton, "ViewButton");
    view.getLayout().addComponent(view.saveAllMacros, "saveButton");
    view.getLayout().addComponent(view.macroTree, "Tree");
  }

  @Override
  public void initializeComponents() {
    view.macrosTable.setVisibleColumns(new Object[]{"Macro",
      "Generador"});

  }

  @Override
  public void entryActions() {

  }
}
