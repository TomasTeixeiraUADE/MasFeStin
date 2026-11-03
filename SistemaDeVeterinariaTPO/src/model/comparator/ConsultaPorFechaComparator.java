package model.comparator;

import java.util.Comparator;

import model.Consulta;

public class ConsultaPorFechaComparator
        implements Comparator<Consulta> {

    @Override
    public int compare(Consulta consulta1, Consulta consulta2) {

        return consulta1.getFecha().compareTo(
            consulta2.getFecha()
        );
    }
}
