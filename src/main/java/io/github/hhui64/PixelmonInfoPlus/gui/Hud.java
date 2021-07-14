//package io.github.hhui64.PixelmonInfoPlus.gui;
//
//import io.github.hhui64.PixelmonInfoPlus.PixelmonInfoPlus;
//import net.minecraft.client.gui.Gui;
//import net.minecraft.client.gui.ScaledResolution;
//import net.minecraft.util.ResourceLocation;
//
//public class Hud extends Gui {
//    private final ScaledResolution sr = new ScaledResolution(PixelmonInfoPlus.minecraft);
//    //"pixelmon", "textures/sprites/pokemon", "textures/sprites/shinypokemon"
//    public static final ResourceLocation wrap = new ResourceLocation(PixelmonInfoPlus.MODID, "textures/gui/wrap.png");
//    public static final ResourceLocation _006 = new PokemonIcon("006", true).getResourceLocation();
//    public static final ResourceLocation _007 = new PokemonIcon("007", true).getResourceLocation();
//    public static final ResourceLocation _008 = new PokemonIcon("008", true).getResourceLocation();
//
//
//    public static Hud builder() {
//        return new Hud();
//    }
//
//    public void renderWrap() {
//        int winW = sr.getScaledWidth();
//        int winH = sr.getScaledHeight();
//
//        int wrapW = 24;
//        int wrapH = 24;
//
//        // 绘制 wrap
//        PixelmonInfoPlus.minecraft.getTextureManager().bindTexture(wrap);
//        drawModalRectWithCustomSizedTexture(winW - (wrapW + 1), 26, 0, 0, wrapW, wrapH, wrapW, wrapH);
//
//        // 绘制 wrap
//        PixelmonInfoPlus.minecraft.getTextureManager().bindTexture(wrap);
//        drawModalRectWithCustomSizedTexture(winW - (wrapW + 1), 51, 0, 0, wrapW, wrapH, wrapW, wrapH);
//
//        // 绘制 wrap
//        PixelmonInfoPlus.minecraft.getTextureManager().bindTexture(wrap);
//        drawModalRectWithCustomSizedTexture(winW - (wrapW + 1), 76, 0, 0, wrapW, wrapH, wrapW, wrapH);
//
//    }
//
//    public void renderIcon() {
//        int winW = sr.getScaledWidth();
//        int winH = sr.getScaledHeight();
//
//        int iconW = 18;
//        int iconH = 18;
//
//        // 绘制 icon
//        PixelmonInfoPlus.minecraft.getTextureManager().bindTexture(_006);
//        drawModalRectWithCustomSizedTexture(winW - (iconW + 4), 26 + 1, 0, 0, iconW, iconH, iconW, iconH);
//
//        PixelmonInfoPlus.minecraft.getTextureManager().bindTexture(_007);
//        drawModalRectWithCustomSizedTexture(winW - (iconW + 4), 51 + 1, 0, 0, iconW, iconH, iconW, iconH);
//
//        PixelmonInfoPlus.minecraft.getTextureManager().bindTexture(_008);
//        drawModalRectWithCustomSizedTexture(winW - (iconW + 4), 76 + 1, 0, 0, iconW, iconH, iconW, iconH);
//
//    }
//
//    private int offsetY(int base, int m) {
//        return (base * m) + m - (m - 1);
//    }
//}
