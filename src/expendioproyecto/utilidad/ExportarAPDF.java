package expendioproyecto.utilidad;

import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import expendioproyecto.utilidad.Utilidad.ValorCelda;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class ExportarAPDF {
      
    public static <T> void exportarAPDF(
        File archivo,
        String titulo,
        List<T> lista,
        List<String> encabezados,
        List<ValorCelda<T>> extractores,
        Font fontTitulo,
        Font fontCelda,
        boolean incluirPieDePagina
    ) throws IOException, DocumentException {
        
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT-6:00"));
        String fechaHora = sdf.format(new Date());
        
        Document documento = new Document();
        PdfWriter.getInstance(documento, new FileOutputStream(archivo));
        documento.open();

        // Título con fuente personalizada
        Paragraph parrafoTitulo = new Paragraph(titulo, fontTitulo);
        parrafoTitulo.setAlignment(Element.ALIGN_CENTER);
        documento.add(parrafoTitulo);

        documento.add(new Paragraph(" ")); // Espacio en blanco

        PdfPTable tabla = new PdfPTable(encabezados.size());

        // Encabezados sin color de fondo personalizado
        for (String encabezado : encabezados) {
            PdfPCell cell = new PdfPCell(new Phrase(encabezado, fontTitulo));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            tabla.addCell(cell);
        }

        // Filas de datos sin color de fondo
        for (T item : lista) {
            for (ValorCelda<T> extractor : extractores) {
                PdfPCell cell = new PdfPCell(new Phrase(extractor.obtenerValor(item), fontCelda));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell.setPadding(5f);
                tabla.addCell(cell);
            }
        }

        documento.add(tabla);

        // Pie de página (si está habilitado)
        if (incluirPieDePagina) {
            Paragraph piePagina = new Paragraph("Generado el: " + fechaHora, new Font(Font.HELVETICA, 10, Font.ITALIC));
            piePagina.setAlignment(Element.ALIGN_RIGHT);
            documento.add(piePagina);
        }

        documento.close();
    }
    
    public static <T, C> void exportarAPDFConCliente(File archivo, String titulo,
                                                    C cliente, List<String> camposCliente, 
                                                    List<ValorCelda<C>> extractoresCliente,
                                                    List<T> listaProductos,
                                                    List<String> encabezados,
                                                    List<ValorCelda<T>> extractores,
                                                    Font fontTitulo,
                                                    Font fontCelda,
                                                    boolean incluirPieDePagina
    ) throws IOException, DocumentException {
        
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT-6:00"));
        String fechaHora = sdf.format(new Date());
        
        Document documento = new Document();
        PdfWriter.getInstance(documento, new FileOutputStream(archivo));
        documento.open();

        // Título principal
        Paragraph parrafoTitulo = new Paragraph(titulo, fontTitulo);
        parrafoTitulo.setAlignment(Element.ALIGN_CENTER);
        documento.add(parrafoTitulo);
        documento.add(new Paragraph(" ")); // Espacio

        // Datos del cliente
        Paragraph subtituloCliente = new Paragraph("Información del Cliente:", fontTitulo);
        documento.add(subtituloCliente);

        for (int i = 0; i < camposCliente.size(); i++) {
            String campo = camposCliente.get(i);
            String valor = extractoresCliente.get(i).obtenerValor(cliente);
            documento.add(new Paragraph(campo + ": " + valor, fontCelda));
        }

        documento.add(new Paragraph(" ")); // Espacio

        // Tabla de productos
        PdfPTable tabla = new PdfPTable(encabezados.size());
        for (String encabezado : encabezados) {
            PdfPCell cell = new PdfPCell(new Phrase(encabezado, fontTitulo));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            tabla.addCell(cell);
        }

        for (T item : listaProductos) {
            for (ValorCelda<T> extractor : extractores) {
                PdfPCell cell = new PdfPCell(new Phrase(extractor.obtenerValor(item), fontCelda));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell.setPadding(5f);
                tabla.addCell(cell);
            }
        }

        documento.add(tabla);

        if (incluirPieDePagina) {
            Paragraph piePagina = new Paragraph("Generado el: " + fechaHora, new Font(Font.HELVETICA, 10, Font.ITALIC));
            piePagina.setAlignment(Element.ALIGN_RIGHT);
            documento.add(piePagina);
        }

        documento.close();
    }
}
