package com.condolence.togglesneak.config;

import com.condolence.togglesneak.gui.elements.HudText;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

import java.io.File;
import java.util.HashMap;

public class ToggleSneakSettings {
    private final Minecraft mc;

    private final File config;

    private boolean sneakToggled;
    private boolean sprintToggled;

    private boolean modEnabled;
    private boolean toggleSprintEnabled;
    private boolean toggleSneakEnabled;
    private boolean flyBoostEnabled;
    private double flyBoostAmount;
    private boolean hudShown;
    private int hudX;
    private int hudY;

    private final HashMap<String, HudText> HudTexts = new HashMap<>();

    public ToggleSneakSettings() {
        this.mc = Minecraft.getMinecraft();
        this.config = new File("./config/togglesneak.cfg");

        this.modEnabled = true;
        this.hudShown = true;
        this.toggleSprintEnabled = true;
        this.toggleSneakEnabled = false;
        this.hudY = 1;
        this.hudX = 1;
        this.flyBoostAmount = 4.0;

        HudTexts.put("SprintVanillaText", new HudText("[Sprinting (Vanilla)]", "Sprinting (Vanilla)", -1));
        HudTexts.put("SprintToggledText", new HudText("[Sprinting (Toggled)]", "Sprinting (Toggled)", -1));

        HudTexts.put("SneakVanillaText", new HudText("[Sneaking (Vanilla)]", "Sneaking (Vanilla)", -1));
        HudTexts.put("SneakToggledText", new HudText("[Sneaking (Toggled)]", "Sneaking (Toggled)", -1));

        HudTexts.put("FlyingText", new HudText("[Flying]", "Flying", -1));
        HudTexts.put("FlyingBoostText", new HudText("[Flying (x{AMOUNT} boost)]", "Flying (Boost)", -1));

        HudTexts.put("RidingText", new HudText("[Riding]", "Riding", -1));
    }

    public void loadConfig() {
        final Configuration config = new Configuration(this.config);
        config.load();
        updateConfig(config, false);
        config.save();
    }

    public void saveConfig() {
        final Configuration config = new Configuration(this.config);
        updateConfig(config, true);
        config.save();
    }

    private void updateConfig(final Configuration config, final boolean save) {
        // Get config properties
        Property modEnabled = config.get("general", "enabled", true);
        Property hudShown = config.get("general", "hudShown", true);
        Property flyBoostAmount = config.get("general", "flyboostamount", 4.0);

        Property toggleSprintEnabled = config.get("features", "sprint", true);
        Property toggleSneakEnabled = config.get("features", "sneak", false);
        Property flyBoostEnabled = config.get("features", "flyboost", true);

        if (save) {
            modEnabled.set(this.modEnabled);
            hudShown.set(this.hudShown);
            flyBoostAmount.set(this.flyBoostAmount);

            toggleSprintEnabled.set(this.toggleSprintEnabled);
            toggleSneakEnabled.set(this.toggleSneakEnabled);
            flyBoostEnabled.set(this.flyBoostEnabled);
        } else {
            this.modEnabled = modEnabled.getBoolean();
            this.hudShown = hudShown.getBoolean();
            this.flyBoostAmount = flyBoostAmount.getDouble();

            this.toggleSprintEnabled = toggleSprintEnabled.getBoolean();
            this.toggleSneakEnabled = toggleSneakEnabled.getBoolean();
            this.flyBoostEnabled = flyBoostEnabled.getBoolean();
        }

        // Get/save text config properties
        for (final HudText current : this.getAllHudTexts()) {
            Property textProperty = config.get(current.getDescription(), "text", current.getText());
            Property colorProperty = config.get(current.getDescription(), "color", current.getColor());
            Property chromaProperty = config.get(current.getDescription(), "chroma", current.isChroma());

            if (save) {
                textProperty.set(current.getText());
                colorProperty.set(current.getColor());
                chromaProperty.set(current.isChroma());
            } else {
                current.setText(textProperty.getString());
                current.setColor(colorProperty.getInt());
                current.setChroma(chromaProperty.getBoolean());
            }
        }
    }

    public void updateHudPosition(final int addX, final int addY) {
        this.hudX += addX;
        this.hudY += addY;
    }

    public boolean contains(final int x, final int y) {
        return x > this.hudX - 3 && x < this.hudX + this.mc.fontRendererObj.getStringWidth(this.getSpecificHudText("SprintToggledText").getText()) && y > this.hudY && y < this.hudY + this.mc.fontRendererObj.FONT_HEIGHT;
    }

    // GET/SET METHODS
    public HudText getHudText() {
        if (!this.hudShown) { return null; }

        final EntityPlayerSP player = this.mc.thePlayer;
        if (player == null) { return null; }

        if (player.capabilities.isFlying) {
            if (this.flyBoostEnabled && this.mc.gameSettings.keyBindSprint.isKeyDown() && player.capabilities.isCreativeMode) {
                return this.getSpecificHudText("FlyingBoostText");
            }

            return this.getSpecificHudText("FlyingText");
        } else {
            if (player.isRiding()) { return this.getSpecificHudText("RidingText"); }

            if (this.sneakToggled) { return this.getSpecificHudText("SneakToggledText"); }
            if (this.sprintToggled) { return this.getSpecificHudText("SprintToggledText"); }

            if (player.isSneaking()) { return this.getSpecificHudText("SneakVanillaText"); }
            if (player.isSprinting()) { return this.getSpecificHudText("SprintVanillaText"); }

            return null;
        }
    }

    public HudText getSpecificHudText(final String textName) { return HudTexts.get(textName); }
    public HudText[] getAllHudTexts() {
        return new HudText[] { HudTexts.get("SprintVanillaText"), HudTexts.get("SprintToggledText"), HudTexts.get("SneakVanillaText"), HudTexts.get("SneakToggledText"), HudTexts.get("FlyingText"), HudTexts.get("FlyingBoostText"), HudTexts.get("RidingText") };
    }

    public boolean isModEnabled() { return this.modEnabled; }
    public void setModEnabled(final boolean modEnabled) { this.modEnabled = modEnabled; }

    public boolean isHudShown() { return this.hudShown; }
    public void setHudShown(final boolean hudShown) { this.hudShown = hudShown; }

    public int getHudX() { return this.hudX; }
    public int getHudY() { return this.hudY; }

    public boolean isSprintToggled() { return this.sprintToggled; }
    public void setSprintToggled(final boolean sprintToggled) { this.sprintToggled = sprintToggled; }

    public boolean isSneakToggled() { return this.sneakToggled; }
    public void setSneakToggled(final boolean sneakToggled) { this.sneakToggled = sneakToggled; }

    public boolean isToggleSprintEnabled() { return this.toggleSprintEnabled; }
    public void setToggleSprintEnabled(final boolean toggleSprintEnabled) { this.toggleSprintEnabled = toggleSprintEnabled; }

    public boolean isToggleSneakEnabled() { return this.toggleSneakEnabled; }
    public void setToggleSneakEnabled(final boolean toggleSneakEnabled) { this.toggleSneakEnabled = toggleSneakEnabled; }

    public boolean isFlyBoostEnabled() { return this.flyBoostEnabled; }
    public void setFlyBoostEnabled(final boolean flyBoostEnabled) { this.flyBoostEnabled = flyBoostEnabled; }

    public double getFlyBoostAmount() { return this.flyBoostAmount; }
    public void setFlyBoostAmount(final double flyBoostAmount) { this.flyBoostAmount = flyBoostAmount; }
}
