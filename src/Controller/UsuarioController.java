/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;


import DAO.UsuarioDAO;
import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import Pojo.*;
import org.json.JSONException;



public class UsuarioController {
    
       
       UsuarioDAO userDAO = new UsuarioDAO();// = new UsuarioDAOImpl();
        
        public List<Usuario> listUsuario(){
            return userDAO.listUsuario();
        }
           
        public Usuario findbyDoc(String doc){
            return userDAO.findByDoc(doc);
        }

        public List<Usuario> FindUser(String cad){
            return userDAO.FindUsuario(cad);
        }
}
