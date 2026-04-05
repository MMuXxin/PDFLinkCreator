package CreatePDFContent.wwwer;


import com.formdev.flatlaf.FlatDarkLaf;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.util.Scanner;

public class Launch {
    static {
        try {
            FlatDarkLaf.setup();

            UIManager.put("Component.arc", 8);
            UIManager.put("Button.arc", 8);
            UIManager.put("TextComponent.arc", 8);
            UIManager.put("FileChooserUI", "com.formdev.flatlaf.ui.FlatFileChooserUI");

            UIManager.put("Button.paintShadow", true);
            UIManager.put("Component.focusWidth", 2);
            UIManager.put("Component.hoverBorderWidth", 1);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private static File currentFile = null;
    private static File linkedFile = null;
    private static File mapFile = new File("resources/inputmap.txt");
    private static Scanner sc = new Scanner(System.in);

    public static void launch() {
        SwingUtilities.invokeLater(() -> {

            System.out.println("===== PDF目录生成器 =====");

            while (true) {
                // 1. 选择文件
                if (currentFile == null) {
                    System.out.println("\n请选择PDF文件...");
                    currentFile = selectPDFFile();
                    if (currentFile == null) {
                        System.out.println("未选择文件，退出");
                        break;
                    }

                    String path = currentFile.getAbsolutePath().replaceFirst("\\.pdf$", "_linked.pdf");
                    linkedFile = new File(path);
                }

                System.out.println("\n当前文件: " + currentFile.getName());
                System.out.println("0:退出  1:处理  2:编辑目录  3:换书");
                int a = sc.nextInt();
                sc.nextLine();

                if (a == 0) {
                    System.exit(0);

                } else if (a == 1) {
                    try {
                        ModifyPDF.modifyPDF(currentFile);
                        if (linkedFile.exists()) {
                            currentFile = linkedFile;
                            System.out.println("已切换到: " + currentFile.getName());
                        }
                    } catch (Exception e) {
                        System.err.println("处理失败: " + e.getMessage());
                    }

                } else if (a == 2) {
                    try {
                        Desktop.getDesktop().open(mapFile);
                        System.out.println("编辑完成后按回车继续...");
                        sc.nextLine();
                    } catch (Exception e) {
                        System.err.println("无法打开目录文件");
                    }

                } else if (a == 3) {
                    currentFile = null;
                    linkedFile = null;
                    System.out.println("已重置，请重新选择文件");
                }
            }

            sc.close();
        });
    }

    public static File selectPDFFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("PDF文件 (*.pdf)", "pdf"));
        fileChooser.setDialogTitle("选择PDF文件");


        int result = fileChooser.showOpenDialog(null);

        if (result == JFileChooser.APPROVE_OPTION) {
            return fileChooser.getSelectedFile();
        }
        return null;
    }
}