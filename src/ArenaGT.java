import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;

public class ArenaGT {

    //Atributos
    private ArrayList<Participante> participantes;
    private ArrayList<Torneo> torneos;
    private double ingresos;
    private double premiosEntregados;
    private ArrayList<Double> ingresosPorDisciplina;
    private ArrayList<String> disciplinasIngresos;
    private ArrayList<Disciplina> disciplinas;
    private String mensaje;

    //Constructor
    //Prepara las listas y las disciplinas del sistema
    public ArenaGT() {
        this.participantes = new ArrayList<>();
        this.torneos = new ArrayList<>();
        this.ingresos = 0.0;
        this.premiosEntregados = 0.0;
        this.ingresosPorDisciplina = new ArrayList<>();
        this.disciplinasIngresos = new ArrayList<>();
        this.disciplinas = new ArrayList<>();
        this.mensaje = "";

        disciplinas.add(new FutbolSala());
        disciplinas.add(new FutbolSalaPenales());
        disciplinas.add(new Baloncesto());
        disciplinas.add(new Voleibol());
        disciplinas.add(new Relevos());
    }

    //Getters
    //Devuelve una copia de los participantes
    public ArrayList<Participante> getParticipantes() {
        return new ArrayList<>(participantes);
    }

    //Devuelve una copia de los torneos
    public ArrayList<Torneo> getTorneos() {
        return new ArrayList<>(torneos);
    }

    //Devuelve el total de premios entregados
    public double getPremiosEntregados() {
        return premiosEntregados;
    }

    //Devuelve las disciplinas disponibles
    public ArrayList<Disciplina> getDisciplinas() {
        return new ArrayList<>(disciplinas);
    }

    //Devuelve el ultimo mensaje del sistema
    public String getMensaje() {
        return mensaje;
    }

    //Metodos
    //Registra un participante si sus datos son validos
    public Participante registrarParticipante(String codigo, String nombre, String empresa) {

        if (codigo == null || codigo.trim().isEmpty()) {
            this.mensaje = "El codigo no puede estar vacio.";
            return null;
        }

        if (nombre == null || nombre.trim().isEmpty()) {
            this.mensaje = "El nombre no puede estar vacio.";
            return null;
        }

        if (empresa == null || empresa.trim().isEmpty()) {
            this.mensaje = "La empresa no puede estar vacia.";
            return null;
        }

        if (buscarParticipante(codigo.trim()) != null) {
            this.mensaje = "Ya existe un participante con ese codigo.";
            return null;
        }

        Participante participante = new Participante(
            codigo.trim(), nombre.trim(), empresa.trim()
        );

        participantes.add(participante);
        this.mensaje = "Participante registrado.";
        return participante;
    }

    //Crea un torneo si el codigo no esta repetido
    public Torneo crearTorneo(String codigo, String nombre, Disciplina disciplina) {

        if (codigo == null || codigo.trim().isEmpty()) {
            this.mensaje = "El codigo no puede estar vacio.";
            return null;
        }

        if (nombre == null || nombre.trim().isEmpty()) {
            this.mensaje = "El nombre no puede estar vacio.";
            return null;
        }

        if (disciplina == null) {
            this.mensaje = "La disciplina es obligatoria.";
            return null;
        }

        if (buscarTorneo(codigo.trim()) != null) {
            this.mensaje = "Ya existe un torneo con ese codigo.";
            return null;
        }

        Torneo torneo = new Torneo(codigo.trim(), nombre.trim(), disciplina);
        torneos.add(torneo);
        this.mensaje = "Torneo creado.";

        return torneo;
    }

    //Busca un participante por su codigo
    public Participante buscarParticipante(String codigo) {

        if (codigo == null) {
            return null;
        }

        for (Participante p : participantes) {
            if (p.getCodigo().equals(codigo.trim())) {
                return p;
            }
        }

        return null;
    }

    //Inscribe participantes iniciales sin cobrar
    private boolean inscribirInicial(String codTorneo, String codParticipante) {

    Torneo torneo = buscarTorneo(codTorneo);
    Participante participante = buscarParticipante(codParticipante);

    if (torneo == null || participante == null) {
        return false;
    }

    return torneo.inscribir(participante);
}

//Guarda un resultado inicial sin sumar premios al reporte
private boolean registrarResultadoInicial(Evento evento, Resultado resultado) {

    if (evento == null || resultado == null) {
        return false;
    }

    for (Torneo torneo : torneos) {
        for (Evento e : torneo.getEventos()) {
            if (e == evento) {
                return torneo.registrarResultado(evento, resultado);
            }
        }
    }

    return false;
}

