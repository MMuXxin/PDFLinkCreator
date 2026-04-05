package CreatePDFContent;

import CreatePDFContent.wwwer.MyAddURLs;
import CreatePDFContent.wwwer.Launch;


import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        Launch.launch();




    }
    @Deprecated
    private static void method() throws IOException, ClassNotFoundException {
        File file2 = new File("D:\\test\\[燦々SUN] 時々ボソッとロシア語でデレる隣のアーリャさん 第01巻_linked.pdf");
        File file = new File("D:\\test\\[燦々SUN] 時々ボソッとロシア語でデレる隣のアーリャさん 第01巻.pdf");
        File file3 =new File("D:\\test\\[閃凡人] 聖なる乙女と秘めごとを 第09巻.pdf");
        File file4 =new File("D:\\test\\[閃凡人] 聖なる乙女と秘めごとを 第09巻_linked.pdf");
        Scanner sc = new Scanner(System.in);
        while (true) {
            int s = sc.nextInt();
            if (s==0) {
                break;
            }else if (s==2) {
                //linked pdf
                MyAddURLs.modifyPDF(file2);
                MyAddURLs.modifyPDF(file4);
            }else if (s==1) {
                //unlinked pdfZ
                MyAddURLs.modifyPDF(file);
                MyAddURLs.modifyPDF(file3);
            }
        }
        //System.out.println(file2.delete());
    }
}
