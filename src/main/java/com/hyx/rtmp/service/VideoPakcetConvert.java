package com.hyx.rtmp.service;

import org.bytedeco.ffmpeg.avcodec.AVPacket;
import org.bytedeco.ffmpeg.avformat.AVFormatContext;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.FFmpegFrameRecorder;
import org.bytedeco.javacv.FrameGrabber.Exception;

import java.io.IOException;
import java.util.Date;

import static org.bytedeco.ffmpeg.global.avcodec.av_packet_unref;

public class VideoPakcetConvert {
    FFmpegFrameGrabber grabber = null;
    FFmpegFrameRecorder record = null;

    int width = -1, height = -1;

    // 视频参数
    protected int audiocodecid;
    protected int codecid;
    protected double framerate;// 帧率
    protected int bitrate;// 比特率

    // 音频参数
    // 想要录制音频，这三个参数必须有：audioChannels > 0 && audioBitrate > 0 && sampleRate > 0
    private int audioChannels;
    private int audioBitrate;
    private int sampleRate;

    /**
     * 选择视频源
     *
     * @param src
     * @throws Exception
     * @author eguid
     */
    public VideoPakcetConvert from(String src) throws Exception {
        System.out.println("rtspUrl 2：" + src);
        // 采集/抓取器
        grabber = new FFmpegFrameGrabber(src);
        if (src.indexOf("rtsp") >= 0) {
            grabber.setOption("rtsp_transport", "tcp");
        }
        grabber.start();// 开始之后ffmpeg会采集视频信息，之后就可以获取音视频信息
        if (width < 0 || height < 0) {
            width = grabber.getImageWidth();
            height = grabber.getImageHeight();
        }
        // 视频参数
        audiocodecid = grabber.getAudioCodec();
        codecid = grabber.getVideoCodec();
        framerate = grabber.getVideoFrameRate();// 帧率
        bitrate = grabber.getVideoBitrate();// 比特率
        // 音频参数
        // 想要录制音频，这三个参数必须有：audioChannels > 0 && audioBitrate > 0 && sampleRate > 0
        audioChannels = grabber.getAudioChannels();
        audioBitrate = grabber.getAudioBitrate();
        if (audioBitrate < 1) {
            audioBitrate = 128 * 1000;// 默认音频比特率
        }
        return this;
    }

    /**
     * 选择输出
     *
     * @param out
     * @throws IOException
     * @author eguid
     */
    public VideoPakcetConvert to(String out) throws IOException {

        System.out.println("rtmpUrl 2：" + out);
        // 录制/推流器
        record = new FFmpegFrameRecorder(out, width, height);
        record.setVideoOption("crf", "18");
        record.setGopSize(2);
        record.setFrameRate(framerate);
        record.setVideoBitrate(bitrate);

        record.setAudioChannels(audioChannels);
        record.setAudioBitrate(audioBitrate);
        record.setSampleRate(sampleRate);
        AVFormatContext fc = null;
        if (out.indexOf("rtmp") >= 0 || out.indexOf("hls") > 0 || out.indexOf("flv") > 0) {
            record.setFormat("flv");
            record.setAudioCodecName("aac");
            record.setVideoCodec(codecid);
            fc = grabber.getFormatContext();
        }
        if (null != fc) {
            record.start(fc);
        }
        return this;
    }

    /**
     * 转封装
     *
     * @throws IOException
     * @author eguid
     */
    public VideoPakcetConvert go(String id) throws IOException {
        long err_index = 0;//采集或推流导致的错误次数
        //连续五次没有采集到帧则认为视频采集结束，程序错误次数超过1次即中断程序
        for (int no_frame_index = 0; no_frame_index < 5 || err_index > 1; ) {
            AVPacket pkt = null;
            try {
                //没有解码的音视频帧
                pkt = grabber.grabPacket();
                if (pkt == null || pkt.size() <= 0 || pkt.data() == null) {
                    //空包记录次数跳过
                    no_frame_index++;
                    continue;
                }

                //不需要编码直接把音视频帧推出去
                err_index += (record.recordPacket(pkt) ? 0 : 1);//如果失败err_index自增1
                av_packet_unref(pkt);
                if (null != VideoService.LIVE_VIDEO_MAP) {
                    Date date = VideoService.LIVE_VIDEO_MAP.get(id);
                    if ((null == date)) {
                        stop();
                        no_frame_index = 10;
                        err_index = 10;
                        break;
                    }
                }


            } catch (Exception e) {//推流失败
                err_index++;
            } catch (IOException e) {
                err_index++;
            }
        }
        return this;
    }

    public VideoPakcetConvert stop() {
        if (null != grabber) {
            try {
                grabber.close();
            } catch (Exception e) {
            }
        }
        if (null != record) {
            try {
                record.close();
            } catch (org.bytedeco.javacv.FrameRecorder.Exception e) {
            }
        }
        return this;
    }

}