package com.example.blockstorage.block;

import java.util.HashMap;
import java.util.Map;

import com.example.blockstorage.BlockStorageMod;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.example.blockstorage.util.LogHelper;

@Mod.EventBusSubscriber(modid = BlockStorageMod.MODID)
public class ModBlocks {

    // 使用HashMap存储所有方块，便于后期访问
    private static final Map<String, Block> BLOCKS = new HashMap<String, Block>();

    // 为每个包/模组创建一个集合
    private static final Map<String, Map<String, Block>> BLOCK_PACKAGES = new HashMap<String, Map<String, Block>>();

    // 预定义16个包名，方便分类管理
    private static final String[] PACKAGES = new String[] { "bamboo", "customnpcs", "flansmod", "harvestcraft", "ic2",
            "jojobadv", "moreplayermodels", "mw", "nuclearcontrol", "railcraft", "shincolle", "tf", "thaumcraft", "thkaguyamod",
            "twilightforest" };

    public static void preInit() {
        // 初始化包集合
        for (String packageName : PACKAGES) {
            BLOCK_PACKAGES.put(packageName, new HashMap<String, Block>());
        }

        // 这里不再直接注册方块，而是准备要注册的方块
        prepareGeneratedBlocks();
    }

    public static void init() {
        // init阶段需要进行的方块设置，比如合成表等
    }

    /**
     * 准备要注册的方块
     */
    private static void prepareGeneratedBlocks() {
        // 调用生成代码准备方块，但不再直接注册
        GeneratedBlocks.prepareAll();
    }

    /**
     * 在此事件中注册所有方块
     */
    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        LogHelper.info("Registering " + BLOCKS.size() + " blocks to the game registry");
        
        // 注册所有准备好的方块
        for (Block block : BLOCKS.values()) {
            event.getRegistry().register(block);
            
            // 尝试从注册名中提取modId和blockName
            ResourceLocation registryName = block.getRegistryName();
            if (registryName != null) {
                String modId = registryName.getResourceDomain();
                String blockName = registryName.getResourcePath();
                
                // 记录方块注册信息（带纹理）
                // 使用-1作为blockID表示这是在注册阶段记录的
                LogHelper.logBlockRegistration(modId, blockName, -1, block);
                
                LogHelper.logBlockJsonFiles(modId, blockName);
            }
        }
    }

    /**
     * 在此事件中为所有方块注册ItemBlock
     */
    @SubscribeEvent
    public static void registerItemBlocks(RegistryEvent.Register<Item> event) {
        // 为每个方块创建并注册ItemBlock
        for (Block block : BLOCKS.values()) {
            ResourceLocation registryName = block.getRegistryName();
            if (registryName != null) {
                ItemBlock itemBlock = new ItemBlock(block);
                itemBlock.setRegistryName(registryName);
                event.getRegistry().register(itemBlock);
            }
        }
    }

    /**
     * 在此事件中注册所有方块和物品的模型
     */
    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public static void registerModels(ModelRegistryEvent event) {
        // 注册所有方块的模型
        for (Block block : BLOCKS.values()) {
            if (block instanceof BlockCustom) {
                registerBlockCustomModel((BlockCustom) block);
            } else {
                registerBlockModel(block);
            }
        }
    }

    /**
     * 注册普通方块的模型
     */
    @SideOnly(Side.CLIENT)
    private static void registerBlockModel(Block block) {
        Item item = Item.getItemFromBlock(block);
        ResourceLocation registryName = block.getRegistryName();
        
        if (registryName != null) {
            String modId = registryName.getResourceDomain();  // 1.12.2 中正确的方法
            String blockName = registryName.getResourcePath();  // 1.12.2 中正确的方法
            
            // 使用与Python脚本生成的资源文件格式匹配的路径
            ModelLoader.setCustomModelResourceLocation(item, 0,
                    new ModelResourceLocation(
                        BlockStorageMod.MODID + ":" + modId + "_" + blockName, 
                        "inventory"
                    ));
        }
    }

    /**
     * 注册自定义方块的模型
     * 注意：实际上，在1.12中你需要创建JSON模型文件
     */
    @SideOnly(Side.CLIENT)
    private static void registerBlockCustomModel(BlockCustom block) {
        Item item = Item.getItemFromBlock(block);
        ResourceLocation registryName = block.getRegistryName();
        
        if (registryName != null) {
            String modId = registryName.getResourceDomain();  // 1.12.2 中正确的方法
            String blockName = registryName.getResourcePath();  // 1.12.2 中正确的方法
            
            // 使用与Python脚本生成的资源文件格式匹配的路径
            ModelLoader.setCustomModelResourceLocation(item, 0,
                    new ModelResourceLocation(
                        BlockStorageMod.MODID + ":" + modId + "_" + blockName, 
                        "inventory"
                    ));
        }
    }

    /**
     * 准备一个方块并存储到对应包集合（不直接注册）
     */
    public static Block prepareBlock(String packageName, String blockName, Block block) {
        // 规范化包名，确保它属于预定义的16个包之一
        String normalizedPackage = normalizePackageName(packageName);

        // 添加到全局方块集合
        BLOCKS.put(blockName, block);

        // 添加到对应包的集合
        if (BLOCK_PACKAGES.containsKey(normalizedPackage)) {
            BLOCK_PACKAGES.get(normalizedPackage).put(blockName, block);
        }

        return block;
    }

    /**
     * 准备带ID的方块（仅用于记录）
     */
    public static Block prepareBlockWithID(String packageName, String blockName, Block block, int blockID) {
        // 记录方块和ID的映射关系 (仅用于参考)
        System.out.println("Prepared block: " + blockName + " with reference ID: " + blockID);
        
        // 记录到日志文件，包含方块对象以获取纹理信息
        LogHelper.logBlockRegistration(packageName, blockName, blockID, block);
        
        return prepareBlock(packageName, blockName, block);
    }

    /**
     * 获取所有已准备的方块
     */
    public static Map<String, Block> getAllBlocks() {
        return BLOCKS;
    }

    /**
     * 获取特定包内的所有方块
     */
    public static Map<String, Block> getPackageBlocks(String packageName) {
        String normalizedPackage = normalizePackageName(packageName);
        return BLOCK_PACKAGES.getOrDefault(normalizedPackage, new HashMap<String, Block>());
    }

    /**
     * 获取单个方块
     */
    public static Block getBlock(String blockName) {
        return BLOCKS.get(blockName);
    }

    /**
     * 规范化包名，确保它属于预定义的16个包之一
     */
    private static String normalizePackageName(String packageName) {
        for (String predefined : PACKAGES) {
            if (packageName.equalsIgnoreCase(predefined) || packageName.contains(predefined)) {
                return predefined;
            }
        }
        // 如果没有匹配的，归为custom包
        return "custom";
    }
}