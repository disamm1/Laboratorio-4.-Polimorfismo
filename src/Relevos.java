import java.util.ArrayList;
import java.util.Scanner;

public class Relevos extends Disciplina {


    //Constructor
    //Crea relevos con sus reglas generales
    public Relevos() {
        super("Relevos", 3, 8 , 300);
    }

    //Sobrescritura
    @Override
    //Crea una sola carrera con todos los participantes
    public ArrayList<Evento> crearEventos(ArrayList<Participante> participantes) {

    //Validar lista
    if (participantes == null) {
        throw new IllegalArgumentException(
            "La lista de participantes no puede ser nula.");
    }

    //Validar cantidad
    if (participantes.size() < minimo || participantes.size() > maximo) {
        throw new IllegalArgumentException(
            "La cantidad de participantes debe estar entre 3 y 8.");
    }

    ArrayList<Evento> eventos = new ArrayList<>();

    //Crear carrera
    Carrera carrera = new Carrera("C1", participantes);

    eventos.add(carrera);

    return eventos;
}

@Override
//Valida tiempos, participantes y descalificados
public boolean validarResultado(Evento evento, Resultado resultado) {

    if (evento == null || resultado == null) {
        this.mensaje = "El evento y el resultado son obligatorios.";
        return false;
    }

    if (!(evento instanceof Carrera)) {
        this.mensaje = "El evento debe ser una carrera.";
        return false;
    }

    if (!(resultado instanceof resultadoCarrera)) {
        this.mensaje = "El resultado debe tener tiempos.";
        return false;
    }

    if (!resultado.estaCompleto()) {
        this.mensaje = "El resultado esta incompleto.";
        return false;
    }

    Carrera carrera = (Carrera) evento;
    resultadoCarrera resultadoCarrera = (resultadoCarrera) resultado;

    ArrayList<Participante> inscritos = carrera.getParticipantes();
    ArrayList<Participante> registrados = resultadoCarrera.getParticipantes();

    //Validar cantidad
    if (inscritos.size() != registrados.size()) {
        this.mensaje = "Faltan resultados de participantes.";
        return false;
    }

    //Validar tiempos
    for (Participante p : inscritos) {

        double tiempo = resultadoCarrera.getTiempo(p);

        if (tiempo == -2) {
            this.mensaje = "Falta el resultado de un participante.";
            return false;
        }

        if (tiempo != -1 && (!Double.isFinite(tiempo) || tiempo <= 0)) {
            this.mensaje = "Los tiempos deben ser positivos.";
            return false;
        }
    }

    //Validar participantes
    for (Participante registrado : registrados) {

        boolean encontrado = false;

        for (Participante inscrito : inscritos) {
            if (registrado.getCodigo().equals(inscrito.getCodigo())) {
                encontrado = true;
            }
        }

        if (!encontrado) {
            this.mensaje = "Hay un participante que no esta inscrito.";
            return false;
        }
    }

    //Validar repetidos
    ArrayList<Double> tiempos = resultadoCarrera.getTiempos();
    ArrayList<Boolean> descalificados = resultadoCarrera.getDescalificados();

    for (int i = 0; i < tiempos.size(); i++) {
        if (!descalificados.get(i)) {
            for (int j = i + 1; j < tiempos.size(); j++) {
                if (!descalificados.get(j) &&
                    tiempos.get(i).equals(tiempos.get(j))) {
                    this.mensaje = "Los tiempos no pueden repetirse.";
                    return false;
                }
            }
        }
    }

    this.mensaje = "Resultado valido.";
    return true;
}

@Override
//Da puntos segun el orden de llegada
public ArrayList<Puntuacion> calcularPuntuaciones(
        Evento evento, Resultado resultado) {

    if (!validarResultado(evento, resultado)) {
        throw new IllegalArgumentException(
            "Resultado invalido para relevos.");
    }

    resultadoCarrera carreraResultado = (resultadoCarrera) resultado;

    ArrayList<Participante> orden = carreraResultado.getOrdenLlegada();
    ArrayList<Participante> inscritos = evento.getParticipantes();
    ArrayList<Puntuacion> puntuaciones = new ArrayList<>();

    //Puntos por lugar
    int[] puntosPorLugar = {10, 8, 6, 5, 4, 3, 2, 1};

    //Tiempos validos
    for (int i = 0; i < orden.size(); i++) {

        Participante p = orden.get(i);

        int puntos = puntosPorLugar[i];
        boolean victoria = (i == 0);

        Puntuacion puntuacion = new Puntuacion(
            p,
            puntos,
            0,
            victoria
        );

        puntuaciones.add(puntuacion);
    }

    //Descalificados
    for (Participante p : inscritos) {

        if (carreraResultado.getTiempo(p) == -1) {

            Puntuacion puntuacion = new Puntuacion(
                p,
                0,
                0,
                false
            );

            puntuaciones.add(puntuacion);
        }
    }

    return puntuaciones;
}

@Override
//Pide el tiempo o descalificacion de cada participante
public Resultado leerResultado(Scanner scanner, Evento evento) {
    resultadoCarrera carrera = new resultadoCarrera();

    for (Participante participante : evento.getParticipantes()) {
        System.out.println("Participante: " + participante.getNombre());
        int opcion = leerEntero(scanner, "Descalificado? (1=Si, 0=No): ");

        if (opcion != 0 && opcion != 1) {
            this.mensaje = "La opcion de descalificacion es invalida.";
            return null;
        }

        boolean descalificado = opcion == 1;
        double tiempo = 0;

        if (!descalificado) {
            tiempo = leerDouble(scanner, "Tiempo: ");
        }

        if (!carrera.agregarParticipante(
                participante, tiempo, descalificado)) {
            this.mensaje = "El tiempo es invalido o esta repetido.";
            return null;
        }
    }

    return carrera;
}

}
