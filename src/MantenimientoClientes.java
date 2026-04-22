

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */

/**
 *
 * @author Jeanc
 */
import javax.swing.*;
import java.io.*;

public class MantenimientoClientes extends javax.swing.JFrame {

    public MantenimientoClientes() {
        initComponents();
        estadoInicial();
           java.awt.event.KeyAdapter eventoEnterGuardar = new java.awt.event.KeyAdapter() {
        @Override
        public void keyPressed(java.awt.event.KeyEvent evt) {
            if (evt.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER && BtnGuardar.isEnabled()) {
                BtnGuardar.doClick(); 
            }
        }
    };

    TxtNombre.addKeyListener(eventoEnterGuardar);
    TxtApellidos.addKeyListener(eventoEnterGuardar);
    TxtDireccion.addKeyListener(eventoEnterGuardar);
    TxtEmail.addKeyListener(eventoEnterGuardar);
    TxtTelefono.addKeyListener(eventoEnterGuardar);

    
    TxtCedula.addKeyListener(new java.awt.event.KeyAdapter() {
        @Override
        public void keyPressed(java.awt.event.KeyEvent evt) {
            if (evt.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) {

                if (BtnBuscar.isEnabled()) {
                    BtnBuscar.doClick();
                } else if (BtnGuardar.isEnabled()) {
                    BtnGuardar.doClick(); 
                }
            }
        }
    });
    
    TxtCedula.addKeyListener(new java.awt.event.KeyAdapter() {
    @Override
    public void keyTyped(java.awt.event.KeyEvent evt) {
        char c = evt.getKeyChar();

        if (!Character.isDigit(c) && c != '-' && c != '\b') {
            evt.consume();
        }

        if (TxtCedula.getText().length() >= 13) {
            evt.consume();
        }
    }
});

TxtTelefono.addKeyListener(new java.awt.event.KeyAdapter() {
    @Override
    public void keyTyped(java.awt.event.KeyEvent evt) {
        char c = evt.getKeyChar();

        if (!Character.isDigit(c) && c != '-' && c != '\b') {
            evt.consume();
        }

        if (TxtTelefono.getText().length() >= 12) {
            evt.consume();
        }
    }
});

TxtNombre.addKeyListener(new java.awt.event.KeyAdapter() {
    @Override
    public void keyTyped(java.awt.event.KeyEvent evt) {
        char c = evt.getKeyChar();

        if (!Character.isLetter(c) && !Character.isWhitespace(c) && c != '\b') {
            evt.consume();
        }
    }
});

TxtApellidos.addKeyListener(new java.awt.event.KeyAdapter() {
    @Override
    public void keyTyped(java.awt.event.KeyEvent evt) {
        char c = evt.getKeyChar();

        if (!Character.isLetter(c) && !Character.isWhitespace(c) && c != '\b') {
            evt.consume();
        }
    }
});
    }

    
    

    private void estadoInicial() {
        TxtCedula.setEnabled(true);

        TxtNombre.setEnabled(false);
        TxtApellidos.setEnabled(false);
        TxtDireccion.setEnabled(false);
        TxtEmail.setEnabled(false);
        TxtTelefono.setEnabled(false);

        BtnGuardar.setEnabled(false);
    }

    private void activarCampos() {
        TxtNombre.setEnabled(true);
        TxtApellidos.setEnabled(true);
        TxtDireccion.setEnabled(true);
        TxtEmail.setEnabled(true);
        TxtTelefono.setEnabled(true);

        BtnGuardar.setEnabled(true);
    }

    private void limpiarCampos() {
        TxtCedula.setText("");
        TxtNombre.setText("");
        TxtApellidos.setText("");
        TxtDireccion.setText("");
        TxtEmail.setText("");
        TxtTelefono.setText("");
    }

