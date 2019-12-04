package vc.voice;

import java.io.*;
import javafx.concurrent.Task;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import javax.sound.sampled.*;

/**
 * A sample program is to demonstrate how to record sound in Java author:
 * www.codejava.net
 * http://www.codejava.net/coding/capture-and-record-sound-into-wav-file-with-java-sound-api
 */
public class VoiceRecorder extends Task<Void> {

	// record duration, in milliseconds
	private static String ID = "";
	private static int rec_num = 1;
	// path of the wav file
	static File file = new File("");
	String rootPath = file.getAbsolutePath();
	private static String rec_file = file.getAbsolutePath() + "/VoiceCheck/record/file" + rec_num + ".wav";
	File wavFile;
	VoiceCheckController vcc = new VoiceCheckController();
	WavToText wtt = new WavToText();
	// format of audio file
	AudioFileFormat.Type fileType = AudioFileFormat.Type.WAVE;

	// the line from which audio data is captured
	static TargetDataLine line;

	void set_id(String id) {
		ID = id;
	}
	static String get_id()
	{
		return ID;
	}
	void set_rec_num(int num) {
		rec_num = num;
	}
	static String getfilename()
	{
		return rec_file;
	}
	public VoiceRecorder() {

	}

	@Override
	protected Void call() throws Exception {
		rec_num = SQLHandler.getindex(this.ID);
		this.rec_file = file.getAbsolutePath() + "\\resource\\" + "-"+ID + "_" + rec_num + ".wav";
		this.wavFile = new File(rec_file);
		try {

			AudioFormat format = getAudioFormat();
			DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);

			// checks if system supports the data line
			if (!AudioSystem.isLineSupported(info)) {
				vcc.showstatelabel("Line not supported");
				System.exit(0);
			}
			line = (TargetDataLine) AudioSystem.getLine(info);
			if (line.isOpen()) {
				line.stop();
				line.close();
			} else {
				line.open(format);
				line.start(); // start capturing
			}

			AudioInputStream ais = new AudioInputStream(line);
			System.out.println(rec_file + " is recording..");
			// start recording
			AudioSystem.write(ais, fileType, wavFile);

		} catch (IOException ex) {
			ex.printStackTrace();
		} catch (LineUnavailableException e)
		{
			AudioFormat format = getAudioFormat();
			DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);

			// checks if system supports the data line
			if (!AudioSystem.isLineSupported(info)) {
				vcc.showstatelabel("Line not supported");
				System.exit(0);
			}
			line = (TargetDataLine) AudioSystem.getLine(info);
			if (line.isOpen()) {
				line.stop();
				line.close();
			} else {
				line.open(format);
				line.start(); // start capturing
			}

			AudioInputStream ais = new AudioInputStream(line);
			System.out.println(rec_file + " is recording..");
			// start recording
			AudioSystem.write(ais, fileType, wavFile);
		}

		return null;
	}
	
	/**
	 * Defines an audio format
	 */
	AudioFormat getAudioFormat() {
		float sampleRate = 16000;
		int sampleSizeInBits = 16;
		int channels = 1;
		boolean signed = true;
		boolean bigEndian = false;
		AudioFormat format = new AudioFormat(sampleRate, sampleSizeInBits, channels, signed, bigEndian);
		return format;
	}
	
	static boolean isopen()
	{
		if(line.isOpen())
			return true;
		else
			return false;
	}

	/**
	 * Closes the target data line to finish capturing and recording
	 */
	void finish() {
		line.stop();
		line.close();
		rec_num++;
		SQLHandler.updateindex(ID, rec_num);
		try {
			wtt.setid(ID);
			wtt.syncRecognizeFile(rec_file);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

}