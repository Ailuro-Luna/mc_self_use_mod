package com.example.blockstorage.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.SoundType;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockCustom extends Block {

    private String modId;
    private String blockName;
    private String[] textureNames = new String[6];

    public BlockCustom(Material material, String modId, String blockName) {
        super(material);
        this.modId = modId;
        this.blockName = blockName;
        this.setRegistryName(new ResourceLocation(modId, blockName));
        this.setUnlocalizedName(modId + "." + blockName);
        this.setCreativeTab(CreativeTabs.BUILDING_BLOCKS);

        // 设置默认纹理名称，如果没有特别指定
        for (int i = 0; i < 6; i++) {
            this.textureNames[i] = modId + ":" + blockName;
        }
    }

    /**
     * 设置所有面的纹理名称 - 在1.12中这只用于记录，实际纹理由JSON模型决定
     */
    public BlockCustom setTextureNames(String... textureNames) {
        if (textureNames.length > 0) {
            // 如果提供的纹理名称不足6个，用最后一个填充其余的
            String lastTexture = textureNames[textureNames.length - 1];
            for (int i = 0; i < 6; i++) {
                if (i < textureNames.length) {
                    this.textureNames[i] = textureNames[i];
                } else {
                    this.textureNames[i] = lastTexture;
                }
            }
        }
        return this;
    }

    /**
     * 设置特定面的纹理名称
     */
    public BlockCustom setTextureName(int side, String textureName) {
        if (side >= 0 && side < 6) {
            this.textureNames[side] = textureName;
        }
        return this;
    }

    /**
     * 获取纹理名称数组，用于模型注册
     */
    public String[] getTextureNames() {
        return textureNames;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.SOLID;
    }

    /**
     * 设置方块的基本属性
     */
    public BlockCustom setBlockProperties(float hardness, float resistance, String soundType) {
        this.setHardness(hardness);
        this.setResistance(resistance);

        // 设置方块的声音类型
        if ("stone".equalsIgnoreCase(soundType)) {
            this.setSoundType(SoundType.STONE);
        } else if ("wood".equalsIgnoreCase(soundType)) {
            this.setSoundType(SoundType.WOOD);
//        } else if ("gravel".equalsIgnoreCase(soundType)) {
//            this.setSoundType(SoundType.GRAVEL);
        } else if ("grass".equalsIgnoreCase(soundType)) {
            this.setSoundType(SoundType.PLANT);
        } else if ("metal".equalsIgnoreCase(soundType)) {
            this.setSoundType(SoundType.METAL);
        } else if ("glass".equalsIgnoreCase(soundType)) {
            this.setSoundType(SoundType.GLASS);
        } else if ("cloth".equalsIgnoreCase(soundType)) {
            this.setSoundType(SoundType.CLOTH);
        } else if ("sand".equalsIgnoreCase(soundType)) {
            this.setSoundType(SoundType.SAND);
        } else if ("snow".equalsIgnoreCase(soundType)) {
            this.setSoundType(SoundType.SNOW);
        } else if ("ladder".equalsIgnoreCase(soundType)) {
            this.setSoundType(SoundType.LADDER);
        } else if ("anvil".equalsIgnoreCase(soundType)) {
            this.setSoundType(SoundType.ANVIL);
        }

        return this;
    }
}