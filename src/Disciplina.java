import java.util.ArrayList;
import java.util.Scanner;

public abstract class Disciplina {

    //Atributos
    protected String nombre;
    protected int minimo;
    protected int maximo;
    protected double cuota;
    protected String mensaje;

    //Constructor
    //Guarda las reglas generales de la disciplina
    protected Disciplina(String nombre, int minimo, int maximo, double cuota) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede ser nulo o vacío.");
        }

        if (minimo <= 0 || maximo < 0 || minimo > maximo) {
            throw new IllegalArgumentException("Los valores de mínimo y máximo son inválidos.");
        }

        if (!Double.isFinite(cuota) || cuota <= 0) {
            throw new IllegalArgumentException("La cuota debe ser positiva.");
        }

        this.nombre = nombre.trim();
        this.minimo = minimo;
        this.maximo = maximo;
        this.cuota = cuota;
        this.mensaje = "";
    }

    //Getters
    //Devuelve el nombre de la disciplina
    public String getNombre() {
        return this.nombre;
    }

    //Devuelve el minimo de participantes
    public int getMinimo() {
        return this.minimo;
    }

    //Devuelve el maximo de participantes
    public int getMaximo() {
        return this.maximo;
    }

    //Devuelve la cuota de inscripcion
    public double getCuota() {
        return this.cuota;
    }

    //Devuelve el ultimo mensaje de validacion
    public String getMensaje() {
        return this.mensaje;
    }

    //Metodos
    //Crea un encuentro entre cada pareja
    protected ArrayList<Evento> crearEncuentros(ArrayList<Participante> participantes) {

        if (participantes == null) {
            throw new IllegalArgumentException(
                "La lista de participantes no puede ser nula.");
        }

        if (participantes.size() < minimo || participantes.size() > maximo) {
            throw new IllegalArgumentException("Cantidad de participantes invalida.");
        }

        ArrayList<Evento> encuentros = new ArrayList<>();

        //Validar la lista antes de crear encuentros
        for (int i = 0; i < participantes.size(); i++) {

            if (participantes.get(i) == null) {
                throw new IllegalArgumentException(
                    "No puede haber participantes nulos.");
            }

            for (int j = 0; j < i; j++) {
                if (participantes.get(i).getCodigo()
                        .equals(participantes.get(j).getCodigo())) {
                    throw new IllegalArgumentException(
                        "No puede haber participantes repetidos.");
                }
            }
        }

        //Combinar cada participante con los demas
        for (int i = 0; i < participantes.size(); i++) {
            for (int j = i + 1; j < participantes.size(); j++) {

                String codigo = "E" + (encuentros.size() + 1);

                Encuentro encuentro = new Encuentro(
                    codigo, participantes.get(i), participantes.get(j));
                encuentros.add(encuentro);
            }
        }

        return encuentros;
    }

    //Calcula la cuota por la cantidad indicada
    public double calcularCosto(int cantidad) {
        if (cantidad < minimo || cantidad > maximo) {
            throw new IllegalArgumentException(
                "La cantidad debe estar entre " + minimo + " y " + maximo + ".");
        }

        return cantidad * this.cuota;
    }

    //Lee un entero hasta que sea valido
    protected int leerEntero(Scanner scanner, String texto) {
        while (true) {
            System.out.print(texto);

            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Ingrese un numero entero valido.");
            }
        }
    }

    //Lee un decimal hasta que sea valido
    protected double leerDouble(Scanner scanner, String texto) {
        while (true) {
            System.out.print(texto);

            try {
                String entrada = scanner.nextLine().trim().replace(',', '.');
                double numero = Double.parseDouble(entrada);

                if (Double.isFinite(numero)) {
                    return numero;
                }

                System.out.println("Ingrese un numero valido.");
            } catch (NumberFormatException e) {
                System.out.println("Ingrese un numero decimal valido.");
            }
        }
    }

    //Lee un marcador normal de dos equipos
    protected Resultado leerMarcador(Scanner scanner) {
        int local = leerEntero(scanner, "Marcador local: ");
        int visita = leerEntero(scanner, "Marcador visitante: ");

        try {
            return new resultadoMarcador(local, visita);
        } catch (IllegalArgumentException e) {
            this.mensaje = e.getMessage();
            return null;
        }
    }

    //Metodos abstractos
    //Cada disciplina crea sus propios eventos
    public abstract ArrayList<Evento> crearEventos(ArrayList<Participante> participantes);

    //Cada disciplina valida sus propios resultados
    public abstract boolean validarResultado(Evento evento, Resultado resultado);

    //Cada disciplina calcula sus puntos
    public abstract ArrayList<Puntuacion> calcularPuntuaciones(Evento evento, Resultado resultado);

    //Cada disciplina pide los datos que necesita
    public abstract Resultado leerResultado(Scanner scanner, Evento evento);
}

