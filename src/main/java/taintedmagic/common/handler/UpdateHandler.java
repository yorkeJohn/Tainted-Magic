package taintedmagic.common.handler;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import net.minecraft.util.EnumChatFormatting;
import taintedmagic.common.TaintedMagic;

public class UpdateHandler {
    private static String latestVersion = null;
    public static String message = null;

    public static void checkForUpdate () {
        getNewestVersion();

        if (latestVersion != null) {
            if (TaintedMagic.VERSION.equals("@VERSION@")) {
                message = String.format("%sTainted Magic is running in DEV!", EnumChatFormatting.GREEN);
                TaintedMagic.logger.info("Tainted Magic is running in DEV!");
            }
            else if (!latestVersion.equalsIgnoreCase(TaintedMagic.VERSION)) {
                message = String.format("%sTainted Magic is out of date! The latest version is %s%s", EnumChatFormatting.RED,
                        EnumChatFormatting.GREEN, latestVersion);
                TaintedMagic.logger.warn("Mod out of date! You're still running {} ... the latest version is {}",
                        TaintedMagic.VERSION, latestVersion);
            }
        }
        else {
            message = String.format("%sTainted Magic failed to connect to the update server!", EnumChatFormatting.RED);
        }
    }

    private static void getNewestVersion () {
        try {
            final URL url = new URL("https://raw.githubusercontent.com/yorkeJohn/Tainted-Magic/master/version.txt");
            final Scanner scan = new Scanner(url.openStream());
            latestVersion = scan.next();
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
