/*
 * Copyright (c) 2019 - present Jiahang Li, All rights reserved.
 *
 *   https://kit.orionsec.cn
 *
 * Members:
 *   Jiahang Li - ljh1553488six@139.com - author
 *
 * The MIT License (MIT)
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package cn.orionsec.kit.lang.utils.unit;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 重量单位
 * <p>
 * 默认舍入模式: {@link RoundingMode#FLOOR}
 * 默认舍入精度: 4
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
        public BigDecimal toMilligram(BigDecimal u, int scale, RoundingMode roundingMode) {
            return u;
        }

        @Override
        public BigDecimal toGram(BigDecimal u, int scale, RoundingMode roundingMode) {
            return u.setScale(scale, roundingMode)
                    .divide(THOUSAND, roundingMode);
        }

        @Override
        public BigDecimal toKilogram(BigDecimal u, int scale, RoundingMode roundingMode) {
            return u.setScale(scale, roundingMode)
                    .divide(THOUSAND, roundingMode)
                    .divide(THOUSAND, roundingMode);
        }

        @Override
        public BigDecimal toTon(BigDecimal u, int scale, RoundingMode roundingMode) {
            return u.setScale(scale, roundingMode)
                    .divide(THOUSAND, roundingMode)
                    .divide(THOUSAND, roundingMode)
                    .divide(THOUSAND, roundingMode);
        }
    },

    /**
     * 克
     */
    G {
        @Override
        public BigDecimal toMilligram(BigDecimal u, int scale, RoundingMode roundingMode) {
            return u.setScale(scale, roundingMode)
                    .multiply(THOUSAND);
        }

        @Override
        public BigDecimal toGram(BigDecimal u, int scale, RoundingMode roundingMode) {
            return u;
        }

        @Override
        public BigDecimal toKilogram(BigDecimal u, int scale, RoundingMode roundingMode) {
            return u.setScale(scale, roundingMode)
                    .divide(THOUSAND, roundingMode);
        }

        @Override
        public BigDecimal toTon(BigDecimal u, int scale, RoundingMode roundingMode) {
            return u.setScale(scale, roundingMode)
                    .divide(THOUSAND, roundingMode)
                    .divide(THOUSAND, roundingMode);
        }
    },

    /**
     * 千克
     */
    KG {
        @Override
        public BigDecimal toMilligram(BigDecimal u, int scale, RoundingMode roundingMode) {
            return u.setScale(scale, roundingMode)
                    .multiply(THOUSAND)
                    .multiply(THOUSAND);
        }

        @Override
        public BigDecimal toGram(BigDecimal u, int scale, RoundingMode roundingMode) {
            return u.setScale(scale, roundingMode)
                    .multiply(THOUSAND);
        }

        @Override
        public BigDecimal toKilogram(BigDecimal u, int scale, RoundingMode roundingMode) {
            return u;
        }

        @Override
        public BigDecimal toTon(BigDecimal u, int scale, RoundingMode roundingMode) {
            return u.setScale(scale, roundingMode)
                    .divide(THOUSAND, roundingMode);
        }
    },

    /**
     * 吨
     */
    T {
        @Override
        public BigDecimal toMilligram(BigDecimal u, int scale, RoundingMode roundingMode) {
            return u.setScale(scale, roundingMode)
                    .multiply(THOUSAND)
                    .multiply(THOUSAND)
                    .multiply(THOUSAND);
        }

        @Override
        public BigDecimal toGram(BigDecimal u, int scale, RoundingMode roundingMode) {
            return u.setScale(scale, roundingMode)
                    .multiply(THOUSAND)
                    .multiply(THOUSAND);
        }

        @Override
        public BigDecimal toKilogram(BigDecimal u, int scale, RoundingMode roundingMode) {
            return u.setScale(scale, roundingMode)
                    .multiply(THOUSAND);
        }

        @Override
        public BigDecimal toTon(BigDecimal u, int scale, RoundingMode roundingMode) {
            return u.setScale(scale, roundingMode);
        }
    };

    private static final BigDecimal THOUSAND = BigDecimal.valueOf(1000);

    /**
     * 默认舍入精度
     */
    private static final int DEFAULT_SCALE = 4;

    /**
     * 默认舍入模式
     */
    private static final RoundingMode DEFAULT_ROUND_MODE = RoundingMode.FLOOR;

    public BigDecimal toMilligram(long u) {
        return this.toMilligram(BigDecimal.valueOf(u), DEFAULT_SCALE, DEFAULT_ROUND_MODE);
    }

    public BigDecimal toMilligram(long u, int scale, RoundingMode roundingMode) {
        return this.toMilligram(BigDecimal.valueOf(u), scale, roundingMode);
    }

    public BigDecimal toMilligram(BigDecimal u) {
        return this.toMilligram(u, DEFAULT_SCALE, DEFAULT_ROUND_MODE);
    }

    /**
     * ? -> MG
     *
     * @param u            unit
     * @param scale        scale
     * @param roundingMode roundingMode
     * @return MG
     */
    public abstract BigDecimal toMilligram(BigDecimal u, int scale, RoundingMode roundingMode);

    public BigDecimal toGram(long u) {
        return this.toGram(BigDecimal.valueOf(u), DEFAULT_SCALE, DEFAULT_ROUND_MODE);
    }

    public BigDecimal toGram(long u, int scale, RoundingMode roundingMode) {
        return this.toGram(BigDecimal.valueOf(u), scale, roundingMode);
    }

    public BigDecimal toGram(BigDecimal u) {
        return this.toGram(u, DEFAULT_SCALE, DEFAULT_ROUND_MODE);
    }

    /**
     * ? -> G
     *
     * @param u            unit
     * @param scale        scale
     * @param roundingMode roundingMode
     * @return G
     */
    public abstract BigDecimal toGram(BigDecimal u, int scale, RoundingMode roundingMode);

    public BigDecimal toKilogram(long u) {
        return this.toKilogram(BigDecimal.valueOf(u), DEFAULT_SCALE, DEFAULT_ROUND_MODE);
    }

    public BigDecimal toKilogram(long u, int scale, RoundingMode roundingMode) {
        return this.toKilogram(BigDecimal.valueOf(u), scale, roundingMode);
    }

    public BigDecimal toKilogram(BigDecimal u) {
        return this.toKilogram(u, DEFAULT_SCALE, DEFAULT_ROUND_MODE);
    }

    /**
     * ? -> KG
     *
     * @param u            unit
     * @param scale        scale
     * @param roundingMode roundingMode
     * @return KG
     */
    public abstract BigDecimal toKilogram(BigDecimal u, int scale, RoundingMode roundingMode);

    public BigDecimal toTon(long u) {
        return this.toTon(BigDecimal.valueOf(u), DEFAULT_SCALE, DEFAULT_ROUND_MODE);
    }

    public BigDecimal toTon(long u, int scale, RoundingMode roundingMode) {
        return this.toTon(BigDecimal.valueOf(u), scale, roundingMode);
    }

    public BigDecimal toTon(BigDecimal u) {
        return this.toTon(u, DEFAULT_SCALE, DEFAULT_ROUND_MODE);
    }

    /**
     * ? -> T
     *
     * @param u            unit
     * @param scale        scale
     * @param roundingMode roundingMode
     * @return T
     */
    public abstract BigDecimal toTon(BigDecimal u, int scale, RoundingMode roundingMode);

}
