package CasioEDicPicConverter.wwwer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class Mover {
    public static void move(File src, File dest) {
        move(src, dest, src);
    }

    private static void move(File src, File dest, File rootSrc) {
        if (src.isDirectory()) {
            File[] files = src.listFiles();
            if (files != null) {
                for (File file : files) {
                    move(file, dest, rootSrc);
                }
            }
        } else if (src.isFile()) {
            if (src.getName().contains("_casio_ver")) {
                String relativePath = rootSrc.toPath().relativize(src.toPath()).toString();
                File targetFile = new File(dest, relativePath);
                if (targetFile.exists()) {
                    return;
                }
                targetFile.getParentFile().mkdirs();
                try {
                    Files.move(src.toPath(), targetFile.toPath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}