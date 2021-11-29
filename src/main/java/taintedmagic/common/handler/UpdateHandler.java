package taintedmagic.common.handler;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import net.minecraft.util.EnumChatFormatting;
import taintedmagic.common.TaintedMagic;

public class UpdateHandler {
    private static String currentVersion = TaintedMagic.VERSION;
    private static String newestVersion;
    public static String updateStatus = null;

    public static void checkForUpdate () {
        getNewestVersion();

        if (newestVersion != null) {
            if (newestVersion.equalsIgnoreCase(currentVersion)) {
            }
            else if (!newestVersion.equalsIgnoreCase(currentVersion)) {
                updateStatus = EnumChatFormatting.RED + "Tainted Magic is out of date! The latest version is "
                        + EnumChatFormatting.GREEN + newestVersion;
                TaintedMagic.logger.warn("Mod out of date! You're still running {} ... the latest version is {}",
                        currentVersion, newestVersion);
            }
        }
        else {
            updateStatus = EnumChatFormatting.RED + "Tainted Magic failed to connect to the update server!";
        }
    }

    private static void getNewestVersion () {
        try {
            final URL url = new URL("https://raw.githubusercontent.com/yorkeJohn/Tainted-Magic/master/version.txt");
            final Scanner scan = new Scanner(url.openStream());
            newestVersion = scan.next();
            scan.close();
        }
        catch (final MalformedURLException e) {
            throw new IllegalStateException(e);
        }
        catch (final IOException e) {
            TaintedMagic.logger.error("Could not connect to GitHub repository!");
        }
    }
}
