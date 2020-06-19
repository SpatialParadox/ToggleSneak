package com.condolence.togglesneak.gui.elements;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ChatAllowedCharacters;

public class GuiTransTextField extends Gui {
    private final FontRenderer fontRendererInstance;
    public int xPosition;
    public int yPosition;
    public int width;
    public int height;
    private String text;
    private int maxStringLength;
    private int cursorCounter;
    private boolean enableBackgroundDrawing;
    private boolean canLoseFocus;
    private boolean isFocused;
    private boolean isEnabled;
    private int lineScrollOffset;
    private int cursorPosition;
    private int selectionEnd;
    private int enabledColor;
    private int disabledColor;
    private boolean visible;

    public GuiTransTextField(final FontRenderer fontRenderer, final int xPosition, final int yPosition, final int width, final int height) {
        this.text = "";
        this.maxStringLength = 32;
        this.enableBackgroundDrawing = true;
        this.canLoseFocus = true;
        this.isEnabled = true;
        this.enabledColor = 14737632;
        this.disabledColor = 7368816;
        this.visible = true;
        this.fontRendererInstance = fontRenderer;
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.width = width;
        this.height = height;
    }

    public void updateCursorCounter() { ++this.cursorCounter; }

    public void setText(final String text) {
        if (text.length() > this.maxStringLength) {
            this.text = text.substring(0, this.maxStringLength);
        }
        else {
            this.text = text;
        }

        this.setCursorPositionEnd();
    }

    public String getText() { return this.text; }

    public String getSelectedText() {
        final int i = Math.min(this.cursorPosition, this.selectionEnd);
        final int j = Math.max(this.cursorPosition, this.selectionEnd);
        return this.text.substring(i, j);
    }

    public void writeText(final String text) {
        String s1 = "";
        final String s2 = ChatAllowedCharacters.filterAllowedCharacters(text);
        final int i = Math.min(this.cursorPosition, this.selectionEnd);
        final int j = Math.max(this.cursorPosition, this.selectionEnd);
        final int k = this.maxStringLength - this.text.length() - (i - this.selectionEnd);
        //final boolean flag = false;
        if (this.text.length() > 0) {
            s1 += this.text.substring(0, i);
        }
        int l;
        if (k < s2.length()) {
            s1 += s2.substring(0, k);
            l = k;
        }
        else {
            s1 += s2;
            l = s2.length();
        }
        if (this.text.length() > 0 && j < this.text.length()) {
            s1 += this.text.substring(j);
        }
        this.text = s1;
        this.moveCursorBy(i - this.selectionEnd + l);
    }

    public void deleteWords(final int position) {
        if (this.text.length() != 0) {
            if (this.selectionEnd != this.cursorPosition) {
                this.writeText("");
            }
            else {
                this.deleteFromCursor(this.getNthWordFromCursor(position) - this.cursorPosition);
            }
        }
    }

    public void deleteFromCursor(final int position) {
        if (this.text.length() != 0) {
            if (this.selectionEnd != this.cursorPosition) {
                this.writeText("");
            }
            else {
                final boolean flag = position < 0;
                final int j = flag ? (this.cursorPosition + position) : this.cursorPosition;
                final int k = flag ? this.cursorPosition : (this.cursorPosition + position);
                String s = "";
                if (j >= 0) {
                    s = this.text.substring(0, j);
                }
                if (k < this.text.length()) {
                    s += this.text.substring(k);
                }
                this.text = s;
                if (flag) {
                    this.moveCursorBy(position);
                }
            }
        }
    }

    public int getNthWordFromCursor(final int position) {
        return this.getNthWordFromPos(position);
    }

    // Argument variable names weren't renamed because the method called in this method is obfuscated (and I cannot figure out what its purpose is.)
    public int getNthWordFromPos(final int position) {
        return this.func_146197_a(position, this.getCursorPosition(), true);
    }

    // No idea what this method does.
    public int func_146197_a(final int p_146197_1_, final int p_146197_2_, final boolean p_146197_3_) {
        int k = p_146197_2_;
        final boolean flag1 = p_146197_1_ < 0;
        for (int l = Math.abs(p_146197_1_), i1 = 0; i1 < l; ++i1) {
            if (flag1) {
                while (p_146197_3_ && k > 0 && this.text.charAt(k - 1) == ' ') {
                    --k;
                }
                while (k > 0 && this.text.charAt(k - 1) != ' ') {
                    --k;
                }
            }
            else {
                final int j1 = this.text.length();
                k = this.text.indexOf(32, k);
                if (k == -1) {
                    k = j1;
                }
                else {
                    while (p_146197_3_ && k < j1 && this.text.charAt(k) == ' ') {
                        ++k;
                    }
                }
            }
        }
        return k;
    }

