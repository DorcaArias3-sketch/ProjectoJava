/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author DELL
 */
import com.toedter.calendar.JDateChooser;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.swing.JOptionPane;

public class ReservasClientes extends javax.swing.JFrame {

    private final String ARCHIVO_CLIENTES = "datos/Clientes.txt";
    private final String ARCHIVO_VEHICULOS = "datos/Vehiculos.txt";
    private final String ARCHIVO_GAMAS = "datos/gamas.txt";
    private final String ARCHIVO_OFERTAS = "Ofertas.txt";
    private final String ARCHIVO_RESERVAS = "datos/Reservas_Clientes.txt";
    private boolean reservaExistente = false;
    

    private final SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");

    public ReservasClientes() {
        
        initComponents();
        formatoFecha.setLenient(false);
        estadoInicial();
        agregarEventos();

        

       
    }

   private void estadoInicial() {
    reservaExistente = false;

    Id_Reserva.setEnabled(true);

    TxtMatricula.setEnabled(false);
    TxtMarca.setEditable(false);
    TxtModelo.setEditable(false);
    TxtTipoVehiculo.setEditable(false);
    TxtTipoMotor.setEditable(false);
    Id_Oferta.setEnabled(false);
    TxtPrecioOferta.setEditable(false);
    Total_Reservas.setEditable(false);
    TxtObservacion.setEnabled(false);
    TxtCedula.setEnabled(false);
    TxtNombre.setEditable(false);
    TxtApellido.setEditable(false);
    Dia_Reserva.setEditable(false);

    Fecha_Reserva.setEnabled(false);
    Fecha_Salida.setEnabled(false);
    Fecha_Entrada.setEnabled(false);

    Veh_Disponible.setEnabled(false);
    Veh_Nodisponible.setEnabled(false);

    BtnGuardar.setEnabled(false);
    BtnBorrarReserva.setEnabled(false);

    Fecha_Reserva.setDate(new Date());
    restringirCedula();

    limpiarCamposMenosIdReserva();
}

    private void agregarEventos() {
        
    TxtMatricula.addFocusListener(new FocusAdapter() {
        @Override
        public void focusLost(FocusEvent e) {
            if (!TxtMatricula.getText().trim().isEmpty()) {
                buscarVehiculo();
            }
        }
    });

    TxtCedula.addFocusListener(new FocusAdapter() {
        @Override
        public void focusLost(FocusEvent e) {
            if (!TxtCedula.getText().trim().isEmpty()) {
                buscarCliente();
            }
        }
    });

    Id_Oferta.addFocusListener(new FocusAdapter() {
        @Override
        public void focusLost(FocusEvent e) {
            if (!Id_Oferta.getText().trim().isEmpty()) {
                buscarOferta();
            } else {
                quitarOferta();
            }
        }
    });

Fecha_Salida.addPropertyChangeListener("date", evt -> calcularDiasYTotal());
    Fecha_Entrada.addPropertyChangeListener("date", evt -> calcularDiasYTotal());
    
}
    
