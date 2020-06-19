package com.condolence.togglesneak.gui.elements;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.OpenGlHelper;
import org.lwjgl.opengl.GL11;

public class GuiTransButton extends GuiButton {
    public GuiTransButton(int buttonId, int x, int y, int widthIn, int heightIn, String buttonText)
    {
        super(buttonId, x, y, widthIn, heightIn, buttonText);
    }

    public void drawButton(final Minecraft mc, final int mouseX, final int mouseY) {
        if (this.visible) {
            final FontRenderer fontrenderer = mc.fontRendererObj;
            mc.getTextureManager().bindTexture(GuiTransButton.buttonTextures);
            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
            this.hovered = (mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height);
            final int k = this.getHoverState(this.hovered);
            GL11.glEnable(3042);
            OpenGlHelper.glBlendFunc(770, 771, 1, 0);
            GL11.glBlendFunc(770, 771);
            drawRect(this.xPosition + 1, this.yPosition + 1, this.xPosition + this.width - 1, this.yPosition + this.height - 1, this.getButtonColor());
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
        }
    }

    public int getButtonColor() {
        switch(super.getHoverState(super.hovered)) {
            case 0: {
                return this.getColorWithAlpha(8168374, 50);
            }
            case 1: {
                return this.getColorWithAlpha(8168374, 100);
            }
            case 2: {
                return this.getColorWithAlpha(8168374, 150);
            }
            default: {
                return 0;
            }
        }
    }

    private int getColorWithAlpha(final int rgb, final int a) {
        final int r = rgb >> 16 & 0xFF;
        final int g = rgb >> 8 & 0xFF;
        final int b = rgb & 0xFF;
        return a << 24 | r << 16 | g << 8 | b;
    }
}
