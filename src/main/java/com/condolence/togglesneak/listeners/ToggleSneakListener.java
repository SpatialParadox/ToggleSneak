package com.condolence.togglesneak.listeners;

import com.condolence.togglesneak.ToggleSneakMod;
import com.condolence.togglesneak.config.ToggleSneakSettings;
import com.condolence.togglesneak.gui.elements.HudRenderer;
import com.condolence.togglesneak.gui.screens.GuiMainMenu;
import com.condolence.togglesneak.gui.screens.GuiOptionsReplace;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;

public class ToggleSneakListener {
    private final ToggleSneakMod mod;
    private final Minecraft mc;
    private final ToggleSneakSettings settings;
    private final HudRenderer hudRenderer;

    private boolean sprintToggled = false;
    private boolean sneakToggled = false;
    private boolean openMenu = false;

    public ToggleSneakListener(final ToggleSneakMod mod) {
        this.mod = mod;
        this.mc = Minecraft.getMinecraft();
        this.settings = this.mod.getSettings();
        this.hudRenderer = this.mod.getHudRenderer();
    }

    // Event which handles the rendering of the HUD text
    @SubscribeEvent
    public void onRenderTick(final TickEvent.RenderTickEvent e) {
        if (!this.settings.isModEnabled() || this.mc.theWorld == null) { return; }
        if (this.mc.gameSettings.showDebugInfo) { return; }

        hudRenderer.renderHud();
    }

    // Event which handles the toggling of the toggle sprint variable
    @SubscribeEvent
    public void onKeyInput(final InputEvent.KeyInputEvent event) {
        if (!Keyboard.isCreated()) { return; }

        final KeyBinding sprintKeyBind = this.mc.gameSettings.keyBindSprint;
        final KeyBinding sneakKeyBind = this.mc.gameSettings.keyBindSneak;

        if (sprintKeyBind.isPressed() && settings.isToggleSprintEnabled()) {
            this.settings.setSprintToggled(!this.settings.isSprintToggled());
        }

        if (sneakKeyBind.isPressed() && settings.isToggleSneakEnabled()) {
            this.settings.setSneakToggled(!this.settings.isSneakToggled());
        }
    }

    // Event which checks if the sprint key is toggled and toggles the sprint according to the value.
    @SubscribeEvent
    public void onTick(final TickEvent.ClientTickEvent event) {
        if (this.openMenu) {
            this.openMenu = false;
            this.mc.displayGuiScreen(new GuiMainMenu(this.mod));
        }

        final EntityPlayerSP player = this.mc.thePlayer;

        if (player == null || this.mc.theWorld == null) { return; }

        final KeyBinding sprintKeyBind = this.mc.gameSettings.keyBindSprint;
        final KeyBinding sneakKeyBind = this.mc.gameSettings.keyBindSneak;

        if (!this.settings.isModEnabled()) {
            if (this.sprintToggled) {
                this.sprintToggled = false;
                KeyBinding.setKeyBindState(sprintKeyBind.getKeyCode(), false);
            }

            if (this.sneakToggled) {
                this.sneakToggled = false;
                KeyBinding.setKeyBindState(sneakKeyBind.getKeyCode(), false);
            }

            return;
        }

        if (this.settings.isFlyBoostEnabled() && player.capabilities.isFlying && sprintKeyBind.isKeyDown() && player.capabilities.isCreativeMode) {
            final double flyBoostAmount = this.settings.getFlyBoostAmount();
            player.capabilities.setFlySpeed(0.05f * (float)flyBoostAmount);

            if (player.movementInput.sneak) { player.motionY -= 0.15 * flyBoostAmount; }
            if (player.movementInput.jump) { player.motionY += 0.15 * flyBoostAmount; }

            return;
        }

        if (player.capabilities.getFlySpeed() != 0.05f) {
            this.mc.thePlayer.capabilities.setFlySpeed(0.05f);
        }

        if (this.settings.isToggleSprintEnabled()) {
            final int sprintKeyCode = sprintKeyBind.getKeyCode();

            if(this.settings.isSprintToggled()) {
                this.sprintToggled = true;
                KeyBinding.setKeyBindState(sprintKeyCode, true);
            } else {
                this.sprintToggled = false;
                if (sprintKeyCode > 0) {
                    KeyBinding.setKeyBindState(sprintKeyCode, Keyboard.isKeyDown(sprintKeyCode));
                }
            }
        }

        if (this.settings.isToggleSneakEnabled()) {
            final int sneakKeyCode = sneakKeyBind.getKeyCode();

            if (this.settings.isSneakToggled()) {
                this.sneakToggled = true;
                KeyBinding.setKeyBindState(sneakKeyCode, true);
            } else {
                this.sneakToggled = false;
                if (sneakKeyCode > 0 ) {
                    KeyBinding.setKeyBindState(sneakKeyCode, Keyboard.isKeyDown(sneakKeyCode) && this.mc.currentScreen == null);
                }
            }
        }
    }

    @SubscribeEvent
    public void GuiOpenEvent(final GuiOpenEvent event) {
        if (event.gui instanceof GuiIngameMenu && this.mc.theWorld != null) {
            event.gui = new GuiOptionsReplace(this.mod);
        }
    }

    public void setOpenMenu(final boolean value) {
        this.openMenu = value;
    }
}
