package com.github.curioustechizen.doubletake;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.view.View;

public class CrossfadeAnimation {

	private final View mOutView;
	private final View mInView;
	private final int mAnimationTime;

	// private final Context mContext;

	private CrossfadeAnimation(View inView, View outView, int animationTime) {
		this.mInView = inView;
		this.mOutView = outView;
		this.mAnimationTime = animationTime;
	}

	public static class Builder {

		private View inView, outView;
		private int animationTime;

		public Builder(View inView, View outView) {
			this.inView = inView;
			this.outView = outView;
			animationTime = inView.getContext().getResources()
					.getInteger(android.R.integer.config_shortAnimTime);
		}

		public Builder animationTime(int animationTime) {
			this.animationTime = animationTime;
			return this;
		}

		public CrossfadeAnimation build() {
			return new CrossfadeAnimation(inView, outView, animationTime);
		}
	}

	public void animate() {
		mInView.setAlpha(0f);
		mInView.setVisibility(View.VISIBLE);
		mInView.animate().alpha(1f)
			.setDuration(mAnimationTime)
			.setListener(null);

		mOutView.animate()
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
