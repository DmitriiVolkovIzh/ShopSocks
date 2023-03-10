package pro.sky.shopsocks.models.enums;

public enum SizeSocks {
    XS(new double[]{35.0, 35.5, 36.0, 36.5}),
    S(new double[]{37.0, 37.5, 38.0, 38.5}),
    M(new double[]{39.0, 39.5, 40.0, 40.5}),
    L(new double[]{41.0, 41.5, 42.0, 42.5, 43.0}),
    XL(new double[]{43.5, 44.0, 44.5, 45.0, 45.5, 46.0, 46.5, 47.0});

    private final double[] size;

    SizeSocks(double[] size) {
        this.size = size;
    }

    public double[] getSize() {
        return size;
    }
    public static SizeSocks checkFitToSize(double reallySize) {
        SizeSocks sizeSocks = null;
        for (SizeSocks socksSizes : SizeSocks.values()) {
            for (Double size : socksSizes.getSize()) {
                if (reallySize == size) {
                    sizeSocks = socksSizes;
                    break;
                }
            }
        }
        return sizeSocks;
    }


    }

