package io.github.hhui64.PixelmonInfoPlus.gui.ivev;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.client.gui.GuiHelper;
import com.pixelmonmod.pixelmon.client.gui.GuiPixelmonOverlay;
import com.pixelmonmod.pixelmon.client.gui.GuiResources;
import com.pixelmonmod.pixelmon.entities.pixelmon.Entity1Base;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.EVStore;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.IVStore;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.StatsType;
import com.pixelmonmod.pixelmon.enums.EnumType;
import io.github.hhui64.PixelmonInfoPlus.PixelmonInfoPlus;
import io.github.hhui64.PixelmonInfoPlus.hotkey.HotKey;
import io.github.hhui64.PixelmonInfoPlus.pixelmon.SlotApi;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.renderer.GlStateManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Keyboard;

import java.io.IOException;

public class IVEVGuiContainer extends GuiContainer {
    private static final Logger logger = LogManager.getLogger("IVEVGuiContainer");
    /**
     * flag: GUI 是否已打开
     */
    public static Boolean isOpen = false;
    private static final ResourceLocation background = new ResourceLocation(PixelmonInfoPlus.MODID, "textures/gui/ivevgui.png");
    public Pokemon pokemon = null;


    public IVEVGuiContainer(Container inventorySlotsIn) {
        super(inventorySlotsIn);
        this.xSize = 256;
        this.ySize = 146;
        this.pokemon = SlotApi.getSelectedPokemon();
    }

    /**
     * 重写 GuiContainer#keyTyped
     * 处理 GUI 的 KeyInputEvent
     *
     * @param typedChar 键位字符
     * @param keyCode   键位码
     * @throws IOException IOException
     */
    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
        // 按下 ESC 和绑定快捷键时，关闭 GUI。
        if (HotKey.SHOW_IVEVGUI_KEY_BINDING.getKeyCode() == keyCode || Keyboard.KEY_ESCAPE == keyCode) {
            IVEVGuiContainer.close();
        }

