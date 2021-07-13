package com.example.lexis.utilities;

import android.graphics.Rect;
import android.text.Layout;
import android.text.SpannableString;
import android.text.style.ClickableSpan;
import android.widget.TextView;

import java.util.Calendar;

public class Utils {

    /*
    Return a Rect object representing the bounds of the clickableSpan that was clicked. Used to
    determine position of word meaning dialog box relative to clicked word.
    Adapted from: https://stackoverflow.com/questions/11905486/how-get-coordinate-of-a-clickablespan-inside-a-textview
    */
    public static Rect getClickedWordPosition(TextView parentTextView, ClickableSpan clickedText) {
        Rect parentTextViewRect = new Rect();
        SpannableString completeText = (SpannableString) (parentTextView).getText();
        Layout textViewLayout = parentTextView.getLayout();

        double startOffsetOfClickedText = completeText.getSpanStart(clickedText);
        double endOffsetOfClickedText = completeText.getSpanEnd(clickedText);
        double startXCoordinatesOfClickedText = textViewLayout.getPrimaryHorizontal((int) startOffsetOfClickedText);
        double endXCoordinatesOfClickedText = textViewLayout.getPrimaryHorizontal((int) endOffsetOfClickedText);

        // Get the rectangle of the clicked text
        int currentLineStartOffset = textViewLayout.getLineForOffset((int) startOffsetOfClickedText);
        textViewLayout.getLineBounds(currentLineStartOffset, parentTextViewRect);

        // Update the rectangle position to his real position on screen
        int[] parentTextViewLocation = {0,0};
        parentTextView.getLocationOnScreen(parentTextViewLocation);

        double parentTextViewTopAndBottomOffset = (
                parentTextViewLocation[1] -
                        parentTextView.getScrollY() +
                        parentTextView.getCompoundPaddingTop()
        );

        parentTextViewRect.top += parentTextViewTopAndBottomOffset;
        parentTextViewRect.bottom += parentTextViewTopAndBottomOffset;

        parentTextViewRect.left += (
                parentTextViewLocation[0] +
                        startXCoordinatesOfClickedText +
                        parentTextView.getCompoundPaddingLeft() -
                        parentTextView.getScrollX()
        );
        parentTextViewRect.right = (int) (
                parentTextViewRect.left +
                        endXCoordinatesOfClickedText -
                        startXCoordinatesOfClickedText
        );

        return parentTextViewRect;
    }

    /*
    Return a Calendar object representing yesterday's date. Used to get most recent top articles
    from Wikipedia (since today's top articles are usually not posted yet).
    */
    public static Calendar getYesterday() {
        final Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        return cal;
    }
}
