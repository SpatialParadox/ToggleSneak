package com.condolence.togglesneak.gui.screens;

import com.condolence.togglesneak.ToggleSneakMod;
import com.condolence.togglesneak.config.ToggleSneakSettings;
import com.condolence.togglesneak.gui.elements.GuiSlideControl;
import com.condolence.togglesneak.gui.elements.GuiTransButton;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.EnumChatFormatting;
import org.lwjgl.opengl.GL11;

public class GuiMainMenu extends GuiScreen {
    private ToggleSneakMod mod;
    private ToggleSneakSettings settings;

    private boolean renderFancier;

    // GUI VARIABLES
    private GuiTransButton modToggleButton;
    private GuiTransButton hudToggleButton;
    private GuiTransButton hudPosButton;
    private GuiTransButton toggleSprintButton;
    private GuiTransButton toggleSneakButton;
    private GuiTransButton hudTextButton;
    private GuiTransButton toggleFlyBoostButton;
    private GuiSlideControl flyBoostAmountSlider;

    // GUI LABEL VARIABLES
    private final EnumChatFormatting greenTextColor = EnumChatFormatting.GREEN;
    private final EnumChatFormatting redTextColor = EnumChatFormatting.RED;

    private final String modStateLabel = "Mod State: %s";
    private final String hudToggleLabel = "HUD: %s";
    private final String hudPosLabel = "Show HUD Position";
    private final String hudTextLabel = "Edit HUD Text";
    private final String toggleSprintLabel = "Toggle Sprint: %s";
    private final String toggleSneakLabel = "Toggle Sneak: %s";
    private final String toggleFlyLabel = "Fly Boost: %s";
    private final String flyBoostAmountLabel = "Fly Boost Amount: ";

    // MAIN GUI METHODS
    public GuiMainMenu(final ToggleSneakMod mod) {
        this.mod = mod;
        this.settings = this.mod.getSettings();
    }

    public void initGui() {
        final String mainToggleText = String.format(modStateLabel, this.settings.isModEnabled() ? greenTextColor + "Enabled" : redTextColor + "Disabled");
        final String hudToggleText = String.format(hudToggleLabel, this.settings.isHudShown() ? greenTextColor + "Shown" : redTextColor + "Hidden");
        final String toggleSprintText = String.format(toggleSprintLabel, this.settings.isToggleSprintEnabled() ? greenTextColor + "Enabled" : redTextColor + "Disabled");
        final String toggleSneakText = String.format(toggleSneakLabel, this.settings.isToggleSneakEnabled() ? greenTextColor + "Enabled" : redTextColor + "Disabled");
        final String toggleFlyText = String.format(toggleFlyLabel, this.settings.isFlyBoostEnabled() ? greenTextColor + "Enabled" : redTextColor + "Disabled");

        // CREATE BUTTONS
        this.modToggleButton = new GuiTransButton(0, this.getCenter() - 75, this.getRowPos(1), 150, 20, mainToggleText);
        this.hudToggleButton = new GuiTransButton(1, this.getCenter() - 75, this.getRowPos(4), 150, 20, hudToggleText);
        this.hudPosButton = new GuiTransButton(2, this.getCenter() - 153, this.getRowPos(5), 150, 20, hudPosLabel);
        this.hudTextButton = new GuiTransButton(3, this.getCenter() + 2, this.getRowPos(5), 150, 20, hudTextLabel);
        this.toggleSprintButton = new GuiTransButton(4, this.getCenter() - 153, this.getRowPos(2), 150, 20, toggleSprintText);
        this.toggleSneakButton = new GuiTransButton(5, this.getCenter() + 2, this.getRowPos(2), 150, 20, toggleSneakText);
        this.toggleFlyBoostButton = new GuiTransButton(6, this.getCenter() - 75, this.getRowPos(7), 150, 20, toggleFlyText);
        this.flyBoostAmountSlider = new GuiSlideControl(7, this.getCenter() - 150, this.getRowPos(8), 300, 20, flyBoostAmountLabel, 1.0f, 10.0f, (float)this.settings.getFlyBoostAmount(), true);

        // ADD BUTTONS TO BUTTON LIST
        this.buttonList.add(this.modToggleButton);
        this.buttonList.add(this.hudToggleButton);
        this.buttonList.add(this.hudPosButton);
        this.buttonList.add(this.hudTextButton);
        this.buttonList.add(this.toggleSprintButton);
        this.buttonList.add(this.toggleSneakButton);
        this.buttonList.add(this.toggleFlyBoostButton);
        this.buttonList.add(this.flyBoostAmountSlider);

        this.updateButtonStates();
    }

