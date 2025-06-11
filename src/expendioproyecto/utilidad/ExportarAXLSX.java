/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package expendioproyecto.utilidad;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author uriel
 */
public class ExportarAXLSX {
    public static <T> void exportarAXLSX(File archivo, String tituloHoja, List<T> lista, 
            List<String> encabezados, List<Utilidad.ValorCelda<T>> extractores) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        
        // Crear estilo para los encabezados (negrita)
        CellStyle estiloEncabezado = workbook.createCellStyle();
        Font fuenteEncabezado = workbook.createFont();
        fuenteEncabezado.setBold(true);
        estiloEncabezado.setFont(fuenteEncabezado);
        estiloEncabezado.setAlignment(HorizontalAlignment.CENTER); // Centrar encabezados
        
        // Crear estilo para el contenido (centrado)
        CellStyle estiloContenido = workbook.createCellStyle();
        estiloContenido.setAlignment(HorizontalAlignment.CENTER); // Centrar contenido
        
        Sheet hoja = workbook.createSheet(tituloHoja);

        // Crear fila de encabezados con estilo
        Row filaEncabezado = hoja.createRow(0);
        for (int i = 0; i < encabezados.size(); i++) {
            Cell celda = filaEncabezado.createCell(i);
            celda.setCellValue(encabezados.get(i));
            celda.setCellStyle(estiloEncabezado);
        }

        // Llenar datos con estilo centrado
        for (int i = 0; i < lista.size(); i++) {
            Row fila = hoja.createRow(i + 1);
            T item = lista.get(i);
            for (int j = 0; j < extractores.size(); j++) {
                String valor = extractores.get(j).obtenerValor(item);
                Cell celda = fila.createCell(j);
                celda.setCellValue(valor);
                celda.setCellStyle(estiloContenido); // Aplicar estilo centrado
            }
        }

        // Autoajustar columnas
        for (int i = 0; i < encabezados.size(); i++) {
            hoja.autoSizeColumn(i);
        }

        // Guardar archivo
        try (FileOutputStream fos = new FileOutputStream(archivo)) {
            workbook.write(fos);
        }

        workbook.close();
    }
    
    public static <T, C> void exportarAXLSXConCliente(File archivo, String tituloHoja,
                                                      C cliente,
                                                      List<String> camposCliente,
                                                      List<Utilidad.ValorCelda<C>> extractoresCliente,
                                                      List<T> lista,
                                                      List<String> encabezados,
                                                      List<Utilidad.ValorCelda<T>> extractores) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet hoja = workbook.createSheet(tituloHoja);

        // Estilos
        CellStyle estiloEncabezado = workbook.createCellStyle();
        Font fuenteEncabezado = workbook.createFont();
        fuenteEncabezado.setBold(true);
        estiloEncabezado.setFont(fuenteEncabezado);

        CellStyle estiloContenido = workbook.createCellStyle();
        estiloContenido.setAlignment(HorizontalAlignment.LEFT);

        int filaActual = 0;

        // Insertar datos del cliente
        for (int i = 0; i < camposCliente.size(); i++) {
            Row fila = hoja.createRow(filaActual++);
            Cell celdaCampo = fila.createCell(0);
            celdaCampo.setCellValue(camposCliente.get(i));
            celdaCampo.setCellStyle(estiloEncabezado);

            Cell celdaValor = fila.createCell(1);
            celdaValor.setCellValue(extractoresCliente.get(i).obtenerValor(cliente));
            celdaValor.setCellStyle(estiloContenido);
        }

        filaActual++; // Espacio en blanco

        // Encabezados
        Row filaEncabezado = hoja.createRow(filaActual++);
        for (int i = 0; i < encabezados.size(); i++) {
            Cell celda = filaEncabezado.createCell(i);
            celda.setCellValue(encabezados.get(i));
            celda.setCellStyle(estiloEncabezado);
        }

        // Datos
        for (T item : lista) {
            Row fila = hoja.createRow(filaActual++);
            for (int j = 0; j < extractores.size(); j++) {
                String valor = extractores.get(j).obtenerValor(item);
                Cell celda = fila.createCell(j);
                celda.setCellValue(valor);
                celda.setCellStyle(estiloContenido);
            }
        }

        // Autoajuste
        for (int i = 0; i < encabezados.size(); i++) {
            hoja.autoSizeColumn(i);
        }

        try (FileOutputStream fos = new FileOutputStream(archivo)) {
            workbook.write(fos);
        }

        workbook.close();
    }
}
