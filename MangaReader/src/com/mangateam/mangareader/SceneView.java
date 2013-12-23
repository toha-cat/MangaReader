/**
 * 
 */
package com.mangateam.mangareader;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.ScaleGestureDetector.OnScaleGestureListener;
import android.view.View;
import android.widget.Scroller;

/**
 * view для отображения сцен
 */
public class SceneView extends View {

	private Bitmap image = null;

	private PageImages pageImages = null;
	
	private boolean fullPageMod = false;

	private final GestureDetector gestureDetector;
	private final ScaleGestureDetector scaleGestureDetector;
	private final Scroller scroller;

	private final Paint paint = new Paint();

	private float scaleFactor;
	private float minScaleFactor;

	private int getScaledWidth() {
		if(image == null) return 0;
		return (int) (image.getWidth() * scaleFactor);
	}

	private int getScaledHeight() {
		if(image == null) return 0;
		return (int) (image.getHeight() * scaleFactor);
	}

	public SceneView(Context context) {
		super(context);

		gestureDetector = new GestureDetector(context, new MyGestureListener());
		scaleGestureDetector = new ScaleGestureDetector(context, new MyScaleGestureListener());

		// init scrollbars
		setHorizontalScrollBarEnabled(true);
		setVerticalScrollBarEnabled(true);

		TypedArray a = context.obtainStyledAttributes(R.styleable.View);
		initializeScrollbars(a);
		a.recycle();

		scroller = new Scroller(context);

		paint.setFilterBitmap(true);
		paint.setDither(false);
	}

	public void setMangaSource(MangaSource mSource) {
		this.pageImages = new PageImages(mSource);
		this.image = pageImages.current();
	}
	
	public void viewFullPage() {
		this.image = pageImages.getFullPage();
		fullPageMod = true;
		invalidate();
		resetScaleFactor();
	}
	
	public void viewSceneMode() {
		this.image = pageImages.current();
		fullPageMod = false;
		invalidate();
		resetScaleFactor();
	}
	
	private int loadNextImg(){
		if(fullPageMod) return -2;
		Bitmap img = null;
		img = pageImages.next();
		if(img != null){
			this.image = img;
			resetScaleFactor();
			invalidate();
			return 0;
		}else{
			return -1;
		}
	}
	
	private int loadPrevImg(){
		if(fullPageMod) return -2;
		Bitmap img = null;
		img = pageImages.prev();
		if(img != null){
			this.image = img;
			resetScaleFactor();
			invalidate();
			return 0;
		}else{
			return -1;
		}
	}

	@Override
	 public void onWindowFocusChanged(boolean hasFocus) {
	  // TODO Auto-generated method stub
	  super.onWindowFocusChanged(hasFocus);
	  //Here you can get the size!
	 }
	
