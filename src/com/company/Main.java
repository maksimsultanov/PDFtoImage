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
        System.out.println("hello world!");
        while (true) {
            var dirName = "./src/PDFtoJPG";
            String[] lsits;
            try (Stream<Path> paths = Files.walk(Paths.get(dirName))) {
                lsits = paths.map(Path::toString).filter(f -> f.endsWith(".pdf")).toArray(String[]::new);
            }
            for (String sourceFile : lsits
            ) {
                try {
                    PDDocument sourceDocument = PDDocument.load(new File(sourceFile));
                    PDFRenderer sourceRender = new PDFRenderer(sourceDocument);
                    int pageIndex = 0;
                    for (PDPage singelePage : sourceDocument.getDocumentCatalog().getPages()) {
                        BufferedImage imagePage = sourceRender.renderImageWithDPI(pageIndex, 300, ImageType.RGB);
                        ImageIO.write(imagePage, "jpg", new File(sourceFile + "-" + (++pageIndex) + ".jpg"));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            Thread.sleep(300000);
        }
    }
}

