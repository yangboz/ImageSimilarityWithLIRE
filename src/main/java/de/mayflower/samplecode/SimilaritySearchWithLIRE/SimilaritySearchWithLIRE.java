package de.mayflower.samplecode.SimilaritySearchWithLIRE;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

import net.semanticmetadata.lire.builders.DocumentBuilder;
import net.semanticmetadata.lire.builders.GlobalDocumentBuilder;
import net.semanticmetadata.lire.imageanalysis.features.GlobalFeature;
import net.semanticmetadata.lire.imageanalysis.features.global.CEDD;
import net.semanticmetadata.lire.imageanalysis.features.global.FCTH;
import net.semanticmetadata.lire.imageanalysis.features.local.selfsimilarities.SelfSimilaritiesFeature;
import org.apache.lucene.document.Document;

import javax.imageio.ImageIO;
import javax.xml.parsers.DocumentBuilderFactory;

public class SimilaritySearchWithLIRE {

    static class ImageInImageDatabase {

        public String fileName;
        public double[] fcthFeatureVector;
        public double distanceToSearchImage;
    }

    static class ImageComparator implements Comparator<ImageInImageDatabase> {

        @Override
        public int compare(ImageInImageDatabase object1, ImageInImageDatabase object2) {
            if (object1.distanceToSearchImage < object2.distanceToSearchImage) {
                return -1;
            } else if (object1.distanceToSearchImage > object2.distanceToSearchImage) {
                return 1;
            } else {
                return 0;
            }
        }
    }

    public static double[] getFCTHFeatureVector(String fullFilePath) throws FileNotFoundException, IOException {
        BufferedImage bufferedImage = ImageIO.read(new File(fullFilePath));
        GlobalFeature fcth = new FCTH();
        fcth.extract(bufferedImage);
        return fcth.getFeatureVector();
    }

    public static double[] getCEDDFeatureVector(String fullFilePath) throws FileNotFoundException, IOException {
        BufferedImage bufferedImage = ImageIO.read(new File(fullFilePath));
        GlobalFeature cedd = new CEDD();
        cedd.extract(bufferedImage);
        return cedd.getFeatureVector();
    }

    public static void main(String[] args) throws FileNotFoundException, IOException {

//        if (args.length != 2) {
//
//            System.out.println("This application requires two parameters: "
//                    + "the name of a directory containing JPEG images, and a file name of a JPEG image.");
//            return;
//
//        }
        
        String imageDatabaseDirectoryName = "/Users/smartkit/git/image-similarity-with-lire/src/main/resources/notablefaces/";//args[0];
        String searchImageFilePath = "/Users/smartkit/git/image-similarity-with-lire/src/main/resources/notablefaces/3.jpeg";//args[1];

        double[] searchImageFeatureVector = getFCTHFeatureVector(searchImageFilePath);

        System.out.println("Search image FCTH vector: " + Arrays.toString(searchImageFeatureVector));

        ArrayList<ImageInImageDatabase> database = new ArrayList();

        File directory = new File(imageDatabaseDirectoryName);

        FilenameFilter filter = new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return name.endsWith(".jpg") || name.endsWith(".jpeg");
            }
        };

        String[] fileNames = directory.list(filter);

        for (String fileName : fileNames) {

            double[] fcthFeatureVector = getFCTHFeatureVector(imageDatabaseDirectoryName + fileName);
            //euclidDistanceToSearchImage
            double distanceToSearchImage = ImageSimilarity.euclidDistance(fcthFeatureVector, searchImageFeatureVector);
            //cosinedistanceToSearchImage
//            double distanceToSearchImage = ImageSimilarity.cosine(fcthFeatureVector, searchImageFeatureVector);

            ImageInImageDatabase imageInImageDatabase = new ImageInImageDatabase();

            imageInImageDatabase.fileName = fileName;
            imageInImageDatabase.fcthFeatureVector = fcthFeatureVector;
            imageInImageDatabase.distanceToSearchImage = distanceToSearchImage;

            database.add(imageInImageDatabase);

        }

        Collections.sort(database, new ImageComparator());

        for (ImageInImageDatabase result : database) {

            System.out.println("Distance " + Double.toString(result.distanceToSearchImage) + ": " + result.fileName);

        }

    }
}
