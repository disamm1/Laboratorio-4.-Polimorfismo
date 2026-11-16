public class resultadoMarcador implements Resultado {

    //Atributos
    private int marcadorLocal;
    private int marcadorVisita;

    //Constructor
    //Guarda el marcador de los dos equipos
    public resultadoMarcador(int marcadorLocal, int marcadorVisita) {
        if (marcadorLocal < 0 || marcadorVisita < 0) {
            throw new IllegalArgumentException(
                "Los marcadores no pueden ser negativos.");
        }

        this.marcadorLocal = marcadorLocal;
        this.marcadorVisita = marcadorVisita;
    }

    //Getters
    //Devuelve el marcador del equipo local
    public int getMarcadorLocal() {
        return marcadorLocal;
    }

    //Devuelve el marcador del equipo visitante
    public int getMarcadorVisita() {
        return marcadorVisita;
    }

    //Sobrescritura
    @Override
    //El marcador siempre queda completo al crearlo
    public boolean estaCompleto() {
        return true;
    }
}
