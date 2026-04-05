package CasioEDicPicConverter.wwwer;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.*;

@Slf4j
public class Converter {
    public static void ConvertPics(List<File> pics) {
        BlockingQueue<File> queue = new LinkedBlockingQueue<>(pics);
        ExecutorService executor = Executors.newFixedThreadPool(8);
        for (int i = 0; i < 8; i++) {
            executor.submit(() -> {
                File image;
                while ((image = queue.poll()) != null) {  // poll 是原子操作，不会重复
                    try {
                        ConvertOnePic.ConvertOnePic(image);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        executor.shutdown();
        try {
            executor.awaitTermination(1, TimeUnit.HOURS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void ConvertPics(File file) {
        List<File> list = getList(file);
        ConvertPics(list);
    }




    public static List<File> getList(File src) {
        List<File> list = new java.util.ArrayList<>();
        if (src.isFile()) {
            if (isStandardPic(src)) {
                list.add(src);
            }
        } else if (src.isDirectory()) {
            File[] files = src.listFiles();
            if (files != null) {
                for (File file : files) {
                    list.addAll(getList(file));
                }
            }
        }
        return list;
    }

    public static boolean isStandardPic(File src) {
        String extension = getExtension(src).toLowerCase();
        if (extension.equals("jpg") ||
                extension.equals("jpeg") ||
                extension.equals("png") ||
                extension.equals("bmp") ||
                extension.equals("wbmp")) {
            return true;
        }
        return false;
    }

    public static String getExtension(File src) {
        return FilenameUtils.getExtension(src.getName());
    }
}
