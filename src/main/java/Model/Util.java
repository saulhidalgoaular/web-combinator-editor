package Model;

import Controller.Main;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.vaadin.server.VaadinService;

import java.sql.Connection;
import java.sql.SQLException;

public class Util {
  protected static ComboPooledDataSource pooledDataSource = null;

  public static boolean initialize(){
    try {
      if ( pooledDataSource == null ){
        pooledDataSource = new ComboPooledDataSource();
        pooledDataSource.setDriverClass(Constants.DATABASE_DRIVER);
        pooledDataSource.setJdbcUrl(Constants.DATABASE_URL);
        pooledDataSource.setUser(Constants.DATABASE_USER);
        pooledDataSource.setPassword(Constants.DATABASE_PASSWORD);
      }
      return true;

    } catch (Exception ex) {
      System.out.println(ex.getMessage());
      return false;
    }
  }

  public static Connection getConnection() throws SQLException {
    return pooledDataSource.getConnection();
  }

  public static String getCurrentUsername(){
    Object username = VaadinService.getCurrentRequest().getWrappedSession().getAttribute("username");
    if ( username != null ){
      return username.toString();
    }
    return null;
  }

  public static Main getMainWindow(){
    return (Main)VaadinService.getCurrentRequest().getWrappedSession().getAttribute("Main");
  }

  public static void saveMainWindow(Main main){
    VaadinService.getCurrentRequest().getWrappedSession().setAttribute("Main", main);
  }
}
