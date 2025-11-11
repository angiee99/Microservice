package ang.mois.pc.util;

import ang.mois.pc.dto.request.FacultyRequestDto;
import ang.mois.pc.dto.request.PcRequestDto;
import ang.mois.pc.dto.request.PcTypeRequestDto;
import ang.mois.pc.dto.request.RoomRequestDto;
import ang.mois.pc.dto.response.FacultyResponseDto;
import ang.mois.pc.dto.response.PcResponseDto;
import ang.mois.pc.dto.response.PcTypeResponseDto;
import ang.mois.pc.dto.response.RoomResponseDto;
import ang.mois.pc.entity.Faculty;

import java.sql.Time;
import java.time.LocalDateTime;

public class TestDataProvider {
    // Faculty
    public static FacultyRequestDto getFacultyRequestDto() {
        return new FacultyRequestDto(
                "Faculty of Informatics",
                "FI",
                Time.valueOf("08:00:00"),
                Time.valueOf("20:00:00"),
                5,
                90,
                180
        );
    }

    public static FacultyResponseDto getFacultyResponseDto() {
        return new FacultyResponseDto(
                1L,
                "Faculty of Informatics",
                "FI",
                Time.valueOf("08:00:00"),
                Time.valueOf("20:00:00"),
                5,
                90,
                180,
                LocalDateTime.now()
        );
    }

    // Room
    public static RoomRequestDto getRoomRequestDto() {
        return new RoomRequestDto(
                "Main Lab",
                1L
        );
    }

    public static RoomResponseDto getRoomResponseDto(){
        return new RoomResponseDto(
                1L,
                1L,
                "Some Room",
                LocalDateTime.now()
        );
    }

    // Pc
    public static PcRequestDto getPcRequestDto() {
        return new PcRequestDto(
                "Gaming Pc - Best one",
                Boolean.TRUE,
                1L,
                1L
        );
    }

    public static PcResponseDto getPcResponseDto(){
        return new PcResponseDto(
                1L,
                "Gaming Pc - Best one",
                Boolean.TRUE,
                1L,
                1L,
                LocalDateTime.now()
        );
    }

    // Pc Type
    public static PcTypeRequestDto getPcTypeRequestDto(){
        return new PcTypeRequestDto(
                "Gaming PC",
                "Intel i9",
                "32GB",
                "RTX 4090"
        );
    }

    public static PcTypeResponseDto getPcTypeResponseDto(){
        return new PcTypeResponseDto(
                1L,
                "Gaming PC",
                "Intel i9",
                "32GB",
                "RTX 4090",
                LocalDateTime.now()
        );
    }

    public static Faculty getFaculty(String name, String shortcut){
        return new Faculty(name, shortcut, "dummy@fake.com");
    }
}
