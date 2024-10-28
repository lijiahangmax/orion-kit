/*
 * Copyright (c) 2019 - present Jiahang Li (kit.orionsec.cn ljh1553488six@139.com).
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
 * 长度单位
 * <p>
 * 默认舍入模式: {@link RoundingMode#FLOOR}
 * 默认舍入精度: 2
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
        public BigDecimal toMillimetre(BigDecimal u, int scale, RoundingMode roundingMode) {
            return u;
        }

        @Override
        public BigDecimal toCentimeter(BigDecimal u, int scale, RoundingMode roundingMode) {
            return u.setScale(scale, roundingMode)
                    .divide(TEN, roundingMode);
        }

        @Override
        public BigDecimal toDecimetre(BigDecimal u, int scale, RoundingMode roundingMode) {
            return u.setScale(scale, roundingMode)
                    .divide(TEN, roundingMode)
                    .divide(TEN, roundingMode);
        }

        @Override
        public BigDecimal toMetre(BigDecimal u, int scale, RoundingMode roundingMode) {
            return u.setScale(scale, roundingMode)
                    .divide(TEN, roundingMode)
                    .divide(HUNDRED, roundingMode);
        }

        @Override
        public BigDecimal toKilometre(BigDecimal u, int scale, RoundingMode roundingMode) {
            return u.setScale(scale, roundingMode)
                    .divide(TEN, roundingMode)
                    .divide(HUNDRED, roundingMode)
                    .divide(THOUSAND, roundingMode);
        }
    },

    /**
     * 厘米
     */
    CM {
        @Override
        public BigDecimal toMillimetre(BigDecimal u, int scale, RoundingMode roundingMode) {
            return u.setScale(scale, roundingMode)
                    .multiply(TEN);
        }

        @Override
        public BigDecimal toCentimeter(BigDecimal u, int scale, RoundingMode roundingMode) {
            return u;
        }

        @Override
        public BigDecimal toDecimetre(BigDecimal u, int scale, RoundingMode roundingMode) {
            return u.setScale(scale, roundingMode)
                    .divide(TEN, roundingMode);
        }

        @Override
        public BigDecimal toMetre(BigDecimal u, int scale, RoundingMode roundingMode) {
            return u.setScale(scale, roundingMode)
                    .divide(HUNDRED, roundingMode);
        }

        @Override
        public BigDecimal toKilometre(BigDecimal u, int scale, RoundingMode roundingMode) {
            return u.setScale(scale, roundingMode)
                    .divide(HUNDRED, roundingMode)
                    .divide(THOUSAND, roundingMode);
        }
    },

    /**
     * 分米
     */
    DM {
        @Override
        public BigDecimal toMillimetre(BigDecimal u, int scale, RoundingMode roundingMode) {
            return u.setScale(scale, roundingMode)
                    .multiply(HUNDRED);
        }

        @Override
        public BigDecimal toCentimeter(BigDecimal u, int scale, RoundingMode roundingMode) {
            return u.setScale(scale, roundingMode)
                    .multiply(TEN);
        }

        @Override
        public BigDecimal toDecimetre(BigDecimal u, int scale, RoundingMode roundingMode) {
            return u;
        }

        @Override
        public BigDecimal toMetre(BigDecimal u, int scale, RoundingMode roundingMode) {
            return u.setScale(scale, roundingMode)
                    .divide(TEN, roundingMode);
        }

        @Override
        public BigDecimal toKilometre(BigDecimal u, int scale, RoundingMode roundingMode) {
            return u.setScale(scale, roundingMode)
                    .divide(TEN, roundingMode)
                    .divide(THOUSAND, roundingMode);
        }
    },

    /**
     * 米
     */
    M {
        @Override
        public BigDecimal toMillimetre(BigDecimal u, int scale, RoundingMode roundingMode) {
            return u.setScale(scale, roundingMode)
                    .multiply(HUNDRED)
                    .multiply(TEN);
        }

        @Override
        public BigDecimal toCentimeter(BigDecimal u, int scale, RoundingMode roundingMode) {
            return u.setScale(scale, roundingMode)
                    .multiply(HUNDRED);
        }

        @Override
        public BigDecimal toDecimetre(BigDecimal u, int scale, RoundingMode roundingMode) {
            return u.setScale(scale, roundingMode)
                    .multiply(TEN);
        }

        @Override
        public BigDecimal toMetre(BigDecimal u, int scale, RoundingMode roundingMode) {
            return u;
        }

        @Override
        public BigDecimal toKilometre(BigDecimal u, int scale, RoundingMode roundingMode) {
            return u.setScale(scale, roundingMode)
                    .divide(THOUSAND, roundingMode);
        }
    },

    /**
     * 千米
     */
    KM {
        @Override
        public BigDecimal toMillimetre(BigDecimal u, int scale, RoundingMode roundingMode) {
            return u.setScale(scale, roundingMode)
                    .multiply(TEN)
                    .multiply(HUNDRED)
                    .multiply(THOUSAND);
        }

        @Override
        public BigDecimal toCentimeter(BigDecimal u, int scale, RoundingMode roundingMode) {
            return u.setScale(scale, roundingMode)
                    .multiply(HUNDRED)
                    .multiply(THOUSAND);
        }

        @Override
        public BigDecimal toDecimetre(BigDecimal u, int scale, RoundingMode roundingMode) {
            return u.setScale(scale, roundingMode)
                    .multiply(TEN)
                    .multiply(THOUSAND);
        }

        @Override
        public BigDecimal toMetre(BigDecimal u, int scale, RoundingMode roundingMode) {
            return u.setScale(scale, roundingMode)
                    .multiply(THOUSAND);
        }

        @Override
        public BigDecimal toKilometre(BigDecimal u, int scale, RoundingMode roundingMode) {
            return u;
        }
    };

    private static final BigDecimal TEN = BigDecimal.valueOf(10);

    private static final BigDecimal HUNDRED = BigDecimal.valueOf(100);

    private static final BigDecimal THOUSAND = BigDecimal.valueOf(1000);

    /**
     * 默认舍入模式
     */
    private static final RoundingMode DEFAULT_ROUND_MODE = RoundingMode.FLOOR;

    /**
     * 默认舍入精度
     */
    private static final int DEFAULT_SCALE = 2;

    public BigDecimal toMillimetre(long u) {
        return this.toMillimetre(BigDecimal.valueOf(u), DEFAULT_SCALE, DEFAULT_ROUND_MODE);
    }

    public BigDecimal toMillimetre(long u, int scale, RoundingMode roundingMode) {
        return this.toMillimetre(BigDecimal.valueOf(u), scale, roundingMode);
    }

    public BigDecimal toMillimetre(BigDecimal u) {
        return this.toMillimetre(u, DEFAULT_SCALE, DEFAULT_ROUND_MODE);
    }

    /**
     * ? -> MM
     *
     * @param u            unit
     * @param scale        scale
     * @param roundingMode roundingMode
     * @return MM
     */
    public abstract BigDecimal toMillimetre(BigDecimal u, int scale, RoundingMode roundingMode);

    public BigDecimal toCentimeter(long u) {
        return this.toCentimeter(BigDecimal.valueOf(u), DEFAULT_SCALE, DEFAULT_ROUND_MODE);
    }

    public BigDecimal toCentimeter(long u, int scale, RoundingMode roundingMode) {
        return this.toCentimeter(BigDecimal.valueOf(u), scale, roundingMode);
    }

    public BigDecimal toCentimeter(BigDecimal u) {
        return this.toCentimeter(u, DEFAULT_SCALE, DEFAULT_ROUND_MODE);
    }

    /**
     * ? -> CM
     *
     * @param u            unit
     * @param scale        scale
     * @param roundingMode roundingMode
     * @return CM
     */
    public abstract BigDecimal toCentimeter(BigDecimal u, int scale, RoundingMode roundingMode);

    public BigDecimal toDecimetre(long u) {
        return this.toDecimetre(BigDecimal.valueOf(u), DEFAULT_SCALE, DEFAULT_ROUND_MODE);
    }

    public BigDecimal toDecimetre(long u, int scale, RoundingMode roundingMode) {
        return this.toDecimetre(BigDecimal.valueOf(u), scale, roundingMode);
    }

    public BigDecimal toDecimetre(BigDecimal u) {
        return this.toDecimetre(u, DEFAULT_SCALE, DEFAULT_ROUND_MODE);
    }

    /**
     * ? -> DM
     *
     * @param u            unit
     * @param scale        scale
     * @param roundingMode roundingMode
     * @return DM
     */
    public abstract BigDecimal toDecimetre(BigDecimal u, int scale, RoundingMode roundingMode);

    public BigDecimal toMetre(long u) {
        return this.toMetre(BigDecimal.valueOf(u), DEFAULT_SCALE, DEFAULT_ROUND_MODE);
    }

    public BigDecimal toMetre(long u, int scale, RoundingMode roundingMode) {
        return this.toMetre(BigDecimal.valueOf(u), scale, roundingMode);
    }

    public BigDecimal toMetre(BigDecimal u) {
        return this.toMetre(u, DEFAULT_SCALE, DEFAULT_ROUND_MODE);
    }

    /**
     * ? -> M
     *
     * @param u            unit
     * @param scale        scale
     * @param roundingMode roundingMode
     * @return M
     */
    public abstract BigDecimal toMetre(BigDecimal u, int scale, RoundingMode roundingMode);

    public BigDecimal toKilometre(long u) {
        return this.toKilometre(BigDecimal.valueOf(u), DEFAULT_SCALE, DEFAULT_ROUND_MODE);
    }

    public BigDecimal toKilometre(long u, int scale, RoundingMode roundingMode) {
        return this.toKilometre(BigDecimal.valueOf(u), scale, roundingMode);
    }

    public BigDecimal toKilometre(BigDecimal u) {
        return this.toKilometre(u, DEFAULT_SCALE, DEFAULT_ROUND_MODE);
    }

    /**
     * ? -> KM
     *
     * @param u            unit
     * @param scale        scale
     * @param roundingMode roundingMode
     * @return KM
     */
    public abstract BigDecimal toKilometre(BigDecimal u, int scale, RoundingMode roundingMode);

}
