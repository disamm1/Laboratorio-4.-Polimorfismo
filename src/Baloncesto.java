
import java.util.ArrayList;
import java.util.Scanner;

public class Baloncesto extends Disciplina {

    //Constructor
    //Crea baloncesto con sus reglas generales
    public Baloncesto() {
        super("Baloncesto", 4, 6, 500);
    }

    //Sobrescritura
    @Override
    //Crea un encuentro entre cada pareja
    public ArrayList<Evento> crearEventos(ArrayList<Participante> participantes) {
        return super.crearEncuentros(participantes);
    }

    @Override
    //Valida el marcador y rechaza empates
    public boolean validarResultado(Evento evento, Resultado resultado) {

        if (evento == null || resultado == null) {
            this.mensaje = "El evento y el resultado son obligatorios.";
            return false;
        }

        if (!(evento instanceof Encuentro)) {
            this.mensaje = "El evento debe ser un encuentro.";
            return false;
        }

        if (!(resultado instanceof resultadoMarcador)) {
            this.mensaje = "El resultado debe tener marcador.";
            return false;
        }

        if (resultado instanceof ResultadoPenales) {
            this.mensaje = "Baloncesto no acepta penales.";
            return false;
        }

        if (!resultado.estaCompleto()) {
            this.mensaje = "El resultado esta incompleto.";
            return false;
        }

        resultadoMarcador marcador = (resultadoMarcador) resultado;

        int marcadorLocal = marcador.getMarcadorLocal();
        int marcadorVisita = marcador.getMarcadorVisita();

        if (marcadorLocal < 0 || marcadorVisita < 0) {
            this.mensaje = "Los puntos no pueden ser negativos.";
            return false;
        }

        if (marcadorLocal == marcadorVisita) {
            this.mensaje = "Baloncesto no acepta empates.";
            return false;
        }

        this.mensaje = "Resultado valido.";
        return true;
    }

    @Override
    //Da dos puntos al ganador y uno al perdedor
    public ArrayList<Puntuacion> calcularPuntuaciones(Evento evento, Resultado resultado) {

        if (!validarResultado(evento, resultado)) {
            throw new IllegalArgumentException(
                "Resultado invalido para baloncesto.");
        }

        Encuentro encuentro = (Encuentro) evento;
        resultadoMarcador marcador = (resultadoMarcador) resultado;

        int marcadorLocal = marcador.getMarcadorLocal();
        int marcadorVisita = marcador.getMarcadorVisita();

        int puntosLocal;
        int puntosVisita;

        boolean victoriaLocal;
        boolean victoriaVisita;

        if (marcadorLocal > marcadorVisita) {
            puntosLocal = 2;
            puntosVisita = 1;

            victoriaLocal = true;
            victoriaVisita = false;

        } else {
            puntosLocal = 1;
            puntosVisita = 2;

            victoriaLocal = false;
            victoriaVisita = true;
        }

        int diferenciaLocal = marcadorLocal - marcadorVisita;
        int diferenciaVisita = marcadorVisita - marcadorLocal;

        Puntuacion puntuacionLocal = new Puntuacion(
            encuentro.getLocal(),
            puntosLocal,
            diferenciaLocal,
            victoriaLocal
        );

        Puntuacion puntuacionVisita = new Puntuacion(
            encuentro.getVisitante(),
            puntosVisita,
            diferenciaVisita,
            victoriaVisita
        );

        ArrayList<Puntuacion> puntuaciones = new ArrayList<>();

        puntuaciones.add(puntuacionLocal);
        puntuaciones.add(puntuacionVisita);

        return puntuaciones;
    }

    @Override
    //Pide los puntos anotados por cada equipo
    public Resultado leerResultado(Scanner scanner, Evento evento) {
        return leerMarcador(scanner);
    }
}
