package com.skillbox.boxes.externalstrings;

import java.io.PrintStream;
import java.util.Locale;
import java.util.Map.Entry;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

abstract class MenuOption {
	public MenuOption() {
		super();
	}

	abstract void command();

	abstract String descriptionTag();
}

public class MenuOptions {

	private final SortedMap<String, MenuOption> mCommands;
	private final ResourceBundle mResourceBundle;
	private String mTitle;
	private Logger mLogger = Logger.getLogger(MenuOptions.class.getName());

	static final String sResourcePrefix = MenuOptions.class.getName() + "."; //$NON-NLS-1$

	String getLocaleString(String tag) {
		try {
			return mResourceBundle.getString(getResourceTag(tag));
		} catch (MissingResourceException e) {
			return '!' + getResourceTag(tag) + '!';
		}
	}

	private String getResourceTag(String tag) {
		return sResourcePrefix + tag;
	}

	void printMenuEntry(String menuKey, MenuOption option, PrintStream stream) {
		String format = getLocaleString("option_format"); //$NON-NLS-1$
		stream.printf(format, getLocaleString(menuKey),
				getLocaleString(option.descriptionTag()));
	}

	public MenuOptions(final Locale locale) {
		mCommands = new TreeMap<String, MenuOption>();
		mResourceBundle = ResourceBundle
				.getBundle(getClass().getName(), locale);

		mTitle = getLocaleString("menu_title");

		// Create anonymous inner classes
		mCommands.put("demo", new MenuOption() { //$NON-NLS-1$
					@Override
					void command() {
						System.out
								.println("Performing the actual demo command (in " + locale.getDisplayLanguage() + ")"); //$NON-NLS-1$
					}

					@Override
					String descriptionTag() {
						return "demo.description";
					}
				});

		// Create anonymous inner classes
		mCommands.put("help", new MenuOption() { //$NON-NLS-1$
					@Override
					void command() {
						System.out
								.println("Performing the actual Help command (in " + locale.getDisplayLanguage() + ")"); //$NON-NLS-1$
					}

					@Override
					String descriptionTag() {
						return "help.description";
					}
				});

	}

	public void runCommand(String command) {
		for (Entry<String, MenuOption> entry : mCommands.entrySet()) {

			mLogger.log(Level.FINER, "Looking at " + entry.getKey() + " for "
					+ command);

			String commandLocalized = getLocaleString(entry.getKey());

			if (commandLocalized.equals(command)) {
				mLogger.log(Level.FINER, "Running " + entry.getKey() + " for "
						+ command);
				entry.getValue().command();
				return;
			}
		}
		System.out.printf(getLocaleString("unknown_command_format"), command);
	}

	public void printMenu() {

		String mTitleUnderline = new String(new char[mTitle.length()]).replace(
				"\0", "=");

		System.out.printf("%n%s%n%s%n", mTitle, mTitleUnderline);

		for (Entry<String, MenuOption> entry : mCommands.entrySet()) {
			printMenuEntry(entry.getKey(), entry.getValue(), System.out);
		}
	}

	static public void main(String[] args) {

		runWithLocale(Locale.getDefault());
		runWithLocale(Locale.FRENCH);

	}

	private static void runWithLocale(Locale locale) {
		MenuOptions defaultOptions = new MenuOptions(locale);

		defaultOptions.printMenu();

		System.out.printf("Pretending the User Entered 'Demo'%n");
		defaultOptions.runCommand("Demo");
		
		System.out.printf("Pretending the User Entered 'LeDemo'%n");
		defaultOptions.runCommand("LeDemo");
	}
}
