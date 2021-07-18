package io.github.hhui64.pixelmoninfoplus.gui.statspanel;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.client.gui.GuiHelper;
import com.pixelmonmod.pixelmon.client.gui.GuiPixelmonOverlay;
import com.pixelmonmod.pixelmon.entities.pixelmon.Entity1Base;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.EVStore;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.IVStore;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.StatsType;
import io.github.hhui64.pixelmoninfoplus.PixelmonInfoPlus;
import io.github.hhui64.pixelmoninfoplus.keybinding.KeyBindingManager;
import io.github.hhui64.pixelmoninfoplus.pixelmon.PartyApi;
import io.github.hhui64.pixelmoninfoplus.pixelmon.PartyCache;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Keyboard;

import java.io.IOException;
import java.util.Arrays;

public class StatsPanelGuiContainer extends GuiContainer {
    private static final Logger logger = LogManager.getLogger("StatsPanelGuiContainer");
    /**
     * flag: GUI 是否已打开
     */
    public static Boolean isOpen = false;
    private static final ResourceLocation background = new ResourceLocation(PixelmonInfoPlus.MODID, "textures/gui/statspanel_background.png");
    public Pokemon pokemon;

    public int top = 0;
    public int left = 0;

    public StatsPanelGuiContainer(Container inventorySlotsIn) {
        super(inventorySlotsIn);
        this.xSize = 256;
        this.ySize = 192;
        this.pokemon = PartyApi.getSelectedPokemon();
    }

    @Override
    public void initGui() {
        this.pokemon = PartyApi.getSelectedPokemon();
        super.initGui();
    }

    /**
     * 获取坐标偏移值
     *
     * @return XY坐标
     */
    public int[] getOffsetXY() {
        return new int[]{(this.width - this.xSize) / 2, (this.height - this.ySize) / 2};
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
        if (KeyBindingManager.showStatsPanel.getKeyCode() == keyCode || Keyboard.KEY_ESCAPE == keyCode) {
            StatsPanelGuiContainer.close();
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
            this.pokemon = PartyApi.getSelectedPokemon();
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
        // 鼠标被点击时，关闭 GUI。
        StatsPanelGuiContainer.close();
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
        EntityPlayer player = Minecraft.getMinecraft().player;

        if (PartyApi.getTeam().size() > 0) {
            player.openGui(PixelmonInfoPlus.instance, StatsPanelGuiHandler.GUI_ID, player.getEntityWorld(), 0, 0, 0);
            StatsPanelGuiContainer.isOpen = true;
            // 尝试更新缓存
            PartyCache.updateCache(false);
        }
    }

    /**
     * 关闭 GUI
     */
    public static void close() {
        EntityPlayer player = Minecraft.getMinecraft().player;
        if (StatsPanelGuiContainer.isOpen) {
            player.closeScreen();
            StatsPanelGuiContainer.isOpen = false;
        }
    }

    /**
     * 绘制背景层
     * <p>
     * Draws the background layer of this container (behind the items).
     */
    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        int offsetX = this.getOffsetXY()[0], offsetY = this.getOffsetXY()[1];

        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(0x0302, 0x0303);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

        // 绘制背景材质
        this.mc.getTextureManager().bindTexture(background);
        this.drawTexturedModalRect(offsetX, offsetY, 0, 0, this.xSize, this.ySize);

        // 绘制进度条
        this.drawProgress();
        // 绘制进度条边框
        this.drawTexturedModalRect(offsetX + 105, offsetY + 152, 105, 208, 137, 31);

        GlStateManager.disableBlend();
        GlStateManager.popMatrix();

        // 绘制完背景后，再绘制文字，以保证文字在最上层显示
        drawProgressText();
        drawPokemonStatsText();
        // 绘制左侧的宝可梦像素图片以及基本信息
        drawPokemonPokedexNumberAndLevel();
        drawPokemonIcon();
        drawPokemonName();
    }

    /**
     * 绘制进度条
     */
    public void drawProgress() {
        int offsetX = this.getOffsetXY()[0], offsetY = this.getOffsetXY()[1];

        IVStore ivs = PartyApi.getCurrentPokemonIVStore(this.pokemon);
        EVStore evs = PartyApi.getCurrentPokemonEVStore(this.pokemon);

        int ivsProgressWidth = Math.toIntExact(Math.round(115 * ivs.getPercentage(0) / 100));
        double evsPercentage = Arrays.stream(evs.getArray()).sum() / 510.0 * 100.0;
        int evsProgressWidth = Math.toIntExact(Math.round(115 * evsPercentage / 100));

        this.drawTexturedModalRect(offsetX + 126, offsetY + 153, 126, 243, ivsProgressWidth, 12);
        this.drawTexturedModalRect(offsetX + 126, offsetY + 170, 126, 243, evsProgressWidth, 12);
    }

