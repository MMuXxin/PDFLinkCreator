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


> ⚠️ iText 9.5.0 may not be available in maven
    
    
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

