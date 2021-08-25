package com.orion.utils.unit;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 重量单位
 * <p>
 * 舍入模式: {@link RoundingMode#FLOOR}
 * 舍入精度: 8
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/2/25 18:27
 */
public enum LengthUnit {

    /**
     * 毫米
     */
    MM {
        @Override
        public BigDecimal toMillimetre(BigDecimal u) {
            return u.setScale(SCALE, ROUND_MODEL);
        }

        @Override
        public BigDecimal toCentimeter(BigDecimal u) {
            return u.setScale(SCALE, ROUND_MODEL)
                    .divide(TEN, ROUND_MODEL);
        }

        @Override
        public BigDecimal toDecimetre(BigDecimal u) {
            return u.setScale(SCALE, ROUND_MODEL)
                    .divide(TEN, ROUND_MODEL)
                    .divide(TEN, ROUND_MODEL);
        }

        @Override
        public BigDecimal toMetre(BigDecimal u) {
            return u.setScale(SCALE, ROUND_MODEL)
                    .divide(TEN, ROUND_MODEL)
                    .divide(HUNDRED, ROUND_MODEL);
        }

        @Override
        public BigDecimal toKilometre(BigDecimal u) {
            return u.setScale(SCALE, ROUND_MODEL)
                    .divide(TEN, ROUND_MODEL)
                    .divide(HUNDRED, ROUND_MODEL)
                    .divide(THOUSAND, ROUND_MODEL);
        }
    },

    /**
     * 厘米
     */
    CM {
        @Override
        public BigDecimal toMillimetre(BigDecimal u) {
            return u.setScale(SCALE, ROUND_MODEL)
                    .multiply(TEN);
        }

        @Override
        public BigDecimal toCentimeter(BigDecimal u) {
            return u.setScale(SCALE, ROUND_MODEL);
        }

        @Override
        public BigDecimal toDecimetre(BigDecimal u) {
            return u.setScale(SCALE, ROUND_MODEL)
                    .divide(TEN, ROUND_MODEL);
        }

        @Override
        public BigDecimal toMetre(BigDecimal u) {
            return u.setScale(SCALE, ROUND_MODEL)
                    .divide(HUNDRED, ROUND_MODEL);
        }

        @Override
        public BigDecimal toKilometre(BigDecimal u) {
            return u.setScale(SCALE, ROUND_MODEL)
                    .divide(HUNDRED, ROUND_MODEL)
                    .divide(THOUSAND, ROUND_MODEL);
        }
    },

    /**
     * 分米
     */
    DM {
        @Override
        public BigDecimal toMillimetre(BigDecimal u) {
            return u.setScale(SCALE, ROUND_MODEL)
                    .multiply(HUNDRED);
        }

        @Override
        public BigDecimal toCentimeter(BigDecimal u) {
            return u.setScale(SCALE, ROUND_MODEL)
                    .multiply(TEN);
        }

        @Override
        public BigDecimal toDecimetre(BigDecimal u) {
            return u.setScale(SCALE, ROUND_MODEL);
        }

        @Override
        public BigDecimal toMetre(BigDecimal u) {
            return u.setScale(SCALE, ROUND_MODEL)
                    .divide(TEN, ROUND_MODEL);
        }

        @Override
        public BigDecimal toKilometre(BigDecimal u) {
            return u.setScale(SCALE, ROUND_MODEL)
                    .divide(TEN, ROUND_MODEL)
                    .divide(THOUSAND, ROUND_MODEL);
        }
    },

    /**
     * 米
     */
    M {
        @Override
        public BigDecimal toMillimetre(BigDecimal u) {
            return u.setScale(SCALE, ROUND_MODEL)
                    .multiply(HUNDRED)
                    .multiply(TEN);
        }

        @Override
        public BigDecimal toCentimeter(BigDecimal u) {
            return u.setScale(SCALE, ROUND_MODEL)
                    .multiply(HUNDRED);
        }

        @Override
        public BigDecimal toDecimetre(BigDecimal u) {
            return u.setScale(SCALE, ROUND_MODEL)
                    .multiply(TEN);
        }

        @Override
        public BigDecimal toMetre(BigDecimal u) {
            return u.setScale(SCALE, ROUND_MODEL);
        }

        @Override
        public BigDecimal toKilometre(BigDecimal u) {
            return u.setScale(SCALE, ROUND_MODEL)
                    .divide(THOUSAND, ROUND_MODEL);
        }
    },

    /**
     * 千米
     */
    KM {
        @Override
        public BigDecimal toMillimetre(BigDecimal u) {
            return u.setScale(SCALE, ROUND_MODEL)
                    .multiply(TEN)
                    .multiply(HUNDRED)
                    .multiply(THOUSAND);
        }

        @Override
        public BigDecimal toCentimeter(BigDecimal u) {
            return u.setScale(SCALE, ROUND_MODEL)
                    .multiply(HUNDRED)
                    .multiply(THOUSAND);
        }

        @Override
        public BigDecimal toDecimetre(BigDecimal u) {
            return u.setScale(SCALE, ROUND_MODEL)
                    .multiply(TEN)
                    .multiply(THOUSAND);
        }

        @Override
        public BigDecimal toMetre(BigDecimal u) {
            return u.setScale(SCALE, ROUND_MODEL)
                    .multiply(THOUSAND);
        }

        @Override
        public BigDecimal toKilometre(BigDecimal u) {
            return u.setScale(SCALE, ROUND_MODEL);
        }
    };

    private static final BigDecimal TEN = BigDecimal.valueOf(10);

    private static final BigDecimal HUNDRED = BigDecimal.valueOf(100);

    private static final BigDecimal THOUSAND = BigDecimal.valueOf(1000);

    /**
     * 舍入模式
     */
    private static final RoundingMode ROUND_MODEL = RoundingMode.FLOOR;

    /**
     * 舍入精度
     */
    private static final int SCALE = 8;

    /**
     * ? -> MM
     *
     * @param u unit
     * @return MM
     */
    public BigDecimal toMillimetre(Long u) {
        return this.toMillimetre(BigDecimal.valueOf(u));
    }

    /**
     * ? -> MM
     *
     * @param u unit
     * @return MM
     */
    public abstract BigDecimal toMillimetre(BigDecimal u);

    /**
     * ? -> CM
     *
     * @param u unit
     * @return MM
     */
    public BigDecimal toCentimeter(Long u) {
        return this.toCentimeter(BigDecimal.valueOf(u));
    }

    /**
     * ? -> CM
     *
     * @param u unit
     * @return CM
     */
    public abstract BigDecimal toCentimeter(BigDecimal u);

    /**
     * ? -> DM
     *
     * @param u unit
     * @return DM
     */
    public BigDecimal toDecimetre(Long u) {
        return this.toDecimetre(BigDecimal.valueOf(u));
    }

    /**
     * ? -> DM
     *
     * @param u unit
     * @return DM
     */
    public abstract BigDecimal toDecimetre(BigDecimal u);

    /**
     * ? -> M
     *
     * @param u unit
     * @return M
     */
    public BigDecimal toMetre(Long u) {
        return this.toMetre(BigDecimal.valueOf(u));
    }

    /**
     * ? -> M
     *
     * @param u unit
     * @return M
     */
    public abstract BigDecimal toMetre(BigDecimal u);

    /**
     * ? -> KM
     *
     * @param u unit
     * @return KM
     */
    public BigDecimal toKilometre(Long u) {
        return this.toKilometre(BigDecimal.valueOf(u));
    }

    /**
     * ? -> KM
     *
     * @param u unit
     * @return KM
     */
    public abstract BigDecimal toKilometre(BigDecimal u);

}
