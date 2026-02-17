import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import java.io.FileOutputStream;

public class PDFUtil {

    public static void export(String result, String a, String b) {
        try {
            Document doc = new Document();
            PdfWriter.getInstance(doc, new FileOutputStream("Report.pdf"));
            doc.open();

            doc.add(new Paragraph("Plagiarism Report"));
            doc.add(new Paragraph(result));
            doc.add(new Paragraph("\nFile A:\n" + a));
            doc.add(new Paragraph("\nFile B:\n" + b));

            doc.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


