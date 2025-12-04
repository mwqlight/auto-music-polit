package com.autospacemusic.util;

import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.builder.FFmpegBuilder;
import net.bramp.ffmpeg.probe.FFmpegProbeResult;
import net.bramp.ffmpeg.probe.FFmpegStream;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

/**
 * 视频处理工具类
 * 用于从视频文件中提取音频
 */
@Component
public class VideoUtils {
    
    private FFmpeg ffmpeg;
    private FFprobe ffprobe;
    
    public VideoUtils() {
        try {
            // 尝试自动查找FFmpeg和FFprobe可执行文件
            ffmpeg = new FFmpeg();
            ffprobe = new FFprobe();
        } catch (IOException e) {
            System.err.println("无法找到FFmpeg或FFprobe，请确保已安装并添加到系统路径");
            e.printStackTrace();
        }
    }
    
    /**
     * 从视频文件中提取音频
     * @param videoFilePath 视频文件路径
     * @param outputAudioFilePath 输出音频文件路径
     * @return 提取成功返回true，失败返回false
     */
    public boolean extractAudioFromVideo(String videoFilePath, String outputAudioFilePath) {
        try {
            // 检查视频文件是否存在
            File videoFile = new File(videoFilePath);
            if (!videoFile.exists()) {
                System.err.println("视频文件不存在: " + videoFilePath);
                return false;
            }
            
            // 检查FFmpeg是否可用
            if (ffmpeg == null || ffprobe == null) {
                System.err.println("FFmpeg或FFprobe不可用");
                return false;
            }
            
            // 创建FFmpeg构建器
            FFmpegBuilder builder = new FFmpegBuilder()
                .setInput(videoFilePath)
                .overrideOutputFiles(true)
                .addOutput(outputAudioFilePath)
                .setAudioCodec("libmp3lame") // 使用MP3编码
                .setAudioBitRate(192000) // 192kbps
                .setAudioChannels(2) // 立体声
                .setAudioSampleRate(44100) // 44.1kHz
                .done();
            
            // 执行FFmpeg命令
            FFmpegExecutor executor = new FFmpegExecutor(ffmpeg, ffprobe);
            executor.createJob(builder).run();
            
            // 检查输出文件是否创建成功
            File outputFile = new File(outputAudioFilePath);
            return outputFile.exists() && outputFile.length() > 0;
            
        } catch (Exception e) {
            System.err.println("提取音频时发生错误: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * 获取视频文件的信息
     * @param videoFilePath 视频文件路径
     * @return 视频信息，包含时长、格式等
     */
    public Optional<VideoInfo> getVideoInfo(String videoFilePath) {
        try {
            if (ffprobe == null) {
                return Optional.empty();
            }
            
            FFmpegProbeResult probeResult = ffprobe.probe(videoFilePath);
            
            // 获取视频时长（秒）
            double duration = probeResult.getFormat().duration;
            
            // 获取视频格式
            String format = probeResult.getFormat().format_name;
            
            // 获取视频流信息
            Optional<FFmpegStream> videoStream = probeResult.streams.stream()
                .filter(stream -> stream.codec_type.equals(FFmpegStream.CodecType.VIDEO))
                .findFirst();
            
            // 获取音频流信息
            Optional<FFmpegStream> audioStream = probeResult.streams.stream()
                .filter(stream -> stream.codec_type.equals(FFmpegStream.CodecType.AUDIO))
                .findFirst();
            
            VideoInfo videoInfo = new VideoInfo();
            videoInfo.setDuration((long) duration);
            videoInfo.setFormat(format);
            videoInfo.setHasVideoStream(videoStream.isPresent());
            videoInfo.setHasAudioStream(audioStream.isPresent());
            
            return Optional.of(videoInfo);
            
        } catch (Exception e) {
            System.err.println("获取视频信息时发生错误: " + e.getMessage());
            e.printStackTrace();
            return Optional.empty();
        }
    }
    
    /**
     * 验证视频文件是否有效
     * @param videoFilePath 视频文件路径
     * @return 有效返回true，无效返回false
     */
    public boolean isValidVideoFile(String videoFilePath) {
        try {
            Path path = Paths.get(videoFilePath);
            if (!Files.exists(path)) {
                return false;
            }
            
            // 检查文件大小
            if (Files.size(path) < 1024) { // 至少1KB
                return false;
            }
            
            // 尝试获取视频信息
            Optional<VideoInfo> videoInfo = getVideoInfo(videoFilePath);
            return videoInfo.isPresent() && videoInfo.get().hasAudioStream();
            
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * 获取音频文件的时长
     * @param audioFilePath 音频文件路径
     * @return 时长（秒），失败返回0
     */
    public int getAudioDuration(String audioFilePath) {
        try {
            if (ffprobe == null) {
                return 0;
            }
            
            FFmpegProbeResult probeResult = ffprobe.probe(audioFilePath);
            
            // 获取音频时长（秒）
            double duration = probeResult.getFormat().duration;
            
            return (int) Math.round(duration);
            
        } catch (Exception e) {
            System.err.println("获取音频时长时发生错误: " + e.getMessage());
            e.printStackTrace();
            return 0;
        }
    }
    
    /**
     * 视频信息类
     */
    public static class VideoInfo {
        private long duration; // 时长（秒）
        private String format; // 格式
        private boolean hasVideoStream; // 是否有视频流
        private boolean hasAudioStream; // 是否有音频流
        
        // Getters and setters
        public long getDuration() {
            return duration;
        }
        
        public void setDuration(long duration) {
            this.duration = duration;
        }
        
        public String getFormat() {
            return format;
        }
        
        public void setFormat(String format) {
            this.format = format;
        }
        
        public boolean hasVideoStream() {
            return hasVideoStream;
        }
        
        public void setHasVideoStream(boolean hasVideoStream) {
            this.hasVideoStream = hasVideoStream;
        }
        
        public boolean hasAudioStream() {
            return hasAudioStream;
        }
        
        public void setHasAudioStream(boolean hasAudioStream) {
            this.hasAudioStream = hasAudioStream;
        }
    }
}