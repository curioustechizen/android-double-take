package com.github.curioustechizen.doubletake;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;

public class ZoomAnimation {

	private final View mZoomedOutView;
	private final View mZoomedInView;
	private final ViewGroup mContainer;
	private final int mAnimationTime;
	private Animator mCurrentAnimator;

	private ZoomAnimation(Builder builder) {
		this.mZoomedInView = builder.zoomedInView;
		this.mZoomedOutView = builder.zoomedOutView;
		this.mContainer = builder.container;
		this.mAnimationTime = builder.animationTime;

		//init();
	}

	public static class Builder {
		private View zoomedOutView, zoomedInView;
		private ViewGroup container;
		private int animationTime;

		public Builder(View zoomedOutView, View zoomedInView,
				ViewGroup container) {
			this.zoomedOutView = zoomedOutView;
			this.zoomedInView = zoomedInView;
			this.container = container;
			animationTime = zoomedInView.getContext().getResources()
					.getInteger(android.R.integer.config_mediumAnimTime);
		}

		public Builder animationTime(int animationTime) {
			this.animationTime = animationTime;
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

		// The start bounds are the global visible rectangle of the thumbnail,
		// and the
		// final bounds are the global visible rectangle of the container view.
		// Also
		// set the container view's offset as the origin for the bounds, since
		// that's
		// the origin for the positioning animation properties (X, Y).
		mZoomedOutView.getGlobalVisibleRect(startBounds);
		mContainer.getGlobalVisibleRect(finalBounds, globalOffset);
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

		// Load the high-resolution "zoomed-in" image.
		// final ImageView expandedImageView = (ImageView)
		// findViewById(R.id.expanded_image);
		// expandedImageView.setImageResource(imageResId);

		// Hide the thumbnail and show the zoomed-in view. When the animation
		// begins,
		// it will position the zoomed-in view in the place of the thumbnail.
		mZoomedOutView.setAlpha(0f);
		mZoomedInView.setVisibility(View.VISIBLE);

		// Set the pivot point for SCALE_X and SCALE_Y transformations to the
		// top-left corner of
		// the zoomed-in view (the default is the center of the view).
		mZoomedInView.setPivotX(0f);
		mZoomedInView.setPivotY(0f);

		// Construct and run the parallel animation of the four translation and
		// scale properties
		// (X, Y, SCALE_X, and SCALE_Y).
		AnimatorSet set = new AnimatorSet();
		set.play(
				ObjectAnimator.ofFloat(mZoomedInView, View.X, startBounds.left,
						finalBounds.left))
				.with(ObjectAnimator.ofFloat(mZoomedInView, View.Y,
						startBounds.top, finalBounds.top))
				.with(ObjectAnimator.ofFloat(mZoomedInView, View.SCALE_X,
						startScale, 1f))
				.with(ObjectAnimator.ofFloat(mZoomedInView, View.SCALE_Y,
						startScale, 1f));
		set.setDuration(mAnimationTime);
		set.setInterpolator(new DecelerateInterpolator());
		set.addListener(new AnimatorListenerAdapter() {
			@Override
			public void onAnimationEnd(Animator animation) {
				mCurrentAnimator = null;
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

		// Animate the four positioning/sizing properties in parallel, back to
		// their
		// original values.
		AnimatorSet set = new AnimatorSet();
		set.play(
				ObjectAnimator.ofFloat(mZoomedInView, View.X, startBounds.left))
				.with(ObjectAnimator.ofFloat(mZoomedInView, View.Y,
						startBounds.top))
				.with(ObjectAnimator.ofFloat(mZoomedInView, View.SCALE_X,
						startScaleFinal))
				.with(ObjectAnimator.ofFloat(mZoomedInView, View.SCALE_Y,
						startScaleFinal));
		set.setDuration(mAnimationTime);
		set.setInterpolator(new DecelerateInterpolator());
		set.addListener(new AnimatorListenerAdapter() {
			@Override
			public void onAnimationEnd(Animator animation) {
				mZoomedOutView.setAlpha(1f);
				mZoomedInView.setVisibility(View.GONE);
				mCurrentAnimator = null;
			}

			@Override
			public void onAnimationCancel(Animator animation) {
				mZoomedOutView.setAlpha(1f);
				mZoomedInView.setVisibility(View.GONE);
				mCurrentAnimator = null;
			}
		});
		set.start();
		mCurrentAnimator = set;
	}
}
