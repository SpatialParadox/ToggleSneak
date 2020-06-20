package com.condolence.togglesneak.gui.screens;

import com.condolence.togglesneak.ToggleSneakMod;
import com.condolence.togglesneak.config.ToggleSneakSettings;
import com.condolence.togglesneak.gui.elements.GuiColorPicker;
import com.condolence.togglesneak.gui.elements.GuiTransButton;
import com.condolence.togglesneak.gui.elements.GuiTransTextField;
import com.condolence.togglesneak.gui.elements.HudText;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.EnumChatFormatting;
import org.lwjgl.opengl.GL11;

import java.io.IOException;
import java.util.Arrays;

public class GuiChangeTextAndColor extends GuiScreen {
    private final ToggleSneakMod mod;

    // GUI VARIABLES
    private GuiButton chromaButton;
    private GuiColorPicker colorPicker;
    private GuiTextField textEditor;

    private final HudText[] allHudTexts;
    private HudText focusedHudText;

    // GUI TEXT VARIABLES
    private final EnumChatFormatting greenTextColor = EnumChatFormatting.GREEN;
    private final EnumChatFormatting redTextColor = EnumChatFormatting.RED;


    public GuiChangeTextAndColor(final ToggleSneakMod mod) {
        this.mod = mod;

        final ToggleSneakSettings settings = this.mod.getSettings();
        this.allHudTexts = settings.getAllHudTexts();
        this.focusedHudText = this.allHudTexts[0];
    }

    public int getRowPos(final int rowNumber) {
        return this.height / 4 + (24 * rowNumber - 24) - 16;
    }

    public int getCenter() {
        return this.width / 2;
    }

    public void initGui() {
        final String chromaText = "Chroma: " + (this.focusedHudText.isChroma() ? greenTextColor + "Enabled" : redTextColor + "Disabled");

        // Create GUI elements
        this.textEditor = new GuiTextField(105, this.fontRendererObj, this.getCenter() - 75, this.getRowPos(2), 140, 20);
        //this.textEditor = new GuiTransTextField(this.fontRendererObj, this.getCenter() - 65, this.getRowPos(2), 140, 20);
        this.colorPicker = new GuiColorPicker(0, 0, 0, "", this.getCenter() - 50, this.getRowPos(3), this.focusedHudText.getColor());

        final GuiButton previousButton = new GuiButton(101, this.getCenter() - 100, this.getRowPos(2), 20, 20, "<");
        final GuiButton nextButton = new GuiButton(102, this.getCenter() + 90, this.getRowPos(2), 20, 20, ">");
        final GuiButton syncColorButton = new GuiButton(103, this.getCenter() - 175, this.getRowPos(5) - 5, 120, 20, "Apply color to all text");
        this.chromaButton = new GuiButton(104, this.getCenter() + 55, this.getRowPos(5) - 5, 120, 20, chromaText);
        final GuiButton saveButton = new GuiButton(105, this.getCenter() - 67, this.height - 20, 140, 20, "Save");

        this.textEditor.setText(this.focusedHudText.getText());

        // Add to button list
        this.buttonList.add(this.colorPicker);
        this.buttonList.add(previousButton);
        this.buttonList.add(nextButton);
        this.buttonList.add(syncColorButton);
        this.buttonList.add(this.chromaButton);
        this.buttonList.add(saveButton);
    }

    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        super.drawDefaultBackground();
        this.updateSettings();
        this.textEditor.drawTextBox();
        this.textEditor.setTextColor(this.focusedHudText.getColor());
        this.renderUI();
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    protected void actionPerformed(final GuiButton button) {
        switch (button.id) {
            // Previous text button
            case 101: {
                final int currentHudTextIndex = Arrays.asList(this.allHudTexts).indexOf(this.focusedHudText);
                if (currentHudTextIndex - 1 < 0) {
                    this.focusedHudText = this.allHudTexts[this.allHudTexts.length - 1];
                }
                else {
                    this.focusedHudText = this.allHudTexts[currentHudTextIndex - 1];
                }
                this.colorPicker.setPickerColor(this.focusedHudText.getColor());
                this.textEditor.setText(this.focusedHudText.getText());
                this.textEditor.setTextColor(this.colorPicker.getPickedColor());
                break;
            }
            // Next text button
            case 102: {
                final int currentHudTextIndex = Arrays.asList(this.allHudTexts).indexOf(this.focusedHudText);
                if (currentHudTextIndex == this.allHudTexts.length - 1) {
                    this.focusedHudText = this.allHudTexts[0];
                }
                else {
                    this.focusedHudText = this.allHudTexts[currentHudTextIndex + 1];
                }
                this.colorPicker.setPickerColor(this.focusedHudText.getColor());
                this.textEditor.setText(this.focusedHudText.getText());
                this.textEditor.setTextColor(this.colorPicker.getPickedColor());
                break;
            }
            // Sync color button
            case 103: {
                for (final HudText hudText : this.allHudTexts) {
                    hudText.setChroma(this.focusedHudText.isChroma());
                    hudText.setColor(this.focusedHudText.getColor());
                }
                break;
            }
            // Chroma button
            case 104: {
                final boolean chromaEnabled = !this.focusedHudText.isChroma();
                this.focusedHudText.setChroma(chromaEnabled);
                this.chromaButton.displayString = "Chroma: " + (chromaEnabled ? greenTextColor + "Enabled" : redTextColor + "Disabled");
                break;
            }
            // Save button
            case 105: {
                this.mc.displayGuiScreen(new GuiMainMenu(this.mod));
                break;
            }
        }
    }

