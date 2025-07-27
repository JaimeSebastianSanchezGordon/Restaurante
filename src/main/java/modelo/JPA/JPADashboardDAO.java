package modelo.JPA;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import modelo.dao.DashboardDAO;
import util.JPAUtil;

public class JPADashboardDAO implements DashboardDAO {

    @Override
    public Double obtenerIngresoTotal(LocalDate fechaInicio, LocalDate fechaFin) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            // Convertir LocalDate a LocalDateTime para las comparaciones
            java.time.LocalDateTime inicioDateTime = fechaInicio.atStartOfDay();
            java.time.LocalDateTime finDateTime = fechaFin.atTime(23, 59, 59);
            
            Query query = em.createQuery(
                "SELECT COALESCE(SUM(p.cantidadPagar), 0.0) " +
                "FROM Pedido p " +
                "WHERE p.estado = 'Pagado' " +
                "AND p.fechaCreacion BETWEEN :fechaInicio AND :fechaFin"
            );
            query.setParameter("fechaInicio", inicioDateTime);
            query.setParameter("fechaFin", finDateTime);
            
            Object resultado = query.getSingleResult();
            
            // Manejar tanto Double como BigDecimal
            if (resultado instanceof BigDecimal) {
                return ((BigDecimal) resultado).doubleValue();
            } else if (resultado instanceof Double) {
                return (Double) resultado;
            } else {
                return 0.0;
            }
            
        } finally {
            em.close();
        }
    }

    @Override
    public Integer obtenerTotalPedidos(LocalDate fechaInicio, LocalDate fechaFin) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            // Convertir LocalDate a LocalDateTime para las comparaciones
            java.time.LocalDateTime inicioDateTime = fechaInicio.atStartOfDay();
            java.time.LocalDateTime finDateTime = fechaFin.atTime(23, 59, 59);
            
            Query query = em.createQuery(
                "SELECT COUNT(p) " +
                "FROM Pedido p " +
                "WHERE p.fechaCreacion BETWEEN :fechaInicio AND :fechaFin"
            );
            query.setParameter("fechaInicio", inicioDateTime);
            query.setParameter("fechaFin", finDateTime);
            
            Long resultado = (Long) query.getSingleResult();
            return resultado != null ? resultado.intValue() : 0;
        } finally {
            em.close();
        }
    }

    @Override
    public Double obtenerPorcentajeCrecimiento(LocalDate fechaInicio, LocalDate fechaFin) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            // Calcular el período anterior con la misma duración
            long diasPeriodo = fechaFin.toEpochDay() - fechaInicio.toEpochDay() + 1;
            LocalDate inicioAnterior = fechaInicio.minusDays(diasPeriodo);
            LocalDate finAnterior = fechaInicio.minusDays(1);
            
            // Ingreso del período actual
            Double ingresoActual = obtenerIngresoTotal(fechaInicio, fechaFin);
            
            // Convertir fechas anteriores a LocalDateTime
            java.time.LocalDateTime inicioAnteriorDateTime = inicioAnterior.atStartOfDay();
            java.time.LocalDateTime finAnteriorDateTime = finAnterior.atTime(23, 59, 59);
            
            // Ingreso del período anterior
            Query queryAnterior = em.createQuery(
                "SELECT COALESCE(SUM(p.cantidadPagar), 0.0) " +
                "FROM Pedido p " +
                "WHERE p.estado = 'Pagado' " +
                "AND p.fechaCreacion BETWEEN :fechaInicio AND :fechaFin"
            );
            queryAnterior.setParameter("fechaInicio", inicioAnteriorDateTime);
            queryAnterior.setParameter("fechaFin", finAnteriorDateTime);
            
            Object resultadoAnterior = queryAnterior.getSingleResult();
            Double ingresoAnterior = 0.0;
            
            // Manejar tanto Double como BigDecimal
            if (resultadoAnterior instanceof BigDecimal) {
                ingresoAnterior = ((BigDecimal) resultadoAnterior).doubleValue();
            } else if (resultadoAnterior instanceof Double) {
                ingresoAnterior = (Double) resultadoAnterior;
            }
            
            // Calcular porcentaje de crecimiento
            if (ingresoAnterior > 0) {
                return ((ingresoActual - ingresoAnterior) / ingresoAnterior) * 100;
            } else {
                return ingresoActual > 0 ? 100.0 : 0.0;
            }
            
        } finally {
            em.close();
        }
    }

    @Override
    public List<Object[]> obtenerVentasDiarias(LocalDate fechaInicio, LocalDate fechaFin) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            // Convertir LocalDate a LocalDateTime para las comparaciones
            java.time.LocalDateTime inicioDateTime = fechaInicio.atStartOfDay();
            java.time.LocalDateTime finDateTime = fechaFin.atTime(23, 59, 59);
            
            TypedQuery<Object[]> query = em.createQuery(
                "SELECT FUNCTION('DATE', p.fechaCreacion), COALESCE(SUM(p.cantidadPagar), 0.0) " +
                "FROM Pedido p " +
                "WHERE p.fechaCreacion BETWEEN :fechaInicio AND :fechaFin " +
                "AND p.estado = 'Pagado' " +
                "GROUP BY FUNCTION('DATE', p.fechaCreacion) " +
                "ORDER BY FUNCTION('DATE', p.fechaCreacion)",
                Object[].class
            );
            query.setParameter("fechaInicio", inicioDateTime);
            query.setParameter("fechaFin", finDateTime);
            
            List<Object[]> resultados = query.getResultList();
            
            // Convertir BigDecimal a Double en los resultados
            for (Object[] fila : resultados) {
                if (fila[1] instanceof BigDecimal) {
                    fila[1] = ((BigDecimal) fila[1]).doubleValue();
                }
            }
            
            return resultados;
            
        } finally {
            em.close();
        }
    }

    @Override
    public List<Object[]> obtenerPlatosPopulares(LocalDate fechaInicio, LocalDate fechaFin) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            // Convertir LocalDate a LocalDateTime para las comparaciones
            java.time.LocalDateTime inicioDateTime = fechaInicio.atStartOfDay();
            java.time.LocalDateTime finDateTime = fechaFin.atTime(23, 59, 59);
            
            TypedQuery<Object[]> query = em.createQuery(
                "SELECT pl.nombrePlato, pl.precio, pl.imagenUrl, " +
                "SUM(dp.cantidad), COUNT(DISTINCT dp.pedido.idPedido) " +
                "FROM DetallePedido dp " +
                "JOIN dp.plato pl " +
                "JOIN dp.pedido p " +
                "WHERE p.fechaCreacion BETWEEN :fechaInicio AND :fechaFin " +
                "AND p.estado = 'Pagado' " +
                "GROUP BY pl.id, pl.nombrePlato, pl.precio, pl.imagenUrl " +
                "ORDER BY SUM(dp.cantidad) DESC",
                Object[].class
            );
            query.setParameter("fechaInicio", inicioDateTime);
            query.setParameter("fechaFin", finDateTime);
            query.setMaxResults(10); // Top 10 platos
            
            List<Object[]> resultados = query.getResultList();
            
            // Convertir BigDecimal/BigInteger a tipos esperados
            for (Object[] fila : resultados) {
                // fila[1] = precio (puede ser BigDecimal)
                if (fila[1] instanceof BigDecimal) {
                    fila[1] = ((BigDecimal) fila[1]).doubleValue();
                }
                // fila[3] = cantidad (puede ser BigDecimal)
                if (fila[3] instanceof BigDecimal) {
                    fila[3] = ((BigDecimal) fila[3]).longValue();
                }
            }
            
            return resultados;
            
        } finally {
            em.close();
        }
    }

    @Override
    public Map<String, Double> obtenerDistribucionCategorias(LocalDate fechaInicio, LocalDate fechaFin) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            Map<String, Double> distribucion = new HashMap<>();

            // Convertir LocalDate a LocalDateTime para las comparaciones
            java.time.LocalDateTime inicioDateTime = fechaInicio.atStartOfDay();
            java.time.LocalDateTime finDateTime = fechaFin.atTime(23, 59, 59);

            // Comida (platos > $20)
            Query queryComida = em.createQuery(
                "SELECT COALESCE(SUM(dp.precio), 0.0) " +
                "FROM DetallePedido dp " +
                "JOIN dp.pedido p " +
                "WHERE p.fechaCreacion BETWEEN :fechaInicio AND :fechaFin " +
                "AND p.estado = 'Pagado' " +
                "AND dp.plato.precio > 20"
            );
            queryComida.setParameter("fechaInicio", inicioDateTime);
            queryComida.setParameter("fechaFin", finDateTime);
            Object resultadoComida = queryComida.getSingleResult();
            Double totalComida = convertirADouble(resultadoComida);

            // Bebida Fría (platos <= $20 y > $10)
            Query queryBebida = em.createQuery(
                "SELECT COALESCE(SUM(dp.precio), 0.0) " +
                "FROM DetallePedido dp " +
                "JOIN dp.pedido p " +
                "WHERE p.fechaCreacion BETWEEN :fechaInicio AND :fechaFin " +
                "AND p.estado = 'Pagado' " +
                "AND dp.plato.precio <= 20 AND dp.plato.precio > 10"
            );
            queryBebida.setParameter("fechaInicio", inicioDateTime);
            queryBebida.setParameter("fechaFin", finDateTime);
            Object resultadoBebida = queryBebida.getSingleResult();
            Double totalBebida = convertirADouble(resultadoBebida);

            // Otros (platos <= $10)
            Query queryOtros = em.createQuery(
                "SELECT COALESCE(SUM(dp.precio), 0.0) " +
                "FROM DetallePedido dp " +
                "JOIN dp.pedido p " +
                "WHERE p.fechaCreacion BETWEEN :fechaInicio AND :fechaFin " +
                "AND p.estado = 'Pagado' " +
                "AND dp.plato.precio <= 10"
            );
            queryOtros.setParameter("fechaInicio", inicioDateTime);
            queryOtros.setParameter("fechaFin", finDateTime);
            Object resultadoOtros = queryOtros.getSingleResult();
            Double totalOtros = convertirADouble(resultadoOtros);

            distribucion.put("Comida", totalComida);
            distribucion.put("Bebida Fría", totalBebida);
            distribucion.put("Otros", totalOtros);

            return distribucion;

        } finally {
            em.close();
        }
    }
    
    // Método helper para convertir Object a Double
    private Double convertirADouble(Object valor) {
        if (valor == null) {
            return 0.0;
        } else if (valor instanceof BigDecimal) {
            return ((BigDecimal) valor).doubleValue();
        } else if (valor instanceof Double) {
            return (Double) valor;
        } else if (valor instanceof Number) {
            return ((Number) valor).doubleValue();
        } else {
            return 0.0;
        }
    }
}