import java.time.LocalDate;

public interface Programable {

    //Firmas
    //Asigna fecha, hora y sede
    boolean programar(LocalDate fecha, int hora, String sede);

    //Devuelve la fecha asignada
    LocalDate getFecha();

    //Devuelve la hora asignada
    int getHora();

    //Devuelve la sede asignada
    String getSede();

    //Indica si ya fue programado
    boolean estaProgramado();
}
