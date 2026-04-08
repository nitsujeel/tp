package seedu.address.ui;

import static java.util.Objects.requireNonNull;

/**
 * Utilities for formatting text displayed in UI controls.
 */
public final class UiTextUtil {

    private UiTextUtil() {}

    /**
     * Inserts line breaks into long unbroken tokens so they can wrap in narrow UI regions.
     *
     * @param text The source text.
     * @param maxTokenLength Maximum length of a contiguous non-whitespace token before forcing a break.
     * @return Text with forced breaks for long tokens.
     */
    public static String wrapLongUnbrokenTokens(String text, int maxTokenLength) {
        requireNonNull(text);
        if (maxTokenLength <= 0) {
            throw new IllegalArgumentException("maxTokenLength must be positive.");
        }

        StringBuilder wrappedText = new StringBuilder();
        int currentTokenLength = 0;

        for (int i = 0; i < text.length(); i++) {
            char currentChar = text.charAt(i);
            if (Character.isWhitespace(currentChar)) {
                currentTokenLength = 0;
                wrappedText.append(currentChar);
                continue;
            }

            if (currentTokenLength > 0 && currentTokenLength % maxTokenLength == 0) {
                wrappedText.append('\n');
            }
            wrappedText.append(currentChar);
            currentTokenLength++;
        }

        return wrappedText.toString();
    }
}
