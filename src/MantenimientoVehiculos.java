

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */

/**
 *
 * @author Jeanc
 */
import javax.swing.*;
import java.awt.event.*;
import java.io.*;

public class MantenimientoVehiculos extends javax.swing.JFrame {

    private ButtonGroup grupoStatus;
    
    private void controlarBotones(boolean mostrarBuscar, boolean mostrarGuardar, boolean mostrarLimpiar) {
    BtnBuscar.setVisible(mostrarBuscar);
    BtnGuardar.setVisible(mostrarGuardar);
    BtnLimpiar.setVisible(mostrarLimpiar);
}

    public MantenimientoVehiculos() {
        initComponents();
        controlarBotones(true, false, false);
        

        grupoStatus = new ButtonGroup();
        grupoStatus.add(Veh_disponible);
        grupoStatus.add(Veh_nodisponible);
        Veh_disponible.setEnabled(false);
        Veh_nodisponible.setEnabled(false);

        TxtDescripGama.setEditable(false);

        CmbTipoVehiculo.removeAllItems();
        CmbTipoVehiculo.addItem("Seleccione");
        CmbTipoVehiculo.addItem("Turístico");
        CmbTipoVehiculo.addItem("Normal");

        CmbTipoMotor.removeAllItems();
        CmbTipoMotor.addItem("Seleccione");
        CmbTipoMotor.addItem("Diésel");
        CmbTipoMotor.addItem("Gasolina");
        
        TxtGama.addKeyListener(new java.awt.event.KeyAdapter() {
    @Override
    public void keyReleased(java.awt.event.KeyEvent evt) {
        buscarGamaEnTiempoReal();
    }
});

     

        TxtMatricula.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                if (!TxtMatricula.getText().trim().isEmpty()) {
                    buscarVehiculo();
                }
            }
        });
    }

    

    private void buscarVehiculo() {
        String matricula = TxtMatricula.getText().trim();
        boolean encontrado = false;

        try (BufferedReader br = new BufferedReader(new FileReader("datos/Vehiculos.txt"))) {
            String linea;

            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split("\\|");

                if (datos.length >= 13 && datos[0].equalsIgnoreCase(matricula)) {
                    TxtMarca.setText(datos[1]);
                    TxtModelo.setText(datos[2]);
                    CmbTipoVehiculo.setSelectedItem(datos[3]);
                    CmbTipoMotor.setSelectedItem(datos[4]);
                    TxtGama.setText(datos[5]);
                    Color_Veh.setText(datos[6]);

                    if (Boolean.parseBoolean(datos[7])) {
                        Veh_disponible.setSelected(true);
                    } else {
                        Veh_nodisponible.setSelected(true);
                    }

                    Techo_Electrico.setSelected(Boolean.parseBoolean(datos[8]));
                    Aire_Acondicionado.setSelected(Boolean.parseBoolean(datos[9]));
                    Interior_Cuero.setSelected(Boolean.parseBoolean(datos[10]));
                    Cambio_Auto.setSelected(Boolean.parseBoolean(datos[11]));
                    TxtDescripVehiculo.setText(datos[12]);

                    buscarGama();
                    encontrado = true;
                    break;
                }
            }
        } catch (IOException e) {
            
        }

        if (encontrado) {
            JOptionPane.showMessageDialog(this, "Modificando");
        } else {
            JOptionPane.showMessageDialog(this, "Creando");
            limpiarCamposMenosMatricula();
        }
        controlarBotones(false, true, true);
    }

    private void buscarGama() {
    String idGama = TxtGama.getText().trim();

    if (idGama.isEmpty()) {
        TxtDescripGama.setText("");
        return;
    }

    boolean existe = false;

    try (BufferedReader br = new BufferedReader(new FileReader("datos/gamas.txt"))) {
        String linea;

        while ((linea = br.readLine()) != null) {
            String[] datos = linea.split(";");

            if (datos.length >= 3 && datos[0].equals(idGama)) {
                TxtDescripGama.setText(datos[1]);
                TxtPrecio.setText(datos[2]);
                existe = true;
                break;
            }
        }
    } catch (IOException e) {
        JOptionPane.showMessageDialog(this, "Archivo gamas.txt no encontrado");
        TxtDescripGama.setText("");
        return;
    }

    if (!existe) {
        JOptionPane.showMessageDialog(this, "Id Gama no existe");
        TxtDescripGama.setText("");
    }
}
    private void buscarGamaEnTiempoReal() {
    String idGama = TxtGama.getText().trim();

    if (idGama.isEmpty()) {
        TxtDescripGama.setText("");
        TxtPrecio.setText("");
        return;
    }

    boolean encontrada = false;

    try (BufferedReader br = new BufferedReader(new FileReader("datos/gamas.txt"))) {
        String linea;

        while ((linea = br.readLine()) != null) {
            String[] datos = linea.split(";");

           
            if (datos.length >= 3 && datos[0].trim().equalsIgnoreCase(idGama)) {
                TxtDescripGama.setText(datos[1].trim());
                TxtPrecio.setText(datos[2].trim());
                encontrada = true;
                break;
            }
        }

    } catch (IOException e) {
        JOptionPane.showMessageDialog(this, "No se pudo leer el archivo de gamas");
        TxtDescripGama.setText("");
        TxtPrecio.setText("");
        return;
    }

    if (!encontrada) {
        TxtDescripGama.setText("");
        TxtPrecio.setText("");
    }
}
    


    private String obtenerRegistro() {
        return TxtMatricula.getText().trim() + "|" +
               TxtMarca.getText().trim() + "|" +
               TxtModelo.getText().trim() + "|" +
               CmbTipoVehiculo.getSelectedItem().toString() + "|" +
               CmbTipoMotor.getSelectedItem().toString() + "|" +
               TxtGama.getText().trim() + "|" +
               Color_Veh.getText().trim() + "|" +
               Veh_disponible.isSelected() + "|" +
               Techo_Electrico.isSelected() + "|" +
               Aire_Acondicionado.isSelected() + "|" +
               Interior_Cuero.isSelected() + "|" +
               Cambio_Auto.isSelected() + "|" +
               TxtDescripVehiculo.getText().trim();
    }
    
    private boolean validarCampos() {
    if (TxtMatricula.getText().trim().isEmpty()) {
        JOptionPane.showMessageDialog(this, "La matrícula es obligatoria");
        TxtMatricula.requestFocus();
        return false;
    }

    if (TxtMarca.getText().trim().isEmpty()) {
        JOptionPane.showMessageDialog(this, "La marca es obligatoria");
        TxtMarca.requestFocus();
        return false;
    }

    if (TxtModelo.getText().trim().isEmpty()) {
        JOptionPane.showMessageDialog(this, "El modelo es obligatorio");
        TxtModelo.requestFocus();
        return false;
    }

    if (CmbTipoVehiculo.getSelectedIndex() == 0) {
        JOptionPane.showMessageDialog(this, "Debe seleccionar el tipo de vehículo");
        CmbTipoVehiculo.requestFocus();
        return false;
    }

    if (CmbTipoMotor.getSelectedIndex() == 0) {
        JOptionPane.showMessageDialog(this, "Debe seleccionar el tipo de motor");
        CmbTipoMotor.requestFocus();
        return false;
    }

    if (TxtGama.getText().trim().isEmpty()) {
        JOptionPane.showMessageDialog(this, "La gama es obligatoria");
        TxtGama.requestFocus();
        return false;
    }

    if (TxtDescripVehiculo.getText().trim().isEmpty()) {
        JOptionPane.showMessageDialog(this, "La descripción del vehículo es obligatoria");
        TxtDescripVehiculo.requestFocus();
        return false;
    }

    return true;
}

    private void guardarDatos() {
        if (!validarCampos()) {
            return;
        }

        File archivo = new File("datos/Vehiculos.txt");
        File temporal = new File("Vehiculos_temp.txt");
        boolean actualizado = false;

        try (
            PrintWriter pw = new PrintWriter(new FileWriter(temporal));
            BufferedReader br = archivo.exists() ? new BufferedReader(new FileReader(archivo)) : null
        ) {
            String matricula = TxtMatricula.getText().trim();

            if (br != null) {
                String linea;

                while ((linea = br.readLine()) != null) {
                    String[] datos = linea.split("\\|");

                    if (datos.length >= 1 && datos[0].equalsIgnoreCase(matricula)) {
                        pw.println(obtenerRegistro());
                        actualizado = true;
                    } else {
                        pw.println(linea);
                    }
                }
            }

            if (!actualizado) {
                pw.println(obtenerRegistro());
            }

        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error al guardar: " + e.getMessage());
            return;
        }

        if (archivo.exists()) {
            archivo.delete();
        }

        if (!temporal.renameTo(archivo)) {
            JOptionPane.showMessageDialog(this, "No se pudo actualizar el archivo");
            return;
        }

        if (actualizado) {
            JOptionPane.showMessageDialog(this, "Registro modificado correctamente");
        } else {
            JOptionPane.showMessageDialog(this, "Registro guardado correctamente");
        }

        limpiarCampos();
       
controlarBotones(true, false, false);
    }

    private void limpiarCampos() {
        TxtMatricula.setText("");
        TxtMarca.setText("");
        TxtModelo.setText("");
        Color_Veh.setText("");
        TxtDescripVehiculo.setText("");
        TxtDescripGama.setText("");
        TxtPrecio.setText("");

        CmbTipoVehiculo.setSelectedIndex(0);
        CmbTipoMotor.setSelectedIndex(0);
        TxtGama.setText("");

        grupoStatus.clearSelection();

        Techo_Electrico.setSelected(false);
        Aire_Acondicionado.setSelected(false);
        Interior_Cuero.setSelected(false);
        Cambio_Auto.setSelected(false);
    }

    private void limpiarCamposMenosMatricula() {
        TxtMarca.setText("");
        TxtModelo.setText("");
        Color_Veh.setText("");
        TxtDescripVehiculo.setText("");
        TxtDescripGama.setText("");

        CmbTipoVehiculo.setSelectedIndex(0);
        CmbTipoMotor.setSelectedIndex(0);
        TxtGama.setText("");

        grupoStatus.clearSelection();

        Techo_Electrico.setSelected(false);
        Aire_Acondicionado.setSelected(false);
        Interior_Cuero.setSelected(false);
        Cambio_Auto.setSelected(false);
    }
    
        public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(MantenimientoVehiculos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MantenimientoVehiculos().setVisible(true);
            }
        });
    }



    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        TxtMatricula = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        TxtMarca = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        TxtModelo = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        CmbTipoVehiculo = new javax.swing.JComboBox<>();
        jLabel6 = new javax.swing.JLabel();
        CmbTipoMotor = new javax.swing.JComboBox<>();
        jLabel7 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        Color_Veh = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        Veh_disponible = new javax.swing.JRadioButton();
        Veh_nodisponible = new javax.swing.JRadioButton();
        Techo_Electrico = new javax.swing.JCheckBox();
        Aire_Acondicionado = new javax.swing.JCheckBox();
        Interior_Cuero = new javax.swing.JCheckBox();
        Cambio_Auto = new javax.swing.JCheckBox();
        jLabel8 = new javax.swing.JLabel();
        TxtDescripVehiculo = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        TxtDescripGama = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        TxtPrecio = new javax.swing.JTextField();
        BtnAtras = new javax.swing.JButton();
        BtnLimpiar = new javax.swing.JButton();
        BtnBuscar = new javax.swing.JButton();
        BtnGuardar = new javax.swing.JButton();
        TxtGama = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel2.setText("Matricula:");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, -1, -1));

        TxtMatricula.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TxtMatriculaActionPerformed(evt);
            }
        });
        jPanel1.add(TxtMatricula, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 20, 130, -1));

        jLabel3.setText("Marca: ");
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 60, -1, -1));
        jPanel1.add(TxtMarca, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 60, 130, -1));

        jLabel4.setText("Modelo: ");
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 100, -1, -1));
        jPanel1.add(TxtModelo, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 100, 130, -1));

        jLabel5.setText("Tipo de Vehiculo: ");
        jPanel1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 20, -1, -1));

        CmbTipoVehiculo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jPanel1.add(CmbTipoVehiculo, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 20, 120, -1));

        jLabel6.setText("Tipo de motor: ");
        jPanel1.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 60, -1, -1));

        CmbTipoMotor.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jPanel1.add(CmbTipoMotor, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 60, 120, -1));

        jLabel7.setText("Gama: ");
        jPanel1.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 100, -1, -1));

        jLabel9.setText("Color:");
        jPanel1.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 140, -1, -1));
        jPanel1.add(Color_Veh, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 140, 130, -1));

        jLabel10.setText("Status:");
        jPanel1.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 140, -1, -1));

        Veh_disponible.setText("Disponible");
        Veh_disponible.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Veh_disponibleActionPerformed(evt);
            }
        });
        jPanel1.add(Veh_disponible, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 140, -1, -1));

        Veh_nodisponible.setText("No disponible");
        Veh_nodisponible.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Veh_nodisponibleActionPerformed(evt);
            }
        });
        jPanel1.add(Veh_nodisponible, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 140, -1, -1));

        Techo_Electrico.setText("Techo electrico");
        jPanel1.add(Techo_Electrico, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 180, -1, -1));

        Aire_Acondicionado.setText("Aire acondicionado");
        jPanel1.add(Aire_Acondicionado, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 180, -1, -1));

        Interior_Cuero.setText("Interior de cuero");
        jPanel1.add(Interior_Cuero, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 180, -1, -1));

        Cambio_Auto.setText("Cambio automatico");
        jPanel1.add(Cambio_Auto, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 180, -1, -1));

        jLabel8.setText("Descripcion del vehiculo:");
        jPanel1.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 220, -1, -1));
        jPanel1.add(TxtDescripVehiculo, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 220, 170, -1));

        jLabel11.setText("Descripcion de Gama: ");
        jPanel1.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 250, -1, -1));
        jPanel1.add(TxtDescripGama, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 250, 170, -1));

        jLabel1.setText("Precio");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 220, -1, -1));

        TxtPrecio.setEditable(false);
        jPanel1.add(TxtPrecio, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 220, 90, -1));

        BtnAtras.setBackground(new java.awt.Color(255, 102, 102));
        BtnAtras.setText("Atras");
        BtnAtras.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnAtrasActionPerformed(evt);
            }
        });
        jPanel1.add(BtnAtras, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 310, -1, -1));

        BtnLimpiar.setBackground(new java.awt.Color(204, 255, 153));
        BtnLimpiar.setText("Limpiar");
        BtnLimpiar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnLimpiarActionPerformed(evt);
            }
        });
        jPanel1.add(BtnLimpiar, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 310, -1, -1));

        BtnBuscar.setBackground(new java.awt.Color(153, 204, 255));
        BtnBuscar.setText("Buscar");
        BtnBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnBuscarActionPerformed(evt);
            }
        });
        jPanel1.add(BtnBuscar, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 310, -1, -1));

        BtnGuardar.setBackground(new java.awt.Color(255, 255, 153));
        BtnGuardar.setText("Guardar");
        BtnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnGuardarActionPerformed(evt);
            }
        });
        jPanel1.add(BtnGuardar, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 310, -1, -1));
        jPanel1.add(TxtGama, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 100, 110, -1));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 550, 370));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void TxtMatriculaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TxtMatriculaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TxtMatriculaActionPerformed

    private void BtnBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnBuscarActionPerformed
     buscarVehiculo();
     
    }//GEN-LAST:event_BtnBuscarActionPerformed

    private void BtnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnGuardarActionPerformed
        guardarDatos();
        limpiarCampos();
    }//GEN-LAST:event_BtnGuardarActionPerformed

    private void BtnLimpiarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnLimpiarActionPerformed
        limpiarCampos();
        
    controlarBotones(true, false, false);
    }//GEN-LAST:event_BtnLimpiarActionPerformed

    private void Veh_disponibleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Veh_disponibleActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_Veh_disponibleActionPerformed

    private void BtnAtrasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnAtrasActionPerformed
        this.dispose();
    }//GEN-LAST:event_BtnAtrasActionPerformed

    private void Veh_nodisponibleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Veh_nodisponibleActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_Veh_nodisponibleActionPerformed

    /**
     * @param args the command line arguments
     */


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox Aire_Acondicionado;
    private javax.swing.JButton BtnAtras;
    private javax.swing.JButton BtnBuscar;
    private javax.swing.JButton BtnGuardar;
    private javax.swing.JButton BtnLimpiar;
    private javax.swing.JCheckBox Cambio_Auto;
    private javax.swing.JComboBox<String> CmbTipoMotor;
    private javax.swing.JComboBox<String> CmbTipoVehiculo;
    private javax.swing.JTextField Color_Veh;
    private javax.swing.JCheckBox Interior_Cuero;
    private javax.swing.JCheckBox Techo_Electrico;
    private javax.swing.JTextField TxtDescripGama;
    private javax.swing.JTextField TxtDescripVehiculo;
    private javax.swing.JTextField TxtGama;
    private javax.swing.JTextField TxtMarca;
    private javax.swing.JTextField TxtMatricula;
    private javax.swing.JTextField TxtModelo;
    private javax.swing.JTextField TxtPrecio;
    private javax.swing.JRadioButton Veh_disponible;
    private javax.swing.JRadioButton Veh_nodisponible;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    // End of variables declaration//GEN-END:variables
}
