package com.autospacemusic.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

public class AudioUtils {
    
    /**
     * 从视频文件提取音频
     * @param videoFilePath 视频文件路径
     * @return 音频文件路径
     */
    public static String extractAudioFromVideo(String videoFilePath) throws IOException {
        // 使用FFmpeg命令提取音频
        File videoFile = new File(videoFilePath);
        if (!videoFile.exists()) {
            throw new IOException("视频文件不存在: " + videoFilePath);
        }
        
        // 生成输出音频文件路径
        String audioFileName = UUID.randomUUID().toString() + ".mp3";
        String audioFilePath = System.getProperty("java.io.tmpdir") + File.separator + audioFileName;
        
        // 构建FFmpeg命令
        String[] command = {
            "ffmpeg",
            "-i", videoFilePath,
            "-q:a", "0",
            "-map", "a",
            audioFilePath
        };
        
        // 执行命令
        executeCommand(command);
        
        return audioFilePath;
    }
    
    /**
     * 获取音频文件时长（秒）
     * @param filePath 音频文件路径
     * @return 时长（秒）
     */
    public static int getAudioDuration(String filePath) {
        try {
            // 使用FFprobe命令获取音频时长
            String[] command = {
                "ffprobe",
                "-v", "error",
                "-select_streams", "a:0",
                "-show_entries", "stream=duration",
                "-of", "default=noprint_wrappers=1:nokey=1",
                filePath
            };
            
            String output = executeCommandAndGetOutput(command);
            if (!output.isEmpty()) {
                return (int) Math.round(Double.parseDouble(output.trim()));
            }
        } catch (Exception e) {
            System.err.println("获取音频时长失败: " + e.getMessage());
        }
        
        //  fallback to file size based estimation
        File file = new File(filePath);
        if (file.exists()) {
            return (int) (file.length() / 10000); // 简化的计算
        }
        return 0;
    }
    
    /**
     * 音频剪切功能
     * @param audioFile 音频文件
     * @param startTime 开始时间（秒）
     * @param endTime 结束时间（秒）
     * @return 剪切后的音频文件
     */
    public static File cutAudio(File audioFile, int startTime, int endTime) throws IOException {
        if (!audioFile.exists()) {
            throw new IOException("音频文件不存在: " + audioFile.getAbsolutePath());
        }
        
        // 生成输出文件
        String outputFileName = "cut_" + UUID.randomUUID().toString() + ".mp3";
        File outputFile = new File(System.getProperty("java.io.tmpdir"), outputFileName);
        
        // 构建FFmpeg命令
        String[] command = {
            "ffmpeg",
            "-i", audioFile.getAbsolutePath(),
            "-ss", String.valueOf(startTime),
            "-to", String.valueOf(endTime),
            "-c", "copy",
            outputFile.getAbsolutePath()
        };
        
        // 执行命令
        executeCommand(command);
        
        return outputFile;
    }
    
    /**
     * 音频合并功能
     * @param audioFiles 音频文件列表
     * @return 合并后的音频文件
     */
    public static File mergeAudios(List<File> audioFiles) throws IOException {
        if (audioFiles.isEmpty()) {
            throw new IllegalArgumentException("音频文件列表为空");
        }
        
        // 创建临时文件列表
        File tempListFile = File.createTempFile("audio_list_", ".txt");
        tempListFile.deleteOnExit();
        
        // 写入文件列表
        StringBuilder content = new StringBuilder();
        for (File audioFile : audioFiles) {
            if (!audioFile.exists()) {
                throw new IOException("音频文件不存在: " + audioFile.getAbsolutePath());
            }
            content.append("file '").append(audioFile.getAbsolutePath()).append("'\n");
        }
        Files.write(tempListFile.toPath(), content.toString().getBytes());
        
        // 生成输出文件
        String outputFileName = "merged_" + UUID.randomUUID().toString() + ".mp3";
        File outputFile = new File(System.getProperty("java.io.tmpdir"), outputFileName);
        
        // 构建FFmpeg命令
        String[] command = {
            "ffmpeg",
            "-f", "concat",
            "-safe", "0",
            "-i", tempListFile.getAbsolutePath(),
            "-c", "copy",
            outputFile.getAbsolutePath()
        };
        
        // 执行命令
        executeCommand(command);
        
        return outputFile;
    }
    
    /**
     * 添加音效功能
     * @param audioFile 音频文件
     * @param effectType 音效类型
     * @return 添加音效后的音频文件
     */
    public static File addAudioEffect(File audioFile, String effectType) throws IOException {
        if (!audioFile.exists()) {
            throw new IOException("音频文件不存在: " + audioFile.getAbsolutePath());
        }
        
        // 生成输出文件
        String outputFileName = effectType + "_" + UUID.randomUUID().toString() + ".mp3";
        File outputFile = new File(System.getProperty("java.io.tmpdir"), outputFileName);
        
        // 根据音效类型构建不同的FFmpeg命令
        String[] command = null;
        
        switch (effectType.toLowerCase()) {
            case "echo":
                command = new String[]{
                    "ffmpeg",
                    "-i", audioFile.getAbsolutePath(),
                    "-af", "aecho=0.8:0.9:1000:0.3",
                    outputFile.getAbsolutePath()
                };
                break;
            case "reverb":
                command = new String[]{
                    "ffmpeg",
                    "-i", audioFile.getAbsolutePath(),
                    "-af", "reverb=50:50:100",
                    outputFile.getAbsolutePath()
                };
                break;
            case "chorus":
                command = new String[]{
                    "ffmpeg",
                    "-i", audioFile.getAbsolutePath(),
                    "-af", "chorus=0.5:0.9:50|60|40:0.4|0.32|0.3:0.25|0.4|0.3:2|2.3|1.3",
                    outputFile.getAbsolutePath()
                };
                break;
            case "bassboost":
                command = new String[]{
                    "ffmpeg",
                    "-i", audioFile.getAbsolutePath(),
                    "-af", "bass=g=10",
                    outputFile.getAbsolutePath()
                };
                break;
            default:
                throw new IllegalArgumentException("不支持的音效类型: " + effectType);
        }
        
        // 执行命令
        executeCommand(command);
        
        return outputFile;
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
    
    /**
     * 执行系统命令
     * @param command 命令数组
     * @throws IOException 执行命令时发生错误
     */
    private static void executeCommand(String[] command) throws IOException {
        ProcessBuilder processBuilder = new ProcessBuilder(command);
        processBuilder.redirectErrorStream(true);
        Process process = processBuilder.start();
        
        try {
            int exitCode = process.waitFor();
            if (exitCode != 0) {
                throw new IOException("命令执行失败，退出码: " + exitCode);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IOException("命令执行被中断", e);
        }
    }
    
    /**
     * 执行系统命令并获取输出
     * @param command 命令数组
     * @return 命令输出
     * @throws IOException 执行命令时发生错误
     */
    private static String executeCommandAndGetOutput(String[] command) throws IOException {
        ProcessBuilder processBuilder = new ProcessBuilder(command);
        processBuilder.redirectErrorStream(true);
        Process process = processBuilder.start();
        
        try {
            int exitCode = process.waitFor();
            if (exitCode != 0) {
                throw new IOException("命令执行失败，退出码: " + exitCode);
            }
            
            return new String(process.getInputStream().readAllBytes());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IOException("命令执行被中断", e);
        }
    }
}