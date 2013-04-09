/*
 * Copyright 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.curioustechizen.doubletake.sample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.github.curioustechizen.doubletake.ZoomAnimation;

/**
 * A sample showing how to zoom an image thumbnail to full-screen, by animating the bounds of the
 * zoomed image from the thumbnail bounds to the screen bounds.
 *
 * <p>In this sample, the user can touch one of two images. Touching an image zooms it in, covering
 * the entire activity content area. Touching the zoomed-in image hides it.</p>
 */
public class ZoomActivity extends FragmentActivity {
   

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_zoom);

		
		final ViewGroup container = (ViewGroup) findViewById(R.id.container);
		final View thumb1View = findViewById(R.id.thumb_button_1);
		
		
		// Hook up clicks on the thumbnail views.
		thumb1View.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				// zoomImageFromThumb(thumb1View, R.drawable.image1);
				final ImageView expandedImageView1 = (ImageView) findViewById(R.id.expanded_image);
				expandedImageView1.setImageResource(R.drawable.image1);

				final ZoomAnimation zoomAnimation1 = new ZoomAnimation.Builder(
						thumb1View, expandedImageView1, container)
					.animationTime(getResources().getInteger(
							android.R.integer.config_longAnimTime))
					.build();
				zoomAnimation1.zoomIn();
			}
		});
		

		final View thumb2View = findViewById(R.id.thumb_button_2);
		
		
		thumb2View.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				final ImageView expandedImageView2 = (ImageView) findViewById(R.id.expanded_image);
				final ZoomAnimation zoomAnimation2 = new ZoomAnimation.Builder(
						thumb2View, expandedImageView2, container).build();
				expandedImageView2.setImageResource(R.drawable.image2);
				zoomAnimation2.zoomIn();
			}
		});
	}

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // Navigate "up" the demo structure to the launchpad activity.
                // See http://developer.android.com/design/patterns/navigation.html for more.
                NavUtils.navigateUpTo(this, new Intent(this, MainActivity.class));
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
