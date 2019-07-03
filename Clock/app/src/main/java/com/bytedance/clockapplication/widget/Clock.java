package com.bytedance.clockapplication.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.text.Layout;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.style.RelativeSizeSpan;
import android.util.AttributeSet;
import android.view.View;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Clock extends View {

    private final static String TAG = Clock.class.getSimpleName();

    private static final int FULL_ANGLE = 360;

    private static final int CUSTOM_ALPHA = 140;
    private static final int FULL_ALPHA = 255;

    private static final int DEFAULT_PRIMARY_COLOR = Color.WHITE;
    private static final int DEFAULT_SECONDARY_COLOR = Color.LTGRAY;

    private static final float DEFAULT_DEGREE_STROKE_WIDTH = 0.010f;

    public final static int AM = 0;

    private static final int RIGHT_ANGLE = 90;

    private int mWidth, mCenterX, mCenterY, mRadius;

    /**
     * properties
     */
    private int centerInnerColor;
    private int centerOuterColor;

    private int secondsNeedleColor;
    private int hoursNeedleColor;
    private int minutesNeedleColor;

    private int degreesColor;

    private int hoursValuesColor;

    private int numbersColor;

    private boolean mShowAnalog = true;
    private double Hour;
    private int Minute, Second;

    public Clock(Context context) {
        super(context);
        init(context, null);
    }

    public Clock(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public Clock(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int size;
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        int widthWithoutPadding = width - getPaddingLeft() - getPaddingRight();
        int heightWithoutPadding = height - getPaddingTop() - getPaddingBottom();

        if (widthWithoutPadding > heightWithoutPadding) {
            size = heightWithoutPadding;
        } else {
            size = widthWithoutPadding;
        }

        setMeasuredDimension(size + getPaddingLeft() + getPaddingRight(), size + getPaddingTop() + getPaddingBottom());
    }

    private void init(Context context, AttributeSet attrs) {

        this.centerInnerColor = Color.LTGRAY;
        this.centerOuterColor = DEFAULT_PRIMARY_COLOR;

        this.secondsNeedleColor = DEFAULT_SECONDARY_COLOR;
        this.hoursNeedleColor = DEFAULT_PRIMARY_COLOR;
        this.minutesNeedleColor = DEFAULT_PRIMARY_COLOR;

        this.degreesColor = DEFAULT_PRIMARY_COLOR;

        this.hoursValuesColor = DEFAULT_PRIMARY_COLOR;

        numbersColor = Color.WHITE;
    }

    @Override
    protected void onDraw(final Canvas canvas) {
        super.onDraw(canvas);

        mWidth = getHeight() > getWidth() ? getWidth() : getHeight();

        int halfWidth = mWidth / 2;
        mCenterX = halfWidth;
        mCenterY = halfWidth;
        mRadius = halfWidth;

        if (mShowAnalog) {
            drawDegrees(canvas);
            drawHoursValues(canvas);
            drawNeedles(canvas);
            drawCenter(canvas);
        } else {
            drawNumbers(canvas);
        }

    }

    private void drawDegrees(Canvas canvas) {

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeWidth(mWidth * DEFAULT_DEGREE_STROKE_WIDTH);
        paint.setColor(degreesColor);

        int rPadded = mCenterX - (int) (mWidth * 0.01f);
        int rEnd = mCenterX - (int) (mWidth * 0.05f);

        for (int i = 0; i < FULL_ANGLE; i += 6 /* Step */) {

            if ((i % RIGHT_ANGLE) != 0 && (i % 15) != 0)
                paint.setAlpha(CUSTOM_ALPHA);
            else {
                paint.setAlpha(FULL_ALPHA);
            }

            int startX = (int) (mCenterX + rPadded * Math.cos(Math.toRadians(i)));
            int startY = (int) (mCenterX - rPadded * Math.sin(Math.toRadians(i)));

            int stopX = (int) (mCenterX + rEnd * Math.cos(Math.toRadians(i)));
            int stopY = (int) (mCenterX - rEnd * Math.sin(Math.toRadians(i)));

            canvas.drawLine(startX, startY, stopX, stopY, paint);

        }
    }

    /**
     * @param canvas
     */
    private void drawNumbers(Canvas canvas) {

        TextPaint textPaint = new TextPaint();
        textPaint.setTextSize(mWidth * 0.2f);
        textPaint.setColor(numbersColor);
        textPaint.setColor(numbersColor);
        textPaint.setAntiAlias(true);

        Calendar calendar = Calendar.getInstance();

        int hour = calendar.get(Calendar.HOUR);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);
        int amPm = calendar.get(Calendar.AM_PM);

        String time = String.format("%s:%s:%s%s",
                String.format(Locale.getDefault(), "%02d", hour),
                String.format(Locale.getDefault(), "%02d", minute),
                String.format(Locale.getDefault(), "%02d", second),
                amPm == AM ? "AM" : "PM");

        SpannableStringBuilder spannableString = new SpannableStringBuilder(time);
        spannableString.setSpan(new RelativeSizeSpan(0.3f), spannableString.toString().length() - 2, spannableString.toString().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); // se superscript percent

        StaticLayout layout = new StaticLayout(spannableString, textPaint, canvas.getWidth(), Layout.Alignment.ALIGN_CENTER, 1, 1, true);
        canvas.translate(mCenterX - layout.getWidth() / 2f, mCenterY - layout.getHeight() / 2f);
        layout.draw(canvas);
    }

    /**
     * Draw Hour Text Values, such as 1 2 3 ...
     *
     * @param canvas
     */
    private void drawHoursValues(Canvas canvas) {
        // Default Color:
        // - hoursValuesColor
        TextPaint textPaint = new TextPaint();
        textPaint.setTextSize(mWidth * 0.07f);
        textPaint.setColor(degreesColor);
        textPaint.setAntiAlias(true);

        textPaint.setTextAlign(TextPaint.Align.CENTER);
        TextPaint.FontMetrics fontMetrics = textPaint.getFontMetrics();
        float top = fontMetrics.top;
        float bottom = fontMetrics.bottom;

        int rPadded = mCenterX - (int) (mWidth * 0.01f);
        int rEnd = mCenterX - (int) (mWidth * 0.05f);
        int count = 3;

        for (int i = 0; i < FULL_ANGLE; i += 6 /* Step */) {

            if ((i % RIGHT_ANGLE) != 0 && (i % 15) != 0)
                textPaint.setAlpha(CUSTOM_ALPHA);
            else {
                textPaint.setAlpha(FULL_ALPHA);
            }

            int stopX = (int) (mCenterX + rEnd * Math.cos(Math.toRadians(i))*0.9);
            int stopY = (int) (mCenterX - rEnd * Math.sin(Math.toRadians(i))*0.9 - top / 2 -bottom / 2);

            if (i % 30 == 0 && count > 0){
                canvas.drawText(count + "", stopX, stopY, textPaint);
                count--;
                if (count == 0){
                    count = 12;
                }
            }

        }

    }

    /**
     * Draw hours, minutes needles
     * Draw progress that indicates hours needle disposition.
     *
     * @param canvas
     */
    private void drawNeedles(final Canvas canvas) {
        // Default Color:
        // - secondsNeedleColor
        // - hoursNeedleColor
        // - minutesNeedleColor
//        SimpleDateFormat formate = new SimpleDateFormat("HH-mm-ss");
//        String time = formate.format(new Date(System.currentTimeMillis()));
//        String[] split = time.split("-");
//        int hour1 = Integer.parseInt(split[0]);
//        int minute1 = Integer.parseInt(split[1]);
//        int second1 = Integer.parseInt(split[2]);
//
//        int hourAngle = hour1 * 30 + minute1 / 2;
//        int minAngle = minute1 * 6 + second1 / 10;
//        int secondAngle = second1 * 6;
        Calendar calendar = Calendar.getInstance();

        Hour = calendar.get(Calendar.HOUR);
        Minute = calendar.get(Calendar.MINUTE);
        Second = calendar.get(Calendar.SECOND);

        Paint min = new Paint();
        min.setColor(Color.BLUE);
        min.setStrokeWidth(10);
        min.setStrokeCap(Paint.Cap.ROUND);


        float rEnd = mCenterX -  (mWidth * 0.23f);
        float startX = (float) (mCenterX);
        float startY = (float) (mCenterX);

        float stopX = (float) (mCenterX + rEnd * Math.sin(Math.toRadians(Minute * 6)));
        float stopY = (float) (mCenterX - rEnd * Math.cos(Math.toRadians(Minute * 6)));

        canvas.drawLine(startX, startY, stopX, stopY, min);

        Paint second = new Paint();
        second.setColor(Color.GREEN);
        second.setStrokeWidth(5);
        second.setStrokeCap(Paint.Cap.ROUND);

        rEnd = mCenterX -  (mWidth * 0.13f);
        startX = (mCenterX);
        startY = (mCenterX);

        stopX = (float) (mCenterX + rEnd * Math.sin(Math.toRadians(Second * 6)));
        stopY = (float) (mCenterX - rEnd * Math.cos(Math.toRadians(Second * 6)));

        canvas.drawLine(startX, startY, stopX, stopY, second);

        Paint hour = new Paint();
        hour.setColor(Color.RED);
        hour.setStrokeWidth(15);
        hour.setStrokeCap(Paint.Cap.ROUND);

        rEnd = mCenterX - (mWidth * 0.30f);
        startX = (mCenterX);
        startY = (mCenterX);

        stopX = (float) (mCenterX + rEnd * Math.sin(Math.toRadians(Hour * 30)));
        stopY = (float) (mCenterX - rEnd * Math.cos(Math.toRadians(Hour * 30)));

        canvas.drawLine(startX, startY, stopX, stopY, hour);

    }

    /**
     * Draw Center Dot
     *
     * @param canvas
     */
    private void drawCenter(Canvas canvas) {
        // Default Color:
        // - centerInnerColor
        // - centerOuterColor
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setStrokeWidth(30);

        Paint paint1 = new Paint();
        paint1.setColor(Color.BLACK);
        paint1.setStrokeWidth(50);

        paint.setStyle(Paint.Style.STROKE);
        canvas.drawCircle(mCenterX, mCenterY, 3, paint);

        paint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(mCenterX, mCenterY, 10, paint1);
    }

    public void setShowAnalog(boolean showAnalog) {
        mShowAnalog = showAnalog;
        invalidate();
    }

    public boolean isShowAnalog() {
        return mShowAnalog;
    }


}