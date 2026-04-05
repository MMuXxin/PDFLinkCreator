package CasioEDicPicConverter.wwwer;

import com.atilika.kuromoji.ipadic.Token;
import com.atilika.kuromoji.ipadic.Tokenizer;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Renamer {
    public static void Renamer(File file){
        List<File> list = firstFirstRenamer(file);
        secondRenamer(list);
        thirdRenamer(file);
    }

    // 检查是否包含中文
    private static boolean hasChinese(String str) {
        return str.codePoints().anyMatch(codepoint ->
                Character.UnicodeScript.of(codepoint) == Character.UnicodeScript.HAN);
    }

    // 检查是否包含英文
    private static boolean hasEnglish(String str) {
        return str.matches(".*[a-zA-Z].*");
    }

    // 检查是否包含日文（平假名、片假名）
    private static boolean hasJapanese(String str) {
        return str.codePoints().anyMatch(codepoint ->
                (codepoint >= 0x3040 && codepoint <= 0x309F) || // 平假名
                        (codepoint >= 0x30A0 && codepoint <= 0x30FF) || // 片假名
                        (codepoint >= 0x4E00 && codepoint <= 0x9FFF && hasChinese(str))); // 日文汉字
    }

    // 日文转罗马字
    private static String japaneseToRoman(String text) {
        Tokenizer tokenizer = new Tokenizer();
        List<Token> tokens = tokenizer.tokenize(text);
        StringBuilder roman = new StringBuilder();
        for (Token token : tokens) {
            String reading = token.getReading();
            if (reading != null && !reading.equals("*")) {
                roman.append(reading.toLowerCase());
            } else {
                roman.append(token.getSurface().toLowerCase());
            }
        }
        return roman.toString();
    }

    // 清理特殊符号，只保留字母、数字、点
    private static String cleanSpecialChars(String str) {
        return str.replaceAll("[^a-zA-Z0-9.]", "");
    }

    // 第一阶段：处理图片文件
    public static List<File> firstFirstRenamer(File directory) {
        List<File> nonChineseFiles = new ArrayList<>();
        File[] files = directory.listFiles();
        if (files == null) return nonChineseFiles;

        for (File file : files) {
            if (file.isFile() && isImageFile(file) &&file.getName().contains("_casio_ver")) {
                String name = FilenameUtils.getBaseName(file.getName());
                String ext = FilenameUtils.getExtension(file.getName());
                String newName = null;

                if (hasChinese(name)) {
                    System.out.println("需手动处理：" + file.getName());
                    continue;
                } else if (hasJapanese(name)) {
                    newName = japaneseToRoman(name);
                } else {
                    newName = name;
                }

                if (newName != null && !newName.equals(name)) {
                    File newFile = new File(file.getParent(), newName + "." + ext);
                    if (file.renameTo(newFile)) {
                        nonChineseFiles.add(newFile);
                    } else {
                        nonChineseFiles.add(file);
                    }
                } else {
                    nonChineseFiles.add(file);
                }
            } else if (file.isDirectory()) {
                nonChineseFiles.addAll(firstFirstRenamer(file));
            }
        }
        return nonChineseFiles;
    }

    // 第二阶段：处理文件名中的空格和特殊符号
    public static void secondRenamer(List<File> files) {
        for (File file : files) {
            if (!file.isFile()) continue;

            String name = FilenameUtils.getBaseName(file.getName());
            String ext = FilenameUtils.getExtension(file.getName());

            String newName = name.replace(" ", "_");
            newName = cleanSpecialChars(newName);

            if (!newName.equals(name)) {
                File newFile = new File(file.getParent(), newName + "." + ext);
                if (file.renameTo(newFile)) {
                    System.out.println(file.getName() + " *** 转换成 *** " + newFile.getName());
                }
            }
        }
    }

    public static void thirdRenamer(File directory) {
        File[] files = directory.listFiles();
        if (files == null) return;

        for (File file : files) {
            if (file.isDirectory()) {
                thirdRenamer(file);
            }
        }

        String folderName = directory.getName();
        String newFolderName = folderName.replace(" ", "_");
        newFolderName = cleanSpecialChars(newFolderName);

        if (!newFolderName.equals(folderName)) {
            File newDir = new File(directory.getParent(), newFolderName);
            if (directory.renameTo(newDir)) {
                thirdRenamer(newDir);
            }
        }
    }

    private static boolean isImageFile(File file) {
        String ext = FilenameUtils.getExtension(file.getName()).toLowerCase();
        return ext.equals("jpg");
    }
}