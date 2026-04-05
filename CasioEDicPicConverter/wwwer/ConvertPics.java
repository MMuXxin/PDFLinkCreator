package CasioEDicPicConverter.wwwer;

import MangaToPDF.wwwer.Finals;

import java.io.File;
import java.io.IOException;
@Deprecated

public class ConvertPics {
    public static void ConvertPics(File src) throws IOException {
        /*String[] extensions = {"jpg", "jpeg", "png", "bmp", "gif", "wbmp"};

        if (src.isFile()) {
            String fileName = src.getName().toLowerCase();
            boolean isSupported = false;
            for (String ext : extensions) {
                if (fileName.endsWith("." + ext)) {
                    ConvertOnePic.ConvertOnePic(src);
                    isSupported = true;
                    break;
                }
            }
            if (!isSupported) {
                System.out.println(src.getPath() + " : 格式错误" +
                        "\n支持格式: \"jpg\", \"jpeg\", \"png\", \"bmp\", \"gif\", \"wbmp\"");
            }
        } else if (src.isDirectory()) {
            File[] files = src.listFiles();
            if (files != null) {
                for (File file : files) {
                    ConvertPics(file);
                }
            }
        }*/
    }
}
