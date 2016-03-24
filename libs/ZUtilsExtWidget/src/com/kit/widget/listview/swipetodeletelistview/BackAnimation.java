/**
 * Copyright 2013 Yoann Delouis
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * 	http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.kit.widget.listview.swipetodeletelistview;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.Animator.AnimatorListener;
import com.nineoldandroids.animation.ValueAnimator;
import com.nineoldandroids.animation.ValueAnimator.AnimatorUpdateListener;

public class BackAnimation
		implements
		AnimatorListener,
		AnimatorUpdateListener
{
	private SwipeToDeleteListView listView;
	private ItemState itemState;
	private ValueAnimator animator;

	public BackAnimation(SwipeToDeleteListView listView, ItemState itemState, long duration) {
		this.listView = listView;
		this.itemState = itemState;
		float dragPercentage = itemState.getDragPercentage();
		animator = ValueAnimator.ofFloat(dragPercentage, 0);
		animator.setDuration(duration);
		animator.addUpdateListener(this);
		animator.addListener(this);
	}

	public void start() {
		animator.start();
	}

	@Override
	public void onAnimationStart(Animator animator) {
	}

	@Override
	public void onAnimationUpdate(ValueAnimator animator) {
		itemState.setDragPercentage((Float) animator.getAnimatedValue());
		listView.invalidate();
	}

	@Override
	public void onAnimationEnd(Animator animator) {
		itemState.reset();
	}

	@Override
	public void onAnimationCancel(Animator animator) {
	}

	@Override
	public void onAnimationRepeat(Animator animator) {
	}
}
