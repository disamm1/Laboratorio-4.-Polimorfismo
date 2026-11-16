import java.util.ArrayList;

public class Carrera extends Evento {

    //Atributos
    private ArrayList<Participante> participantes;

    //Constructor
    //Crea una carrera con todos sus participantes
    public Carrera(String codigo, ArrayList<Participante> participantes) {
    super(codigo);

    if (participantes == null || participantes.isEmpty()) {
        throw new IllegalArgumentException(
            "La lista de participantes no puede ser nula o vacia.");
    }

    //Validar participantes
    for (Participante p : participantes) {
        if (p == null) {
            throw new IllegalArgumentException(
                "La lista no puede contener participantes nulos.");
        }
    }

    //Validar repetidos
    for (int i = 0; i < participantes.size(); i++) {
        for (int j = i + 1; j < participantes.size(); j++) {
            if (participantes.get(i).getCodigo()
                    .equals(participantes.get(j).getCodigo())) {
                throw new IllegalArgumentException(
                    "No puede haber participantes repetidos.");
            }
        }
    }

    this.participantes = new ArrayList<>(participantes);
}

    //Sobrescritura
    @Override
    //Devuelve una copia de los participantes
    public ArrayList<Participante> getParticipantes() {
        return new ArrayList<>(this.participantes);
    }
}
