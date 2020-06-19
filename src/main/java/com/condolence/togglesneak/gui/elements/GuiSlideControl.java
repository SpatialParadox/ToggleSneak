package com.condolence.togglesneak.gui.elements;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import org.lwjgl.opengl.GL11;

import java.text.DecimalFormat;

public class GuiSlideControl extends GuiButton {
    public String label;
    public float currentValue;
    public float minimumValue;
    public float maximumValue;
    public boolean isSliding;
    public boolean useIntegers;
    private static DecimalFormat numberFormat;

    public GuiSlideControl(final int id, final int x, final int y, final int width, final int height, final String displayString, final float minVal, final float maxVal, final float curVal, final boolean useInts) {
        super(id, x, y, width, height, useInts ? (displayString + (int)curVal) : (displayString + GuiSlideControl.numberFormat.format(curVal)));
        this.label = displayString;
        this.minimumValue = minVal;
        this.maximumValue = maxVal;
        this.currentValue = (curVal - minVal) / (maxVal - minVal);
        this.useIntegers = useInts;
    }

    // GET VALUE METHODS
    public float getFloat() {
        return (this.maximumValue - this.minimumValue) * this.currentValue + this.minimumValue;
    }

    public float getInt() {
        return (int)((this.maximumValue - this.minimumValue) * this.currentValue + this.minimumValue);
    }

    // GET LABEL METHODS
    public String getLabel() {
        if (this.useIntegers) { return this.label + this.getInt(); }
        return this.label + GuiSlideControl.numberFormat.format(this.getFloat());
    }

    protected void setLabel() {
        this.displayString = this.getLabel();
    }

    public int getHoverState(final boolean isMouseOver) {
        return 0;
    }

    // MAIN METHODS
    protected void mouseDragged(final Minecraft mc, final int mousePosX, final int mousePosY) {
        if (this.visible) {
            if (this.isSliding) {
                this.currentValue = (mousePosX - (this.xPosition + 4.0f)) / (this.width - 8.0f);
                this.currentValue = this.clamp(this.currentValue, 0.0f, 1.0f);
                this.setLabel();
            }

            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            drawRect(this.xPosition + 1, this.yPosition + 1, this.xPosition + (int)(this.currentValue * (this.width - 8)) + 7, this.yPosition + 19, this.getColorWithAlpha(8168374, 100));
            drawRect(this.xPosition + (int)(this.currentValue * (this.width - 8)) + 1, this.yPosition + 1, this.xPosition + (int)(this.currentValue * (this.width - 8)) + 7, this.yPosition + 19, this.getColorWithAlpha(8168374, 200));
        }
    }

    public boolean mousePressed(final Minecraft mc, final int mousePosX, final int mousePosY) {
        if (!super.mousePressed(mc, mousePosX, mousePosY)) { return false; }

        this.currentValue = (mousePosX - (this.xPosition + 4.0f)) / (this.width - 8.0f);
        this.currentValue = this.clamp(this.currentValue, 0.0f, 1.0f);
        this.setLabel();

        if (this.isSliding) { return this.isSliding = false; }
        return this.isSliding = true;
    }

    public void mouseReleased(final int mousePosX, final int mousePosY) {
        this.isSliding = false;
    }

    public void drawButton(final Minecraft mc, final int mouseX, final int mouseY) {
        if (this.visible) {
            final FontRenderer fontrenderer = mc.fontRendererObj;
            mc.getTextureManager().bindTexture(GuiSlideControl.buttonTextures);
            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
            this.hovered = (mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height);
            final int k = this.getHoverState(this.visible);
            GL11.glEnable(3042);
            OpenGlHelper.glBlendFunc(770, 771, 1, 0);
            GL11.glBlendFunc(770, 771);
            drawRect(this.xPosition + 1, this.yPosition + 1, this.xPosition + this.width - 1, this.yPosition + this.height - 1, this.getColorWithAlpha(8168374, 50));
            this.mouseDragged(mc, mouseX, mouseY);
            int l = 14737632;
            if (this.packedFGColour != 0) {
                l = this.packedFGColour;
            }
            else if (!this.enabled) {
                l = 10526880;
            }
            else if (this.hovered) {
                l = 16777120;
            }
            this.drawCenteredString(fontrenderer, this.displayString, this.xPosition + this.width / 2, this.yPosition + (this.height - 8) / 2, l);
            GL11.glColor3f(1.0f, 1.0f, 1.0f);
        }
    }

    // UTIL METHODS
    private int getColorWithAlpha(final int rgb, final int a) {
        final int r = rgb >> 16 & 0xFF;
        final int g = rgb >> 8 & 0xFF;
        final int b = rgb & 0xFF;
        return a << 24 | r << 16 | g << 8 | b;
    }

    private float clamp(float value, float min, float max) {
        return Math.min(Math.max(value, min), max);
    }
    static { GuiSlideControl.numberFormat = new DecimalFormat("#.00"); }
}
