package Model;

import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.util.HierarchicalContainer;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Tree;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class MainApplicationModel extends AbstractModel {
  private Container macroContainer = new IndexedContainer();
  private ObjectProperty<String> name = new ObjectProperty<String>("");
  private ObjectProperty<String> generator = new ObjectProperty<String>("");
  private HierarchicalContainer hierarchicalContainer = new HierarchicalContainer();
  private HashMap<Object, String> itemIdToName = new HashMap<Object, String>();

  public MainApplicationModel() {
    loadContainer();
  }

  public ObjectProperty<String> getName() {
    return name;
  }

  public ObjectProperty<String> getGenerator() {
    return generator;
  }

  public Container getMacroContainer() {
    return macroContainer;
  }

  public HashMap<Object, String> getItemIdToName() {
    return itemIdToName;
  }

  public HierarchicalContainer getHierarchicalContainer() {
    return hierarchicalContainer;
  }

  public void cleanHierarchicalContainer(){
    hierarchicalContainer.removeAllItems();
  }

  public void generateHierarchicalContainer(){
    try {
      Expression expression = Expression.parse(generator.getValue());
      hierarchicalContainer.removeAllItems();
      itemIdToName.clear();

      include(expression, hierarchicalContainer, null);

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void include(Expression expression, HierarchicalContainer hierarchicalContainer, Object father) {
    Object itemId = hierarchicalContainer.addItem();

    if ( father != null ){
      hierarchicalContainer.setParent( itemId, father );
    }

    if ( expression.getChildren().isEmpty() ){
      hierarchicalContainer.setChildrenAllowed(itemId, false);
    }

    itemIdToName.put(itemId, expression.getName());
    for ( Expression child : expression.getChildren() ){
      include(child, hierarchicalContainer, itemId);
    }
  }

  private void loadContainer(){
    macroContainer.addContainerProperty("Macro", String.class, "");
    macroContainer.addContainerProperty("Generador", String.class, "");
    macroContainer.addContainerProperty("ID" , String.class, "");


    Connection connection = null;
    ResultSet resultSet = null;

    try {
      connection = Util.getConnection();
      PreparedStatement preparedStatement = connection.prepareStatement("select id, name, generator from macro where user_id = ? ");
      preparedStatement.setString(1, Util.getCurrentUsername());
      resultSet = preparedStatement.executeQuery();

      while ( resultSet.next() ){
        Object itemId = macroContainer.addItem();
        Item realItem = macroContainer.getItem(itemId);
        realItem.getItemProperty("Macro").setValue( resultSet.getString("name") );
        realItem.getItemProperty("Generador").setValue( resultSet.getString("generator") );
        realItem.getItemProperty("ID").setValue( resultSet.getString("id") );
      }

    }catch (SQLException e){
      e.printStackTrace();
    }finally {
      if ( resultSet != null ){
        try {
          resultSet.close();
        } catch (SQLException e) {

        }
      }
      if ( connection != null ){
        try {
          connection.close();
        } catch (SQLException e) {

        }
      }
    }
  }

  public boolean saveContainer(){
    Connection connection= null;
    boolean ans = false;

    try {
      connection = Util.getConnection();
      connection.setAutoCommit(false);

      PreparedStatement preparedStatement = connection.prepareStatement("delete from macro where user_id = ? ");
      preparedStatement.setString(1, Util.getCurrentUsername());
      preparedStatement.executeUpdate();

      for ( Object o : macroContainer.getItemIds() ){
        Item i = macroContainer.getItem(o);
        preparedStatement = connection.prepareStatement(" insert into macro( id, name, generator, user_id ) values (?, ?, ?, ?) ");
        preparedStatement.setString(1, i.getItemProperty("ID").getValue().toString());
        preparedStatement.setString(2, i.getItemProperty("Macro").getValue().toString());
        preparedStatement.setString(3, i.getItemProperty("Generador").getValue().toString());
        preparedStatement.setString(4, Util.getCurrentUsername());
        preparedStatement.executeUpdate();
      }

      connection.commit();

      ans = true;
    }catch ( SQLException e ){
      try {
        Notification.show(e.toString());
        connection.rollback();
      } catch (SQLException e1) {

      }
    }finally {
      if ( connection != null ){
        try {
          connection.setAutoCommit(true);
          connection.close();
        } catch (SQLException e) {

        }
      }
    }

    return ans;
  }
}