    //Busca un torneo por su codigo
    public Torneo buscarTorneo(String codigo) {

        if (codigo == null) {
            return null;
        }

        for (Torneo torneo : torneos) {
            if (torneo.getCodigo().equals(codigo.trim())) {
                return torneo;
            }
        }

        return null;
    }

    //Calcula el costo de una inscripcion
    public double cotizar(Disciplina disciplina, int cantidad) {

        if (disciplina == null) {
            this.mensaje = "La disciplina es obligatoria.";
            return -1;
        }

        try {
            double total = disciplina.calcularCosto(cantidad);
            this.mensaje = "Cotizacion calculada.";
            return total;
        } catch (IllegalArgumentException e) {
            this.mensaje = e.getMessage();
            return -1;
        }
    }

    //Inscribe un participante y suma el ingreso
    public boolean inscribir(String codTorneo, String codParticipante) {

        Torneo torneo = buscarTorneo(codTorneo);
        Participante participante = buscarParticipante(codParticipante);

        if (torneo == null || participante == null) {
            this.mensaje = "El torneo o el participante no existe.";
            return false;
        }

        if (!torneo.inscribir(participante)) {
            this.mensaje = torneo.getMensaje();
            return false;
        }

        double cuota = torneo.getDisciplina().getCuota();

        ingresos += cuota;

        String nombreDisciplina = torneo.getDisciplina().getNombre();

        //Buscar la disciplina en el reporte
        int indice = -1;

        for (int i = 0; i < disciplinasIngresos.size(); i++) {
            if (indice == -1 &&
                disciplinasIngresos.get(i).equals(nombreDisciplina)) {
                indice = i;
            }
        }

        if (indice == -1) {
            disciplinasIngresos.add(nombreDisciplina);
            ingresosPorDisciplina.add(cuota);
        } else {
            ingresosPorDisciplina.set(
                indice,
                ingresosPorDisciplina.get(indice) + cuota
            );
        }

        this.mensaje = "Inscripcion exitosa.";
        return true;
    }

    //Cierra las inscripciones de un torneo
    public boolean cerrarInscripciones(String codTorneo) {

        Torneo torneo = buscarTorneo(codTorneo);

        if (torneo == null) {
            this.mensaje = "El torneo no existe.";
            return false;
        }

        boolean cerrado = torneo.cerrarInscripciones();
        this.mensaje = torneo.getMensaje();
        return cerrado;
    }

    //Programa un evento si no hay conflictos
    public boolean programarEvento(Evento evento, LocalDate fecha, int hora, String sede) {

        if (evento == null || fecha == null) {
            this.mensaje = "El evento y la fecha son obligatorios.";
            return false;
        }

        if (hora < 0 || hora > 23) {
            this.mensaje = "La hora debe estar entre 0 y 23.";
            return false;
        }

        if (sede == null || sede.trim().isEmpty()) {
            this.mensaje = "La sede no puede estar vacia.";
            return false;
        }

        if (evento.tieneResultado()) {
            this.mensaje = "Un evento finalizado no se puede reprogramar.";
            return false;
        }

        //Revisar que el evento sea del sistema
        boolean perteneceSistema = false;

        //Revisar conflictos con los demas eventos
        for (Torneo torneo : torneos) {
            for (Evento e : torneo.getEventos()) {
                if (e == evento) {
                    perteneceSistema = true;
                }
            }
        }

        if (!perteneceSistema) {
            this.mensaje = "El evento no pertenece al sistema.";
            return false;
        }

        for (Torneo torneo : torneos) {

            for (Evento otro : torneo.getEventos()) {

                if (otro == evento) {
                    continue;
                }

                if (otro.getFecha() == null || otro.getSede() == null) {
                    continue;
                }

                if (otro.getFecha().equals(fecha) &&
                    otro.getHora() == hora) {

                    if (otro.getSede().equals(sede.trim())) {
                        this.mensaje = "La sede ya tiene un evento en esa fecha y hora.";
                        return false;
                    }

                    ArrayList<Participante> participantesEvento =
                        evento.getParticipantes();

                    ArrayList<Participante> participantesOtro =
                        otro.getParticipantes();

                    for (Participante p1 : participantesEvento) {
                        for (Participante p2 : participantesOtro) {

                            if (p1.getCodigo().equals(p2.getCodigo())) {
                                this.mensaje = "Un participante ya tiene evento en esa fecha y hora.";
                                return false;
                            }
                        }
                    }
                }
            }
        }

        boolean programado = evento.programar(fecha, hora, sede.trim());

        if (programado) {
            this.mensaje = "Evento programado.";
        } else {
            this.mensaje = "No se pudo programar el evento.";
        }

        return programado;
    }

