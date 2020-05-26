package com.company;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class Main {

    public static void main(String[] args) throws Exception {
        while (true) {
            //set work directory
            var dirName = "./src/PDFtoJPG";
            String[] lsits;
            // take list of pdf
            try (Stream<Path> paths = Files.walk(Paths.get(dirName))) {
                lsits = paths.map(Path::toString).filter(f -> f.endsWith(".pdf")).toArray(String[]::new);
            }
            //start work with pdf
            for (String sourceFile : lsits
            ) {
                try {
                    //create document
                    PDDocument sourceDocument = PDDocument.load(new File(sourceFile));
                    PDFRenderer sourceRender = new PDFRenderer(sourceDocument);
                    //take index start file name
                    int indexFile = sourceFile.lastIndexOf('/');
                    //create new dir
                    String newDir = sourceFile.substring(0, indexFile) + "/JPG";
                    new File(newDir).mkdir();
                    // set new dir for save jpg files
                    sourceFile = newDir + sourceFile.substring(indexFile);
                    // set page counter
                    int pageIndex = 0;
                    //create jpg
                    for (PDPage singelePage : sourceDocument.getDocumentCatalog().getPages()) {
                        BufferedImage imagePage = sourceRender.renderImageWithDPI(pageIndex, 300, ImageType.RGB);
                        ImageIO.write(imagePage, "jpg", new File(sourceFile + "-" + (++pageIndex) + ".jpg"));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            //create wait timer
            Thread.sleep(300000);
        }
    }
}
