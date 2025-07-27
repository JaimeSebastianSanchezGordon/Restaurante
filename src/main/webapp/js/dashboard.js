// ====================== VARIABLES GLOBALES ====================== 
const lienzoIngreso = document.getElementById('graficoIngreso');
const contextoIngreso = lienzoIngreso?.getContext('2d');
const lienzoDiario = document.getElementById('graficoDiario');
const contextoDiario = lienzoDiario?.getContext('2d');

// Datos globales con validación (nombres únicos para evitar conflictos)
let datosVentasDiarias = [];
let datosDistribucion = { comida: 0, bebida: 0, otros: 0 };
let datosFechasVentas = [];

// ====================== FUNCIONES DE VALIDACIÓN ======================

/**
 * Valida y inicializa los datos desde el JSP
 */
function inicializarDatos() {
    console.log('Inicializando datos del dashboard...');
    
    // Validar datos de ventas diarias
    if (typeof ventasDiarias !== 'undefined' && Array.isArray(ventasDiarias)) {
        datosVentasDiarias = ventasDiarias.map(valor => {
            const numero = parseFloat(valor);
            return isNaN(numero) ? 0 : numero;
        });
        console.log('Ventas diarias cargadas:', datosVentasDiarias.length, 'elementos');
    } else {
        datosVentasDiarias = [];
        console.warn('ventasDiarias no está definido o no es un array');
    }

    // Validar fechas de ventas (usando variable del JSP)
    if (typeof fechasVentas !== 'undefined' && Array.isArray(fechasVentas)) {
        datosFechasVentas = fechasVentas.slice(); // Copia del array
        console.log('Fechas de ventas cargadas:', datosFechasVentas.length, 'elementos');
    } else {
        datosFechasVentas = [];
        console.warn('fechasVentas no está definido o no es un array');
    }

    // Validar distribución de categorías
    if (typeof distribucionCategorias !== 'undefined' && distribucionCategorias !== null) {
        datosDistribucion = {
            comida: parseFloat(distribucionCategorias.comida) || 0,
            bebida: parseFloat(distribucionCategorias.bebida) || 0,
            otros: parseFloat(distribucionCategorias.otros) || 0
        };
        console.log('Distribución cargada:', datosDistribucion);
    } else {
        datosDistribucion = { comida: 0, bebida: 0, otros: 0 };
        console.warn('distribucionCategorias no está definido');
    }

    console.log('=== DATOS INICIALIZADOS CORRECTAMENTE ===');
    console.log('Ventas diarias:', datosVentasDiarias);
    console.log('Fechas ventas:', datosFechasVentas);
    console.log('Distribución:', datosDistribucion);
    console.log('=========================================');
}

/**
 * Verifica que los elementos del DOM existan
 */
function verificarElementos() {
    if (!lienzoIngreso || !contextoIngreso) {
        console.error('Canvas de ingreso no encontrado');
        return false;
    }
    
    if (!lienzoDiario || !contextoDiario) {
        console.error('Canvas diario no encontrado');
        return false;
    }
    
    console.log('Elementos del DOM verificados correctamente');
    return true;
}

// ====================== FUNCIONES DE GRÁFICOS ======================

/**
 * Dibuja el gráfico de dona con distribución de categorías
 */