    private void buscarReservaPorId() {
    String idReserva = Id_Reserva.getText().trim();
    boolean encontrada = false;
    reservaExistente = false;

    if (idReserva.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Debe digitar el ID de la reserva");
        Id_Reserva.requestFocus();
        return;
    }

    try (BufferedReader br = new BufferedReader(new FileReader(ARCHIVO_RESERVAS))) {
        String linea;

        while ((linea = br.readLine()) != null) {
            String[] datos = linea.split("\\|");

            if (datos.length >= 10 && datos[0].trim().equalsIgnoreCase(idReserva)) {
                TxtMatricula.setText(datos[1].trim());
                TxtCedula.setText(datos[2].trim());
                Id_Oferta.setText(datos[3].trim());

                Fecha_Reserva.setDate(formatoFecha.parse(datos[4].trim()));
                Fecha_Salida.setDate(formatoFecha.parse(datos[5].trim()));
                Fecha_Entrada.setDate(formatoFecha.parse(datos[6].trim()));

                TxtObservacion.setText(datos[7].trim());
                Dia_Reserva.setText(datos[8].trim());
                Total_Reservas.setText(datos[9].trim());

                buscarVehiculo();
                buscarCliente();

                if (!Id_Oferta.getText().trim().isEmpty()) {
                    buscarOferta();
                }

                encontrada = true;
                reservaExistente = true;
                break;
            }
        }
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Error al buscar la reserva");
        return;
    }

    if (encontrada) {
        
        BtnGuardar.setEnabled(false);
        BtnBorrarReserva.setEnabled(true);

        TxtMatricula.setEnabled(false);
        TxtCedula.setEnabled(false);
        Id_Oferta.setEnabled(false);
        TxtObservacion.setEnabled(false);
        Fecha_Salida.setEnabled(false);
        Fecha_Entrada.setEnabled(false);
    } else {
        JOptionPane.showMessageDialog(this, "Creando");
        limpiarCamposMenosIdReserva();
        habilitarCreacionReserva();
        Fecha_Reserva.setDate(new Date());
        Id_Reserva.setText(idReserva); 
        TxtMatricula.requestFocus();
    }
}
    
    private void habilitarCreacionReserva() {
    TxtMatricula.setEnabled(true);
    Id_Oferta.setEnabled(true);
    TxtObservacion.setEnabled(true);
    TxtCedula.setEnabled(true);

    Fecha_Salida.setEnabled(true);
    Fecha_Entrada.setEnabled(true);

    BtnGuardar.setEnabled(true);
    BtnBorrarReserva.setEnabled(false);

    Veh_Disponible.setEnabled(false);
    Veh_Nodisponible.setEnabled(false);
}