    public void moveCursorBy(final int position) { this.setCursorPosition(this.selectionEnd + position); }

    public void setCursorPosition(final int position) {
        this.cursorPosition = position;

        final int j = this.text.length();
        if (this.cursorPosition < 0) {
            this.cursorPosition = 0;
        }
        if (this.cursorPosition > j) {
            this.cursorPosition = j;
        }
        this.setSelectionPos(this.cursorPosition);
    }

    public void setCursorPositionZero() { this.setCursorPosition(0); }
    public void setCursorPositionEnd() { this.setCursorPosition(this.text.length()); }

    public boolean textboxKeyTyped(final char character, final int position) {
        if (!this.isFocused) { return false; }

        switch (character) {
            case '\u0001': {
                this.setCursorPositionEnd();
                this.setSelectionPos(0);
                return true;
            }
            case '\u0003': {
                GuiScreen.setClipboardString(this.getSelectedText());
                return true;
            }
            case '\u0016': {
                if (this.isEnabled) {
                    this.writeText(GuiScreen.getClipboardString());
                }
                return true;
            }
            case '\u0018': {
                GuiScreen.setClipboardString(this.getSelectedText());
                if (this.isEnabled) {
                    this.writeText("");
                }
                return true;
            }
            default: {
                switch (position) {
                    case 14: {
                        if (GuiScreen.isCtrlKeyDown()) {
                            if (this.isEnabled) {
                                this.deleteWords(-1);
                            }
                        }
                        else if (this.isEnabled) {
                            this.deleteFromCursor(-1);
                        }
                        return true;
                    }
                    case 199: {
                        if (GuiScreen.isShiftKeyDown()) {
                            this.setSelectionPos(0);
                        }
                        else {
                            this.setCursorPositionZero();
                        }
                        return true;
                    }
                    case 203: {
                        if (GuiScreen.isShiftKeyDown()) {
                            if (GuiScreen.isCtrlKeyDown()) {
                                this.setSelectionPos(this.getNthWordFromPos(-1));
                            }
                            else {
                                this.setSelectionPos(this.getSelectionEnd() - 1);
                            }
                        }
                        else if (GuiScreen.isCtrlKeyDown()) {
                            this.setCursorPosition(this.getNthWordFromCursor(-1));
                        }
                        else {
                            this.moveCursorBy(-1);
                        }
                        return true;
                    }
                    case 205: {
                        if (GuiScreen.isShiftKeyDown()) {
                            if (GuiScreen.isCtrlKeyDown()) {
                                this.setSelectionPos(this.getNthWordFromPos(1));
                            }
                            else {
                                this.setSelectionPos(this.getSelectionEnd() + 1);
                            }
                        }
                        else if (GuiScreen.isCtrlKeyDown()) {
                            this.setCursorPosition(this.getNthWordFromCursor(1));
                        }
                        else {
                            this.moveCursorBy(1);
                        }
                        return true;
                    }
                    case 207: {
                        if (GuiScreen.isShiftKeyDown()) {
                            this.setSelectionPos(this.text.length());
                        }
                        else {
                            this.setCursorPositionEnd();
                        }
                        return true;
                    }
                    case 211: {
                        if (GuiScreen.isCtrlKeyDown()) {
                            if (this.isEnabled) {
                                this.deleteWords(1);
                            }
                        }
                        else if (this.isEnabled) {
                            this.deleteFromCursor(1);
                        }
                        return true;
                    }
                    default: {
                        if (ChatAllowedCharacters.isAllowedCharacter(character)) {
                            if (this.isEnabled) {
                                this.writeText(Character.toString(character));
                            }
                            return true;
                        }
                        return false;
                    }
                }
            }
        }
    }

    public void mouseClicked(final int p_146192_1_, final int p_146192_2_, final int p_146192_3_) {
        final boolean flag = p_146192_1_ >= this.xPosition && p_146192_1_ < this.xPosition + this.width && p_146192_2_ >= this.yPosition && p_146192_2_ < this.yPosition + this.height;
        if (this.canLoseFocus) {
            this.setFocused(flag);
        }
        if (this.isFocused && p_146192_3_ == 0) {
            int l = p_146192_1_ - this.xPosition;
            if (this.enableBackgroundDrawing) {
                l -= 4;
            }
            final String s = this.fontRendererInstance.trimStringToWidth(this.text.substring(this.lineScrollOffset), this.getWidth());
            this.setCursorPosition(this.fontRendererInstance.trimStringToWidth(s, l).length() + this.lineScrollOffset);
        }
    }

