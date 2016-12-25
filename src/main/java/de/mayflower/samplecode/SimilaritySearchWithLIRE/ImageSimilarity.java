package de.mayflower.samplecode.SimilaritySearchWithLIRE;

import org.opencv.core.Mat;

import static org.opencv.core.Core.NORM_L2;
import static org.opencv.core.Core.norm;

/**
 * Created by smartkit on 2016/12/22.
 * @see: https://en.wikipedia.org/wiki/Cosine_similarity
 */
public class ImageSimilarity {

    public static float cosine(double[] vectorA, double[] vectorB) {
        double dotProduct = 0.0;
        double normA = 0.0;
        double normB = 0.0;
        for (int i = 0; i < vectorA.length; i++) {
            dotProduct += vectorA[i] * vectorB[i];
            normA += Math.pow(vectorA[i], 2);
            normB += Math.pow(vectorB[i], 2);
        }
        return (float) (dotProduct / (Math.sqrt(normA) * Math.sqrt(normB)));
    }

    public static double euclidDistance(double[] vector1, double[] vector2) {
        double innerSum = 0.0;
        for (int i = 0; i < vector1.length; i++) {
            innerSum += Math.pow(vector1[i] - vector2[i], 2.0);
        }
        return Math.sqrt(innerSum);
    }
}
