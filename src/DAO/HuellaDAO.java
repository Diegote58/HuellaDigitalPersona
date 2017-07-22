/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import Pojo.Usuario;
import com.digitalpersona.onetouch.DPFPFeatureSet;
import com.digitalpersona.onetouch.DPFPTemplate;
import com.digitalpersona.onetouch.verification.DPFPVerification;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.HttpURLConnection;
import java.util.Arrays;
import static org.apache.http.HttpHeaders.USER_AGENT;



public class HuellaDAO {
    
    
          
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
    
    public int guardarHuella(int id,String dedo,DPFPTemplate template,DPFPFeatureSet feature) throws IOException{
        
        try {
        
            byte[] b = template.serialize();
             
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

                if(responseCode != 200){
                    return 0;
                }
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
                return 1;
        } catch (IOException e) {
           System.err.println("ERROR: Post Huella -> " + e.getMessage());
           return 0;
        }
         
    }
    
    
     //Verificar Huella
     public Usuario VerificarHuella(int id_usuario,String dedo,DPFPVerification Verificador,DPFPFeatureSet featuredVerificacion){
        /*try {
            
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
            return null;
        }
        */
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
                
                
                usuario = new Gson().fromJson(response.toString(), Usuario.class);
                //ObjectMapper mapper = new ObjectMapper();
             //   usuario = mapper.readValue(response.toString(), Usuario.class);
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
        }
        
    }

     
    
}
