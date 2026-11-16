
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    //Atributos
    private static ArenaGT sistema = new ArenaGT();
    private static Scanner scanner = new Scanner(System.in);

    //Metodos
    //Inicia los datos y mantiene abierto el menu
    public static void main(String[] args) {

        cargarDatosIniciales();

        int opcion = -1;

        while (opcion != 0) {
            mostrarMenu();
            opcion = leerEntero("Seleccione una opcion: ");

            try {
                procesarOpcion(opcion);
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }

        scanner.close();
    }

    //Muestra todas las opciones del programa
    private static void mostrarMenu() {

        System.out.println("\n========== ARENAGT ==========");
        System.out.println("1. Registrar participante");
        System.out.println("2. Crear torneo");
        System.out.println("3. Cotizar inscripcion");
        System.out.println("4. Inscribir participante");
        System.out.println("5. Cerrar inscripciones");
        System.out.println("6. Programar o reprogramar evento");
        System.out.println("7. Registrar resultado");
        System.out.println("8. Consultar torneos");
        System.out.println("9. Consultar participantes");
        System.out.println("10. Calendario general");
        System.out.println("11. Calendario de torneo");
        System.out.println("12. Calendario de participante");
        System.out.println("13. Tabla de posiciones");
        System.out.println("14. Campeon de torneo");
        System.out.println("15. Reporte financiero");
        System.out.println("16. Ranking de empresas");
        System.out.println("0. Salir");
    }

    //Llama al metodo que corresponde a la opcion
    private static void procesarOpcion(int opcion) {

        if (opcion == 1) {
            registrarParticipante();

        } else if (opcion == 2) {
            crearTorneo();

        } else if (opcion == 3) {
            cotizar();

        } else if (opcion == 4) {
            inscribir();

        } else if (opcion == 5) {
            cerrarInscripciones();

        } else if (opcion == 6) {
            programarEvento();

        } else if (opcion == 7) {
            registrarResultado();

        } else if (opcion == 8) {
            consultarTorneos();

        } else if (opcion == 9) {
            consultarParticipantes();

        } else if (opcion == 10) {
            mostrarCalendario(sistema.calendarioGeneral());

        } else if (opcion == 11) {
            calendarioTorneo();

        } else if (opcion == 12) {
            calendarioParticipante();

        } else if (opcion == 13) {
            tablaPosiciones();

        } else if (opcion == 14) {
            mostrarCampeon();

        } else if (opcion == 15) {
            reporteFinanciero();

        } else if (opcion == 16) {
            rankingEmpresas();

        } else if (opcion == 0) {
            System.out.println("Saliendo de ArenaGT...");

        } else {
            System.out.println("Opcion invalida.");
        }
    }

    //Lee un entero sin dejar que el programa se cierre
    private static int leerEntero(String mensaje) {

        boolean valido = false;
        int numero = 0;

        while (!valido) {
            System.out.print(mensaje);

            try {
                numero = Integer.parseInt(scanner.nextLine().trim());
                valido = true;
            } catch (NumberFormatException e) {
                System.out.println("Ingrese un numero entero valido.");
            }
        }

        return numero;
    }

    //Lee un texto y le quita espacios de los lados
    private static String leerTexto(String mensaje) {
        System.out.print(mensaje);
        return scanner.nextLine().trim();
    }

    //Lee una fecha hasta que tenga el formato correcto
    private static LocalDate leerFecha() {

        LocalDate fecha = null;

        while (fecha == null) {
            String entrada = leerTexto("Fecha (AAAA-MM-DD): ");

            try {
                fecha = LocalDate.parse(entrada);
            } catch (Exception e) {
                System.out.println("Fecha invalida.");
            }
        }

        return fecha;
    }

    //Muestra las disciplinas y devuelve la elegida
    private static Disciplina seleccionarDisciplina() {

        ArrayList<Disciplina> disciplinas = sistema.getDisciplinas();

        for (int i = 0; i < disciplinas.size(); i++) {
            System.out.println((i + 1) + ". " + disciplinas.get(i).getNombre());
        }

        int opcion = leerEntero("Disciplina: ");

        if (opcion < 1 || opcion > disciplinas.size()) {
            System.out.println("Disciplina invalida.");
            return null;
        }

        return disciplinas.get(opcion - 1);
    }

    //Pide los datos para registrar un participante
    private static void registrarParticipante() {

        String codigo = leerTexto("Codigo: ");
        String nombre = leerTexto("Nombre: ");
        String empresa = leerTexto("Empresa: ");

        Participante p = sistema.registrarParticipante(
            codigo, nombre, empresa
        );

        if (p == null) {
            System.out.println(sistema.getMensaje());
        } else {
            System.out.println(sistema.getMensaje());
        }
    }

    //Pide los datos para crear un torneo
    private static void crearTorneo() {

        String codigo = leerTexto("Codigo del torneo: ");
        String nombre = leerTexto("Nombre del torneo: ");
        Disciplina disciplina = seleccionarDisciplina();

        if (disciplina != null) {
            Torneo torneo = sistema.crearTorneo(
                codigo, nombre, disciplina
            );

            if (torneo == null) {
                System.out.println(sistema.getMensaje());
            } else {
                System.out.println(sistema.getMensaje());
            }
        }
    }

    //Muestra cuanto costaria una inscripcion
    private static void cotizar() {

        Disciplina disciplina = seleccionarDisciplina();

        if (disciplina != null) {
            int cantidad = leerEntero("Cantidad: ");
            double total = sistema.cotizar(disciplina, cantidad);

            if (total < 0) {
                System.out.println(sistema.getMensaje());
            } else {
                System.out.printf("Total: Q%.2f%n", total);
            }
        }
    }

    //Inscribe un participante en un torneo
    private static void inscribir() {

        String torneo = leerTexto("Codigo del torneo: ");
        String participante = leerTexto("Codigo del participante: ");

        if (sistema.inscribir(torneo, participante)) {
            System.out.println(sistema.getMensaje());
        } else {
            System.out.println(sistema.getMensaje());
        }
    }

    //Cierra las inscripciones del torneo indicado
    private static void cerrarInscripciones() {

        String codigo = leerTexto("Codigo del torneo: ");

        if (sistema.cerrarInscripciones(codigo)) {
            System.out.println(sistema.getMensaje());
        } else {
            System.out.println(sistema.getMensaje());
        }
    }

    //Muestra los eventos de un torneo y devuelve el elegido
    private static Evento seleccionarEvento() {

        String codigo = leerTexto("Codigo del torneo: ");
        Torneo torneo = sistema.buscarTorneo(codigo);

        if (torneo == null) {
            System.out.println("Torneo no encontrado.");
            return null;
        }

        ArrayList<Evento> eventos = torneo.getEventos();

        if (eventos.isEmpty()) {
            System.out.println("No hay eventos.");
            return null;
        }

        for (int i = 0; i < eventos.size(); i++) {
            System.out.println(
                (i + 1) + ". " + eventos.get(i).getCodigo()
            );
        }

        int indice = leerEntero("Numero de evento: ") - 1;

        if (indice < 0 || indice >= eventos.size()) {
            System.out.println("Evento invalido.");
            return null;
        }

        return eventos.get(indice);
    }

    //Pide fecha, hora y sede para programar un evento
    private static void programarEvento() {

        Evento evento = seleccionarEvento();

        if (evento != null) {

            LocalDate fecha = leerFecha();
            int hora = leerEntero("Hora (0-23): ");
            String sede = leerTexto("Sede: ");

            if (sistema.programarEvento(evento, fecha, hora, sede)) {
                System.out.println(sistema.getMensaje());
            } else {
                System.out.println(sistema.getMensaje());
            }
        }
    }

    //Pide y registra el resultado de un evento
    private static void registrarResultado() {

        Evento evento = seleccionarEvento();

        if (evento == null) {
            return;
        }

        //Buscar a que torneo pertenece el evento
        Torneo torneoEvento = null;

        for (Torneo torneo : sistema.getTorneos()) {
            for (Evento e : torneo.getEventos()) {
                if (e == evento) {
                    torneoEvento = torneo;
                }
            }
        }

        if (torneoEvento == null) {
            return;
        }

        Disciplina disciplina = torneoEvento.getDisciplina();
        //Cada disciplina pide los datos que necesita
        Resultado resultado = disciplina.leerResultado(scanner, evento);

        if (resultado == null) {
            System.out.println(disciplina.getMensaje());
            return;
        }

        if (sistema.registrarResultado(evento, resultado)) {
            System.out.println(sistema.getMensaje());
        } else {
            System.out.println(sistema.getMensaje());
        }
    }

    //Muestra los torneos registrados
    private static void consultarTorneos() {

        ArrayList<Torneo> torneos = sistema.getTorneos();

        if (torneos.isEmpty()) {
            System.out.println("No hay torneos.");
        }

        for (Torneo torneo : torneos) {
            System.out.println(
                torneo.getCodigo() + " - " +
                torneo.getNombre() + " - " +
                torneo.getDisciplina().getNombre() + " - " +
                torneo.getEstado()
            );
        }
    }

    //Muestra los participantes registrados
    private static void consultarParticipantes() {

        for (Participante p : sistema.getParticipantes()) {
            System.out.println(
                p.getCodigo() + " - " +
                p.getNombre() + " - " +
                p.getEmpresa()
            );
        }
    }

    //Imprime los eventos de un calendario
    private static void mostrarCalendario(Calendario calendario) {

        for (Evento evento : calendario) {
            System.out.println(
                evento.getCodigo() + " - " +
                evento.getFecha() + " - " +
                evento.getHora() + ":00 - " +
                evento.getSede() + " - " +
                evento.getEstado()
            );
        }
    }

    //Muestra el calendario de un torneo
    private static void calendarioTorneo() {

        Torneo torneo = sistema.buscarTorneo(
            leerTexto("Codigo del torneo: ")
        );

        if (torneo != null) {
            mostrarCalendario(torneo.calendario());
        } else {
            System.out.println("Torneo no encontrado.");
        }
    }

    //Muestra el calendario de un participante
    private static void calendarioParticipante() {

        Participante p = sistema.buscarParticipante(
            leerTexto("Codigo del participante: ")
        );

        if (p != null) {
            mostrarCalendario(sistema.calendarioParticipante(p));
        } else {
            System.out.println("Participante no encontrado.");
        }
    }

    //Muestra la tabla ordenada de un torneo
    private static void tablaPosiciones() {

        Torneo torneo = sistema.buscarTorneo(
            leerTexto("Codigo del torneo: ")
        );

        if (torneo != null) {
            for (Posicion posicion : torneo.tablaPosiciones()) {
                System.out.println(
                    posicion.getParticipante().getNombre() +
                    " | Puntos: " + posicion.getPuntos() +
                    " | Diferencia: " + posicion.getDiferencia() +
                    " | Victorias: " + posicion.getVictorias()
                );
            }
        } else {
            System.out.println("Torneo no encontrado.");
        }
    }

    //Muestra el campeon si el torneo ya termino
    private static void mostrarCampeon() {

        Torneo torneo = sistema.buscarTorneo(
            leerTexto("Codigo del torneo: ")
        );

        if (torneo != null) {

            Participante campeon = torneo.getCampeon();

            if (campeon != null) {
                System.out.println("Campeon: " + campeon.getNombre());
                System.out.printf(
                    "Premio: Q%.2f%n", torneo.getPremio()
                );
            } else {
                System.out.println("El torneo no tiene campeon.");
            }

        } else {
            System.out.println("Torneo no encontrado.");
        }
    }

    //Muestra los ingresos y los premios
    private static void reporteFinanciero() {

        System.out.printf(
            "Ingresos totales: Q%.2f%n",
            sistema.ingresosTotales()
        );

        System.out.printf(
            "Premios entregados: Q%.2f%n",
            sistema.getPremiosEntregados()
        );

        for (Disciplina d : sistema.getDisciplinas()) {
            System.out.printf(
                "%s: Q%.2f%n",
                d.getNombre(),
                sistema.ingresosDisciplina(d)
            );
        }
    }

    //Muestra las empresas ordenadas por puntos
    private static void rankingEmpresas() {

        ArrayList<PosicionEmpresa> ranking =
            sistema.rankingEmpresas();

        for (PosicionEmpresa posicion : ranking) {
            System.out.println(
                posicion.getEmpresa() + " - " +
                posicion.getPuntos() + " puntos"
            );
        }
    }

    //Carga los datos con los que empieza el programa
    private static void cargarDatosIniciales() {
        sistema.cargarDatosIniciales();
    }
}
