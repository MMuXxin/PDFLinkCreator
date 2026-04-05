package CreatePDFContent.wwwer;

import com.itextpdf.io.font.FontProgram;
import com.itextpdf.io.font.FontProgramFactory;
import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfArray;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.action.PdfAction;
import com.itextpdf.kernel.pdf.annot.PdfLinkAnnotation;
import com.itextpdf.kernel.pdf.navigation.PdfExplicitDestination;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.VerticalAlignment;
import lombok.extern.slf4j.Slf4j;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

@Slf4j
public class Add {
    final static String fontPath = "resources/simhei.ttf";
    private static final Properties PROPS = new Properties();
    //properties 仅load一次
    static {
        try {
            PROPS.load(new FileInputStream("resources/config.properties"));
        } catch (IOException e) {
            log.error("全局加载配置失败");
            System.exit(1);
        }
    }

    //添加一个超链接
    //（“内容” ， 左上角坐标x , 左上角坐标y ， Document , 超链接目标页 ， 字体大小 ， 添加到页码）
    public static void addURL(String text, int x, int y, Document document, int targetPageNum, int fontSize, int pageNum) {

        try {
            String colorStr = PROPS.getProperty("text.color");

            PdfPage firstPage = document.getPdfDocument().getFirstPage();
            float pageHeight = firstPage.getPageSize().getHeight();
            PdfFont currentFont = getFont(fontPath);
            if (currentFont == null) {
                log.error("font load failed, font path: {}", fontPath);
                return;
            }
            Color colorFromProperties = getColorFromProperties(colorStr);
            if (colorFromProperties == null) {
                log.error("color load failed, color property: {}", colorStr);
                return;
            }
            Paragraph paragraph = new Paragraph(text);
            paragraph.setFontSize(fontSize)
                    .setFont(currentFont)
                    .setFontColor(colorFromProperties)
                    .setUnderline();

            float yPosition = pageHeight - y;
            document.showTextAligned(paragraph, x, yPosition, pageNum, TextAlignment.LEFT, VerticalAlignment.TOP, 0);

            float textWidth = currentFont.getWidth(text, fontSize);

            float leftX = x;
            float bottomY = yPosition - (float) fontSize;
            Rectangle linkRect = new Rectangle(leftX, bottomY, textWidth, (float) fontSize);

            PdfLinkAnnotation linkAnnotation = new PdfLinkAnnotation(linkRect);
            linkAnnotation.setBorder(new PdfArray(new float[]{0, 0, 0, 0}));

            PdfExplicitDestination destination = PdfExplicitDestination.createFit(document.getPdfDocument().getPage(targetPageNum));
            PdfAction action = PdfAction.createGoTo(destination);
            linkAnnotation.setAction(action);

            PdfPage currentPage = document.getPdfDocument().getPage(pageNum);
            currentPage.addAnnotation(linkAnnotation);
        } catch (NullPointerException e) {
            log.error("document is null");
        }catch (IndexOutOfBoundsException e) {
            log.error("pageNum is out of bounds");
        } catch (IllegalArgumentException e) {
            log.error("properties wrong format");
        } catch (RuntimeException e) {
            log.error("runtime error");
        }
    }

    public static int[] addURLfromMap(LinkedHashMap<String, Integer> map,
                                      int startX,
                                      int startY,
                                      int lineHeight,
                                      Document document,
                                      boolean modified,
                                      int fontSize,
                                      int pageNum){

        int Y;
        try {
            Y = Integer.parseInt(PROPS.getProperty("startY"));
        } catch (NumberFormatException e) {
            log.error("startY is not an integer, set it to 150");
            Y = 150;
        }
        int x = startX;
        int y = startY;
        int targetpageNum;
        float pageHeight = document.getPdfDocument().getFirstPage().getPageSize().getHeight();
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            String title = entry.getKey();

            targetpageNum = entry.getValue();

            if (y + lineHeight <= pageHeight - 50) {
                addURL(title, x, y, document, targetpageNum, fontSize, pageNum);
                y += lineHeight;
            } else {
                //System.out.println(Y);
                y = Y;
                x += 120;
                addURL(title, x, y, document, targetpageNum, fontSize, pageNum);
                y += lineHeight;
            }
        }
        return new int[]{x, y};
    }

    public static PdfFont getFont(String fontPath) {
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
            //System.out.println("成功加载中文字体：" + fontPath);
            return chineseFont;
        } catch (IOException e) {
            log.error("font load failed, font path: {}", fontPath, e);
        }
        return null;
    }

    public static Color getColorFromProperties(String colorName) {
        if (colorName == null) {
            log.warn("colorName is null, use default BLACK");
            return ColorConstants.BLACK;
        }
        switch (colorName.toLowerCase()) {
            case "blue":
                return ColorConstants.BLUE;
            case "red":
                return ColorConstants.RED;
            case "green":
                return ColorConstants.GREEN;
            case "black":
                return ColorConstants.BLACK;
            // 添加更多预定义颜色
            default:
                return ColorConstants.BLACK; // 默认颜色
        }
    }

}