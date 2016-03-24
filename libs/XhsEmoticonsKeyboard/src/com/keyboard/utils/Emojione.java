package com.keyboard.utils;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.os.Build;

public abstract class Emojione {
	private static final HashMap<String, String> _shortNameToUnicode = new HashMap<String, String>();
	private static final Pattern SHORTNAME_PATTERN = Pattern.compile(":([-+\\w]+)");

	/**
	 * Replace shortnames to unicode characters.
	 */
	public static String replaceShortname(String input, boolean removeIfUnsupported) {
		Matcher matcher = SHORTNAME_PATTERN.matcher(input);
		boolean supported = Build.VERSION.SDK_INT >= 16;
		while (matcher.find()) {
			String unicode = _shortNameToUnicode.get(matcher.group(1));
			if (unicode == null) {
				continue;
			}
			if (supported) {
				input = input.replace(matcher.group(1), unicode);
			} else if (!supported && removeIfUnsupported) {
				input = input.replace( matcher.group(1), "");
			}
		}
		return input;
	}
	
	public static String replaceUnicode(String input) {
		return null;
	}

	/*public static String getUnicodeByShortname(String shortname) {
		Set<Entry<String, String>> set = _shortNameToUnicode.entrySet();
		for (Entry<String, String> entry : set) {
			if (entry.getValue().equals(shortname)) {
				return entry.getKey();
			}
		}
		return "";
	}*/
	