function dibujarGraficoDona() {
    if (!contextoIngreso) {
        console.error('Contexto de ingreso no disponible');
        return;
    }
    
    const centroX = lienzoIngreso.width / 2;
    const centroY = lienzoIngreso.height / 2;
    const radio = 70;
    const grosor = 25;

    contextoIngreso.clearRect(0, 0, lienzoIngreso.width, lienzoIngreso.height);

    // Calcular totales para porcentajes
    const total = datosDistribucion.comida + datosDistribucion.bebida + datosDistribucion.otros;
    
    console.log('Dibujando gráfico de dona - Total:', total);
    
    // Si no hay datos, mostrar mensaje
    if (total === 0) {
        dibujarMensajeSinDatos(contextoIngreso, centroX, centroY, 'Sin datos\ndisponibles');
        return;
    }
    
    const datos = [
        { 
            valor: (datosDistribucion.comida / total) * 100, 
            color: '#F67F20', 
            etiqueta: 'Comida' 
        },
        { 
            valor: (datosDistribucion.bebida / total) * 100, 
            color: '#333333', 
            etiqueta: 'Bebida Fría' 
        },
        { 
            valor: (datosDistribucion.otros / total) * 100, 
            color: '#e0e0e0', 
            etiqueta: 'Otros' 
        }
    ];

    let anguloActual = -Math.PI / 2;

    // Dibujar segmentos
    datos.forEach(segmento => {
        if (segmento.valor > 0) {
            const angulo = (segmento.valor / 100) * Math.PI * 2;

            contextoIngreso.beginPath();
            contextoIngreso.arc(centroX, centroY, radio, anguloActual, anguloActual + angulo);
            contextoIngreso.lineWidth = grosor;
            contextoIngreso.strokeStyle = segmento.color;
            contextoIngreso.stroke();

            anguloActual += angulo + 0.02; // Pequeño espacio entre segmentos
        }
    });

    // Círculo interior para efecto de dona
    contextoIngreso.beginPath();
    contextoIngreso.arc(centroX, centroY, radio - grosor / 2, 0, Math.PI * 2);
    contextoIngreso.fillStyle = 'rgba(255, 255, 255, 0.9)';
    contextoIngreso.fill();

    // Sombra interior
    contextoIngreso.beginPath();
    contextoIngreso.arc(centroX, centroY, radio - grosor + 5, 0, Math.PI * 2);
    contextoIngreso.fillStyle = 'rgba(0, 0, 0, 0.03)';
    contextoIngreso.fill();
    
    console.log('Gráfico de dona dibujado correctamente');
}

/**
 * Prepara el lienzo del gráfico diario
 */
function prepararLienzo() {
    if (!lienzoDiario) {
        console.error('Lienzo diario no disponible');
        return;
    }
    
    const contenedor = lienzoDiario.parentElement;
    const anchoContenedor = contenedor.offsetWidth - 60;

    lienzoDiario.width = anchoContenedor;
    lienzoDiario.height = 320;

    console.log('Lienzo preparado:', anchoContenedor + 'x320');
    dibujarGraficoLinea();
}

/**
 * Dibuja el gráfico de líneas para ventas diarias
 */
