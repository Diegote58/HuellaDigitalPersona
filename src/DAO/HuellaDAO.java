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
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
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
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.URL;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.net.HttpURLConnection;
import java.net.URLEncoder;
import java.sql.Blob;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import javax.net.ssl.HttpsURLConnection;
import static org.apache.http.HttpHeaders.USER_AGENT;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONObject;



public class HuellaDAO {
    
    
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
    
    public void Sendhuella(int id,String dedo,DPFPTemplate template,DPFPFeatureSet feature){
    
        try {
            
            String httpURL = "http://localhost:8080/SistemaHistoria/rest/huella/save";
            
                URL obj = new URL(httpURL);
		//HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
                HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		//add reuqest header
		con.setRequestMethod("POST");
		con.setRequestProperty("User-Agent", USER_AGENT);
		con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

                String urlParameters = "id="+id+"&finger="+dedo+"&data="+ Arrays.toString(template.serialize());
                                
		// Send post request
		con.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.writeBytes(urlParameters);
		wr.flush();
		wr.close();

		int responseCode = con.getResponseCode();
		System.out.println("\nSending 'POST' request to URL : " + httpURL);
		System.out.println("Post parameters : " + urlParameters);
		System.out.println("Response Code : " + responseCode);

		BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuilder response = new StringBuilder();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

		//print result
		System.out.println(response.toString());

        } catch (IOException e) {
            System.err.println("ERROR: Post Huella -> " + e.getMessage());
           
        }
    }
    
    public void guardarHuella(int id,String dedo,DPFPTemplate template,DPFPFeatureSet feature) throws IOException{
        
        
        try {
            byte[] b = template.serialize();
        
            //Función que envía datos por HTTP
            Sendhuella(id, dedo, template,feature);                        
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
            
           Usuario usuario = null; 
                      
           String httpURL = "http://localhost:8080/SistemaHistoria/rest/huella/identificar";
            
           
                URL obj = new URL(httpURL);
                //HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
                HttpURLConnection conn = (HttpURLConnection) obj.openConnection();

		//add request header
		conn.setRequestMethod("POST");
		conn.setRequestProperty("User-Agent", USER_AGENT);
		conn.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
                
                //Convertir Array String
                //String[] byteValues = Arrays.toString(encode_template).substring(1, Arrays.toString(encode_template).length() - 1).split(",");
                
                //Data POST
                String urlParameters = "data="+ Arrays.toString(featuredVerificacion.serialize());
                
                
		// Send post request
		conn.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
		wr.writeBytes(urlParameters);
		wr.flush();
		wr.close();

		int responseCode = conn.getResponseCode();
		System.out.println("\nSending 'POST' request to URL : " + httpURL);
		System.out.println("Post parameters : " + urlParameters);
		System.out.println("Response Code : " + responseCode);

		BufferedReader in = new BufferedReader(
		        new InputStreamReader(conn.getInputStream()));
		String inputLine;
		StringBuilder response = new StringBuilder();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

		//print result
                System.out.println("RESPONSE: " + response.toString());
                //usuario = new Gson().fromJson(response.toString(), Usuario.class);
                ObjectMapper mapper = new ObjectMapper();
                usuario = mapper.readValue(response.toString(), Usuario.class);
                System.out.println("USUARIO: " + usuario.toString());
                if(usuario != null){
                    return usuario;
                }else {
                    return null;
                }
                
        }catch (JsonSyntaxException | IOException e) {
            System.err.println("ERROR: Al Identificar: " + e.getMessage());
            e.printStackTrace();
            return null;
        }finally{con.Desconectar();}
        
    }

     
    
}
