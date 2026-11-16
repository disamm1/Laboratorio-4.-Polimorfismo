import java.util.ArrayList;

public class resultadoCarrera implements Resultado {

    //Atributos
    private ArrayList<Boolean> descalificados;
    private ArrayList<Participante> participantes;
    private ArrayList<Double> tiempos;

    //Constructor
    //Prepara las listas del resultado de carrera
    public resultadoCarrera() {
        participantes = new ArrayList<>();
        tiempos = new ArrayList<>();
        descalificados = new ArrayList<>();
    }

    //Getters
    //Devuelve una copia de los participantes registrados
    public ArrayList<Participante> getParticipantes() {
        return new ArrayList<>(participantes);
    }

    //Devuelve una copia de los tiempos
    public ArrayList<Double> getTiempos() {
        return new ArrayList<>(tiempos);
    }

    //Devuelve una copia de las descalificaciones
    public ArrayList<Boolean> getDescalificados() {
        return new ArrayList<>(descalificados);
    }

    //Devuelve el tiempo o un valor especial si falta
    public double getTiempo(Participante participante) {
        if (participante == null) {
            return -2;
        }

        for (int i = 0; i < participantes.size(); i++) {
            if (participantes.get(i).getCodigo()
                    .equals(participante.getCodigo())) {

                if (descalificados.get(i)) {
                    return -1;
                }

                return tiempos.get(i);
            }
        }

        return -2;
    }

    //Devuelve a los participantes ordenados por tiempo
    public ArrayList<Participante> getOrdenLlegada() {
        ArrayList<Participante> ordenados = new ArrayList<>();

        for (int i = 0; i < participantes.size(); i++) {
            if (!descalificados.get(i)) {
                ordenados.add(participantes.get(i));
            }
        }

        for (int i = 0; i < ordenados.size() - 1; i++) {
            for (int j = i + 1; j < ordenados.size(); j++) {
                if (getTiempo(ordenados.get(i)) > getTiempo(ordenados.get(j))) {
                    Participante temporal = ordenados.get(i);
                    ordenados.set(i, ordenados.get(j));
                    ordenados.set(j, temporal);
                }
            }
        }

        return ordenados;
    }

    //Metodos
    //Revisa si un participante fue descalificado
    public boolean estaDescalificado(Participante participante) {
        if (participante == null) {
            throw new IllegalArgumentException(
                "El participante no puede ser nulo.");
        }

        for (int i = 0; i < participantes.size(); i++) {
            if (participantes.get(i).getCodigo()
                    .equals(participante.getCodigo())) {
                return descalificados.get(i);
            }
        }

        throw new IllegalArgumentException(
            "El participante no tiene resultado registrado.");
    }

    //Agrega el tiempo o la descalificacion de un participante
    public boolean agregarParticipante(
            Participante participante, double tiempo, boolean descalificado) {

        if (participante == null) {
            return false;
        }

        for (Participante registrado : participantes) {
            if (registrado.getCodigo().equals(participante.getCodigo())) {
                return false;
            }
        }

        if (!descalificado && (!Double.isFinite(tiempo) || tiempo <= 0)) {
            return false;
        }

        if (!descalificado) {
            for (int i = 0; i < tiempos.size(); i++) {
                if (!descalificados.get(i) && tiempos.get(i) == tiempo) {
                    return false;
                }
            }
        }

        participantes.add(participante);
        tiempos.add(descalificado ? 0.0 : tiempo);
        descalificados.add(descalificado);
        return true;
    }

    //Sobrescritura
    @Override
    //Revisa que las tres listas tengan datos completos
    public boolean estaCompleto() {
        return participantes.size() == tiempos.size()
            && participantes.size() == descalificados.size()
            && !participantes.isEmpty();
    }
}