function dibujarGraficoLinea() {
    if (!contextoDiario) {
        console.error('Contexto diario no disponible');
        return;
    }
    
    const datos = datosVentasDiarias || [];
    
    contextoDiario.clearRect(0, 0, lienzoDiario.width, lienzoDiario.height);
    
    console.log('Dibujando gráfico de línea con', datos.length, 'datos');
    
    // Si no hay datos, mostrar mensaje
    if (datos.length === 0) {
        const centroX = lienzoDiario.width / 2;
        const centroY = lienzoDiario.height / 2;
        dibujarMensajeSinDatos(contextoDiario, centroX, centroY, 
            'No hay datos de ventas\npara el período seleccionado');
        return;
    }
    
    // Configuración del gráfico
    const valorMaximo = Math.max(...datos, 100); // Mínimo 100 para mejor escala
    const valorMinimo = 0;
    const espacio = 60;
    const ancho = lienzoDiario.width - espacio * 2;
    const alto = lienzoDiario.height - espacio * 2;

    // Configurar estilos
    contextoDiario.font = '12px Lexend, sans-serif';
    contextoDiario.strokeStyle = '#f5f5f5';
    contextoDiario.lineWidth = 1;

    // Dibujar cuadrícula horizontal
    const lineasHorizontales = 5;
    for (let i = 0; i <= lineasHorizontales; i++) {
        const y = espacio + (alto / lineasHorizontales) * i;

        contextoDiario.beginPath();
        contextoDiario.moveTo(espacio, y);
        contextoDiario.lineTo(lienzoDiario.width - espacio, y);
        contextoDiario.stroke();

        // Etiquetas del eje Y
        const valor = Math.round(valorMaximo - (valorMaximo / lineasHorizontales) * i);
        contextoDiario.fillStyle = '#666666';
        contextoDiario.textAlign = 'right';
        contextoDiario.fillText('$' + formatearNumero(valor), espacio - 10, y + 4);
    }

    // Dibujar cuadrícula vertical y etiquetas del eje X
    const maxEtiquetas = Math.min(7, datos.length);
    contextoDiario.fillStyle = '#666666';
    contextoDiario.textAlign = 'center';

    for (let i = 0; i < maxEtiquetas; i++) {
        const indice = Math.floor(i * (datos.length - 1) / Math.max(maxEtiquetas - 1, 1));
        const x = espacio + (ancho / Math.max(maxEtiquetas - 1, 1)) * i;
        
        // Línea vertical
        contextoDiario.strokeStyle = '#f5f5f5';
        contextoDiario.beginPath();
        contextoDiario.moveTo(x, espacio);
        contextoDiario.lineTo(x, espacio + alto);
        contextoDiario.stroke();
        
        // Etiqueta
        const etiqueta = datosFechasVentas[indice] || `Día ${indice + 1}`;
        contextoDiario.fillText(etiqueta, x, alto + espacio + 20);
    }

    // Dibujar área bajo la curva
    dibujarAreaGrafico(datos, valorMaximo, valorMinimo, espacio, ancho, alto);
    
    // Dibujar línea principal
    dibujarLineaGrafico(datos, valorMaximo, valorMinimo, espacio, ancho, alto);
    
    // Dibujar puntos
    dibujarPuntosGrafico(datos, valorMaximo, valorMinimo, espacio, ancho, alto);
    
    // Agregar interactividad
    agregarInteractividadGrafico(datos, valorMaximo, valorMinimo, espacio, ancho, alto);
    
    console.log('Gráfico de línea dibujado correctamente');
}

/**
 * Dibuja el área bajo la curva del gráfico
 */
function dibujarAreaGrafico(datos, valorMaximo, valorMinimo, espacio, ancho, alto) {
    if (datos.length === 0) return;
    
    contextoDiario.beginPath();
    contextoDiario.moveTo(espacio, alto + espacio);

    datos.forEach((valor, indice) => {
        const x = espacio + (ancho / Math.max(datos.length - 1, 1)) * indice;
        const y = espacio + alto - ((valor - valorMinimo) / (valorMaximo - valorMinimo)) * alto;
        contextoDiario.lineTo(x, y);
    });

    contextoDiario.lineTo(espacio + ancho, alto + espacio);
    contextoDiario.closePath();

    // Gradiente para el área
    const degradado = contextoDiario.createLinearGradient(0, espacio, 0, alto + espacio);
    degradado.addColorStop(0, 'rgba(246, 127, 32, 0.3)');
    degradado.addColorStop(0.5, 'rgba(246, 127, 32, 0.1)');
    degradado.addColorStop(1, 'rgba(246, 127, 32, 0)');

    contextoDiario.fillStyle = degradado;
    contextoDiario.fill();
}

/**
 * Dibuja la línea principal del gráfico
 */
function dibujarLineaGrafico(datos, valorMaximo, valorMinimo, espacio, ancho, alto) {
    if (datos.length === 0) return;
    
    contextoDiario.beginPath();
    contextoDiario.strokeStyle = '#F67F20';
    contextoDiario.lineWidth = 3;
    contextoDiario.lineJoin = 'round';
    contextoDiario.lineCap = 'round';

    datos.forEach((valor, indice) => {
        const x = espacio + (ancho / Math.max(datos.length - 1, 1)) * indice;
        const y = espacio + alto - ((valor - valorMinimo) / (valorMaximo - valorMinimo)) * alto;

        if (indice === 0) {
            contextoDiario.moveTo(x, y);
        } else {
            contextoDiario.lineTo(x, y);
        }
    });

    contextoDiario.stroke();
}

