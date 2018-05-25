package com.baigu.dms.common.view.clip;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.baigu.dms.common.utils.ViewUtils;

public class ClipImageBorderView extends View
{
	private static final PorterDuffXfermode clearMode=new PorterDuffXfermode(Mode.CLEAR);
	/**
	 * 水平方向与View的边距
	 */
	private int mHorizontalPadding;
	/**
	 * 边框的宽度 单位dp
	 */
	private int mBorderWidth = 2;

	private Paint mPaint;

	public ClipImageBorderView(Context context)
	{
		this(context, null);
	}

	public ClipImageBorderView(Context context, AttributeSet attrs)
	{
		this(context, attrs, 0);
	}

	public ClipImageBorderView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
	
		mBorderWidth = (int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, mBorderWidth, getResources()
						.getDisplayMetrics());
		mPaint = new Paint();
		mPaint.setAntiAlias(true);
	}

	@Override
	protected void onDraw(Canvas canvas)
	{
		super.onDraw(canvas);
		this.setAlpha(0.5f);
		// 绘制边框
//		mPaint.setXfermode(null);
		mPaint.setXfermode(clearMode);
		mPaint.setColor(Color.parseColor("#80000000"));
		mPaint.setStyle(Style.FILL);
		canvas.drawColor(Color.parseColor("#88000000"));

		//方形边框
//		canvas.drawRect(mHorizontalPadding, mVerticalPadding, getWidth()- mHorizontalPadding, getHeight() - mVerticalPadding, mPaint);
		//圆形边框
		canvas.drawCircle( getWidth()/2, getHeight()/2, getWidth()/2-mHorizontalPadding, mPaint);
		Paint paint = new Paint();
		paint.setStrokeWidth(ViewUtils.dip2px(2));
		paint.setColor(Color.parseColor("#ffffff"));
		paint.setAntiAlias(true);
		paint.setStyle(Style.STROKE);
		canvas.drawCircle( getWidth()/2, getHeight()/2, getWidth()/2-mHorizontalPadding, paint);

	}

	public void setHorizontalPadding(int mHorizontalPadding)
	{
		this.mHorizontalPadding = mHorizontalPadding;
		
	}

}
