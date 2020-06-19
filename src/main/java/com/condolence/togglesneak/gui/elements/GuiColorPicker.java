package com.condolence.togglesneak.gui.elements;

import com.condolence.togglesneak.util.MouseUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.Point;

import java.awt.*;

public class GuiColorPicker extends GuiButton {
    private int PICKER_SIZE;
    private boolean pickerVisible;
    private int pickedColor;
    public int pickerX;
    public int pickerY;
    private long pickerCreated;

    public GuiColorPicker(final int buttonId, final int x, final int y, final String text, final int pickerX, final int pickerY, final int color) {
        super(buttonId, x, y, text);
        this.PICKER_SIZE = 100;
        this.pickerVisible = true;
        this.pickedColor = color;
        this.pickerX = pickerX;
        this.pickerY = pickerY;
        this.pickerCreated = System.currentTimeMillis() / 1000L;
    }

    public void drawButton(final Minecraft mc, final int mouseX, final int mouseY) {
        if (this.visible) {
            final FontRenderer fontrenderer = mc.fontRendererObj;
            mc.getTextureManager().bindTexture(GuiColorPicker.buttonTextures);
            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
            GL11.glEnable(3042);
            GL11.glBlendFunc(770, 771);
            this.drawGradientRect(this.pickerX - 1, this.pickerY + 105, this.pickerX + 101, this.pickerY + 121, Color.BLACK.getRGB(), Color.BLACK.getRGB());
            this.drawGradientRect(this.pickerX, this.pickerY + 106, this.pickerX + 100, this.pickerY + 120, this.pickedColor, this.pickedColor);
            this.drawCenteredString(fontrenderer, "" + this.pickedColor, this.pickerX + 50, this.pickerY + 109, -1);
            if (this.pickerVisible && this.enabled) {
                int current = 0;
                int currentY = 0;
                this.drawGradientRect(this.pickerX - 1, this.pickerY - 1, this.pickerX + 100 + 1, this.pickerY + 100 + 1, Color.BLACK.getRGB(), Color.BLACK.getRGB());
                for (int x = 0; x < 100; ++x) {
                    for (int y = 0; y < 100; ++y) {
                        final int color = this.getColorAtPosition(x, y);
                        this.drawGradientRect(this.pickerX + x, this.pickerY + y, this.pickerX + x + 1, this.pickerY + y + 1, color, color);

                        if (x != 0 && y != 0) {
                            if (this.pickedColor == color) {
                                current = x;
                                currentY = y;
                                this.drawHorizontalLine(this.pickerX + x - 2, this.pickerX + x + 2, this.pickerY + y, -1);
                            }
                            if (x - 1 == current && y == currentY) {
                                this.drawVerticalLine(this.pickerX + x - 1, this.pickerY + y - 3, this.pickerY + y + 3, -1);
                            }
                            if (x - 2 == current && y == currentY) {
                                this.drawHorizontalLine(this.pickerX + x - 2, this.pickerX + x + 2, this.pickerY + y, -1);
                            }
                        }
                    }
                }
            }
            if (System.currentTimeMillis() / 1000L > this.pickerCreated + 1L) {
                this.mouseDragged(mc, mouseX, mouseY);
            }
        }
    }

    public boolean mousePressed(final Minecraft mc, final int mouseX, final int mouseY) {
        if (this.pickerVisible && this.enabled && MouseUtil.isMouseWithinBounds(this.pickerX, this.pickerY, 100, 100)) {
            final Point mousePoint = MouseUtil.getMousePos();
            this.setPickerColor(this.getColorAtPosition(mousePoint.getX() - this.pickerX, mousePoint.getY() - this.pickerY));
        }
        return super.mousePressed(mc, mouseX, mouseY);
    }

    public void mouseDragged(final Minecraft mc, final int mouseX, final int mouseY) {
        if (Mouse.isButtonDown(0)) {
            this.mousePressed(mc, mouseX, mouseY);
        }
    }

    public void setPickerVisible(final boolean visible) {
        this.pickerVisible = visible;
    }

    private int getColorAtPosition(final int x, final int y) {
        if (x < 0 || x > 100 || y < 0 || y > 100) {
            return 0;
        }
        final boolean grey = x >= 95;
        final float h = grey ? 0.0f : (x / 95.0f);
        final float s = grey ? 0.0f : ((y <= 50) ? (y / 50.0f) : 1.0f);
        final float v = grey ? (1.0f - y / 100.0f) : ((y > 50) ? (1.0f - (y - 50) / 50.0f) : 1.0f);
        return Color.HSBtoRGB(h, s, v);
    }

    public void setPickerColor(final int color) {
        this.pickedColor = color;
    }

    public int getPickedColor() { return this.pickedColor; }

    public void playPressSound(final SoundHandler p_146113_1_) { }
}
