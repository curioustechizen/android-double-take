package com.github.curioustechizen.doubletake;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorListenerAdapter;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.view.ViewHelper;

import android.graphics.Point;
import android.graphics.Rect;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;

public class ZoomAnimation {

	private final View mZoomedOutView;
	private final View mZoomedInView;
	private final ViewGroup mZoomedInContainer;
	private final ViewGroup mZoomedOutContainer;
	private final int mAnimationTime;
	private Animator mCurrentAnimator;

	private ZoomAnimation(Builder builder) {
		this.mZoomedInView = builder.zoomedInView;
		this.mZoomedOutView = builder.zoomedOutView;
		this.mZoomedInContainer = builder.zoomedInContainer;
		this.mZoomedOutContainer = builder.zoomedOutContainer;
		this.mAnimationTime = builder.animationTime;

		//init();
	}

	public static class Builder {
		private View zoomedOutView, zoomedInView;
		private ViewGroup zoomedInContainer, zoomedOutContainer;
		private int animationTime;

		public Builder(View zoomedOutView, View zoomedInView,
				ViewGroup zoomedInContainer) {
			this.zoomedOutView = zoomedOutView;
			this.zoomedInView = zoomedInView;
			this.zoomedInContainer = zoomedInContainer;
			this.zoomedOutContainer = null;
			animationTime = zoomedInView.getContext().getResources()
					.getInteger(android.R.integer.config_shortAnimTime);
		}

		public Builder animationTime(int animationTime) {
			this.animationTime = animationTime;
			return this;
		}
		
		public Builder zoomedOutContainer(ViewGroup zoomedOutContainer){
			this.zoomedOutContainer = zoomedOutContainer;
			return this;
		}

		public ZoomAnimation build() {
			return new ZoomAnimation(this);
		}
	}

	final Rect startBounds = new Rect();
	final Rect finalBounds = new Rect();
	float startScale;

	private void init() {
		// Calculate the starting and ending bounds for the zoomed-in image.
		// This step
		// involves lots of math. Yay, math.

		final Point globalOffset = new Point();

		// The start bounds are the global visible rectangle of the zoomed out view,
		// and the
		// final bounds are the global visible rectangle of the container view.
		// Also
		// set the container view's offset as the origin for the bounds, since
		// that's
		// the origin for the positioning animation properties (X, Y).
		mZoomedOutView.getGlobalVisibleRect(startBounds);
		mZoomedInContainer.getGlobalVisibleRect(finalBounds, globalOffset);
		startBounds.offset(-globalOffset.x, -globalOffset.y);
		finalBounds.offset(-globalOffset.x, -globalOffset.y);

		// Adjust the start bounds to be the same aspect ratio as the final
		// bounds using the
		// "center crop" technique. This prevents undesirable stretching during
		// the animation.
		// Also calculate the start scaling factor (the end scaling factor is
		// always 1.0).
		
		if ((float) finalBounds.width() / finalBounds.height() > (float) startBounds
				.width() / startBounds.height()) {
			// Extend start bounds horizontally
			startScale = (float) startBounds.height() / finalBounds.height();
			float startWidth = startScale * finalBounds.width();
			float deltaWidth = (startWidth - startBounds.width()) / 2;
			startBounds.left -= deltaWidth;
			startBounds.right += deltaWidth;
		} else {
			// Extend start bounds vertically
			startScale = (float) startBounds.width() / finalBounds.width();
			float startHeight = startScale * finalBounds.height();
			float deltaHeight = (startHeight - startBounds.height()) / 2;
			startBounds.top -= deltaHeight;
			startBounds.bottom += deltaHeight;
		}
	}

	public void zoomIn() {
		init();
		// If there's an animation in progress, cancel it immediately and
		// proceed with this one.
		if (mCurrentAnimator != null) {
			mCurrentAnimator.cancel();
		}

		
		// Hide the zoomed-out view and show the zoomed-in view. When the animation
		// begins,
		// it will position the zoomed-in view in the place of the zoomed-out view.
		ViewHelper.setAlpha(mZoomedOutView, 0f);
		//mZoomedOutView.setAlpha(0f);
		mZoomedInView.setVisibility(View.VISIBLE);

		// Set the pivot point for SCALE_X and SCALE_Y transformations to the
		// top-left corner of
		// the zoomed-in view (the default is the center of the view).
		ViewHelper.setPivotX(mZoomedInView, 0f);
		ViewHelper.setPivotY(mZoomedInView, 0f);
//		mZoomedInView.setPivotX(0f);
//		mZoomedInView.setPivotY(0f);
		

		// Construct and run the parallel animation of the four translation and
		// scale properties
		// (X, Y, SCALE_X, and SCALE_Y).
		AnimatorSet set = new AnimatorSet();
		set.play(
				ObjectAnimator.ofFloat(mZoomedInView, "x", startBounds.left,
						finalBounds.left))
				.with(ObjectAnimator.ofFloat(mZoomedInView, "y",
						startBounds.top, finalBounds.top))
				.with(ObjectAnimator.ofFloat(mZoomedInView, "scaleX",
						startScale, 1f))
				.with(ObjectAnimator.ofFloat(mZoomedInView, "scaleY",
						startScale, 1f));
		
		set.setDuration(mAnimationTime);
		set.setInterpolator(new DecelerateInterpolator());
		set.addListener(new AnimatorListenerAdapter() {
			@Override
			public void onAnimationEnd(Animator animation) {
				mCurrentAnimator = null;
				if(mZoomedOutContainer != null){
					mZoomedOutContainer.setVisibility(View.GONE);
				}
			}

			@Override
			public void onAnimationCancel(Animator animation) {
				mCurrentAnimator = null;
			}
		});
		set.start();
		mCurrentAnimator = set;
		
		mZoomedInView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				zoomOut();
			}
		});
	}

	public void zoomOut() {
		final float startScaleFinal = startScale;
		if (mCurrentAnimator != null) {
			mCurrentAnimator.cancel();
		}

		if(mZoomedOutContainer != null){
			mZoomedOutContainer.setVisibility(View.VISIBLE);
		}
		// Animate the four positioning/sizing properties in parallel, back to
		// their
		// original values.
		AnimatorSet set = new AnimatorSet();
		set.play(
				ObjectAnimator.ofFloat(mZoomedInView, "x", startBounds.left))
				.with(ObjectAnimator.ofFloat(mZoomedInView, "y",
						startBounds.top))
				.with(ObjectAnimator.ofFloat(mZoomedInView, "scaleX",
						startScaleFinal))
				.with(ObjectAnimator.ofFloat(mZoomedInView, "scaleY",
						startScaleFinal));
		set.setDuration(mAnimationTime);
		set.setInterpolator(new DecelerateInterpolator());
		set.addListener(new AnimatorListenerAdapter() {
			@Override
			public void onAnimationEnd(Animator animation) {
				//mZoomedOutView.setAlpha(1f);
				ViewHelper.setAlpha(mZoomedOutView, 1f);
				mZoomedInView.setVisibility(View.GONE);
				mCurrentAnimator = null;
			}

			@Override
			public void onAnimationCancel(Animator animation) {
				//mZoomedOutView.setAlpha(1f);
				ViewHelper.setAlpha(mZoomedOutView, 1f);
				mZoomedInView.setVisibility(View.GONE);
				mCurrentAnimator = null;
			}
		});
		set.start();
		mCurrentAnimator = set;
	}
}
