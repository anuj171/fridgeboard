package com.fridgeboard;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.SpannableStringBuilder;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

public class ExpandableTextView extends TextView {
	private static final int DEFAULT_TRIM_LENGTH = 150;
    private static final String ELLIPSIS = "\n.....";
    private static final int MAX_LINES = 3;

    private CharSequence originalText;
    private CharSequence trimmedText;
    private BufferType bufferType;
    private boolean trim = true;
    private int trimLength;

    public ExpandableTextView(Context context) {
        this(context, null);
    }

    public ExpandableTextView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ExpandableTextView);
        this.trimLength = typedArray.getInt(R.styleable.ExpandableTextView_trimLength, DEFAULT_TRIM_LENGTH);
        typedArray.recycle();

        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                trim = !trim;
                setText();
                requestFocusFromTouch();
            }
        });
    }

    private void setText() {
        super.setText(getDisplayableText(), bufferType);
    }

    private CharSequence getDisplayableText() {
        return trim ? trimmedText : originalText;
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        originalText = text;
        trimmedText = getTrimmedText(text);
        bufferType = type;
        setText();
    }

    private CharSequence getTrimmedText(CharSequence text) {
    	CharSequence trimText = text;
    	
    	int lineCount = 0;
    	int i = 0;
    	for (;i < trimText.length(); ++i) {
    		if (trimText.charAt(i) == '\n')
    			++lineCount;
    		
    		if (lineCount == MAX_LINES) {
    			trimText =  new SpannableStringBuilder(trimText, 0, i).append(ELLIPSIS);
    			break;
    		}
    	}
		
//        if (trimText != null && trimText.length() > trimLength) {
//        	trimText =  new SpannableStringBuilder(trimText, 0, trimLength + 1).append(ELLIPSIS);
//        }
        
        return trimText;
    }

    public CharSequence getOriginalText() {
        return originalText;
    }

    public void setTrimLength(int trimLength) {
        this.trimLength = trimLength;
        trimmedText = getTrimmedText(originalText);
        setText();
    }

    public int getTrimLength() {
        return trimLength;
    }
}
