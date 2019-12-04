package vc.voice;

import com.google.cloud.language.v1.Document;
import com.google.cloud.language.v1.LanguageServiceClient;
import com.google.cloud.language.v1.Sentiment;
import com.google.cloud.language.v1.Document.Type;
import com.google.cloud.speech.v1.RecognitionAudio;
import com.google.cloud.speech.v1.RecognitionConfig;
import com.google.cloud.speech.v1.RecognitionConfig.AudioEncoding;
import com.google.cloud.speech.v1.RecognizeResponse;
import com.google.cloud.speech.v1.SpeechClient;
import com.google.cloud.speech.v1.SpeechRecognitionAlternative;
import com.google.cloud.speech.v1.SpeechRecognitionResult;
import com.google.protobuf.ByteString;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class WavToText {
	VoiceCheckController vcc = new VoiceCheckController();
	private String id = "";
	private SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private Date time = new Date();
	private int log_num = SQLHandler.getlognum();
	private int count = 0;
	private int scount = 0;

	public void setid(String ID) {
		id = ID;
	}

	public void syncRecognizeFile(String fileName) throws Exception {
		try (SpeechClient speech = SpeechClient.create()) {
			Path path = Paths.get(fileName);
			System.out.println(fileName);
			byte[] data = Files.readAllBytes(path);
			ByteString audioBytes = ByteString.copyFrom(data);
			String[] bits = fileName.split("-");
			String lastOne = bits[bits.length-1];

			// Configure request with local raw PCM audio
			RecognitionConfig config = RecognitionConfig.newBuilder().setEncoding(AudioEncoding.LINEAR16)
					.setLanguageCode("ko-KR").setSampleRateHertz(16000).build();
			RecognitionAudio audio = RecognitionAudio.newBuilder().setContent(audioBytes).build();

			// Use blocking call to get audio transcript
			RecognizeResponse response = speech.recognize(config, audio);
			List<SpeechRecognitionResult> results = response.getResultsList();

			for (SpeechRecognitionResult result : results) {
				// There can be several alternative transcripts for a given chunk of speech.
				// Just use the
				// first (most likely) one here.
				SpeechRecognitionAlternative alternative = result.getAlternativesList().get(0);
				System.out.printf("Transcription: %s%n", alternative.getTranscript());
				count++;

				try (LanguageServiceClient language = LanguageServiceClient.create()) {

					// The text to analyze
					String text = alternative.getTranscript().toString();
					Document doc = Document.newBuilder().setContent(text).setType(Type.PLAIN_TEXT).build();
					String time1 = format1.format(time);	
					// Detects the sentiment of the text
					Sentiment sentiment = language.analyzeSentiment(doc).getDocumentSentiment();

					System.out.printf("Text: %s%n", text);
					System.out.printf("Sentiment: %s%n", sentiment.getScore());
					if (sentiment.getScore() < -0.5) {
						if (SQLHandler.getlogcount(log_num) > 10000) {
							log_num++;
							SQLHandler.CreateTable("log_" + log_num);
						}
						SQLHandler.insertswearingtext(id, text, time1, lastOne, log_num);
						scount++;
					}

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (count == 0 || scount == 0) {
				File file = new File(fileName);
				System.out.println("swearing text not detected");
				if (file.exists()) {
					if (file.delete()) {
						System.out.println("file delete complete");
					} else {
						System.out.println("file delete failed!");
					}
				} else {
					System.out.println("file not exist");
				}
			}
		}
	}
}
