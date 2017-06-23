/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Vistas;


import BD.ConexionBD;
import BD.HibernateConnector;
import DAO.HuellaDAO;
import DAO.UsuarioDAO;
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


public class CapturarHuella extends javax.swing.JFrame {

    
    private DPFPCapture Lector = DPFPGlobal.getCaptureFactory().createCapture();
    private DPFPEnrollment Reclutador = DPFPGlobal.getEnrollmentFactory().createEnrollment();
    private DPFPVerification Verificador = DPFPGlobal.getVerificationFactory().createVerification();
    private DPFPTemplate template;
    private String TEMPLATE_PROPERTY = "template";
    
    //otras variables
    public DPFPFeatureSet featuredSinInscripcion;
    public DPFPFeatureSet featuredVerificacion;
    private JLabel imageLabel;
    
   
    //Pasar Variables a otro Form   
    public void set_ID(String id){
        txt_id_user.setText(id);
    }
    
    public void SetRadioButtonText(){
        //izquiedo
        rb_menique_izquierdo.setToolTipText("menique_izquierdo");
        rb_anular_izquierdo.setToolTipText("anular_izquierdo");
        rb_medio_izquierdo.setToolTipText("medio_izquierdo");
        rb_indice_izquierdo.setToolTipText("indice_izquierdo");
        rb_pulgar_izquierdo.setToolTipText("pulgar_izquierdo");
        
        //derecho
        rb_menique_derecho.setToolTipText("menique_derecho");
        rb_anular_derecho.setToolTipText("anular_derecho");
        rb_medio_derecho.setToolTipText("medio_derecho");
        rb_indice_derecho.setToolTipText("indice_derecho");
        rb_pulgar_derecho.setToolTipText("pulgar_derecho");
    }
    
    
    
