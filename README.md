# PDF HyperaLink Creator

A Java tool to automatically generate clickable table of contents for PDF files. Supports incremental updates and state persistence.

## Features

   1.Automatically generate table of contents page <br>
   2.Add clickable hyperlinks to each chapter <br>
   3.Support Chinese fonts (SimHei) <br>
   4.Incremental modifications with state persistence <br>
   5.Configurable layout (position, font size, colors) <br>
   6.GUI file selector with dark theme <br>
   7.Resume from last position for large PDFs <br>

## How to use
   1.Add dependencies <br> 
   
    iText 7 Kernel
    iText 7 Layout  
    iText 7 IO
    Apache Commons IO
    FlatLaf
    FlatLaf IntelliJ Themes
    Lombok
    SLF4J API
    Logback Core
    Logback Classic

   maven:
               <?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 
         http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.wwwer</groupId>
    <artifactId>PDFLinkCreator</artifactId>
    <version>1.0</version>

    <properties>
        <maven.compiler.source>11</maven.compiler.source>
        <maven.compiler.target>11</maven.compiler.target>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    </properties>

    <dependencies>
        <dependency>
            <groupId>com.itextpdf</groupId>
            <artifactId>kernel</artifactId>
            <version>9.5.0</version>
        </dependency>
        <dependency>
            <groupId>com.itextpdf</groupId>
            <artifactId>layout</artifactId>
            <version>9.5.0</version>
        </dependency>
        <dependency>
            <groupId>com.itextpdf</groupId>
            <artifactId>io</artifactId>
            <version>9.5.0</version>
        </dependency>
        <dependency>
            <groupId>commons-io</groupId>
            <artifactId>commons-io</artifactId>
            <version>2.21.0</version>
        </dependency>
        <dependency>
            <groupId>com.formdev</groupId>
            <artifactId>flatlaf</artifactId>
            <version>3.7.1</version>
        </dependency>
        <dependency>
            <groupId>com.formdev</groupId>
            <artifactId>flatlaf-intellij-themes</artifactId>
            <version>3.7.1</version>
        </dependency>
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <version>1.18.36</version>
            <scope>provided</scope>
        </dependency>
        <dependency>
            <groupId>ch.qos.logback</groupId>
            <artifactId>logback-classic</artifactId>
            <version>1.4.14</version>
        </dependency>
        <dependency>
            <groupId>org.slf4j</groupId>
            <artifactId>slf4j-api</artifactId>
            <version>2.0.17</version>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <version>3.13.0</version>
                <configuration>
                    <source>11</source>
                    <target>11</target>
                    <encoding>UTF-8</encoding>
                </configuration>
            </plugin>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-jar-plugin</artifactId>
                <version>3.4.2</version>
                <configuration>
                    <archive>
                        <manifest>
                            <mainClass>com.wwwer.Launch</mainClass>
                        </manifest>
                    </archive>
                </configuration>
            </plugin>
        </plugins>
    </build>
</project>

    
    
   2.Configure input mapping <br>
     Edit resources/inputmap.txt: <br>

    Chapter 1@5 
    Chapter 2@12 
    Chapter 3@20 

   3.Configure settings if you like <br>
     Edit resources/config.properties <br>



## Structures

    PDFLinkCreator/ <br>
    ├── src/CreatePDFContent/wwwer/ <br>
    │ ├── Add.java                 # Core link addition logic <br>
    │ ├── Launch.java              # GUI launcher <br>
    │ ├── ModifyPDF.java           # Main modification logic <br>
    │ ├── MyAddURLs.java           # URL batch processor <br>
    │ ├── MyPDF.java               # PDF state model <br>
    │ └── states/ # Persistence directory <br>
    ├── resources/ <br>
    │ ├── config.properties        # Configuration file <br>
    │ ├── inputmap.txt             # Chapter-page mapping <br>
    │ └── simhei.ttf               # Chinese font <br>
    └── lib/                       # Dependencies <br>


## License

   This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details. <br>

