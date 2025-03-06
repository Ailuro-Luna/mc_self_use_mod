package com.example.blockstorage.util;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.appender.FileAppender;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.apache.logging.log4j.core.layout.PatternLayout;

import com.example.blockstorage.BlockStorageMod;
import com.example.blockstorage.block.BlockCustom;

import net.minecraft.block.Block;

import java.io.File;
import java.util.Arrays;

public class LogHelper {

    private static Logger LOGGER = LogManager.getLogger(BlockStorageMod.MODID);
    private static Logger BLOCK_LOGGER;
    private static boolean isBlockLoggerInitialized = false;

    // 初始化块日志记录器
    public static void initBlockLogger() {
        if (!isBlockLoggerInitialized) {
            try {
                // 使用Log4j2的核心API创建自定义日志记录器
                final LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
                final Configuration config = ctx.getConfiguration();
                
                // 创建布局（决定日志消息的格式）
                PatternLayout layout = PatternLayout.newBuilder()
                        .withPattern("%d{yyyy-MM-dd HH:mm:ss} [%t] %-5level - %msg%n")
                        .build();
                
                // 确保logs目录存在
                File logsDir = new File("logs");
                if (!logsDir.exists()) {
                    logsDir.mkdirs();
                }
                
                // 创建文件追加器（写入到指定文件）
                FileAppender appender = FileAppender.newBuilder()
                        .setName("BlockLogFileAppender")
                        .withFileName("logs/" + BlockStorageMod.MODID + "_blocks.log")
                        .setLayout(layout)
                        .build();
                
                // 启动追加器
                appender.start();
                
                // 将追加器添加到配置
                config.addAppender(appender);
                
                // 创建专门的日志配置
                LoggerConfig loggerConfig = new LoggerConfig("BlockLogger", Level.ALL, false);
                loggerConfig.addAppender(appender, Level.ALL, null);
                
                // 将日志配置添加到主配置
                config.addLogger("BlockLogger", loggerConfig);
                
                // 更新日志上下文
                ctx.updateLoggers();
                
                // 获取特定的日志记录器实例
                BLOCK_LOGGER = LogManager.getLogger("BlockLogger");
                isBlockLoggerInitialized = true;
                
                info("Block logger initialized successfully.");
            } catch (Exception e) {
                error("Failed to initialize block logger: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    // 记录方块注册（扩展版本，包含纹理信息）
    public static void logBlockRegistration(String modId, String blockName, int blockId, Block block) {
        if (isBlockLoggerInitialized && BLOCK_LOGGER != null) {
            // 基本方块信息
            StringBuilder logMessage = new StringBuilder();
            logMessage.append("Registered Block - ID: ").append(blockId)
                      .append(", Name: ").append(modId).append(":").append(blockName);
            
            // 如果是自定义方块，添加纹理信息
            if (block instanceof BlockCustom) {
                BlockCustom customBlock = (BlockCustom) block;
                String[] textures = customBlock.getTextureNames();
                if (textures != null && textures.length > 0) {
                    logMessage.append("\nTextures:");
                    for (int i = 0; i < textures.length; i++) {
                        String face = getFaceName(i);
                        logMessage.append("\n  ").append(face).append(": ").append(textures[i]);
                    }
                }
            }
            
            BLOCK_LOGGER.info(logMessage.toString());
        } else {
            info("Block Registration: ID=" + blockId + ", Name=" + modId + ":" + blockName);
        }
    }
    
    // 获取面的名称
    private static String getFaceName(int faceIndex) {
        switch (faceIndex) {
            case 0: return "DOWN";
            case 1: return "UP";
            case 2: return "NORTH";
            case 3: return "EAST";
            case 4: return "SOUTH";
            case 5: return "WEST";
            default: return "UNKNOWN(" + faceIndex + ")";
        }
    }
    
 // 在LogHelper类中添加
    public static void logBlockJsonFiles(String modId, String blockName) {
        if (isBlockLoggerInitialized && BLOCK_LOGGER != null) {
            StringBuilder logMessage = new StringBuilder();
            logMessage.append("JSON Files for ").append(modId).append(":").append(blockName);
            logMessage.append("\n  Blockstate: assets/blockstorage/blockstates/").append(modId).append("_").append(blockName).append(".json");
            logMessage.append("\n  Block Model: assets/blockstorage/models/block/").append(modId).append("/").append(blockName).append(".json");
            logMessage.append("\n  Item Model: assets/blockstorage/models/item/").append(modId).append("_").append(blockName).append(".json");
            
            BLOCK_LOGGER.info(logMessage.toString());
        }
    }

    // 基本日志方法
    public static void log(Level level, Object object) {
        LOGGER.log(level, object);
    }

    public static void info(Object object) {
        log(Level.INFO, object);
    }

    public static void warn(Object object) {
        log(Level.WARN, object);
    }

    public static void error(Object object) {
        log(Level.ERROR, object);
    }

    public static void debug(Object object) {
        log(Level.DEBUG, object);
    }
}