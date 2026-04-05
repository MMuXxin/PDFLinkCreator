package CreatePDFContent.wwwer;

import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.VerticalAlignment;
import lombok.extern.slf4j.Slf4j;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Properties;

@Slf4j
public class MyAddURLs extends ModifyPDF {

    public static int[] addURLfromMap(LinkedHashMap<String, Integer> map, Document document, boolean modified) {
        try {
            Properties props = new Properties();
            try (InputStream input = new FileInputStream("resources/config.properties")) {
                props.load(input);
            } catch (Exception e) {
                log.error("load properties error");
            }
            int startX = Integer.parseInt(props.getProperty("startX"));
            int startY = Integer.parseInt(props.getProperty("startY"));
            int lineHeight = Integer.parseInt(props.getProperty("lineHeight"));
            int fontSize = Integer.parseInt(props.getProperty("fontSize"));
            int pageNumber = Integer.parseInt(props.getProperty("pageNumber"));
            String size = props.getProperty("pageSize");
            PageSize pageSize;

            if ("A4".equals(size)) {
                pageSize = PageSize.A4;
            } else if ("A3".equals(size)) {
                pageSize = PageSize.A3;
            } else if ("A5".equals(size)) {
                pageSize = PageSize.A5;
            } else if ("LETTER".equals(size)) {
                pageSize = PageSize.LETTER;
            } else if ("LEGAL".equals(size)) {
                pageSize = PageSize.LEGAL;
            } else if ("A2".equals(size)) {
                pageSize = PageSize.A2;
            } else {
                pageSize = PageSize.A4; // 默认
            }
            if (modified) {
                return Add.addURLfromMap(map, startX, startY, lineHeight, document, modified, fontSize, pageNumber);
            } else {
                document.getPdfDocument().addNewPage(1, pageSize);
                int height = (int) document.getPdfDocument().getFirstPage().getPageSize().getHeight();
                int width = (int) document.getPdfDocument().getFirstPage().getPageSize().getWidth();
                PdfFont font = Add.getFont(Add.fontPath);

                Paragraph titleParagraph = new Paragraph("目录").setFontSize(fontSize + 30).setFont(font);
                document.showTextAligned(titleParagraph,
                        width / 2,
                        height - 50,
                        1,
                        TextAlignment.CENTER,
                        VerticalAlignment.TOP,
                        0);
                return Add.addURLfromMap(map, startX, startY, lineHeight, document, modified, fontSize, pageNumber);

            }
        } catch (NullPointerException e) {
            log.error("document is null");
            return null;
        } catch (IndexOutOfBoundsException e) {
            log.error("pageNum is out of bounds");
            return null;
        } catch (IllegalArgumentException e) {
            log.error("properties wrong format");
            return null;
        } catch (RuntimeException e) {
            log.error("runtime error");
            return null;
        }

    }


}
