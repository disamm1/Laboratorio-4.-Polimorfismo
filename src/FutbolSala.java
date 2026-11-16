import java.util.ArrayList;
import java.util.Scanner;

public class FutbolSala extends Disciplina {

    //Constructores
    //Crea futbol sala con su cuota normal
    public FutbolSala() {
        super("Futbol Sala", 4, 8, 600);
    }

    //Permite cambiar la cuota para la version con penales
    protected FutbolSala(double cuota) {
        super("Futbol Sala con Penales", 4, 8, cuota);
    }

    //Sobrescritura
    @Override 
    //Crea un encuentro entre cada pareja
    public ArrayList<Evento> crearEventos(ArrayList<Participante> participantes) {
        return super.crearEncuentros(participantes);
    }

    @Override
    //Revisa que el marcador sea valido para futbol sala
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
            this.mensaje = "Futbol sala no acepta penales.";
            return false;
        }

        if (!resultado.estaCompleto()) {
            this.mensaje = "El resultado esta incompleto.";
            return false;
        }

        resultadoMarcador marcador = (resultadoMarcador) resultado;

        if (marcador.getMarcadorLocal() < 0 ||
            marcador.getMarcadorVisita() < 0) {
            this.mensaje = "Los goles no pueden ser negativos.";
            return false;
        }

        this.mensaje = "Resultado valido.";
        return true;
    }

    @Override
    //Calcula puntos, diferencia y victoria
    public ArrayList<Puntuacion> calcularPuntuaciones(
            Evento evento, Resultado resultado) {

        if (!validarResultado(evento, resultado)) {
            throw new IllegalArgumentException(
                "Resultado invalido para futbol sala.");
        }

        Encuentro encuentro = (Encuentro) evento;
        resultadoMarcador marcador = (resultadoMarcador) resultado;

        int golesLocal = marcador.getMarcadorLocal();
        int golesVisita = marcador.getMarcadorVisita();

        int puntosLocal = 0;
        int puntosVisita = 0;

        boolean victoriaLocal = false;
        boolean victoriaVisita = false;

        //Decidir quien gano o si fue empate
        if (golesLocal > golesVisita) {
            puntosLocal = 3;
            victoriaLocal = true;

        } else if (golesVisita > golesLocal) {
            puntosVisita = 3;
            victoriaVisita = true;

        } else {
            puntosLocal = 1;
            puntosVisita = 1;
        }

        int diferenciaLocal = golesLocal - golesVisita;
        int diferenciaVisita = golesVisita - golesLocal;

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
    //Pide los goles de los dos equipos
    public Resultado leerResultado(Scanner scanner, Evento evento) {
        return leerMarcador(scanner);
    }
}
