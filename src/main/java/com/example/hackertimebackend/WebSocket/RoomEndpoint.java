package com.example.hackertimebackend.WebSocket;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import com.example.hackertimebackend.OTStuff.OT;
import com.example.hackertimebackend.WebSocketData.InterviewRoomSetting;
import com.example.hackertimebackend.WebSocketData.Interviewer;
import com.example.hackertimebackend.WebSocketData.WebSocketGlobalData;
import com.example.hackertimebackend.compiler.compileFile;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RoomEndpoint {

    public String CodeGenerator(int size) {
        int leftLimit = 65; // letter 'A'
        int rightLimit = 122; // letter 'z'
        Random random = new Random();
        StringBuilder buffer = new StringBuilder(size);
        for (int i = 0; i < size; i++) {
            int randomInt = leftLimit + (int) (random.nextFloat() * (rightLimit - leftLimit + 1));
            if (randomInt >= 91 && randomInt <= 96) {
                i--;
                continue;
            }
            buffer.append((char) randomInt);
        }
        return buffer.toString();
    }
    
    @PostMapping("/hostroom")
    public String create_room(@RequestBody Interviewer empName) {
        String newCode = CodeGenerator(30);
        InterviewRoomSetting newRoom = new InterviewRoomSetting();
        newRoom.RoomCode = newCode;
        WebSocketGlobalData.Room_mapper.put(newCode, WebSocketGlobalData.AllRooms.size());
        WebSocketGlobalData.AllRooms.add(newRoom);
        return newCode;
    }

    @CrossOrigin
    @PostMapping("/getCode")
    public Map<String, String> create_room(@RequestBody CodeStruct code) throws IOException {
        System.out.println(code.lang);
        System.out.println(code.code);
        compileFile compiler = new compileFile();
        String name = compiler.createTempFile(code.code, code.lang);
        String bash_name = compiler.generate_bash_script(name);
        String[] result = compiler.runBash(bash_name);
        Map<String, String> return_val = new HashMap<>();
        return_val.put("stdout", result[0]);
        return_val.put("stderr", result[1]);
        return return_val;

    }
}
