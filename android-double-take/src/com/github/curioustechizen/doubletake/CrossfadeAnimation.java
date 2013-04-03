package com.github.curioustechizen.doubletake;


import android.os.Build;
import android.view.View;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorListenerAdapter;
import com.nineoldandroids.view.ViewHelper;
import com.nineoldandroids.view.ViewPropertyAnimator;

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
