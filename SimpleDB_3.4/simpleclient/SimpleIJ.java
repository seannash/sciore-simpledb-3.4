import java.sql.*;
import java.util.Scanner;
import simpledb.jdbc.embedded.EmbeddedDriver;
import simpledb.jdbc.network.NetworkDriver;

public class SimpleIJ {
   public static void main(String[] args) {

      Scanner sc = new Scanner(System.in);
      String connectStr = "";
      if (args.length == 1) {
         connectStr = args[0];
      } else {
         System.out.println("Connect> ");
         connectStr = sc.nextLine();
      }
      Driver d = (connectStr.contains("//")) ? new NetworkDriver() : new EmbeddedDriver();

      try (Connection conn = d.connect(connectStr, null);
           Statement stmt = conn.createStatement()) {
         System.out.print("\nSQL> ");
         while (sc.hasNextLine()) {
            // process one line of input
            String cmd = getNextLine(sc);
            if (cmd.startsWith("exit"))
               break;
            else if (cmd.startsWith("select"))
               doQuery(stmt, cmd);
            else
               doUpdate(stmt, cmd);
            System.out.print("\nSQL> ");
         }
      }
      catch (SQLException e) {
         e.printStackTrace();
      }
      sc.close();
   }

   public static String getNextLine(Scanner sc) {
      StringBuffer sb = new StringBuffer();

      String first = sc.nextLine();
      sb.append(first);
      first.trim();
      if (first.startsWith("exit") || first.endsWith(";"))
         return first;
      while (sc.hasNextLine()) {
         String s = sc.nextLine();
         sb.append(s);
         sb.append(" ");
         s.trim();
         if (s.endsWith(";")) {
            break;
         }
      }
      var line = sb.toString();
      line.trim();
      line = line.substring(0, line.length() - 1);
      return line;

   }

   private static void doQuery(Statement stmt, String cmd) {
      try (ResultSet rs = stmt.executeQuery(cmd)) {
         ResultSetMetaData md = rs.getMetaData();
         int numcols = md.getColumnCount();
         int totalwidth = 0;

         // print header
         for(int i=1; i<=numcols; i++) {
            String fldname = md.getColumnName(i);
            int width = md.getColumnDisplaySize(i);
            totalwidth += width;
            String fmt = "%" + width + "s";
            System.out.format(fmt, fldname);
         }
         System.out.println();
         for(int i=0; i<totalwidth; i++)
            System.out.print("-");
         System.out.println();

         // print records
         while(rs.next()) {
            for (int i=1; i<=numcols; i++) {
               String fldname = md.getColumnName(i);
               int fldtype = md.getColumnType(i);
               String fmt = "%" + md.getColumnDisplaySize(i);
               if (fldtype == Types.INTEGER) {
                  int ival = rs.getInt(fldname);
                  System.out.format(fmt + "d", ival);
               }
               else {
                  String sval = rs.getString(fldname);
                  System.out.format(fmt + "s", sval);
               }
            }
            System.out.println();
         }
      }
      catch (SQLException e) {
         System.out.println("SQL Exception: " + e.getMessage());
      }
   }

   private static void doUpdate(Statement stmt, String cmd) {
      try {
         int howmany = stmt.executeUpdate(cmd);
         System.out.println(howmany + " records processed");
      }
      catch (SQLException e) {
         System.out.println("SQL Exception: " + e.getMessage());
      }
   }
}