    //Registra el resultado y actualiza los premios
    public boolean registrarResultado(Evento evento, Resultado resultado) {

        if (evento == null || resultado == null) {
            this.mensaje = "El evento y el resultado son obligatorios.";
            return false;
        }

        for (Torneo torneo : torneos) {

            boolean pertenece = false;

            for (Evento e : torneo.getEventos()) {
                if (e == evento) {
                    pertenece = true;
                }
            }

            if (pertenece) {

                double premioAnterior = torneo.getPremio();

                if (!torneo.registrarResultado(evento, resultado)) {
                    this.mensaje = torneo.getMensaje();
                    return false;
                }

                double premioNuevo = torneo.getPremio();

                if (premioNuevo > premioAnterior) {
                    premiosEntregados += premioNuevo - premioAnterior;
                }

                this.mensaje = "Resultado registrado.";
                return true;
            }
        }

        this.mensaje = "El evento no pertenece al sistema.";
        return false;
    }

    //Arma el calendario con todos los torneos
    public Calendario calendarioGeneral() {

        ArrayList<Evento> eventos = new ArrayList<>();

        for (Torneo torneo : torneos) {
            eventos.addAll(torneo.getEventos());
        }

        return new Calendario(eventos);
    }

    //Arma el calendario de un participante
    public Calendario calendarioParticipante(Participante p) {

        ArrayList<Evento> eventos = new ArrayList<>();

        if (p == null) {
            return new Calendario(eventos);
        }

        for (Torneo torneo : torneos) {

            for (Evento evento : torneo.getEventos()) {

                boolean participa = false;

                for (Participante participante : evento.getParticipantes()) {

                    if (participante.getCodigo().equals(p.getCodigo())) {
                        participa = true;
                    }
                }

                if (participa) {
                    eventos.add(evento);
                }
            }
        }

        return new Calendario(eventos);
    }

    //Devuelve todos los ingresos cobrados
    public double ingresosTotales() {
        return ingresos;
    }

    //Devuelve los ingresos de una disciplina
    public double ingresosDisciplina(Disciplina disciplina) {

        if (disciplina == null) {
            return 0.0;
        }

        for (int i = 0; i < disciplinasIngresos.size(); i++) {

            if (disciplinasIngresos.get(i).equals(disciplina.getNombre())) {
                return ingresosPorDisciplina.get(i);
            }
        }

        return 0.0;
    }

    //Suma los puntos de cada empresa y los ordena
    public ArrayList<PosicionEmpresa> rankingEmpresas() {

        ArrayList<PosicionEmpresa> ranking = new ArrayList<>();

        for (Torneo torneo : torneos) {

            for (Posicion posicion : torneo.tablaPosiciones()) {

                String empresa = posicion.getParticipante().getEmpresa();
                int puntos = posicion.getPuntos();

                PosicionEmpresa posicionEmpresa = null;

                for (PosicionEmpresa pe : ranking) {
                    if (pe.getEmpresa().equals(empresa)) {
                        posicionEmpresa = pe;
                    }
                }

                if (posicionEmpresa == null) {
                    posicionEmpresa = new PosicionEmpresa(empresa);
                    ranking.add(posicionEmpresa);
                }

                posicionEmpresa.sumarPuntos(puntos);
            }
        }

        Collections.sort(ranking);

        return ranking;
    }

