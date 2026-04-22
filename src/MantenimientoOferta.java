


import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.JOptionPane;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */

/**
 *
 * @author Jeanc
 */
public class MantenimientoOferta extends javax.swing.JFrame {

    /**
     * Creates new form MantenimientoOferta
     */
public MantenimientoOferta() {
    initComponents();
    estadoInicial();

    TxtMatricula.addFocusListener(new java.awt.event.FocusAdapter() {
        @Override
        public void focusLost(java.awt.event.FocusEvent e) {
            if (TxtMatricula.isEnabled() && !TxtMatricula.getText().trim().isEmpty()) {
                buscarVehiculoPorMatricula();
            }
        }
    });
}

       private String buscarPrecioGamaPorId(String idGama) {
    try (BufferedReader br = new BufferedReader(new FileReader("datos/gamas.txt"))) {
        String linea;

        while ((linea = br.readLine()) != null) {
            String[] datos = linea.split(";");

            if (datos.length >= 3 && datos[0].trim().equals(idGama)) {
                return datos[2].trim();
            }
        }
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Error al leer gamas");
    }

    return "";
}


    private void estadoInicial() {
        Id_Oferta.setEnabled(true);

        TxtMatricula.setEnabled(false);
        TxtMarca.setEnabled(false);
        TxtModelo.setEnabled(false);
        TxtDescripcion.setEnabled(false);
        TxtPrecio.setEnabled(false);
        TxtPrecio_Oferta.setEnabled(false);

        BtnGuardar.setEnabled(false);

        limpiarCamposMenosId();
    }

    private void habilitarEdicionOferta() {
        TxtMatricula.setEnabled(true);
        TxtPrecio_Oferta.setEnabled(true);

        TxtMarca.setEnabled(false);
        TxtModelo.setEnabled(false);
        TxtDescripcion.setEnabled(false);
        TxtPrecio.setEnabled(false);

        BtnGuardar.setEnabled(true);
    }

    private void limpiarCampos() {
        Id_Oferta.setText("");
        TxtMatricula.setText("");
        TxtMarca.setText("");
        TxtModelo.setText("");
        TxtDescripcion.setText("");
        TxtPrecio.setText("");
        TxtPrecio_Oferta.setText("");
    }

    private void limpiarCamposMenosId() {
        TxtMatricula.setText("");
        TxtMarca.setText("");
        TxtModelo.setText("");
        TxtDescripcion.setText("");
        TxtPrecio.setText("");
        TxtPrecio_Oferta.setText("");
    }

    private boolean validarCampos() {
    if (Id_Oferta.getText().trim().isEmpty()) {
        JOptionPane.showMessageDialog(this, "El Id Oferta es obligatorio");
        Id_Oferta.requestFocus();
        return false;
    }

    if (TxtMatricula.getText().trim().isEmpty()) {
        JOptionPane.showMessageDialog(this, "La matrícula es obligatoria");
        TxtMatricula.requestFocus();
        return false;
    }

    if (TxtMarca.getText().trim().isEmpty()) {
        JOptionPane.showMessageDialog(this, "Debe buscar una matrícula válida");
        TxtMatricula.requestFocus();
        return false;
    }

    if (TxtPrecio.getText().trim().isEmpty()) {
        JOptionPane.showMessageDialog(this, "No se pudo obtener el precio de la gama");
        TxtMatricula.requestFocus();
        return false;
    }

    if (TxtPrecio_Oferta.getText().trim().isEmpty()) {
        JOptionPane.showMessageDialog(this, "El precio de oferta es obligatorio");
        TxtPrecio_Oferta.requestFocus();
        return false;
    }

    double precioGama;
    double precioOferta;

    try {
        precioGama = Double.parseDouble(TxtPrecio.getText().trim());
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(this, "Precio de gama inválido");
        TxtPrecio.requestFocus();
        return false;
    }

    try {
        precioOferta = Double.parseDouble(TxtPrecio_Oferta.getText().trim());
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(this, "Precio de oferta inválido");
        TxtPrecio_Oferta.requestFocus();
        return false;
    }

    double precioMinimoPermitido = precioGama - (precioGama * 0.15);

if (precioOferta < precioMinimoPermitido) {
    JOptionPane.showMessageDialog(
        this,
        "El precio oferta no puede ser menor a " + precioMinimoPermitido +
        "\n(Solo se permite hasta 15% de descuento)"
    );
    TxtPrecio_Oferta.requestFocus();
    return false;
}
    return true;
    }

