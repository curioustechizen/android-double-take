package com.github.curioustechizen.doubletake.sample;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.github.curioustechizen.doubletake.ZoomAnimation;

public class GenericViewZoomActivity extends FragmentActivity {

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_zoom_generic_views);
		final View view1 = findViewById(R.id.view1);
		final View view2 = findViewById(R.id.view2);
		final View view3 = findViewById(R.id.view3);
		final View view4 = findViewById(R.id.view4);

		
		final ViewGroup zoomedInContainer = (ViewGroup) findViewById(
				R.id.zoomedContainer);
		final ViewGroup zoomedOutContainer = (ViewGroup) findViewById(
				R.id.tableLayout1);

		OnClickListener listener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				View zoomedView = null;
				if(BuildConfig.DEBUG){
					Log.d("android-double-take-sample", "Clicked on view");
					
				}
				if (v == view1) {
					zoomedView = findViewById(R.id.zoomedView1);
				} else if (v == view2) {
					zoomedView = findViewById(R.id.zoomedView2);
				} else if (v == view3) {
					zoomedView = findViewById(R.id.zoomedView3);
				} else if (v == view4) {
					zoomedView = findViewById(R.id.zoomedView4);
				}
				ZoomAnimation zoomAnim =new ZoomAnimation.Builder(v,zoomedView,
						zoomedInContainer)
					.animationTime(getResources().getInteger(
							android.R.integer.config_longAnimTime))
					.zoomedOutContainer(zoomedOutContainer)
					.build();
				zoomAnim.zoomIn();
			}
		};

		setCommonListenerOnMultipleView(listener, view1, view2, view3, view4);

	}

	private void setCommonListenerOnMultipleView(OnClickListener listener,
			View... views) {
		for (View v : views) {
			if (v != null) {
				v.setOnClickListener(listener);
			}
		}
	}

}
