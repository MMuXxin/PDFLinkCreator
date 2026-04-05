package CasioEDicPicConverter.wwwer;

import com.formdev.flatlaf.FlatDarkLaf;

import javax.swing.*;
import java.io.File;
import java.util.Scanner;

public class Launch {
    static {
        try {
            FlatDarkLaf.setup();

            UIManager.put("Component.arc", 8);
            UIManager.put("Button.arc", 8);
            UIManager.put("TextComponent.arc", 8);

            UIManager.put("Button.paintShadow", true);
            UIManager.put("Component.focusWidth", 2);
            UIManager.put("Component.hoverBorderWidth", 1);

            UIManager.put("FileChooserUI", "com.formdev.flatlaf.ui.FlatFileChooserUI");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static File currentFile = null;
    private static Scanner sc = new Scanner(System.in);
    private static File file = new File("D:\\CASIO");
    public static void launch() {
        SwingUtilities.invokeLater(() -> {
            System.out.println("===Casio可识别图片转换器===");

            while (true) {
                if (currentFile == null) {
                    System.out.println("请选择图片文件夹");
                    currentFile = selectFile();
                }
                if(currentFile == null) {
                    System.exit(0);
                }
                System.out.println("\n当前文件: " + currentFile.getName());
                System.out.println("0:退出  1:处理 2:重新选择");
                int a = sc.nextInt();
                switch (a) {
                    case 0 -> System.exit(0);
                    case 1 -> {
                        Converter.ConvertPics(currentFile);
                        //这里需要完善
                        File dest = new File("D:\\CASIO");


                        Mover.move(currentFile, dest);
                        Renamer.Renamer(dest);
                    }
                    case 2 -> {
                        currentFile = null;
                    }
                }
            }
        });
    }
    public static File selectFile(){
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File("D:\\test"));
        fileChooser.setDialogTitle("选择图片文件夹");
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int result = fileChooser.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            return fileChooser.getSelectedFile();
        }
        return null;
    }


}
