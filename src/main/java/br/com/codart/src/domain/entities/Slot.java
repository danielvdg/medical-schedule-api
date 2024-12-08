package br.com.codart.src.domain.entities;

import br.com.codart.src.domain.Enum.SlotStatusEnum;

import java.time.LocalDateTime;

public class Slot {

    private Long slotId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private SlotStatusEnum slotStatus;

    public Slot() {};

    public Slot(Long slotId, LocalDateTime startTime, LocalDateTime endTime, SlotStatusEnum slotStatus) {
        this.slotId = slotId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.slotStatus = slotStatus;
    }

    public Long getSlotId() {
        return slotId;
    }

    public void setSlotId(Long slotId) {
        this.slotId = slotId;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public SlotStatusEnum getSlotStatus() {
        return slotStatus;
    }

    public void setSlotStatus(SlotStatusEnum slotStatus) {
        this.slotStatus = slotStatus;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }
}
