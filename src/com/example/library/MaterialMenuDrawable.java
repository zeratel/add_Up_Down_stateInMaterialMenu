/*
 * Copyright (C) 2014 Balys Valentukevicius
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.library;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.util.TypedValue;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorListenerAdapter;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.util.Property;

import static android.graphics.Paint.Style;
import static com.nineoldandroids.animation.Animator.AnimatorListener;

public class MaterialMenuDrawable extends Drawable implements Animatable {

	public enum IconState {
		BURGER, ARROW, X, CHECK, UP, DOWN, PLUS
	}

	public enum AnimationState {
		BURGER_ARROW, BURGER_X, BURGER_CHECK, BURGER_UP, BURGER_DOWN, BURGER_PLUS,
		ARROW_X, ARROW_CHECK, ARROW_UP, ARROW_DOWN, ARROW_PLUS,
		X_CHECK, X_UP, X_DOWN, X_PLUS,
		CHECK_UP, CHECK_DOWN, CHECK_PLUS,
		UP_DOWN, UP_PLUS,
		DOWN_PLUS;

		public IconState getFirstState() {
			switch (this) {
			case BURGER_ARROW:
				return IconState.BURGER;
			case BURGER_X:
				return IconState.BURGER;
			case BURGER_CHECK:
				return IconState.BURGER;
			case BURGER_UP:
				return IconState.BURGER;
			case BURGER_DOWN:
				return IconState.BURGER;
			case BURGER_PLUS:
				return IconState.BURGER;
			case ARROW_X:
				return IconState.ARROW;
			case ARROW_CHECK:
				return IconState.ARROW;
			case ARROW_UP:
				return IconState.ARROW;
			case ARROW_DOWN:
				return IconState.ARROW;
			case ARROW_PLUS:
				return IconState.ARROW;
			case X_CHECK:
				return IconState.X;
			case X_UP:
				return IconState.X;
			case X_DOWN:
				return IconState.X;
			case X_PLUS:
				return IconState.X;
			case CHECK_UP:
				return IconState.CHECK;
			case CHECK_DOWN:
				return IconState.CHECK;
			case CHECK_PLUS:
				return IconState.CHECK;
			case UP_DOWN:
				return IconState.UP;
			case UP_PLUS:
				return IconState.UP;
			case DOWN_PLUS:
				return IconState.DOWN;
			default:
				return null;
			}
		}

		public IconState getSecondState() {
			switch (this) {
			case BURGER_ARROW:
				return IconState.ARROW;
			case BURGER_X:
				return IconState.X;
			case BURGER_CHECK:
				return IconState.CHECK;
			case BURGER_UP:
				return IconState.UP;
			case BURGER_DOWN:
				return IconState.DOWN;
			case BURGER_PLUS:
				return IconState.PLUS;
			case ARROW_X:
				return IconState.X;
			case ARROW_UP:
				return IconState.UP;
			case ARROW_DOWN:
				return IconState.DOWN;
			case ARROW_CHECK:
				return IconState.CHECK;
			case ARROW_PLUS:
				return IconState.PLUS;
			case X_CHECK:
				return IconState.CHECK;
			case X_UP:
				return IconState.UP;
			case X_DOWN:
				return IconState.DOWN;
			case X_PLUS:
				return IconState.PLUS;
			case CHECK_UP:
				return IconState.UP;
			case CHECK_DOWN:
				return IconState.DOWN;
			case CHECK_PLUS:
				return IconState.PLUS;
			case UP_DOWN:
				return IconState.DOWN;
			case UP_PLUS:
				return IconState.PLUS;
			case DOWN_PLUS:
				return IconState.PLUS;
			default:
				return null;
			}
		}
	}

	public enum Stroke {
		/**
		 * 3 dip
		 */
		REGULAR(3),
		/**
		 * 2 dip
		 */
		THIN(2),
		/**
		 * 1 dip
		 */
		EXTRA_THIN(1);

		private final int strokeWidth;

		Stroke(int strokeWidth) {
			this.strokeWidth = strokeWidth;
		}

		protected static Stroke valueOf(int strokeWidth) {
			switch (strokeWidth) {
			case 3:
				return REGULAR;
			case 2:
				return THIN;
			case 1:
				return EXTRA_THIN;
			default:
				return THIN;
			}
		}
	}

	public static final int DEFAULT_COLOR = Color.WHITE;
	public static final int DEFAULT_SCALE = 1;
	public static final int DEFAULT_TRANSFORM_DURATION = 800;
	public static final int DEFAULT_PRESSED_DURATION = 400;

	private static final int BASE_DRAWABLE_WIDTH = 40;
	private static final int BASE_DRAWABLE_HEIGHT = 40;
	private static final int BASE_ICON_WIDTH = 20;
	private static final int BASE_CIRCLE_RADIUS = 18;

	private static final float ARROW_MID_LINE_ANGLE = 180;
	private static final float ARROW_TOP_LINE_ANGLE = 135;
	private static final float ARROW_BOT_LINE_ANGLE = 225;
	private static final float X_TOP_LINE_ANGLE = 44;
	private static final float X_BOT_LINE_ANGLE = -44;
	private static final float X_ROTATION_ANGLE = 90;
	private static final float CHECK_MIDDLE_ANGLE = 135;
	private static final float CHECK_BOTTOM_ANGLE = -90;
	private static final float UP_TOP_ANGLE = -30;
	private static final float UP_BOTTOM_ANGLE = 30;
	private static final float PLUS_TOP_ANGLE = 46;
	private static final float PLUS_BOTTOM_ANGLE = 44;

	private static final float TRANSFORMATION_START = 0;
	private static final float TRANSFORMATION_MID = 1.0f;
	private static final float TRANSFORMATION_END = 2.0f;

	private static final int DEFAULT_CIRCLE_ALPHA = 200;

	private final float diph;
	private final float dip1;
	private final float dip2;
	private final float dip3;
	private final float dip4;
	private final float dip6;
	private final float dip8;

	private final int width;
	private final int height;
	private final float strokeWidth;
	private final float iconWidth;
	private final float topPadding;
	private final float sidePadding;
	private final float circleRadius;

	private final Stroke stroke;

	private final Object lock = new Object();

	private final Paint iconPaint = new Paint();
	private final Paint circlePaint = new Paint();

	private float transformationValue = 0f;
	private float pressedProgressValue = 0f;
	private boolean transformationRunning = false;

	private IconState currentIconState = IconState.BURGER;
	private AnimationState animationState = AnimationState.BURGER_ARROW;

	private IconState animatingIconState;
	private boolean drawTouchCircle;
	private boolean neverDrawTouch;
	private boolean rtlEnabled;

	private ObjectAnimator transformation;
	private ObjectAnimator pressedCircle;
	private AnimatorListener animatorListener;

	private MaterialMenuState materialMenuState;
	
	private boolean isDrawCircle = true;

	public MaterialMenuDrawable(Context context, int color, Stroke stroke) {
		this(context, color, stroke, DEFAULT_SCALE, DEFAULT_TRANSFORM_DURATION,
				DEFAULT_PRESSED_DURATION);
	}

	public MaterialMenuDrawable(Context context, int color, Stroke stroke,
			int transformDuration, int pressedDuration) {
		this(context, color, stroke, DEFAULT_SCALE, transformDuration,
				pressedDuration);
	}

	public MaterialMenuDrawable(Context context, int color, Stroke stroke,
			int scale, int transformDuration, int pressedDuration) {
		Resources resources = context.getResources();
		// convert each separately due to various densities
		this.dip1 = dpToPx(resources, 1) * scale;
		this.dip2 = dpToPx(resources, 2) * scale;
		this.dip3 = dpToPx(resources, 3) * scale;
		this.dip4 = dpToPx(resources, 4) * scale;
		this.dip6 = dpToPx(resources, 6) * scale;
		this.dip8 = dpToPx(resources, 8) * scale;
		this.diph = dip1 / 2;

		this.stroke = stroke;
		this.width = (int) (dpToPx(resources, BASE_DRAWABLE_WIDTH) * scale);
		this.height = (int) (dpToPx(resources, BASE_DRAWABLE_HEIGHT) * scale);
		this.iconWidth = dpToPx(resources, BASE_ICON_WIDTH) * scale;
		this.circleRadius = dpToPx(resources, BASE_CIRCLE_RADIUS) * scale;
		this.strokeWidth = dpToPx(resources, stroke.strokeWidth) * scale;

		this.sidePadding = (width - iconWidth) / 2;
		this.topPadding = (height - 5 * dip3) / 2;

		initPaint(color);
		initAnimations(transformDuration, pressedDuration);

		materialMenuState = new MaterialMenuState();
		
	}

	private MaterialMenuDrawable(int color, Stroke stroke,
			long transformDuration, long pressedDuration, int width,
			int height, float iconWidth, float circleRadius, float strokeWidth,
			float dip1) {
		this.dip1 = dip1;
		this.dip2 = dip1 * 2;
		this.dip3 = dip1 * 3;
		this.dip4 = dip1 * 4;
		this.dip6 = dip1 * 6;
		this.dip8 = dip1 * 8;
		this.diph = dip1 / 2;
		this.stroke = stroke;
		this.width = width;
		this.height = height;
		this.iconWidth = iconWidth;
		this.circleRadius = circleRadius;
		this.strokeWidth = strokeWidth;
		this.sidePadding = (width - iconWidth) / 2;
		this.topPadding = (height - 5 * dip3) / 2;

		initPaint(color);
		initAnimations((int) transformDuration, (int) pressedDuration);

		materialMenuState = new MaterialMenuState();
	}

	private void initPaint(int color) {
		iconPaint.setAntiAlias(true);
		iconPaint.setStyle(Style.STROKE);
		iconPaint.setStrokeWidth(strokeWidth);
		iconPaint.setColor(color);

		circlePaint.setAntiAlias(true);
		circlePaint.setStyle(Style.FILL);
		circlePaint.setColor(color);
		circlePaint.setAlpha(DEFAULT_CIRCLE_ALPHA);

		setBounds(0, 0, width, height);
	}

	/*
	 * Drawing
	 */

	@Override
	public void draw(Canvas canvas) {
		final float ratio = transformationValue <= 1 ? transformationValue
				: 2 - transformationValue;

		if (rtlEnabled) {
			canvas.save();
			canvas.scale(-1, 1, 0, 0);
			canvas.translate(-getIntrinsicWidth(), 0);
		}

		drawTopLine(canvas, ratio);
		drawMiddleLine(canvas, ratio);
		drawBottomLine(canvas, ratio);

		if (rtlEnabled) {
			canvas.restore();
		}

		if (drawTouchCircle)
			drawTouchCircle(canvas);
	}

	private void drawTouchCircle(Canvas canvas) {
		canvas.restore();
		if (isDrawCircle) {
			canvas.drawCircle(width / 2, height / 2, pressedProgressValue,
					circlePaint);
		}
	}

	/**
	 * 
	* <p>Author: LHF</p>
	* <p> @date 2015��3��26�� ����6:05:57 </p>
	* <p>Description: 
	* 
	* 	����Ҳ��д���Ķ���Ч��
	* 	��Ҫ˼����ǣ�ץס��һ��ͼ�εĽ���״̬����Ҫ���ͼ�ε�״̬��Ȼ��ratio���0��1��ͼ����Ȼ��Ȼ�Ķ�����
	* 	������Ҫ Arrow_up �ҵ�B_A��B_up����
	* 
	* 
	* 
	* 
	* 
	* 
	* 
	* </p> 
	* @param canvas
	* @param ratio
	 */
	
	
	// ���߶���
	private void drawMiddleLine(Canvas canvas, float ratio) {
		canvas.restore();
		canvas.save();

		float rotation = 0;
		float pivotX = width / 2;
		float pivotY = width / 2;
		float startX = sidePadding;
		float startY = topPadding + dip3 / 2 * 5;
		float stopX = width - sidePadding;
		float stopY = topPadding + dip3 / 2 * 5;
		int alpha = 255;

		switch (animationState) {
		case BURGER_ARROW:
			// rotate by 180
			if (isMorphingForward()) {
				rotation = ratio * ARROW_MID_LINE_ANGLE;
			} else {
				rotation = ARROW_MID_LINE_ANGLE + (1 - ratio)
						* ARROW_MID_LINE_ANGLE;
			}
			// shorten one end
			stopX -= ratio * resolveStrokeModifier(ratio) / 2;
			break;
		case BURGER_X:
			// fade out
			alpha = (int) ((1 - ratio) * 255);
			break;
		case BURGER_CHECK:
			// rotate until required angle
			rotation = ratio * CHECK_MIDDLE_ANGLE;
			// lengthen both ends
			startX += ratio * (dip4 + dip3 / 2);
			stopX += ratio * dip1;
			pivotX = width / 2 + dip3 + diph;
			break;
		case BURGER_UP:
			// �˶�̫���⡣����
			// rotation = CHECK_MIDDLE_ANGLE * ratio;
			// startX += (dip4 + dip3 / 2) * ratio;
			// stopX += dip1 * ratio;
			// pivotX = (width / 2 + dip3 + diph)* ratio;
			alpha = (int) ((1 - ratio) * 255);
			break;
		case BURGER_DOWN:
			// �˶�̫���⡣����
			// rotation = CHECK_MIDDLE_ANGLE * ratio;
			// startX += (dip4 + dip3 / 2) * ratio;
			// stopX += dip1 * ratio;
			// pivotX = (width / 2 + dip3 + diph) * ratio;
			alpha = (int) ((1 - ratio) * 255);
			break;
		case BURGER_PLUS:
			// fade out
			alpha = (int) ((1 - ratio) * 255);
			break;
		case ARROW_X:
			// fade out and shorten one end
			alpha = (int) ((1 - ratio) * 255);
			startX += (1 - ratio) * dip2;
			break;
		case ARROW_CHECK:
			if (isMorphingForward()) {
				// rotate until required angle
				rotation = ratio * CHECK_MIDDLE_ANGLE;
			} else {
				// rotate back to starting angle
				rotation = CHECK_MIDDLE_ANGLE - CHECK_MIDDLE_ANGLE
						* (1 - ratio);
			}
			// shorten one end and lengthen the other
			startX += dip3 / 2 + dip4 - (1 - ratio) * dip2;
			stopX += ratio * dip1;
			pivotX = width / 2 + dip3 + diph;
			break;
		case ARROW_UP:
			startX += (1 - ratio) * dip2;
			alpha = (int) ((1 - ratio) * 255);
			break;
		case ARROW_DOWN:
			startX += (1 - ratio) * dip2;
			alpha = (int) ((1 - ratio) * 255);
			break;
		case ARROW_PLUS:
			// fade out and shorten one end
			alpha = (int) ((1 - ratio) * 255);
			startX += (1 - ratio) * dip2;
			break;
		case X_CHECK:
			// fade in
			alpha = (int) (ratio * 255);
			// rotation to check angle
			rotation = ratio * CHECK_MIDDLE_ANGLE;
			// lengthen both ends
			startX += ratio * (dip4 + dip3 / 2);
			stopX += ratio * dip1;
			pivotX = width / 2 + dip3 + diph;
			break;
		case X_UP:
			alpha = 0;
			break;
		case X_DOWN:
			alpha = 0;
			break;
		case X_PLUS:
			alpha = 0;
			break;
		case CHECK_UP:
			// ����check״̬��ֻ�ǵ���
			// rotation to check angle
			rotation = CHECK_MIDDLE_ANGLE * (1 - ratio);
			// lengthen both ends
			startX += (dip4 + dip3 / 2) * (1 - ratio);
			stopX += dip1 * (1 - ratio);
			pivotX = (width / 2 + dip3 + diph) * (1 - ratio);
			// fade out
			alpha = (int) ((1 - ratio) * 255);
			break;
		case CHECK_DOWN:
			// rotation to check angle
			rotation = CHECK_MIDDLE_ANGLE * (1 - ratio);
			// lengthen both ends
			startX += (dip4 + dip3 / 2) * (1 - ratio);
			stopX += dip1 * (1 - ratio);
			pivotX = (width / 2 + dip3 + diph) * (1 - ratio);
			// fade out
			alpha = (int) ((1 - ratio) * 255);

			break;
			//TODO
		case CHECK_PLUS:
			// rotation to check angle
			rotation = CHECK_MIDDLE_ANGLE * (1 - ratio);
			// lengthen both ends
			startX += (dip4 + dip3 / 2) * (1 - ratio);
			stopX += dip1 * (1 - ratio);
			pivotX = (width / 2 + dip3 + diph) * (1 - ratio);
			// fade out
			alpha = (int) ((1 - ratio) * 255);
			
			break;
		case UP_DOWN:
			alpha = 0;
		case UP_PLUS:
			alpha = 0;
		case DOWN_PLUS:
			alpha = 0;
			break;
		default:
			break;

		}

		iconPaint.setAlpha(alpha);// 255 -> 0 A->
		canvas.rotate(rotation, pivotX, pivotY);// 0 400 400 -> 0 400 400
		canvas.drawLine(startX, startY, stopX, stopY, iconPaint);// 240 400 600
																	// 400 ->
																	// 200 400
																	// 600 400
		iconPaint.setAlpha(255);
	}

	// ���ߵĶ���
	private void drawTopLine(Canvas canvas, float ratio) {
		canvas.save();

		float rotation = 0, pivotX = 0, pivotY = 0;
		float rotation2 = 0;
		// pivot at center of line
		float pivotX2 = width / 2 + dip3 / 2;
		float pivotY2 = topPadding + dip2;

		float startX = sidePadding;
		float startY = topPadding + dip2;
		float stopX = width - sidePadding;
		float stopY = topPadding + dip2;
		int alpha = 255;
		// ratio 0.0 -> 1.0
		// Log.i("LHF", "MaterialMenuDrawable.drawTopLine.ratio:"+ratio);

		switch (animationState) {
		case BURGER_ARROW:
			if (isMorphingForward()) {
				// rotate until required angle
				rotation = ratio * ARROW_BOT_LINE_ANGLE;
			} else {
				// rotate back to start doing a 360
				rotation = ARROW_BOT_LINE_ANGLE + (1 - ratio)
						* ARROW_TOP_LINE_ANGLE;
			}
			// rotate by middle
			pivotX = width / 2;
			pivotY = height / 2;

			// shorten both ends
			stopX -= resolveStrokeModifier(ratio);
			startX += dip3 * ratio;

			break;
		case BURGER_X:
			// rotate until required angles
			rotation = X_TOP_LINE_ANGLE * ratio;
			rotation2 = X_ROTATION_ANGLE * ratio;

			// pivot at left corner of line
			pivotX = sidePadding + dip4;
			pivotY = topPadding + dip3;

			// shorten one end
			startX += dip3 * ratio;
			break;
		case BURGER_CHECK:
			// fade out
			alpha = (int) ((1 - ratio) * 255);
			break;
		case BURGER_UP:
			// rotation = -360 * ratio;//-30
			pivotX = width / 2;
			pivotY = height / 2;
			rotation2 = UP_TOP_ANGLE * ratio; // ��Ŧ2�Ѿ����ߵ��е��ˡ�����
			startX -= dip3 * ratio; // ע�� start��+������ stop��-������
			stopX -= (dip8 + dip2) * ratio; // ע�� start��+������ stop��-������
			// ע�� /����TOP ����ϵ -> �Ҳ�Ϊ0
			// ֹͣ����ʱ��ͼ��
			// Log.i("LHF", "MaterialMenuDrawable.drawTopLine.ratio:"+ratio);
			// rotation = -30;//-30
			// pivotX = width / 2;//ע����ת���ѡ��
			// pivotY = height / 3;
			// rotation2 = 0;
			// pivotX2 = 0;
			// pivotY2 = 0;
			// //��ʵ���ǻ�����Ȼ��������ת����ȷ��λ��
			// startX = 60;
			// startY = height / 3;
			// stopX = width / 2;
			// stopY = height / 3;
			break;
		case BURGER_DOWN:
			rotation = 180 * ratio;// -30
			pivotX = width / 2;
			pivotY = height / 2;
			rotation2 = UP_TOP_ANGLE * ratio; // ��Ŧ2�Ѿ����ߵ��е��ˡ�����
			startX -= dip3 * ratio; // ע�� start��+������ stop��-������
			stopX -= (dip8 + dip2) * ratio; // ע�� start��+������ stop��-������

			// rotation = (-30+180)*ratio;//-30
			// pivotX = width / 2;//ע����ת���ѡ��
			// pivotY = ((height*2)/ 3) * ratio;
			// rotation2 = 0;
			// pivotX2 = 0;
			// pivotY2 = 0;
			// //��ʵ���ǻ�����Ȼ��������ת����ȷ��λ��
			// startX = 60;
			// startY = height / 3;
			// stopX = width / 2;
			// stopY = height / 3;
			break;
		case BURGER_PLUS:
			// rotate until required angles
			rotation = X_TOP_LINE_ANGLE * ratio;
			rotation2 = (X_ROTATION_ANGLE + PLUS_TOP_ANGLE) * ratio;

			// pivot at left corner of line
			pivotX = sidePadding + dip4;
			pivotY = topPadding + dip3;

			// shorten one end
			startX += dip3 * ratio;
			break;
		case ARROW_X:
			// rotate from ARROW angle to X angle
			rotation = ARROW_BOT_LINE_ANGLE
					+ (X_TOP_LINE_ANGLE - ARROW_BOT_LINE_ANGLE) * ratio;
			rotation2 = X_ROTATION_ANGLE * ratio;

			// move pivot from ARROW pivot to X pivot
			pivotX = width / 2 + (sidePadding + dip4 - width / 2) * ratio;
			pivotY = height / 2 + (topPadding + dip3 - height / 2) * ratio;

			// lengthen both ends
			stopX -= resolveStrokeModifier(ratio);
			startX += dip3;
			break;
		case ARROW_CHECK:
			// fade out
			alpha = (int) ((1 - ratio) * 255);
			// retain starting arrow configuration
			rotation = ARROW_BOT_LINE_ANGLE;
			pivotX = width / 2;
			pivotY = height / 2;

			// shorted both ends
			stopX -= resolveStrokeModifier(1);
			startX += dip3;
			break;
		case ARROW_UP:

			rotation = ARROW_BOT_LINE_ANGLE * (1 - ratio);
			rotation2 = UP_TOP_ANGLE * ratio; // ��Ŧ2�Ѿ����ߵ��е��ˡ�����
			// rotate by middle
			pivotX = width / 2;
			pivotY = height / 2;

			// shorten both ends
			stopX -= resolveStrokeModifier(ratio) * (1 - ratio) + (dip8 + dip2)
					* ratio;
			startX += dip3 * (1 - ratio) - dip3 * ratio;
			// stopX -= (dip8+dip2) * ratio;
			// startX -= dip3 * ratio;

			break;
		case ARROW_DOWN:

			rotation = ARROW_BOT_LINE_ANGLE * (1 - ratio) + 180 * ratio;
			rotation2 = UP_TOP_ANGLE * ratio; // ��Ŧ2�Ѿ����ߵ��е��ˡ�����
			// rotate by middle
			pivotX = width / 2;
			pivotY = height / 2;

			// shorten both ends
			stopX -= resolveStrokeModifier(ratio) * (1 - ratio) + (dip8 + dip2)
					* ratio;
			startX += dip3 * (1 - ratio) - dip3 * ratio;

			break;
		case ARROW_PLUS:
			
			// rotate from ARROW angle to plus angle
			rotation = ARROW_BOT_LINE_ANGLE
					+ (X_TOP_LINE_ANGLE - ARROW_BOT_LINE_ANGLE) * ratio;
			rotation2 = (X_ROTATION_ANGLE+PLUS_TOP_ANGLE) * ratio;

			// move pivot from ARROW pivot to X pivot
			pivotX = width / 2 + (sidePadding + dip4 - width / 2) * ratio;
			pivotY = height / 2 + (topPadding + dip3 - height / 2) * ratio;

			// lengthen both ends
			stopX -= resolveStrokeModifier(ratio);
			startX += dip3;
			
			break;
		case X_CHECK:
			// retain X configuration
			rotation = X_TOP_LINE_ANGLE;
			rotation2 = X_ROTATION_ANGLE;
			pivotX = sidePadding + dip4;
			pivotY = topPadding + dip3;
			stopX += dip3 - dip3 * (1 - ratio);
			startX += dip3;

			// fade out
			alpha = (int) ((1 - ratio) * 255);
			break;
		case X_UP:

			// rotation2 = X_ROTATION_ANGLE * ratio;
			// pivotX = sidePadding + dip4;
			// pivotY = topPadding + dip3;
			// startX += dip3 * ratio;

			rotation = X_TOP_LINE_ANGLE * (1 - ratio);
			pivotX = (sidePadding + dip4) * (1 - ratio) + (width / 2) * ratio;
			pivotY = (topPadding + dip3) * (1 - ratio) + (height / 2) * ratio;
			rotation2 = X_ROTATION_ANGLE * (1 - ratio) + UP_TOP_ANGLE * ratio; // ��Ŧ2�Ѿ����ߵ��е��ˡ�����
			startX += dip3 * (1 - ratio) - dip3 * ratio; // ע�� start��+������
															// stop��-������
			stopX -= (dip8 + dip2) * ratio; // ע�� start��+������ stop��-������

			break;
		case X_DOWN:

			rotation = X_TOP_LINE_ANGLE * (1 - ratio) + 180 * ratio;// -30
			pivotX = (sidePadding + dip4) * (1 - ratio) + (width / 2) * ratio;
			pivotY = (topPadding + dip3) * (1 - ratio) + (height / 2) * ratio;
			rotation2 = X_ROTATION_ANGLE * (1 - ratio) + UP_TOP_ANGLE * ratio; // ��Ŧ2�Ѿ����ߵ��е��ˡ�����
			startX += dip3 * (1 - ratio) - dip3 * ratio; // ע�� start��+������
															// stop��-������
			stopX -= (dip8 + dip2) * ratio; // ע�� start��+������ stop��-������

			break;
		case X_PLUS:
			
			// rotate until required angles
			rotation = X_TOP_LINE_ANGLE ;
			rotation2 = X_ROTATION_ANGLE + 45 * ratio;

			// pivot at left corner of line
			pivotX = sidePadding + dip4;
			pivotY = topPadding + dip3;

			// shorten one end
			startX += dip3 ;
			
			break;
		case CHECK_UP:
			alpha = (int) (ratio * 255);
			pivotX = width / 2;
			pivotY = height / 2;
			rotation2 = UP_TOP_ANGLE * ratio; // ��Ŧ2�Ѿ����ߵ��е��ˡ�����
			startX -= dip3 * ratio; // ע�� start��+������ stop��-������
			stopX -= (dip8 + dip2) * ratio; // ע�� start��+������ stop��-������

			// ע�� /����TOP ����ϵ -> �Ҳ�Ϊ0
			// rotation = -30;//-30
			// pivotX = width / 2;//ע����ת���ѡ��
			// pivotY = height / 3;
			// rotation2 = 0;
			// pivotX2 = 0;
			// pivotY2 = 0;
			// //��ʵ���ǻ�����Ȼ��������ת����ȷ��λ��
			// startX = 60;
			// startY = height / 3;
			// stopX = width / 2;
			// stopY = height / 3;
			break;
		case CHECK_DOWN:

			// retain X configuration
			// rotation = X_TOP_LINE_ANGLE;
			// rotation2 = X_ROTATION_ANGLE;
			// pivotX = sidePadding + dip4;
			// pivotY = topPadding + dip3;
			// stopX += dip3 - dip3 * (1 - ratio);
			// startX += dip3;

			rotation = X_TOP_LINE_ANGLE * (1 - ratio) + 180 * ratio;// -30
			pivotX = (sidePadding + dip4) * (1 - ratio) + (width / 2) * ratio;
			pivotY = (topPadding + dip3) * (1 - ratio) + (height / 2) * ratio;
			rotation2 = X_ROTATION_ANGLE * (1 - ratio) + UP_TOP_ANGLE * ratio; // ��Ŧ2�Ѿ����ߵ��е��ˡ�����
			startX += dip3 * (1 - ratio) - dip3 * ratio; // ע�� start��+������
															// stop��-������
			stopX += dip3 * (1 - ratio) - (dip8 + dip2) * ratio; // ע��
																	// start��+������
																	// stop��-������

			alpha = (int) (ratio * 255);

			break;
		case CHECK_PLUS:

			// fade out
			alpha = (int) (255 * ratio);
			// rotate until required angles
			rotation = X_TOP_LINE_ANGLE * ratio;
			rotation2 = (X_ROTATION_ANGLE + PLUS_TOP_ANGLE) * ratio;

			// pivot at left corner of line
			pivotX = sidePadding + dip4;
			pivotY = topPadding + dip3;

			// shorten one end
			startX += dip3 * ratio;

			break;
		case UP_DOWN:

			rotation = 180 * ratio;// -30
			pivotX = width / 2;
			pivotY = height / 2;
			rotation2 = UP_TOP_ANGLE; // ��Ŧ2�Ѿ����ߵ��е��ˡ�����
			startX -= dip3; // ע�� start��+������ stop��-������
			stopX -= (dip8 + dip2); // ע�� start��+������ stop��-������

			// rotation = ( -30 + 180 ) * ratio;//-30
			// pivotX = width / 2 ;
			// pivotY = ((height*2)/ 3) * ratio;
			//
			// //��ʵ���ǻ�����Ȼ��������ת����ȷ��λ��
			// startX = 60;
			// stopX = width/2;
			// startY = height / 3;
			// stopY = height / 3;

			break;
		case UP_PLUS:
			
			rotation = X_TOP_LINE_ANGLE * ratio;
			pivotX = (width / 2)*(1-ratio)+(sidePadding + dip4)*ratio;
			pivotY = (height / 2)*(1-ratio)+(topPadding + dip3)*ratio;
			rotation2 = UP_TOP_ANGLE * (1-ratio) + (X_ROTATION_ANGLE + PLUS_TOP_ANGLE) * ratio; // ��Ŧ2�Ѿ����ߵ��е��ˡ�����
			startX -= dip3 * (1-ratio) - dip3 * ratio; // ע�� start��+������ stop��-������
			stopX -= (dip8 + dip2) * (1-ratio); // ע�� start��+������ stop��-������
			
			break;
		case DOWN_PLUS:
			
			rotation = 180 * (1-ratio)+X_TOP_LINE_ANGLE * ratio;// -30
			pivotX = (width / 2)*(1-ratio)+(sidePadding + dip4)*ratio;
			pivotY = (height / 2)*(1-ratio)+(topPadding + dip3)*ratio;
			rotation2 = UP_TOP_ANGLE * (1-ratio)+(X_ROTATION_ANGLE + PLUS_TOP_ANGLE) * ratio; // ��Ŧ2�Ѿ����ߵ��е��ˡ�����
			startX -= dip3 * (1-ratio) - dip3 * ratio; // ע�� start��+������ stop��-������
			stopX -= (dip8 + dip2) * (1-ratio); // ע�� start��+������ stop��-������
			
			break;
		default:
			break;

		}

		iconPaint.setAlpha(alpha);
		canvas.rotate(rotation, pivotX, pivotY);// 225 400 400 -> 44 280 310
		canvas.rotate(rotation2, pivotX2, pivotY2);// 0 430 290 -> 90 430 290
		canvas.drawLine(startX, startY, stopX, stopY, iconPaint);// 260 290 520
																	// 290 -> 3
																	// 600
		iconPaint.setAlpha(255);
	}

	// �±��ߵĶ���
	private void drawBottomLine(Canvas canvas, float ratio) {
		canvas.restore();
		canvas.save();

		float rotation = 0, pivotX = 0, pivotY = 0;
		float rotation2 = 0;
		// pivot at center of line
		float pivotX2 = width / 2 + dip3 / 2;
		float pivotY2 = height - topPadding - dip2;

		float startX = sidePadding;
		float startY = height - topPadding - dip2;
		float stopX = width - sidePadding;
		float stopY = height - topPadding - dip2;

		switch (animationState) {
		case BURGER_ARROW:
			if (isMorphingForward()) {
				// rotate to required angle
				rotation = ARROW_TOP_LINE_ANGLE * ratio;
			} else {
				// rotate back to start doing a 360
				rotation = ARROW_TOP_LINE_ANGLE + (1 - ratio)
						* ARROW_BOT_LINE_ANGLE;
			}
			// pivot center of canvas
			pivotX = width / 2;
			pivotY = height / 2;

			// shorten both ends
			stopX = width - sidePadding - resolveStrokeModifier(ratio);
			startX = sidePadding + dip3 * ratio;
			break;
		case BURGER_X:
			if (isMorphingForward()) {
				// rotate around
				rotation2 = -X_ROTATION_ANGLE * ratio;
			} else {
				// rotate directly
				rotation2 = X_ROTATION_ANGLE * ratio;
			}
			// rotate to required angle
			rotation = X_BOT_LINE_ANGLE * ratio;

			// pivot left corner of line
			pivotX = sidePadding + dip4;
			pivotY = height - topPadding - dip3;

			// shorten one end
			startX += dip3 * ratio;
			break;
		case BURGER_CHECK:
			// rotate from ARROW angle to CHECK angle
			rotation = ratio * (CHECK_BOTTOM_ANGLE + ARROW_TOP_LINE_ANGLE);

			// move pivot from BURGER pivot to CHECK pivot
			pivotX = width / 2 + dip3 * ratio;
			pivotY = height / 2 - dip3 * ratio;

			// length stays same as BURGER
			startX += dip8 * ratio;
			stopX -= resolveStrokeModifier(ratio);
			break;
		case BURGER_UP:

			// ע�� /����TOP ����ϵ -> �Ҳ�Ϊ0
			// ֹͣ����ʱ��ͼ��
			// rotation = 180 * ratio;
			// pivotX = width / 2 ;//ע����ת���ѡ��
			// pivotY = height / 2 ;
			// rotation2 = UP_BOTTOM_ANGLE * ratio;
			// //��ʵ���ǻ�����Ȼ��������ת����ȷ��λ��
			// startX -= dip3 * ratio; //ע�� start��+������ stop��-������
			// stopX -= (dip8+dip2) * ratio; //ע�� start��+������ stop��-������

			// ע�� \��bottom ��ʵ���ǻ�����Ȼ��������ת����ȷ��λ��
			rotation = 180 * ratio;
			pivotX = width / 2;// ע����ת���ѡ��
			pivotY = height / 2;
			rotation2 = UP_BOTTOM_ANGLE * ratio;
			// ��ʵ���ǻ�����Ȼ��������ת����ȷ��λ��
			startX -= dip3 * ratio; // ע�� start��+������ stop��-������
			stopX -= (dip8 + dip2) * ratio; // ע�� start��+������ stop��-������

			break;
		case BURGER_DOWN:
			// ����180��
			// rotation = 180 * (1- ratio);//-30
			// rotation = 180 * (1- ratio);//-30
			pivotX = width / 2;
			pivotY = height / 2;
			rotation2 = UP_BOTTOM_ANGLE * ratio; // ��Ŧ2�Ѿ����ߵ��е��ˡ�����
			startX -= dip3 * ratio; // ע�� start��+������ stop��-������
			stopX -= (dip8 + dip2) * ratio; // ע�� start��+������ stop��-������

			// rotation = (30+180) *ratio;//-30
			// pivotX = width / 2;//ע����ת���ѡ��
			// pivotY = ((height*2)/ 3) * ratio;
			// //��ʵ���ǻ�����Ȼ��������ת����ȷ��λ��
			// startX = width / 2;
			// // startY = height / 3;
			// stopX = width - 60;
			// stopY = height / 3;
			break;
		case BURGER_PLUS:
			rotation2 = (X_ROTATION_ANGLE+PLUS_BOTTOM_ANGLE) * ratio;
			// rotate to required angle
			rotation = X_BOT_LINE_ANGLE * ratio;

			// pivot left corner of line
			pivotX = sidePadding + dip4;
			pivotY = height - topPadding - dip3;

			// shorten one end
			startX += dip3 * ratio;
			break;
		case ARROW_X:
			// rotate from ARROW angle to X angle
			rotation = ARROW_TOP_LINE_ANGLE
					+ (360 + X_BOT_LINE_ANGLE - ARROW_TOP_LINE_ANGLE) * ratio;
			rotation2 = -X_ROTATION_ANGLE * ratio;

			// move pivot from ARROW pivot to X pivot
			pivotX = width / 2 + (sidePadding + dip4 - width / 2) * ratio;
			pivotY = height / 2 + (height / 2 - topPadding - dip3) * ratio;

			// lengthen both ends
			stopX -= resolveStrokeModifier(ratio);
			startX += dip3;
			break;
		case ARROW_CHECK:
			// rotate from ARROW angle to CHECK angle
			rotation = ARROW_TOP_LINE_ANGLE + ratio * CHECK_BOTTOM_ANGLE;

			// move pivot from ARROW pivot to CHECK pivot
			pivotX = width / 2 + dip3 * ratio;
			pivotY = height / 2 - dip3 * ratio;

			// length stays same as ARROW
			stopX -= resolveStrokeModifier(1);
			startX += dip3 + (dip4 + dip1) * ratio;
			break;
		case ARROW_UP:

			rotation = ARROW_TOP_LINE_ANGLE * (1 - ratio) + 180 * ratio;
			// pivot center of canvas
			pivotX = width / 2;
			pivotY = height / 2;
			rotation2 = UP_BOTTOM_ANGLE * ratio;

			// shorten both ends
			stopX = (width - sidePadding) * (1 - ratio)
					- resolveStrokeModifier(ratio) * (1 - ratio)
					+ (stopX - dip8 - dip2) * ratio;
			startX = sidePadding * (1 - ratio) + dip3 * (1 - ratio)
					+ (startX - dip3) * ratio;
			// startX -= dip3 * ratio; //ע�� start��+������ stop��-������
			// stopX -= (dip8+dip2) * ratio; //ע�� start��+������ stop��-������
			// startX = (startX-dip3) * ratio; //ע�� start��+������ stop��-������
			// stopX = (stopX-dip8-dip2) * ratio; //ע�� start��+������ stop��-������

			break;
		case ARROW_DOWN:

			rotation = ARROW_TOP_LINE_ANGLE * (1 - ratio);
			// pivot center of canvas
			pivotX = width / 2;
			pivotY = height / 2;
			rotation2 = UP_BOTTOM_ANGLE * ratio;

			// shorten both ends
			stopX = (width - sidePadding) * (1 - ratio)
					- resolveStrokeModifier(ratio) * (1 - ratio)
					+ (stopX - dip8 - dip2) * ratio;
			startX = sidePadding * (1 - ratio) + dip3 * (1 - ratio)
					+ (startX - dip3) * ratio;

			break;
		case ARROW_PLUS:
			// rotate from ARROW angle to X angle
			rotation = ARROW_TOP_LINE_ANGLE
					+ (360 + X_BOT_LINE_ANGLE - ARROW_TOP_LINE_ANGLE) * ratio;
			rotation2 = (-X_ROTATION_ANGLE+PLUS_BOTTOM_ANGLE) * ratio;

			// move pivot from ARROW pivot to X pivot
			pivotX = width / 2 + (sidePadding + dip4 - width / 2) * ratio;
			pivotY = height / 2 + (height / 2 - topPadding - dip3) * ratio;

			// lengthen both ends
			stopX -= resolveStrokeModifier(ratio);
			startX += dip3;
			break;
		case X_CHECK:
			// rotate from X to CHECK angles
			rotation2 = -X_ROTATION_ANGLE * (1 - ratio);
			rotation = X_BOT_LINE_ANGLE
					+ (CHECK_BOTTOM_ANGLE + ARROW_TOP_LINE_ANGLE - X_BOT_LINE_ANGLE)
					* ratio;

			// move pivot from X to CHECK
			pivotX = sidePadding + dip4
					+ (width / 2 + dip3 - sidePadding - dip4) * ratio;
			pivotY = height - topPadding - dip3
					+ (topPadding + height / 2 - height) * ratio;

			// shorten both ends
			startX += dip8 - (dip4 + dip1) * (1 - ratio);
			stopX -= resolveStrokeModifier(1 - ratio);
			break;
		case X_UP:

			rotation = X_BOT_LINE_ANGLE * (1 - ratio) + 180 * ratio;
			pivotX = (sidePadding + dip4) * (1 - ratio) + (width / 2) * ratio;// ע����ת���ѡ��
			pivotY = (height - topPadding - dip3) * (1 - ratio) + (height / 2)
					* ratio;
			rotation2 = X_ROTATION_ANGLE * (1 - ratio) + UP_BOTTOM_ANGLE
					* ratio;
			// ��ʵ���ǻ�����Ȼ��������ת����ȷ��λ��
			startX += dip3 * (1 - ratio) - dip3 * ratio; // ע�� start��+������
															// stop��-������
			stopX -= (dip8 + dip2) * ratio; // ע�� start��+������ stop��-������

			break;
		case X_DOWN:

			rotation = X_BOT_LINE_ANGLE * (1 - ratio);
			pivotX = (sidePadding + dip4) * (1 - ratio) + (width / 2) * ratio;
			pivotY = (height - topPadding - dip3) * (1 - ratio) + (height / 2)
					* ratio;
			rotation2 = X_ROTATION_ANGLE * (1 - ratio) + UP_BOTTOM_ANGLE
					* ratio; // ��Ŧ2�Ѿ����ߵ��е��ˡ�����
			startX += dip3 * (1 - ratio) - dip3 * ratio; // ע�� start��+������
															// stop��-������
			stopX -= (dip8 + dip2) * ratio; // ע�� start��+������ stop��-������

			break;
		case X_PLUS:
			
			rotation2 = X_ROTATION_ANGLE + PLUS_BOTTOM_ANGLE * ratio;
			// rotate to required angle
			rotation = X_BOT_LINE_ANGLE ;

			// pivot left corner of line
			pivotX = sidePadding + dip4;
			pivotY = height - topPadding - dip3;

			// shorten one end
			startX += dip3 ;
			
			break;
		case CHECK_UP:
			// ע�� \��bottom ��ʵ���ǻ�����Ȼ��������ת����ȷ��λ��
			// ע�� /����TOP ����ϵ -> �Ҳ�Ϊ0
			// ֹͣ����ʱ��ͼ��

			// rotate from X to CHECK angles
			rotation2 = UP_BOTTOM_ANGLE * ratio;
			rotation = (CHECK_UP_ROTATION) * (1 - ratio) + 180 * ratio;

			// move pivot from X to CHECK
			pivotX = (CHECK_UP_PIVOTX) * (1 - ratio) + (width / 2) * ratio;
			pivotY = (CHECK_UP_PIVOTY) * (1 - ratio) + (height / 2) * ratio;

			// shorten both ends
			startX += dip8 * (1 - ratio) - dip3 * ratio;
			stopX -= resolveStrokeModifier(1 - ratio) * (1 - ratio) + (dip8 + dip2)
					* ratio;

			// //ע�� /����TOP ����ϵ -> �Ҳ�Ϊ0
			// //ֹͣ����ʱ��ͼ��
			// rotation = 180 * ratio;
			// pivotX = width / 2 ;//ע����ת���ѡ��
			// pivotY = height / 2 ;
			// rotation2 = UP_BOTTOM_ANGLE * ratio;
			// //��ʵ���ǻ�����Ȼ��������ת����ȷ��λ��
			// startX -= dip3 * ratio; //ע�� start��+������ stop��-������
			// stopX -= (dip8+dip2) * ratio; //ע�� start��+������ stop��-������

			break;
		case CHECK_DOWN:

			// rotate from X to CHECK angles
			rotation2 = UP_BOTTOM_ANGLE * ratio;
			rotation = (CHECK_UP_ROTATION) * (1 - ratio);// ���������򵥴ֱ��Ĵ���ʽ��ץס��һ��ͼ�ε�����״̬������һ��ͼ�ε�����״̬
			// + (180 * (1-ratio))*ratio

			// move pivot from X to CHECK
			pivotX = (CHECK_UP_PIVOTX) * (1 - ratio) + (width / 2) * ratio;
			pivotY = (CHECK_UP_PIVOTY) * (1 - ratio) + (height / 2) * ratio;

			// shorten both ends
			startX += dip8 * (1 - ratio) - dip3 * ratio;
			stopX -= resolveStrokeModifier(1 - ratio) * (1 - ratio) + (dip8 + dip2)
					* ratio;

			break;
		case CHECK_PLUS:
			
			rotation = (CHECK_BOTTOM_ANGLE + ARROW_TOP_LINE_ANGLE)*(1-ratio) + X_BOT_LINE_ANGLE * ratio;

			rotation2 = (X_ROTATION_ANGLE+PLUS_BOTTOM_ANGLE) * ratio;
			// move pivot from BURGER pivot to CHECK pivot
			pivotX = (width / 2 + dip3)*(1-ratio) + (sidePadding + dip4)*ratio;
			pivotY = (height / 2 - dip3)*(1-ratio) + (height - topPadding - dip3)*ratio;

			// length stays same as BURGER
			startX += dip8 * (1-ratio) + dip3 * ratio;
			stopX -= resolveStrokeModifier(1-ratio);
						
			break;
		case UP_DOWN:
			// 180��
			// rotation = 180 - 180 * ratio;//-30
			rotation = 180 * (1 - ratio);// -30
			pivotX = width / 2;
			pivotY = height / 2;
			rotation2 = UP_BOTTOM_ANGLE; // ��Ŧ2�Ѿ����ߵ��е��ˡ�����
			startX -= dip3; // ע�� start��+������ stop��-������
			stopX -= (dip8 + dip2); // ע�� start��+������ stop��-������

			// rotation = 30 * ratio;//-30
			// pivotX = width / 2 ;//ע����ת���ѡ��
			// pivotY = height / 3 ;
			// //��ʵ���ǻ�����Ȼ��������ת����ȷ��λ��
			// startX = width / 2 + dip1;
			// // startY = height / 3;
			// stopX = width / 2 + dip1;

			// rotation = -45;
			// pivotX = 280;
			// pivotY = 490;
			// rotation2 = -90;
			// pivotX2 = 430;
			// pivotY2 = 510;
			// startX = 260;
			// startY = 510;
			// stopX = 600;
			// stopY = 510;
			break;
		case UP_PLUS:
			rotation = 180 * (1-ratio) + X_BOT_LINE_ANGLE * ratio;
			pivotX = (width / 2)*(1-ratio) + (sidePadding + dip4)*ratio;// ע����ת���ѡ��
			pivotY = (height / 2)*(1-ratio) + (height - topPadding - dip3)*ratio;
			rotation2 = UP_BOTTOM_ANGLE * (1-ratio) + (X_ROTATION_ANGLE+PLUS_BOTTOM_ANGLE) * ratio;
			// ��ʵ���ǻ�����Ȼ��������ת����ȷ��λ��
			startX -= dip3 * (1-ratio) - dip3 * ratio; // ע�� start��+������ stop��-������
			stopX -= (dip8 + dip2) * (1-ratio); // ע�� start��+������ stop��-������
			
			break;
		case DOWN_PLUS:
			
			rotation = X_BOT_LINE_ANGLE * ratio;
			pivotX = (width / 2)*(1-ratio) + (sidePadding + dip4)*ratio;
			pivotY = (height / 2)*(1-ratio) + (height - topPadding - dip3)*ratio;
			rotation2 = UP_BOTTOM_ANGLE * (1-ratio) + (X_ROTATION_ANGLE+PLUS_BOTTOM_ANGLE) * ratio; // ��Ŧ2�Ѿ����ߵ��е��ˡ�����
			startX -= dip3 * (1-ratio) - dip3 * ratio; // ע�� start��+������ stop��-������
			stopX -= (dip8 + dip2) * (1-ratio); // ע�� start��+������ stop��-������
			
			break;
		
		default:
			break;
		}

		canvas.rotate(rotation, pivotX, pivotY);// 135 400 400 -> -44 280 490
		canvas.rotate(rotation2, pivotX2, pivotY2);// 0 430 510 -> -90 430 510
		canvas.drawLine(startX, startY, stopX, stopY, iconPaint);// 260 510 520
																	// 510 -> 3
																	// 600
	}

	private boolean isMorphingForward() {
		return transformationValue <= TRANSFORMATION_MID;
	}

	private float resolveStrokeModifier(float ratio) {
		switch (stroke) {
		case REGULAR:
			if (animationState == AnimationState.ARROW_X
					|| animationState == AnimationState.X_CHECK
					|| animationState == AnimationState.ARROW_UP
					|| animationState == AnimationState.ARROW_DOWN
					|| animationState == AnimationState.ARROW_PLUS
					) {
				return dip3 - (dip3 * ratio);
			}
			return ratio * dip3;
		case THIN:
			if (animationState == AnimationState.ARROW_X
					|| animationState == AnimationState.X_CHECK
					|| animationState == AnimationState.ARROW_UP
					|| animationState == AnimationState.ARROW_DOWN
					|| animationState == AnimationState.ARROW_PLUS
					) {
				return dip3 + diph - (dip3 + diph) * ratio;
			}
			return ratio * (dip3 + diph);
		case EXTRA_THIN:
			if (animationState == AnimationState.ARROW_X
					|| animationState == AnimationState.X_CHECK
					|| animationState == AnimationState.ARROW_UP
					|| animationState == AnimationState.ARROW_DOWN
					|| animationState == AnimationState.ARROW_PLUS
					) {
				return dip4 - ((dip3 + dip1) * ratio);
			}
			return ratio * dip4;
		}
		return 0;
	}

	@Override
	public void setAlpha(int alpha) {
		iconPaint.setAlpha(alpha);
	}

	@Override
	public void setColorFilter(ColorFilter cf) {
		iconPaint.setColorFilter(cf);
	}

	@Override
	public int getOpacity() {
		return PixelFormat.TRANSPARENT;
	}

	/*
	 * Accessor methods
	 */

	public void setColor(int color) {
		iconPaint.setColor(color);
		circlePaint.setColor(color);
		invalidateSelf();
	}

	public void setTransformationDuration(int duration) {
		transformation.setDuration(duration);
	}

	public void setPressedDuration(int duration) {
		pressedCircle.setDuration(duration);
	}

	public void setInterpolator(Interpolator interpolator) {
		transformation.setInterpolator(interpolator);
	}

	public void setAnimationListener(AnimatorListener listener) {
		if (animatorListener != null) {
			transformation.removeListener(animatorListener);
		}

		if (listener != null) {
			transformation.addListener(listener);
		}

		animatorListener = listener;
	}

	public void setNeverDrawTouch(boolean neverDrawTouch) {
		this.neverDrawTouch = neverDrawTouch;
	}

	public void setIconState(IconState iconState) {
		synchronized (lock) {
			if (transformationRunning) {
				transformation.cancel();
				transformationRunning = false;
			}

			if (currentIconState == iconState)
				return;

			switch (iconState) {
			case BURGER:
				animationState = AnimationState.BURGER_ARROW;
				transformationValue = TRANSFORMATION_START;
				break;
			case ARROW:
				animationState = AnimationState.BURGER_ARROW;
				transformationValue = TRANSFORMATION_MID;
				break;
			case X:
				animationState = AnimationState.BURGER_X;
				transformationValue = TRANSFORMATION_MID;
				break;
			case CHECK:
				animationState = AnimationState.BURGER_CHECK;
				transformationValue = TRANSFORMATION_MID;
				break;
			case UP:
				animationState = AnimationState.BURGER_UP;
				transformationValue = TRANSFORMATION_MID;
				break;
			case DOWN:
				animationState = AnimationState.BURGER_DOWN;
				transformationValue = TRANSFORMATION_MID;
				break;
			case PLUS:                   //ע�⣡����
				animationState = AnimationState.BURGER_PLUS;
				transformationValue = TRANSFORMATION_MID;
			}
			currentIconState = iconState;
			invalidateSelf();
		}
	}

	public void animateIconState(IconState state, boolean drawTouch) {
		synchronized (lock) {
			if (transformationRunning) {
				transformation.end();
				pressedCircle.end();
			}
			drawTouchCircle = drawTouch;
			animatingIconState = state;
			start();
		}
	}

	public IconState setTransformationOffset(AnimationState animationState,
			float offset) {
		if (offset < TRANSFORMATION_START || offset > TRANSFORMATION_END) {
			throw new IllegalArgumentException(String.format(
					"Value must be between %s and %s", TRANSFORMATION_START,
					TRANSFORMATION_END));
		}

		this.animationState = animationState;

		final boolean isFirstIcon = offset < TRANSFORMATION_MID
				|| offset == TRANSFORMATION_END;

		currentIconState = isFirstIcon ? animationState.getFirstState()
				: animationState.getSecondState();
		animatingIconState = isFirstIcon ? animationState.getSecondState()
				: animationState.getFirstState();

		setTransformationValue(offset);

		return currentIconState;
	}

	public void setRTLEnabled(boolean rtlEnabled) {
		this.rtlEnabled = rtlEnabled;
		invalidateSelf();
	}

	public IconState getIconState() {
		return currentIconState;
	}

	/*
	 * Animations
	 */
	private Property<MaterialMenuDrawable, Float> transformationProperty = new Property<MaterialMenuDrawable, Float>(
			Float.class, "transformation") {
		@Override
		public Float get(MaterialMenuDrawable object) {
			return object.getTransformationValue();
		}

		@Override
		public void set(MaterialMenuDrawable object, Float value) {
			object.setTransformationValue(value);
		}
	};

	private Property<MaterialMenuDrawable, Float> pressedProgressProperty = new Property<MaterialMenuDrawable, Float>(
			Float.class, "pressedProgress") {
		@Override
		public Float get(MaterialMenuDrawable object) {
			return object.getPressedProgress();
		}

		@Override
		public void set(MaterialMenuDrawable object, Float value) {
			object.setPressedProgress(value);
		}
	};
	private float CHECK_UP_ROTATION;
	private float CHECK_UP_PIVOTX;
	private float CHECK_UP_PIVOTY;

	public Float getTransformationValue() {
		return transformationValue;
	}

	public void setTransformationValue(Float value) {
		this.transformationValue = value;
		invalidateSelf();
	}

	public Float getPressedProgress() {
		return pressedProgressValue;
	}

	public void setPressedProgress(Float value) {
		this.pressedProgressValue = value;
		circlePaint.setAlpha((int) (DEFAULT_CIRCLE_ALPHA * (1 - value
				/ (circleRadius * 1.22f))));
		invalidateSelf();
	}

	private void initAnimations(int transformDuration, int pressedDuration) {
		transformation = ObjectAnimator
				.ofFloat(this, transformationProperty, 0);
		transformation.setInterpolator(new DecelerateInterpolator(3));
		transformation.setDuration(transformDuration);
		transformation.addListener(new AnimatorListenerAdapter() {
			@Override
			public void onAnimationEnd(Animator animation) {
				transformationRunning = false;
				setIconState(animatingIconState);
			}
		});

		pressedCircle = ObjectAnimator.ofFloat(this, pressedProgressProperty,
				0, 0);
		pressedCircle.setDuration(pressedDuration);
		pressedCircle.setInterpolator(new DecelerateInterpolator());
		pressedCircle.addListener(new AnimatorListenerAdapter() {
			@Override
			public void onAnimationEnd(Animator animation) {
				pressedProgressValue = 0;
			}

			@Override
			public void onAnimationCancel(Animator animation) {
				pressedProgressValue = 0;
			}
		});

		initUpDown();

	}

	// ��ʼ���ߺͽǶ�
	private void initUpDown() {
		CHECK_UP_ROTATION = X_BOT_LINE_ANGLE + CHECK_BOTTOM_ANGLE
				+ ARROW_TOP_LINE_ANGLE - X_BOT_LINE_ANGLE;
		CHECK_UP_PIVOTX = sidePadding + dip4 + width / 2 + dip3 - sidePadding
				- dip4;
		CHECK_UP_PIVOTY = height - topPadding - dip3 + topPadding + height / 2
				- height;

	}

	private boolean resolveTransformation() {
		boolean isCurrentBurger = currentIconState == IconState.BURGER;
		boolean isCurrentArrow = currentIconState == IconState.ARROW;
		boolean isCurrentX = currentIconState == IconState.X;
		boolean isCurrentCheck = currentIconState == IconState.CHECK;
		boolean isCurrentUp = currentIconState == IconState.UP;
		boolean isCurrentDown = currentIconState == IconState.DOWN;
		boolean isCurrentPlus = currentIconState == IconState.PLUS;
		boolean isAnimatingBurger = animatingIconState == IconState.BURGER;
		boolean isAnimatingArrow = animatingIconState == IconState.ARROW;
		boolean isAnimatingX = animatingIconState == IconState.X;
		boolean isAnimatingCheck = animatingIconState == IconState.CHECK;
		boolean isAnimatingUp = animatingIconState == IconState.UP;
		boolean isAnimatingDown = animatingIconState == IconState.DOWN;
		boolean isAnimatingPlus = animatingIconState == IconState.PLUS;

		if ((isCurrentBurger && isAnimatingArrow)
				|| (isCurrentArrow && isAnimatingBurger)) {
			animationState = AnimationState.BURGER_ARROW;
			return isCurrentBurger;
		}

		if ((isCurrentBurger && isAnimatingX)
				|| (isCurrentX && isAnimatingBurger)) {
			animationState = AnimationState.BURGER_X;
			return isCurrentBurger;
		}

		if ((isCurrentBurger && isAnimatingCheck)
				|| (isCurrentCheck && isAnimatingBurger)) {
			animationState = AnimationState.BURGER_CHECK;
			return isCurrentBurger;
		}

		if ((isCurrentBurger && isAnimatingUp)
				|| (isCurrentUp && isAnimatingBurger)) {
			animationState = AnimationState.BURGER_UP;
			return isCurrentBurger;
		}

		if ((isCurrentBurger && isAnimatingDown)
				|| (isCurrentDown && isAnimatingBurger)) {
			animationState = AnimationState.BURGER_DOWN;
			return isCurrentBurger;
		}
		
		if ((isCurrentBurger && isAnimatingPlus)
				|| (isCurrentPlus && isAnimatingBurger)) {
			animationState = AnimationState.BURGER_PLUS;
			return isCurrentBurger;
		}

		if ((isCurrentArrow && isAnimatingX)
				|| (isCurrentX && isAnimatingArrow)) {
			animationState = AnimationState.ARROW_X;
			return isCurrentArrow;
		}

		if ((isCurrentArrow && isAnimatingCheck)
				|| (isCurrentCheck && isAnimatingArrow)) {
			animationState = AnimationState.ARROW_CHECK;
			return isCurrentArrow;
		}

		if ((isCurrentArrow && isAnimatingUp)
				|| (isCurrentUp && isAnimatingArrow)) {
			animationState = AnimationState.ARROW_UP;
			return isCurrentArrow;
		}

		if ((isCurrentArrow && isAnimatingDown)
				|| (isCurrentDown && isAnimatingArrow)) {
			animationState = AnimationState.ARROW_DOWN;
			return isCurrentArrow;
		}
		
		if ((isCurrentArrow && isAnimatingPlus)
				|| (isCurrentPlus && isAnimatingArrow)) {
			animationState = AnimationState.ARROW_PLUS;
			return isCurrentArrow;
		}

		if ((isCurrentX && isAnimatingCheck)
				|| (isCurrentCheck && isAnimatingX)) {
			animationState = AnimationState.X_CHECK;
			return isCurrentX;
		}

		if ((isCurrentX && isAnimatingUp) || (isCurrentUp && isAnimatingX)) {
			animationState = AnimationState.X_UP;
			return isCurrentX;
		}

		if ((isCurrentX && isAnimatingDown) || (isCurrentDown && isAnimatingX)) {
			animationState = AnimationState.X_DOWN;
			return isCurrentX;
		}
		
		if ((isCurrentX && isAnimatingPlus) || (isCurrentPlus && isAnimatingX)) {
			animationState = AnimationState.X_PLUS;
			return isCurrentX;
		}

		if ((isCurrentCheck && isAnimatingUp)
				|| (isCurrentUp && isAnimatingCheck)) {
			animationState = AnimationState.CHECK_UP;
			return isCurrentCheck;
		}

		if ((isCurrentCheck && isAnimatingDown)
				|| (isCurrentDown && isAnimatingCheck)) {
			animationState = AnimationState.CHECK_DOWN;
			return isCurrentCheck;
		}
		
		if ((isCurrentCheck && isAnimatingPlus)
				|| (isCurrentPlus && isAnimatingCheck)) {
			animationState = AnimationState.CHECK_PLUS;
			return isCurrentCheck;
		}

		if ((isCurrentUp && isAnimatingDown)
				|| (isCurrentDown && isAnimatingUp)) {
			animationState = AnimationState.UP_DOWN;
			return isCurrentUp;
		}
		if ((isCurrentUp && isAnimatingPlus)
				|| (isCurrentPlus && isAnimatingUp)) {
			animationState = AnimationState.UP_PLUS;
			return isCurrentUp;
		}
		if ((isCurrentDown && isAnimatingPlus)
				|| (isCurrentPlus && isAnimatingDown)) {
			animationState = AnimationState.DOWN_PLUS;
			return isCurrentDown;        //ע�⣡����
		}

		throw new IllegalStateException(String.format(
				"Animating from %s to %s is not supported", currentIconState,
				animatingIconState));
	}

	@Override
	public void start() {
		if (transformationRunning)
			return;

		if (animatingIconState != null
				&& animatingIconState != currentIconState) {
			transformationRunning = true;

			final boolean direction = resolveTransformation();
			transformation.setFloatValues(direction ? TRANSFORMATION_START
					: TRANSFORMATION_MID, direction ? TRANSFORMATION_MID
					: TRANSFORMATION_END);
			transformation.start();
		}

		if (pressedCircle.isRunning()) {
			pressedCircle.cancel();
		}
		if (drawTouchCircle && !neverDrawTouch) {
			pressedCircle.setFloatValues(0, circleRadius * 1.22f);
			pressedCircle.start();
		}

		invalidateSelf();
	}

	@Override
	public void stop() {
		if (isRunning() && transformation.isRunning()) {
			transformation.end();
		} else {
			transformationRunning = false;
			invalidateSelf();
		}
	}

	@Override
	public boolean isRunning() {
		return transformationRunning;
	}

	@Override
	public int getIntrinsicWidth() {
		return width;
	}

	@Override
	public int getIntrinsicHeight() {
		return height;
	}

	@Override
	public ConstantState getConstantState() {
		materialMenuState.changingConfigurations = getChangingConfigurations();
		return materialMenuState;
	}

	@Override
	public Drawable mutate() {
		materialMenuState = new MaterialMenuState();
		return this;
	}

	private final class MaterialMenuState extends ConstantState {
		private int changingConfigurations;

		private MaterialMenuState() {
		}

		@Override
		public Drawable newDrawable() {
			MaterialMenuDrawable drawable = new MaterialMenuDrawable(
					circlePaint.getColor(), stroke,
					transformation.getDuration(), pressedCircle.getDuration(),
					width, height, iconWidth, circleRadius, strokeWidth, dip1);
			drawable.setIconState(animatingIconState != null ? animatingIconState
					: currentIconState);
			drawable.setRTLEnabled(rtlEnabled);
			return drawable;
		}

		@Override
		public int getChangingConfigurations() {
			return changingConfigurations;
		}
	}

	static float dpToPx(Resources resources, float dp) {
		return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
				resources.getDisplayMetrics());
	}
	
	public boolean isDrawCircle() {
		return isDrawCircle;
	}

	public void setDrawCircle(boolean isDrawCircle) {
		this.isDrawCircle = isDrawCircle;
	}
}
