package expendioproyecto.utilidad;

import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import expendioproyecto.utilidad.Utilidad.ValorCelda;
import java.awt.Color;

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
            boolean incluirPieDePagina,
            boolean resaltarPrimeraFila
        ) throws IOException, DocumentException {

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT-6:00"));
        String fechaHora = sdf.format(new Date());

        Document documento = new Document();
        PdfWriter.getInstance(documento, new FileOutputStream(archivo));
        documento.open();

        // Título
        Paragraph parrafoTitulo = new Paragraph(titulo, fontTitulo);
        parrafoTitulo.setAlignment(Element.ALIGN_CENTER);
        documento.add(parrafoTitulo);
        documento.add(new Paragraph(" ")); // Espacio

        PdfPTable tabla = new PdfPTable(encabezados.size());

        // Encabezados
        for (String encabezado : encabezados) {
            PdfPCell cell = new PdfPCell(new Phrase(encabezado, fontTitulo));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            tabla.addCell(cell);
        }

        boolean esPrimeraFila = true;
        for (T item : lista) {
            for (ValorCelda<T> extractor : extractores) {
                PdfPCell cell = new PdfPCell(new Phrase(extractor.obtenerValor(item), fontCelda));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell.setPadding(5f);
                if (resaltarPrimeraFila && esPrimeraFila) {
                    cell.setBackgroundColor(Color.YELLOW); // Resalta si se indica
                }
                tabla.addCell(cell);
            }
            esPrimeraFila = false;
        }

        documento.add(tabla);

        // Pie de página
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
                                                 boolean incluirPieDePagina,
                                                 boolean resaltarPrimeraFila 
        ) throws IOException, DocumentException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT-6:00"));
        String fechaHora = sdf.format(new Date());

        Document documento = new Document();
        PdfWriter.getInstance(documento, new FileOutputStream(archivo));
        documento.open();

        Paragraph parrafoTitulo = new Paragraph(titulo, fontTitulo);
        parrafoTitulo.setAlignment(Element.ALIGN_CENTER);
        documento.add(parrafoTitulo);
        documento.add(new Paragraph(" "));

        Paragraph subtituloCliente = new Paragraph("Información del Cliente:", fontTitulo);
        documento.add(subtituloCliente);

        for (int i = 0; i < camposCliente.size(); i++) {
            String campo = camposCliente.get(i);
            String valor = extractoresCliente.get(i).obtenerValor(cliente);
            documento.add(new Paragraph(campo + ": " + valor, fontCelda));
        }

        documento.add(new Paragraph(" "));

        PdfPTable tabla = new PdfPTable(encabezados.size());
        for (String encabezado : encabezados) {
            PdfPCell cell = new PdfPCell(new Phrase(encabezado, fontTitulo));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            tabla.addCell(cell);
        }

        boolean esPrimeraFila = true;
        for (T item : listaProductos) {
            for (ValorCelda<T> extractor : extractores) {
                PdfPCell cell = new PdfPCell(new Phrase(extractor.obtenerValor(item), fontCelda));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell.setPadding(5f);
                if (esPrimeraFila && resaltarPrimeraFila) {
                    cell.setBackgroundColor(Color.YELLOW);
                }
                tabla.addCell(cell);
            }
            esPrimeraFila = false;
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