/**
 * Dibuja los puntos en la línea del gráfico
 */
function dibujarPuntosGrafico(datos, valorMaximo, valorMinimo, espacio, ancho, alto) {
    if (datos.length === 0) return;
    
    datos.forEach((valor, indice) => {
        const x = espacio + (ancho / Math.max(datos.length - 1, 1)) * indice;
        const y = espacio + alto - ((valor - valorMinimo) / (valorMaximo - valorMinimo)) * alto;

        // Círculo exterior (halo)
        contextoDiario.beginPath();
        contextoDiario.arc(x, y, 8, 0, Math.PI * 2);
        contextoDiario.fillStyle = 'rgba(246, 127, 32, 0.2)';
        contextoDiario.fill();

        // Círculo principal
        contextoDiario.beginPath();
        contextoDiario.arc(x, y, 5, 0, Math.PI * 2);
        contextoDiario.fillStyle = '#F67F20';
        contextoDiario.fill();

        // Punto central
        contextoDiario.beginPath();
        contextoDiario.arc(x, y, 2, 0, Math.PI * 2);
        contextoDiario.fillStyle = 'white';
        contextoDiario.fill();
    });
}

/**
 * Agrega interactividad al gráfico
 */
function agregarInteractividadGrafico(datos, valorMaximo, valorMinimo, espacio, ancho, alto) {
    if (datos.length === 0) return;
    
    lienzoDiario.onmousemove = function(e) {
        const rect = lienzoDiario.getBoundingClientRect();
        const mouseX = e.clientX - rect.left;
        const mouseY = e.clientY - rect.top;
        
        let puntoEncontrado = false;

        datos.forEach((valor, indice) => {
            const x = espacio + (ancho / Math.max(datos.length - 1, 1)) * indice;
            const y = espacio + alto - ((valor - valorMinimo) / (valorMaximo - valorMinimo)) * alto;

            const distancia = Math.sqrt(Math.pow(mouseX - x, 2) + Math.pow(mouseY - y, 2));

            if (distancia < 15) {
                lienzoDiario.style.cursor = 'pointer';
                mostrarTooltip(mouseX, mouseY, valor, datosFechasVentas[indice] || `Día ${indice + 1}`);
                puntoEncontrado = true;
            }
        });
        
        if (!puntoEncontrado) {
            lienzoDiario.style.cursor = 'default';
            ocultarTooltip();
        }
    };
    
    lienzoDiario.onmouseleave = function() {
        lienzoDiario.style.cursor = 'default';
        ocultarTooltip();
    };
}

/**
 * Muestra mensaje cuando no hay datos
 */
function dibujarMensajeSinDatos(contexto, x, y, mensaje) {
    contexto.font = '16px Lexend, sans-serif';
    contexto.fillStyle = '#999999';
    contexto.textAlign = 'center';
    
    const lineas = mensaje.split('\n');
    lineas.forEach((linea, indice) => {
        contexto.fillText(linea, x, y - (lineas.length - 1) * 10 + indice * 20);
    });
}

// ====================== FUNCIONES DE UTILIDAD ======================

/**
 * Formatea números para mostrar
 */
function formatearNumero(numero) {
    if (numero >= 1000000) {
        return (numero / 1000000).toFixed(1) + 'M';
    } else if (numero >= 1000) {
        return (numero / 1000).toFixed(1) + 'K';
    }
    return numero.toString();
}

/**
 * Muestra tooltip en el gráfico
 */
