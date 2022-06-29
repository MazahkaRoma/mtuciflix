package mtuciflix.api.utils;

import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.builder.FFmpegBuilder;

@Deprecated
public class FileConverter {
    public static void convertMedia(String filePath, String ffmpegPath, String ffprobePath) {
        try {
            FFmpeg ffmpeg = new FFmpeg(ffmpegPath);
            FFprobe ffprobe = new FFprobe(ffprobePath);

            FFmpegBuilder builder = new FFmpegBuilder()
                    .setInput(filePath)
                    .overrideOutputFiles(true)
                    .addOutput(filePath)
                    .setFormat("mp4")


                    .setAudioChannels(1)
                    .setAudioCodec("truehd")
                    .setAudioSampleRate(48_000)
                    .setAudioBitRate(32768)

                    .setVideoCodec("libx265")
                    .setVideoPixelFormat("yuv444p")
                    .setVideoFrameRate(24, 1)

                    .done();

            FFmpegExecutor executor = new FFmpegExecutor(ffmpeg, ffprobe);
            executor.createJob(builder).run();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