    private void limpiarCamposMenosCedula() {
        TxtNombre.setText("");
        TxtApellidos.setText("");
        TxtDireccion.setText("");
        TxtEmail.setText("");
        TxtTelefono.setText("");
    }

private boolean validarCampos() {
    String cedula = TxtCedula.getText().trim();
    String nombre = TxtNombre.getText().trim();
    String apellidos = TxtApellidos.getText().trim();
    String direccion = TxtDireccion.getText().trim();
    String email = TxtEmail.getText().trim();
    String telefono = TxtTelefono.getText().trim();

    if (cedula.isEmpty()) {
        JOptionPane.showMessageDialog(this, "La cédula es obligatoria");
        TxtCedula.requestFocus();
        return false;
    }

    if (!validarCedulaDominicana(cedula)) {
        JOptionPane.showMessageDialog(this, "La cédula no tiene un formato válido. Use 11 dígitos o ###-#######-#");
        TxtCedula.requestFocus();
        return false;
    }

    if (nombre.isEmpty()) {
        JOptionPane.showMessageDialog(this, "El nombre es obligatorio");
        TxtNombre.requestFocus();
        return false;
    }

    if (!nombre.matches("[A-Za-zÁÉÍÓÚáéíóúÑñ ]{2,40}")) {
        JOptionPane.showMessageDialog(this, "El nombre solo debe contener letras y espacios");
        TxtNombre.requestFocus();
        return false;
    }

    if (apellidos.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Los apellidos son obligatorios");
        TxtApellidos.requestFocus();
        return false;
    }

    if (!apellidos.matches("[A-Za-zÁÉÍÓÚáéíóúÑñ ]{2,60}")) {
        JOptionPane.showMessageDialog(this, "Los apellidos solo deben contener letras y espacios");
        TxtApellidos.requestFocus();
        return false;
    }

    if (direccion.isEmpty()) {
        JOptionPane.showMessageDialog(this, "La dirección es obligatoria");
        TxtDireccion.requestFocus();
        return false;
    }

    if (direccion.length() < 5 || direccion.length() > 120) {
        JOptionPane.showMessageDialog(this, "La dirección debe tener entre 5 y 120 caracteres");
        TxtDireccion.requestFocus();
        return false;
    }

    if (!email.isEmpty() && !validarEmail(email)) {
        JOptionPane.showMessageDialog(this, "El email no tiene un formato válido");
        TxtEmail.requestFocus();
        return false;
    }

    if (telefono.isEmpty()) {
        JOptionPane.showMessageDialog(this, "El teléfono es obligatorio");
        TxtTelefono.requestFocus();
        return false;
    }

    if (!validarTelefonoDominicano(telefono)) {
        JOptionPane.showMessageDialog(this, "El teléfono no tiene un formato válido. Use 10 dígitos o ###-###-####");
        TxtTelefono.requestFocus();
        return false;
    }

    return true;
}

private boolean validarCedulaDominicana(String cedula) {
    String limpia = cedula.replace("-", "").trim();

    if (!limpia.matches("\\d{11}")) {
        return false;
    }

    if (limpia.equals("00000000000")) {
        return false;
    }

    return true;
}

private boolean validarTelefonoDominicano(String telefono) {
    String limpio = telefono.replace("-", "").trim();

    
    if (!limpio.matches("\\d{10}")) {
        return false;
    }

    return limpio.startsWith("809") || limpio.startsWith("829") || limpio.startsWith("849");
}

private boolean validarEmail(String email) {
    return email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
}

    private void buscarCliente() {
        String cedula = TxtCedula.getText().trim();

        if (cedula.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Digite la cédula del cliente");
            TxtCedula.requestFocus();
            return;
        }

        boolean encontrado = false;

        try (BufferedReader br = new BufferedReader(new FileReader("datos/Clientes.txt"))) {
            String linea;

            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split("\\|");

                if (datos.length >= 6 && datos[0].equalsIgnoreCase(cedula)) {
                    TxtNombre.setText(datos[1]);
                    TxtApellidos.setText(datos[2]);
                    TxtDireccion.setText(datos[3]);
                    TxtEmail.setText(datos[4]);
                    TxtTelefono.setText(datos[5]);

                    activarCampos();
                    JOptionPane.showMessageDialog(this, "Modificando");
                    encontrado = true;
                    break;
                }
            }
        } catch (IOException e) {
           
        }

