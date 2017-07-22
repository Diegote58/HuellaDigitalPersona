/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import BD.ConexionBD;
import BD.HibernateConnector;
import Pojo.Huelladigital;
import Pojo.Usuario;
import com.digitalpersona.onetouch.DPFPDataPurpose;
import com.digitalpersona.onetouch.DPFPFeatureSet;
import com.digitalpersona.onetouch.DPFPGlobal;
import com.digitalpersona.onetouch.DPFPSample;
import com.digitalpersona.onetouch.DPFPTemplate;
import com.digitalpersona.onetouch.capture.DPFPCapture;
import com.digitalpersona.onetouch.capture.event.DPFPDataAdapter;
import com.digitalpersona.onetouch.capture.event.DPFPDataEvent;
import com.digitalpersona.onetouch.capture.event.DPFPErrorAdapter;
import com.digitalpersona.onetouch.capture.event.DPFPErrorEvent;
import com.digitalpersona.onetouch.capture.event.DPFPReaderStatusAdapter;
import com.digitalpersona.onetouch.capture.event.DPFPReaderStatusEvent;
import com.digitalpersona.onetouch.capture.event.DPFPSensorAdapter;
import com.digitalpersona.onetouch.capture.event.DPFPSensorEvent;
import com.digitalpersona.onetouch.processing.DPFPEnrollment;
import com.digitalpersona.onetouch.processing.DPFPFeatureExtraction;
import com.digitalpersona.onetouch.processing.DPFPImageQualityException;
import com.digitalpersona.onetouch.verification.DPFPVerification;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import com.digitalpersona.onetouch.verification.DPFPVerificationResult;
import java.awt.Color;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractButton;
import javax.swing.JTextField;
import javax.swing.UnsupportedLookAndFeelException;
import java.awt.Component;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;


public class HuellaDAO_OLD {
    
    
    ConexionBD con = new ConexionBD();
          
    //Variables Lector
    private DPFPTemplate template;
    private String TEMPLATE_PROPERTY = "template";
        
     public DPFPTemplate getTemplate() {
        return template;
    }

    public void setTemplate(DPFPTemplate Template) {    
        this.template = Template;
    }
    
    
    
    public void guardarHuella(int id,String dedo,DPFPTemplate template) throws IOException{
        
        Huelladigital huella = new Huelladigital();
        huella.setId_huella(null);
        huella.setId_usuario(id);
        huella.setDedo(dedo);
        
        //System.out.println("TEMPLATE: " + template.serialize());
        //Obtiene los datos del Template de la Huella.
        ByteArrayInputStream datosHuella = new ByteArrayInputStream(template.serialize());
        Integer tamañoHuella = template.serialize().length;
      
        try {
        
            //session = HibernateConnector.getInstance().getSession();
            //Save Huella.
            //session.save(huella);
            Connection c = con.Conectar();
            PreparedStatement ps = c.prepareStatement("INSERT INTO huelladigital(id_usuario,dedo,huella) values(?,?,?)");
            ps.setInt(1, huella.getId_usuario());
            ps.setString(2, huella.getDedo());
            ps.setBinaryStream(3,datosHuella, template.serialize().length);
            ps.execute();
            ps.close();
            //JOptionPane.showMessageDialog(null,"Huella Cargada Correctamente");
            
        } catch (Exception e) {
            System.err.println("ERROR: Guardando Huella: " + e.getMessage());
            
        }finally{
            con.Desconectar();
        }
        
    }
    
    
     //Verificar Huella
     public Usuario VerificarHuellaPersona(int id_usuario,String dedo,DPFPVerification Verificador,DPFPFeatureSet featuredVerificacion){
        try {
            Connection c = con.Conectar();
            PreparedStatement ps = c.prepareStatement("SELECT huella FROM huelladigital WHERE id_usuario=? and dedo=?");
            ps.setInt(1, id_usuario);
            ps.setString(2, dedo);
            ResultSet rs = ps.executeQuery();
            
            if(rs.next()){
            //Buffer donde se almacena Huella desde la BD
            byte templateBuffer[] = rs.getBytes("huella");
            DPFPTemplate referenceTemplate = DPFPGlobal.getTemplateFactory().createTemplate(templateBuffer);
            //Se comparan las huellas Capturada con la de la BD.
            DPFPVerificationResult result = Verificador.verify(featuredVerificacion, referenceTemplate);
            
            //Consulta de usuario seleccionado.
            PreparedStatement us = c.prepareStatement("SELECT * FROM usuario WHERE id_usuario=?");
            us.setInt(1, id_usuario);
            ResultSet rst = us.executeQuery();
            Usuario user = new Usuario();// = new Usuario();

            while (rst.next()) {
                user = new Usuario(rst.getInt("id_usuario"), rst.getString("tipo_doc"), rst.getInt("nro_doc"), rst.getString("nombre"), rst.getString("apellido1"), rst.getString("apellido2"), rst.getInt("matricula"), rst.getString("categoria"),rst.getString("mail"), rst.getString("password"), rst.getString("foto"), rst.getString("telefono"), rst.getString("sexo"), rst.getString("fechaNacimiento"), rst.getTimestamp("fechaAlta"), rst.getString("grupoSanguineo"), 
                rst.getString("estadoCivil"),rst.getString("rol"), rst.getString("estado"));

            }
                
                if(result.isVerified()){
                    
               // JOptionPane.showMessageDialog(null, "Verifica la Huella con " + user.getApellido1() + " " + user.getApellido2()+ " " + user.getNombre()," Veriicacion de Huella", JOptionPane.INFORMATION_MESSAGE);
                return user;
                }
                else{
                //JOptionPane.showMessageDialog(null, "No Verifica la Huella con " + user.getApellido1() + " " + user.getApellido2()+ " " + user.getNombre(),"Veriicacion de Huella", JOptionPane.ERROR_MESSAGE);
                return null;
                }
            }
            else{
                //JOptionPane.showMessageDialog(null, "No Existe la Huella" ,"Veriicacion de Huella", JOptionPane.ERROR_MESSAGE);        
                return null;
            }
              
            
        } catch (Exception e) {
            System.err.println("ERROR: Al Verificar: " + e.getMessage());
        }finally{con.Desconectar();}
        return null;
    }
     
