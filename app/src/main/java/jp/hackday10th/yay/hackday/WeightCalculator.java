package jp.hackday10th.yay.hackday;

public class WeightCalculator {
    public WeightCalculator() {

    }

    public float computeWeight(float[] footPrints) {
        if (footPrints.length == 0) {
            return 0.0f;
        }
        if (footPrints[0] < 0.0f) {
            return 0.0f;
        }
        return footPrints[0] * 100.0f;
    }
}
