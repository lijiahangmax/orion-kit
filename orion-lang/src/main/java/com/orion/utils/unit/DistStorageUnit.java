package com.orion.utils.unit;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 磁盘存储单位
 * <p>
 * 默认舍入模式: {@link RoundingMode#FLOOR}
 * 默认舍入精度: 2
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2022/5/17 10:12
 */
public enum DistStorageUnit {

    /**
     * bit
     */
    BIT {
        @Override
        public BigDecimal toBit(BigDecimal u, int scale, RoundingMode roundingMode) {
            return u;
        }

        @Override
        public BigDecimal toByte(BigDecimal u, int scale, RoundingMode roundingMode) {
            return u.setScale(scale, roundingMode)
                    .divide(BIT_8, roundingMode);
        }

        @Override
        public BigDecimal toKilobyte(BigDecimal u, int scale, RoundingMode roundingMode) {
            return u.setScale(scale, roundingMode)
                    .divide(BIT_8, roundingMode)
                    .divide(UNIT, roundingMode);
        }

        @Override
        public BigDecimal toMegabyte(BigDecimal u, int scale, RoundingMode roundingMode) {
            return u.setScale(scale, roundingMode)
                    .divide(BIT_8, roundingMode)
                    .divide(UNIT, roundingMode)
                    .divide(UNIT, roundingMode);
        }

        @Override
        public BigDecimal toGigabyte(BigDecimal u, int scale, RoundingMode roundingMode) {
            return u.setScale(scale, roundingMode)
                    .divide(BIT_8, roundingMode)
                    .divide(UNIT, roundingMode)
                    .divide(UNIT, roundingMode)
                    .divide(UNIT, roundingMode);
        }

        @Override
        public BigDecimal toTerabyte(BigDecimal u, int scale, RoundingMode roundingMode) {
            return u.setScale(scale, roundingMode)
                    .divide(BIT_8, roundingMode)
                    .divide(UNIT, roundingMode)
                    .divide(UNIT, roundingMode)
                    .divide(UNIT, roundingMode)
                    .divide(UNIT, roundingMode);
        }
    },

    /**
     * Byte
     */
    B {
        @Override
        public BigDecimal toBit(BigDecimal u, int scale, RoundingMode roundingMode) {
            return u.setScale(scale, roundingMode)
                    .multiply(BIT_8);
        }

        @Override
        public BigDecimal toByte(BigDecimal u, int scale, RoundingMode roundingMode) {
            return u;
        }

        @Override
        public BigDecimal toKilobyte(BigDecimal u, int scale, RoundingMode roundingMode) {
            return u.setScale(scale, roundingMode)
                    .divide(UNIT, roundingMode);
        }

        @Override
        public BigDecimal toMegabyte(BigDecimal u, int scale, RoundingMode roundingMode) {
            return u.setScale(scale, roundingMode)
                    .divide(UNIT, roundingMode)
                    .divide(UNIT, roundingMode);
        }

        @Override
        public BigDecimal toGigabyte(BigDecimal u, int scale, RoundingMode roundingMode) {
            return u.setScale(scale, roundingMode)
                    .divide(UNIT, roundingMode)
                    .divide(UNIT, roundingMode)
                    .divide(UNIT, roundingMode);
        }

        @Override
        public BigDecimal toTerabyte(BigDecimal u, int scale, RoundingMode roundingMode) {
            return u.setScale(scale, roundingMode)
                    .divide(UNIT, roundingMode)
                    .divide(UNIT, roundingMode)
                    .divide(UNIT, roundingMode)
                    .divide(UNIT, roundingMode);
        }
    },

    /**
     * KB
     */
    KB {
        @Override
        public BigDecimal toBit(BigDecimal u, int scale, RoundingMode roundingMode) {
            return u.setScale(scale, roundingMode)
                    .multiply(BIT_8)
                    .multiply(UNIT);
        }

        @Override
        public BigDecimal toByte(BigDecimal u, int scale, RoundingMode roundingMode) {
            return u.setScale(scale, roundingMode)
                    .multiply(UNIT);
        }

        @Override
        public BigDecimal toKilobyte(BigDecimal u, int scale, RoundingMode roundingMode) {
            return u;
        }

        @Override
        public BigDecimal toMegabyte(BigDecimal u, int scale, RoundingMode roundingMode) {
            return u.setScale(scale, roundingMode)
                    .divide(UNIT, roundingMode);
        }

        @Override
        public BigDecimal toGigabyte(BigDecimal u, int scale, RoundingMode roundingMode) {
            return u.setScale(scale, roundingMode)
                    .divide(UNIT, roundingMode)
                    .divide(UNIT, roundingMode);
        }

        @Override
        public BigDecimal toTerabyte(BigDecimal u, int scale, RoundingMode roundingMode) {
            return u.setScale(scale, roundingMode)
                    .divide(UNIT, roundingMode)
                    .divide(UNIT, roundingMode)
                    .divide(UNIT, roundingMode);
        }
    },

    /**
     * MB
     */
    MB {
        @Override
        public BigDecimal toBit(BigDecimal u, int scale, RoundingMode roundingMode) {
            return u.setScale(scale, roundingMode)
                    .multiply(BIT_8)
                    .multiply(UNIT)
                    .multiply(UNIT);
        }

        @Override
        public BigDecimal toByte(BigDecimal u, int scale, RoundingMode roundingMode) {
            return u.setScale(scale, roundingMode)
                    .multiply(UNIT)
                    .multiply(UNIT);
        }

        @Override
        public BigDecimal toKilobyte(BigDecimal u, int scale, RoundingMode roundingMode) {
            return u.setScale(scale, roundingMode)
                    .multiply(UNIT);
        }

        @Override
        public BigDecimal toMegabyte(BigDecimal u, int scale, RoundingMode roundingMode) {
            return u;
        }

        @Override
        public BigDecimal toGigabyte(BigDecimal u, int scale, RoundingMode roundingMode) {
            return u.setScale(scale, roundingMode)
                    .divide(UNIT, roundingMode);
        }

        @Override
        public BigDecimal toTerabyte(BigDecimal u, int scale, RoundingMode roundingMode) {
            return u.setScale(scale, roundingMode)
                    .divide(UNIT, roundingMode)
                    .divide(UNIT, roundingMode);
        }
    },

    /**
     * GB
     */
    GB {
        @Override
        public BigDecimal toBit(BigDecimal u, int scale, RoundingMode roundingMode) {
            return u.setScale(scale, roundingMode)
                    .multiply(BIT_8)
                    .multiply(UNIT)
                    .multiply(UNIT)
                    .multiply(UNIT);
        }

        @Override
        public BigDecimal toByte(BigDecimal u, int scale, RoundingMode roundingMode) {
            return u.setScale(scale, roundingMode)
                    .multiply(UNIT)
                    .multiply(UNIT)
                    .multiply(UNIT);
        }

        @Override
        public BigDecimal toKilobyte(BigDecimal u, int scale, RoundingMode roundingMode) {
            return u.setScale(scale, roundingMode)
                    .multiply(UNIT)
                    .multiply(UNIT);
        }

        @Override
        public BigDecimal toMegabyte(BigDecimal u, int scale, RoundingMode roundingMode) {
            return u.setScale(scale, roundingMode)
                    .multiply(UNIT);
        }

        @Override
        public BigDecimal toGigabyte(BigDecimal u, int scale, RoundingMode roundingMode) {
            return u;
        }

        @Override
        public BigDecimal toTerabyte(BigDecimal u, int scale, RoundingMode roundingMode) {
            return u.setScale(scale, roundingMode)
                    .divide(UNIT, roundingMode);
        }
    },

    /**
     * TB
     */
    TB {
        @Override
        public BigDecimal toBit(BigDecimal u, int scale, RoundingMode roundingMode) {
            return u.setScale(scale, roundingMode)
                    .multiply(BIT_8)
                    .multiply(UNIT)
                    .multiply(UNIT)
                    .multiply(UNIT)
                    .multiply(UNIT);
        }

        @Override
        public BigDecimal toByte(BigDecimal u, int scale, RoundingMode roundingMode) {
            return u.setScale(scale, roundingMode)
                    .multiply(UNIT)
                    .multiply(UNIT)
                    .multiply(UNIT)
                    .multiply(UNIT);
        }

        @Override
        public BigDecimal toKilobyte(BigDecimal u, int scale, RoundingMode roundingMode) {
            return u.setScale(scale, roundingMode)
                    .multiply(UNIT)
                    .multiply(UNIT)
                    .multiply(UNIT);
        }

        @Override
        public BigDecimal toMegabyte(BigDecimal u, int scale, RoundingMode roundingMode) {
            return u.setScale(scale, roundingMode)
                    .multiply(UNIT)
                    .multiply(UNIT);
        }

        @Override
        public BigDecimal toGigabyte(BigDecimal u, int scale, RoundingMode roundingMode) {
            return u.setScale(scale, roundingMode)
                    .multiply(UNIT);
        }

        @Override
        public BigDecimal toTerabyte(BigDecimal u, int scale, RoundingMode roundingMode) {
            return u;
        }
    },

    ;

    private static final BigDecimal BIT_8 = BigDecimal.valueOf(8);

    private static final BigDecimal UNIT = BigDecimal.valueOf(1024);

    /**
     * 默认舍入模式
     */
    private static final RoundingMode DEFAULT_ROUND_MODE = RoundingMode.FLOOR;

    /**
     * 默认舍入精度
     */
    private static final int DEFAULT_SCALE = 2;

    public BigDecimal toBit(long u) {
        return this.toBit(BigDecimal.valueOf(u), DEFAULT_SCALE, DEFAULT_ROUND_MODE);
    }

    public BigDecimal toBit(long u, int scale, RoundingMode roundingMode) {
        return this.toBit(BigDecimal.valueOf(u), scale, roundingMode);
    }

    public BigDecimal toBit(BigDecimal u) {
        return this.toBit(u, DEFAULT_SCALE, DEFAULT_ROUND_MODE);
    }

    /**
     * ? -> bit
     *
     * @param u            unit
     * @param scale        scale
     * @param roundingMode roundingMode
     * @return bit
     */
    public abstract BigDecimal toBit(BigDecimal u, int scale, RoundingMode roundingMode);

    public BigDecimal toByte(long u) {
        return this.toByte(BigDecimal.valueOf(u), DEFAULT_SCALE, DEFAULT_ROUND_MODE);
    }

    public BigDecimal toByte(long u, int scale, RoundingMode roundingMode) {
        return this.toByte(BigDecimal.valueOf(u), scale, roundingMode);
    }

    public BigDecimal toByte(BigDecimal u) {
        return this.toByte(u, DEFAULT_SCALE, DEFAULT_ROUND_MODE);
    }

    /**
     * ? -> byte
     *
     * @param u            unit
     * @param scale        scale
     * @param roundingMode roundingMode
     * @return byte
     */
    public abstract BigDecimal toByte(BigDecimal u, int scale, RoundingMode roundingMode);

    public BigDecimal toKilobyte(long u) {
        return this.toKilobyte(BigDecimal.valueOf(u), DEFAULT_SCALE, DEFAULT_ROUND_MODE);
    }

    public BigDecimal toKilobyte(long u, int scale, RoundingMode roundingMode) {
        return this.toKilobyte(BigDecimal.valueOf(u), scale, roundingMode);
    }

    public BigDecimal toKilobyte(BigDecimal u) {
        return this.toKilobyte(u, DEFAULT_SCALE, DEFAULT_ROUND_MODE);
    }

    /**
     * ? -> KB
     *
     * @param u            unit
     * @param scale        scale
     * @param roundingMode roundingMode
     * @return KB
     */
    public abstract BigDecimal toKilobyte(BigDecimal u, int scale, RoundingMode roundingMode);

    public BigDecimal toMegabyte(long u) {
        return this.toMegabyte(BigDecimal.valueOf(u), DEFAULT_SCALE, DEFAULT_ROUND_MODE);
    }

    public BigDecimal toMegabyte(long u, int scale, RoundingMode roundingMode) {
        return this.toMegabyte(BigDecimal.valueOf(u), scale, roundingMode);
    }

    public BigDecimal toMegabyte(BigDecimal u) {
        return this.toMegabyte(u, DEFAULT_SCALE, DEFAULT_ROUND_MODE);
    }

    /**
     * ? -> MB
     *
     * @param u            unit
     * @param scale        scale
     * @param roundingMode roundingMode
     * @return MB
     */
    public abstract BigDecimal toMegabyte(BigDecimal u, int scale, RoundingMode roundingMode);

    public BigDecimal toGigabyte(long u) {
        return this.toGigabyte(BigDecimal.valueOf(u), DEFAULT_SCALE, DEFAULT_ROUND_MODE);
    }

    public BigDecimal toGigabyte(long u, int scale, RoundingMode roundingMode) {
        return this.toGigabyte(BigDecimal.valueOf(u), scale, roundingMode);
    }

    public BigDecimal toGigabyte(BigDecimal u) {
        return this.toGigabyte(u, DEFAULT_SCALE, DEFAULT_ROUND_MODE);
    }

    /**
     * ? -> GB
     *
     * @param u            unit
     * @param scale        scale
     * @param roundingMode roundingMode
     * @return GB
     */
    public abstract BigDecimal toGigabyte(BigDecimal u, int scale, RoundingMode roundingMode);

    public BigDecimal toTerabyte(long u) {
        return this.toTerabyte(BigDecimal.valueOf(u), DEFAULT_SCALE, DEFAULT_ROUND_MODE);
    }

    public BigDecimal toTerabyte(long u, int scale, RoundingMode roundingMode) {
        return this.toTerabyte(BigDecimal.valueOf(u), scale, roundingMode);
    }

    public BigDecimal toTerabyte(BigDecimal u) {
        return this.toTerabyte(u, DEFAULT_SCALE, DEFAULT_ROUND_MODE);
    }

    /**
     * ? -> TB
     *
     * @param u            unit
     * @param scale        scale
     * @param roundingMode roundingMode
     * @return TB
     */
    public abstract BigDecimal toTerabyte(BigDecimal u, int scale, RoundingMode roundingMode);

}