function mostrarTooltip(x, y, valor, fecha) {
    ocultarTooltip();
    
    const tooltip = document.createElement('div');
    tooltip.className = 'tooltip-grafico';
    tooltip.innerHTML = `
        <div><strong>${fecha}</strong></div>
        <div>$${valor.toLocaleString()}</div>
    `;
    
    tooltip.style.cssText = `
        position: absolute;
        background: rgba(0, 0, 0, 0.8);
        color: white;
        padding: 8px 12px;
        border-radius: 6px;
        font-size: 12px;
        font-family: Lexend, sans-serif;
        pointer-events: none;
        z-index: 1000;
        left: ${x + 10}px;
        top: ${y - 10}px;
        transform: translateY(-100%);
        box-shadow: 0 2px 8px rgba(0, 0, 0, 0.2);
    `;
    
    document.body.appendChild(tooltip);
}

/**
 * Oculta el tooltip
 */
function ocultarTooltip() {
    const tooltips = document.querySelectorAll('.tooltip-grafico');
    tooltips.forEach(tooltip => tooltip.remove());
}

/**
 * Actualiza los datos y redibuja los gráficos
 */
function actualizarDatos(nuevosVentas, nuevaDistribucion, nuevasFechas = []) {
    datosVentasDiarias = nuevosVentas || [];
    datosDistribucion = nuevaDistribucion || { comida: 0, bebida: 0, otros: 0 };
    datosFechasVentas = nuevasFechas || [];
    
    console.log('Datos actualizados manualmente:', {
        ventas: datosVentasDiarias,
        distribucion: datosDistribucion,
        fechas: datosFechasVentas
    });
    
    dibujarGraficoDona();
    dibujarGraficoLinea();
}

/**
 * Funciones individuales para actualizar gráficos
 */
function actualizarGraficoDona(nuevaDistribucion) {
    datosDistribucion = nuevaDistribucion || { comida: 0, bebida: 0, otros: 0 };
    dibujarGraficoDona();
}

function actualizarGraficoLinea(nuevosVentas, nuevasFechas = []) {
    datosVentasDiarias = nuevosVentas || [];
    datosFechasVentas = nuevasFechas || [];
    dibujarGraficoLinea();
}

// ====================== MANEJO DE FILTROS ======================

/**
 * Maneja los filtros de tiempo con efectos visuales
 */
function configurarFiltros() {
    const botonesFiltro = document.querySelectorAll('.boton_filtro');
    
    botonesFiltro.forEach(boton => {
        boton.addEventListener('click', function() {
            // Deshabilitar todos los botones durante la carga
            botonesFiltro.forEach(btn => {
                btn.classList.remove('activo');
                btn.disabled = true;
            });
            
            // Activar el botón clickeado
            this.classList.add('activo');
            
            // Mostrar indicador de carga
            mostrarCargando();
        });
    });
    
    console.log('Filtros configurados para', botonesFiltro.length, 'botones');
}

/**
 * Muestra indicador de carga
 */
function mostrarCargando() {
    const tarjetas = document.querySelectorAll('.tarjeta_estadistica, .tarjeta_grafico, .mejores_platos');
    tarjetas.forEach(tarjeta => {
        tarjeta.classList.add('loading');
    });
}

/**
 * Oculta indicador de carga
 */
function ocultarCargando() {
    const tarjetas = document.querySelectorAll('.tarjeta_estadistica, .tarjeta_grafico, .mejores_platos');
    tarjetas.forEach(tarjeta => {
        tarjeta.classList.remove('loading');
    });
}

// ====================== MANEJO DE REDIMENSIONAMIENTO ======================

let temporizadorRedimension;

/**
 * Maneja el redimensionamiento de la ventana
 */
function manejarRedimensionamiento() {
    clearTimeout(temporizadorRedimension);
    temporizadorRedimension = setTimeout(() => {
        console.log('Redimensionando gráficos...');
        
        if (verificarElementos()) {
            prepararLienzo();
            dibujarGraficoDona();
        }
    }, 250);
}

// ====================== INICIALIZACIÓN PRINCIPAL ======================

/**
 * Inicializa todo el dashboard
 */
