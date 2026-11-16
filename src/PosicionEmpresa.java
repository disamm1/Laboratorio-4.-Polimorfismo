public class PosicionEmpresa implements Comparable<PosicionEmpresa> {

    //Atributos
    private String empresa;
    private int puntos;

    //Constructor
    //Crea una posicion para una empresa
    public PosicionEmpresa(String empresa) {
        if (empresa == null || empresa.trim().isEmpty()) {
            throw new IllegalArgumentException(
                "El nombre de la empresa no puede ser nulo o vacío.");
        }

        this.empresa = empresa;
        this.puntos = 0;
    }

    //Getters
    //Devuelve el nombre de la empresa
    public String getEmpresa() {
        return empresa;
    }

    //Devuelve los puntos de la empresa
    public int getPuntos() {
        return puntos;
    }

    //Metodos
    //Suma puntos al total de la empresa
    public void sumarPuntos(int puntos) {
        if (puntos < 0) {
            throw new IllegalArgumentException(
                "Los puntos a sumar no pueden ser negativos.");
        }

        this.puntos += puntos;
    }

    //Sobrescritura
    @Override
    //Ordena por puntos y despues por empresa
    public int compareTo(PosicionEmpresa otraPosicion) {
        if (this.puntos != otraPosicion.getPuntos()) {
            return Integer.compare(otraPosicion.getPuntos(), this.puntos);
        }

        return this.empresa.compareTo(otraPosicion.getEmpresa());
    }
}
