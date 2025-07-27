const lienzoIngreso = document.getElementById('graficoIngreso');
const contextoIngreso = lienzoIngreso.getContext('2d');
const lienzoDiario = document.getElementById('graficoDiario');
const contextoDiario = lienzoDiario.getContext('2d');

// Datos globales (serán definidos por el JSP)
let datosVentasDiarias = ventasDiarias || [];
let datosDistribucion = distribucionCategorias || {comida: 0, bebida: 0, otros: 0};

function dibujarGraficoDona() {
    const centroX = lienzoIngreso.width / 2;
    const centroY = lienzoIngreso.height / 2;
    const radio = 70;
    const grosor = 25;

    contextoIngreso.clearRect(0, 0, lienzoIngreso.width, lienzoIngreso.height);

    // Calcular totales para porcentajes
    const total = datosDistribucion.comida + datosDistribucion.bebida + datosDistribucion.otros;
    
    // Si no hay datos, mostrar mensaje
    if (total === 0) {
        contextoIngreso.font = '14px Lexend';
        contextoIngreso.fillStyle = '#999999';
        contextoIngreso.textAlign = 'center';
        contextoIngreso.fillText('Sin datos', centroX, centroY - 10);
        contextoIngreso.fillText('disponibles', centroX, centroY + 10);
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

    datos.forEach(segmento => {
        if (segmento.valor > 0) { // Solo dibujar si hay valor
            const angulo = (segmento.valor / 100) * Math.PI * 2;

            contextoIngreso.beginPath();
            contextoIngreso.arc(centroX, centroY, radio, anguloActual, anguloActual + angulo);
            contextoIngreso.lineWidth = grosor;
            contextoIngreso.strokeStyle = segmento.color;
            contextoIngreso.stroke();

            anguloActual += angulo + 0.02;
        }
    });

    // Círculo interior para efecto de dona
    contextoIngreso.beginPath();
    contextoIngreso.arc(centroX, centroY, radio - grosor / 2, 0, Math.PI * 2);
    contextoIngreso.fillStyle = 'rgba(0,0,0,0.05)';
    contextoIngreso.fill();
}

function prepararLienzo() {
    const contenedor = lienzoDiario.parentElement;
    const anchoContenedor = contenedor.offsetWidth - 60;

    lienzoDiario.width = anchoContenedor;
    lienzoDiario.height = 280;

    dibujarGraficoLinea();
}

function dibujarGraficoLinea() {
    // Solo usar datos reales, no datos de ejemplo
    const datos = datosVentasDiarias || [];
    
    contextoDiario.clearRect(0, 0, lienzoDiario.width, lienzoDiario.height);
    
    // Si no hay datos, mostrar mensaje
    if (datos.length === 0) {
        contextoDiario.font = '16px Lexend';
        contextoDiario.fillStyle = '#999999';
        contextoDiario.textAlign = 'center';
        const centroX = lienzoDiario.width / 2;
        const centroY = lienzoDiario.height / 2;
        contextoDiario.fillText('No hay datos de ventas', centroX, centroY - 10);
        contextoDiario.fillText('para el período seleccionado', centroX, centroY + 15);
        return;
    }
    
    // Generar etiquetas basadas en la cantidad de datos
    const etiquetas = [];
    const maxEtiquetas = Math.min(7, datos.length); // Máximo 7 etiquetas
    
    for (let i = 0; i < maxEtiquetas; i++) {
        const indice = Math.floor(i * (datos.length - 1) / (maxEtiquetas - 1));
        etiquetas.push((indice + 1).toString());
    }

    const valorMaximo = Math.max(...datos);
    const valorMinimo = 0;
    const espacio = 50;
    const ancho = lienzoDiario.width - espacio * 2;
    const alto = lienzoDiario.height - espacio * 2;

    contextoDiario.font = '13px Lexend';
    contextoDiario.strokeStyle = '#f5f5f5';
    contextoDiario.lineWidth = 1;

    // Dibujar líneas horizontales de la cuadrícula
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
        contextoDiario.fillText('$' + valor.toLocaleString(), espacio - 10, y + 4);
    }

    // Dibujar etiquetas del eje X
    contextoDiario.fillStyle = '#666666';
    contextoDiario.textAlign = 'center';

    etiquetas.forEach((etiqueta, indice) => {
        const x = espacio + (ancho / (etiquetas.length - 1)) * indice;
        contextoDiario.fillText(etiqueta, x, alto + espacio + 20);
    });

    // Dibujar el área bajo la curva
    contextoDiario.beginPath();
    contextoDiario.moveTo(espacio, alto + espacio);

    datos.forEach((valor, indice) => {
        const x = espacio + (ancho / (datos.length - 1)) * indice;
        const y = espacio + alto - ((valor - valorMinimo) / (valorMaximo - valorMinimo)) * alto;

        if (indice === 0) {
            contextoDiario.lineTo(x, y);
        } else {
            contextoDiario.lineTo(x, y);
        }
    });

    contextoDiario.lineTo(lienzoDiario.width - espacio, alto + espacio);
    contextoDiario.closePath();

    // Gradiente para el área
    const degradado = contextoDiario.createLinearGradient(0, espacio, 0, alto + espacio);
    degradado.addColorStop(0, 'rgba(246, 127, 32, 0.3)');
    degradado.addColorStop(0.5, 'rgba(246, 127, 32, 0.1)');
    degradado.addColorStop(1, 'rgba(246, 127, 32, 0)');

    contextoDiario.fillStyle = degradado;
    contextoDiario.fill();

    // Dibujar la línea principal
    contextoDiario.beginPath();
    contextoDiario.strokeStyle = '#F67F20';
    contextoDiario.lineWidth = 3;
    contextoDiario.lineJoin = 'round';
    contextoDiario.lineCap = 'round';

    datos.forEach((valor, indice) => {
        const x = espacio + (ancho / (datos.length - 1)) * indice;
        const y = espacio + alto - ((valor - valorMinimo) / (valorMaximo - valorMinimo)) * alto;

        if (indice === 0) {
            contextoDiario.moveTo(x, y);
        } else {
            contextoDiario.lineTo(x, y);
        }
    });

    contextoDiario.stroke();

    // Dibujar puntos en la línea
    datos.forEach((valor, indice) => {
        const x = espacio + (ancho / (datos.length - 1)) * indice;
        const y = espacio + alto - ((valor - valorMinimo) / (valorMaximo - valorMinimo)) * alto;

        // Círculo exterior (halo)
        contextoDiario.beginPath();
        contextoDiario.arc(x, y, 6, 0, Math.PI * 2);
        contextoDiario.fillStyle = 'rgba(246, 127, 32, 0.2)';
        contextoDiario.fill();

        // Círculo principal
        contextoDiario.beginPath();
        contextoDiario.arc(x, y, 4, 0, Math.PI * 2);
        contextoDiario.fillStyle = '#F67F20';
        contextoDiario.fill();

        // Punto central
        contextoDiario.beginPath();
        contextoDiario.arc(x, y, 2, 0, Math.PI * 2);
        contextoDiario.fillStyle = 'white';
        contextoDiario.fill();
    });

    // Agregar interactividad
    lienzoDiario.onmousemove = function(e) {
        const rect = lienzoDiario.getBoundingClientRect();
        const mouseX = e.clientX - rect.left;
        const mouseY = e.clientY - rect.top;

        datos.forEach((valor, indice) => {
            const x = espacio + (ancho / (datos.length - 1)) * indice;
            const y = espacio + alto - ((valor - valorMinimo) / (valorMaximo - valorMinimo)) * alto;

            const distancia = Math.sqrt(Math.pow(mouseX - x, 2) + Math.pow(mouseY - y, 2));

            if (distancia < 10) {
                lienzoDiario.style.cursor = 'pointer';
                lienzoDiario.title = `$${valor.toLocaleString()}`;
            } else {
                lienzoDiario.style.cursor = 'default';
            }
        });
    };
}

