package taintedmagic.common.handler;

import java.net.URL;
import java.util.Scanner;

import net.minecraft.util.EnumChatFormatting;
import taintedmagic.common.TaintedMagic;
import taintedmagic.common.lib.LibInfo;

public class UpdateHandler
{
	private static String currentVersion = LibInfo.VERSION;
	private static String newestVersion;
	public static String updateStatus = null;
	public static boolean show = false;

	public static void init ()
	{
		getNewestVersion();

		if (newestVersion != null)
		{
			if (newestVersion.equalsIgnoreCase(currentVersion))
			{
				show = false;
			}
			else if (!newestVersion.equalsIgnoreCase(currentVersion))
			{
				show = true;
				updateStatus = (EnumChatFormatting.RED + "Tainted Magic is out of date! The latest version is " + EnumChatFormatting.GREEN + newestVersion);
				TaintedMagic.log.info("Mod out of date! You're still running: " + currentVersion + ", Latest Version: " + newestVersion);
			}
		}
		else
		{
			show = true;
			updateStatus = (EnumChatFormatting.RED + "Tainted Magic failed to connect to update server!");
			TaintedMagic.log.info("Failed to connect to update server...");
		}
	}

	private static void getNewestVersion ()
	{
		try
		{
			URL url = new URL("https://raw.githubusercontent.com/yorkeJohn/Tainted-Magic/master/version.txt");
			Scanner s = new Scanner(url.openStream());
			newestVersion = s.next();
			s.close();
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			TaintedMagic.log.info("Could not connect to GitHub repository!");
		}
	}
}
