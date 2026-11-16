import java.time.LocalDate;
import java.util.ArrayList;

public abstract class Evento implements Comparable<Evento>, Programable {

    //Atributos
    protected String codigo;
    protected LocalDate fecha;
    protected int hora;
    protected String sede;
    protected String estado;
    protected Resultado resultado;

    protected static final String SIN_PROGRAMAR = "Sin programar";
    protected static final String PROGRAMADO = "Programado";
    protected static final String FINALIZADO = "Finalizado";

    //Constructor
    //Crea el evento sin fecha ni resultado
    protected Evento(String codigo) {
        if (codigo == null || codigo.trim().isEmpty()) {
            throw new IllegalArgumentException(
                "El codigo no puede estar vacio.");
        }

        this.codigo = codigo.trim();
        this.fecha = null;
        this.hora = -1;
        this.sede = null;
        this.estado = SIN_PROGRAMAR;
        this.resultado = null;
    }

    //Getters
    //Devuelve el codigo del evento
    public String getCodigo() {
        return this.codigo;
    }

    //Devuelve el estado del evento
    public String getEstado() {
        return this.estado;
    }

    //Devuelve el resultado guardado
    public Resultado getResultado() {
        return this.resultado;
    }

    @Override
    //Devuelve la fecha programada
    public LocalDate getFecha() {
        return this.fecha;
    }

    @Override
    //Devuelve la hora programada
    public int getHora() {
        return this.hora;
    }

    @Override
    //Devuelve la sede programada
    public String getSede() {
        return this.sede;
    }

    //Metodos
    //Indica si el evento ya tiene resultado
    public boolean tieneResultado() {
        return this.resultado != null;
    }

    //Guarda el resultado y finaliza el evento
    boolean registrarResultado(Resultado resultado) {
        if (!PROGRAMADO.equals(this.estado)) {
            return false;
        }

        if (this.resultado != null || resultado == null) {
            return false;
        }

        if (!resultado.estaCompleto()) {
            return false;
        }

        this.resultado = resultado;
        this.estado = FINALIZADO;
        return true;
    }

    //Cada evento devuelve sus participantes
    public abstract ArrayList<Participante> getParticipantes();

    //Sobrescritura
    @Override
    //Asigna o cambia la fecha, hora y sede
    public boolean programar(LocalDate fecha, int hora, String sede) {
        if (fecha == null) {
            return false;
        }

        if (hora < 0 || hora > 23) {
            return false;
        }

        if (sede == null || sede.trim().isEmpty()) {
            return false;
        }

        if (FINALIZADO.equals(this.estado) || this.resultado != null) {
            return false;
        }

        this.fecha = fecha;
        this.hora = hora;
        this.sede = sede.trim();
        this.estado = PROGRAMADO;
        return true;
    }

    @Override
    //Compara eventos por fecha, hora y sede
    public int compareTo(Evento otroEvento) {
        if (otroEvento == null) {
            throw new IllegalArgumentException(
                "El evento a comparar no puede ser nulo.");
        }

        if (this.fecha == null || otroEvento.fecha == null ||
            this.sede == null || otroEvento.sede == null) {
            throw new IllegalStateException(
                "No se pueden comparar eventos sin programar.");
        }

        int comparacionFecha = this.fecha.compareTo(otroEvento.fecha);

        if (comparacionFecha != 0) {
            return comparacionFecha;
        }

        if (this.hora != otroEvento.hora) {
            return Integer.compare(this.hora, otroEvento.hora);
        }

        return this.sede.compareTo(otroEvento.sede);
    }

    @Override
    //Indica si el evento esta listo para jugarse
    public boolean estaProgramado() {
        return PROGRAMADO.equals(this.estado);
    }
}
