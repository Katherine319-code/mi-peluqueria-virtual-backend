package com.peluqueria.mipeluqueriavirtual.service;

import com.peluqueria.mipeluqueriavirtual.entity.Cita;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    private static final DateTimeFormatter FORMATO_FECHA =
            DateTimeFormatter.ofPattern("EEEE d 'de' MMMM 'de' yyyy", new Locale("es", "ES"));

    public void enviarConfirmacionCita(Cita cita) {
        String correoCliente = cita.getCliente().getCorreo();
        if (correoCliente == null || correoCliente.isBlank()) return;

        String nombreCliente    = cita.getCliente().getNombre();
        String nombreEstilista  = cita.getEstilista().getUsuario().getNombre() + " "
                + cita.getEstilista().getUsuario().getApellido();
        String servicio         = cita.getServicio().getNombre();
        String fecha             = cita.getFecha().format(FORMATO_FECHA);
        String hora               = cita.getHora().toString().substring(0, 5);

        String asunto = "Confirmacion de tu cita - Mi Peluqueria Virtual";
        String cuerpo = "Hola " + nombreCliente + ",\n\n"
                + "Tu cita ha sido confirmada con los siguientes detalles:\n\n"
                + "Servicio: " + servicio + "\n"
                + "Estilista: " + nombreEstilista + "\n"
                + "Fecha: " + fecha + "\n"
                + "Hora: " + hora + "\n"
                + "Total: $" + String.format("%,.0f", cita.getTotal()) + "\n\n"
                + "Te esperamos en Mi Peluqueria Virtual.\n\n"
                + "Si necesitas cancelar o reprogramar, puedes hacerlo desde la app en la seccion 'Mis citas'.";

        SimpleMailMessage mensaje = new SimpleMailMessage();
        mensaje.setTo(correoCliente);
        mensaje.setSubject(asunto);
        mensaje.setText(cuerpo);

        try {
            mailSender.send(mensaje);
        } catch (Exception e) {
            // No queremos que un fallo de correo tumbe la creacion de la cita
            System.err.println("No se pudo enviar el correo de confirmacion: " + e.getMessage());
        }
    }

    public void enviarCodigoRecuperacion(String correo, String nombre, String codigo) {
        String asunto = "Recuperacion de contrasena - Mi Peluqueria Virtual";
        String cuerpo = "Hola " + nombre + ",\n\n"
                + "Recibimos una solicitud para restablecer tu contrasena.\n\n"
                + "Tu codigo de verificacion es: " + codigo + "\n\n"
                + "Este codigo es valido por 15 minutos. Si no solicitaste este cambio, "
                + "puedes ignorar este correo con confianza.\n\n"
                + "Mi Peluqueria Virtual";

        SimpleMailMessage mensaje = new SimpleMailMessage();
        mensaje.setTo(correo);
        mensaje.setSubject(asunto);
        mensaje.setText(cuerpo);

        try {
            mailSender.send(mensaje);
        } catch (Exception e) {
            System.err.println("No se pudo enviar el correo de recuperacion: " + e.getMessage());
        }
    }

}