function inicializarDashboard() {
    console.log('=== INICIANDO DASHBOARD ===');
    
    try {
        // Verificar que los elementos existan
        if (!verificarElementos()) {
            throw new Error('Elementos del canvas no encontrados');
        }
        
        // Inicializar datos desde JSP
        inicializarDatos();
        
        // Configurar filtros
        configurarFiltros();
        
        // Dibujar gráficos iniciales
        dibujarGraficoDona();
        prepararLienzo();
        
        // Ocultar cualquier indicador de carga
        ocultarCargando();
        
        console.log('Dashboard inicializado correctamente');
        
    } catch (error) {
        console.error('Error al inicializar dashboard:', error);
        mostrarErrorGeneral('Error al inicializar el dashboard. Por favor, recarga la página.');
    }
}

/**
 * Muestra mensaje de error general
 */
function mostrarErrorGeneral(mensaje) {
    console.error('Error general:', mensaje);
    
    const alertaExistente = document.querySelector('.alert-ajax');
    if (alertaExistente) {
        alertaExistente.remove();
    }
    
    const alerta = document.createElement('div');
    alerta.className = 'alert alert-danger alert-ajax';
    alerta.innerHTML = `
        <i class="fas fa-exclamation-triangle"></i>
        ${mensaje}
    `;
    
    const contenido = document.querySelector('.contenido_principal');
    if (contenido) {
        contenido.insertBefore(alerta, contenido.firstChild);
        
        // Auto-ocultar después de 5 segundos
        setTimeout(() => {
            alerta.style.opacity = '0';
            setTimeout(() => alerta.remove(), 300);
        }, 5000);
    }
}

// ====================== EVENT LISTENERS ======================

document.addEventListener('DOMContentLoaded', function() {
    console.log('DOM cargado, inicializando dashboard...');
    inicializarDashboard();
});

window.addEventListener('resize', manejarRedimensionamiento);

// Manejar errores globales de JavaScript
window.addEventListener('error', function(e) {
    console.error('Error global en dashboard:', e.error);
});

// Manejar promesas rechazadas
window.addEventListener('unhandledrejection', function(e) {
    console.error('Promise rechazada en dashboard:', e.reason);
});

// ====================== FUNCIONES PÚBLICAS PARA JSP ======================

/**
 * Función expuesta globalmente para ser llamada desde el JSP
 */
window.reiniciarFiltros = function() {
    const botonesFiltro = document.querySelectorAll('.boton_filtro');
    botonesFiltro.forEach(btn => {
        btn.disabled = false;
    });
    ocultarCargando();
    console.log('Filtros reiniciados');
};

/**
 * Función para debug manual desde consola
 */
window.debugDashboard = function() {
    console.log('=== DEBUG MANUAL DASHBOARD ===');
    console.log('Elementos canvas:', {
        ingreso: !!lienzoIngreso,
        diario: !!lienzoDiario
    });
    console.log('Datos actuales:', {
        ventas: datosVentasDiarias,
        distribucion: datosDistribucion,
        fechas: datosFechasVentas
    });
    console.log('Dimensiones canvas:', {
        ingreso: lienzoIngreso ? `${lienzoIngreso.width}x${lienzoIngreso.height}` : 'N/A',
        diario: lienzoDiario ? `${lienzoDiario.width}x${lienzoDiario.height}` : 'N/A'
    });
    console.log('Variables JSP disponibles:', {
        ventasDiarias: typeof ventasDiarias !== 'undefined',
        fechasVentas: typeof fechasVentas !== 'undefined', 
        distribucionCategorias: typeof distribucionCategorias !== 'undefined'
    });
    console.log('=============================');
};

/**
 * Función para redibujar manualmente los gráficos
 */
window.redibujarGraficos = function() {
    console.log('Redibujando gráficos manualmente...');
    if (verificarElementos()) {
        dibujarGraficoDona();
        prepararLienzo();
    }
};

console.log('Dashboard JavaScript cargado correctamente');