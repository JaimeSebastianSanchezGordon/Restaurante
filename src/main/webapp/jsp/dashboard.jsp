<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
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
        <aside class="barra_lateral">
            <div class="logo">
                <i class="fas fa-utensils"></i>
            </div>

            <nav class="menu_navegacion">
                <a href="${pageContext.request.contextPath}/Dashboard" class="elemento_navegacion activo">
                    <i class="fas fa-th-large"></i>
                </a>
                <a href="${pageContext.request.contextPath}/GestionarPedido" class="elemento_navegacion">
                    <i class="fas fa-file-alt"></i>
                </a>
                <a href="${pageContext.request.contextPath}/platos" class="elemento_navegacion">
                    <i class="fas fa-hamburger"></i>
                </a>
            </nav>
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
                <div class="alert alert-danger" style="margin: 20px; padding: 15px; background-color: #f8d7da; border: 1px solid #f5c6cb; border-radius: 5px; color: #721c24;">
                    <i class="fas fa-exclamation-triangle"></i>
                    ${error}
                </div>
            </c:if>

            <div class="estadisticas_superiores">
                <div class="tarjeta_estadistica">
                    <h3>Ingreso Total</h3>
                    <div class="grafico_dona">
                        <canvas id="graficoIngreso" width="200" height="200"></canvas>
                        <div class="centro_grafico">
                            <span>$<fmt:formatNumber value="${ingresoTotal}" minFractionDigits="0" maxFractionDigits="0"/></span>
                        </div>
                    </div>
                    <div class="leyenda">
                        <span class="elemento_leyenda">
                            <span class="punto naranja"></span> Comida
                        </span>
                        <span class="elemento_leyenda">
                            <span class="punto negro"></span> Bebida Fría
                        </span>
                        <span class="elemento_leyenda">
                            <span class="punto gris"></span> Otros
                        </span>
                    </div>
                </div>

                <div class="tarjeta_estadistica">
                    <h3>Saldo Total</h3>
                    <div class="monto_saldo">$<fmt:formatNumber value="${ingresoTotal}" minFractionDigits="0" maxFractionDigits="0"/></div>

                    <div class="elemento_saldo">
                        <div class="icono_saldo negro">
                            <i class="fas fa-chart-line"></i>
                        </div>
                        <div class="info_saldo">
                            <p>Ingreso Total</p>
                            <p class="monto">$<fmt:formatNumber value="${ingresoTotal}" minFractionDigits="2" maxFractionDigits="2"/></p>
                        </div>
                        <span class="porcentaje ${porcentajeCrecimiento >= 0 ? 'positivo' : 'negativo'}">
                            (<fmt:formatNumber value="${porcentajeCrecimiento}" minFractionDigits="1" maxFractionDigits="1"/>% 
                            ${porcentajeCrecimiento >= 0 ? 'Aumento' : 'Descenso'})
                        </span>
                    </div>

                    <div class="elemento_saldo">
                        <div class="icono_saldo naranja">
                            <i class="fas fa-chart-pie"></i>
                        </div>
                        <div class="info_saldo">
                            <p>Pedidos Totales</p>
                            <p class="monto">${totalPedidos}</p>
                        </div>
                        <span class="porcentaje">
                            (Promedio: $<fmt:formatNumber value="${promedioVenta}" minFractionDigits="2" maxFractionDigits="2"/>)
                        </span>
                    </div>
                </div>
            </div>

            <div class="seccion_inferior">
                <div class="tarjeta_grafico">
                    <h3>Ventas Diarias - ${periodoSeleccionado}</h3>
                    <p class="periodo_info">Período: ${fechaInicio} - ${fechaFin}</p>
                    <canvas id="graficoDiario"></canvas>
                </div>

                <div class="mejores_platos">
                    <h3>Mejores Platos</h3>
                    <div class="encabezado_platos">
                        <span>Plato</span>
                        <span>Vendidos</span>
                    </div>

                    <c:forEach var="platoData" items="${platosPopulares}" varStatus="status">
                        <div class="elemento_plato">
                            <img src="${pageContext.request.contextPath}${not empty platoData[2] ? platoData[2] : '/images/default-food.png'}" 
                                 alt="${platoData[0]}" class="imagen_plato">
                            <div class="info_plato">
                                <p class="nombre_plato">${platoData[0]}</p>
                                <p class="precio_plato">$<fmt:formatNumber value="${platoData[1]}" minFractionDigits="2" maxFractionDigits="2"/></p>
                            </div>
                            <span class="cantidad_pedidos">${platoData[3]}</span>
                        </div>
                    </c:forEach>

                    <c:if test="${empty platosPopulares}">
                        <div class="elemento_plato">
                            <div class="info_plato">
                                <p class="nombre_plato">No hay datos disponibles</p>
                            </div>
                        </div>
                    </c:if>
                </div>
            </div>
        </main>
    </div>

    <!-- Variables JavaScript con datos del servidor -->
    <script>
        // Datos para los gráficos - solo datos reales de la BD
        var ventasDiarias = [
            <c:choose>
                <c:when test="${not empty ventasDiarias}">
                    <c:forEach var="venta" items="${ventasDiarias}" varStatus="status">
                        ${venta[1]}<c:if test="${!status.last}">,</c:if>
                    </c:forEach>
                </c:when>
                <c:otherwise>
                    // Array vacío si no hay datos
                </c:otherwise>
            </c:choose>
        ];

        var distribucionCategorias = {
            comida: <c:choose><c:when test="${not empty distribucionCategorias['Comida']}">${distribucionCategorias['Comida']}</c:when><c:otherwise>0</c:otherwise></c:choose>,
            bebida: <c:choose><c:when test="${not empty distribucionCategorias['Bebida Fría']}">${distribucionCategorias['Bebida Fría']}</c:when><c:otherwise>0</c:otherwise></c:choose>,
            otros: <c:choose><c:when test="${not empty distribucionCategorias['Otros']}">${distribucionCategorias['Otros']}</c:when><c:otherwise>0</c:otherwise></c:choose>
        };

        // Debug: Información sobre los datos cargados
        console.log('=== DASHBOARD DEBUG ===');
        console.log('Período seleccionado: ${periodoSeleccionado}');
        console.log('Total de datos de ventas:', ventasDiarias.length);
        console.log('Datos de ventas diarias:', ventasDiarias);
        console.log('Distribución categorías:', distribucionCategorias);
        console.log('Ingreso total: ${ingresoTotal}');
        console.log('Total pedidos: ${totalPedidos}');
        console.log('======================');

        // Función para filtrar por período
        function filtrarPeriodo(ruta) {
            console.log('Filtrando por:', ruta);
            window.location.href = '${pageContext.request.contextPath}/Dashboard?ruta=' + ruta;
        }
    </script>

    <script src="${pageContext.request.contextPath}/js/dashboard.js"></script>
</body>
</html>