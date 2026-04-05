package CreatePDFContent.wwwer;

import java.io.File;
import java.io.Serializable;

public class MyPDF implements Serializable {
    private static final long serialVersionUID = 5275L;
    String filename;
    int lastmodifiedX;
    int lastmodifiedY;
    boolean modified;
    File file;

    public MyPDF() {
    }

    public MyPDF(String filename, int lastmodifiedX, int lastmodifiedY, boolean modified, File file) {
        this.filename = filename;
        this.lastmodifiedX = lastmodifiedX;
        this.lastmodifiedY = lastmodifiedY;
        this.modified = modified;
        this.file = file;
    }

    /**
     * 获取
     * @return filename
     */
    public String getFilename() {
        return filename;
    }

    /**
     * 设置
     * @param filename
     */
    public void setFilename(String filename) {
        this.filename = filename;
    }

    /**
     * 获取
     * @return lastmodifiedX
     */
    public int getLastmodifiedX() {
        return lastmodifiedX;
    }

    /**
     * 设置
     * @param lastmodifiedX
     */
    public void setLastmodifiedX(int lastmodifiedX) {
        this.lastmodifiedX = lastmodifiedX;
    }

    /**
     * 获取
     * @return lastmodifiedY
     */
    public int getLastmodifiedY() {
        return lastmodifiedY;
    }

    /**
     * 设置
     * @param lastmodifiedY
     */
    public void setLastmodifiedY(int lastmodifiedY) {
        this.lastmodifiedY = lastmodifiedY;
    }

    /**
     * 获取
     * @return modified
     */
    public boolean isModified() {
        return modified;
    }

    /**
     * 设置
     * @param modified
     */
    public void setModified(boolean modified) {
        this.modified = modified;
    }

    /**
     * 获取
     * @return file
     */
    public File getFile() {
        return file;
    }

    /**
     * 设置
     * @param file
     */
    public void setFile(File file) {
        this.file = file;
    }

    public String toString() {
        return "MyPDF{filename = " + filename + ", lastmodifiedX = " + lastmodifiedX + ", lastmodifiedY = " + lastmodifiedY + ", modified = " + modified + ", file = " + file + "}";
    }
}
