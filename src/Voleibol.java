import java.util.ArrayList;
import java.util.Scanner;

public class Voleibol extends Disciplina  {


    //Constructor
    //Crea voleibol con sus reglas generales
     public Voleibol() {
        super("Voleibol", 4, 6 , 450);
    }

    //Sobrescritura
    @Override 
    //Crea un encuentro entre cada pareja
    public ArrayList<Evento> crearEventos(ArrayList<Participante> participantes) {
        return super.crearEncuentros(participantes);
    }

    @Override
//Valida que el ganador tenga tres sets
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
        this.mensaje = "Voleibol no acepta penales.";
        return false;
    }

    if (!resultado.estaCompleto()) {
        this.mensaje = "El resultado esta incompleto.";
        return false;
    }

    resultadoMarcador marcador = (resultadoMarcador) resultado;

    int setsLocal = marcador.getMarcadorLocal();
    int setsVisita = marcador.getMarcadorVisita();

    //Validar sets
    if (setsLocal < 0 || setsVisita < 0) {
        this.mensaje = "Los sets no pueden ser negativos.";
        return false;
    }

    //Validar ganador
    if (setsLocal == 3 && setsVisita <= 2) {
        this.mensaje = "Resultado valido.";
        return true;
    }

    if (setsVisita == 3 && setsLocal <= 2) {
        this.mensaje = "Resultado valido.";
        return true;
    }

    this.mensaje = "El ganador debe tener 3 sets.";
    return false;
}

@Override
//Calcula puntos segun si el juego termino 3-2 o no
public ArrayList<Puntuacion> calcularPuntuaciones(Evento evento, Resultado resultado) {

    if (!validarResultado(evento, resultado)) {
        throw new IllegalArgumentException(
            "Resultado invalido para voleibol.");
    }

    Encuentro encuentro = (Encuentro) evento;
    resultadoMarcador marcador = (resultadoMarcador) resultado;

    int setsLocal = marcador.getMarcadorLocal();
    int setsVisita = marcador.getMarcadorVisita();

    int puntosLocal = 0;
    int puntosVisita = 0;

    boolean victoriaLocal = false;
    boolean victoriaVisita = false;

    //Ganador local
    if (setsLocal == 3) {
        victoriaLocal = true;

        if (setsVisita == 2) {
            puntosLocal = 2;
            puntosVisita = 1;
        } else {
            puntosLocal = 3;
            puntosVisita = 0;
        }

    //Ganador visitante
    } else {
        victoriaVisita = true;

        if (setsLocal == 2) {
            puntosLocal = 1;
            puntosVisita = 2;
        } else {
            puntosLocal = 0;
            puntosVisita = 3;
        }
    }

    //Diferencia de sets
    int diferenciaLocal = setsLocal - setsVisita;
    int diferenciaVisita = setsVisita - setsLocal;

    Puntuacion puntuacionLocal = new Puntuacion(encuentro.getLocal(),puntosLocal,diferenciaLocal,victoriaLocal
    );

    Puntuacion puntuacionVisita = new Puntuacion(encuentro.getVisitante(),puntosVisita,diferenciaVisita,victoriaVisita
    );

    ArrayList<Puntuacion> puntuaciones = new ArrayList<>();

    puntuaciones.add(puntuacionLocal);
    puntuaciones.add(puntuacionVisita);

    return puntuaciones;
}

@Override
//Pide los sets ganados por cada equipo
public Resultado leerResultado(Scanner scanner, Evento evento) {
    return leerMarcador(scanner);
}


}