	static {
		_shortNameToUnicode.put(":)", new String(new int[] { 0x1F60C }, 0, 1));
		_shortNameToUnicode.put(":(", new String(new int[] { 0x1F614 }, 0, 1));
		_shortNameToUnicode.put(":D", new String(new int[] { 0x1F603 }, 0, 1));
		_shortNameToUnicode.put(":'(", new String(new int[] { 0x1F62D }, 0, 1));
		_shortNameToUnicode.put(":@", new String(new int[] { 0x1F620 }, 0, 1));
		_shortNameToUnicode.put(":o", new String(new int[] { 0x1F632 }, 0, 1));
		_shortNameToUnicode.put(":P", new String(new int[] { 0x1F61C }, 0, 1));
		_shortNameToUnicode.put(":$", new String(new int[] { 0x1F606 }, 0, 1));
		_shortNameToUnicode.put(";P", new String(new int[] { 0x1F61D }, 0, 1));
		_shortNameToUnicode.put(":L", new String(new int[] { 0x1F613 }, 0, 1));
		_shortNameToUnicode.put(":Q", new String(new int[] { 0x1F62B }, 0, 1));
		_shortNameToUnicode.put(":lol", new String(new int[] { 0x1F601 }, 0, 1));
		_shortNameToUnicode.put(":loveliness:", new String(new int[] { 0x1F60A }, 0, 1));
		_shortNameToUnicode.put(":funk:", new String(new int[] { 0x1F631 }, 0, 1));
		_shortNameToUnicode.put(":curse:", new String(new int[] { 0x1F624 }, 0, 1));
		_shortNameToUnicode.put(":dizzy:", new String(new int[] { 0x1F616 }, 0, 1));
		_shortNameToUnicode.put(":shutup:", new String(new int[] { 0x1F637 }, 0, 1));
		_shortNameToUnicode.put(":sleepy:", new String(new int[] { 0x1F62A }, 0, 1));
		_shortNameToUnicode.put(":hug:", new String(new int[] { 0x1F61A }, 0, 1));
		_shortNameToUnicode.put(":victory:", new String(new int[] { 0x0270C }, 0, 1));
		_shortNameToUnicode.put(":time:", new String(new int[] { 0x023F0 }, 0, 1));
		_shortNameToUnicode.put(":kiss:", new String(new int[] { 0x1F48B }, 0, 1));
		_shortNameToUnicode.put(":handshake", new String(new int[] { 0x1F44C }, 0, 1));
		_shortNameToUnicode.put(":call:", new String(new int[] { 0x1F4DE }, 0, 1));
		/*
		 * _shortNameToUnicode.put("100", new String(new int[] {0x1F4AF}, 0,
		 * 1)); _shortNameToUnicode.put("hearts", new String(new int[] {0x2665},
		 * 0, 1)); _shortNameToUnicode.put("joy", new String(new int[]
		 * {0x1F602}, 0, 1)); _shortNameToUnicode.put("unamused", new String(new
		 * int[] {0x1F612}, 0, 1)); _shortNameToUnicode.put("heart_eyes", new
		 * String(new int[] {0x1F60D}, 0, 1)); _shortNameToUnicode.put("heart",
		 * new String(new int[] {0x2764}, 0, 1));
		 * _shortNameToUnicode.put("relaxed", new String(new int[] {0x263A}, 0,
		 * 1)); _shortNameToUnicode.put("ok_hand", new String(new int[]
		 * {0x1F44C}, 0, 1)); _shortNameToUnicode.put("kissing_heart", new
		 * String(new int[] {0x1F618}, 0, 1)); _shortNameToUnicode.put("blush",
		 * new String(new int[] {0x1F60A}, 0, 1));
		 * _shortNameToUnicode.put("weary", new String(new int[] {0x1F629}, 0,
		 * 1)); _shortNameToUnicode.put("pensive", new String(new int[]
		 * {0x1F614}, 0, 1)); _shortNameToUnicode.put("sob", new String(new
		 * int[] {0x1F62D}, 0, 1)); _shortNameToUnicode.put("smirk", new
		 * String(new int[] {0x1F60F}, 0, 1));
		 * _shortNameToUnicode.put("two_hearts", new String(new int[] {0x1F495},
		 * 0, 1)); _shortNameToUnicode.put("grin", new String(new int[]
		 * {0x1F601}, 0, 1)); _shortNameToUnicode.put("flushed", new String(new
		 * int[] {0x1F633}, 0, 1)); _shortNameToUnicode.put("thumbsup", new
		 * String(new int[] {0x1F44D}, 0, 1));
		 * _shortNameToUnicode.put("raised_hands", new String(new int[]
		 * {0x1F64C}, 0, 1)); _shortNameToUnicode.put("wink", new String(new
		 * int[] {0x1F609}, 0, 1));
		 * _shortNameToUnicode.put("information_desk_person", new String(new
		 * int[] {0x1F481}, 0, 1)); _shortNameToUnicode.put("relieved", new
		 * String(new int[] {0x1F60C}, 0, 1));
		 * _shortNameToUnicode.put("see_no_evil", new String(new int[]
		 * {0x1F648}, 0, 1)); _shortNameToUnicode.put("v", new String(new int[]
		 * {0x270C}, 0, 1)); _shortNameToUnicode.put("pray", new String(new
		 * int[] {0x1F64F}, 0, 1)); _shortNameToUnicode.put("yum", new
		 * String(new int[] {0x1F60B}, 0, 1));
		 * _shortNameToUnicode.put("stuck_out_tongue_winking_eye", new
		 * String(new int[] {0x1F61C}, 0, 1)); _shortNameToUnicode.put("notes",
		 * new String(new int[] {0x1F3B6}, 0, 1));
		 * _shortNameToUnicode.put("eyes", new String(new int[] {0x1F440}, 0,
		 * 1)); _shortNameToUnicode.put("smile", new String(new int[] {0x1F604},
		 * 0, 1)); _shortNameToUnicode.put("disappointed", new String(new int[]
		 * {0x1F61E}, 0, 1)); _shortNameToUnicode.put("raised_hand", new
		 * String(new int[] {0x270B}, 0, 1)); _shortNameToUnicode.put("clap",
		 * new String(new int[] {0x1F44F}, 0, 1));
		 * _shortNameToUnicode.put("speak_no_evil", new String(new int[]
		 * {0x1F64A}, 0, 1)); _shortNameToUnicode.put("cry", new String(new
		 * int[] {0x1F622}, 0, 1)); _shortNameToUnicode.put("rage", new
		 * String(new int[] {0x1F621}, 0, 1));
		 * _shortNameToUnicode.put("tired_face", new String(new int[] {0x1F62B},
		 * 0, 1)); _shortNameToUnicode.put("scream", new String(new int[]
		 * {0x1F631}, 0, 1)); _shortNameToUnicode.put("purple_heart", new
		 * String(new int[] {0x1F49C}, 0, 1));
		 * _shortNameToUnicode.put("broken_heart", new String(new int[]
		 * {0x1F494}, 0, 1)); _shortNameToUnicode.put("kiss", new String(new
		 * int[] {0x1F48B}, 0, 1)); _shortNameToUnicode.put("blue_heart", new
		 * String(new int[] {0x1F499}, 0, 1)); _shortNameToUnicode.put("sleepy",
		 * new String(new int[] {0x1F62A}, 0, 1));
		 * _shortNameToUnicode.put("sweat_smile", new String(new int[]
		 * {0x1F605}, 0, 1));
		 * _shortNameToUnicode.put("stuck_out_tongue_closed_eyes", new
		 * String(new int[] {0x1F61D}, 0, 1)); _shortNameToUnicode.put("punch",
		 * new String(new int[] {0x1F44A}, 0, 1));
		 * _shortNameToUnicode.put("triumph", new String(new int[] {0x1F624}, 0,
		 * 1)); _shortNameToUnicode.put("sparkling_heart", new String(new int[]
		 * {0x1F496}, 0, 1)); _shortNameToUnicode.put("smiley", new String(new
		 * int[] {0x1F603}, 0, 1)); _shortNameToUnicode.put("sunny", new
		 * String(new int[] {0x2600}, 0, 1));
		 * _shortNameToUnicode.put("heartpulse", new String(new int[] {0x1F497},
		 * 0, 1)); _shortNameToUnicode.put("wave", new String(new int[]
		 * {0x1F44B}, 0, 1)); _shortNameToUnicode.put("mask", new String(new
		 * int[] {0x1F637}, 0, 1)); _shortNameToUnicode.put("heavy_check_mark",
		 * new String(new int[] {0x2714}, 0, 1));
		 * _shortNameToUnicode.put("cherry_blossom", new String(new int[]
		 * {0x1F338}, 0, 1)); _shortNameToUnicode.put("rose", new String(new
		 * int[] {0x1F339}, 0, 1)); _shortNameToUnicode.put("persevere", new
		 * String(new int[] {0x1F623}, 0, 1));
		 * _shortNameToUnicode.put("revolving_hearts", new String(new int[]
		 * {0x1F49E}, 0, 1)); _shortNameToUnicode.put("sparkles", new String(new
		 * int[] {0x2728}, 0, 1)); _shortNameToUnicode.put("confounded", new
		 * String(new int[] {0x1F616}, 0, 1)); _shortNameToUnicode.put("tada",
		 * new String(new int[] {0x1F389}, 0, 1));
		 * _shortNameToUnicode.put("no_good", new String(new int[] {0x1F645}, 0,
		 * 1)); _shortNameToUnicode.put("muscle", new String(new int[]
		 * {0x1F4AA}, 0, 1)); _shortNameToUnicode.put("angry", new String(new
		 * int[] {0x1F620}, 0, 1)); _shortNameToUnicode.put("gun", new
		 * String(new int[] {0x1F52B}, 0, 1)); _shortNameToUnicode.put("cupid",
		 * new String(new int[] {0x1F498}, 0, 1));
		 * _shortNameToUnicode.put("sweat", new String(new int[] {0x1F613}, 0,
		 * 1)); _shortNameToUnicode.put("laughing", new String(new int[]
		 * {0x1F606}, 0, 1)); _shortNameToUnicode.put("yellow_heart", new
		 * String(new int[] {0x1F49B}, 0, 1));
		 * _shortNameToUnicode.put("kissing_closed_eyes", new String(new int[]
		 * {0x1F61A}, 0, 1)); _shortNameToUnicode.put("disappointed_relieved",
		 * new String(new int[] {0x1F625}, 0, 1));
		 * _shortNameToUnicode.put("raising_hand", new String(new int[]
		 * {0x1F64B}, 0, 1)); _shortNameToUnicode.put("fist", new String(new
		 * int[] {0x270A}, 0, 1)); _shortNameToUnicode.put("green_heart", new
		 * String(new int[] {0x1F49A}, 0, 1));
		 * _shortNameToUnicode.put("headphones", new String(new int[] {0x1F3A7},
		 * 0, 1)); _shortNameToUnicode.put("thumbsdown", new String(new int[]
		 * {0x1F44E}, 0, 1)); _shortNameToUnicode.put("heart_eyes_cat", new
		 * String(new int[] {0x1F63B}, 0, 1)); _shortNameToUnicode.put("dancer",
		 * new String(new int[] {0x1F483}, 0, 1));
		 * _shortNameToUnicode.put("skull", new String(new int[] {0x1F480}, 0,
		 * 1)); _shortNameToUnicode.put("poop", new String(new int[] {0x1F4A9},
		 * 0, 1)); _shortNameToUnicode.put("fire", new String(new int[]
		 * {0x1F525}, 0, 1)); _shortNameToUnicode.put("walking", new String(new
		 * int[] {0x1F6B6}, 0, 1)); _shortNameToUnicode.put("cold_sweat", new
		 * String(new int[] {0x1F630}, 0, 1));
		 * _shortNameToUnicode.put("copyright", new String(new int[] {0x00A9},
		 * 0, 1)); _shortNameToUnicode.put("penguin", new String(new int[]
		 * {0x1F427}, 0, 1)); _shortNameToUnicode.put("crown", new String(new
		 * int[] {0x1F451}, 0, 1)); _shortNameToUnicode.put("open_hands", new
		 * String(new int[] {0x1F450}, 0, 1));
		 * _shortNameToUnicode.put("point_right", new String(new int[]
		 * {0x1F449}, 0, 1)); _shortNameToUnicode.put("heartbeat", new
		 * String(new int[] {0x1F493}, 0, 1));
		 * _shortNameToUnicode.put("dancers", new String(new int[] {0x1F46F}, 0,
		 * 1)); _shortNameToUnicode.put("ok_woman", new String(new int[]
		 * {0x1F646}, 0, 1)); _shortNameToUnicode.put("pizza", new String(new
		 * int[] {0x1F355}, 0, 1));
		 * _shortNameToUnicode.put("ballot_box_with_check", new String(new int[]
		 * {0x2611}, 0, 1)); _shortNameToUnicode.put("zzz", new String(new int[]
		 * {0x1F4A4}, 0, 1)); _shortNameToUnicode.put("point_left", new
		 * String(new int[] {0x1F448}, 0, 1));
		 * _shortNameToUnicode.put("musical_note", new String(new int[]
		 * {0x1F3B5}, 0, 1)); _shortNameToUnicode.put("bow", new String(new
		 * int[] {0x1F647}, 0, 1)); _shortNameToUnicode.put("fearful", new
		 * String(new int[] {0x1F628}, 0, 1)); _shortNameToUnicode.put("ribbon",
		 * new String(new int[] {0x1F380}, 0, 1));
		 * _shortNameToUnicode.put("joy_cat", new String(new int[] {0x1F639}, 0,
		 * 1)); _shortNameToUnicode.put("arrow_forward", new String(new int[]
		 * {0x25B6}, 0, 1)); _shortNameToUnicode.put("tongue", new String(new
		 * int[] {0x1F445}, 0, 1)); _shortNameToUnicode.put("runner", new
		 * String(new int[] {0x1F3C3}, 0, 1));
		 * _shortNameToUnicode.put("point_up", new String(new int[] {0x261D}, 0,
		 * 1)); _shortNameToUnicode.put("airplane", new String(new int[]
		 * {0x2708}, 0, 1)); _shortNameToUnicode.put("gem", new String(new int[]
		 * {0x1F48E}, 0, 1)); _shortNameToUnicode.put("person_frowning", new
		 * String(new int[] {0x1F64D}, 0, 1));
		 * _shortNameToUnicode.put("hibiscus", new String(new int[] {0x1F33A},
		 * 0, 1)); _shortNameToUnicode.put("basketball", new String(new int[]
		 * {0x1F3C0}, 0, 1)); _shortNameToUnicode.put("boom", new String(new
		 * int[] {0x1F4A5}, 0, 1)); _shortNameToUnicode.put("nail_care", new
		 * String(new int[] {0x1F485}, 0, 1));
		 * _shortNameToUnicode.put("dizzy_face", new String(new int[] {0x1F635},
		 * 0, 1)); _shortNameToUnicode.put("balloon", new String(new int[]
		 * {0x1F388}, 0, 1)); _shortNameToUnicode.put("couple", new String(new
		 * int[] {0x1F46B}, 0, 1)); _shortNameToUnicode.put("dog", new
		 * String(new int[] {0x1F436}, 0, 1));
		 * _shortNameToUnicode.put("sweat_drops", new String(new int[]
		 * {0x1F4A6}, 0, 1)); _shortNameToUnicode.put("star2", new String(new
		 * int[] {0x1F31F}, 0, 1)); _shortNameToUnicode.put("hear_no_evil", new
		 * String(new int[] {0x1F649}, 0, 1));
		 * _shortNameToUnicode.put("moneybag", new String(new int[] {0x1F4B0},
		 * 0, 1)); _shortNameToUnicode.put("beers", new String(new int[]
		 * {0x1F37B}, 0, 1)); _shortNameToUnicode.put("couplekiss", new
		 * String(new int[] {0x1F48F}, 0, 1));
		 * _shortNameToUnicode.put("point_down", new String(new int[] {0x1F447},
		 * 0, 1)); _shortNameToUnicode.put("cloud", new String(new int[]
		 * {0x2601}, 0, 1)); _shortNameToUnicode.put("alien", new String(new
		 * int[] {0x1F47D}, 0, 1)); _shortNameToUnicode.put("dizzy", new
		 * String(new int[] {0x1F4AB}, 0, 1));
		 * _shortNameToUnicode.put("heavy_multiplication_x", new String(new
		 * int[] {0x2716}, 0, 1)); _shortNameToUnicode.put("white_check_mark",
		 * new String(new int[] {0x2705}, 0, 1));
		 * _shortNameToUnicode.put("palm_tree", new String(new int[] {0x1F334},
		 * 0, 1)); _shortNameToUnicode.put("dash", new String(new int[]
		 * {0x1F4A8}, 0, 1)); _shortNameToUnicode.put("exclamation", new
		 * String(new int[] {0x2757}, 0, 1)); _shortNameToUnicode.put("soccer",
		 * new String(new int[] {0x26BD}, 0, 1));
		 * _shortNameToUnicode.put("microphone", new String(new int[] {0x1F3A4},
		 * 0, 1)); _shortNameToUnicode.put("angel", new String(new int[]
		 * {0x1F47C}, 0, 1)); _shortNameToUnicode.put("point_up_2", new
		 * String(new int[] {0x1F446}, 0, 1));
		 * _shortNameToUnicode.put("snowflake", new String(new int[] {0x2744},
		 * 0, 1)); _shortNameToUnicode.put("astonished", new String(new int[]
		 * {0x1F632}, 0, 1)); _shortNameToUnicode.put("four_leaf_clover", new
		 * String(new int[] {0x1F340}, 0, 1)); _shortNameToUnicode.put("ghost",
		 * new String(new int[] {0x1F47B}, 0, 1));
		 * _shortNameToUnicode.put("princess", new String(new int[] {0x1F478},
		 * 0, 1)); _shortNameToUnicode.put("cat", new String(new int[]
		 * {0x1F431}, 0, 1)); _shortNameToUnicode.put("ring", new String(new
		 * int[] {0x1F48D}, 0, 1)); _shortNameToUnicode.put("sunflower", new
		 * String(new int[] {0x1F33B}, 0, 1)); _shortNameToUnicode.put("o", new
		 * String(new int[] {0x2B55}, 0, 1));
		 * _shortNameToUnicode.put("crescent_moon", new String(new int[]
		 * {0x1F319}, 0, 1)); _shortNameToUnicode.put("gift", new String(new
		 * int[] {0x1F381}, 0, 1)); _shortNameToUnicode.put("crying_cat_face",
		 * new String(new int[] {0x1F63F}, 0, 1));
		 * _shortNameToUnicode.put("bouquet", new String(new int[] {0x1F490}, 0,
		 * 1)); _shortNameToUnicode.put("star", new String(new int[] {0x2B50},
		 * 0, 1)); _shortNameToUnicode.put("leaves", new String(new int[]
		 * {0x1F343}, 0, 1)); _shortNameToUnicode.put("cactus", new String(new
		 * int[] {0x1F335}, 0, 1)); _shortNameToUnicode.put("clubs", new
		 * String(new int[] {0x2663}, 0, 1));
		 * _shortNameToUnicode.put("diamonds", new String(new int[] {0x2666}, 0,
		 * 1)); _shortNameToUnicode.put("massage", new String(new int[]
		 * {0x1F486}, 0, 1)); _shortNameToUnicode.put("imp", new String(new
		 * int[] {0x1F47F}, 0, 1)); _shortNameToUnicode.put("red_circle", new
		 * String(new int[] {0x1F534}, 0, 1));
		 * _shortNameToUnicode.put("money_with_wings", new String(new int[]
		 * {0x1F4B8}, 0, 1)); _shortNameToUnicode.put("football", new String(new
		 * int[] {0x1F3C8}, 0, 1)); _shortNameToUnicode.put("cyclone", new
		 * String(new int[] {0x1F300}, 0, 1));
		 * _shortNameToUnicode.put("smirk_cat", new String(new int[] {0x1F63C},
		 * 0, 1)); _shortNameToUnicode.put("snowman", new String(new int[]
		 * {0x26C4}, 0, 1)); _shortNameToUnicode.put("birthday", new String(new
		 * int[] {0x1F382}, 0, 1)); _shortNameToUnicode.put("baby", new
		 * String(new int[] {0x1F476}, 0, 1));
		 * _shortNameToUnicode.put("telephone", new String(new int[] {0x260E},
		 * 0, 1)); _shortNameToUnicode.put("eggplant", new String(new int[]
		 * {0x1F346}, 0, 1)); _shortNameToUnicode.put("gift_heart", new
		 * String(new int[] {0x1F49D}, 0, 1)); _shortNameToUnicode.put("tulip",
		 * new String(new int[] {0x1F337}, 0, 1));
		 * _shortNameToUnicode.put("confetti_ball", new String(new int[]
		 * {0x1F38A}, 0, 1)); _shortNameToUnicode.put("black_small_square", new
		 * String(new int[] {0x25AA}, 0, 1)); _shortNameToUnicode.put("coffee",
		 * new String(new int[] {0x2615}, 0, 1));
		 * _shortNameToUnicode.put("scream_cat", new String(new int[] {0x1F640},
		 * 0, 1)); _shortNameToUnicode.put("rocket", new String(new int[]
		 * {0x1F680}, 0, 1)); _shortNameToUnicode.put("christmas_tree", new
		 * String(new int[] {0x1F384}, 0, 1)); _shortNameToUnicode.put("x", new
		 * String(new int[] {0x274C}, 0, 1)); _shortNameToUnicode.put("knife",
		 * new String(new int[] {0x1F52A}, 0, 1));
		 * _shortNameToUnicode.put("bangbang", new String(new int[] {0x203C}, 0,
		 * 1)); _shortNameToUnicode.put("smile_cat", new String(new int[]
		 * {0x1F638}, 0, 1)); _shortNameToUnicode.put("kissing_cat", new
		 * String(new int[] {0x1F63D}, 0, 1));
		 * _shortNameToUnicode.put("doughnut", new String(new int[] {0x1F369},
		 * 0, 1)); _shortNameToUnicode.put("couple_with_heart", new String(new
		 * int[] {0x1F491}, 0, 1)); _shortNameToUnicode.put("spades", new
		 * String(new int[] {0x2660}, 0, 1)); _shortNameToUnicode.put("bomb",
		 * new String(new int[] {0x1F4A3}, 0, 1));
		 * _shortNameToUnicode.put("guitar", new String(new int[] {0x1F3B8}, 0,
		 * 1)); _shortNameToUnicode.put("space_invader", new String(new int[]
		 * {0x1F47E}, 0, 1)); _shortNameToUnicode.put("maple_leaf", new
		 * String(new int[] {0x1F341}, 0, 1)); _shortNameToUnicode.put("pig",
		 * new String(new int[] {0x1F437}, 0, 1));
		 * _shortNameToUnicode.put("guardsman", new String(new int[] {0x1F482},
		 * 0, 1)); _shortNameToUnicode.put("fork_and_knife", new String(new
		 * int[] {0x1F374}, 0, 1)); _shortNameToUnicode.put("lips", new
		 * String(new int[] {0x1F444}, 0, 1)); _shortNameToUnicode.put("santa",
		 * new String(new int[] {0x1F385}, 0, 1));
		 * _shortNameToUnicode.put("beer", new String(new int[] {0x1F37A}, 0,
		 * 1)); _shortNameToUnicode.put("red_car", new String(new int[]
		 * {0x1F697}, 0, 1)); _shortNameToUnicode.put("zap", new String(new
		 * int[] {0x26A1}, 0, 1)); _shortNameToUnicode.put("ocean", new
		 * String(new int[] {0x1F30A}, 0, 1)); _shortNameToUnicode.put("banana",
		 * new String(new int[] {0x1F34C}, 0, 1)); _shortNameToUnicode.put("tm",
		 * new String(new int[] {0x1F1F9,0x1F1F2}, 0, 2));
		 * _shortNameToUnicode.put("turtle", new String(new int[] {0x1F422}, 0,
		 * 1)); _shortNameToUnicode.put("movie_camera", new String(new int[]
		 * {0x1F3A5}, 0, 1)); _shortNameToUnicode.put("video_game", new
		 * String(new int[] {0x1F3AE}, 0, 1)); _shortNameToUnicode.put("trophy",
		 * new String(new int[] {0x1F3C6}, 0, 1));
		 * _shortNameToUnicode.put("man", new String(new int[] {0x1F468}, 0,
		 * 1)); _shortNameToUnicode.put("umbrella", new String(new int[]
		 * {0x2614}, 0, 1)); _shortNameToUnicode.put("tiger", new String(new
		 * int[] {0x1F42F}, 0, 1)); _shortNameToUnicode.put("smoking", new
		 * String(new int[] {0x1F6AC}, 0, 1));
		 * _shortNameToUnicode.put("watermelon", new String(new int[] {0x1F349},
		 * 0, 1)); _shortNameToUnicode.put("person_with_pouting_face", new
		 * String(new int[] {0x1F64E}, 0, 1)); _shortNameToUnicode.put("herb",
		 * new String(new int[] {0x1F33F}, 0, 1));
		 * _shortNameToUnicode.put("footprints", new String(new int[] {0x1F463},
		 * 0, 1)); _shortNameToUnicode.put("camera", new String(new int[]
		 * {0x1F4F7}, 0, 1)); _shortNameToUnicode.put("japanese_ogre", new
		 * String(new int[] {0x1F479}, 0, 1)); _shortNameToUnicode.put("cookie",
		 * new String(new int[] {0x1F36A}, 0, 1));
		 * _shortNameToUnicode.put("recycle", new String(new int[] {0x267B}, 0,
		 * 1)); _shortNameToUnicode.put("wine_glass", new String(new int[]
		 * {0x1F377}, 0, 1)); _shortNameToUnicode.put("arrow_right", new
		 * String(new int[] {0x27A1}, 0, 1));
		 * _shortNameToUnicode.put("panda_face", new String(new int[] {0x1F43C},
		 * 0, 1)); _shortNameToUnicode.put("dollar", new String(new int[]
		 * {0x1F4B5}, 0, 1)); _shortNameToUnicode.put("hamburger", new
		 * String(new int[] {0x1F354}, 0, 1));
		 * _shortNameToUnicode.put("icecream", new String(new int[] {0x1F366},
		 * 0, 1)); _shortNameToUnicode.put("fries", new String(new int[]
		 * {0x1F35F}, 0, 1)); _shortNameToUnicode.put("arrow_left", new
		 * String(new int[] {0x2B05}, 0, 1)); _shortNameToUnicode.put("rainbow",
		 * new String(new int[] {0x1F308}, 0, 1));
		 * _shortNameToUnicode.put("earth_asia", new String(new int[] {0x1F30F},
		 * 0, 1)); _shortNameToUnicode.put("anger", new String(new int[]
		 * {0x1F4A2}, 0, 1)); _shortNameToUnicode.put("swimmer", new String(new
		 * int[] {0x1F3CA}, 0, 1)); _shortNameToUnicode.put("blossom", new
		 * String(new int[] {0x1F33C}, 0, 1));
		 * _shortNameToUnicode.put("calling", new String(new int[] {0x1F4F2}, 0,
		 * 1)); _shortNameToUnicode.put("haircut", new String(new int[]
		 * {0x1F487}, 0, 1)); _shortNameToUnicode.put("heart_decoration", new
		 * String(new int[] {0x1F49F}, 0, 1)); _shortNameToUnicode.put("cake",
		 * new String(new int[] {0x1F370}, 0, 1));
		 * _shortNameToUnicode.put("lollipop", new String(new int[] {0x1F36D},
		 * 0, 1)); _shortNameToUnicode.put("pouting_cat", new String(new int[]
		 * {0x1F63E}, 0, 1)); _shortNameToUnicode.put("syringe", new String(new
		 * int[] {0x1F489}, 0, 1)); _shortNameToUnicode.put("registered", new
		 * String(new int[] {0x00AE}, 0, 1));
		 * _shortNameToUnicode.put("partly_sunny", new String(new int[]
		 * {0x26C5}, 0, 1)); _shortNameToUnicode.put("iphone", new String(new
		 * int[] {0x1F4F1}, 0, 1)); _shortNameToUnicode.put("arrow_backward",
		 * new String(new int[] {0x25C0}, 0, 1));
		 * _shortNameToUnicode.put("whale", new String(new int[] {0x1F433}, 0,
		 * 1)); _shortNameToUnicode.put("envelope", new String(new int[]
		 * {0x2709}, 0, 1)); _shortNameToUnicode.put("tropical_drink", new
		 * String(new int[] {0x1F379}, 0, 1));
		 * _shortNameToUnicode.put("cocktail", new String(new int[] {0x1F378},
		 * 0, 1)); _shortNameToUnicode.put("hatching_chick", new String(new
		 * int[] {0x1F423}, 0, 1)); _shortNameToUnicode.put("smiley_cat", new
		 * String(new int[] {0x1F63A}, 0, 1));
		 * _shortNameToUnicode.put("fallen_leaf", new String(new int[]
		 * {0x1F342}, 0, 1)); _shortNameToUnicode.put("bear", new String(new
		 * int[] {0x1F43B}, 0, 1)); _shortNameToUnicode.put("man_with_turban",
		 * new String(new int[] {0x1F473}, 0, 1));
		 * _shortNameToUnicode.put("monkey", new String(new int[] {0x1F412}, 0,
		 * 1)); _shortNameToUnicode.put("full_moon", new String(new int[]
		 * {0x1F315}, 0, 1)); _shortNameToUnicode.put("chocolate_bar", new
		 * String(new int[] {0x1F36B}, 0, 1)); _shortNameToUnicode.put("rabbit",
		 * new String(new int[] {0x1F430}, 0, 1));
		 * _shortNameToUnicode.put("musical_score", new String(new int[]
		 * {0x1F3BC}, 0, 1)); _shortNameToUnicode.put("snake", new String(new
		 * int[] {0x1F40D}, 0, 1)); _shortNameToUnicode.put("bee", new
		 * String(new int[] {0x1F41D}, 0, 1));
		 * _shortNameToUnicode.put("mortar_board", new String(new int[]
		 * {0x1F393}, 0, 1)); _shortNameToUnicode.put("new_moon", new String(new
		 * int[] {0x1F311}, 0, 1)); _shortNameToUnicode.put("woman", new
		 * String(new int[] {0x1F469}, 0, 1));
		 * _shortNameToUnicode.put("baseball", new String(new int[] {0x26BE}, 0,
		 * 1)); _shortNameToUnicode.put("older_woman", new String(new int[]
		 * {0x1F475}, 0, 1)); _shortNameToUnicode.put("no_entry_sign", new
		 * String(new int[] {0x1F6AB}, 0, 1));
		 * _shortNameToUnicode.put("dolphin", new String(new int[] {0x1F42C}, 0,
		 * 1)); _shortNameToUnicode.put("books", new String(new int[] {0x1F4DA},
		 * 0, 1)); _shortNameToUnicode.put("bikini", new String(new int[]
		 * {0x1F459}, 0, 1)); _shortNameToUnicode.put("tv", new String(new int[]
		 * {0x1F1F9,0x1F1FB}, 0, 2)); _shortNameToUnicode.put("strawberry", new
		 * String(new int[] {0x1F353}, 0, 1)); _shortNameToUnicode.put("feet",
		 * new String(new int[] {0x1F43E}, 0, 1));
		 * _shortNameToUnicode.put("family", new String(new int[] {0x1F46A}, 0,
		 * 1)); _shortNameToUnicode.put("hatched_chick", new String(new int[]
		 * {0x1F425}, 0, 1)); _shortNameToUnicode.put("nose", new String(new
		 * int[] {0x1F443}, 0, 1)); _shortNameToUnicode.put("cherries", new
		 * String(new int[] {0x1F352}, 0, 1));
		 * _shortNameToUnicode.put("jack_o_lantern", new String(new int[]
		 * {0x1F383}, 0, 1)); _shortNameToUnicode.put("ear_of_rice", new
		 * String(new int[] {0x1F33E}, 0, 1));
		 * _shortNameToUnicode.put("scissors", new String(new int[] {0x2702}, 0,
		 * 1)); _shortNameToUnicode.put("frog", new String(new int[] {0x1F438},
		 * 0, 1)); _shortNameToUnicode.put("octopus", new String(new int[]
		 * {0x1F419}, 0, 1)); _shortNameToUnicode.put("high_heel", new
		 * String(new int[] {0x1F460}, 0, 1));
		 * _shortNameToUnicode.put("loud_sound", new String(new int[] {0x1F50A},
		 * 0, 1)); _shortNameToUnicode.put("top", new String(new int[]
		 * {0x1F51D}, 0, 1)); _shortNameToUnicode.put("house_with_garden", new
		 * String(new int[] {0x1F3E1}, 0, 1));
		 * _shortNameToUnicode.put("rotating_light", new String(new int[]
		 * {0x1F6A8}, 0, 1)); _shortNameToUnicode.put("lipstick", new String(new
		 * int[] {0x1F484}, 0, 1)); _shortNameToUnicode.put("ear", new
		 * String(new int[] {0x1F442}, 0, 1));
		 * _shortNameToUnicode.put("first_quarter_moon", new String(new int[]
		 * {0x1F313}, 0, 1)); _shortNameToUnicode.put("pineapple", new
		 * String(new int[] {0x1F34D}, 0, 1));
		 * _shortNameToUnicode.put("elephant", new String(new int[] {0x1F418},
		 * 0, 1)); _shortNameToUnicode.put("athletic_shoe", new String(new int[]
		 * {0x1F45F}, 0, 1)); _shortNameToUnicode.put("crystal_ball", new
		 * String(new int[] {0x1F52E}, 0, 1));
		 * _shortNameToUnicode.put("love_letter", new String(new int[]
		 * {0x1F48C}, 0, 1)); _shortNameToUnicode.put("waxing_gibbous_moon", new
		 * String(new int[] {0x1F314}, 0, 1)); _shortNameToUnicode.put("girl",
		 * new String(new int[] {0x1F467}, 0, 1));
		 * _shortNameToUnicode.put("cool", new String(new int[] {0x1F192}, 0,
		 * 1)); _shortNameToUnicode.put("white_circle", new String(new int[]
		 * {0x26AA}, 0, 1)); _shortNameToUnicode.put("poultry_leg", new
		 * String(new int[] {0x1F357}, 0, 1));
		 * _shortNameToUnicode.put("speech_balloon", new String(new int[]
		 * {0x1F4AC}, 0, 1)); _shortNameToUnicode.put("question", new String(new
		 * int[] {0x2753}, 0, 1)); _shortNameToUnicode.put("tropical_fish", new
		 * String(new int[] {0x1F420}, 0, 1));
		 * _shortNameToUnicode.put("older_man", new String(new int[] {0x1F474},
		 * 0, 1)); _shortNameToUnicode.put("bride_with_veil", new String(new
		 * int[] {0x1F470}, 0, 1)); _shortNameToUnicode.put("peach", new
		 * String(new int[] {0x1F351}, 0, 1));
		 * _shortNameToUnicode.put("eyeglasses", new String(new int[] {0x1F453},
		 * 0, 1)); _shortNameToUnicode.put("pencil", new String(new int[]
		 * {0x1F4DD}, 0, 1)); _shortNameToUnicode.put("spaghetti", new
		 * String(new int[] {0x1F35D}, 0, 1)); _shortNameToUnicode.put("boy",
		 * new String(new int[] {0x1F466}, 0, 1));
		 * _shortNameToUnicode.put("black_circle", new String(new int[]
		 * {0x26AB}, 0, 1)); _shortNameToUnicode.put("book", new String(new
		 * int[] {0x1F4D6}, 0, 1)); _shortNameToUnicode.put("pill", new
		 * String(new int[] {0x1F48A}, 0, 1));
		 * _shortNameToUnicode.put("loudspeaker", new String(new int[]
		 * {0x1F4E2}, 0, 1)); _shortNameToUnicode.put("horse", new String(new
		 * int[] {0x1F434}, 0, 1)); _shortNameToUnicode.put("milky_way", new
		 * String(new int[] {0x1F30C}, 0, 1)); _shortNameToUnicode.put("fish",
		 * new String(new int[] {0x1F41F}, 0, 1));
		 * _shortNameToUnicode.put("surfer", new String(new int[] {0x1F3C4}, 0,
		 * 1)); _shortNameToUnicode.put("closed_lock_with_key", new String(new
		 * int[] {0x1F510}, 0, 1)); _shortNameToUnicode.put("warning", new
		 * String(new int[] {0x26A0}, 0, 1)); _shortNameToUnicode.put("apple",
		 * new String(new int[] {0x1F34E}, 0, 1));
		 * _shortNameToUnicode.put("fishing_pole_and_fish", new String(new int[]
		 * {0x1F3A3}, 0, 1)); _shortNameToUnicode.put("dress", new String(new
		 * int[] {0x1F457}, 0, 1)); _shortNameToUnicode.put("clapper", new
		 * String(new int[] {0x1F3AC}, 0, 1));
		 * _shortNameToUnicode.put("man_with_gua_pi_mao", new String(new int[]
		 * {0x1F472}, 0, 1)); _shortNameToUnicode.put("sunrise", new String(new
		 * int[] {0x1F305}, 0, 1)); _shortNameToUnicode.put("grapes", new
		 * String(new int[] {0x1F347}, 0, 1));
		 * _shortNameToUnicode.put("first_quarter_moon_with_face", new
		 * String(new int[] {0x1F31B}, 0, 1));
		 * _shortNameToUnicode.put("telephone_receiver", new String(new int[]
		 * {0x1F4DE}, 0, 1)); _shortNameToUnicode.put("eight_spoked_asterisk",
		 * new String(new int[] {0x2733}, 0, 1)); _shortNameToUnicode.put("sos",
		 * new String(new int[] {0x1F198}, 0, 1));
		 * _shortNameToUnicode.put("koala", new String(new int[] {0x1F428}, 0,
		 * 1)); _shortNameToUnicode.put("blue_car", new String(new int[]
		 * {0x1F699}, 0, 1)); _shortNameToUnicode.put("arrow_down", new
		 * String(new int[] {0x2B07}, 0, 1)); _shortNameToUnicode.put("ramen",
		 * new String(new int[] {0x1F35C}, 0, 1));
		 * _shortNameToUnicode.put("house", new String(new int[] {0x1F3E0}, 0,
		 * 1)); _shortNameToUnicode.put("pig_nose", new String(new int[]
		 * {0x1F43D}, 0, 1)); _shortNameToUnicode.put("anchor", new String(new
		 * int[] {0x2693}, 0, 1)); _shortNameToUnicode.put("art", new String(new
		 * int[] {0x1F3A8}, 0, 1)); _shortNameToUnicode.put("chicken", new
		 * String(new int[] {0x1F414}, 0, 1));
		 * _shortNameToUnicode.put("wavy_dash", new String(new int[] {0x3030},
		 * 0, 1)); _shortNameToUnicode.put("monkey_face", new String(new int[]
		 * {0x1F435}, 0, 1)); _shortNameToUnicode.put("ok", new String(new int[]
		 * {0x1F197}, 0, 1)); _shortNameToUnicode.put("candy", new String(new
		 * int[] {0x1F36C}, 0, 1)); _shortNameToUnicode.put("tangerine", new
		 * String(new int[] {0x1F34A}, 0, 1)); _shortNameToUnicode.put("m", new
		 * String(new int[] {0x24C2}, 0, 1)); _shortNameToUnicode.put("bath",
		 * new String(new int[] {0x1F6C0}, 0, 1));
		 * _shortNameToUnicode.put("cow", new String(new int[] {0x1F42E}, 0,
		 * 1)); _shortNameToUnicode.put("mushroom", new String(new int[]
		 * {0x1F344}, 0, 1)); _shortNameToUnicode.put("mouse", new String(new
		 * int[] {0x1F42D}, 0, 1)); _shortNameToUnicode.put("large_blue_circle",
		 * new String(new int[] {0x1F535}, 0, 1));
		 * _shortNameToUnicode.put("japanese_goblin", new String(new int[]
		 * {0x1F47A}, 0, 1)); _shortNameToUnicode.put("moyai", new String(new
		 * int[] {0x1F5FF}, 0, 1)); _shortNameToUnicode.put("egg", new
		 * String(new int[] {0x1F373}, 0, 1)); _shortNameToUnicode.put("tennis",
		 * new String(new int[] {0x1F3BE}, 0, 1));
		 * _shortNameToUnicode.put("fireworks", new String(new int[] {0x1F386},
		 * 0, 1)); _shortNameToUnicode.put("racehorse", new String(new int[]
		 * {0x1F40E}, 0, 1)); _shortNameToUnicode.put("bread", new String(new
		 * int[] {0x1F35E}, 0, 1)); _shortNameToUnicode.put("bird", new
		 * String(new int[] {0x1F426}, 0, 1));
		 * _shortNameToUnicode.put("droplet", new String(new int[] {0x1F4A7}, 0,
		 * 1)); _shortNameToUnicode.put("fried_shrimp", new String(new int[]
		 * {0x1F364}, 0, 1)); _shortNameToUnicode.put("key", new String(new
		 * int[] {0x1F511}, 0, 1)); _shortNameToUnicode.put("back", new
		 * String(new int[] {0x1F519}, 0, 1)); _shortNameToUnicode.put("bike",
		 * new String(new int[] {0x1F6B2}, 0, 1));
		 * _shortNameToUnicode.put("pencil2", new String(new int[] {0x270F}, 0,
		 * 1)); _shortNameToUnicode.put("shaved_ice", new String(new int[]
		 * {0x1F367}, 0, 1)); _shortNameToUnicode.put("arrow_right_hook", new
		 * String(new int[] {0x21AA}, 0, 1)); _shortNameToUnicode.put("bulb",
		 * new String(new int[] {0x1F4A1}, 0, 1));
		 * _shortNameToUnicode.put("tophat", new String(new int[] {0x1F3A9}, 0,
		 * 1)); _shortNameToUnicode.put("wolf", new String(new int[] {0x1F43A},
		 * 0, 1)); _shortNameToUnicode.put("night_with_stars", new String(new
		 * int[] {0x1F303}, 0, 1)); _shortNameToUnicode.put("grey_exclamation",
		 * new String(new int[] {0x2755}, 0, 1));
		 * _shortNameToUnicode.put("alarm_clock", new String(new int[] {0x23F0},
		 * 0, 1)); _shortNameToUnicode.put("cop", new String(new int[]
		 * {0x1F46E}, 0, 1)); _shortNameToUnicode.put("arrow_lower_left", new
		 * String(new int[] {0x2199}, 0, 1));
		 * _shortNameToUnicode.put("person_with_blond_hair", new String(new
		 * int[] {0x1F471}, 0, 1)); _shortNameToUnicode.put("jeans", new
		 * String(new int[] {0x1F456}, 0, 1)); _shortNameToUnicode.put("sheep",
		 * new String(new int[] {0x1F411}, 0, 1));
		 * _shortNameToUnicode.put("golf", new String(new int[] {0x26F3}, 0,
		 * 1)); _shortNameToUnicode.put("arrow_upper_right", new String(new
		 * int[] {0x2197}, 0, 1)); _shortNameToUnicode.put("cd", new String(new
		 * int[] {0x1F1E8,0x1F1E9}, 0, 2)); _shortNameToUnicode.put("watch", new
		 * String(new int[] {0x231A}, 0, 1));
		 * _shortNameToUnicode.put("performing_arts", new String(new int[]
		 * {0x1F3AD}, 0, 1)); _shortNameToUnicode.put("bug", new String(new
		 * int[] {0x1F41B}, 0, 1)); _shortNameToUnicode.put("sushi", new
		 * String(new int[] {0x1F363}, 0, 1));
		 * _shortNameToUnicode.put("baby_chick", new String(new int[] {0x1F424},
		 * 0, 1)); _shortNameToUnicode.put("small_blue_diamond", new String(new
		 * int[] {0x1F539}, 0, 1)); _shortNameToUnicode.put("electric_plug", new
		 * String(new int[] {0x1F50C}, 0, 1)); _shortNameToUnicode.put("lock",
		 * new String(new int[] {0x1F512}, 0, 1));
		 * _shortNameToUnicode.put("black_square_button", new String(new int[]
		 * {0x1F532}, 0, 1)); _shortNameToUnicode.put("fish_cake", new
		 * String(new int[] {0x1F365}, 0, 1));
		 * _shortNameToUnicode.put("seedling", new String(new int[] {0x1F331},
		 * 0, 1)); _shortNameToUnicode.put("corn", new String(new int[]
		 * {0x1F33D}, 0, 1));
		 * _shortNameToUnicode.put("leftwards_arrow_with_hook", new String(new
		 * int[] {0x21A9}, 0, 1)); _shortNameToUnicode.put("arrow_heading_down",
		 * new String(new int[] {0x2935}, 0, 1)); _shortNameToUnicode.put("ant",
		 * new String(new int[] {0x1F41C}, 0, 1));
		 * _shortNameToUnicode.put("checkered_flag", new String(new int[]
		 * {0x1F3C1}, 0, 1)); _shortNameToUnicode.put("tea", new String(new
		 * int[] {0x1F375}, 0, 1)); _shortNameToUnicode.put("stew", new
		 * String(new int[] {0x1F372}, 0, 1));
		 * _shortNameToUnicode.put("arrow_up", new String(new int[] {0x2B06}, 0,
		 * 1)); _shortNameToUnicode.put("underage", new String(new int[]
		 * {0x1F51E}, 0, 1)); _shortNameToUnicode.put("snail", new String(new
		 * int[] {0x1F40C}, 0, 1)); _shortNameToUnicode.put("meat_on_bone", new
		 * String(new int[] {0x1F356}, 0, 1)); _shortNameToUnicode.put("camel",
		 * new String(new int[] {0x1F42B}, 0, 1));
		 * _shortNameToUnicode.put("necktie", new String(new int[] {0x1F454}, 0,
		 * 1)); _shortNameToUnicode.put("toilet", new String(new int[]
		 * {0x1F6BD}, 0, 1)); _shortNameToUnicode.put("a", new String(new int[]
		 * {0x1F170}, 0, 1)); _shortNameToUnicode.put("arrow_lower_right", new
		 * String(new int[] {0x2198}, 0, 1)); _shortNameToUnicode.put("hamster",
		 * new String(new int[] {0x1F439}, 0, 1));
		 * _shortNameToUnicode.put("fuelpump", new String(new int[] {0x26FD}, 0,
		 * 1)); _shortNameToUnicode.put("hammer", new String(new int[]
		 * {0x1F528}, 0, 1)); _shortNameToUnicode.put("bust_in_silhouette", new
		 * String(new int[] {0x1F464}, 0, 1)); _shortNameToUnicode.put("up", new
		 * String(new int[] {0x1F199}, 0, 1));
		 * _shortNameToUnicode.put("snowboarder", new String(new int[]
		 * {0x1F3C2}, 0, 1)); _shortNameToUnicode.put("curly_loop", new
		 * String(new int[] {0x27B0}, 0, 1)); _shortNameToUnicode.put("handbag",
		 * new String(new int[] {0x1F45C}, 0, 1));
		 * _shortNameToUnicode.put("dart", new String(new int[] {0x1F3AF}, 0,
		 * 1)); _shortNameToUnicode.put("computer", new String(new int[]
		 * {0x1F4BB}, 0, 1)); _shortNameToUnicode.put("poodle", new String(new
		 * int[] {0x1F429}, 0, 1)); _shortNameToUnicode.put("cancer", new
		 * String(new int[] {0x264B}, 0, 1)); _shortNameToUnicode.put("rice",
		 * new String(new int[] {0x1F35A}, 0, 1));
		 * _shortNameToUnicode.put("black_medium_small_square", new String(new
		 * int[] {0x25FE}, 0, 1)); _shortNameToUnicode.put("seat", new
		 * String(new int[] {0x1F4BA}, 0, 1)); _shortNameToUnicode.put("shell",
		 * new String(new int[] {0x1F41A}, 0, 1));
		 * _shortNameToUnicode.put("trident", new String(new int[] {0x1F531}, 0,
		 * 1)); _shortNameToUnicode.put("hotsprings", new String(new int[]
		 * {0x2668}, 0, 1)); _shortNameToUnicode.put("curry", new String(new
		 * int[] {0x1F35B}, 0, 1)); _shortNameToUnicode.put("ice_cream", new
		 * String(new int[] {0x1F368}, 0, 1));
		 * _shortNameToUnicode.put("diamond_shape_with_a_dot_inside", new
		 * String(new int[] {0x1F4A0}, 0, 1));
		 * _shortNameToUnicode.put("green_apple", new String(new int[]
		 * {0x1F34F}, 0, 1)); _shortNameToUnicode.put("statue_of_liberty", new
		 * String(new int[] {0x1F5FD}, 0, 1)); _shortNameToUnicode.put("bus",
		 * new String(new int[] {0x1F68C}, 0, 1));
		 * _shortNameToUnicode.put("bowling", new String(new int[] {0x1F3B3}, 0,
		 * 1)); _shortNameToUnicode.put("dolls", new String(new int[] {0x1F38E},
		 * 0, 1)); _shortNameToUnicode.put("baby_symbol", new String(new int[]
		 * {0x1F6BC}, 0, 1)); _shortNameToUnicode.put("construction_worker", new
		 * String(new int[] {0x1F477}, 0, 1));
		 * _shortNameToUnicode.put("custard", new String(new int[] {0x1F36E}, 0,
		 * 1)); _shortNameToUnicode.put("unlock", new String(new int[]
		 * {0x1F513}, 0, 1)); _shortNameToUnicode.put("shirt", new String(new
		 * int[] {0x1F455}, 0, 1)); _shortNameToUnicode.put("credit_card", new
		 * String(new int[] {0x1F4B3}, 0, 1)); _shortNameToUnicode.put("bento",
		 * new String(new int[] {0x1F371}, 0, 1));
		 * _shortNameToUnicode.put("beetle", new String(new int[] {0x1F41E}, 0,
		 * 1)); _shortNameToUnicode.put("mans_shoe", new String(new int[]
		 * {0x1F45E}, 0, 1)); _shortNameToUnicode.put("chestnut", new String(new
		 * int[] {0x1F330}, 0, 1)); _shortNameToUnicode.put("interrobang", new
		 * String(new int[] {0x2049}, 0, 1));
		 * _shortNameToUnicode.put("small_red_triangle", new String(new int[]
		 * {0x1F53A}, 0, 1)); _shortNameToUnicode.put("heavy_dollar_sign", new
		 * String(new int[] {0x1F4B2}, 0, 1));
		 * _shortNameToUnicode.put("battery", new String(new int[] {0x1F50B}, 0,
		 * 1)); _shortNameToUnicode.put("black_nib", new String(new int[]
		 * {0x2712}, 0, 1)); _shortNameToUnicode.put("police_car", new
		 * String(new int[] {0x1F693}, 0, 1));
		 * _shortNameToUnicode.put("honey_pot", new String(new int[] {0x1F36F},
		 * 0, 1)); _shortNameToUnicode.put("small_orange_diamond", new
		 * String(new int[] {0x1F538}, 0, 1)); _shortNameToUnicode.put("b", new
		 * String(new int[] {0x1F171}, 0, 1));
		 * _shortNameToUnicode.put("arrows_clockwise", new String(new int[]
		 * {0x1F503}, 0, 1)); _shortNameToUnicode.put("roller_coaster", new
		 * String(new int[] {0x1F3A2}, 0, 1)); _shortNameToUnicode.put("door",
		 * new String(new int[] {0x1F6AA}, 0, 1));
		 * _shortNameToUnicode.put("sunrise_over_mountains", new String(new
		 * int[] {0x1F304}, 0, 1)); _shortNameToUnicode.put("8ball", new
		 * String(new int[] {0x1F3B1}, 0, 1));
		 * _shortNameToUnicode.put("eight_pointed_black_star", new String(new
		 * int[] {0x2734}, 0, 1)); _shortNameToUnicode.put("musical_keyboard",
		 * new String(new int[] {0x1F3B9}, 0, 1));
		 * _shortNameToUnicode.put("sparkler", new String(new int[] {0x1F387},
		 * 0, 1)); _shortNameToUnicode.put("small_red_triangle_down", new
		 * String(new int[] {0x1F53B}, 0, 1));
		 * _shortNameToUnicode.put("arrow_upper_left", new String(new int[]
		 * {0x2196}, 0, 1)); _shortNameToUnicode.put("left_right_arrow", new
		 * String(new int[] {0x2194}, 0, 1)); _shortNameToUnicode.put("barber",
		 * new String(new int[] {0x1F488}, 0, 1));
		 * _shortNameToUnicode.put("large_orange_diamond", new String(new int[]
		 * {0x1F536}, 0, 1)); _shortNameToUnicode.put("hospital", new String(new
		 * int[] {0x1F3E5}, 0, 1)); _shortNameToUnicode.put("city_dusk", new
		 * String(new int[] {0x1F306}, 0, 1));
		 * _shortNameToUnicode.put("scorpius", new String(new int[] {0x264F}, 0,
		 * 1)); _shortNameToUnicode.put("sailboat", new String(new int[]
		 * {0x26F5}, 0, 1)); _shortNameToUnicode.put("tomato", new String(new
		 * int[] {0x1F345}, 0, 1)); _shortNameToUnicode.put("sparkle", new
		 * String(new int[] {0x2747}, 0, 1));
		 * _shortNameToUnicode.put("closed_umbrella", new String(new int[]
		 * {0x1F302}, 0, 1)); _shortNameToUnicode.put("heavy_plus_sign", new
		 * String(new int[] {0x2795}, 0, 1)); _shortNameToUnicode.put("mega",
		 * new String(new int[] {0x1F4E3}, 0, 1));
		 * _shortNameToUnicode.put("large_blue_diamond", new String(new int[]
		 * {0x1F537}, 0, 1)); _shortNameToUnicode.put("package", new String(new
		 * int[] {0x1F4E6}, 0, 1)); _shortNameToUnicode.put("heavy_minus_sign",
		 * new String(new int[] {0x2796}, 0, 1));
		 * _shortNameToUnicode.put("city_sunset", new String(new int[]
		 * {0x1F307}, 0, 1)); _shortNameToUnicode.put("soon", new String(new
		 * int[] {0x1F51C}, 0, 1)); _shortNameToUnicode.put("congratulations",
		 * new String(new int[] {0x3297}, 0, 1));
		 * _shortNameToUnicode.put("secret", new String(new int[] {0x3299}, 0,
		 * 1)); _shortNameToUnicode.put("no_entry", new String(new int[]
		 * {0x26D4}, 0, 1)); _shortNameToUnicode.put("aries", new String(new
		 * int[] {0x2648}, 0, 1)); _shortNameToUnicode.put("purse", new
		 * String(new int[] {0x1F45B}, 0, 1));
		 * _shortNameToUnicode.put("dragon_face", new String(new int[]
		 * {0x1F432}, 0, 1)); _shortNameToUnicode.put("leo", new String(new
		 * int[] {0x264C}, 0, 1)); _shortNameToUnicode.put("ship", new
		 * String(new int[] {0x1F6A2}, 0, 1));
		 * _shortNameToUnicode.put("white_flower", new String(new int[]
		 * {0x1F4AE}, 0, 1)); _shortNameToUnicode.put("id", new String(new int[]
		 * {0x1F1EE,0x1F1E9}, 0, 2)); _shortNameToUnicode.put("wedding", new
		 * String(new int[] {0x1F492}, 0, 1)); _shortNameToUnicode.put("boot",
		 * new String(new int[] {0x1F462}, 0, 1));
		 * _shortNameToUnicode.put("radio_button", new String(new int[]
		 * {0x1F518}, 0, 1)); _shortNameToUnicode.put("notebook", new String(new
		 * int[] {0x1F4D3}, 0, 1)); _shortNameToUnicode.put("gemini", new
		 * String(new int[] {0x264A}, 0, 1)); _shortNameToUnicode.put("bell",
		 * new String(new int[] {0x1F514}, 0, 1));
		 * _shortNameToUnicode.put("boar", new String(new int[] {0x1F417}, 0,
		 * 1)); _shortNameToUnicode.put("ambulance", new String(new int[]
		 * {0x1F691}, 0, 1)); _shortNameToUnicode.put("mount_fuji", new
		 * String(new int[] {0x1F5FB}, 0, 1)); _shortNameToUnicode.put("sandal",
		 * new String(new int[] {0x1F461}, 0, 1));
		 * _shortNameToUnicode.put("round_pushpin", new String(new int[]
		 * {0x1F4CD}, 0, 1)); _shortNameToUnicode.put("keycap_ten", new
		 * String(new int[] {0x1F51F}, 0, 1)); _shortNameToUnicode.put("ledger",
		 * new String(new int[] {0x1F4D2}, 0, 1));
		 * _shortNameToUnicode.put("womans_hat", new String(new int[] {0x1F452},
		 * 0, 1)); _shortNameToUnicode.put("envelope_with_arrow", new String(new
		 * int[] {0x1F4E9}, 0, 1)); _shortNameToUnicode.put("black_joker", new
		 * String(new int[] {0x1F0CF}, 0, 1));
		 * _shortNameToUnicode.put("part_alternation_mark", new String(new int[]
		 * {0x303D}, 0, 1)); _shortNameToUnicode.put("o2", new String(new int[]
		 * {0x1F17E}, 0, 1)); _shortNameToUnicode.put("office", new String(new
		 * int[] {0x1F3E2}, 0, 1)); _shortNameToUnicode.put("volcano", new
		 * String(new int[] {0x1F30B}, 0, 1));
		 * _shortNameToUnicode.put("aquarius", new String(new int[] {0x2652}, 0,
		 * 1)); _shortNameToUnicode.put("taurus", new String(new int[] {0x2649},
		 * 0, 1)); _shortNameToUnicode.put("pushpin", new String(new int[]
		 * {0x1F4CC}, 0, 1)); _shortNameToUnicode.put("violin", new String(new
		 * int[] {0x1F3BB}, 0, 1)); _shortNameToUnicode.put("virgo", new
		 * String(new int[] {0x264D}, 0, 1)); _shortNameToUnicode.put("ski", new
		 * String(new int[] {0x1F3BF}, 0, 1)); _shortNameToUnicode.put("taxi",
		 * new String(new int[] {0x1F695}, 0, 1));
		 * _shortNameToUnicode.put("stars", new String(new int[] {0x1F320}, 0,
		 * 1)); _shortNameToUnicode.put("speedboat", new String(new int[]
		 * {0x1F6A4}, 0, 1)); _shortNameToUnicode.put("hourglass_flowing_sand",
		 * new String(new int[] {0x23F3}, 0, 1));
		 * _shortNameToUnicode.put("ferris_wheel", new String(new int[]
		 * {0x1F3A1}, 0, 1)); _shortNameToUnicode.put("tent", new String(new
		 * int[] {0x26FA}, 0, 1)); _shortNameToUnicode.put("love_hotel", new
		 * String(new int[] {0x1F3E9}, 0, 1)); _shortNameToUnicode.put("church",
		 * new String(new int[] {0x26EA}, 0, 1));
		 * _shortNameToUnicode.put("briefcase", new String(new int[] {0x1F4BC},
		 * 0, 1)); _shortNameToUnicode.put("womans_clothes", new String(new
		 * int[] {0x1F45A}, 0, 1)); _shortNameToUnicode.put("dvd", new
		 * String(new int[] {0x1F4C0}, 0, 1)); _shortNameToUnicode.put("libra",
		 * new String(new int[] {0x264E}, 0, 1));
		 * _shortNameToUnicode.put("sagittarius", new String(new int[] {0x2650},
		 * 0, 1)); _shortNameToUnicode.put("oden", new String(new int[]
		 * {0x1F362}, 0, 1)); _shortNameToUnicode.put("game_die", new String(new
		 * int[] {0x1F3B2}, 0, 1)); _shortNameToUnicode.put("grey_question", new
		 * String(new int[] {0x2754}, 0, 1));
		 * _shortNameToUnicode.put("fast_forward", new String(new int[]
		 * {0x23E9}, 0, 1)); _shortNameToUnicode.put("flashlight", new
		 * String(new int[] {0x1F526}, 0, 1));
		 * _shortNameToUnicode.put("triangular_flag_on_post", new String(new
		 * int[] {0x1F6A9}, 0, 1)); _shortNameToUnicode.put("tanabata_tree", new
		 * String(new int[] {0x1F38B}, 0, 1)); _shortNameToUnicode.put("dango",
		 * new String(new int[] {0x1F361}, 0, 1));
		 * _shortNameToUnicode.put("signal_strength", new String(new int[]
		 * {0x1F4F6}, 0, 1)); _shortNameToUnicode.put("video_camera", new
		 * String(new int[] {0x1F4F9}, 0, 1));
		 * _shortNameToUnicode.put("negative_squared_cross_mark", new String(new
		 * int[] {0x274E}, 0, 1));
		 * _shortNameToUnicode.put("black_medium_square", new String(new int[]
		 * {0x25FC}, 0, 1)); _shortNameToUnicode.put("yen", new String(new int[]
		 * {0x1F4B4}, 0, 1)); _shortNameToUnicode.put("blowfish", new String(new
		 * int[] {0x1F421}, 0, 1));
		 * _shortNameToUnicode.put("white_large_square", new String(new int[]
		 * {0x2B1C}, 0, 1)); _shortNameToUnicode.put("beginner", new String(new
		 * int[] {0x1F530}, 0, 1)); _shortNameToUnicode.put("school", new
		 * String(new int[] {0x1F3EB}, 0, 1)); _shortNameToUnicode.put("new",
		 * new String(new int[] {0x1F195}, 0, 1));
		 * _shortNameToUnicode.put("clock1", new String(new int[] {0x1F550}, 0,
		 * 1)); _shortNameToUnicode.put("womens", new String(new int[]
		 * {0x1F6BA}, 0, 1)); _shortNameToUnicode.put("running_shirt_with_sash",
		 * new String(new int[] {0x1F3BD}, 0, 1));
		 * _shortNameToUnicode.put("radio", new String(new int[] {0x1F4FB}, 0,
		 * 1)); _shortNameToUnicode.put("on", new String(new int[] {0x1F51B}, 0,
		 * 1)); _shortNameToUnicode.put("hourglass", new String(new int[]
		 * {0x231B}, 0, 1)); _shortNameToUnicode.put("pisces", new String(new
		 * int[] {0x2653}, 0, 1)); _shortNameToUnicode.put("nut_and_bolt", new
		 * String(new int[] {0x1F529}, 0, 1)); _shortNameToUnicode.put("free",
		 * new String(new int[] {0x1F193}, 0, 1));
		 * _shortNameToUnicode.put("bridge_at_night", new String(new int[]
		 * {0x1F309}, 0, 1)); _shortNameToUnicode.put("saxophone", new
		 * String(new int[] {0x1F3B7}, 0, 1));
		 * _shortNameToUnicode.put("white_square_button", new String(new int[]
		 * {0x1F533}, 0, 1)); _shortNameToUnicode.put("mobile_phone_off", new
		 * String(new int[] {0x1F4F4}, 0, 1));
		 * _shortNameToUnicode.put("closed_book", new String(new int[]
		 * {0x1F4D5}, 0, 1)); _shortNameToUnicode.put("european_castle", new
		 * String(new int[] {0x1F3F0}, 0, 1));
		 * _shortNameToUnicode.put("clock12", new String(new int[] {0x1F55B}, 0,
		 * 1)); _shortNameToUnicode.put("white_medium_square", new String(new
		 * int[] {0x25FB}, 0, 1)); _shortNameToUnicode.put("foggy", new
		 * String(new int[] {0x1F301}, 0, 1));
		 * _shortNameToUnicode.put("minidisc", new String(new int[] {0x1F4BD},
		 * 0, 1)); _shortNameToUnicode.put("fire_engine", new String(new int[]
		 * {0x1F692}, 0, 1)); _shortNameToUnicode.put("clock2", new String(new
		 * int[] {0x1F551}, 0, 1)); _shortNameToUnicode.put("rice_ball", new
		 * String(new int[] {0x1F359}, 0, 1));
		 * _shortNameToUnicode.put("wind_chime", new String(new int[] {0x1F390},
		 * 0, 1)); _shortNameToUnicode.put("capricorn", new String(new int[]
		 * {0x2651}, 0, 1)); _shortNameToUnicode.put("vs", new String(new int[]
		 * {0x1F19A}, 0, 1)); _shortNameToUnicode.put("melon", new String(new
		 * int[] {0x1F348}, 0, 1)); _shortNameToUnicode.put("trumpet", new
		 * String(new int[] {0x1F3BA}, 0, 1));
		 * _shortNameToUnicode.put("school_satchel", new String(new int[]
		 * {0x1F392}, 0, 1)); _shortNameToUnicode.put("tokyo_tower", new
		 * String(new int[] {0x1F5FC}, 0, 1));
		 * _shortNameToUnicode.put("station", new String(new int[] {0x1F689}, 0,
		 * 1)); _shortNameToUnicode.put("end", new String(new int[] {0x1F51A},
		 * 0, 1)); _shortNameToUnicode.put("bamboo", new String(new int[]
		 * {0x1F38D}, 0, 1)); _shortNameToUnicode.put("truck", new String(new
		 * int[] {0x1F69A}, 0, 1)); _shortNameToUnicode.put("clock3", new
		 * String(new int[] {0x1F552}, 0, 1));
		 * _shortNameToUnicode.put("six_pointed_star", new String(new int[]
		 * {0x1F52F}, 0, 1)); _shortNameToUnicode.put("mag_right", new
		 * String(new int[] {0x1F50E}, 0, 1)); _shortNameToUnicode.put("kimono",
		 * new String(new int[] {0x1F458}, 0, 1));
		 * _shortNameToUnicode.put("railway_car", new String(new int[]
		 * {0x1F683}, 0, 1)); _shortNameToUnicode.put("crossed_flags", new
		 * String(new int[] {0x1F38C}, 0, 1));
		 * _shortNameToUnicode.put("sweet_potato", new String(new int[]
		 * {0x1F360}, 0, 1)); _shortNameToUnicode.put("white_small_square", new
		 * String(new int[] {0x25AB}, 0, 1)); _shortNameToUnicode.put("date",
		 * new String(new int[] {0x1F4C5}, 0, 1));
		 * _shortNameToUnicode.put("newspaper", new String(new int[] {0x1F4F0},
		 * 0, 1)); _shortNameToUnicode.put("no_smoking", new String(new int[]
		 * {0x1F6AD}, 0, 1)); _shortNameToUnicode.put("scroll", new String(new
		 * int[] {0x1F4DC}, 0, 1)); _shortNameToUnicode.put("flags", new
		 * String(new int[] {0x1F38F}, 0, 1)); _shortNameToUnicode.put("mag",
		 * new String(new int[] {0x1F50D}, 0, 1));
		 * _shortNameToUnicode.put("wheelchair", new String(new int[] {0x267F},
		 * 0, 1)); _shortNameToUnicode.put("sake", new String(new int[]
		 * {0x1F376}, 0, 1)); _shortNameToUnicode.put("arrow_up_down", new
		 * String(new int[] {0x2195}, 0, 1));
		 * _shortNameToUnicode.put("black_large_square", new String(new int[]
		 * {0x2B1B}, 0, 1)); _shortNameToUnicode.put("wrench", new String(new
		 * int[] {0x1F527}, 0, 1)); _shortNameToUnicode.put("construction", new
		 * String(new int[] {0x1F6A7}, 0, 1));
		 * _shortNameToUnicode.put("calendar", new String(new int[] {0x1F4C6},
		 * 0, 1)); _shortNameToUnicode.put("hotel", new String(new int[]
		 * {0x1F3E8}, 0, 1)); _shortNameToUnicode.put("satellite", new
		 * String(new int[] {0x1F4E1}, 0, 1)); _shortNameToUnicode.put("rewind",
		 * new String(new int[] {0x23EA}, 0, 1));
		 * _shortNameToUnicode.put("clock4", new String(new int[] {0x1F553}, 0,
		 * 1)); _shortNameToUnicode.put("circus_tent", new String(new int[]
		 * {0x1F3AA}, 0, 1)); _shortNameToUnicode.put("link", new String(new
		 * int[] {0x1F517}, 0, 1)); _shortNameToUnicode.put("bullettrain_side",
		 * new String(new int[] {0x1F684}, 0, 1));
		 * _shortNameToUnicode.put("mens", new String(new int[] {0x1F6B9}, 0,
		 * 1)); _shortNameToUnicode.put("carousel_horse", new String(new int[]
		 * {0x1F3A0}, 0, 1)); _shortNameToUnicode.put("ideograph_advantage", new
		 * String(new int[] {0x1F250}, 0, 1)); _shortNameToUnicode.put("atm",
		 * new String(new int[] {0x1F3E7}, 0, 1));
		 * _shortNameToUnicode.put("vhs", new String(new int[] {0x1F4FC}, 0,
		 * 1)); _shortNameToUnicode.put("arrow_double_down", new String(new
		 * int[] {0x23EC}, 0, 1)); _shortNameToUnicode.put("clock9", new
		 * String(new int[] {0x1F558}, 0, 1));
		 * _shortNameToUnicode.put("blue_book", new String(new int[] {0x1F4D8},
		 * 0, 1)); _shortNameToUnicode.put("arrow_heading_up", new String(new
		 * int[] {0x2934}, 0, 1)); _shortNameToUnicode.put("metro", new
		 * String(new int[] {0x1F687}, 0, 1)); _shortNameToUnicode.put("clock5",
		 * new String(new int[] {0x1F554}, 0, 1)); _shortNameToUnicode.put("wc",
		 * new String(new int[] {0x1F6BE}, 0, 1));
		 * _shortNameToUnicode.put("chart_with_upwards_trend", new String(new
		 * int[] {0x1F4C8}, 0, 1)); _shortNameToUnicode.put("slot_machine", new
		 * String(new int[] {0x1F3B0}, 0, 1));
		 * _shortNameToUnicode.put("rice_cracker", new String(new int[]
		 * {0x1F358}, 0, 1)); _shortNameToUnicode.put("page_facing_up", new
		 * String(new int[] {0x1F4C4}, 0, 1));
		 * _shortNameToUnicode.put("arrow_up_small", new String(new int[]
		 * {0x1F53C}, 0, 1)); _shortNameToUnicode.put("green_book", new
		 * String(new int[] {0x1F4D7}, 0, 1));
		 * _shortNameToUnicode.put("white_medium_small_square", new String(new
		 * int[] {0x25FD}, 0, 1)); _shortNameToUnicode.put("traffic_light", new
		 * String(new int[] {0x1F6A5}, 0, 1)); _shortNameToUnicode.put("ng", new
		 * String(new int[] {0x1F1F3,0x1F1EC}, 0, 2));
		 * _shortNameToUnicode.put("clock10", new String(new int[] {0x1F559}, 0,
		 * 1)); _shortNameToUnicode.put("convenience_store", new String(new
		 * int[] {0x1F3EA}, 0, 1)); _shortNameToUnicode.put("paperclip", new
		 * String(new int[] {0x1F4CE}, 0, 1));
		 * _shortNameToUnicode.put("name_badge", new String(new int[] {0x1F4DB},
		 * 0, 1)); _shortNameToUnicode.put("clock8", new String(new int[]
		 * {0x1F557}, 0, 1)); _shortNameToUnicode.put("arrow_down_small", new
		 * String(new int[] {0x1F53D}, 0, 1));
		 * _shortNameToUnicode.put("clipboard", new String(new int[] {0x1F4CB},
		 * 0, 1)); _shortNameToUnicode.put("page_with_curl", new String(new
		 * int[] {0x1F4C3}, 0, 1)); _shortNameToUnicode.put("bookmark_tabs", new
		 * String(new int[] {0x1F4D1}, 0, 1)); _shortNameToUnicode.put("bank",
		 * new String(new int[] {0x1F3E6}, 0, 1));
		 * _shortNameToUnicode.put("clock11", new String(new int[] {0x1F55A}, 0,
		 * 1)); _shortNameToUnicode.put("e-mail", new String(new int[]
		 * {0x1F4E7}, 0, 1));
		 * _shortNameToUnicode.put("chart_with_downwards_trend", new String(new
		 * int[] {0x1F4C9}, 0, 1)); _shortNameToUnicode.put("bullettrain_front",
		 * new String(new int[] {0x1F685}, 0, 1));
		 * _shortNameToUnicode.put("bar_chart", new String(new int[] {0x1F4CA},
		 * 0, 1)); _shortNameToUnicode.put("notebook_with_decorative_cover", new
		 * String(new int[] {0x1F4D4}, 0, 1)); _shortNameToUnicode.put("ticket",
		 * new String(new int[] {0x1F3AB}, 0, 1));
		 * _shortNameToUnicode.put("information_source", new String(new int[]
		 * {0x2139}, 0, 1)); _shortNameToUnicode.put("pouch", new String(new
		 * int[] {0x1F45D}, 0, 1)); _shortNameToUnicode.put("chart", new
		 * String(new int[] {0x1F4B9}, 0, 1));
		 * _shortNameToUnicode.put("japanese_castle", new String(new int[]
		 * {0x1F3EF}, 0, 1)); _shortNameToUnicode.put("cinema", new String(new
		 * int[] {0x1F3A6}, 0, 1)); _shortNameToUnicode.put("clock7", new
		 * String(new int[] {0x1F556}, 0, 1));
		 * _shortNameToUnicode.put("orange_book", new String(new int[]
		 * {0x1F4D9}, 0, 1)); _shortNameToUnicode.put("restroom", new String(new
		 * int[] {0x1F6BB}, 0, 1)); _shortNameToUnicode.put("fountain", new
		 * String(new int[] {0x26F2}, 0, 1)); _shortNameToUnicode.put("clock6",
		 * new String(new int[] {0x1F555}, 0, 1));
		 * _shortNameToUnicode.put("vibration_mode", new String(new int[]
		 * {0x1F4F3}, 0, 1)); _shortNameToUnicode.put("ab", new String(new int[]
		 * {0x1F18E}, 0, 1)); _shortNameToUnicode.put("postbox", new String(new
		 * int[] {0x1F4EE}, 0, 1)); _shortNameToUnicode.put("rice_scene", new
		 * String(new int[] {0x1F391}, 0, 1));
		 * _shortNameToUnicode.put("floppy_disk", new String(new int[]
		 * {0x1F4BE}, 0, 1)); _shortNameToUnicode.put("parking", new String(new
		 * int[] {0x1F17F}, 0, 1)); _shortNameToUnicode.put("department_store",
		 * new String(new int[] {0x1F3EC}, 0, 1));
		 * _shortNameToUnicode.put("pager", new String(new int[] {0x1F4DF}, 0,
		 * 1)); _shortNameToUnicode.put("currency_exchange", new String(new
		 * int[] {0x1F4B1}, 0, 1)); _shortNameToUnicode.put("bookmark", new
		 * String(new int[] {0x1F516}, 0, 1));
		 * _shortNameToUnicode.put("triangular_ruler", new String(new int[]
		 * {0x1F4D0}, 0, 1)); _shortNameToUnicode.put("straight_ruler", new
		 * String(new int[] {0x1F4CF}, 0, 1)); _shortNameToUnicode.put("japan",
		 * new String(new int[] {0x1F5FE}, 0, 1));
		 * _shortNameToUnicode.put("flower_playing_cards", new String(new int[]
		 * {0x1F3B4}, 0, 1)); _shortNameToUnicode.put("u5272", new String(new
		 * int[] {0x1F239}, 0, 1)); _shortNameToUnicode.put("fax", new
		 * String(new int[] {0x1F4E0}, 0, 1));
		 * _shortNameToUnicode.put("izakaya_lantern", new String(new int[]
		 * {0x1F3EE}, 0, 1)); _shortNameToUnicode.put("incoming_envelope", new
		 * String(new int[] {0x1F4E8}, 0, 1));
		 * _shortNameToUnicode.put("mailbox", new String(new int[] {0x1F4EB}, 0,
		 * 1)); _shortNameToUnicode.put("lock_with_ink_pen", new String(new
		 * int[] {0x1F50F}, 0, 1)); _shortNameToUnicode.put("inbox_tray", new
		 * String(new int[] {0x1F4E5}, 0, 1));
		 * _shortNameToUnicode.put("post_office", new String(new int[]
		 * {0x1F3E3}, 0, 1)); _shortNameToUnicode.put("card_index", new
		 * String(new int[] {0x1F4C7}, 0, 1)); _shortNameToUnicode.put("cl", new
		 * String(new int[] {0x1F1E8,0x1F1F1}, 0, 2));
		 * _shortNameToUnicode.put("open_file_folder", new String(new int[]
		 * {0x1F4C2}, 0, 1)); _shortNameToUnicode.put("mahjong", new String(new
		 * int[] {0x1F004}, 0, 1)); _shortNameToUnicode.put("ophiuchus", new
		 * String(new int[] {0x26CE}, 0, 1)); _shortNameToUnicode.put("busstop",
		 * new String(new int[] {0x1F68F}, 0, 1));
		 * _shortNameToUnicode.put("abc", new String(new int[] {0x1F524}, 0,
		 * 1)); _shortNameToUnicode.put("u7a7a", new String(new int[] {0x1F233},
		 * 0, 1)); _shortNameToUnicode.put("capital_abcd", new String(new int[]
		 * {0x1F520}, 0, 1)); _shortNameToUnicode.put("factory", new String(new
		 * int[] {0x1F3ED}, 0, 1)); _shortNameToUnicode.put("u7981", new
		 * String(new int[] {0x1F232}, 0, 1)); _shortNameToUnicode.put("u6e80",
		 * new String(new int[] {0x1F235}, 0, 1));
		 * _shortNameToUnicode.put("heavy_division_sign", new String(new int[]
		 * {0x2797}, 0, 1)); _shortNameToUnicode.put("file_folder", new
		 * String(new int[] {0x1F4C1}, 0, 1));
		 * _shortNameToUnicode.put("symbols", new String(new int[] {0x1F523}, 0,
		 * 1)); _shortNameToUnicode.put("arrow_double_up", new String(new int[]
		 * {0x23EB}, 0, 1)); _shortNameToUnicode.put("u5408", new String(new
		 * int[] {0x1F234}, 0, 1)); _shortNameToUnicode.put("u6307", new
		 * String(new int[] {0x1F22F}, 0, 1)); _shortNameToUnicode.put("abcd",
		 * new String(new int[] {0x1F521}, 0, 1));
		 * _shortNameToUnicode.put("mailbox_closed", new String(new int[]
		 * {0x1F4EA}, 0, 1)); _shortNameToUnicode.put("outbox_tray", new
		 * String(new int[] {0x1F4E4}, 0, 1)); _shortNameToUnicode.put("sa", new
		 * String(new int[] {0x1F1F8,0x1F1E6}, 0, 2));
		 * _shortNameToUnicode.put("u55b6", new String(new int[] {0x1F23A}, 0,
		 * 1)); _shortNameToUnicode.put("u6709", new String(new int[] {0x1F236},
		 * 0, 1)); _shortNameToUnicode.put("accept", new String(new int[]
		 * {0x1F251}, 0, 1)); _shortNameToUnicode.put("u7121", new String(new
		 * int[] {0x1F21A}, 0, 1)); _shortNameToUnicode.put("koko", new
		 * String(new int[] {0x1F201}, 0, 1)); _shortNameToUnicode.put("u7533",
		 * new String(new int[] {0x1F238}, 0, 1));
		 * _shortNameToUnicode.put("u6708", new String(new int[] {0x1F237}, 0,
		 * 1)); _shortNameToUnicode.put("hash", new String(new int[]
		 * {0x0023,0x20E3}, 0, 2)); _shortNameToUnicode.put("zero", new
		 * String(new int[] {0x0030,0x20E3}, 0, 2));
		 * _shortNameToUnicode.put("one", new String(new int[] {0x0031,0x20E3},
		 * 0, 2)); _shortNameToUnicode.put("two", new String(new int[]
		 * {0x0032,0x20E3}, 0, 2)); _shortNameToUnicode.put("three", new
		 * String(new int[] {0x0033,0x20E3}, 0, 2));
		 * _shortNameToUnicode.put("four", new String(new int[] {0x0034,0x20E3},
		 * 0, 2)); _shortNameToUnicode.put("five", new String(new int[]
		 * {0x0035,0x20E3}, 0, 2)); _shortNameToUnicode.put("speaker", new
		 * String(new int[] {0x1F508}, 0, 1)); _shortNameToUnicode.put("six",
		 * new String(new int[] {0x0036,0x20E3}, 0, 2));
		 * _shortNameToUnicode.put("train", new String(new int[] {0x1F68B}, 0,
		 * 1)); _shortNameToUnicode.put("loop", new String(new int[] {0x27BF},
		 * 0, 1)); _shortNameToUnicode.put("seven", new String(new int[]
		 * {0x0037,0x20E3}, 0, 2)); _shortNameToUnicode.put("af", new String(new
		 * int[] {0x1F1E6,0x1F1EB}, 0, 2)); _shortNameToUnicode.put("al", new
		 * String(new int[] {0x1F1E6,0x1F1F1}, 0, 2));
		 * _shortNameToUnicode.put("eight", new String(new int[]
		 * {0x0038,0x20E3}, 0, 2)); _shortNameToUnicode.put("dz", new String(new
		 * int[] {0x1F1E9,0x1F1FF}, 0, 2)); _shortNameToUnicode.put("ad", new
		 * String(new int[] {0x1F1E6,0x1F1E9}, 0, 2));
		 * _shortNameToUnicode.put("nine", new String(new int[] {0x0039,0x20E3},
		 * 0, 2)); _shortNameToUnicode.put("ao", new String(new int[]
		 * {0x1F1E6,0x1F1F4}, 0, 2)); _shortNameToUnicode.put("ag", new
		 * String(new int[] {0x1F1E6,0x1F1EC}, 0, 2));
		 * _shortNameToUnicode.put("ar", new String(new int[] {0x1F1E6,0x1F1F7},
		 * 0, 2)); _shortNameToUnicode.put("am", new String(new int[]
		 * {0x1F1E6,0x1F1F2}, 0, 2)); _shortNameToUnicode.put("au", new
		 * String(new int[] {0x1F1E6,0x1F1FA}, 0, 2));
		 * _shortNameToUnicode.put("at", new String(new int[] {0x1F1E6,0x1F1F9},
		 * 0, 2)); _shortNameToUnicode.put("az", new String(new int[]
		 * {0x1F1E6,0x1F1FF}, 0, 2)); _shortNameToUnicode.put("bs", new
		 * String(new int[] {0x1F1E7,0x1F1F8}, 0, 2));
		 * _shortNameToUnicode.put("bh", new String(new int[] {0x1F1E7,0x1F1ED},
		 * 0, 2)); _shortNameToUnicode.put("bd", new String(new int[]
		 * {0x1F1E7,0x1F1E9}, 0, 2)); _shortNameToUnicode.put("bb", new
		 * String(new int[] {0x1F1E7,0x1F1E7}, 0, 2));
		 * _shortNameToUnicode.put("by", new String(new int[] {0x1F1E7,0x1F1FE},
		 * 0, 2)); _shortNameToUnicode.put("be", new String(new int[]
		 * {0x1F1E7,0x1F1EA}, 0, 2)); _shortNameToUnicode.put("bz", new
		 * String(new int[] {0x1F1E7,0x1F1FF}, 0, 2));
		 * _shortNameToUnicode.put("bj", new String(new int[] {0x1F1E7,0x1F1EF},
		 * 0, 2)); _shortNameToUnicode.put("bt", new String(new int[]
		 * {0x1F1E7,0x1F1F9}, 0, 2)); _shortNameToUnicode.put("bo", new
		 * String(new int[] {0x1F1E7,0x1F1F4}, 0, 2));
		 * _shortNameToUnicode.put("ba", new String(new int[] {0x1F1E7,0x1F1E6},
		 * 0, 2)); _shortNameToUnicode.put("bw", new String(new int[]
		 * {0x1F1E7,0x1F1FC}, 0, 2)); _shortNameToUnicode.put("br", new
		 * String(new int[] {0x1F1E7,0x1F1F7}, 0, 2));
		 * _shortNameToUnicode.put("bn", new String(new int[] {0x1F1E7,0x1F1F3},
		 * 0, 2)); _shortNameToUnicode.put("bg", new String(new int[]
		 * {0x1F1E7,0x1F1EC}, 0, 2)); _shortNameToUnicode.put("bf", new
		 * String(new int[] {0x1F1E7,0x1F1EB}, 0, 2));
		 * _shortNameToUnicode.put("bi", new String(new int[] {0x1F1E7,0x1F1EE},
		 * 0, 2)); _shortNameToUnicode.put("kh", new String(new int[]
		 * {0x1F1F0,0x1F1ED}, 0, 2)); _shortNameToUnicode.put("cm", new
		 * String(new int[] {0x1F1E8,0x1F1F2}, 0, 2));
		 * _shortNameToUnicode.put("ca", new String(new int[] {0x1F1E8,0x1F1E6},
		 * 0, 2)); _shortNameToUnicode.put("cv", new String(new int[]
		 * {0x1F1E8,0x1F1FB}, 0, 2)); _shortNameToUnicode.put("cf", new
		 * String(new int[] {0x1F1E8,0x1F1EB}, 0, 2));
		 * _shortNameToUnicode.put("td", new String(new int[] {0x1F1F9,0x1F1E9},
		 * 0, 2)); _shortNameToUnicode.put("co", new String(new int[]
		 * {0x1F1E8,0x1F1F4}, 0, 2)); _shortNameToUnicode.put("km", new
		 * String(new int[] {0x1F1F0,0x1F1F2}, 0, 2));
		 * _shortNameToUnicode.put("cr", new String(new int[] {0x1F1E8,0x1F1F7},
		 * 0, 2)); _shortNameToUnicode.put("ci", new String(new int[]
		 * {0x1F1E8,0x1F1EE}, 0, 2)); _shortNameToUnicode.put("hr", new
		 * String(new int[] {0x1F1ED,0x1F1F7}, 0, 2));
		 * _shortNameToUnicode.put("cu", new String(new int[] {0x1F1E8,0x1F1FA},
		 * 0, 2)); _shortNameToUnicode.put("cy", new String(new int[]
		 * {0x1F1E8,0x1F1FE}, 0, 2)); _shortNameToUnicode.put("cz", new
		 * String(new int[] {0x1F1E8,0x1F1FF}, 0, 2));
		 * _shortNameToUnicode.put("dk", new String(new int[] {0x1F1E9,0x1F1F0},
		 * 0, 2)); _shortNameToUnicode.put("dj", new String(new int[]
		 * {0x1F1E9,0x1F1EF}, 0, 2)); _shortNameToUnicode.put("dm", new
		 * String(new int[] {0x1F1E9,0x1F1F2}, 0, 2));
		 * _shortNameToUnicode.put("do", new String(new int[] {0x1F1E9,0x1F1F4},
		 * 0, 2)); _shortNameToUnicode.put("tl", new String(new int[]
		 * {0x1F1F9,0x1F1F1}, 0, 2)); _shortNameToUnicode.put("ec", new
		 * String(new int[] {0x1F1EA,0x1F1E8}, 0, 2));
		 * _shortNameToUnicode.put("eg", new String(new int[] {0x1F1EA,0x1F1EC},
		 * 0, 2)); _shortNameToUnicode.put("sv", new String(new int[]
		 * {0x1F1F8,0x1F1FB}, 0, 2)); _shortNameToUnicode.put("gq", new
		 * String(new int[] {0x1F1EC,0x1F1F6}, 0, 2));
		 * _shortNameToUnicode.put("er", new String(new int[] {0x1F1EA,0x1F1F7},
		 * 0, 2)); _shortNameToUnicode.put("ee", new String(new int[]
		 * {0x1F1EA,0x1F1EA}, 0, 2)); _shortNameToUnicode.put("et", new
		 * String(new int[] {0x1F1EA,0x1F1F9}, 0, 2));
		 * _shortNameToUnicode.put("fj", new String(new int[] {0x1F1EB,0x1F1EF},
		 * 0, 2)); _shortNameToUnicode.put("fi", new String(new int[]
		 * {0x1F1EB,0x1F1EE}, 0, 2)); _shortNameToUnicode.put("ga", new
		 * String(new int[] {0x1F1EC,0x1F1E6}, 0, 2));
		 * _shortNameToUnicode.put("gm", new String(new int[] {0x1F1EC,0x1F1F2},
		 * 0, 2)); _shortNameToUnicode.put("ge", new String(new int[]
		 * {0x1F1EC,0x1F1EA}, 0, 2)); _shortNameToUnicode.put("gh", new
		 * String(new int[] {0x1F1EC,0x1F1ED}, 0, 2));
		 * _shortNameToUnicode.put("gr", new String(new int[] {0x1F1EC,0x1F1F7},
		 * 0, 2)); _shortNameToUnicode.put("gd", new String(new int[]
		 * {0x1F1EC,0x1F1E9}, 0, 2)); _shortNameToUnicode.put("gt", new
		 * String(new int[] {0x1F1EC,0x1F1F9}, 0, 2));
		 * _shortNameToUnicode.put("gn", new String(new int[] {0x1F1EC,0x1F1F3},
		 * 0, 2)); _shortNameToUnicode.put("gw", new String(new int[]
		 * {0x1F1EC,0x1F1FC}, 0, 2)); _shortNameToUnicode.put("gy", new
		 * String(new int[] {0x1F1EC,0x1F1FE}, 0, 2));
		 * _shortNameToUnicode.put("ht", new String(new int[] {0x1F1ED,0x1F1F9},
		 * 0, 2)); _shortNameToUnicode.put("hn", new String(new int[]
		 * {0x1F1ED,0x1F1F3}, 0, 2)); _shortNameToUnicode.put("hu", new
		 * String(new int[] {0x1F1ED,0x1F1FA}, 0, 2));
		 * _shortNameToUnicode.put("is", new String(new int[] {0x1F1EE,0x1F1F8},
		 * 0, 2)); _shortNameToUnicode.put("in", new String(new int[]
		 * {0x1F1EE,0x1F1F3}, 0, 2)); _shortNameToUnicode.put("ir", new
		 * String(new int[] {0x1F1EE,0x1F1F7}, 0, 2));
		 * _shortNameToUnicode.put("iq", new String(new int[] {0x1F1EE,0x1F1F6},
		 * 0, 2)); _shortNameToUnicode.put("ie", new String(new int[]
		 * {0x1F1EE,0x1F1EA}, 0, 2)); _shortNameToUnicode.put("il", new
		 * String(new int[] {0x1F1EE,0x1F1F1}, 0, 2));
		 * _shortNameToUnicode.put("jm", new String(new int[] {0x1F1EF,0x1F1F2},
		 * 0, 2)); _shortNameToUnicode.put("jo", new String(new int[]
		 * {0x1F1EF,0x1F1F4}, 0, 2)); _shortNameToUnicode.put("kz", new
		 * String(new int[] {0x1F1F0,0x1F1FF}, 0, 2));
		 * _shortNameToUnicode.put("ke", new String(new int[] {0x1F1F0,0x1F1EA},
		 * 0, 2)); _shortNameToUnicode.put("ki", new String(new int[]
		 * {0x1F1F0,0x1F1EE}, 0, 2)); _shortNameToUnicode.put("xk", new
		 * String(new int[] {0x1F1FD,0x1F1F0}, 0, 2));
		 * _shortNameToUnicode.put("kw", new String(new int[] {0x1F1F0,0x1F1FC},
		 * 0, 2)); _shortNameToUnicode.put("cn", new String(new int[]
		 * {0x1F1E8,0x1F1F3}, 0, 2)); _shortNameToUnicode.put("kg", new
		 * String(new int[] {0x1F1F0,0x1F1EC}, 0, 2));
		 * _shortNameToUnicode.put("la", new String(new int[] {0x1F1F1,0x1F1E6},
		 * 0, 2)); _shortNameToUnicode.put("lv", new String(new int[]
		 * {0x1F1F1,0x1F1FB}, 0, 2)); _shortNameToUnicode.put("de", new
		 * String(new int[] {0x1F1E9,0x1F1EA}, 0, 2));
		 * _shortNameToUnicode.put("lb", new String(new int[] {0x1F1F1,0x1F1E7},
		 * 0, 2)); _shortNameToUnicode.put("ls", new String(new int[]
		 * {0x1F1F1,0x1F1F8}, 0, 2)); _shortNameToUnicode.put("lr", new
		 * String(new int[] {0x1F1F1,0x1F1F7}, 0, 2));
		 * _shortNameToUnicode.put("es", new String(new int[] {0x1F1EA,0x1F1F8},
		 * 0, 2)); _shortNameToUnicode.put("ly", new String(new int[]
		 * {0x1F1F1,0x1F1FE}, 0, 2)); _shortNameToUnicode.put("li", new
		 * String(new int[] {0x1F1F1,0x1F1EE}, 0, 2));
		 * _shortNameToUnicode.put("lt", new String(new int[] {0x1F1F1,0x1F1F9},
		 * 0, 2)); _shortNameToUnicode.put("fr", new String(new int[]
		 * {0x1F1EB,0x1F1F7}, 0, 2)); _shortNameToUnicode.put("lu", new
		 * String(new int[] {0x1F1F1,0x1F1FA}, 0, 2));
		 * _shortNameToUnicode.put("mk", new String(new int[] {0x1F1F2,0x1F1F0},
		 * 0, 2)); _shortNameToUnicode.put("mg", new String(new int[]
		 * {0x1F1F2,0x1F1EC}, 0, 2)); _shortNameToUnicode.put("gb", new
		 * String(new int[] {0x1F1EC,0x1F1E7}, 0, 2));
		 * _shortNameToUnicode.put("mw", new String(new int[] {0x1F1F2,0x1F1FC},
		 * 0, 2)); _shortNameToUnicode.put("my", new String(new int[]
		 * {0x1F1F2,0x1F1FE}, 0, 2)); _shortNameToUnicode.put("mv", new
		 * String(new int[] {0x1F1F2,0x1F1FB}, 0, 2));
		 * _shortNameToUnicode.put("it", new String(new int[] {0x1F1EE,0x1F1F9},
		 * 0, 2)); _shortNameToUnicode.put("ml", new String(new int[]
		 * {0x1F1F2,0x1F1F1}, 0, 2)); _shortNameToUnicode.put("mt", new
		 * String(new int[] {0x1F1F2,0x1F1F9}, 0, 2));
		 * _shortNameToUnicode.put("mh", new String(new int[] {0x1F1F2,0x1F1ED},
		 * 0, 2)); _shortNameToUnicode.put("jp", new String(new int[]
		 * {0x1F1EF,0x1F1F5}, 0, 2)); _shortNameToUnicode.put("mr", new
		 * String(new int[] {0x1F1F2,0x1F1F7}, 0, 2));
		 * _shortNameToUnicode.put("mu", new String(new int[] {0x1F1F2,0x1F1FA},
		 * 0, 2)); _shortNameToUnicode.put("mx", new String(new int[]
		 * {0x1F1F2,0x1F1FD}, 0, 2)); _shortNameToUnicode.put("kr", new
		 * String(new int[] {0x1F1F0,0x1F1F7}, 0, 2));
		 * _shortNameToUnicode.put("fm", new String(new int[] {0x1F1EB,0x1F1F2},
		 * 0, 2)); _shortNameToUnicode.put("md", new String(new int[]
		 * {0x1F1F2,0x1F1E9}, 0, 2)); _shortNameToUnicode.put("mc", new
		 * String(new int[] {0x1F1F2,0x1F1E8}, 0, 2));
		 * _shortNameToUnicode.put("us", new String(new int[] {0x1F1FA,0x1F1F8},
		 * 0, 2)); _shortNameToUnicode.put("grinning", new String(new int[]
		 * {0x1F600}, 0, 1)); _shortNameToUnicode.put("mn", new String(new int[]
		 * {0x1F1F2,0x1F1F3}, 0, 2)); _shortNameToUnicode.put("innocent", new
		 * String(new int[] {0x1F607}, 0, 1)); _shortNameToUnicode.put("me", new
		 * String(new int[] {0x1F1F2,0x1F1EA}, 0, 2));
		 * _shortNameToUnicode.put("smiling_imp", new String(new int[]
		 * {0x1F608}, 0, 1)); _shortNameToUnicode.put("ma", new String(new int[]
		 * {0x1F1F2,0x1F1E6}, 0, 2)); _shortNameToUnicode.put("ru", new
		 * String(new int[] {0x1F1F7,0x1F1FA}, 0, 2));
		 * _shortNameToUnicode.put("sunglasses", new String(new int[] {0x1F60E},
		 * 0, 1)); _shortNameToUnicode.put("mz", new String(new int[]
		 * {0x1F1F2,0x1F1FF}, 0, 2)); _shortNameToUnicode.put("neutral_face",
		 * new String(new int[] {0x1F610}, 0, 1)); _shortNameToUnicode.put("mm",
		 * new String(new int[] {0x1F1F2,0x1F1F2}, 0, 2));
		 * _shortNameToUnicode.put("expressionless", new String(new int[]
		 * {0x1F611}, 0, 1)); _shortNameToUnicode.put("na", new String(new int[]
		 * {0x1F1F3,0x1F1E6}, 0, 2)); _shortNameToUnicode.put("confused", new
		 * String(new int[] {0x1F615}, 0, 1)); _shortNameToUnicode.put("nr", new
		 * String(new int[] {0x1F1F3,0x1F1F7}, 0, 2));
		 * _shortNameToUnicode.put("kissing", new String(new int[] {0x1F617}, 0,
		 * 1)); _shortNameToUnicode.put("np", new String(new int[]
		 * {0x1F1F3,0x1F1F5}, 0, 2));
		 * _shortNameToUnicode.put("kissing_smiling_eyes", new String(new int[]
		 * {0x1F619}, 0, 1)); _shortNameToUnicode.put("nl", new String(new int[]
		 * {0x1F1F3,0x1F1F1}, 0, 2));
		 * _shortNameToUnicode.put("stuck_out_tongue", new String(new int[]
		 * {0x1F61B}, 0, 1)); _shortNameToUnicode.put("nz", new String(new int[]
		 * {0x1F1F3,0x1F1FF}, 0, 2)); _shortNameToUnicode.put("worried", new
		 * String(new int[] {0x1F61F}, 0, 1)); _shortNameToUnicode.put("ni", new
		 * String(new int[] {0x1F1F3,0x1F1EE}, 0, 2));
		 * _shortNameToUnicode.put("frowning", new String(new int[] {0x1F626},
		 * 0, 1)); _shortNameToUnicode.put("ne", new String(new int[]
		 * {0x1F1F3,0x1F1EA}, 0, 2)); _shortNameToUnicode.put("anguished", new
		 * String(new int[] {0x1F627}, 0, 1));
		 * _shortNameToUnicode.put("grimacing", new String(new int[] {0x1F62C},
		 * 0, 1)); _shortNameToUnicode.put("kp", new String(new int[]
		 * {0x1F1F0,0x1F1F5}, 0, 2)); _shortNameToUnicode.put("open_mouth", new
		 * String(new int[] {0x1F62E}, 0, 1)); _shortNameToUnicode.put("no", new
		 * String(new int[] {0x1F1F3,0x1F1F4}, 0, 2));
		 * _shortNameToUnicode.put("hushed", new String(new int[] {0x1F62F}, 0,
		 * 1)); _shortNameToUnicode.put("om", new String(new int[]
		 * {0x1F1F4,0x1F1F2}, 0, 2)); _shortNameToUnicode.put("sleeping", new
		 * String(new int[] {0x1F634}, 0, 1)); _shortNameToUnicode.put("pk", new
		 * String(new int[] {0x1F1F5,0x1F1F0}, 0, 2));
		 * _shortNameToUnicode.put("no_mouth", new String(new int[] {0x1F636},
		 * 0, 1)); _shortNameToUnicode.put("pw", new String(new int[]
		 * {0x1F1F5,0x1F1FC}, 0, 2)); _shortNameToUnicode.put("helicopter", new
		 * String(new int[] {0x1F681}, 0, 1)); _shortNameToUnicode.put("pa", new
		 * String(new int[] {0x1F1F5,0x1F1E6}, 0, 2));
		 * _shortNameToUnicode.put("steam_locomotive", new String(new int[]
		 * {0x1F682}, 0, 1)); _shortNameToUnicode.put("pg", new String(new int[]
		 * {0x1F1F5,0x1F1EC}, 0, 2)); _shortNameToUnicode.put("train2", new
		 * String(new int[] {0x1F686}, 0, 1)); _shortNameToUnicode.put("py", new
		 * String(new int[] {0x1F1F5,0x1F1FE}, 0, 2));
		 * _shortNameToUnicode.put("light_rail", new String(new int[] {0x1F688},
		 * 0, 1)); _shortNameToUnicode.put("tram", new String(new int[]
		 * {0x1F68A}, 0, 1)); _shortNameToUnicode.put("pe", new String(new int[]
		 * {0x1F1F5,0x1F1EA}, 0, 2)); _shortNameToUnicode.put("oncoming_bus",
		 * new String(new int[] {0x1F68D}, 0, 1)); _shortNameToUnicode.put("ph",
		 * new String(new int[] {0x1F1F5,0x1F1ED}, 0, 2));
		 * _shortNameToUnicode.put("trolleybus", new String(new int[] {0x1F68E},
		 * 0, 1)); _shortNameToUnicode.put("pl", new String(new int[]
		 * {0x1F1F5,0x1F1F1}, 0, 2)); _shortNameToUnicode.put("minibus", new
		 * String(new int[] {0x1F690}, 0, 1)); _shortNameToUnicode.put("pt", new
		 * String(new int[] {0x1F1F5,0x1F1F9}, 0, 2));
		 * _shortNameToUnicode.put("oncoming_police_car", new String(new int[]
		 * {0x1F694}, 0, 1)); _shortNameToUnicode.put("qa", new String(new int[]
		 * {0x1F1F6,0x1F1E6}, 0, 2)); _shortNameToUnicode.put("oncoming_taxi",
		 * new String(new int[] {0x1F696}, 0, 1)); _shortNameToUnicode.put("tw",
		 * new String(new int[] {0x1F1F9,0x1F1FC}, 0, 2));
		 * _shortNameToUnicode.put("oncoming_automobile", new String(new int[]
		 * {0x1F698}, 0, 1)); _shortNameToUnicode.put("cg", new String(new int[]
		 * {0x1F1E8,0x1F1EC}, 0, 2));
		 * _shortNameToUnicode.put("articulated_lorry", new String(new int[]
		 * {0x1F69B}, 0, 1)); _shortNameToUnicode.put("ro", new String(new int[]
		 * {0x1F1F7,0x1F1F4}, 0, 2)); _shortNameToUnicode.put("tractor", new
		 * String(new int[] {0x1F69C}, 0, 1));
		 * _shortNameToUnicode.put("monorail", new String(new int[] {0x1F69D},
		 * 0, 1)); _shortNameToUnicode.put("rw", new String(new int[]
		 * {0x1F1F7,0x1F1FC}, 0, 2));
		 * _shortNameToUnicode.put("mountain_railway", new String(new int[]
		 * {0x1F69E}, 0, 1)); _shortNameToUnicode.put("kn", new String(new int[]
		 * {0x1F1F0,0x1F1F3}, 0, 2));
		 * _shortNameToUnicode.put("suspension_railway", new String(new int[]
		 * {0x1F69F}, 0, 1)); _shortNameToUnicode.put("lc", new String(new int[]
		 * {0x1F1F1,0x1F1E8}, 0, 2));
		 * _shortNameToUnicode.put("mountain_cableway", new String(new int[]
		 * {0x1F6A0}, 0, 1)); _shortNameToUnicode.put("vc", new String(new int[]
		 * {0x1F1FB,0x1F1E8}, 0, 2)); _shortNameToUnicode.put("aerial_tramway",
		 * new String(new int[] {0x1F6A1}, 0, 1)); _shortNameToUnicode.put("ws",
		 * new String(new int[] {0x1F1FC,0x1F1F8}, 0, 2));
		 * _shortNameToUnicode.put("rowboat", new String(new int[] {0x1F6A3}, 0,
		 * 1)); _shortNameToUnicode.put("sm", new String(new int[]
		 * {0x1F1F8,0x1F1F2}, 0, 2));
		 * _shortNameToUnicode.put("vertical_traffic_light", new String(new
		 * int[] {0x1F6A6}, 0, 1)); _shortNameToUnicode.put("st", new String(new
		 * int[] {0x1F1F8,0x1F1F9}, 0, 2));
		 * _shortNameToUnicode.put("put_litter_in_its_place", new String(new
		 * int[] {0x1F6AE}, 0, 1)); _shortNameToUnicode.put("do_not_litter", new
		 * String(new int[] {0x1F6AF}, 0, 1)); _shortNameToUnicode.put("sn", new
		 * String(new int[] {0x1F1F8,0x1F1F3}, 0, 2));
		 * _shortNameToUnicode.put("potable_water", new String(new int[]
		 * {0x1F6B0}, 0, 1)); _shortNameToUnicode.put("rs", new String(new int[]
		 * {0x1F1F7,0x1F1F8}, 0, 2));
		 * _shortNameToUnicode.put("non-potable_water", new String(new int[]
		 * {0x1F6B1}, 0, 1)); _shortNameToUnicode.put("sc", new String(new int[]
		 * {0x1F1F8,0x1F1E8}, 0, 2)); _shortNameToUnicode.put("no_bicycles", new
		 * String(new int[] {0x1F6B3}, 0, 1)); _shortNameToUnicode.put("sl", new
		 * String(new int[] {0x1F1F8,0x1F1F1}, 0, 2));
		 * _shortNameToUnicode.put("bicyclist", new String(new int[] {0x1F6B4},
		 * 0, 1)); _shortNameToUnicode.put("sg", new String(new int[]
		 * {0x1F1F8,0x1F1EC}, 0, 2));
		 * _shortNameToUnicode.put("mountain_bicyclist", new String(new int[]
		 * {0x1F6B5}, 0, 1)); _shortNameToUnicode.put("sk", new String(new int[]
		 * {0x1F1F8,0x1F1F0}, 0, 2)); _shortNameToUnicode.put("no_pedestrians",
		 * new String(new int[] {0x1F6B7}, 0, 1)); _shortNameToUnicode.put("si",
		 * new String(new int[] {0x1F1F8,0x1F1EE}, 0, 2));
		 * _shortNameToUnicode.put("children_crossing", new String(new int[]
		 * {0x1F6B8}, 0, 1)); _shortNameToUnicode.put("sb", new String(new int[]
		 * {0x1F1F8,0x1F1E7}, 0, 2)); _shortNameToUnicode.put("shower", new
		 * String(new int[] {0x1F6BF}, 0, 1)); _shortNameToUnicode.put("so", new
		 * String(new int[] {0x1F1F8,0x1F1F4}, 0, 2));
		 * _shortNameToUnicode.put("bathtub", new String(new int[] {0x1F6C1}, 0,
		 * 1)); _shortNameToUnicode.put("za", new String(new int[]
		 * {0x1F1FF,0x1F1E6}, 0, 2));
		 * _shortNameToUnicode.put("passport_control", new String(new int[]
		 * {0x1F6C2}, 0, 1)); _shortNameToUnicode.put("customs", new String(new
		 * int[] {0x1F6C3}, 0, 1)); _shortNameToUnicode.put("baggage_claim", new
		 * String(new int[] {0x1F6C4}, 0, 1)); _shortNameToUnicode.put("lk", new
		 * String(new int[] {0x1F1F1,0x1F1F0}, 0, 2));
		 * _shortNameToUnicode.put("left_luggage", new String(new int[]
		 * {0x1F6C5}, 0, 1)); _shortNameToUnicode.put("sd", new String(new int[]
		 * {0x1F1F8,0x1F1E9}, 0, 2)); _shortNameToUnicode.put("earth_africa",
		 * new String(new int[] {0x1F30D}, 0, 1)); _shortNameToUnicode.put("sr",
		 * new String(new int[] {0x1F1F8,0x1F1F7}, 0, 2));
		 * _shortNameToUnicode.put("earth_americas", new String(new int[]
		 * {0x1F30E}, 0, 1)); _shortNameToUnicode.put("sz", new String(new int[]
		 * {0x1F1F8,0x1F1FF}, 0, 2));
		 * _shortNameToUnicode.put("globe_with_meridians", new String(new int[]
		 * {0x1F310}, 0, 1)); _shortNameToUnicode.put("se", new String(new int[]
		 * {0x1F1F8,0x1F1EA}, 0, 2));
		 * _shortNameToUnicode.put("waxing_crescent_moon", new String(new int[]
		 * {0x1F312}, 0, 1)); _shortNameToUnicode.put("ch", new String(new int[]
		 * {0x1F1E8,0x1F1ED}, 0, 2));
		 * _shortNameToUnicode.put("waning_gibbous_moon", new String(new int[]
		 * {0x1F316}, 0, 1)); _shortNameToUnicode.put("sy", new String(new int[]
		 * {0x1F1F8,0x1F1FE}, 0, 2));
		 * _shortNameToUnicode.put("last_quarter_moon", new String(new int[]
		 * {0x1F317}, 0, 1)); _shortNameToUnicode.put("tj", new String(new int[]
		 * {0x1F1F9,0x1F1EF}, 0, 2));
		 * _shortNameToUnicode.put("waning_crescent_moon", new String(new int[]
		 * {0x1F318}, 0, 1)); _shortNameToUnicode.put("tz", new String(new int[]
		 * {0x1F1F9,0x1F1FF}, 0, 2));
		 * _shortNameToUnicode.put("new_moon_with_face", new String(new int[]
		 * {0x1F31A}, 0, 1)); _shortNameToUnicode.put("th", new String(new int[]
		 * {0x1F1F9,0x1F1ED}, 0, 2));
		 * _shortNameToUnicode.put("last_quarter_moon_with_face", new String(new
		 * int[] {0x1F31C}, 0, 1)); _shortNameToUnicode.put("tg", new String(new
		 * int[] {0x1F1F9,0x1F1EC}, 0, 2));
		 * _shortNameToUnicode.put("full_moon_with_face", new String(new int[]
		 * {0x1F31D}, 0, 1)); _shortNameToUnicode.put("to", new String(new int[]
		 * {0x1F1F9,0x1F1F4}, 0, 2)); _shortNameToUnicode.put("sun_with_face",
		 * new String(new int[] {0x1F31E}, 0, 1)); _shortNameToUnicode.put("tt",
		 * new String(new int[] {0x1F1F9,0x1F1F9}, 0, 2));
		 * _shortNameToUnicode.put("evergreen_tree", new String(new int[]
		 * {0x1F332}, 0, 1)); _shortNameToUnicode.put("tn", new String(new int[]
		 * {0x1F1F9,0x1F1F3}, 0, 2)); _shortNameToUnicode.put("deciduous_tree",
		 * new String(new int[] {0x1F333}, 0, 1)); _shortNameToUnicode.put("tr",
		 * new String(new int[] {0x1F1F9,0x1F1F7}, 0, 2));
		 * _shortNameToUnicode.put("lemon", new String(new int[] {0x1F34B}, 0,
		 * 1)); _shortNameToUnicode.put("pear", new String(new int[] {0x1F350},
		 * 0, 1)); _shortNameToUnicode.put("baby_bottle", new String(new int[]
		 * {0x1F37C}, 0, 1)); _shortNameToUnicode.put("ug", new String(new int[]
		 * {0x1F1FA,0x1F1EC}, 0, 2)); _shortNameToUnicode.put("horse_racing",
		 * new String(new int[] {0x1F3C7}, 0, 1)); _shortNameToUnicode.put("ua",
		 * new String(new int[] {0x1F1FA,0x1F1E6}, 0, 2));
		 * _shortNameToUnicode.put("rugby_football", new String(new int[]
		 * {0x1F3C9}, 0, 1)); _shortNameToUnicode.put("ae", new String(new int[]
		 * {0x1F1E6,0x1F1EA}, 0, 2));
		 * _shortNameToUnicode.put("european_post_office", new String(new int[]
		 * {0x1F3E4}, 0, 1)); _shortNameToUnicode.put("rat", new String(new
		 * int[] {0x1F400}, 0, 1)); _shortNameToUnicode.put("mouse2", new
		 * String(new int[] {0x1F401}, 0, 1)); _shortNameToUnicode.put("uy", new
		 * String(new int[] {0x1F1FA,0x1F1FE}, 0, 2));
		 * _shortNameToUnicode.put("ox", new String(new int[] {0x1F402}, 0, 1));
		 * _shortNameToUnicode.put("uz", new String(new int[] {0x1F1FA,0x1F1FF},
		 * 0, 2)); _shortNameToUnicode.put("water_buffalo", new String(new int[]
		 * {0x1F403}, 0, 1)); _shortNameToUnicode.put("vu", new String(new int[]
		 * {0x1F1FB,0x1F1FA}, 0, 2)); _shortNameToUnicode.put("cow2", new
		 * String(new int[] {0x1F404}, 0, 1)); _shortNameToUnicode.put("va", new
		 * String(new int[] {0x1F1FB,0x1F1E6}, 0, 2));
		 * _shortNameToUnicode.put("tiger2", new String(new int[] {0x1F405}, 0,
		 * 1)); _shortNameToUnicode.put("ve", new String(new int[]
		 * {0x1F1FB,0x1F1EA}, 0, 2)); _shortNameToUnicode.put("leopard", new
		 * String(new int[] {0x1F406}, 0, 1)); _shortNameToUnicode.put("vn", new
		 * String(new int[] {0x1F1FB,0x1F1F3}, 0, 2));
		 * _shortNameToUnicode.put("rabbit2", new String(new int[] {0x1F407}, 0,
		 * 1)); _shortNameToUnicode.put("eh", new String(new int[]
		 * {0x1F1EA,0x1F1ED}, 0, 2)); _shortNameToUnicode.put("cat2", new
		 * String(new int[] {0x1F408}, 0, 1)); _shortNameToUnicode.put("ye", new
		 * String(new int[] {0x1F1FE,0x1F1EA}, 0, 2));
		 * _shortNameToUnicode.put("dragon", new String(new int[] {0x1F409}, 0,
		 * 1)); _shortNameToUnicode.put("zm", new String(new int[]
		 * {0x1F1FF,0x1F1F2}, 0, 2)); _shortNameToUnicode.put("crocodile", new
		 * String(new int[] {0x1F40A}, 0, 1)); _shortNameToUnicode.put("zw", new
		 * String(new int[] {0x1F1FF,0x1F1FC}, 0, 2));
		 * _shortNameToUnicode.put("whale2", new String(new int[] {0x1F40B}, 0,
		 * 1)); _shortNameToUnicode.put("pr", new String(new int[]
		 * {0x1F1F5,0x1F1F7}, 0, 2)); _shortNameToUnicode.put("ram", new
		 * String(new int[] {0x1F40F}, 0, 1)); _shortNameToUnicode.put("ky", new
		 * String(new int[] {0x1F1F0,0x1F1FE}, 0, 2));
		 * _shortNameToUnicode.put("goat", new String(new int[] {0x1F410}, 0,
		 * 1)); _shortNameToUnicode.put("bm", new String(new int[]
		 * {0x1F1E7,0x1F1F2}, 0, 2)); _shortNameToUnicode.put("rooster", new
		 * String(new int[] {0x1F413}, 0, 1)); _shortNameToUnicode.put("pf", new
		 * String(new int[] {0x1F1F5,0x1F1EB}, 0, 2));
		 * _shortNameToUnicode.put("dog2", new String(new int[] {0x1F415}, 0,
		 * 1)); _shortNameToUnicode.put("ps", new String(new int[]
		 * {0x1F1F5,0x1F1F8}, 0, 2)); _shortNameToUnicode.put("pig2", new
		 * String(new int[] {0x1F416}, 0, 1)); _shortNameToUnicode.put("nc", new
		 * String(new int[] {0x1F1F3,0x1F1E8}, 0, 2));
		 * _shortNameToUnicode.put("dromedary_camel", new String(new int[]
		 * {0x1F42A}, 0, 1)); _shortNameToUnicode.put("sh", new String(new int[]
		 * {0x1F1F8,0x1F1ED}, 0, 2));
		 * _shortNameToUnicode.put("busts_in_silhouette", new String(new int[]
		 * {0x1F465}, 0, 1)); _shortNameToUnicode.put("aw", new String(new int[]
		 * {0x1F1E6,0x1F1FC}, 0, 2));
		 * _shortNameToUnicode.put("two_men_holding_hands", new String(new int[]
		 * {0x1F46C}, 0, 1)); _shortNameToUnicode.put("vi", new String(new int[]
		 * {0x1F1FB,0x1F1EE}, 0, 2));
		 * _shortNameToUnicode.put("two_women_holding_hands", new String(new
		 * int[] {0x1F46D}, 0, 1)); _shortNameToUnicode.put("hk", new String(new
		 * int[] {0x1F1ED,0x1F1F0}, 0, 2));
		 * _shortNameToUnicode.put("thought_balloon", new String(new int[]
		 * {0x1F4AD}, 0, 1)); _shortNameToUnicode.put("ac", new String(new int[]
		 * {0x1F1E6,0x1F1E8}, 0, 2)); _shortNameToUnicode.put("euro", new
		 * String(new int[] {0x1F4B6}, 0, 1)); _shortNameToUnicode.put("ms", new
		 * String(new int[] {0x1F1F2,0x1F1F8}, 0, 2));
		 * _shortNameToUnicode.put("pound", new String(new int[] {0x1F4B7}, 0,
		 * 1)); _shortNameToUnicode.put("gu", new String(new int[]
		 * {0x1F1EC,0x1F1FA}, 0, 2));
		 * _shortNameToUnicode.put("mailbox_with_mail", new String(new int[]
		 * {0x1F4EC}, 0, 1)); _shortNameToUnicode.put("gl", new String(new int[]
		 * {0x1F1EC,0x1F1F1}, 0, 2));
		 * _shortNameToUnicode.put("mailbox_with_no_mail", new String(new int[]
		 * {0x1F4ED}, 0, 1)); _shortNameToUnicode.put("nu", new String(new int[]
		 * {0x1F1F3,0x1F1FA}, 0, 2)); _shortNameToUnicode.put("postal_horn", new
		 * String(new int[] {0x1F4EF}, 0, 1)); _shortNameToUnicode.put("wf", new
		 * String(new int[] {0x1F1FC,0x1F1EB}, 0, 2));
		 * _shortNameToUnicode.put("no_mobile_phones", new String(new int[]
		 * {0x1F4F5}, 0, 1)); _shortNameToUnicode.put("mo", new String(new int[]
		 * {0x1F1F2,0x1F1F4}, 0, 2));
		 * _shortNameToUnicode.put("twisted_rightwards_arrows", new String(new
		 * int[] {0x1F500}, 0, 1)); _shortNameToUnicode.put("fo", new String(new
		 * int[] {0x1F1EB,0x1F1F4}, 0, 2)); _shortNameToUnicode.put("repeat",
		 * new String(new int[] {0x1F501}, 0, 1)); _shortNameToUnicode.put("fk",
		 * new String(new int[] {0x1F1EB,0x1F1F0}, 0, 2));
		 * _shortNameToUnicode.put("repeat_one", new String(new int[] {0x1F502},
		 * 0, 1)); _shortNameToUnicode.put("je", new String(new int[]
		 * {0x1F1EF,0x1F1EA}, 0, 2));
		 * _shortNameToUnicode.put("arrows_counterclockwise", new String(new
		 * int[] {0x1F504}, 0, 1)); _shortNameToUnicode.put("ai", new String(new
		 * int[] {0x1F1E6,0x1F1EE}, 0, 2));
		 * _shortNameToUnicode.put("low_brightness", new String(new int[]
		 * {0x1F505}, 0, 1)); _shortNameToUnicode.put("gi", new String(new int[]
		 * {0x1F1EC,0x1F1EE}, 0, 2)); _shortNameToUnicode.put("high_brightness",
		 * new String(new int[] {0x1F506}, 0, 1));
		 * _shortNameToUnicode.put("mute", new String(new int[] {0x1F507}, 0,
		 * 1)); _shortNameToUnicode.put("sound", new String(new int[] {0x1F509},
		 * 0, 1)); _shortNameToUnicode.put("no_bell", new String(new int[]
		 * {0x1F515}, 0, 1)); _shortNameToUnicode.put("microscope", new
		 * String(new int[] {0x1F52C}, 0, 1));
		 * _shortNameToUnicode.put("telescope", new String(new int[] {0x1F52D},
		 * 0, 1)); _shortNameToUnicode.put("clock130", new String(new int[]
		 * {0x1F55C}, 0, 1)); _shortNameToUnicode.put("clock230", new String(new
		 * int[] {0x1F55D}, 0, 1)); _shortNameToUnicode.put("clock330", new
		 * String(new int[] {0x1F55E}, 0, 1));
		 * _shortNameToUnicode.put("clock430", new String(new int[] {0x1F55F},
		 * 0, 1)); _shortNameToUnicode.put("clock530", new String(new int[]
		 * {0x1F560}, 0, 1)); _shortNameToUnicode.put("clock630", new String(new
		 * int[] {0x1F561}, 0, 1)); _shortNameToUnicode.put("clock730", new
		 * String(new int[] {0x1F562}, 0, 1));
		 * _shortNameToUnicode.put("clock830", new String(new int[] {0x1F563},
		 * 0, 1)); _shortNameToUnicode.put("clock930", new String(new int[]
		 * {0x1F564}, 0, 1)); _shortNameToUnicode.put("clock1030", new
		 * String(new int[] {0x1F565}, 0, 1));
		 * _shortNameToUnicode.put("clock1130", new String(new int[] {0x1F566},
		 * 0, 1)); _shortNameToUnicode.put("clock1230", new String(new int[]
		 * {0x1F567}, 0, 1));
		 */
	}

}
