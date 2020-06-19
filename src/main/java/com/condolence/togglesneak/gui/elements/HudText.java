package com.condolence.togglesneak.gui.elements;

import java.awt.*;

public class HudText {
    private String description;
    private String hudText;
    private int hudColor;
    private boolean chroma;

    public HudText(final String hudText, final String description, final int hudColor) {
        this.hudText = hudText;
        this.hudColor = hudColor;
        this.description = description;
    }

    public String getText() { return this.hudText; }
    public void setText(final String text) { this.hudText = text; }

    public int getColor() {
        return this.hudColor;
    }

    public void setColor(final int hudColor) { this.hudColor = hudColor; }

    public String getDescription() { return this.description; }

    public boolean isChroma() { return this.chroma; }

    public void setChroma(final boolean chroma) { this.chroma = chroma; }
}
