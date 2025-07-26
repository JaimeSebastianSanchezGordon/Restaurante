package service;

import jakarta.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Set;

/**
 * Servicio para el manejo de imágenes de platos
 * Centraliza la lógica de validación, guardado y gestión de archivos de imagen
 */
public class ImagenService {

    // Path padre donde se guardarán las imágenes
    private static final String DIRECTORIO_BASE_IMAGENES = "images/platos";

    // Tipos MIME permitidos para las imágenes
    private static final Set<String> TIPOS_MIME_PERMITIDOS = Set.of(
            "image/jpeg",
            "image/png",
            "image/gif"
    );

    // Tamaño máximo permitido (5MB en bytes)
    private static final long TAMAÑO_MAXIMO_ARCHIVO = 5 * 1024 * 1024;

    // Imagen por defecto cuando no se sube ninguna
    private static final String IMAGEN_POR_DEFECTO = "/images/default-food.png";

    /**
     * Valida si un archivo de imagen cumple con los criterios establecidos
     * @param filePart El archivo a validar
     * @return true si es válido, false en caso contrario
     */
    public boolean validarImagen(Part filePart) {
        if (filePart == null || filePart.getSize() == 0) {
            return false;
        }

        // Validar tipo MIME
        String contentType = filePart.getContentType();
        if (contentType == null || !TIPOS_MIME_PERMITIDOS.contains(contentType.toLowerCase())) {
            return false;
        }

        // Validar tamaño
        return filePart.getSize() <= TAMAÑO_MAXIMO_ARCHIVO;
    }

    /**
     * Guarda una imagen en el sistema de archivos
     * @param filePart El archivo a guardar
     * @param codigoProducto Código único del producto para nombrar el archivo
     * @param directorioBase Directorio base de la aplicación web
     * @return La ruta relativa del archivo guardado
     * @throws IOException Si ocurre un error al guardar el archivo
     */
    public String guardarImagen(Part filePart, String codigoProducto, String directorioBase)
            throws IOException {

        if (!validarImagen(filePart)) {
            throw new IllegalArgumentException("Archivo de imagen inválido");
        }

        // Generar nombre único para el archivo
        String nombreArchivo = generarNombreArchivo(filePart, codigoProducto);

        // Crear directorio si no existe
        Path directorioDestino = crearDirectorioDestino(directorioBase);

        // Guardar archivo
        Path archivoDestino = directorioDestino.resolve(nombreArchivo);
        try (InputStream inputStream = filePart.getInputStream()) {
            Files.copy(inputStream, archivoDestino, StandardCopyOption.REPLACE_EXISTING);
        }

        // Retornar ruta relativa
        return DIRECTORIO_BASE_IMAGENES + "/" + nombreArchivo;
    }

    /**
     * Elimina una imagen del sistema de archivos
     * @param rutaImagen Ruta relativa de la imagen a eliminar
     * @param directorioBase Directorio base de la aplicación web
     * @return true si se eliminó correctamente, false en caso contrario
     */
    public boolean eliminarImagen(String rutaImagen, String directorioBase) {
        if (rutaImagen == null || rutaImagen.equals(IMAGEN_POR_DEFECTO)) {
            return true; // No eliminar imagen por defecto
        }

        try {
            Path archivoAEliminar = Paths.get(directorioBase, rutaImagen);
            return Files.deleteIfExists(archivoAEliminar);
        } catch (IOException e) {
            System.err.println("Error al eliminar imagen: " + rutaImagen + " - " + e.getMessage());
            return false;
        }
    }

    /**
     * Obtiene la ruta de la imagen por defecto
     * @return Ruta de la imagen por defecto
     */
    public String getImagenPorDefecto() {
        return IMAGEN_POR_DEFECTO;
    }

    /**
     * Obtiene el mensaje de error para validación de imágenes
     * @return Mensaje descriptivo de los criterios de validación
     */
    public String getMensajeErrorValidacion() {
        return String.format("La imagen debe ser JPG, PNG o GIF y no exceder %.1f MB",
                TAMAÑO_MAXIMO_ARCHIVO / (1024.0 * 1024.0));
    }

    // Métodos privados auxiliares

    private String generarNombreArchivo(Part filePart, String codigoProducto) {
        String nombreOriginal = filePart.getSubmittedFileName();
        String extension = extraerExtension(nombreOriginal);

        // Sanitizar código de producto para uso como nombre de archivo
        String codigoLimpio = codigoProducto.replaceAll("[^a-zA-Z0-9._-]", "_");

        return codigoLimpio + extension;
    }

    private String extraerExtension(String nombreArchivo) {
        if (nombreArchivo == null || !nombreArchivo.contains(".")) {
            return ".jpg"; // Extensión por defecto
        }
        return nombreArchivo.substring(nombreArchivo.lastIndexOf(".")).toLowerCase();
    }

    private Path crearDirectorioDestino(String directorioBase) throws IOException {
        Path directorio = Paths.get(directorioBase, DIRECTORIO_BASE_IMAGENES);
        if (!Files.exists(directorio)) {
            Files.createDirectories(directorio);
        }
        return directorio;
    }
}