    //Inicializar Conexión.
    public CapturarHuella() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());   
            Connection c = con.Conectar();
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | UnsupportedLookAndFeelException e) {
            JOptionPane.showMessageDialog(null,"Imposible Modificar el Template Visual","LookAndFeel Invalido",JOptionPane.ERROR_MESSAGE);        
        }
        initComponents();
        SetRadioButtonText();
        //JOptionPane.showMessageDialog(this, "ID: "+this.getId_Usuario());
    }
        
    
   
    //Lee si la captura de la Huella es Correcta
    protected void iniciar(){
    Lector.addDataListener(new DPFPDataAdapter(){
        @Override
        public void dataAcquired(final DPFPDataEvent e){
            SwingUtilities.invokeLater(new Runnable(){
                @Override
                public void run(){
                EnviarTexto("La Huella Digital ha sido Capturada");
                ProcesarCaptura(e.getSample());
                }
            });
        }
    });
    
    //
    Lector.addReaderStatusListener(new DPFPReaderStatusAdapter(){
        @Override
        public void readerConnected(final DPFPReaderStatusEvent e){
            SwingUtilities.invokeLater(new Runnable(){
                @Override
                public void run(){
                EnviarTexto("El Lector de Huellas esta Conectado y Funcionando..."); 
                }
            });
        }
        @Override
        public void readerDisconnected(final DPFPReaderStatusEvent e){
            SwingUtilities.invokeLater(new Runnable(){
                @Override
                public void run(){
                EnviarTexto("El Lector de Huellas esta Desconectado..."); 
                }
            });
        }
    });
    
    
    Lector.addSensorListener(new DPFPSensorAdapter(){
        @Override
        public void fingerTouched(final DPFPSensorEvent e){
            SwingUtilities.invokeLater(new Runnable(){
                @Override
                public void run(){
                EnviarTexto("El Dedo esta sobre el Lector de Huellas"); 
                }
            });
        }
        public void fingerGone(final DPFPSensorEvent e){
            SwingUtilities.invokeLater(new Runnable(){
                @Override
                public void run(){
                EnviarTexto("El Dedo esta sobre el Lector de Huellas"); 
                }
            });
        }              
    });
    
    Lector.addErrorListener(new DPFPErrorAdapter(){
        @Override
        public void errorOccured(final DPFPErrorEvent e){
        SwingUtilities.invokeLater(new Runnable(){
                @Override
                public void run(){
                EnviarTexto("Error: " + e.getError()); 
                }
            });
        }
    });
    }
    
    
    
    public DPFPFeatureSet extraerCaracteristicas(DPFPSample sample, DPFPDataPurpose purpose){
        DPFPFeatureExtraction extractor = DPFPGlobal.getFeatureExtractionFactory().createFeatureExtraction();
        try {
            return extractor.createFeatureSet(sample, purpose);
        } catch (DPFPImageQualityException e) {
            JOptionPane.showMessageDialog(null,"Error: ", e.getMessage(),ERROR);
            return null;
        }
        
    }
    
    public Image crearImagenHuella(DPFPSample sample){
        try{
            return DPFPGlobal.getSampleConversionFactory().createImage(sample);
//            return DPFPGlobal.getSampleConversionFactory().createImage(sample);
        }catch(Exception e){
            System.err.println("ERROR al CREAR ImagenHuella" + e.getMessage());
            return null;
        }
    }
    
    public void dibujarHuella(Image image){
        int width = ImagenHuella.getWidth();
        int height = ImagenHuella.getHeight();
        //System.out.println("width: " + width + " height: " + height);
        ImagenHuella.setIcon(new ImageIcon(
                image.getScaledInstance(width, height,1)));
        repaint();        
    }
    
    public void estadoHuellas(){
    EnviarTexto("Muestra las huellas necesarias " + Reclutador.getFeaturesNeeded());    
    
    Color default_color = new Color(50, 210, 50); 
    switch(Reclutador.getFeaturesNeeded()){
        case 0: 
                /*lbl_4.setBackground(default_color);
                lbl_4.setOpaque(true);*/
                lbl_4.setIcon(new ImageIcon(this.getClass().getResource("/Images/Huella_Success.png")));
                btnVerificar.setVisible(true);
                break;
        
        case 1: 
                
                /*lbl_3.setBackground(default_color);
                lbl_3.setOpaque(true);*/
                lbl_3.setIcon(new ImageIcon(this.getClass().getResource("/Images/Huella_Success.png")));
            
                break;
        case 2: 
            
            //lbl_2.setBackground(default_color);
            //lbl_2.setOpaque(true);
            lbl_2.setIcon(new ImageIcon(this.getClass().getResource("/Images/Huella_Success.png")));
            break;
        
        case 3: 
            
                // lbl_1.setBackground(default_color);
                // lbl_1.setOpaque(true);
                 lbl_1.setIcon(new ImageIcon(this.getClass().getResource("/Images/Huella_Success.png")));
                break;
        
        default: break;
    }
    }
    
    public void EnviarTexto(String texto){
        txtArea.append(texto+"\n");
    }
    
    public void start(){
    Lector.startCapture();
        EnviarTexto("Utilizando el Lector de Huellas");
    }
    
    public void stop(){
    Lector.stopCapture();
        EnviarTexto("Lector de Huellas Detenido");
    }
    
    public DPFPTemplate getTemplate() {
        return template;
    }

    public void setTemplate(DPFPTemplate Template) {
        DPFPTemplate old = this.template;
        this.template = Template;
        firePropertyChange(TEMPLATE_PROPERTY, old, Template);
    }


    
    
    
    public void ProcesarCaptura(DPFPSample sample){
        //Procesar la Muestra de la Huella y crear conjunto de caracteristicas con el proposito de Inscripcion
        featuredSinInscripcion = extraerCaracteristicas(sample, DPFPDataPurpose.DATA_PURPOSE_ENROLLMENT);
        
        //Procesar la Muestra de la Huella y crear conjunto de caracteristicas con el proposito de Verificacion
        featuredVerificacion = extraerCaracteristicas(sample, DPFPDataPurpose.DATA_PURPOSE_VERIFICATION);
        
                
        //Procesar la Muestra de la Huella y añade a su reclutador si es BUENA
                if(featuredSinInscripcion != null){
                    try {
                        System.out.println("Las Caracteristicas de la Huella Han sido creadas");
                        Reclutador.addFeatures(featuredSinInscripcion);
                        
                        //Dibujar la Imagen de la HUELLA
                        Image image = crearImagenHuella(sample);
                        dibujarHuella(image);
                       // btnGuardar.setEnabled(true);
                        btnVerificar.setEnabled(true);
                        btnIdentificar.setEnabled(true);
                        
                    } catch (DPFPImageQualityException ex) {
                        System.out.println("ERROR: " + ex.getMessage());
                    }finally{
                    estadoHuellas();
                        System.out.println("SATTUS: " + Reclutador.getTemplateStatus());
                        //Comprueba si la plantilla se ha creado.
                        switch(Reclutador.getTemplateStatus()){
                            
                            case TEMPLATE_STATUS_READY: // informe de éxito, se detiene la captura de las huellas.
                                stop();
                                setTemplate(Reclutador.getTemplate());
                                EnviarTexto("La plantilla de la huella ha sido creada, ya puede identificarla o Guardarla");
                                btnVerificar.setEnabled(false);
                                btnIdentificar.setEnabled(true);
                                btnGuardar.setEnabled(true);
                                btnGuardar.grabFocus();
                                break;
                            
                            case TEMPLATE_STATUS_FAILED: 
                                Reclutador.clear();
                                stop();
                                estadoHuellas();
                                setTemplate(null);
                                ImagenHuella.setIcon(null);
                                JOptionPane.showMessageDialog(CapturarHuella.this,"Error al Leer la Huella, Vuelva a intentarlo");
                                start();
                                
                                break;                           
                                
                        
                        }
                    
                    }
                
                }
    
    }
    
    
    ConexionBD con = new ConexionBD();
    HuellaDAO huellaDAO = new HuellaDAO();
    
    public void guardarHuella(){
                
        //Obtiene los datos del Template de la Huella.
        ByteArrayInputStream datosHuella = new ByteArrayInputStream(template.serialize());
        Integer tamañoHuella = template.serialize().length;
        
        try {

                //retorna texto del boton seleccionado - ej: meñique-izq.
                String button = buttonIsSelected(); 
                if (button != "") {  
                    huellaDAO.guardarHuella(Integer.parseInt(txt_id_user.getText()),button, template,featuredVerificacion);   
                    JOptionPane.showMessageDialog(this, "Se Guardo la huella Correctamente.");
                }
            
            
            if(button == ""){JOptionPane.showMessageDialog(this, "Debe Seleccionar "
                    + "un dedo para registrar la Huella","Verificar Datos al guardar",
                    JOptionPane.PLAIN_MESSAGE,
                    new ImageIcon(this.getClass().getResource("/Images/Huella_Error.png")));}
            
            btnGuardar.setEnabled(true);
            btnVerificar.grabFocus();
            
            
        } catch (Exception e) {
            System.err.println("ERROR: Guardando Huella");
            
        }finally{
            con.Desconectar();
        }
        
    }
    
    public void VerificarHuellaPersona(String dedo){
        
        try {
            
            
             if (dedo != "") {  
                Usuario user = new Usuario();
                 user = huellaDAO.VerificarHuellaPersona(Integer.parseInt(txt_id_user.getText()), dedo,Verificador, featuredVerificacion);
                //verifica que exista el usuario
                if (user != null) {
                       JOptionPane.showMessageDialog(this, "La Huella se Verifica."
                               + "\nNombre: " + user.getApellido1() + " " + user.getApellido2()+ ", " + user.getNombre() 
                               + ".\nNro Doc: " + user.getNroDoc() + ".\nDedo: " + dedo+".",
                               "Identificación de Huellas", JOptionPane.PLAIN_MESSAGE,
                               new ImageIcon(this.getClass().getResource("/Images/Huella_Success.png")));
                      clear();
                } else {
                      //Si no encuentra alguna huella correspondiente al nombre lo indica con un mensaje
                      JOptionPane.showMessageDialog(this, "No existe ningún registro "
                              + "que coincida con la huella", "Verificacion de Huella", 
                              JOptionPane.PLAIN_MESSAGE,
                              new ImageIcon(this.getClass().getResource("/Images/Huella_Error.png")));
                      clear();
                }
           }
           
           if(dedo == ""){JOptionPane.showMessageDialog(this, "Debe Seleccionar "
                   + "un dedo para verificar la Huella","Verificar Datos al guardar",
                   JOptionPane.PLAIN_MESSAGE,new ImageIcon(this.getClass().getResource("/Images/Huella_Error.png")));}
           
        } catch (Exception e) {
            System.err.println("ERROR: Al Verificar");
        }finally{//con.Desconectar();   
        }
    }
    
    public void identificarHuella() throws IOException{
       
        try {
            Usuario user =  huellaDAO.IdentificarHuella(Verificador, featuredVerificacion);
            
            if (user != null) {
                JOptionPane.showMessageDialog(this, "La Huella se Identifica.\nNombre: " + user.getApellido1() + " " + user.getApellido2()+ ", " + user.getNombre() + ".\nNro Doc: "+user.getNroDoc()+".","Identificación de Huellas", JOptionPane.PLAIN_MESSAGE, new ImageIcon(this.getClass().getResource("/Images/Huella_Success.png")));
                clear();
            } else {
                //Si no encuentra alguna huella correspondiente al nombre lo indica con un mensaje
                JOptionPane.showMessageDialog(this, "No existe ningún registro de la Huella.", "Verificacion de Huella", JOptionPane.PLAIN_MESSAGE, new ImageIcon(this.getClass().getResource("/Images/Huella_Error.png")));
                clear();
            }
            
        } catch (Exception  e) {
             //Si ocurre un error lo indica en la consola
             System.err.println("Error al identificar huella dactilar."+e.getMessage());
        }finally{con.Desconectar();} 
    }

    public void run(){
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroupFinger = new javax.swing.ButtonGroup();
        javax.swing.JPanel panelHuella = new javax.swing.JPanel();
        ImagenHuella = new javax.swing.JLabel();
        panelAccion = new javax.swing.JPanel();
        btnGuardar = new javax.swing.JButton();
        btnIdentificar = new javax.swing.JButton();
        btnSalir = new javax.swing.JButton();
        btnClear = new javax.swing.JButton();
        btnPersonas = new javax.swing.JButton();
        btnVerificar = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtArea = new javax.swing.JTextArea();
        jPanel1 = new javax.swing.JPanel();
        javax.swing.JPanel jPanel2 = new javax.swing.JPanel();
        lbl_izquierda = new javax.swing.JLabel();
        rb_menique_izquierdo = new javax.swing.JRadioButton();
        rb_anular_izquierdo = new javax.swing.JRadioButton();
        rb_medio_izquierdo = new javax.swing.JRadioButton();
        rb_indice_izquierdo = new javax.swing.JRadioButton();
        rb_pulgar_izquierdo = new javax.swing.JRadioButton();
        jPanel3 = new javax.swing.JPanel();
        lbl_Derecha = new javax.swing.JLabel();
        rb_pulgar_derecho = new javax.swing.JRadioButton();
        rb_indice_derecho = new javax.swing.JRadioButton();
        rb_medio_derecho = new javax.swing.JRadioButton();
        rb_anular_derecho = new javax.swing.JRadioButton();
        rb_menique_derecho = new javax.swing.JRadioButton();
        panelChecadas = new javax.swing.JPanel();
        lbl_1 = new javax.swing.JLabel();
        lbl_2 = new javax.swing.JLabel();
        lbl_3 = new javax.swing.JLabel();
        lbl_4 = new javax.swing.JLabel();
        txt_id_user = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Registro de Huella Digital");
        setAlwaysOnTop(true);
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setLocation(new java.awt.Point(0, 0));
        setMaximizedBounds(new java.awt.Rectangle(0, 0, 1920, 1080));
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        panelHuella.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED), "Huellas Digitales", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Verdana", 1, 11))); // NOI18N
        panelHuella.setToolTipText("");
        panelHuella.setPreferredSize(new java.awt.Dimension(120, 120));

        ImagenHuella.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        ImagenHuella.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        ImagenHuella.setRequestFocusEnabled(false);
        ImagenHuella.setVerifyInputWhenFocusTarget(false);

        javax.swing.GroupLayout panelHuellaLayout = new javax.swing.GroupLayout(panelHuella);
        panelHuella.setLayout(panelHuellaLayout);
        panelHuellaLayout.setHorizontalGroup(
            panelHuellaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelHuellaLayout.createSequentialGroup()
                .addGap(48, 48, 48)
                .addComponent(ImagenHuella, javax.swing.GroupLayout.PREFERRED_SIZE, 280, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panelHuellaLayout.setVerticalGroup(
            panelHuellaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelHuellaLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(ImagenHuella, javax.swing.GroupLayout.PREFERRED_SIZE, 360, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        panelAccion.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, null, null, null, new java.awt.Color(240, 240, 240)), "Panel de Acción", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Consolas", 1, 12))); // NOI18N

        btnGuardar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Huella_Save_3.png"))); // NOI18N
        btnGuardar.setText("GUARDAR");
        btnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarActionPerformed(evt);
            }
        });

        btnIdentificar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Huella_Search.png"))); // NOI18N
        btnIdentificar.setText("IDENTIFICAR");
        btnIdentificar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnIdentificarActionPerformed(evt);
            }
        });

        btnSalir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/exit_close.png"))); // NOI18N
        btnSalir.setText("SALIR");
        btnSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalirActionPerformed(evt);
            }
        });

        btnClear.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Huella_Clean.png"))); // NOI18N
        btnClear.setText("CLEAR");
        btnClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearActionPerformed(evt);
            }
        });

        btnPersonas.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/icono_user.png"))); // NOI18N
        btnPersonas.setText("USUARIOS");
        btnPersonas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPersonasActionPerformed(evt);
            }
        });

        btnVerificar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Huella_Success.png"))); // NOI18N
        btnVerificar.setText("VERIFICAR");
        btnVerificar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVerificarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelAccionLayout = new javax.swing.GroupLayout(panelAccion);
        panelAccion.setLayout(panelAccionLayout);
        panelAccionLayout.setHorizontalGroup(
            panelAccionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelAccionLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnPersonas)
                .addGap(18, 18, 18)
                .addComponent(btnGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 213, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnVerificar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(btnIdentificar, javax.swing.GroupLayout.PREFERRED_SIZE, 206, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnClear, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnSalir, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29))
        );
        panelAccionLayout.setVerticalGroup(
            panelAccionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelAccionLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelAccionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnIdentificar, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSalir)
                    .addComponent(btnClear, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnPersonas, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnVerificar, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        txtArea.setColumns(20);
        txtArea.setRows(5);
        jScrollPane1.setViewportView(txtArea);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 399, Short.MAX_VALUE)
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Mano Izquierda", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Verdana", 1, 13))); // NOI18N
        jPanel2.setToolTipText("");

        lbl_izquierda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Mano_Izquierda.png"))); // NOI18N
        lbl_izquierda.setVerticalTextPosition(javax.swing.SwingConstants.TOP);

        buttonGroupFinger.add(rb_menique_izquierdo);
        rb_menique_izquierdo.setToolTipText("");

        buttonGroupFinger.add(rb_anular_izquierdo);
        rb_anular_izquierdo.setToolTipText("");

        buttonGroupFinger.add(rb_medio_izquierdo);
        rb_medio_izquierdo.setToolTipText("");

        buttonGroupFinger.add(rb_indice_izquierdo);
        rb_indice_izquierdo.setToolTipText("");

        buttonGroupFinger.add(rb_pulgar_izquierdo);
        rb_pulgar_izquierdo.setToolTipText("");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(35, 35, 35)
                        .addComponent(rb_menique_izquierdo)
                        .addGap(27, 27, 27)
                        .addComponent(rb_anular_izquierdo)
                        .addGap(30, 30, 30)
                        .addComponent(rb_medio_izquierdo)
                        .addGap(30, 30, 30)
                        .addComponent(rb_indice_izquierdo)
                        .addGap(53, 53, 53)
                        .addComponent(rb_pulgar_izquierdo))
                    .addComponent(lbl_izquierda, javax.swing.GroupLayout.PREFERRED_SIZE, 310, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(43, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(59, 59, 59)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(rb_menique_izquierdo)
                    .addComponent(rb_medio_izquierdo)
                    .addComponent(rb_anular_izquierdo)
                    .addComponent(rb_indice_izquierdo)
                    .addComponent(rb_pulgar_izquierdo))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lbl_izquierda, javax.swing.GroupLayout.PREFERRED_SIZE, 340, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(68, Short.MAX_VALUE))
        );

        lbl_izquierda.getAccessibleContext().setAccessibleName("Mano Izquierda");

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Mano Derecha", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Verdana", 1, 13))); // NOI18N

        lbl_Derecha.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Mano_Derecha.png"))); // NOI18N

        buttonGroupFinger.add(rb_pulgar_derecho);

        buttonGroupFinger.add(rb_indice_derecho);

        buttonGroupFinger.add(rb_medio_derecho);

        buttonGroupFinger.add(rb_anular_derecho);

        buttonGroupFinger.add(rb_menique_derecho);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(36, 36, 36)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lbl_Derecha, javax.swing.GroupLayout.PREFERRED_SIZE, 310, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(33, 33, 33)
                        .addComponent(rb_pulgar_derecho)
                        .addGap(53, 53, 53)
                        .addComponent(rb_indice_derecho)
                        .addGap(26, 26, 26)
                        .addComponent(rb_medio_derecho)
                        .addGap(26, 26, 26)
                        .addComponent(rb_anular_derecho)
                        .addGap(26, 26, 26)
                        .addComponent(rb_menique_derecho)))
                .addContainerGap(36, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(52, 52, 52)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(rb_pulgar_derecho)
                    .addComponent(rb_indice_derecho)
                    .addComponent(rb_medio_derecho)
                    .addComponent(rb_anular_derecho)
                    .addComponent(rb_menique_derecho))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lbl_Derecha)
                .addContainerGap(70, Short.MAX_VALUE))
        );

        panelChecadas.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Muestas de huellas", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Verdana", 1, 13))); // NOI18N

        lbl_1.setBackground(java.awt.Color.lightGray);
        lbl_1.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        lbl_1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl_1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Huella_Scan.png"))); // NOI18N
        lbl_1.setToolTipText("");
        lbl_1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        lbl_1.setOpaque(true);

        lbl_2.setBackground(java.awt.Color.lightGray);
        lbl_2.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        lbl_2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl_2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Huella_Scan.png"))); // NOI18N
        lbl_2.setToolTipText("");
        lbl_2.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        lbl_2.setOpaque(true);

        lbl_3.setBackground(java.awt.Color.lightGray);
        lbl_3.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        lbl_3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl_3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Huella_Scan.png"))); // NOI18N
        lbl_3.setToolTipText("");
        lbl_3.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        lbl_3.setOpaque(true);

        lbl_4.setBackground(java.awt.Color.lightGray);
        lbl_4.setFont(new java.awt.Font("Trebuchet MS", 1, 18)); // NOI18N
        lbl_4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl_4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Huella_Scan.png"))); // NOI18N
        lbl_4.setToolTipText("");
        lbl_4.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        lbl_4.setOpaque(true);

        javax.swing.GroupLayout panelChecadasLayout = new javax.swing.GroupLayout(panelChecadas);
        panelChecadas.setLayout(panelChecadasLayout);
        panelChecadasLayout.setHorizontalGroup(
            panelChecadasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelChecadasLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lbl_1, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lbl_2, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lbl_3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lbl_4, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        panelChecadasLayout.setVerticalGroup(
            panelChecadasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lbl_4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(lbl_3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(lbl_1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(lbl_2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        txt_id_user.setEditable(false);
        txt_id_user.setText("ID_User");
        txt_id_user.setUI(null);
        txt_id_user.setFocusable(false);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(txt_id_user, javax.swing.GroupLayout.PREFERRED_SIZE, 257, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(panelAccion, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jScrollPane1)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(31, 31, 31)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(panelChecadas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(panelHuella, javax.swing.GroupLayout.DEFAULT_SIZE, 391, Short.MAX_VALUE))
                                .addGap(39, 39, 39)
                                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(28, 28, 28))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(7, 7, 7)
                                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(panelHuella, javax.swing.GroupLayout.PREFERRED_SIZE, 399, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(panelChecadas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(panelAccion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txt_id_user, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        setSize(new java.awt.Dimension(1334, 917));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalirActionPerformed
        System.exit(0);
    }//GEN-LAST:event_btnSalirActionPerformed

    private void btnVerificarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVerificarActionPerformed

        String button = "";
        button = buttonIsSelected();
        VerificarHuellaPersona(button);
           
        Reclutador.clear();
    }//GEN-LAST:event_btnVerificarActionPerformed

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed
        
        try {
        
            guardarHuella();
            Reclutador.clear();
            ImagenHuella.setIcon(null);
            start();
        } catch (Exception e) {
            System.err.println("ERROR en GUARDAR: " + e.getMessage());
        }
    }//GEN-LAST:event_btnGuardarActionPerformed

    private void btnIdentificarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnIdentificarActionPerformed
           try {
        identificarHuella();
        Reclutador.clear();
         } catch (IOException ex) {
         Logger.getLogger(CapturarHuella.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnIdentificarActionPerformed

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        // Inicializa el Formulario.
       /* iniciar();
	start();
        estadoHuellas();
        btnGuardar.setEnabled(false);
        btnIdentificar.setEnabled(false);
        btnVerificar.setEnabled(false);
        btnSalir.grabFocus();*/
    }//GEN-LAST:event_formWindowOpened

    public void EventoInicializar(){
       // Inicializa el Formulario.
        iniciar();
	start();
        estadoHuellas();
        btnGuardar.setEnabled(false);
        btnIdentificar.setEnabled(false);
        btnVerificar.setEnabled(false);
        btnSalir.grabFocus();
    }
    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
      stop();
    }//GEN-LAST:event_formWindowClosing

    private void btnClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearActionPerformed
        
        
        /*lbl_1.setBackground(Color.LIGHT_GRAY);        lbl_1.setOpaque(true);
        lbl_2.setBackground(Color.LIGHT_GRAY);        lbl_2.setOpaque(true);
        lbl_3.setBackground(Color.LIGHT_GRAY);        lbl_3.setOpaque(true);
        lbl_4.setBackground(Color.LIGHT_GRAY);        lbl_4.setOpaque(true);*/
        
        clear();
    }//GEN-LAST:event_btnClearActionPerformed

    //Limpia los datos de las huellas capturadas y reinicia el lector.
    private void clear(){
         lbl_1.setIcon(new ImageIcon(this.getClass().getResource("/Images/Huella_Scan.png")));
        lbl_2.setIcon(new ImageIcon(this.getClass().getResource("/Images/Huella_Scan.png")));
        lbl_3.setIcon(new ImageIcon(this.getClass().getResource("/Images/Huella_Scan.png")));
        lbl_4.setIcon(new ImageIcon(this.getClass().getResource("/Images/Huella_Scan.png")));
        btnGuardar.setEnabled(false);
        
        Reclutador.clear();
        stop();
        start();
    }
    
    //Verifica que haya seleccinado un dedo para Guardar o Verificar la huella.
    private String buttonIsSelected(){
        
        int i = 0;
        AbstractButton buttonn = null;
        for (Enumeration<AbstractButton> GroupButtonFinger = buttonGroupFinger.getElements(); GroupButtonFinger.hasMoreElements();) {
                
                buttonn = GroupButtonFinger.nextElement();

                if (buttonn.isSelected()) {
                    i++;
                }
            }
        if (i == 0) {
            return "";
        } else {
            return buttonn.getToolTipText();
        }
    }
    
    private void btnPersonasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPersonasActionPerformed
        UsuariosForm uf = new UsuariosForm();
        uf.setVisible(true);
        stop();
        this.dispose();
    }//GEN-LAST:event_btnPersonasActionPerformed

    
    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(CapturarHuella.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(CapturarHuella.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(CapturarHuella.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(CapturarHuella.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new CapturarHuella().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel ImagenHuella;
    private javax.swing.JButton btnClear;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnIdentificar;
    private javax.swing.JButton btnPersonas;
    private javax.swing.JButton btnSalir;
    private javax.swing.JButton btnVerificar;
    private javax.swing.ButtonGroup buttonGroupFinger;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lbl_1;
    private javax.swing.JLabel lbl_2;
    private javax.swing.JLabel lbl_3;
    private javax.swing.JLabel lbl_4;
    private javax.swing.JLabel lbl_Derecha;
    private javax.swing.JLabel lbl_izquierda;
    private javax.swing.JPanel panelAccion;
    private javax.swing.JPanel panelChecadas;
    private javax.swing.JRadioButton rb_anular_derecho;
    private javax.swing.JRadioButton rb_anular_izquierdo;
    private javax.swing.JRadioButton rb_indice_derecho;
    private javax.swing.JRadioButton rb_indice_izquierdo;
    private javax.swing.JRadioButton rb_medio_derecho;
    private javax.swing.JRadioButton rb_medio_izquierdo;
    private javax.swing.JRadioButton rb_menique_derecho;
    private javax.swing.JRadioButton rb_menique_izquierdo;
    private javax.swing.JRadioButton rb_pulgar_derecho;
    private javax.swing.JRadioButton rb_pulgar_izquierdo;
    private javax.swing.JTextArea txtArea;
    private javax.swing.JTextField txt_id_user;
    // End of variables declaration//GEN-END:variables

   
}