        // 按下↑↓键时切换宝可梦槽，并获取当前选中的槽的宝可梦
        if (keyCode == Keyboard.KEY_UP || keyCode == Keyboard.KEY_DOWN) {
            switch (keyCode) {
                case Keyboard.KEY_UP:
                    GuiPixelmonOverlay.selectPreviousPixelmon();
                    break;
                case Keyboard.KEY_DOWN:
                    GuiPixelmonOverlay.selectNextPixelmon();
                    break;
            }
            this.pokemon = SlotApi.getSelectedPokemon();
        }
    }

    /**
     * 重写 GuiContainer#mouseClicked 以处理鼠标点击事件
     *
     * @param mouseX
     * @param mouseY
     * @param mouseButton
     * @throws IOException
     */
    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        IVEVGuiContainer.close();
    }

    /**
     * 控制 GUI 被打开时，是否暂停游戏(仅单机有效)。
     *
     * @return 是否暂停游戏
     */
    @Override
    public boolean doesGuiPauseGame() {
        return true;
    }

    /**
     * 打开 GUI
     */
    public static void open() {
        EntityPlayer player = PixelmonInfoPlus.minecraft.player;

        player.openGui(PixelmonInfoPlus.instance, IVEVGuiHandler.GUI_ID, player.getEntityWorld(), 0, 0, 0);
        IVEVGuiContainer.isOpen = true;
    }

    /**
     * 关闭 GUI
     */
    public static void close() {
        EntityPlayer player = PixelmonInfoPlus.minecraft.player;
        if (IVEVGuiContainer.isOpen) {
            player.closeScreen();
            IVEVGuiContainer.isOpen = false;
        }
    }


    /**
     * 绘制背景层
     * Draws the background layer of this container (behind the items).
     */
    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(0x0302, 0x0303);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(background);
        this.drawTexturedModalRect(offsetX, offsetY, 0, 0, this.xSize, this.ySize);
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();

        this.pokemon = SlotApi.getSelectedPokemon();

        // 绘制完背景后，再绘制文字，以保证文字在最上层显示
        drawPokemonStatsText();
        // 绘制左侧的宝可梦像素图片以及基本信息
        drawPokemonPokedexNumberAndLevel();
        drawPokemon();
        drawPokemonName();
    }

    /**
     * 绘制宝可梦像素图标 & 属性图标
     */
    public void drawPokemon() {
        int offsetX = (this.width - this.xSize) / 2 + 16, offsetY = (this.height - this.ySize) / 2;

        if (this.pokemon != null) {
            // 绑定材质
            GuiHelper.bindPokemonSprite(this.pokemon, this.mc);
            // 渲染宝可梦像素图标
            GuiHelper.drawImageQuad(offsetX - 2, offsetY + 18.0D, 68.0D, 68.0F, 0.0D, 0.0D, 1.0D, 1.0D, this.zLevel);

            // 启用 OpenGL 色彩混合 & 设置颜色混合模式
            GlStateManager.enableBlend();
            GlStateManager.blendFunc(0x0302, 0x0303);

//            if (!this.pokemon.isEgg()) {
//                EnumType type1 = this.pokemon.getBaseStats().getType1();
//                EnumType type2 = this.pokemon.getBaseStats().getType2();
//                float x = type1.textureX;
//                float y = type1.textureY;
//                float x1 = 0.0F;
//                float y1 = 0.0F;
//                if (type2 != null) {
//                    x1 = type2.textureX;
//                    y1 = type2.textureY;
//                }
//
//                GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
//
//                // 绑定属性图标材质
//                this.mc.getTextureManager().bindTexture(GuiResources.types);
//                // 渲染宝可梦属性图标
//                if (type2 != EnumType.Mystery && type2 != null) {
//                    GuiHelper.drawImageQuad(offsetX + 20 + 8.0D, offsetY + 25, 21.0D, 21.0F, (double) (x1 / 1792.0F), (double) (y1 / 768.0F), (double) ((x1 + 240.0F) / 1792.0F), (double) ((y1 + 240.0F) / 768.0F), this.zLevel);
//                    GuiHelper.drawImageQuad(offsetX + 20 - 8.0D, offsetY + 25, 21.0D, 21.0F, (double) (x / 1792.0F), (double) (y / 768.0F), (double) ((x + 240.0F) / 1792.0F), (double) ((y + 240.0F) / 768.0F), this.zLevel);
//                } else {
//                    // 单(主)属性
//                    GuiHelper.drawImageQuad(offsetX + 20, offsetY + 25, 21.0D, 21.0F, (double) (x / 1792.0F), (double) (y / 768.0F), (double) ((x + 240.0F) / 1792.0F), (double) ((y + 240.0F) / 768.0F), this.zLevel);
//                }
//            }

            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            GlStateManager.disableBlend();
        }
    }

    /**
     * 绘制宝可梦图鉴编号 & 等级文字层
     */
    public void drawPokemonPokedexNumberAndLevel() {
        int offsetX = (this.width - this.xSize) / 2 + 47, offsetY = (this.height - this.ySize) / 2;
        int o = 11;

        if (this.pokemon != null) {
            if (this.pokemon.isEgg()) {
                drawString(this.mc.fontRenderer, "No. ???", offsetX - 47 + 8, offsetY + 11, 16777215);
                drawCenteredString(this.mc.fontRenderer, "Lvl: ???", offsetX + 19, offsetY + 11, 16777215);
            } else {
                drawString(this.mc.fontRenderer, "No. " + this.pokemon.getSpecies().getNationalPokedexNumber(), offsetX - 47 + 8, offsetY + 11, 16777215);
                drawCenteredString(this.mc.fontRenderer, "Lvl: " + this.pokemon.getLevel(), offsetX + 19, offsetY + 11, 16777215);
            }
        }
    }

    /**
     * 绘制宝可梦 info 文字层
     */
    public void drawPokemonName() {
        int offsetX = (this.width - this.xSize) / 2 + 47, offsetY = (this.height - this.ySize) / 2;
        int o = 11;

        if (this.pokemon != null) {
            String pokemonNickname = this.pokemon.getNickname();

            if (this.pokemon.isEgg()) {
                drawCenteredString(this.mc.fontRenderer, Entity1Base.getLocalizedName("Egg"), offsetX, offsetY + 99, 16777215);

            } else {
                // int offset = pokemon.hasGigantamaxFactor() ? 9 : 0;
                if (pokemonNickname != null && !pokemonNickname.equals("")) {
                    String ogName = "(" + this.pokemon.getSpecies().getLocalizedName() + ")";
                    this.drawCenteredString(this.mc.fontRenderer, pokemonNickname, offsetX, offsetY + 95, 16777215);
                    this.drawCenteredString(this.mc.fontRenderer, ogName, offsetX, offsetY + 104, 16777215);
                } else {
                    this.drawCenteredString(this.mc.fontRenderer, this.pokemon.getDisplayName(), offsetX, offsetY + 99, 16777215);
                }
            }
        }
    }


    /**
     * 绘制 stats 文字层
     */
    public void drawPokemonStatsText() {
        int offsetX = (this.width - this.xSize) / 2 + 104, offsetY = (this.height - this.ySize) / 2;
        int y = (this.ySize / 6);

        if (this.pokemon != null) {
            IVStore ivs = this.pokemon.getIVs();
            EVStore evs = this.pokemon.getEVs();

            // 绘制标题
            drawString(this.mc.fontRenderer, "HP", offsetX, offsetY + 12, 0xFFFFFF);
            drawString(this.mc.fontRenderer, "攻击", offsetX, offsetY + 33, 0xFFFFFF);
            drawString(this.mc.fontRenderer, "防御", offsetX, offsetY + 53, 0xFFFFFF);
            drawString(this.mc.fontRenderer, "特攻", offsetX, offsetY + 72, 0xFFFFFF);
            drawString(this.mc.fontRenderer, "特防", offsetX, offsetY + 93, 0xFFFFFF);
            drawString(this.mc.fontRenderer, "速度", offsetX, offsetY + 113, 0xFFFFFF);

            // 绘制数值 IVS
            drawCenteredString(this.mc.fontRenderer, String.valueOf(ivs.getStat(StatsType.HP)), offsetX + 55, offsetY + 12, 0xFFFFFF);
            drawCenteredString(this.mc.fontRenderer, String.valueOf(ivs.getStat(StatsType.Attack)), offsetX + 55, offsetY + 33, 0xFFFFFF);
            drawCenteredString(this.mc.fontRenderer, String.valueOf(ivs.getStat(StatsType.Defence)), offsetX + 55, offsetY + 53, 0xFFFFFF);
            drawCenteredString(this.mc.fontRenderer, String.valueOf(ivs.getStat(StatsType.SpecialAttack)), offsetX + 55, offsetY + 72, 0xFFFFFF);
            drawCenteredString(this.mc.fontRenderer, String.valueOf(ivs.getStat(StatsType.SpecialDefence)), offsetX + 55, offsetY + 93, 0xFFFFFF);
            drawCenteredString(this.mc.fontRenderer, String.valueOf(ivs.getStat(StatsType.Speed)), offsetX + 55, offsetY + 113, 0xFFFFFF);

            // 绘制数值 EVS
            drawCenteredString(this.mc.fontRenderer, String.valueOf(evs.getStat(StatsType.HP)), offsetX + 92, offsetY + 12, 0xFFFFFF);
            drawCenteredString(this.mc.fontRenderer, String.valueOf(evs.getStat(StatsType.Attack)), offsetX + 92, offsetY + 33, 0xFFFFFF);
            drawCenteredString(this.mc.fontRenderer, String.valueOf(evs.getStat(StatsType.Defence)), offsetX + 92, offsetY + 53, 0xFFFFFF);
            drawCenteredString(this.mc.fontRenderer, String.valueOf(evs.getStat(StatsType.SpecialAttack)), offsetX + 92, offsetY + 72, 0xFFFFFF);
            drawCenteredString(this.mc.fontRenderer, String.valueOf(evs.getStat(StatsType.SpecialDefence)), offsetX + 92, offsetY + 93, 0xFFFFFF);
            drawCenteredString(this.mc.fontRenderer, String.valueOf(evs.getStat(StatsType.Speed)), offsetX + 92, offsetY + 113, 0xFFFFFF);

            // 绘制数值 SS
            drawCenteredString(this.mc.fontRenderer, String.valueOf(this.pokemon.getStat(StatsType.HP)), offsetX + 129, offsetY + 12, 0xFFFFFF);
            drawCenteredString(this.mc.fontRenderer, String.valueOf(this.pokemon.getStat(StatsType.Attack)), offsetX + 129, offsetY + 33, 0xFFFFFF);
            drawCenteredString(this.mc.fontRenderer, String.valueOf(this.pokemon.getStat(StatsType.Defence)), offsetX + 129, offsetY + 53, 0xFFFFFF);
            drawCenteredString(this.mc.fontRenderer, String.valueOf(this.pokemon.getStat(StatsType.SpecialAttack)), offsetX + 129, offsetY + 72, 0xFFFFFF);
            drawCenteredString(this.mc.fontRenderer, String.valueOf(this.pokemon.getStat(StatsType.SpecialDefence)), offsetX + 129, offsetY + 93, 0xFFFFFF);
            drawCenteredString(this.mc.fontRenderer, String.valueOf(this.pokemon.getStat(StatsType.Speed)), offsetX + 129, offsetY + 113, 0xFFFFFF);

        }
    }
}
