public class Puntuacion {

    //Atributos
    private Participante participante;
    private int puntos;
    private int diferencia;
    private boolean victoria;

    //Constructor
    //Guarda la puntuacion obtenida en un evento
    public Puntuacion(
            Participante participante, int puntos,
            int diferencia, boolean victoria) {

        if (participante == null) {
            throw new IllegalArgumentException("El participante no puede ser nulo.");
        }

        if (puntos < 0) {
            throw new IllegalArgumentException("Los puntos no pueden ser negativos.");
        }

        this.participante = participante;
        this.puntos = puntos;
        this.diferencia = diferencia;
        this.victoria = victoria;
    }

    //Getters
    //Devuelve el participante de la puntuacion
    public Participante getParticipante() {
        return this.participante;
    }

    //Devuelve los puntos de tabla
    public int getPuntos() {
        return this.puntos;
    }

    //Devuelve la diferencia obtenida
    public int getDiferencia() {
        return this.diferencia;
    }

    //Indica si el participante gano
    public boolean esVictoria() {
        return this.victoria;
    }
}
