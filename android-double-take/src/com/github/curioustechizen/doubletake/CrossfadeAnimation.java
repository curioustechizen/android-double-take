package com.github.curioustechizen.doubletake;


import android.view.View;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorListenerAdapter;
import com.nineoldandroids.view.ViewHelper;
import com.nineoldandroids.view.ViewPropertyAnimator;

/**
 * A class representing a crossfade animation between two views. There is an <em>InView</em> and an <em>OutView</em>. The <em>InView</em> fades in simultaneously as <em>OutView</em> fades out.
 * <p>
 * Works best when <em>InView</em> takes up the area vacated by <em>OutView</em> - but this is not necessary.
 * <p>
 * Obtain an instance using {@link Builder} 
 * @author Kiran Rao
 *
 */
public class CrossfadeAnimation {

	private final View mOutView;
	private final View mInView;
	private final int mAnimationTime;

	private CrossfadeAnimation(View inView, View outView, int animationTime) {
		this.mInView = inView;
		this.mOutView = outView;
		this.mAnimationTime = animationTime;
	}

	/**
	 * Builder for creating an instance of {@link CrossfadeAnimation}
	 * @author Kiran Rao
	 *
	 */
	public static class Builder {

		private View inView, outView;
		private int animationTime;

		/**
		 * Creates a Builder with the mandatory parameters
		 * @param inView The View to fade in
		 * @param outView The view to fade out
		 */
		public Builder(View inView, View outView) {
			this.inView = inView;
			this.outView = outView;
			animationTime = inView.getContext().getResources()
					.getInteger(android.R.integer.config_shortAnimTime);
		}

		/**
		 * Sets the animation duration in milliseconds. Default is {@code android.R.integer.config_shortAnimTime} 
		 * @param animationTime The animation time in milliseconds
		 * @return This {@code Builder} object to allow for chaining.
		 */
		public Builder animationTime(int animationTime) {
			this.animationTime = animationTime;
			return this;
		}

		/**
		 * Build the {@code CrossfadeAnimation} object
		 * @return The {@code CrossfadeAnimation} object
		 */
		public CrossfadeAnimation build() {
			return new CrossfadeAnimation(inView, outView, animationTime);
		}
	}

	/**
	 * Run the crossfade animation
	 */
	public void animate() {
		ViewHelper.setAlpha(mInView, 0);
		mInView.setVisibility(View.VISIBLE);
		ViewPropertyAnimator.animate(mInView).alpha(1f)
			.setDuration(mAnimationTime)
			.setListener(null);

		ViewPropertyAnimator.animate(mOutView)
			.alpha(0f)
			.setDuration(mAnimationTime)
			.setListener(new AnimatorListenerAdapter() {
				@Override
				public void onAnimationEnd(Animator animation) {
					mOutView.setVisibility(View.GONE);
				}

			});
	}
	
	
}
