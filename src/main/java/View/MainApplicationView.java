package View;

import com.vaadin.ui.Button;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Tree;

public class MainApplicationView extends AbstractView{

  public final Table macrosTable = new Table();
  public final Button addButton = new Button("Agregar Macro");
  public final TextField macroName = new TextField();
  public final TextField macroGenerator = new TextField();
  public final Button macroSaveEditButton = new Button("Guardar Modificación");
  public final Button macroShowButton = new Button("Mostrar");
  public final Button saveAllMacros = new Button("Guardar Macros");
  public final Tree macroTree = new Tree();
  public boolean isTableSelected = false;

  public MainApplicationView() {
    super("Web Combinator Editor");
    super.setLayout("../../../html/MainApplication");

    macrosTable.setSelectable(true);
    macrosTable.setImmediate(true);
    macrosTable.setWidth("300px");
  }
}