        if (!encontrado) {
            limpiarCamposMenosCedula();
            activarCampos();
            JOptionPane.showMessageDialog(this, "Creando");
        }
    }

  
   private String obtenerRegistro() {
    String cedula = TxtCedula.getText().trim().replace("-", "");
    String telefono = TxtTelefono.getText().trim().replace("-", "");

    return cedula + "|" +
           TxtNombre.getText().trim() + "|" +
           TxtApellidos.getText().trim() + "|" +
           TxtDireccion.getText().trim() + "|" +
           TxtEmail.getText().trim().toLowerCase() + "|" +
           telefono;
}

    private void guardarCliente() {
        if (!validarCampos()) {
            return;
        }

        File archivo = new File("datos/Clientes.txt");
        File temporal = new File("Clientes_temp.txt");
        boolean actualizado = false;

        try {
            PrintWriter pw = new PrintWriter(new FileWriter(temporal));

            if (archivo.exists()) {
                BufferedReader br = new BufferedReader(new FileReader(archivo));
                String linea;
                String cedula = TxtCedula.getText().trim();

                while ((linea = br.readLine()) != null) {
                    String[] datos = linea.split("\\|");

                    if (datos.length >= 1 && datos[0].equalsIgnoreCase(cedula)) {
                        pw.println(obtenerRegistro());
                        actualizado = true;
                    } else {
                        pw.println(linea);
                    }
                }

                br.close();
            }

            if (!actualizado) {
                pw.println(obtenerRegistro());
            }

            pw.close();

            if (archivo.exists()) {
                archivo.delete();
            }

            temporal.renameTo(archivo);

            if (actualizado) {
                JOptionPane.showMessageDialog(this, "Cliente modificado correctamente");
            } else {
                JOptionPane.showMessageDialog(this, "Cliente guardado correctamente");
            }

            limpiarCampos();
            estadoInicial();

        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error al guardar: " + e.getMessage());
        }
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        TxtCedula = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        TxtNombre = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        TxtApellidos = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        TxtDireccion = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        TxtEmail = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        TxtTelefono = new javax.swing.JTextField();
        BtnAtras = new javax.swing.JButton();
        BtnLimpiar = new javax.swing.JButton();
        BtnBuscar = new javax.swing.JButton();
        BtnGuardar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel2.setText("Cedula:");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 70, -1, -1));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel1.setText("Mantenimiento de Clientes");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 10, -1, -1));
        jPanel1.add(TxtCedula, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 70, 250, -1));

        jLabel3.setText("Nombre:");
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 120, -1, -1));
        jPanel1.add(TxtNombre, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 120, 250, -1));

        jLabel4.setText("Apellidos:");
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 170, -1, -1));
        jPanel1.add(TxtApellidos, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 170, 250, -1));

        jLabel5.setText("Dirección:");
        jPanel1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 220, -1, -1));
        jPanel1.add(TxtDireccion, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 220, 250, -1));

        jLabel7.setText("Email:");
        jPanel1.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 270, -1, -1));
        jPanel1.add(TxtEmail, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 270, 250, -1));

        jLabel6.setText("Telefono:");
        jPanel1.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 320, -1, -1));
        jPanel1.add(TxtTelefono, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 320, 250, -1));

        BtnAtras.setBackground(new java.awt.Color(255, 102, 102));
        BtnAtras.setText("Atras");
        BtnAtras.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnAtrasActionPerformed(evt);
            }
        });
        jPanel1.add(BtnAtras, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 380, -1, -1));

        BtnLimpiar.setBackground(new java.awt.Color(153, 255, 153));
        BtnLimpiar.setText("Limpiar");
        BtnLimpiar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnLimpiarActionPerformed(evt);
            }
        });
        jPanel1.add(BtnLimpiar, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 380, -1, -1));

        BtnBuscar.setBackground(new java.awt.Color(153, 204, 255));
        BtnBuscar.setText("Buscar");
        BtnBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnBuscarActionPerformed(evt);
            }
        });
        jPanel1.add(BtnBuscar, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 380, -1, -1));

        BtnGuardar.setBackground(new java.awt.Color(255, 255, 153));
        BtnGuardar.setText("Guardar");
        BtnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnGuardarActionPerformed(evt);
            }
        });
        BtnGuardar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                BtnGuardarKeyTyped(evt);
            }
        });
        jPanel1.add(BtnGuardar, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 380, -1, -1));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 520, 430));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void BtnBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnBuscarActionPerformed
        buscarCliente();
    }//GEN-LAST:event_BtnBuscarActionPerformed

    private void BtnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnGuardarActionPerformed
        guardarCliente();
       

    }//GEN-LAST:event_BtnGuardarActionPerformed

    private void BtnLimpiarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnLimpiarActionPerformed
        limpiarCampos();
        estadoInicial();
    }//GEN-LAST:event_BtnLimpiarActionPerformed

    private void BtnAtrasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnAtrasActionPerformed
         dispose();
    }//GEN-LAST:event_BtnAtrasActionPerformed

    private void BtnGuardarKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnGuardarKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_BtnGuardarKeyTyped

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
            java.util.logging.Logger.getLogger(MantenimientoClientes.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MantenimientoClientes.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MantenimientoClientes.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MantenimientoClientes.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MantenimientoClientes().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BtnAtras;
    private javax.swing.JButton BtnBuscar;
    private javax.swing.JButton BtnGuardar;
    private javax.swing.JButton BtnLimpiar;
    private javax.swing.JTextField TxtApellidos;
    private javax.swing.JTextField TxtCedula;
    private javax.swing.JTextField TxtDireccion;
    private javax.swing.JTextField TxtEmail;
    private javax.swing.JTextField TxtNombre;
    private javax.swing.JTextField TxtTelefono;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    // End of variables declaration//GEN-END:variables
}