    private void buscarOferta() {
        String idOferta = Id_Oferta.getText().trim();

        if (idOferta.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Digite el Id de la oferta");
            Id_Oferta.requestFocus();
            return;
        }

        boolean encontrado = false;

        try (BufferedReader br = new BufferedReader(new FileReader("Ofertas.txt"))) {
            String linea;

            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split("\\|");

                if (datos.length >= 7 && datos[0].trim().equalsIgnoreCase(idOferta)) {
                    TxtMatricula.setText(datos[1].trim());
                    TxtMarca.setText(datos[2].trim());
                    TxtModelo.setText(datos[3].trim());
                    TxtDescripcion.setText(datos[4].trim());
                    TxtPrecio.setText(datos[5].trim());
                    TxtPrecio_Oferta.setText(datos[6].trim());

                    encontrado = true;
                    break;
                }
            }
        } catch (IOException e) {
    
        }

        habilitarEdicionOferta();
        Id_Oferta.setEnabled(false);

        if (encontrado) {
            JOptionPane.showMessageDialog(this, "Modificando");
        } else {
            JOptionPane.showMessageDialog(this, "Creando");
            limpiarCamposMenosId();
            TxtMatricula.requestFocus();
        }
    }

    private void buscarVehiculoPorMatricula() {
        String matricula = TxtMatricula.getText().trim();

        if (matricula.isEmpty()) {
            return;
        }

        boolean encontrado = false;

        try (BufferedReader br = new BufferedReader(new FileReader("datos/Vehiculos.txt"))) {
            String linea;

            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split("\\|");

             

                if (datos.length >= 13 && datos[0].trim().equalsIgnoreCase(matricula)) {
                    TxtMarca.setText(datos[1].trim());
                    TxtModelo.setText(datos[2].trim());
                    TxtDescripcion.setText(datos[12].trim());

               
                    if (datos.length >= 14 && !datos[13].trim().isEmpty()) {
                        TxtPrecio.setText(datos[13].trim());
                    } else {
                        
                        String idGama = datos[5].trim();
                        String precioGama = buscarPrecioGamaPorId(idGama);
                        TxtPrecio.setText(precioGama);
                    }

                    encontrado = true;
                    break;
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "No se pudo leer el archivo de vehículos");
            return;
        }

        if (!encontrado) {
            TxtMarca.setText("");
            TxtModelo.setText("");
            TxtDescripcion.setText("");
            TxtPrecio.setText("");

            JOptionPane.showMessageDialog(this, "La matrícula no existe en el archivo de vehículos");
            TxtMatricula.requestFocus();
        }
    }
 

 
    


    private String obtenerRegistro() {
        return Id_Oferta.getText().trim() + "|" +
               TxtMatricula.getText().trim() + "|" +
               TxtMarca.getText().trim() + "|" +
               TxtModelo.getText().trim() + "|" +
               TxtDescripcion.getText().trim() + "|" +
               TxtPrecio.getText().trim() + "|" +
               TxtPrecio_Oferta.getText().trim();
    }

 private void guardarOferta() {
    if (!validarCampos()) {
        return;
    }

    File archivo = new File("Ofertas.txt");
    File temporal = new File("Ofertas_temp.txt");
    boolean actualizado = false;

    try {
        ArrayList<String> lista = new ArrayList<>();

        if (archivo.exists()) {
            BufferedReader br = new BufferedReader(new FileReader(archivo));
            String linea;

            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split("\\|");

                
                if (datos.length >= 1 && datos[0].trim().equalsIgnoreCase(Id_Oferta.getText().trim())) {
                    lista.add(obtenerRegistro());
                    actualizado = true;
                } else {
                    lista.add(linea);
                }
            }
            br.close();
        }

     
        if (!actualizado) {
            lista.add(obtenerRegistro());
        }

        BufferedWriter bw = new BufferedWriter(new FileWriter(temporal));
        for (String l : lista) {
            bw.write(l);
            bw.newLine();
        }
        bw.close();

        if (archivo.exists()) {
            archivo.delete();
        }

        temporal.renameTo(archivo);

        if (actualizado) {
            JOptionPane.showMessageDialog(this, "Oferta modificada correctamente");
        } else {
            JOptionPane.showMessageDialog(this, "Oferta guardada correctamente");
        }

        limpiarCampos();
        estadoInicial();

    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Error al guardar la oferta");
    }
    
 } 
 

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
            java.util.logging.Logger.getLogger(Mantenimiento.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Mantenimiento.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Mantenimiento.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Mantenimiento.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MantenimientoOferta().setVisible(true);
            }
        });
    }




    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        Id_Oferta = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        TxtMatricula = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        TxtMarca = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        TxtModelo = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        TxtDescripcion = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        TxtPrecio = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        TxtPrecio_Oferta = new javax.swing.JTextField();
        BtnAtras = new javax.swing.JButton();
        BtnBuscar = new javax.swing.JButton();
        BtnGuardar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel1.setText("Mantenimiento de Ofertas: ");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(166, 6, -1, -1));

        jLabel2.setText("ID Oferta: ");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(49, 72, -1, -1));
        jPanel1.add(Id_Oferta, new org.netbeans.lib.awtextra.AbsoluteConstraints(109, 69, 138, -1));

        jLabel3.setText("Matricula: ");
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(48, 106, -1, -1));
        jPanel1.add(TxtMatricula, new org.netbeans.lib.awtextra.AbsoluteConstraints(109, 103, 137, -1));

        jLabel4.setText("Marca: ");
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(48, 146, 55, -1));
        jPanel1.add(TxtMarca, new org.netbeans.lib.awtextra.AbsoluteConstraints(109, 143, 137, -1));

        jLabel5.setText("Modelo: ");
        jPanel1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(48, 186, -1, -1));
        jPanel1.add(TxtModelo, new org.netbeans.lib.awtextra.AbsoluteConstraints(107, 183, 139, -1));

        jLabel6.setText("Descripcion:");
        jPanel1.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(48, 234, -1, -1));
        jPanel1.add(TxtDescripcion, new org.netbeans.lib.awtextra.AbsoluteConstraints(125, 231, 256, -1));

        jLabel7.setText("Precio:");
        jPanel1.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(48, 274, 37, -1));
        jPanel1.add(TxtPrecio, new org.netbeans.lib.awtextra.AbsoluteConstraints(91, 271, 71, -1));

        jLabel8.setText("En oferta: ");
        jPanel1.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 274, -1, -1));
        jPanel1.add(TxtPrecio_Oferta, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 271, 71, -1));

        BtnAtras.setBackground(new java.awt.Color(255, 102, 102));
        BtnAtras.setText("Atras");
        BtnAtras.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnAtrasActionPerformed(evt);
            }
        });
        jPanel1.add(BtnAtras, new org.netbeans.lib.awtextra.AbsoluteConstraints(48, 334, -1, -1));

        BtnBuscar.setBackground(new java.awt.Color(153, 204, 255));
        BtnBuscar.setText("Buscar");
        BtnBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnBuscarActionPerformed(evt);
            }
        });
        jPanel1.add(BtnBuscar, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 334, -1, -1));

        BtnGuardar.setBackground(new java.awt.Color(255, 255, 153));
        BtnGuardar.setText("Guardar");
        BtnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnGuardarActionPerformed(evt);
            }
        });
        jPanel1.add(BtnGuardar, new org.netbeans.lib.awtextra.AbsoluteConstraints(317, 334, -1, -1));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 490, 390));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void BtnAtrasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnAtrasActionPerformed
        this.dispose();
    }//GEN-LAST:event_BtnAtrasActionPerformed

    private void BtnBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnBuscarActionPerformed
        buscarOferta();
    }//GEN-LAST:event_BtnBuscarActionPerformed

    private void BtnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnGuardarActionPerformed
      guardarOferta();
    }//GEN-LAST:event_BtnGuardarActionPerformed



    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BtnAtras;
    private javax.swing.JButton BtnBuscar;
    private javax.swing.JButton BtnGuardar;
    private javax.swing.JTextField Id_Oferta;
    private javax.swing.JTextField TxtDescripcion;
    private javax.swing.JTextField TxtMarca;
    private javax.swing.JTextField TxtMatricula;
    private javax.swing.JTextField TxtModelo;
    private javax.swing.JTextField TxtPrecio;
    private javax.swing.JTextField TxtPrecio_Oferta;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    // End of variables declaration//GEN-END:variables
}
