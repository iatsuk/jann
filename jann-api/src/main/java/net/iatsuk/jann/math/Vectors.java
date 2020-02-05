package net.iatsuk.jann.math;

public class Vectors {

    public static float[] sum(float[] u, float[] v) {
        if (u.length != v.length) {
            throw new IllegalArgumentException("Vectors have different length");
        }

        float[] w = new float[u.length];
        for (int i = 0; i < w.length; i++) {
            w[i] = u[i] + v[i];
        }
        return w;
    }

    public static float dot(float[] u, float[] v) {
        if (u.length != v.length) {
            throw new IllegalArgumentException("Vectors have different length");
        }

        float result = 0;
        for (int i = 0; i < u.length; i++) {
            result += u[i] * v[i];
        }
        return result;
    }

    public static float magnitude(float[] v) {
        double sum = 0f;
        for (float vi : v) {
            sum += vi * vi;
        }
        return (float) Math.sqrt(sum);
    }

    public static float cos(float[] u, float[] v) {
        // @formatter:off
        return
                         dot(u, v)
        / //-----------------------------------
               (magnitude(u) * magnitude(v));
        // @formatter:on
    }

    public static int hamming(float[] u, float[] v) {
        if (u.length != v.length) {
            throw new IllegalArgumentException("Vectors have different length");
        }

        int dist = 0;
        for (int i = 0; i < u.length; i++) {
            if (Math.abs(u[i] - v[i]) >= 1e6) {
                dist++;
            }
        }
        return dist;
    }
}
