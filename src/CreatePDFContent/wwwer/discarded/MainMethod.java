package CreatePDFContent.wwwer.discarded;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.layout.Document;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
@Slf4j
@Deprecated
public class MainMethod {

    public static void method(File file, LinkedHashMap<String,Integer>map) {
        try {
            PdfDocument pdfDocument = CreateURL.getPdfDocument(file);
            Document document = CreateURL.addOnePage(pdfDocument);
            Document document1 = CreateURL.createAndAddContext(document);
            CreateURL.createAndAddURL(document1,map);
            document1.close();
            pdfDocument.close();
            File tempFile = new File(file.getAbsolutePath().replaceFirst("\\.pdf$", "_temp.pdf"));
            tempFile.delete();
            log.info("PDF目录生成成功: {}", file.getAbsolutePath());

        } catch (IOException e) {
            log.error("PDF目录处理失败, 文件: {}", file.getAbsolutePath(), e);
            throw new RuntimeException(e);
        }
    }
}