// Función para actualizar datos cuando cambia el filtro
function actualizarDatos(nuevosVentas, nuevaDistribucion) {
    datosVentasDiarias = nuevosVentas;
    datosDistribucion = nuevaDistribucion;
    
    dibujarGraficoDona();
    dibujarGraficoLinea();
}

document.addEventListener('DOMContentLoaded', function() {
    // Debug: Mostrar datos recibidos en consola
    console.log('Datos de ventas diarias:', datosVentasDiarias);
    console.log('Distribución de categorías:', datosDistribucion);
    
    dibujarGraficoDona();
    prepararLienzo();

    // Manejar filtros de tiempo
    const botonesFiltro = document.querySelectorAll('.boton_filtro');
    botonesFiltro.forEach(boton => {
        boton.addEventListener('click', function() {
            // Efectos visuales del botón activo
            botonesFiltro.forEach(btn => btn.classList.remove('activo'));
            this.classList.add('activo');
        });
    });
});

// Manejar redimensionamiento de ventana
let temporizadorRedimension;
window.addEventListener('resize', function() {
    clearTimeout(temporizadorRedimension);
    temporizadorRedimension = setTimeout(function() {
        prepararLienzo();
        dibujarGraficoDona();
    }, 250);
});

// Funciones de utilidad para actualizar gráficos desde AJAX
function actualizarGraficoDona(nuevaDistribucion) {
    datosDistribucion = nuevaDistribucion;
    dibujarGraficoDona();
}

function actualizarGraficoLinea(nuevosVentas) {
    datosVentasDiarias = nuevosVentas;
    dibujarGraficoLinea();
}

// Función para cargar datos vía AJAX (opcional)
function cargarDatosAjax(periodo) {
    fetch(`Dashboard?ruta=obtenerDatosGrafico&periodo=${periodo}`)
        .then(response => response.json())
        .then(data => {
            const ventas = data.map(item => item.total);
            actualizarGraficoLinea(ventas);
        })
        .catch(error => {
            console.error('Error al cargar datos:', error);
        });
}