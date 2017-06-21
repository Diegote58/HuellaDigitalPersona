/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import BD.ConexionBD;
import BD.HibernateConnector;
import DAO.HuellaDAO;
import com.digitalpersona.onetouch.DPFPFeatureSet;
import com.digitalpersona.onetouch.DPFPGlobal;
import com.digitalpersona.onetouch.DPFPTemplate;
import com.digitalpersona.onetouch.verification.DPFPVerificationResult;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author ASUS555
 */
public class HuellaController {
    
    
    //Recibe los datos de la vista y guarda la Huella.
    public void guardarHuella(int id,String dedo,DPFPTemplate template,DPFPFeatureSet feature){
    
       HuellaDAO huellaDao = new HuellaDAO();
        try{            
            huellaDao.guardarHuella(id, dedo, template, feature);
        } catch (IOException ex) {
            Logger.getLogger(HuellaController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    //Verificar la Huella
    
    
    
   
}
