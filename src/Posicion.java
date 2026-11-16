public class Posicion implements Comparable<Posicion> {

    //Atributos
    private Participante participante;
    private int puntos;
    private int diferencia;
    private int victorias;

    //Constructor
    //Crea una posicion con valores en cero
    public Posicion(Participante participante) {
        if (participante == null) {
        throw new IllegalArgumentException(
            "El participante no puede ser nulo.");
    }
        this.participante = participante;
        this.puntos = 0;
        this.diferencia = 0;
        this.victorias = 0;
    }

    //Getters
    //Devuelve el participante de la posicion
    public Participante getParticipante() {
        return participante;
    }

    //Devuelve los puntos acumulados
    public int getPuntos() {
        return puntos;
    }

    //Devuelve la diferencia acumulada
    public int getDiferencia() {
        return diferencia;
    }

    //Devuelve las victorias acumuladas
    public int getVictorias() {
        return victorias;
    }

    //Metodos
    //Suma puntos a la posicion
    public void sumarPuntos(int puntos) {
        if (puntos < 0) {
            throw new IllegalArgumentException(
                "Los puntos a sumar no pueden ser negativos.");
        }
        this.puntos += puntos;
        
    }

    //Suma la diferencia del resultado
    public void sumarDiferencia(int diferencia) {
        this.diferencia += diferencia;
        
    }

    //Suma una victoria
    public void sumarVictoria() {
        this.victorias++;
        
    }

    //Sobrescritura
    @Override
    //Ordena por puntos, diferencia, victorias y nombre
    public int compareTo(Posicion otraPosicion) {

    //Puntos
    if (this.puntos != otraPosicion.puntos) {
        return Integer.compare(otraPosicion.puntos, this.puntos);
    }

    //Diferencia
    if (this.diferencia != otraPosicion.diferencia) {
        return Integer.compare(otraPosicion.diferencia, this.diferencia);
    }

    //Victorias
    if (this.victorias != otraPosicion.victorias) {
        return Integer.compare(otraPosicion.victorias, this.victorias);
    }

    //Nombre
    return this.participante.getNombre().compareTo(
        otraPosicion.participante.getNombre()
    );
}

}
