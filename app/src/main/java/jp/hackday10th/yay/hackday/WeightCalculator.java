package jp.hackday10th.yay.hackday;

import android.util.Log;

import java.util.Arrays;

public class WeightCalculator {
    private static final String TAG = WeightCalculator.class.getSimpleName();
    private static final float[] DATA_POINTS = {
            0.25f,
            0.43f,
            0.50f,
            0.53f,
            0.54f
    };

    public WeightCalculator() {

    }

    public float computeWeight(float[] footPrints) {
        if (footPrints.length == 0) {
            return 0.0f;
        }

        float averageTop3FootPrint = getAverageOfTop3FootPrint(footPrints);
        float slope = getSlope(averageTop3FootPrint);
        float yIntercept = getYIntercept(averageTop3FootPrint, slope);
        float weight = averageTop3FootPrint * slope + yIntercept;
        Log.i(TAG, "ave top3 FootPrint=" + averageTop3FootPrint);
        return (weight <= 0.0f) ? 0.0f : weight;
    }

    private float getSlope(float x) {
        if (x <= DATA_POINTS[0]) {
            return 0.0f;
        }
        int lowIndex = 0;
        int highIndex = 1;
        for (int i = 0; i < DATA_POINTS.length - 1; ++i) {
            if (DATA_POINTS[i] < x && x <= DATA_POINTS[i + 1]) {
                lowIndex = i;
                highIndex = lowIndex + 1;
            }

        }
        if (DATA_POINTS[DATA_POINTS.length - 1] < x) {
            lowIndex = DATA_POINTS.length - 2;
            highIndex = lowIndex + 1;
        }
        return 100.0f / (DATA_POINTS[highIndex] - DATA_POINTS[lowIndex]);
    }

    private float getYIntercept(float x, float slope) {
        if (x <= DATA_POINTS[0]) {
            return 0.0f;
        }
        int highIndex = 0;
        for (int i = 0; i < DATA_POINTS.length - 1; ++i) {
            if (DATA_POINTS[i] < x && x <= DATA_POINTS[i + 1]) {
                highIndex = i;
            }

        }
        if (DATA_POINTS[DATA_POINTS.length - 1] < x) {
            highIndex = DATA_POINTS.length - 1;
        }
        return highIndex * 100.0f - slope * DATA_POINTS[highIndex];
    }

    private float getAverageOfTop3FootPrint(float[] footPrints) {
        final int TOP3 = 3;
        float averageOfTop3 = 0.0f;
        float count = Math.min(footPrints.length, TOP3);
        Arrays.sort(footPrints);
        for (int i = 0; i < count; ++i) {
            float footPrint = footPrints[footPrints.length - i - 1];
            if (0.0f < footPrint) {
                averageOfTop3 += footPrint;
            }
        }
        return averageOfTop3 / count;
    }
}
