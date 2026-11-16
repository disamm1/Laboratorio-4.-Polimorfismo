import java.util.ArrayList;

public class Encuentro extends Evento {

    //Atributos
    private Participante equipoLocal;
    private Participante equipoVisitante;

    //Constructor
    //Crea un encuentro entre dos equipos distintos
    public Encuentro(String codigo, Participante equipoLocal,
                 Participante equipoVisitante) {

    super(codigo);

    //Validar equipos
    if (equipoLocal == null || equipoVisitante == null) {
        throw new IllegalArgumentException(
            "Los equipos no pueden ser nulos.");
    }

    //Validar repetidos
    if (equipoLocal.getCodigo().equals(equipoVisitante.getCodigo())) {
        throw new IllegalArgumentException(
            "Un equipo no puede jugar contra si mismo.");
    }

    this.equipoLocal = equipoLocal;
    this.equipoVisitante = equipoVisitante;
}

    //Getters

    //Devuelve el equipo local
    public Participante getLocal() {
        return equipoLocal;
    }

    //Devuelve el equipo visitante
    public Participante getVisitante() {
        return equipoVisitante;
    }

    //Sobrescritura
    @Override 
    //Devuelve los dos equipos del encuentro
    public ArrayList<Participante> getParticipantes() {
        ArrayList<Participante> participantes = new ArrayList<>();
        participantes.add(equipoLocal);
        participantes.add(equipoVisitante);
        return participantes;
    }
}
