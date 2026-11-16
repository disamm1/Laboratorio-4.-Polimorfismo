public class Participante {

    //Atributos
    private String codigo;
    private String nombre;
    private String empresa;

    //Constructor
    //Crea un participante con sus datos basicos
    public Participante(String codigo, String nombre, String empresa) {
        if (codigo == null || codigo.trim().isEmpty()) {
            throw new IllegalArgumentException("Codigo invalido.");
        }

        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("Nombre invalido.");
        }

        if (empresa == null || empresa.trim().isEmpty()) {
            throw new IllegalArgumentException("Empresa invalida.");
        }
        
        this.codigo = codigo;
        this.nombre = nombre;
        this.empresa = empresa;
    }

    //Getters
    //Devuelve el codigo del participante
    public String getCodigo() {
        return codigo;
    }

    //Devuelve el nombre del participante
    public String getNombre() {
        return nombre;
    }

    //Devuelve la empresa que representa
    public String getEmpresa() {
        return empresa;
    }
}
