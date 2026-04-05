package CreatePDFContent.wwwer;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.util.*;

@Slf4j
public class ModifyPDF {

    private static final Properties PROPS = new Properties();
    //properties 仅load一次
    static {
        try {
            PROPS.load(new FileInputStream("resources/config.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void modifyPDF(File file) throws IOException, ClassNotFoundException {
        boolean modified = Modified(file);
        //复制一份，处理复制的那份
        String basePath = file.getAbsolutePath().replaceFirst("\\.pdf$", "");
        File tempFile = new File(basePath + "_temp.pdf");
        File targetFile = modified ? file : new File(basePath + "_linked.pdf");
        MyPDF myPDF = new MyPDF(file.getName(), 0, 0, modified, file);
        ObjectInputStream ois;
        ObjectOutputStream oos;
        LinkedHashMap<String, Integer> map = getMap(modified);

        if (!modified) {
            firstModify(file, tempFile, targetFile, map, false, myPDF);
            log.info(file.getName() + " firstModified");
        } else {
            String stateFilePath = "src/CreatePDFContent/wwwer/states/" +
                    myPDF.getFilename().replace("_linked", "") + ".ser";
            File stateFile = new File(stateFilePath);
            if (!stateFile.exists()) {
                firstModify(file, tempFile, targetFile, map, false, myPDF);
                log.info(file.getName() + " firstModified");
            } else {
                ois = new ObjectInputStream(new FileInputStream("src/CreatePDFContent/wwwer/states/" + myPDF.getFilename().replace("_linked", "") + ".ser"));
                myPDF = (MyPDF) ois.readObject();
                int lastmodifiedX = myPDF.getLastmodifiedX();
                int lastmodifiedY = myPDF.getLastmodifiedY();
                FileUtils.copyFile(file, tempFile);
                PdfDocument pdfDocument = new PdfDocument(new PdfReader(tempFile), new PdfWriter(targetFile));
                Document document = new Document(pdfDocument);
                int[] ints = Add.addURLfromMap(map,
                        lastmodifiedX,
                        lastmodifiedY,
                        Integer.parseInt(PROPS.getProperty("lineHeight")),
                        document,
                        modified,
                        Integer.parseInt(PROPS.getProperty("fontSize")),
                        Integer.parseInt(PROPS.getProperty("pageNumber"))
                );
                myPDF.setLastmodifiedX(ints[0]);
                myPDF.setLastmodifiedY(ints[1]);
                myPDF.setModified(true);
                oos = new ObjectOutputStream(new FileOutputStream("src/CreatePDFContent/wwwer/states/" + myPDF.getFilename().replace("_linked", "") + ".ser"));
                oos.writeObject(myPDF);
                oos.close();
                document.close();
                pdfDocument.close();
                tempFile.delete();
                log.info(file.getName() + "Modified");
            }
        }

    }
    //首次处理
    private static void firstModify(File file, File tempFile, File targetFile, LinkedHashMap<String, Integer> map, boolean modified, MyPDF myPDF) throws IOException {
        ObjectOutputStream oos;
        FileUtils.copyFile(file, tempFile);
        PdfDocument pdfDocument = new PdfDocument(new PdfReader(tempFile), new PdfWriter(targetFile));
        Document document = new Document(pdfDocument);
        int[] ints = MyAddURLs.addURLfromMap(map, document, modified);
        document.close();
        pdfDocument.close();
        tempFile.delete();
        myPDF.setLastmodifiedX(ints[0]);
        myPDF.setLastmodifiedY(ints[1]);
        myPDF.setModified(true);
        String stateFileName = myPDF.getFilename().replace("_linked", "");
        oos = new ObjectOutputStream(new FileOutputStream(
                "src/CreatePDFContent/wwwer/states/" + stateFileName + ".ser"
        ));
        oos.writeObject(myPDF);
        oos.close();
    }

    private static LinkedHashMap<String, Integer> getMap(boolean modified) {
        Map<String, Integer> map = new HashMap<>();
        BufferedReader bufferedReader;
        try {
            bufferedReader = new BufferedReader(new FileReader("resources/inputmap.txt"));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] parts = line.split("@");
                String title = parts[0];
                int pageNum;
                if (modified) {
                    pageNum = Integer.parseInt(parts[1]);
                }else {
                    //首次添加页码需要+1
                    pageNum = Integer.parseInt(parts[1])+1;
                }
                map.put(title, pageNum);
            }
        } catch (Exception e) {
            log.error("map format error or map file error:"+e.getMessage());
        }
        List<Map.Entry<String, Integer>> list = new ArrayList<>(map.entrySet());
        list.sort(Map.Entry.comparingByValue());
        LinkedHashMap<String, Integer> sortedMap = new LinkedHashMap<>();
        for (Map.Entry<String, Integer> entry : list) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }
        return sortedMap;
    }

    private static Boolean Modified(File file) {
        if (file.getName().contains("_linked")) {
            return true;
        }
        return false;
    }


}
