import java.util.ArrayList;
import java.util.Scanner;

public class FutbolSalaPenales extends FutbolSala {

    //Constructor
    //Crea futbol sala con la cuota de penales
    public FutbolSalaPenales() {
        super(750);
    }

    //Sobrescritura
    @Override
    //Valida goles y penales segun el resultado
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

        if (!resultado.estaCompleto()) {
            this.mensaje = "El resultado esta incompleto.";
            return false;
        }

        resultadoMarcador marcador = (resultadoMarcador) resultado;

        int golesLocal = marcador.getMarcadorLocal();
        int golesVisita = marcador.getMarcadorVisita();

        //Validar goles
        if (golesLocal < 0 || golesVisita < 0) {
            this.mensaje = "Los goles no pueden ser negativos.";
            return false;
        }

        //Validar penales
        if (golesLocal != golesVisita) {
            if (resultado instanceof ResultadoPenales) {
                this.mensaje = "No se registran penales si hay ganador.";
                return false;
            }

            this.mensaje = "Resultado valido.";
            return true;
        }

        if (!(resultado instanceof ResultadoPenales)) {
            this.mensaje = "El empate debe definirse por penales.";
            return false;
        }

        ResultadoPenales penales = (ResultadoPenales) resultado;

        int penalesLocal = penales.getPenalesLocal();
        int penalesVisita = penales.getPenalesVisita();

        if (penalesLocal < 0 || penalesVisita < 0) {
            this.mensaje = "Los penales no pueden ser negativos.";
            return false;
        }

        if (penalesLocal == penalesVisita) {
            this.mensaje = "Los penales no pueden quedar empatados.";
            return false;
        }

        this.mensaje = "Resultado valido.";
        return true;
    }

    @Override
    //Calcula puntos normales o puntos por penales
    public ArrayList<Puntuacion> calcularPuntuaciones(
            Evento evento, Resultado resultado) {

        if (!validarResultado(evento, resultado)) {
            throw new IllegalArgumentException(
                "Resultado invalido para futbol sala con penales.");
        }

        Encuentro encuentro = (Encuentro) evento;
        resultadoMarcador marcador = (resultadoMarcador) resultado;

        int golesLocal = marcador.getMarcadorLocal();
        int golesVisita = marcador.getMarcadorVisita();

        int puntosLocal = 0;
        int puntosVisita = 0;

        boolean victoriaLocal = false;
        boolean victoriaVisita = false;

        //Ganador normal
        if (golesLocal > golesVisita) {
            puntosLocal = 3;
            victoriaLocal = true;

        } else if (golesVisita > golesLocal) {
            puntosVisita = 3;
            victoriaVisita = true;

        } else {

            //Ganador por penales
            ResultadoPenales penales = (ResultadoPenales) resultado;

            int penalesLocal = penales.getPenalesLocal();
            int penalesVisita = penales.getPenalesVisita();

            if (penalesLocal > penalesVisita) {
                puntosLocal = 2;
                puntosVisita = 1;
                victoriaLocal = true;

            } else {
                puntosLocal = 1;
                puntosVisita = 2;
                victoriaVisita = true;
            }
        }

        //Diferencia de goles
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
    //Pide penales solamente cuando los goles empatan
    public Resultado leerResultado(Scanner scanner, Evento evento) {
        int local = leerEntero(scanner, "Marcador local: ");
        int visita = leerEntero(scanner, "Marcador visitante: ");

        try {
            if (local == visita) {
                int penalesLocal = leerEntero(scanner, "Penales local: ");
                int penalesVisita = leerEntero(scanner, "Penales visita: ");

                return new ResultadoPenales(
                    local, visita, penalesLocal, penalesVisita);
            }

            return new resultadoMarcador(local, visita);
        } catch (IllegalArgumentException e) {
            this.mensaje = e.getMessage();
            return null;
        }
    }
}
