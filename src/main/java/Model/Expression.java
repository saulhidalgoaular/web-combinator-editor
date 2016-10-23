package Model;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Scanner;

public class Expression {
  private String name;
  private ArrayList<Expression> children;

  /**
   * Chars allowed in name
   */
  public static String character_allowed = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789[]";
  /**
   * Space between expressions.
   */
  public static Integer tabSize = 1;

  public static final String CONFIGURATION_FILE = "conf.txt";

  public Expression() {
    this.name = "";
    children = new ArrayList<Expression>();
  }

  /**
   * Reads the configuration file and set the values with reflection.
   * @throws java.io.FileNotFoundException
   * @throws NoSuchFieldException
   * @throws IllegalAccessException
   * @throws NoSuchMethodException
   * @throws java.lang.reflect.InvocationTargetException
   * @throws InstantiationException
   */
  public static void loadConfiguration() throws FileNotFoundException,
                                                NoSuchFieldException,
                                                IllegalAccessException,
                                                NoSuchMethodException,
                                                InvocationTargetException,
                                                InstantiationException {
    Scanner scanner = new Scanner(new File(CONFIGURATION_FILE));
    while( scanner.hasNextLine() ){
      String line = scanner.nextLine();

      if ( !line.startsWith("#") && !line.isEmpty() ){
        String[] tokens = line.split("==");
        assert (tokens.length == 2);
        Field field = Expression.class.getDeclaredField(tokens[0]);

        Class [] arguments = {String.class};
        Class type = field.getType();
        Constructor declaredConstructor = type.getDeclaredConstructor(arguments);

        field.set(null,declaredConstructor.newInstance(tokens[1]));
      }

    }
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public ArrayList<Expression> getChildren() {
    return children;
  }

  public void addChildren(Expression e){
    children.add( e );
  }

  /**
   * Given an expression in raw format, returns its name.
   * @param s Expression in raw format
   * @return Name.
   */
  public static String getName(String s){
    StringBuilder ans = new StringBuilder();

    for (char c : s.toCharArray()) {
      if ( character_allowed.indexOf(c) < 0 ){
        break;
      }
      ans.append(c);
    }

    return ans.toString();
  }

  /**
   * Given all the parameters of an expression, separated with commas (,)
   * It returns an array of raw expressions.
   * @param s Parameters of an expression in raw format
   * @return Array of expressions in raw format.
   */
  public static String[] splitExpression(String s){
    int currentLevel = 0;
    ArrayList<String> ansT = new ArrayList<String>();
    StringBuilder stringBuilder = new StringBuilder();

    for (int i = 0; i < s.length(); i++) {
      char c = s.charAt(i);

      stringBuilder.append(c);
      if ( c == '(' ){
        ++currentLevel;
      }else if ( c == ')' ){
        --currentLevel;
      }else if ( c == ',' && currentLevel == 0 ){
        // String is cut when I am not inside a parameter of my children.
        stringBuilder.setLength(stringBuilder.length()-1);
        ansT.add(stringBuilder.toString());
        stringBuilder.setLength(0);
      }
    }

    // Last parameter is inside the builder. It must be added too.
    if ( stringBuilder.length() > 0 ){
      ansT.add(stringBuilder.toString());
    }

    String [] ans = new String[ansT.size()];
    ansT.toArray(ans);

    return ans;
  }

  /**
   * Given an expression in raw format, returns an expression
   * @param s Expression in raw format
   * @return Expression
   * @throws Exception In case expression is malformed.
   */
  public static Expression parse(String s) throws Exception {
    try{
      Expression ans = new Expression();

      ans.setName( getName(s) );
      s = s.substring(ans.getName().length());

      // Just a literal.
      if ( s.isEmpty() ){
        return ans;
      }

      assert( s.length() >= 2 );
      assert( s.charAt(s.length() - 1) == ')' && s.charAt(0) == '(' );

      // parenthesis are removed.
      s = s.substring(1, s.length() - 1);

      String[] children = splitExpression(s);

      // Then all my children are parsed.
      for (String iChildren : children) {
        Expression e = parse(iChildren);
        ans.addChildren(e);
      }

      return ans;
    }catch (Exception e){
      throw new Exception("Malformed Expression");
    }
  }

  /**
   * Estimate the length needed for a expression given.
   * @param e Expression to draw.
   * @return Space needed
   */
  public static int getLengthNeeded(Expression e){
    int ans = e.getName().length() + tabSize;
    for ( Expression expression : e.getChildren() ){
      ans += getLengthNeeded(expression);
    }
    return ans;
  }

  /**
   * Return the expression tree as a string.
   * @return
   */
  @Override
  public String toString() {
    int getHigherDeep = getHigherDeep(this);
    int length = getLengthNeeded(this);

    StringBuilder ans = new StringBuilder();

    char[][] builder = new char[getHigherDeep+1][length+1];
    for (int i = 0; i < builder.length; i++) {
      for (int j = 0; j < builder[i].length; j++) {
        builder[i][j] = ' ';
      }
    }
    draw(builder, this, 0, 0);

    for (char[] iBuilder : builder) {
      ans.append(new String(iBuilder)).append("\n");
    }

    return ans.toString();
  }

  /**
   * Given a "blackboard", it draws an expression in the point (x,y).
   * @param builder "Blackboard".
   * @param expression Expression to be drawn.
   * @param x X
   * @param y Y
   * @return Next Y where an expression can be drawn.
   */
  private int draw(char[][] builder, Expression expression, int x, int y) {
    assert ( x < builder.length && y < builder[x].length );

    char[] name = expression.getName().toCharArray();
    System.arraycopy(name, 0, builder[x], y, name.length);

    int startAt = y + name.length + tabSize;
    ArrayList<Expression> children = expression.getChildren();

    if ( !children.isEmpty() ){
      for (int i = 0; i < tabSize; i++) {
        builder[x][y + name.length + i] = '─';
      }
    }

    for (int i = 0; i < children.size(); i++) {
      int nextStart = draw(builder, children.get(i), x+1, startAt);

      if ( i < children.size() - 1 ){
        builder[x][startAt] = '┬';
        for (int j = startAt+1; j < nextStart; j++) {
          builder[x][j] = '─';
        }
      }else{
        builder[x][startAt] = '┐';
      }

      startAt = nextStart;
    }
    return startAt;
  }

  /**
   * Get the deep from the current expression the deepest leaf
   * @param expression Expression
   * @return deepest leaf.
   */
  private int getHigherDeep(Expression expression) {
    if ( expression.getChildren().size() == 0 ){
      return 0;
    }
    int ans = 0;
    for ( Expression e : expression.getChildren() ){
      ans = Math.max(1 + getHigherDeep(e), ans);
    }
    return ans;
  }
}