    //Carga los datos que usa el programa al iniciar
    public boolean cargarDatosIniciales() {
        if (!participantes.isEmpty() || !torneos.isEmpty()) {
            this.mensaje = "Los datos iniciales ya fueron cargados.";
            return false;
        }

        registrarParticipante("P1", "Aguilas", "Empresa A");
        registrarParticipante("P2", "Leones", "Empresa A");
        registrarParticipante("P3", "Tigres", "Empresa B");
        registrarParticipante("P4", "Lobos", "Empresa B");
        registrarParticipante("P5", "Halcones", "Empresa C");
        registrarParticipante("P6", "Toros", "Empresa C");
        registrarParticipante("P7", "Panteras", "Empresa D");
        registrarParticipante("P8", "Dragones", "Empresa D");

        crearTorneo("T1", "Copa Futbol Sala", disciplinas.get(0));
        crearTorneo("T2", "Copa Penales", disciplinas.get(1));
        crearTorneo("T3", "Copa Baloncesto", disciplinas.get(2));
        crearTorneo("T4", "Copa Voleibol", disciplinas.get(3));
        crearTorneo("T5", "Copa Relevos", disciplinas.get(4));

        inscribirInicial("T1", "P1");
        inscribirInicial("T1", "P2");
        inscribirInicial("T1", "P3");
        inscribirInicial("T1", "P5");
        inscribirInicial("T1", "P7");

        String[] torneosCuatro = {"T2", "T3", "T4", "T5"};

        for (String codigoTorneo : torneosCuatro) {
            inscribirInicial(codigoTorneo, "P1");
            inscribirInicial(codigoTorneo, "P3");
            inscribirInicial(codigoTorneo, "P5");
            inscribirInicial(codigoTorneo, "P7");
        }

        cerrarInscripciones("T1");
        cerrarInscripciones("T2");
        cerrarInscripciones("T3");
        cerrarInscripciones("T4");
        cerrarInscripciones("T5");

        programarEventosIniciales("T1", LocalDate.of(2026, 11, 15), 10,
            "Estadio Central");
        programarEventosIniciales("T2", LocalDate.of(2026, 12, 15), 11,
            "Estadio Norte");
        programarEventosIniciales("T3", LocalDate.of(2027, 1, 15), 12,
            "Gimnasio Central");
        programarEventosIniciales("T4", LocalDate.of(2027, 2, 15), 13,
            "Gimnasio Norte");
        programarEventosIniciales("T5", LocalDate.of(2027, 3, 15), 14,
            "Pista Central");

        ArrayList<Evento> futbol = buscarTorneo("T1").getEventos();
        registrarResultadoInicial(futbol.get(0), new resultadoMarcador(0, 0));
        registrarResultadoInicial(futbol.get(1), new resultadoMarcador(2, 0));
        registrarResultadoInicial(futbol.get(2), new resultadoMarcador(0, 1));
        registrarResultadoInicial(futbol.get(3), new resultadoMarcador(0, 1));
        registrarResultadoInicial(futbol.get(4), new resultadoMarcador(0, 0));
        registrarResultadoInicial(futbol.get(5), new resultadoMarcador(0, 0));
        registrarResultadoInicial(futbol.get(6), new resultadoMarcador(0, 0));

        ArrayList<Evento> penales = buscarTorneo("T2").getEventos();
        registrarResultadoInicial(
            penales.get(0), new ResultadoPenales(2, 2, 4, 3));

        ArrayList<Evento> baloncesto = buscarTorneo("T3").getEventos();
        registrarResultadoInicial(
            baloncesto.get(0), new resultadoMarcador(10, 9));

        ArrayList<Evento> voleibol = buscarTorneo("T4").getEventos();
        registrarResultadoInicial(
            voleibol.get(0), new resultadoMarcador(3, 0));
        registrarResultadoInicial(
            voleibol.get(1), new resultadoMarcador(3, 2));

        this.ingresos = 0.0;
        this.premiosEntregados = 0.0;
        this.ingresosPorDisciplina.clear();
        this.disciplinasIngresos.clear();
        this.mensaje = "Datos iniciales cargados.";
        return true;
    }

    //Programa todos los eventos iniciales de un torneo
    private void programarEventosIniciales(
            String codigoTorneo, LocalDate fecha, int hora, String sede) {

        Torneo torneo = buscarTorneo(codigoTorneo);
        ArrayList<Evento> eventos = torneo.getEventos();

        for (int i = 0; i < eventos.size(); i++) {
            programarEvento(eventos.get(i), fecha.plusDays(i), hora, sede);
        }
    }
}
