package daring.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;

@Controller
@RequestMapping("flask")
public class FlaskController {

        private void sendToFlask(String textToSend) {
            // 플라스크 서버의 URL
            String flaskUrl = "http://localhost:5000/"; // 실제 Flask 서버의 엔드포인트로 변경하세요.

            // RestTemplate을 사용하여 Flask 서버에 HTTP POST 요청 보내기
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.postForObject(flaskUrl, textToSend, String.class);
        }

        public String resultFromFlask(){//(Model model) throws java.io.IOException{
            // Flask 서버에서 데이터 가져오기
            String flaskUrl = "http://localhost:5000"; // Flask 서버의 주소 및 포트
            RestTemplate restTemplate = new RestTemplate();
            byte[] gifBytes = restTemplate.getForObject(flaskUrl, byte[].class);

            String base64GIF = Base64.getEncoder().encodeToString(gifBytes);

            return base64GIF;
            //model.addAttribute("imgFromFlask", "data:image/gif;base64, " + base64GIF);


            // HTML 템플릿을 렌더링
            //return "form_for_flask";
        }

        /*@GetMapping("/")
        public String mainPage(Model model) throws java.io.IOException{

            // HTML 템플릿을 렌더링
            return "/";
        }*/

        @PostMapping("/")
        public String submitForm(@RequestParam("userInput") String userInput, Model model) {
            // 여기서 userInput을 Flask로 전달하거나 다른 작업을 수행할 수 있습니다.

            // 입력받은 값을 플라스크로 전달
            sendToFlask(userInput);

            // 전달할 데이터를 모델에 추가
            model.addAttribute("userInput", userInput);


            String result = resultFromFlask();
            model.addAttribute("imgFromFlask", "data:image/gif;base64, " + result);

            // HTML 템플릿을 렌더링
            return "index"; // result.html 파일로 이동
        }

}