    public void drawTextBox() {
        if (this.getVisible()) {
            if (this.getEnableBackgroundDrawing()) {
                drawRect(this.xPosition + 1, this.yPosition + 1, this.xPosition + 2, this.yPosition + this.height - 1, this.getColorWithAlpha(8103095, 255));
                drawRect(this.xPosition + 1, this.yPosition + 1, this.xPosition + 10, this.yPosition + 2, this.getColorWithAlpha(8103095, 255));
                drawRect(this.xPosition + 1, this.yPosition + this.height - 2, this.xPosition + 10, this.yPosition + this.height - 1, this.getColorWithAlpha(8103095, 255));
                drawRect(this.xPosition + this.width - 1, this.yPosition + this.height - 2, this.xPosition + this.width - 10, this.yPosition + this.height - 1, this.getColorWithAlpha(8103095, 255));
                drawRect(this.xPosition + this.width - 1, this.yPosition + 1, this.xPosition + this.width - 10, this.yPosition + 2, this.getColorWithAlpha(8103095, 255));
                drawRect(this.xPosition + this.width - 1, this.yPosition + this.height - 2, this.xPosition + this.width - 2, this.yPosition + 1, this.getColorWithAlpha(8103095, 255));
                drawRect(this.xPosition + 2, this.yPosition + 2, this.xPosition + this.width - 2, this.yPosition + this.height - 2, this.getColorWithAlpha(8168374, 100));
            }
            final int i = this.isEnabled ? this.enabledColor : this.disabledColor;
            final int j = this.cursorPosition - this.lineScrollOffset;
            int k = this.selectionEnd - this.lineScrollOffset;
            final String s = this.fontRendererInstance.trimStringToWidth(this.text.substring(this.lineScrollOffset), this.getWidth());
            final boolean flag = j >= 0 && j <= s.length();
            final boolean flag2 = this.isFocused && this.cursorCounter / 6 % 2 == 0 && flag;
            final int l = this.enableBackgroundDrawing ? (this.xPosition + 4) : this.xPosition;
            final int i2 = this.enableBackgroundDrawing ? (this.yPosition + (this.height - 8) / 2) : this.yPosition;
            int j2 = l;
            if (k > s.length()) {
                k = s.length();
            }
            if (s.length() > 0) {
                final String s2 = flag ? s.substring(0, j) : s;
                j2 = this.fontRendererInstance.drawStringWithShadow(s2, (float)l, (float)i2, i);
            }
            final boolean flag3 = this.cursorPosition < this.text.length() || this.text.length() >= this.getMaxStringLength();
            int k2 = j2;
            if (!flag) {
                k2 = ((j > 0) ? (l + this.width) : l);
            }
            else if (flag3) {
                k2 = j2 - 1;
                --j2;
            }
            if (s.length() > 0 && flag && j < s.length()) {
                this.fontRendererInstance.drawStringWithShadow(s.substring(j), (float)j2, (float)i2, i);
            }
            if (flag2) {
                if (flag3) {
                    Gui.drawRect(k2, i2 - 1, k2 + 1, i2 + 1 + this.fontRendererInstance.FONT_HEIGHT, -3092272);
                }
                else {
                    this.fontRendererInstance.drawStringWithShadow("_", (float)k2, (float)i2, i);
                }
            }
            if (k != j) {
                final int l2 = l + this.fontRendererInstance.getStringWidth(s.substring(0, k));
                this.drawCursorVertical(k2, i2 - 1, l2 - 1, i2 + 1 + this.fontRendererInstance.FONT_HEIGHT);
            }
        }
    }

