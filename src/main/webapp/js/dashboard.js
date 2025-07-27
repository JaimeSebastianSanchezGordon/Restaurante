// ====================== VARIABLES GLOBALES ====================== 
const lienzoIngreso = document.getElementById('graficoIngreso');
const contextoIngreso = lienzoIngreso?.getContext('2d');
const lienzoDiario = document.getElementById('graficoDiario');
const contextoDiario = lienzoDiario?.getContext('2d');

// Datos globales
let datosVentasDiarias = [];
let datosDistribucion = { comida: 0, bebida: 0, otros: 0 };
let datosFechasVentas = [];

// ====================== INICIALIZACIÓN DE DATOS ======================

function inicializarDatos() {
    console.log('Inicializando datos del dashboard...');
    
    // Validar datos de ventas diarias
    if (typeof ventasDiarias !== 'undefined' && Array.isArray(ventasDiarias)) {
        datosVentasDiarias = ventasDiarias.map(valor => {
            const numero = parseFloat(valor);
            return isNaN(numero) ? 0 : numero;
        });
    } else {
        datosVentasDiarias = [];
    }

    // Validar fechas de ventas
    if (typeof fechasVentas !== 'undefined' && Array.isArray(fechasVentas)) {
        datosFechasVentas = fechasVentas.slice();
    } else {
        datosFechasVentas = [];
    }

    // Validar distribución de categorías
    if (typeof distribucionCategorias !== 'undefined' && distribucionCategorias !== null) {
        datosDistribucion = {
            comida: parseFloat(distribucionCategorias.comida) || 0,
            bebida: parseFloat(distribucionCategorias.bebida) || 0,
            otros: parseFloat(distribucionCategorias.otros) || 0
        };
    } else {
        datosDistribucion = { comida: 0, bebida: 0, otros: 0 };
    }

    console.log('Datos inicializados:', {
        ventas: datosVentasDiarias.length,
        fechas: datosFechasVentas.length,
        distribucion: datosDistribucion
    });
}

// ====================== GRÁFICO DE DONA ======================

function dibujarGraficoDona() {
    if (!contextoIngreso) return;
    
    const centroX = lienzoIngreso.width / 2;
    const centroY = lienzoIngreso.height / 2;
    const radio = 70;
    const grosor = 25;

    contextoIngreso.clearRect(0, 0, lienzoIngreso.width, lienzoIngreso.height);

    const total = datosDistribucion.comida + datosDistribucion.bebida + datosDistribucion.otros;
    
    if (total === 0) {
        // Mostrar mensaje de sin datos
        contextoIngreso.font = '16px Lexend, sans-serif';
        contextoIngreso.fillStyle = '#999999';
        contextoIngreso.textAlign = 'center';
        contextoIngreso.fillText('Sin datos', centroX, centroY - 10);
        contextoIngreso.fillText('disponibles', centroX, centroY + 10);
        return;
    }
    
    const datos = [
        { valor: (datosDistribucion.comida / total) * 100, color: '#F67F20' },
        { valor: (datosDistribucion.bebida / total) * 100, color: '#333333' },
        { valor: (datosDistribucion.otros / total) * 100, color: '#e0e0e0' }
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

            anguloActual += angulo + 0.02;
        }
    });

    // Círculo interior
    contextoIngreso.beginPath();
    contextoIngreso.arc(centroX, centroY, radio - grosor / 2, 0, Math.PI * 2);
    contextoIngreso.fillStyle = 'rgba(255, 255, 255, 0.9)';
    contextoIngreso.fill();
}

// ====================== GRÁFICO DE LÍNEAS ======================

function prepararLienzo() {
    if (!lienzoDiario) return;
    
    const contenedor = lienzoDiario.parentElement;
    const anchoContenedor = contenedor.offsetWidth - 60;

    lienzoDiario.width = anchoContenedor;
    lienzoDiario.height = 320;

    dibujarGraficoLinea();
}

function dibujarGraficoLinea() {
    if (!contextoDiario) return;
    
    const datos = datosVentasDiarias || [];
    
    contextoDiario.clearRect(0, 0, lienzoDiario.width, lienzoDiario.height);
    
    if (datos.length === 0) {
        // Mostrar mensaje de sin datos
        const centroX = lienzoDiario.width / 2;
        const centroY = lienzoDiario.height / 2;
        contextoDiario.font = '16px Lexend, sans-serif';
        contextoDiario.fillStyle = '#999999';
        contextoDiario.textAlign = 'center';
        contextoDiario.fillText('No hay datos de ventas', centroX, centroY - 10);
        contextoDiario.fillText('para el período seleccionado', centroX, centroY + 10);
        return;
    }
    
    // Configuración del gráfico
    const valorMaximo = Math.max(...datos, 100);
    const valorMinimo = 0;
    const espacio = 60;
    const ancho = lienzoDiario.width - espacio * 2;
    const alto = lienzoDiario.height - espacio * 2;

    // Dibujar cuadrícula y etiquetas
    dibujarCuadricula(valorMaximo, espacio, ancho, alto, datos.length);
    
    // Dibujar área bajo la curva
    dibujarArea(datos, valorMaximo, valorMinimo, espacio, ancho, alto);
    
    // Dibujar línea principal
    dibujarLinea(datos, valorMaximo, valorMinimo, espacio, ancho, alto);
    
    // Dibujar puntos
    dibujarPuntos(datos, valorMaximo, valorMinimo, espacio, ancho, alto);
}

