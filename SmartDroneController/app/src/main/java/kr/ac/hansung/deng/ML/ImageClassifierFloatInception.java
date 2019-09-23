package kr.ac.hansung.deng.ML;

import android.app.Activity;

import java.io.IOException;

public class ImageClassifierFloatInception extends ImageClassifier {

    /**
     * The inception net requires additional normalization of the used input.
     */
    private static final int IMAGE_MEAN = 128;
    private static final float IMAGE_STD = 128.0f;

    /**
     * An array to hold inference results, to be feed into Tensorflow Lite as outputs.
     * This isn't part of the super class, because we need a primitive array here.
     */
    private float[][] labelProbArray = null;

    /**
     * Initializes an {@code ImageClassifier}.
     *
     * @param activity
     */
    public ImageClassifierFloatInception(Activity activity) throws IOException {
        super(activity);
        labelProbArray = new float[1][getNumLabels()];
    }

    @Override
    public String getModelPath() {
        // you can download this file from
        // https://storage.googleapis.com/download.tensorflow.org/models/tflite/inception_v3_slim_2016_android_2017_11_10.zip
        return "dsls_graph.tflite";
        //return "inceptionv3_slim_2016.tflite";
    }

    @Override
    public String getLabelPath() {
        return "dsls_labels.txt";
        //return "labels_imagenet_slim.txt";
    }



    @Override
    public int getImageSizeX() {
        return 299;
    }

    @Override
    public int getImageSizeY() {
        return 299;
    }

    @Override
    public int getNumBytesPerChannel() {
        // a 32bit float value requires 4 bytes
        return 4;
    }

    @Override
    public void addPixelValue(int pixelValue) {
        imgData.putFloat((((pixelValue >> 16) & 0xFF) - IMAGE_MEAN) / IMAGE_STD);
        imgData.putFloat((((pixelValue >> 8) & 0xFF) - IMAGE_MEAN) / IMAGE_STD);
        imgData.putFloat(((pixelValue & 0xFF) - IMAGE_MEAN) / IMAGE_STD);
        //Log.i("add", imgData.toString());
    }

    @Override
    public float getProbability(int labelIndex) {
        return labelProbArray[0][labelIndex];
    }

    @Override
    public void setProbability(int labelIndex, Number value) {
        labelProbArray[0][labelIndex] = value.floatValue();
    }

    @Override
    public float getNormalizedProbability(int labelIndex) {
        // TODO the following value isn't in [0,1] yet, but may be greater. Why?
        return getProbability(labelIndex);
    }

    @Override
    public void runInference() {
        tflite.run(imgData, labelProbArray);

    }
}
