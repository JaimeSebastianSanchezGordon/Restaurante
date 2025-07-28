<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<%@ taglib prefix="fn" uri="jakarta.tags.functions"%>
<!DOCTYPE html>
<html lang="es">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Panel de Control - Dashboard</title>

    <link href="https://fonts.googleapis.com/css2?family=Lexend:wght@400;500;600;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/dashboard.css">
</head>

<body>
    <div class="contenedor_panel">
        <!-- SIDEBAR MODERNA CON TOOLTIPS Y BOTÓN SALIR -->
        <aside class="barra_lateral">
            <div class="logo">
                <i class="fas fa-utensils"></i>
            </div>

            <nav class="navegacion">
                <a href="${pageContext.request.contextPath}/Dashboard" class="elemento_navegacion active">
                    <i class="fas fa-th-large"></i>
                    <span class="tooltip">Dashboard</span>
                </a>
                <a href="${pageContext.request.contextPath}/GestionarPedido" class="elemento_navegacion">
                    <i class="fas fa-file-alt"></i>
                    <span class="tooltip">Gestionar</span>
                </a>
                <a href="${pageContext.request.contextPath}/platos" class="elemento_navegacion">
                    <i class="fas fa-hamburger"></i>
                    <span class="tooltip">Platos</span>
                </a>
            </nav>
            
            <a href="jsp/login.jsp" class="elemento_navegacion logout">
    <i class="fas fa-sign-out-alt"></i>
    <span class="tooltip">Salir</span>
