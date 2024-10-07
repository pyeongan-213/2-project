package kr.co.duck.controller;

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletContext; // ServletContext 사용

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import kr.co.duck.beans.MusicBean;
import kr.co.duck.service.MusicService;

@RestController
@RequestMapping("/youtube")
public class YouTubeController {

    @Autowired
    private MusicService musicService;  // MusicService를 통해 DB에 저장
    
    @Autowired
    private ServletContext servletContext;  // ServletContext 주입

    // URL을 입력받아 동영상을 MP3로 변환하는 메서드
    @PostMapping("/downloadMp3")
    public ResponseEntity<String> downloadMp3(
            @RequestParam String videoUrl,
            @RequestParam String musicName,
            @RequestParam String artist,
            @RequestParam String thumbnailUrl) {

        String absolutePath = "C:/JAVA/test1/Project_2/src/main/webContent/resources/audio/";
        System.out.println("Absolute Path: " + absolutePath);
        String outputFilePath = absolutePath + "output.mp3"; // 출력 파일 경로

        try {
            // yt-dlp를 사용하여 동영상 다운로드 및 MP3 변환
            ProcessBuilder pb = new ProcessBuilder(
                "yt-dlp", 
                "-x", 
                "--audio-format", 
                "mp3", 
                videoUrl, 
                "-o", absolutePath + "temp.%(ext)s");
            
            Process downloadProcess = pb.start();
            int exitCode = downloadProcess.waitFor(); // 다운로드 완료될 때까지 대기
            System.out.println("yt-dlp 명령어 종료 코드: " + exitCode); // 0이어야 성공

            // 변환 완료 확인
            File mp3File = new File(absolutePath + "temp.mp3"); // 변환된 MP3 파일
            if (mp3File.exists()) {
                // 성공 시 DB에 정보 저장
                MusicBean musicBean = new MusicBean();
                musicBean.setMusicName(musicName);  // 폼에서 받은 값 설정
                musicBean.setArtist(artist);        // 폼에서 받은 값 설정
                musicBean.setVideoUrl(videoUrl);    // 폼에서 받은 값 설정
                musicBean.setMusicLength("00:00");  // 실제 길이로 수정 필요
                musicBean.setThumbnailUrl(thumbnailUrl);  // 폼에서 받은 값 설정

                musicService.saveMusic(musicBean);  // DB에 곡 정보 저장
                System.out.println("MP3 파일이 성공적으로 생성되었습니다.");
                return new ResponseEntity<>("MP3 파일이 성공적으로 저장되었고, DB에 정보가 추가되었습니다.", HttpStatus.OK);
            } else {
                System.out.println("MP3 파일 생성에 실패했습니다.");
                return new ResponseEntity<>("다운로드 또는 변환에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
            }
            
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return new ResponseEntity<>("에러 발생: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
