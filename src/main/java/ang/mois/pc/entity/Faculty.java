package ang.mois.pc.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.sql.Time;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "faculties")
public class Faculty {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String shortcut;
    private String email;
    private Time reservationTimeStart;
    private Time reservationTimeEnd;
    private Integer maxUserReservationCount; // of all time
    private Integer maxUserReservationTime; // for 1 reservation in minutes
    private Integer maxUserReservationTimeWeekly; // weekly in minutes
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "faculty")
    @JsonIgnore
    private List<Room> rooms;

    public Faculty(String name, String shortcut, String email) {
        this.name = name;
        this.shortcut = shortcut;
        this.email = email;
        // default params
        this.createdAt = LocalDateTime.now();
        this.reservationTimeStart = Time.valueOf("08:00:00");
        this.reservationTimeEnd = Time.valueOf("20:00:00");
        this.maxUserReservationCount = 9999;
        this.maxUserReservationTime = 180;
        this.maxUserReservationTimeWeekly = 900;
    }

    public Faculty(String name, String shortcut, String email, Time reservationTimeStart, Time reservationTimeEnd, Integer maxUserReservationCount, Integer maxUserReservationTime, Integer maxUserReservationTimeWeekly, LocalDateTime createdAt) {
        this.name = name;
        this.shortcut = shortcut;
        this.email = email;
        this.reservationTimeStart = reservationTimeStart;
        this.reservationTimeEnd = reservationTimeEnd;
        this.maxUserReservationCount = maxUserReservationCount;
        this.maxUserReservationTime = maxUserReservationTime;
        this.maxUserReservationTimeWeekly = maxUserReservationTimeWeekly;
        this.createdAt = createdAt;
    }

    public Faculty() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShortcut() {
        return shortcut;
    }

    public void setShortcut(String shortcut) {
        this.shortcut = shortcut;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Time getReservationTimeStart() {
        return reservationTimeStart;
    }

    public void setReservationTimeStart(Time reservationTimeStart) {
        this.reservationTimeStart = reservationTimeStart;
    }

    public Time getReservationTimeEnd() {
        return reservationTimeEnd;
    }

    public void setReservationTimeEnd(Time reservationTimeEnd) {
        this.reservationTimeEnd = reservationTimeEnd;
    }

    public Integer getMaxUserReservationCount() {
        return maxUserReservationCount;
    }

    public void setMaxUserReservationCount(Integer maxUserReservationCount) {
        this.maxUserReservationCount = maxUserReservationCount;
    }

    public Integer getMaxUserReservationTime() {
        return maxUserReservationTime;
    }

    public void setMaxUserReservationTime(Integer maxUserReservationTime) {
        this.maxUserReservationTime = maxUserReservationTime;
    }

    public Integer getMaxUserReservationTimeWeekly() {
        return maxUserReservationTimeWeekly;
    }

    public void setMaxUserReservationTimeWeekly(Integer maxUserReservationTimeWeekly) {
        this.maxUserReservationTimeWeekly = maxUserReservationTimeWeekly;
    }

    public List<Room> getRooms() {
        return rooms;
    }

    public void setRooms(List<Room> rooms) {
        this.rooms = rooms;
    }
}
