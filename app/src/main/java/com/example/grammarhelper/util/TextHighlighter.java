package com.example.grammarhelper.util;

import android.content.Context;
import android.content.res.Configuration;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import androidx.core.content.ContextCompat;
import com.example.grammarhelper.R;
import com.example.grammarhelper.model.GrammarError;

import java.util.List;

public class TextHighlighter {

    public static void clearHighlights(android.text.Editable editable) {
        if (editable == null) return;

        ForegroundColorSpan[] colorSpans = editable.getSpans(0, editable.length(), ForegroundColorSpan.class);
        for (ForegroundColorSpan span : colorSpans) {
            editable.removeSpan(span);
        }

        UnderlineSpan[] underlineSpans = editable.getSpans(0, editable.length(), UnderlineSpan.class);
        for (UnderlineSpan span : underlineSpans) {
            editable.removeSpan(span);
        }
    }

    public static void applyHighlights(Context context, android.text.Editable editable, List<GrammarError> errors) {
        if (editable == null) return;

        clearHighlights(editable);

        if (errors == null || errors.isEmpty()) return;

        for (GrammarError error : errors) {
            int start = error.positionStart;
            int end = error.positionEnd;

            // Ensure indices are within bounds of current text length
            if (start < 0 || end > editable.length() || start >= end) {
                continue;
            }

            int color = getColorForErrorType(context, error.errorType);

            editable.setSpan(new ForegroundColorSpan(color), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            editable.setSpan(new UnderlineSpan(), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
    }

    private static int getColorForErrorType(Context context, String errorType) {
        if (errorType == null) {
            return ContextCompat.getColor(context, R.color.error_red);
        }

        int nightModeFlags = context.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        boolean isDarkMode = nightModeFlags == Configuration.UI_MODE_NIGHT_YES;

        switch (errorType.toLowerCase()) {
            case "correctness":
                return ContextCompat.getColor(context, R.color.error_red);
            case "clarity":
                return ContextCompat.getColor(context, R.color.clarity_blue);
            case "tone":
                if (isDarkMode) {
                    return ContextCompat.getColor(context, R.color.tone_orange);
                } else {
                    return ContextCompat.getColor(context, R.color.tone_orange);
                }
            case "engagement":
                if (isDarkMode) {
                    return ContextCompat.getColor(context, R.color.engagement_purple);
                } else {
                    return ContextCompat.getColor(context, R.color.engagement_purple);
                }
            default:
                return ContextCompat.getColor(context, R.color.error_red);
        }
    }
}