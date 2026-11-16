import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator; 

public class Calendario implements Iterable<Evento> {
    //Atributos
    private ArrayList<Evento> eventos;

    //Constructor
    //Guarda solamente eventos programados y los ordena
    public Calendario(ArrayList<Evento> eventos) {
        if (eventos == null) {
            throw new IllegalArgumentException(
                "La lista de eventos no puede ser nula.");
        }

        this.eventos = new ArrayList<>();

          for (Evento evento : eventos) {

            if (evento == null) {
                throw new IllegalArgumentException(
                    "La lista no puede contener eventos nulos.");
            }
             if (evento.getFecha() != null &&
                evento.getSede() != null) {

                this.eventos.add(evento);
            }
        }

        //Ordenar eventos
        Collections.sort(this.eventos);
    }

    //Getters
    //Devuelve una copia de los eventos ordenados
    public ArrayList<Evento> getEventosOrdenados() {
        return new ArrayList<>(eventos);
    }

    //Sobrescritura
    @Override
    //Permite recorrer el calendario con for-each
    public Iterator<Evento> iterator() {
        return new ArrayList<>(eventos).iterator();
    }
}