    public int getRowPos(final int rowNumber) {
        return (this.height / 4) + (24 * rowNumber - 24) - 16;
    }
    public int getCenter() {
        return this.width / 2 ;
    }


    // GUI RENDERING METHODS
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        super.drawDefaultBackground();
        this.renderUI();
        this.settings.setFlyBoostAmount(this.flyBoostAmountSlider.getFloat());
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    protected void actionPerformed(final GuiButton button) {
        switch (button.id) {
            case 0: {
                this.settings.setModEnabled(!this.settings.isModEnabled());
                button.displayString = String.format(modStateLabel, this.settings.isModEnabled() ? greenTextColor + "Enabled" : redTextColor + "Disabled");
                break;
            }
            case 1: {
                this.settings.setHudShown(!this.settings.isHudShown());
                button.displayString = String.format(hudToggleLabel, this.settings.isHudShown() ? greenTextColor + "Shown" : redTextColor + "Hidden");
                break;
            }
            case 2: {
                this.mc.displayGuiScreen(new GuiHudPositionEditor(this.mod));
                break;
            }
            case 3: {
                this.mc.displayGuiScreen(new GuiChangeTextAndColor(this.mod));
                break;
            }
            case 4: {
                this.settings.setToggleSprintEnabled(!this.settings.isToggleSprintEnabled());
                button.displayString = String.format(toggleSprintLabel, this.settings.isToggleSprintEnabled() ? greenTextColor + "Enabled" : redTextColor + "Disabled");
                break;
            }
            case 5: {
                this.settings.setToggleSneakEnabled(!this.settings.isToggleSneakEnabled());
                button.displayString = String.format(toggleSneakLabel, this.settings.isToggleSneakEnabled() ? greenTextColor + "Enabled" : redTextColor + "Disabled");
                break;
            }
            case 6: {
                this.settings.setFlyBoostEnabled(!this.settings.isFlyBoostEnabled());
                button.displayString = String.format(toggleFlyLabel, this.settings.isFlyBoostEnabled() ? greenTextColor + "Enabled" : redTextColor + "Disabled");
                break;
            }
        }

        this.updateButtonStates();
    }

    private void updateButtonStates() {
        final boolean modEnabled = this.settings.isModEnabled();
        final boolean hudShown = this.settings.isHudShown();
        final boolean flyBoostEnabled = this.settings.isFlyBoostEnabled();

        this.toggleFlyBoostButton.visible = modEnabled;
        this.flyBoostAmountSlider.visible = modEnabled;

        this.hudTextButton.visible = modEnabled;
        this.hudPosButton.visible = modEnabled;
        this.hudToggleButton.visible = modEnabled;

        this.toggleSneakButton.enabled = modEnabled;
        this.toggleSprintButton.enabled = modEnabled;

        this.hudPosButton.enabled = hudShown;
        this.hudTextButton.enabled = hudShown;

        this.flyBoostAmountSlider.enabled = flyBoostEnabled;
    }

    protected void renderUI() {
        this.renderFancier = (this.width >= 640 && this.height >= 350);

        final float f2 = 0.4862745f;
        final float f3 = 0.6392157f;
        final float f4 = 0.7137255f;

        GL11.glPushMatrix();
        GL11.glTranslatef(1.0f * -this.getCenter(), -25.0f, 0.0f);
        GL11.glScaled(2.0, 2.0, 2.0);

        // DRAW TEXT
        this.drawCenteredString(this.mc.fontRendererObj, "ToggleSneak", this.getCenter(), 20, 815000);

        GL11.glPopMatrix();

        if (this.renderFancier) {
            GL11.glPushMatrix();
            GL11.glScaled(1.0, 1.0, 0.0);

            // DRAW TEXT
            this.drawString(this.mc.fontRendererObj, "1.8.9 - 1.0", 2, 2, -1);

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
        this.settings.saveConfig();
    }
}
