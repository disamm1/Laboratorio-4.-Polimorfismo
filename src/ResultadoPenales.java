public class ResultadoPenales extends resultadoMarcador {

    //Atributos
    private int penalesLocal;
    private int penalesVisita;

    //Constructor
    //Guarda los goles y los penales convertidos
    public ResultadoPenales(
            int golLocal, int golVisita,
            int penalesLocal, int penalesVisita) {

        super(golLocal, golVisita);

        if (penalesLocal < 0 || penalesVisita < 0) {
            throw new IllegalArgumentException(
                "Los penales no pueden ser negativos.");
        }

        this.penalesLocal = penalesLocal;
        this.penalesVisita = penalesVisita;
    }

    //Getters
    //Devuelve los penales del equipo local
    public int getPenalesLocal() {
        return penalesLocal;
    }

    //Devuelve los penales del equipo visitante
    public int getPenalesVisita() {
        return penalesVisita;
    }
}