    protected void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        final GuiTextField target = this.textEditor;
        if (mouseX > target.xPosition && mouseX < target.xPosition + target.width && mouseY > target.yPosition && mouseY < target.yPosition + target.height && target.getVisible()) {
            this.textEditor.mouseClicked(mouseX, mouseY, mouseButton);
        }
        else {
            this.textEditor.setFocused(false);
        }
    }

    protected void keyTyped(final char par1, final int par2) throws IOException {
        super.keyTyped(par1, par2);
        this.textEditor.textboxKeyTyped(par1, par2);
    }

    private void updateSettings() {
        this.focusedHudText.setColor(this.colorPicker.getPickedColor());
        this.focusedHudText.setText(this.textEditor.getText());
    }

    protected void renderUI() {
        final boolean renderFancier = (this.width >= 640 && this.height >= 350);
        final float f2 = 0.4862745f;
        final float f3 = 0.6392157f;
        final float f4 = 0.7137255f;
        GL11.glPushMatrix();
        GL11.glTranslatef(1.0f * -this.getCenter(), -25.0f, 0.0f);
        GL11.glScaled(2.0, 2.0, 2.0);
        this.drawCenteredString(this.fontRendererObj, "ToggleSneak", this.getCenter(), 25, 815000);
        GL11.glPopMatrix();
        this.drawCenteredString(this.fontRendererObj, this.focusedHudText.getDescription(), this.getCenter(), this.getRowPos(1) + 10, -1);
        if (renderFancier) {
            GL11.glPushMatrix();
            GL11.glScaled(1.0, 1.0, 0.0);
            this.drawString(this.mc.fontRendererObj, "1.8.9 - 3.0", 2, 2, -1);
            GL11.glPopMatrix();
            GL11.glPushMatrix();
            GL11.glEnable(3042);
            GL11.glDisable(3553);
            GL11.glBlendFunc(770, 771);
            GL11.glColor4f(f2, f3, f4, 0.3f);

            // RENDER
            final Tessellator tessellator = Tessellator.getInstance();
            final WorldRenderer worldRenderer = tessellator.getWorldRenderer();
            worldRenderer.begin(4, DefaultVertexFormats.POSITION);
            worldRenderer.pos(0.0, 0.0, 0.0).endVertex();
            worldRenderer.pos(0.0, 140.0, 0.0).endVertex();
            worldRenderer.pos(140.0, 0.0, 0.0).endVertex();
            tessellator.draw();

            GL11.glEnable(3553);
            GL11.glDisable(3042);
            GL11.glPopMatrix();
            GL11.glPushMatrix();
            GL11.glEnable(3042);
            GL11.glDisable(3553);
            GL11.glBlendFunc(770, 771);
            GL11.glColor4f(f2, f3, f4, 0.3f);

            // RENDER (again)
            final Tessellator tessellator2 = Tessellator.getInstance();
            final WorldRenderer worldRenderer2 = tessellator2.getWorldRenderer();
            worldRenderer2.begin(4, DefaultVertexFormats.POSITION);
            worldRenderer2.pos(this.width, this.height, 0.0).endVertex();
            worldRenderer2.pos(this.width, (this.height - 140), 0.0).endVertex();
            worldRenderer2.pos((this.width - 140), this.height, 0.0).endVertex();
            tessellator2.draw();

            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
            GL11.glEnable(3553);
            GL11.glDisable(3042);
            GL11.glPopMatrix();
            GlStateManager.pushMatrix();
            GlStateManager.enableBlend();
            GlStateManager.enableAlpha();
            GlStateManager.popMatrix();
        }
    }

    public void onGuiClosed() {
        this.mod.getSettings().saveConfig();
    }
}
