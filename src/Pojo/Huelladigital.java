package Pojo;
// Generated 02/05/2017 11:18:00 by Hibernate Tools 4.3.1

import java.io.InputStream;
import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import org.hibernate.annotations.Type;




@Entity
@Table(name = "huelladigital")
public class Huelladigital  implements java.io.Serializable {


     @Id
     @GeneratedValue(strategy = GenerationType.IDENTITY)
     private Integer id_huella;
     private int id_usuario;
     private String dedo;
     
     private byte[] huella;

    public Huelladigital() {
    }

	
    public Huelladigital(int dniUsuario, String dedo) {
        this.id_usuario = dniUsuario;
        this.dedo = dedo;
    }
    public Huelladigital(int dniUsuario, String dedo, byte[] huella) {
       this.id_usuario = dniUsuario;
       this.dedo = dedo;
       this.huella = huella;
    }

    public Integer getId_huella() {
        return id_huella;
    }

    public void setId_huella(Integer id_huella) {
        this.id_huella = id_huella;
    }

    public int getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(int id_usuario) {
        this.id_usuario = id_usuario;
    }

    public String getDedo() {
        return dedo;
    }

    public void setDedo(String dedo) {
        this.dedo = dedo;
    }

    public byte[] getHuella() {
        return huella;
    }

    public void setHuella(byte[] huella) {
        this.huella = huella;
    }
   
   

    @Override
    public String toString() {
        return "Huelladigital{" + "idHuella=" + id_huella + ", dniUsuario=" + id_usuario + ",dedo=" + dedo + ", huella=" + huella + '}';
    }

    public void setHuella(InputStream byteArray) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }




}


