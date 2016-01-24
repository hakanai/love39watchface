package org.trypticon.dozenalwatchface.time;

/**
 * Formatter for dozenal numbers.
 */
class DozenalNumberFormat {
    private final int padLength;

    DozenalNumberFormat(int padLength) {
        this.padLength = padLength;
    }

    String format(int number) {
        String noPadding = Integer.toString(number, 12);

        StringBuilder builder = new StringBuilder();
        int padSize = Math.max(0, padLength - noPadding.length());
        for (int i = 0; i < padSize; i++) {
            builder.append('0');
        }
        builder.append(noPadding);

        int length = builder.length();
        for (int i = 0; i < length; i++) {
            switch (builder.charAt(i)) {
                case 'a':
                    builder.setCharAt(i, '\u218A');
                    break;
                case 'b':
                    builder.setCharAt(i, '\u218B');
                    break;
            }
        }

        return builder.toString();
    }
}
