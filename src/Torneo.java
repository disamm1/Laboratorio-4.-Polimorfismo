
import java.util.ArrayList;
import java.util.Collections;

public class Torneo {

    //Atributos
    private String codigo;
    private String nombre;
    private Disciplina disciplina;
    private String estado;
    private ArrayList<Posicion> posiciones;
    private ArrayList<Evento> eventos;
    private double premio;
    private String mensaje;

    private static final String INSCRIPCION = "EN INSCRIPCION";
    private static final String EN_JUEGO = "EN JUEGO";
    private static final String FINALIZADO = "FINALIZADO";

    //Constructor
    //Crea el torneo en estado de inscripcion
    public Torneo(String codigo, String nombre, Disciplina disciplina) {

        if (codigo == null || codigo.trim().isEmpty()) {
            throw new IllegalArgumentException("El codigo no puede estar vacio.");
        }

        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede estar vacio.");
        }

        if (disciplina == null) {
            throw new IllegalArgumentException("La disciplina no puede ser nula.");
        }

        this.codigo = codigo.trim();
        this.nombre = nombre.trim();
        this.disciplina = disciplina;
        this.posiciones = new ArrayList<>();
        this.eventos = new ArrayList<>();
        this.premio = 0.0;
        this.estado = INSCRIPCION;
        this.mensaje = "";
    }

    //Getters
    //Devuelve el codigo del torneo
    public String getCodigo() {
        return this.codigo;
    }

    //Devuelve el nombre del torneo
    public String getNombre() {
        return this.nombre;
    }

    //Devuelve la disciplina del torneo
    public Disciplina getDisciplina() {
        return this.disciplina;
    }

    //Devuelve el estado actual del torneo
    public String getEstado() {
        return this.estado;
    }

    //Devuelve el premio calculado
    public double getPremio() {
        return this.premio;
    }

    //Devuelve el ultimo mensaje del torneo
    public String getMensaje() {
        return this.mensaje;
    }

    //Devuelve los participantes inscritos
    public ArrayList<Participante> getParticipantes() {
        ArrayList<Participante> participantes = new ArrayList<>();

        for (Posicion posicion : posiciones) {
            participantes.add(posicion.getParticipante());
        }

        return participantes;
    }

    //Devuelve una copia de los eventos
    public ArrayList<Evento> getEventos() {
        return new ArrayList<>(eventos);
    }

    //Metodos
    //Inscribe un participante si cumple las reglas
    public boolean inscribir(Participante p) {

        if (p == null) {
            this.mensaje = "El participante no existe.";
            return false;
        }

        if (!INSCRIPCION.equals(this.estado)) {
            this.mensaje = "El torneo ya no esta en inscripcion.";
            return false;
        }

        if (posiciones.size() >= disciplina.getMaximo()) {
            this.mensaje = "El torneo alcanzo el maximo de participantes.";
            return false;
        }

        int participantesEmpresa = 0;

        for (Posicion posicion : posiciones) {
            Participante inscrito = posicion.getParticipante();

            if (inscrito.getCodigo().equals(p.getCodigo())) {
                this.mensaje = "El participante ya esta inscrito.";
                return false;
            }

            if (inscrito.getEmpresa().equals(p.getEmpresa())) {
                participantesEmpresa++;
            }
        }

        if (participantesEmpresa >= 2) {
            this.mensaje = "La empresa ya tiene dos participantes.";
            return false;
        }

        posiciones.add(new Posicion(p));
        this.mensaje = "Inscripcion exitosa.";
        return true;
    }

    //Cierra las inscripciones y crea los eventos
    public boolean cerrarInscripciones() {

    if (!INSCRIPCION.equals(this.estado)) {
        this.mensaje = "El torneo ya cerro inscripciones.";
        return false;
    }

    if (posiciones.size() < disciplina.getMinimo()) {
        this.mensaje = "No se alcanzo el minimo de participantes.";
        return false;
    }

    ArrayList<Evento> nuevosEventos;

    try {
        nuevosEventos = disciplina.crearEventos(getParticipantes());
    } catch (IllegalArgumentException e) {
        this.mensaje = e.getMessage();
        return false;
    }

    if (nuevosEventos == null || nuevosEventos.isEmpty()) {
        this.mensaje = "No se pudieron crear los eventos.";
        return false;
    }

    this.eventos = nuevosEventos;
    this.estado = EN_JUEGO;
    this.mensaje = "Inscripciones cerradas.";

    return true;
}

    //Registra un resultado y actualiza la tabla
    public boolean registrarResultado(Evento evento, Resultado resultado) {

        if (!EN_JUEGO.equals(this.estado)) {
            this.mensaje = "El torneo no esta en juego.";
            return false;
        }

        if (evento == null || resultado == null) {
            this.mensaje = "El evento y el resultado son obligatorios.";
            return false;
        }

        boolean encontrado = false;

        for (Evento e : eventos) {
            if (e == evento) {
                encontrado = true;
            }
        }

        if (!encontrado) {
            this.mensaje = "El evento no pertenece al torneo.";
            return false;
        }

        if (!evento.estaProgramado() || evento.tieneResultado()) {
            this.mensaje = "El evento no esta programado o ya finalizo.";
            return false;
        }

        if (!disciplina.validarResultado(evento, resultado)) {
            this.mensaje = disciplina.getMensaje();
            return false;
        }

        ArrayList<Puntuacion> puntuaciones =
            disciplina.calcularPuntuaciones(evento, resultado);

        if (puntuaciones == null ||
            puntuaciones.size() != evento.getParticipantes().size()) {
            this.mensaje = "Las puntuaciones estan incompletas.";
            return false;
        }

        ArrayList<Participante> participantesEvento =
            evento.getParticipantes();

        //Comprobar que las puntuaciones sean validas
        for (Puntuacion puntuacion : puntuaciones) {

            if (puntuacion == null ||
                puntuacion.getParticipante() == null ||
                puntuacion.getPuntos() < 0) {
                this.mensaje = "Hay una puntuacion invalida.";
                return false;
            }

            boolean perteneceEvento = false;

            for (Participante p : participantesEvento) {
                if (p.getCodigo().equals(
                        puntuacion.getParticipante().getCodigo())) {
                    perteneceEvento = true;
                }
            }

            if (!perteneceEvento) {
                this.mensaje = "La puntuacion no pertenece al evento.";
                return false;
            }

            boolean inscrito = false;

            for (Posicion posicion : posiciones) {
                if (posicion.getParticipante().getCodigo().equals(
                        puntuacion.getParticipante().getCodigo())) {
                    inscrito = true;
                }
            }

            if (!inscrito) {
                this.mensaje = "La puntuacion no pertenece al torneo.";
                return false;
            }
        }

        //Evitar puntuaciones repetidas
        for (int i = 0; i < puntuaciones.size(); i++) {
            for (int j = i + 1; j < puntuaciones.size(); j++) {

                if (puntuaciones.get(i).getParticipante().getCodigo()
                        .equals(puntuaciones.get(j).getParticipante().getCodigo())) {
                    this.mensaje = "Hay puntuaciones repetidas.";
                    return false;
                }
            }
        }

        if (!evento.registrarResultado(resultado)) {
            this.mensaje = "No se pudo guardar el resultado.";
            return false;
        }

        //Sumar los datos en la tabla
        for (Puntuacion puntuacion : puntuaciones) {
            for (Posicion posicion : posiciones) {

                if (posicion.getParticipante().getCodigo().equals(
                        puntuacion.getParticipante().getCodigo())) {

                    posicion.sumarPuntos(puntuacion.getPuntos());
                    posicion.sumarDiferencia(puntuacion.getDiferencia());

                    if (puntuacion.esVictoria()) {
                        posicion.sumarVictoria();
                    }
                }
            }
        }

        verificarFinalizacion();
        this.mensaje = "Resultado registrado.";
        return true;
    }

    //Devuelve una copia ordenada de la tabla
    public ArrayList<Posicion> tablaPosiciones() {
    ArrayList<Posicion> tabla = new ArrayList<>();

    for (Posicion posicion : posiciones) {
        Posicion copia = new Posicion(posicion.getParticipante());

        copia.sumarPuntos(posicion.getPuntos());
        copia.sumarDiferencia(posicion.getDiferencia());

        for (int i = 0; i < posicion.getVictorias(); i++) {
            copia.sumarVictoria();
        }

        tabla.add(copia);
    }

    Collections.sort(tabla);
    return tabla;
}

    //Devuelve el calendario del torneo
    public Calendario calendario() {
        return new Calendario(eventos);
    }

    //Devuelve el campeon cuando el torneo termino
    public Participante getCampeon() {

        if (!FINALIZADO.equals(this.estado)) {
            return null;
        }

        ArrayList<Posicion> tabla = tablaPosiciones();

        if (tabla.isEmpty()) {
            return null;
        }

        return tabla.get(0).getParticipante();
    }

    //Finaliza el torneo cuando todos los eventos terminaron
    private void verificarFinalizacion() {

        if (!EN_JUEGO.equals(this.estado)) {
            return;
        }

        if (eventos.isEmpty()) {
            return;
        }

        for (Evento evento : eventos) {
            if (!evento.tieneResultado()) {
                return;
            }
        }

        this.estado = FINALIZADO;
        this.premio = 0.50 * disciplina.getCuota() * posiciones.size();
    }

    //Devuelve las posiciones ordenadas
    public ArrayList<Posicion> getPosiciones() {
    return tablaPosiciones();
}
}
