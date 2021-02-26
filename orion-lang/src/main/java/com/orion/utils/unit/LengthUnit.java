package com.orion.utils.unit;

import com.orion.utils.Exceptions;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 重量单位
 * <p>
 * 舍入模式: {@link RoundingMode#HALF_DOWN}
 * 舍入精度: 8
 *
 * @author ljh15
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
            return u.setScale(scale, roundModel);
        }

        @Override
        public BigDecimal toCentimeter(BigDecimal u) {
            return u.setScale(scale, roundModel)
                    .divide(TEN, roundModel);
        }

        @Override
        public BigDecimal toDecimetre(BigDecimal u) {
            return u.setScale(scale, roundModel)
                    .divide(TEN, roundModel)
                    .divide(TEN, roundModel);
        }

        @Override
        public BigDecimal toMetre(BigDecimal u) {
            return u.setScale(scale, roundModel)
                    .divide(TEN, roundModel)
                    .divide(HUNDRED, roundModel);
        }

        @Override
        public BigDecimal toKilometre(BigDecimal u) {
            return u.setScale(scale, roundModel)
                    .divide(TEN, roundModel)
                    .divide(HUNDRED, roundModel)
                    .divide(THOUSAND, roundModel);
        }
    },

    /**
     * 厘米
     */
    CM {
        @Override
        public BigDecimal toMillimetre(BigDecimal u) {
            return u.setScale(scale, roundModel)
                    .multiply(TEN);
        }

        @Override
        public BigDecimal toCentimeter(BigDecimal u) {
            return u.setScale(scale, roundModel);
        }

        @Override
        public BigDecimal toDecimetre(BigDecimal u) {
            return u.setScale(scale, roundModel)
                    .divide(TEN, roundModel);
        }

        @Override
        public BigDecimal toMetre(BigDecimal u) {
            return u.setScale(scale, roundModel)
                    .divide(HUNDRED, roundModel);
        }

        @Override
        public BigDecimal toKilometre(BigDecimal u) {
            return u.setScale(scale, roundModel)
                    .divide(HUNDRED, roundModel)
                    .divide(THOUSAND, roundModel);
        }
    },

    /**
     * 分米
     */
    DM {
        @Override
        public BigDecimal toMillimetre(BigDecimal u) {
            return u.setScale(scale, roundModel)
                    .multiply(HUNDRED);
        }

        @Override
        public BigDecimal toCentimeter(BigDecimal u) {
            return u.setScale(scale, roundModel)
                    .multiply(TEN);
        }

        @Override
        public BigDecimal toDecimetre(BigDecimal u) {
            return u.setScale(scale, roundModel);
        }

        @Override
        public BigDecimal toMetre(BigDecimal u) {
            return u.setScale(scale, roundModel)
                    .divide(TEN, roundModel);
        }

        @Override
        public BigDecimal toKilometre(BigDecimal u) {
            return u.setScale(scale, roundModel)
                    .divide(TEN, roundModel)
                    .divide(THOUSAND, roundModel);
        }
    },

    /**
     * 米
     */
    M {
        @Override
        public BigDecimal toMillimetre(BigDecimal u) {
            return u.setScale(scale, roundModel)
                    .multiply(HUNDRED)
                    .multiply(TEN);
        }

        @Override
        public BigDecimal toCentimeter(BigDecimal u) {
            return u.setScale(scale, roundModel)
                    .multiply(HUNDRED);
        }

        @Override
        public BigDecimal toDecimetre(BigDecimal u) {
            return u.setScale(scale, roundModel)
                    .multiply(TEN);
        }

        @Override
        public BigDecimal toMetre(BigDecimal u) {
            return u.setScale(scale, roundModel);
        }

        @Override
        public BigDecimal toKilometre(BigDecimal u) {
            return u.setScale(scale, roundModel)
                    .divide(THOUSAND, roundModel);
        }
    },

    /**
     * 千米
     */
    KM {
        @Override
        public BigDecimal toMillimetre(BigDecimal u) {
            return u.setScale(scale, roundModel)
                    .multiply(TEN)
                    .multiply(HUNDRED)
                    .multiply(THOUSAND);
        }

        @Override
        public BigDecimal toCentimeter(BigDecimal u) {
            return u.setScale(scale, roundModel)
                    .multiply(HUNDRED)
                    .multiply(THOUSAND);
        }

        @Override
        public BigDecimal toDecimetre(BigDecimal u) {
            return u.setScale(scale, roundModel)
                    .multiply(TEN)
                    .multiply(THOUSAND);
        }

        @Override
        public BigDecimal toMetre(BigDecimal u) {
            return u.setScale(scale, roundModel)
                    .multiply(THOUSAND);
        }

        @Override
        public BigDecimal toKilometre(BigDecimal u) {
            return u.setScale(scale, roundModel);
        }
    };

    private static final BigDecimal TEN = BigDecimal.valueOf(10);

    private static final BigDecimal HUNDRED = BigDecimal.valueOf(100);

    private static final BigDecimal THOUSAND = BigDecimal.valueOf(1000);

    /**
     * 舍入模式
     */
    private static RoundingMode roundModel = RoundingMode.HALF_DOWN;

    /**
     * 舍入精度
     */
    private static int scale = 8;

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
    public BigDecimal toMillimetre(BigDecimal u) {
        throw Exceptions.unSupport();
    }

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
    public BigDecimal toCentimeter(BigDecimal u) {
        throw Exceptions.unSupport();
    }

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
    public BigDecimal toDecimetre(BigDecimal u) {
        throw Exceptions.unSupport();
    }

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
    public BigDecimal toMetre(BigDecimal u) {
        throw Exceptions.unSupport();
    }

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
    public BigDecimal toKilometre(BigDecimal u) {
        throw Exceptions.unSupport();
    }

}
