package com.condolence.togglesneak.gui.screens;

import com.condolence.togglesneak.ToggleSneakMod;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiIngameMenu;

import java.io.IOException;

public class GuiOptionsReplace extends GuiIngameMenu {
    private final ToggleSneakMod mod;

    public GuiOptionsReplace(final ToggleSneakMod mod) {
        this.mod = mod;
    }

    public void initGui() {
        super.initGui();
        final GuiButton btnToggleSneakOptions = new GuiButton(9999, this.width - 85, 5, 80, 20, "ToggleSneak");
        this.buttonList.add(btnToggleSneakOptions);
    }

    protected void actionPerformed(final GuiButton buttonPressed) throws IOException {
        super.actionPerformed(buttonPressed);

        if (buttonPressed.id == 9999) {
            this.mc.displayGuiScreen(new GuiMainMenu(this.mod));
        }
    }
}
