package CreatePDFContent.wwwer.discarded;

import com.itextpdf.io.font.FontProgram;
import com.itextpdf.io.font.FontProgramFactory;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.*;
import com.itextpdf.kernel.pdf.action.PdfAction;
import com.itextpdf.kernel.pdf.annot.PdfLinkAnnotation;
import com.itextpdf.kernel.pdf.navigation.PdfExplicitDestination;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.VerticalAlignment;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
@Slf4j
@Deprecated
public class CreateURL {

    private static PdfFont chinesefront;

    static {
        try {
            chinesefront = getFront(Finals.fontPath);
        } catch (IOException e) {
            log.error("加载中文字体失败: {}", Finals.fontPath, e);
            e.printStackTrace();
        }
    }

    public CreateURL()  {
    }

    public static PdfDocument getPdfDocument(File pdffile) throws IOException {
        try {
            File tempFile = new File(pdffile.getAbsolutePath().replaceFirst("\\.pdf$", "_temp.pdf"));
            FileUtils.copyFile(pdffile, tempFile);
            File targetFile = new File(pdffile.getAbsolutePath().replaceFirst("\\.pdf$", "_linked.pdf"));
            return new PdfDocument(new PdfReader(tempFile), new PdfWriter(targetFile));
        } catch (IOException e) {
            log.error("获取PdfDocument失败, 源文件: {}", pdffile.getAbsolutePath(), e);
            throw new RuntimeException(e);
        }
    }

    public static Document addOnePage(PdfDocument pdfDoc){
        try {
            pdfDoc.addNewPage(1);
            PageSize pageSize = pdfDoc.getDefaultPageSize();
            Document document = new Document(pdfDoc, pageSize);
            return document;
        } catch (Exception e) {
            log.error("添加目录页失败", e);
            throw new RuntimeException(e);
        }
    }

    public static PdfFont getFront(String fontPath) throws IOException {
        try {
            PdfFont chineseFont;
            FontProgram fontProgram = FontProgramFactory.createFont(fontPath);
            /*
             * 编码方式	                说明	                        适用场景
             * Identity-H	            Unicode直接映射，支持所有字符	中文字体、多语言字体
             * StandardFonts.HELVETICA	内置英文字体编码	            纯英文内容
             * "Cp1252"	                Windows Latin-1编码	        西欧语言
             * "UnicodeBigUnmarked"	    标准Unicode编码	            需要明确指定编码时
             */
            chineseFont = PdfFontFactory.createFont(fontProgram, "Identity-H");
            System.out.println("成功加载中文字体：" + fontPath);
            return chineseFont;
        } catch (IOException e) {
            log.error("加载字体失败, 字体路径: {}", fontPath, e);
            throw e;
        }
    }

    public static Document createAndAddContext(Document document) throws IOException {
        try {
            PdfPage newPage = document.getPdfDocument().getFirstPage();
            float pageWidth = newPage.getPageSize().getWidth();
            float pageHeight = newPage.getPageSize().getHeight();
            Paragraph titleParagraph = new Paragraph("目 录")
                    .setFontSize(24)
                    .setFont(chinesefront);
            document.showTextAligned(titleParagraph, pageWidth / 2, pageHeight - 50,
                    1, TextAlignment.CENTER, VerticalAlignment.TOP, 0);
            return document;
        } catch (Exception e) {
            log.error("创建目录内容失败", e);
            throw new RuntimeException(e);
        }
    }

    public static void createAndAddURL(Document document, LinkedHashMap<String,Integer> map) throws IOException {
        try {
            PdfPage firstPage = document.getPdfDocument().getFirstPage();
            float pageWidth = firstPage.getPageSize().getWidth();
            float pageHeight = firstPage.getPageSize().getHeight();
            float yStart = pageHeight - 100;
            float lineHeight = 25;
            int index = 0;
            for (Map.Entry<String, Integer> entry : map.entrySet()) {
                String title = entry.getKey();
                int targetPage = entry.getValue()+1;
                float yPosition = yStart - (index * lineHeight);
                Paragraph paragraph = new Paragraph(title + " ...... "+targetPage)
                        .setFontSize(12)
                        .setFont(chinesefront)
                        .setFontColor(ColorConstants.BLUE);

                document.showTextAligned(paragraph, pageWidth / 2, yPosition, 1,
                        TextAlignment.CENTER, VerticalAlignment.TOP, 0);

                float fontSize = 12;
                String fullText = title + " ...... " + targetPage;  // 完整文本内容
                float textWidth = chinesefront.getWidth(fullText, fontSize);
                float textHeight = fontSize;

                // 计算矩形边界（因为文本居中对齐）
                float leftX = pageWidth / 2 - textWidth / 2;     // 矩形左下角X坐标
                float bottomY = yPosition - textHeight;          // 矩形左下角Y坐标

                // 创建精确覆盖的矩形
                Rectangle linkRect = new Rectangle(leftX, bottomY, textWidth, textHeight);

                // PdfLinkAnnotation: PDF链接注释对象，用于创建可点击的链接区域
                PdfLinkAnnotation linkAnnotation = new PdfLinkAnnotation(linkRect);

                // 设置链接边框样式
                // PdfArray: PDF数组对象，[0, 0, 0, 0]表示边框宽度为0，即无边框
                linkAnnotation.setBorder(new PdfArray(new float[]{0, 0, 0, 0}));

                // PdfExplicitDestination: 明确的跳转目标
                // createFit(): 创建"适合窗口"的跳转方式，跳转到目标页面并调整视图使整页可见
                // pdfDoc.getPage(targetPage): 获取目标页码的页面对象
                PdfExplicitDestination destination = PdfExplicitDestination.createFit(document.getPdfDocument().getPage(targetPage));

                // PdfAction: PDF动作对象，定义点击链接后的行为
                // createGoTo(): 创建跳转到指定目标位置的动作
                PdfAction action = PdfAction.createGoTo(destination);

                // 将动作设置到链接注释上，这样点击该区域就会执行跳转动作
                linkAnnotation.setAction(action);

                // 获取当前目录页（也就是文档的最后一页，因为我们刚添加的目录页）
                PdfPage currentPage = document.getPdfDocument().getPage(1);
                // 将链接注释添加到当前页面，用户点击该区域时会触发跳转
                currentPage.addAnnotation(linkAnnotation);

                index++;
            }
        } catch (Exception e) {
            log.error("创建目录链接失败", e);
            throw new RuntimeException(e);
        }
    }

}
