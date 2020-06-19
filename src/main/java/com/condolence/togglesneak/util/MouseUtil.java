package com.condolence.togglesneak.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.Point;

public class MouseUtil {
    private static final Minecraft mc;

    public static Point getMousePos() {
        final Point scaled = getScaledDimensions();
        final int width = scaled.getX();
        final int height = scaled.getY();
        final int mouseX = Mouse.getX() * width / MouseUtil.mc.displayWidth;
        final int mouseY = height - Mouse.getY() * height / MouseUtil.mc.displayHeight;
        return new Point(mouseX, mouseY);
    }

    public static void moveMouse(final int mouseX, final int mouseY) {
        final Point scaled = getScaledDimensions();
        final int width = scaled.getX();
        final int height = scaled.getY();
        final int x = (int)Math.round((mouseX + 0.5) * MouseUtil.mc.displayWidth / width);
        final int y = mouseY * MouseUtil.mc.displayHeight / height;
        Mouse.setCursorPosition(x, y);
    }

    public static Point getScaledDimensions() {
        final ScaledResolution sr = new ScaledResolution(MouseUtil.mc);
        final int width = sr.getScaledWidth();
        final int height = sr.getScaledHeight();
        return new Point(width, height);
    }

    public static boolean isMouseWithinBounds(final int minX, final int minY, final int width, final int height) {
        final Point mousePos = getMousePos();
        return mousePos.getX() >= minX && mousePos.getX() <= minX + width && mousePos.getY() >= minY && mousePos.getY() <= minY + height;
    }

    static {
        mc = Minecraft.getMinecraft();
    }
}