    private void restringirCedula() {
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
   

private void buscarVehiculo() {
    String matricula = TxtMatricula.getText().trim();
    boolean encontrado = false;

    if (matricula.isEmpty()) {
        return;
    }

    try (BufferedReader br = new BufferedReader(new FileReader(ARCHIVO_VEHICULOS))) {
        String linea;

        while ((linea = br.readLine()) != null) {

            
            String[] datos = linea.split("\\|");

            if (datos.length >= 8 && datos[0].trim().equalsIgnoreCase(matricula)) {
                TxtMarca.setText(datos[1].trim());
                TxtModelo.setText(datos[2].trim());
                TxtTipoVehiculo.setText(datos[3].trim());
                TxtTipoMotor.setText(datos[4].trim());

                boolean disponible = Boolean.parseBoolean(datos[7].trim());

                if (disponible) {
                    Veh_Disponible.setSelected(true);
                    Veh_Nodisponible.setSelected(false);
                } else {
                    Veh_Disponible.setSelected(false);
                    Veh_Nodisponible.setSelected(true);
                }

                if (!disponible && !reservaExistente) {
                    JOptionPane.showMessageDialog(this, "El vehículo no está disponible para reserva");
                    limpiarVehiculo();
                    return;
                }

      
                if (Id_Oferta.getText().trim().isEmpty()) {
                    String precioBase = buscarPrecioGamaPorId(datos[5].trim());
                    TxtPrecioOferta.setText("");
                    TxtPrecioOferta.putClientProperty("precioBase", precioBase);
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
        JOptionPane.showMessageDialog(this, "La matrícula no existe");
        limpiarVehiculo();
        return;
    }

    calcularDiasYTotal();
}
    private String buscarPrecioGamaPorId(String idGama) {
        try (BufferedReader br = new BufferedReader(new FileReader(ARCHIVO_GAMAS))) {
            String linea;

            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(";");
                if (datos.length >= 3 && datos[0].trim().equals(idGama)) {
                    return datos[2].trim();
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "No se pudo leer gamas.txt");
        }

        return "";
    }

    private void buscarCliente() {
        String cedula = TxtCedula.getText().trim().replace("-", "");

        if (!validarCedulaDominicana(TxtCedula.getText().trim())) {
            JOptionPane.showMessageDialog(this, "La cédula no es válida");
            TxtCedula.requestFocus();
            TxtNombre.setText("");
            TxtApellido.setText("");
            return;
        }

        boolean encontrado = false;

        try (BufferedReader br = new BufferedReader(new FileReader(ARCHIVO_CLIENTES))) {
            String linea;

            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split("\\|");

                if (datos.length >= 6 && datos[0].replace("-", "").trim().equalsIgnoreCase(cedula)) {
                    TxtNombre.setText(datos[1].trim());
                    TxtApellido.setText(datos[2].trim());
                    encontrado = true;
                    break;
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "No se pudo leer el archivo de clientes");
            return;
        }

        if (!encontrado) {
            JOptionPane.showMessageDialog(this, "La cédula no existe en clientes");
            TxtNombre.setText("");
            TxtApellido.setText("");
        }
    }

private void buscarOferta() {
 String idOferta = Id_Oferta.getText().trim();

    if (idOferta.isEmpty()) {
        quitarOferta();
        return;
    }

    boolean encontrada = false;

    try (BufferedReader br = new BufferedReader(new FileReader(ARCHIVO_OFERTAS))) {
        String linea;

        while ((linea = br.readLine()) != null) {
            String[] datos = linea.split("\\|");

            if (datos.length >= 7 && datos[0].trim().equalsIgnoreCase(idOferta)) {
                TxtPrecioOferta.setText(datos[6].trim());
                encontrada = true;
                break;
            }
        }
    } catch (IOException e) {
        JOptionPane.showMessageDialog(this, "No se pudo leer el archivo de ofertas");
        return;
    }

    if (!encontrada) {
        JOptionPane.showMessageDialog(this, "La oferta no existe");
        quitarOferta();
        return;
    }

    calcularDiasYTotal();
}

private void quitarOferta() {
    Id_Oferta.setText("");
    TxtPrecioOferta.setText("");

    if (!TxtMatricula.getText().trim().isEmpty()) {
        buscarVehiculo(); 
    } else {
        Dia_Reserva.setText("");
        Total_Reservas.setText("");
    }

    calcularDiasYTotal();
}
private void calcularDiasYTotal() {
    Date fechaSalida = Fecha_Salida.getDate();
    Date fechaEntrada = Fecha_Entrada.getDate();

    if (fechaSalida == null || fechaEntrada == null) {
        Dia_Reserva.setText("");
        Total_Reservas.setText("");
        return;
    }

    String precioTexto = TxtPrecioOferta.getText().trim();

    
    if (precioTexto.isEmpty()) {
        Object precioBase = TxtPrecioOferta.getClientProperty("precioBase");
        if (precioBase != null) {
            precioTexto = precioBase.toString();
        }
    }

    if (precioTexto.isEmpty()) {
        Dia_Reserva.setText("");
        Total_Reservas.setText("");
        return;
    }

    try {
        Calendar calSalida = Calendar.getInstance();
        calSalida.setTime(fechaSalida);
        calSalida.set(Calendar.HOUR_OF_DAY, 0);
        calSalida.set(Calendar.MINUTE, 0);
        calSalida.set(Calendar.SECOND, 0);
        calSalida.set(Calendar.MILLISECOND, 0);

        Calendar calEntrada = Calendar.getInstance();
        calEntrada.setTime(fechaEntrada);
        calEntrada.set(Calendar.HOUR_OF_DAY, 0);
        calEntrada.set(Calendar.MINUTE, 0);
        calEntrada.set(Calendar.SECOND, 0);
        calEntrada.set(Calendar.MILLISECOND, 0);

        if (calEntrada.getTime().before(calSalida.getTime())) {
            Dia_Reserva.setText("");
            Total_Reservas.setText("");
            return;
        }

        long diferencia = calEntrada.getTimeInMillis() - calSalida.getTimeInMillis();
        int dias = (int) (diferencia / (1000 * 60 * 60 * 24));

        if (dias <= 0) {
            Dia_Reserva.setText("");
            Total_Reservas.setText("");
            return;
        }

        Dia_Reserva.setText(String.valueOf(dias));

        double precio = Double.parseDouble(precioTexto);
        double total = precio * dias;

        Total_Reservas.setText(String.valueOf(total));

    } catch (Exception e) {
        Dia_Reserva.setText("");
        Total_Reservas.setText("");
    }
}

    private boolean validarCampos() {
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

        if (TxtCedula.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "La cédula es obligatoria");
            TxtCedula.requestFocus();
            return false;
        }

        if (!validarCedulaDominicana(TxtCedula.getText().trim())) {
            JOptionPane.showMessageDialog(this, "La cédula digitada no es válida");
            TxtCedula.requestFocus();
            return false;
        }

        if (TxtNombre.getText().trim().isEmpty() || TxtApellido.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debe buscar una cédula válida");
            TxtCedula.requestFocus();
            return false;
        }

if (Fecha_Reserva.getDate() == null) {
    JOptionPane.showMessageDialog(this, "La fecha de reserva es obligatoria");
    return false;
}

if (Fecha_Salida.getDate() == null) {
    JOptionPane.showMessageDialog(this, "La fecha de salida es obligatoria");
    Fecha_Salida.requestFocus();
    return false;
}

if (Fecha_Entrada.getDate() == null) {
    JOptionPane.showMessageDialog(this, "La fecha de entrada es obligatoria");
    Fecha_Entrada.requestFocus();
    return false;
}
        if (Id_Reserva.getText().trim().isEmpty()) {
    JOptionPane.showMessageDialog(this, "El ID de reserva es obligatorio");
    Id_Reserva.requestFocus();
    return false;
}

        return true;
    }

 private String obtenerRegistroReserva() {
    return Id_Reserva.getText().trim() + "|" +
           TxtMatricula.getText().trim() + "|" +
           TxtCedula.getText().trim().replace("-", "") + "|" +
           Id_Oferta.getText().trim() + "|" +
           formatoFecha.format(Fecha_Reserva.getDate()) + "|" +
           formatoFecha.format(Fecha_Salida.getDate()) + "|" +
           formatoFecha.format(Fecha_Entrada.getDate()) + "|" +
           TxtObservacion.getText().trim() + "|" +
           Dia_Reserva.getText().trim() + "|" +
           Total_Reservas.getText().trim();
}

private void guardarReserva() {
    if (reservaExistente) {
        JOptionPane.showMessageDialog(this, "Las reservas no se pueden modificar. Debe borrar la reserva primero.");
        return;
    }

    if (!validarCampos()) {
        return;
    }

    try (BufferedWriter bw = new BufferedWriter(new FileWriter(ARCHIVO_RESERVAS, true))) {
        bw.write(obtenerRegistroReserva());
        bw.newLine();
    } catch (IOException e) {
        JOptionPane.showMessageDialog(this, "Error al guardar la reserva");
        return;
    }

    cambiarEstadoVehiculoReservado();

    JOptionPane.showMessageDialog(this, "Reserva guardada correctamente");
    limpiarCampos();
    estadoInicial();
    reservaExistente = false;
}
    
    private void borrarReserva() {
    String matricula = TxtMatricula.getText().trim();

    
    File archivoReservas = new File("datos/Reservas_Clientes.txt");
    File tempReservas = new File("datos/Reservas_Clientes_temp.txt");

    boolean eliminada = false;

    try (BufferedReader br = new BufferedReader(new FileReader(archivoReservas));
         PrintWriter pw = new PrintWriter(new FileWriter(tempReservas))) {

        String linea;
        while ((linea = br.readLine()) != null) {
            String[] datos = linea.split("\\|");

if (datos.length >= 2 && datos[1].trim().equalsIgnoreCase(matricula)) {
    eliminada = true;
} else {
    pw.println(linea);
}
        }
    } catch (IOException e) {
        JOptionPane.showMessageDialog(this, "Error al leer o escribir el archivo de reservas.");
        return;
    }

    if (eliminada) {
        
        if (archivoReservas.delete()) {
            tempReservas.renameTo(archivoReservas);
        }

        
        File archivoVehiculos = new File("datos/Vehiculos.txt");
        File tempVehiculos = new File("datos/Vehiculos_temp.txt");

        try (BufferedReader brVeh = new BufferedReader(new FileReader(archivoVehiculos));
             PrintWriter pwVeh = new PrintWriter(new FileWriter(tempVehiculos))) {

            String lineaVeh;
            while ((lineaVeh = brVeh.readLine()) != null) {
                String[] datosVeh = lineaVeh.split("\\|");

                if (datosVeh.length >= 8 && datosVeh[0].trim().equalsIgnoreCase(matricula)) {
                   
                    datosVeh[7] = "true";
                    String nuevaLinea = String.join("|", datosVeh);
                    pwVeh.println(nuevaLinea);
                } else {
                    pwVeh.println(lineaVeh);
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error al actualizar el estado del vehículo.");
            return;
        }

        
        if (archivoVehiculos.delete()) {
            tempVehiculos.renameTo(archivoVehiculos);
        }

        JOptionPane.showMessageDialog(this, "Reserva eliminada y vehículo disponible.");
        limpiarCampos();
        reservaExistente = false;
        BtnGuardar.setEnabled(true);
        estadoInicial();  
    } else {
        JOptionPane.showMessageDialog(this, "No se encontró la reserva para borrar.");
    }
}

    private void cambiarEstadoVehiculoReservado() {
        File archivo = new File(ARCHIVO_VEHICULOS);
        File temporal = new File("datos/Vehiculos_temp.txt");
        String matricula = TxtMatricula.getText().trim();

        try (
            BufferedReader br = new BufferedReader(new FileReader(archivo));
            PrintWriter pw = new PrintWriter(new FileWriter(temporal))
        ) {
            String linea;

            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split("\\|");

                if (datos.length >= 13 && datos[0].trim().equalsIgnoreCase(matricula)) {
                    datos[7] = "false";

                    String nuevaLinea = String.join("|", datos);
                    pw.println(nuevaLinea);
                } else {
                    pw.println(linea);
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "No se pudo actualizar el estado del vehículo");
            return;
        }

        if (archivo.exists()) {
            archivo.delete();
        }

        temporal.renameTo(archivo);
    }

  private void limpiarVehiculo() {
    TxtMarca.setText("");
    TxtModelo.setText("");
    TxtTipoVehiculo.setText("");
    TxtTipoMotor.setText("");
    TxtPrecioOferta.setText("");
    Total_Reservas.setText("");

    Veh_Disponible.setSelected(false);
    Veh_Nodisponible.setSelected(false);
}

    private void limpiarCampos() {
        TxtMatricula.setText("");
        TxtMarca.setText("");
        TxtModelo.setText("");
        TxtTipoVehiculo.setText("");
        TxtTipoMotor.setText("");
        Id_Oferta.setText("");
        TxtPrecioOferta.setText("");
        Total_Reservas.setText("");
        TxtObservacion.setText("");
        TxtCedula.setText("");
        TxtNombre.setText("");
        TxtApellido.setText("");
        Fecha_Reserva.setDate(null);
        Fecha_Salida.setDate(null);
        Fecha_Entrada.setDate(null);
        Dia_Reserva.setText("");

        BtnBorrarReserva.setEnabled(false);
    }
    private void limpiarCamposMenosIdReserva() {
    TxtMatricula.setText("");
    TxtMarca.setText("");
    TxtModelo.setText("");
    TxtTipoVehiculo.setText("");
    TxtTipoMotor.setText("");
    Id_Oferta.setText("");
    TxtPrecioOferta.setText("");
    Total_Reservas.setText("");
    TxtObservacion.setText("");
    TxtCedula.setText("");
    TxtNombre.setText("");
    TxtApellido.setText("");
    Dia_Reserva.setText("");

    Fecha_Salida.setDate(null);
    Fecha_Entrada.setDate(null);

    Veh_Disponible.setSelected(false);
    Veh_Nodisponible.setSelected(false);
}
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        TxtMatricula = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        TxtMarca = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        TxtModelo = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        TxtTipoVehiculo = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        TxtTipoMotor = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        TxtCedula = new javax.swing.JTextField();
        TxtNombre = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        TxtApellido = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        Id_Oferta = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        TxtPrecioOferta = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        Total_Reservas = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        Fecha_Reserva = new com.toedter.calendar.JDateChooser();
        Fecha_Salida = new com.toedter.calendar.JDateChooser();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        Fecha_Entrada = new com.toedter.calendar.JDateChooser();
        jLabel19 = new javax.swing.JLabel();
        Dia_Reserva = new javax.swing.JTextField();
        jLabel20 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        TxtObservacion = new javax.swing.JTextArea();
        BtnAtras = new javax.swing.JButton();
        BtnBorrarReserva = new javax.swing.JButton();
        BtnGuardar = new javax.swing.JButton();
        jLabel21 = new javax.swing.JLabel();
        Veh_Disponible = new javax.swing.JRadioButton();
        Veh_Nodisponible = new javax.swing.JRadioButton();
        jLabel22 = new javax.swing.JLabel();
        Id_Reserva = new javax.swing.JTextField();
        BtnBuscar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel1.setText("Reserva de Clientes ");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 100, -1, -1));

        jLabel2.setText("Matricula: ");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 200, -1, -1));
        jPanel1.add(TxtMatricula, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 200, 120, -1));

