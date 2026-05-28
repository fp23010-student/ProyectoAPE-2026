package Clases;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfWriter;
import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class TicketPDF {

    public void generarTicket(int idOrden) {

        String sqlOrden = """
            SELECT o.idOrden, o.fechaHora, o.total,
                   u.nombre, u.apellido,
                   m.numero AS numeroMesa
            FROM orden o
            JOIN usuario u ON o.idUsuario = u.idUsuario
            JOIN mesa    m ON o.idMesa    = m.idMesa
            WHERE o.idOrden = ?
        """;

        String sqlDetalle = """
            SELECT
                COALESCE(p.nombre, c.combo) AS nombre,
                d.cantidad,
                d.precioUnitario,
                (d.cantidad * d.precioUnitario) AS subtotal
            FROM detalleorden d
            LEFT JOIN producto p ON d.idProducto = p.idProducto
            LEFT JOIN combo    c ON d.idCombo    = c.idCombo
            WHERE d.idOrden = ?
            ORDER BY d.idLinea
        """;

        try (
            Connection con = Conexion.conectar();
            PreparedStatement psOrden = con.prepareStatement(sqlOrden);
            PreparedStatement psDet   = con.prepareStatement(sqlDetalle);
        ) {
            psOrden.setInt(1, idOrden);
            ResultSet rsOrden = psOrden.executeQuery();

            String fechaHora    = "";
            String mesero       = "";
            int    numeroMesa   = 0;
            double totalOrden   = 0;

            if (rsOrden.next()) {
                totalOrden  = rsOrden.getDouble("total");
                mesero      = rsOrden.getString("nombre") + " " + rsOrden.getString("apellido");
                numeroMesa  = rsOrden.getInt("numeroMesa");

                Timestamp ts = rsOrden.getTimestamp("fechaHora");
                if (ts != null) {
                    fechaHora = ts.toLocalDateTime()
                            .format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
                }
            }


            String fechaCarpeta = LocalDate.now()
                    .format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));

            File carpeta = new File("documentos" + File.separator + fechaCarpeta);
            if (!carpeta.exists()) {
                carpeta.mkdirs();
            }

            String ruta = carpeta.getAbsolutePath()
                    + File.separator
                    + "ticket_" + idOrden + ".pdf";

            // ── DOCUMENTO PDF ────────────────────────────────────
            Rectangle ticket = new Rectangle(164f, 600f); // 58mm aprox
            Document doc = new Document(ticket, 10, 10, 10, 10);
            PdfWriter.getInstance(doc, new FileOutputStream(ruta));
            doc.open();

            Font titulo = new Font(Font.COURIER, 10, Font.BOLD);
            Font normal = new Font(Font.COURIER,  8, Font.NORMAL);
            Font bold   = new Font(Font.COURIER,  8, Font.BOLD);

            // ── ENCABEZADO ───────────────────────────────────────
            linea(doc, "TACOS NEGOCIO",          titulo, Element.ALIGN_CENTER);
            linea(doc, "Mexican Food",            normal, Element.ALIGN_CENTER);
            linea(doc, "Tel: 0000-0000",          normal, Element.ALIGN_CENTER);
            linea(doc, separador(),               normal, Element.ALIGN_LEFT);

            linea(doc, "ORDEN:  " + idOrden,      bold,   Element.ALIGN_LEFT);
            linea(doc, "FECHA:  " + fechaHora,    normal, Element.ALIGN_LEFT);
            linea(doc, "MESERO: " + mesero,       normal, Element.ALIGN_LEFT);
            linea(doc, "MESA:   " + numeroMesa,   normal, Element.ALIGN_LEFT);
            linea(doc, separador(),               normal, Element.ALIGN_LEFT);

           
            psDet.setInt(1, idOrden);
            ResultSet rsDet = psDet.executeQuery();

            double totalCalculado = 0;

            while (rsDet.next()) {
                String nombre   = rsDet.getString("nombre");
                int    cantidad = rsDet.getInt("cantidad");
                double precio   = rsDet.getDouble("precioUnitario");
                double subtotal = rsDet.getDouble("subtotal");
                totalCalculado += subtotal;

                linea(doc, nombre, bold, Element.ALIGN_LEFT);
                linea(doc,
                    cantidad + " x $" + String.format("%.2f", precio)
                    + "   $" + String.format("%.2f", subtotal),
                    normal, Element.ALIGN_LEFT);
            }

            linea(doc, separador(), normal, Element.ALIGN_LEFT);

            
            Paragraph pTotal = new Paragraph(
                    "TOTAL: $" + String.format("%.2f", totalCalculado), titulo);
            pTotal.setAlignment(Element.ALIGN_RIGHT);
            doc.add(pTotal);

            linea(doc, " ",                     normal, Element.ALIGN_LEFT);
            linea(doc, "Gracias por su visita", normal, Element.ALIGN_CENTER);
            linea(doc, "Vuelva pronto :)",       normal, Element.ALIGN_CENTER);

            doc.close();
            Desktop.getDesktop().open(new File(ruta));
            System.out.println("Ticket generado: " + ruta);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void linea(Document doc, String texto, Font fuente, int alineacion)
            throws DocumentException {
        Paragraph p = new Paragraph(texto, fuente);
        p.setAlignment(alineacion);
        p.setSpacingAfter(2);
        doc.add(p);
    }

    private String separador() {
        return "----------------------";
    }
}