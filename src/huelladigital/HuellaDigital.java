/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package huelladigital;

import BD.HibernateConnector;
import Controller.UsuarioController;

import Pojo.Usuario;
import Vistas.CapturarHuella;
import Vistas.Interfazprincipal;
import Vistas.UsuariosForm;
import Vistas.UsuariosForm;

import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

public class HuellaDigital {

    
    public static void main(String[] args) {
        
       // CapturarHuella cp = new CapturarHuella();
        //cp.setVisible(true);
        
        UsuariosForm form = new UsuariosForm();
        form.setVisible(true);
              
        
     /*   UsuarioController us = new UsuarioController();
        
        List<Usuario> li = us.listUsuario();
    
        System.out.println("huelladigital.HuellaDigital.main()" + li.toString());*/
            
       
    }
    
    
    
}