        jLabel3.setText("Marca: ");
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 230, -1, -1));
        jPanel1.add(TxtMarca, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 230, 120, -1));

        jLabel4.setText("Modelo: ");
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 260, -1, -1));
        jPanel1.add(TxtModelo, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 260, 120, -1));

        jLabel5.setText("Tipo Vehiculo: ");
        jPanel1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 290, -1, -1));
        jPanel1.add(TxtTipoVehiculo, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 290, 130, -1));

        jLabel6.setText("Tipo motor: ");
        jPanel1.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 320, -1, -1));
        jPanel1.add(TxtTipoMotor, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 320, 130, -1));

        jLabel7.setText("Cedula:");
        jPanel1.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 200, -1, -1));
        jPanel1.add(TxtCedula, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 200, 160, -1));
        jPanel1.add(TxtNombre, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 240, 160, -1));

        jLabel9.setText("Nombre:");
        jPanel1.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 240, -1, -1));

        jLabel11.setText("Apellidos:");
        jPanel1.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 270, -1, -1));
        jPanel1.add(TxtApellido, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 270, 150, -1));

        jLabel12.setText("ID Oferta: ");
        jPanel1.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 350, -1, -1));
        jPanel1.add(Id_Oferta, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 350, 120, -1));

        jLabel13.setText("Precio Oferta: ");
        jPanel1.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 380, -1, -1));
        jPanel1.add(TxtPrecioOferta, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 380, 120, -1));

        jLabel14.setText("Total de la reserva: ");
        jPanel1.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 410, -1, -1));
        jPanel1.add(Total_Reservas, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 410, 110, -1));

        jLabel15.setText("Fecha reserva: ");
        jPanel1.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 300, -1, -1));
        jPanel1.add(Fecha_Reserva, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 300, 130, -1));
        jPanel1.add(Fecha_Salida, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 330, 130, -1));

        jLabel17.setText("Fecha Salida: ");
        jPanel1.add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 330, -1, -1));

        jLabel18.setText("Fecha Entrada:");
        jPanel1.add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 360, -1, -1));
        jPanel1.add(Fecha_Entrada, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 360, 130, -1));

        jLabel19.setText("Dias reservados:");
        jPanel1.add(jLabel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 390, -1, -1));
        jPanel1.add(Dia_Reserva, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 390, 60, -1));

        jLabel20.setText("Observacion: ");
        jPanel1.add(jLabel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 460, -1, -1));

        TxtObservacion.setColumns(20);
        TxtObservacion.setRows(5);
        jScrollPane1.setViewportView(TxtObservacion);

        jPanel1.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 460, 400, 50));

        BtnAtras.setText("Atras");
        BtnAtras.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnAtrasActionPerformed(evt);
            }
        });
        jPanel1.add(BtnAtras, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 530, -1, -1));

        BtnBorrarReserva.setText("Borrar Reserva");
        BtnBorrarReserva.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnBorrarReservaActionPerformed(evt);
            }
        });
        jPanel1.add(BtnBorrarReserva, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 530, -1, -1));

        BtnGuardar.setText("Guardar");
        BtnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnGuardarActionPerformed(evt);
            }
        });
        jPanel1.add(BtnGuardar, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 530, -1, -1));

        jLabel21.setText("Status: ");
        jPanel1.add(jLabel21, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 420, -1, -1));

        Veh_Disponible.setText("Disponible");
        jPanel1.add(Veh_Disponible, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 420, -1, -1));

        Veh_Nodisponible.setText("No disponible");
        jPanel1.add(Veh_Nodisponible, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 420, -1, -1));

        jLabel22.setText("ID Reserva:");
        jPanel1.add(jLabel22, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 170, -1, -1));
        jPanel1.add(Id_Reserva, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 170, 120, -1));

        BtnBuscar.setText("Buscar");
        BtnBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnBuscarActionPerformed(evt);
            }
        });
        jPanel1.add(BtnBuscar, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 530, -1, -1));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 570, 620));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void BtnAtrasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnAtrasActionPerformed
        this.dispose();
    }//GEN-LAST:event_BtnAtrasActionPerformed

    private void BtnBorrarReservaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnBorrarReservaActionPerformed
        int confirmacion = JOptionPane.showConfirmDialog(this, "¿Está seguro de borrar la reserva?", "Confirmar", JOptionPane.YES_NO_OPTION);
    if (confirmacion == JOptionPane.YES_OPTION) {
        borrarReserva();
    }
    }//GEN-LAST:event_BtnBorrarReservaActionPerformed

    private void BtnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnGuardarActionPerformed
          
        guardarReserva();
    
    }//GEN-LAST:event_BtnGuardarActionPerformed

    private void BtnBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnBuscarActionPerformed
        buscarReservaPorId();
    }//GEN-LAST:event_BtnBuscarActionPerformed

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
            java.util.logging.Logger.getLogger(ReservasClientes.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ReservasClientes.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ReservasClientes.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ReservasClientes.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ReservasClientes().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BtnAtras;
    private javax.swing.JButton BtnBorrarReserva;
    private javax.swing.JButton BtnBuscar;
    private javax.swing.JButton BtnGuardar;
    private javax.swing.JTextField Dia_Reserva;
    private com.toedter.calendar.JDateChooser Fecha_Entrada;
    private com.toedter.calendar.JDateChooser Fecha_Reserva;
    private com.toedter.calendar.JDateChooser Fecha_Salida;
    private javax.swing.JTextField Id_Oferta;
    private javax.swing.JTextField Id_Reserva;
    private javax.swing.JTextField Total_Reservas;
    private javax.swing.JTextField TxtApellido;
    private javax.swing.JTextField TxtCedula;
    private javax.swing.JTextField TxtMarca;
    private javax.swing.JTextField TxtMatricula;
    private javax.swing.JTextField TxtModelo;
    private javax.swing.JTextField TxtNombre;
    private javax.swing.JTextArea TxtObservacion;
    private javax.swing.JTextField TxtPrecioOferta;
    private javax.swing.JTextField TxtTipoMotor;
    private javax.swing.JTextField TxtTipoVehiculo;
    private javax.swing.JRadioButton Veh_Disponible;
    private javax.swing.JRadioButton Veh_Nodisponible;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
}
