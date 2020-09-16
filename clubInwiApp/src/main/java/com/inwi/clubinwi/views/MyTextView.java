package com.inwi.clubinwi.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import com.inwi.clubinwi.R;
import com.inwi.clubinwi.Utils.Constants;
import com.inwi.clubinwi.Utils.TextJustifyUtils;

public class MyTextView extends TextView {

	private Context		mContext;
	private Typeface	mTypeface;
	private int			mTextStyle;

	private Paint		paint					= new Paint();

	private String[]	blocks;
	private float		spaceOffset				= 0;
	private float		horizontalOffset		= 0;
	private float		verticalOffset			= 0;
	private float		horizontalFontOffset	= 0;
	private float		dirtyRegionWidth		= 0;
	private boolean		wrapEnabled				= false;

	private float		strecthOffset;
	private float		wrappedEdgeSpace;
	private String		block;
	private String		wrappedLine;
	private String[]	lineAsWords;
	private Object[]	wrappedObj;

	private Bitmap		cache					= null;
	private boolean		cacheEnabled			= false;

	public MyTextView(final Context context) {
		this(context, null);
		init(context);
	}

	public MyTextView(final Context context, final AttributeSet attrs) {
		this(context, attrs, 0);
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.fonts);
		mTextStyle = a.getInteger(R.styleable.fonts_font_style, 2);
		init(context);
		a.recycle();
	}

	public MyTextView(final Context context, final AttributeSet attrs, final int defStyle) {
		super(context, attrs, defStyle);
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.fonts);
		mTextStyle = a.getInteger(R.styleable.fonts_font_style, 2);
		init(context);
		a.recycle();
	}

	private void init(Context context) {
		mContext = context;
		if (!isInEditMode()) {
			setTypeface();
		}
	}

	public void setTextStyle(int style) {
		mTextStyle = style;
		setTypeface();
	}

	private void setTypeface() {
		if (mTextStyle == Constants.FONT_STYLE_LIGHT)
			mTypeface = Typeface.createFromAsset(mContext.getAssets(), Constants.FONT_NAME_LIGHT);
		else if (mTextStyle == Constants.FONT_STYLE_BOLD)
			mTypeface = Typeface.createFromAsset(mContext.getAssets(), Constants.FONT_NAME_BOLD);
		else
			mTypeface = Typeface.createFromAsset(mContext.getAssets(), Constants.FONT_NAME_REGULAR);
		setTypeface(mTypeface);
	}

	public void setText(String st, boolean wrap) {
		wrapEnabled = wrap;
		super.setText(st);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// If wrap is disabled then,
		// request original onDraw
		if (!wrapEnabled) {
			super.onDraw(canvas);
			return;
		}

		// Active canas needs to be set
		// based on cacheEnabled
		Canvas activeCanvas = null;

		// Set the active canvas based on
		// whether cache is enabled
		if (cacheEnabled) {

			if (cache != null) {
				// Draw to the OS provided canvas
				// if the cache is not empty
				canvas.drawBitmap(cache, 0, 0, paint);
				return;
			} else {
				// Create a bitmap and set the activeCanvas
				// to the one derived from the bitmap
				cache = Bitmap.createBitmap(getWidth(), getHeight(), Config.ARGB_4444);
				activeCanvas = new Canvas(cache);
			}
		} else {
			// Active canvas is the OS
			// provided canvas
			activeCanvas = canvas;
		}

		// Pull widget properties
		paint.setColor(getCurrentTextColor());
		paint.setTypeface(getTypeface());
		paint.setTextSize(getTextSize());

		dirtyRegionWidth = getWidth();
		int maxLines = Integer.MAX_VALUE;
		int currentapiVersion = android.os.Build.VERSION.SDK_INT;
		if (currentapiVersion >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
			maxLines = getLineCount();
		}
		int lines = 1;
		blocks = getText().toString().split("((?<=\n)|(?=\n))");
		verticalOffset = horizontalFontOffset = getLineHeight() - 0.5f; // Temp
																		// fix
		spaceOffset = paint.measureText(" ");

		for (int i = 0; i < blocks.length && lines <= maxLines; i++) {
			block = blocks[i];
			horizontalOffset = 0;

			if (block.length() == 0) {
				continue;
			} else if (block.equals("\n")) {
				verticalOffset += horizontalFontOffset;
				continue;
			}

			block = block.trim();

			if (block.length() == 0) {
				continue;
			}

			wrappedObj = TextJustifyUtils.createWrappedLine(block, paint, spaceOffset, dirtyRegionWidth);

			wrappedLine = ((String) wrappedObj[0]);
			wrappedEdgeSpace = (Float) wrappedObj[1];
			lineAsWords = wrappedLine.split(" ");
			strecthOffset = wrappedEdgeSpace != Float.MIN_VALUE ? wrappedEdgeSpace / (lineAsWords.length - 1) : 0;

			for (int j = 0; j < lineAsWords.length; j++) {
				String word = lineAsWords[j];
				if (lines == maxLines && j == lineAsWords.length - 1) {
					activeCanvas.drawText("...", horizontalOffset, verticalOffset, paint);
				} else {
					activeCanvas.drawText(word, horizontalOffset, verticalOffset, paint);
				}

				horizontalOffset += paint.measureText(word) + spaceOffset + strecthOffset;
			}

			lines++;

			if (blocks[i].length() > 0) {
				blocks[i] = blocks[i].substring(wrappedLine.length());
				verticalOffset += blocks[i].length() > 0 ? horizontalFontOffset : 0;
				i--;
			}
		}

		if (cacheEnabled) {
			// Draw the cache onto the OS provided
			// canvas.
			canvas.drawBitmap(cache, 0, 0, paint);
		}
	}
}