package com.example.blockstorage.util;

import net.minecraft.block.material.Material;
import net.minecraft.util.ResourceLocation;

import com.example.blockstorage.block.BlockCustom;
import com.example.blockstorage.block.ModBlocks;

public class BlockRegistryHelper {

    /**
     * 批量准备方块
     * 
     * @param packageName 包名
     * @param blockData   方块数据，格式：{blockName, blockID, material, textures...}
     */
    public static void registerBlocks(String packageName, Object[][] blockData) {
        for (Object[] data : blockData) {
            if (data.length < 3) {
                continue; // 忽略数据不足的条目
            }

            String blockName = (String) data[0];
            int blockID = (Integer) data[1];
            Material material = getMaterialFromString((String) data[2]);

            BlockCustom block = new BlockCustom(material, packageName, blockName);

            // 如果有提供纹理信息
            if (data.length > 3) {
                String[] textures = new String[Math.min(data.length - 3, 6)];
                for (int i = 0; i < textures.length; i++) {
                    textures[i] = (String) data[i + 3];
                }
                block.setTextureNames(textures);
            }

            // 准备方块并指定ID
            if (blockID > 0) {
                ModBlocks.prepareBlockWithID(packageName, blockName, block, blockID);
            } else {
                ModBlocks.prepareBlock(packageName, blockName, block);
            }
        }
    }

    /**
     * 根据字符串获取对应的Material
     */
    private static Material getMaterialFromString(String materialName) {
        if ("rock".equalsIgnoreCase(materialName)) {
            return Material.ROCK;
        } else if ("ground".equalsIgnoreCase(materialName)) {
            return Material.GROUND;
        } else if ("wood".equalsIgnoreCase(materialName)) {
            return Material.WOOD;
        } else if ("iron".equalsIgnoreCase(materialName)) {
            return Material.IRON;
        } else if ("anvil".equalsIgnoreCase(materialName)) {
            return Material.ANVIL;
        } else if ("water".equalsIgnoreCase(materialName)) {
            return Material.WATER;
        } else if ("lava".equalsIgnoreCase(materialName)) {
            return Material.LAVA;
        } else if ("leaves".equalsIgnoreCase(materialName)) {
            return Material.LEAVES;
        } else if ("plants".equalsIgnoreCase(materialName)) {
            return Material.PLANTS;
        } else if ("vine".equalsIgnoreCase(materialName)) {
            return Material.VINE;
        } else if ("sponge".equalsIgnoreCase(materialName)) {
            return Material.SPONGE;
        } else if ("cloth".equalsIgnoreCase(materialName)) {
            return Material.CLOTH;
        } else if ("fire".equalsIgnoreCase(materialName)) {
            return Material.FIRE;
        } else if ("sand".equalsIgnoreCase(materialName)) {
            return Material.SAND;
        } else if ("circuits".equalsIgnoreCase(materialName)) {
            return Material.CIRCUITS;
        } else if ("glass".equalsIgnoreCase(materialName)) {
            return Material.GLASS;
        } else if ("tnt".equalsIgnoreCase(materialName)) {
            return Material.TNT;
        } else if ("coral".equalsIgnoreCase(materialName)) {
            return Material.CORAL;
        } else if ("ice".equalsIgnoreCase(materialName)) {
            return Material.ICE;
        } else if ("snow".equalsIgnoreCase(materialName)) {
            return Material.SNOW;
        } else if ("craftedSnow".equalsIgnoreCase(materialName)) {
            return Material.CRAFTED_SNOW;
        } else if ("cactus".equalsIgnoreCase(materialName)) {
            return Material.CACTUS;
        } else if ("clay".equalsIgnoreCase(materialName)) {
            return Material.CLAY;
        } else if ("pumpkin".equalsIgnoreCase(materialName)) {
            return Material.GOURD;
        } else if ("dragonEgg".equalsIgnoreCase(materialName)) {
            return Material.DRAGON_EGG;
        } else if ("portal".equalsIgnoreCase(materialName)) {
            return Material.PORTAL;
        } else if ("cake".equalsIgnoreCase(materialName)) {
            return Material.CAKE;
        } else if ("web".equalsIgnoreCase(materialName)) {
            return Material.WEB;
        } else {
            // 默认为石头材质
            return Material.ROCK;
        }
    }
}