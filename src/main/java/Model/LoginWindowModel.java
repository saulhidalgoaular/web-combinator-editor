package Model;

import com.vaadin.data.util.ObjectProperty;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginWindowModel extends AbstractModel{
  private ObjectProperty<String> username;
  private ObjectProperty<String> password;

  public LoginWindowModel() {
    username = new ObjectProperty<String>("");
    password = new ObjectProperty<String>("");
  }

  public ObjectProperty<String> getUsername() {
    return username;
  }

  public ObjectProperty<String> getPassword() {
    return password;
  }

  public boolean checkLogin(){
    if ( username.getValue().isEmpty() ){
      return false;
    }

    boolean ans = false;
    Connection connection = null;
    ResultSet resultSet = null;

    try{
      connection = Util.getConnection();

      PreparedStatement preparedStatement = connection.prepareStatement("select * from user where username = ?");
      preparedStatement.setString(1, username.getValue());
      resultSet = preparedStatement.executeQuery();

      if ( !resultSet.next() ){
        createUser();
        ans = true;
      }else{

        preparedStatement = connection.prepareStatement("select * from user where username = ? and password = ?");
        preparedStatement.setString(1, username.getValue());
        preparedStatement.setString(2, password.getValue());
        resultSet = preparedStatement.executeQuery();

        ans = resultSet.next();
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

    return ans;
  }

  public void createUser(){
    Connection connection = null;

    try {
      connection = Util.getConnection();
      PreparedStatement preparedStatement = connection.prepareStatement("insert into user( username, password ) values (? , ?) ");
      preparedStatement.setString(1, username.getValue());
      preparedStatement.setString(2, password.getValue());
      preparedStatement.executeUpdate();
    }catch (SQLException e){
      e.printStackTrace();
    }finally {
      if ( connection != null ){
        try {
          connection.close();
        } catch (SQLException e) {

        }
      }
    }
  }
}