function dibujarCuadricula(valorMaximo, espacio, ancho, alto, cantidadDatos) {
    contextoDiario.font = '12px Lexend, sans-serif';
    contextoDiario.strokeStyle = '#f5f5f5';
    contextoDiario.lineWidth = 1;

    // Líneas horizontales y etiquetas Y
    const lineasHorizontales = 5;
    for (let i = 0; i <= lineasHorizontales; i++) {
        const y = espacio + (alto / lineasHorizontales) * i;
        
        contextoDiario.beginPath();
        contextoDiario.moveTo(espacio, y);
        contextoDiario.lineTo(lienzoDiario.width - espacio, y);
        contextoDiario.stroke();

        const valor = Math.round(valorMaximo - (valorMaximo / lineasHorizontales) * i);
        contextoDiario.fillStyle = '#666666';
        contextoDiario.textAlign = 'right';
        contextoDiario.fillText('$' + formatearNumero(valor), espacio - 10, y + 4);
    }

    // Líneas verticales y etiquetas X
    const maxEtiquetas = Math.min(7, cantidadDatos);
    contextoDiario.fillStyle = '#666666';
    contextoDiario.textAlign = 'center';

    for (let i = 0; i < maxEtiquetas; i++) {
        const indice = Math.floor(i * (cantidadDatos - 1) / Math.max(maxEtiquetas - 1, 1));
        const x = espacio + (ancho / Math.max(maxEtiquetas - 1, 1)) * i;
        
        contextoDiario.strokeStyle = '#f5f5f5';
        contextoDiario.beginPath();
        contextoDiario.moveTo(x, espacio);
        contextoDiario.lineTo(x, espacio + alto);
        contextoDiario.stroke();
        
        const etiqueta = datosFechasVentas[indice] || `Día ${indice + 1}`;
        contextoDiario.fillText(etiqueta, x, alto + espacio + 20);
    }
}

function dibujarArea(datos, valorMaximo, valorMinimo, espacio, ancho, alto) {
    contextoDiario.beginPath();
    contextoDiario.moveTo(espacio, alto + espacio);

    datos.forEach((valor, indice) => {
        const x = espacio + (ancho / Math.max(datos.length - 1, 1)) * indice;
        const y = espacio + alto - ((valor - valorMinimo) / (valorMaximo - valorMinimo)) * alto;
        contextoDiario.lineTo(x, y);
    });

    contextoDiario.lineTo(espacio + ancho, alto + espacio);
    contextoDiario.closePath();

    const degradado = contextoDiario.createLinearGradient(0, espacio, 0, alto + espacio);
    degradado.addColorStop(0, 'rgba(246, 127, 32, 0.3)');
    degradado.addColorStop(0.5, 'rgba(246, 127, 32, 0.1)');
    degradado.addColorStop(1, 'rgba(246, 127, 32, 0)');

    contextoDiario.fillStyle = degradado;
    contextoDiario.fill();
}

function dibujarLinea(datos, valorMaximo, valorMinimo, espacio, ancho, alto) {
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

function dibujarPuntos(datos, valorMaximo, valorMinimo, espacio, ancho, alto) {
    datos.forEach((valor, indice) => {
        const x = espacio + (ancho / Math.max(datos.length - 1, 1)) * indice;
        const y = espacio + alto - ((valor - valorMinimo) / (valorMaximo - valorMinimo)) * alto;

        // Círculo exterior
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

// ====================== UTILIDADES ======================

function formatearNumero(numero) {
    if (numero >= 1000000) {
        return (numero / 1000000).toFixed(1) + 'M';
    } else if (numero >= 1000) {
        return (numero / 1000).toFixed(1) + 'K';
    }
    return numero.toString();
}

function configurarFiltros() {
    const botonesFiltro = document.querySelectorAll('.boton_filtro');
    
    botonesFiltro.forEach(boton => {
        boton.addEventListener('click', function() {
            botonesFiltro.forEach(btn => {
                btn.classList.remove('activo');
                btn.disabled = true;
            });
            this.classList.add('activo');
        });
    });
}

// ====================== INICIALIZACIÓN ======================

function inicializarDashboard() {
    console.log('=== INICIANDO DASHBOARD ===');
    
    try {
        if (!lienzoIngreso || !contextoIngreso || !lienzoDiario || !contextoDiario) {
            throw new Error('Elementos del canvas no encontrados');
        }
        
        inicializarDatos();
        configurarFiltros();
        dibujarGraficoDona();
        prepararLienzo();
        
        console.log('Dashboard inicializado correctamente');
        
    } catch (error) {
        console.error('Error al inicializar dashboard:', error);
    }
}

// ====================== EVENT LISTENERS ======================

document.addEventListener('DOMContentLoaded', inicializarDashboard);

window.addEventListener('resize', function() {
    clearTimeout(window.resizeTimeout);
    window.resizeTimeout = setTimeout(() => {
        if (lienzoIngreso && lienzoDiario) {
            prepararLienzo();
            dibujarGraficoDona();
        }
    }, 250);
});

console.log('Dashboard JavaScript cargado correctamente');