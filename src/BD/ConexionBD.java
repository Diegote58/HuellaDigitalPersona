/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BD;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class ConexionBD {
    
    public String puerto ="3306";
    public String servidor ="localhost";
    public String db ="huelladigital";
    public String user = "root";
    public String pass = "yz450f";
    Connection conn = null; 
    
  
    
    public Connection Conectar(){
        try {
            Class.forName("com.mysql.jdbc.Driver");
            //conn = DriverManager.getConnection("jdbc:mysql://"+servidor+":"+puerto+"/" + db,user,pass);
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/huelladigital",user, pass);
            if (conn != null) {
                System.out.println("Conectado a la BD");
            }else if(conn==null){
                System.out.println("ERROR: Al Conectar a la BD");
                throw new SQLException();
            }
            
            return conn;
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null,"ERROR_BD: "+ex.getMessage());
        }catch (ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(null,"ERROR_BD: "+ex.getMessage());
        }catch (NullPointerException ex) {
            JOptionPane.showMessageDialog(null,"ERROR_BD: "+ex.getMessage());
        }finally{
        return conn;
        }
    }
    
    public void Desconectar(){
    conn=null;
     System.out.println("Desconectado de la BD");
    
    }
    

}