</a>
        </aside>

        <main class="contenido_principal">
            <header class="encabezado">
                <h1>Panel de Control</h1>
                <div class="filtros_tiempo">
                    <button class="boton_filtro ${periodoSeleccionado == 'Hoy' ? 'activo' : ''}" 
                            onclick="filtrarPeriodo('filtrarHoy')">Hoy</button>
                    <button class="boton_filtro ${periodoSeleccionado == 'Esta Semana' ? 'activo' : ''}" 
                            onclick="filtrarPeriodo('filtrarSemana')">Esta Semana</button>
                    <button class="boton_filtro ${periodoSeleccionado == 'Este Mes' ? 'activo' : ''}" 
                            onclick="filtrarPeriodo('filtrarMes')">Este Mes</button>
                    <button class="boton_filtro ${periodoSeleccionado == 'Este Año' ? 'activo' : ''}" 
                            onclick="filtrarPeriodo('filtrarAno')">Este Año</button>
                </div>
            </header>

            <!-- Mostrar errores si existen -->
            <c:if test="${not empty error}">
                <div class="alert alert-danger">
                    <i class="fas fa-exclamation-triangle"></i>
                    ${error}
                </div>
            </c:if>

            <div class="estadisticas_superiores">
                <!-- Tarjeta de Ingreso Total con Gráfico de Dona -->
                <div class="tarjeta_estadistica">
                    <h3>Distribución de Ventas</h3>
                    <div class="grafico_dona">
                        <canvas id="graficoIngreso" width="200" height="200"></canvas>
                        <div class="centro_grafico">
                            <span>
                                $<fmt:formatNumber value="${ingresoTotal != null ? ingresoTotal : 0}" 
                                   minFractionDigits="0" maxFractionDigits="0"/>
                            </span>
                        </div>
                    </div>
                    <div class="leyenda">
                        <span class="elemento_leyenda">
                            <span class="punto naranja"></span> Comida
                        </span>
                        <span class="elemento_leyenda">
                            <span class="punto negro"></span> Bebidas
                        </span>
                        <span class="elemento_leyenda">
                            <span class="punto gris"></span> Otros
                        </span>
                    </div>
                </div>

                <!-- Tarjeta de Estadísticas Generales -->
                <div class="tarjeta_estadistica">
                    <h3>Resumen ${periodoSeleccionado != null ? periodoSeleccionado : 'General'}</h3>
                    <div class="monto_saldo">
                        $<fmt:formatNumber value="${ingresoTotal != null ? ingresoTotal : 0}" 
                           minFractionDigits="0" maxFractionDigits="0"/>
                    </div>

                    <div class="elemento_saldo">
                        <div class="icono_saldo negro">
                            <i class="fas fa-chart-line"></i>
                        </div>
                        <div class="info_saldo">
                            <p>Ingreso Total</p>
                            <p class="monto">
                                $<fmt:formatNumber value="${ingresoTotal != null ? ingresoTotal : 0}" 
                                   minFractionDigits="2" maxFractionDigits="2"/>
                            </p>
                        </div>
                        <span class="porcentaje ${porcentajeCrecimiento >= 0 ? 'positivo' : 'negativo'}">
                            <c:choose>
                                <c:when test="${porcentajeCrecimiento != null}">
                                    (<fmt:formatNumber value="${porcentajeCrecimiento}" 
                                       minFractionDigits="1" maxFractionDigits="1"/>% 
                                    ${porcentajeCrecimiento >= 0 ? 'Aumento' : 'Descenso'})
                                </c:when>
                                <c:otherwise>
                                    (Sin datos previos)
                                </c:otherwise>
                            </c:choose>
                        </span>
                    </div>

                    <div class="elemento_saldo">
                        <div class="icono_saldo naranja">
                            <i class="fas fa-chart-pie"></i>
                        </div>
                        <div class="info_saldo">
                            <p>Pedidos Totales</p>
                            <p class="monto">${totalPedidos != null ? totalPedidos : 0}</p>
                        </div>
                        <span class="porcentaje">
                            <c:choose>
                                <c:when test="${promedioVenta != null and totalPedidos > 0}">
                                    (Promedio: $<fmt:formatNumber value="${promedioVenta}" 
                                     minFractionDigits="2" maxFractionDigits="2"/>)
                                </c:when>
                                <c:otherwise>
                                    (Sin promedio)
                                </c:otherwise>
                            </c:choose>
                        </span>
                    </div>
                </div>
            </div>

            <div class="seccion_inferior">
                <!-- Gráfico de Ventas Diarias -->
                <div class="tarjeta_grafico">
                    <h3>Ventas Diarias - ${periodoSeleccionado != null ? periodoSeleccionado : 'Período Actual'}</h3>
                    <p class="periodo_info">
                        Período: ${fechaInicio != null ? fechaInicio : 'N/A'} - ${fechaFin != null ? fechaFin : 'N/A'}
                        <c:if test="${not empty ventasDiarias}">
                            | ${fn:length(ventasDiarias)} días con datos
                        </c:if>
                    </p>
                    <canvas id="graficoDiario"></canvas>
                </div>

                <!-- Mejores Platos -->
                <div class="mejores_platos">
                    <h3>Platos Más Vendidos</h3>
                    <div class="encabezado_platos">
                        <span>Plato</span>
                        <span>Vendidos</span>
                    </div>

                    <c:choose>
                        <c:when test="${not empty platosPopulares}">
                            <c:forEach var="platoData" items="${platosPopulares}" varStatus="status">
                                <div class="elemento_plato">
                                    <img src="${pageContext.request.contextPath}<c:choose><c:when test="${not empty platoData[2]}">${platoData[2]}</c:when><c:otherwise>/images/default-food.png</c:otherwise></c:choose>" 
                                         alt="${platoData[0]}" class="imagen_plato"
                                         onerror="this.src='${pageContext.request.contextPath}/images/default-food.png'">
                                    <div class="info_plato">
                                        <p class="nombre_plato">${platoData[0]}</p>
                                        <p class="precio_plato">
                                            $<fmt:formatNumber value="${platoData[1]}" 
                                               minFractionDigits="2" maxFractionDigits="2"/>
                                        </p>
                                    </div>
                                    <span class="cantidad_pedidos">${platoData[3]}</span>
                                </div>
                            </c:forEach>
                        </c:when>
                        <c:otherwise>
                            <div class="sin_datos">
                                <i class="fas fa-chart-bar"></i>
                                <p>No hay datos de platos disponibles para el período seleccionado</p>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </main>
    </div>

    <!-- Variables JavaScript con datos del servidor -->
    <script>
        // ====================== DATOS PARA JAVASCRIPT ======================
        
        // Datos de ventas diarias con validación
        var ventasDiarias = [
            <c:choose>
                <c:when test="${not empty ventasDiarias}">
                    <c:forEach var="venta" items="${ventasDiarias}" varStatus="status">
                        <c:set var="valor" value="${venta[1]}" />
                        <c:choose>
                            <c:when test="${valor != null}">
                                <fmt:formatNumber value="${valor}" pattern="0.00" />
                            </c:when>
                            <c:otherwise>0.00</c:otherwise>
                        </c:choose>
                        <c:if test="${!status.last}">,</c:if>
                    </c:forEach>
                </c:when>
                <c:otherwise>
                    // Array vacío - no hay datos de ventas
                </c:otherwise>
            </c:choose>
        ];

        // Fechas correspondientes a las ventas
        var fechasVentas = [
            <c:choose>
                <c:when test="${not empty ventasDiarias}">
                    <c:forEach var="venta" items="${ventasDiarias}" varStatus="status">
                        '<c:choose><c:when test="${venta[0] != null}"><fmt:formatDate value="${venta[0]}" pattern="dd/MM" /></c:when><c:otherwise>Día ${status.index + 1}</c:otherwise></c:choose>'<c:if test="${!status.last}">,</c:if>
                    </c:forEach>
                </c:when>
                <c:otherwise>
                    // Array vacío - no hay fechas
                </c:otherwise>
            </c:choose>
        ];

        // Distribución de categorías con validación
        var distribucionCategorias = {
            comida: <c:choose>
                <c:when test="${not empty distribucionCategorias['Comida']}">
                    <fmt:formatNumber value="${distribucionCategorias['Comida']}" pattern="0.00" />
                </c:when>
                <c:otherwise>0.00</c:otherwise>
            </c:choose>,
            bebida: <c:choose>
                <c:when test="${not empty distribucionCategorias['Bebida Fría']}">
                    <fmt:formatNumber value="${distribucionCategorias['Bebida Fría']}" pattern="0.00" />
                </c:when>
                <c:otherwise>0.00</c:otherwise>
            </c:choose>,
            otros: <c:choose>
                <c:when test="${not empty distribucionCategorias['Otros']}">
                    <fmt:formatNumber value="${distribucionCategorias['Otros']}" pattern="0.00" />
                </c:when>
                <c:otherwise>0.00</c:otherwise>
            </c:choose>
        };

        // Variables adicionales para debug y funcionalidad
        var datosGenerales = {
            ingresoTotal: <c:choose>
                <c:when test="${not empty ingresoTotal}">
                    <fmt:formatNumber value="${ingresoTotal}" pattern="0.00" />
                </c:when>
                <c:otherwise>0.00</c:otherwise>
            </c:choose>,
            totalPedidos: <c:choose>
                <c:when test="${not empty totalPedidos}">${totalPedidos}</c:when>
                <c:otherwise>0</c:otherwise>
            </c:choose>,
            promedioVenta: <c:choose>
                <c:when test="${not empty promedioVenta}">
                    <fmt:formatNumber value="${promedioVenta}" pattern="0.00" />
                </c:when>
                <c:otherwise>0.00</c:otherwise>
            </c:choose>,
            porcentajeCrecimiento: <c:choose>
                <c:when test="${not empty porcentajeCrecimiento}">
                    <fmt:formatNumber value="${porcentajeCrecimiento}" pattern="0.00" />
                </c:when>
                <c:otherwise>0.00</c:otherwise>
            </c:choose>,
            periodo: "${periodoSeleccionado != null ? periodoSeleccionado : 'N/A'}",
            fechaInicio: "${fechaInicio != null ? fechaInicio : 'N/A'}",
            fechaFin: "${fechaFin != null ? fechaFin : 'N/A'}"
        };

        // ====================== DEBUG INFORMACIÓN ======================
        console.log('=== DASHBOARD DEBUG COMPLETO ===');
        console.log('Período seleccionado:', datosGenerales.periodo);
        console.log('Rango de fechas:', datosGenerales.fechaInicio + ' - ' + datosGenerales.fechaFin);
        console.log('Datos generales:', datosGenerales);
        console.log('Ventas diarias (' + ventasDiarias.length + ' elementos):', ventasDiarias);
        console.log('Fechas ventas (' + fechasVentas.length + ' elementos):', fechasVentas);
        console.log('Distribución categorías:', distribucionCategorias);
        console.log('Total distribución:', distribucionCategorias.comida + distribucionCategorias.bebida + distribucionCategorias.otros);
        console.log('===================================');

        // ====================== FUNCIONES GLOBALES ======================
        
        /**
         * Función para filtrar por período (llamada desde botones)
         */
        function filtrarPeriodo(ruta) {
            console.log('Filtrando por período:', ruta);
            
            // Deshabilitar botones para evitar clicks múltiples
            const botones = document.querySelectorAll('.boton_filtro');
            botones.forEach(btn => btn.disabled = true);
            
            // Redirigir con la nueva ruta
            window.location.href = '${pageContext.request.contextPath}/Dashboard?ruta=' + ruta;
        }
        
        /**
         * Función para reiniciar el estado después de cargar
         */
        function reiniciarEstado() {
            const botones = document.querySelectorAll('.boton_filtro');
            botones.forEach(btn => btn.disabled = false);
        }

        // ====================== INICIALIZACIÓN ======================
        
        // Reiniciar estado cuando se carga la página
        window.addEventListener('load', function() {
            reiniciarEstado();
            
            // Verificar si hay errores de JavaScript en datos
            if (ventasDiarias.some(isNaN)) {
                console.warn('Algunos valores de ventas diarias no son números válidos');
            }
            
            if (isNaN(datosGenerales.ingresoTotal) || isNaN(datosGenerales.totalPedidos)) {
                console.warn('Algunos datos generales no son números válidos');
            }
        });

        // Manejar errores de carga de página
        window.addEventListener('error', function(e) {
            console.error('Error en la página del dashboard:', e.error);
        });

    </script>

    <!-- Cargar el JavaScript del dashboard -->
    <script src="${pageContext.request.contextPath}/js/dashboard.js"></script>
</body>
</html>