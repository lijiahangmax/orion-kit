package com.orion.utils.unit;

import com.orion.utils.Exceptions;

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
public enum WeightUnit {

    /**
     * 毫克
     */
    MG {
        @Override
        public BigDecimal toMilligram(BigDecimal u) {
            return u.setScale(SCALE, ROUND_MODE);
        }

        @Override
        public BigDecimal toGram(BigDecimal u) {
            return u.setScale(SCALE, ROUND_MODE)
                    .divide(THOUSAND, ROUND_MODE);
        }

        @Override
        public BigDecimal toKilogram(BigDecimal u) {
            return u.setScale(SCALE, ROUND_MODE)
                    .divide(THOUSAND, ROUND_MODE)
                    .divide(THOUSAND, ROUND_MODE);
        }

        @Override
        public BigDecimal toTon(BigDecimal u) {
            return u.setScale(SCALE, ROUND_MODE)
                    .divide(THOUSAND, ROUND_MODE)
                    .divide(THOUSAND, ROUND_MODE)
                    .divide(THOUSAND, ROUND_MODE);
        }
    },

    /**
     * 克
     */
    G {
        @Override
        public BigDecimal toMilligram(BigDecimal u) {
            return u.setScale(SCALE, ROUND_MODE)
                    .multiply(THOUSAND);
        }

        @Override
        public BigDecimal toGram(BigDecimal u) {
            return u.setScale(SCALE, ROUND_MODE);
        }

        @Override
        public BigDecimal toKilogram(BigDecimal u) {
            return u.setScale(SCALE, ROUND_MODE)
                    .divide(THOUSAND, ROUND_MODE);
        }

        @Override
        public BigDecimal toTon(BigDecimal u) {
            return u.setScale(SCALE, ROUND_MODE)
                    .divide(THOUSAND, ROUND_MODE)
                    .divide(THOUSAND, ROUND_MODE);
        }
    },

    /**
     * 千克
     */
    KG {
        @Override
        public BigDecimal toMilligram(BigDecimal u) {
            return u.setScale(SCALE, ROUND_MODE)
                    .multiply(THOUSAND)
                    .multiply(THOUSAND);
        }

        @Override
        public BigDecimal toGram(BigDecimal u) {
            return u.setScale(SCALE, ROUND_MODE)
                    .multiply(THOUSAND);
        }

        @Override
        public BigDecimal toKilogram(BigDecimal u) {
            return u.setScale(SCALE, ROUND_MODE);
        }

        @Override
        public BigDecimal toTon(BigDecimal u) {
            return u.setScale(SCALE, ROUND_MODE)
                    .divide(THOUSAND, ROUND_MODE);
        }
    },

    /**
     * 吨
     */
    T {
        @Override
        public BigDecimal toMilligram(BigDecimal u) {
            return u.setScale(SCALE, ROUND_MODE)
                    .multiply(THOUSAND)
                    .multiply(THOUSAND)
                    .multiply(THOUSAND);
        }

        @Override
        public BigDecimal toGram(BigDecimal u) {
            return u.setScale(SCALE, ROUND_MODE)
                    .multiply(THOUSAND)
                    .multiply(THOUSAND);
        }

        @Override
        public BigDecimal toKilogram(BigDecimal u) {
            return u.setScale(SCALE, ROUND_MODE)
                    .multiply(THOUSAND);
        }

        @Override
        public BigDecimal toTon(BigDecimal u) {
            return u.setScale(SCALE, ROUND_MODE);
        }
    };

    private static final BigDecimal THOUSAND = BigDecimal.valueOf(1000);

    /**
     * 舍入模式
     */
    private static final RoundingMode ROUND_MODE = RoundingMode.FLOOR;

    /**
     * 舍入精度
     */
    private static final int SCALE = 8;

    /**
     * ? -> MG
     *
     * @param u unit
     * @return MG
     */
    public BigDecimal toMilligram(Long u) {
        return this.toMilligram(BigDecimal.valueOf(u));
    }

    /**
     * ? -> MG
     *
     * @param u unit
     * @return MG
     */
    public BigDecimal toMilligram(BigDecimal u) {
        throw Exceptions.unsupported();
    }

    /**
     * ? -> G
     *
     * @param u unit
     * @return G
     */
    public BigDecimal toGram(Long u) {
        return this.toGram(BigDecimal.valueOf(u));
    }

    /**
     * ? -> G
     *
     * @param u unit
     * @return G
     */
    public BigDecimal toGram(BigDecimal u) {
        throw Exceptions.unsupported();
    }

    /**
     * ? -> KG
     *
     * @param u unit
     * @return KG
     */
    public BigDecimal toKilogram(Long u) {
        return this.toKilogram(BigDecimal.valueOf(u));
    }

    /**
     * ? -> G
     *
     * @param u unit
     * @return KG
     */
    public BigDecimal toKilogram(BigDecimal u) {
        throw Exceptions.unsupported();
    }

    /**
     * ? -> T
     *
     * @param u unit
     * @return T
     */
    public BigDecimal toTon(Long u) {
        return this.toTon(BigDecimal.valueOf(u));
    }

    /**
     * ? -> T
     *
     * @param u unit
     * @return T
     */
    public BigDecimal toTon(BigDecimal u) {
        throw Exceptions.unsupported();
    }

}
