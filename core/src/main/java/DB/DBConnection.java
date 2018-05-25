package DB;

import com.sun.rmi.rmid.ExecPermission;
import jdk.nashorn.internal.scripts.JD;

import java.sql.*;
import java.util.ArrayList;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;


/**
 * Created by clara.marti on 08/05/2018.
 */
public class DBConnection {

    static final String JDBC_DRIVER = "org.h2.Driver";
    static final String DB_URL = "jdbc:h2:~/test";
    static final String USER = "admin";
    static final String PASS = "system";


    public List<Users> showUsers() {
        ArrayList<Users> llistaUsuaris = new ArrayList<Users>();

        try {
            Class.forName(JDBC_DRIVER);
            Connection con = DriverManager.getConnection(DB_URL, USER, PASS);
            Statement stmt = con.createStatement();
            String sql = "SELECT * FROM Exemple;";

            ResultSet rs = stmt.executeQuery(sql);


            while (rs.next()) {
                Users usu = new Users();
                usu.setId(rs.getString("ID"));
                usu.setName(rs.getString("NAME"));
                usu.setSurname(rs.getString("LASTNAME"));
                usu.setBalance(rs.getString("BALANCE"));

                llistaUsuaris.add(usu);
            }

            stmt.close();
            con.close();


        } catch (Exception e) {
            System.out.println((e.toString()));
        }
        return llistaUsuaris;

    }

    public static void addUser(String id, String name, String surname, String balance) {

        try {
            Class.forName(JDBC_DRIVER);
            Connection con = DriverManager.getConnection(DB_URL, USER, PASS);
            Statement stmt = con.createStatement();

            String update = "INSERT INTO Exemple(ID, NAME, LASTNAME, BALANCE) VALUES ('" + id + "','" + name + "','" + surname + "','" + balance + "')";

            stmt.executeUpdate(update);

            stmt.close();
            con.close();

        } catch (Exception e) {
            System.out.println((e.toString()));

        }
    }

    /**
     * Show all the info from the selected user
     **/
    public Users eachuser(String id) {
        Users usuari = null;
        ArrayList<Expenses> ar = new ArrayList<Expenses>();
        try {
            Class.forName(JDBC_DRIVER);
            Connection con = DriverManager.getConnection(DB_URL, USER, PASS);
            String sql = "SELECT * FROM Exemple ex LEFT JOIN gastos gas ON ex.id=gas.iduser WHERE ID ='" + id + "'";

            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                if (usuari == null) {
                    usuari = new Users();
                    usuari.setId(rs.getString("ID"));
                    usuari.setName(rs.getString("NAME"));
                    usuari.setSurname(rs.getString("LASTNAME"));
                    usuari.setBalance(rs.getString("BALANCE"));
                }

                Expenses exp = new Expenses();
                exp.setId(rs.getString("ID_G"));
                exp.setCategory(rs.getString("CATEGORY"));
                exp.setAmount(rs.getString("AMOUNT"));
                exp.setIdUsuari(rs.getString("IDUSER"));

                ar.add(exp);

            }

            usuari.setExpencount(ar);

            stmt.close();
            con.close();
        } catch (Exception e) {
            System.out.println((e.toString()));
        }
        return usuari;

    }

    /**
     * Add expense with the parametes from the form and the hidden iduser
     **/

    public static void addexpense(String id, String category, String amount, String idusuari) {
        try {
            Class.forName(JDBC_DRIVER);
            Connection con = DriverManager.getConnection(DB_URL, USER, PASS);
            Statement stmt = con.createStatement();

            String addGasto = "INSERT INTO GASTOS (id_g,category,amount,iduser) VALUES(" + id + ",'" + category + "','" + amount + "'," + idusuari + ")";
            stmt.executeUpdate(addGasto);
//
            stmt.close();
            con.close();

        } catch (Exception e) {
            System.out.println("error");
        }
    }

    /**
     * Amount's sum of each user
     **/

    public static double total(String idusu) {

        Double sumtotal = 0.0;

        try {
            Class.forName(JDBC_DRIVER);
            Connection con = DriverManager.getConnection(DB_URL, USER, PASS);
            Statement stmt = con.createStatement();

            String sql = "SELECT sum(amount) FROM gastos WHERE iduser ='" + idusu + "'";

            ResultSet rs = stmt.executeQuery(sql);

            if (rs.next()) {
                sumtotal = rs.getDouble(1);

            } else {
                sumtotal = 0.0;
            }

            stmt.close();
            con.close();

        } catch (Exception e) {
            System.out.println("error");
        }
        return sumtotal;

    }


    public static double alltotal() {
        Double alltotal = 0.0;
        try {
            Class.forName(JDBC_DRIVER);
            Connection con = DriverManager.getConnection(DB_URL, USER, PASS);
            Statement stmt = con.createStatement();

            String sql = "SELECT sum(amount) FROM gastos";
            ResultSet rs = stmt.executeQuery(sql);

            if(rs.next()){
                alltotal = rs.getDouble(1);
            } else {
                alltotal = 0.0;
            }

            stmt.close();
            con.close();

        } catch (Exception e) {
            System.out.println("error");
        }
        return alltotal;

    }

    public static void deleterow(String columnid) {

        try {
            Class.forName(JDBC_DRIVER);
            Connection con = DriverManager.getConnection(DB_URL, USER, PASS);
            Statement stmt = con.createStatement();

            String sql = "DELETE FROM gastos WHERE id_g='" + columnid + "'";
            stmt.executeUpdate(sql);


            stmt.close();
            con.close();

        } catch (Exception e) {
            System.out.println("error");
        }

    }

    public ArrayList<Expenses> forcategory (String category){

        ArrayList<Expenses> forcat = new ArrayList<Expenses>();
        try {
            Class.forName(JDBC_DRIVER);
            Connection con = DriverManager.getConnection(DB_URL, USER, PASS);
            Statement stmt = con.createStatement();

            String sql = "SELECT category,amount, ex.name FROM GASTOS ga JOIN Exemple ex ON ga.iduser = ex.id WHERE category ='" + category + "'";

            ResultSet rs = stmt.executeQuery(sql);

            while(rs.next()){
                Expenses gastotipo = new Expenses();
                gastotipo.setCategory(rs.getString("category"));
                gastotipo.setAmount(rs.getString("amount"));
                //Aqui hauria de ser s'id, pero li pos es nom, amem si cuela
                gastotipo.setIdUsuari(rs.getString("name"));

                forcat.add(gastotipo);
            }

            stmt.close();
            con.close();

        } catch (Exception e) {
            System.out.println("error");
        }
        return forcat;
    }


}



//    Insert into gastos (id_g,category,amount,iduser) values (01,'cuina',1.6,1);