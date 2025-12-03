package com.autospacemusic.util;

import java.io.File;

public class AudioUtils {
    
    /**
     * 获取音频文件时长（秒）
     * @param filePath 音频文件路径
     * @return 时长（秒）
     */
    public static int getAudioDuration(String filePath) {
        // 这里应该使用FFmpeg或其他音频处理库来获取实际时长
        // 为了简化示例，这里返回固定值
        File file = new File(filePath);
        if (file.exists()) {
            // 模拟计算时长的逻辑
            return (int) (file.length() / 10000); // 简化的计算
        }
        return 0;
    }
    
    /**
     * 验证音频文件格式
     * @param filePath 文件路径
     * @return 是否为支持的音频格式
     */
    public static boolean isValidAudioFormat(String filePath) {
        String[] supportedFormats = {".mp3", ".wav", ".flac", ".aac", ".ogg"};
        String lowerPath = filePath.toLowerCase();
        
        for (String format : supportedFormats) {
            if (lowerPath.endsWith(format)) {
                return true;
            }
        }
        return false;
    }
}