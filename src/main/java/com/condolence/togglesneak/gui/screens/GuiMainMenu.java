package com.condolence.togglesneak.gui.screens;

import com.condolence.togglesneak.ToggleSneakMod;
import com.condolence.togglesneak.config.ToggleSneakSettings;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fml.client.config.GuiSlider;

public class GuiMainMenu extends GuiScreen {
    private final ToggleSneakMod mod;
    private final ToggleSneakSettings settings;

    // GUI VARIABLES
    private GuiButton hudToggleButton;
    private GuiButton hudPosButton;
    private GuiButton toggleSprintButton;
    private GuiButton toggleSneakButton;
    private GuiButton hudTextButton;
    private GuiButton toggleFlyBoostButton;
    private GuiSlider flyBoostAmountSlider;

    // GUI LABEL VARIABLES
    private final EnumChatFormatting greenTextColor = EnumChatFormatting.GREEN;
    private final EnumChatFormatting redTextColor = EnumChatFormatting.RED;

    // MAIN GUI METHODS
    public GuiMainMenu(final ToggleSneakMod mod) {
        this.mod = mod;
        this.settings = this.mod.getSettings();
    }

    public void initGui() {
        // CREATE TEXT STRINGS
        final String mainToggleText = "Mod State: " + (this.settings.isModEnabled() ? greenTextColor + "Enabled" : redTextColor + "Disabled");
        final String hudToggleText = "HUD: " + (this.settings.isHudShown() ? greenTextColor + "Shown" : redTextColor + "Hidden");
        final String toggleSprintText = "Toggle Sprint: " + (this.settings.isToggleSprintEnabled() ? greenTextColor + "Enabled" : redTextColor + "Disabled");
        final String toggleSneakText = "Toggle Sneak: " + (this.settings.isToggleSneakEnabled() ? greenTextColor + "Enabled" : redTextColor + "Disabled");
        final String toggleFlyText = "Fly Boost: " + (this.settings.isFlyBoostEnabled() ? greenTextColor + "Enabled" : redTextColor + "Disabled");

        final String hudPosLabel = "Show HUD Position";
        final String hudTextLabel = "Edit HUD Text";
        final String flyBoostAmountLabel = "Fly Boost Amount: ";

        // CREATE BUTTONS
        final GuiButton modToggleButton = new GuiButton(0, this.getCenter() - 75, this.getRowPos(1), 150, 20, mainToggleText);
        this.hudToggleButton = new GuiButton(1, this.getCenter() - 75, this.getRowPos(4), 150, 20, hudToggleText);
        this.hudPosButton = new GuiButton(2, this.getCenter() - 153, this.getRowPos(5), 150, 20, hudPosLabel);
        this.hudTextButton = new GuiButton(3, this.getCenter() + 2, this.getRowPos(5), 150, 20, hudTextLabel);
        this.toggleSprintButton = new GuiButton(4, this.getCenter() - 153, this.getRowPos(2), 150, 20, toggleSprintText);
        this.toggleSneakButton = new GuiButton(5, this.getCenter() + 2, this.getRowPos(2), 150, 20, toggleSneakText);
        this.toggleFlyBoostButton = new GuiButton(6, this.getCenter() - 75, this.getRowPos(7), 150, 20, toggleFlyText);
        this.flyBoostAmountSlider = new GuiSlider(7, this.getCenter() - 150, this.getRowPos(8), 300, 20, flyBoostAmountLabel, "", 1.0f, 10.0f, this.settings.getFlyBoostAmount(), false, true);

        // ADD BUTTONS TO BUTTON LIST
        this.buttonList.add(modToggleButton);
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
        // DRAW BACKGROUND
        super.drawDefaultBackground();

        // DRAW TEXT
        this.drawCenteredString(this.mc.fontRendererObj, "ToggleSneak", this.getCenter(), 20, 2201331);
        this.drawCenteredString(this.mc.fontRendererObj, this.mod.getVersion(), this.getCenter(), 30, 3162015);

        // SET FLY BOOST AMOUNT
        this.settings.setFlyBoostAmount(this.flyBoostAmountSlider.getValue());

        // DRAW SCREEN
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    protected void actionPerformed(final GuiButton button) {
        switch (button.id) {
            case 0: {
                final boolean modEnabled = !this.settings.isModEnabled();
                this.settings.setModEnabled(modEnabled);
                button.displayString = "Mod State: " + (modEnabled ? greenTextColor + "Enabled" : redTextColor + "Disabled");
                break;
            }
            case 1: {
                final boolean hudShown =  !this.settings.isHudShown();
                this.settings.setHudShown(hudShown);
                button.displayString = "HUD: " + (hudShown ? greenTextColor + "Shown" : redTextColor + "Hidden");
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
                final boolean toggleSprintEnabled = !this.settings.isToggleSprintEnabled();
                this.settings.setToggleSprintEnabled(toggleSprintEnabled);
                button.displayString = "Toggle Sprint: " + (toggleSprintEnabled ? greenTextColor + "Enabled" : redTextColor + "Disabled");
                break;
            }
            case 5: {
                final boolean toggleSneakEnabled = !this.settings.isToggleSneakEnabled();
                this.settings.setToggleSneakEnabled(toggleSneakEnabled);
                button.displayString = "Toggle Sneak: " + (toggleSneakEnabled ? greenTextColor + "Enabled" : redTextColor + "Disabled");
                break;
            }
            case 6: {
                final boolean flyBoostEnabled = !this.settings.isFlyBoostEnabled();
                this.settings.setFlyBoostEnabled(flyBoostEnabled);
                button.displayString = "Fly Boost: " + (flyBoostEnabled ? greenTextColor + "Enabled" : redTextColor + "Disabled");
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

    public void onGuiClosed() {
        this.settings.saveConfig();
    }
}
