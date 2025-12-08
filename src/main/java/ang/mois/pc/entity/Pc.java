package ang.mois.pc.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

/**
 * PC DB entity
 */
@Entity
@Table(name = "pcs")
public class Pc {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private boolean available;

    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

    @ManyToOne
    @JoinColumn(name = "pc_type_id", nullable = false)
    private PcType pcType;

    public Pc(String name, boolean available, Room room, PcType pcType) {
        this.name = name;
        this.available = available;
        this.room = room;
        this.pcType = pcType;
        this.createdAt = LocalDateTime.now();
    }

    public Pc() {
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

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public PcType getPcType() {
        return pcType;
    }

    public void setPcType(PcType pcType) {
        this.pcType = pcType;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