     //Identificar Huella
     public Usuario IdentificarHuella(DPFPVerification Verificador,DPFPFeatureSet featuredVerificacion){
        try {
            Connection c = con.Conectar();
            PreparedStatement ps = c.prepareStatement("SELECT * FROM huelladigital");
            
            ResultSet rs = ps.executeQuery();
            
            while(rs.next()){
            //Buffer donde se almacena Huella desde la BD
            byte templateBuffer[] = rs.getBytes("huella");
            DPFPTemplate referenceTemplate = DPFPGlobal.getTemplateFactory().createTemplate(templateBuffer);
            //Se comparan las huellas Capturada con la de la BD.
            DPFPVerificationResult result = Verificador.verify(featuredVerificacion, referenceTemplate);
            
            if(result.isVerified()){
            
                //Consulta de usuario seleccionado.
                PreparedStatement us = c.prepareStatement("SELECT * FROM usuario WHERE id_usuario=?");
                us.setInt(1, rs.getInt("id_usuario"));
                ResultSet rst = us.executeQuery();
                Usuario user = new Usuario();// = new Usuario();

                while (rst.next()) {
                    user = new Usuario(rst.getInt("id_usuario"), rst.getString("tipo_doc"), rst.getInt("nro_doc"), rst.getString("nombre"), rst.getString("apellido1"), rst.getString("apellido2"), rst.getInt("matricula"), rst.getString("categoria"),rst.getString("mail"), rst.getString("password"), rst.getString("foto"), rst.getString("telefono"), rst.getString("sexo"), rst.getString("fechaNacimiento"), rst.getTimestamp("fechaAlta"), rst.getString("grupoSanguineo"), 
                    rst.getString("estadoCivil"),rst.getString("rol"), rst.getString("estado"));
                }
                //Mensaje
                //JOptionPane.showMessageDialog(null, "La Huella se Identifica con " + user.getApellido1() + " " + user.getApellido2()+ ", " + user.getNombre(),"Identificación de Huellas", JOptionPane.INFORMATION_MESSAGE);
                return user;
                }
            }
           
            
        } catch (Exception e) {
            System.err.println("ERROR: Al Idnetificar: " + e.getMessage());
        }finally{con.Desconectar();}
        return null;
    }

    
}