	@Override
	public void onDraw(Canvas canvas) {
		if (image == null)
			return;
		Rect dst = new Rect(0, 0, getScaledWidth(), getScaledHeight());
		try {
			canvas.drawBitmap(image, null, dst, paint);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	// высота облости скролинга
	@Override
	protected int computeHorizontalScrollRange() {
		return getScaledWidth();
	}

	// ширина области скролинга
	@Override
	protected int computeVerticalScrollRange() {
		return getScaledHeight();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// check for tap and cancel fling
		if ((event.getAction() & MotionEvent.ACTION_MASK) == MotionEvent.ACTION_DOWN) {
			if (!scroller.isFinished())
				scroller.abortAnimation();
		}

		// handle pinch zoom gesture
		// don't check return value since it is always true
		scaleGestureDetector.onTouchEvent(event);

		// check for scroll gesture
		if (gestureDetector.onTouchEvent(event))
			return true;
		
		// check for pointer release
		if ((event.getPointerCount() == 1)
				&& ((event.getAction() & MotionEvent.ACTION_MASK) == MotionEvent.ACTION_UP)) {
			int newScrollX = getScrollX();
			if (getScaledWidth() < getWidth())
				newScrollX = -(getWidth() - getScaledWidth()) / 2;
			else if (getScrollX() < 0)
				newScrollX = 0;
			else if (getScrollX() > getScaledWidth() - getWidth())
				newScrollX = getScaledWidth() - getWidth();

			int newScrollY = getScrollY();
			if (getScaledHeight() < getHeight())
				newScrollY = -(getHeight() - getScaledHeight()) / 2;
			else if (getScrollY() < 0)
				newScrollY = 0;
			else if (getScrollY() > getScaledHeight() - getHeight())
				newScrollY = getScaledHeight() - getHeight();

			if ((newScrollX != getScrollX()) || (newScrollY != getScrollY())) {
				scroller.startScroll(getScrollX(), getScrollY(), newScrollX
						- getScrollX(), newScrollY - getScrollY());
				awakenScrollBars(scroller.getDuration());
			}
		}
		

		return true;
	}

	// скролинг при броске
	@Override
	public void computeScroll() {
		if (scroller.computeScrollOffset()) {
			int oldX = getScrollX();
			int oldY = getScrollY();
			int x = scroller.getCurrX();
			int y = scroller.getCurrY();
			scrollTo(x, y);
			if (oldX != getScrollX() || oldY != getScrollY()) {
				onScrollChanged(getScrollX(), getScrollY(), oldX, oldY);
			}
			postInvalidate();
		}
	}

	@Override
	protected void onScrollChanged(int x, int y, int oldX, int oldY) {
	}

	// персчитываем начальный маштаб для изображения
	protected void resetScaleFactor(){
		float width = getWidth();
		float height = getHeight();
		float imgW = image.getWidth();
		float imgH = image.getHeight();
		scaleFactor = 1;
		if(width < imgW){
			scaleFactor = (float)width/imgW;
		}
		if(height < imgH * scaleFactor){
			scaleFactor = (float)height/imgH;
		}
		minScaleFactor = scaleFactor;

	}
	
	// метод вызывается при изменении размера области отображения
	@Override
	protected void onSizeChanged(int width, int height, int oldWidth, int oldHeight) {
		resetScaleFactor();
	}

	private class MyGestureListener extends SimpleOnGestureListener {
		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
			scrollBy((int) distanceX, (int) distanceY);
			return true;
		}
		
		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
			int fixedScrollX = 0, fixedScrollY = 0;
			int maxScrollX = getScaledWidth(), maxScrollY = getScaledHeight();

			if (getScaledWidth() < getWidth()) {
				fixedScrollX = -(getWidth() - getScaledWidth()) / 2;
				maxScrollX = fixedScrollX + getScaledWidth();
			}

			if (getScaledHeight() < getHeight()) {
				fixedScrollY = -(getHeight() - getScaledHeight()) / 2;
				maxScrollY = fixedScrollY + getScaledHeight();
			}
			
			if(fixedScrollX <= 0 && scaleFactor == minScaleFactor){
				if((int) velocityX < 0){
					loadNextImg();
				}else{
					loadPrevImg();
				}
				return false;
			}
			
			boolean scrollBeyondImage = (fixedScrollX < 0)
					|| (fixedScrollX > maxScrollX) || (fixedScrollY < 0)
					|| (fixedScrollY > maxScrollY);
			if (scrollBeyondImage){
				return false;
			}

			scroller.fling(getScrollX(), getScrollY(), -(int) velocityX,
					-(int) velocityY, 0, getScaledWidth() - getWidth(), 0,
					getScaledHeight() - getHeight());
			awakenScrollBars(scroller.getDuration());

			return true;
		}
		
	}
	// для зума
	private class MyScaleGestureListener implements OnScaleGestureListener {
		public boolean onScale(ScaleGestureDetector detector) {
			if(scaleFactor == 0) scaleFactor = 1;
			scaleFactor *= detector.getScaleFactor();
			if(scaleFactor < minScaleFactor){
				scaleFactor = minScaleFactor;
			}else{
				int newScrollX = (int) ((getScrollX() + detector.getFocusX())
						* detector.getScaleFactor() - detector.getFocusX());
				int newScrollY = (int) ((getScrollY() + detector.getFocusY())
						* detector.getScaleFactor() - detector.getFocusY());
				scrollTo(newScrollX, newScrollY);

				invalidate();
			}

			return true;
		}

		public boolean onScaleBegin(ScaleGestureDetector detector) {
			return true;
		}

		public void onScaleEnd(ScaleGestureDetector detector) {
		}
	}
}
