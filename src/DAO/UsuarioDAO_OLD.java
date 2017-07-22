/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import BD.HibernateConnector;
import Pojo.Usuario;
import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import jdk.nashorn.internal.parser.JSONParser;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.json.JSONObject;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONException;


public class UsuarioDAO_OLD {
    
    
      
    public List<Usuario> listUsuarioOLD() {
        Session session = null;
        try {
            session = HibernateConnector.getInstance().getSession();
            Query query = session.createQuery("from Usuario");
            
            List list = query.list();
            
            if (list != null && list.isEmpty()) {
                return null;
            } else {
                System.out.println("Lista Usuarios: " + list);
                return (List<Usuario>)list;
            }
            
        } catch (Exception e) {
            System.err.println("ERROR: Lista de Usuarios" + e.getMessage());
            return null;
        }finally{
            session.close();
        }
    }
    
    public List<Usuario> listUsuario() {
       
        try {
            
            String httpURL = "http://localhost:8080/SistemaHistoria/rest/usuarios/listar";
            
            HttpClient client = HttpClientBuilder.create().build();
            HttpGet request = new HttpGet(httpURL);
            HttpResponse response = client.execute(request);

            // Get the response
            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

            String line = "";
            String buffer = "";
            while ((line = rd.readLine()) != null) {
                //textView.append(line);
                
                System.out.println(line);
                buffer = line;
            }
                      
            JSONObject json = new JSONObject(buffer);
            Usuario user = new Usuario((Integer)json.get("id_usuario"),(Integer)json.get("id_usuario"),(String)json.get("nombre"), 
                    (String)json.get("apellido1"),(String)json.get("apellido2"),
                    (String)json.get("categoria"), (String)json.get("mail"),
                    (String)json.get("estado"));//new ObjectMapper().readValue(buffer,Usuario.class);
            

            System.out.println("UsuarioDAO: " + user.toString());
            List<Usuario> list = new ArrayList<Usuario>();
            list.add(user);
            
            if (list != null && list.isEmpty()) {
                return null;
            } else {
                System.out.println("Lista Usuarios: " + list);
                return (List<Usuario>)list;
            }
            
        } catch (Exception e) {
            System.err.println("ERROR: Lista de Usuarios" + e.getMessage());
            return null;
        }
    }
    
    public Usuario listUsuarioConection() throws JSONException {
       
         try {

                URL url = new URL("http://localhost:8080/SistemaHistoria/rest/usuarios/6589741");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		conn.setRequestProperty("Accept", "application/json");

                
            
		if (conn.getResponseCode() != 200) {
			throw new RuntimeException("Failed : HTTP error code : "
					+ conn.getResponseCode());
		}

		BufferedReader br = new BufferedReader(new InputStreamReader(
			(conn.getInputStream())));

		String output;
                String buffer = "";
		System.out.println("Output from Server .... \n");
		while ((output = br.readLine()) != null) {
			System.out.println(output);
                        buffer = output;
		}
		conn.disconnect();

                JSONObject json = new JSONObject(buffer);
                    Usuario user = new Usuario((Integer)json.get("id_usuario"),(Integer)json.get("id_usuario"),(String)json.get("nombre"), 
                    (String)json.get("apellido1"),(String)json.get("apellido2"),
                    (String)json.get("categoria"), (String)json.get("mail"),
                    (String)json.get("estado"));
                    
                return user;
	  } catch (MalformedURLException e) {

		e.printStackTrace();
                return null;
	  } catch (IOException e) {

		e.printStackTrace();
                return null;
	  }

    }
    
    
    
    public Usuario findUserById(int id) {
        Session session = null;
        try {
            session = HibernateConnector.getInstance().getSession();
            Criteria criteria = session.createCriteria(Usuario.class);
            criteria.add(Restrictions.eq("id_usuario", id));
            return (Usuario) criteria.uniqueResult();
        } catch (Exception e) {
            System.out.println("DAO.UsuarioDAO.findUserById(): " + e.getMessage());
            return null;
        }finally{
            session.close();
        }
                
    }

    public Usuario findUserByDni(int dni) {
        Session session = null;
        try {
            session = HibernateConnector.getInstance().getSession();
            Criteria criteria = session.createCriteria(Usuario.class);
            criteria.add(Restrictions.eq("nro_doc", dni));
            return (Usuario) criteria.uniqueResult();
        } catch (Exception e) {
            System.out.println("DAO.UsuarioDAO.findUserByDni(): " + e.getMessage());
            return null;
        }finally{
            session.close();
        }
    }

    public List<Usuario> FindUser(String cadena){
        Session session = null;
        try {
            session = HibernateConnector.getInstance().getSession();
            Query query = session.createSQLQuery("select * from usuario where concat(nro_doc,' ',nombre,' ',apellido1,' ', apellido2) LIKE '%"+cadena +"%';");
            
           List list = query.list();
            
            if (list != null && list.isEmpty()) {
                return null;
            } else {
                System.out.println("Lista Usuarios LIKE: " + list);
                return (List<Usuario>)list;
            }
        } catch (Exception e) {
            System.out.println("DAO.UsuarioDAO.findUser(): " + e.getMessage());
            return null;
        }finally{
            session.close();
        }
    }   
    
    public void updateUser(Usuario us) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public Usuario addUser(Usuario us) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    
    public void deleteUser(int id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
}
