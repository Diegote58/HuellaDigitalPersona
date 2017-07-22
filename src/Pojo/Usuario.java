package Pojo;
// Generated 02/05/2017 11:18:00 by Hibernate Tools 4.3.1


import java.io.Serializable;
import java.sql.Timestamp;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;



public class Usuario implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
     private int id_usuario;
     private String tipo_doc;
     private int nro_doc;
     private String nombre;
     private String apellido1;
     private String apellido2;
     private int matricula;
     private String categoria;
     private String mail;
     private String password;
     private String foto;
     private String telefono;
     private String sexo;
     private String fechaNacimiento;
     private Timestamp fechaAlta;
     private String grupoSanguineo;
     private String estadoCivil;
     private String rol;
     private String estado;
     
   /*  @JsonIgnore
     private String domicilio;
     @JsonIgnore
     private String historia;*/
     

    public Usuario() {
    }

    public Usuario(int idUsuario,String tipoDoc,int nro_doc,String nombre,String apellido1,String apellido2,int matricula,String categoria,String mail, String password,String foto,String telefono,String sexo,String fechaNacimiento,Timestamp fechaAlta,String grupoSanguineo,String estadoCivil,String rol,String estado) {
       this.tipo_doc = tipoDoc;
       this.nro_doc = nro_doc;
       this.nombre = nombre;
       this.apellido1 = apellido1;
       this.apellido2 = apellido2;
       this.matricula = matricula;
       this.categoria = categoria;
       this.mail = mail;
       this.password = password;
       this.foto = foto;
       this.telefono = telefono;
       this.sexo = sexo;
       this.fechaNacimiento = fechaNacimiento;
       this.fechaAlta = fechaAlta;
       this.grupoSanguineo = grupoSanguineo;
       this.estadoCivil = estadoCivil;
       this.rol = rol;
       this.estado = estado;
    }
   
    
     public Usuario(int id_usuario,int nro_doc,String nombre,String apellido1,String apellido2,String categoria,String mail,String estado) {
        this.id_usuario = id_usuario;
        this.nro_doc = nro_doc;
        this.nombre = nombre;
        this.apellido1 = apellido1;
        this.apellido2 = apellido2;
        this.categoria = categoria;
        this.mail = mail;
        this.estado = estado;
    }
     
    public int getId_usuario() {
        return this.id_usuario;
    }
    
    public void setId_usuario(int idUsuario) {
        this.id_usuario = idUsuario;
    }
    public String getTipoDoc() {
        return this.tipo_doc;
    }
    
    public void setTipoDoc(String tipoDoc) {
        this.tipo_doc = tipoDoc;
    }
    public int getNroDoc() {
        return this.nro_doc;
    }
    
    public void setNroDoc(int nro_doc) {
        this.nro_doc = nro_doc;
    }
    public String getNombre() {
        return this.nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public String getApellido1() {
        return this.apellido1;
    }
    
    public void setApellido1(String apellido1) {
        this.apellido1 = apellido1;
    }
    public String getApellido2() {
        return this.apellido2;
    }
    
    public void setApellido2(String apellido2) {
        this.apellido2 = apellido2;
    }
    public int getMatricula() {
        return this.matricula;
    }
    
    public void setMatricula(int matricula) {
        this.matricula = matricula;
    }
    public String getCategoria() {
        return this.categoria;
    }
    
    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }
    public String getMail() {
        return this.mail;
    }
    
    public void setMail(String mail) {
        this.mail = mail;
    }
    public String getPassword() {
        return this.password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    public String getFoto() {
        return this.foto;
    }
    
    public void setFoto(String foto) {
        this.foto = foto;
    }
    public String getTelefono() {
        return this.telefono;
    }
    
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
    public String getSexo() {
        return this.sexo;
    }
    
    public void setSexo(String sexo) {
        this.sexo = sexo;
    }
    public String getFechaNacimiento() {
        return this.fechaNacimiento;
    }
    
    public void setFechaNacimiento(String fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }
    
    //s@JsonSerialize(using = JsonDateSerialize.class)
    public Timestamp getFechaAlta() {
        return this.fechaAlta;
    }
    
    //@JsonDeserialize(using = JsonDateDeserialize.class,as = Timestamp.class)
    public void setFechaAlta(Timestamp fechaAlta) {
        this.fechaAlta = fechaAlta;
    }
    public String getGrupoSanguineo() {
        return this.grupoSanguineo;
    }
    
    public void setGrupoSanguineo(String grupoSanguineo) {
        this.grupoSanguineo = grupoSanguineo;
    }
    public String getEstadoCivil() {
        return this.estadoCivil;
    }
    
    public void setEstadoCivil(String estadoCivil) {
        this.estadoCivil = estadoCivil;
    }
    public String getRol() {
        return this.rol;
    }
    
    public void setRol(String rol) {
        this.rol = rol;
    }
    public String getEstado() {
        return this.estado;
    }
    
    public void setEstado(String estado) {
        this.estado = estado;
    }




}