    private void drawCursorVertical(int p_146188_1_, int p_146188_2_, int p_146188_3_, int p_146188_4_) {
        if (p_146188_1_ < p_146188_3_) {
            final int i1 = p_146188_1_;
            p_146188_1_ = p_146188_3_;
            p_146188_3_ = i1;
        }
        if (p_146188_2_ < p_146188_4_) {
            final int i1 = p_146188_2_;
            p_146188_2_ = p_146188_4_;
            p_146188_4_ = i1;
        }
        if (p_146188_3_ > this.xPosition + this.width) {
            p_146188_3_ = this.xPosition + this.width;
        }
        if (p_146188_1_ > this.xPosition + this.width) {
            p_146188_1_ = this.xPosition + this.width;
        }
        final Tessellator tessellator = Tessellator.getInstance();
        final WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        GlStateManager.color(0.0f, 0.0f, 255.0f, 255.0f);
        GlStateManager.disableTexture2D();
        GlStateManager.enableColorLogic();
        GlStateManager.colorLogicOp(5387);
        worldrenderer.begin(7, DefaultVertexFormats.POSITION);
        worldrenderer.pos(p_146188_1_, p_146188_4_, 0.0).endVertex();
        worldrenderer.pos(p_146188_3_, p_146188_4_, 0.0).endVertex();
        worldrenderer.pos(p_146188_3_, p_146188_2_, 0.0).endVertex();
        worldrenderer.pos(p_146188_1_, p_146188_2_, 0.0).endVertex();
        tessellator.draw();
        GlStateManager.disableColorLogic();
        GlStateManager.enableTexture2D();
    }

    /*
    public void setMaxStringLength(final int p_146203_1_) {
        this.maxStringLength = p_146203_1_;
        if (this.text.length() > p_146203_1_) {
            this.text = this.text.substring(0, p_146203_1_);
        }
    }
    */


    public int getMaxStringLength() {
        return this.maxStringLength;
    }

    public int getCursorPosition() {
        return this.cursorPosition;
    }

    public boolean getEnableBackgroundDrawing() {
        return this.enableBackgroundDrawing;
    }

    /*
    public void setEnableBackgroundDrawing(final boolean p_146185_1_) {
        this.enableBackgroundDrawing = p_146185_1_;
    }
     */

    public void setTextColor(final int p_146193_1_) {
        this.enabledColor = p_146193_1_;
    }

    public void setDisabledTextColour(final int p_146204_1_) {
        this.disabledColor = p_146204_1_;
    }

    public void setFocused(final boolean p_146195_1_) {
        if (p_146195_1_ && !this.isFocused) {
            this.cursorCounter = 0;
        }
        this.isFocused = p_146195_1_;
    }

    public boolean isFocused() {
        return this.isFocused;
    }

    public boolean isEnabled() {
        return this.isEnabled;
    }

    public void setEnabled(final boolean p_146184_1_) {
        this.isEnabled = p_146184_1_;
    }

    public int getSelectionEnd() {
        return this.selectionEnd;
    }

    public int getWidth() {
        return this.getEnableBackgroundDrawing() ? (this.width - 8) : this.width;
    }

    public void setSelectionPos(int p_146199_1_) {
        final int j = this.text.length();
        if (p_146199_1_ > j) {
            p_146199_1_ = j;
        }
        if (p_146199_1_ < 0) {
            p_146199_1_ = 0;
        }
        this.selectionEnd = p_146199_1_;
        if (this.fontRendererInstance != null) {
            if (this.lineScrollOffset > j) {
                this.lineScrollOffset = j;
            }
            final int k = this.getWidth();
            final String s = this.fontRendererInstance.trimStringToWidth(this.text.substring(this.lineScrollOffset), k);
            final int l = s.length() + this.lineScrollOffset;
            if (p_146199_1_ == this.lineScrollOffset) {
                this.lineScrollOffset -= this.fontRendererInstance.trimStringToWidth(this.text, k, true).length();
            }
            if (p_146199_1_ > l) {
                this.lineScrollOffset += p_146199_1_ - l;
            }
            else if (p_146199_1_ <= this.lineScrollOffset) {
                this.lineScrollOffset -= this.lineScrollOffset - p_146199_1_;
            }
            if (this.lineScrollOffset < 0) {
                this.lineScrollOffset = 0;
            }
            if (this.lineScrollOffset > j) {
                this.lineScrollOffset = j;
            }
        }
    }

    public void setCanLoseFocus(final boolean p_146205_1_) {
        this.canLoseFocus = p_146205_1_;
    }

    public boolean getVisible() {
        return this.visible;
    }

    public void setVisible(final boolean p_146189_1_) {
        this.visible = p_146189_1_;
    }

    private int getColorWithAlpha(final int rgb, final int a) {
        final int r = rgb >> 16 & 0xFF;
        final int g = rgb >> 8 & 0xFF;
        final int b = rgb & 0xFF;
        return a << 24 | r << 16 | g << 8 | b;
    }
}