    /**
     * 绘制进度条上的文本
     */
    public void drawProgressText() {
        int offsetX = this.getOffsetXY()[0], offsetY = this.getOffsetXY()[1];

        if (this.pokemon != null) {
            IVStore ivs = PartyApi.getCurrentPokemonIVStore(this.pokemon);
            EVStore evs = PartyApi.getCurrentPokemonEVStore(this.pokemon);
            double evsPercentage = Arrays.stream(evs.getArray()).sum() / 510.0 * 100.0;

            String strIVs = this.pokemon.isEgg() ? "???/??? (???%)" : Arrays.stream(ivs.getArray()).sum() + "/186 (" + Math.round(ivs.getPercentage(0)) + "%)";
            String strEVs = this.pokemon.isEgg() ? "???/??? (???%)" : Arrays.stream(evs.getArray()).sum() + "/510 (" + Math.round(evsPercentage) + "%)";
            this.drawCenteredString(this.mc.fontRenderer, strIVs, offsetX + 183, offsetY + 155, 0xFFFFFF);
            this.drawCenteredString(this.mc.fontRenderer, strEVs, offsetX + 183, offsetY + 172, 0xFFFFFF);
        }
    }

    /**
     * 绘制宝可梦像素图标 & 属性图标
     */
    public void drawPokemonIcon() {
        int offsetX = this.getOffsetXY()[0], offsetY = this.getOffsetXY()[1];

        if (this.pokemon != null) {
            // 绑定材质
            GuiHelper.bindPokemonSprite(this.pokemon, this.mc);
            // 渲染宝可梦像素图标
            GuiHelper.drawImageQuad(offsetX - 2 + 16, offsetY + 18.0D, 68.0D, 68.0F, 0.0D, 0.0D, 1.0D, 1.0D, this.zLevel);
            GuiHelper.drawImageQuad(offsetX - 2 + 16, offsetY + 18.0D + 22, 68.0D, 68.0F, 0.0D, 0.0D, 1.0D, 1.0D, this.zLevel);

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
        int offsetX = this.getOffsetXY()[0], offsetY = this.getOffsetXY()[1];
        int y = offsetY + 11;

        if (this.pokemon != null) {
            if (this.pokemon.isEgg()) {
                drawString(this.mc.fontRenderer, I18n.format("gui.statspanel.number") + " ???", offsetX - 47 + 47 + 8, y, 0xFFFFFF);
                drawCenteredString(this.mc.fontRenderer, I18n.format("gui.statspanel.lvl") + " ???", offsetX + 19 + 47, y, 0xFFFFFF);
            } else {
                drawString(this.mc.fontRenderer, I18n.format("gui.statspanel.number") + " " + this.pokemon.getSpecies().getNationalPokedexNumber(), offsetX - 47 + 47 + 8, y, 0xFFFFFF);
                drawCenteredString(this.mc.fontRenderer, I18n.format("gui.statspanel.lvl") + " " + this.pokemon.getLevel(), offsetX + 47 + 19, y, 0xFFFFFF);
            }
        }
    }

    /**
     * 绘制宝可梦 info 文字层
     */
    public void drawPokemonName() {
        int offsetX = this.getOffsetXY()[0], offsetY = this.getOffsetXY()[1];
        int x = 11;

        if (this.pokemon != null) {
            String pokemonNickname = this.pokemon.getNickname();

            if (this.pokemon.isEgg()) {
                drawCenteredString(this.mc.fontRenderer, Entity1Base.getLocalizedName("Egg"), offsetX + 47, offsetY + 99 + 17, 0xFFFFFF);
            } else {
                // int offset = pokemon.hasGigantamaxFactor() ? 9 : 0;
                if (pokemonNickname != null && !pokemonNickname.equals("")) {
                    String ogName = "(" + this.pokemon.getSpecies().getLocalizedName() + ")";
                    this.drawCenteredString(this.mc.fontRenderer, pokemonNickname, offsetX + 47, offsetY + 95 + 17, 0xFFFFFF);
                    this.drawCenteredString(this.mc.fontRenderer, ogName, offsetX + 47, offsetY + 104 + 17, 0xFFFFFF);
                } else {
                    this.drawCenteredString(this.mc.fontRenderer, this.pokemon.getDisplayName(), offsetX + 47, offsetY + 99 + 17, 0xFFFFFF);
                }
            }
        }
    }


    /**
     * 绘制 stats 文字层
     */
    public void drawPokemonStatsText() {
        int offsetX = this.getOffsetXY()[0], offsetY = this.getOffsetXY()[1];
        int x = offsetX + 104;

        if (this.pokemon != null) {
            IVStore ivs = PartyApi.getCurrentPokemonIVStore(this.pokemon);
            EVStore evs = PartyApi.getCurrentPokemonEVStore(this.pokemon);

            String IVsHp = this.pokemon.isEgg() ? "???" : ivs.isHyperTrained(StatsType.HP) ? "31(" + ivs.getStat(StatsType.HP) + ")" : String.valueOf(ivs.getStat(StatsType.HP));
            String IVsAtk = this.pokemon.isEgg() ? "???" : ivs.isHyperTrained(StatsType.Attack) ? "31(" + ivs.getStat(StatsType.Attack) + ")" : String.valueOf(ivs.getStat(StatsType.Attack));
            String IVsDef = this.pokemon.isEgg() ? "???" : ivs.isHyperTrained(StatsType.Defence) ? "31(" + ivs.getStat(StatsType.Defence) + ")" : String.valueOf(ivs.getStat(StatsType.Defence));
            String IVsSpAtk = this.pokemon.isEgg() ? "???" : ivs.isHyperTrained(StatsType.SpecialAttack) ? "31(" + ivs.getStat(StatsType.SpecialAttack) + ")" : String.valueOf(ivs.getStat(StatsType.SpecialAttack));
            String IVsSpDef = this.pokemon.isEgg() ? "???" : ivs.isHyperTrained(StatsType.SpecialDefence) ? "31(" + ivs.getStat(StatsType.SpecialDefence) + ")" : String.valueOf(ivs.getStat(StatsType.SpecialDefence));
            String IVsSpd = this.pokemon.isEgg() ? "???" : ivs.isHyperTrained(StatsType.Speed) ? "31(" + ivs.getStat(StatsType.Speed) + ")" : String.valueOf(ivs.getStat(StatsType.Speed));

            String EVsHp = this.pokemon.isEgg() ? "--" : String.valueOf(evs.getStat(StatsType.HP));
            String EVsAtk = this.pokemon.isEgg() ? "--" : String.valueOf(evs.getStat(StatsType.Attack));
            String EVsDef = this.pokemon.isEgg() ? "--" : String.valueOf(evs.getStat(StatsType.Defence));
            String EVsSpAtk = this.pokemon.isEgg() ? "--" : String.valueOf(evs.getStat(StatsType.SpecialAttack));
            String EVsSpDef = this.pokemon.isEgg() ? "--" : String.valueOf(evs.getStat(StatsType.SpecialDefence));
            String EVsSpd = this.pokemon.isEgg() ? "--" : String.valueOf(evs.getStat(StatsType.Speed));

            String Hp = this.pokemon.isEgg() ? "???" : String.valueOf(this.pokemon.getStat(StatsType.HP));
            String Atk = this.pokemon.isEgg() ? "???" : String.valueOf(this.pokemon.getStat(StatsType.Attack));
            String Def = this.pokemon.isEgg() ? "???" : String.valueOf(this.pokemon.getStat(StatsType.Defence));
            String SpAtk = this.pokemon.isEgg() ? "???" : String.valueOf(this.pokemon.getStat(StatsType.SpecialAttack));
            String SpDef = this.pokemon.isEgg() ? "???" : String.valueOf(this.pokemon.getStat(StatsType.SpecialDefence));
            String Spd = this.pokemon.isEgg() ? "???" : String.valueOf(this.pokemon.getStat(StatsType.Speed));

            int HpColor = this.pokemon.getNature().increasedStat == StatsType.HP ? 65280 : (this.pokemon.getNature().decreasedStat == StatsType.HP ? 16724016 : 0xFFFFFF);
            int AtkColor = this.pokemon.getNature().increasedStat == StatsType.Attack ? 65280 : (this.pokemon.getNature().decreasedStat == StatsType.Attack ? 16724016 : 0xFFFFFF);
            int DefColor = this.pokemon.getNature().increasedStat == StatsType.Defence ? 65280 : (this.pokemon.getNature().decreasedStat == StatsType.Defence ? 16724016 : 0xFFFFFF);
            int SpAtkColor = this.pokemon.getNature().increasedStat == StatsType.SpecialAttack ? 65280 : (this.pokemon.getNature().decreasedStat == StatsType.SpecialAttack ? 16724016 : 0xFFFFFF);
            int SpDefColor = this.pokemon.getNature().increasedStat == StatsType.SpecialDefence ? 65280 : (this.pokemon.getNature().decreasedStat == StatsType.SpecialDefence ? 16724016 : 0xFFFFFF);
            int SpdColor = this.pokemon.getNature().increasedStat == StatsType.Speed ? 65280 : (this.pokemon.getNature().decreasedStat == StatsType.Speed ? 16724016 : 0xFFFFFF);

            // 绘制标题
            drawString(this.mc.fontRenderer, I18n.format("gui.statspanel.hp.name"), x, offsetY + 12, 0xFFFFFF);
            drawString(this.mc.fontRenderer, I18n.format("gui.statspanel.attack.name"), x, offsetY + 33, 0xFFFFFF);
            drawString(this.mc.fontRenderer, I18n.format("gui.statspanel.defense.name"), x, offsetY + 53, 0xFFFFFF);
            drawString(this.mc.fontRenderer, I18n.format("gui.statspanel.spattack.name"), x, offsetY + 72, 0xFFFFFF);
            drawString(this.mc.fontRenderer, I18n.format("gui.statspanel.spdefense.name"), x, offsetY + 93, 0xFFFFFF);
            drawString(this.mc.fontRenderer, I18n.format("gui.statspanel.speed.name"), x, offsetY + 113, 0xFFFFFF);

            // 绘制数值 IVS
            drawCenteredString(this.mc.fontRenderer, IVsHp, x + 55, offsetY + 12, ivs.isHyperTrained(StatsType.HP) ? 0xFF55FF : 0xFFFFFF);
            drawCenteredString(this.mc.fontRenderer, IVsAtk, x + 55, offsetY + 33, ivs.isHyperTrained(StatsType.Attack) ? 0xFF55FF : 0xFFFFFF);
            drawCenteredString(this.mc.fontRenderer, IVsDef, x + 55, offsetY + 53, ivs.isHyperTrained(StatsType.Defence) ? 0xFF55FF : 0xFFFFFF);
            drawCenteredString(this.mc.fontRenderer, IVsSpAtk, x + 55, offsetY + 72, ivs.isHyperTrained(StatsType.SpecialAttack) ? 0xFF55FF : 0xFFFFFF);
            drawCenteredString(this.mc.fontRenderer, IVsSpDef, x + 55, offsetY + 93, ivs.isHyperTrained(StatsType.SpecialDefence) ? 0xFF55FF : 0xFFFFFF);
            drawCenteredString(this.mc.fontRenderer, IVsSpd, x + 55, offsetY + 113, ivs.isHyperTrained(StatsType.Speed) ? 0xFF55FF : 0xFFFFFF);

            // 绘制数值 EVS
            drawCenteredString(this.mc.fontRenderer, EVsHp, x + 92, offsetY + 12, 0xFFFFFF);
            drawCenteredString(this.mc.fontRenderer, EVsAtk, x + 92, offsetY + 33, 0xFFFFFF);
            drawCenteredString(this.mc.fontRenderer, EVsDef, x + 92, offsetY + 53, 0xFFFFFF);
            drawCenteredString(this.mc.fontRenderer, EVsSpAtk, x + 92, offsetY + 72, 0xFFFFFF);
            drawCenteredString(this.mc.fontRenderer, EVsSpDef, x + 92, offsetY + 93, 0xFFFFFF);
            drawCenteredString(this.mc.fontRenderer, EVsSpd, x + 92, offsetY + 113, 0xFFFFFF);

            // 绘制数值 ST
            drawCenteredString(this.mc.fontRenderer, Hp, x + 129, offsetY + 12, HpColor);
            drawCenteredString(this.mc.fontRenderer, Atk, x + 129, offsetY + 33, AtkColor);
            drawCenteredString(this.mc.fontRenderer, Def, x + 129, offsetY + 53, DefColor);
            drawCenteredString(this.mc.fontRenderer, SpAtk, x + 129, offsetY + 72, SpAtkColor);
            drawCenteredString(this.mc.fontRenderer, SpDef, x + 129, offsetY + 93, SpDefColor);
            drawCenteredString(this.mc.fontRenderer, Spd, x + 129, offsetY + 113, SpdColor);
        }
    }
}
