package modelo.dao;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface DashboardDAO {
    Double obtenerIngresoTotal(LocalDate fechaInicio, LocalDate fechaFin);
    Integer obtenerTotalPedidos(LocalDate fechaInicio, LocalDate fechaFin);
    Double obtenerPorcentajeCrecimiento(LocalDate fechaInicio, LocalDate fechaFin);
    List<Object[]> obtenerVentasDiarias(LocalDate fechaInicio, LocalDate fechaFin);
    List<Object[]> obtenerPlatosPopulares(LocalDate fechaInicio, LocalDate fechaFin);
    Map<String, Double> obtenerDistribucionCategorias(LocalDate fechaInicio, LocalDate fechaFin);
}