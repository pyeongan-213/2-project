package kr.co.duck.controller;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;

@RestController
@RequestMapping("/youtube")
public class YouTubeController {

    // URL을 입력받아 동영상을 MP3로 변환하는 메서드
    @PostMapping("/downloadMp3")
    public ResponseEntity<String> downloadMp3(@RequestParam String videoUrl) {
        String downloadPath = "/path/to/downloaded/file/"; // 파일 저장 경로
        String outputFilePath = downloadPath + "output.mp3"; // 출력 파일 경로

        try {
            // yt-dlp를 사용하여 동영상 다운로드 및 MP3 변환
            ProcessBuilder pb = new ProcessBuilder(
                "yt-dlp", 
                "-x", 
                "--audio-format", 
                "mp3", 
                videoUrl, 
                "-o", downloadPath + "temp.%(ext)s");
            
            Process downloadProcess = pb.start();
            downloadProcess.waitFor(); // 다운로드 완료될 때까지 대기

            // 변환 완료 확인
            File mp3File = new File(downloadPath + "temp.mp3"); // 변환된 MP3 파일
            if (mp3File.exists()) {
                return new ResponseEntity<>("MP3 파일이 성공적으로 저장되었습니다.", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("다운로드 또는 변환에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
            }
            
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return new ResponseEntity<>("에러 발생: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
